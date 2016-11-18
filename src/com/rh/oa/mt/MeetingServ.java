package com.rh.oa.mt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.comm.msg.MsgSender.MsgItem;
import com.rh.core.comm.todo.TodoBean;
import com.rh.core.comm.todo.TodoUtils;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.ServUtils;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.Lang;
import com.rh.core.util.Strings;
import com.rh.core.util.lang.Assert;

/**
 * 会议室通知审批扩展服务.
 * 
 * @author wangchen
 */

public class MeetingServ extends CommonServ {

    /** 会议室预定状态-预定. */
    private static final int BOOKING_FLAG_BOOKING = 1;
    /** 会议室预定状态-已预定. */
    // private static final int BOOKING_FLAG_BOOKIED = 2;
    /** 会议室预定状态-不预定. */
    private static final int BOOKING_FLAG_NOBOOK = 3;
    /** 会议通知-审批中. */
    private static final int STATUS_CHECKING = 1;
    /** 会议通知-已取消. */
    //private static final int STATUS_CANCEL = 11;
    
    /**会议通知服务ID*/
    private final String mtMtConferee = "OA_MT_CONFEREE";
    
    /**会议通知申请通过*/
    private static final String STATUS_PASS = "2";
    /**会议通知申请取消*/
    private static final String STATUS_CANCEL = "11";
    /**会议通知已发送*/
    private static final String NOTICE_FLAG = "2";
    /**部门服务*/
    private static final String SY_ORG_DEPT = "SY_ORG_DEPT";
    /**会议室字典*/
    private static final String OA_MT_MEETINGROOM_DIC = "OA_MT_MEETINGROOM_DIC";
    /**我的与会人员服务id*/
    private final String confereeServId = "OA_MT_CONFEREE";
    /**会议通知待办服务*/
    private static final String OA_MT_MEETING = "OA_MT_MEETING_RETURN_NOTICE";
    /**会议通知部门领导角色配置*/
    private static final String OA_MT_MEETING_ROLE_CODE = "OA_MT_MEETING_ROLE_CODE";
    /**会议室维护服务id*/
    private static final String OA_MT_MEETINGROOM = "OA_MT_MEETINGROOM";

    /**
     * 保存操作前判断是否需要预定会议室
     * 
     * @param paramBean
     *            1调用会议室预定服务添加或修改会议室预定记录，会议室被占用则抛错误提醒并回滚. 2继续调用本服务添加或修改记录.
     *            3继续调用本服务添加或修改记录.
     */
    @Override
    protected void beforeSave(ParamBean paramBean) {
        Bean fullParamBean = paramBean.getSaveFullData();
        int bookingFlag = fullParamBean.getInt("BOOKING_FLAG"); // 会议室预定标志RR
        int status = fullParamBean.getInt("STATUS"); // 状态位
        // 会议室还未预定且还未提请送交审批
        if (bookingFlag == BOOKING_FLAG_BOOKING && status <= STATUS_CHECKING) {
            ParamBean bookingParamBean = new ParamBean("OA_MT_BOOKING_NOWFE", ServMgr.ACT_SAVE);
            if (fullParamBean.isNotEmpty("BOOKING_ID")) {
                // 修改记录
                Bean oldBean = paramBean.getSaveOldData();
                // 判断是否修改了会议室ID，开始结束时间
                if (!isModified(fullParamBean, oldBean, "MR_ID")
                        && !isModified(fullParamBean, oldBean, "BEGIN_TIME")
                        && !isModified(fullParamBean, oldBean, "END_TIME")) {
                    return;
                }
                bookingParamBean.set(Constant.KEY_ID, fullParamBean
                        .get("BOOKING_ID"));
            } else {
                bookingParamBean.set("STATUS", 1);
            }

            int confereesNum = fullParamBean.getStr("CONFEREES_CODES").split(
                    Constant.SEPARATOR).length; // 与会人数
            int notifiedNum = fullParamBean.getStr("NOTIFIED_CODES").split(
                    Constant.SEPARATOR).length; // 被通知人数

            bookingParamBean.set("MR_ID", fullParamBean.get("MR_ID"));
            bookingParamBean.set("START_TIME", fullParamBean.get("BEGIN_TIME")
                    + ":00");
            bookingParamBean.set("END_TIME", fullParamBean.get("END_TIME")
                    + ":00");
            bookingParamBean.set("TITLE", fullParamBean.get("TITLE"));
            bookingParamBean.set("MEMO", fullParamBean.get("MEMO"));
            bookingParamBean.set("ATTENDANCE", confereesNum + notifiedNum);
            Bean resultBean = new Bean();

            resultBean = ServMgr.act(bookingParamBean);

            if (resultBean.isEmpty("BOOKING_ID")) { // 预定失败则抛错回滚
                String msg = Context.getSyMsg("OA_MT_BOOKING_DATE_ERROR");
                if (resultBean.getStr("_MSG_").indexOf("warn") >= 0) {
                    msg = resultBean.getStr("_MSG_").substring(5,
                            msg.length() - 1);
                }
                throw new TipException(msg);
            }
            paramBean.set("BOOKING_ID", resultBean.getStr("BOOKING_ID")); // 预订成功后回写预定ID号
        }
    }
    
    @Override
    protected void afterQuery(ParamBean paramBean, OutBean outBean) {
        Bean bean = null; //查询结果
        List<Bean> outList = outBean.getDataList();
        formatAfterQueryList(outList, new ParamBean(), bean);
    }

    /**
     * 取消会议通知 包含服务名和取消会议原因 顺序执行以下操作： 0、判断是否满足取消条件 1、删除与会人员表中关联的与会记录.
     * 2、改变已预订会议室状态位，调会议预定服务（不绑定流程的）取消方法 3、改变本服务对应记录的状态位及添加取消原因. 4、发布取消会议通知的待办.
     * 
     * @param paramBean
     *            参数bean
     * @return paramBean
     * */
    public OutBean cancelMTBooking(ParamBean paramBean) {
        //取消会议室占用
        modifyBookedPlace(paramBean.getStr("BOOKING_ID"));
        //修改次会议通知[会议室预定状态为不预定]
        modifyMeetingRecord(paramBean);
        // 发布取消会议通知的消息
        return sendMeetingRemindMsg(paramBean);
    }

    @Override
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        //saveConfereeAndReturn(paramBean, outBean);
    }
    
    
    /**
     * 发送会议通知消息 
     * @param paramBean 参数
     * @return 结果集
     */
    public OutBean sendMeetingNoticeMsg(ParamBean paramBean) {
        TodoBean todo = new TodoBean();
        //设置提醒标题
        todo.setTitle(paramBean.getStr("TODO_TITLE"));
        todo.setSender(Context.getUserBean().getCode()); //发送用户
        //添加服务id
        todo.setServId(OA_MT_MEETING);
        //添加数据code
        todo.setCode(OA_MT_MEETING);
        //获取服务名
        ServDefBean servDef = ServUtils.getServDef(OA_MT_MEETING);
        //设置数据名
        todo.setCodeName(servDef.getName());
        //获取数据主键
        todo.setObjectId1(paramBean.getStr("MEETING_ID"));
        todo.setObjectId2(paramBean.getStr("MEETING_ID"));
        todo.setBenchFlag(1); //允许委托
        todo.setEmergency(Integer.parseInt(paramBean.getStr("S_EMERGENCY"))); //缓急
        String urlStr = OA_MT_MEETING + ".byid.do?data={_PK_:" + paramBean.getStr("MEETING_ID") + "}";
        todo.setUrl(urlStr);
        Map<String, String> userCodesMap = new HashMap<String, String>();
        Bean deptLeven = getDeptLevelCode(paramBean.getStr("COMPLEADER"));
        String confereesCodes = "";
        if (null == deptLeven) {
            confereesCodes = getSendUserCodes(paramBean.getStr("CONFEREES"), "");
        } else {
            confereesCodes = getSendUserCodes(paramBean.getStr("CONFEREES"), deptLeven.getStr("CODE")); 
        }
        userCodesMap.put("CONFEREES", confereesCodes);
        userCodesMap.put("NOTIFIED", filterDuplicationUsers(confereesCodes, 
                         paramBean.getStr("NOTIFIED")));
        if (settingReceivers(userCodesMap, todo, paramBean)) {
            return new OutBean().setOk();
        } else {
            return new OutBean().setError("通知发送失败！");
        }
    }
    
    
    /**
     * 发送会议通知提醒
     * @param paramBean 参数
     * @return 返回结果集
     */
    public OutBean sendMeetingRemindMsg(ParamBean paramBean) {
        TodoBean msg = new TodoBean();
        msg.setTitle(paramBean.getStr("TODO_TITLE")); //发送标题
        msg.setSender(Context.getUserBean().getCode()); //发送用户
        msg.setCode("TODO_REMIND");
        // 取得服务ID
        String servId = paramBean.getServId();
        msg.setServId(servId);
        // 取得服务名称
        msg.setCodeName(ServUtils.getServDef(OA_MT_MEETING).getName());
        msg.setUrl(servId + ".showDialog.do");
        msg.setObjectId1(paramBean.getStr("MEETING_ID"));
        msg.setObjectId2(paramBean.getStr("MEETING_ID"));
        if (paramBean.isNotEmpty("TODO_CONTENT")) {
            msg.setContent(paramBean.getStr("TODO_CONTENT"));
        }
        msg.setCatalog(TodoUtils.TODO_CATLOG_MSG);
        
        List<Bean> receivers = getConfereesUsers(paramBean.getStr("MEETING_ID"));
        Assert.notNull(receivers, "接收人列表" + MsgItem.RECEIVER_LIST + "的值不能为NULL");

        int successCount = 0; //发送成功条数
        int receiversCount = receivers.size(); //要发送的用户个数
        /**
         * 设置接收人
         */
        for (Bean user : receivers) {
            TodoBean newToDoBean = new TodoBean(msg.copyOf());
            newToDoBean.setOwner(user.getStr("USER_CODE"));
            try {
                // 加入标识，通知此待办是由提醒触发的，待办发完后不再发提醒，防止嵌套循环
                //newToDoBean.set("remindFlag", true);
                TodoUtils.insert(newToDoBean);
                successCount += 1; //发送成功条数记录
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return new OutBean().setError();
            }
        }
        if (successCount == receiversCount) { //发送条数和成功条数一致
            return new OutBean().setOk();
        }
        return new OutBean().setError("所选通知用户没有全部发送成功！");
    }
   
    /**
     * 查看当前选中值中有没有部门code
     * @param paramBean 参数
     * @return 返回结果
     */
    public OutBean isExiteDeptCodes(ParamBean paramBean) {
        SqlBean sql = null;
        String oldCodes = paramBean.getStr("CODES");
        String[] oldCodesArray = oldCodes.split(",");
        StringBuffer deptCode = new StringBuffer(); //部门编码
        StringBuffer deptName = new StringBuffer(); // 部门名称
        for (int i = 0; i < oldCodesArray.length; i++) {
            sql = new SqlBean();
            sql.selects("DEPT_CODE, DEPT_NAME").and("DEPT_CODE", oldCodesArray[i]);
            Bean dept = ServDao.find(SY_ORG_DEPT, sql);
            if (null != dept) {
                deptCode.append(",").append(dept.getId());
                deptName.append(",").append(dept.getStr("DEPT_NAME"));
            }
        }
        OutBean out = new OutBean();
        if (deptCode.toString().length() > 0) {
            out.set("CODE", getOutCodes(oldCodes, deptCode.toString()));
            out.set("NAME", deptName.toString().substring(1));
            out.set("VALUE", getOutCodes(paramBean.getStr("VALUE"), deptName.toString()));
            out.set("OK", "false");
            return out;
        }
        return new OutBean().set("OK", "true");
    }
    
    /**
     * 取消当前会议通知
     * @param paramBean 参数
     * @return 返回数据信息
     */
    public OutBean cancelNotice(ParamBean paramBean) {
        int successCount = TodoUtils.endAllUserTodo(paramBean.getStr("MEETING_ID"), paramBean.getStr("MEETING_ID"));
        if (successCount >= 0) {
            if (sendMeetingRemindMsg(paramBean).isOk() && settingBookingNoticeFlag(paramBean.getStr("BOOKING_ID"))) {
                Bean meeting = new Bean();
                meeting.setId(paramBean.getStr("MEETING_ID"));
                meeting.set("STATUS", 11); //将状态置成[取消]
                Bean out = ServDao.update("OA_MT_MEETING", meeting);
                if (out.isEmpty()) {
                    return new OutBean().setError("会议通知取消失败");
                }
                //查看是否有日程处理
                String meetingReceiverConfig = Context.getSyConf("OA_MT_MEETING_CALENDAR", "");
                if (!Strings.isEmpty(meetingReceiverConfig)) {
                    //删除日程
                    MeetingCalendar mn = Lang.createObject(MeetingCalendar.class, meetingReceiverConfig);
                    OutBean outCalendar = mn.delete(ServDao.find(OA_MT_MEETING, paramBean.getStr("MEETING_ID")));
                    if (outCalendar.isOk()) {
                        return new OutBean().setOk();
                    }
                }
                //门户中否有日程处理
                String ptMTReceiverConfig = Context.getSyConf("PT_MT_MEETING_CALENDAR", "");
                if (!Strings.isEmpty(ptMTReceiverConfig)) {
                    //删除日程
                    MeetingCalendar mn = Lang.createObject(MeetingCalendar.class, ptMTReceiverConfig);
                    OutBean outCalendar = mn.delete(ServDao.find(OA_MT_MEETING, paramBean.getStr("MEETING_ID")));
                    if (outCalendar.isOk()) {
                        return new OutBean().setOk();
                    }
                }
            }
        }
        return new OutBean().setError("会议通知取消失败");
    }
    
    /**
     * 获取所有用户code，包括部门领导
     * @param paramBean 参数集合
     * @return 用户code
     */
    public OutBean getAllUserCode(ParamBean paramBean) {
        Bean compLeaderBean = null;
        if (!paramBean.getStr("COMPLEADER").isEmpty()) {
            compLeaderBean = getDeptLevelCode(paramBean.getStr("COMPLEADER"));
        }
        OutBean out = isExiteDeptCodes(paramBean);
        return  null == compLeaderBean ? out : out.set("CODE", out.getStr("CODE") + "," + compLeaderBean.getStr("CODE"))
                .set("VALUE", out.getStr("VALUE") + "," + compLeaderBean.getStr("NAME"));
    }
    
    /**
     * 重置会议室预定中，通知发送状态
     * @param bookingId 预定id
     * @return 修改是否成功
     */
    private boolean settingBookingNoticeFlag(String bookingId) {
        if (bookingId.equals("")) { //如果会议没有预定会议室，则直接返回
            return true;
        }
        Bean booking = new Bean();
        booking.setId(bookingId);
        booking.set("NOTICE_FLAG", 2);
        return !(null == ServDao.update("OA_MT_BOOKING_NOWFE", booking)
                || ServDao.update("OA_MT_BOOKING_NOWFE", booking).isEmpty());
    }
    
    /**
     * 获取当前会议与会人员列表，用户code
     * @param meetingId 会议id
     * @return 用户id结合
     */
    private List<Bean> getConfereesUsers(String meetingId) {
        SqlBean sql = new SqlBean();
        sql.selects("USER_CODE").and("MEETING_ID", meetingId).and("S_FLAG", 1).andNotNull("SET_TIME_OK");
        return ServDao.finds("OA_MT_CONFEREE", sql);
    }
    
    /**
     * 获取最后的输出codes值
     * @param oldCodes 原始编码字符转
     * @param deptCodes 包含的部门字符编码
     * @return 最终codes值
     */
    private String getOutCodes(String oldCodes, String deptCodes) {
        String[] codeArray = oldCodes.split(",");
        String[] deptArray = deptCodes.split(",");
        StringBuffer outCodes = new StringBuffer();
        for (int i = 0; i < codeArray.length; i++) {
            boolean isExite = false;
            for (int j = 0; j < deptArray.length; j++) {
                if (codeArray[i].equals(deptArray[j])) {
                    isExite = true;
                }
            }
            if (!isExite) {
                outCodes.append(",").append(codeArray[i]);
            }
        }
       return outCodes.toString().length() > 0 ? outCodes.toString().substring(1) : "";
    }
    
    /**
     * 获取所有的发送用户code
     * @param confereesCodes 与会人员code
     * @param deptLevelCode 部门领导code
     * @return 合并的字符串
     */
    private String getSendUserCodes(String confereesCodes, String deptLevelCode) {
        if (null == deptLevelCode) {
            deptLevelCode = "";
        }
        String[] codesArray = (confereesCodes + "," + deptLevelCode).split(",");
        StringBuffer codes = new StringBuffer();
        Set<String> filterCodes = new HashSet<String>();
        for (int i = 0; i < codesArray.length; i++) {
            if (!codesArray[i].trim().equals("")) {
                filterCodes.add(codesArray[i]);
            }
        }
        for (String s : filterCodes) {
            codes.append(",").append(s);
        }
        return codes.toString().length() > 0 ? codes.toString().substring(1) : "";
    }
    
    /**
     * 根据部门获取部门领导
     * @param deptCodes 部门code
     * @return 领导字符串
     */
    private Bean getDeptLevelCode(String deptCodes) {
        if (!deptCodes.trim().equals("")) {
            StringBuffer levelStr = new StringBuffer();
            StringBuffer levelName = new StringBuffer();
            List<Bean> userList = UserMgr.getUserListByDept(deptCodes, Context.getSyConf(OA_MT_MEETING_ROLE_CODE, ""));
            for (Bean b : userList) {
                levelStr.append(",").append(b.getId());
                levelName.append(",").append(b.getStr("USER_NAME"));
            }
            String codes = levelStr.toString().length() > 0 ? levelStr.toString().substring(1) : "";
            String names = levelName.toString().length() > 0 ? levelName.toString().substring(1) : "";
            return new Bean().set("CODE", codes).set("NAME", names);
        }
        return null;
    }
    
    /**
     * 去除与会人员与被通知人员中的重复值，以与会人员为主
     * @param confereesUsers 与会人员
     * @param noticeUsers 被通知人
     * @return 结果集
     */
    private String filterDuplicationUsers(String confereesUsers, String noticeUsers) {
        //与会人员数组
        String[] confereesArray = confereesUsers.split(",");
        //被通知人数组
        String[] noticeArray = noticeUsers.split(",");
        //新的被通知人字符转
        StringBuffer newNoticeString = new StringBuffer();
        for (int i = 0; i < noticeArray.length; i++) {
            boolean isDuplication = false;
            for (int j = 0; j < confereesArray.length; j++) {
              //当前不是重复值，则加入新的字符转中
              if (noticeArray[i].equals(confereesArray[j])) {
                  isDuplication = true;
              }
            }
            if (!isDuplication) {
                newNoticeString.append(",").append(noticeArray[i]);
            }
        }
        return newNoticeString.toString().length() > 0 ? newNoticeString.toString().substring(1) : "";
    } 
    
    /**
     * 设置接收人
     * @param userAndType 接受人code 接接收人类型（与会人员/被通知人员）
     * @param todoBean 消息对象
     * @param paramBean 参数集合
     * @return 结果集
     */
    private boolean settingReceivers(Map<String, String> userAndType, TodoBean todoBean, ParamBean paramBean) {
        List<UserBean> receivers1 = getReceiversList(userAndType.get("CONFEREES"));
        Assert.notNull(receivers1, "接收人列表" + MsgItem.RECEIVER_LIST + "的值不能为NULL");
        int successFlag = 0;
        if (sendNotice2User(receivers1, todoBean, paramBean, "CONFEREES")) {
            successFlag += 1;
        }
        List<UserBean> receivers2 = getReceiversList(userAndType.get("NOTIFIED"));
        Assert.notNull(receivers2, "接收人列表" + MsgItem.RECEIVER_LIST + "的值不能为NULL");
        if (sendNotice2User(receivers2, todoBean, paramBean, "NOTIFIED")) {
            successFlag += 1;
        }
        if (successFlag == 2) { //两种类型都发送成功
            if (updateMeetingNoticeType(paramBean)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 更新会议的通知发送状态
     * @param paramBean 参数
     * @return 返回结果集
     */
    private boolean updateMeetingNoticeType(ParamBean paramBean) {
        Bean udateMeeting = new Bean();
        udateMeeting.setId(paramBean.getStr("MEETING_ID"));
        udateMeeting.set("NOTICE_FLAG", 1);
        Bean out = ServDao.update("OA_MT_MEETING", udateMeeting);
        return out.isEmpty() ? false : true;
    }
    
    /**
     * 给接收人发送消息
     * @param receivers 接收人列表
     * @param todoBean 消息对象
     * @param paramBean 参数集合
     * @param userType 用户类型（与会人员/被通知人）
     * @return 结果集
     */
    private boolean sendNotice2User(List<UserBean> receivers, TodoBean todoBean, ParamBean paramBean, String userType) {
        int successCount = 0; //发送成功条数
        int receiversCount = receivers.size(); //要发送的用户个数
      //设置接收人
        for (UserBean rUserBean : receivers) {
            TodoBean newToDoBean = new TodoBean(todoBean.copyOf());
            newToDoBean.setOwner(rUserBean.getCode());
            try {
                // 加入标识，通知此待办是由提醒触发的，待办发完后不再发提醒，防止嵌套循环
                //newToDoBean.set("remindFlag", true);
                TodoUtils.insert(newToDoBean);
                //添加我的与会人员数据对象
                Bean confereeBean = new Bean();
                confereeBean.set("MEETING_ID", paramBean.getStr("MEETING_ID")).set("USER_CODE", rUserBean.getCode());
                confereeBean.set("USER_TYPE", userType);
                if (addMtConferee(confereeBean)) {
                    successCount += 1; //发送成功条数记录
                } else {
                    return false;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return false;
            }
        }
        if (successCount == receiversCount) { //发送条数和成功条数一致
            return true;
        }
        return false;
    }
    
    /**
     * 添加我的与会人员记录
     * @param conferee 当前对象
     * @return 结果集
     */
    private boolean addMtConferee(Bean conferee) {
        Bean out = ServDao.save(confereeServId, conferee);
        if (!out.isEmpty()) {
            return true;
        }
        return false;
    }
    
    /**
     * 格式化sql语句
     * @param ids 传过来的id
     * @return 格式化之后的SqlBean
     */
    private List<UserBean> getReceiversList(String ids) {
        String[] idsArray = ids.split(",");
        List<UserBean> result = new ArrayList<UserBean>();
        for (String userId :idsArray) {
            if (userId.length() <= 0) {
                continue;
            }
            UserBean ub = UserMgr.getUser(userId);
            result.add(ub);
        }
        return result;
    }
    
    /**
     * 删除与会人员表中关联的与会记录
     * 
     * @param dataBean
     *            根据前台主键取出的后台数据
     * @return List<Bean> 与会人员列表
     * */
    /*private List<Bean> deleteConfereesRecord(Bean dataBean) {
        // 查询出本会议关联的与会人员数组记录
        ParamBean confereesQueryBean = new ParamBean("OA_MT_CONFEREE", ServMgr.ACT_FINDS);
        confereesQueryBean.set(Constant.PARAM_SELECT, "USER_CODE");
        confereesQueryBean.set("MEETING_ID", dataBean.getId());
        List<Bean> confereesList = ServMgr.act(confereesQueryBean).getDataList();
        if (confereesList.size() > 0) {
            // 删除与会人员表中关联的与会记录（批量）（目前启用了假删除）
            Bean confereesDelBean = new Bean();
            confereesDelBean.set("MEETING_ID", dataBean.getId());
            ServDao.deletes("OA_MT_CONFEREE", confereesDelBean);
        }
        return confereesList;
    }*/

    /**
     * 格式化查询数据
     * @author hdy
     * @param outBeanList 查询记录集合
     * @param query 查询条件
     * @param bean 单条记录对象
     */
    private void formatAfterQueryList(List<Bean> outBeanList, ParamBean query, Bean bean) {
        for (Bean b : outBeanList) {
            b.set("SHOW_FLAG", showListFlag(b.getStr("STATUS"), b.getStr("NOTICE_FLAG")));
            //添加[通知签收]自定义字段
            query.setWhere(" and MEETING_ID = '" + b.getId() + "' and S_FLAG = '1'").setSelect("CONFEREE_ID");
            bean = ServDao.find(mtMtConferee, query);
            if (null == bean || bean.isEmpty()) {
                b.set("CONFEREE_ID", "无");
            } else {
                b.set("CONFEREE_ID", bean.getId());
            }
        }
    }
    
    @Override
    public void afterByid(ParamBean paramBean, OutBean outBean) {
        if (!paramBean.getAddFlag()) {
            //getConfereeUser(outBean);
        }
    }
    
    /**
     * 获取会议室名称(跨机构的时候)
     * @param paramBean 参数集合
     * @return 结果集
     */
    public OutBean getMtRoomName(ParamBean paramBean) {
        Bean mtRoom = DictMgr.getItem(OA_MT_MEETINGROOM_DIC, paramBean.getStr("MR_ID"));
        if (null == mtRoom) {
            SqlBean sql = new SqlBean();
            sql.selects("NAME, S_ODEPT").and("MR_ID", paramBean.getStr("MR_ID")).and("S_FLAG", 1);
            Bean out = ServDao.find(OA_MT_MEETINGROOM, sql);
            if (null != out) {
                String fullName = DictMgr.getFullName(SY_ORG_DEPT, out.getStr("S_ODEPT"));
                if (fullName.contains("/")) {
                    fullName = fullName.substring(fullName.lastIndexOf("/") + 1);
                }
                return new OutBean().setOk().set("NAME", "[" + fullName + "]" + out.getStr("NAME"));
            }
        }
        return new OutBean().setError();
    }
    
    /**
     * 格式化被通知人codes和names 
     * @param outBean 打开卡片页面数据
     */
    /*private void getConfereeUser(OutBean outBean) {
        ParamBean query = new ParamBean();
        //查出与会人员中被通知人的code
        query.setWhere(" and MEETING_ID = '" + outBean.getId() + "' and S_FLAG = '" + sFlag + "'")
             .setSelect("USER_CODE");
        List<Bean> confereeList = ServDao.finds(mtMtConferee, query);
        String userNames = ""; //用户名字
        String userCodes = ""; //用户codes
        //格式化用户code值
        userCodes = formatStr(confereeList, "USER_CODE");
        //获取names值
        userNames = DictMgr.getFullNames(syOrgUserDict, userCodes);
        if (userCodes.length() > 0) {
            //重置 被通知人codes和names
            outBean.set("NOTIFIED_CODES", userCodes).set("NOTIFIED_CODES__NAME", userNames);
        }
    }*/
    
    /**
     * 格式化用户codes字符串
     * @param list 要被格式化的list
     * @param userCode 用户code值
     * @return 格式化之后的字符串，用逗号隔开
     */
   /* private String formatStr(List<Bean> list, String userCode) {
        String userCodes = "";
        //格式化用户code值
        for (Bean b : list) {
            userCodes = userCodes + b.getStr(userCode) + ",";
        }
        //去除，codes值最后面的逗号
        if (userCodes.lastIndexOf(",") == userCodes.length()) {
            userCodes = userCodes.substring(0, userCodes.length() - 1);
        }
        return userCodes;
    }*/
    
    /**
     * 改变已预订会议室状态位，调会议预定服务（不绑定流程的）取消方法
     * 
     * @param bookingId
     *            根据前台主键取出的后台数据
     * */
    private void modifyBookedPlace(String bookingId) {
        ParamBean meetingroomUpdateBean = new ParamBean("OA_MT_BOOKING_NOWFE", "cancelBooking", bookingId);
        ServMgr.act(meetingroomUpdateBean);
    }

    /**
     * 改变本服务对应记录的状态位及添加取消原因
     * 
     * @param paramBean
     *            根据前台主键取出的后台数据
     * */
    private void modifyMeetingRecord(ParamBean paramBean) {
        Bean meetingParamBean = new Bean();
        meetingParamBean.setId(paramBean.getStr("MEETING_ID")); // 主键
        meetingParamBean.set("BOOKING_FLAG", BOOKING_FLAG_NOBOOK); //修改会议室占用状态为不预定
        meetingParamBean.set("CANCEL_REASON", paramBean.getStr("TODO_CONTENT")); // "取消原因"字段
        ServDao.update("OA_MT_MEETING", meetingParamBean);
    }

    /**
     * 是否为修改
     * 
     * @param newBean
     *            修改后的参数
     * @param oldBean
     *            修改前的参数
     * @param fieldName
     *            判断字段
     * @return 是否修改
     */
    private boolean isModified(Bean newBean, Bean oldBean, String fieldName) {
        String oldVal = oldBean.getStr(fieldName);
        String newVal = newBean.getStr(fieldName);

        if (oldVal == null && newVal == null) {
            return false;
        }
        if (newVal != null && newVal.equals(oldVal)) {
            return false;
        }
        return true;
    }
    
    /**
     * 列表显示当前操作状态
     * @param status 状态位
     * @param noticeFlag 是否发送通知
     * @return 显示文字
     */
    private String showListFlag(String status, String noticeFlag) {
        //审批通过并且已发通知
        if (status.equals(STATUS_PASS)) {
            if (noticeFlag.equals(NOTICE_FLAG)) {
                return "未通知";
            }
            return "已通知";
        //已取消
        } else if (status.equals(STATUS_CANCEL)) {
            return "已取消";
        }
        return "未通过";
    }
}
