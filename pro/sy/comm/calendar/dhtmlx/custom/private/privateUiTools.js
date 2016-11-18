/*封装的日程方法*/

var ExtCalendar = new Object();

//日程_获取后台下属用户信息
ExtCalendar.getBelongUsers = function(){
	var result = FireFly.doAct("SY_COMM_BELONG","getBelongUsers",{});
	return result;
};

//日程_获取后台共享用户信息
ExtCalendar.getShareUsers = function(){
	var result = FireFly.doAct(MapBean.extServerId, "getShareUsers",{});
	return result;
};

//日程_配置下属视图
ExtCalendar.setBelongView = function(){
	var users = ExtCalendar.belongUserTree._userArray;
	if(users.length > 0){
		jQuery('<div class="dhx_cal_tab" name="unit_tab" style="right:280px;"></div>').appendTo(jQuery(".dhx_cal_navline"));
	}else{
		return;
	}
	var belongUserSections = new Array();
	for(var i=0;i < users.length;i++){
		belongUserSections[i] = {"key":users[i].S_USER, "label":users[i].S_USER__NAME};
	}
	scheduler.createUnitsView("unit", "S_USER", belongUserSections, 5, 5, true);
	scheduler.locale.labels.unit_tab = "下属";
};

//日程_弹出详细页
ExtCalendar.showCalendarCard = function(eventId, eventObj){
	Calendar.cardOpts = {};
	Calendar.cardOpts.currEvent = eventObj; 	
	//打开迷你卡片页面	
	var options = {"sId": MapBean.extServerId,"widHeiArray": [1000,500],"xyArray": [50,50]};
	var opts = {"act":UIConst.ACT_CARD_ADD,"parHandler":null}
	if(eventObj["CAL_ID"] && eventObj["CAL_ID"]!=""){
		opts[UIConst.PK_KEY] = eventObj["CAL_ID"];
		opts["act"] = UIConst.ACT_CARD_MODIFY;	
	}
	//删除按钮对象
	var deleteBtn = {
		ACT_CODE: "deleteEve",
		ACT_CSS: "delete",
		ACT_EXPRESSION: "",
		ACT_EXPRESSION_FLAG: "",
		//ACT_MEMO: "(Calendar.currDeleteMethod)(null, '#CAL_ID#');",
		ACT_MEMO: "Calendar.deleteData(Calendar.cardOpts.currEvent, true)",
		ACT_NAME: "删除日程",
		ACT_ORDER: "0",
		ACT_TYPE: "2",
		ACT_WS_FLAG: "2",
		ACT_WS_RESULT: "",
		S_FLAG: "1"
	}
	//初始化卡片引擎
	var cardView = new rh.vi.cardView(jQuery.extend(opts,options));		
	cardView._data.BTNS.deleteEve = deleteBtn;
	Calendar.cardOpts.currCard = cardView; 
	//展示卡片
	cardView.show();
	
	if(cardView.getItem("CAL_START_DATE")){cardView.getItem("CAL_START_DATE").setValue(convertToDate(scheduler.getEventStartDate(eventId)));}
	if(cardView.getItem("CAL_START_TIME")){cardView.getItem("CAL_START_TIME").setValue(convertToTime(scheduler.getEventStartDate(eventId)));}
	if(cardView.getItem("CAL_END_DATE")){cardView.getItem("CAL_END_DATE").setValue(convertToDate(scheduler.getEventEndDate(eventId)));}
	if(cardView.getItem("CAL_END_TIME")){cardView.getItem("CAL_END_TIME").setValue(convertToTime(scheduler.getEventEndDate(eventId)));}
	
	if(!eventObj.CAL_ID){
		//jQuery("[actcode='deleteEve']").hide();
	}
	//保存之后
	cardView.afterSave = function(resultData){
		eventObj.text = resultData.text;
		eventObj.CAL_ID = resultData.CAL_ID;
		eventObj.CAL_TITLE = resultData.CAL_TITLE;
		eventObj.CAL_START_DATE = resultData.CAL_START_DATE;
		eventObj.CAL_START_TIME = resultData.CAL_START_TIME;
		eventObj.CAL_END_DATE = resultData.CAL_END_DATE;
		eventObj.CAL_END_TIME = resultData.CAL_END_TIME;
		eventObj.CAL_TYPE = resultData.CAL_TYPE;
		eventObj.CAL_CONTENT = resultData.CAL_CONTENT;
		eventObj.start_date = convertToUtc(resultData.CAL_START_DATE + " " + resultData.CAL_START_TIME);
		eventObj.end_date = convertToUtc(resultData.CAL_END_DATE + " " + resultData.CAL_END_TIME);
		eventObj.type = resultData.CAL_TYPE;
	    scheduler.updateEvent(eventId);
	}
};

//日程_用户列表组件
ExtCalendar.userTree = function(options){
	if(options.act == "belong"){
		this._userArray = ExtCalendar.getBelongUsers()._DATA_;//下属用户信息
	}else{
		this._userArray = ExtCalendar.getShareUsers()._DATA_;//共享用户信息
	}
	this._act = options.act;
	this._containerDiv = null;//列表容器
	this._headDiv = null;//列表头部
	this._headSpan = null;
	this._bodyDiv = null;//列表容器
	this._ulObj = null;
	this._liObjArray = new Array();
	this._uimgSpanObjArray = new Array();
	this._userSpanObjArray = new Array();
	this._dimgSpanObjArray = new Array();
	this._deptSpanObjArray = new Array();
};

//日程_构造用户列表jquery dom
ExtCalendar.userTree.prototype.renderUserTree = function(){
	var _self = this;
	var conId = this._act == "belong"?"belongUserTree_container":"shareUserTree_container";
	this._containerDiv = jQuery("#" + conId);
	//构造头部
	this._headDiv = jQuery("<div></div>").addClass(MapBean.extServerId + "_userTree_headDiv");
	var headTitle = this._act == "belong"?"我的下属":"我的共享用户";
	this._headSpan = jQuery("<span></span>").addClass(MapBean.extServerId + "_userTree_headSpan").text(headTitle);
	this._headSpan.appendTo(this._headDiv);
	this._headDiv.appendTo(this._containerDiv);
	//构造内容
	this._bodyDiv = jQuery("<div></div>").addClass(MapBean.extServerId + "_userTree_bodyDiv");
	if(!this._userArray || this._userArray.length == 0){
		var warnStr = this._act == "belong"?"没用下属":"没有共享用户";
		jQuery("<span></span>").text(warnStr).css({position:"relative",left:"5px"}).appendTo(this._bodyDiv);
		this._bodyDiv.appendTo(this._containerDiv);
		return;
	}
	this._ulObj = jQuery("<ul></ul>").addClass(MapBean.extServerId + "_userTree_ul");
	for(var i = 0;i < this._userArray.length; i++){
		var _userCode = this._act == "belong"?this._userArray[i].S_USER:this._userArray[i].userCode;
		var _userName = this._act == "belong"?this._userArray[i].S_USER__NAME:this._userArray[i].userName;
		var _deptName = this._act == "belong"?"":this._userArray[i].deptName;
		this._liObjArray[i] = jQuery("<li></li>");
		this._liObjArray[i].addClass(MapBean.extServerId + "_userTree_li").attr("id", MapBean.extServerId + "_userTree_li_" + _userCode);
		this._liObjArray[i].unbind("click").bind("click",{"code":_userCode,"name":_userName},function(event){
			Calendar.setCurrentUsers({"code":event.data.code, "name":event.data.name});
			Calendar.updateView(null, "week");
		});
		this._uimgSpanObjArray[i] = jQuery("<span></span>").addClass(MapBean.extServerId + "_userTree_uimgSpan");
		this._userSpanObjArray[i] = jQuery("<span></span>").text(_userName);
		this._userSpanObjArray[i].addClass(MapBean.extServerId + "_userTree_userSpan").attr("id", MapBean.extServerId + "_userTree_userSpan_" + _userCode);
		this._uimgSpanObjArray[i].appendTo(jQuery(this._liObjArray[i]));
		this._userSpanObjArray[i].appendTo(jQuery(this._liObjArray[i]));
		if(this._act == "share"){
			this._dimgSpanObjArray[i] = jQuery("<span></span>").addClass(MapBean.extServerId + "_userTree_dimgSpan");
			this._deptSpanObjArray[i] = jQuery("<span></span>").text(_deptName+"中华人名共和国中央人民政府");
			this._deptSpanObjArray[i].addClass(MapBean.extServerId + "_userTree_deptSpan").attr("id", MapBean.extServerId + "_userTree_deptSpan_" + _userCode);	
			this._dimgSpanObjArray[i].appendTo(jQuery(this._liObjArray[i]));
			this._deptSpanObjArray[i].appendTo(jQuery(this._liObjArray[i]));
		}
		this._liObjArray[i].appendTo(this._ulObj);
	}
	this._ulObj.appendTo(this._bodyDiv);
	this._bodyDiv.appendTo(this._containerDiv);
};