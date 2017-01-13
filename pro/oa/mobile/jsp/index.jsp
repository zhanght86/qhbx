<!DOCTYPE html>
<!--login.jsp 登录页面-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Random"%>
<%@ page import="com.rh.core.org.mgr.UserMgr" %>
<%@ page import="com.rh.core.util.RequestUtils" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.rh.core.base.Context" %>
<%@ page import="com.rh.core.comm.ConfMgr" %>
<%@ page import="com.rh.core.org.UserBean" %>
<%@ page import="com.rh.core.util.Lang" %>
<%@ page import="com.rh.core.comm.loginbkg.*" %>


<%
    final String CONTEXT_PATH = request.getContextPath();
	String jumpTo = RequestUtils.get(request,"jumpTo","portal");
	response.setHeader("Pragma","no-cache"); 
	response.setHeader("Cache-Control","no-cache"); 
	response.setDateHeader("Expires", 0); 
	
	Random rand = new Random();
	long randnum = Math.abs(rand.nextLong());
	session.setAttribute("RandNum", String.valueOf(randnum));
	String loginType = RequestUtils.get(request, "loginType", "key");
	if(Context.getUserBean(request)!=null){
		Context.getUserBean(request).set("hasOaFlag", "");
	}
	Bean bkgBean = null;//bkgServ.getLoginBkgPic();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>个人门户-登录</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <!-- 引用公用头部资源文件：开始 -->
    <%@ include file= "/sy/base/view/inHeader-mb.jsp" %>
    <link rel="apple-touch-icon-precomposed" href="/apple-touch-icon-precompose.png"/>
    <link rel="shortcut icon" href="favicon.ico"/>
    <link rel="stylesheet" type="text/css" href="/login/login_dialog.css" charset="UTF-8"/>
    <link rel="stylesheet" type="text/css" href="/login/login_mobile.css?v=1" charset="UTF-8"/>
    <script type="text/javascript" src="/login/js/jquery.easing.1.3.js"></script>
</head>
<body style="padding-top:0px;">
<!--script type="text/javascript" src="/login/js/method.js"></script--> 
<script type="text/javascript" src="/login/js/login.js"></script>
<script type="text/javascript" src="/sy/comm/index/incl-index-mobile.js"></script>
<%
	if(userBean != null) {
%>
		<script type="text/javascript">
		var homeUrl = "/oa/mobile/jsp/main.jsp";
		//设置cookie
		document.cookie="RhClientLogin=true";
		window.location.href = homeUrl;
		</script>	
<%
    } else {
%>

<div class="login-wrapper">
    <div class="login-header">
        <div class="login-header-left">
        	<a href="#" target="_blank" title="前海再保险股份有限公司" tabindex="-1">
            	<img src="/login/img/login-logo.png" alt="前海再保险股份有限公司" />
            </a>
        </div>
        <div ></div>
    </div>
    <!--<div id="msg">-->
    <%	
    	String pic = "";
    	String bannerPic = "";
    	String dyStyle = "";
    
    	if("".equals(pic)){
    	Context.setApp("bannerBkgPic","");
    %>
    <div class="login-container">
    <%}else{ %>
    <script>
	    if('<%=bannerPic%>'.length!=0){
	    	System.setBannerBkgUrl('<%=CONTEXT_PATH+"/file/"+bannerPic %>');
	    }else{
	    	System.setBannerBkgUrl('');
	    };
	    if('<%=dyStyle%>'.length!=0){
	    	System.setBannerDyStyle('<%=dyStyle%>');
	    }else{
	    	System.setBannerDyStyle('');
	    }
	    
    </script>
    <div class="login-container" style="background:transparent url('<%=CONTEXT_PATH+"/file/"+pic %>') no-repeat center center">
    <%} %>
    	<div class="login-main">
        	<div class="login-section">
                <div class="login-form-wrapper">
				<div id="msg" style=""></div>
                 	<form id="ukey-form" class="ukey-form active" action="return false" method="post">
                    	<input type="hidden" name="randnum" value="<%=randnum%>" />
		               
                    	<div class="name-block nomal-block">
                        	<label for="ukey_form_cmpyName" class="tips">用户名称</label>                    
							<input name="USER_CODE"  id="USER_CODE" class="nomal-input" type="text" value="" tabindex="2"/>
                        </div>
                        <div class="pwd-block mt30 nomal-block">
                        	<label for="ukey_form_pwd"  class="tips">密码</label>
                        	<input type="password" class="nomal-input" id="USER_PASSWORDS" name="USER_PASSWORDS"tabindex="3"/>
                        </div>
						<input name="cmpyCode" id="CMPY_CODE" class="cmpy_code" type="hidden" value="zhbx"/>
							<input name="CMPY_CODE__NAME" id="CMPY_CODE__NAME" class="cmpy_code" type="hidden" value="前海再保险"/>
						<input type="hidden" id="jumpTo" value="<%=jumpTo%>"/>
                        <div class="btn-group">
                    		 <input type="button" id="btnLogin" class="btn btn-fl" value="登录" tabindex="3"/><!--input id="LOGIN_AUTO" name="LOGIN_AUTO" type="checkbox" class="loginAuto-check"/><label class="loginAuto-lb">记住密码</label-->
                   		</div>
			
                  
       
                </div>
            </div>
        </div>
    </div>
</div>
 
<%
 }
 %>
<script>
$(function(){
    jQuery("#USER_CODE").keypress(function(e){
        if (e.keyCode == "13") {
            jQuery("#USER_PASSWORDS").focus();
        }
    });
    jQuery("#USER_PASSWORDS").keypress(function(e){
        if (e.keyCode == "13") {
            doLogin();
        }
    });
	jQuery(".login-container").css({"min-height":"490px","height":$(window).height()-110});
	
	
	
   });
</script>
</body>

</html>