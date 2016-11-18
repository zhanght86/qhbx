var _viewer = this;
_viewer.getBtn("save").hide();

// 批量添加按钮绑定click事件
_viewer.getBtn("BATCHADD").unbind("click").bind("click",function(){
	if(_viewer.getItem("ODEPT_CODES").getValue()=="") {
		alert("请选择所属机构");
	   	return false;
	}
	if(_viewer.getItem("TDEPT_NAMES").getValue()=="") {
		alert("请输入部门名称");
	   	return false;
	}
	FireFly.doAct(_viewer.servId,"batchGen",{"ODEPT_CODES":_viewer.getItem("ODEPT_CODES").getValue(),"TDEPT_NAMES":_viewer.getItem("TDEPT_NAMES").getValue(),"DEPT_NAMES":_viewer.getItem("DEPT_NAMES").getValue()},false,false,function(result){
		if(result.SUCCESS&&result.SUCCESS=='true'){
		}
	});
});

