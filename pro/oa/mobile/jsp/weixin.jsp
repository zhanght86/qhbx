<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="java.io.BufferedReader" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>Insert title here</title>
</head>
<body>
	<form method="post" action="/moa/SsoInterfacePC">
		<label>用户工号：</label>
		<input value="" placeholder="请输入工号" id="WORK_NUM" name="WORK_NUM" />
		<label>操作：</label>
		<input type="submit" value="提交" id="tiJiao" />
	</form>
	<%
		BufferedReader br = request.getReader();
		String content = br.readLine();
		out.println("Hello World!");
		out.println(content);
	%>
</body>
</html>