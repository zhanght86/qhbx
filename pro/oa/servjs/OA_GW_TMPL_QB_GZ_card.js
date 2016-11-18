var _viewer = this;
//启动工作流时的节点，绑定beforesave事件
if(_viewer.wfCard && _viewer.wfCard.isWorkflow()){
	if( _viewer.wfCard.isFirstNode()){
		
		_viewer.beforeSave = function(){
			//判断是否修改了签报类型数据
			var changeData = _viewer.getChangeData();
			//修改签报类型，则需要重启新流程，则将表单打开的replaceUrl置空，以便加载最新的流程信息
			if(changeData["GW_FILE_TYPE"]){
				_viewer._replaceUrl = "";
			}
			return true;
		}
		_viewer.wfCard._beforeSaveAndSend = function(){
			//判断是否修改了签报类型数据
			var changeData = _viewer.getChangeData();
			//修改签报类型，则提示先保存，再送交
			if(changeData["GW_FILE_TYPE"]){
				alert("您已修改签报类型，请先保存");
				return false;
			}
			return true;
		}
	}else{//签报类型数据项置为只读
		if (_viewer.getItem("GW_FILE_TYPE")) {
			_viewer.getItem("GW_FILE_TYPE").disabled();
		}
	}
	//20160426
	//在“提交同意”节点且存在办结按钮，则对非起草人能够隐藏办结按钮
	if (_viewer.wfCard._getBtn("finish") && _viewer.wfCard.getNodeInstBean().NODE_CODE == '29') {
		if ((System.getVar("@USER_CODE@") != _viewer.getItem("S_USER").getValue())) {
			_viewer.wfCard._getBtn("finish").layoutObj.hide();
		}
	}
}

