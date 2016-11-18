var _viewer = this;

//修改列表中的[确认状态]字段值
_viewer.grid.getBodyTr().each(function() {
	var thisReturnObj = jQuery(this).find("td[icode='RETURN_SATUS']");
	var thisReturnObjName = jQuery(this).find("td[icode='RETURN_SATUS__NAME']");
	if (thisReturnObj.html() == "1") {
		thisReturnObjName.html("已确定").css({"color":"blue"});
	} else if (thisReturnObj.html() == "2") {
		thisReturnObjName.html("派他人");
	} else if (thisReturnObj.html() == "0") {
		thisReturnObjName.html("未确定").css({"color":"red"});
	}
});