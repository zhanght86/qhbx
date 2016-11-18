var _viewer = this;

//去除列表双击事件
_viewer.grid.getBodyTr().unbind("dblclick");

//去除平铺查询中[名称]的[选择]和[取消]链接
_viewer.getTileItem("OFFICE_NAME").find("a").remove();

var params = _viewer.getParams() || {"OUT_FLAG":""};

if (params["OUT_FLAG"]== "OUT") { //若为直接出库，而不是申请单出库，则[出库]列不可操作
	jQuery("th[icode='outStorage']").remove();
	_viewer.grid.getTdItems("outStorage").remove();
} else {
	_viewer.getBtn("batchSave").remove(); //删除保存按钮
	//给列表[出库]按钮绑定事件
	_viewer.grid.getBtn("outStorage").unbind("click").bind("click",function(event) {
		var thisPkCode = jQuery(this).attr("rowpk")
		var decData = {"OFFICE_ID":_viewer.grid.getRowItemValue(thisPkCode,"OFFICE_ID"),
					   "OFFICE_STOCK":_viewer.grid.getRowItemValue(thisPkCode,"OFFICE_STOCK")
					  };
		//判断库存量是否大于出库量
		var decSave = FireFly.doAct("OA_OFF_OUTSTORAGE","decInStorage", decData);
		if (decSave["FLAG"]) { //若库存量大于出库量，则做出库操作
			var data = {"OFFICE_ID":_viewer.grid.getRowItemValue(thisPkCode,"OFFICE_ID"),
						"APPLY_ID":_viewer.grid.getRowItemValue(thisPkCode,"APPLY_ID"),
						"OFFICE_NAME":_viewer.grid.getRowItemValue(thisPkCode,"OFFICE_NAME"),
						"OFFICE_UNITE":_viewer.grid.getRowItemValue(thisPkCode,"OFFICE_UNITE"),
						"OFFICE_PRICE":_viewer.grid.getRowItemValue(thisPkCode,"OFFICE_PRICE"),
						"OFFICE_BRAND":_viewer.grid.getRowItemValue(thisPkCode,"OFFICE_BRAND"),
						"OFFICE_STOCK":_viewer.grid.getRowItemValue(thisPkCode,"OFFICE_NUMBER"),
						"OFFICE_AMOUNT":_viewer.grid.getRowItemValue(thisPkCode,"OFFICE_AMOUNT"),
						"OFFICE_TYPE":_viewer.grid.getRowItemValue(thisPkCode,"OFFICE_TYPE"),
						"GET_USER":_viewer.getParHandler().getItem("GIVE_USER").getValue(),
						"GET_DEPT":_viewer.getParHandler().getItem("GIVE_DEPT").getValue(),
						"GET_DATE":_viewer.getParHandler().getItem("GIVE_DATE").getValue(),
						"OUTSTORAGE_STATUS":"2", //发放状态
						"APPLY_TYPE":_viewer.grid.getRowItemValue(thisPkCode,"APPLY_TYPE"),
						"APPLY_STATUS":_viewer.grid.getRowItemValue(thisPkCode,"APPLY_STATUS")
					   };
			var saveObj = FireFly.doAct("OA_OFF_OUTSTORAGE","save",data);
			if (saveObj["_ADD_"]) {
				_viewer.refresh();//刷新列表
			}
		} else {
			_viewer.listBarTipError("申请用品个数大于库存个数");
		}
	});

	//遍历数据
	_viewer.grid.getBodyTr().each(function() {
		if (jQuery(this).find("[icode='OUTSTORAGE_STATUS']").html() == "2") {
			jQuery(this).find("[icode='outStorage']").css({"color":"red","text-align":"center"}).html("已出库");
		}
		bindNumItemEvent(jQuery(this).find("[icode='OFFICE_STOCK']"), jQuery(this).find("[icode='OFFICE_ID']")
						,jQuery(this).find("[icode='OFFICE_PRICE']"), jQuery(this).find("[icode='OFFICE_AMOUNT']")
						,jQuery(this).find("[icode='OFFICE_STOCK']").find("input").val());
		//格式化数字
		var officePrice = jQuery(this).find("[icode='OFFICE_PRICE']").html();
		jQuery(this).find("[icode='OFFICE_PRICE']").html(parseFloat(officePrice).toFixed(2));
		var officeAmount = jQuery(this).find("[icode='OFFICE_AMOUNT']").html();
		jQuery(this).find("[icode='OFFICE_AMOUNT']").html(parseFloat(officeAmount).toFixed(2));
	});
}

//给数量绑定事件
function bindNumItemEvent(thisNumberObj, thisOffID, thisPictObj, thisAmountObj, thisOldNumVal){
	thisNumberObj.unbind("blur keyup").bind("blur keyup", function(){
		getInStroageCount(thisNumberObj, thisOffID, thisPictObj, thisAmountObj, thisOldNumVal);
	});
}

//获取库存中某个物品的数量，在出库时候进行比对，若出库数量大于库存数量，则显示原有数量
function getInStroageCount(thisNumberObj, thisOffID, thisPictObj, thisAmountObj,thisOldNumVal){
	var outBean = FireFly.doAct("OA_OFF_OUTSTORAGE","inStorageCount",{"OFFICE_ID":thisOffID.html()});
	var count = parseInt(outBean["_OKCOUNT_"]);
	var number = parseInt(thisNumberObj.find("input").val()) || 0;
	number == 0 ? thisNumberObj.find("input").val(thisOldNumVal) : "";
	if (count < number) {
		_viewer.listBarTipError("出库数量大于库存数量");
		thisNumberObj.find("input").val(thisOldNumVal);
	}
	var amount = thisPictObj.html() * (thisNumberObj.find("input").val() || 0);
	thisAmountObj.html(parseFloat(amount).toFixed(2));
}



