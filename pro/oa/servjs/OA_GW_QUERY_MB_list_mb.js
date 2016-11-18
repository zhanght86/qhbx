var _viewer = this;

_viewer.grid.click(function(value, node){
	var dataId = node["GW_ID"];
	var title = node["GW_TITLE"];
	var servId = node["TMPL_CODE"];
	var params = {"handlerRefresh":_viewer};
	var options = {"url":servId + ".card.do?pkCode=" + dataId, "tTitle":title 
				,"menuFlag":3,"params":params};
	Tab.open(options);
}, _viewer);