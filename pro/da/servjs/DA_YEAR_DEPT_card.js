var _viewer = this;
//根据用户选择的子公司的code设置“部门名称”一项弹出的部门
//_viewer._data.ITEMS.DEPT_NAME.ITEM_INPUT_CONFIG = "SY_ORG_DEPT_SYNC,{'PID':'"+getOdeptCode()+"'}";

//保存前设置DEPT_NAME，S_ODEPT的值
_viewer.beforeSave = function(){
	var odept = getOdeptCode();
	var deptName = _viewer.getItem("DEPT_NAME").getValue();
	_viewer.setExtendSubmitData({"DEPT_NAME":deptName,"S_ODEPT":odept});
};


/**
 * 获取用户所选机构的code如果没有选择机构，则默认获取当前用户的机构code
 * @returns {String} 当前机构code
 */
function getOdeptCode(){
	var whereStr = _viewer.getParHandler().whereData._extWhere || "";
	var odeptCode = "";
	if(whereStr == ""){
		odeptCode = System.getVar("@ODEPT_CODE@");
	} else{
		var odeptCodes = whereStr.split("'");
		odeptCode = odeptCodes[1];
	}	
	return odeptCode;
}