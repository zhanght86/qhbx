(function(_viewer){
	var servId = _viewer.servId;
	
	init();
	function init() {
		// 删除保存按钮
		var btnSave = _viewer.getBtn("save");
		btnSave.remove();
		
		var queryBtn = _viewer.getBtn("query");
		var clearBtn = _viewer.getBtn("clear");
		if (queryBtn) {
			queryBtn.unbind("click").click(function(){
				var queryData = _viewer.itemValues();
				queryData["SEARCH_FLAG"] = "true";
				var url = "OA_GW_TMPL_QS_QUERY_LIST.list.do";
				var opts = {
					"url" : url,
					"tTitle" : "请示报告查询结果",
					"params" : queryData,
					"menuFlag" : 4
				};
				Tab.open(opts);
			});
		}
		
		if (clearBtn) {
			clearBtn.unbind("click").click(function(){
				_viewer.refresh();
			});
		}
	}
})(this);