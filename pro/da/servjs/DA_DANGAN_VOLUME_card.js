var _viewer = this ;
//从卷信息中获取到卷的档号
var vNum = _viewer.getParHandler().getParHandler().getItem("DA_CODE").getValue() || "";
if(vNum != ""){
	_viewer.getItem("V_NUM").setValue(vNum);
}
/**默认添加当前机构的全宗号**/
if (_viewer.getItem("FONDS_ID").getValue() == "") {
	var userOdept = System.getVar("@ODEPT_CODE@");
	var data = {"ODEPT_CODE":userOdept};	
	
	var fonds = FireFly.doAct("DA_FONDS_NUMBER","getFonds",data);
	if(fonds["_OKCOUNT_"] != 0){
		_viewer.getItem("FONDS_NAME").setValue(fonds["FONDS_NAME"]);
		_viewer.getItem("FONDS_ID").setValue(fonds["FONDS_NUMBER"]);
	}
}