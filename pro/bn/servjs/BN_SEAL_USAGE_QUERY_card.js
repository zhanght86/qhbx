var _viewer = this;
//删除保存按钮
var btnSave = _viewer.getBtn("save");
btnSave.remove();
var queryBtn = _viewer.getBtn("query");
var clearBtn = _viewer.getBtn("clear");
if (queryBtn) {
	queryBtn.unbind("click").click(function(){
		var where = "";
		var cmpy = _viewer.getItem("SEAL_CMPY").getValue() || "";
		var dept = _viewer.getItem("SEAL_DEPT").getValue() || "";
		var type1 = _viewer.getItem("SEAL_TYPE1").getValue() || "";
		var type2 = _viewer.getItem("SEAL_TYPE2").getValue() || "";
		var sealType = _viewer.getItem("BN_SEAL_TYPE").getValue() || "";
		if(cmpy != ""){
			where += " and S_CMPY='"+cmpy+"' ";
		}
		if(dept != ""){
			where += " and S_DEPT='"+dept+"' ";
		}
		if(type1 != ""){
			where += " and SEAL_TYPE1='"+type1+"' ";
			if(type2 != ""){
				where += " and SEAL_TYPE2='"+type2+"' ";
			}
		}
		if(type2 != ""){
			where += " and BN_SEAL_TYPE='"+sealType+"' ";
		}
		var url = "BN_SEAL_USAGE_APPLY.list.do";
		var opts = {
			"url" : url,
			"tTitle" : "印章查询结果", 
			"params" : {"_extWhere":where},
			"menuFlag" : 4
		};
		Tab.open(opts);
	});
}
if(clearBtn){
	clearBtn.unbind("click").click(function(){
		_viewer.refresh();
	});
}


