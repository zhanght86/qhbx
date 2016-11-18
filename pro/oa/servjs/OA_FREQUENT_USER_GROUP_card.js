var _viewer = this;

_viewer.getItem("USER_CODES").callback = function(codeArray, nameArray) {
	_viewer.getItem("USER_NAMES").setValue(nameArray.join(","));
}

/**
 * 保存之前
 */
/*_viewer.beforeSave = function() {
	var out = FireFly.doAct(_viewer.servId, "isExitDept", {"DEPT_CODES":_viewer.getItem("USER_CODES").getValue()});
	debugger;
	if (out["_MSG_"].indexOf("ERROR,") >= 0) {
		_viewer.cardBarTipError(out["_MSG_"].replace("ERROR,", ""));
		return false;
	}
	return true;
};*/
