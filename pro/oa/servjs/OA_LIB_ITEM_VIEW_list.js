var _viewer = this;
//把列表对象放到Data中，供其它引用的JS使用
jQuery(document).data("_viewer",_viewer);

jQuery("#rh_search_treeLink").attr("disabled","true");

var DICT_ID = "@com.rh.oa.lib.DeptDict";
_viewer.beforeTreeNodeClickLoad = function(){
	//如果没有选中任何节点，则以根节点的值来查询。
	var treeWhere = _viewer.whereData[_viewer.TREE_WHERE];
	if(treeWhere.length == 0){ //如果没有选中任何节点，那么默认选择根节点
		var rootNodeId = _viewer.nvaTreeArray[DICT_ID].getRootNodeId();
		_viewer.whereData[_viewer.TREE_WHERE] = [{
			"DICT_ITEM" : "S_TDEPT",
			"DICT_VALUE": rootNodeId
		}];
	}
};

//第一次进入页面时，默认选中根节点
if(_viewer.nvaTreeArray[DICT_ID].getCheckedNodes() == 0){
	var rootNodeId = _viewer.nvaTreeArray[DICT_ID].getRootNodeId();
	_viewer.nvaTreeArray[DICT_ID].selectNodes([rootNodeId]);
}

/** 删除灰色的按钮 */
_viewer.grid.getBtn("download").each(function(){
	if(jQuery(this).hasClass("rh-icon-disable")){
		jQuery(this).remove();
	}
});

//下载按钮绑定事件
_viewer.grid.getBtn("download").unbind().click(function(btnObj){
	if(!jQuery(this).hasClass("rh-icon-disable")){
		window.open("/OA_LIB_ITEM.download.do?_PK_=" + _viewer.grid.getRowPkByElement(jQuery(this)),"","");
	}
});

//关闭当前Tab，打开总公司列表页面
_viewer.getBtn("zgsFile").unbind().bind("click", function() {
	Tab.open({
		"url" : "OA_LIB_ITEM_VIEW_ZGS.list.do",
		"tTitle" : "文档阅读（总公司）",
		"menuFlag" : 4
	});	
	_viewer.backImg.mousedown();
	return false;
});

//关闭当前Tab，打开分公司列表页面
_viewer.getBtn("fgsFile").unbind().bind("click", function() {
	Tab.open({
		"url" : "OA_LIB_ITEM_VIEW_FGS.list.do",
		"tTitle" : "文档阅读（分公司）",
		"menuFlag" : 4
	});	
	_viewer.backImg.mousedown();
	return false;
});

//关闭当前Tab，打开分公司列表页面
_viewer.getBtn("selfFile").unbind().bind("click", function() {
	Tab.open({
		"url" : "OA_LIB_ITEM_VIEW.list.do",
		"tTitle" : "文档阅读（本机构）",
		"menuFlag" : 4
	});	
	_viewer.backImg.mousedown();
	return false;
});

