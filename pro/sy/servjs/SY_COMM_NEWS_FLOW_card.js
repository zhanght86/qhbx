(function(_viewer){
	var servId = _viewer.servId;
	
	if (_viewer.wfCard) {
		var backModifyBtn = _viewer.wfCard._getBtn('backModify');
		var checkPassBtn = _viewer.wfCard._getBtn('pass');
		var checkNoPassBtn = _viewer.wfCard._getBtn('nopass');
		
		// 退回修改
		if (backModifyBtn) {
			backModifyBtn.layoutObj.unbind("click").click(function(){
				alert("退回修改");
			});
		} 
		
		// 审核通过
		if (checkPassBtn) {
			checkPassBtn.layoutObj.unbind("click").click(function(){
				alert("审核通过");
			});
		}
		
		// 审核不通过
		if (checkNoPassBtn) {
			checkNoPassBtn.layoutObj.unbind("click").click(function(){
				alert("审核不通过");
			});
		}
	}
})(this);