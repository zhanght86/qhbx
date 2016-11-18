var _viewer = this;
//初始化盖章按钮，已盖章状态时隐藏按钮
var sealBtn = _viewer.grid.getBtn("seal");
jQuery.each(this.grid.getBodyTr(), function(i, n) {
	var sealState = jQuery(this).find("td[icode='SEAL_STATE']").text();
	if(sealState == '2'){
		jQuery(this).find("td[icode='seal']").html('&nbsp;');
	}
});
sealBtn.unbind("click").bind("click",function(){
	var param = {};
	var pk = jQuery(this).attr("rowpk");
	param["SEAL_ID"] = _viewer.grid.getRowItemValue(pk,"SEAL_ID")
	param["TITLE"] = _viewer.getParHandler().getItem("APPLY_TITLE").getValue();
	param["SERV_ID"] = _viewer.getParHandler().servId;
	param["_PK_"] = pk;
	param["DATA_ID"] = _viewer.getParHandler().getPKCode();
	FireFly.doAct(_viewer.servId,"saveSeal",param);
	_viewer.getParHandler().refresh();
});