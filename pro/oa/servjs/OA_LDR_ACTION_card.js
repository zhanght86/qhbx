var _viewer = this;

/**
 * 送审核
 */
_viewer.getBtn('sendToChkCard').unbind("click").bind("click",function() {
	var param = {};
	param["chkIds"] = _viewer.getOrigPK();	
	FireFly.doAct(_viewer.servId, "sendToChk", param, true);
	_viewer.refresh();
	_viewer.setParentRefresh();
});

/**
 * 取消发布
 */
_viewer.getBtn('cancelPublish').unbind("click").bind("click",function() {
	var param = {};
	param["_PK_"] = _viewer.getOrigPK();
	param["ACT_STATE"] = "2";
	FireFly.doAct(_viewer.servId, "save", param, true);
	_viewer.refresh();
	_viewer.setParentRefresh();
});

/**
 * 取消发布
 */
_viewer.getBtn('publish').unbind("click").bind("click",function() {
	var param = {};
	param["_PK_"] = _viewer.getOrigPK();
	param["ACT_STATE"] = "1";
	FireFly.doAct(_viewer.servId, "save", param, true);
	_viewer.refresh();
	_viewer.setParentRefresh();
});