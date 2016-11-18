package com.rh.oa.mt;

import java.util.Date;

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
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.core.util.Lang;
import com.rh.core.util.Strings;

/**
 * 会议通知回执确定
 * @author hdy
 *
 */
public class MtReturnNotice extends CommonServ {
    
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
    
    @Override
    protected void beforeSave(ParamBean paramBean) {
        //过滤掉[邀请其他会议人员]中有有本人code
        String agentUserCode = paramBean.getStr("EXIT_AGENT"); //是否存在委托用户
        String mySelfCode = Context.getUserBean().getCode();
        if (StringUtils.isNotBlank(agentUserCode)) {
            mySelfCode = agentUserCode;
            paramBean.set("AGENT_USER", Context.getUserBean().getCode());
            paramBean.set("S_USER", agentUserCode);
        }
        resetParamBean(paramBean, mySelfCode);
        String otherUserCodes =  paramBean.getStr("USER_CODES");
        paramBean.set("USER_CODES", getFormatStr(otherUserCodes, mySelfCode));
    }
    
    @Override
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        saveMyConferee(paramBean, outBean);
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
        String queryUserCode = Context.getUserBean().getCode();
        String agentUserCode = paramBean.getStr("AGENT_USER"); //委托用户
        if (StringUtils.isNotBlank(agentUserCode)) {
            queryUserCode = agentUserCode;
        }
        SqlBean sql = new SqlBean();
        sql.and("MEETING_ID", paramBean.getStr("MEETING_ID")).and("S_USER", queryUserCode).selects("*");
        Bean thisBean = ServDao.find(paramBean.getServId(), sql);
        if (null == thisBean || thisBean.isEmpty()) {
            return new OutBean().set("RETURN_MSG", "ERROR,");
        }
        resetOutBean(thisBean);
         return new OutBean(thisBean).set("RETURN_MSG", "OK,");
    }
    
    /**
     * 将会议回执单中的 与会人员添加到 我的会议与会人员中
     * @author hdy 
     * @param paramBean 参数
     * @param outBean [回执单]
     */
    private void saveMyConferee(ParamBean paramBean, OutBean outBean) {
        Bean saveBean = new Bean();
        String agentUserCode = paramBean.getStr("EXIT_AGENT"); //是否存在委托用户
        String mySelf = Context.getUserBean().getCode();
        if (StringUtils.isNotBlank(agentUserCode)) {
            mySelf = agentUserCode;
        }
        saveBean = ServDao.find(myConferee, new ParamBean().setWhere(" and MEETING_ID = '"
                + outBean.getStr("MEETING_ID") + "' and USER_CODE = '" + mySelf + "' and S_FLAG = '" + sFlag + "'"));
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
     * 重置返回对象，添加对应的name属性
     * @param saveBean 对象
     */
    private void resetOutBean(Bean saveBean) {
        saveBean.set("USER_CODES__NAME", DictMgr.getFullNames("SY_ORG_USER", saveBean.getStr("USER_CODES")))
                .set("AGENT_USER__NAME", DictMgr.getFullName("SY_ORG_USER", saveBean.getStr("AGENT_USER")));
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
     * @param thisUser 当前用户code
     * @return 格式化之后的字符串
     */
    private String getFormatStr(String fullStr, String thisUser) {
        //如果本人code在字符串开始或中间出现，则替换掉 'code,'
        String formatStr = thisUser + Constant.SEPARATOR;
        String returnStr = fullStr;
        if (fullStr.contains(formatStr)) {
            returnStr = fullStr.replace(formatStr, "");
        }
        formatStr =  Constant.SEPARATOR + thisUser;
        //如果本人code在字符串结尾出现，则替换掉',code'
        if (fullStr.contains(formatStr)) {
            returnStr = fullStr.replace(formatStr, "");
        }
        return returnStr;
    }
    
    /**
     * 重置paramBean
     * @param paramBean 参数
     * @param userCode 用户code
     */
    private void resetParamBean(ParamBean paramBean, String userCode) {
        if (StringUtils.isNotBlank(paramBean.getStr("OLD_MEETING_ID"))) {
            paramBean.set("MEETING_ID", paramBean.getStr("OLD_MEETING_ID"))
                     .set("S_ATIME", DateUtils.getDatetimeTS()).set("S_USER", userCode);
           if (!paramBean.getStr("STATUS").equals(paramBean.getStr("OLD_STATUS"))) {
               paramBean.set("STATUS", paramBean.getStr("OLD_STATUS"));
           }
           if (!paramBean.getStr("USER_CODES").equals(paramBean.getStr("OLD_USER_CODES"))) {
               paramBean.set("USER_CODES", paramBean.getStr("OLD_USER_CODES"));
           }
        }
    }
}
