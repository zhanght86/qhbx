/**当前作用域句柄*/
var _viewer = this;

/*html模板中将输入框width设为100%*/
//jQuery(".right").css({"width":"100%"});

/*修改输入框背景颜色*/
//jQuery(".blank").css({"background-color":"#fbf3e6;","background-image":"none"});
//jQuery(".blank *").css({"background-color":"#fbf3e6;"});

//jQuery(".ui-textarea-default").css({"margin-left":"0px","width":"100%","border":"0px #CCC solid"});

//覆盖gw.css的输入框边框样式
//jQuery(".ui-text-default input").css({"width":"100%","border":"0px solid #FF0000"});

//去除上部[保存]按钮
//jQuery("#"+_viewer.servId+"-mainTab .rhCard-btnBar").css({"margin-left":"-1000px","height":"0px"});
//审批状态
var parentStatus = _viewer.getParHandler().getItem("STATUS").getValue() || "";
//委托用户code
var agentUserCode = _viewer.getParHandler().getByIdData("_AGENT_USER_") || "";

//获取回执主键
var returnNoticePK = _viewer.getParHandler().getByIdData("RETURN_NOTICE_PK") || "";

var isExitAgentUserCode = "";

//已回执或者审批未通过
if (isExitData() || parentStatus != "2") {
	_viewer.getBtn("save").remove();
	_viewer.getItem("STATUS").disabled();
	if (parentStatus != "2") {
		_viewer.getItem("STATUS").obj.html("会议已取消");
	}
	_viewer.getItem("MEETING_ID").disabled();
	_viewer.getItem("USER_CODES").disabled();
	_viewer.getItem("MEMO").disabled();
}

if (_viewer.getItem("STATUS").getValue() == "1") { //初始化是本人参加
	_viewer.getItem("USER_CODES").hide();
}

_viewer.beforeSave = function() {
	_viewer.getItem("EXIT_AGENT").setValue(agentUserCode);
	if (_viewer._actVar != UIConst.ACT_CARD_ADD) {
		_viewer.getItem("RETURN_NOTICE_ID").setValue(returnNoticePK);
		_viewer.getItem("OLD_MEETING_ID").setValue(_viewer.getItem("MEETING_ID").getValue());
		_viewer.getItem("OLD_STATUS").setValue(_viewer.getItem("STATUS").getValue());
		_viewer.getItem("OLD_USER_CODES").setValue(_viewer.getItem("USER_CODES").getValue());
		_viewer.setOrigPK(returnNoticePK);
		_viewer._actVar = UIConst.ACT_CARD_ADD;
	}
};

/**
 * 显示委托用户
 */
var agentUserObj = _viewer.getItem("AGENT_USER");
if (agentUserObj.getValue().length <= 0) {
	agentUserObj.hide();
}

//当前数据在数据库中是否存在，存在则不可保存，不存在则可保存
//当前数据在数据库中是否存在，存在则不可保存，不存在则可保存
function isExitData() {
	var thisPkCode = _viewer.getItem("RETURN_NOTICE_ID").getValue() || "";
	var outBean = FireFly.doAct(_viewer.servId, "isExiteThisReturn", {
		"AGENT_USER": agentUserCode,
		"MEETING_ID":_viewer.getItem("MEETING_ID").getValue()
	});
	if (outBean["RETURN_MSG"].indexOf("ERROR,") >= 0) { //存在则返回true
		_viewer.getItem("STATUS").setValue("1");
		_viewer.getItem("AGENT_USER").setValue("");
		_viewer.getItem("AGENT_USER").setText("");
		_viewer.getItem("USER_CODES").setValue("");
		_viewer.getItem("USER_CODES").setText("");
		_viewer.getItem("MEMO").setValue("");
		return false;
	} else {
		for (i in outBean) {
			var itemCode = "";
			var thisObj = _viewer.getItem(i);
			if (i.indexOf("__NAME") >= 0) {
				thisObj = _viewer.getItem(i.replace("__NAME", ""));
				thisObj.setText(outBean[i]);
			} else  if (thisObj) {
				thisObj.setValue(outBean[i]);
			}
		}
		return true;
	}
}
/*function isExitData() {
	var thisMeetingId = _viewer.getParHandler().getItem("MEETING_ID").getValue() || "";
	var outBean = FireFly.doAct(_viewer.servId, "isExiteThisReturn", {"MEETING_ID" : thisMeetingId, "AGENT_USER":agentUserCode});
	if (outBean["RETURN_MSG"].indexOf("ERROR,") >= 0) { //存在则返回true
		return false;
	}
	return true;
}*/
