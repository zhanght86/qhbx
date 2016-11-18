var _viewer = this;
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