var _viewer = this;
// 对列表上选中行双击查看绑定事件
_viewer.grid.unbindTrdblClick();
_viewer.grid.dblClick(function(value, node) {
	// 双击打开该选中行的服务卡片页面
	Todo.openEntity(_viewer);
}, _viewer);
var colDef = this.grid.getColumnDef("S_WF_USER_STATE");
//如果字段存在且为显示
if(colDef && colDef.ITEM_LIST_FLAG == "1"){
	var titleStr = System.getVar("@C_SY_LIST_PROMPT_CONFIG@") || "";
	var titleStyle = "font-size:12px;margin:0px 10px;color:red;text-align:left;padding:6px;";
	_viewer.addRedHeader(titleStr,titleStyle);
}

var odeptObj = jQuery("label[value='S_ODEPT']").parent().next().children().first();
odeptObj.removeClass("rh-advSearch-val");
var hasSubOdeptFlagObj = jQuery("label[value='HAS_SUB_ODEPT_FLAG']").parent().next().children().first();
hasSubOdeptFlagObj.removeClass("rh-advSearch-val");
if (hasSubOdeptFlagObj.val()=='') {
	jQuery("option[value='']",hasSubOdeptFlagObj).remove();
	jQuery("option[value='2']",hasSubOdeptFlagObj).attr("selected",true);
}
//重载“开始查询”按钮
rh.vi.listView.prototype._setSearchWhereAndRefresh2 = function(where,treeLink,options) {
	where = this.beforeSearch(where);
	this.whereData[this.SEARCH_WHERE] = where;
	this.whereData["_NOPAGEFLAG_"] = "true";
	jQuery.extend(this.whereData,options);
	if (treeLink == true) {//关联左侧导航树条件
		this.refresh(this.whereData);
	} else {//不关联左侧树条件
		this.whereData[this.TREE_WHERE] = "";
		this.refresh(this.whereData);
		this._refreshNavTree();
	}
};

var gaojiSearch = _viewer.advSearch;
jQuery(".rh-advSearch-btn",gaojiSearch._pCon).unbind("click").bind("click",function() {
	var odeptCode = odeptObj.val();
	var hasSubOdeptFlagVal = hasSubOdeptFlagObj.val();
	
	if (gaojiSearch._parHandler) {
		gaojiSearch._parHandler.listBarTipLoad("加载中...");
		setTimeout(function() {
			gaojiSearch._parHandler._clearSearchValue();//清空普通查询
			var where = gaojiSearch.getWhere();
			var treeLink = false;
			var check = jQuery(".rh-advSearch-div",jQuery("#" + gaojiSearch._id)).find("input:checked");
			if ((gaojiSearch._treeLink == true) && (check.prop("checked") == true)) {
				check = true;
			}
			//if (where.length > 0) {
			var opts = {};
			opts["S_ODEPT"] = odeptCode;
			opts["HAS_SUB_ODEPT_FLAG"] = hasSubOdeptFlagVal;
			gaojiSearch._parHandler._setSearchWhereAndRefresh2(where,check,opts);//开始查询
			//}
			jQuery("#" + gaojiSearch._id).dialog("close");
		},0);
	}
});