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
	var url = "/cms/SY_COMM_NEWS/" +_viewer.itemValue("NEWS_ID") + ".html";
	//window.open(url);
	
	var opts={'scrollFlag':true , 'url':url,'tTitle':'内容预览','menuFlag':3};
	Tab.open(opts);
	
});

//显示栏目下内容模版继承关系
var newsTmpl = _viewer.getItem('TMPL_EXTENDS').getValue();
// if(1 == newsTmpl) {
//	 var value = _viewer.getItem('TMPL_NAME').getValue();
//	_viewer.getItem('TMPL_NAME').setValue("--继承-- " + value);
//}

 if(1 == newsTmpl) {
	 var value = _viewer.getItem('TMPL_ID').getText();
	_viewer.getItem('TMPL_ID').setText("--继承-- " + value);
}
 
//显示栏内容评论继承关系
 var commentExtends = _viewer.getItem('NEWS_COMMENT_EXTENDS').getValue();
  if(1 == commentExtends) {
 	 var text = _viewer.getItem('NEWS_COMMENT_STATUS').getText();
	 var value = _viewer.getItem('NEWS_COMMENT_STATUS').getValue();
 	 
 	 _viewer.getItem('NEWS_COMMENT_STATUS').obj.find("option[value='" + value + "']").text("--继承-- " + text);
// 	_viewer.getItem('CHNL_NEWS_COMMENT_STATUS').setValue("--继承-- " + value);
 }
  
//新闻审核状态不存在，自动设置为“已审核”（对以前的数据补充）
(function autoChecked(){
	var newsChecked = _viewer.getItem("NEWS_CHECKED");
	if(!newsChecked.getValue()){
		newsChecked.setValue("2");
		//_viewer.getBtn("save").click();
	}
})();

//栏目审核级别控制审核人是否必填
var newsCheck = _viewer.getItem("NEWS_CHECK");
var newsChecker = _viewer.getItem("NEWS_CHECKER");
if (newsCheck) {
	newsCheck.obj.change(function(){
		// 简单审核时审核人必须输入
		newsCheck.setOtherItemNotNull([1], newsChecker, true);
		// 并且可填
		newsCheck.setOtherItemEnabled([1], newsChecker);
	});
}

// 栏目公开范围控制可查看人是否必填
var newsScope = _viewer.getItem("NEWS_SCOPE");
var newsOwner = _viewer.getItem("NEWS_OWNER");
if (newsScope) {
	newsScope.obj.change(function(){
		// 简单审核时审核人必须输入并且可填
		newsScope.setOtherItemNotNull([4,5], newsOwner, true);
		newsScope.setOtherItemEnabled([4,5], newsOwner);
	});
}

// 消息公开范围继承
//var newsScopeExtends = _viewer.getItem('NEWS_SCOPE_EXTENDS').getValue();
//if (1 == newsScopeExtends) {
//	var text = _viewer.getItem('NEWS_SCOPE').getText();
//	var value = _viewer.getItem('NEWS_SCOPE').getValue();
//	_viewer.getItem('NEWS_SCOPE').obj.find("option[value='" + value + "']").text("--继承-- " + text);
//}
 
//消息可查看人继承
//var newsOwnerExtends = _viewer.getItem('NEWS_OWNER_EXTENDS').getValue();
//if (1 == newsOwnerExtends) {
//	var text = _viewer.getItem('NEWS_OWNER').getText();
//	var value = _viewer.getItem('NEWS_OWNER').getValue();
//	_viewer.getItem('NEWS_OWNER').obj.find("option[value='" + value + "']").text("--继承-- " + text);
//}
	
//消息审核类型继承
//var newsCheckExtends = _viewer.getItem('NEWS_CHECK_EXTENDS').getValue();
//if (1 == newsCheckExtends) {
//	var text = _viewer.getItem('NEWS_CHECK').getText();
//	var value = _viewer.getItem('NEWS_CHECK').getValue();
//	_viewer.getItem('NEWS_CHECK').obj.find("option[value='" + value + "']").text("--继承-- " + text);
//}

//消息审核人继承
//var newsCheckerExtends = _viewer.getItem('NEWS_CHECKER_EXTENDS').getValue();
//if (1 == newsCheckerExtends) {
//	var text = _viewer.getItem('NEWS_CHECKER').getText();
//	var value = _viewer.getItem('NEWS_CHECKER').getValue();
//	_viewer.getItem('NEWS_CHECKER').obj.find("option[value='" + value + "']").text("--继承-- " + text);
//}

