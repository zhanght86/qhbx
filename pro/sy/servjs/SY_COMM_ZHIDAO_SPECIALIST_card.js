var _viewer = this;

var spec = _viewer.getBtn("spec");

spec.unbind("click").bind("click",function(event) {
	if(_viewer.getPKCode()){
		var userCode = _viewer.getItem ("USER_ID").getValue();
		var userSpec = FireFly.doAct("SY_COMM_ZHIDAO_SPEC_SUBJECT","finds",{"_WHERE_":" and SPEC_ID = '"+userCode+"'"})._DATA_;
		var replacePosArray = new Array();
		var specPK = new Array();
		for(var j=0;j<userSpec.length;j++){
			replacePosArray.push(userSpec[j].CHNL_ID);
			specPK.push(userSpec[j].ID);
		}
		var configStr = "SY_COMM_ZHIDAO_CHNL_MANAGE,{'TYPE':'multi'}";
		var options = {
			"config" :configStr,
			"replaceCallBack":function(idArray,nameArray){
				if(specPK.length){
					FireFly.listDelete("SY_COMM_ZHIDAO_SPEC_SUBJECT",{"_PK_":specPK.join(",")});
				}
				for(var i=0;i<idArray.length;i++){
					FireFly.cardAdd("SY_COMM_ZHIDAO_SPEC_SUBJECT",{"CHNL_ID":idArray[i],"SPEC_ID":userCode});
				}
				_viewer.refresh();
			}
		};
		var dictView = new rh.vi.rhDictTreeView(options);
		dictView.show(event);
		dictView.tree.selectNodes(replacePosArray);
		dictView.tree.expandParent();
	}else{
		alert("请先保存要添加的专家！");
	}
});
var userCode = _viewer.getItem ("USER_ID").getValue();
var uSpec = FireFly.doAct("SY_COMM_ZHIDAO_SPEC_SUBJECT","finds",{"_WHERE_":" and SPEC_ID = '"+userCode+"'"})._DATA_;
var zjly = "";
if(uSpec.length>0){
	for(var i=0;i<uSpec.length-1;i++){
		zjly = zjly + uSpec[i].CHNL_NAME+",";
	}
	zjly = zjly + uSpec[uSpec.length-1].CHNL_NAME;
}
_viewer.getItem("ZJLY").setValue(zjly);