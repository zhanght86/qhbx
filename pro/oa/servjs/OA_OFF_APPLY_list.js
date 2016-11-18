var _viewer = this;

_viewer.grid.getBtn("sendNotice").hide(); //隐藏列表行按钮
var params = _viewer.getParams() || {"flag":""};
if (params["flag"] == "query") { //若为菜单中[申请查询]跳转来的，则去除[删除]按钮
	_viewer.getBtn("delete").remove();
}else if (params["flag"] == "notice") { //领用通知
	_viewer.grid.getBodyTr().unbind("dblclick"); //去除列表双击事件
	var thisListBtnsObj = _viewer.btns; //列表按钮对象
	for (i in thisListBtnsObj) { //删除列表按钮
		thisListBtnsObj[i].remove();
	}
	var sendNoticeObj = _viewer.grid.getBtn("sendNotice");
	sendNoticeObj.show();
	sendNoticeObj.each(function(){
		var thisPk = jQuery(this).attr("rowpk");
		var noticeFlag = _viewer.grid.getRowItemValue(thisPk, "NOTICE_STATUS");
		var ssTime = _viewer.grid.getRowItemValue(thisPk, "SS_TIME") || "";
		//当前没有通知，并且已送审至办公用品管理员
        if (noticeFlag == "1" && "" != ssTime) {
            jQuery(this).unbind("click").bind("click", function(event){
                var applyTitle = _viewer.grid.getRowItemValue(thisPk, "APPLY_TITLE");
                var configStr = "SY_ORG_DEPT_USER,{'TYPE':'multi'}";
                var extendTreeSetting = {
                    'cascadecheck': true,
                    "childOnly": true
                };
                var options = {
                    "itemCode": "hello",
                    "config": configStr,
                    "hide": "explode",
                    "show": "blind",
                    "extendDicSetting": extendTreeSetting,
                    "replaceCallBack": function(idArray, nameArray){
                        showNoticeDialog(applyTitle, idArray, thisPk);
                    }
                };
                var dictView = new rh.vi.rhDictTreeView(options);
                dictView.show(event);
                dictView.setRightSelect([{
                    "ID": _viewer.grid.getRowItemValue(thisPk, "S_USER"),
                    "NAME": _viewer.grid.getRowItemValue(thisPk, "S_USER__NAME")
                }]);
            });
        } else if (noticeFlag == "2") {
			jQuery(this).parent().css({"color":"red","text-align":"center"}).html("已通知");
		} else if ("" == ssTime) {
			jQuery(this).parent().css({"text-align":"center"}).html("无通知").attr("title", "未送交至办公用品管理员");
		}
	});
}

//通知发放调用方法
function showNoticeDialog(applyTitle, idArray, pkCode){
	if (idArray.length <= 0) {
		_viewer.listBarTipError("请选择物品领用人");
		return;
	}
	var data = {"TODO_TITLE": "[办公用品领用通知]" + applyTitle, 
				"TODO_CONTENT":replaceConfigData(pkCode), 
				"ids":idArray.join(","),
				"servId":_viewer.servId,
				"APPLY_ID":pkCode,
				"pk":pkCode
			   };
	var outBean = FireFly.doAct(_viewer.servId, "sendNoticeMsg", data);
	if (outBean["_MSG_"].indexOf("OK") >= 0) {
		_viewer.listBarTip("通知发送成功");
	} else if (outBean["_MSG_"].indexOf("ERROR,") >= 0) {
		_viewer.listBarTipError(outBean["_MSG_"].replace("ERROR,",""));
	} else {
		_viewer.listBarTipError("通知发送失败");
	}
	_viewer.refresh();
}

//替换系统配置中的送审时间
function replaceConfigData(pkCode){
	//送审时间
	var ssTime = _viewer.grid.getRowItemValue(pkCode, "SS_TIME");
	var configData = System.getVar("@C_OA_OFF_NOTICE_TODO_CONTENT@");
	return configData.replace("#DATE_YM#", ssTime.substring(0, 7));
}
