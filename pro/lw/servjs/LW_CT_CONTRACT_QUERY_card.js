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
				var url = "LW_CT_CONTRACT_QUERY_RESULT.list.do";
				var opts = {
					"url" : url,
					"tTitle" : "合同查询结果", 
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