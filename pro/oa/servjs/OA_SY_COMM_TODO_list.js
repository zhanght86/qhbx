var _viewer = this;

//遍历行
var pkCodesArray = _viewer.grid.getPKCodes();
for (var i = 0; i < pkCodesArray.length; i++) {
	//获取待办记录主键
	var thisObjectId = _viewer.grid.getRowItemValue(pkCodesArray[i],"TODO_OBJECT_ID1");
	//获取当前行 拟稿部门 code 对象
	var thisTdeptCode = _viewer.grid.getRowItem(pkCodesArray[i],"S_TDEPT");
	//获取当前行 拟稿部门 name 对象
	var thisTdeptName = _viewer.grid.getRowItem(pkCodesArray[i],"S_TDEPT__NAME");
	//调用数据
	var thisTodoBean = getTodoTDeptCode(thisObjectId);
	//如果此字段没有配置字典项
	if (thisTdeptName.length < 1) {
		//赋值
		thisTdeptCode.html(thisTodoBean["TDEPT_NAME"]);
	//如果此字段配置字典项
	} else {
		//赋值
		thisTdeptCode.html(thisTodoBean["TDEPT_CODE"]);
		thisTdeptName.html(thisTodoBean["TDEPT_NAME"]);
	}	
}

//根据相应的serv_id和data_id获取对应的拟稿部门code和name值
function getTodoTDeptCode(todoDataId){
	//获取当前对象
	return FireFly.doAct("SY_COMM_ENTITY","getTdeptName",{"DATA_ID":todoDataId});
}


var cmQianShou = _viewer.getBtn("batchSend");
cmQianShou.unbind().bind("click", function() {
	if (jQuery("td.checkTD input").is(':checked')) {
		batchFenfa();
	} else {
		alert("请选择需要传阅的文件。");
	}
});


/** 批量分发 **/
function batchFenfa(){
	var _self = this;
	var dataIds = _viewer.grid.getSelectItemValues("TODO_OBJECT_ID1");
	var pkCodes = dataIds.join("@@");
	var servIds = _viewer.grid.getSelectItemValues("SERV_ID").join("@@");

	var params = {
			"DATA_ID" : dataIds.length == 1?dataIds[0]:"",  //只有一个则人员姓名后面出现传阅字样
			"userSelectDict":"SY_ORG_DEPT_USER",
			"displaySendSchm":false
	};
		
	var configStr = "@com.rh.core.serv.send.SimpleFenfaDict,{'TYPE':'multi','MODEL':'link'}"; // multi

	var options = {
		"config" : configStr,
		"hide" : "explode",
		"show" : "blind",
		"rebackCodes" : "inputName",
		"replaceCallBack" : function(id,value){
			var sendObj = {
				"SERV_IDS" : servIds,
				"DATA_IDS" : pkCodes
			};

			sendObj.fromScheme = "yes"; // 来源于方案
			sendObj.ifFirst = "yes";
			//sendObj._extWhere = " and DATA_ID = '" + _self._parHandler._pkCode + "'";
			sendObj.SEND_ITEM = [{"sendId":id.join(",")}];
			_viewer.shield();
			
			setTimeout(function() { // 适当加上延迟提升用户感觉
				try{
					var data = jQuery.toJSON(sendObj);
					var result = FireFly.doAct("SY_COMM_SEND_SHOW_USERS", "batchAutoSend",{"data":data}, true);
					if(!Tools.actIsSuccessed(result)){
						alert("发送失败");
					}
				} catch (e){
					throw e;
				} finally{
					_viewer.shieldHide();
				}
			}, 50);
			
		},
		"dialogName" : "传阅",
		"params" : params
	};

	var dictView = new rh.vi.rhDictTreeView(options);
	dictView.show(event);
}

jQuery("td[icode='S_EMERGENCY__NAME']", _viewer.grid.getTable())
				.each(function() {
					alert(jQuery(this).text());
					if(jQuery(this).text()==""){
						jQuery(this).text(jQuery(this).attr("title"));
						
					}
				});