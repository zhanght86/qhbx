var _viewer = this;
var previewBtn = _viewer.getBtn('preview');
//预览
previewBtn.unbind("click").click(function(){
	if(!_viewer.form.validate()) {// 校验通过才可预览
		_viewer.cardBarTipError("校验未通过");
		return false;
	}

	var servId = _viewer.servId;
	var data = {};
	var templId = _viewer.getItem('TMPL_ID').getValue();
	var siteId = _viewer.itemValue("SITE_ID");
	if (!templId || !siteId) {
		alert("请配置模版!");
	}
//	var url = "/cms/site/" +_viewer.itemValue("SITE_ID") + "/";
	var url = "/sy/comm/page/SY_COMM_TEMPL.show.do?model=view&pkCode=" + templId + "&$SITE_ID$=" + siteId;
	
	var opts={'scrollFlag':true , 'url':url,'tTitle':'站点预览','menuFlag':3};
	Tab.open(opts);
	
//	FireFly.doAct(servId, "preview", data, true, true, function(result){
////		var OpenWindow = window.open("about:blank");
////		OpenWindow.document.write(result.HTML);
//		var url = "/news/content/" + _viewer.itemValue("NEWS_ID") + "/";
//		window.open(url);
//	});
});

