<%@page import="com.rh.core.org.UserBean"%>
<%@page import="com.rh.core.base.Context"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%
	String urlPath = request.getContextPath();
	Context.setRequest(request);
    UserBean userBean = Context.getUserBean(request);
    if (null == userBean) { //session过期
    	response.sendRedirect(urlPath);
    }
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
</head>
<body>
<script type="text/javascript" src="<%=urlPath %>/sy/base/frame/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="<%=urlPath %>/sy/base/frame/plugs/bootstrop/bootstrap.min.js"></script>
<script type="text/javascript">
	
</script>
</body>
</html>