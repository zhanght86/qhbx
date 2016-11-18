_gongwenViewer = this;

//读取工作流上按钮的json配置
var createRelatedServOpt = arguments[0];
var selectServs = createRelatedServOpt.SELECT_SERVS;

//初始化文件信息
var files = _gongwenViewer.form.getAttachFiles();//File数组

//构造列表数据
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

//弹出容器
getDialog(event,"OA_CREATE_RELATED_SERV-dial","创建相关服务",800);

/**上-卡片版*/
var temp = {	
	"sId":"OA_CREATE_RELATED_SERV",
	"act":UIConst.ACT_CARD_ADD,
	"pCon":jQuery("#OA_CREATE_RELATED_SERV-dial"),
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
jQuery("#OA_CREATE_RELATED_SERV-winTabs").css({"min-height":"initial"});
jQuery("#OA_CREATE_RELATED_SERV-dial").find(".rhCard-backLi").remove();
jQuery("#OA_CREATE_RELATED_SERV-dial").find(".rhCard-refreshLi").remove();

//需要多个服务中选择时显示
if(selectServs.length == 0){
	alert("无法获得相关服务信息");
} else {
	var relatedServsItem = cardView.getItem("RELATED_SERVS");
	if(selectServs.length == 1){
		relatedServsItem.setValue(selectServs[0].CODE,selectServs[0].NAME);
		relatedServsItem.obj.children('option:selected').attr("suffix", (selectServs[0].TYPE || ""));
		relatedServsItem.disabled();
	} else {
		var optHtml = "<option value=''></option>";
		jQuery.each(selectServs,function(i,n){
			optHtml = optHtml + "<option value='" + 
				selectServs[i].CODE + "' suffix='" + 
				(selectServs[i].TYPE || "") + "'>" + 
					selectServs[i].NAME + 
				"</option>";
		});
		relatedServsItem.obj.html(optHtml);
	}
	relatedServsItem.obj.change(function(){
        var sel = jQuery(this).children('option:selected');
        var code = sel.val();
        var name = sel.text();
        var suffixs = sel.attr("suffix") || "";
        filterFile(code,name,suffixs);
    });
}

//过滤不符合配置格式的文件（uncheck 并且 disable）
function filterFile(code,name,suffixs){
	if(grid){
		var warnStr = "不符合" + name + "的规定格式";
		var fileNameTds = grid.getTdItems("FILE_NAME");
		jQuery.each(fileNameTds, function(i,n){
			var trObj = jQuery(this).parent()
			var chkObj = jQuery("input[type='checkbox']", trObj);
			//配置了文件类型
			if (suffixs.length > 0 || code.length == 0) {
				var fileSuffix = "*." + Tools.getFileSuffix(jQuery(this).text()) + ",";
				if((suffixs + ",").indexOf(fileSuffix) >= 0){ //匹配的类型
					//启用CHECKBOX
					unDisableChk(chkObj);
					//选中CHECKBOX
					check(chkObj);
					//去掉行提示
					removeTitle(chkObj);
				} else { //未匹配
					//禁用CHECKBOX
					disableChk(chkObj);
					//反选CHECKBOX
					unCheck(chkObj);
					//添加行提示
					addTitle(trObj, warnStr);
				}
			} else { //没配置默认全启用
				//启用CHECKBOX
				unDisableChk(chkObj);
				//选中CHECKBOX
				check(chkObj);
				//去掉行提示
				removeTitle(trObj);
			}
		});
	}
}

//去掉行提示
function removeTitle(trObj){
	trObj.removeAttr("title");
}

//添加行提示
function addTitle(trObj,str){
	trObj.attr("title", str)
}

//选取多选框
function check(jObj,unChkFlag){
	jObj.attr('checked',true);
}

//反选多选框
function unCheck(jObj,unChkFlag){
	jObj.attr('checked',false);
}

//生效多选框
function unDisableChk(chkObj){
	chkObj.removeAttr("disabled");
}

//失效多选框
function disableChk(chkObj){
	chkObj.attr("disabled","disabled");
}

//"开始创建"按钮事件
cardView.getBtn("save").unbind("click").bind("click",function(event){
	//校验
	var newServItem = cardView.getItem("RELATED_SERVS");
	var newServId = newServItem.getValue();
	var newServName = newServItem.getText();
	if(grid){
		var fileIds = grid.getSelectPKCodes().join();
		if (fileIds == "") {
			alert("请选择文件");
			return false;
		}
	}
	if (newServId == "") {
		alert("请选择相关服务");
		return false;
	}
	
	var param = {};
	param["oldServId"] = _gongwenViewer.servId;
	param["oldDataId"] = _gongwenViewer.getPKCode();
	param["newServId"] = newServId;
	if(fileIds){
		param["_fileIds"] = fileIds;
	}
	cardView.shield();
	var res = FireFly.doAct("SY_WFE_PROC_DEF", "createRelatedServ", param, true, false);
	cardView.shieldHide();
	if (res[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) >= 0) {
		jQuery("#OA_CREATE_RELATED_SERV-dial").remove();
		if(confirm("是否进入新的审批单？")){
			var data = res;
			var options = {"url":data.serv + ".card.do?pkCode=" + data._PK_, 
				"tTitle":newServName, "menuFlag":4,"params":{}
			};
			Tab.open(options);
			_gongwenViewer._preTabHandlerRefresh = false;
			_gongwenViewer.backClick();
		}else{
			_gongwenViewer.refresh();
		}
	} else {
		alert("创建失败：" + res[UIConst.RTN_MSG]);
	}
});

//构造文件列表
if(createRelatedServOpt.SELECT_FILE_FLAG){
	/**下-列表版*/
	var fileListCon = jQuery("<div id='FILE_LIST_CONTAINER'></div>").appendTo(jQuery("#OA_CREATE_RELATED_SERV-dial"));//文件列表容器
//	//头部：名称信息条等-开始
//		var listHeader = jQuery("<div></div>").addClass("conHeader").appendTo(fileListCon);
//		var titleDiv = jQuery("<div></div>").addClass("conHeaderTitle").addClass("rh-right-radius-head");
//		titleDiv.appendTo(listHeader);
//		jQuery("<span></span>").addClass("conHeaderTitle-span rh-slide-flagYellow").text("子文件列表").appendTo(titleDiv);
//	//头部：名称信息条等-结束
	var param = {
		"id":"OA_CREATE_RELATED_SERV_LIST",
		"pid":"",
		"mainData":FireFly.getCache("OA_CREATE_RELATED_SERV_LIST",FireFly.servMainData),
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
	if(relatedServsItem){
		relatedServsItem.obj.change();
	}
}

//动态执行加载公文编码在ie9下的css，此处需动态加载一次，不然select元素只出现一个汉字，显示不全
relatedServsItem.obj.width(relatedServsItem.obj.width());

/**内部函数--构建弹出框页面布局*/
function getDialog(event,dialogId,title,wid,hei) {
	//设置jqueryUi的dialog参数
	if(title == null){
		title = ""
	}
	var winDialog = jQuery("<div></div>").addClass("selectDialog").attr("id",dialogId).attr("title",title);
	winDialog.appendTo(jQuery("body"));
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

function checkListValid(){
	return false
}