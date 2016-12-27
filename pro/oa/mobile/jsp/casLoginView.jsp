<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="org.apache.commons.lang.BooleanUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title>微信OA登录界面</title>
	<style type="text/css">
		html, body {
			margin:0;
			padding:0;
		}
		
		.custom {
			background:url('./images/login/top_blue.png') no-repeat; 
		}
		
		.mobile {
			width:100%;
			height:100%;
   			background:white url('./images/login/top_blue_mb.jpg') no-repeat  center top;
   			font-size:12px;
   			font-family:微软雅黑,宋体;
   			color:#383838;
   			overflow-x:hidden;
   			border:0px red solid;
		}
		
		.custom #container {
			margin:0;
			padding:0;
		}
		
		.mobile #container {
			margin:0;
			padding:0;
		}
		
		.custom #top {
			height:120px;
		}
		
		.mobile #top {
			display:none;
		}
		
		.custom #logo {
			height:47px;
			width:419px;
			margin:0 auto;
			background:url('./images/login/logo.png') no-repeat; 
		}
		
		.mobile #logo {
			margin:3% auto 20px auto;
   			padding-top:0px;
   			width:296px;
   			height:71px;
   			border:0px red solid;
   			background:url('./images/login/logo_mb.png')  no-repeat center center;
		}
		
		.custom .nav {
			width:400px;
			height:35px;
			margin-bottom:15px;
			list-style:none;
		}
		
		.custom .tabContainer {
			width:100%;
			height:100%;
			margin:0px 0px 15px 0px;
			padding:0px 0px;
			background-color: #eeeeee; /* Old browsers */
			background-repeat: repeat-x; /* Repeat the gradient */
			background-image: -moz-linear-gradient(top, #f5f5f5 0%, #eeeeee 100%); /* FF3.6+ */
			background-image: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#f5f5f5), color-stop(100%,#eeeeee)); /* Chrome,Safari4+ */
			background-image: -webkit-linear-gradient(top, #f5f5f5 0%,#eeeeee 100%); /* Chrome 10+,Safari 5.1+ */
			background-image: -ms-linear-gradient(top, #f5f5f5 0%,#eeeeee 100%); /* IE10+ */
			background-image: -o-linear-gradient(top, #f5f5f5 0%,#eeeeee 100%); /* Opera 11.10+ */
			filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#f5f5f5', endColorstr='#eeeeee',GradientType=0 ); /* IE6-9 */
			background-image: linear-gradient(top, #f5f5f5 0%,#eeeeee 100%); /* W3C */
			border-top: 1px solid #e5e5e5;
			border-bottom: 1px solid #e5e5e5;
		}
		
		.custom .tabContainer .tab {
			display:inline;
		}
		
		.custom .tab a {
			color:black;
			text-decoration:none;
			line-height:36px;
			padding:4px 4px;
			border-right:1px solid #e5e5e5;
			font-size:14px;
		}
		
		.custom .tab a:hover {
			color:#0088cc;
		}
		
		.custom .tab a.active {
			color:#0088cc;
		}
		
		.radius5 {
			-moz-border-radius:5px 5px;
			-webkit-border-radius:5px 5px;
			-o-border-radius:5px 5px;
			-ms-border-radius:5px 5px;
			-khtml-border-radius:5px 5px;
			border-radius:5px 5px;
		}
		
		.custom #content {
			margin:30px auto;
			height:240px;
			width:400px;
			position:relative;
			border: 1px solid #e5e5e5;
			border-top:0px;
		}
		
		.custom #footer {
			height:236px;
			background:url('./images/login/bottom_back.png')
		}
		
		.custom #bottom {
			height:236px;
			width:888px;
			margin:0 auto;
			background:url('./images/login/taizi.jpg') no-repeat; 
		}
		
		.mobile #bottom {
			width:296px;
   			height:71px;
			margin:150px auto 0 auto;
			background:url('./images/login/taizi_mb.png') no-repeat center center;
		}
		
		.custom #activeTest {
			margin:0 auto;
			width:auto;
			height:20px;
			position:relative;
		}
		
		.mobile #activeTest {
			display:none;
		}
		
		.custom #activeTest span {
			position:absolute;
			top:10px;
			right:10px;
		}
		
		.custom #activeTest a:link {
			color:#FF9224;
		}
		
		.mobile #spanValicode {
			height:60px;
		}
		
		.custom #valicode {
			width:140px;
		}
		
		.mobile #valicode {
			width:110px;
			height:30px;
		}
		
		.custom #imgValicode {
			width:75px; 
			height:25px; 
			_height:22px; 
			vertical-align:middle;
		}
		
		.mobile #imgValicode {
			width:50px; 
			height:30px; 
			vertical-align:middle;
		}
		
		.custom #usual {
			text-align:center;
			width:120px;
			height:30px;
			line-height:30px;
			border:0px;
			background-image:url('./images/login/submit.png'); 
			cursor:pointer;
		}
		
		.custom #getcode {
			text-align:center;
			width:100px;
			height:30px;
			line-height:30px;
			border:0px;
			cursor:pointer;
			margin-left:2px;
			background-color:#F4F4F4;
		}
		
		.mobile #usual {
			cursor:pointer;
			width:180px;
			height:30px;
			font-size:20px;
			background:url('./images/login/submit_mb.png') no-repeat center center;
			color:white;
			font-family:微软雅黑,宋体;
			text-shadow:0px -1px 1px black;
		}
		
		.custom .input {
			margin-top:10px;
		}
		
		.mobile .input {
			margin:20px auto 20px auto;
		}
		
		.custom .input input {
			width:220px;
			border:solid #ACD6FF 1px;
			height:25px;
			line-height:30px;
			padding:2px 2px;
		}
		
		.mobile .input input {
			-moz-box-shadow:2px 2px 3px #BDBEBB inset;
			-webkit-box-shadow:2px 2px 3px #BDBEBB inset;
			box-shadow:2px 2px 3px #BDBEBB inset;
			border-radius:12px;
			width:100%;
			height:100%;
		}
		
		.mobile .inputCon {
			font-size:20px;
			width:180px;
			height:30px;
			line-height:30px;
			margin:0 auto 0 auto;
			display:block;
		}
		
		.custom .label {
			display:inline-block;
			width:110px;
			line-height:30px;
			margin-left:25px;
		}
		
		.mobile .label {
			display:none;
		}
		
		.custom .errorMessage {
			top:-20px;
			clear:both;
			width :340px;
			height :20px;	
			line-height:20px;
			text-align:left;
			color:red;
			font-weight:bold;
			letter-spacing:0px;
			position:absolute;
		}
		
		.mobile .errorMessage {
			clear:both;
			width :180px;
			height :10px;	
			line-height:10px;
			text-align:left;
			color:red;
			font-weight:bold;
			letter-spacing:0px;
			margin:auto auto;
		}
	</style>
	<script type='text/javascript' src='<c:url value="/js/jquery-1.6.4.min.js" />'></script>
	<script type="text/javascript">
	
	// 移动终端
	function doMobile() {
		if (Browser.versions().android || Browser.versions().iPhone) {
			var viewport = '<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>';
			$("meta").first().after(viewport);
			// 切换到移动样式
			$(".custom").addClass("mobile").removeClass("custom");
		}
		
		// 隐藏地址栏
		if (Browser.versions().mobile) {
		    window.addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false);
		    function hideURLbar() {
		      window.scrollTo(0,1);
		    }
	    }
	}
	
	function writeCookie(name, value, hours) {
		var expire = "";
	  	if (hours != null) {
	  		expire = new Date((new Date()).getTime() + hours * 1000);
	   		expire = "; expires=" + expire.toGMTString();
	  	}
	  	document.cookie = name + "=" + escape(value) + expire;
	}

	function readCookie(name) {
		var cookieValue = "";
	  	var search = name + "=";
	  	if (document.cookie.length > 0) {
	    	offset = document.cookie.indexOf(search); 
	    	if (offset != -1) {
	      		offset += search.length;
	      		end = document.cookie.indexOf(";", offset);
	      		if (end == -1) {
	      			end = document.cookie.length;
	      		}
	      		cookieValue = unescape(document.cookie.substring(offset, end))
	    	}
	  	}
	  	return cookieValue;
	} 
	
	var isMSGLogin = true; // 默认短信登录
	function validate() { // 验证不能为空
		var errorString = "错误提示：";
		var username = $("#username").val();
		if (isMSGLogin) { // 短信登录
			var msgValid = $("#msgValid").val();
			var cellphone = $("#cellphone").val();
			if (!username || username.length == 0) {
				errorString = errorString + "工号/登录名不能为空";
				$("#errorMessage").show().text(errorString);
				$("#username").focus();
				$("div[name='errorMessage']").hide().text("");
				return false;
			} 
			if (!cellphone || cellphone.length == 0) {
				errorString = errorString + "手机后四位不能为空";
				$("#errorMessage").show().text(errorString);
				$("#cellphone").focus();
				$("div[name='errorMessage']").hide().text("");
				return false;
			}
			if (!msgValid || msgValid.length == 0) {
				errorString = errorString + "短信验证码不能为空";
				$("#errorMessage").show().text(errorString);
				$("#msgValid").focus();
				$("div[name='errorMessage']").hide().text("");
				return false;
			}
		} else { // 密码登录
			var password = $("#password").val();
			var valicode = $("#valicode").val();
			if (!username || username.length == 0) {
				errorString = errorString + "用户名不能为空";
				$("#errorMessage").show().text(errorString);
				$("#username").focus();
				$("div[name='errorMessage']").hide().text("");
				return false;
			} 
			if (!password || password.length == 0) {
				errorString = errorString + "密码不能为空";
				$("#errorMessage").show().text(errorString);
				$("#password").focus();
				$("div[name='errorMessage']").hide().text("");
				return false;
			}
			if (!valicode || valicode.length == 0) {
				errorString = errorString + "校验码不能为空";
				$("#errorMessage").show().text(errorString);
				$("#valicode").focus();
				$("div[name='errorMessage']").hide().text("");
				return false;
			}
		}

		return true;
	}
	
	function doSubmit() {
		// 公司默认为中华保险
		$("#cmpycode").val("zhbx");
		// 校验通过提交
		if(validate()) {
			$("#form").submit();
		}
		storeCookie();
	}
	
	function storeCookie() {
		// 默认姓名和密码保存100天
		writeCookie("username", $("#username").val(), 60 * 60 * 24);
	}
	
	// 获取手机验证码
	function getValiCode() {
		var errorString = "错误提示：";
		var username = $("#username");
		var cellphone = $("#cellphone");
		if (username.val().length == 0) {
			errorString = errorString + "用户名不能为空";
			$("#errorMessage").show().text(errorString);
			return;
		}
		if (cellphone.val().length != 4) {
			errorString = errorString + "请输入手机后四位";
			$("#errorMessage").show().text(errorString);
			return;
		}
		
		var url = "msg/send.do?username=" + $("#username").val() + "&cellphone=" + $("#cellphone").val();
		$.get(url, function(data) {
			if (data.indexOf("success:") >= 0) {
				var $getcode = $("#getcode");
				$getcode.attr("disabled", "disabled");
				var time = 120; // 2分钟之内只能点击一次
				var timer = setInterval(function(){
					time = time - 1;
					$getcode.val("获取验证码(" + time + ")");
				}, 1000);
				setTimeout(function(){
					clearInterval(timer);
					$getcode.val("获取验证码");
					$getcode.removeAttr("disabled");
				}, time * 1000);
			} else if (data.indexOf("failure:1") >= 0) {
				errorString = errorString + "您所登录的用户不存在";
				$("#errorMessage").show().text(errorString);
			} else if (data.indexOf("failure:2") >= 0) {
				errorString = errorString + "您的手机号无效：" + data.substring(data.indexOf("failure:2") + 10);
				$("#errorMessage").show().text(errorString);
			} else if (data.indexOf("failure:3") >= 0) {
				errorString = errorString + "您所登录的用户没有手机号码";
				$("#errorMessage").show().text(errorString);
			} else if (data.indexOf("failure:4") >= 0) {
				//errorString = errorString + "获取验证码出现异常";
				errorString = errorString + "验证码已发送";
				$("#errorMessage").show().text(errorString);
			} else if (data.indexOf("failure:5") >= 0) {
				errorString = errorString + "手机号后四位错误";
				$("#errorMessage").show().text(errorString);
			} else if (data.indexOf("failure:6") >= 0) {
				errorString = errorString + "您没有短信登录权限，请联系管理员";
				$("#errorMessage").show().text(errorString);
			} else { // 其它不可预测的异常
				//errorString = errorString + "获取验证码失败";
				$("#errorMessage").show().text("验证码已发送").css({"color":"green"});
			}
		}); 
		storeCookie();
	}
	
	//返回短信登录
	function backMsgLogin() {
		$("#msgLogin").trigger("click");
		$("#pwdLoginLi").hide();
		$("#msgLoginLi").show();
		$("#backMsgLogin").hide();
		// 默认姓名和密码保存100天
		var expire = "; expires=-1";
	  	document.cookie = "<%=urlCookieName %>=" + expire;
	}
	
	
	$(function(){
		// 移动终端
		doMobile();
		
		// 回车提交
		$(".custom").keypress(function (e) {
			var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode;
			if (keyCode == 13) {
				$("#usual").click();
			}
		});
		// 读取cookie里的姓名和密码
		$("#username").val(readCookie("username"));
		
		// 切换登录方式
		$(".loginTab").click(function(){
			$("#errorMessage").text("");
			var $node = $(this);
			$(".loginTab").removeClass("active");
			$node.addClass("active");
			$("#password").val("");
			
			var id = $node.attr("id");
			if (id == "msgLogin") {
				isMSGLogin = true;
				$("#isMSG").val(true);
				$("#username").parent().parent().find(".label").html("用<span style='display:inline-block;width:15px;'></span>户<span style='display:inline-block;width:15px;'></span>名：");
				$("#cellphone").parent().parent().show();
				$("#msgValid").attr("name", "password");
				$("#msgValid").parent().parent().show();
				$("#password").parent().parent().find(".label").html("密<span style='display:inline-block;width:15px;'></span>码：");
				$("#password").attr("name", "_password_");
				$("#password").parent().parent().hide();
				$("#valicode").parent().parent().hide();
				$("#valicode").attr("name", "_valicode_");
				$("#getcode").show();
			} else if (id == "pwdLogin") {
				isMSGLogin = false;
				$("#isMSG").val(false);
				$("#username").parent().parent().find(".label").html("用户名：");
				$("#cellphone").parent().parent().hide();
				$("#msgValid").attr("name", "_password_");
				$("#msgValid").parent().parent().hide();
				$("#password").attr("name", "password");
				$("#password").parent().parent().show();
				$("#valicode").parent().parent().show();
				$("#valicode").attr("name", "valicode");
				$("#getcode").hide();
			}
		});
		
		// 初始化登录方式
		isMSGLogin = <%=request.getParameter("MSG")%>;
		if (isMSGLogin == null) { // 一开始进入request里还没有MSG
			isMSGLogin = true;
		}
		if ($("#isMSG").val().length == 0 && $("#isMSG").val() == "false") {
			isMSGLogin = false;
		}
		if (isMSGLogin) {
			$("#msgLogin").trigger("click");
		} else {
			$("#pwdLogin").trigger("click");
		}
		var show_Flag = "<%= showFlag%>" || "false";
		if (show_Flag == "true") {
			$("#pwdLogin").trigger("click");
			$("#pwdLoginLi").show();
			$("#msgLoginLi").hide();
		} else {
			$("#msgLogin").trigger("click");
			$("#msgLoginLi").show();
			$("#pwdLoginLi").hide();
			$("#backMsgLogin").hide();
		}
	});
	</script>
</head>
<body class="custom">
	<div id="container">
		<div id="top">
			<div id="activeTest">
				<span>
					<a id="backMsgLogin" style="margin-right:20px;" href="javascript:void(0);" onclick="javascript:backMsgLogin();">短信登录</a>
					<a href="/sy/comm/index/activeTest.jsp" target="_blank">控件检测</a>
				</span>
			</div>
		</div>
		<div id="logo"></div>
		<div id="content">
			<div class="nav">
				<ul class="tabContainer">
					<!-- 默认为短信登录，填写showFlag参数并且为true时显示密码登录 -->
						<li class="tab" id="pwdLoginLi"><a id="pwdLogin" class="loginTab" href="javascript:void(0);">密码登录</a></li>
						<li class="tab" id="msgLoginLi"><a id="msgLogin" class="loginTab active" href="javascript:void(0);">短信登录</a></li>
				</ul>
			</div>
			<form:form method="post" id="form" commandName="${commandName}" htmlEscape="true">
				<div id="errorMessage" class="errorMessage"></div>
				<form:errors path="*" class="errorMessage" element="div" name="errorMessage" />
				<input type="hidden" name="lt" value="${loginTicket}" />
				<input type="hidden" name="execution" value="${flowExecutionKey}" />
				<input type="hidden" name="_eventId" value="submit" />
				<input type="hidden" id="isMSG" name="MSG" value="false" />
				<div class="input">
					<span class="label">用户名：</span>
					<span class="inputCon"><input type="text" placeholder="用户名" id="username" name="username" /></span>
				</div>
				<div class="input">
					<span class="label">手机后四位：</span>
					<span class="inputCon"><input type="text" placeholder="手机后四位" id="cellphone" name="cellphone" /></span>
				</div>
				<div class="input">
					<span class="label">短信验证码：</span>
					<span class="inputCon"><input type="text" placeholder="验证码" id="msgValid" name="password" /></span>
				</div>
				<div class="input">
					<span class="label">密&nbsp;&nbsp;码：</span>
					<span class="inputCon"><input type="password" placeholder="密码" id="password" name="_password_" /></span>
				</div>
				<div class="input" style="display:none;">
					<span class="label">校验码：</span>
					<span id="spanValicode" class="inputCon"><input type="text" placeholder="点击图片刷新验证码" style="width:120px;" id="valicode" name="_valicode_" maxlength="4" /><img id="imgValicode" src="valicodeimage?" style="margin-left:5px;width:95px;height:25px;_height:22px;vertical-align: middle;" onclick="this.src='valicodeimage?'+new Date().getTime();" /></span>
				</div>
				<input type="hidden" id="cmpycode" name="cmpycode" value="zhbx" />
				<div class="input">
					<span class="label"></span>
					<span class="inputCon">
						<input type="button" id="usual" value="登　录" onClick="doSubmit()" />
						<input type="button" id="getcode" value="获取验证码" onClick="getValiCode()" />
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