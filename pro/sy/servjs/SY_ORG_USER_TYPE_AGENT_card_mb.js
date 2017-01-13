var _viewer = this;
var saveBtn = _viewer.getBtn("save");
var startAgentBtn = _viewer.getBtn("startAgent");
var stopAgentBtn = _viewer.getBtn("stopAgent");
//清除按钮	
_viewer.footerNavWrp.find("li").empty();
_viewer.footerWrp.find("li").empty();
if (saveBtn && !(_viewer.itemValue("AGT_STATUS") == 1 || _viewer.itemValue("AGT_STATUS") == 2)) {
		// 添加到footer
		var $liWrapper = $("<li></li>");
		var btn = $("<a href='#'></a>").appendTo($liWrapper);
		btn.attr(saveBtn).text(saveBtn.ACT_NAME);
		var $liWrapper = $("<li></li>");
		var btn = $("<a href='#'></a>").appendTo($liWrapper);
		btn.attr(saveBtn).text(saveBtn.ACT_NAME);
		// 绑定事件
		btn.on('vclick', function(event) {
			event.preventDefault();
			event.stopImmediatePropagation();
			if(!checkValid()){
				jQuery('.rh-barTip').remove();
				return false;
			}
			//结束委托方式字段绑定事件
			var endType = _viewer.itemValue("AGT_END_TYPE");
			if(endType == 1){
				_viewer.getItem("AGT_END_DATE").setValue("2099-12-31");
			}else if(endType == 2){
				var endDate = _viewer.itemValue("AGT_END_DATE");
				if(endDate == ""){
					alert("自动结束委托方式请选择截止日期");
					return false;
				}
			}
			FireFly.doAct(saveBtn.SERV_ID, saveBtn.ACT_CODE, _viewer.reqdata).done(function(result) {
					// 送交给oa得到oa返回的成功信息
					alert(result["_MSG_"]);
			});
		});
		
		_viewer.addBtn($liWrapper);
}

if (stopAgentBtn &&! (_viewer.itemValue("AGT_STATUS") != 1)){
	var $liWrapper = $("<li></li>");
	var btn = $("<a href='#'></a>").appendTo($liWrapper);
	btn.attr(stopAgentBtn).text(stopAgentBtn.ACT_NAME);
	// 绑定事件
	btn.on('vclick', function(event) {
		event.preventDefault();
		event.stopImmediatePropagation();
		if(!confirm("确认后您将不再处于委托状态？")){
			return;
		}
		var param = {
			"_PK_":_viewer.getPKCode(),
			"AGT_STATUS": 1,
			"action":"stopAllAgent"
		};
		var result = FireFly.doAct("SY_ORG_USER_TYPE_AGENT", "doAgentAction", param, true, false);
		_viewer.refresh();
		_viewer.setParentRefresh();
	});
	_viewer.addBtn($liWrapper);
}

//检查提交表单有效性
function checkValid(){
	var agtStatus = _viewer.itemValue("AGT_STATUS");
	if(agtStatus == 2){
		alert("委托已结束");
		return false;
	}
	if(rhDate.doDateDiff("D", System.getVar("@DATE@"), _viewer.itemValue("AGT_BEGIN_DATE"), 0) < 0) {
		alert("开始日期必须大于当前日期！");
		return false;
	}
	if(rhDate.doDateDiff("D", _viewer.itemValue("AGT_BEGIN_DATE"), _viewer.itemValue("AGT_END_DATE"), 0) < 0) {
		alert("结束日期不能小于开始日期！");
		return false;
	}
	if(_viewer.sonTab && _viewer.sonTab.SY_ORG_USER_TYPE_AGENT_FROM){
		var listData = _viewer.sonTab.SY_ORG_USER_TYPE_AGENT_FROM.getListData();
		if(listData._DATA_.length == 0){
			alert("请先设定委托业务");
			return false;
		}
	}
	return true;
}



//有效并启动状态下标识未开始与超期
var agtStatus = _viewer.itemValue("AGT_STATUS");
var sFlag = _viewer.itemValue("S_FLAG");
if(sFlag==1 && agtStatus==1){
	var statusInput = _viewer.getItem("AGT_STATUS").obj;
	var currDate = System.getVar("@DATE@");
	var beginDate = _viewer.itemValue("AGT_BEGIN_DATE");
	var endDate = _viewer.itemValue("AGT_END_DATE");
	if(rhDate.doDateDiff("D", currDate, beginDate, 0) > 0) {
		statusInput.val("已启动(未开始)").css("background-color","yellow");
		statusInput.parent().css("background-color","yellow");
	} else if(rhDate.doDateDiff("D", endDate, currDate, 0) > 0) {
		statusInput.val("已启动(超期)").css("background-color","red");
		statusInput.parent().css("background-color","red");
	} else {
		
	}
}