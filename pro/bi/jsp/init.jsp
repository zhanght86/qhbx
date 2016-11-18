<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.rh.core.base.Context" %>
<%@ page import="com.rh.core.org.UserBean" %>
<%@ page import="com.rh.core.base.Bean"%>
<%@ page import="com.rh.core.serv.ServDao"%>
<%@ page import="com.aeon.bi.bean.BiAppTreeView" %>
<%@ page import="com.aeon.bi.business.Bi" %>
<%@ page import="com.aeon.bi.util.Util" %>
<%@ page import="com.aeon.bi.util.Const" %>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<% 
	request.setCharacterEncoding("UTF-8");
	Context.setRequest(request);
	String urlPath = request.getContextPath();
    UserBean userBean = Context.getUserBean(request);
%>
<meta name="servName" content="<%=System.getProperty("servName")%>"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<script type="text/javascript" src="<%=urlPath %>/sy/base/frame/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="<%=urlPath %>/bi/js/bi.js"></script>