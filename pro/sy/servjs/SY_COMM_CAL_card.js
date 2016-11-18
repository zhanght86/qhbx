var _viewer = this;

if(!MB){
	var MB = {
		"serv": "SY_COMM_CAL",
		"pk": "CAL_ID",
		"sdt": "CAL_START_TIME",
		"edt": "CAL_END_TIME"
	}
}
if(!RHTimeCenter){
	var RHTimeCenter = {
		"before_can_modify":true
	}
}

if(_viewer.miniCard){
	_viewer.getBtn("save").hide();
}
var saveBtn = _viewer.getBtn("saveEve");
var delBtn = _viewer.getBtn("deleteEve");
var sdt = _viewer.opts.sdt;
_viewer.refreshA.hide();

//过去开始的记录是否能修改
//if(Date.parse(sdt) < Date.parse(Date())){
//	saveBtn.hide();
//	delBtn.hide();
//}

if(_viewer.getPKCode() == ""){
	delBtn.hide();
}

_viewer.beforeSave = function(){
	if(Date.parse(_viewer.itemValue(MB.sdt)) >= Date.parse(_viewer.itemValue(MB.edt))){
		_viewer.cardClearTipLoad();
		alert("开始时间必须小于结束时间");
		return false;
	}
	if(!RHTimeCenter.before_can_modify && Date.parse(_viewer.itemValue(MB.sdt)) < Date.parse(new Date())){
		_viewer.cardClearTipLoad();
		alert("开始时间必须大于当前时间");
		return false;
	}
};
