var _viewer = this;


jQuery("#OA_LDR_ACTION_NSH .rhGrid-btnBar .searchDiv tr td:last .rhSearch-advancedButton-inner").text("查询");
jQuery("#OA_LDR_ACTION_NSH .rhGrid-btnBar .searchDiv tr td:last").siblings().hide();


/**
 * 发布
 */
_viewer.getBtn('publish').unbind("click").bind("click",function() {
	var ids = _viewer.grid.getSelectPKCodes();
	var validIds = new Array();
	jQuery.each(ids,function(index,itemVal){
		var actState = _viewer.grid.getRowItemValue(itemVal,"ACT_STATE");
		if(actState == 2){ //未发布的信息可以启用
			validIds.push(itemVal);
		}
	});
	if(validIds.length > 0){
		var param = {};
		param["ids"] = validIds.join(",");	
		FireFly.doAct(_viewer.servId, "publish", param, true);
		_viewer.refresh();
	}else{
		alert("请选择状态为‘未发布’的记录!");
	}
});