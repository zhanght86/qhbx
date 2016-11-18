var _viewer = this;

var ACL_OTYPE = {
	"USER" : 3,
	"DEPT" : 2,
	"ROLE" : 1
};

var aclType = _viewer.links["ACL_TYPE"];

/**
 * 批量保存数据
 * **/
function batchSaveAcl(ids, param) {
	var list = new Array();
	if (ids.length == 0) {
		return;
	}
	for ( var i = 0; i < ids.length; i++) {
		var nparam = {
			"ACL_OWNER" : ids[i]
		};
		jQuery.extend(nparam,param);
		list.push(nparam);
	}

	var data = {
		"BATCHDATAS" : list
	};
	
	FireFly.batchSave(_viewer.servId, data);
	_viewer.refresh();
}

// 增加用户
_viewer.getBtn("addUser").unbind("click").bind("click", function() {
	// 1.构造树形选择参数
	var configStr = "SY_ORG_DEPT_USER,{'TYPE':'multi'}";// 此部分参数说明可参照说明文档的【树形选择】配置说明

	var options = {
		"config" : configStr,
		"extendDicSetting" : {'rhexpand':false,'cascadecheck':true,'checkParent':false,'childOnly':true},
		"replaceCallBack" : function(idArray, nameArray) {// 回调，idArray为选中记录的相应字段的数组集合
			var param = {};
			param["ACL_OTYPE"] = ACL_OTYPE.USER;
			param["SERV_ID"] = _viewer.getParHandler().getPKCode();
			param["ACL_TYPE"] = aclType; // 阅读
			batchSaveAcl(idArray,param);
		}
	};

	// 2.显示树形
	var dictView = new rh.vi.rhDictTreeView(options);
	dictView.show(event);
});

// 增加部门
_viewer.getBtn("addDept").unbind("click").bind("click", function() {
	// 1.构造树形选择参数
	var configStr = "SY_ORG_DEPT,{'TYPE':'multi'}";// 此部分参数说明可参照说明文档的【树形选择】配置说明
	var extendTreeSetting = "{'cascadecheck':true,'childOnly':true}";

	var options = {
		"config" : configStr,
		"extendDicSetting" : StrToJson(extendTreeSetting),// 非必须参数，一般用不到
		"replaceCallBack" : function(idArray, nameArray) {// 回调，idArray为选中记录的相应字段的数组集合
			var param = {};
			param["ACL_OTYPE"] = ACL_OTYPE.DEPT;
			param["SERV_ID"] = _viewer.getParHandler().getPKCode();
			param["ACL_TYPE"] = aclType;
			batchSaveAcl(idArray,param);
		}
	};
	// 2.显示树形
	var dictView = new rh.vi.rhDictTreeView(options);
	dictView.show(event);
});

// 增加角色
_viewer.getBtn("addRole").unbind("click").bind("click", function() {
	// 1.构造树形选择参数
	var configStr = "SY_ORG_ROLE,{'TYPE':'multi'}";// 此部分参数说明可参照说明文档的【树形选择】配置说明
	var extendTreeSetting = "{'cascadecheck':true,'childOnly':false}";
	var options = {
		"config" : configStr,
		"extendDicSetting" : StrToJson(extendTreeSetting),// 非必须参数，一般用不到
		"replaceCallBack" : function(idArray, nameArray) {// 回调，idArray为选中记录的相应字段的数组集合
			var param = {};
			param["ACL_OTYPE"] = ACL_OTYPE.ROLE;
			param["SERV_ID"] = _viewer.getParHandler().getPKCode();
			param["ACL_TYPE"] = aclType;
			batchSaveAcl(idArray,param);
		}
	};
	// 2.显示树形
	var dictView = new rh.vi.rhDictTreeView(options);
	dictView.show(event);
});