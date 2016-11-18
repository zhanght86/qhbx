var _viewer = this;
//// 构建审批单头部
//var gwHeader = new rh.vi.gwHeader({
//	"servId" : _viewer.opts.sId,
//	"parHandler" : _viewer
//});
//gwHeader.createHeader();
//
//if(_viewer.form.getItem("GW_YEAR_CODE")){
//	var container = _viewer.form.getItem("GW_YEAR_CODE").obj.parent();
//	container.append(gwHeader.createYearCodeItem());
//	container.find(".gw-year-code").css("width","150px");
//	container.css("padding-top" ,"6px").css("padding-left","3px");
//}
//
//// 如果存在主键，则可以构建文件编号框
//if (_viewer._pkCode) {
//
//	var gwExtCard = new rh.vi.gwExtCardView({
//		"parHandler" : _viewer
//	});
//	gwExtCard.init();
//}


(function(_viewer){
	
//	init();
	//说明字段
	//var memo = FireFly.doAct("OA_GW_TMPL","finds",{"_WHERE_":" and TMPL_CODE = '"+_viewer.servId+"'","_SELECT_":"MEMO,DISPLAY_MEMO"}) || "";
	//如果存在说明
	//if ("" != memo) {
		//如果说明字段启用
		//if (memo["_DATA_"][0]["DISPLAY_MEMO"] == "1") {
			//reSetSendTitle(memo);
		//} else {
			//_viewer.form.getItem("SEND_TITLE").getContainer().remove();
		//}
	//}
	function init() {
		
		// 配套制度文号表单
		var GW_YEAR_CODE = _viewer.getItem("GW_YEAR_CODE");
		
		// 把紧急程度放到右上角
		var width = _viewer.form.getItemWidth(_viewer.form.cols, _viewer.form.cols);
		width = {"itemWidth":width.ITEM_WIDTH, "leftWidth":width.LEFT_WIDTH, "rightWidth":width.RIGHT_WIDTH, "maxWidth":width.MAX_WIDTH};
		var emergencyItem = _viewer.getItem("S_EMERGENCY");
		var container = emergencyItem.getContainer()
		container.width("130px");
		container.css({"float":"right","padding-bottom":"30px"});
		container.find(".left").width("70px");
		container.find(".right").width("60px");
		var item = new rh.ui.Item(width);
		item.addLabel(jQuery("<div>&nbsp;</div>"));
		
		var gwNum = GW_YEAR_CODE.getContainer();
		gwNum.css({"width":"410px","margin":"0 auto"});
		gwNum.removeClass("inner");
		gwNum.find(".left").width("63px");
		gwNum.find(".right").width("250px");
		var divCon = jQuery("<div/>").append(gwNum).append(container);
		item.addContent(divCon);
		jQuery(".inner").first().before(item.getObj());
		jQuery(".item").first().css({"padding-top":"0"});
		
		_viewer.resetSize();
	}
	
	//重载传阅参数
	if (_viewer.wfCard != null) {
		_viewer.wfCard.beforeCmSimpleFenFa = function(actItem,params){
			params["userSelectDict"] = "SY_ORG_DEPT_USER";
			params["displaySendSchm"] = true;
			params["includeSubOdept"] = false;
			return true;
		}
	}
	
	//替换正文文件中的标题书签
	var fileItem = _viewer.getItem("ZHENGWEN");
	if(fileItem){
		fileItem.beforeEditFile = function(){
			window["_replaceBookmark"] = {"title":_viewer.itemValue("GW_TITLE")};
			fileItem.replaceBookmark = "_replaceBookmark";
			return true;
		}
	}
})(this);

/**
 * 重置卡片提示字段信息
 * @author hdy
 */
/**function reSetSendTitle(memo){
	//如果说明不为空
	var memoVal = memo["_DATA_"][0]["MEMO"] || "暂无说明";
	//去除html标签
	memoVal = memoVal.replace(/<[^>]+>/g,"");
	//为提示框设置值
	_viewer.getItem("SEND_TITLE").setValue(memoVal);
	//设置样式
	var itemObj= _viewer.form.getItem("SEND_TITLE").getContainer();
	itemObj.find(".blank").addClass("disabled");
	itemObj.find(".name").html("");
	_viewer.form.getItem("SEND_TITLE").obj.addClass("disabled").attr("readonly",true);	
}*/
