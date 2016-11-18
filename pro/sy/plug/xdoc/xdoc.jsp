<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.rh.core.base.Context" %>
<%@ page import="com.rh.core.serv.ServMgr"%>
<%@ page import="com.rh.core.util.JsonUtils" %>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.HashMap"%>
<%
String server = Context.getSyConf("SY_XDOC_URL", "");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>XDOC</title>
	<script type="text/javascript" src="<%=server%>/xdoc.js"></script>
</head>
<body>
<script type="text/javascript">
<%
String serv = request.getParameter("serv");
if (serv == null || serv.length() == 0) {
	throw new RuntimeException("服务不能为空");
}
String act = "byid";
if (serv.indexOf(".") > 0) {
	act = serv.substring(serv.indexOf(".") + 1);
	serv = serv.substring(0, serv.indexOf("."));
}
String xdoc = request.getParameter("xdoc");
if (xdoc == null) {
	xdoc = serv + "." + act + ".xdoc";
}
String name;
HashMap<String, Object> param = new HashMap<String, Object>();
Enumeration<?> pe = request.getParameterNames();
while(pe.hasMoreElements()) {
    name = (String) pe.nextElement();
	if (!name.equals("xdoc")
	    && !name.equals("serv")
		&& !name.equals("data")) {
		param.put(name, request.getParameter(name));
	}
}
//判断session权限
Context.getUserBean(request);
//获取结果xdata
param.put("_xdata", ServMgr.act(serv, act, JsonUtils.toBean(request.getParameter("data"))));
%>
XDoc.server = "<%=server%>";
XDoc.run("<%=xdoc%>", <%=JsonUtils.toJson(param)%>);
</script>
</body>
</html>
