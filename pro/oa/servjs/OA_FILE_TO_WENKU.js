_gongwenViewer = this;

/**初始化文件信息*/
var files = _gongwenViewer.form.getAttachFiles();//File数组

/**构造列表数据*/
var dataList = {
	"_OKCOUNT_":5, 
	"_PAGE_":{"ALLNUM":5, "SHOWNUM":50, "NOWPAGE":1, "PAGES":1}, 	
	"_DATA_":[],
	"_COLS_":{
		"FILE_ID":{"ITEM_NAME":"文件ID","ITEM_CODE":"FILE_ID","ITEM_LIST_FLAG":2},
		"FILE_NAME":{"ITEM_NAME":"文件名称","ITEM_CODE":"FILE_NAME","ITEM_LIST_FLAG":1},
		"DIS_NAME":{"ITEM_NAME":"显示名称","ITEM_CODE":"DIS_NAME","ITEM_LIST_FLAG":1},
		"FILE_SIZE":{"ITEM_NAME":"文件大小(B)","ITEM_CODE":"FILE_SIZE","ITEM_LIST_FLAG":1},
		"FILE_CAT":{"ITEM_NAME":"分类ID","ITEM_CODE":"FILE_CAT","ITEM_LIST_FLAG":2},
		"FILE_CAT_NAME":{"ITEM_NAME":"分类","ITEM_CODE":"FILE_CAT_NAME","ITEM_LIST_FLAG":1}
	}
}
jQuery.each(files,function(i,n) {
	jQuery.each(n.getFileData(),function(j,m) {
		var bean = {"_PK_":m.FILE_ID,"FILE_NAME":m.FILE_NAME,"FILE_CAT":m.FILE_CAT,"FILE_CAT_NAME":n.getItemName(),"DIS_NAME":m.DIS_NAME,"ROWNUM_":m._ROWNUM_+1,"_ROWNUM_":m._ROWNUM_,"FILE_ID":m.FILE_ID,"FILE_SIZE":m.FILE_SIZE}
		dataList._DATA_.push(bean);
	});
});

/**弹出容器*/
getDialog(event,"file_to_wenku-dial","文件同步列表",800,600);

/**上-卡片版*/
var temp = {	
	"sId":"OA_FILE_TO_WENKU",
	"act":UIConst.ACT_CARD_ADD,
	"pCon":jQuery("#file_to_wenku-dial"),
	"_PK_":"",
	"parHandler":_gongwenViewer,
	"widHeiArray":[0,0],
	"xyArray":[0,0],
	"links":_gongwenViewer.opts.links,
	"params":{}
};
var cardView = new rh.vi.cardView(temp);
cardView.show();
cardView.getItem("MAIN_DATA_ID").setValue(_gongwenViewer.getPKCode());
jQuery("#OA_FILE_TO_WENKU-winTabs").css({"min-height":"auto"});
jQuery("#file_to_wenku-dial").find(".rhCard-backLi").remove();
jQuery("#file_to_wenku-dial").find(".rhCard-refreshLi").remove();

/**下-列表版*/
var fileListCon = jQuery("<div id='FILE_LIST_CONTAINER'></div>").appendTo(jQuery("#file_to_wenku-dial"));//文件列表容器
//头部：名称信息条等-开始
	var listHeader = jQuery("<div></div>").addClass("conHeader").appendTo(fileListCon);
	var titleDiv = jQuery("<div></div>").addClass("conHeaderTitle").addClass("rh-right-radius-head");
	titleDiv.appendTo(listHeader);
	jQuery("<span></span>").addClass("conHeaderTitle-span rh-slide-flagYellow").text("子文件列表").appendTo(titleDiv);
//头部：名称信息条等-结束
var param = {
	"id":"OA_FILE_TO_WENKU_LIST",
	"pid":"",
	"mainData":FireFly.getCache("OA_FILE_TO_WENKU_LIST",FireFly.servMainData),
	"rowBtns":new Array(),
	"type":null,
	"pkHide":false,
	"parHandler":_gongwenViewer,
	"pCon":fileListCon,
	"byIdFlag":"false",
	"batchFlag":"false",
	"sortGridFlag":"false",
	"buildPageFlag":"false",
	"listData":dataList
};
var grid = new rh.ui.grid(param);
grid.render();

/**内部函数--构建弹出框页面布局*/
function getDialog(event,dialogId,title,wid,hei) {
	//设置jqueryUi的dialog参数
	if(title == null){
		title = ""
	}
	var winDialog = jQuery("<div></div>").addClass("selectDialog").attr("id",dialogId).attr("title",title);
	winDialog.appendTo(jQuery("body"));
	if(hei == null || wid == null || hei == "" || wid == ""){
		wid = jQuery("body").width() - 500;
		hei = GLOBAL.getDefaultFrameHei()-550;	
	}
	var posArray = [30,30];
	if (event) {
		var cy = event.clientY;
	    posArray[0] = "";
	    posArray[1] = cy-300;
	}
  
  //生成jqueryUi的dialog
	jQuery("#" + dialogId).dialog({
		autoOpen: false,
		height: hei,
		width: wid,
		modal: true,
		resizable:false,
		position:posArray,
		open: function() { 

		},
		close: function() {
			jQuery("#" + dialogId).remove();
			_gongwenViewer.refresh();
			jQuery(cardView).remove();
			jQuery(fileListCon).remove();
			jQuery(grid).remove();
		}
	});
	
	//手动打开dialog
	var dialogObj = jQuery("#" + dialogId);
	dialogObj.dialog("open");
	dialogObj.focus();
  jQuery(".ui-dialog-titlebar").last().css("display","block");//设置标题显示
  dialogObj.parent().addClass("rh-bottom-right-radius rhSelectWidget-content");
  Tip.show("努力加载中...",null,jQuery(".ui-dialog-title",winDialog).last());
}

/**同步按钮事件*/
cardView.getBtn("synToWenku").unbind("click").bind("click",function(event){
	if(!cardView.form.validate()) {
		cardView.cardBarTipError("校验未通过");
    	return;
    }
//	if(!checkListValid()){showRHDialog("同步失败提示","请。。。",null,null,[100,200]);return;}
	var param = {};
	param["DOCUMENT_CHNL"] = cardView.itemValue("TOWENKU_TYPE");
	param["DOCUMENT_TITLE"] = cardView.itemValue("TOWENKU_TITLE");
	param["MAIN_SERV_ID"] = _gongwenViewer.servId;
	param["MAIN_DATA_ID"] = _gongwenViewer.getPKCode();
	param["DOCUMENT_MAIN_ID"] = cardView.itemValue("MAIN_DOC_ID");
	param["DOCUMENT_AUX_ID"] = grid.getSelectPKCodes().join();
	cardView.shield();
	var res = FireFly.doAct("OA_FILE_TO_WENKU", "fileToWenku", param, true, false);
	cardView.shieldHide();
	if (res[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {
		jQuery("#file_to_wenku-dial").remove();
	}	
});

function checkListValid(){
	return false
}

//其他服务调用同步文件到文库服务需要添加的前台代码(公文需要记录文档栏目的请添加字段DOCUMENT_CHNL)
/**************************************************************************************************
if (_viewer.wfCard) {
	_viewer.wfCard._getBtn("fileToWenku").layoutObj.unbind("click").bind("click",function(event){
		var jsFileUrl = FireFly.getContextPath() + "/oa/servjs/OA_FILE_TO_WENKU.js";
	    jQuery.ajax({
	        url: jsFileUrl,
	        type: "GET",
	        dataType: "text",
	        async: false,
	        data: {},
	        success: function(data){
	            try {
	                var servExt = new Function(data);
	                servExt.apply(_viewer);
	            } catch(e){
	            	throw e;
	            }
	        },
	        error: function(){;}
	    });
	});
}
**************************************************************************************************/