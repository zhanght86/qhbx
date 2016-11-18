var _viewer = this;
_viewer.getBtn("destroy").unbind("click").bind("click",function(){
	_viewer.getItem("S_USER").setValue(System.getVar("@USER_CODE@"));
	_viewer.getItem("DEST_STATE").setValue("1");
	_viewer.getItem("S_DEPT").setValue(System.getVar("@DEPT_CODE@"));
	_viewer.getItem("S_MTIME").setValue(rhDate.getCurentTime());
	doAct("save");
	alert("销毁成功！！！");
	Tab.close();
});