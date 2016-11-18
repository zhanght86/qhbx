var _viewer = this;

var showMemoItem = _viewer.getItem("SHOW_MEMO");
showMemoItem.obj.parent().removeClass("disabled").css({
	"background" : "url('')"
});
showMemoItem.obj.css({
	"color" : "black",
	"font-weight" : "bolder",
	"background-color" : "white",
	"font-size" : "14px"
});
var showMomeItems = new Array();
var sUname = _viewer.getItem("S_USER");
var sTname = _viewer.getItem("S_ATIME");
var gwTime = _viewer.getItem("S_ODEPT");
var semergencyTname = _viewer.getItem("S_EMERGENCY");
showMomeItems.push("<table border='0' width='100%'><tr>");
showMomeItems.push("<td style='width:35px;'>"
		+ sUname.getLabel().find("span[class='name']").html() + ":</td>");
showMomeItems.push("<td style='width:80px;'>" + sUname.getText() + "</td>");
showMomeItems.push("<td style='width:40px;'>"
		+ sTname.getLabel().find("span[class='name']").html() + ":</td>");
showMomeItems.push("<td style='width:80px;'>"
		+ sTname.getValue().substring(0, 16) + "</td>");
showMomeItems.push("<td style='width:40px;'>"
		+ gwTime.getLabel().find("span[class='name']").html() + ":</td>");
showMomeItems.push("<td style='width:180px;'>" + gwTime.getText() + "</td>");
var emergencyType = semergencyTname.getValue();
if (emergencyType <= 10) {
	showMomeItems.push("<td style='width:50px;' id='" + _viewer.servId
			+ "SHOW_MEMO_table_S_EMERGENCY' title='紧急程度:"
			+ semergencyTname.getText()
			+ "'><span class='span_emergency'></span></td>");
} else if (emergencyType <= 20) {
	showMomeItems
			.push("<td style='width:50px;' id='"
					+ _viewer.servId
					+ "SHOW_MEMO_table_S_EMERGENCY' title='紧急程度:"
					+ semergencyTname.getText()
					+ "'><span class='span_emergency comm_emergency__normal'></span></td>");
} else {
	showMomeItems
			.push("<td style='width:50px;' id='"
					+ _viewer.servId
					+ "SHOW_MEMO_table_S_EMERGENCY' title='紧急程度:"
					+ semergencyTname.getText()
					+ "'><span class='span_emergency comm_emergency__very'></span></td>");
}
showMomeItems.push("</tr></table>");
showMemoItem.obj.append(showMomeItems.join(""));

semergencyTname.change(function() {
	var emergencyTdObj = jQuery("td[id='" + _viewer.servId
			+ "SHOW_MEMO_table_S_EMERGENCY']");
	emergencyTdObj.attr("title", "紧急程度:" + semergencyTname.getText());
	var emergencyTypes = semergencyTname.getValue();
	var thisSpanObj = emergencyTdObj.find("span");
	if ((emergencyTypes || 0) == 0 || emergencyTypes <= 10) {
		thisSpanObj.removeClass("comm_emergency__normal");
		thisSpanObj.removeClass("comm_emergency__very");
	} else if (emergencyTypes <= 20) {
		thisSpanObj.addClass("comm_emergency__normal");
		thisSpanObj.removeClass("comm_emergency__very");
	} else if (emergencyTypes > 20) {
		thisSpanObj.addClass("comm_emergency__very");
		thisSpanObj.removeClass("comm_emergency__normal");
	}
});

// 为windows IE内核的加载NTKO控件对象
Load.scriptJS("/sy/util/office/zotnClientLib_NTKO.js");

// 初始化字段变量
var yyOdept = _viewer.getItem("YY_ODEPT");
var pubFileList = _viewer.getItem("YY_PUB_FILE_NUM");
var pubFileCheckArea = _viewer.getItem("YY_PUB_FILE");
var checkBoxs = jQuery("input", pubFileCheckArea.obj);
var YyspdPkCode = _viewer.getPKCode();
var YyspdFilePubServId = "OA_YY_YYSPD_FILE_PUB";
var yySealNameObj = _viewer.getItem("YY_SEALS_NAME");
var isCallbakcFlag = false; // 是否回调过判断追加打印分数权限函数标识，目的是为了防止多次回调没有意义
var callbackAppendPrintOutBean = null; // 追加打印分数权限函数返回对象

// 初始化操作权限
_viewer.yyAuth = {};
if (_viewer.wfCard) {
	initYyWfAuth(_viewer);
}
if (_viewer.wfCard && getYyWfAuth && typeof getYyWfAuth == "function") {
	getYyWfAuth(_viewer);
}
var uploadActFlag = _viewer.yyAuth.uploadActFlag;
var delPriFileActFlag = _viewer.yyAuth.delPriFileActFlag;
var readPriFileActFlag = _viewer.yyAuth.readPriFileActFlag;
var modPubFileActFlag = _viewer.yyAuth.modPubFileActFlag;
var readPubFileActFlag = _viewer.yyAuth.readPubFileActFlag;
var modPriPrintNumActFlag = _viewer.yyAuth.modPriPrintNumActFlag;
var modPubPrintNumActFlag = _viewer.yyAuth.modPubPrintNumActFlag;
var sealPriFileActFlag = _viewer.yyAuth.sealPriFileActFlag;
var sealPubFileActFlag = _viewer.yyAuth.sealPubFileActFlag;
var cancelSealPriFileActFlag = _viewer.yyAuth.cancelSealPriFileActFlag;
var cancelSealPubFileActFlag = _viewer.yyAuth.cancelSealPubFileActFlag;
var printPriFileActFlag = _viewer.yyAuth.printPriFileActFlag;
var printPubFileActFlag = _viewer.yyAuth.printPubFileActFlag;
var appendPrintActFlag = _viewer.yyAuth.appendPrintActFlag;

// 监听对印章所属机构字段的修改
yyOdept.callback = function(codeArr, nameArr) {
	if (codeArr.length > 1) {
		alert("印章所属单位只支持一级");
		return false;
	}
	if (YyspdPkCode.length > 0) {
		var odeptPubFileCodeStr = getOdeptPubFileCodeStr(codeArr[0], nameArr[0]);
		// 处理公共文档列表
		clearPubFiles();
		// 处理公共文档字段
		filterOdeptPubFile(odeptPubFileCodeStr);
	}
};

// 初始化时处理用印材料列表
var priFileList = _viewer.getItem("YY_PRI_FILE");
if (uploadActFlag) { // 权限控制
	var uploadFileBtn = priFileList.getBtn("uploadFile");
	uploadFileBtn
			.html("<span class='rh-icon-inner' style='padding-left:0px;padding-right:0px;'><span id='uploadPriFileBtn'></span></span>");
	jQuery(
			"<fieldset class='progress ui-corner-2' id='fsUploadProgress_yyspdPri'><legend>上传进度</legend></fieldset>")
			.prependTo(priFileList.obj);
	var uploadSettings = {};
	uploadSettings.post_params = {};
	uploadSettings.post_params.data = "{\"SERV_ID\" : \"" + _viewer.servId
			+ "\", \"FILE_CAT\" : \"YY_PRI_FILE\", \"DATA_ID\" : \""
			+ YyspdPkCode + "\"}";
	uploadSettings.button_placeholder_id = "uploadPriFileBtn";
	uploadSettings.button_text = "上传材料";
	uploadSettings.custom_settings = {};
	uploadSettings.custom_settings.progressTarget = "fsUploadProgress_yyspdPri";
	uploadSettings.file_types = "*.doc;*.docx;*.xls;*.xlsx;*.pdf;*.jpg;";
	// uploadSettings.file_types_description = "上传格式为doc、docx、xls、xlsx、pdf、jpg";
	try {
		var _upload = new rh.ui.Upload(uploadSettings);
		_viewer.priFileArr = [];
		_upload.uploadSuccess = function(serverData) {
			var data = jQuery.parseJSON(serverData)._DATA_;
			jQuery.each(data, function(i, n) {
				if (_viewer.priFileArr) {
					_viewer.priFileArr.push(n);
				}
			});
		};
		_upload.queueComplete = function(numFilesUploaded) {
			// 隐藏进度栏
			// setTimeout(function(){
			jQuery("#fsUploadProgress_yyspdPri").hide();
			// },1000);
			// 将文件队列中的所有文件上传到后台
			if (_viewer.priFileArr && _viewer.priFileArr.length > 0) {
				var fileCodes = "";
				var fileIds = "";
				jQuery.each(_viewer.priFileArr, function(i, n) {
					if (i == 0) {
						fileCodes = fileCodes + n.DIS_NAME;
						fileIds = fileIds + n.FILE_ID;
					} else {
						fileCodes = fileCodes + "," + n.DIS_NAME;
						fileIds = fileIds + "," + n.FILE_ID;
					}
				});
				_viewer.priFileArr = [];
				if (fileCodes.length > 0 && fileIds.length > 0) {
					addFileToPriList(fileCodes, fileIds);
				}
			}
		};
	} catch (e) {
		alert("创建flash上传组件失败！" + e);
		priFileList.getBtn("uploadFile").hide();
	}
} else {
	priFileList.getBtn("uploadFile").hide();
}

// 设置初始化时公共文档列表文件code字符串
_getPubFileCodeStr();

// 初始化时处理公共文档列表
if (modPubFileActFlag) { // 权限控制
	jQuery.each(checkBoxs, function(i, n) {
		// 绑定单击事件
		jQuery(this).bind("click", function(event) {
			var chk = jQuery(this);
			var checkFlag = chk.is(":checked");
			var disableFlag = chk.attr("disabled");
			if (disableFlag && disableFlag == "disabled") {
				return false;
			}
			if (event.srcElement) {
				checkFlag = !checkFlag;
			}
			var fileCode = chk.val();
			var yyOdeptCode = yyOdept.getValue();
			if (checkFlag) {
				unCheck(chk, true);
				delFileFromPubList(fileCode, yyOdeptCode);
			} else {
				check(chk, true);
				addFileToPubList(fileCode, yyOdeptCode);
			}
		});
		// 填充checkBox值
		if (_viewer.pubFileCodeStr.indexOf(jQuery(this).val()) >= 0) {
			check(jQuery(this));
		}
	});
}
if (YyspdPkCode.length > 0) {
	// 初始化时过滤公共文档多选框
	filterOdeptPubFile(getOdeptPubFileCodeStr(yyOdept.getValue()));
	// 权限控制
	if (!modPubFileActFlag) {
		yyOdept.disabled();
		pubFileCheckArea.disabled();
	}
}

// 通过后台添加公共文档函数
function addFileToPubList(addFileCode, yyOdeptCode) {
	var data = {
		"mainSpdId" : YyspdPkCode,// 审批单外键
		"addFileCode" : addFileCode,// 添加文件的名称
		"yyOdeptCode" : yyOdeptCode
	// 用印机构编码
	};
	var result = FireFly.doAct(YyspdFilePubServId, "addFileToPubList", data,
			true);
	if (result[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) >= 0) {
		pubFileList.refreshGrid();
	} else {
		alert("添加公共文档失败");
		_viewer.refresh();
	}
}

// 通过后台删除公共文档函数
function delFileFromPubList(delFileCode, yyOdeptCode) {
	var data = {
		"mainSpdId" : YyspdPkCode,// 审批单外键
		"delFileCode" : delFileCode,// 删除文件的名称
	};
	var result = FireFly.doAct(YyspdFilePubServId, "delFileFromPubList", data,
			true);
	if (result[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) >= 0) {
		pubFileList.refreshGrid();
	} else {
		alert("取消公共文档失败");
		_viewer.refresh();
	}
}

// 通过后台添加私有材料函数
function addFileToPriList(addFileCodes, addFileIds) {
	var data = {
		"mainSpdId" : YyspdPkCode,// 审批单外键
		"addFileCodes" : addFileCodes,// 添加文件的名称
		"addFileIds" : addFileIds
	// 添加文件的编码
	};
	var result = FireFly.doAct(YyspdFilePubServId, "addFileToPriList", data,
			true);
	if (result[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) >= 0) {
		priFileList.refreshGrid();
	} else {
		alert("添加用印材料失败");
		_viewer.refresh();
	}
}

// 通过前端获取当前卡片页公共文档列表公共文档名称拼成的字符串
function _getPubFileCodeStr() {
	_viewer.pubFileCodeStr = "";
	var fileCodeTds = pubFileList.grid.getTdItems("YY_FILE_CODE");
	jQuery.each(fileCodeTds, function(i, n) {
		_viewer.pubFileCodeStr = _viewer.pubFileCodeStr + ","
				+ jQuery(this).text();
	});
	if (_viewer.pubFileCodeStr.length > 0) {
		_viewer.pubFileCodeStr = _viewer.pubFileCodeStr.substr(1);
	}
}

// 通过后台获取机构对应的公共文档字符串
function getOdeptPubFileCodeStr(yyOdeptCode, yyOdeptName) {
	var pubFiles = _getOdeptPubFileCodes(yyOdeptCode, yyOdeptName);
	var pubFileCodeStr = "";
	jQuery.each(pubFiles, function(i, n) {
		if (i == 0) {
			pubFileCodeStr = pubFileCodeStr + n.NAME;
		} else {
			pubFileCodeStr = pubFileCodeStr + "," + n.NAME;
		}
	});
	return pubFileCodeStr;
}

// 通过后台获取机构对应的公共文档数组
function _getOdeptPubFileCodes(yyOdeptCode, yyOdeptName) {
	var data = {
		"_ROWNUM_" : 1000, // 取消分页
		"_searchWhere" : " AND S_ODEPT = '" + yyOdeptCode
				+ "' AND FILE_ID is not null" // 查询条件
	};
	var result = FireFly.getListData("OA_YY_PUB_FILE", data);
	if (result[UIConst.RTN_MSG].indexOf(UIConst.RTN_ERR) < 0
			&& result[UIConst.RTN_MSG].indexOf(UIConst.RTN_WARN) < 0) {
		return result._DATA_;
	} else {
		alert("获取机构对应的公共文档异常");
		return [];
	}
}

// 清除公共文档
function clearPubFiles() {
	// 清除多选框
	jQuery.each(checkBoxs, function(i, n) {
		var chk = jQuery(n);
		unCheck(chk, false);
	});
	// 清除列表
	var filePkTd = pubFileList.grid.getTdItems("YFID");
	var wailDelFilePkStr = "";
	jQuery.each(filePkTd, function(i, n) {
		if (i == 0) {
			wailDelFilePkStr = wailDelFilePkStr + jQuery(this).text();
		} else {
			wailDelFilePkStr = wailDelFilePkStr + "," + jQuery(this).text();
		}
	});
	if (wailDelFilePkStr.length > 0) {
		var data = {
			"_PK_" : wailDelFilePkStr
		}
		FireFly.listDelete(YyspdFilePubServId, data, false);
		pubFileList.refreshGrid();
	}
}

// 通过前端针对公共文档字段过滤
function filterOdeptPubFile(odeptPubFileCodeStr) {
	// 处理多选框
	jQuery.each(checkBoxs, function(i, n) {
		if (jQuery(this).attr("disabled") == "disabled") {
			if (odeptPubFileCodeStr.indexOf(jQuery(this).val()) >= 0) {
				unDisableChk(jQuery(this));
			}
		} else {
			if (odeptPubFileCodeStr.indexOf(jQuery(this).val()) < 0) {
				disableChk(jQuery(this));
			}
		}
	});
}

// 选取多选框
function check(jObj, unChkFlag) {
	if (!unChkFlag) {
		jObj.attr('checked', true);
	}
	jObj.next().css("color", "blue");
}

// 反选多选框
function unCheck(jObj, unChkFlag) {
	if (!unChkFlag) {
		jObj.attr('checked', false);
	}
	if (jObj.attr("disabled") != "disabled") {
		jObj.next().css("color", "black");
	}
}

// 生效多选框
function unDisableChk(chkObj) {
	chkObj.removeAttr("disabled");
	chkObj.next().css("color", "black");
	chkObj.removeAttr("title");
	chkObj.next().removeAttr("title");
}

// 失效多选框
function disableChk(chkObj) {
	chkObj.attr("disabled", "disabled");
	chkObj.next().css("color", "gray");
	chkObj.attr("title", "请添加相应用印机构的" + chkObj.val());
	chkObj.next().attr("title", "请添加相应用印机构的" + chkObj.val());
}

/**
 * 构建行按钮
 * 
 * @param pkCode
 *            数据主键
 * @param actCode
 *            按钮编码
 * @param actName
 *            按钮名称
 * @param imgClass
 *            按钮样式
 * @param func
 *            按钮操作
 * @param obj
 *            TR对象
 * @return
 */
function _bldRowBtn(pkCode, actCode, actName, imgClass, func, obj) {
	var btn = jQuery('<a class="rh-icon rhGrid-btnBar-a" id="OA_YY_YYSPD_FILE-'
			+ actCode + '" actcode="' + actCode
			+ '"><span class="rh-icon-inner">' + actName
			+ '</span><span class="rh-icon-img btn-' + imgClass
			+ '"></span></a>');
	btn.bind("click", {
		"id" : pkCode,
		"trObj" : obj
	}, func);
	return btn;
}

priFileList.afterRefresh = function() {
	// 权限控制
	if (!delPriFileActFlag) {
		priFileList.getBtn("delete").hide();
		priFileList.grid.hideCheckBoxColum();
	}
	if (!readPriFileActFlag) {
		priFileList.obj.find("table").attr("canRead", "no");
	}
	if (!Browser.isMobileOS()) {
		// 登陆用户机构编码
		var dept_codez = System.getVar("@DEPT_CODE@");
		
		// 构造用印材料列表行按钮
		jQuery
				.each(
						this.grid.getBodyTr(),
						function(i, n) {
							var optTdObj = jQuery(n).find(
									'td[icode="OPERATION_S"]');
							if (!modPriPrintNumActFlag) { // 权限控制
								var numInputObj = jQuery(n).find(
										'input[icode="YY_FILE_NUM"]');
								numInputObj.attr("disabled", "disabled");
							}
							// 用印组织机构 操作标识SEAL_ODEPTS
							var sealedFileId = jQuery(n).find(
									'td[icode="SEALED_FILE_ID"]').text();
							var cancelSeal = jQuery(n).find(
									'td[icode="CANCELSEAL"]').text();
							var sealodepts = jQuery(n).find(
									'td[icode="SEAL_ODEPTS"]').text();
							var fid = jQuery(n).find('td[icode="YFID"]').text();
							var Identification = 2;
							if (typeof (sealodepts) == "undefined"
									|| sealodepts == 0 || sealodepts == null) {
								Identification = 0;
							} else if (sealodepts.indexOf(dept_codez) != -1) {
								var len = dept_codez.length;
								Identification = sealodepts.substr(sealodepts
										.indexOf(dept_codez)
										+ len + 1, 1);
							}

							if (sealedFileId.length == 0) {
								if (sealPriFileActFlag && Identification != 1) { // 权限控制
									_bldRowBtn(n.id, "seal", "签章", "search",
											seal, n).appendTo(optTdObj);
								}
							} else if (sealedFileId.length > 0
									&& Identification != 0
									&& Identification != 1) {
								// if (sealedFileId.length == 0) {
								if (sealPriFileActFlag) { // 权限控制
									_bldRowBtn(n.id, "seal", "签章", "search",
											seal, n).appendTo(optTdObj);
								}
							} else if (sealedFileId.length > 0
									&& Identification == 0) {
								if (sealPriFileActFlag) { // 权限控制
									_bldRowBtn(n.id, "seal", "签章", "search",
											seal, n).appendTo(optTdObj);
								}
							} else {
								if (cancelSealPriFileActFlag) { // 权限控制
									_bldRowBtn(n.id, "undoSeal", "取消签章",
											"search", undoSeal, n).appendTo(
											optTdObj);
								}
							}
							if (Identification != 0) {
								if (printPriFileActFlag) { // 权限控制
									_bldRowBtn(n.id, "printSealedFile", "打印",
											"search", printSealedFile, n)
											.appendTo(optTdObj);
									// _bldRowBtn(n.id,"appendPrintNum","追加打印份数","search",appendPrintNum,n).appendTo(optTdObj);
								}
								var yyAppendPrint = jQuery(n).find(
										'td[icode="YY_APPEND_PRINT"]').text()
										|| "false";
								// if (appendPrintActFlag && yyAppendPrint ==
								// "true") {
								if (appendPrintActFlag) {
									// if (!isCallbakcFlag) { //
									// 判断追加打印分数的机构是否为当前机构，后期可以放开 var
									reqdata = {};
									reqdata._PK_ = fid;
									reqdata.servC = YyspdFilePubServId;
									callbackAppendPrintOutBean = FireFly.doAct(
											_viewer.servId,
											"getAddSealPrintOdept", reqdata);
									// isCallbakcFlag = true;
									if (callbackAppendPrintOutBean["_MSG_"]
											.indexOf("OK,") >= 0) { // 存在盖章文件
										if (System.getVar("@DEPT_CODE@") == callbackAppendPrintOutBean["SEAL_ODEPTL"]) {
											_bldRowBtn(n.id, "appendPrintNum",
													"追加打印份数", "search",
													appendPrintNum, n)
													.appendTo(optTdObj);
										}
									}

									/*
									 * _bldRowBtn(n.id, "appendPrintNum",
									 * "追加打印份数", "search", appendPrintNum,
									 * n).appendTo(optTdObj);
									 */

								}

							}
						});
	}
};
priFileList.afterRefresh();
pubFileList.afterRefresh = function() {
	// 权限控制
	if (!modPubFileActFlag) {
		pubFileList.getBtn("delete").hide();
		pubFileList.grid.hideCheckBoxColum();
	}
	if (!readPubFileActFlag) {
		pubFileList.obj.find("table").attr("canRead", "no");
	}
	if (!Browser.isMobileOS()) {
		// 登陆用户机构编码
		var dept_codez = System.getVar("@DEPT_CODE@");
		// 构造公共文档列表行按钮
		jQuery
				.each(
						pubFileList.grid.getBodyTr(),
						function(i, n) {
							var optTdObj = jQuery(n).find(
									'td[icode="OPERATION_S"]');
							if (!modPubPrintNumActFlag) { // 权限控制
								var numInputObj = jQuery(n).find(
										'input[icode="YY_FILE_NUM"]');
								numInputObj.attr("disabled", "disabled");
							}
							var sealedFileId = jQuery(n).find(
									'td[icode="SEALED_FILE_ID"]').text();

							var cancelSeal = jQuery(n).find(
									'td[icode="CANCELSEAL"]').text();
							var sealodepts = jQuery(n).find(
									'td[icode="SEAL_ODEPTS"]').text();
							var fid = jQuery(n).find('td[icode="YFID"]').text();
							var Identification = 2;

							if (typeof (sealodepts) == "undefined"
									|| sealodepts == 0 || sealodepts == null) {
								Identification = 0;
							} else if (sealodepts.indexOf(dept_codez) != -1) {
								var len = dept_codez.length;
								Identification = sealodepts.substr(sealodepts
										.indexOf(dept_codez)
										+ len + 1, 1);
							}

							if (sealedFileId.length == 0) {
								if (sealPriFileActFlag && Identification != 1) { // 权限控制
									_bldRowBtn(n.id, "seal", "签章", "search",
											seal, n).appendTo(optTdObj);
								}
							} else if (sealedFileId.length > 0
									&& Identification != 0
									&& Identification != 1) {
								// if (sealedFileId.length == 0) {
								if (sealPriFileActFlag) { // 权限控制
									_bldRowBtn(n.id, "seal", "签章", "search",
											seal, n).appendTo(optTdObj);
								}
							} else if (sealedFileId.length > 0
									&& Identification == 0) {
								if (sealPriFileActFlag) { // 权限控制
									_bldRowBtn(n.id, "seal", "签章", "search",
											seal, n).appendTo(optTdObj);
								}
							} else {
								if (cancelSealPriFileActFlag) { // 权限控制
									_bldRowBtn(n.id, "undoSeal", "取消签章",
											"search", undoSeal, n).appendTo(
											optTdObj);
								}
							}
							if (Identification != 0) {

								if (printPriFileActFlag) { // 权限控制
									_bldRowBtn(n.id, "printSealedFile", "打印",
											"search", printSealedFile, n)
											.appendTo(optTdObj);
									// _bldRowBtn(n.id,"appendPrintNum","追加打印份数","search",appendPrintNum,n).appendTo(optTdObj);
								}
								var yyAppendPrint = jQuery(n).find(
										'td[icode="YY_APPEND_PRINT"]').text()
										|| "false";
								// if (appendPrintActFlag && yyAppendPrint ==
								// "true") {
								if (appendPrintActFlag) {
									// if (!isCallbakcFlag) { //
									// 判断追加打印分数的机构是否为当前机构，后期可以放开 var
									reqdata = {};
									reqdata._PK_ = fid;
									reqdata.servC = YyspdFilePubServId;
									callbackAppendPrintOutBean = FireFly.doAct(
											_viewer.servId,
											"getAddSealPrintOdept", reqdata);
									// isCallbakcFlag = true;

									if (callbackAppendPrintOutBean["_MSG_"]
											.indexOf("OK,") >= 0) { // 存在盖章文件
										if (System.getVar("@DEPT_CODE@") == callbackAppendPrintOutBean["SEAL_ODEPTL"]) {
											_bldRowBtn(n.id, "appendPrintNum",
													"追加打印份数", "search",
													appendPrintNum, n)
													.appendTo(optTdObj);
										}
									}

									/*
									 * _bldRowBtn(n.id, "appendPrintNum",
									 * "追加打印份数", "search", appendPrintNum,
									 * n).appendTo(optTdObj);
									 */

								}

							}
						});
	}
};
pubFileList.afterRefresh();

// 修改用印材料样式
var btnBarObj1 = _viewer.getItem("YY_PRI_FILE").obj
		.find("div[class='rhGrid-btnBar']");
if ((btnBarObj1.html() || "").length <= 0) {
	btnBarObj1.hide();
} else {
	var isExitKj = false; // 是否存在可见按钮
	btnBarObj1.find("a").each(function() {
		if (jQuery(this).is(":visible")) {
			isExitKj = true;
			return false;
		}
	});
	if (!isExitKj) {
		btnBarObj1.hide();
	} else {
		btnBarObj1.css({
			"border" : "1px solid #CCC",
			"border-bottom" : "0px",
			"background-color" : "white"
		});
	}
}
_viewer.getItem("YY_PRI_FILE").obj.find("thead[class='rhGrid-thead']").css({
	"font-size" : "1em"
});

// 修改用印公共文档
var btnBarObj2 = _viewer.getItem("YY_PUB_FILE_NUM").obj
		.find("div[class='rhGrid-btnBar']");
if ((btnBarObj2.html() || "").length <= 0) {
	btnBarObj2.hide();
} else {
	var isExitKj = false; // 是否存在可见按钮
	btnBarObj2.find("a").each(function() {
		if (jQuery(this).is(":visible")) {
			isExitKj = true;
			return false;
		}
	});
	if (!isExitKj) {
		btnBarObj2.hide();
	} else {
		btnBarObj2.css({
			"border" : "1px solid #CCC",
			"border-bottom" : "0px",
			"background-color" : "white"
		});
	}
}
_viewer.getItem("YY_PUB_FILE_NUM").obj.find("thead[class='rhGrid-thead']").css(
		{
			"font-size" : "1em"
		});

/**
 * 选择印章弹出页面
 */
yySealNameObj._choose.unbind("click").bind(
		"click",
		function() {
			var outSealBean = FireFly.doAct(_viewer.servId, "getSealOdept", {
				"addUser" : _viewer.getItem("S_USER").getValue()
			});
			window.open(System.getVar("@C_SEAL_SYS_HOST@")
					+ "/general/modules/system/dept/deptSeal.jsp?deptid="
					+ outSealBean["ODEPT_CODE"] + "&url="
					+ System.getVar("@C_SYS_HOST_ADDR@")
					+ "/oa/zh/returnChooseSeal.jsp&sealids="
					+ _viewer.getItem("YY_SEALS").getValue(), "newwindow");
		});

/**
 * 清除数据
 */
yySealNameObj._cancel.unbind("click").bind("click", function() {
	yySealNameObj.setValue("");
	_viewer.getItem("YY_SEALS").setValue("");
});

/**
 * 获取印章系统选择印章
 * 
 * @param {Object}
 *            sealIds
 */
window.getSealNamesCallBack = function(sealIds) {
	var sealArray = (sealIds || "").split(";");
	if (sealArray.length != 2) {
		_viewer.cardBarTipError("获取选择印章出错");
		return;
	}
	_viewer.getItem("YY_SEALS").setValue(sealArray[0]);
	yySealNameObj.setValue(sealArray[1]);
}

/**
 * 对指定文件盖章
 * 
 * @sealParams 从服务器端获取的盖章文件信息
 */
function seal(event) {
	// 校验是否保存
	var changeData = _viewer.getChangeData();
	if (!jQuery.isEmptyObject(changeData)) {
		alert("页面数据已修改，请先保存或恢复修改");
		return false;
	}
	// 校验印章字段
	var yySealsVal = _viewer.itemValue("YY_SEALS");
	if (yySealsVal.length == 0) {
		alert("请先选择印章");
		return false;
	}
	var sealParams = undefined;
	try {
		sealParams = _getSealFileInfo(event);
	} catch (e) {
		alert(e.message);
		return;
	}
	var yySeal = new rh.vi.rhYySeal(_viewer);
	yySeal.seal(sealParams);
}

/**
 * 取得盖章文件信息，请求当前卡片对应服务的“getSealFileInfo”方法，获取盖章文件的信息
 */
function _getSealFileInfo(event) {
	var trObj = event.data.trObj;
	var yfid = jQuery('td[icode="YFID"]', trObj).text();
	var reqdata = {};
	reqdata.PK_FORM = YyspdPkCode;
	reqdata.PK_FILE = yfid;
	var result = FireFly.doAct(YyspdFilePubServId, "getSealFileInfo", reqdata);
	return result;
}

/**
 * 取消盖章
 */
function undoSeal(event) {
	
	//var trObj = event.data.trObj;
	//var yfid = jQuery('td[icode="YFID"]', trObj).text();
	// var param = {
	// "PK_FILE": yfid
	// };
	var param = undefined;
	try {
		param = _getSealFileInfo(event)
	} catch (e) {
		alert(e.message);
		return;
	}
	var yySeal = new rh.vi.rhYySeal(_viewer);
	yySeal.undoSeal(param);
}

/**
 * 打印盖章文件
 */
function printSealedFile(event) {
	var self = this;
	var result = _getPrintFileInfo(event);
	var yySeal = new rh.vi.rhYySeal(_viewer);
	yySeal.printSealFile(result);
}

/**
 * 追加打印份数
 */
function appendPrintNum(event) {
	var self = this;
	var result = _getPrintFileInfo(event);
	var yySeal = new rh.vi.rhYySeal(_viewer);
	yySeal.appendPringNum(result);
}

/**
 * 获取打印文件的信息
 */
function _getPrintFileInfo(event) {
	var trObj = event.data.trObj;
	var yfid = jQuery('td[icode="YFID"]', trObj).text();
	var reqdata = {};
	reqdata.PK_FORM = YyspdPkCode;
	reqdata.PK_FILE = yfid;
	var result = FireFly.doAct(YyspdFilePubServId, "getPrintFileInfo", reqdata);
	return result;
}