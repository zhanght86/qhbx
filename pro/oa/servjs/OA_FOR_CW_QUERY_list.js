var _viewer = this;
params = _viewer.getParams(); //参数
var order_num = "";
if (params && ((params["order_num"] || "").length > 0)) {
	order_num = params["order_num"];
}

//完成按钮
var checkok = _viewer.getBtn("checkok"); //完成按钮
checkok.unbind("click").bind("click", function(){
	var dataArray = _viewer.grid.getSelectPKCodes();
	if (dataArray.length == 0) {
		_viewer.listBarTipError("请选中要关联的文件");
		return;
	} else {
		if (order_num == "") {
			_viewer.listBarTipError("缺少财务单据编号!");
			return;
		}
		var datas = new Array();
		for (var i = 0; i < dataArray.length; i++) {
			var data = {};
			var indexPkCode = dataArray[i];
			data["TYPE"] = _viewer.grid.getRowItemValue(indexPkCode, "SERV_NAME__NAME");
			data["ENTITY_CODE"] = _viewer.grid.getRowItemValue(indexPkCode, "ENTITY_CODE");
			data["TITLE"] = _viewer.grid.getRowItemValue(indexPkCode, "TITLE");
			data["SERV_ID"] = _viewer.grid.getRowItemValue(indexPkCode, "SERV_ID");
			data["DATA_ID"] = _viewer.grid.getRowItemValue(indexPkCode, "DATA_ID");
			datas.push(data);
		}
		var out = FireFly.doAct(_viewer.servId, "sendRequest", 
				{
					"DATA_ARRAY" : jQuery.toJSON(datas), 
					"order_num" : order_num,
					"data_ids" : dataArray.join(",")
				});
		if (out["_MSG_"].indexOf("OK,") >= 0) {
			_viewer.listBarTip("关联文件成功!");
			if (!confirm("是否继续关联？")) {
				top.close();
			}
			//关闭窗口
		} else {
			_viewer.listBarTipError("关联文件失败!");
		}
	}
});

//取消按钮
var cancel = _viewer.getBtn("cancel");
cancel.unbind("click").bind("click", function(){
	//关闭当前窗口
	top.close();
});

//关联OA审批单查询  服务中回显已关联选择
var outBean = FireFly.doAct("OA_FOR_CW_QUERY_ECHO","orderreload",{"ORDER_NUM":order_num});
var dataIdsObj = outBean["DATA_IDS_OBJ"] || "";
if (dataIdsObj) {
	var dataIdsArray = dataIdsObj["DATA_IDS"].split(",");
	var checkObjs = _viewer.grid.getCheckBox();	
	for (var i = 0; i < checkObjs.length; i++){		
		for (var j = 0; j < dataIdsArray.length; j++) {
			if(_viewer.grid.getRowItemValue(checkObjs[i].name,"DATA_ID") == dataIdsArray[j]){
				 jQuery("input[name =" +checkObjs[i].name+"]").attr("checked","true");				  	
			}			
		}
	}	
}