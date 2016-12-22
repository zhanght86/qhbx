
 
/**
 * 初始化登录页面
 */
function loginInit_mb() {
	$(".content","#login").show();
	if(window.localStorage){
			var userData = localStorage.getItem("userData");
			if(userData){
				$("#USER_CODE").val(userData.substring(0,userData.indexOf(",")));
				$("#USER_CODE").prev("label").hide();
				$("#USER_PWD").val(userData.substring(userData.indexOf(",")+1));
				$("#USER_PWD").prev("label").hide();
			}else{
			}
	}
	$("#USER_PWD,#USER_CODE").on("focus",function(){
		$(this).val("");
		$(this).prev("label").hide();
	}).on("blur",function(){
		var value = $(this).val();
		
		if(!value){
			$(this).prev("label").show();
		}
	});
    //给按钮绑定事件
    $("#btnLogin").on("vclick", function(event) {
    	if(window.localStorage){
			var userData = localStorage.getItem("userData");
			var pwd = $("#USER_PWD").val();
			var userName = $("#USER_CODE").val();	
			localStorage.removeItem("userData");
			localStorage.setItem("userData",userName+","+pwd);
		
		
		}
    	event.preventDefault();
		event.stopImmediatePropagation();
    	doLogin();
    });
    
}
/**
 * 登录页面登录按钮click方法
 */
function doLogin() {
	//获取IMEI
	//window.plugins.telephonyInfo.getIMEI(function(info){
		//if(info.MAC){
			var username = $.trim($("#USER_CODE").val());
			var pwd = $("#USER_PWD").val();
			//var macStr = info.MAC.replace(/:/g,"");
			var macStr = "123";
			if(username){
				LogMgr.login(username, pwd,"name", "zhbx");
			}else {
				LogMgr.login(macStr, pwd,"key", 2);
			}
		//}
	//},function(){});
	 
}
/**
 * 系统登录验证管理类
 * @param {} id
 * @param {} pswds
 */
var LogMgr ={
    login : function(id, pswds, type, cmpyCode) {
        var msg = "正在进行登录验证，请稍候...";
        $.mobile.loading( "show", {
 		   text: "正在登录……",
 		   textVisible: true,
 		   textonly: false 
 	    });
        $("#msg").html(msg);
        if (pswds.length == 0) {
            $("#USER_PWD").focus();
            msg = "密码不能为空，请输入";
            $("#msg").html(msg);
			$.mobile.loading( "hide" );
            return;
        }
        FireFly.login(id, pswds, cmpyCode, type,{}).then(function(result){
 			var rtnMsg = result[UIConst.RTN_MSG];
 			if (StringUtils.startWith(rtnMsg, UIConst.RTN_OK)) {
		    	    //桌面系统交互RTN_MSG
	                msg = "验证通过，正准备进入系统......";
	                //记住用户名和密码
	                $("#msg").css("color","green");
	                $("#msg").html(msg);
 
	                var data = result["orgVar"];
	                var userCode  = data["@USER_CODE@"],
	                	username  = data["@USER_NAME@"],
	                	userImg   = data["@USER_IMG@"],
	                	deptCode  = data["@DEPT_CODE@"],
	                	deptName  = data["@DEPT_NAME@"],
	                	odeptCode = data["@ODEPT_CODE@"],
	                	jianCode  = data["@JIAN_CODES@"];
	                System.setUser("USER_CODE", userCode);
	        		System.setUser("USER_NAME", username);
	        		System.setUser("DEPT_CODE", deptCode);
	        		System.setUser("ODEPT_CODE", odeptCode);
	        		System.setUser("DEPT_NAME", deptName);
	        		System.setUser("CMPY_CODE", cmpyCode);
	        		System.setUser("JIAN_CODES", jianCode);
	        		if(odeptCode=="24"){//总公司
	        			$.mobile.window.isBranch = false;
	        		}else{//分公司
	        			$.mobile.window.isBranch = true;
	        		}
	                $.mobile.pageContainer.pagecontainer( "change","#mbDesk");

	                $("#msg").html("");

			    } else if (StringUtils.startWith(rtnMsg, UIConst.RTN_ERR)) {
	                var msg = rtnMsg.substring(6) + "  请重新输入!";
	                $("#msg").html(msg); 
			    } 
 		});
    }
};
