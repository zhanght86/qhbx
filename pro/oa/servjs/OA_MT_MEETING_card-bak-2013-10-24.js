/**
 * 
 * @author wangchen
 *
 */

/*********************工程级代码目录**********************
 * 当前作用域句柄
 * 字段配置--设置BOOKING_FLAG<会议室预定标志>字段下拉框的值
 * 字段配置--设置CONFEREES_CODES<本单位与会人员ID>字段调用树形字典 的回调函数
 * 字段配置--设置NOTIFIED_CODES<被通知人ID>字段调用树形字典 的回调函数
 * 选择会议室地点
 * 字段绑定事件--绑定点击<会议地点>输入框的事件,字段绑定图标--绑定点击<会议地点>输入框的内侧后部并复制其事件
 * 判断是否获取工作流按钮对象，工作流卡片中才生效
 * 按钮绑定事件--卡片按钮<保存>插入监听事件
 * 按钮绑定事件--工作流按钮<保存>插入监听事件
 * 按钮绑定事件--工作流按钮<办结>插入监听事件
 * 按钮绑定事件--工作流自定义按钮<取消会议>插入监听事件并绑定事件
 * 内部函数--构建弹出框页面布局
 * 内部函数--构造输入<取消会议>原因对话框
 * 内部函数--调用<会议室查询统计>服务，预定新会议室
 * 内部函数--查询本人已选定且未启用的会议室预定记录
 * 内部函数--提交数据前检查
 * 内部函数--比较 “本单位与会人员名单” 和 “被通知人”是否存在交集并返回交集
 * 知识总结
 */

/**当前作用域句柄*/
var _viewer = this;

//html模板中将输入框width设为100%
//jQuery(".right").css({"width":"100%"});

//html模板中div width设为100%
//jQuery(".right div").css({"width":"100%"});

//输入框宽度
//jQuery(".right input").css({"width":"100%","border":"0px #CCC solid","margin-left":"0px"});

//表格边距
//jQuery(".rh-tmpl-tabel td").css({"padding":"3px 6px 3px 5px"});

//文件头部表格内部输入框
//jQuery(".gwHeaderTable td span").css({"line-height":"2"});

//jQuery(".ui-textarea-default").css({"margin-left":"0px","width":"100%","border":"0px #CCC solid"});

/*修改输入框背景颜色*/
//jQuery(".blank").css({"background-color":"#fbf3e6;","background-image":"none"});
//jQuery(".blank *").css({"background-color":"#fbf3e6;"});

//去除上部[保存]按钮
//jQuery("#OA_MT_MEETING-mainTab .rhCard-btnBar").css({"margin-left":"-1000px","height":"0px"});

//获取指定节点中的自定义变量值
var isShowPs = _viewer.wfCard.getCustomVarContent("ADD_MIND") || "";
if (isShowPs != "true") {
	_viewer.getItem("STATUS").disabled();
}

/**字段配置--设置BOOKING_FLAG<会议室预定标志>字段下拉框的值*/
if(_viewer.links != null && _viewer.links.roomtype){
	//为“不预定时”，解除只读
	if(_viewer.links.roomtype != '3'){
		_viewer.getItem("PLACE").obj.attr("readonly","true");
	}
	_viewer.getItem("BOOKING_FLAG").setValue(_viewer.links.roomtype);
}

/**字段配置--设置CONFEREES_CODES<本单位与会人员ID>字段调用树形字典 的回调函数*/
_viewer.getItem("CONFEREES_CODES").callback = function(id,value) {
	//alert('选择完毕');
	_viewer.getItem("CONFEREES_NAMES").setValue(value);
	//tellBackEndWhichChoosedOnCONFEREES_CODES();
	if(value !="" && _viewer.itemValue("NOTIFIED_NAMES")){
		var cUserStr = _compareTwoNoticeUsers();
		if(cUserStr !=""){
			alert(cUserStr + "只能在与会人员和被通知人中选中一次！！！");
		}
	}
};

/**字段配置--设置NOTIFIED_CODES<被通知人ID>字段调用树形字典 的回调函数*/
_viewer.getItem("NOTIFIED_CODES").callback = function(id,value) {
	//alert('选择完毕');
	_viewer.getItem("NOTIFIED_NAMES").setValue(value);
	if(value !="" && _viewer.itemValue("CONFEREES_NAMES")){
		var cUserStr = _compareTwoNoticeUsers();
		if(cUserStr !=""){
			alert(cUserStr + "只能在与会人员和被通知人中选中一次！！！");
		}
	}
};

/**选择会议室地点*/
function findPlace(event){
	if(_viewer.itemValue("BOOKING_ID") != "" && !confirm("您已经预定了会议室，是否要重新修改？")){
		return;
	}
	if(bookFlag == "1"){
		_addMeetingroomServ(event);//服务id："OA_MT_MEETING"
	}else if(bookFlag == "2"){
		_selectMeetingroomServ(event);//查询本人已选定且未启用的会议室预定记录
	}else{
		//
	}
}

/**字段绑定事件--绑定点击<会议地点>输入框的事件,字段绑定图标--绑定点击<会议地点>输入框的内侧后部并复制其事件*/
var bookFlag = _viewer.itemValue("BOOKING_FLAG");
_viewer.getItem("PLACE").obj.unbind("click").bind("click",findPlace);
if(bookFlag == "1" || bookFlag == "2"){
	_viewer.getItem("PLACE").obj.css({'cursor':'pointer'});
	//插入图标
	var triggerImg = jQuery('<span class="iconChoose icon-input-select" style="cursor: pointer;position:relative;left:-30px"></span>');
	triggerImg.appendTo(_viewer.getItem("PLACE").obj.parent());
	triggerImg.bind("click",findPlace);
}else{
	//
}

/**判断是否获取工作流按钮对象，工作流卡片中才生效*/
if (_viewer.wfCard) {
	var saveBtn = _viewer.wfCard._getBtn('save');
	var finishBtn = _viewer.wfCard._getBtn('finish');
	var cancelBtn = _viewer.wfCard._getBtn('cancelMt');
}

/**按钮绑定事件--卡片按钮<保存>插入监听事件*/
_viewer.beforeSave = function() {
	return _checkValid();
};

/**按钮绑定事件--工作流按钮<保存>插入监听事件*/
if(saveBtn && (_viewer.itemValue("STATUS") == 2 || _viewer.itemValue("STATUS") == 11)){//审批通过、取消状态下保存失效并提醒
	_viewer.wfCard._doWfBtnBeforeClick(function(){
		alert("状态：" + jQuery("#OA_MT_MEETING-STATUS__NAME").val() + "，数据禁止更改！");
		return false;
	},saveBtn);
}

/**按钮绑定事件--工作流按钮<办结>插入监听事件*/
if(finishBtn && _viewer.itemValue("STATUS") != 11){//未取消会议状态下办结失效并提醒
	_viewer.wfCard._doWfBtnBeforeClick(function(){
		alert("请先取消会议！");
		return false;
	},finishBtn);
}

/**按钮绑定事件--工作流自定义按钮<取消会议>插入监听事件并绑定事件*/
if(cancelBtn){
	_viewer.wfCard._doWfBtnBeforeClick(function(){
		if(System.getVar('@USER_CODE@')==_viewer.itemValue("S_USER") && //必须是起草人且状态是未取消
				_viewer.itemValue("STATUS") != 11){
			//构建弹出框页面布局
			if(confirm("是否确定取消会议？")){
				getDialog(event,"mt-cancelInputReason-dial","取消会议原因","300","200");
				_showInputReasonItems();
			}
			return false;//此处返回false代表按钮设置中的memo字段没有定义操作表达式，否则会报找不到method的错误
		}else{
			if(_viewer.itemValue("STATUS") == 11){
				alert("会议已经取消！");	
			}else{
				alert("只有起草人有取消权限！");
			}
			return false;//此处返回false代表按钮设置中的memo字段没有定义操作表达式，否则会报找不到method的错误
		}
	},cancelBtn);
}

/**内部函数--构建弹出框页面布局*/
function getDialog(event,dialogId,title,wid,hei) {
	//设置jqueryUi的dialog参数
	if(title == null){
		title = ""
	}
	var winDialog = jQuery("<div></div>").addClass("selectDialog").attr("id",dialogId).attr("title",title);
	winDialog.appendTo(jQuery("body"));
	if(hei == null || wid == null || hei == "" || wid == ""){
		wid = jQuery("body").width() - 500;
		hei = GLOBAL.getDefaultFrameHei()-550;	
	}
	var posArray = [30,30];
	if (event) {
		var cy = event.clientY;
	    posArray[0] = "";
	    posArray[1] = cy-300;
	}
  
  //生成jqueryUi的dialog
	jQuery("#" + dialogId).dialog({
		autoOpen: false,
		height: hei,
		width: wid,
		modal: true,
		resizable:false,
		position:posArray,
		open: function() { 

		},
		close: function() {
			jQuery("#" + dialogId).remove();
		}
	});
	
	//手动打开dialog
	var dialogObj = jQuery("#" + dialogId);
	dialogObj.dialog("open");
	dialogObj.focus();
  jQuery(".ui-dialog-titlebar").last().css("display","block");//设置标题显示
  dialogObj.parent().addClass("rh-bottom-right-radius rhSelectWidget-content");
  Tip.show("努力加载中...",null,jQuery(".ui-dialog-title",winDialog).last());
}

/**内部函数--构造输入取消会议原因对话框*/
function _showInputReasonItems(){
	var textareaObj = jQuery("<textarea style='width:90%;height:120px;margin:5% auto 5% 5%'></textarea>").attr("id","cancelInputReason-reason");
	textareaObj.appendTo(jQuery("#mt-cancelInputReason-dial"));	
	var btnContainer = jQuery("<div></div>").addClass("rhGrid-btnBar").attr("id","btn_ctn").css({"width":"100%"});		
	//构造确认按钮并绑定事件
	var enterBtn = jQuery("<a></a>").addClass("rh-icon rhGrid-btnBar-a").attr("id","enter_btn").bind("click",function(event){		
		var reason = jQuery("#cancelInputReason-reason").val(); 
		if(reason == null || reason == ""){
			alert("请输入原因！")
			return;
		}
		_viewer.doAct("cancelMeeting",_viewer.servKeys,true,{"reason":reason});		
	    jQuery("#mt-cancelInputReason-dial").empty();
	    jQuery("#mt-cancelInputReason-dial").remove();
	});
	var enterText = jQuery("<span>确认</span>").addClass("rh-icon-inner");
	var enterImg = jQuery("<span></span>").addClass("rh-icon-img btn-add");
	enterText.appendTo(enterBtn);
	enterImg.appendTo(enterBtn);
	enterBtn.appendTo(btnContainer);
	
	//构造取消按钮并绑定事件
	var cancelBtn = jQuery("<a></a>").addClass("rh-icon rhGrid-btnBar-a").attr("id","cancel_btn").bind("click",function(event){
		jQuery("#mt-cancelInputReason-dial").empty();
		jQuery("#mt-cancelInputReason-dial").remove();
	});
	var cancelText = jQuery("<span>取消</span>").addClass("rh-icon-inner");
	var cancelImg = jQuery("<span></span>").addClass("rh-icon-img btn-delete");
	cancelText.appendTo(cancelBtn);
	cancelImg.appendTo(cancelBtn);
	cancelBtn.appendTo(btnContainer);	
	btnContainer.appendTo(jQuery("#mt-cancelInputReason-dial"));
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

	/*解决方案二：覆盖当前标签方式*/
	

	/*解决方案三：弹出对话窗口方式
		//构建弹出框页面布局
		getDialog(event,"mt-meetingroom-dial","会议室查询统计");
		//OA_MT_MEETINGROOM_V<会议室查询统计>服务需要的参数
		var params = {
					"sId":"OA_MT_MEETING",
					"pCon":jQuery("#mt-meetingroom-dial"),
					"batchFlag":false,
					"showTitleBarFlag":"false",
				    "_SELECT_":"",
				    "type":_viewer.type,
				    "selectView":true,
				    "pkHide":this.pkHide,
		            "resetHeiWid":_viewer._resetHeiWid,
		            "parHandler":_viewer,
		            "extWhere":"",
		            "dataFlag":_viewer._dataFlag,
		            "params":_viewer.params
		            };
		this.listView = new rh.vi.listView(params);
		this.listView.show();
	*/
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
		//alert(idArray.START_TIME);
		}
	};
	var queryView = new rh.vi.rhSelectListView(options);
	queryView.show(event);

	/*解决方案二：自定义查询选择方式
	getDialog(event,"mt-choosedMeetingroom-dial","会议室查询统计");
	//OA_MT_MEETINGROOM_V<会议室查询统计>服务中查询本人已选定且未启用的会议室预定记录
	var params = {
				"sId":"OA_MT_BOOKING",
				"pCon":jQuery("#mt-choosedMeetingroom-dial"),
				"batchFlag":false,
				"showTitleBarFlag":"false",
			    "_SELECT_":"",
			    "type":_viewer.type,
			    "selectView":true,
			    "readOnly":true,
			    "pkHide":this.pkHide,
	            "resetHeiWid":_viewer._resetHeiWid,
	            "parHandler":_viewer,
	            "extWhere":" and START_TIME >= substr('@DATETIME@',0,16)",
	            "dataFlag":_viewer._dataFlag,
	            "params":_viewer.params
            	};
	var listView = new rh.vi.listView(params);
	listView.show();
	*/
}

/**内部函数--提交数据前检查*/
function _checkValid(){
	//检查时间正负
	var sDate = _viewer.itemValue("BEGIN_TIME");
	var eDate = _viewer.itemValue("END_TIME");
	if(rhDate.doDateDiff("M", sDate, eDate, 2)<=0){
		alert("会议结束时间应该大于开始时间!!!");
		return false;
	}
	//其他
	
	return true;
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
	
/**知识总结*/	
//”审批中“状态禁止办结,尽量不在代码中强行的隐藏工作流配置的按钮，这样别人维护的时候会遇到问题，可以对按钮加before设定
//if(_viewer.itemValue("STATUS") == 1){
//	jQuery('#OA_MT_MEETING-finish').hide();
//}

//”审批中“状态禁止办结
//if(_viewer.itemValue("STATUS") >= 10){
//	jQuery('#OA_MT_MEETING-save').hide();alert();
//}

//function tellBackEndWhichChoosedOnCONFEREES_CODES(){
//var data = {'a':'123'};
//FireFly.doAct(_viewer.servId,"rememberWhichChoosedOnCONFEREESCODES",data)
//}