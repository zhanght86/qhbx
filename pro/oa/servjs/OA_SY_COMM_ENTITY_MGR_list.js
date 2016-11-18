var _viewer = this;
// 对列表上选中行双击查看绑定事件
_viewer.grid.unbindTrdblClick();
_viewer.grid.dblClick(function(value, node) {
	// 双击打开该选中行的服务卡片页面
	Todo.openEntity(_viewer);
}, _viewer);

var gaojiSearch = _viewer.advSearch;
gaojiSearch.beforeSearch = function(){
	var val = gaojiSearch.getItemVal("S_ODEPT");
	if(val == ""){
		Tip.showError("请选择所属机构","list");
		return false;
	}
	
	return true;
}