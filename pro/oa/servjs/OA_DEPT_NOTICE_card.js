var _viewer = this;

//获取卡片紧急程度字段对象
//var sEmergency = _viewer.form.getItem("S_EMERGENCY").getContainer();
//sEmergency.css({"max-width": "270px","float":"right","margin-top": "-5px","margin-bottom": "50px"});
//sEmergency.find(".left").css({"width":"70%"});
//sEmergency.find(".right").css({"width":"30%"});

//设置主送抄送的名称
try{
	var mainToCodeItem = _viewer.form.getItem("NOTICE_ZS_DEPT");
	if(mainToCodeItem && mainToCodeItem.type=='DictChoose' && !mainToCodeItem.isHidden){
		mainToCodeItem.setText(_viewer.form.getItem("NOTICE_ZS_DEPT_NAME").getValue());
	}
	//设置抄送显示
	var mainCopyCodeItem = _viewer.form.getItem("NOTICE_CS_DEPT");
	if(mainCopyCodeItem && mainCopyCodeItem.type=='DictChoose' && !mainCopyCodeItem.isHidden){
		mainCopyCodeItem.setText(_viewer.form.getItem("NOTICE_CS_DEPT_NAME").getValue());
	}
}catch(e){
	console.error("OA_DEPT_NOTICE_card.js:" + e.message);
}
//备注显示样式
var memoTitle = _viewer.form.getItem("MEMO_TITLE");

memoTitle.obj.parent().removeClass("disabled").css({"background":"url('')"});
memoTitle.obj.css({"color":"red","background-color":"white","font-size":"12px"});
memoTitle.getContainer().css({"width":"75%"});

//重载发送、传阅参数
if (_viewer.wfCard != null) {
	_viewer.wfCard.beforeFenfa = function(sendObj){
		sendObj["includeSubOdept"] = false;
		return true;
	};
	_viewer.wfCard.beforeCmSimpleFenFa = function(actItem,params){
		params["userSelectDict"] = "SY_ORG_DEPT_USER";
		params["displaySendSchm"] = true;
		params["includeSubOdept"] = false;
		return true;
	};
}

//替换正文文件中的标题书签
var fileItem = _viewer.getItem("ZHENGWEN");
if(fileItem){
	fileItem.beforeEditFile = function(){
		window["_replaceBookmark"] = {"title":_viewer.itemValue("NOTICE_TITLE")};
		fileItem.replaceBookmark = "_replaceBookmark";
		return true;
	}
}

//重置紧急程度高度
_viewer.form.getItem("S_EMERGENCY").getContainer().css({"top":"0px"});
//顶层元素重置高度
_viewer.form.getItem("S_EMERGENCY").getContainer().parent().css({"margin-top":"0px"});
