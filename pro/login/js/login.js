/**
 * Ukey登录验证
 */
function doUkeyRequest(){
	var pwd = jQuery("#USER_PASSWORDS").val();
    var logMgr = new LogMgr(jQuery("#USER_CODE").val(), pwd, jQuery("#CMPY_CODE").val(),jQuery("#CMPY_CODE__NAME").val());
    logMgr.checkLogin();
}
/**
 * Cert登录验证
 */
function doCertRequest(){
	var pfxcert = $("form.active").find("input[name=SAFE_File_CONTENT]").val(),
		cert	= $("form.active").find("input[name=serverCert]").val(),
		pass    = $("form.active").find("input[name=cipher]").val(),
		rand    = $("form.active").find("input[name=randnum]").val();
		try {
			if(SafeCtrl.FILE_GenAuthReq(pass,pfxcert,cert,rand)) {
				var errmsg = SafeCtrl.USB_GetLastError();
				alert("产生认证请求失败，原因为："+errmsg);
				return false;
			}
		
			var authreq = SafeCtrl.USB_GetAuthReq();
			$("form.active").find("input[type=password]").val(authreq);
			
		} catch(e) {
			alert("认证控件没有正确安装，请下载安装后再试！");
			return false;	
		}
		return true;
}
/**
 * 修改Ukey密码
 * @returns {Boolean}
 */
function modifyUkeyPwd(){
	  var pin_old  = $("#ukey_m_form_oldPwd").val(),
	      pin_new1 = $("#ukey_m_form_newPwd").val(),
	      pin_new2 = $("#ukey_m_form_confirmPwd").val();
	  try {
		    if(SafeCtrl.USB_OpenDevice(0)) {
			      SafeCtrl.USB_CloseDevice();
			      alert("未检测到智能卡！");
			      return false;
		    }
		    if(SafeCtrl.USB_ChangePin(1,pin_old,pin_new1)) {
			      SafeCtrl.USB_CloseDevice();
			      var errmsg = SafeCtrl.USB_GetLastError();
			      alert("修改智能卡密码失败，原因为："+errmsg);
			      return false;
		    }
		    SafeCtrl.USB_CloseDevice();
		    $("form.active").trigger("reset");
		    alert("修改智能卡密码成功！");
		    //self.close();
		   return true;
	  } catch(e) {
		    alert("客户端没有正确安装，请下载安装后再试！");
		    return false;
	  }
}
/**
 * 修改证书密码
 * @returns {Boolean}
 */
function modifyCertPwd(){
	var pfxcert  = $("form.active").find("input[type=file]").val(),
	    oldpass  = $("#cert_m_form_oldPwd").val(),
	    newpass  = $("#cert_m_form_newPwd").val(),
	    newpass2 = $("#cert_m_form_confirmPwd").val();
 
	  try {
		    if(SafeCtrl.FILE_ChangePFXPass(pfxcert, oldpass, newpass)) {
			      var errmsg = SafeCtrl.USB_GetLastError();
			      alert("修改PFX证书密码失败，原因为："+errmsg);
			      return false;
		    }
		    alert("修改PFX证书密码成功");
		    return true;
	  } catch(e) {
		    alert("客户端没有正确安装，请下载安装后再试！");
		    return false;
	}
}

/**
 * 打开测试页
 * 
 */
function openTestUrl(){
	var testUrl = "/sy/comm/index/activeTest.jsp";
	window.location.href = testUrl;
}

/**
 * 初始化页面时默认填充公司编码和公司名称
 */
function getCookie() { 
	//获取公司code和公司名称
	var cmpyCode = Cookie.get("scmpycode"),
	    cmpyName = Cookie.get("scmpyname");
	
	if (!!(cmpyCode && cmpyName)) {
        $("input[name=cmpyCode]" , "form.active").val(cmpyCode); 
        $("input[name=cmpyName]" , "form.active").val(cmpyName).prev("label").hide(); 
	}
}
 
/**
 * 成功登陆后记住用户名、密码
 * type:cookie类型  ukey或cert
 */
function setCookie() {
  var expires = "365",//一年
  	  cmpyCode = $("input[name=cmpyCode]" , "form.active").val(),
      cmpyName = $("input[name=cmpyName]" , "form.active").val();
  
  Cookie.set("scmpycode" , cmpyCode, expires); //保存用户信息
  Cookie.set("scmpyname" , cmpyName, expires); //保存用户名称信息
} 

jQuery(document).ready(function(){
	$(window).resize(function() {//window 改变大小时,居中
		var bodyW = $("body").width(),
			bodyH = $("body").height(),
			clientW = document.documentElement.clientWidth,
			clientH = document.documentElement.clientHeight;
		if(clientH > bodyH){
			//$("body").css("padding-top",(clientH-bodyH)/2);
		}else{
			$("body").css("padding-top",0);
		}
		if(clientW > 2564){
			$(".login-container").width(2564);
		}else{
			$(".login-container").width("100%");
		}
	});
//	$(this).keyup(function(event) {//按回车键触发表单提交 
//	       if ((event.keyCode == 13)) {
//	            $("form.active").trigger("submit");
//	       }
//	}); 
		var $section 	 = $(".login-section"),
			$tabWrapper  = $section.find(".login-tab-wrapper"),
			$currendTab  = $tabWrapper.children(".tab.selected"),
			$fromWrapper = $section.find('.login-form-wrapper'),
			$currentForm = $fromWrapper.children('form.active');
		
		getCookie();
		
//		$(this).on("keyup.login" , function(event) {//按回车键触发表单提交 
//		       if ((event.keyCode == 13)) {
//		    	   //在ie9+按回车默认提交表单，会导致 form提交2次
//		    	   //即对doCertRequest()执行2次，导致错误
//		    	   if ( $.browser.msie && parseInt($.browser.version) <9) {
//			    	   $fromWrapper.find("form.active").trigger("submit");
//		    	   }
//		       }
//		}); 
		
		
		//选项卡切换
	    $currendTab.on("click",".item",function(){
			var formId =$(this).attr("data-target"),
				form   = $("#"+formId);
				$(this).siblings(".current").removeClass("current");
				$(this).addClass("current");
				var sib = form.siblings(".active").removeClass("active").hide()
					sib.trigger("reset");
				form.addClass("active").show();
				errorReset();
				getCookie();
		});
		//返回登录页面
		$tabWrapper.on("click",".back-login",function(event){
			event.preventDefault();
			var tabRef = $(this).attr("data-ref");	
			var tab 	= $("#"+tabRef),
				formId = tab.attr("data-target"),
				form	= $("#"+formId);

			$(this).parents(".tab").removeClass("selected").hide();
			tab.parents(".tab").addClass("selected").fadeIn(100,function(){
				form.siblings().removeClass("active").hide();
				form.addClass("active").show();
				errorReset();
				getCookie();
			});
		});
		$fromWrapper.on("change","form.active input[type=file]",function(event){
			var $tempfile = $(this).parent().siblings("input[type=text]");
			$tempfile.val($(this).val()).focus();
		});
		
		$fromWrapper.on("click",".test-tab",function(){
			openTestUrl();
		});
		$fromWrapper.on("click",".link-tab",function(){
			var tabId  = $(this).attr("data-target"),
				tab    = $("#"+tabId);
				formId = tab.find("li.current").attr("data-target");
				$currForm	= $fromWrapper.children('form.active');
				
				tab.siblings().removeClass("selected").hide();
				tab.addClass("selected").show();
	 
				$section.fadeOut(100,function(){
					$currForm.removeClass('active').hide();
					$currForm.trigger("reset");
					$(this).css("right","1000px").show();
					  
					$currForm= $fromWrapper.children('form#'+formId);
					$(this).stop()
								 .animate({
									right:0
								 },300,"easeOutBounce",function(){
									$currForm.addClass('active');
									errorReset();
									getCookie();
									$currForm.fadeIn(400);
								 });
				});
		});
		
		$fromWrapper.on("submit","form.active",function(){
			checkForm(this);
			if(hasErrors()){
				return false;	
			}
			var id = $(this).attr("id");
			var f ;
			switch(id){
			
				case "ukey-form":f = doUkeyRequest();
								 f && setCookie();
								break;
				case "cert-form":f = doCertRequest();
								 f && setCookie();
								break;
				case "ukey-m-form":f = modifyUkeyPwd();
								break;
				case "cert-m-form":f = modifyCertPwd();
								break;
				default:
								f=false;
			}
			return f;	
		});
		 
		$fromWrapper.on("blur","form.active .nomal-block>input",function(){
			 // checkFild(this);
			var $value  = $(this).val(),
				$block  = $(this).parent();		 
			if(!$.trim($value)){
				$block.find("label").show();  
			}
		});
		$fromWrapper.on("focus","form.active .nomal-block>input",function(){
			 var value  = $(this).val(),
				 $block = $(this).parent(),
				  msg    = $block.find("label").html();	
			 if($block.hasClass("error")){
				 $block.removeClass("error").find(".errorMsg").remove(); 
			 }else{
				$block.find("label").hide(); 
			 }
		});
		$fromWrapper.on("click","form.active .nomal-block",function(){
			var $this = $(this);
			if($this.find(".errorMsg").length){
				$this.find(".errorMsg").remove(); 
				$this.find("[type!=file]").focus();
			}
		});
		
		/**
		 * 表单验证
		 */
		function checkForm(elem){
			var $inputs = $(elem).find(".nomal-block>input");
			$inputs.each(function(){
				checkField(this);
			});
		}
		/**
		 * 表单字段验证
		 */
		function checkField(elem){
			if(!isValidatableType(elem)){
				return false;	
			}
			var $this   = $(elem),
			   	$value  = $this.val(),
				$block  = $this.parent(),
				$msg    = $block.find("label").html();		 
			if(!$.trim($this.val())){
				$block.addClass("error");	
				if(!$block.find(".errorMsg").length){
					$block.append("<div class='errorMsg'>"+$msg+"不允许为空!</div>");	
				} 
			}else{
				//comfirmPwd
				var newPwd  = $block.parent().find(".js-newPwd");
				 confirmPwd = $block.find(".js-confirmPwd");
				if($this.is(confirmPwd)&& confirmPwd.val()!=newPwd.val()){
					$this.val('');
					$block.addClass("error");	
					if(!$block.find(".errorMsg").length){
						$block.append("<div class='errorMsg'>两次密码不一致!</div>");	
					} 
				}	 
			}
		}
		/**
		 * 可验证的字段
		 */
		function isValidatableType(elem,novalite){
		    //($(elem).attr("type") == "text" && $(elem).attr("name")!="cmpyName") || $(elem).attr("type") == "password" || $(elem).attr("type") == "file";
			return $(elem).attr("type") == "text" || $(elem).attr("type") == "password" || $(elem).attr("type") == "file";	
		}
		
		/**
		 * 是否有错误
		 */
		function hasErrors(){
			return $("form.active").find(".error").length!== 0;	
		}
		/**
		 * 重置错误样式
		 */
		function errorReset(){
			$("form.active").find(".error").removeClass("error")
						.end().find(".errorMsg").remove()
						.end().find("label").show();
		}
		function log(msg,o){
			console.log(msg,o);
//			console.log("----------"+msg+" start--------------");
//			console.debug(o);
		}
		

	(function(){//垂直居中设置
		var bodyH = $(".login-wrapper").height(),
		oClientHeight = document.documentElement.clientHeight;
		if(oClientHeight > bodyH){
			//$("body").css("padding-top",(oClientHeight-bodyH)/2);
		}
	})();
	jQuery("#btnLogin").bind("click", doLogin);
	
});
