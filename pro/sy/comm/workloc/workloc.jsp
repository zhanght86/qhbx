<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.rh.core.base.Context" %>
<%@ page import="com.rh.core.base.Bean" %>
<%@ page import="com.rh.core.util.Constant" %>
<%@ page import="com.rh.core.util.RequestUtils" %>
<%
    String urlPath = RequestUtils.getHttpURL(request);
	Bean outBean = (Bean)request.getAttribute(Constant.RTN_DISP_DATA);
	Bean paramBean = outBean.getBean("PARAM_BEAN");
	String func = paramBean.get("func","view");
	String im = paramBean.get("im","false");
    String url = urlPath + "/sy/comm/workloc/WLMgr.swf?func=" + func + "&im=" + im + "&host=" + urlPath + "/";
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>工位系统</title>
</head>
<body>
<div id="workloc"  style="height:700px">
	<object id="wlmgr" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab" width="100%" height="100%">
	<param name="movie" value="<%=url %>">
	<param name="quality" value="high">
	<param name="bgcolor" value="#869ca7">
	<param name="allowScriptAccess" value="sameDomain">
	<param name="wmode" value="transparent">
	<param name="allowFullScreen" value="true">
	<comment>
	<embed id="wlmgr2" src="<%=url %>" quality="high" bgcolor="#869ca7"
	width="100%" height="100%" name="fpd" align="middle"
	play="true" loop="false" allowScriptAccess="sameDomain" allowFullScreen="true" type="application/x-shockwave-flash"
	pluginspage="http://www.adobe.com/go/getflashplayer">
	</embed>
	</comment>
	</object>
</div>
<script type="text/javascript">
function showIM(userCode,userName) {
	var user_id = userCode + "-rhim-server";
	var chat_id = userCode + "@rhim.server";
	var user_name = userName;
	parent.rhImFunc.showChatArea({"id":user_id,"jid":chat_id,"name":user_name,"status":"online"});
};
</script>
</body>
</html>