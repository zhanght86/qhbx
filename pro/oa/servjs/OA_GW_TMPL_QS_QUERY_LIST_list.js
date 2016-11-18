(function(_viewer){
	var addBtn = _viewer.getBtn("add");
	if (addBtn) {
		addBtn.remove();
	}
	
	var servId = _viewer.servId;
	_viewer.grid.unbindTrdblClick();
	_viewer.grid.unbindIndexTDClick();
	_viewer.grid.dblClick(function(id, node) {
		var tmplType = _viewer.grid.getRowItemValue(id, "TMPL_TYPE_CODE");
		_viewer._openCardView("", id, tmplType);
	}, _viewer);
})(this);