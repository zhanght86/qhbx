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
	var url = "/cms/SY_COMM_BBS_TOPIC/" +_viewer.itemValue("TOPIC_ID") + "/";
	//window.open(url);
	
	var opts={'scrollFlag':true , 'url':url,'tTitle':'内容预览','menuFlag':3};
	Tab.open(opts);
});


//显示栏目下内容模版继承关系
var tmpl = _viewer.getItem('TMPL_EXTENDS').getValue();
// if(1 == tmpl) {
//	 var value = _viewer.getItem('TMPL_NAME').getValue();
//	_viewer.getItem('TMPL_NAME').setValue("--继承-- " + value);
//}
 if(1 == tmpl) {
	 var value = _viewer.getItem('TMPL_ID').getText();
	_viewer.getItem('TMPL_ID').setText("--继承-- " + value);
}
 
//显示栏内容评论继承关系
 /*
 var commentExtends = _viewer.getItem('TOPIC_COMMENT_EXTENDS').getValue();
  if(1 == commentExtends) {
 	 var text = _viewer.getItem('TOPIC_COMMENT_STATUS').getText();
	 var value = _viewer.getItem('TOPIC_COMMENT_STATUS').getValue();
 	 
 	 _viewer.getItem('TOPIC_COMMENT_STATUS').obj.find("option[value='" + value + "']").text("--继承-- " + text);
// 	_viewer.getItem('CHNL_NEWS_COMMENT_STATUS').setValue("--继承-- " + value);
 }
  */
