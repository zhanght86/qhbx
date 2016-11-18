var _viewer = this;
function newsView(id) {
	var url = "/cms/SY_COMM_INFOS/" + id + ".html";
	window.open(url);
}
_viewer.grid.unbindTrdblClick();
_viewer.grid.unbindIndexTDClick();
_viewer.grid.dblClick(function(id, node) {
	newsView(id);
		}, _viewer);
