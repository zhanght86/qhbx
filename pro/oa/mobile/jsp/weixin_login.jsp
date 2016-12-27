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
<title>微信OA登录界面</title>
</head>
<body>
<center>
	<form method="post" action="/moa/SsoInterfacePC">
	          <table>
	            <tr>欢迎使用微信OA办公系统
	            </tr>
	            <tr style="display: none"><td>用户名:</td>
	                <td><input id="USER_CODE" name="USER_CODE" type="text" />
	                </td>
	            </tr>
	            <tr><td>人员工号:</td>
	                <td><input id="WORK_NUM" name="WORK_NUM" type="text"/>
	                </td>
	            </tr>
	            <tr><td>密<span style="padding:0px 18px;"></span>码:</td>
	                <td><input id="USER_PASSWORDS" name="USER_PASSWORDS" type="password" />
	                </td>
	            </tr>
	         </table>
		<input type="submit" value="提交" id="tiJiao" />
	</form>
	</center>
</body>
</html>