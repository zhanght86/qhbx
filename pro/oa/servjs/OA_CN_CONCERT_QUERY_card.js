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
				var url = "OA_CN_CONCERT.list.do";
				var opts = {
					"url" : url,
					"tTitle" : "工作协调单查询结果",
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
		
		// 添加查询按钮
//		var width = _viewer.form.getItemWidth(_viewer.form.cols, _viewer.form.cols);
//		width = {"itemWidth":width.ITEM_WIDTH, "leftWidth":width.LEFT_WIDTH, "rightWidth":width.RIGHT_WIDTH, "maxWidth":width.MAX_WIDTH};
//		var item = new rh.ui.Item(width);
//		item.getObj().css({"margin-top":"20px"});
//		item.addLabel(jQuery("<div>&nbsp;</div>"));
//		var queryBtn = jQuery("<a class='rh-icon rhGrid-btnBar-a' style='margin-left:0;' id='OA_CN_CONCERT_QUERY-query' actcode='query'>" +
//				"<span class='rh-icon-inner'>查询</span><span class='rh-icon-img btn-search'/></a>");
//		
//		queryBtn.click(function(){
//			var queryData = _viewer.itemValues();
//			queryData["SEARCH_FLAG"] = "true";
//			var url = "OA_CN_CONCERT.list.do";
//			var opts = {
//				"url" : url,
//				"tTitle" : "工作协调单查询结果",
//				"params" : queryData,
//				"menuFlag" : 4
//			};
//			Tab.open(opts);
//		});
		
//		var clearBtn = jQuery("<a class='rh-icon rhGrid-btnBar-a' style='margin-left:0;' id='OA_CN_CONCERT_QUERY-query' actcode='query'>" +
//		"<span class='rh-icon-inner'>清除条件</span><span class='rh-icon-img btn-clear'/></a>");
//		clearBtn.click(function(){
//			_viewer.refresh();
//		});
		
//		item.addContent(jQuery("<span></span>").append(queryBtn.css({"margin-right":"10px"})).append().append(clearBtn));
//		jQuery(".inner").last().after(item.getObj());
	}
})(this);