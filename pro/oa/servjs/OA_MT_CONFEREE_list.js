var _viewer = this;

jQuery("table[class='searchDiv']").remove();

_viewer.grid.unbindTrdblClick();

_viewer.grid.dblClick(function(__value, node) {
	if (node.hasClass("batchModify") || node.hasClass("rowIndex") || node.hasClass("span-detail")) {
		event.stopPropagation();
		return false;
	}
	openNewTab({"url":"OA_MT_MEETING.card.do?pkCode=" + jQuery("[icode='MEETING_ID']",node.parent()).text() + "&readOnly=true", "tTitle":"会议通知", "menuFlag":4, "params":{}});
}, _viewer);
