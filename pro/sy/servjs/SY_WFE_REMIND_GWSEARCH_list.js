var _viewer = this;

/**
 * 打开行数据对应的审批单
 */
_viewer.grid.unbindTrdblClick();
_viewer.grid.dblClick(function(value,node) {
	var servId = "OA_SY_WFE_REMIND";
	var dataId = _viewer.grid.getSelectItemVal("REMD_ID");
	var title = _viewer.grid.getSelectItemVal("REMD_TITLE");
	var params = {"handlerRefresh":_viewer};
	var options = {"url":servId + ".card.do?readOnly=true&pkCode=" + dataId, "tTitle":title,"menuFlag":3,"params":params};
	Tab.open(options);
},_viewer);

