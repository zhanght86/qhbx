<%@ page language="java" contentType="text/html;charset=GBK"%>
<%@ page import = "com.sinosoft.common.*" %>
<%
response.setHeader("Cache-Control","No-Cache");//HTTP 1.1
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
String type = Data.filterStr(request.getParameter("type") == null? "":request.getParameter("type").toString());
String URL = Data.filterStr(request.getParameter("URL") == null? "":request.getParameter("URL").toString());
if(!Data.hasValue(URL)){
	URL = request.getContextPath()+"/personal/index.jsp";
}
String message = Data.filterStr(request.getParameter("msg") == null? "":request.getParameter("msg").toString());
if("randerror".equals(message))message = "���������֤�벻��ȷ";
else if("nouser".equals(message))message = "������Ŀͻ�������";
else if("passworderror".equals(message))message = "����������벻��ȷ";
else if("loginFail".equals(message))message = "��¼ʧ��";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<html xml:lang="en" xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<meta name="keywords" content="�������ٱ��չɷ����޹�˾,��������,����">
		<title>��������</title>
		<link href="<%=request.getContextPath()%>/bn/cms/global/css/aeon.css" rel="stylesheet" type="text/css" />
		<link href="<%=request.getContextPath()%>/bn/cms/global/css/info_and_login.css" rel="stylesheet" type="text/css" />
		<!--[if lt IE 7]>
		<script src="<%=request.getContextPath()%>/global/js/IE8.js" type="text/javascript"></script>
		<![endif]-->
		
<script type="text/javascript" src="<%=request.getContextPath()%>/bn/cms/global/js/ajax.js"></script>
<script type="text/javascript">
function verify(){
    
	var LoginType = document.getElementById("LoginType").value;
	var AccountType = document.getElementById("AccountType").value;
	var UserID = document.getElementById("UserID").value;
	var Password = document.getElementById("password").value;
	var Rand = document.getElementById("Rand").value;
	var type = document.getElementById("type1").value;
	
	if(LoginType == ""){
		alert("��ѡ���½��ݣ�");
		document.all.UserID.focus();
		changeImage();
		return false;
	}
	if(UserID == ""){
		alert("�������ʻ����룡");
		document.all.UserID.focus();
		changeImage();
		return false;
	}
	if(Password == ""){
		alert("���������룡");
		document.all.password.focus();
		changeImage();
		return false;
	}
	if(Rand == ""){
		alert("��������֤�룡");
		document.all.Rand.focus();
		changeImage();
		return false;
	}
	if(AccountType=="1" && LoginType!="1"){
		alert("ֻ�е�¼����ǡ����ձ������ˡ����û�����ѡ�����֤���е�¼��");
		changeImage();
		return false;
	}
	var ajaxUrl ="login.jsp";
	var ajaxPostValue = "LoginType="+LoginType+"&AccountType="+AccountType+"&UserID="+UserID+"&Password="+Password+"&Rand="+Rand;
	var rCode = doPostAjaxRequestMore(ajaxUrl,ajaxPostValue).replace(/(^\s*)|(\s*$)/g, "");

	if(rCode =="error"){
		alert("�����쳣");
		changeImage();
	}else if(rCode =="correct"){
	    if("4" == LoginType){
			document.location.href="<%=request.getContextPath()%>/group/onlineService/policyService/index.jsp";
			return true;
	    }else{
	        if("1" == LoginType){
	            if("policy" == type){
	            	document.location.href="lawMes.jsp?url="+'<%=request.getContextPath()%>'+"/group/policySearch/index.jsp";
	            	return true;
	            }else if("claims" == type){
	            	document.location.href="lawMes.jsp?url="+'<%=request.getContextPath()%>'+"/group/claimsSearch/index.jsp";
	            	return true;
	            }else if("account" == type){
	            	document.location.href="lawMes.jsp?url="+'<%=request.getContextPath()%>'+"/group/accountSearch/index.jsp";
	            	return true;
	            }else{
	                document.location.href="lawMes.jsp?url="+'<%=URL%>';
	                return true;
	            }
	        }else if ("2" == LoginType){
	            if("policy" == type){
	            	document.location.href="lawMes.jsp?url="+'<%=request.getContextPath()%>'+"/group/companyPolicySearch/index.jsp";
	            	return true;
	            }else if("claims" == type){
	            	document.location.href="lawMes.jsp?url="+'<%=request.getContextPath()%>'+"/group/companyClaimsSearch/index.jsp";
	            	return true;
	            }else if("account" == type){
	            	document.location.href="lawMes.jsp?url="+'<%=request.getContextPath()%>'+"/group/companyAccountSearch/index.jsp";
	            	return true;
	            }else{
	                document.location.href="lawMes.jsp?url="+'<%=URL%>';
	                return true;
	            }
	        }else if ("3" == LoginType){
	            if("policy" == type){
	            	document.location.href="lawMes.jsp?url="+'<%=request.getContextPath()%>'+"/group/mediationPolicySearch/index.jsp";
	            	return true;
	            }else if("claims" == type){
	            	document.location.href="lawMes.jsp?url="+'<%=request.getContextPath()%>'+"/group/mediationClaimsSearch/index.jsp";
	            	return true;
	            }else if("account" == type){
	            	document.location.href="lawMes.jsp?url="+'<%=request.getContextPath()%>'+"/group/mediationAccountSearch/index.jsp";
	            	return true;
	            }else{
	                document.location.href="lawMes.jsp?url="+'<%=URL%>';
	                return true;
	            }
	        }
			return true;
		}
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
</script>
<style>
.explain{
    font-size:9pt;
	text-align:left;
}
</style>
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
							<input name="type1" id="type1" type="hidden" value="<%=type%>"/>
							<table width="100%" height="100" cellpadding="0" cellspacing="0" border="0" align="center" style="font-size:9pt">
							<tr>
							</tr>
							<tr>
								<td align="center" width="100%">
									<table style="background-repeat:no-repeat;" width="525" height="100%" cellpadding="0" cellspacing="0" border="0" style="font-size:9pt">
										<tr>
											<td height="50" class="explain" colspan="3">�����֤Ͷ���ġ����ձ������ˡ��û�������ѡ���˺����͡�Ϊ���֤���е�¼��</td>
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
														<td class="log_tdleft"><font style="font-size:9pt;color:#737373;" valign="middle">��½���:</font></td>
														<td width="300" class="log_tdright">
															<table width="126" cellpadding="0" cellspacing="0" border="0" align="left">
																<tr>
																	<td align="left">
															            <select name="LoginType" id="LoginType" style="color:#737373;width:128px;">
																			<option value="">��ѡ��</option>
																			<option value="1">���ձ�������</option>
																			<option value="2">Ͷ����λHR�û�</option>
																			<option value="3">�н�����û�</option>
																			<option value="4">���屣����</option>
																		</select>
																	</td>
																</tr>
															</table>
														</td>
													<tr>
													<tr>
														<td class="log_tdleft" width="100">�ʻ�����:</td>
														<td width="300" class="log_tdright">
															<table width="126" cellpadding="0" cellspacing="0" border="0" align="left">
																<tr>
																	<td align="left">
															            <select name="AccountType" id="AccountType" style="color:#737373;width:128px;">
																			<option value="0">��¼�ʺ�</option>
																			<option value="1">���֤</option>
																		</select>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td class="log_tdleft"><font style="font-size:9pt;color:#737373;" valign="middle">�ʻ�����:</font></td>
														<td><input name="UserID" id="UserID" value="" size=15 maxlength=50 class="input_log"></td>
													</tr>
													<tr>
														<td class="log_tdleft">��&nbsp;&nbsp;&nbsp;&nbsp;��:</td>
														<td><input name="password" id="password" type="password" size=15 maxlength=50 class="input_log"></td>
													</tr>
													<tr >
														<td class="log_tdleft">��&nbsp;֤&nbsp;��:</td>
														<td align="left">	
															<table cellpadding="0" cellspacing="0" border="0" align="left">
																<tr>
																	<td align="left"><img alt="���������֤��" style="cursor:pointer;" id="mFrame" name="mFrame" src="<%=request.getContextPath()%>/passport/registration/image.jsp" onclick="changeImage();"></td>
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
														<td width="200"><span style="cursor:pointer" onclick="window.open('help.jsp','','fullscreen=0,height=680,width=705,top=100,left=100, scrollbars=no,resizable=1')">�����ע�����̣�������</span></td>
														<td class="btn_com" onclick="javascript:goRegister()">&nbsp;ע��&nbsp;</td>
														<td>&nbsp;&nbsp;&nbsp;</td>
														<td onclick="javascript:verify();" class="btn_com">&nbsp;��¼&nbsp;</td>
														<td>&nbsp;&nbsp;&nbsp;</td>
														<td class="btn_com"><a style="color:white;text-decoration:none;" href="<%=request.getContextPath()%>/groupPassport/getPassword/">�һ�����</a></td>
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