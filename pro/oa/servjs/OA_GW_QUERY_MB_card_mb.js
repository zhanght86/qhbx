var _viewer = this;

var hideItemsObj = {"S_TNAME":true,"GW_ZB_TNAME":true,"GW_SW_CNAME":true,"GW_SW_TYPE":true,
					"GW_MAIN_TO":true,"GW_COPY_TO":true,"GW_COSIGN_TO":true,"GW_BEGIN_TIME_1":true,
					"GW_BEGIN_TIME_2":true,"GW_SIGN_TIME_1":true,"GW_SIGN_TIME_2":true,
					"GW_CW_TIME_1":true,"GW_CW_TIME_2":true}; //声明隐藏字段数组
					
var isXiangXiQuery = false; //是否是详细查询，默认为false

initItems(); //初始化显示字段

//文件类型变化的时候，修改其显示的字段  TMPL_TYPE_CODE
_viewer.getItem("TMPL_TYPE_CODE").afterSetValue = function(){
    var typeVal = _viewer.getItem("TMPL_TYPE_CODE").getValue();
	if (typeVal == "OA_GW_TYPE_FW") {
		_viewer.getItem("TMPL_CODE").reSetDict("OA_GW_TMPL_FW_CODE");
		if (isXiangXiQuery) {
			setHideItemObj("S_TNAME,GW_MAIN_TO,GW_COPY_TO,GW_COSIGN_TO,GW_BEGIN_TIME_1,GW_BEGIN_TIME_2,GW_SIGN_TIME_1," 
						   + "GW_SIGN_TIME_2,GW_CW_TIME_1,GW_CW_TIME_2");
		}
		_viewer.getItem("TMPL_CODE").setValue("","");
	} else if (typeVal == "OA_GW_TYPE_SW") {
		_viewer.getItem("TMPL_CODE").reSetDict("OA_GW_TMPL_SW_CODE");
		if (isXiangXiQuery) {
			setHideItemObj("GW_ZB_TNAME,GW_SW_CNAME,GW_SW_TYPE,GW_MAIN_TO,GW_COPY_TO,GW_COSIGN_TO,GW_BEGIN_TIME_1,GW_BEGIN_TIME_2,GW_SIGN_TIME_1," 
						   + "GW_SIGN_TIME_2");
		}
		_viewer.getItem("TMPL_CODE").setValue("","");
	} else if (typeVal == "OA_GW_TYPE_QS") {
		_viewer.getItem("TMPL_CODE").reSetDict("OA_GW_TMPL_QS_CODE");
		if (isXiangXiQuery) {
			setHideItemObj("GW_BEGIN_TIME_1,GW_BEGIN_TIME_2");
		}
		_viewer.getItem("TMPL_CODE").setValue("","");
	}
	clearOtherItems();
};

_viewer.getBtn('fastMode').hide(); //初始化隐藏快速查询按钮

//查询按钮
_viewer.getBtn('gwQuery').bind("click",function() {
   queryGw(_viewer.form.getAllItemsValue());
});	

//详细查询按钮,展开相应的隐藏项
_viewer.getBtn('detailMode').unbind("click").bind("click",function(){
	var typeVal = _viewer.getItem("TMPL_TYPE_CODE").getValue();
	isXiangXiQuery = true;
	if (typeVal == "OA_GW_TYPE_FW") {
		_viewer.getItem("TMPL_CODE").reSetDict("OA_GW_TMPL_FW_CODE");
		setHideItemObj("S_TNAME,GW_MAIN_TO,GW_COPY_TO,GW_COSIGN_TO,GW_BEGIN_TIME_1,GW_BEGIN_TIME_2,GW_SIGN_TIME_1," 
						   + "GW_SIGN_TIME_2,GW_CW_TIME_1,GW_CW_TIME_2");
	} else if (typeVal == "OA_GW_TYPE_SW") {
		_viewer.getItem("TMPL_CODE").reSetDict("OA_GW_TMPL_SW_CODE");
		setHideItemObj("GW_ZB_TNAME,GW_SW_CNAME,GW_SW_TYPE,GW_MAIN_TO,GW_COPY_TO,GW_COSIGN_TO,GW_BEGIN_TIME_1,GW_BEGIN_TIME_2,GW_SIGN_TIME_1," 
						   + "GW_SIGN_TIME_2");
	} else if (typeVal == "OA_GW_TYPE_QS"){
		_viewer.getItem("TMPL_CODE").reSetDict("OA_GW_TMPL_QS_CODE");
		setHideItemObj("GW_BEGIN_TIME_1,GW_BEGIN_TIME_2");
	}
	jQuery(this).hide();
	_viewer.getBtn('fastMode').show();
});

//快速查询按钮，隐藏相应的数据项
_viewer.getBtn('fastMode').unbind("click").bind("click",function(){
	var typeVal = _viewer.getItem("TMPL_TYPE_CODE").getValue();
	isXiangXiQuery = false;
	if (typeVal == "OA_GW_TYPE_FW") {
		_viewer.getItem("TMPL_CODE").reSetDict("OA_GW_TMPL_FW_CODE");
		setShowItemObj("S_TNAME,GW_MAIN_TO,GW_COPY_TO,GW_COSIGN_TO,GW_BEGIN_TIME_1,GW_BEGIN_TIME_2,GW_SIGN_TIME_1," 
						   + "GW_SIGN_TIME_2,GW_CW_TIME_1,GW_CW_TIME_2");
	} else if (typeVal == "OA_GW_TYPE_SW") {
		_viewer.getItem("TMPL_CODE").reSetDict("OA_GW_TMPL_SW_CODE");
		setShowItemObj("GW_ZB_TNAME,GW_SW_CNAME,GW_SW_TYPE,GW_MAIN_TO,GW_COPY_TO,GW_COSIGN_TO,GW_BEGIN_TIME_1,GW_BEGIN_TIME_2,GW_SIGN_TIME_1," 
						   + "GW_SIGN_TIME_2");
	} else if (typeVal == "OA_GW_TYPE_QS"){
		_viewer.getItem("TMPL_CODE").reSetDict("OA_GW_TMPL_QS_CODE");
		setShowItemObj("GW_BEGIN_TIME_1,GW_BEGIN_TIME_2");
	}
	jQuery(this).hide();
	_viewer.getBtn('detailMode').show();
});

//清楚当前表单中隐藏的其他数据
function clearOtherItems() {
	var items = _viewer.form.getAllItems();
	for (var i in items) {// 遍历所有表单域
		var item = items[i];
		if (!item.obj.is(":visible")) {
			if (item.clear) {
				item.clear();
			}
		}
	}
}

//卡片初始化隐藏字段
function initItems(){
	for (var j in hideItemsObj) {
		if (hideItemsObj[j]) {
			_viewer.getItem(j).getContainer().hide();
		} else {
			_viewer.getItem(j).getContainer().show();
		}
	}
}

//重置隐藏字段
function setHideItemObj(hideItems){
	for (var j in hideItemsObj) {
		if (hideItems.indexOf(j) >= 0) { //存在
			hideItemsObj[j] = false;
		} else {
			hideItemsObj[j] = true;
		}
	}
	initItems();
}

//重置展开字段
function setShowItemObj(hideItems){
	for (var j in hideItemsObj) {
		if (hideItems.indexOf(j) >= 0) { //存在
			hideItemsObj[j] = true;
		}
	}
	initItems();
}

/**
 * 查询公文
 */
function queryGw(itemsValues){
	itemsValues.GW_CODE = _viewer.getItem("GW_CODE").getValue();
	if (_viewer.getItem("GW_BEGIN_TIME_1").getValue() != undefined) {
	    itemsValues.GW_BEGIN_TIME_1 = _viewer.getItem("GW_BEGIN_TIME_1").getValue();
	}
	if (_viewer.getItem("GW_BEGIN_TIME_2").getValue() != undefined) {
	    itemsValues.GW_BEGIN_TIME_2 = _viewer.getItem("GW_BEGIN_TIME_2").getValue();
	}	
	if (_viewer.getItem("GW_SIGN_TIME_1").getValue() != undefined) {
	    itemsValues.GW_SIGN_TIME_1 = _viewer.getItem("GW_SIGN_TIME_1").getValue();
	}
	if (_viewer.getItem("GW_SIGN_TIME_2").getValue() != undefined) {
	    itemsValues.GW_SIGN_TIME_2 = _viewer.getItem("GW_SIGN_TIME_2").getValue();
	}
	if (_viewer.getItem("GW_CW_TIME_1").getValue() != undefined) {
	    itemsValues.GW_CW_TIME_1 = _viewer.getItem("GW_CW_TIME_1").getValue();
	}
	if (_viewer.getItem("GW_CW_TIME_2").getValue() != undefined) {
	    itemsValues.GW_CW_TIME_2 = _viewer.getItem("GW_CW_TIME_2").getValue();
	}
	var opts = {"url":"OA_GW_QUERY_MB.list.do","tTitle":"查询结果","params":itemsValues};
	Tab.open(opts);
}
