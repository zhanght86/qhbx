var _viewer = this;
_viewer.grid.getCheckBox().each(function(){
	var rowItem = jQuery(this);
	var pkCode = _viewer.grid.getRowPkByElement(rowItem);
	//����ʾ�ֻ�����Ĳ�����Ա
	var deptId = _viewer.grid.getRowItemValue(pkCode,"DEPT_CODE");
	
	if(System.getVar("@C_NOTSHOWPHONE@").indexOf(deptId)>0){
		jQuery("#"+pkCode).find("td[icode=USER_MOBILE]").text("");
	}
	//����ʾ�ֻ�����Ľ�ɫ
	var roleId = System.getVar("@C_NOTSHOWPHONEROLE@") || "";
	if(System.getVar("@ROLE_CODES@").indexOf(roleId)>0){
		jQuery("#"+pkCode).find("td[icode=USER_MOBILE]").text("");
	}
});