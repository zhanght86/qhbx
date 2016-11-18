GLOBAL.namespace("rh.vi");

/**
 * OA系统用印审批模块与中华保险项目的吉大正源印章系统接口实现
 * 
 * @param cardViewer
 *            卡片页面对象
 * @returns
 */
rh.vi.rhYySeal = function(cardViewer) {
	this.viewer = cardViewer;
}

/**
 * 对指定文件盖章
 * 
 * @sealParams 从服务器端获取的盖章文件信息
 */
rh.vi.rhYySeal.prototype.seal = function(sealParams) {

	if (sealParams._MSG_ != undefined
			&& StringUtils.startWith(sealParams._MSG_, "ERROR,")) {
		throw ({
			"message" : sealParams._MSG_
		});
	}

	var oaHost = sealParams.SYS_HOST_ADDR;
	

	var urlStr = sealParams.SEAL_SYS_HOST
			+ "/general/modules/wendang/anonDoc/sealShow.jsp" + "?sealIds="
			+ sealParams.SEAL_CODE + "&userName=" + sealParams.USER_LOGIN_NAME
			+ "&sealFilePath=" + oaHost + sealParams.FILE_PATH + "&printNum="
			+ sealParams.DEFAULT_PRINT_NUM + "&wyh=" + sealParams.sealFileId
			+ "&rtnUrl=" + oaHost + "/oa/zh/returnSealFile.jsp"
			+ "&sealFileName=" + (this.viewer.getByIdData("YY_TITLE"))
			+ "&printDeptId=" + sealParams.ODEPT_NUM + "&fileNo="
			+ sealParams.SEAL_FILE_ID + "&cancelSeal=1";

	// cancelSeal 1:盖章，2:取消盖章
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
 * 取消盖章
 */
rh.vi.rhYySeal.prototype.undoSeal = function(params) {
	var self = this;
	// 灰章文件上的印章个数
	var seal_count = params.SEAL_COUNT;

	if (seal_count > 1) {
		
		if (params._MSG_ != undefined
				&& StringUtils.startWith(params._MSG_, "ERROR,")) {
			throw ({
				"message" : params._MSG_
			});
		}

		var oaHost = params.SYS_HOST_ADDR;

		//调用取消盖章接口，返回到取消处理界面
		var urlStr = params.SEAL_SYS_HOST
				+ "/general/modules/wendang/anonDoc/sealcancle.jsp" + "?sealIds="
				+ params.SEAL_CODE + "&userName=" + params.USER_LOGIN_NAME
				+ "&sealFilePath=" + oaHost + params.FILE_PATH + "&printNum="
				+ params.DEFAULT_PRINT_NUM + "&wyh=" + params.sealFileId
				+ "&rtnUrl=" + oaHost + "/oa/zh/returnCancelSealFile.jsp"
				+ "&sealFileName=" + (this.viewer.getByIdData("YY_TITLE"))
				+ "&printDeptId=" + params.ODEPT_NUM + "&fileNo="
				+ params.SEAL_FILE_ID + "&cancelSeal=2";

		// cancelSeal 1:盖章，2:取消盖章
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

	} else {
       
		var reqdata = {};
		reqdata.PK_FORM = this.viewer._pkCode;
		try {
			self.viewer.shield();
			jQuery.extend(reqdata, params); // 合并参数
			var result = FireFly.doAct("OA_YY_YYSPD_FILE_PUB", "undoSeal",
					reqdata);
			if (result.rtnMsg == "succ") {
				self.viewer.refresh();
				alert("取消成功！");
			}
		} catch (e) {

		} finally {
			self.viewer.shieldHide();
		}
	}
}

/**
 * 打印盖章文件
 * 
 * @param printFileInfo
 *            打印文件相关信息
 */
rh.vi.rhYySeal.prototype.printSealFile = function(printFileInfo) {
	if (printFileInfo.SEAL_FILE_ID == undefined
			|| printFileInfo.SEAL_FILE_ID.length == 0) {
		alert("盖章文件不存在。");
		return;
	}

	var urlStr = printFileInfo.SEAL_SYS_HOST
			+ "/general/modules/wendang/printDoc/oa_print.jsp" + "?wyh="
			+ printFileInfo.GW_ID + "&saveNo=" + printFileInfo.SEAL_FILE_ID
			+ "&printUserName=" + printFileInfo.USER_LOGIN_NAME
			+ "&printDeptId=" + printFileInfo.ODEPT_NUM;

	var str = window.prompt("请输入打印份数：", "1");
	if (isNaN(str)) {
		alert("输入的数字无效。");
		return;
	}

	urlStr += "&printNum=" + str;

	window.open(urlStr);
}

/**
 * 追加打印份数
 * 
 * @param printFileInfo
 *            打印文件信息
 */
rh.vi.rhYySeal.prototype.appendPringNum = function(printFileInfo) {
	var url = printFileInfo.SEAL_SYS_HOST
			+ "/general/modules/wendang/printDoc/oa_play.jsp?wyh="
			+ printFileInfo.SEAL_FILE_ID + "&printDeptId="
			+ printFileInfo.ODEPT_NUM + "&userName="
			+ printFileInfo.USER_LOGIN_NAME;

	window.open(url);
}
