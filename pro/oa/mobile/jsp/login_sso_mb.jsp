<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.rh.core.base.Bean" %>
<%@ page import="com.rh.core.org.UserBean" %>
<%@ page import="java.util.Map" %>

<%
	final String CONTEXT_PATH = request.getContextPath();
	//Cookie[] cookies = request.getCookies(); // 获取Cookie数组
	String userCode = ""; // 用户ID
	if (userCode.isEmpty()) { // 从request中获取
		userCode = request.getParameter("userCode") == null ? "" : request.getParameter("userCode").toString();
	}
	String homeUrl = "";
	Bean user = new Bean();
	Map orgMap = null;
	if (userCode.isEmpty()) { // 如果为空，说明Cookie中没有取到值
		// TODO 如果不支持除微信以外的浏览器，直接关闭页面
		out.println("用户编码没有从Cookie中带过来，这种情况极少发生，在这里暂不做处理！");
	} else {
		if (!"".equals(userCode)) {
			//String usercodetmp = LoginHelper.getOnceLoginInfo(userCode);
			request.getSession().setAttribute("userCode", userCode);
			homeUrl = "/oa/mobile/jsp/main.jsp";
		} else {
			// TODO 如果不支持除微信以外的浏览器，直接关闭页面
			out.println("用户登录失败，这种情况应该不会发生，在这里暂不做处理！");
		}
	}
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="viewport"
		content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" />
	<title>正在进行登陆跳转...</title>
	<script type="text/javascript">
		var homeUrl = "<%=homeUrl %>";
		var userCode = "<%=userCode %>";
		if (homeUrl == '') {
			// 跳至登陆页
			// TODO 如果不支持除微信以外的浏览器，直接关闭页面
			window.location.href = "http://portal.cic.cn:8888"; // 此地址为中华保险移动端登录地址
		} else {
			window.location.href = homeUrl;
		}
	</script>
</head>
<body>
</body>
</html>