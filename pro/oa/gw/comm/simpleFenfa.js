function gwSimpleFenfa(obj,includeSubOdept){
	var _includeSubOdept = includeSubOdept || false;
	var dictName = "SY_ORG_DEPT_USER";
	if(_includeSubOdept){
		dictName = "SY_ORG_DEPT_USER_SUB";
	}
	
	var _viewer = obj;
//	var _self = this;
	var params = {
		"DATA_ID" : _viewer._pkCode,
		"userSelectDict":dictName,
		"displaySendSchm":true,
		"includeSubOdept":_includeSubOdept
	};
	
	var configStr = "@com.rh.core.serv.send.SimpleFenfaDict,{'TYPE':'multi','CHECKBOX':true,'extendDicSetting':{'rhexpand':false,'expandLevel':1,'cascadecheck':true,'checkParent':false,'childOnly':true}}"; //针对传阅，多选时可以全选部门下的节点

	var options = {
		"config" : configStr,
		"hide" : "explode",
		"show" : "blind",
		"rebackCodes" : "inputName",
		"replaceCallBack" : function(id,value){
			var sendObj = {
				"SERV_ID" : _viewer.servId,
				"DATA_ID" : _viewer._pkCode,
				"DATA_TITLE" : "传阅"
			};

			sendObj.fromScheme = "yes"; // 来源于方案
			sendObj.ifFirst = "yes";
			sendObj._extWhere = " and DATA_ID = '" + _viewer._pkCode + "'";
			sendObj.SEND_ITEM = [{"sendId":id.join(",")}];
			sendObj.includeSubOdept = params.includeSubOdept;
			
			_viewer.shield();
			
			setTimeout(function() { // 适当加上延迟提升用户感觉
				try{
					var data = jQuery.toJSON(sendObj);
					var result = FireFly.doAct("SY_COMM_SEND_SHOW_USERS", "autoSend",{"data":data}, true);
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
};