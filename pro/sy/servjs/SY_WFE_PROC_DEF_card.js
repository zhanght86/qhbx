/** 服务卡片使用的js方法定义：开始 */
var _viewer = this;

/**
 * 点击保存按钮
 */
_viewer.getBtn('saveWf').bind("click",function() {
	saveProcDef("saveWf");
	parent.window.scrollTo(0,0); //进入卡片，外层页面滚动到顶部
});


/**
 * 点击“另存为新版本”按钮
 */
_viewer.getBtn('saveWfAsNewVersion').bind("click",function() {
	if(!_viewer.byIdData._PK_){
		saveProcDef("saveWf");
	}else{
		saveProcDef("saveWfAsNewVersion");
	}
	parent.window.scrollTo(0,0); //进入卡片，外层页面滚动到顶部
});


/**
 * 提交流程
 * @returns
 */
function saveProcDef(act){
	
	if(!_viewer.form.validate()) {
		_viewer.cardBarTipError("校验未通过");
    	return false;
    }
	
	var data = _viewer.form.getItemsValues();
	if(frames[_viewer.opts.sId + "-_WF_IFRAME"] != undefined){
		//取得定义页面上的
		var wfResStr = frames[_viewer.opts.sId + "-_WF_IFRAME"].showWorkflowDefXml();
	    data["xmlStr"] = wfResStr;
	}
	
	data["PROC_TYPE"] = "1";
	
	var result = rh_processData(_viewer.opts.sId + "." + act + ".do",data);
	if (result[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {
		Tip.show("保存成功");
		
		_viewer._parentRefreshFlag = true;
		_viewer._pkCode = result["_DATA_"]["PROC_CODE"];
		_viewer._actVar = UIConst.ACT_CARD_MODIFY;
		
		_viewer.getItem("PROC_CODE").setValue(result["_DATA_"]["PROC_CODE"]);
		
		//“另存为新版本”时，刷新页面
		if(result["_DATA_"]["PROC_VERSION"]){
			_viewer.refresh();
		}
	}
	
	return result;
}


var _oldProcDefFrameHeight = 0;

/**
 * 点击放大按钮
 */
var fullScreenFlag = 0; //当前点击是放大还是缩小按钮操作
_viewer.getBtn("fullScreen").bind("click",function() {
	fullScreenFlag += 1 ;
	var iframeElem = jQuery("#SY_WFE_PROC_DEF-_WF_IFRAME");
	if(iframeElem.hasClass("pa")){
		Menu.expandLeftMenu();
		//缩小
		iframeElem.removeClass();
		_viewer.getBtn("fullScreen").find("span.rh-icon-inner").text("放大");
		//iframeElem.resizable();
		iframeElem.height(_oldProcDefFrameHeight);
	}else{
		_oldProcDefFrameHeight = iframeElem.height();
		//放大
		Menu.closeLeftMenu();
		var pageHeight = jQuery(document).height();
		var siblings = iframeElem.siblings();
		var pos = { top: 80, left: 10 };
		if(siblings.length > 0){
			pos = siblings.offset();
		}
		iframeElem.addClass("pa ");
		iframeElem.offset(pos);
		iframeElem.height(pageHeight - 120);
		_viewer.getBtn("fullScreen").find("span.rh-icon-inner").text("缩小");
	}
});

//增加卡片关闭时的扩展销毁
this.extendDestroy =  function() {
	return false;
};

/**
 * 下面关于中华保险遇到ie8浏览器出现崩溃问题
 * 在查看工作流定义时，打开某个流程定义卡片，在某个节点上点击弹出设置页面之后，不管点击[确定]或是[取消]按钮，不做保存操作，再点击流程定义卡片中的[返回]按钮
 * 会出现<ie8浏览器停止工作>现象，目前解决问题方法是更新浏览器falsh插件，目前测试11.5一下版本会出现这个问题，11.7以上没有重现。
 * 前提是SY_WFE_PROC_DEF_card.js中不使用
 * //增加卡片关闭时的扩展销毁
 *	this.extendDestroy =  function() {
 *		_viewer.destroyIframe("SY_WFE_PROC_DEF-_WF_IFRAME");
 *	};
 * 变量
 */
