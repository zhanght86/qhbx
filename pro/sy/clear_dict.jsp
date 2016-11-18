<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.rh.core.base.start.TimerClearDict" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file= "/sy/base/view/inHeader.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>清除组织机构缓存</title>
<%
	String msg = "";
	TimerClearDict timer = new TimerClearDict();
	try{
		timer.run();
		out.print("<div style='margin:20px 0px 0px 10px;'><h1 style='font-size:1.5em;'>清除成功！</h1><div>");
	} catch(Exception e){
		out.print("<div style='margin:20px 0px 0px 10px><h1 style='font-size:1.5em;>清除失败！</h1></div>");
	}
%>
</head>
<body>
</body>
</html>