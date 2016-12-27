//@ sourceURL=OA_GW_TMPL_QS_JGBM_NEW_card_mb.js

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