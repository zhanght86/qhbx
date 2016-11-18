<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Error</title>
<style type="text/css">
.portlet-msg-error {
background: url("/bi-portlet/images/error.png") no-repeat scroll 6px 50% #FFDDDD;
border: 1px solid #FF0000;
display: block;
font-weight: bold;
margin: 2px auto 14px;
padding: 6px 6px 6px 30px;
text-align: left;
font-size: 12px;
}
</style>
</head>
<body>
<%@ include file="init.jsp" %>
<%
String errorKey = request.getParameter("errorKey");
String value = Util.getMessage(errorKey);
if (Util.isNull(value)) {
	value = "System Error!!!";
}
%>
<span class="portlet-msg-error"><%= value %></span>
</body>
</html>