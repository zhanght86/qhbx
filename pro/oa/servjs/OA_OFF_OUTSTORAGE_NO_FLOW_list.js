var _viewer = this;
//去除列表双击事件
_viewer.grid.getBodyTr().unbind("dblclick");

//去除平铺查询中[名称]的[选择]和[取消]链接
_viewer.getTileItem("OFFICE_NAME").find("a").remove();

//格式化数字
_viewer.grid.getBodyTr().each(function() {
	var officePrice = jQuery(this).find("td[icode='OFFICE_PRICE']").html();
	jQuery(this).find("td[icode='OFFICE_PRICE']").html(parseFloat(officePrice).toFixed(2));
	var officeAmount = jQuery(this).find("td[icode='OFFICE_AMOUNT']").html();
	jQuery(this).find("td[icode='OFFICE_AMOUNT']").html(parseFloat(officeAmount).toFixed(2));
});

//高级查询，替换名称字段配置类型
_viewer.advSearch.resetWhere = function(where){
	if (where.indexOf("OFFICE_NAME") >= 0) {
		var resetWhereArry = where.split("and");
		for (var i = 0; i < resetWhereArry.length; i++) {
			if (resetWhereArry[i].indexOf("OFFICE_NAME") >= 0) {
				var officeNameArry = resetWhereArry[i].split("'");
				for (var j = 0; j < officeNameArry.length; j++) {
					if (officeNameArry[j].indexOf("=") >= 0) {
						officeNameArry[j] = officeNameArry[j].replace("=","like");
					}
					if (officeNameArry[j].replace(/ /gi,"") != "" && officeNameArry[j].indexOf("like") < 0) {
						officeNameArry[j] = "%" + officeNameArry[j] + "%";
					}
				}
				resetWhereArry[i] = officeNameArry.join("'");
			}
		}
		resetWhereArry = resetWhereArry.join("and");
		return resetWhereArry;
	}
};