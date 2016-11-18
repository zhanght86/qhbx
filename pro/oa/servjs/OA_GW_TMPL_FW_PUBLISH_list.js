var _viewer = this;

//接触列表行双击事件
_viewer.grid.unbindTrdblClick();
_viewer.grid.dblClick(function(value, node) {
		var trackObj = {};
		trackObj.SERV_ID = _viewer.grid.getSelectItemVal("TMPL_CODE");
		trackObj._PK_ = _viewer.grid.getSelectItemVal("GW_ID");
		var opts = {
			"url" : "OA_GW_TMPL_VIEW.doc.do?data=" + jQuery.toJSON(trackObj),
			"tTitle" : "公文发布",
			"params" : trackObj,
			"menuFlag" : 3
		};
		Tab.open(opts);
}, _viewer);

