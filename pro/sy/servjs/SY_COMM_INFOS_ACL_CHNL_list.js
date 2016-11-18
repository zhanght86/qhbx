var _viewer = this;

var OWNER_TYPE = {"USER":3,"DEPT":2,"ROLE":1};

var aclType = _viewer.links["ACL_TYPE"];

//遍历行数据，隐藏非角色修改权限范围功能。
_viewer.grid.getBodyTr().each(function(i,item){
	var itemObj = jQuery(item);
	var ownerType = itemObj.find("[icode=OWNER_TYPE]").text();
	
	if(ownerType == OWNER_TYPE.ROLE ){
		
	}else{
		//隐藏非角色修改权限范围功能。
		jQuery("input[icode=OWNER_SCOPE__NAME]",itemObj).attr("disabled","true");
		jQuery("td > a",itemObj).remove();
	}
});

//增加用户
_viewer.getBtn("addUser").unbind("click").bind("click", function() {
	// 1.构造树形选择参数
	var configStr = "SY_ORG_DEPT_USER_SUB,{'TYPE':'multi'}";// 此部分参数说明可参照说明文档的【树形选择】配置说明
	var extendTreeSetting = "{'cascadecheck':true,'childOnly':true}";

	var options = {
		"config" : configStr,
		"extendDicSetting" : StrToJson(extendTreeSetting),// 非必须参数，一般用不到
		"replaceCallBack" : function(idArray, nameArray) {// 回调，idArray为选中记录的相应字段的数组集合
			var param = {};
			param.idArray = idArray.join(",");
			param.nameArray = nameArray.join(",");
			param["OWNER_TYPE"] = OWNER_TYPE.USER;
			param["DATA_ID"] = _viewer.getParHandler().getItem("CHNL_ID").getValue();
			param["ACL_TYPE"] = aclType; //阅读
			param["SERV_ID"] = _viewer.getParHandler().getServSrcId();
			FireFly.doAct(_viewer.servId, "addAcl", param);
			_viewer.refresh();
		}
	};
	
	// 2.显示树形
	var dictView = new rh.vi.rhDictTreeView(options);
	dictView.show(event);
});

//增加部门
_viewer.getBtn("addDept").unbind("click").bind("click", function() {
	// 1.构造树形选择参数
	var configStr = "SY_ORG_DEPT_SUB,{'TYPE':'multi'}";// 此部分参数说明可参照说明文档的【树形选择】配置说明
	var extendTreeSetting = "{'cascadecheck':true,'childOnly':true}";

	var options = {
		"config" : configStr,
		"extendDicSetting" : StrToJson(extendTreeSetting),// 非必须参数，一般用不到
		"replaceCallBack" : function(idArray, nameArray) {// 回调，idArray为选中记录的相应字段的数组集合
			var param = {};
			param.idArray = idArray.join(",");
			param.nameArray = nameArray.join(",");
			param["OWNER_TYPE"] = OWNER_TYPE.DEPT;
			param["DATA_ID"] = _viewer.getParHandler().getItem("CHNL_ID").getValue();
			param["ACL_TYPE"] = aclType;
			param["SERV_ID"] = _viewer.getParHandler().getServSrcId();
			FireFly.doAct(_viewer.servId, "addAcl", param);
			_viewer.refresh();
		}
	};
	// 2.显示树形
	var dictView = new rh.vi.rhDictTreeView(options);
	dictView.show(event);
});

//增加角色
_viewer.getBtn("addRole").unbind("click").bind("click", function() {
	// 1.构造树形选择参数
	var configStr = "SY_ORG_ROLE,{'TYPE':'multi'}";// 此部分参数说明可参照说明文档的【树形选择】配置说明
	var extendTreeSetting = "{'cascadecheck':true,'childOnly':false}";
	var options = {
		"config" : configStr,
		"extendDicSetting" : StrToJson(extendTreeSetting),// 非必须参数，一般用不到
		"replaceCallBack" : function(idArray, nameArray) {// 回调，idArray为选中记录的相应字段的数组集合
			var param = {};
			param.idArray = idArray.join(",");
			param.nameArray = nameArray.join(",");
			param["OWNER_TYPE"] = OWNER_TYPE.ROLE;
			param["DATA_ID"] = _viewer.getParHandler().getItem("CHNL_ID").getValue();
			param["ACL_TYPE"] = aclType;
			param["SERV_ID"] = _viewer.getParHandler().getServSrcId();
			FireFly.doAct(_viewer.servId, "addAcl", param);	
			_viewer.refresh();
		}
	};
	// 2.显示树形
	var dictView = new rh.vi.rhDictTreeView(options);
	dictView.show(event);
});

