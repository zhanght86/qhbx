var _viewer = this;

//重写查询选择的回调函数
_viewer.getItem("OFFICE_NAME")._choose.unbind("click").bind("click",function(event){
	var cmpy =System.getVar("@ODEPT_CODE@");
	var configStr = "OA_OFF_QUOTATION,{" +
							   "'SOURCE':'QUOTATION_ID~OFFICE_NAME~OFFICE_UNITE~OFFICE_PRICE~OFFICE_BRAND~OFFICE_MODEL~OFFICE_SPEC~OFFICE_TYPE~OFFICE_SELLER'," +
							   "'TARGET':'OFFICE_ID~OFFICE_NAME~OFFICE_UNITE~OFFICE_PRICE~OFFICE_BRAND~~~OFFICE_TYPE~','TYPE':'single'," +
							   "'EXTWHERE':\" and 1=1 and S_ODEPT = '" + cmpy + "' and PUBLIC_FLAG = '1'\"" +
							   "}";
	var options = {
			"config":configStr,
			"searchFlag":true,
			"replaceCallBack":function(idArray){
				chooseBackData(idArray);
			}
		};
	var queryView = new rh.vi.rhSelectListView(options);
	queryView.show(event);
});

//给[数量]元素添加键盘监听事件
jQuery("#OA_OFF_OUTSTORAGE-OFFICE_STOCK").keyup(function(event){
	getInStroageCount(this);
});

//给[数量]元素添加鼠标事件
jQuery("#OA_OFF_OUTSTORAGE-OFFICE_STOCK").bind("blur",function(event){
	getInStroageCount(this);
});

//给[单价]元素添加键盘监听事件
jQuery("#OA_OFF_OUTSTORAGE-OFFICE_PRICE").keyup(function(event){
	var amount = _viewer.getItem("OFFICE_STOCK").getValue() * jQuery(this).val();
	_viewer.getItem("OFFICE_AMOUNT").setValue(parseFloat(amount).toFixed(2));
});

//查询选择回调函数
function chooseBackData(idArray){
	_viewer.getItem("OFFICE_ID").setValue(idArray["QUOTATION_ID"]);
	_viewer.getItem("OFFICE_NAME").setValue(idArray["OFFICE_NAME"]);
	_viewer.getItem("OFFICE_UNITE").setValue(idArray["OFFICE_UNITE"]);
	_viewer.getItem("OFFICE_PRICE").setValue(idArray["OFFICE_PRICE"]);
	_viewer.getItem("OFFICE_BRAND").setValue(idArray["OFFICE_BRAND"]);
	_viewer.getItem("OFFICE_TYPE").setValue(idArray["OFFICE_TYPE"]);
	
	_viewer.getItem("APPLY_ID").setValue(_viewer.getParHandler().getParHandler().getItem("APPLY_ID").getValue());
	_viewer.getItem("GET_USER").setValue(_viewer.getParHandler().getParHandler().getItem("GIVE_USER").getValue());
	_viewer.getItem("GET_DEPT").setValue(_viewer.getParHandler().getParHandler().getItem("GIVE_DEPT").getValue());
	_viewer.getItem("GET_DATE").setValue(_viewer.getParHandler().getParHandler().getItem("GIVE_DATE").getValue());
	
	initAmount();
}

//当单价变化时，总金额也要变化
function initAmount(){
	var amount = _viewer.getItem("OFFICE_PRICE").getValue() *  _viewer.getItem("OFFICE_STOCK").getValue();
	_viewer.getItem("OFFICE_AMOUNT").setValue(parseFloat(amount).toFixed(2));
}

//获取库存中某个物品的数量，在出库时候进行比对，若出库数量大于库存数量，则显示库存最大数量
function getInStroageCount(thisObj){
	var outBean = FireFly.doAct("OA_OFF_OUTSTORAGE","inStorageCount",{"OFFICE_ID":_viewer.getItem("OFFICE_ID").getValue()});
	var count = parseInt(outBean["_OKCOUNT_"]);
	var number = parseInt(jQuery(thisObj).val());
	number == 0 ? jQuery(thisObj).val("") : "";
	if (count < number) {
		_viewer.cardBarTipError("出库数量大于库存数量");
		jQuery(thisObj).val("");
	}
	var amount = _viewer.getItem("OFFICE_PRICE").getValue() * (jQuery(thisObj).val() || 0);
	_viewer.getItem("OFFICE_AMOUNT").setValue(parseFloat(amount).toFixed(2));
}




