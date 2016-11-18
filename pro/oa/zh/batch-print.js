GLOBAL.namespace("rh.oa");

/**
 * 用印审批单批量打印
 * @param {Object} options 参数句柄
 */
rh.oa.batchPrint = function(options) {
	this._viewer = options.parHandler;
	this._actObj = options.actObj;
	this._printPic = options.printPic;
	this._printAudit = options.printAudit;
	this.init();
};

/**
 * 初始化组件
 */
rh.oa.batchPrint.prototype.init = function(){
	var _self = this;
	if (_self._actObj) {
		_self._actObj.layoutObj.unbind("click").bind("click", function(){
			_self.printAttach();
		});
	}
};

/**
 * 批量打印
 */
rh.oa.batchPrint.prototype.printAttach = function(){
	var _self = this;
	var paramZw = {};
	var wordDocsStr = "";
	paramZw.DATA_ID = _self._viewer._pkCode;
	
	jQuery("div[id='batchPrintWord']").remove();
	var attachInfos = FireFly.doAct(_self._viewer.servId, "getAttachInfo", paramZw);
	_self._viewer.cardBarTip("正在打印，请稍候……");
	jQuery.each(attachInfos._DATA_, function(index, fileBean) { //循环多个附件，
	    var fileName = fileBean.FILE_NAME;
		var fileSuffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
	
		if (fileSuffix.indexOf(".doc") >= 0) { //word文件
			//_self.printWordDoc(fileBean.FILE_ID);
			wordDocsStr = wordDocsStr + "," + fileBean.FILE_ID;
		} else if (fileSuffix.indexOf(".pdf") >= 0) { //pdf文件
			_self.printPdf(fileBean.FILE_ID, fileBean);
		} else if (fileSuffix.indexOf(".gd") >= 0) { //书生的归档文件
			_self.printGdFile(fileBean.FILE_ID);
		} else if (fileSuffix.indexOf(".xls") >= 0) { //excel 文件
		    //_self.printExcel(fileBean.FILE_ID);
		} else if (fileSuffix.indexOf(".jpg") >= 0 || fileSuffix.indexOf(".jpeg") >= 0 || fileSuffix.indexOf(".png") >= 0 || fileSuffix.indexOf(".bmp") >= 0 ) { //图片文件
			if (_self._printPic) {
				_self.printPicFile(fileBean.FILE_ID, fileSuffix);
			}
			//_self.printPicFile(fileBean.FILE_ID, fileSuffix);
		}
	});
	if (_self._printAudit) { //打印审签单
		_self.printAudit(_self._viewer.getPKCode());
	}
	if (wordDocsStr.length > 0) {
		if (wordDocsStr.indexOf(",") ==0) {
			wordDocsStr = wordDocsStr.substring(1);
		}
		_self.printWordDoc(wordDocsStr);
	}
};

/**
 * 打印word文件
 */
rh.oa.batchPrint.prototype.printWordDoc = function(docId){
	var _self = this;
	var redhatUrl = "/oa/zh/batchPrintDoc.jsp?FILE_ID=" + docId;
	
	var winHtml = new Array();
	winHtml.push("<div style='width:400;height:100;' title='打印word文档' id='");
	winHtml.push("batchPrintWord'><iframe style='width:99%;height:99%;border:0px;' id='batchPrintWordiframe' src='");
	winHtml.push("' />");
	winHtml.push("</div>");
	jQuery("body").append(winHtml.join(""));
	winHtml = null;
	
	jQuery("iframe[id='batchPrintWordiframe']").attr("src",redhatUrl);
	
	//zhegediang, mei qing ling
	//window.open(redhatUrl);
};

/**
 * 打印pdf文件
 */
rh.oa.batchPrint.prototype.printPdf = function(docId, fileBean){
	var _self = this;
	try {
		if (fileBean["ITEM_CODE"] == "ZHENGWEN") {
			_self.printSealFile(_self.getPrintFileInfo());
		} else {
			jQuery("object[id='" + docId + "']").remove();
			var p = document.createElement("object");
			p.id = docId;
			p.classid = "CLSID:CA8A9780-280D-11CF-A24D-444553540000";
			p.width = 1;
			p.height = 1;
			var srcPdfUrl = "http://" + window.location.host;
			if (window.location.port.length > 0 && window.location.host.indexOf(":") < 0) {
				srcPdfUrl += ":" + window.location.port;
			}
			srcPdfUrl += "/file/" + docId;
			p.src = srcPdfUrl;
			document.body.appendChild(p);
			p.printAllFit(true);
			p = null;
		}
	} catch (e) {
		alert("打印pdf文件出现问题，请检查是否安装有 Adobe PDF Reader ， 或者将pdf文件下载之后手动打印");
	}
};

/**
 * 打印盖章文件
 * @param printFileInfo 打印文件相关信息
 */
rh.oa.batchPrint.prototype.printSealFile = function(printFileInfo) {
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
	urlStr += "&printNum=1";
	window.open(urlStr);
};

/**
 * 获取打印文件的信息 ， 原来公文的信息
 */
rh.oa.batchPrint.prototype.getPrintFileInfo = function(){
	var _self = this;
	//先通过档案，找到原始的公文，通过公文再去打红头文件
	var queryGw = {};
	queryGw.GW_ID = _self._viewer.getPKCode();
	var gwBeans = FireFly.doAct("OA_GW_GONGWEN", "finds", queryGw, false);
	if (gwBeans._OKCOUNT_ == 0) {
		return;
	}
	var gwBean = gwBeans._DATA_[0]
	var reqdata = {};
	var gwServId = gwBean.TMPL_CODE;
	reqdata.PK_CODE = _self._viewer.getPKCode();  //原来公文的ID
	reqdata.TMP_CODE = gwServId; //原来公文的类型
	var result = FireFly.doAct(gwServId, "getPrintFileInfo", reqdata,false);
	return result;
}

/**
 * 打印gd文件
 */
rh.oa.batchPrint.prototype.printGdFile = function(docId){
	var srcGdUrl = "http://" + window.location.host;
	if (window.location.port.length > 0 && window.location.host.indexOf(":") < 0) {
		srcGdUrl += ":" + window.location.port;
	}
	srcGdUrl += "/file/" + docId;
	window.open(srcGdUrl);  //下载这个gd文件
};

/**
 * 打印图片文件
 */
rh.oa.batchPrint.prototype.printPicFile = function(docId, fileSuffix){
	var _self = this;
	var redhatUrl = "/oa/zh/batchPrintPic.jsp?FILE_ID=" + docId + "&PICTYPE=" + fileSuffix;
	window.open(redhatUrl);
};

/**
 * 打印 审签单 
 */
rh.oa.batchPrint.prototype.printAudit = function(dataId){
	//先根据dataId从entity中查询到servId 和 servSrcId
	var resultEntity = FireFly.doAct("SY_COMM_ENTITY","finds",{"DATA_ID":dataId});
	if (resultEntity._OKCOUNT_ > 0) {
		jQuery.each(resultEntity._DATA_, function(index, biData){
			var reg=new RegExp("^DA_"); 
			if (!reg.test(biData.SERV_ID)) {
				FireFly.doPrint(biData.SERV_ID, biData.SERV_SRC_ID, dataId);
			} 
		});
	} 
};
