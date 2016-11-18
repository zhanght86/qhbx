var _viewer = this;
var saveLibrary = _viewer.getBtn("saveLibrary")
saveLibrary.unbind("click").bind("click",function(){
	var params = {};
	params["_PK_"] = _viewer.getPKCode();
	var result = FireFly.doAct(_viewer.servId,"saveInLibrary",params);
	_viewer.refresh();
	Tip.show("入库成功");
});
//入库按钮只有流程中配置了变量才可看到
saveLibrary.hide();
if (_viewer.wfCard && _viewer.wfCard.isWorkflow()){
	if(_viewer.wfCard.getCustomVarContent("canSave") && _viewer.getItem("APPLY_STATUS").getValue()==1){
		saveLibrary.show();
	}
}