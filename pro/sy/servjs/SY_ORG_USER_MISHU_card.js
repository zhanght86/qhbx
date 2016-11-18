var _viewer = this;
_viewer.getBtn("save").unbind("click").bind("click",function(){
	_viewer.doAct("save");
	_viewer.setParentRefresh();
	_viewer.backClick();
});
