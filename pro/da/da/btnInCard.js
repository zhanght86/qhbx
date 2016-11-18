GLOBAL.namespace("rh.da");

/**
 * 中华保险 - 档案打印 - 因为和OA在一个系统中，所以复用OA中审签单的打印
 * 档案 卡片页面上的 公共 按钮
 */
rh.da.cardPubBtn = function(options) {
	this._viewer = options.parHandler;
	
	this.defaultValue();
}


/**
 * 处理 根据树上自定义 的节点， 转换到卡片上字段的默认值
 */
rh.da.cardPubBtn.prototype.defaultValue = function(){
	var _self = this;
	
	if (_self._viewer.getStatus() == UIConst.ACT_CARD_ADD) { //添加的时候，才去设置默认值
		if (_self._viewer._parHandler && _self._viewer._parHandler.getNavTreeObj("@com.rh.da.dir.ExtSubDirDict")) {
			var nodeId = _self._viewer._parHandler.getNavTreeObj("@com.rh.da.dir.ExtSubDirDict").getCurrentNode().ID;
			
			if (nodeId.indexOf("___") >= 0) { //对自定义的树节点的操作
				_self.handleDefaultValue(nodeId);
			}
		}
	}
}

/**
 * 1trjfsLjdeiF9AxhQBuFyz___DA_YEAR___2012_______DA_TERM___10--u5e74____
 * 拆分处理树上过来的默认值
 */
rh.da.cardPubBtn.prototype.handleDefaultValue = function(nodeId){
	var _self = this;
	
	var firstFlag = nodeId.indexOf("___");
	
	var fields = nodeId.substring(firstFlag).split("____");
	
    jQuery.each(fields, function(i, field) { //取出字段和值

    	if (field && field.indexOf("___") >= 0) {
        	var nameAndVar = field.split("___");
        	
        	var result = nameAndVar[2].replace(new RegExp("--","gm"),"\\"); //将--还原成 \
        	
        	result = result.replace(/(\\u\w{4})/gi,function($0){ //将unicode转成汉字
                return (String.fromCharCode(parseInt((escape($0).replace(/(%5Cu)(\w{4})/g,"$2")),16)));
            });
            
        	_self._viewer.getItem(nameAndVar[1]).setValue(result);  //对字段设置树节点下的值  		
    	}
    });	
}


/**
 * 初始化按钮
 * 
 * @returns
 */
rh.da.cardPubBtn.prototype.init = function(){
	var _self = this;

	var printDaGdInfo  = _self._viewer.getBtn("printDaGdInfo");
	if(printDaGdInfo.length > 0){
		printDaGdInfo.unbind("click").bind("click",function(){
			//档案归档所需要打印的
			var printGdInfoBtns = System.getVar("@C_DA_PRINT_GD_INFO_BTNS@") || "printDaZw,printAudit,printAttach";
			
			var wordDocsStr = "";
			if (printGdInfoBtns.indexOf("printDaZw") >= 0) {
				//setTimeout(function(){_self.printDaZw();}, 2000);
				var zwWordDoc = _self.printDaZw(); //打印正文
				if (zwWordDoc && zwWordDoc.length > 0) {
					wordDocsStr = zwWordDoc;
				}
			}
			
			//打印附件，
			if (printGdInfoBtns.indexOf("printAttach") >= 0) {
				_self.printAttach(wordDocsStr);
			}
			
			//打印审签单 , 用原来的 数据的ID
			if (_self._viewer.itemValue("DATA_ID") != "" && printGdInfoBtns.indexOf("printAudit") >= 0) {
				_self.printAudit(_self._viewer.itemValue("DATA_ID"));
			}
			
		});	
	}
	
	var printAuditBtn  = _self._viewer.getBtn("printAudit");
	if(printAuditBtn.length > 0){
		printAuditBtn.unbind("click").bind("click",function(){
			//打印审签单 , 用原来的 数据的ID
			if (_self._viewer.itemValue("DATA_ID") != "") {
				_self.printAudit(_self._viewer.itemValue("DATA_ID"));
			} else {
				alert("没有找到关联的审签单，不能打印。");
			}
		});	
	}
};


/**
 * 打印档案的正文，有可能是gd,doc,pdf类型的文件
 */
rh.da.cardPubBtn.prototype.printDaZw = function(){
	var _self = this;

	var currServId = _self._viewer.servId;
	
	//先获取到正文信息 , 正文要打红头的文件
	var paramZw = {};
	paramZw.DATA_ID = _self._viewer._pkCode;
	paramZw.DA_SERV = _self._viewer.servId;
	
	//先判断如果是用印的 ，则调用用印的方法去打印正文
	if (currServId.indexOf("DA_YONGYIN") >= 0 && _self._viewer.itemValue("DATA_ID") != "") { //用印
		//用印的取printInfo ， 
		var result = _self._getYYPrintFileInfo();
		
	} else {
		var zwInfo = FireFly.doAct(_self._viewer.servId, "getZwInfo", paramZw);

		if (zwInfo.FILE_NAME == undefined) {
			alert("没有找到正文文件");
			
			return;
		}
		
		
		if (zwInfo.FILE_NAME.indexOf(".doc") >= 0) { //word文件
			return zwInfo.FILE_ID; //_self.printWordDoc(zwInfo.FILE_ID);
		} else if (zwInfo.FILE_NAME.indexOf(".pdf") >= 0) { //pdf文件, 那就是中华的新的正文方式，需要调用公文的打印去打
			//如果是公文的话， 则去打红头的盖章的文件， 否则，只是打本地的pdf //文书档案 - 文档一体过来的
			if (_self._viewer.servId.indexOf("DA_DANGAN") >= 0 && _self._viewer.itemValue("DATA_ID") != "") { 
				var result = _self._getPrintFileInfo();
				
				_self.printSealFile(result);
			} else {
				_self.printPdf(zwInfo.FILE_ID);
			}
		} else if (zwInfo.FILE_NAME.indexOf(".gd") >= 0) { //书生的归档文件
			_self.printGdFile(zwInfo.FILE_ID);
		} else if (zwInfo.FILE_NAME.indexOf(".gw") >= 0) { //书生的红头文件
			_self.printGwFile(zwInfo.FILE_ID);
		}		
		
	}
}

/**
 * 打印盖章文件
 * @param printFileInfo 打印文件相关信息
 */
rh.da.cardPubBtn.prototype.printSealFile = function(printFileInfo) {
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
	
//	var str = window.prompt("请输入打印份数：","1");
//	if(isNaN(str)){
//		alert("输入的数字无效。");
//		return;
//	}
	
	urlStr += "&printNum=1";
	
	window.open(urlStr);
}


/**
 * 获取打印文件的信息 ， 原来用印的信息
 */
rh.da.cardPubBtn.prototype._getYYPrintFileInfo = function(){
	var _self = this;
	
	var YyspdFilePubServId = "OA_YY_YYSPD_FILE_PUB";
	var pkForm = _self._viewer.itemValue("DATA_ID");  ////用印审签单的主键

	//根据 用印审签单的主键 和 查出的公共文件， 循环去打印
	var reqPubdata = {};
	reqPubdata._extWhere = " and YYID = '" + pkForm + "'";
	var result = FireFly.doAct(YyspdFilePubServId, "query", reqPubdata);	
	
	jQuery.each(result._DATA_, function(index, filePub) {  //公共的文件
		var reqdata = {};
		reqdata.PK_FORM = pkForm;         //用印审签单的主键， 在档案中就是 DA_yongyin 中的 data_id 字段的值
		reqdata.PK_FILE = filePub._PK_;   // OA_YY_YYSPD_FILE 中的主键 ， 
		var result = FireFly.doAct(YyspdFilePubServId, "getPrintFileInfo", reqdata);
		
		_self.printSealFile(result);
	});
	
	
	var reqPridata = {};
	reqPridata._extWhere = " and YYID = '" + pkForm + "'";
	var result = FireFly.doAct("OA_YY_YYSPD_FILE_PRI", "query", reqPridata);	
	
	jQuery.each(result._DATA_, function(index, filePri) {  //私有的文件
		var reqdata = {};
		reqdata.PK_FORM = pkForm;         //用印审签单的主键， 在档案中就是 DA_yongyin 中的 data_id 字段的值
		reqdata.PK_FILE = filePri._PK_;   // OA_YY_YYSPD_FILE 中的主键 ， 
		var result = FireFly.doAct(YyspdFilePubServId, "getPrintFileInfo", reqdata);
		
		_self.printSealFile(result);
	});	
}




/**
 * 获取打印文件的信息 ， 原来公文的信息
 */
rh.da.cardPubBtn.prototype._getPrintFileInfo = function(){
	var _self = this;
	
	//先通过档案，找到原始的公文，通过公文再去打红头文件
	var queryGw = {};
	queryGw.GW_ID = _self._viewer.itemValue("DATA_ID");
	var gwBeans = FireFly.doAct("OA_GW_GONGWEN", "finds", queryGw, false);
	if (gwBeans._OKCOUNT_ == 0) {
		return;
	}
	
	var gwBean = gwBeans._DATA_[0]
	var reqdata = {};
	var gwServId = gwBean.TMPL_CODE;
	
	reqdata.PK_CODE = _self._viewer.itemValue("DATA_ID");  //原来公文的ID
	reqdata.TMP_CODE = gwServId; //原来公文的类型

	var result = FireFly.doAct(gwServId, "getPrintFileInfo", reqdata,false);

	return result;
}


/**
 * 打印档案的附件，有可能是gd,doc,pdf类型的文件
 */
rh.da.cardPubBtn.prototype.printAttach = function(wordDocsStr){
	var _self = this;

	var paramZw = {};
	paramZw.DATA_ID = _self._viewer._pkCode;
	paramZw.DA_SERV = _self._viewer.servId;
	
	var attachInfos = FireFly.doAct(_self._viewer.servId, "getAttachInfo", paramZw);

	jQuery.each(attachInfos._DATA_, function(index, fileBean) { //循环多个附件，
	    var fileName = fileBean.FILE_NAME;
		var fileSuffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
	
		if (fileSuffix.indexOf(".doc") >= 0) { //word文件
			//_self.printWordDoc(fileBean.FILE_ID);
			wordDocsStr = wordDocsStr + "," + fileBean.FILE_ID;
		} else if (fileSuffix.indexOf(".pdf") >= 0) { //pdf文件
			_self.printPdf(fileBean.FILE_ID);
		} else if (fileSuffix.indexOf(".gd") >= 0) { //书生的归档文件
			_self.printGdFile(fileBean.FILE_ID);
		} else if (fileSuffix.indexOf(".xls") >= 0) { //excel 文件
		    //_self.printExcel(fileBean.FILE_ID);
		} else if (fileSuffix.indexOf(".jpg") >= 0 || fileSuffix.indexOf(".jpeg") >= 0 || fileSuffix.indexOf(".png") >= 0 || fileSuffix.indexOf(".bmp") >= 0 ) { //图片文件
			_self.printPicFile(fileBean.FILE_ID, fileSuffix);
		}
	});
	if (wordDocsStr.length > 0) {
		if (wordDocsStr.indexOf(",") ==0) {
			wordDocsStr = wordDocsStr.substring(1);
		}
		_self.printWordDoc(wordDocsStr);
	}
}

/**
 * 打印word文件
 */
rh.da.cardPubBtn.prototype.printPicFile = function(docId, fileSuffix){
	var _self = this;
	
	var redhatUrl = "/da/jsp/printPic.jsp?FILE_ID=" + docId + "&PICTYPE=" + fileSuffix;

	window.open(redhatUrl);
}

/**
 * 打印word文件
 */
rh.da.cardPubBtn.prototype.printWordDoc = function(docId){
	var _self = this;	
	var redhatUrl = "/da/jsp/printZwDoc.jsp?FILE_ID=" + docId;
	
	var winHtml = new Array();
	winHtml.push("<div style='width:400;height:100;' title='打印正文' id='");
	winHtml.push("daPrintZw11'><iframe style='width:99%;height:99%;border:0px;' id='daPrintZw' src='");
	winHtml.push("' />");
	winHtml.push("</div>");
	jQuery("body").append(winHtml.join(""));
	winHtml = null;
	
	jQuery("#daPrintZw").attr("src",redhatUrl);
	
	//window.open(redhatUrl);
}

/**
 * 打印excel
 */
rh.da.cardPubBtn.prototype.printExcel = function(docId) {  
	var redhatUrl = "/da/jsp/printZwXls.jsp?FILE_ID=" + docId;

	/**
	var varId = docId;
	if (docId.indexOf(".") > 0) {
		varId = docId.substring(0, docId.indexOf("."));
	}	
	
	var winHtml = new Array();
	winHtml.push("<div style='width:400;height:100;' title='打印excel' id='");
	winHtml.push("daPrintZwExcel'><iframe style='width:99%;height:99%;border:0px;' id='daPrZwExcel"+varId+"' src='");
	winHtml.push("' />");
	winHtml.push("</div>");
	jQuery("body").append(winHtml.join(""));
	winHtml = null;
	
	jQuery("#daPrZwExcel" + varId).attr("src",redhatUrl);
	*/
	
	window.open(redhatUrl);
} 


/**
 * 打印pdf文件
 */
rh.da.cardPubBtn.prototype.printPdf = function(docId){
/**	var pdf = document.getElementById("createPDF");
	if (pdf != undefined && pdf != null) {
		var parentNode = pdf.parentNode;
		parentNode.removeChild(pdf);
	}	
*/	
	try {
		var p = document.createElement("object");
		//p.id = "createPDF";
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
	} catch (e) {
		alert("打印pdf文件出现问题，请检查是否安装有 Adobe PDF Reader ， 或者将pdf文件下载之后手动打印");
	}
}


/**
 * 打印gd文件
 */
rh.da.cardPubBtn.prototype.printGdFile = function(docId){
	
	var srcGdUrl = "http://" + window.location.host;
	
	if (window.location.port.length > 0 && window.location.host.indexOf(":") < 0) {
		srcGdUrl += ":" + window.location.port;
	}
	srcGdUrl += "/file/" + docId;
	
	window.open(srcGdUrl);  //下载这个gd文件
}



/**
 * 打印gw文件
 * <OBJECT ID="ShuShengSeal" CLASSID="CLSID:567FF69D-56E3-11D6-81D1-00E04CE60E84" height="0" width="0" ></OBJECT>
 */
rh.da.cardPubBtn.prototype.printGwFile = function(docId){
	var _self = this;	
	var redhatUrl = "/da/jsp/printShuShengGwFile.jsp?FILE_ID=" + docId;
	
	var winHtml = new Array();
	winHtml.push("<div style='width:400;height:100;' title='打印正文' id='");
	winHtml.push("daPrintGw11'><iframe style='width:99%;height:99%;border:0px;' id='daPrintGw' src='");
	winHtml.push("' />");
	winHtml.push("</div>");
	jQuery("body").append(winHtml.join(""));
	winHtml = null;
	
	jQuery("#daPrintGw").attr("src",redhatUrl);
	
	//window.open(redhatUrl);
}


/**
 * 打印 审签单 
 */
rh.da.cardPubBtn.prototype.printAudit = function(dataId){
	//先根据dataId从entity中查询到servId 和 servSrcId
	alert(dataId)l
	var resultEntity = FireFly.doAct("SY_COMM_ENTITY","finds",{"DATA_ID":dataId});
	
	if (resultEntity._OKCOUNT_ > 0) {
		
		jQuery.each(resultEntity._DATA_, function(index, biData){
			var reg=new RegExp("^DA_"); 
			
			if (!reg.test(biData.SERV_ID)) {
				FireFly.doPrint(biData.SERV_ID, biData.SERV_SRC_ID, dataId);
				
				return false;
			} 
		});
	} 
}

