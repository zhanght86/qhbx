var _viewer = this;

//重写查询选择的回调函数
_viewer.getItem("OFFICE_NAME")._choose.unbind("click").bind("click",function(event){
	var cmpy =System.getVar("@ODEPT_CODE@");
	var configStr = "OA_OFF_QUOTATION,{" +
							   "'SOURCE':'QUOTATION_ID~OFFICE_NAME~OFFICE_UNITE~OFFICE_PRICE~OFFICE_BRAND~OFFICE_MODEL~OFFICE_SPEC~OFFICE_TYPE~OFFICE_SELLER'," +
							   "'TARGET':'OFFICE_ID~OFFICE_NAME~OFFICE_UNITE~OFFICE_PRICE~OFFICE_BRAND~~~OFFICE_TYPE~','TYPE':'single'," +
							   "'EXTWHERE':\" and 1=1 and S_ODEPT = '" + cmpy + "' and PUBLIC_FLAG = 1\"" +
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
_viewer.getItem("OFFICE_NUMBER").obj.keyup(function(event){
	var amount = _viewer.getItem("OFFICE_PRICE").getValue() * jQuery(this).val();
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
	initAmount();
}

//当单价变化时，总金额也要变化
function initAmount(){
	var amount = _viewer.getItem("OFFICE_PRICE").getValue() *  _viewer.getItem("OFFICE_NUMBER").getValue();
	_viewer.getItem("OFFICE_AMOUNT").setValue(parseFloat(amount).toFixed(2));
}




