(function(){
    $(document).ready(function(){
        setTimeout(function(){
            $("#USER_SERCH_TAB_CON").tabs({});	
			$("#self-resume-tabs").tabs({});				
        }, 0);
    });
})();
/**
 * 页面加载执行的方法
 * @param {Object} 
 */
$(document).ready(function(){
	
	/**
	 * 获得人员姓名信息
	 */
	var users = getSearchDatas("");
	/**
	 * 组织机构面板
	 */
	var options = {
        "itemCode": "rh-select-serv",
        "config": "SY_ORG_DEPT_USER,{'rtnLeaf':true,'extendDicSetting':{'rhexpand':false,'childOnly':true},'TYPE':'single','rtnNullFlag':true}",
        "parHandler": this,
        "hide": "explode",
        "show": "blind",
        "pCon": jQuery("#USER_SERCH_TAB_ORG_TREE"),
        "replaceNodeClick": function(item){
            var leaf = item.LEAF;
            if (leaf == '1') {
                showUserInfor(item.ID);
            }
            return false;
        }
    };
    var dictView = new rh.vi.rhDictTreeView(options);
    dictView.show();
	showUserInfor(System.getVar("@USER_CODE@"));
	getDoApplyPanel();
	var clientH = GLOBAL.getDefaultFrameHei();
	if(clientH<580){
		$('#txl-out').attr("style","height:580px");
		$('#txl').attr("style","height:580px");
	}else{
		$('#txl-out').attr("style","height:"+clientH+"px;");
		$('#txl').attr("style","height:"+clientH+"px;");
		var margH =parseInt((clientH-560)/2);
		$("#USER_TXL").attr("style","margin-top:"+margH+"px;");
	}
	/**
	 * 绑定按钮事件
	 */        
});


/**
 * 获得用户列表数据
 * @param {Object} inputValue
 */
function getSearchDatas(inputValue){
	var datas = {};
    datas["_NOPAGE_"] = true;
	datas["_SELECT_"]="USER_CODE,USER_LOGIN_NAME,USER_NAME";
	if(inputValue.trim().length==0){
		datas["_searchWhere"] = " and ODEPT_CODE='"+System.getVar("@ODEPT_CODE@")+"'";
	}else{
		datas["_searchWhere"] = " and ODEPT_CODE='"+System.getVar("@ODEPT_CODE@")+"' and (USER_CODE like '%" + inputValue + "%' or USER_NAME like '%" + inputValue + "%')";
	}
    var users = FireFly.getListData("SY_COMM_ADDRESS_LIST", datas);	
	createSearchUserView(users);	
}
/**
 * 获得得查询后的用户列表
 */
function createSearchUserView(users){
	$("#PY_ZM_LI").empty();
	/**
	 * 构建字母列
	 */
	var pyzmAry = ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"];
	$(pyzmAry).each(function(){
		var pyzmLi = $("#PY_ZM_LI");
		$("<li class='USER_SERCH_TAB_PINYIN_ZB_li'><a href='#' class='USER_SERCH_TAB_PINYIN_ZB_li_a'>"+this+"</a></li>").appendTo(pyzmLi);
	});			
	/**
	 * 构建姓名面板区
	 */
	$("#PY_ZM_LAB").empty();
	$(pyzmAry).each(function(){
		var pyzmLi = $("#PY_ZM_LAB");
		$("<li class='PY_ZM_LAB-li'></li>").attr("id",this).text(this).appendTo(pyzmLi);
		$("<ul style='color:black'></ul>").attr("id",this+"-DATAS").appendTo($("#"+this));
	});
	$(users._DATA_).each(function(){
		var login_name = this.USER_LOGIN_NAME;
		var userName = this.USER_NAME;
		var userId = this.USER_CODE;
        var pre_login_name = login_name.substring(0, 1).toUpperCase();
		$(pyzmAry).each(function(){
			if(pre_login_name==this){
				$("<li class='PY_ZM_LAB-name-li' onmouseover=showUserInfor('"+userId+"')>"+userName+"</li>").appendTo($("#"+pre_login_name+"-DATAS"));
				return false;
			}			
		});	
	});
	/**
	 * 去除空节标签
	 */
	$(pyzmAry).each(function(){
		if($("#"+this+"-DATAS").children().length==0){
			$("#"+this).remove();
		}
	});
	
	$(".USER_SERCH_TAB_PINYIN_ZB_li_a").bind("mouseover",function(){
		var zm = this.innerHTML;
		var cswz = $("#user-contener").offset().top;
		var zmlab = $("#"+zm);
		var hei=0;
		if(zmlab.size()>0){
			hei = $("#"+zm).offset().top-cswz;
			$("#user-contener").parent().scrollTop(hei);
			$(".PY_ZM_LAB-li-selected").removeClass().addClass("PY_ZM_LAB-li");
			zmlab.addClass("PY_ZM_LAB-li-selected");
		}		
	});
}

/**
 * 显示用户
 * @param {Object} Uid 用户Id
 */
function showUserInfor(uid){
	cleareUserInfo();
    var userObj = FireFly.byId("SY_COMM_ADDRESS_LIST", uid);
	if(userObj.USER_NAME&&userObj.USER_NAME.length>0){
		$("#user_name").text(userObj.USER_NAME);
	}else{
		$("#user_name").text("").append("<span>&nbsp</span>");
	}
	if(userObj.USER_POST&&userObj.USER_POST.length>0){
		$("#user_post").text(userObj.USER_POST);
	}else{
		$("#user_post").text("").append("<span>&nbsp</span>");
	}
	
	if(userObj.ODEPT_CODE&&userObj.ODEPT_CODE.length>0){
		var odept = FireFly.byId("SY_ORG_DEPT",userObj.ODEPT_CODE);
		$("#user_cmpy").text(odept.DEPT_NAME);
		if(userObj.DEPT_CODE__NAME&&userObj.DEPT_CODE__NAME.length>0){
			var pos = (userObj.DEPT_CODE__NAME).indexOf(odept.DEPT_NAME);
			if(pos>=0){
				$("#user_dept").text((userObj.DEPT_CODE__NAME).substr((odept.DEPT_NAME).length+1+pos)).append("<span>&nbsp</span>");
			}else{
				$("#user_dept").text(userObj.DEPT_CODE__NAME).append("<span>&nbsp</span>");
			}
		}else{
			$("#user_dept").text("").append("<span>&nbsp</span>");
		}
	}else{
		$("#user_cmpy").text("").append("<span>&nbsp</span>");
	}

	if(userObj.USER_OFFICE_PHONE&&userObj.USER_OFFICE_PHONE.length>0){
		$("#user_offer_tel").text(userObj.USER_OFFICE_PHONE);
	}else{
		$("#user_offer_tel").text("").append("<span>&nbsp</span>");
	}
	if(userObj.USER_MOBILE&&userObj.USER_MOBILE.length>0){
		$("#user_mobile").text(userObj.USER_MOBILE);
	}else{
		$("#user_mobile").text("").append("<span>&nbsp</span>");
	}
	
	$("#user_sex").text(userObj.USER_SEX__NAME||"男");
	
	var imgSub = userObj.USER_IMG;
	
	if (userObj.USER_IMG_SRC.length <= 0) {
        if (userObj.USER_SEX == "0") {
            imgSrc = FireFly.getContextPath() + "/sy/theme/default/images/common/rh-male-icon.png";
        } else if (userObj.USER_SEX == "1") {
            imgSrc = FireFly.getContextPath() + "/sy/theme/default/images/common/rh-lady-icon.png";
        }
    }else {
        imgSrc = FireFly.getContextPath() + imgSub+"&size=100x100";
    }
	$("#user_img_view").attr("src",imgSrc);
	getUserBaseInfo(uid);
	getUserWorkInfo(uid);
	getUserEducationInfo(uid);
	getUserJcInfo(uid);
	if("1"==userObj.USER_RESUME){
		$("#resume-apply").hide();
		$("#base-info").show();
		$("#work-info").show();
		$("#education-info").show();
		$("#jc-info").show();
	}else{
		$("#resume-apply").show();
		$("#base-info").hide();
		$("#work-info").hide();
		$("#education-info").hide();
		$("#jc-info").hide();
	}
	$("#send-mobile-apply").hide();
	/* 取消对手机号的申请	
	if("OK"==userObj.USER_PHONE_OK){
		$("#send-mobile-apply").hide();
		
	}else{
		$("#send-mobile-apply").show();
	}*/
	
	btnBindEvent(uid,userObj.USER_NAME);
//	console.debug(userObj);
	//设置RTX
	setUpRtx(uid,userObj.USER_LOGIN_NAME);
}
function setUpRtx(id,name){
	if(id && id == System.getVar('@USER_CODE@')){
		$(".RTX").hide();
	}else{
		$(".RTX").attr("data-id",name).show();
		if(rtx_isFirstLoad){
			$(".RTX").trigger("mouseenter");
		}
	}
}
/**
 * 清空用户信息
 * @param {Object} Uid
 */
function cleareUserInfo(){
	$("#user_img_view").attr("src","");
	var infoAry = ["user_name","user_post","user_cmpy","user_dept","user_offer_tel","user_mobile","user_sex"];
	$(infoAry).each(function(){
		$("#"+this).empty().append("<span>&nbsp</span>");
	});
	$("#work-info").empty();
	$("#education-info").empty();
	$("#jc-info").empty();
}
/**
 * 获得用户基本信息
 */
function getUserBaseInfo(uid){

	var userObj= FireFly.byId("SY_ORG_USER_INFO_SELF_ALL", uid);
	if(userObj.USER_NAME&&userObj.USER_NAME.length>0){
		
		$("#base-info-user-name").text(userObj.USER_NAME);
	}else{
		$("#base-info-user-name").text("").append("<span>&nbsp</span>")
	}
	if(userObj.USER_NATION&&userObj.USER_NATION.length>0){
		$("#base-info-user-nation").text(userObj.USER_NATION);
	}else{
		$("#base-info-user-nation").text("").append("<span>&nbsp</span>");
	}
	if(userObj.USER_HOME_LAND__NAME&&userObj.USER_HOME_LAND__NAME.length>0){
		$("#base-info-user-home-land").text(userObj.USER_HOME_LAND__NAME);
	}else{
		$("#base-info-user-home-land").text("").append("<span>&nbsp</span>");
	}
	if(userObj.USER_HEIGHT&&userObj.USER_HEIGHT!='0'){
		$("#base-info-user-height").text(userObj.USER_HEIGHT);
	}else{
		$("#base-info-user-height").text("").append("<span>&nbsp</span>");
	}
	if(userObj.USER_EDU_LEVLE__NAME&&userObj.USER_EDU_LEVLE__NAME.length>0){
		$("#base-info-user-edu-level").text(userObj.USER_EDU_LEVLE__NAME);
	}else{
		$("#base-info-user-edu-level").text("").append("<span>&nbsp</span>");
	}
	if(userObj.USER_TITLE&&userObj.USER_TITLE.length>0){
		$("#base-info-user-title").text(userObj.USER_TITLE);
	}else{
		$("#base-info-user-title").text("").append("<span>&nbsp</span>");
	}
	if(userObj.USER_BIRTHDAY&&userObj.USER_BIRTHDAY.length>0){
		$("#base-info-user-birthday").text(userObj.USER_BIRTHDAY);
	}else{
		$("#base-info-user-birthday").text("").append("<span>&nbsp</span>");
	}
	if(userObj.USER_MARRIAGE__NAME&&userObj.USER_MARRIAGE__NAME.length>0){
		$("#base-info-user-marriage").text(userObj.USER_MARRIAGE__NAME);
	}else{
		$("#base-info-user-marriage").text("").append("<span>&nbsp</span>");
	}
	if(userObj.USER_POLITICS&&userObj.USER_POLITICS.length>0){
		$("#base-info-user-politics").text(userObj.USER_POLITICS);
	}else{
		$("#base-info-user-politics").text("").append("<span>&nbsp</span>");
	}	
	if(userObj.USER_WORK_DATE&&userObj.USER_WORK_DATE.length>0){
		$("#base-info-user-work-date").text(userObj.USER_WORK_DATE);
	}else{
		$("#base-info-user-work-date").text("").append("<span>&nbsp</span>");
	}
	if(userObj.USER_CMPY_DATE&&userObj.USER_CMPY_DATE.length>0){
		$("#base-info-user-cmpy-date").text(userObj.USER_CMPY_DATE);
	}else{
		$("#base-info-user-cmpy-date").text("").append("<span>&nbsp</span>");
	}
	if(userObj.USER_WORK_LOC&&userObj.USER_WORK_LOC.length>0){
		$("#base-info-user-work-loc").text(userObj.USER_WORK_LOC);
	}else{
		$("#base-info-user-work-loc").text("").append("<span>&nbsp</span>");
	}
	if(userObj.USER_IDCARD&&userObj.USER_IDCARD.length>0){
		$("#base-info-user-idcard").text(userObj.USER_IDCARD);
	}else{
		$("#base-info-user-idcard").text("").append("<span>&nbsp</span>");
	}	
	if(userObj.USER_EDU_SCHOOL&&userObj.USER_EDU_SCHOOL.length>0){
		$("#base-info-user-edu-school").text(userObj.USER_EDU_SCHOOL);
	}else{
		$("#base-info-user-edu-school").text("").append("<span>&nbsp</span>");
	}		
	if(userObj.USER_EDU_MAJOR&&userObj.USER_EDU_MAJOR.length>0){
		$("#base-info-user-edu-major").text(userObj.USER_EDU_MAJOR);
	}else{
		$("#base-info-user-edu-major").text("").append("<span>&nbsp</span>");
	}		
}
/**
 * 获得用户工作经历
 */
function getUserWorkInfo(Uid){
	var data = {};
	data["_PAGE_"]={"SHOWNUM":'3'};

	var temp = {"sId":"SY_ORG_USER_RESUME_FORWORK","pCon":$('#work-info'),"reset":"false","showPageFlag":true,
		"showSearchFlag":"false","showTitleBarFlag":"false","showButtonFlag":false,"links":data,"extWhere":" and user_code='"+Uid+"'","_SELECT_":"substr(RSM_BEGIN_DATE,0,7) ||'~'|| substr(RSM_END_DATE,0,7) JZ_DATE,RSM_COMPANY,RSM_TITLE"};
	var listView = new rh.vi.listView(temp);
	listView.show();
	$("#SY_ORG_USER_RESUME_FORWORK").addClass("list-data-grid");
}
/**
 * 获得用户教育经历
 */
function getUserEducationInfo(Uid){
	var data = {};
	data["_PAGE_"]={"SHOWNUM":'3'};
	data["SELECT"]="RSM_BEGIN_DATE";
	var temp = {"sId":"SY_ORG_USER_RESUME_FOREDU","pCon":$('#education-info'),"reset":"false","showPageFlag":true,
		"showSearchFlag":"false","showTitleBarFlag":"false","showButtonFlag":false,"links":data,"extWhere":" and user_code='"+Uid+"'","_SELECT_":"substr(RSM_BEGIN_DATE,0,7) ||'~'|| substr(RSM_END_DATE,0,7) JZ_DATE,RSM_COMPANY,RSM_TITLE"};
	var listView = new rh.vi.listView(temp);
	listView.show();
	$("#SY_ORG_USER_RESUME_FOREDU").addClass("list-data-grid");
}
/**
 * 获得用户奖惩情况
 */
function getUserJcInfo(Uid){
	var data = {};
	data["_PAGE_"]={"SHOWNUM":'3'};
	var temp = {"sId":"SY_ORG_USER_REWARD_ALL","pCon":$('#jc-info'),"reset":"false","showPageFlag":true,
		"showSearchFlag":"false","showTitleBarFlag":"false","showButtonFlag":false,"links":data,"extWhere":" and user_code='"+Uid+"'"};
	var listView = new rh.vi.listView(temp);
	listView.show();
	$("#SY_ORG_USER_REWARD_ALL").addClass("list-data-grid");
}

/**
 * 绑定个人名片按钮事件
 * @param {Object} userId 被查看用户ID
 * @param {Object} userName 被查看用户名
 */
function btnBindEvent(userId, userName){
	/**
 * 绑定即时通讯按钮
 * @param {Object} event
 */
	$("#instant-messaging").unbind('click').bind("click", function(event){
		var user_id = userId + "-rhim-server";
		var chat_id = userId + "@rhim.server";
		var user_name1 = userName;
		/*parent.rhImFunc.showChatArea({
			"id": user_id,
			"jid": chat_id,
			"name": user_name1,
			"status": "online"
		});*/
		//RAP(userName);
	});
	/**绑定发送邮件按钮*/
	$("#send-email").unbind("click").bind("click", function(e){
		window.open("http://staff.zotn.com/");
	});
	/**绑定发送信息按钮*/
	$("#send-phone-msg").unbind("click").bind("click", function(e){
		var phone = $("#user_mobile").text();
		var p1 = /^1[3458][0-9]\d{8}$/;
		if (p1.test(phone)){
			var params={"SMS_SENDTOID":"quxch","SMS_SENDTONAME":$("#user_name").text()};
			Tab.open({"url":"SC_SMSCENTER.card.do","tTitle":"短信中心","menuFlag":2,"menuId":"0E5CSqu8RN5tbC1poPCbQjd","params":params});
		}else{
			alert("此号码有误，不能发送短信！");
		}
		
	});
	/**绑定手机号码“申请”按钮*/
	$("#send-mobile-apply").unbind("click").bind("click", function(e){
		createApply(userId,userName,'phone');
	});
	/**绑定简历“申请”按钮*/
	$("#resume-apply").unbind("click").bind("click", function(e){
		createApply(userId,userName,'sume');
	});

}

function getDoApplyPanel(){
	var txurl = window.location.href;				
	if(txurl.indexOf('typeNum=2')!=-1){
		var title = "手机号码查看申请处理";
		var applyContent = "USER_MOBILE=1";
		if(txurl.indexOf('USER_RESUME=1')!=-1){
			title = "简历查看申请处理";
			applyContent="USER_RESUME=1"
		}
		var data_id = txurl.substr((txurl.indexOf('DATA_ID')));
		var dataArray = data_id.split("=");
		var applyData = FireFly.byId("SY_COMM_ADDRESS_APPLY", dataArray[1]);
		jQuery("#user-info-to-apply-dialog").remove();
		var dialogId = "user-info-to-apply-dialog"; 
		var winDialog = jQuery("<div style='padding: 5px 5px 5px 5px;'></div>").attr("id", dialogId).attr("title",title);
		winDialog.appendTo(jQuery("body"));
		var bodyWid = jQuery("body").width();
		var hei = 200;
		var wid = 300;
		var posArray = [ 700, 100 ];
		jQuery("#" + dialogId).dialog({
			autoOpen : false,height : hei,width : wid,modal : true,show:"blud",hide:"blue",draggable:true,
			resizable : false,position : posArray,
			buttons: {
				"通过申请": function() {
					var data = {};
					data["APPLY_ID"]=dataArray[1];
					data["APPLY_CONTENT"] = applyContent;
					data["USER_AUDIT_FLAG"]="1";
					FireFly.doAct('SY_COMM_ADDRESS_APPLY','doApply',data);
					jQuery("#" + dialogId).remove();
				},
				"不同意": function() {
					var data = {};
					data["APPLY_ID"]=dataArray[1];
					data["APPLY_CONTENT"] = applyContent;
					data["USER_AUDIT_FLAG"]="2";
					FireFly.doAct('SY_COMM_ADDRESS_APPLY','doApply',data);
					jQuery("#" + dialogId).remove();
				}
		},
			open : function() {},
			close : function() {jQuery("#" + dialogId).remove();}
		});

		var dialogObj = jQuery("#" + dialogId);
		dialogObj.dialog("open");
		dialogObj.focus();
		jQuery(".ui-dialog-titlebar").last().css("display", "block");
		dialogObj.parent().addClass("rh-bottom-right-radius rhSelectWidget-content");
		Tip.showLoad("努力加载中...", null, jQuery(".ui-dialog-title", winDialog).last());
		var btns = jQuery(".ui-dialog-buttonpane button",dialogObj.parent()).attr("onfocus","this.blur()");
		btns.first().addClass("rh-small-dialog-ok");
		btns.last().addClass("rh-small-dialog-close");
		dialogObj.parent().addClass("rh-small-dialog").addClass("rh-bottom-right-radius");
	    jQuery(".ui-dialog-titlebar").last().css("display","block");
		
		jQuery("<div id='dialog-user' style='margin-left:5%'></div>").appendTo(dialogObj);
		jQuery("<hidden id = dialog-user-code></hidden>").val(applyData.USER_CODE).appendTo(dialogObj);

		var usDetail = FireFly.byId("SY_ORG_USER",applyData.USER_CODE);
		var applyusDetail=FireFly.byId("SY_ORG_USER",applyData.APPLY_USER);
		
		jQuery("<div id='dialog-user' style='margin-left:5%'></div>").appendTo(dialogObj);
		jQuery("<hidden id = dialog-user-code></hidden>").val(System.getVar("@USER_CODE@")).appendTo(dialogObj);
		jQuery("<hidden id = dialog-apply-user-code></hidden>").val(applyusDetail.USER_CODE).appendTo(dialogObj);
		$("<ul><li class='user-all-info-lab-li'>申请人："+applyusDetail.USER_NAME+"</li><li class='user-all-info-lab-li'>备注：</li><li><textarea id = 'apply_mark_content' cols='38' rows='6'>"+applyData.APPLY_MARK+"</textarea></li><ul>").appendTo(jQuery("#dialog-user"));
			
	}else if(txurl.indexOf('typeNum=3')!=-1){
		var title = "手机号码查看申请反馈";
		var applyContent = "USER_MOBILE=1";
		if(txurl.indexOf('USER_RESUME=1')!=-1){
			title = "简历查看申请反馈";
			applyContent="USER_RESUME=1"
		}
		var data_id = txurl.substr((txurl.indexOf('DATA_ID')));
		var dataArray = data_id.split("=");
		var applyData = FireFly.byId("SY_COMM_ADDRESS_APPLY", dataArray[1]);
		var usDetail = FireFly.byId("SY_ORG_USER",applyData.USER_CODE);
		var applyusDetail=FireFly.byId("SY_ORG_USER",applyData.APPLY_USER);
		jQuery("#user-info-to-apply-dialog").remove();
		var dialogId = "user-info-to-apply-dialog"; 
		var winDialog = jQuery("<div style='padding: 5px 5px 5px 5px;'></div>").attr("id", dialogId).attr("title",title);
		winDialog.appendTo(jQuery("body"));
		var bodyWid = jQuery("body").width();
		var hei = 170;
		var wid = 300;
		var posArray = [ 700, 100 ];
		jQuery("#" + dialogId).dialog({
			autoOpen : false,height : hei,width : wid,modal : true,show:"blud",hide:"blue",draggable:true,
			resizable : false,position : posArray,
			buttons: {
				"关  闭":function(){
					var data = {};
					data["APPLY_ID"]=dataArray[1];
					data["SEND_USER"] = applyData.USER_CODE;
					data["OWNER_USER"]= applyData.APPLY_USER;
					FireFly.doAct('SY_COMM_ADDRESS_APPLY','doCloseApply',data);
					jQuery("#" + dialogId).remove();
				}
			},
			open : function() {},
			close : function() {
				var data = {};
				data["APPLY_ID"]=dataArray[1];
				data["SEND_USER"] = applyData.USER_CODE;
				data["OWNER_USER"]= applyData.APPLY_USER;
				FireFly.doAct('SY_COMM_ADDRESS_APPLY','doCloseApply',data);
				jQuery("#" + dialogId).remove();
			}
		});

		var dialogObj = jQuery("#" + dialogId);
		dialogObj.dialog("open");
		dialogObj.focus();
		jQuery(".ui-dialog-titlebar").last().css("display", "block");
		dialogObj.parent().addClass("rh-bottom-right-radius rhSelectWidget-content");
		Tip.showLoad("努力加载中...", null, jQuery(".ui-dialog-title", winDialog).last());
		var btns = jQuery(".ui-dialog-buttonpane button",dialogObj.parent()).attr("onfocus","this.blur()");
		btns.first().addClass("rh-small-dialog-ok");
		btns.last().addClass("rh-small-dialog-close");
		dialogObj.parent().addClass("rh-small-dialog").addClass("rh-bottom-right-radius");
	    jQuery(".ui-dialog-titlebar").last().css("display","block");
		
		jQuery("<div id='dialog-user' style='margin-left:5%'></div>").appendTo(dialogObj);
		
		jQuery("<div id='dialog-user' style='margin-left:5%'></div>").appendTo(dialogObj);
		jQuery("<hidden id = dialog-user-code></hidden>").val(System.getVar("@USER_CODE@")).appendTo(dialogObj);
		jQuery("<hidden id = dialog-apply-user-code></hidden>").val(applyusDetail.USER_CODE).appendTo(dialogObj);
		$("<ul><li class='user-all-info-lab-li'>反馈：</li><li><textarea  cols='38' rows='6'>"+applyusDetail.USER_NAME+","+usDetail.USER_NAME+"未同意您的查看请求"+"</textarea></li><ul>").appendTo(jQuery("#dialog-user"));
	}
};

function createApply(uid,userName,type){
	var title = "简历查看申请";
	var applyContent = "USER_RESUME=1";
	if(type=='phone'){
		title='手机号码查看申请';
		applyContent = "USER_MOBILE=1";
	}
	jQuery("#user-info-to-apply-dialog").remove();
	var dialogId = "user-info-to-apply-dialog"; 
	var winDialog = jQuery("<div style='padding: 5px 5px 5px 5px;'></div>").attr("id", dialogId).attr("title",title);
	winDialog.appendTo(jQuery("body"));
	var bodyWid = jQuery("body").width();
	var hei = 200;
	var wid = 300;
	var posArray = [ 700, 100 ];
	jQuery("#" + dialogId).dialog({
		autoOpen : false,height : hei,width : wid,modal : true,show:"blud",hide:"blue",draggable:true,
		resizable : false,position : posArray,
		buttons: {
			"发送请求": function() {
				var data = {};
				data['USER_CODE']=jQuery("#dialog-user-code").val();
				data['APPLY_USER']=jQuery("#dialog-apply-user-code").val();
				data["APPLY_CONTENT"] = applyContent;
				data["APPLY_MARK"] = jQuery("#apply_mark_content").val();
				FireFly.doAct('SY_COMM_ADDRESS_APPLY','applyAdd',data);
				jQuery("#" + dialogId).remove();
			},
			"取消": function() {
				jQuery("#" + dialogId).remove();
			}
		},
		open : function() {},
		close : function() {jQuery("#" + dialogId).remove();}
	});

	var dialogObj = jQuery("#" + dialogId);
	dialogObj.dialog("open");
	dialogObj.focus();
	jQuery(".ui-dialog-titlebar").last().css("display", "block");
	dialogObj.parent().addClass("rh-bottom-right-radius rhSelectWidget-content");
	Tip.showLoad("努力加载中...", null, jQuery(".ui-dialog-title", winDialog).last());
	var btns = jQuery(".ui-dialog-buttonpane button",dialogObj.parent()).attr("onfocus","this.blur()");
	btns.first().addClass("rh-small-dialog-ok");
	btns.last().addClass("rh-small-dialog-close");
	dialogObj.parent().addClass("rh-small-dialog").addClass("rh-bottom-right-radius");
    jQuery(".ui-dialog-titlebar").last().css("display","block");
	
	jQuery("<div id='dialog-user' style='margin-left:5%'></div>").appendTo(dialogObj);
	jQuery("<hidden id = dialog-user-code></hidden>").val(uid).appendTo(dialogObj);
	jQuery("<hidden id = dialog-apply-user-code></hidden>").val(System.getVar("@USER_CODE@")).appendTo(dialogObj);
	$("<ul><li class='user-all-info-lab-li'>申请人："+System.getVar("@USER_NAME@")+"</li><li class='user-all-info-lab-li'>备注：</li><li><textarea id = 'apply_mark_content' cols='38' rows='6'></textarea></li><ul>").appendTo(jQuery("#dialog-user"));	
}

function openTxl(){
	var params = {};
	var options = {"url":"SY_COMM_ADDRESS_LIST.list.do","tTitle":"公司通讯录","params":params,"menuFlag":3};
	Tab.open(options);
}
