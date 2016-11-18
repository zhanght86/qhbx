var _viewer = this;

_viewer.getBtn('add').unbind("click");
_viewer.getBtn('add').hide();
/**
 * 审核不通过
 */
_viewer.getBtn('chkPass').unbind("click").bind("click",function() {
	var ids = _viewer.grid.getSelectPKCodes();
	var validIds = new Array();
	jQuery.each(ids,function(index,itemVal){
		var chkState = _viewer.grid.getRowItemValue(itemVal,"CHK_STATE");
		if(chkState == 5){ //送审的数据可以审核不通过
			validIds.push(itemVal);
		}
	});
	
	if(validIds.length > 0){
		var param = {};
		param["chkIds"] = validIds.join(",");	
		FireFly.doAct(_viewer.servId, "chkPass", param, true);
		_viewer.refresh();
	}
});


/**
 * 审核通过
 */
_viewer.getBtn('chkNotPass').unbind("click").bind("click",function() {
	var ids = _viewer.grid.getSelectPKCodes();
	var validIds = new Array();
	jQuery.each(ids,function(index,itemVal){
		var chkState = _viewer.grid.getRowItemValue(itemVal,"CHK_STATE");
		if(chkState == 5){ //送审的数据可以审核不通过
			validIds.push(itemVal);
		}
	});
	
	if(validIds.length > 0){
		var param = {};
		param["chkIds"] = validIds.join(",");
		FireFly.doAct(_viewer.servId, "chkNotPass", param, true);
		_viewer.refresh();
	}
});