//@ sourceURL=OA_GW_TYPE_SW_card_mb.js

// 收文管理 总服务
var _viewer = this;

//重载传阅参数
if (_viewer.wfCard != null) {
	_viewer.wfCard.beforeCmSimpleFenFa = function(actItem,params){
		params["userSelectDict"] = "SY_ORG_DEPT_USER";
		params["displaySendSchm"] = true;
		params["includeSubOdept"] = false;
		return true;
	};
}