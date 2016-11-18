var _viewer = this;
/**
 * 批量打印
 */
var printDaGdInfoObj = _viewer.wfCard._getBtn('printDaGdInfo');

if (printDaGdInfoObj) {
	// 动态装载意见代码
	Load.scriptJS("/oa/zh/batch-print.js");
	new rh.oa.batchPrint({
		"parHandler": _viewer,
		"actObj":printDaGdInfoObj,
		"printPic":true,
		"printAudit":true
	});
}
// 初始化盖章按钮
var gwSeal = new rh.vi.gwSeal({servId:_viewer.servId,parHandler:_viewer});
gwSeal.init();

if(_viewer.wfCard != null){
	_viewer.wfCard.beforeCmSimpleFenFa = function(actItem,params){
		params["userSelectDict"] = "SY_ORG_DEPT_USER_SUB";
		params["displaySendSchm"] = true;
		params["includeSubOdept"] = true;
		return true;
	}
}

_viewer.wfCard.getAuthBean();



