var _viewer = this;
jQuery(".ui-staticText-default").css({"line-height":"5px","height":"auto"});
jQuery(".rh-tmpl-tabel").css({"margin-bottom":"0px"});
jQuery("#OA_GW_TMPL_FW_GL_OLD-GW_ARCV_TIME").css({"border":"0px"});
jQuery("#OA_GW_TMPL_FW_GL_OLD-S_TNAME_div span").css({"line-height":""});

/**
 * 批量打印
 */
var printDaGdInfoObj = _viewer.wfCard._getBtn('printDaGdInfo');
if (printDaGdInfoObj) {
	// 动态装载意见代码
	Load.scriptJS("/oa/zh/batch-print.js");
	new rh.oa.batchPrint({
		"parHandler": _viewer,
		"actObj":printDaGdInfoObj,
		"printPic":true,
		"printAudit":true
	});
}