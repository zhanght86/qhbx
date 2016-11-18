var _viewer = this;

var addRole = _viewer.getBtn("add_role");

//给添加角色绑定时间
addRole.unbind("click").bind("click", function() {
	// 1.构造角色树形选择参数
	var configStr = "SY_ORG_ROLE,{'TYPE':'multi'}";// 此部分参数说明可参照说明文档的【树形选择】配置说明

	var extendTreeSetting = "{'cascadecheck':true,'childOnly':false}";

	var options = {
		"config" : configStr,
		"extendDicSetting" : StrToJson(extendTreeSetting),// 非必须参数，一般用不到
		"replaceCallBack" : function(idArray, nameArray) {// 回调，idArray为选中记录的相应字段的数组集合
			selectCallback(idArray, nameArray);
		}
	};
	// 2.显示树形
	var dictView = new rh.vi.rhDictTreeView(options);
	dictView.show(event);
});

//给每一行数据添加选择部门按钮
jQuery(".tBody-tr", _viewer.grid._table).each(function(i, n) {

	var obj = jQuery(n).find("td[icode='DEPT_NAME']");
	
	var deptCode = jQuery(n).find("td[icode='DEPT_CODE']");
	
	var span = jQuery("<span style='color:red'>【选择部门】</span>").appendTo(obj);

	span.unbind("click").bind("click", function() {
		selectDept(n.id,deptCode.text());
	});

});


//选择部门
function selectDept(reId,text) {
	// 1.构造树形选择参数
	var configStr = "SY_ORG_DEPT_ALL,{'TYPE':'multi'}";// 此部分参数说明可参照说明文档的【树形选择】配置说明

	var extendTreeSetting = "{'cascadecheck':false,'childOnly':false}";

	var options = {
		"config" : configStr,
		"extendDicSetting" : StrToJson(extendTreeSetting),// 非必须参数，一般用不到
		"replaceCallBack" : function(idArray, nameArray) {// 回调，idArray为选中记录的相应字段的数组集合
			selectDeptCallback(idArray, nameArray, reId);
		}
	};
	// 2.显示树形
	var dictView = new rh.vi.rhDictTreeView(options);
	dictView.show(event);
	dictView.tree.selectNodes(text.split(","));
}

function selectDeptCallback(idArray, nameArray, reId) {
	var param = {};
	param["DEPT_CODE"] = idArray.join(",");
	param["DEPT_NAME"] = nameArray.join(",");
	param["RE_ID"] = reId;
	FireFly.doAct(_viewer.getParHandler().servId, "saveRoleDept", param, true,
			false, function() {
				_viewer.refreshGridBodyNoResize();
			});
}

function selectCallback(idArray, nameArray) {
	var param = {};
	param.idArray = idArray.join(",");
	param.nameArray = nameArray.join(",");
	param["QUERY_ID"] = _viewer.getParHandler().getItem("QUERY_ID").getValue();
	FireFly.doAct(_viewer.getParHandler().servId, "saveItem", param, true,
			false, function() {
				_viewer.refreshGridBodyNoResize();
			});
}