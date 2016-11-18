(function(_viewer){
	_viewer.beforeSave = function() {
		if (_viewer.getItem("CT_NAME").getValue().length == 0) {
			Tip.clearLoad();
			alert("请选择关联合同！");
			return false;
		}
	};
})(this);