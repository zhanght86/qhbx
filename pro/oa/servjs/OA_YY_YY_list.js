var _viewer = this;

_viewer.grid.dblClick(function(value, node) {
	var tmplCode = _viewer.grid.getRowItemValue(value, "TMPL_CODE"); //子服务
	var yyTitle = _viewer.grid.getRowItemValue(value, "YY_TITLE"); //标题
	var options = {"url":tmplCode + ".card.do?pkCode=" + value, 
				"tTitle":yyTitle, "menuFlag":4,"params":{}
			};
	Tab.open(options);
});