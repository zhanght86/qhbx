var _viewer = this;

//重置模板大类
_viewer.getBtn("resetTmplType").unbind("click").bind("click", function(){
	var out = FireFly.doAct(_viewer.servId, "resetTmplType", {});
	if (out["_MSG_"].indexOf("OK") >= 0) {
		_viewer.listBarTip("模板大类重置成功！");
	} else {
		_viewer.listBarTipError(out["_MSG_"].replace("ERROR,", ""));
	}
});
