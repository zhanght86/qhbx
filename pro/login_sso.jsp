<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--index.jsp 平台登录页面-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.rh.core.base.Context"%>

<%
    final String CONTEXT_PATH = request.getContextPath();
	%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>综合办公系统</title>
<link rel="apple-touch-icon-precomposed"
	href="/apple-touch-icon-precompose.png" />
<link rel="shortcut icon" href="favicon.ico" />

<link rel="stylesheet" type="text/css"
	href="<%=CONTEXT_PATH %>/sy/comm/index/incl-index.css" charset="UTF-8" />
<script type="text/javascript">
   var FireFlyContextPath = "<%=CONTEXT_PATH %>";//虚拟路径
</script>
<script type="text/javascript" src="<%=CONTEXT_PATH %>/sy/base/frame/jquery-1.8.2.min.js"></script>
<!-- 平台UI核心js库 -->
	<script type="text/javascript" src="<%=CONTEXT_PATH %>/sy/base/frame/platform.js"></script>
	<script type="text/javascript" src="<%=CONTEXT_PATH %>/sy/base/frame/tools.js"></script>
	<script type="text/javascript" src="<%=CONTEXT_PATH %>/sy/base/frame/constant.js"></script>
	<!-- 平台UI核心组件js库 -->
</head>
<body>
	<input id="userName" type="hidden" />
	<script type="text/javascript"> 
	    jQuery(function($) {
		resizeWindow();         
		    var homeUrl="";
				try{
			        var wshNetwork = new ActiveXObject("WScript.Network");
					if(wshNetwork.UserDomain=="QHR"&&(wshNetwork.UserName!=""|| wshNetwork.UserName!="null")){
						var userName = wshNetwork.UserName;
						if(userName.length > 0){
							var loginParam = {};
							loginParam["userName"] = userName;
							var loginData = FireFly.doAct("PT_AD_USER","toADLogin",loginParam);
							homeUrl = loginData.homeUrl;
						}
					} 
				}catch(e){
					homeUrl = "";
				}
				if (homeUrl.length > 0) {
					//设置cookie
					document.cookie="RhClientLogin=true";
					window.location.href = homeUrl;
				} else {
					homeUrl = FireFly.getContextPath() + "/index.jsp";
					window.location.href = homeUrl; 
				}
			
	    });
		function resizeWindow(){
    if (window.screen) {//判断浏览器是否支持window.screen判断浏览器是否支持screen     
      var myw = screen.availWidth;   //定义一个myw，接受到当前全屏的宽     
      var myh = screen.availHeight;  //定义一个myw，接受到当前全屏的高     
      window.moveTo(0, 0);           //把window放在左上脚     
      window.resizeTo(myw, myh);     //把当前窗体的长宽跳转为myw和myh     
	  flag =2;
    } 
}
	</script>
</body>
</html>

