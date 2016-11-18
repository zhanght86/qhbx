var _viewer = this;

var params = "";
if (_viewer.getParams() != undefined) {
	params = _viewer.getParams().links || false;
}

//备注显示样式
//_viewer.form.getItem("MEMO_TITLE").obj.parent().removeClass("disabled").css({"background":"url('')"});
//_viewer.form.getItem("MEMO_TITLE").obj.css({"color":"black","font-weight":"bolder","background-color":"white","font-size":"14px"});

//当前节点是否有修改[审核状态]权限
var shFlag = _viewer.wfCard.getCustomVarContent("SH_FLAG") || "";
//是否为起草点
var isQc = _viewer.wfCard.getCustomVarContent("IS_QC") || "";

//当前节点是否有审核权限并并且不是起草点
if (shFlag == "true" && isQc != "true") {
	_viewer.getItem("STATUS").enabled();
} else {
	_viewer.getItem("STATUS").disabled();
}
if (params) {
	var beginDateVal = _viewer.getItem("START_TIME").getValue() || "";
	var endDateVal = _viewer.getItem("END_TIME").getValue() || "";
	if ("" == beginDateVal || "" == endDateVal) {
		// 为开始时间和结束时间赋值
		if ("MORNING" == params.timeArea) {
			_viewer.getItem("START_TIME").setValue(params.NEW_DATE + " 09:00");
			_viewer.getItem("END_TIME").setValue(params.NEW_DATE + " 12:00");
		} else if ("AFTERNOON" == params.timeArea) {
			_viewer.getItem("START_TIME").setValue(params.NEW_DATE + " 13:00");
			_viewer.getItem("END_TIME").setValue(params.NEW_DATE + " 18:00");
		} else if ("EVENING" == params.timeArea) {
			_viewer.getItem("START_TIME").setValue(params.NEW_DATE + " 18:00");
			_viewer.getItem("END_TIME").setValue(params.NEW_DATE + " 23:00");
		}
	}
}

//给取消按钮绑定事件
if (_viewer.wfCard._getBtn("cancelBooking")){
	var bookingStatus = _viewer.getItem("STATUS").getValue();
	if (bookingStatus == "12") {
		_viewer.wfCard._getBtn('cancelBooking').layoutObj.remove()
	} else {
		_viewer.wfCard._doWfBtnBeforeClick(function(){
			if (confirm("确定取消会议室预定？")) {
				//判断ID是否为NULL，如果为NULL则不允许提交。
				//成功之后刷新页面
				var out = FireFly.doAct(_viewer.servId, "cancelBooking", {
					"_PK_": _viewer.getItem("BOOKING_ID").getValue()
				});
				if (out["_MSG_"].indexOf("OK") >= 0) {
					_viewer.cardBarTip("会议室取消成功！");
					_viewer.backClick();
				}
				else {
					_viewer.cardBarTipError("会议室取消失败");
				}
			}
		}, _viewer.wfCard._getBtn("cancelBooking"));
	}
}

var startTime = _viewer.getItem("START_TIME").obj;
var endTime = _viewer.getItem("END_TIME").obj;

rhDate.compareTwoDate(startTime, endTime, "yyyy-MM-dd HH:mm", false); //绑定开始时间和结束时间联动

//保存前的开始时间和结束时间大小校验
_viewer.beforeSave = function() {
	var s = startTime.val() || "";
	var e = endTime.val() || "";
	if ("" == s || "" == e) {
		_viewer.cardBarTipError("开始时间大于结束时间，请重新选择！");
		return false;
	}
};

if (_viewer.wfCard._getBtn("revisionBooking")) { //会议调整按钮
 	//审批通过并且已发送会议通知
	if(_viewer.getItem("STATUS").getValue() == "2" && _viewer.getItem("NOTICE_FLAG").getValue() == "1") {
			_viewer.wfCard._doWfBtnBeforeClick(function(){
			//alert("明天来做操作，就是找到当前会议室的会议id，然后给这些会议被通知人发送调整通知");
			//给[变更通知]按钮绑定事件
			sendRevisionMsg(); //明天来做操作，就是找到当前会议室的会议id，然后给这些会议被通知人发送调整通知
		},_viewer.wfCard._getBtn('revisionBooking'));
		_viewer.getItem("MR_ID").enabled();
		_viewer.getItem("START_TIME").enabled();
		_viewer.getItem("END_TIME").enabled();
	} else {
		_viewer.wfCard._getBtn('revisionBooking').layoutObj.remove();
	}
}

//起草会议通知按钮
if (_viewer.wfCard._getBtn("addMeetingNotice")) {
	if (_viewer.getItem("STATUS").getValue() == "2" && _viewer.getItem("NOTICE_FLAG").getValue() != "1") {
		_viewer.wfCard._doWfBtnBeforeClick(function(){
			addMeetingNotice();
		}, _viewer.wfCard._getBtn("addMeetingNotice"));
	} else {
		_viewer.wfCard._getBtn("addMeetingNotice").layoutObj.remove();
	}
}

//去草会议通知按钮
function addMeetingNotice() {
	Tab.open({
		"url" : "OA_MT_MEETING.card.do",
		"tTitle" : "会议通知",
		"params": {
			"links": {
				"HAVE_NOTICE":true,
				"MR_ID" : _viewer.getItem("MR_ID").getValue(),
				"PLACE": _viewer.getItem("MR_ID").getText(),
				"BEGIN_TIME": _viewer.getItem("START_TIME").getValue(),
				"END_TIME": _viewer.getItem("END_TIME").getValue(),
				"BOOKING_FLAG": "2",
				"BOOKING_ID":_viewer.getItem("BOOKING_ID").getValue(),
				"TITLE":_viewer.getItem("TITLE").getValue()
			},
			"callBackHandler" : _viewer,
			"closeCallBackFunc" : function() {
				_viewer.refresh();
			}
		},
		"parHandler":_viewer,
		"_parentRefreshFlag":true,
		"menuFlag" : 4
	});
}

//发送会议室调整通知
function sendRevisionMsg() {
	if (confirm("确定已变更会议通知，并已保存调整数据？")) {
		//发送调整会议室通知
		var data = {"_PK_" : _viewer.getItem("BOOKING_ID").getValue(),
					"TODO_TITLE":"[调整会议室]关于" + _viewer.getItem("TITLE").getValue() + "的通知"
					};
		var textAreaObj = jQuery("<textarea style='width:100%;height:135px;'></textarea>");
		showRHDialog("调整原因", textAreaObj, function(){
			if (textAreaObj.val() != "") {
				data = jQuery.extend(data, {"TODO_CONTENT" :textAreaObj.val()});
				var cancelNoticeBean = FireFly.doAct("OA_MT_BOOKING_NOWFE", "sendMeetingRemindMsg", data);
				if (cancelNoticeBean["_MSG_"].indexOf("OK") >= 0) {
					_viewer.cardBarTip("会议室调整通知发送成功");
					_viewer.backClick();
				} else {
					_viewer.cardBarTipError(cancelNoticeBean["_MSG_"].replace("ERROR,", ""));
				}
			} else {
				_viewer.cardBarTipError("请填写调整原因");
				return false;
			}
		}, _viewer);
	}
}
