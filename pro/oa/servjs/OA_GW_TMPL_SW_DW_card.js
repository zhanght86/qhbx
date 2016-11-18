var _viewer = this;

if (_viewer.wfCard != null) {
	_viewer.wfCard.beforeFenfa = function(sendObj){
		sendObj["includeSubOdept"] = false;
		return true;
	};
	_viewer.wfCard.beforeCmSimpleFenFa = function(actItem,params){
		params["userSelectDict"] = "SY_ORG_DEPT_USER_SUB";
		params["displaySendSchm"] = true;
		params["includeSubOdept"] = false;
		return true;
	}
}
