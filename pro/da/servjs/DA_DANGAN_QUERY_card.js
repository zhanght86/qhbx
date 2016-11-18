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
				//var queryData = _viewer.itemValues();
				var where = " and S_ODEPT='"+System.getVar("@ODEPT_CODE@")+"'";
				var catID = _viewer.getItem("DA_CATEGORY").getValue() || "";
				var gdBuMen = _viewer.getItem("GD_DEPT_NAME").getValue() || "";
				var title = _viewer.getItem("TITLE").getValue() || "";
				var daYear = _viewer.getItem("DA_YEAR").getValue() || "";
				var gwCode = _viewer.getItem("GW_CODE").getValue() || "";
				var daTerm = _viewer.getItem("DA_TERM").getValue() || "";
				//var secret = _viewer.getItem("SECRET").getValue() || "";
				var daCode = _viewer.getItem("DA_CODE").getValue() || "";
				//var daNum = _viewer.getItem("DA_NUM").getValue() || "";
				//var boxNum = _viewer.getItem("BOX_NUM").getValue() || "";
				if(catID == ""){
					_viewer.cardBarTipError("请选择档案分类");
					return false;
				}
				//如果选择的是卷
				if(gdBuMen != ""){
					where += " and GD_DEPT_NAME='"+gdBuMen+"' ";
				}
				if(title != ""){
					where += " and TITLE='"+title+"' ";
				}
				if(daYear != ""){
					where += " and DA_YEAR="+daYear+" ";
				}
				if(gwCode != ""){
					where += " and GW_CODE='"+gwCode+"' ";
				}
				if(daTerm != ""){
					where += " and DA_TERM='"+daTerm+"' ";
				}
//				if(secret != ""){
//					where += " and SECRET='"+secret+"' ";
//				}
				if(daCode != ""){
					where += " and DA_CODE='"+daCode+"' ";
				}
//				if(daNum != ""){
//					where += " and DA_NUM="+daNum+" ";
//				}
//				if(boxNum != ""){
//					where += " and BOX_NUM="+boxNum+" ";
//				}
				var url = catID + ".list.do";
				
				var opts = {
					"url" : url,
					"tTitle" : "档案查询结果", 
					"params" : {"_extWhere":where},
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