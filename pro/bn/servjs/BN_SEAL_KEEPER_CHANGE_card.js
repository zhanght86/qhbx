var _viewer = this;
var sealId = _viewer.getItem("SEAL_ID").getValue();
if(_viewer.byIdData["_ADD_"]!="true"){
	_viewer.readCard();
	/*_viewer.getItem("KEEP_TDEPT_CODE").enabled();*/
	//因为页面有两个保存按钮 所以不显示
//	_viewer.getBtn("save").show();
}
//印章ID字段，初始化的时候要给字典赋值
if(params != undefined && params.toString().length>0){
	_viewer.getItem("SEAL_ID").setValue(params.sealList);
	/*_viewer.getItem("SEAL_ID").setName(params.sealListName);*/
	jQuery("#BN_SEAL_KEEPER_CHANGE-SEAL_ID__NAME").val(params.sealListName);
}
if(sealId!=""){
	var sealIds = sealId.toString().split(",");
	var sealId__name = [];
	for ( var i = 0; i < sealIds.length; i++) {
		var sealDict = FireFly.doAct("BN_SEAL_BASIC_INFO","byid",{"_PK_":sealIds[i]});
		sealId__name.push(sealDict.SEAL_NAME);
	}
	/*sealId__name =sealId__name.substring(0,sealId__name.lastIndexOf(","));*/
	jQuery("#BN_SEAL_KEEPER_CHANGE-SEAL_ID__NAME").val(sealId__name);
}
//只有起草保存之前需要自动带入相关印章 所以判断流程
//因为办结时WF_NODE == "" 所以加判断条件 非办结时
var params = _viewer.getParams();
	// 根据印章列表页面选择的印章 自动带入到相关印章中
	if(params != undefined && params.sealList != undefined){
		_viewer.getItem("SEAL_ID").setValue(params.sealList);
		/*_viewer.getItem("SEAL_ID").setName(params.sealListName);*/
		jQuery("#BN_SEAL_KEEPER_CHANGE-SEAL_ID__NAME").val(params.sealListName);
	}
//更改保管人按钮
var change = _viewer.getBtn("changeKeeper");
if (_viewer.wfCard && _viewer.wfCard.isWorkflow()){
	var sealVar = _viewer.wfCard.getCustomVarContent("canChangeKeeper");
	if (!(sealVar && sealVar == "true")){
		change.hide();
	}
}
change.unbind("click").bind("click",function(){
	var params = {};
	params["SEAL_ID"] = _viewer.getItem("SEAL_ID").getValue();
	params["NOW_USER_CODE"] = _viewer.getItem("NOW_USER_CODE").getValue();
	params["_PK_"] = _viewer.getPKCode();
	var result = FireFly.doAct(_viewer.servId,"hange",params);
	Tab.close();
	params.handler.refresh();
});
/*
 * 当CHANGE_TYPE修改类型改变时，显示对应的类型
 * 3：行政经理
 * 4：公司负责人
 * 5：印章保管人
 */
_viewer.getItem("CHANGE_TYPE").change(function(){
	var changeType = _viewer.getItem("CHANGE_TYPE").getValue();
	if(changeType == "3"){
		_viewer.getItem("XZ_MANAGER").show();
		_viewer.getItem("CHARGE").hide();
		_viewer.getItem("NOW_USER_CODE").hide();
	}else if(changeType == "4"){
		_viewer.getItem("XZ_MANAGER").hide();
		_viewer.getItem("CHARGE").show();
		_viewer.getItem("NOW_USER_CODE").hide();
	}else if(changeType == "5"){
		_viewer.getItem("XZ_MANAGER").hide();
		_viewer.getItem("CHARGE").hide();
		_viewer.getItem("NOW_USER_CODE").show();
	}
});
_viewer.getItem("NOW_USER_CODE").change(function(){
	var outBean = FireFly.doAct("SY_ORG_USER","byid",{"_PK_":_viewer.getItem("NOW_USER_CODE").getValue()});
	_viewer.getItem("KEEP_TDEPT_CODE").setValue(outBean.DEPT_CODE);
	jQuery("#BN_SEAL_KEEPER_CHANGE-KEEP_TDEPT_CODE__NAME").val(outBean.DEPT_NAME);
});
//开始时间
_viewer.getItem("KEEP_BEGIN_TIME").obj.unbind("click").bind("click", function(){
	WdatePicker({minDate:"%y-%M-%d"});
});
//结束时间
_viewer.getItem("KEEP_END_TIME").obj.unbind("click").bind("click", function(){
	WdatePicker({minDate:"#F{$dp.$D('" + _viewer.servId + "-KEEP_BEGIN_TIME')}"});
});

