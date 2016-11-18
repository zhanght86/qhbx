var _viewer = this;
//取消行双击事件
_viewer.grid.unbindTrdblClick();
/**
 * 绑定打开行记录查看新闻。
 */
_viewer.grid.trClick(function(value,node) {
	var articleId = _viewer.grid.getSelectItemVal("ARTICLEID");
	var url = FireFly.getHttpHost() + "/bnnews/get_news?articleId="+articleId;
	window.open(encodeURI(url));
},_viewer);