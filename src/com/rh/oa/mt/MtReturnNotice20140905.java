package com.rh.oa.mt;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.todo.TodoBean;
import com.rh.core.comm.todo.TodoUtils;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServUtils;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.core.util.Lang;
import com.rh.core.util.Strings;

/**
 * 会议通知回执确定
 * @author hdy
 *
 */
public class MtReturnNotice20140905 extends CommonServ {
    
    /**确定回执成功 ！--------在系统配置里面配置---------------*/
    private final String returnOkMsg = "回执成功！";
    
    /**我的会议服务ID*/
    private final String myConferee = "OA_MT_CONFEREE";
    /**会议服务ID*/
    private static final String OA_MT_MEETING = "OA_MT_MEETING_RETURN_NOTICE";
    /**可用标示位*/
    private final String sFlag = "1";
    /**与会人员，人员类型，NOTIFIED为被通知人*/
    private final String userTypeNotified = "NOTIFIED";
    /**确认参加*/
    private static final String RETURN_SATUS = "1";
    /** 委托他人服务id */
    private static final String AGENT_FROM_SERV = "SY_ORG_USER_TYPE_AGENT_FROM";
    /**全部委托类型*/
    private static final String AGT_TYPE_ALL = "_ALL_";
    /**会议通知委托类型*/
    private static final String AGT_TYPE_MEETING = "HYTZ";
            
    @Override
    protected void beforeSave(ParamBean paramBean) {
        //过滤掉[邀请其他会议人员]中有有本人code
        String mySelfCode = Context.getUserBean().getCode();
        String otherUserCodes =  paramBean.getStr("USER_CODES");
        paramBean.set("USER_CODES", getFormatStr(otherUserCodes, mySelfCode));
    }
    
    @Override
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        saveMyConferee(outBean);
        if (!paramBean.getSaveOldData().getStr("USER_CODES").equals(outBean.getStr("USER_CODES"))) {
            saveOtherUsers(paramBean, outBean);
        }
        //如果需要消除首页[待办]记录，则删除待办记录，防止日后后台调用冲突
        if (!paramBean.getSaveOldData().getStr("STATUS").equals(outBean.getStr("STATUS"))) {
          //填写回执单之后，消除当前待办数据
          int seccessCount = TodoUtils.endUserTodo(Context.getUserBean().getCode(), outBean.getStr("MEETING_ID"),
                  outBean.getStr("MEETING_ID"));
          if (seccessCount >= 0) {
              //查看是否有日程处理
              String meetingReceiverConfig = Context.getSyConf("OA_MT_MEETING_CALENDAR", "");
              if (!Strings.isEmpty(meetingReceiverConfig)) {
                  //添加日程
                  MeetingCalendar mn = Lang.createObject(MeetingCalendar.class, meetingReceiverConfig);
                  OutBean out = mn.add(ServDao.find(OA_MT_MEETING, outBean.getStr("MEETING_ID")),
                          outBean.getStr("USER_CODES"));
                  if (out.isOk()) {
                      outBean.setOk(returnOkMsg);
                  }
              }
              //向门户日程中添加会议日程
              String ptMTReceiverConfig = Context.getSyConf("PT_MT_MEETING_CALENDAR", "");
              if (!Strings.isEmpty(ptMTReceiverConfig)) {
                  //添加日程
                  MeetingCalendar mn = Lang.createObject(MeetingCalendar.class, ptMTReceiverConfig);
                  OutBean out = mn.add(ServDao.find(OA_MT_MEETING, outBean.getStr("MEETING_ID")),
                          outBean.getStr("USER_CODES"));
                  if (out.isOk()) {
                      outBean.setOk(returnOkMsg);
                  }
              }
          }
        }
    }
    
    /**
     * 判断当前记录是否存在
     * @param paramBean 参数
     * @return 结果集
     */
    public OutBean isExiteThisReturn(ParamBean paramBean) {
        SqlBean sql = new SqlBean();
        sql.and("RETURN_NOTICE_ID", paramBean.getId()).selects("MEETING_ID");
        Bean thisBean = ServDao.find(paramBean.getServId(), sql);
        if (null == thisBean || thisBean.isEmpty()) {
            return new OutBean().set("RETURN_MSG", "ERROR,");
        }
         return new OutBean().set("RETURN_MSG", "OK,");
    }
    
    /**
     * 将会议回执单中的 与会人员添加到 我的会议与会人员中
     * @author hdy 
     * @param outBean [回执单]
     */
    private void saveMyConferee(OutBean outBean) {
        Bean saveBean = new Bean();
        String mySelf = Context.getUserBean().getCode();
        String agtUserCode = dealWithAGt(mySelf); //委托用户
        if (StringUtils.isNotBlank(agtUserCode)) {
            saveBean = ServDao.find(myConferee, new ParamBean().setWhere(" and MEETING_ID = '" 
                    + outBean.getStr("MEETING_ID") + "' and USER_CODE = '" + agtUserCode + "' and S_FLAG = '" + sFlag
                    + "'"));
        }
        if (saveBean.isEmpty() || saveBean.getInt("IS_AGT") == Constant.YES_INT) { //已经处理过委托
            saveBean = ServDao.find(myConferee, new ParamBean().setWhere(" and MEETING_ID = '"
                    + outBean.getStr("MEETING_ID") + "' and USER_CODE = '" + mySelf + "' and S_FLAG = '" + sFlag
                    + "'")); //获取当前登录人数据对象
            
        } else { //未处理委托
            saveBean.set("IS_AGT", Constant.YES_INT);
        }
        saveBean.set("RETURN_SATUS", outBean.getStr("STATUS"));
        if (saveBean.getStr("RETURN_SATUS").equals(RETURN_SATUS)) {
            saveBean.set("SET_TIME_OK", DateUtils.getStringFromDate(new Date(), "yyyy-MM-dd HH:mm"));
        } else {
            saveBean.set("SET_TIME_OK", null);
        }
        ServDao.save(myConferee, saveBean);
    }
    
    /**
     * 发送会议待办
     * @param bean 参数
     * @return 返回发送装填
     */
    public OutBean sendTodoMsg(Bean bean) {
        TodoBean todo = new TodoBean();
        //设置提醒标题
        todo.setTitle("[会议通知]" + bean.getStr("TITLE"));
        //添加服务id
        todo.setServId(OA_MT_MEETING);
        //添加数据code
        todo.setCode(OA_MT_MEETING);
        //获取服务名
        ServDefBean servDef = ServUtils.getServDef(OA_MT_MEETING);
        //设置数据名
        todo.setCodeName(servDef.getName());
        //获取数据主键
        todo.setObjectId1(bean.getStr("MEETING_ID"));
        todo.setObjectId2(bean.getStr("MEETING_ID"));
        todo.setBenchFlag(1); //允许委托
        todo.setOwner(bean.getStr("sendUser"));
        //提醒人
        todo.setSender(bean.getStr("S_USER"));
        todo.setEmergency(Integer.parseInt(bean.getStr("S_EMERGENCY"))); //缓急
        String urlStr = OA_MT_MEETING + ".byid.do?data={_PK_:" + bean.getStr("MEETING_ID") + "}";
        todo.setUrl(urlStr);
        TodoUtils.insert(todo);
        return new OutBean().set("sendOk", "ok");
    }
    
    /**
     * 在[回执单]保存前，要看 [邀请其他与会人员] 在 [被通知人]中是否已经存在，不存在，则保存
     * @param paramBean 参数
     * @param outBean 参数
     */
    private void saveOtherUsers(ParamBean paramBean, OutBean outBean) {
        
        //获取[其他被邀请人]用户codes
        String userCodes = outBean.getStr("USER_CODES");
        //获取[被邀请人]对应codes数组
        String[] userArray = userCodes.split(Constant.SEPARATOR);
        int confereeCount = 0;
        //会议信息
        Bean meeting = ServDao.find(OA_MT_MEETING, outBean.getStr("MEETING_ID"));
        for (int i = 0; i < userArray.length; i++) {
            ParamBean query = new ParamBean();
            //设置查询条件
            query.setWhere(" and MEETING_ID = '" + outBean.getStr("MEETING_ID") + "' and USER_CODE = '"
                    + userArray[i] + "' and S_FLAG = '" + sFlag + "'").setSelect("USER_CODE");
            //获取[与会人员]中此用户code的数据条数
            confereeCount = ServDao.count(myConferee, query);
            //如果[与会人员]中不存在此用户信息，则保存
            if (confereeCount <= 0) {
                query.clear();
                query.set("MEETING_ID", outBean.getStr("MEETING_ID")).set("USER_CODE", userArray[i])
                    .set("USER_TYPE", userTypeNotified).set("RETURN_NOTICE_ID", outBean.getId());
                //发送提醒
                ServDao.save(myConferee, query);
                Bean sendBean = new Bean();
                sendBean.set("TITLE", meeting.getStr("TITLE"));
                sendBean.set("MEETING_ID", meeting.getId());
                sendBean.set("sendUser", userArray[i]);
                sendBean.set("S_USER", outBean.getStr("S_USER"));
                sendBean.set("S_EMERGENCY", meeting.getStr("S_EMERGENCY"));
                sendTodoMsg(sendBean);
            }
        }
        
    }
    
    /**
     * 获取比比较后的字符串
     * @param fullStr 格式化之前字符转
     * @param formatStr 格式化字符串
     * @return returnStr 格式化之后的字符转
     */
    private String getFormatStr(String fullStr, String formatStr) {
        String format = formatStr + Constant.SEPARATOR;
        String returnStr  = null;
        String agtUserCode = dealWithAGt(formatStr); //委托人
        if (StringUtils.isNotBlank(agtUserCode)) { //存在委托人
            if (fullStr.contains(formatStr)) { //其他与会人员中包括当前用户（被委托人）
                returnStr = fullStr;
            }
        //没有委托人，则剔除其他与会人员中含本人的code
        } else {
            //如果本人code在字符串开始或中间出现，则替换掉 'code,'
            if (fullStr.contains(format)) {
                returnStr = fullStr.replace(format, "");
            }
            format =  Constant.SEPARATOR + formatStr;
            //如果本人code在字符串结尾出现，则替换掉',code'
            if (fullStr.contains(format)) {
                returnStr = fullStr.replace(format, "");
            }
        }
        return returnStr;
    }
    
    /**
     * 处理兼岗为题
     * @param agtFromUserCode 被委托人
     * @return 委托人用户code
     */
    private String dealWithAGt(String agtFromUserCode) {
        SqlBean sql = new SqlBean();
        String agtUserCode  = null;
        sql.selects("USER_CODE, AGT_TYPE_CODE").and("TO_USER_CODE", agtFromUserCode)
           .and("AGT_STATUS", Constant.YES_INT).and("S_FLAG", Constant.YES_INT);
        List<Bean> list = ServDao.finds(AGENT_FROM_SERV, sql);
        for (Bean b : list) {
            if (b.getStr("AGT_TYPE_CODE").contains(AGT_TYPE_MEETING)) { //会议通知委托人
                agtUserCode = b.getStr("USER_CODE");
                break;
            }
            if (b.getStr("AGT_TYPE_CODE").contains(AGT_TYPE_ALL)) { //全部业务委托人
                agtUserCode = b.getStr("USER_CODE");
                break;
            }
        }
        return agtUserCode;
    }
}
