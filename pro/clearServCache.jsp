<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.rh.core.serv.ServUtils"%>
<%
  request.setCharacterEncoding("UTF-8");
  String servId = request.getParameter("serv");
  if(servId != null){
  String[] servs = servId.split(",");
  for (String serv : servs){
	  ServUtils.clearServCache(serv, false);
	  out.print("服务："+serv+" 缓存已清除！"+"<br/>");
  }
  } else {
	  out.print("请输入要清除缓存的服务！");
  }
%>