var _viewer = this;

var isShowNotice = _viewer.getParHandler().wfCard.getCustomVarContent("PISHI") || ""; //获取指定节点中的自定义变量值
if ("" == isShowNotice) { //如果当前节点中不存在这个变量值
	reloadTdVal();
}
else {
	//给[同意]按钮绑定单击事件
	_viewer.getBtn("agree").unbind("click").bind("click", function(){
		var ids = "";
		var count = 0;
		var idsAttr = _viewer.grid.getSelectPKCodes();
		for (var i = 0; i < idsAttr.length; i++) {
			ids += "'" + idsAttr[i] + "',";
		}
		if (idsAttr.length == 0) {
			_viewer.listBarTipError("请选择要更新的记录！");
			return;
		}
		checkedObjs(ids,"agree");
	});
	
	//给[不同意]按钮绑定单击事件
	_viewer.getBtn("unagree").unbind("click").bind("click", function(){
		var ids = "";
		var count = 0;
		var idsAttr = _viewer.grid.getSelectPKCodes();
		for (var i = 0; i < idsAttr.length; i++) {
			ids += "'" + idsAttr[i] + "',";
		}
		if (idsAttr.length == 0) {
			_viewer.listBarTipError("请选择要更新的记录！");
			return;
		}
		checkedObjs(ids,"unagree");
	});
}

//给列表[数量]字段绑定键盘事件
jQuery("td[icode='OFFICE_NUMBER']").each(function(){
	jQuery(this).keyup(function(event){
		jQuery(this).parent().find("[icode='OFFICE_AMOUNT']").html(jQuery(this).find("[icode='OFFICE_NUMBER']").val() * jQuery(this).parent().find("[icode='OFFICE_PRICE']").html());
	});
});

//格式化数字
_viewer.grid.getBodyTr().each(function() {
	var officePrice = jQuery(this).find("[icode='OFFICE_PRICE']").html();
	jQuery(this).find("[icode='OFFICE_PRICE']").html(parseFloat(officePrice).toFixed(2));
	var officeAmount = jQuery(this).find("[icode='OFFICE_AMOUNT']").html();
	jQuery(this).find("[icode='OFFICE_AMOUNT']").html(parseFloat(officeAmount).toFixed(2));
});

//给添加按钮绑定事件
_viewer.getBtn("add").unbind().bind("click", function(){
	var configStr = "OA_OFF_QUOTATION,{'SOURCE':'QUOTATION_ID~OFFICE_NAME~OFFICE_UNITE~OFFICE_PRICE~OFFICE_BRAND~OFFICE_MODEL~OFFICE_SPEC~OFFICE_TYPE~OFFICE_SELLER',"
				  + "'TARGET':'OFFICE_ID~OFFICE_NAME~OFFICE_UNITE~OFFICE_PRICE~OFFICE_BRAND~~~OFFICE_TYPE~',"
				  + "'EXTWHERE':\"and 1=1 and S_ODEPT = '@ODEPT_CODE@' and PUBLIC_FLAG = 1\",'TYPE':'multi',HIDE:'QUOTATION_ID'}";
	var options = {
			"config":configStr,
			"searchFlag":false,
			"replaceCallBack":function(idArray){
				var parentApplyId = _viewer.getParHandler().getItem("APPLY_ID").getValue();
				idArray["APPLY_ID"] = parentApplyId;
				var out = FireFly.doAct(_viewer.servId,"batchSaveOff",idArray);
				if (out["_MSG_"].indexOf("OK") >= 0) {
					_viewer.listBarTip("添加成功");
				} else if (out["_MSG_"].indexOf("ERROR") >= 0) {
					_viewer.listBarTipError(out["_MSG_"].replace("ERROR,",""));
				}
				_viewer.refresh();
			}
		};
	var queryView = new rh.vi.rhSelectListView(options);
	queryView.show(event);
});

//调用后台方法，将选中的记录的[同意审批]修改成[同意]
function checkedObjs(ids, agreeFlag){
	if (ids.lastIndexOf(",") == (ids.length - 1)) {
		ids = ids.substring(0,ids.length - 1);
	}
	var data = FireFly.doAct("OA_OFF_APPLY_TRAITS","updateApplyTraits", {"ids":ids,"agreeFlag":agreeFlag});
	_viewer.listBarTip(data["count"] + "条记录更新成功！");
	_viewer.refresh();//刷新列表
}

//重载列表数据
function reloadTdVal() {
	_viewer.getBtn("agree").remove();
	_viewer.getBtn("unagree").remove();
	var thisSelectTextArray = new Array();
	var thisSelectValArray = new Array();
	var thisTdNameColum = _viewer.grid.getTable().find("td[icode='APPLY_STATUS__NAME']");
	var thisTdCodeColum = _viewer.grid.getTable().find("td[icode='APPLY_STATUS']");
	thisTdNameColum.each(function(){
		var thisSelectText = jQuery(this).find("select").find("option:selected").text() || "";
		if ("" == thisSelectText) { //如果当前就是不可修改的值，则不在修改列表
			return;
		}
		thisSelectTextArray.push(thisSelectText);
		thisSelectValArray.push(jQuery(this).find("select").val());
	});
	thisTdNameColum.each(function(i){
		jQuery(this).html(thisSelectTextArray[i]);
		
	});
	thisTdCodeColum.each(function(i){
		jQuery(this).html(thisSelectValArray[i]);
		
	});
}




