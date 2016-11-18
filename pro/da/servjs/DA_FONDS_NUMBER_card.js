/**_viewer = this;

_viewer.beforeSave = function(){
	var fondsName = _viewer.getItem("ODEPT_CODE").getText();
	_viewer.setExtendSubmitData({"FONDS_NAME":fondsName});
}
**/