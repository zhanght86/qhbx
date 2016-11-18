var _viewer = this;

//绑定停用按钮点击事件
_viewer.getBtn("sealEnd").unbind("click").bind("click",function(){
	var sealLists=_viewer.grid.getSelectPKCodes();
	for ( var index in sealLists) {
		var icon=jQuery("#"+sealLists[index]);
		var stateHtml = icon.find("td[icode='SEAL_STATE__NAME']").html();
		var stateVal = icon.find("td[icode='SEAL_STATE']").html();
		var stateName = icon.find("td[icode='SEAL_NAME']").html();
		if(stateVal==1){
			var resultData = FireFly.doAct(_viewer.servId,"sealEnd",{"IDS":_viewer.grid.getSelectPKCodes().toString()});
			if(resultData[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0){
				alert("停用成功");
				_viewer.refresh();
			
			}
		}else{
			alert("印章："+stateName+" 停用失败！当前状态为："+stateHtml);
		}
	}
});
//绑定启用按钮点击事件
_viewer.getBtn("sealStartList").unbind("click").bind("click",function(){
	var resultData = FireFly.doAct(_viewer.servId,"sealStartList",{"IDS":_viewer.grid.getSelectPKCodes().toString()});
	if(resultData[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0){
		alert("启用成功");
		_viewer.refresh();
	}
	if(resultData[UIConst.RTN_MSG].indexOf("ERROR") == 0){
		_viewer.listBarTipError("报废或者销毁印章不可被启用");
	}
});
/**
 * 印章作废
 */
_viewer.getBtn("SCRAP").unbind("click").bind("click",function(event){
	_viewer.openCard("BN_SEAL_SCRAP","印章作废");
});
/**
 * 保管人变更记录
 */
_viewer.getBtn("keepUserChange").unbind("click").bind("click",function(event){
	var sealList=_viewer.grid.getSelectPKCodes();
	var sealListName = _viewer.grid.getSelectItemValues("SEAL_NAME");
	var params = {};
	params["sealList"] = sealList.toString();
	params["handler"] = _viewer;
	params["sealListName"] = sealListName.toString();
	params["keepUserPhone"] = _viewer.grid.getSelectItemValues("SEAL_USER_PHONE");
	params["keepUser"] = _viewer.grid.getSelectItemValues("SEAL_OWNER_USER");
	if(sealList.length==0){
		alert("您还未选中印章信息！");
	}else{
		var options = {"url":"BN_SEAL_KEEPER_CHANGE.card.do",
				  "tTitle":"更改保管人","params":params,"menuFlag":3};
		Tab.open(options);
	}
});
/**
 * 销毁，作废等方法打开卡片按钮的方法。参数：服务ID
 */
_viewer.openCard = function(servId,title){
	var sealList=_viewer.grid.getSelectPKCodes();
	var sealListName = _viewer.grid.getSelectItemValues("SEAL_NAME");
	var params = {};
	params["sealList"] = sealList.toString();
	params["sealListName"] = sealListName.toString();
	params["handler"] = _viewer;
	if(sealList.length==0){
		alert("您还未选中印章信息！");
	}else{
		var options = {"url":servId+".card.do",
				  "tTitle":title,"params":params,"menuFlag":3};
		Tab.open(options);
		
	}
}
_viewer.getBtn("destroy").unbind("click").bind("click",function(event){
	/*var sealList=_viewer.grid.getSelectPKCodes();
	if(sealList.length==0){
		alert("您还未选中待销毁印章信息！");
	}else{
		//把选中的待销毁印章信息列表放进session中
		FireFly.doAct("BN_SEAL_BASIC_INFO","saveSealList",{"sealList" : sealList.toString()},true,false);
		  var options = {"url":"BN_SEAL_DESTROY.card.do?pkCode=ADD",
		  "tTitle":"印章销毁","params":"","menuFlag":4};
	      }
	
	Tab.open(options);*/
	_viewer.openCard("BN_SEAL_DESTROY","印章销毁");
	_viewer.refresh();
});

if(System.getVar("@ODEPT_LEVEL@")>2){
	if($("#ALL_QUERY").hasClass("active")){
		jQuery(".rh-norQuery").hide();
	}else{
		jQuery("#ALL_QUERY").click();
	}	
}