var _viewer = this;

_viewer.setTitle("入库明细添加");

//重写查询选择的回调函数
_viewer.getItem("OFFICE_NAME")._choose.unbind("click").bind("click",function(event){
	var cmpy =System.getVar("@ODEPT_CODE@");
	var configStr = "OA_OFF_QUOTATION,{" +
							   "'SOURCE':'QUOTATION_ID~OFFICE_NAME~OFFICE_UNITE~OFFICE_PRICE~OFFICE_BRAND~OFFICE_MODEL~OFFICE_SPEC~OFFICE_TYPE~OFFICE_SELLER'," +
							   "'TARGET':'OFFICE_ID~OFFICE_NAME~OFFICE_UNITE~OFFICE_PRICE~OFFICE_BRAND~OFFICE_MODEL~~OFFICE_TYPE~','TYPE':'single'," +
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
jQuery("#OA_OFF_INSTORAGE-OFFICE_STOCK").keyup(function(event){
	var amount = jQuery("#OA_OFF_INSTORAGE-OFFICE_PRICE").val() * jQuery(this).val();
	jQuery("#OA_OFF_INSTORAGE-OFFICE_AMOUNT").val(parseFloat(amount).toFixed(2));
	jQuery("#OA_OFF_INSTORAGE-RESIDUE_NUMBER").val(jQuery(this).val());
});

//给[单价]元素添加键盘监听事件
jQuery("#OA_OFF_INSTORAGE-OFFICE_PRICE").keyup(function(event){
	var amount = jQuery("#OA_OFF_INSTORAGE-OFFICE_STOCK").val() * jQuery(this).val();
	jQuery("#OA_OFF_INSTORAGE-OFFICE_AMOUNT").val(parseFloat(amount).toFixed(2));
});

//查询选择回调函数
function chooseBackData(idArray){
	jQuery("#OA_OFF_INSTORAGE-OFFICE_ID").val(idArray["QUOTATION_ID"]);
	jQuery("#OA_OFF_INSTORAGE-OFFICE_NAME").val(idArray["OFFICE_NAME"]);
	jQuery("#OA_OFF_INSTORAGE-OFFICE_UNITE").val(idArray["OFFICE_UNITE"]);
	jQuery("#OA_OFF_INSTORAGE-OFFICE_PRICE").val(idArray["OFFICE_PRICE"]);
	jQuery("#OA_OFF_INSTORAGE-OFFICE_BRAND").val(idArray["OFFICE_BRAND"]);
	jQuery("#OA_OFF_INSTORAGE-OFFICE_MODEL").val(idArray["OFFICE_MODEL"]);
	jQuery("#OA_OFF_INSTORAGE-OFFICE_TYPE").val(idArray["OFFICE_TYPE"]);
	initAmount();
}

//当单价变化时，总金额也要变化
function initAmount(){
	var amount = jQuery("#OA_OFF_INSTORAGE-OFFICE_PRICE").val() * jQuery("#OA_OFF_INSTORAGE-OFFICE_STOCK").val();
	jQuery("#OA_OFF_INSTORAGE-OFFICE_AMOUNT").val(parseFloat(amount).toFixed(2));
}






