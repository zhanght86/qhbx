$(function(){
	// 实现界面填满手机窗口
	var viewport = '<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>';
	$("meta").first().after(viewport);
});

function doSubmit(){
	var userLoginName = $("#username").val(); // 用户登录名
	var password = $("#password").val(); // 用户密码
	if (password.length == 0) {
		$("#errorMessage").text("错误提示：密码不能为空");	
		return;
	} else {
		$("#errorMessage").text("");
		  var pswdsBase64 = jQuery.base64.encode(password);
		var resultData = FireFly.login(userLoginName, pswdsBase64, 'zhbx');		
		if (resultData.exception) {
			 jQuery("#errorMessage").text("系统登录出错", resultData["msg"]);
        } else {
		       if (resultData[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {
		    	   $("#errorMessage").css("color","green");
		    	   $("#errorMessage").text("验证通过，正准备进入系统......");
	                setTimeout(function() {
	                	Tools.redirect($("#path_url").val());
	                },100);
			    } else if (resultData[UIConst.RTN_MSG].indexOf(UIConst.RTN_ERR) == 0) {
	                var msg = resultData[UIConst.RTN_MSG];
	                msg = msg.substring(6);
	                jQuery("#errorMessage").html(msg);
	                jQuery("#errorMessage").append("  请重新输入!");
			    } 
        }
	}	
};

