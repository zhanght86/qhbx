GLOBAL.namespace("rh.vi");

/**
 * OA系统与中华保险项目的吉大正源印章系统接口实现
 * 
 * @param cardViewer 卡片页面对象
 * @returns
 */
rh.vi.rhGwSeal = function(cardViewer) {
	this.viewer = cardViewer;
}

/**
 * 对指定文件盖章
 * @sealParams 从服务器端获取的盖章文件信息
 */
rh.vi.rhGwSeal.prototype.seal = function(sealParams) {
	
	if (sealParams._MSG_ != undefined
			&& StringUtils.startWith(sealParams._MSG_, "ERROR,")) {
		throw ({"message":sealParams._MSG_});
	} else if (sealParams.ODEPT_NUM == "") {
		throw ({"message":"机构编号不能为空，请检查机构编号或与管理员联系。"});
	} else if (sealParams.SEAL_CODE == undefined || sealParams.SEAL_CODE == "") {
		throw ({"message":"机关代字对应的印章不能为空，请先设置代字对应的印章。"});
	}
	
	var oaHost = sealParams.SYS_HOST_ADDR;

	var urlStr = sealParams.SEAL_SYS_HOST
			+ "/general/modules/wendang/anonDoc/sealShow.jsp" + "?sealIds="
			+ sealParams.SEAL_CODE + "&userName=" + sealParams.USER_LOGIN_NAME
			+ "&sealFilePath=" + oaHost + sealParams.FILE_PATH + "&printNum="
			+ sealParams.DEFAULT_PRINT_NUM + "&wyh=" + sealParams.sealFileId
			+ "&rtnUrl=" + oaHost + "/oa/zh/returnSealFile.jsp"
			+ "&sealFileName=" + (this.viewer.getByIdData("GW_TITLE"))
			+ "&printDeptId=" + sealParams.ODEPT_NUM
			+ "&wh=" + (sealParams.APP_CODE)
			+ "&sealDescribe=" + (sealParams.SEAL_NAME);

	jQuery(document).data("parHandler", this.viewer);
	window.callbackAfterSeal = function() {
		var parHandler = jQuery(document).data("parHandler");
		try {
			parHandler.shield();
			parHandler.refresh();
		} catch (e) {

		} finally {
			parHandler.shieldHide();
		}
	};

	window.open(urlStr);
}

/**
 * 取消印章
 */
rh.vi.rhGwSeal.prototype.undoSeal = function(params) {
	var self = this;

	var servId = self.viewer.servId;

	var reqdata = {};
	reqdata.PK_CODE = this.viewer._pkCode;
	reqdata.TMP_CODE = servId;
	try {
		self.viewer.shield();
		jQuery.extend(reqdata,params); //合并参数
		var result = FireFly.doAct(servId, "undoSeal", reqdata);
		if (result.rtnMsg == "succ") {
			self.viewer.refresh();
			alert("取消成功！");
		}
	} catch (e) {

	} finally {
		self.viewer.shieldHide();
	}
}

/**
 * 打印盖章文件
 * @param printFileInfo 打印文件相关信息
 */
rh.vi.rhGwSeal.prototype.printSealFile = function(printFileInfo) {
	if(printFileInfo.SEAL_FILE_ID == undefined || printFileInfo.SEAL_FILE_ID.length == 0){
		alert("盖章文件不存在。");
		return;
	}
	
	var urlStr = printFileInfo.SEAL_SYS_HOST
			+ "/general/modules/wendang/printDoc/oa_print.jsp"
			+ "?wyh=" + printFileInfo.GW_ID
			+ "&saveNo=" + printFileInfo.SEAL_FILE_ID
			+ "&printUserName=" + printFileInfo.USER_LOGIN_NAME
			+ "&printDeptId=" + printFileInfo.ODEPT_NUM;
	
	var str = window.prompt("请输入打印份数：","1");
	if(isNaN(str)){
		alert("输入的数字无效。");
		return;
	}
	
	urlStr += "&printNum=" + str;
	
	window.open(urlStr);
}

rh.vi.rhGwSeal.prototype._sendAppendPrintNum = function(printFileInfo,ODeptNum){
	var url = printFileInfo.SEAL_SYS_HOST
	+ "/general/modules/wendang/printDoc/oa_play.jsp?wyh="
	+ printFileInfo.SEAL_FILE_ID
	+ "&printDeptId=" + ODeptNum
	+ "&userName=" + printFileInfo.USER_LOGIN_NAME;

	window.open(url);
}

/**
 * 追加打印份数
 * 
 * @param printFileInfo
 *            打印文件信息
 */
rh.vi.rhGwSeal.prototype.appendPringNum = function(printFileInfo) {
	var _self = this;
	var param = {"GW_ID":printFileInfo.GW_ID,"ORIG_FILE_ID":printFileInfo.ORIG_FILE_ID};
	var result = FireFly.doAct("OA_GW_SEAL_FILE","findPrintODept",param,false);
	
	var arr = new Array();
	var htmlArr = new Array();
	htmlArr.push("<div class='appendPrintNumDlg' id='appendPrintNumDlgDiv' title='追加打印份数'>");
	htmlArr.push("<div class='ml20 mt20'>");
	htmlArr.push("<div class='p5 f14 fb'>请选择机构:</div>");
	htmlArr.push("<div class='ml5'><table class='wp80 mt10'>");
	
	var list = result._DATA_;
	for(var i=0;i<list.length;i++){
		var data = list[i];
		htmlArr.push("<tr class='cp' ON='");
		htmlArr.push(data.ODEPT_NUM);
		htmlArr.push("'><td class='w10 p5'>");
		htmlArr.push("<input type='radio' name='appendPrintNumRadio' id='appendPrintNumRadio'>");
		htmlArr.push("</td><td class='p5' >");
		htmlArr.push(data.DEPT_NAME);
		htmlArr.push("</td></tr>");
	}
	htmlArr.push("</table></div></div></div>");
	
	var winDialog = jQuery(htmlArr.join(""));
	
	jQuery("tr",winDialog).click(function(){
		jQuery("#appendPrintNumDlgDiv").dialog("close");
		var odeptNum = jQuery(this).attr("ON");
		_self._sendAppendPrintNum(printFileInfo, odeptNum);
	});
	
	htmlArr = null;
	
	winDialog.appendTo(jQuery("body"));
	jQuery("#appendPrintNumDlgDiv").dialog({
		autoOpen : true,
		width : 400,
		height : 260,
		modal : true,
		resizable : false,
		position : {my:"center top+50", at:"center bottom", of:"#funcBtnDivCon"},
		close : function() {
			jQuery(this).remove();
		},buttons: [{"text":"关闭",
			"click": function() {
				jQuery(this).remove();
			}}]
		
	});	
}
