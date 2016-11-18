var _viewer = this;
_viewer.getBtn("addRightObj").unbind("click").bind("click", function(event) {
	//1.构造树形选择参数
	var configStr = "@com.rh.core.org.auth.UserDeptRoleDict,{'TYPE':'multi'}";//此部分参数说明可参照说明文档的【树形选择】配置说明
	var extendTreeSetting = "{'rhexpand':false,'expandLevel':1,'cascadecheck':false,'checkParent':false,'childOnly':false}";
	var options = {
		"config" :configStr,
		"extendDicSetting":StrToJson(extendTreeSetting),//非必须参数，一般用不到
		"replaceCallBack":function(idArray,nameArray){//回调，idArray为选中记录的相应字段的数组集合
			if(idArray && idArray.length > 0){
				var parHandler = _viewer.getParHandler();
				var datas = new Array();
				for(var i=0;i<idArray.length;i++){
					var bean = {
							"ACL_TYPE":"R",
							"OBJ_ID":parHandler.getByIdData("ITEM_ID") ,
							"SERV_ID":parHandler.servId,
							"OWNER_ID":idArray[i],
							"OWNER_NAME":nameArray[i]
					};
					datas.push(bean);
				}
				//批量增加授权对象
				FireFly.batchSave(_viewer.servId,{"BATCHDATAS": datas},undefined,true);
				_viewer.refresh();
			}
		}
	};
	//2.显示树形
	var dictView = new rh.vi.rhDictTreeView(options);
	dictView.show(event);	
});