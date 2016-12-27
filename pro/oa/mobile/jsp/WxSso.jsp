<%@ page language="java" contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8"%>
<%@ page import="com.rh.core.org.mgr.UserMgr" %>
<%@ page import="com.rh.core.org.UserBean" %>
<%@ page import="com.rh.core.util.Lang" %>
<%@ page import="com.rh.core.util.Strings" %>
<%
	String url = Context.getSyConf("WX_FIREFLY_PATH", "http://localhost:8009") + "/oa/mobile/jsp/main.jsp";
%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title>移动OA登录界面</title>
	<!-- 引用公用头部资源文件：开始 -->
    <%@ include file= "/sy/base/view/inHeader.jsp" %>
	<link rel="stylesheet" type="text/css" href="/oa/mobile/jsp/WxSso.css" charset="UTF-8"/>
	<script type="text/javascript" src="/oa/mobile/jsp/WxSso.js" charset="UTF-8"></script>
	
</head>
<body class="mobile">
	<div id="container">
		<div id="top">
			<div id="activeTest">
				<input type="hidden" value="<%=url%>" id="path_url" />
			</div>
		</div>
		<div id="logo"></div>
		<div id="content">
			<div class="nav">
				<ul class="tabContainer">
				</ul>
			</div>
			<form:form method="post" id="form" commandName="${commandName}" htmlEscape="true">
				<div id="errorMessage" class="errorMessage"></div>
				<form:errors path="*" class="errorMessage" element="div" name="errorMessage" />
				<input type="hidden" id="isMSG" name="MSG" value="false" />
				<div class="input">
					<span class="label">用户名：</span>
					<span class="inputCon"><input type="text" value="" placeholder="请输入用户登录名"  id="username" name="username" /></span>
				</div>
				<div class="input">
					<span class="label">密&nbsp;&nbsp;码：</span>
					<span class="inputCon"><input type="password" placeholder="请输入OA系统密码" id="password" name="_password_" /></span>
				</div>
				<input type="hidden" id="cmpycode" name="cmpycode" value="zhbx" />
				<div class="input">
					<span class="label"></span>
					<span class="inputCon">
						<input type="button" id="usual" value="登　录" onClick="doSubmit()" />
					</span>
				</div>
			</form:form> 
		</div>
		<div id="footer">
			<div id="bottom"></div>
		</div>
	</div>
</body>
</html>