var _viewer = this;

/**
 * 送审核
 */
_viewer.getBtn('sendToChk').unbind("click").bind("click",function() {
	var ids = _viewer.grid.getSelectPKCodes();
	var validIds = new Array();
	jQuery.each(ids,function(index,itemVal){
		var chkState = _viewer.grid.getRowItemValue(itemVal,"CHK_STATE");
		if(chkState == 1){ //未送审的数据可以送审
			validIds.push(itemVal);
		}
	});
	if(validIds.length > 0){
		var param = {};
		param["chkIds"] = validIds.join(",");	
		FireFly.doAct(_viewer.servId, "sendToChk", param, true);
		_viewer.refresh();
	}else{
		alert("未选中状态为‘未审核’的记录!");
	}
});

/**
 * 发布
 */
_viewer.getBtn('publish').unbind("click").bind("click",function() {
	var ids = _viewer.grid.getSelectPKCodes();
	var validIds = new Array();
	jQuery.each(ids,function(index,itemVal){
		var chkState = _viewer.grid.getRowItemValue(itemVal,"CHK_STATE");
		var actState = _viewer.grid.getRowItemValue(itemVal,"ACT_STATE");
		if(chkState == 20 && actState == 2){ //不审核，且未发布的信息可以启用
			validIds.push(itemVal);
		}
	});
	
	if(validIds.length > 0){
		var param = {};
		param["ids"] = validIds.join(",");	
		FireFly.doAct(_viewer.servId, "publish", param, true);
		_viewer.refresh();
	}else{
		alert("未选中状态为‘未发布’的记录!");
	}
});