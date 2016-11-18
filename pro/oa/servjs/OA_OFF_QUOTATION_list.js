var _viewer = this;

//格式化数字
_viewer.grid.getBodyTr().each(function() {
	var officePrice = jQuery(this).find("[icode='OFFICE_PRICE']").html();
	jQuery(this).find("[icode='OFFICE_PRICE']").html(parseFloat(officePrice).toFixed(2));
});