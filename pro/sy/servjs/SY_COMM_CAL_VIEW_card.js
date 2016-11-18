var _viewer = this;

jQuery(document).ready(function(){
	// 给删除绑定事件
	_viewer.getBtn("delete").unbind().bind("click",function(){
		var res = confirm("您确定要删除该数据么？");
			 if (res == true) {
	    		setTimeout(function() {
		    		var strs = _viewer.getItem("CAL_ID").getValue();
		    		var temp = {};
		    		temp[UIConst.PK_KEY]=strs;
		    		var resultData = FireFly.listDelete("SY_COMM_CAL",temp);
		    		Tab.close();
	    		},0);	
			 }
	});
	
	// 添加模式下，点击卡片或者列表，进入到卡片页面后，根据mini日历传入的日期参数，赋值开始时间
	if (!(_viewer.getPKCode())) { 
		var paramData = _viewer.getParams()["DATA"];
		if (paramData == undefined) {
			paramData = _viewer.getParHandler().getParams()["DATA"];
		}
		if (paramData != undefined && paramData != '') {
			_viewer.getItem("CAL_START_TIME").setValue(paramData + " 00:00");
		}
		_viewer.getBtn("delete").hide(); // 去掉删除按钮
	} else { // 修改模式下判断是否是本用户填写的日程，如不是去掉保存按钮
		var currentUser = System.getVar("@USER_CODE@");
		var createUser = _viewer.getItem("S_USER").getValue();
		if (currentUser != createUser) {
			_viewer.getBtn("save").hide(); // 不是一个人，去掉保存按钮
			_viewer.getBtn("delete").hide(); // 去掉删除按钮
		}
	}
	
	// zjx - 根据公司判断是否显示'短信息'提醒
	if (System.getVar('@ODEPT_CODE@') != '24') {
		var calRemindContainer = _viewer.getItem('CAL_REMIND').getContainer();
		$(calRemindContainer).find('input[value=MESSAGE]').attr('disabled',true);
	}
});

_viewer.beforeSave = function(){
	if(Date.parse(_viewer.itemValue("CAL_START_TIME")) >= Date.parse(_viewer.itemValue("CAL_END_TIME"))){
		_viewer.cardClearTipLoad();
		alert("开始时间必须小于结束时间");
		return false;
	}
};