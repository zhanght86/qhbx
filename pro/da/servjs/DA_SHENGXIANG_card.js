var _viewer = this;
//设置P_ID字段的值。
setPID(_viewer);

/**设置全宗号、全宗名**/
//针对添加, 如果没有值，默认添加当前机构的全宗号
if (_viewer.getItem("FONDS_ID").getValue() == "") {
	var userOdept = System.getVar("@ODEPT_CODE@");
	var data = {"ODEPT_CODE":userOdept};	
	
	var fonds = FireFly.doAct("DA_FONDS_NUMBER","getFonds",data);
	if(fonds["_OKCOUNT_"] != 0){
		_viewer.getItem("FONDS_NAME").setValue(fonds["FONDS_NAME"]);
		_viewer.getItem("FONDS_ID").setValue(fonds["FONDS_NUMBER"]);
	}
}

//处理打开档案卡片的时候，卡片上的按钮的类
var optsPubBtn = {};
optsPubBtn.parHandler = _viewer;
var cardPubBtnObj = new rh.da.cardPubBtn(optsPubBtn);
cardPubBtnObj.init();