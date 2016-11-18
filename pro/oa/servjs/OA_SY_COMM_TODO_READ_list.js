var _viewer = this;
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