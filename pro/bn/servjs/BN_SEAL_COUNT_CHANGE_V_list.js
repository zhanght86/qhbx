var _viewer = this;

//列表显示
_viewer.grid.getCheckBox().each(function(i,item){

	var chkboxObj = jQuery(this);
	//部门编码 
	var KEEP_ODEPT_CODE = _viewer.grid.getRowItemValueByElement(chkboxObj,"KEEP_ODEPT_CODE");
	
	var param = {};
	param["_PK_"]=KEEP_ODEPT_CODE;
	
	var returnDate =FireFly.doAct("SY_ORG_DEPT", "byid", param, true);

	if(returnDate.DEPT_LEVEL==5){
		jQuery(jQuery("td[icode='KEEP_ODEPT_CODE__NAME']")[i]).text(returnDate.DEPT_PCODE__NAME.substring(0,returnDate.DEPT_PCODE__NAME.indexOf("/")));
	}else if(returnDate.DEPT_LEVEL==4){
		jQuery(jQuery("td[icode='KEEP_ODEPT_CODE__NAME']")[i]).text(returnDate.DEPT_PCODE__NAME.substring(returnDate.DEPT_PCODE__NAME.indexOf("/")+1));
	}else{
		jQuery(jQuery("td[icode='KEEP_ODEPT_CODE__NAME']")[i]).text(returnDate.DEPT_NAME);
	}
	
	//作废类别 为0则不显示
	var CHANGE_SC_TYPE = _viewer.grid.getRowItemValueByElement(chkboxObj,"CHANGE_SC_TYPE");
	var CHANGE_TYPE = _viewer.grid.getRowItemValueByElement(chkboxObj,"CHANGE_TYPE");
	if("0"==CHANGE_SC_TYPE){
		jQuery(jQuery("td[icode='CHANGE_SC_TYPE__NAME']")[i]).text("");
	}
	if("0"==CHANGE_TYPE){
		jQuery(jQuery("td[icode='CHANGE_TYPE__NAME']")[i]).text("");
	}
	
});
