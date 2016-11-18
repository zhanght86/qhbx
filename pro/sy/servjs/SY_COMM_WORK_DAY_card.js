var _viewer = this;

_viewer.getBtn("initWorkDay").unbind("click").bind("click",function(){
	FireFly.doAct("SY_COMM_WORK_DAY","initWorkDay",{"YEAR":_viewer.getItem("YEAR").getValue()});
	
});
