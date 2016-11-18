var _viewer = this;

//遍历数据
_viewer.grid.getBodyTr().each(function() {
	if (jQuery(this).find("[icode='COLOR_FLAG']").html() == "RED") {
		jQuery(this).find("[icode='OFFICE_STOCK']").css({"color":"red","font-weight":"bold"});
	}
});