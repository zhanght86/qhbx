<%@ page language="java" contentType="text/html;charset=GBK" %>
<%@ page import = "com.AEON.*"%>
<%@ page import = "com.sinosoft.common.*"%>
<%
response.setHeader("Cache-Control","No-Cache");//HTTP 1.1
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
request.setCharacterEncoding("GBK");
String UserCode = Data.filterStr(request.getParameter("UserCode")==null?"":request.getParameter("UserCode").trim());
String Password = Data.filterStr(request.getParameter("Password")==null?"":request.getParameter("Password").trim());
String str = (String)session.getAttribute("getImg");
String rand = request.getParameter("Rand")==null?"":request.getParameter("Rand").trim();
if(!str.equalsIgnoreCase(rand)){
	session.setAttribute("LSUserCode",null);
	out.clear();
	out.print("验证码错误!");
}else{
	TempUser tu = new TempUser();
	String returnValue=tu.checkTempUser(UserCode,Password);
	if("5".equals(returnValue)){
		session.setAttribute("LSUserCode",UserCode);
		out.clear();
		out.print("correct");
	}else{
		session.setAttribute("LSUserCode",null);
		out.clear();
		if("3".equals(returnValue))returnValue = "你输入的代理人不存在！";
		else if("4".equals(returnValue))returnValue = "你输入的密码不正确！";
		else if("1".equals(returnValue))returnValue = "用户失效！";
		else if("2".equals(returnValue))returnValue = "用户暂停！";
		out.print(returnValue);
	}
}%>