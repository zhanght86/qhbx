var _viewer = this;
//获取父服务句柄，且父服务流程已启动，且起草节点
var parObj = _viewer.opts;
if(parObj && parObj.parHandler && parObj.parHandler.wfCard && parObj.parHandler.wfCard.isWorkflow()){
	//隐藏添加、删除按钮
	jQuery("#"+_viewer.servId).find(".rhGrid-btnBar").hide();
}
