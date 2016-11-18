var _viewer = this;

_viewer.getItem("CT_CHANGE_NAME").show();
_viewer.getItem("CT_CHANGE_DESC").show();
_viewer.getItem("CT_NAME").hide();


var CT_STATE = _viewer.getItem("CT_STATE").getValue();
if("1"==CT_STATE){
	var CT_NAME=_viewer.getItem("CT_NAME").getValue();
	_viewer.getItem("CT_CHANGE_NAME").setValue(CT_NAME);
}



/*
_viewer.getItem("CT_CHANGE_NAME").change(function(){

	var CT_CHANGE_NAME=_viewer.getItem("CT_CHANGE_NAME").getValue();
	if(CT_CHANGE_NAME!=""){
		var CT_ID=_viewer.getItem("CT_CHANGE_ID").getValue();
		
		
		var ZHENGWEN=FireFly.doAct("SY_COMM_FILE","finds",{"SERV_ID":"LW_CT_CONTRACT","DATA_ID":CT_ID,"FILE_CAT":"ZHENGWEN"});
		var FUJIAN=FireFly.doAct("SY_COMM_FILE","finds",{"SERV_ID":"LW_CT_CONTRACT","DATA_ID":CT_ID,"FILE_CAT":"FUJIAN"});
		var XIUGAIGAO=FireFly.doAct("SY_COMM_FILE","finds",{"SERV_ID":"LW_CT_CONTRACT","DATA_ID":CT_ID,"FILE_CAT":"XIUGAIGAO"});
		
		_viewer.getItem("ZHENGWEN").setValue(ZHENGWEN);
		_viewer.getItem("FUJIAN").setValue(FUJIAN);
		_viewer.getItem("XIUGAIGAO").setValue(XIUGAIGAO);
		
		
	}
	
});
*/








_viewer.beforeSave=function(){
	
	var CT_CHANGE_NAME=_viewer.getItem("CT_CHANGE_NAME").getValue();
	if(CT_CHANGE_NAME!=""){
		_viewer.getItem("CT_NAME").setValue(CT_CHANGE_NAME);
	}else{
		_viewer.cardBarTipError("变更前合同不能为空！");
		return false;
	}
	
}


var confirmChange = _viewer.getBtn("confirmChange")


//确认变更按钮只有流程中配置了变量才可看到

if (_viewer.wfCard && _viewer.wfCard.isWorkflow()){
	
	if(_viewer.wfCard.getCustomVarContent("canSave") && _viewer.getItem("CT_STATE").getValue()==0){
		confirmChange.show();
	}
}




confirmChange.unbind("click").bind("click",function(){
	
	var params = {};
	params["_PK_"] = _viewer.getPKCode();
	//获得合同审批单的id
	params["CT_CHANGE_ID"]=_viewer.getItem("CT_CHANGE_ID").getValue();
	var result = FireFly.doAct(_viewer.servId,"confirmChange",params);
	_viewer.refresh();
	Tip.show("变更成功");
});



