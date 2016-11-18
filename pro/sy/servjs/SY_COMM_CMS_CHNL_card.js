var _viewer = this;

var previewBtn = _viewer.getBtn('preview');
previewBtn.click(function(){
	if(!_viewer.form.validate()) {// 校验通过才可预览
		_viewer.cardBarTipError("校验未通过");
		return false;
	}
	
	var servId = _viewer.servId;
	var data = {};
	var url = "/cms/channel/" +_viewer.itemValue("CHNL_ID") + "/index_1.html";
	
	var opts={'scrollFlag':true , 'url':url,'tTitle':'列表预览','menuFlag':3};
	Tab.open(opts);
});

//如果为站点跟栏目,禁止用户修改
var siteRoot = _viewer.getItem('IS_SITE_ROOT').getValue();
if (1 == siteRoot) {
	_viewer.getItem('CHNL_CODE').disabled();
	_viewer.getItem('CHNL_NAME').disabled();
	_viewer.getItem('CHNL_PID').disabled();
}

////显示栏目封面模版继承关系
//var tmplExtends = _viewer.getItem('CHNL_TMPL_EXTENDS').getValue();
//if (1 == tmplExtends) {
//	var value = _viewer.getItem('CHNL_TMPL').getText();
//	_viewer.getItem('CHNL_TMPL').setText("--继承-- " + value);
//}
//
////显示栏目下内容模版继承关系
//var newsTmplExtends = _viewer.getItem('CHNL_CONTENT_TMPL_EXTENDS').getValue();
//if (1 == newsTmplExtends) {
//	var value = _viewer.getItem('CHNL_CONTENT_TMPL').getText();
//	_viewer.getItem('CHNL_CONTENT_TMPL').setText("--继承-- " + value);
//}
//
////显示栏目列表模版继承关系
//var listTmplExtends = _viewer.getItem('CHNL_LIST_TMPL_EXTENDS').getValue();
//if (1 == listTmplExtends) {
//	var value = _viewer.getItem('CHNL_LIST_TMPL').getText();
//	_viewer.getItem('CHNL_LIST_TMPL').setText("--继承-- " + value);
//}
//
////显示栏目下内容评论继承关系
//var commentExtends = _viewer.getItem('CHNL_CONTENT_COMMENT_EXTENDS').getValue();
//if (1 == commentExtends) {
//	var text = _viewer.getItem('CHNL_CONTENT_COMMENT_STATUS').getText();
//	var value = _viewer.getItem('CHNL_CONTENT_COMMENT_STATUS').getValue();
//	 
//	 _viewer.getItem('CHNL_CONTENT_COMMENT_STATUS').obj.find("option[value='" + value + "']").text("--继承-- " + text);
////	_viewer.getItem('CHNL_CONTENT_COMMENT_STATUS').setValue("--继承-- " + value);
//}
   
// 显示栏目下审核级别继承关系
//var tilteTmplExtends = _viewer.getItem('CHNL_TITLE_TMPL_EXTENDS').getValue();
//if (1 == tilteTmplExtends) {
//	var chnlTitleTmplItem = _viewer.getItem('CHNL_TITLE_TMPL');
//	var text = chnlTitleTmplItem.getText();
//	chnlTitleTmplItem.setText("--继承-- " + text);
//}
   
// 显示栏目下审核级别继承关系
//var checkExtends = _viewer.getItem('CHNL_CHECK_EXTENDS').getValue();
//if (1 == checkExtends) {
//	var checkItem = _viewer.getItem('CHNL_CHECK');
// 	 var text = checkItem.getText();
// 	 var value = checkItem.getValue();
// 	checkItem.obj.find("option[value='" + value + "']").text("--继承-- " + text);
//}

//显示栏目下审核人继承关系
//var checkerExtends = _viewer.getItem('CHNL_CHECKER_EXTENDS').getValue();
//if (1 == checkerExtends) {
//	var checkerItem = _viewer.getItem('CHNL_CHECKER');
// 	 var text = checkerItem.getText();
// 	checkerItem.setText("--继承-- " + text);
//}
  
//显示栏目公开范围继承关系
//var chnlScopeExtends = _viewer.getItem('CHNL_SCOPE_EXTENDS').getValue();
//if (1 == chnlScopeExtends) {
//	var chnlScopeItem = _viewer.getItem('CHNL_SCOPE');
//	var text = chnlScopeItem.getText();
//	 var value = chnlScopeItem.getValue();
//	 chnlScopeItem.obj.find("option[value='" + value + "']").text("--继承-- " + text);
//}

//显示栏目公开范围继承关系
//var chnlOwnerExtends = _viewer.getItem('CHNL_OWNER_EXTENDS').getValue();
//if (1 == chnlOwnerExtends) {
//	var chnlOwnerItem = _viewer.getItem('CHNL_OWNER');
// 	 var text = chnlOwnerItem.getText();
// 	chnlOwnerItem.setText("--继承-- " + text);
//}

//对“页面显示模式”为空时自动设值并保存
var disModel = _viewer.getItem("CHNL_TMPL_DISMODEL");

if(!disModel.getValue()){
	disModel.setValue("1");
	_viewer.getBtn("save").click();
}
/*
 * 打开授权页面
 * @param {} aclType 权限定义CODE
 * @param {} dataName 所授权限数据名称，可随意，只是显示在Dialog的左上角
 */
function doDelegate(aclType, dataName) {
	var servId = _viewer.servId;
	var dataId = _viewer._pkCode;

	new rh.ui.Delegate({"servId":servId,"dataId":dataId,"aclType":aclType,"title":dataName + "-授权"}).open();
};

var MANAGE = _viewer.getBtn("SY_COMM_NEWS_CHNL_MANAGE");
if (MANAGE) {
	MANAGE.unbind("click").bind("click",function() {
		doDelegate('SY_COMM_NEWS_CHNL_MANAGE','新闻栏目-管理权限');
	});
}

var POST = _viewer.getBtn("SY_COMM_NEWS_CHNL_POST");
if (POST) {
	POST.unbind("click").bind("click",function() {
		doDelegate('SY_COMM_NEWS_CHNL_POST','新闻栏目-发布权限');
	});
}

var VIEW = _viewer.getBtn("SY_COMM_NEWS_CHNLVIEW");
if (VIEW) {
	VIEW.unbind("click").bind("click",function() {
		doDelegate('SY_COMM_NEWS_CHNLVIEW','新闻栏目-查看权限');
	});
}
//
//var CHECK = _viewer.getBtn("SY_COMM_NEWS_CHNL_CHECK");
//if (CHECK) {
//	CHECK.unbind("click").bind("click",function() {
//		doDelegate('SY_COMM_NEWS_CHNL_CHECK','新闻栏目-审核权限');
//	});
//}

// 栏目审核级别控制审核人是否必填
var chnlCheck = _viewer.getItem("CHNL_CHECK");
var chnlChecker = _viewer.getItem("CHNL_CHECKER");
if (chnlCheck) {
	chnlCheck.obj.change(function(){
		// 简单审核时审核人必须输入
		chnlCheck.setOtherItemNotNull([1], chnlChecker, true);
		// 并且可填
		chnlCheck.setOtherItemEnabled([1], chnlChecker);
	});
}

// 栏目公开范围控制可查看人是否必填
var chnlScope = _viewer.getItem("CHNL_SCOPE");
var chnlOwner = _viewer.getItem("CHNL_OWNER");
if (chnlScope) {
	chnlScope.obj.change(function(){
		// 简单审核时审核人必须输入并且可填
		chnlScope.setOtherItemNotNull([4,5], chnlOwner, true);
		chnlScope.setOtherItemEnabled([4,5], chnlOwner);
	});
}


