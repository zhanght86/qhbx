var _viewer = this;

/*对[操作]列添加操作链接*/
_viewer.grid.getBodyTr().each(function() {
	//给链接重新绑定事件
	jQuery(this).unbind().bind("dblclick", function() {
		Tab.open({
				"url" : "OA_MT_MEETING_RETURN_NOTICE.card.do?pkCode=" + jQuery(this).find("td[icode='MEETING_ID']").html(),
				"tTitle" : jQuery(this).find("td[icode='TITLE']").find("a").eq(0).html(),
				"menuFlag" : 4
			});
	});
});

var pkCodes = _viewer.grid.getPKCodes();
/*for (var i = 0; i < pkCodes.length; i++) {
	//跨机构处理会议室名称
	var nameVal = _viewer.grid.getRowItemValue(pkCodes[i],"PLACE");
	var nameText = _viewer.grid.getRowItemValue(pkCodes[i],"PLACE__NAME");
	var thisRowObj = _viewer.grid.getRowItem(pkCodes[i],"PLACE__NAME");
	if (nameVal.length > 0 && nameText == nameVal) {
		var out = FireFly.doAct("OA_MT_MEETING", "getMtRoomName", {"MR_ID":nameVal});
		if (out["_MSG_"].indexOf("OK") >= 0) {
			thisRowObj.html(out["NAME"]);
		}
	}
}*/
