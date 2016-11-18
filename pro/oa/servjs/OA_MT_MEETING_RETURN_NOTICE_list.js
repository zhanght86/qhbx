var _viewer = this;

var thisListPks = _viewer.grid.getPKCodes();
/*for (var i = 0; i < thisListPks.length; i++) {
	var thisId = thisListPks[i];
	settingMrName(_viewer.grid.getRowItemValue(thisId,"PLACE__NAME"), _viewer.grid.getRowItemValue(thisId,"PLACE"),
			_viewer.grid.getRowItem(thisId,"PLACE__NAME"));
}

function settingMrName(nameText, nameVal, thisRowObj){
	//跨机构处理会议室名称
	if (nameVal.length > 0 && nameText == nameVal) {
		var out = FireFly.doAct("OA_MT_MEETING", "getMtRoomName", {"MR_ID":nameVal});
		if (out["_MSG_"].indexOf("OK") >= 0) {
			thisRowObj.html(out["NAME"]);
		}
	}
}*/
