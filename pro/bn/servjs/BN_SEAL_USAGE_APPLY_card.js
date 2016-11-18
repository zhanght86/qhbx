var _viewer = this;
//Load.js("/bn/seal/js/sealFileOper.js");
//取得工作流变量
//var canDownLoadFile = _viewer.wfCard.getCustomVarContent("canDownLoadFile") || "false";
//var canSeal = _viewer.wfCard.getCustomVarContent("canSeal") || "false";
//var canAddSeal = _viewer.wfCard.getCustomVarContent("canAddSeal") || "false";
//var canEditable = _viewer.wfCard.getCustomVarContent("canEditable") || "false";
//var canPrint = _viewer.wfCard.getCustomVarContent("canPrint") || "false";

_viewer.getBtn("seal").unbind("click").bind("click",function(){
	FireFly.doAct(_viewer.servId,"updateSeal",{"_PK_":_viewer.getPKCode()});
});
var gwSeal = new rh.vi.gwSeal({servId:_viewer.servId,parHandler:_viewer});
gwSeal.init();
var gwExtCard = new rh.vi.gwExtCardView({
	"parHandler" : _viewer
});
gwExtCard.init();

//if(_viewer.wfCard._getBtn("ADD_SEAL_FILE")) {
//	_viewer.wfCard._getBtn("ADD_SEAL_FILE").layoutObj.unbind("click").bind("click",function(event){
//		Tab.open({
//			"url" : "BN_SEAL_FILE_LIST.card.do",
//			"tTitle" : "用印文件",
//			"menuFlag" : 4,
//			"params" : {
//				"APPLY_ID" : _viewer.getPKCode(),
//				"APPLY_TYPE" : _viewer.getItem("APPLY_TYPE").getValue(),
//				"callBackHandler" : _viewer,
//				"closeCallBackFunc" : function() {
//					_viewer.refresh();
//				}
//			}
//		});
//	});
//}
// 确认已盖章
if(_viewer.wfCard._getBtn("CONFIRM_SEAL")){
	_viewer.wfCard._getBtn("CONFIRM_SEAL").layoutObj.unbind("click").bind("click",function(event){
		FireFly.doAct(_viewer.servId,"confirmSeal",{"APPLY_ID":_viewer.getPKCode()},false,false,function(result){
			if(result.IS_OK="true"){
				_viewer.cardBarTip("操作成功");
				_viewer.refresh();
			}else{
				_viewer.cardBarTipError("操作失败");
			}
		});
	});
}
_viewer.beforeSave = function() {
	if(_viewer.getPKCode()) {
		var fileList = FireFly.doAct("BN_SEAL_FILE_LIST","finds",{"APPLY_ID":_viewer.getPKCode(),"FILE_TYPE":"0"});
		if(!fileList || !fileList["_DATA_"] || fileList["_DATA_"].length <= 0) {
			//alert("请上传用印文件...");
			//return false;
		} else {
			var fileDataList = fileList["_DATA_"];
			for(var i = 0; i < fileDataList.length; i++) {
				var fileData = fileDataList[i];
				var _sealData = FireFly.doAct("BN_SEAL_USE_LIST","finds",{"FILE_ID":fileData.FILE_ID});
				if(!_sealData || !_sealData["_DATA_"] || _sealData["_DATA_"].length <= 0) {
					alert("请为‘"+ fileData.FILE_OBJ.split(",")[1] +"’盖章文件添加所使用的印章...");
					return false;
				}
			}
		}
	}
}
//if(_viewer.getPKCode()) {
//	var params = {
//		"applyId":_viewer.getPKCode(),
//		"parentNode":"SEAL_FILE_LIST",
//		"parHandler":_viewer,
//		"applyType":_viewer.getItem("APPLY_TYPE").getValue(),
//		"canDownLoadFile":canDownLoadFile,
//		"canSeal":canSeal,
//		"canAddSeal":canAddSeal,
//		"canEditable":canEditable,
//		"canPrint":canPrint
//	};
//	new rh.seal.sealFileOper(params).render();
//} else {
	$("#"+ _viewer.servId + "-SEAL_FILE_LIST_div").hide();
//}
