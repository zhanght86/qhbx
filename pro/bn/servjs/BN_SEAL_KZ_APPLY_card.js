_viewer = this;
var saveSeal = _viewer.getBtn("saveSeal");

var deleBut = _viewer.wfCard.getCustomVarContent("deleBut11");
var addBut = _viewer.wfCard.getCustomVarContent("addBut11");

/*
var deleBut = "";
if (_viewer.wfCard && _viewer.wfCard.isWorkflow()) {
	deleBut = _viewer.wfCard.getCustomVarContent("test");
	if(deleBut){
		 _viewer.getParHandler().getBtn("delete").hide();
		//alert(btnName+"1234567");
		alert(deleBut);	
	}
}
*/
/*if(_viewer.byIdData["_ADD_"]=="true"){
	saveSeal.hide();
}*/
if (_viewer.wfCard && _viewer.wfCard.isWorkflow()){
	var sealVar = _viewer.wfCard.getCustomVarContent("canSealed");
	if (!(sealVar && sealVar == "true")){
		saveSeal.hide();
	}
}
saveSeal.unbind("click").bind("click",function(result){
	var param = {};
	param["_PK_"]=_viewer.getPKCode();
	param["TITLE"] = _viewer.getItem("APPLY_TITLE").getValue();
	FireFly.doAct(_viewer.servId,"saveSeal",param);
	Tab.close();
});
var gwExtCard = new rh.vi.gwExtCardView({
	"parHandler" : _viewer
});
gwExtCard.init();
//构建审批单头部
/*var gwHeader = new rh.vi.gwHeader({
	"servId" : _viewer.opts.sId,
	"parHandler" : _viewer
});
alert(2);
gwHeader.init();*/
