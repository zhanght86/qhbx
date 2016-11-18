// 载入ZotnClient控件
var ZotnClient = ZotnClient || null;
if (!ZotnClient && typeof (FireFly) != "undefined") {
	FireFly.createZotnClient();
	ZotnClient = FireFly.getZotnClient();
}
/**
 * 只读文件zotnReadOffice fileName:文件名 downLoadUrl:下载文件url showRevision:是否显示痕迹
 */
function readOfficeFile(fileName, downLoadUrl, showRevision) {
	readOfficeFileExt(fileName, downLoadUrl, showRevision, false, -1);
}
/**
 * 只读文件zotnReadOffice fileName:文件名 downLoadUrl:下载文件url showRevision:是否显示痕迹  bnPrint:是否显示打印按钮
 */
function readOfficeFileExt(fileName, downLoadUrl, showRevision, bnPrint, bnTransPdf, params) {
	readOfficeFileExt(fileName, downLoadUrl, showRevision, bnPrint, false, params);
}
/**
 * 只读文件zotnReadOffice fileName:文件名 downLoadUrl:下载文件url showRevision:是否显示痕迹  bnPrint:是否显示打印按钮 bnTransPdf 是否转pdf
 */
function readOfficeFileExt(fileName, downLoadUrl, showRevision, bnPrint, bnTransPdf, params) {
	var newFileName = encodeURIComponent(fileName);
	var newDownloadUrl = encodeURIComponent(downLoadUrl);

	var contextPath = zotnClientNTKO._getContextPath();
	var url = contextPath
				+ "/sy/util/office/officeClient.jsp?fileName=" + newFileName
				+ "&downLoadUrl=" + newDownloadUrl + "&fileAction=;read;print;";
	if(showRevision){
		url += "showRevision;";
	}
	if(bnPrint){
		url += "bnPrint;";
		if(params && params.printNum){ // 打印份数
			url += "&printNum="+params.printNum;
		}
	}
	if(bnPrint || bnTransPdf){
		if(params && params.fileId){ // 文件ID
			url += "&fileId="+params.fileId;
		}
		if(params && params.dataId){ // 数据ID
			url += "&dataId="+params.dataId;
		}
		if(params && params.servId){ // 服务ID
			url += "&servId="+params.servId;
		}
	}
	//转pdf，加上上传url参数
	if(bnTransPdf && params){
		url += "&isTransPdf=true&upLoadUrl=" + encodeURIComponent("/file/?keepMetaData=true&DATA_ID="+params.dataId+"&FILE_CAT="+params.fileCat+"&SERV_ID="+params.servId+"&ITEM_CODE=ZWPDF&FILE_SORT=50");
	}
	return showDialog(url, window.screen.width,window.screen.height);
}

/**
 * 编辑文件zotnEditOffice fileName:文件名 downLoadUrl:下载文件url upLoadUrl:上传文件url
 * revision:是否记录痕迹 qrCode:插入二维码
 */
function editOfficeFile(fileName, downLoadUrl, upLoadUrl, revision, qrCode) {
	var params = {
		"revision" : revision,
		"qrCode" : qrCode
	};
	editOfficeFileExt(fileName, downLoadUrl, upLoadUrl, params);
}

/**
 * 
 * @param fileName
 *            文件名称
 * @param downLoadUrl
 *            下载文件URL
 * @param upLoadUrl
 *            上传文件URL
 * @param params
 *            扩展参数对象。支持的对象为： { "revision":revision, //是否记录痕迹 "qrCode":qrCode,
 *            //插入二维码 "bookmark":dataName
 *            //是否替换bookmark，替换的数据在opener页面的dataName参数中，
 *            数据获取方式为jQuery(opener).data(dataName); }
 */
function editOfficeFileExt(fileName, downLoadUrl, upLoadUrl, params) {
	var newFileName = encodeURIComponent(fileName);
	var contextPath = zotnClientNTKO._getContextPath();
	var url = contextPath + "/sy/util/office/officeClient.jsp?fileName="
			+ newFileName + "&downLoadUrl=" + encodeURIComponent(downLoadUrl)
			+ "&upLoadUrl=" + encodeURIComponent(upLoadUrl);

	if (params && params.bookmark) { // 替换标签
		url += "&bookmark=" + params.bookmark;
	}

	if (params && params.qrCode) { // 插入二维码
		url += "&qrCode=" + params.qrCode;
	}

	if (params && params.revision) { // 是否记录痕迹
		url += "&fileAction=;edit;print;showRevision;revision;";
	} else {
		url += "&fileAction=;edit;print;showRevision;";
	}
	var extParams = "";
	//是否盖章
	if (params && params.isSeal) {
		url += "seal;";
		if (!isNaN(params.sealIndex)) {
			extParams += "&sealIndex="+params.sealIndex;
		}
		if(params.isQfz){
			extParams += "&isQfz=true";
		}
		if(params.dataId){// 审批单ID
			extParams += "&dataId="+params.dataId;
		}
		extParams += "&eKeySN="+params.eKeySN+"&sealID="+params.sealID+"&fileId="+params.fileId+"&servId="+params.servId;
	}
	url += extParams;
	return showDialog(url, window.screen.width, window.screen.height);
}

/**
 * 判断文件的类型，如果是.doc或.xls返回true，否则返回false
 * 
 * fileName:文件名
 */
function isWordOrExcelFile(fileName) {
	if (fileName == '')
		return false;
	var allType = ".doc|.docx|.xls|.xlsx|.ppt|.pptx|.wps|.et|";
	if (fileName.indexOf(".") < 0) {
		fileName = "." + fileName;
	}
	var extType = fileName.substr(fileName.lastIndexOf(".")).toLowerCase();
	if (allType.indexOf(extType + "|") >= 0) {
		return true;
	} else {
		return false;
	}
}

var zotnClientNTKO = zotnClientNTKO || {};
/**
 * revision : 是否记录修订信息
 */

zotnClientNTKO.OnLineEditFile = function(DownloadURL, UploadURL, fileType,
		ifEncrypt, revision, ifSeal) {
	if (revision == undefined || revision == 'undefined') {
		revision = false;
	}
	editOfficeFile(fileType, DownloadURL, UploadURL, revision);
}

/**
 * 套红头结束之后，使用此方法打开红头文件
 */
zotnClientNTKO.openRedHeadFile = function(downLoadUrl, upLoadUrl, fileName) {
	var revision = false;
	editOfficeFile(fileName, downLoadUrl, upLoadUrl, revision);
}

/**
 * isRevision: 是否显示修订信息
 */
zotnClientNTKO.DownloadFile = function(DownloadURL, fileName, ifEncrypt,
		ifDialog, ifDownload, isRevision) {
	if (isRevision == 'undefined') {
		isRevision = false;
	}
	if (ifDownload == 'undefined') {
		ifDownload = false;
	}
	if (!ifDownload && isWordOrExcelFile(fileName)) {
		readOfficeFile(fileName, DownloadURL, isRevision);
	} else {
		if (ZotnClient && ZotnClient.IpAddress) {
			ZotnClient.DownloadFile(this._createUrl(DownloadURL), fileName,
					ifEncrypt, ifDialog, ifDownload, isRevision);
		} else {
			window.open(DownloadURL);
		}
	}
}

zotnClientNTKO._getContextPath = function() {
	if (typeof (FireFly) != "undefined") {
		return FireFly.contextPath;
	}

	if (typeof (FireFlyContextPath) != "undefined") {
		return FireFlyContextPath;

	}
	return "";
}

/**
 * 创建下载文件的URL，如果原始URL不以http://开始，则增加http://主机地址:端口
 * @param downloadURL 原始下载文件的URL
 */
zotnClientNTKO._createUrl = function(downloadURL) {
	if (StringUtils.startWith(downloadURL, "http://")) {
		return downloadURL;
	} else if (StringUtils.startWith(downloadURL, "https://")) {
		return downloadURL;
	} else {
		return this._getHostURL() + downloadURL;
	}
}

/**
 * 取得当前主机的地址
 */
zotnClientNTKO._getHostURL = function() {
	return window.location.protocol + "//" + window.location.host;
}

/** 显示Window对话框* */
function showDialog(url, width, height) {
	// return openNewWindow(url,width,height);
	var windowArgs = "width="
			+ width
			+ ",height="
			+ height
			+ ",toolbar=no,status=no,directories=no,menubar=no,resizable=yes,scrollable=no,scrollbars=no";
	return window.open(url, "_blank", windowArgs);
	// var dialogArgs = "scroll:no;status:off;dialogWidth:" +width+
	// "px;dialogHeight:"+height+"px"
	+";resizable:yes";
	// return window.showModalDialog(url,window,dialogArgs);
}