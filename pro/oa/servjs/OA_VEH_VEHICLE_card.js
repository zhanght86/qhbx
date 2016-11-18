var _viewer = this;

var numberIsExist = false; //车牌号存在
var engineIsExist = false; //发动机号存在
var distinguishIsExist = false; //车辆识别码存在

//获取初始值
var numberInitVal = _viewer.getItem("VEH_PLATE_NUMBER").getValue();
var engineInitVal = _viewer.getItem("VEH_ENGINE_CODE").getValue();
var distinguishInitVal = _viewer.getItem("VEH_DISTINGUISH_CODE").getValue();

/**
 * 车牌号不可重复
 */
_viewer.form.getItem("VEH_PLATE_NUMBER").obj.unbind("blur").bind("blur",function(){
	var thisObjVal = jQuery(this).val() || "";
	if (numberInitVal != thisObjVal) {
		var numberBean = FireFly.doAct(_viewer.servId, "isExist", {"QUERY_ITEM":"VEH_PLATE_NUMBER","QUERY_VALUE":jQuery(this).val()});
		if (parseInt(numberBean["COUNT"]) > 0) {
			_viewer.cardBarTipError("车牌号" + thisObjVal + "已存在");
			numberIsExist = true;
		} else {
			numberIsExist = false;
		}
	}
});

/**
 * 发动机
 */
_viewer.form.getItem("VEH_ENGINE_CODE").obj.unbind("blur").bind("blur",function(){
	var thisObjVal = jQuery(this).val() || "";
	if (engineInitVal != thisObjVal) {
		var numberBean = FireFly.doAct(_viewer.servId, "isExist", {"QUERY_ITEM":"VEH_ENGINE_CODE","QUERY_VALUE":jQuery(this).val()});
		if (parseInt(numberBean["COUNT"]) > 0) {
			_viewer.cardBarTipError("发动机号" + thisObjVal + "已存在");
			engineIsExist = true;
		} else {
			engineIsExist = false;
		}
	}
});

/**
 * 车辆识别码
 */
_viewer.form.getItem("VEH_DISTINGUISH_CODE").obj.unbind("blur").bind("blur",function(){
	var thisObjVal = jQuery(this).val() || "";
	if (distinguishInitVal != thisObjVal) {
		var numberBean = FireFly.doAct(_viewer.servId, "isExist", {"QUERY_ITEM":"VEH_DISTINGUISH_CODE","QUERY_VALUE":jQuery(this).val()});
		if (parseInt(numberBean["COUNT"]) > 0) {
			_viewer.cardBarTipError("车辆识别码" + thisObjVal + "已存在");
			engineIsExist = true;
		} else {
			engineIsExist = false;
		}
	}
});

/**
 * 保存前校验
 */
_viewer.beforeSave = function(){
	if (numberIsExist) {
		_viewer.cardBarTipError("车牌号" + thisObjVal + "已存在");
		return false;
	} else if (engineIsExist) {
		_viewer.cardBarTipError("发动机号" + thisObjVal + "已存在");
		return false;
	} else if (distinguishIsExist) {
		_viewer.cardBarTipError("车辆识别码" + thisObjVal + "已存在");
		return false;
	}
} 
