<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@ page import=" java.util.ArrayList" %>
<%@ page import=" java.util.Date" %>
<%@ page import=" java.util.HashMap" %>
<%@ page import=" java.util.List" %>
<%@ page import=" java.util.Map" %>
<%@ page import=" java.lang.StringBuilder" %>

<%@ page import=" org.apache.commons.lang.StringUtils" %>
<%@ page import=" org.apache.log4j.Logger" %>
<%@ page import=" org.quartz.Job" %>
<%@ page import=" org.quartz.JobExecutionContext" %>
<%@ page import=" org.quartz.JobExecutionException" %>

<%@ page import=" com.rh.core.base.Bean" %>
<%@ page import=" com.rh.core.base.Context" %>
<%@ page import=" com.rh.core.comm.ConfMgr" %>
<%@ page import=" com.rh.core.comm.msg.MsgSender" %>
<%@ page import=" com.rh.core.comm.msg.MsgSenderFactory" %>
<%@ page import=" com.rh.core.org.UserBean" %>
<%@ page import=" com.rh.core.org.mgr.UserMgr" %>
<%@ page import="com.rh.core.serv.ServDao" %>
<%@ page import=" com.rh.core.util.DateUtils" %>
<%@ page import=" com.rh.core.comm.remind.RemindMgr" %>
<%@ page import=" com.rh.core.comm.remind.RemindJob" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Insert title here</title>
</head>
<body>
	<%
	
	 final String CONF_TIME_RANGE = "SY_COMM_REMIND_TIME_RANGE";

    /** ʵʱ�������ݵĹ���ʱ�䣬����ȥ���ܾ�֮ǰ�ύ����ʧȥʱЧ���������ݡ� **/
     final String CONF_TIME_VALID = "SY_COMM_REMIND_TIME_VALID";


    /** ��¼�ɹ����û��������ѵ����ѷ�ʽ **/
    final Map<String, String> successMap = new HashMap<String, String>();

    /** ��¼���ɹ����û��������ѵ����ѷ�ʽ **/
    final Map<String, String> failedMap = new HashMap<String, String>();
	Date nowTime = DateUtils.createDate();
    int rangeVal = ConfMgr.getConf(CONF_TIME_RANGE, 5);
    int validVal = ConfMgr.getConf(CONF_TIME_VALID, 30);
    Date maxExecTime = DateUtils.addMinutes(nowTime, rangeVal);
    Date minExecTime = DateUtils.addMinutes(nowTime, -rangeVal);
    Date minAddTime = DateUtils.addMinutes(nowTime, -validVal);

    /** ȡ�õ�ǰʱ��ǰ��5���ӵ�δ���͵����� �Լ� EXECUTE_TIMEΪNULL **/
    StringBuilder str = new StringBuilder();
    str.append("select * from SY_COMM_REMIND where ");
    str.append(" EXECUTE_TIME >= '").append(DateUtils.formatDatetime(minExecTime));
    str.append("' and EXECUTE_TIME <= '").append(DateUtils.formatDatetime(maxExecTime));
    str.append("' and STATUS = 'WATING' ");
    str.append(" union ");
    str.append(" select * from SY_COMM_REMIND where ");
    str.append(" EXECUTE_TIME is null ");
    str.append(" and STATUS = 'WATING' ");
    str.append(" and S_ATIME >= '").append(DateUtils.formatDatetime(minAddTime)).append("'");

    List<Bean> sendList = Context.getExecutor().query(str.toString());

    for (final Bean sendBean : sendList) {
    	RemindJob remJob = new RemindJob();
    	remJob.send(sendBean);
        RemindMgr.finish(sendBean);
    }
	%>
</body>
</html>