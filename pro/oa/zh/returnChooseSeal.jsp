<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.rh.core.util.RequestUtils" %>
<%@ page import="com.rh.core.serv.ParamBean" %>
<%
	ParamBean paramBean = RequestUtils.transParam(request);
%>
<html>
	<head>返回选择印章</head>
	<body>
		<script type="text/javascript">
			window.onload = function() {
	 			if(typeof(window.opener.getSealNamesCallBack) != "undefined") {
				 	window.opener.getSealNamesCallBack("<%=paramBean.getStr("sealsid") %>");
		 			window.close();
			 	}
 			}
		</script>
	</body>
</html>