var _viewer = this;

//页面加载的时候把形影数据添加上去，并把重复只去除
var compLeaderCodes = _viewer.getItem("COMPLEADER").getValue();
var userCodes = _viewer.getItem("USER_ID").getValue();
var userValues = _viewer.getItem("USER_ID").getText();

//页面初始化加载方法
isExiteDept(compLeaderCodes + userCodes, userValues, "USER_ID");

//调用父句柄的函数
_viewer.getItem("USER_ID").callback = function(id, value){
	compLeaderCodes = "";
	isExiteDept(id.join(","), value.join(","), "USER_ID",true);
};

//给提醒内容注入文字
_viewer.getItem("REM_CONTENT").setValue(function(){
	var linksObj = _viewer.links || "";
	if (linksObj != "") {
		var remindTitle = new Array();
		remindTitle.push(linksObj["REM_TDEPT__NAME"]);
		remindTitle.push(linksObj["REM_PLACE"]);
		remindTitle.push("召开");
		remindTitle.push(linksObj["REM_TITLE"]);
	}
	return remindTitle.join("");
});

_viewer.getItem("TYPE").setValue("MESSAGE");

//[本单位与会人员/被通知人]中是否存在部门code，存在去除，并提示
function isExiteDept (ids, value, code, isTip) {
	var outBeanObj = FireFly.doAct("OA_MT_MEETING", "getAllUserCode",{"CODES":ids,"VALUE":value,"COMPLEADER":compLeaderCodes});
	if (outBeanObj["OK"] == "false") {
		_viewer.getItem(code).setValue(outBeanObj["CODE"]);
		_viewer.getItem(code).setText(outBeanObj["VALUE"]);
		if (isTip) {
			_viewer.cardBarTipError(outBeanObj["NAME"] + "是部门，不是用户，请从新选择！！！");
		}
	}
}
