var _viewer = this;

/**
 * 审核通过
 */
_viewer.getBtn('chkPass').unbind("click").bind("click",function() {
//	var param = {};
//	param["_PK_"] = _viewer.getOrigPK();
//	param["CHK_STATE"] = "10";
//	FireFly.doAct(_viewer.servId, "save", param, true);
	
	var param = {};
	param["chkIds"] = _viewer.getOrigPK();	
	FireFly.doAct(_viewer.servId, "chkPass", param, true);
	_viewer.refresh();
	_viewer.setParentRefresh();
});


/**
 * 审核不通过
 */
_viewer.getBtn('chkNotPass').unbind("click").bind("click",function() {
	var param = {};
	param["chkIds"] = _viewer.getOrigPK();
	FireFly.doAct(_viewer.servId, "chkNotPass", param, true);
	_viewer.refresh();
	_viewer.refresh();
	_viewer.setParentRefresh();
});