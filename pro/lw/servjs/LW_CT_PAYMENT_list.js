(function(_viewer){
	var payBtn = _viewer.grid.getBtn("pay");
	if (payBtn) {
		payBtn.unbind("click").click(function(){
			var pk = jQuery(this).attr("rowpk");
			alert("支付，需要支付接口支持！" + pk);
		});
	}
	//获取父服务句柄，且父服务流程已启动，且起草节点
	var parObj = _viewer.opts;
	if(parObj && parObj.parHandler && parObj.parHandler.wfCard && parObj.parHandler.wfCard.isWorkflow()){
		//非起草节点，则隐藏添加、删除按钮
		if(!parObj.parHandler.wfCard.isDraftNode()) {
			jQuery("#"+_viewer.servId).find(".rhGrid-btnBar").hide();
		}
	}
})(this);