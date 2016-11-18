var _viewer = this;

_viewer.grid.click(function(value, node){
	var dataId = node["DATA_ID"];
	var title = node["TITLE"];
	var servId = node["SERV_ID"];
	var params = {"handlerRefresh":_viewer};
	var options = {"url":servId + ".card.do?pkCode=" + dataId, "tTitle":title 
				,"menuFlag":3,"params":params};
	Tab.open(options);
}, _viewer);