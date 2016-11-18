var _viewer = this;
jQuery("#"+_viewer.servId).find(".content-mainCont").hide();
//查询按钮，重新绑定click事件
_viewer.advSearch.advBtn.unbind("click").bind("click",function(){
	var params = {};
	params["SEARCH_WHERE"] = _viewer.advSearch.getWhere();
	params["WHERE_JSON"] = _viewer.advSearch.getWhereJson();
	FireFly.doFormAct("BN_SEAL_USAGE_TJ_V","sealUsageTj",params,"_blank");
});


