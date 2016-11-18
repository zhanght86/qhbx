var _viewer = this;

if(_viewer.params && _viewer.params["APPLY_ID"]) {
	_viewer.getItem("APPLY_ID").setValue(_viewer.params["APPLY_ID"]);
}

if(_viewer.params && _viewer.params["FILE_ID"]) {
	_viewer.getItem("FILE_ID").setValue(_viewer.params["FILE_ID"]);
}

if(_viewer.params) {
	var readOnly = _viewer.params["READ_ONLY"];
	if(readOnly == true) {
		_viewer.readCard();
	}
}
