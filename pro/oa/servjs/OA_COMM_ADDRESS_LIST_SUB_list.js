var _viewer = this;
_viewer.grid.getCheckBox().each(function(){
	var rowItem = jQuery(this);
	var pkCode = _viewer.grid.getRowPkByElement(rowItem);
	//不显示手机号码的部门人员
	var deptId = _viewer.grid.getRowItemValue(pkCode,"DEPT_CODE");
	
	if(System.getVar("@C_NOTSHOWPHONE@").indexOf(deptId)>0){
		jQuery("#"+pkCode).find("td[icode=USER_MOBILE]").text("");
	}
	//不显示手机号码的角色
	var roleId = System.getVar("@C_NOTSHOWPHONEROLE@") || "";
	if(System.getVar("@ROLE_CODES@").indexOf(roleId)>0){
		jQuery("#"+pkCode).find("td[icode=USER_MOBILE]").text("");
	}
});