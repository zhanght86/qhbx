var _viewer = this;

//设置完全公开选中“否”
_viewer.getItem("FULLY_OPEN").setValue("2");

/**
 * 当点击保存按钮后，进行输入框的效验。取值、将值给后台处理
 */
//为保存按钮绑定点击事件
var saveShareObject = _viewer.getBtn("saveShareObject");
saveShareObject.bind("click",function(){
	//获取当前服务ID
	var servId = _viewer.servId;
	//param用于保存页面中的值。
	var param = {};
	param["SHARE_ID"] = _viewer.getPKCode();
	var checkedBox = _viewer.getItem("FULLY_OPEN").getValue();
	
	//勾选用户后，展开“用户”输入框，这里获取输入框内的用户名称、用户CODE
	var userInfo = _viewer.getItem("SELECT_USER").getText();
	var userInfoId = _viewer.getItem("SELECT_USER").getValue();
	//勾选部门后，展开“部门”输入框，这里获取输入框的部门名称、部门code
	var deptInfo = _viewer.getItem("SELECT_DEPT").getText()
	var deptInfoId = _viewer.getItem("SELECT_DEPT").getValue();
	if(userInfo==""&&deptInfo==""){
		if(checkedBox=="1"){
		}
		else{
			alert("请选择共享对象后点击\"保存\"");
			return false;
		}
	}
	else{
	}
	if(checkedBox !=""&&checkedBox !=undefined){
		param["FULLY_OPEN"] = checkedBox;
	}
	//将获取的用户信息保存到param中
	if(userInfo!=undefined&&userInfo!=""){
		param["shareTypeUser"] = userInfo;
		param["shareTypeUserId"] = userInfoId;
	}

	//将获取的部门信息保存到param中
	if(deptInfo!=undefined&&deptInfo!=""){
		param["shareTypeDept"] = deptInfo;
		param["shareTypeDeptId"] = deptInfoId;
	}
	//将param的值传到后台处理。
	FireFly.doAct(servId,"shareObjectSet",param,true,false);
	//刷新列表页
	_viewer.refresh();

})
//判断如果用户点击了“完全公开”则清除“用户、角色、部门”下面的值，如果选择了“用户、角色、部门”则清除“完全公开”...mutual Exclusion（互斥）
_viewer.form.getItem("FULLY_OPEN").obj.bind("click",function(){
	if(_viewer.getItem("FULLY_OPEN").getValue() == "1"){
		_viewer.getItem("SELECT_USER").setValue("");
		_viewer.getItem("SELECT_USER").setText("");	
		_viewer.getItem("SELECT_DEPT").setValue("");
		_viewer.getItem("SELECT_DEPT").setText("");
	}
	
});
_viewer.form.getItem("SELECT_USER").obj.bind("click",function(){
	_viewer.getItem("FULLY_OPEN").setValue("2");
});

_viewer.form.getItem("SELECT_DEPT").obj.bind("click",function(){
	_viewer.getItem("FULLY_OPEN").setValue("2");
});
jQuery(".iconChoose").bind("click",function(){
	_viewer.getItem("FULLY_OPEN").setValue("2");
});
