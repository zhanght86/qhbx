var _viewer = this;

//判断如果不是管理员角色并且记录是公共项，则将列表可编辑项去除
if (System.getVar("@ROLE_CODES@").indexOf("RADMIN") < 0) {
	//去除[公开]可编辑
	_viewer.grid.getTdItems("IS_PUBLIC__NAME").each(function(){
		var selfObj = jQuery(this).find("select").eq(0);
		var flagConfigObj = _viewer.grid.getRowItem(selfObj.attr("pk"), "S_FLAG__NAME");
		var flagObj = flagConfigObj.find("select").eq(0);
		if (selfObj.val() == 1) {
			jQuery(this).html(selfObj.find("option:selected").text());
			flagConfigObj.html(flagObj.find("option:selected").text());
		}
	});
}
