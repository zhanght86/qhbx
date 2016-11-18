var _viewer = this;
var params = _viewer.getParams();
if(params != undefined && params.toString().length>0){
	_viewer.getItem("SC_SEAL").setValue(params.sealList);
	/*_viewer.getItem("SEAL_ID").setName(params.sealListName);*/
	jQuery("#BN_SEAL_SCRAP-SC_SEAL__NAME").val(params.sealListName);
}
//初始化印章显示名称
/*if(_viewer.getItem("SC_SEAL").getValue()!=""){
	jQuery("#BN_SEAL_SCRAP-SC_SEAL__NAME").val(DictMgr.dict());
}*/
//稍后修改：只有总部的印章管理员可以看到同意按钮
if(this.wfCard){
	var agree = this.wfCard.getCustomVarContent("Agreen");
	if(_viewer._byIdData()._ADD_=="true"){
		//添加模式不显示
		_viewer.getBtn("agree").hide();
	}else if(!(agree=="true")){
		//没有配置流程变量不显示
		_viewer.getBtn("agree").hide();
	}else if(_viewer.getItem("SC_STATUS").getValue()==1){
		//同意之后不显示
		_viewer.getBtn("agree").hide();
	}
}
//
if(this.wfCard){
	var scrap = this.wfCard.getCustomVarContent("scrap");
	if(_viewer._byIdData()._ADD_=="true"){
		//添加模式不显示
		_viewer.getBtn("scrap").hide();
	}else if(!(scrap=="true")){
		//没有配置流程变量不显示
		_viewer.getBtn("scrap").hide();
	}else if(_viewer.getItem("SC_STATUS").getValue()!=1){
		//总部未同意不显示
		_viewer.getBtn("scrap").hide();
	}
}
_viewer.getBtn("scrap").unbind("click").bind("click",function(){
	_viewer.getItem("S_FLAG").setValue(2);
	_viewer.doAct("save");
	FireFly.doAct(
			_viewer.servId,
			"sealScrap",
				{"_PK_":_viewer.getPKCode(),
				"BN_SC_REASON":_viewer.getItem("BN_SC_REASON").getValue(),
				"SEAL_ID":_viewer.getItem("SC_SEAL").getValue().toString(),
				"REASON":_viewer.getItem("SC_REASON_SCRIPT").getValue()}
			);
	Tab.close();
	params.handler.refresh();
});
//总部印章管理员同意之后更新状态，SC_STATUS为同意的才可以入库
_viewer.getBtn("agree").unbind("click").bind("click",function(){
	_viewer.getItem("SC_STATUS").setValue(1);
	_viewer.doAct("save");
	//_viewer.refresh();
	Tab.close();
});