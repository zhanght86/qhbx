<%@ page language="java" contentType="text/html;charset=GBK"%>
<%@ page import = "com.sinosoft.common.*" %>
<%
response.setHeader("Cache-Control","No-Cache");//HTTP 1.1
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
String hotmenuName="";
String channelName="客户登录";
String channelUrl=request.getRequestURL().toString()==null? "" : request.getRequestURL().toString().trim();
String URL = Data.filterStr(request.getParameter("URL") == null? "":request.getParameter("URL").toString());
if(!Data.hasValue(URL)){
	URL = request.getContextPath()+"/personal/onlineService/policyService/index.jsp";
}
String message = Data.filterStr(request.getParameter("msg") == null? "":request.getParameter("msg").toString());
if("randerror".equals(message))message = "你输入的验证码不正确";
else if("nouser".equals(message))message = "你输入的客户不存在";
else if("passworderror".equals(message))message = "你输入的密码不正确";
else if("loginFail".equals(message))message = "登录失败";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<html xml:lang="en" xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<meta name="keywords" content="百年人寿保险股份有限公司,百年人寿,百年">
		<title>百年人寿</title>
		<link href="<%=request.getContextPath()%>/bn/cms/global/css/aeon.css" rel="stylesheet" type="text/css" />
		<link href="<%=request.getContextPath()%>/bn/cms/global/css/info_and_login.css" rel="stylesheet" type="text/css" />
		<!--[if lt IE 7]>
		<script src="<%=request.getContextPath()%>/global/js/IE8.js" type="text/javascript"></script>
		<![endif]-->
		
<script type="text/javascript" src="<%=request.getContextPath()%>/bn/cms/global/js/ajax.js"></script>
<script type="text/javascript">
function verify(){
	var AccountType = document.getElementById("AccountType").value;
	var UserID = document.getElementById("UserID").value;
	var Password = document.getElementById("password").value;
	var Rand = document.getElementById("Rand").value;
	if(UserID == ""){
		alert("请输入帐户号码！");
		document.all.UserID.focus();
		changeImage();
		return false;
	}
	if(Password == ""){
		alert("请输入密码！");
		document.all.password.focus();
		changeImage();
		return false;
	}
	if(Rand == ""){
		alert("请输入验证码！");
		document.all.Rand.focus();
		changeImage();
		return false;
				}
	var ajaxUrl ="login.jsp";
	var ajaxPostValue = "AccountType="+AccountType+"&UserID="+UserID+"&Password="+Password+"&Rand="+Rand;
	var rCode = doPostAjaxRequestMore(ajaxUrl,ajaxPostValue).replace(/(^\s*)|(\s*$)/g, "");
	if(rCode =="error"){
		alert("网络异常");
		changeImage();
	}else if(rCode =="correct"){
	    changeImage();
	    //个险登录成功后，进入页面
		//window..location.href="<%=request.getContextPath()%>/personal/onlineService/policyService/index.jsp";
		//alert('<%=URL%>');
		document.location.href="lawMes.jsp?url="+'<%=URL%>';
		//document.location.href="<%=URL%>";
		return true;
	}else if(rCode =="GroupCorrect"){
	    changeImage();
	    //团险账号登录成功后，进入页面
		document.location.href="<%=request.getContextPath()%>/group/onlineService/policyService/index.jsp";
		return true;
	}else{
		alert(rCode);
		changeImage();
	}
	return false;
}
function goRegister(){
	window.top.location.href = "registration/index.jsp";
}
function keydown(eve){
	var isie = (document.all) ? true:false;
	if(eve!=null){
		event = eve;
	}
	if (event.keyCode==13){
		verify();
	}
}
var ua = navigator.userAgent;
var ps = navigator.productSub;
var dom = (document.getElementById)? 1:0
var ie4 = (document.all&&!dom)? 1:0
var ie5 = (document.all&&dom)? 1:0
var nn4 =(navigator.appName.toLowerCase() == "netscape" && parseInt(navigator.appVersion) == 4)
var nn6 = (dom&&!ie5)? 1:0
var sNav = (nn4||nn6||ie4||ie5)? 1:0
if(sNav) {
	document.onkeydown = keydown;
	if(nn4) {
		document.onkeydown = keydown(Event.KEYDOWN);
		document.captureEvents(Event.KEYDOWN) 
	}
}
var id = 0;
function changeImage(){
	id = 2 - id; 
	document.getElementById("mFrame").src="<%=request.getContextPath()%>/passport/registration/image.jsp?id="+id;
	id = id -1;
}
function Sesrch(){
var AccountType=document.getElementById("AccountType").value;
if(AccountType=='3'){
var time =new Date();
window.showModalDialog("../personal/gclaimSearch/SearchClaimNo/index.jsp?time="+time,window,"close:1;dialogheight:700px;dialogwidth:1000px;status:yes;center:yes;help:off;scroll:yes");		
document.getElementById('AccountType').value ='0';	
}	

}
</script>
	</head>
	<body>
		<div id="AllHtml">
			<div id="CenterHtml">
				<div id="head_channel_menu"><jsp:include page="/personal/head.jsp" flush="true" /></div>
				<div id="channelHtml">
					<div id="channel_content">
						<div id="login_height50"></div>
						<div id="login_showName_2">
							<img src="<%=request.getContextPath()%>/bn/cms/global/images/showName_login.jpg "/>
						</div>
						<div id="login_content">
							<form name="frmInput" id="frmInput" method="post" action="login.jsp">
							<table width="100%" height="100" cellpadding="0" cellspacing="0" border="0" align="center" style="font-size:9pt">
							<tr>
								<td align="center" width="100%">
									<table style="background-repeat:no-repeat;" width="525" height="100%" cellpadding="0" cellspacing="0" border="0" style="font-size:9pt">
										<tr>
											<td height="50" colspan="3"></td>
										</tr>
										<tr>
											<td height="38" width="8"><img src="<%=request.getContextPath()%>/global/images/login_left_top.jpg"/></td>
											<td width="504" align="left" style="background:url(<%=request.getContextPath()%>/global/images/login_bg_top.jpg) repeat-x;"><img src="<%=request.getContextPath()%>/global/images/login_yhdl.jpg"/></td>
											<td width="11"><img src="<%=request.getContextPath()%>/global/images/login_right_top.jpg"/></td>
										</tr>
										<tr>
											<td colspan="3" height="206" class="login_img">
												<table align="left" width="400" height="100%" cellpadding="0" cellspacing="0" border="0" style="font-size:9pt">
												<%
												if (!message.equals("")) {
												%>
													<tr>
														<td height="30" colspan="2" align="left" valign="middle"><font color=red><%=message%></font></td>
													</tr>
												<%
												} else {
												%>
													<tr>
														<td height="30" colspan="2">&nbsp;</td>
													</tr>
												<%
												}
												%>
													<tr>
														<td class="log_tdleft" width="100">帐户类型:</td>
														<td width="300" class="log_tdright">
															<table width="126" cellpadding="0" cellspacing="0" border="0" align="left">
																<tr>
																	<td align="left">
															            <select name="AccountType" id="AccountType" onchange="Sesrch()" style="color:#737373;width:128px;">
																			 <option value="0">登录帐号</option>
																			<option value="1">证件号</option>
																		 	<option value="2">百年一保通号</option>
																			<option value="3">案件号</option>
																			<!--  <option value="3">团体保单号</option>-->
																		</select>
																	</td>
																</tr>
															</table>
														</td>
													<tr>
													<tr>
														<td class="log_tdleft"><font style="font-size:9pt;color:#737373;" valign="middle">帐户号码:</font></td>
														<td><input name="UserID" id="UserID" value="" size=15 maxlength=50 class="input_log"></td>
													<tr>
													<tr>
														<td class="log_tdleft">密&nbsp;&nbsp;&nbsp;&nbsp;码:</td>
														<td><input name="password" id="password" type="password" size=15 maxlength=50 class="input_log"></td>
													</tr>
													<tr >
														<td class="log_tdleft">验&nbsp;证&nbsp;码:</td>
														<td align="left">	
															<table cellpadding="0" cellspacing="0" border="0" align="left">
																<tr>
																	<td align="left"><img alt="点击更换验证码" style="cursor:pointer;" id="mFrame" name="mFrame" src="<%=request.getContextPath()%>/passport/registration/image.jsp" onclick="changeImage();"></td>
																	<td>&nbsp;&nbsp;<input name="Rand" id="Rand" size=5 maxlength=4 value="" class="input_log_short"></td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td colspan=2 height="17"></td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td height="70" width="8"><img src="<%=request.getContextPath()%>/global/images/login_left_bottom.jpg"/></td>
											<td width="504" style="vertical-align:top;padding-top:17px;background:url(<%=request.getContextPath()%>/global/images/login_bg_bottom.jpg) repeat-x;">
												<table cellpadding="0" cellspacing="0" border="0">
													<tr>
														<td width="200"><span style="cursor:pointer" onclick="window.open('help.jsp','','fullscreen=0,height=680,width=705,top=100,left=100, scrollbars=no,resizable=1')">不清楚注册流程？点这里</span></td>
														<td class="btn_com" onclick="javascript:goRegister()">&nbsp;注册&nbsp;</td>
														<td>&nbsp;&nbsp;&nbsp;</td>
														<td onclick="javascript:verify();" class="btn_com">&nbsp;登录&nbsp;</td>
														<td>&nbsp;&nbsp;&nbsp;</td>
														<td class="btn_com"><a style="color:white;text-decoration:none;" href="<%=request.getContextPath()%>/passport/getPassword/">找回密码</a></td>
													</tr>
												</table>
											
											</td>
											<td width="11"><img src="<%=request.getContextPath()%>/global/images/login_right_bottom.jpg"/></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td height="100%">&nbsp;</td>
							</tr>
							</table>	
							<input name="URL" value="<%=URL %>" style="display:none">
							</form>
						</div>
						<div id="channel_return"></div>
					</div>
				</div>
				<div id="copyright">
					<jsp:include page="/copyright.jsp" flush="true" />
				</div>
			</div>
		</div>
	</body>
</html>
<script type="text/javascript">
	window.frmInput.UserID.focus();	
</script>