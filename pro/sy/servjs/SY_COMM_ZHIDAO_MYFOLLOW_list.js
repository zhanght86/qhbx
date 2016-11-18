var _viewer = this;
/**
 * 对行按钮查看绑定事件
 */
_viewer.grid.getBtn("byid").unbind("click").bind("click", function() {
	var followID = jQuery(this).attr("rowpk");
	var Q_ID = _viewer.grid.getRowItemValue(followID,"Q_ID");
	var Q_TITLE = _viewer.grid.getRowItemValue(followID,"Q_TITLE");
	Tab.open({"url":"SY_COMM_ZHIDAO_QUESTION.card.do?pkCode="+Q_ID+"","tTitle":Q_TITLE,"menuFlag":2});
});