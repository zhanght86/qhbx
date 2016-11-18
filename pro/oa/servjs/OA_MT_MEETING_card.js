var _viewer = this;

//获取指定节点中的自定义变量值
var isShowPs = _viewer.wfCard.getCustomVarContent("ADD_MIND") || "";
var cancelBtn = ""; //取消会议室流程按钮变量
var sendCardMsg = ""; //发送会议通知流程按钮变量

/**
 * 根据当前用户修改字典信息；如果当前用户是总公司用户，则替换字典，否则不替换
 */
if (System.getVar("@ODEPT_LEVEL@") == 2) {
	var chbmObj = _viewer.getItem("COMPLEADER"); //参会人员
	chbmObj.beforeGetConfigVal = function(){
		var _self = this;
		_self._opts.item_input_config = "SY_ORG_DEPT_ALL,{\"TYPE\":\"multi\"}";
	};
	var yhry = _viewer.getItem("CONFEREES_CODES"); //与会人员
	yhry.beforeGetConfigVal = function(){
		var _self = this;
		_self._opts.item_input_config = "SY_ORG_DEPT_USER_ALL,{\"TYPE\":\"multi\"}";
	};
	var lxry = _viewer.getItem("NOTIFIED_CODES"); //列席人员
	lxry.beforeGetConfigVal = function(){
		var _self = this;
		_self._opts.item_input_config = "SY_ORG_DEPT_USER_ALL,{\"TYPE\":\"multi\"}";
	};
}

//判断参会部门合参会人员某一个必填
//修改操作
settingNotNullItems();

//跨机构处理会议室名称
/*var mrName = _viewer.getItem("PLACE");
if (mrName.getValue().length > 0) {
	var out = FireFly.doAct(_viewer.servId, "getMtRoomName", {"MR_ID":mrName.getValue()});
	if (out["_MSG_"].indexOf("OK") >= 0) {
		mrName.setText(out["NAME"]);
	}
}*/

//当前节点没有修改[审批状态]的权限时，将字段置为只读
if (isShowPs != "true") {
	_viewer.getItem("STATUS").disabled();
} else {
	_viewer.getItem("STATUS").show();
	if (_viewer.getItem("STATUS").getValue() == "11") {
		_viewer.getItem("STATUS").setText("已取消");
	}
}

_viewer.setParentRefresh(); //点击返回刷新父页面

/**字段配置--设置BOOKING_FLAG<会议室预定标志>字段下拉框的值*/
if(_viewer.links != null){
	//为“不预定时”，解除只读
	if(_viewer.links.roomtype){
		if(_viewer.links.roomtype != '3'){
			_viewer.getItem("PLACE").obj.attr("readonly","true");
		}
		_viewer.getItem("BOOKING_FLAG").setValue(_viewer.links.roomtype);
	} else if (_viewer.links["HAVE_NOTICE"]){ //存在通知标志
		_viewer.getItem("PLACE").disabled();
		_viewer.getItem("BEGIN_TIME").disabled();
		_viewer.getItem("END_TIME").disabled();
	}
}

/**字段配置--设置CONFEREES_CODES<本单位与会人员ID>字段调用树形字典 的回调函数*/
_viewer.getItem("CONFEREES_CODES").callback = function(id,value) {
	_viewer.getItem("CONFEREES_NAMES").setValue(value);
	if(value !="" && _viewer.itemValue("NOTIFIED_NAMES")){
		var cUserStr = _compareTwoNoticeUsers();
		if(cUserStr !=""){
			_viewer.cardBarTipError(cUserStr + "只能在与会人员和被通知人中选中一次！！！");
		}
	}
	//判断当前输入框中有没有输入部门code
	isExiteDeptCode(id.join(","), value.join(",") ,"CONFEREES_CODES");
};

/**字段配置--设置NOTIFIED_CODES<被通知人ID>字段调用树形字典 的回调函数*/
_viewer.getItem("NOTIFIED_CODES").callback = function(id,value) {
	_viewer.getItem("NOTIFIED_NAMES").setValue(value);
	if(value !="" && _viewer.itemValue("CONFEREES_NAMES")){
		var cUserStr = _compareTwoNoticeUsers();
		if(cUserStr !=""){
			_viewer.cardBarTipError(cUserStr + "只能在与会人员和被通知人中选中一次！！！");
		}
	}
	//判断当前输入框中有没有输入部门code
	isExiteDeptCode(id.join(","), value.join(","), "NOTIFIED_CODES");
};

/**判断是否获取工作流按钮对象，工作流卡片中才生效*/
if (_viewer.wfCard) {
	cancelBtn = _viewer.wfCard._getBtn('cancelMt');
	sendCardMsg = _viewer.wfCard._getBtn('sendCardMsg');
}

showBtns(); //显示[发送通知]，[变更通知]，[取消通知]相应的操作按钮

_viewer.afterSave = function(resultData){
	if (resultData["_MSG_"].indexOf("OK") >= 0) {
		if (_viewer.links != null) {
			if (_viewer.links["HAVE_NOTICE"]) {
				var saveData = {"PK_CODE":_viewer.links["BOOKING_ID"],
								"SERV_ID":"OA_MT_BOOKING","NOTICE_FLAG":"1"};
				var outBean = FireFly.doAct("OA_MT_BOOKING", "updataBooking", saveData);
				if (outBean["_MSG_"].indexOf("OK") >= 0) {
					return true;
				} else {
					return false;
				}
			}
		}
	}
	
};

//添加常用组链接
if (_viewer.getItem("CONFEREES_CODES")._choose.hasClass("icon-input-user")) {
	addItemGroup("CONFEREES_CODES,NOTIFIED_CODES");
}

/*	var editBtnsFlag = _viewer._judgeEditBtns();
 	if (!(_viewer._readOnly == true || _viewer._readOnly == "true"　|| editBtnsFlag == false)) {
	addItemGroup("CONFEREES_CODES,NOTIFIED_CODES");
}*/

//清除操作
_viewer.getItem("CONFEREES_CODES")._cancel.unbind("click").bind("click", function(){
	clearNotNullItems(this, "CONFEREES_CODES", "COMPLEADER");
});

_viewer.getItem("COMPLEADER")._cancel.unbind("click").bind("click", function(){
	clearNotNullItems(this, "COMPLEADER", "CONFEREES_CODES");
});

//弹出常用组选择查询
function showFrequenUserGroup(itemCode, thisObj){
	var configStr = "OA_FREQUENT_USER_GROUP,{'SOURCE':'TITLE~USER_CODES~USER_NAMES','TARGET':'~~"
			 	  + itemCode + "','TYPE':'single','HIDE':'USER_NAMES','EXTWHERE':\" and S_FLAG = '1'\"}";
	var options = {
			"config":configStr,
			"searchFlag":false,
			"replaceCallBack":function(idArray){
				_viewer.getItem(itemCode).setText(idArray["USER_NAMES"]);
				_viewer.getItem(itemCode).setValue(idArray["USER_CODES"]);
				if (!isExiteDeptCode(idArray["USER_CODES"], idArray["USER_NAMES"] ,itemCode)){
					return;
				}
				if (!isExiteDeptCode(idArray["USER_CODES"], idArray["USER_NAMES"] ,itemCode)){
					return;
				}
				settingNotNullItems(thisObj, itemCode);
			}
		};
	var queryView = new rh.vi.rhSelectListView(options);
	queryView.show(event);
}

//添加常用组链接
function addItemGroup(itemCodes){
	var codesArray = itemCodes.split(",");
	for (var i = 0; i < codesArray.length; i++) {
		//常用组对象
		var meetingGroup = jQuery("<a href='javascript:void(0);'><font color='blue'>常用组</font></a>");
		meetingGroup.css({"position":"absolute","right":"42px","top":"50px"});
		var CodesParent = _viewer.getItem(codesArray[i]).obj;
		//类型不是字典选择的
		if ("DictChoose" != (_viewer.getItem(codesArray[i]).type || "")) {
			CodesParent.parent().css({"position":"relative"});
			meetingGroup.css({"right":"2px","top":"59px"});
		} else {
			meetingGroup.unbind("click").bind("click", {"itemCode":codesArray[i]}, function(event){
				showFrequenUserGroup(event.data.itemCode, this);
			});
		}
		CodesParent.after(meetingGroup);
	}
}

//[本单位与会人员/被通知人]中是否存在部门code，存在去除，并提示
function isExiteDeptCode (ids, value, code) {
	var outBeanObj = FireFly.doAct(_viewer.servId, "isExiteDeptCodes",{"CODES":ids,"VALUE":value});
	if (outBeanObj["OK"] == "false") {
		_viewer.getItem(code).setValue(outBeanObj["CODE"]);
		_viewer.getItem(code).setText(outBeanObj["VALUE"]);
		_viewer.cardBarTipError(outBeanObj["NAME"] + "是部门，不是用户，请从新选择！！！");
		return false;
	}
	return true;
}

/**内部函数--调用<会议室查询统计>服务，预定新会议室*/
function _addMeetingroomServ(event){
	/*解决方案一：打开新标签方式*/	
	//传递参数
	var startTime = _viewer.itemValue("BEGIN_TIME");
	var endTime = _viewer.itemValue("END_TIME");
	var pSid = "OA_MT_MEETING";
	var handler = _viewer;
	var params = {
				"links":{"callFlag":"OA_MT_MEETING"},//需要传到后台的参数放到里面
				"mtData":{//会议室查询统计服务需要的参数，不经过后台的参数
						"BEGIN_TIME":startTime,//会议开始时间
						"END_TIME":endTime,//会议结束时间
						"pSid":pSid//传给子服务的父服务（本服务）ID
						},
				"handler":_viewer,//当前文档句柄供新页面回调用
				};
	var options = {
				"url":"OA_MT_MEETINGROOM_V.list.do",
				"tTitle":"会议室预定申请",
				"params":params,
				"menuFlag":3
				};
	Tab.open(options);
}

/**内部函数--查询本人已选定且未启用的会议室预定记录*/
function _selectMeetingroomServ(event){
	/*解决方案一：查询选择方式  用系统的查询选择组件 rh.vi.rhSelectListView()*/
	//构造查询选择参数
	var sid = "OA_MT_BOOKING";
	var confBean = {
		'TARGET':'PLACE~MR_ID~~~~BOOKING_ID~BEGIN_TIME~END_TIME',//调用查询选择页面的字段组（被列其中的字段会被回写以“~”分隔的“source”参数中的字段值）
		'SOURCE':'MR_ID__NAME~MR_ID~BOOKING_LEVEL~ATTENDANCE~BZR~BOOKING_ID~START_TIME~END_TIME',//被查询选择的服务需要展示的字段（PKHIDE为true时主键不展示，其次列表字段服务中设为不隐藏有数据时对应字段不展示）
		'EXTWHERE':" and STATUS < 11 and START_TIME >= SUBSTR('@DATETIME@',0,16) and S_USER = '@USER_CODE@' and BOOKING_ID not in (select BOOKING_ID from OA_MT_MEETING where S_USER = '@USER_CODE@' and STATUS < 10 and BOOKING_ID <>'')",//后台列表查询时的where条件，其中例如想利用后台的系统时间则可以拼写@DATETIME@系统变量，其传送到后台后会被替换成当时的系统时间
		'TYPE':'single',//single:单选，multi:多选
		'PKHIDE':true,//是否展示主键
		'ADDBTN':false,//是否显示添加按钮
		'DELETEBTN':false,//是否显示删除按钮
		'MODIFYBTN':false,//是否显示修改按钮
		'SPLIT':false,//是否用替换的分割符
		'SEARCHTYPE':null,//可选，只在高级查询起作用，高级查询里显示多选；
		'SEARCHEXTWHERE':null,//高级查询里的查询选择的覆盖条件；
		'SEARCHHIDE':null,//可选，只在高级查询时起作用，高级查询里显示与否；
		'DATAFLAG':false//是否显示列表内容，打开选择页面默认不显示列表数据；
	};
	var configStr = sid + "," + JsonToStr(confBean);
	var options = {
		"params":null,// 扩展参数对象
		"config":configStr,//confBean
		"searchFlag":true,//
		"linkWhere":null,//关联功能过滤条件
		"links":null,//关联功能过滤条件
		"parHandler":_viewer,//
		"formHandler":_viewer,//启用回写功能是需要被回写的页面或页面中form表单的句柄都可以，
		"replaceCallBack":function(idArray){//回调，idArray为选中记录的相应字段组的json串
		}
	};
	var queryView = new rh.vi.rhSelectListView(options);
	queryView.show(event);
}

/**内部函数--比较 “本单位与会人员名单” 和 “被通知人”是否存在交集并返回交集*/
function _compareTwoNoticeUsers(){
	var conferUserArray = _viewer.itemValue("CONFEREES_NAMES").split(",");
	var noticeUserArray = _viewer.itemValue("NOTIFIED_NAMES").split(",");
	var lArray = new Array();
	var sArray = new Array();
	var cUsersStr = "";
	if(conferUserArray.length >= noticeUserArray.length){
		lArray = conferUserArray;
		sArray = noticeUserArray;
	}else{
		sArray = conferUserArray;
		lArray = noticeUserArray;
	}
	for(i=0;i<lArray.length;i++){
		for(j=0;j<sArray.length;j++){
			if(lArray[i]==sArray[j]){
				cUsersStr = cUsersStr + " " + lArray[i];
				continue;
			}
		}
	}
	return cUsersStr;
}

//按钮显示
function showBtns(){
	var notice_flag = _viewer.getItem("NOTICE_FLAG").getValue();
	var status_flag = _viewer.getItem("STATUS").getValue();
	if (status_flag == "2") { //如果审批通过，发送通知
		if (notice_flag == "1") { //如果已通知
			if (sendCardMsg) { //当前节点存在 [发送通知]按钮
				sendCardMsg.layoutObj.remove(); //已通知，则删除此按钮
				if (status_flag == "11") { //当前单子被取消，删除[取消通知]和[变更通知]按钮
					_viewer.wfCard._getBtn('cancelNotice').layoutObj.remove();
					_viewer.wfCard._getBtn('changeNotice').layoutObj.remove();
				} else {
					_viewer.wfCard._doWfBtnBeforeClick(function(){
						//给[取消通知]按钮绑定事件
						var data = {"TODO_TITLE":"[会议通知取消]" + _viewer.getItem("TITLE").getValue(),
									"BOOKING_ID":_viewer.getItem("BOOKING_ID").getValue()
								   };
						var reMindTitle = "预计" + _viewer.getItem("BEGIN_TIME").getValue() + "至" + 
										_viewer.getItem("END_TIME").getValue() + "召开的" + _viewer.getItem("TITLE").getValue() +
										"会议通知取消原因为：";
						callBackBtnFunc(data, "cancelNotice", "是否确定取消会议通知？", "会议通知取消成功", "", reMindTitle, true);
					},_viewer.wfCard._getBtn('cancelNotice'));
					_viewer.wfCard._doWfBtnBeforeClick(function(){
						//给[变更通知]按钮绑定事件
						var data = {"TODO_TITLE":"[会议通知变更]" + _viewer.getItem("TITLE").getValue()};
						var reMindTitle = "预计" + _viewer.getItem("BEGIN_TIME").getValue() + "至" + 
										_viewer.getItem("END_TIME").getValue() + "召开的" + _viewer.getItem("TITLE").getValue() +
										"会议通知变更原因为：";
						callBackBtnFunc(data, "sendMeetingRemindMsg", "确定已变更会议通知，并已保存变更数据？", "会议变更通知成功", "变更原因", reMindTitle);
					},_viewer.wfCard._getBtn('changeNotice'));
				}
			}
		} else {
			if (sendCardMsg) { //当前节点可以发会议通知
					_viewer.wfCard._getBtn('cancelNotice').layoutObj.remove();
					_viewer.wfCard._getBtn('changeNotice').layoutObj.remove();
					_viewer.wfCard._doWfBtnBeforeClick(function(){
					sendNoticeFunc(_viewer.getItem("MEETING_ID").getValue());
				},sendCardMsg);
			}
		}
	} else {
		if (_viewer.wfCard._getBtn('cancelNotice')) {
			_viewer.wfCard._getBtn('cancelNotice').layoutObj.remove();
		}
		if (_viewer.wfCard._getBtn('changeNotice')) {
			_viewer.wfCard._getBtn('changeNotice').layoutObj.remove();
		}
		if (sendCardMsg) {
			sendCardMsg.layoutObj.remove();
		}
	}
	if (cancelBtn) {
		//没有预定会议室，则没有[取消会议室]操作,并且已做取消操作
		if (_viewer.itemValue("BOOKING_FLAG") != 2) {
			cancelBtn.layoutObj.remove(); //删除
		} else {
			_viewer.wfCard._doWfBtnBeforeClick(function(){
				var data = {"TODO_TITLE":"[会议室取消]" + _viewer.getItem("TITLE").getValue(), 
							"BOOKING_ID":_viewer.getItem("BOOKING_ID").getValue()
						   };
				var reMindTitle = "预计" + _viewer.getItem("BEGIN_TIME").getValue() + "至" + 
										_viewer.getItem("END_TIME").getValue() + "召开的" + _viewer.getItem("TITLE").getValue() +
										"会议室原因为：";
				callBackBtnFunc(data, "cancelMTBooking", "确定取消会议室预定后，此会议的会议室占用将被取消？", "会议室取消预定成功", reMindTitle);
			}, cancelBtn);
		}
	}
}

//此页面上受控制的流程按钮调用公共方法
function callBackBtnFunc(data, act, confirmStr, tipOKMsg, title, remindTitle, calcenTodo) {
	if (confirm(confirmStr)) {
		var textAreaObj = jQuery("<textarea style='width:100%;height:135px;'></textarea>");
		textAreaObj.val(remindTitle);
		showRHDialog(title || "取消原因", textAreaObj, function(){
			if (textAreaObj.val() != remindTitle && textAreaObj.val() != "") {
				data = jQuery.extend(data, {
					   "TODO_CONTENT" :textAreaObj.val(),
					   "MEETING_ID": _viewer.getItem("MEETING_ID").getValue()
				});
				var cancelNoticeBean = FireFly.doAct("OA_MT_MEETING", act, data);
				if (cancelNoticeBean["_MSG_"].indexOf("OK") >= 0) {
					//去除当前起草人的待办事务
					if (calcenTodo) {
						var result = rh_processData("SY_WFE_PROC_DEF.finish.do", _viewer.wfCard.reqdata);
						if (result.rtnstr = "success") {
							_viewer.cardBarTip(tipOKMsg);
							_viewer.backClick();
						}
					} else {
						_viewer.cardBarTip(tipOKMsg);
						_viewer.backClick();
					}
				} else {
					_viewer.cardBarTipError(cancelNoticeBean["_MSG_"].replace("ERROR,", ""));
				}
			} else {
				_viewer.cardBarTipError("请填写 " + (title || "取消原因"));
				return false;
			}
		}, _viewer);
	}
}

//发送会议通知
function sendNoticeFunc(meetingId) {
	var data = {"MEETING_ID":meetingId,
				"TODO_TITLE":"[会议通知]" + _viewer.getItem("TITLE").getValue(),
				"CONFEREES":_viewer.getItem("CONFEREES_CODES").getValue() || "",
				"NOTIFIED":_viewer.getItem("NOTIFIED_CODES").getValue() || "",
				"S_EMERGENCY":_viewer.getItem("S_EMERGENCY").getValue(),
				"COMPLEADER":_viewer.getItem("COMPLEADER").getValue() || ""
				};
	if (confirm("是否确定发送会议通知？")) {
		var outBean = FireFly.doAct("OA_MT_MEETING","sendMeetingNoticeMsg",data);
		if (outBean["_MSG_"].indexOf("OK") >= 0) {
			_viewer.cardBarTip("通知发送成功");
			_viewer.refresh();
		} else {
			_viewer.cardBarTipError(outBean["_MSG_"].relpacl("ERROR," ,""));
		}
	}
}

//重新设置必填联动
function settingNotNullItems(thisObj, itemCode){
    var compLeader = _viewer.getItem("COMPLEADER").getValue() || ""; //获取参会部门数值
    var conferees_codes = _viewer.getItem("CONFEREES_CODES").getValue() || ""; //获取参会人员数值
    if (_viewer._actVar == UIConst.ACT_CARD_MODIFY) {
        //如果参会部门没有值
        if ("" == compLeader) {
            //如果参会人员不为空，则不显示必填
            if ("" != conferees_codes) {
                _viewer.form.setNotNull("COMPLEADER", false);
            }
		//如果参会部门有值
        } else {
            //如果参会人员为空，则不显示必填
            if ("" == conferees_codes) {
                _viewer.form.setNotNull("CONFEREES_CODES", false);
            }
        }
	//添加
    } else if (_viewer._actVar == UIConst.ACT_CARD_ADD) {
		//参会部门
		if (itemCode == "CONFEREES_CODES") {
			var thisObjVal = jQuery(thisObj).parent().find("textarea").eq(0).val() || "";
			if ("" != thisObjVal) {
				_viewer.form.setNotNull("COMPLEADER", false);
				_viewer.form.setNotNull("CONFEREES_CODES", true);
			}
		}
	}
}

//清理字段值的必填联动
function clearNotNullItems(thisObj, itemCode, otherItemCode){
	_viewer.getItem(itemCode).setValue("");
	_viewer.getItem(itemCode).setText("");
	var otherObjVal = _viewer.getItem(otherItemCode).getValue();
	if ("" == otherObjVal) {
		_viewer.form.setNotNull(itemCode, true);
		_viewer.form.setNotNull(otherItemCode, true);
	} else {
		_viewer.form.setNotNull(itemCode, false);
		_viewer.form.setNotNull(otherItemCode, true);
	}
}