var _viewer = this;

//给列表[出库]按钮绑定事件

var oldDataArray = new Array(); //获取列表初始化旧数据

_viewer.grid.getBtn("outStorage").each(function() {
	var _thisObj = this;
	jQuery(_thisObj).unbind("click").bind("click",function(event) {
		var thisPkCode = jQuery(_thisObj).attr("rowpk");
		var decData = {"OFFICE_ID":_viewer.grid.getRowItemValue(thisPkCode, "OFFICE_ID"),
					   "OFFICE_STOCK":_viewer.grid.getRowItemValue(thisPkCode, "OFFICE_NUMBER")
					  };
		//判断库存量是否大于出库量
		var decSave = FireFly.doAct("OA_OFF_OUTSTORAGE","decInStorage", decData);
		if (decSave["FLAG"] == "true") {  //若库存量大于出库量，则做出库操作
			outStorage(thisPkCode);
		} else {
			_viewer.listBarTipError("申请用品个数大于库存个数");
		}
	});
});

//遍历数据
_viewer.grid.getBodyTr().each(function() {
	jQuery(this).find("td[icode='OFFICE_AMOUNT']").find("input").eq(0).attr("readonly",true).css({"text-align":"right"});
	if (jQuery(this).find("[icode='OUTSTORAGE_STATUS']").html() == "2") {
		jQuery(this).unbind("dblclick");
		jQuery(this).find("input[class='rowIndex']").remove();
		var thisInpVal = jQuery(this).find("td[icode='OFFICE_NUMBER']").find("input").eq(0).val();
		jQuery(this).find("td[icode='OFFICE_NUMBER']").html(thisInpVal);
		var thisSelText = jQuery(this).find("td[icode='APPLY_TYPE__NAME']").find("select").eq(0).find("option:selected").text();
		var thisSelVal = jQuery(this).find("td[icode='APPLY_TYPE__NAME']").find("select").eq(0).val();
		jQuery(this).find("td[icode='APPLY_TYPE__NAME']").html(thisSelText);
		jQuery(this).find("td[icode='APPLY_TYPE']").html(thisSelVal);
		jQuery(this).find("td[icode='outStorage']").css({"color":"red","text-align":"center"}).html("已出库");
	} else {
		oldDataArray.push({
			"PK": jQuery(this).attr("id"),
			"VAL": jQuery(this).find("td[icode='OFFICE_NUMBER']").find("input").eq(0).val()
		}); //存储旧的数据值
		
		//修改样式
		jQuery(this).find("td[icode='OFFICE_NUMBER']").find("input").eq(0).css({"text-align":"right"});
		bindNumItemEvent(jQuery(this).find("td[icode='OFFICE_NUMBER']")
					,jQuery(this).find("td[icode='OFFICE_ID']")
					,jQuery(this).find("td[icode='OFFICE_PRICE']")
					,jQuery(this).find("td[icode='OFFICE_AMOUNT']").find("input").eq(0)
					,jQuery(this).find("td[icode='OFFICE_NUMBER']").find("input").val());
		//格式化数字
		var officePrice = jQuery(this).find("td[icode='OFFICE_PRICE']").html();
		jQuery(this).find("td[icode='OFFICE_PRICE']").html(parseFloat(officePrice).toFixed(2));
		var officeAmount = jQuery(this).find("td[icode='OFFICE_AMOUNT']").find("input").eq(0).val();
		jQuery(this).find("td[icode='OFFICE_AMOUNT']").find("input").eq(0).val(parseFloat(officeAmount).toFixed(2));
	}
});

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
	thisAmountObj.val(parseFloat(amount).toFixed(2));
}

//出库操作
function outStorage(thisPkCode){
	var data = {"OFFICE_ID":_viewer.grid.getRowItemValue(thisPkCode, "OFFICE_ID"),
				"APPLY_ID":_viewer.grid.getRowItemValue(thisPkCode, "APPLY_ID"),
				"OFFICE_NAME":_viewer.grid.getRowItemValue(thisPkCode, "OFFICE_NAME"),
				"OFFICE_UNITE":_viewer.grid.getRowItemValue(thisPkCode, "OFFICE_UNITE"),
				"OFFICE_PRICE":_viewer.grid.getRowItemValue(thisPkCode, "OFFICE_PRICE"),
				"OFFICE_BRAND":_viewer.grid.getRowItemValue(thisPkCode, "OFFICE_BRAND"),
				"OFFICE_STOCK":_viewer.grid.getRowItemValue(thisPkCode, "OFFICE_NUMBER"),
				"OFFICE_AMOUNT":_viewer.grid.getRowItemValue(thisPkCode, "OFFICE_AMOUNT"),
				"OFFICE_TYPE":_viewer.grid.getRowItemValue(thisPkCode, "OFFICE_TYPE"),
				"GET_USER":_viewer.getParHandler().getItem("GIVE_USER").getValue(),
				"GET_DEPT":_viewer.getParHandler().getItem("GIVE_DEPT").getValue(),
				"GET_DATE":_viewer.getParHandler().getItem("GIVE_DATE").getValue(),
				"OUTSTORAGE_STATUS":"2", //发放状态
				"APPLY_TYPE":_viewer.grid.getRowItemValue(thisPkCode, "APPLY_TYPE")
			   };
		if (!isMotifyNumVal(thisPkCode, data["OFFICE_STOCK"])){ //校验数据是否保存
			return;
		}
		var saveObj = FireFly.doAct("OA_OFF_OUTSTORAGE","save",data);
		if (saveObj["_ADD_"]) {
			var updateObj = FireFly.doAct("OA_OFF_OUTSTORAGE","updateOffTraits"
						   ,{"SERV_ID":"OA_OFF_APPLY_TRAITS_GIVE",
						   	 "PK_CODE":_viewer.grid.getRowItemValue(thisPkCode, "TRAITS_ID"),
							 "APPLY_ID":_viewer.grid.getRowItemValue(thisPkCode, "APPLY_ID")
							});
			if (updateObj["_MSG_"].indexOf("OK,") >= 0) {
				_viewer.listBarTip("出库成功");
				_viewer.refresh();//刷新列表
			} else {
				_viewer.listBarTipError(updateObj["_MSG_"].replaceAll("ERROR,", ""));
			}
		} else {
			_viewer.listBarTipError("出库失败");
		}
}

//判断当前数值是否已保存
function isMotifyNumVal(thisPkCode, thisNumVal){
	for (var i = 0; i < oldDataArray.length; i++){
		if (oldDataArray[i]["PK"] == thisPkCode && oldDataArray[i]["VAL"] != thisNumVal) {
			_viewer.listBarTipError("请先保存修改的数据，再做[出库]操作");
			return false;
		}
	}
	return true;
}
