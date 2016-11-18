var _viewer = this;

_viewer.afterSave = function (){
	var assignParHandler = _viewer.getParHandler().getParHandler();
	var taskParHandler = assignParHandler.getParHandler().getParHandler();
	taskParHandler.refresh();
	assignParHandler.refresh();
	
}
var savebtn=_viewer.getBtn("save");
if(savebtn){
	savebtn.unbind("click").bind("click",function(){
	_viewer.doAct("save");
	_viewer.setParentRefresh();
	_viewer.backClick();
	});
}
