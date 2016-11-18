<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.rh.core.base.Context" %>
<%@ page import="com.rh.core.util.Constant" %>
<%@ page import="com.rh.core.base.Bean"%>
<%@ page import="com.rh.core.util.JsonUtils" %>
<%@ page import="com.rh.core.serv.OutBean" %>

<!-- 获取到 xdoc配置地址 -->
<%
	//本地xdoc配置信息
	String serverConfig = Context.getSyConf("SY_XDOC_URL", "");
	OutBean outBean = (OutBean)request.getAttribute(Constant.RTN_DISP_DATA);
	Bean bean = JsonUtils.toBean(outBean.getStr("xdocParam"));
	//判断session权限
	Context.getUserBean(request);
	//根据配置获取xdoc配置信息
	String xdocServer = bean.isNotEmpty("URL") ? bean.getStr("URL") : serverConfig;
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>XDOC</title>
	<script type="text/javascript" src="<%=serverConfig %>/xdoc.js"></script>
	<script type="text/javascript">
		XDoc.server = "<%=xdocServer %>";
		XDoc.run("<%=bean.getStr("filePath")%>", <%=bean%>);
	</script>
</head>
<body>
</body>
</html>