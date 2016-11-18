var _viewer = this ;
/**针对添加, 如果没有值，默认添加当前机构的全宗号**/
if (_viewer.getItem("FONDS_ID").getValue() == "") {
	var userOdept = System.getVar("@ODEPT_CODE@");
	var data = {"ODEPT_CODE":userOdept};	
	
	var fonds = FireFly.doAct("DA_FONDS_NUMBER","getFonds",data);
	if(fonds["_OKCOUNT_"] != 0){
		_viewer.getItem("FONDS_NAME").setValue(fonds["FONDS_NAME"]);
		_viewer.getItem("FONDS_ID").setValue(fonds["FONDS_NUMBER"]);
	}
}

//如果是只读模式，不添加P_ID
var read = _viewer._readOnly;
if(read){
	return ;
} else {
	/**设置P_ID字段的值:点“添加”按钮进入卡片则从左侧树直接获取。通过“组新卷”按钮跳转过来，则从参数中获取P_ID的值**/
	var pIDs = _viewer._transferData.P_ID || "";
	if(pIDs != ""){
		pid = pIDs.split("___");
		_viewer.getItem("P_ID").getValue(pid[0]);
	} else{
		var dataObj = _viewer.getParams();
		_viewer.getItem("P_ID").setValue(dataObj["TERR_DIR_ID"]);
		/**传递值，包括：勾选的“文书档案的ID--件号” 、勾选的数据对应的服务、所选树的ID。 **/
		_viewer.setExtendSubmitData({"TRANSMIT_DATA_ID_DA_NUM":dataObj.DATA_ID_DA_NUM,"TRANSMIT_DATA_SERV":dataObj.DATA_SERV_ID,"TRANSMIT_TERR_DIR_ID":dataObj.TERR_DIR_ID});
		_viewer.afterSave = function(){
			//保存成功后刷父页面
			dataObj.callBackHandler.refresh();
			dataObj = null;
		}
	}
}




