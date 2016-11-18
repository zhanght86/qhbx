var _viewer = this;
//隐藏删除按钮

debugger;

if (_viewer.getParHandler().wfCard && _viewer.getParHandler().wfCard.isWorkflow()) {
	var deleBut = _viewer.getParHandler().wfCard.getCustomVarContent("deleBut");
	
	alert(deleBut);
	
	if(deleBut!="true"){
		 _viewer.getBtn("delete").hide();
	}
}
//隐藏新建按钮
if (_viewer.getParHandler().wfCard && _viewer.getParHandler().wfCard.isWorkflow()) {
	var addBut = _viewer.getParHandler().wfCard.getCustomVarContent("addBut");
	if(addBut!="true"){
		 _viewer.getBtn("add").hide();
	}
}