var _viewer = this;

//设置主送抄送的名称
try{
	var mainToCodeItem = _viewer.form.getItem("CO_TO");
	if(mainToCodeItem && mainToCodeItem.type=='DictChoose' && !mainToCodeItem.isHidden){
		mainToCodeItem.setText(_viewer.form.getItem("CO_TO_NAME").getValue());
	}
	
}catch(e){
	console.error("OA_CN_CONCERT_card.js:" + e.message);
}

//获取byId中是控股公司的配置数据数据
var getThisByIdData = _viewer.getByIdData("MODIFIY_RED_TITLE");
if ((getThisByIdData || "").length > 0) {
	jQuery("div[class='redHeader']").html(getThisByIdData);
}
//备注显示样式
_viewer.form.getItem("MEMO_TITLE").obj.parent().removeClass("disabled").css({"background":"url('')"});
_viewer.form.getItem("MEMO_TITLE").obj.css({"color":"black","font-weight":"bolder","background-color":"white","font-size":"14px"});

//重载传阅参数
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
		window["_replaceBookmark"] = {"title":_viewer.itemValue("CO_TITLE")};
		fileItem.replaceBookmark = "_replaceBookmark";
		return true;
	}
}