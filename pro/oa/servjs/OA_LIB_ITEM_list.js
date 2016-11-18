var _viewer = this;

//如果用户没有权限，则印章添加按钮
if(_viewer.getListData()["_NO_RIGHT"] == "true"){
	_viewer.getBtn("add").hide();
	_viewer.getBtn("delete").hide();
}
