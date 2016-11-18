var _viewer = this;

_viewer.grid.unbindTrdblClick();
_viewer.grid.dblClick(function(value,node) {
	var dataId = _viewer.grid.getSelectItemVal("NEWS_ID");
	var dateStr = _viewer.grid.getSelectItemVal("NEWS_TIME");
	
	
	window.open("/html/" + dateStr.replace(new RegExp("-","gm"),"") + "/" + dataId + ".html");
	
},_viewer);

