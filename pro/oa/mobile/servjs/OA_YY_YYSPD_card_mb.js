//@ sourceURL=OA_YY_YYSPD_card_mb.js

// 用印审批单
/*
	已废弃
var _viewer = this;

var yyPriFileCon = _viewer.getItem('YY_PRI_FILE').getContainer();
var yyPriFileWrp = yyPriFileCon.parent();

yyPriFileCon.remove();

var ulWrp = $("<ul data-role='listview' data-inset='true'></ul>");

$(_viewer._fileData).each(function(index, item) {
	if (item['FILE_CAT'] == 'YY_PRI_FILE') {
		ulWrp.append("<li>" + item['DIS_NAME'] + "</li>");
	}
});

yyPriFileWrp.append(ulWrp);

*/