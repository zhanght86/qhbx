<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--login.jsp 门户登录页面-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Random"%>
<%@ page import="com.rh.core.org.mgr.UserMgr" %>
<%@ page import="com.rh.core.util.RequestUtils" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.rh.core.base.Context" %>
<%@ page import="com.rh.core.comm.ConfMgr" %>
<%@ page import="com.rh.core.org.UserBean" %>
<%@ page import="com.rh.core.util.Lang" %>

<%
    final String CONTEXT_PATH = request.getContextPath();

	response.setHeader("Pragma","no-cache"); 
	response.setHeader("Cache-Control","no-cache"); 
	response.setDateHeader("Expires", 0); 
	
	Random rand = new Random();
	long randnum = Math.abs(rand.nextLong());
	session.setAttribute("RandNum", String.valueOf(randnum));
	String loginType = RequestUtils.get(request, "loginType", "key");

%>	
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>首创股份门户系统</title>
    <!-- 引用公用头部资源文件：开始 -->
    <%@ include file= "../sy/base/view/inHeader.jsp" %>
    <link rel="apple-touch-icon-precomposed" href="../apple-touch-icon-precompose.png"/>
    <link rel="shortcut icon" href="../favicon.ico"/>
    <link rel="stylesheet" type="text/css" href="login_dialog.css" charset="UTF-8"/>
    <link rel="stylesheet" type="text/css" href="login.css" charset="UTF-8"/>
    <script type="text/javascript" src="/login/js/jquery.easing.1.3.js"></script>
</head>
<body style="padding-top:0;">
<object style="width:0;height:0" classid="CLSID:F83A15A2-BAD8-465E-85C4-74ACB165924C" codebase="SafeCtrl.CAB#version=3,1,0,1" ID="SafeCtrl" width=0 height=0 style="display:none;"></object>

<script type="text/javascript" src="/login/js/method.js"></script> 
<script type="text/javascript" src="/login/js/login.js"></script>
<%
	if(userBean != null) {
%>
		<script type="text/javascript">
		var homeUrl = "/sy/comm/page/page.jsp";
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
        	<a href="http://www.capitalwater.cn" target="_blank" title="北京首创股份有限公司" tabindex="-1">
            	<img src="img/login-logo.png" alt="北京首创股份有限公司" />
            </a>
        </div>
        <div class="login-header-right"></div>
    </div>
    <!--<div id="msg">-->
    <div class="login-container">
    	<div class="login-main">
        	<div class="login-section">
            	<div class="login-tab-wrapper">
                    <div class="tab selected">
                        <ul>
                            <li class="item current" id="tab-ukey" data-target="ukey-form">
                                 <div class="login-tab-ukey">
                                     UKey登录
                                 </div>
                            </li>
                            <li>
                                <span class="login-separater"></span>
                            </li>
                            <li class="item " id="tab-cert" data-target="cert-form">
                                 <div class="login-tab-cert">
                                                                       证书登录
                                 </div>
                            </li>
                        </ul>
                    </div>
                    <div class="tab" id="ukey-m-tab" style="display:none;">
                        <ul>
                            <li class="item current" data-target="ukey-m-form">
                                 <div class="login-tab-ukey">
                                     Ukey修改密码
                                 </div>
                            </li>
                            <li class="">
                                 <div class="login-tab-ukey">
                                     <a href="javascript:void(0);" class="back-login" data-ref="tab-ukey" tabindex="-1">返回登录页面</a>
                                 </div>
                            </li>
                        </ul>
                    </div>
                    <div class="tab" id="cert-m-tab" style="display:none;">
                        <ul>
                            <li class="item current" data-target="cert-m-form">
                                 <div class="login-tab-ukey">
                                     Cert修改密码
                                 </div>
                            </li>
                            <li>
                                 <div class="login-tab-ukey">
                                     <a href="javascript:void(0);" class="back-login" data-ref="tab-cert" tabindex="-1">返回登录页面</a>
                                 </div>
                            </li>
                        </ul>
                    </div>
                </div>
                <!-------------end of login-tab----------------->
                <div class="login-form-wrapper">
                 	<form id="ukey-form" class="ukey-form active" action="http://caadmin.capitalwater.cn/ekeyLogin.do" method="post">
                    	<input type="hidden" name="randnum" value="<%=randnum%>" />
		                <input type="hidden" name="serverCert" value="MIIEKTCCA5KgAwIBAgIDEUUgMA0GCSqGSIb3DQEBBQUAMIGGMQswCQYDVQQGEwJDTjELMAkGA1UECBMCQkoxEDAOBgNVBAcTB0JlaWppbmcxDTALBgNVBAoTBFNDR0YxDTALBgNVBAsTBFNDR0YxIzAhBgkqhkiG9w0BCQEWFHNjZ2ZAY2FwaXRhbHdhdGVyLmNuMRUwEwYDVQQDEwxTQ0dGIFJPT1QgQ0EwHhcNMDkxMjE4MDkyNzE3WhcNMTkxMjE2MDkyNzE3WjAqMQswCQYDVQQGEwJDTjEbMBkGA1UEAxMSY2EuY2FwaXRhbHdhdGVyLmNuMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGS5Xzo8KLW6fxeUwZ2xKvuRePi+oMBcdIdo2FaxXwDS0Oj4+wmzBRXPSDgjzlHsdRudCMiOHGnI7gd/cgzB5AaP3fLkd2TFM80wyc0d4AFTZiMVA1Xo+kRlGbrR4PuDnw5KzgqGxH8wwcFBuLqRILt9EOXo6uCwRcwnH19WA1hwIDAQABo4IB/jCCAfowCwYDVR0PBAQDAgP4MEcGA1UdJQRAMD4GCCsGAQUFBwMBBggrBgEFBQcDAgYKKwYBBAGCNxQCAgYIKwYBBQUHAwUGCCsGAQUFBwMGBggrBgEFBQcDBzAJBgNVHRMEAjAAMEEGCCsGAQUFBwEBBDUwMzAxBggrBgEFBQcwAYYlaHR0cHM6Ly8xMjcuMC4wLjE6ODQ0My9ldGNhL2luZGV4Lmh0bTAdBgNVHQ4EFgQUWJ8yaxuSxBHn2pTD4r4YPW/sYYswgbsGA1UdIwSBszCBsIAUhOw/O+Q+sWz47CMEtjKTbMwcdLGhgYykgYkwgYYxCzAJBgNVBAYTAkNOMQswCQYDVQQIEwJCSjEQMA4GA1UEBxMHQmVpamluZzENMAsGA1UEChMEU0NHRjENMAsGA1UECxMEU0NHRjEjMCEGCSqGSIb3DQEJARYUc2NnZkBjYXBpdGFsd2F0ZXIuY24xFTATBgNVBAMTDFNDR0YgUk9PVCBDQYIJALH7r2h18z0FMBsGA1UdEQQUMBKgEAYKKwYBBAGCNxQCA6ACDAAwCQYDVR0SBAIwADA8BgNVHR8ENTAzMDGgL6AthitodHRwczovLzEyNy4wLjAuMTo4NDQzL2V0Y2EvY2VydHNydi9DUkwuY3JsMBEGCWCGSAGG+EIBAQQEAwIE8DANBgkqhkiG9w0BAQUFAAOBgQCjVxe1fb7BOo7QR+F3HJDnX8sMc9P9hhyNxncLh8KjJ7nasr843vqaSfwaV+rtDro2uBZ7dYmX6nTlK1HLzK8Y+9IjEpZJLm7pIQW0f2iwHMUngNrjmNcjAVjmk1PK6YaHABhQg+2DL98QO5gnuBtzMUrB39ry6sOZsWruO+0C2A==" />
		                <input type="hidden" name="authType" value="1" />
		                <input type="hidden" name="appId" value="201"/>
		                <input type="hidden" name="userPIN" value="" />
                    	<div class="name-block nomal-block">
                        	<label for="ukey_form_cmpyName" class="tips">公司名称</label>
                        	<input type="text" class="nomal-input" name="cmpyName" id="ukey_form_cmpyName" tabindex="1" onpaste="return false;" />
                            <span class="name-icon"></span>
                            <input name="cmpyCode" class="cmpy_code" type="hidden" />
                        </div>
                        <div class="pwd-block mt30 nomal-block">
                        	<label for="ukey_form_pwd"  class="tips">密码</label>
                        	<input type="password" class="nomal-input" name="cipher" id="ukey_form_pwd" tabindex="2"/>
                        </div>
                        <div class="btn-group">
                    		 <input type="submit" class="btn btn-fl" value="登录" tabindex="3"/>
                   		</div>
                        <div class="form-bottom">
                        	<div class="bottom-half">
                                <a href="javascript:void(0);"  class="test-tab"  tabindex="4">
                                    <em></em>
                                    <span>系统插件测试页</span>
                                </a>
                            </div>
                            <div class="bottom-half">
                                <a href="javascript:void(0);" class="link-tab" data-target="ukey-m-tab" tabindex="5">
                                    <em></em>
                                    <span>修改UKey登录密码</span>
                                </a>
                            </div>
                        </div>
                    </form>
                    <!-------------end of login-ukey-form----------------->
                    <form id="cert-form" class="cert-form" action="http://caadmin.capitalwater.cn/certFileLogin.do" method="post"  style="display:none;" >
                    	<input type="hidden" name="userPIN" value="" />
                        <input type="hidden" name="randnum" value="<%=randnum%>" />
		                <input type="hidden" name="serverCert" value="MIIEKTCCA5KgAwIBAgIDEUUgMA0GCSqGSIb3DQEBBQUAMIGGMQswCQYDVQQGEwJDTjELMAkGA1UECBMCQkoxEDAOBgNVBAcTB0JlaWppbmcxDTALBgNVBAoTBFNDR0YxDTALBgNVBAsTBFNDR0YxIzAhBgkqhkiG9w0BCQEWFHNjZ2ZAY2FwaXRhbHdhdGVyLmNuMRUwEwYDVQQDEwxTQ0dGIFJPT1QgQ0EwHhcNMDkxMjE4MDkyNzE3WhcNMTkxMjE2MDkyNzE3WjAqMQswCQYDVQQGEwJDTjEbMBkGA1UEAxMSY2EuY2FwaXRhbHdhdGVyLmNuMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGS5Xzo8KLW6fxeUwZ2xKvuRePi+oMBcdIdo2FaxXwDS0Oj4+wmzBRXPSDgjzlHsdRudCMiOHGnI7gd/cgzB5AaP3fLkd2TFM80wyc0d4AFTZiMVA1Xo+kRlGbrR4PuDnw5KzgqGxH8wwcFBuLqRILt9EOXo6uCwRcwnH19WA1hwIDAQABo4IB/jCCAfowCwYDVR0PBAQDAgP4MEcGA1UdJQRAMD4GCCsGAQUFBwMBBggrBgEFBQcDAgYKKwYBBAGCNxQCAgYIKwYBBQUHAwUGCCsGAQUFBwMGBggrBgEFBQcDBzAJBgNVHRMEAjAAMEEGCCsGAQUFBwEBBDUwMzAxBggrBgEFBQcwAYYlaHR0cHM6Ly8xMjcuMC4wLjE6ODQ0My9ldGNhL2luZGV4Lmh0bTAdBgNVHQ4EFgQUWJ8yaxuSxBHn2pTD4r4YPW/sYYswgbsGA1UdIwSBszCBsIAUhOw/O+Q+sWz47CMEtjKTbMwcdLGhgYykgYkwgYYxCzAJBgNVBAYTAkNOMQswCQYDVQQIEwJCSjEQMA4GA1UEBxMHQmVpamluZzENMAsGA1UEChMEU0NHRjENMAsGA1UECxMEU0NHRjEjMCEGCSqGSIb3DQEJARYUc2NnZkBjYXBpdGFsd2F0ZXIuY24xFTATBgNVBAMTDFNDR0YgUk9PVCBDQYIJALH7r2h18z0FMBsGA1UdEQQUMBKgEAYKKwYBBAGCNxQCA6ACDAAwCQYDVR0SBAIwADA8BgNVHR8ENTAzMDGgL6AthitodHRwczovLzEyNy4wLjAuMTo4NDQzL2V0Y2EvY2VydHNydi9DUkwuY3JsMBEGCWCGSAGG+EIBAQQEAwIE8DANBgkqhkiG9w0BAQUFAAOBgQCjVxe1fb7BOo7QR+F3HJDnX8sMc9P9hhyNxncLh8KjJ7nasr843vqaSfwaV+rtDro2uBZ7dYmX6nTlK1HLzK8Y+9IjEpZJLm7pIQW0f2iwHMUngNrjmNcjAVjmk1PK6YaHABhQg+2DL98QO5gnuBtzMUrB39ry6sOZsWruO+0C2A==" />
		                <input type="hidden" name="authType" value="1" />
		                <input type="hidden" name="appId" value="201"/>
		                
                    	<div class="name-block nomal-block">
                        	<label for="cert_form_cmpyName" class="tips">公司名称</label>
                        	<input type="text" class="nomal-input" name="cmpyName"  id="cert_form_cmpyName" onpaste="return false;" tabindex="1"/>
                            <span class="name-icon"></span>
                            <input name="cmpyCode" class="cmpy_code" type="hidden" />
                        </div>
                        <div class="file-block mt15 nomal-block">
                        	<label for="cert_form_tempfile" class="tips">证书文件</label>
                        	<input type="text" class="nomal-input" name="tempfile" id="cert_form_tempfile" tabindex="2" />
                            <a href="javascript:void(0);" tabindex="3">选择文件
                            	<input type="file" name="SAFE_File_CONTENT" class="file"/>
                            </a>
                        </div>
                        <div class="pwd-block mt15 nomal-block">
                        	<label for="cert_form_pwd"  class="tips">密码</label>
                        	<input type="password" class="nomal-input" name="cipher" id="cert_form_pwd" tabindex="4"/>
                        </div>
                        <div class="btn-group">
                    		 <input type="submit" class="btn btn-fl" value="登录" tabindex="5"/>
                   		</div>
                        <div class="form-bottom">
                       	   <div class="bottom-half">
                                <a href="javascript:void(0);" class="test-tab"  tabindex="6">
                                    <em></em>
                                    <span>系统插件测试页</span>
                                </a>
                           </div>
                           <div class="bottom-half">
                                <a href="javascript:void(0);" class="link-tab" data-target="cert-m-tab" tabindex="6">
                                    <em></em>
                                    <span>修改证书密码</span>
                                </a>
                            </div>
                        </div>
                    </form>
                   <!-------------end of login-cert-form----------------->
                   <form id="ukey-m-form" class="ukey-m-form" action="" method="post" style="display:none;">
                        <div class="pwd-block mt15 nomal-block">
                        	<label for="ukey-m-form_oldPwd" class="tips">旧密码</label>
                        	<input type="password" class="nomal-input" name="SAFE_PLUGIN_CHANGEPASSWORD_OLD" id="ukey_m_form_oldPwd" maxlength="20" tabindex="1"/>
                        </div>
                        <div class="pwd-block mt15 nomal-block">
                        	<label for="ukey_m_form_oldPwd"  class="tips">新密码</label>
                        	<input type="password" class="nomal-input js-newPwd" name="SAFE_PLUGIN_CHANGEPASSWORD_NEW1" id="ukey_m_form_newPwd" maxlength="20" tabindex="2"/>
                        </div>
                        <div class="pwd-block mt15 nomal-block">
                        	<label for="ukey_m_form_confirmPwd"  class="tips">确认密码</label>
                        	<input type="password" class="nomal-input js-confirmPwd" name="SAFE_PLUGIN_CHANGEPASSWORD_NEW2" id="ukey_m_form_confirmPwd" maxlength="20" tabindex="3"/>
                        </div>
                        <div class="btn-group">
                    		 <input type="submit" class="btn btn-fl" value="提交" tabindex="4"/>
                             <input type="reset"  class="btn btn-fr" value="重置" tabindex="5"/>
                   		</div>
                    </form>
                   <!-------------end of login-cert-form----------------->
                   <form id="cert-m-form" class="cert-m-form" action="" method="post" style="display:none;">
		                <input type="hidden" name="userPIN" value="" />
                        <input type="hidden" name="randnum" value="<%=randnum%>" />
		                <input type="hidden" name="cert" value="MIIEKTCCA5KgAwIBAgIDEUUgMA0GCSqGSIb3DQEBBQUAMIGGMQswCQYDVQQGEwJDTjELMAkGA1UECBMCQkoxEDAOBgNVBAcTB0JlaWppbmcxDTALBgNVBAoTBFNDR0YxDTALBgNVBAsTBFNDR0YxIzAhBgkqhkiG9w0BCQEWFHNjZ2ZAY2FwaXRhbHdhdGVyLmNuMRUwEwYDVQQDEwxTQ0dGIFJPT1QgQ0EwHhcNMDkxMjE4MDkyNzE3WhcNMTkxMjE2MDkyNzE3WjAqMQswCQYDVQQGEwJDTjEbMBkGA1UEAxMSY2EuY2FwaXRhbHdhdGVyLmNuMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGS5Xzo8KLW6fxeUwZ2xKvuRePi+oMBcdIdo2FaxXwDS0Oj4+wmzBRXPSDgjzlHsdRudCMiOHGnI7gd/cgzB5AaP3fLkd2TFM80wyc0d4AFTZiMVA1Xo+kRlGbrR4PuDnw5KzgqGxH8wwcFBuLqRILt9EOXo6uCwRcwnH19WA1hwIDAQABo4IB/jCCAfowCwYDVR0PBAQDAgP4MEcGA1UdJQRAMD4GCCsGAQUFBwMBBggrBgEFBQcDAgYKKwYBBAGCNxQCAgYIKwYBBQUHAwUGCCsGAQUFBwMGBggrBgEFBQcDBzAJBgNVHRMEAjAAMEEGCCsGAQUFBwEBBDUwMzAxBggrBgEFBQcwAYYlaHR0cHM6Ly8xMjcuMC4wLjE6ODQ0My9ldGNhL2luZGV4Lmh0bTAdBgNVHQ4EFgQUWJ8yaxuSxBHn2pTD4r4YPW/sYYswgbsGA1UdIwSBszCBsIAUhOw/O+Q+sWz47CMEtjKTbMwcdLGhgYykgYkwgYYxCzAJBgNVBAYTAkNOMQswCQYDVQQIEwJCSjEQMA4GA1UEBxMHQmVpamluZzENMAsGA1UEChMEU0NHRjENMAsGA1UECxMEU0NHRjEjMCEGCSqGSIb3DQEJARYUc2NnZkBjYXBpdGFsd2F0ZXIuY24xFTATBgNVBAMTDFNDR0YgUk9PVCBDQYIJALH7r2h18z0FMBsGA1UdEQQUMBKgEAYKKwYBBAGCNxQCA6ACDAAwCQYDVR0SBAIwADA8BgNVHR8ENTAzMDGgL6AthitodHRwczovLzEyNy4wLjAuMTo4NDQzL2V0Y2EvY2VydHNydi9DUkwuY3JsMBEGCWCGSAGG+EIBAQQEAwIE8DANBgkqhkiG9w0BAQUFAAOBgQCjVxe1fb7BOo7QR+F3HJDnX8sMc9P9hhyNxncLh8KjJ7nasr843vqaSfwaV+rtDro2uBZ7dYmX6nTlK1HLzK8Y+9IjEpZJLm7pIQW0f2iwHMUngNrjmNcjAVjmk1PK6YaHABhQg+2DL98QO5gnuBtzMUrB39ry6sOZsWruO+0C2A==" />
		                <input type="hidden" name="authType" value="certfile" />
		                <input type="hidden" name="appId" value="189"/>
                        <div class="file-block mt15 nomal-block">
                        	<label for="cert_m_form_tempfile" class="tips">证书文件</label>
                        	<input type="text" class="nomal-input" name="tempfile" id="cert_m_form_tempfile" tabindex="1" />
                            <a href="javascript:void(0);" tabindex="2">选择文件
                            	<input type="file" name="p12certfile" class="file"/>
                            </a>
                        </div>
                        <div class="pwd-block mt15 nomal-block">
                        	<label for="cert_m_form_oldPwd"  class="tips">旧密码</label>
                        	<input type="password" class="nomal-input" name="oldpass" id="cert_m_form_oldPwd" maxlength="20" tabindex="3"/>
                        </div>
                        <div class="pwd-block mt15 nomal-block">
                        	<label for="cert_m_form_oldPwd"  class="tips">新密码</label>
                        	<input type="password" class="nomal-input js-newPwd" name="newpass" id="cert_m_form_newPwd" maxlength="20" tabindex="4"/>
                        </div>
                        <div class="pwd-block mt15 nomal-block">
                        	<label for="cert_m_form_oldPwd"  class="tips">确认密码</label>
                        	<input type="password" class="nomal-input js-confirmPwd" name="newpass2" id="cert_m_form_confirmPwd" maxlength="20" tabindex="5"/>
                        </div>
                        <div class="btn-group">
                    		 <input type="submit" class="btn btn-fl" value="提交" tabindex="6"/>
                             <input type="reset"  class="btn btn-fr" value="重置" tabindex="7"/>
                   		</div>
                    </form>
                   <!-------------end of login-cert-form----------------->
                </div>
                <!-------------end of login-form----------------->
            </div>
        </div>
    </div>
     
    <!--<div id="progess"></div>-->
    <div class="login-footer">
        <p>Copyright &copy;版权所有 <a href="http://www.capitalwater.cn" target="_blank" tabindex="-1">北京首创股份有限公司</a></p>
    </div>
</div>
<div id="progess"></div>
 
<%
 }
 %>
</body>

</html>

