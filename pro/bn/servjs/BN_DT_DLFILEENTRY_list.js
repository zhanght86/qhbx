var _viewer = this;
//隐藏删除按钮
jQuery("#"+_viewer.servId + "-delete").hide();
if(System.getVar("@LOGIN_NAME@") == "admin"){
	//授权按钮绑定事件
	_viewer.getBtn("DOC_ACL").unbind("click").bind("click",function(){
		var pkArray = _viewer.grid.getSelectPKCodes();
		if (jQuery.isEmptyObject(pkArray)) {
			alert("请选择相应记录！");
		} else {
			var pkStrs = pkArray.join(",");
			//选择角色
			var configStr = "SY_ORG_ROLE,{'TYPE':'multi'}";// 1.构造树形选择参数
			var extendTreeSetting = "{'cascadecheck':true,'childOnly':false}";
			var options = {
				"config" : configStr,
				"extendDicSetting" : StrToJson(extendTreeSetting),// 非必须参数，一般用不到
				"replaceCallBack" : function(idArray, nameArray) {// 回调，idArray为选中记录的相应字段的数组集合
					if (jQuery.isEmptyObject(idArray)) {
						alert("请选择授权角色");
					} else {
						var param = {};
						param.roleIds = idArray.join(",");
						param.docIds = pkStrs;
						FireFly.doAct(_viewer.servId, "doAcl", param, false, false, function(result) {
							alert(result._MSG_);
						});
					}
				}
			};
			// 2.显示树形
			var dictView = new rh.vi.rhDictTreeView(options);
			dictView.show(event);
		}
	});
	//取消授权按钮绑定事件
	_viewer.getBtn("UNDO_DOC_ACL").unbind("click").bind("click",function(){
		var pkArray = _viewer.grid.getSelectPKCodes();
		if (jQuery.isEmptyObject(pkArray)) {
			alert("请选择相应记录！");
		} else {
			var pkStrs = pkArray.join(",");
			var param = {};
			param.docIds = pkStrs;
			FireFly.doAct(_viewer.servId, "unDoAcl", param, false, false, function(result) {
				alert(result._MSG_);
			});
		}
	});
}else{
	jQuery("#"+_viewer.servId + "-DOC_ACL").hide();
	jQuery("#"+_viewer.servId + "-UNDO_DOC_ACL").hide();
}
//取消行双击事件
_viewer.grid.unbindTrdblClick();
/**
 * 绑定打开行记录下载文档。
 */
_viewer.grid.trClick(function(value,node) {
	var uuId = _viewer.grid.getSelectItemVal("UUID_");
	var groupId = _viewer.grid.getSelectItemVal("GROUPID");
	var url = FireFly.getHttpHost() + "/c/document_library/get_file?uuid="+uuId+"&groupId=" + groupId;
	window.open(encodeURI(url));
},_viewer);