package com.rh.core.comm.calendar;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.remind.RemindMgr;
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
import com.rh.core.util.Constant;

/**
 * 日程管理服务类
 * 
 * @author wangchen
 * 
 */
public class CalendarServ extends CommonServ {
    private static final String CALENDAR_MAIN_SERV = "SY_COMM_CAL";
    private static final String CALENDAR_DELE_SERV = "SY_COMM_CAL_USERS";
    private static final String CALENDAR_SHARE_SERV = "SY_COMM_CAL_SHARE";
    private static final String CALENDAR_TYPE_SERV = "SY_COMM_CAL_TYPE";
    private static final String BELONG_SERV = "SY_COMM_BELONG";
    /** 提醒服务ID **/
    private static final String REMIND_SERV_ID = "SY_COMM_REMIND";
    /** 提醒人服务ID **/
    public static final String REMIND_USER_SERV_ID = "SY_COMM_REMIND_USERS";

    /**
     * 日程管理页面
     * 
     * @param paramBean 传入的参数
     * @return 传出的参数
     */
    public OutBean show(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        String initMode = "week";
        if(paramBean.isNotEmpty("initMode")){
            initMode = paramBean.getStr("initMode");
        }
        String initDate = "";
        if(paramBean.isNotEmpty("initDate")){
            initDate = paramBean.getStr("initDate");
        }
        outBean.setToDispatcher("/sy/comm/calendar/dhtmlx/calendar.jsp?initMode=" + initMode + "&initDate=" + initDate);
        return outBean;
    }

    /**
     * 获取用户的所有身份串 处理步骤： 根据用户获得其所有身份actCodes串(usercode rolecodes deptcodes)
     * 
     * @param userCode 传入的参数包括userCode
     * @return actCodes 传出的参数包括actCodes 数据格式：[String]==="'code-1','code-2',...,'code-n'"
     */
    private String getUserActCodes(String userCode) {
        UserBean userInfo = UserMgr.getUser(userCode);
        String actCodes = "";
        StringBuffer ac = new StringBuffer();
        if (userInfo.isEmpty()) {
            return actCodes;
        }
        // 添加用户编码
        String userStr = "'" + userInfo.getCode() + "'";
        ac.append(userStr);
        // 添加角色编码串
        String roleStr = userInfo.getRoleCodeQuotaStr();
        if (!roleStr.isEmpty()) {
            ac.append(Constant.SEPARATOR);
            ac.append(roleStr);
        }
        // 添加部门编码串
        String deptStr = userInfo.getDeptCodeQuotaStr();
        if (!deptStr.isEmpty()) {
            ac.append(Constant.SEPARATOR);
            ac.append(deptStr);
        }
        actCodes = ac.toString();
        return actCodes;
    }

    /**
     * 获取用户的共享用户 处理步骤： 根据actCodes串或userCode从日程共享的子表获取所有共享用户shareUsers
     * 
     * @param paramBean 传入的参数包括 actCodes,userCode
     * @return 传出的参数包括shareUsers 数据格式：[Bean]==={"_DATA_": [前台Array，后台list]===[ [Bean
     *         ]0==={"userCode":[String]===userCode-0,"userName":[String]=== userName-0,
     *         "deptCode":[String]===deptCode-0,"deptName":[String]===deptName-0 },
     *         [Bean]1==={"userCode":[String]===userCode-1,"userName":[String] ===userName-1,
     *         "deptCode":[String]===deptCode-1,"deptName":[String ]===deptName-1}, ...,
     *         [Bean]n==={"userCode":[String]===userCode-n, "userName":[String]===userName-n,
     *         "deptCode":[String]===deptCode-n ,"deptName":[String]===deptName-n} ] }
     */
    public Bean getShareUsers(Bean paramBean) {
        Bean resBean = new Bean();
        String actCodes = paramBean.getStr("actCodes");
        if (actCodes.isEmpty()) {
            actCodes = this.getUserActCodes(Context.getUserBean().getCode());
        }
        ParamBean shareBean = new ParamBean(CALENDAR_SHARE_SERV, "loadShareUsers");
        shareBean.set("actCodes", actCodes);
        // 返回bean里应有key：List shareUsers<Bean> 或 null
        resBean = ServMgr.act(shareBean);
        // 如果我的下属不为空，将共享用户中的下属剔除
        Bean belongBean = ServMgr.act(
                new ParamBean(BELONG_SERV, "getBelongUserInStr").set("userCode", Context.getUserBean().getCode()));
        String belongUserStr = belongBean.getStr("belongUserStr");
        if (belongUserStr.length() > 0) {
            List<Bean> shareUserList = resBean.getList("_DATA_");
            for (int i = 0; i < shareUserList.size(); i++) {
                if (belongUserStr.indexOf(shareUserList.get(i).getStr(
                        "userCode")) >= 0) {
                    shareUserList.remove(i);
                    i--;
                }
            }
        }
        return resBean;
    }

    /**
     * 获取用户共享的某个人的设定数据 处理步骤： 根据共享人shareUser和用户code从日程共享的子表获取该共享用户共享给本人的设定数据
     * 
     * @param paramBean 传入的参数包括 shareUserCode,actCodes
     * @return 传出的参数包括UserSettings 数据格式：[Bean]==={"UserSettings": [前台Array，后台list]===[
     *         [Bean]0==={"typeCode":[String]==="xxxxxx-0","typeName" :[String]==="某某某-0"},
     *         [Bean]1==={"typeCode":[String]==="xxxxxx-1", "typeName":[String]==="某某某-1"}, ...,
     *         [Bean]n==={"typeCode":[String ]==="xxxxxx-n","typeName":[String]==="某某某-n"} ] }
     * 
     */
    public Bean getShareSettingDataForSingleUser(Bean paramBean) {
        String actCodes = paramBean.getStr("actCodes");
        String shareUserCode = paramBean.getStr("shareUserCode");
        OutBean resBean = new OutBean();
        if (actCodes.isEmpty() || shareUserCode.isEmpty()) {
            return resBean.set("UserSettings", new ArrayList<Bean>());
        }
        ParamBean shareBean = new ParamBean(CALENDAR_TYPE_SERV, "typeForUser");
        shareBean.set("actCodes", actCodes);
        shareBean.set("shareUserCode", shareUserCode);
        return ServMgr.act(shareBean);
    }

    /**
     * 获取某用户共享给当前用户的共享设置列表
     * 
     * @param shareUser 共享用户编码
     * @param initBean 初始信息
     * @return 设置列表
     */
    private List<Bean> getUserShareSettingList(String shareUser, Bean initBean) {
        List<Bean> settingList;
        Bean singleBean = new Bean();
        singleBean.set("actCodes", initBean.getStr("thisActCodes"));
        singleBean.set("shareUserCode", shareUser);
        Bean settingBean = this.getShareSettingDataForSingleUser(singleBean);
        settingList = settingBean.getList(Constant.RTN_DATA);
        return settingList;

    }

    /**
     * 获取所需的日程事件数据 处理步骤：
     * 
     * @param paramBean 传入的参数包括 startDateTime endDateTime userCodeStr typeCodeStr
     * @return 传出的参数 传入格式：[Bean]==={ "startDateTime":[String]==="YYYY-MM-DD hh:mm",
     *         "endDateTime":[String]==="YYYY-MM-DD hh:mm", "queryUserStr":[String]==="123,456,789..."
     * 
     *         传出格式: { start_date: "2013-01-11 09:00", end_date: "2013-01-11 12:00", text:#title#, type:#CAL_TYPE#,
     *         unit_USER_BELONG} ...
     */
    public Bean getDatas(Bean paramBean) {
        UserBean curUser = Context.getUserBean();
        Bean resBean = new Bean();
        // 初始化数据
        Bean initBean = this.initParams(paramBean);
        // 验证请求查看用户的有效性并分类
        if (!checkQueryAcl(initBean)) {
            resBean.set(Constant.RTN_DATA, null);
            return resBean;
        }
        List<Bean> multiUserEventList = new ArrayList<Bean>();
        List<String> queryBelongList = initBean.getList("queryBelongList");
        List<String> queryShareList = initBean.getList("queryShareList");
        // 获取下属数据
        for (String belongUser : queryBelongList) {
            multiUserEventList.addAll(getCreateAndDistributeDatas(belongUser,
                    initBean, false));
        }
        // 获取共享用户数据
        for (String shareUser : queryShareList) {
            if (shareUser.equals(curUser.getCode())) { //如果被共享的人和当前人是同一个人，则不加载这部分数据
                continue;
            }
            multiUserEventList.addAll(getCreateAndDistributeDatas(shareUser,
                    initBean, true));
        }
        // 合并数据
        resBean.set(Constant.RTN_DATA, multiUserEventList);
        return resBean;
    }

    /**
     * 获取某用户创建和被分配到的日程
     * 
     * @param userCode 请求查询的用户
     * @param initBean 初始信息
     * @param shareFlag 是否查的是共享用户
     * 
     * @return singleUserEventList 某用户的日程数据
     */
    private List<Bean> getCreateAndDistributeDatas(String userCode,
            Bean initBean, boolean shareFlag) {
        List<Bean> settingList;
        if (shareFlag) {
            //取用户的共享设置
            settingList = getUserShareSettingList(userCode, initBean);
        } else {
            settingList = null;
        }
        String whereStr = generateWhereStr(userCode, initBean, shareFlag,
                settingList);
        Bean queryBean = new Bean();
        queryBean
                .set(Constant.PARAM_SELECT,
                        "CAL_ID, CAL_TITLE, CAL_TYPE, CAL_START_TIME, CAL_END_TIME, S_USER");
        queryBean.set(Constant.PARAM_WHERE, whereStr);
        List<Bean> singleUserEventList = ServDao.finds(CALENDAR_MAIN_SERV,
                queryBean);
        for (Bean calBean : singleUserEventList) {
            generateDataInJsonForSingle(calBean, userCode, initBean);
        }
        return singleUserEventList;
    }

    /**
     * 构造前台dhtmlx开源插件的指定格式数据
     * 
     * @param calBean 日程信息
     * @param unitUserCode 用户编码
     * @param initBean 初始格式
     * 
     */
    private void generateDataInJsonForSingle(Bean calBean, String unitUserCode,
            Bean initBean) {
        if (initBean == null) {
            initBean = this.initParams(calBean);
        }
        calBean.set("start_date", calBean.getStr("CAL_START_TIME"));
        calBean.set("end_date", calBean.getStr("CAL_END_TIME"));
        calBean.set("text", calBean.getStr("CAL_TITLE"));
        calBean.set("type", calBean.getStr("CAL_TYPE"));
        calBean.set("unit_USER_BELONG", unitUserCode);
        if (calBean.isNotEmpty("S_USER")) {
            calBean.set("S_USER__NAME", UserMgr.getUser(calBean.getStr("S_USER")).getName());
        }
        calBean.set("can_Modify", this.checkModifyAcl(calBean).toString());
        calBean.set("can_Del", this.checkDelAcl(calBean).toString());
        
    }

    @Override
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        String doneUsers = paramBean.getStr("DONE_USERS");
        // 添加模式
        String mainPkCode = outBean.getStr("CAL_ID"); // 主表主键
        String mainCalType = outBean.getStr("CAL_TYPE");
        if (paramBean.getAddFlag()) {
            if (doneUsers.length() > 0) {
                String[] doneUser = doneUsers.split(",");
                Bean doneUserBean = new Bean();
                for (String doneUserID : doneUser) {
                    doneUserBean = new Bean();
                    doneUserBean.set("USER_CODE", doneUserID);
                    doneUserBean.set("CAL_ID", mainPkCode);
                    doneUserBean.set("TYPE", mainCalType);
                    ServDao.save(CALENDAR_DELE_SERV, doneUserBean);
                }
            }
            // 修改模式
        } else {
            // 修改的时候首先判断，DONE_USERS是否变更
            if (paramBean.containsKey("DONE_USERS")) {
                Bean oldBean = paramBean.getSaveOldData();
                String oldDoneUsers = oldBean.getStr("DONE_USERS");
                // 分析变化的用户code，然后进行修改
                if (doneUsers.length() > 0) {
                    Bean doneUserBean = new Bean();
                    String[] doneUser = doneUsers.split(",");
                    // 以前的记录分配过用户
                    if (oldDoneUsers.length() > 0) {
                        // 标记新旧两个集合中的交集
                        String[] oldDoneUser = oldDoneUsers.split(",");
                        for (int i = 0; i < doneUser.length; i++) {
                            for (int j = 0; j < oldDoneUser.length; j++) {
                                if (doneUser[i].equals(oldDoneUser[j])) {
                                    String oldMainCalType = oldBean
                                            .getStr("CAL_TYPE");
                                    // 交集且type变更了则更新
                                    if (!mainCalType.equals(oldMainCalType)) {
                                        Bean doneUserSetBean = new Bean();
                                        Bean doneUserWhereBean = new Bean();
                                        doneUserSetBean
                                                .set("TYPE", mainCalType);
                                        doneUserWhereBean.set("CAL_ID",
                                                mainPkCode);
                                        doneUserWhereBean.set("USER_CODE",
                                                doneUser[i]);
                                        ServDao.updates(CALENDAR_DELE_SERV,
                                                doneUserSetBean,
                                                doneUserWhereBean);
                                    }
                                    oldDoneUser[j] = "";
                                    doneUser[i] = "";
                                    break;
                                }
                            }
                        }
                        // 若不为空则添加(修改后新的记录中需要添加的)
                        for (int k = 0; k < doneUser.length; k++) {
                            if (doneUser[k].length() > 0) {
                                doneUserBean = new Bean();
                                doneUserBean.set("USER_CODE", doneUser[k]);
                                doneUserBean.set("CAL_ID", mainPkCode);
                                doneUserBean.set("TYPE", mainCalType);
                                ServDao.save(CALENDAR_DELE_SERV, doneUserBean);
                            }
                        }
                        // 若不为空则删除(修改后新的记录中需要去除的)
                        String delteUsers = "";
                        for (int m = 0; m < oldDoneUser.length; m++) {
                            if (oldDoneUser[m].length() > 0) {
                                delteUsers = delteUsers + ",'" + oldDoneUser[m]
                                        + "'";
                            }
                        }
                        if (delteUsers.length() > 1) {
                            delteUsers = delteUsers.substring(1);
                            String sql = " and user_code in(" + delteUsers
                                    + ")";
                            doneUserBean = new Bean();
                            doneUserBean.set("CAL_ID", mainPkCode);
                            doneUserBean.set(Constant.PARAM_WHERE, sql);
                            ServDao.deletes(CALENDAR_DELE_SERV, doneUserBean);
                        }
                        // 以前的记录未分配过用户
                    } else {
                        for (String doneUserID : doneUser) {
                            doneUserBean = new Bean();
                            doneUserBean.set("USER_CODE", doneUserID);
                            doneUserBean.set("CAL_ID", mainPkCode);
                            doneUserBean.set("TYPE", mainCalType);
                            ServDao.save(CALENDAR_DELE_SERV, doneUserBean);
                        }
                    }
                    // 新记录无分配人，需删除旧记录所有分配人
                } else {
                    Bean doneUserBean = new Bean();
                    doneUserBean.set("CAL_ID", mainPkCode);
                    ServDao.deletes(CALENDAR_DELE_SERV, doneUserBean);
                }
                // 交集且type变更了则更新
            } else if (paramBean.containsKey("CAL_TYPE")) {
                Bean doneUserSetBean = new Bean();
                Bean doneUserWhereBean = new Bean();
                doneUserSetBean.set("TYPE", mainCalType);
                doneUserWhereBean.set("CAL_ID", mainPkCode);
                ServDao.updates(CALENDAR_DELE_SERV, doneUserSetBean,
                        doneUserWhereBean);
            }
        }
        generateDataInJsonForSingle(outBean, paramBean
                .getStr("unit_USER_BELONG"), null);
    }

    @Override
    protected void afterByid(ParamBean paramBean, OutBean outBean) {
        generateDataInJsonForSingle(outBean, paramBean
                .getStr("unit_USER_BELONG"), null);
    }

    @Override
    protected void beforeDelete(ParamBean paramBean) {
        // 验证能否删除
        if (!this.checkDelAcl(paramBean)) {
            paramBean = new ParamBean();
        }

        Bean doneUserBean = new Bean();
        doneUserBean.set("CAL_ID", paramBean.getStr(Constant.KEY_ID));
        ServDao.deletes(CALENDAR_DELE_SERV, doneUserBean);
    }

    @Override
    protected void beforeSave(ParamBean paramBean) {
        if (paramBean.getAddFlag()) {
            // 验证能否创建
            if (!this.checkAddAcl(paramBean)) {
                paramBean = new ParamBean();
            }
        } else {
            // 验证能否修改
            if (!this.checkModifyAcl(paramBean)) {
                paramBean = new ParamBean();
            }
        }
        sendCalenderRemindMsg(paramBean); //添加提醒
    }

    @Override
    protected void beforeQuery(ParamBean paramBean) {
        // 初始化用户信息
        Bean initBean = this.initParams(paramBean);
        // 验证
        if (!checkQueryAcl(initBean)) {
            paramBean = new ParamBean();
            return;
        }
        // where条件生成
        String whereStr = generateWhereStr(initBean.getStr("thisUser"),
                initBean, false, null);
        paramBean.set("_extWhere", whereStr);
    }

    @Override
    protected void afterQuery(ParamBean paramBean, OutBean outBean) {
        List<Bean> datasList = outBean.getList("_DATA_");
        Bean initBean = this.initParams(paramBean);
        for (Bean eventBean : datasList) {
            this.generateDataInJsonForSingle(eventBean, "", initBean);
        }
    }

    /**
     * 通过取消会议删除日程记录
     * @param paramBean 参数
     * @return 数据集
     */
    public OutBean deleteAll(ParamBean paramBean) {
    	return CalendarMgr.deleteAll(paramBean);
    }
    
    /**
     * 根据会议会回执添加日程记录
     * @param calendarBean 参数
     * @return 数据集
     */
    public OutBean addCalendar(Bean calendarBean){
    	CalendarMgr.addCalendar(calendarBean);
    	return new OutBean().setOk();
    }
    
    /**
     * 初始化用户信息
     * 
     * @param paramBean 参数信息
     * @return 初始化结果
     */
    private Bean initParams(Bean paramBean) {
        Bean initBean = new Bean();
        initBean.set("querySDT", paramBean.getStr("startDateTime")); // *时间下限(yy-mm-dd
        // hh:mm)
        initBean.set("queryEDT", paramBean.getStr("endDateTime")); // *时间上限(yy-mm-dd
        // hh:mm)
        initBean.set("thisUser", Context.getUserBean().getCode()); // *当前用户
        initBean.set("thisBelongUserStr", ServMgr.act(new ParamBean(BELONG_SERV, "getBelongUserInStr").
                set("userCode", initBean.getStr("thisUser"))).getStr("belongUserStr")); // *当前用户下属串
        initBean.set("thisActCodes", this.getUserActCodes(initBean
                .getStr("thisUser"))); // *当前用户身份串
        initBean.set("allShareUsers", ServMgr.act(new ParamBean(CALENDAR_TYPE_SERV, "selectUserCode")
            .set("actCodes", initBean.getStr("thisActCodes"))).getStr(Constant.RTN_DATA)); // *当前用户共享用户串
        initBean.set("queryUserStr",
                paramBean.getStr("queryUserStr").length() > 0 ? paramBean
                        .getStr("queryUserStr") : initBean.getStr("thisUser")); // *请求查询用户串
        initBean.set("queryUsers", initBean.getStr("queryUserStr").split(
                Constant.SEPARATOR)); // *请求查询用户集
        initBean.set("queryBelongList", new ArrayList<String>());
        initBean.set("queryShareList", new ArrayList<String>());
        return initBean;

    }

    /**
     * where条件生成
     * 
     * @param userCode 用户编码
     * @param initBean 初始数据
     * @param shareFlag 共享标志
     * @param settingList 设置信息
     * @return 生成的过滤条件
     */
    private String generateWhereStr(String userCode, Bean initBean,
            boolean shareFlag, List<Bean> settingList) {
        String querySDT = initBean.getStr("querySDT");
        String queryEDT = initBean.getStr("queryEDT");
        StringBuilder wh = new StringBuilder();
        if (queryEDT.length() > 0) {
            wh.append(" and ");
            wh.append(" CAL_START_TIME < '" + queryEDT + "'");
        }
        if (querySDT.length() > 0) {
            wh.append(" and ");
            wh.append(" CAL_END_TIME > '" + querySDT + "'");
        }
        wh.append(" and ");
        wh.append("(");
        wh.append("CAL_ID in (select distinct CAL_ID from "
                + CALENDAR_DELE_SERV + " where USER_CODE = '" + userCode + "'");
        if (shareFlag) {
            // ////
            wh.append(" and TYPE in(");
            for (int i = 0; i < settingList.size(); i++) {
                if (i == 0) {
                    wh
                            .append("'" + settingList.get(i).getStr("typeCode")
                                    + "'");
                } else {
                    wh.append(",'" + settingList.get(i).getStr("typeCode")
                            + "'");
                }
            }
            wh.append(")");
            // ////
        }
        wh.append(")");
        wh.append(" or ");
        wh.append("(");
        wh.append(" S_USER = '" + userCode + "'");
        if (shareFlag) {
            // ////
            wh.append(" and CAL_TYPE in(");
            for (int i = 0; i < settingList.size(); i++) {
                if (i == 0) {
                    wh
                            .append("'" + settingList.get(i).getStr("typeCode")
                                    + "'");
                } else {
                    wh.append(",'" + settingList.get(i).getStr("typeCode")
                            + "'");
                }
            }
            wh.append(")");
            // ////
        }
        wh.append(")");
        wh.append(")");
        wh.append(" and S_CMPY = '" + Context.getCmpy() + "'");
        return wh.toString();
    }

    /**
     * 验证能否查询
     * 
     * @param initBean 初始信息
     * @return 是否能查询
     */
    private Boolean checkQueryAcl(Bean initBean) {
        for (String qUser : (String[]) initBean.get("queryUsers")) {
            if (initBean.getStr("thisBelongUserStr").indexOf(qUser) >= 0) {
                initBean.getList("queryBelongList").add(qUser); // *下属类用户列表
            } else {
                if (initBean.getStr("allShareUsers").indexOf(qUser) >= 0) {
                    initBean.getList("queryShareList").add(qUser); // *共享类用户列表
                } else if (qUser.equals(initBean.getStr("thisUser"))) {
                    initBean.getList("queryBelongList").add(qUser);
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 验证能否创建（后台禁止添加的程序写在此处）
     * 
     * @param eventBean 参数
     * @return 是否能创建
     * 
     */
    private Boolean checkAddAcl(Bean eventBean) {
        return true;
    }

    /**
     * 验证能否修改（后台禁止修改的程序写在此处）
     * 
     * @param eventBean 参数
     * @return 是否能修改
     */
    private Boolean checkModifyAcl(Bean eventBean) {
        String currUser = Context.getUserBean().getCode();
        String sUser = eventBean.getStr("S_USER");
        if (!sUser.isEmpty() && !currUser.equals(sUser)) {
            return false;
        }
        return true;
    }

    /**
     * 验证能否删除（后台禁止删除的程序写在此处）
     * 
     * @param eventBean 参数
     * @return 是否能删除
     */
    private Boolean checkDelAcl(Bean eventBean) {
        String currUser = Context.getUserBean().getCode();
        String sUser = eventBean.getStr("S_USER");
        if (!sUser.isEmpty() && !currUser.equals(sUser)) {
            return false;
        }
        return true;
    }
    
    /**
     * 个人日程添加提醒
     * @param paramBean 参数
     * 
     */
    private void sendCalenderRemindMsg(ParamBean paramBean){
    	 Bean oldData = paramBean.getSaveOldData();
    	 String servId = paramBean.getServId();
    	 ServDefBean servDef =  ServUtils.getServDef(servId);
    	 String title = paramBean.getStr("CAL_TITLE");
    	 if (StringUtils.isBlank(title)) {
    		 title = oldData.getStr("CAL_TITLE");
    	 }
    	 Bean remindBean = new Bean();
         StringBuilder msg = new StringBuilder();
         msg.append("您有一项新日程：");
         msg.append(title);
         remindBean.set("STATUS", "WATING");
         remindBean.set("REM_TITLE", "[" + servDef.getName() + "]" + title);
         remindBean.set("REM_CONTENT", msg.toString());
         remindBean.set("EXECUTE_TIME", "");
         String remindType = paramBean.getStr("CAL_REMIND");
         if (StringUtils.isBlank(remindType)) {
        	 remindType = oldData.get("CAL_REMIND", "TODO");
         }
         remindBean.set("TYPE", remindType);
         String emergency = paramBean.getStr("S_EMERGENCY");
         if (StringUtils.isBlank(emergency)) {
        	 emergency = oldData.get("S_EMERGENCY", "10");
         }
         remindBean.set("S_EMGRENCY", emergency);
         remindBean.set("SERV_ID", servId);
         remindBean.set("DATA_ID", paramBean.getId());
         Bean remind = isExtieRemind(paramBean);
         if (null != remind) {
        	 remindBean.setId(remind.getId());
         }
         String receivers = paramBean.getStr("DONE_USERS");
         if (StringUtils.isBlank(receivers)) {
        	 receivers = oldData.getStr("DONE_USERS");
         }
         RemindMgr.add(remindBean, receivers);
    }
    
    /**
     * 查看当前消息表中存在不存在
     * @param paramBean
     * @return
     */
    private Bean isExtieRemind(ParamBean paramBean) {
    	Bean oldData = paramBean.getSaveOldData();
    	SqlBean sql = new SqlBean();
    	String cmpyCode = paramBean.getStr("S_CMPY");
    	if (StringUtils.isBlank(cmpyCode)) {
    		cmpyCode = oldData.getStr("S_CMPY");
    	}
    	String userCode = paramBean.getStr("S_USER");
    	if (StringUtils.isBlank(userCode)) {
    		userCode = oldData.getStr("S_USER");
    	}
    	sql.selects("REM_ID").and("DATA_ID", paramBean.getId()).and("S_CMPY", cmpyCode)
    	   .and("STATUS", "WATING").and("S_USER", userCode);
    	return ServDao.find(REMIND_SERV_ID, sql);
    }
}
