/** 服务卡片使用的js方法定义：开始 */
var _viewer = this;

_viewer.grid.getBtn("view").unbind("click").bind("click", function(event) {
//	var pk = jQuery(this).attr("rowpk");
//	Tab.open({'url':pk + '.list.do','tTitle':_viewer.grid.getRowItemValue(pk, 'SERV_NAME'),'menuFlag':3});
//	event.stopPropagation();
	var rowPk = jQuery(this).attr("rowpk");//获取主键信息
	var dataId = _viewer.grid.getRowItemValue(rowPk, 'DATA_ID');
	var title = _viewer.grid.getRowItemValue(rowPk, 'DATA_DIS_NAME');
	var url = '/wenku/content/' +  dataId+  '.html';
	Tab.open({'url':url,'tTitle':title,'menuFlag':3});
	return false;
});