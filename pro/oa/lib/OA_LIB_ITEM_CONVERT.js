jQuery(document).ready(function(){
	//文档转换完成，回调方法
	window["callbackAfterConvertDoc"] = function(fileId,result,ifRefresh){
		//记录转换结果
		var data = {"fileId":fileId,"result":result};
		var result = FireFly.doAct("OA_LIB_FILE","convertOk",data,false,true);
		
		var ids = jQuery(document).data("_convertIds");
		
		if(ids && ids.length > 0){
			jQuery(document).data("_convertIsOk",true);
		} else {
			jQuery("#convertDocDiv").dialog( "close" ); // 关闭对话框
			Tools.destroyIframe("redhead-includeJSP"); // 删除IFrame
			jQuery("#convertDocDiv").html(""); 
			jQuery("#convertDocDiv").remove(); //删除div
			if(ifRefresh){
				//刷新页面
				jQuery(document).data("_cardViewer").refresh();
			}
		}
	}
});

/**
 * 
 * @param fileItem fileItem对象
 */
function convertAllFile(fileItem) {
	if (fileItem) {
		var _viewer = jQuery(document).data("_cardViewer");
		var fileDatas = fileItem.getFileData();
		var ids = new Array();
		for ( var key in fileDatas) {
			//_convertFile(key, param);
			ids.push(key);
		}
		_viewer.shield();
		_createConvertDlg();
		
		var fileId = ids[0];
		ids.splice(0,1);		
		jQuery(document).data("_convertIds",ids);
		_convertFile(fileId);
		_loopCheckFileIds();
	}
}

/**
 * 转换单个文件
 * @param fileId 文件ID
 */
function convertSingleFile(fileId,ifRefresh){
	_createConvertDlg();
	_convertFile(fileId,ifRefresh);
}

function _createConvertDlg(){
	var winHtml = new Array();
	winHtml.push("<div style='width:400;height:100;' title='套红头' id='convertDocDiv'>");
	winHtml.push("<iframe style='width:99%;height:99%;border:0px;' id='redhead-includeJSP' src='");
	winHtml.push("' display='none' style='width:1px;height:1px' />");
	winHtml.push("</div>");
	jQuery("body").append(winHtml.join(""));
	winHtml = null;

	jQuery("#convertDocDiv").dialog({
		modal: true,
		width:400,height:400,
		position: { my: "top", at: "top", of: ".form-container" }
	});	
}


/**
 * 转换单个文件成安全文档
 * @param fileId 文件ID
 * @param param 相关参数。包括OA系统地址，印章系统地址。
 */
function _convertFile(fileId,ifRefresh){
	console.log("_convertFile:" + fileId);
	var param = jQuery(document).data("_cardViewer").getByIdData("convertInfo");
	var filePath = param.OA_HOST_URL + "/file/" + fileId;

	var url = param.SEAL_HOST_URL + "/general/file/saveFile.jsp?wyh=" + fileId 
			+ "&file_path=" + filePath
			+ "&retURL=" + param.OA_HOST_URL + "/oa/zh/returnSecureFile.jsp?wyh=" + fileId;
	
	if(ifRefresh && ifRefresh == true){
		url += "&ifRefresh=true";
	} else {
		url += "&ifRefresh=false";
	}
	
	jQuery("#redhead-includeJSP").attr("src",url);
}

/**
 * 循环检测是否多个文件转换完成。
 */
function _loopCheckFileIds(){
	var isOk = jQuery(document).data("_convertIsOk");
	var ids = jQuery(document).data("_convertIds");
	console.log("_loopCheckFileIds:" + isOk + ";" + ids.join(","));
	if(isOk){
		if(ids.length > 0){
			var fileId = ids[0];
			_convertFile(fileId);
			jQuery(document).data("_convertIsOk",false);
			ids.splice(0,1); //删除转换完成的fileId
			jQuery(document).data("_convertIds",ids);
			setTimeout(_loopCheckFileIds,500);
		}
	} else if(ids.length > 0){
		setTimeout(_loopCheckFileIds,500);
	}
}
