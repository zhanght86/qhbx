var _viewer=this;

var s_user=_viewer.getItem("S_USER").getValue();
if(s_user != System.getVar("@USER_CODE@")){
	_viewer.readCard();
	
}