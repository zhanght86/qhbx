var _viewer = this;

_viewer.getBtn("updateHRUser").unbind("click").bind("click",function(){
	FireFly.doAct("SY_ORG_USER","upUserForTime",{"BEGIN_TIME":_viewer.getItem("BEGIN_TIME").getValue()});
	
});
