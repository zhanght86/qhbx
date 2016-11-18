var _viewer = this;

//单价/每公里绑定事件，精确到小数点两位
_viewer.count4TwoObj(_viewer.form.getItem("COS_PRICE").obj, _viewer.form.getItem("COS_KM").obj,  _viewer.form.getItem("COS_TOTAL").obj);

//开始时间和结束时间之间的联动
rhDate.compareTwoDate(_viewer.form.getItem("COS_BEFDATE").obj, _viewer.form.getItem("COS_ENDDATE").obj);

_viewer.getItem("VEH_ID").callback = function(arrayId, arrayName) {
	getVehNumber(arrayId);
};

//获取车牌号
function getVehNumber(vehId){
	var outBean = FireFly.doAct("OA_VEH_VEHICLE", "byid", {"_PK_":vehId[0]});
	_viewer.getItem("PLATE_NUMBER").setValue(outBean["VEH_PLATE_NUMBER"]);
}

//车辆名称[清除]图标事件绑定
_viewer.getItem("VEH_ID")._cancel.unbind("click").bind("click", function(){
		_viewer.getItem("VEH_ID").setValue("");
		_viewer.getItem("VEH_ID").setText("");
		_viewer.getItem("PLATE_NUMBER").setValue("");
});