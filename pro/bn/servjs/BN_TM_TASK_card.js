var _viewer=this;

var data = {};
data["_WHERE_"] = " and TASK_ID ='"+_viewer.getPKCode()+"' and TASK_USER ='" + System.getVar("@USER_CODE@") + "'";
var result = FireFly.doAct("BN_TM_ASSIGN", "finds", data);

if(result._OKCOUNT_!=0){
	var todoFlag = result._DATA_[0]["TODO_FLAG"];
	if(todoFlag == '2' ){
		alert("您的此项任务需要重新办理，具体办理要求请查看任务重办需求说明！");
	}else{
		
	}
}


var taskState = _viewer.getItem("TASK_STATE").getValue();
if(taskState != 0){
	//_viewer.form.disabledAll();
	_viewer.getBtn("save").hide();
	//_viewer.getBtn("assignStart").hide();
	_viewer.getItem("TASK_NAME").disabled();
	_viewer.getItem("TASK_DESCRIPTION").disabled();
	_viewer.getItem("TASK_RECEIVER").disabled();
	_viewer.getItem("TASK_END_DATE").disabled();
	_viewer.getItem("TASK_FUJIAN").disabled();
}
if(taskState == "7"){
	_viewer.readCard();
}
var assignStartBtn=_viewer.getBtn("assignStart");
if(assignStartBtn){
	assignStartBtn.unbind("click").bind("click",function(){
		//param用于保存页面中的值
		var param={};
		param["TASK_ID"] = _viewer.getPKCode();
		//获取任务接收人（任务分配对象）
		//树形字典选择用户后，展开“用户”输入框，这里获取输入框内的用户名称、用户CODE
		var TASK_RECEIVER = _viewer.getItem("TASK_RECEIVER").getValue();
		param.TASK_RECEIVER = TASK_RECEIVER;
		var TASK_NAME = _viewer.getItem("TASK_NAME").getValue();
		param.TASK_NAME = TASK_NAME;
		
		//将param的值传到后台处理
		FireFly.doAct("BN_TM_TASK","taskAssignStart",param);
		//刷新列表
		_viewer.refresh();
//		_viewer.readCard();
	});
}
var assignEndBtn=_viewer.getBtn("assignEnd");
//var taskUser=_viewer.getItem("S_USER").getValue();
if(assignEndBtn){debugger;
	assignEndBtn.unbind("click").bind("click",function(){
		var paramk={};
		var taskId = _viewer.getPKCode();
		paramk["TASK_ID"]=taskId;
		FireFly.doAct("BN_TM_TASK","taskAssignEnd",paramk);
		_viewer.refresh();
	});
}
var timeoutBtn=_viewer.getBtn("timeout");
if(timeoutBtn){
	timeoutBtn.unbind("click").bind("click",function(){
		var params={};
		FireFly.doAct("BN_TM_TASK","hh",params);
		_viewer.refresh();
	});
}
var sureCompleteBtn=_viewer.getBtn("sure_complete");
if(sureCompleteBtn){
	sureCompleteBtn.unbind("click").bind("click",function(){
		var paramB={};
		paramB["TASK_ID"]=_viewer.getPKCode();
		FireFly.doAct("BN_TM_TASK","sureComplete",paramB);
		_viewer.refresh();
	});
}

try{
	var taskRecevier = _viewer.form.getItem("TASK_RECEIVER");
	if(taskRecevier && taskRecevier.type=='DictChoose' && !taskRecevier.isHidden){
		taskRecevier.setText(_viewer.form.getItem("TASK_RECEIVER_NAME").getValue());
	}	
}catch(e){
	console.error("BN_TM_TASK_card.js:" + e.message);
}



//_viewer.beforeSave = function(){
//	if(_viewer.getItem("TASK_RECEIVER").getValue()==''&&_viewer.getItem("COMMON_GROUPS").getValue()==''){
//		alert("请选择任务接收人或者接收组！");
//	}
//}
//jQuery("#BN_TM_TASK-TASK_RECEIVER__NAME").unbind("click");
//var GroupImg = jQuery("<span id = 'selectGroup' class='icon-input-dict' style='width:16px;height:16px;position: absolute;right:45px;top:5px;'></span>");
//jQuery("#BN_TM_TASK-TASK_RECEIVER__NAME").after(GroupImg);
//var selectGroup = jQuery("#selectGroup");

//selectGroup.unbind("click").bind("click",function(){debugger;
//	//常用分组查询选择
//	var extWhereStr =" AND (S_PUBLIC = 1 OR S_USER = ^@USER_CODE@^) AND S_FLAG = 1 and O_DEPT in (select DEPT_CODE from SY_ORG_DEPT where code_path like ^@ODEPT_CODE_PATH@%^)";
//	var configStr =  "BN_TM_TASK_COMMON_GROUPS,{'SOURCE':'GROUPS_NAME~GROUP_ID','TARGET':'TASK_RECEIVER','PKHIDE':true,'TYPE':'multi','EXTWHERE':'"+extWhereStr+"','HIDE':'GROUP_ID'}";
//	var options = {
//			"config" :configStr,
//			"title":"请选择常用分组",
//			"replaceCallBack":function(nameArray,strWhere,idArray){
//					alert(66);
//					jQuery("#TASK_RECEIVER").val(idArray.GROUPS_NAME);
//				}
//			};
//	var queryView = new rh.vi.rhSelectListView(options);
//	queryView.show(event);	
//});

