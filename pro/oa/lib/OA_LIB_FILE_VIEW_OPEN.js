/*******************************************************************************
 * 停止事件冒泡
 ******************************************************************************/
function stopBubble(e) {
	if (e && e.stopPropagation) {
		e.stopPropagation()
	} else {
		window.event.cancelBubble = true
	}
}

/**
 * 打开文件
 * 
 * @param obj
 */
function openLibFile(obj) {
	stopBubble(event);
	var _viewer = jQuery(document).data("_viewer");
	var readType = _viewer.grid.getRowItemValueByElement(jQuery(obj),"READ_TYPE");
	
	var fileList = queryFileList(obj);

	if (fileList) {
		if (fileList.length == 1) { //只有一个文件，直接打开
			RHWindow.openWindow(fileList[0].ACCESS_URL);
		} else if (fileList.length > 1) { //有多个文件，显示列表
			openFileListDlg(obj,fileList);
		}
	}
}

/**
 * 实现文件列表对话框
 * @param obj 被点击对象
 * @param fileList 文件列表
 * @returns {Boolean}
 */
function openFileListDlg(obj,fileList) {
	var htmlArr = new Array();
	htmlArr.push("<div id='chengWenTmplListDiv' style='background-color:#FFFFEE' title='文件列表'>");
	htmlArr.push("<div class='ml20 mt20'>");
	htmlArr.push("<ul>");
	for(var i=0;i<fileList.length;i++){
		htmlArr.push("<li class='h25'>");
		var url = fileList[i].ACCESS_URL;
		if(url.length > 0){
			htmlArr.push("<a href='" + url + "' target='_blank'>");
			htmlArr.push("文件");
			htmlArr.push(i+1);
			htmlArr.push("</a>");
			htmlArr.push("</li>");
		}else{
			htmlArr.push("文件");
			htmlArr.push(i+1);
			htmlArr.push(" (文档转换中，请稍候……)");
		}
	}
	htmlArr.push("</ul>");
	htmlArr.push("</div></div>");
	var winDialog = jQuery(htmlArr.join(""));

	htmlArr = null;
	
	winDialog.appendTo(jQuery("body"));

	jQuery("#chengWenTmplListDiv").dialog({
		autoOpen : true,
		width : 200,
		//height : 100,
		modal : false,
		resizable : true,
		position : {
			my : "left top",
			at : "left bottom",
			of : jQuery(obj)
		},
		close : function() {
			jQuery(this).remove();
		}
	});

	jQuery(document).click(function() {
		var dlgObj = jQuery("#chengWenTmplListDiv");
		if (dlgObj.length > 0 && dlgObj.dialog("isOpen")) {
			dlgObj.dialog("destroy");
			dlgObj.remove();
		}
	});

	jQuery("#chengWenTmplListDiv").click(function() {
		stopBubble(event);
	});

	return false;
}

/**
 * 获取文件列表
 */
function queryFileList(obj) {
	var _viewer = jQuery(document).data("_viewer");
	var itemId = _viewer.grid.getRowPkByElement(jQuery(obj)); 
	var datas = {};
	datas["_NOPAGE_"] = true;
	datas["_searchWhere"] = " and ITEM_ID ='" + itemId + "'";
	
	var listDatas = FireFly.getListData("OA_LIB_FILE",datas);
	
	return listDatas._DATA_;
}
