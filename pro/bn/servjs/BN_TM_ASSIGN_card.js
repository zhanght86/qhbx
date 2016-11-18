var _viewer = this;

//var strcookie = document.cookie;  
//var arrcookie = strcookie.spit("=")  
//var statuscookie = arrcookie[1];  
//if(statuscookie == "" || statuscookie == "0"){  
//   //retset flag  
//    document.cookie="statuscookie=1";
//}else{  
//    window.location.reload();  
//    document.cookie="statuscookie=0";
//}

//可获取当前登录用户编码
var userCode = System.getVar("@USER_CODE@");
var savebtn=_viewer.getBtn("save");
//var beginBtn=_viewer.getBtn("begin");
var completeBtn=_viewer.getBtn("complete");
//获取反馈人
var taskUser = _viewer.getItem("TASK_USER").getValue();
var taskState=_viewer.getItem("TASK_STATE").getValue();
if(userCode != taskUser || taskState == '5'){
	savebtn.hide();
	_viewer.readCard();
};
if (userCode == taskUser &&( taskState == '6' || taskState == '7')) {
		_viewer.readCard();
};
_viewer.setParentRefresh();
_viewer._parentRefreshFlag = true;
if(completeBtn){
	completeBtn.unbind("click").bind("click",function(){
		var param={};
		param["ASSIGN_ID"]=_viewer.getPKCode();
		param["TASK_ID"] = _viewer.getItem("TASK_ID").getValue();
		FireFly.doAct(_viewer.servId,"completeFeedBack",param);
		_viewer.refresh();
		_viewer.readCard();
	});
};
_viewer.setParentRefresh();
_viewer._parentRefreshFlag = true;