var _viewer = this;

//给下拉列表绑定事件
var plateNumObj = _viewer.form.getItem("BAK_PLATE_NUMBER").obj;
plateNumObj.unbind("change").bind("change", function(){
	//如果当前选择为空
	if ("" == (plateNumObj.val() || "")) {
		_viewer.getItem("VEH_NAME").setValue("");
	//如果下拉框不为空
	} else {
		//获取车辆信息
		var returnBean = FireFly.doAct("OA_VEH_VEHICLE", "getVehPlateNumber", {"VEH_PLATE_NUMBER":plateNumObj.val()});
		//如果车牌号重复，则返回错误信息
		/*if (vehNameObj.length > 1) {
			_viewer.cardBarTipError("车牌号" + plateNumObj.val() + "出现重复");
			plateNumObj.val("");
			return;
		}*/
		//填充车辆名称
		_viewer.getItem("VEH_NAME").setValue(returnBean["VEH_NAME"] || "");
	}
});
