GLOBAL.namespace("rh.vi");

rh.vi.gwExtCardView = function(options) {
	this.parHandler = options.parHandler || null;

	// 工作流按钮处理对象
	this.wfCard = this.parHandler.wfCard || null;

	// 对应公文的相关数据
	this.gwData = this.parHandler.form.getItemsValues() || null;
	
	//公文模板定义
	this.tmpl = this.parHandler.byIdData.tmpl;

	// 审批单操作按钮集合
	this.btnCodes = [ {
		"name" : "套红头",
		"code" : "redHat"
	}, {
		"name" : "文档一体",
		"code" : "wenDangYiTi"
	}, {
		"name" : "转换类型",
		"code" : "conversion"
	}, {
		"name" : "扫描正文",
		"code" : "scan"
	}, {
		"name" : "电子印章",
		"code" : "seal"
	}, {
		"name" : "取消印章",
		"code" : "undoSeal"
	}, {
		"name" : "加密",
		"code" : "encrypt"
	}, {
		"name" : "解密",
		"code" : "decrypt"
	}, {
		"name" : "公文管理",
		"code" : "fileGuanLi"
	}, {
		"name" : "预览红头文件",
		"code" : "previewRedHead"
	}, {
		"name" : "打印正文",
		"code" : "printZw"
	}
	]

}

/**
 * 初始化所有审批单按钮及表单的相关信息
 */
rh.vi.gwExtCardView.prototype.init = function() {
	var self = this;
	var canSeal = this.parHandler.form._data["CAN_SEAL"];
	var canRedhead = this.parHandler.form._data["CAN_REDHEAD"];
	var canUndoSeal = this.parHandler.form._data["CAN_UNDOSEAL"];
	if (this.wfCard != null) {
		this.wfCard.beforeCmSimpleFenFa = function(actItem,params){
			params["userSelectDict"] = "SY_ORG_DEPT_USER";
			params["displaySendSchm"] = true;
			params["includeSubOdept"] = false;
			return true;
		}
		for ( var i in this.btnCodes) {
			var btn = this.wfCard._getBtn(this.btnCodes[i].code);
			(function(i) {
				if (btn) {
					var actItem = btn.dataObj;
					btn.layoutObj.unbind("click").bind(
							"click",
							function(event) {
								if (actItem.ACT_MEMO.length > 1
										&& actItem.ACT_MEMO != "null") {
									var func = new Function(dataObj.ACT_MEMO);
									func.apply(self);
								} else {
									eval("self." + actItem.ACT_CODE
											+ "(event,actItem)");
								}
							});
					if (actItem.ACT_CODE == "redHat") {
						if (canRedhead != "true" || self.wfCard.isLocked()) {
							btn.layoutObj.css("display","none");
						}
					} else if (actItem.ACT_CODE == "seal") {
						if (canSeal != "true" || self.wfCard.isLocked()) {
							btn.layoutObj.css("display","none");
						}
					} else if (actItem.ACT_CODE == "undoSeal") {
						
						if (canUndoSeal != "true" || self.wfCard.isLocked()) {
							btn.layoutObj.css("display","none");
						}
					}
				}
			})(i)
		}
	}

}

/**
 * 套红头
 */
rh.vi.gwExtCardView.prototype.redHat = function(event, actItem,opts) {

	var self = this;
	var _opts = opts;
	/**
	 * 套红头 ， 套红头之前，需要先选择成文模板 ， 点按钮出一个dialog ，可以选择成文模板，确定的时候，再提交
	 */
	var redHead = function(event) {
		// 查询成文模板的列表
		var reqCwData = {};
		var gwYearCodeItemValue = self.parHandler.itemValue("GW_YEAR_CODE");
		if (gwYearCodeItemValue == undefined || gwYearCodeItemValue == "") {
			alert("文件没有编号，不能套红头");
			return false;
		};

		reqCwData["GW_YEAR_CODE"] = gwYearCodeItemValue;

		var cwTmplRtnList = FireFly.doAct("OA_GW_CODE_CW_TMPL",
				"getCwTmplListByCode", reqCwData);

		var cwListObj = cwTmplRtnList.cwList;

		if (cwListObj.length == 1) { // 如果只有一个模板，直接套
			var orgFileId = cwListObj[0].FILE_ID;
			var realFileId = orgFileId.split(",")[0];
			_doRedHeadConfirm(realFileId);
		} else { // 多个，则选择
			var htmlArr = new Array();
			htmlArr.push("<div class='selectDialog' id='chengWenTmplListDiv' title='成文模板'>");
			htmlArr.push("<div class='ml20 mt20'>");
			htmlArr.push("<div class='fl'>请选择一个成文模板:</div>");
			htmlArr.push("<div class='fl ml5'><select id='templateSelectName' class='ui-se1lect-default2' name='templateSelectName'>");
			
			jQuery.each(cwTmplRtnList.cwList, function(i, cwTmp) {
				// 将FILE_ID 进行截串
				var orgFileId = cwTmp.FILE_ID;
				var realFileId = orgFileId.split(",")[0];
				htmlArr.push("<option value='" + realFileId + "'>");
				htmlArr.push(cwTmp.CW_NAME);
				htmlArr.push("</option>");
			});
			
			htmlArr.push("</select></div></div></div>");
			
			var winDialog = jQuery(htmlArr.join(""));
			
			htmlArr = null;
			
			var posArray = [];
			if (event) {
				var cy = event.clientY;
				posArray[0] = "";
				posArray[1] = cy - 120;
			}
			winDialog.appendTo(jQuery("body"));
			jQuery("#chengWenTmplListDiv").dialog({
				autoOpen : false,
				width : 400,
				height : 260,
				modal : true,
				resizable : false,
				position : posArray,
				open : function() {

				},
				close : function() {
					jQuery(this).remove();
				},buttons: {
					"确认": function() { // 取到选中的成文模板
						_doRedHeadConfirm(jQuery("#templateSelectName").val());
						jQuery(this).remove();
					},
					"关闭": function() {
						jQuery(this).remove();
					}
				}
				
			});
			jQuery("#chengWenTmplListDiv").dialog("open");
			jQuery(".ui-dialog-titlebar").last().css("display", "block");
			winDialog = null;
		}
	}

	/**
	 * 套红头
	 */
	var _doRedHeadConfirm = function(cwTmplFileId) {
		var servDataId = self.parHandler._pkCode;

		var servId = self.parHandler.servId; //
		var fileInfo = self._getRedheadFileInfo();
		var source = fileInfo.wengaoId; //文稿文件ID
		var target = fileInfo.zhengwenId; //上传红头文件的ID
		if(!source || source.length == 0){
			Tip.show("无效的文稿");
			return;
		}
		var result = self.parHandler.byIdData;
		var redhatUrl = "/oa/gw/office/doGwRedHead.jsp?gwId=" + servDataId
				+ "&servId=" + servId + "&source=" + source + "&target="
				+ target + "&cwTmplFileId=" + cwTmplFileId
				+ "&wfNiId=" + self.wfCard.getNodeInstBean().NI_ID ;
		if(_opts && _opts.readonly){
			redhatUrl += "&readonly=true";
		}
		window["_redHatArgs"] = result;
		
		var winHtml = new Array();
		winHtml.push("<div style='width:400;height:100;' title='套红头' id='");
		winHtml.push("gwRedHead'><iframe style='width:99%;height:99%;border:0px;' id='redhead-includeJSP' src='");
		//winHtml.push(redhatUrl);
		winHtml.push("' />");
		winHtml.push("</div>");
		jQuery("body").append(winHtml.join(""));
		winHtml = null;
		
		jQuery("#gwRedHead").dialog({modal: false,width:400,height:100,position: { my: "top", at: "top", of: ".form-container" }});

		jQuery("#redhead-includeJSP").attr("src",redhatUrl);
		
		jQuery(document).data("_viewer",self.parHandler);
		//回调事件
		if(typeof(window["closeGwRedHead"]) != "function"){
			window["closeGwRedHead"] = function(opts){
				try{
					//关闭Dialog
					jQuery("#gwRedHead").dialog( "close" );
					Tools.destroyIframe("redhead-includeJSP");
					jQuery("#redhead-includeJSP").remove();
					jQuery("#gwRedHead").remove();
					if(opts && opts.readonly){
						//只读不刷新页面
					}else{
						//刷新页面
						jQuery(document).data("_viewer").refresh();						
					}
					window["closeGwRedHead"] = null;
				}catch(e){
					//throw e;
				}
			}
		}
	}

	redHead(event);
}

/**
 * 锁定
 */
rh.vi.gwExtCardView.prototype.lock = function(event, actItem) {

}

/**
 * 解锁
 */
rh.vi.gwExtCardView.prototype.unlock = function(event, actItem) {

}

/**
 * 文档一体
 */
rh.vi.gwExtCardView.prototype.wenDangYiTi = function(event, actItem) {

}

/**
 * 转换类型
 */
rh.vi.gwExtCardView.prototype.conversion = function(event, actItem) {
	var _self = this;
    //弹出  选择 公文类型的 选择框  
    //var extWhereStr = " and SERV_ID != ^" + this.parHandler.servId + "^"; //查询的时候，去除自己
	var inputName = "servCodes";
	var configStr = "OA_GW_CONVERT_TYPE,{'TARGET':'SERV_ID~SERV_NAME','SOURCE':'TMPL_CODE~TMPL_NAME','PKHIDE':true,'TYPE':'single'}";
	var options = {"itemCode":inputName,
		"config" :configStr,
		"rebackCodes":inputName,
		"parHandler":this,
		"formHandler":this,
		"replaceCallBack":function(gwTypeObjs){
			    _self._doConvert(gwTypeObjs.TMPL_CODE, gwTypeObjs.TMPL_NAME);
			},
		"params":{"TMPL_CODE":this.parHandler.servId},
		"showSearchFlag":"false"
	};
	var queryView = new rh.vi.rhSelectListView(options);
	queryView.show(event, null, [500,400]);	
}

/**
 * @param convertToServId 需要转换成的服务
 * @param convertToServName 需要转成的服务名称
 */
rh.vi.gwExtCardView.prototype._doConvert = function (convertToServId, convertToServName) {
	var ifConfirm = confirm("是否确定将文件转成 " + convertToServName + ", 转换之后文件位于起草点。");
	
	if (!ifConfirm) {
		return;
	}
	
	var data = {};
	data.SERV_ID = this.parHandler.servId;
	data.DATA_ID = this.parHandler._pkCode;  //当前数据的主键
	data.TO_SERV_ID = convertToServId; 
	data.NI_ID = this.wfCard.getNodeInstBean().NI_ID;
	
	var resultData = FireFly.doAct("OA_GW_TYPE_FW", "conversion", data);
	
	if (resultData[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) { // 转换成功
		alert("转换成功！");
		
		var URL = convertToServId + ".card.do?pkCode=" + this.parHandler._pkCode;
		var options = {
				"url":URL,
				"tTitle":convertToServName,
				"menuFlag":"4"};
		Tab.open(options);
		
		Tab.close();
	} else {
		alert("转换出现错误，" + resultData[UIConst.RTN_MSG]);
	}
}


/**
 * 扫描正文
 */
rh.vi.gwExtCardView.prototype.scan = function(event, actItem) {

}

/**
 * 电子印章
 */
rh.vi.gwExtCardView.prototype.seal = function(event, actItem) {
	var self = this;
	var sealType =  actItem.WFE_PARAM;
	
	if(sealType == "SHUSHENG"){ //书生盖章功能
		var servDataId = self.parHandler._pkCode;
		var servId = self.parHandler.servId; //
		var source = self._getZhengWenId(); // 文件ID
		
		var urlStr = "/oa/gw/wfeExt/gwSeal.jsp?gwId=" + servDataId + "&servId="
		+ servId + "&source=" + source + "&sealType=" + actItem.WFE_PARAM;

		var sealUrl = encodeURI(urlStr);
		
		var rtnObj = window
				.showModalDialog(sealUrl, "电子印章",
						"dialogWidth:700px;dialogHeight:300px;center:yes;resizable:yes;status=no");
		
		if (rtnObj) {
			this.wfCard.getParHandler().refresh();
		}	
	} else { //中华保险吉大正源印章接口 //(sealType == "ZH_JD")
		var sealParams = undefined;
		try{
			sealParams = this.getSealFileInfo();
		} catch(e){
			alert(e.message);
			return;
		}
		try{
			var gwSeal = new rh.vi.rhGwSeal(self.parHandler);
			gwSeal.seal(sealParams);
		}catch(e){
			if(typeof(e) == "string"){
				alert(e);
				return;
			}
			if(e && e.message) {
				alert(e.message);
			}
		}
	}
}

/**
 * 取消印章
 */
rh.vi.gwExtCardView.prototype.undoSeal = function(event, actItem) {
	var gwSeal = new rh.vi.rhGwSeal(this.parHandler);
	gwSeal.undoSeal();
}

/**
 * 取得盖章文件信息，请求当前卡片对应服务的“getSealFileInfo”方法，获取盖章文件的信息。
 */
rh.vi.gwExtCardView.prototype.getSealFileInfo = function() {
	var reqdata = {};

	var formServId = this.parHandler.servId;
	reqdata.PK_CODE = this.parHandler._pkCode;
	reqdata.TMP_CODE = formServId;

	var result = FireFly.doAct(formServId, "getSealFileInfo", reqdata);

	return result;
}

/**
 * 加密
 */
rh.vi.gwExtCardView.prototype.encrypt = function(event, actItem) {

}

/**
 * 解密
 */
rh.vi.gwExtCardView.prototype.decrypt = function(event, actItem) {

}

/**
 * 预览红头
 */
rh.vi.gwExtCardView.prototype.previewRedHead = function(event,actItem){
	var redheadInfo = this._getRedheadFileInfo();
	if(redheadInfo.previewRedHead == "zhengwen"){
		readOfficeFile(redheadInfo.zhengwenId,"/file/" + redheadInfo.zhengwenId,false);
	}else{
		this.redHat(event,actItem,{readonly:true});
	}
}

/**
 * 取得套红头文件的ID
 */
rh.vi.gwExtCardView.prototype._getRedheadFileInfo = function(){
	return this.parHandler.getByIdData("redheadFileInfo") || {};
}

/**
 * 取到公文的正文ID
 */
rh.vi.gwExtCardView.prototype._getZhengWenId = function() {
	var reqdata = {};

	var formServId = this.parHandler.servId;
	reqdata.PK_CODE = this.parHandler._pkCode;
	reqdata.TMP_CODE = formServId;

	var result = FireFly.doAct(formServId, "getGwZhengWenId", reqdata);

	return result.zhengWenId;
}

/**
 * 相关文件
 */
rh.vi.gwExtCardView.prototype.getRelate = function(arr, _viewer,selectHandler) {
	
	this.getDialog(event, "relate");
	this.showRelateItems(arr, _viewer,selectHandler);
}
rh.vi.gwExtCardView.prototype.getDialog = function(event, dialogId) {
	// 设置jqueryUi的dialog参数
	var winDialog = jQuery("<div style='background-color:#F5FAFD;'></div>")
			.addClass("selectDialog").attr("id", dialogId)
			.attr("title", "相关文件");
	winDialog.appendTo(jQuery("body"));
	var hei = 200;
	var wid = 300;
	var posArray = [ 30, 30 ];
	if (event) {
		var cy = event.clientY;
		posArray[0] = "";
		posArray[1] = cy - 300;
	}

	// 生成jqueryUi的dialog
	jQuery("#" + dialogId).dialog({
		autoOpen : false,
		height : hei,
		width : wid,
		modal : true,
		resizable : false,
		position : posArray,
		open : function() {

		},
		close : function() {
			jQuery("#" + dialogId).remove();
		}
	});

	// 手动打开dialog
	var dialogObj = jQuery("#" + dialogId);
	dialogObj.dialog("open");
	dialogObj.focus();
	jQuery(".ui-dialog-titlebar").last().css("display", "block");// 设置标题显示
	dialogObj.parent()
			.addClass("rh-bottom-right-radius rhSelectWidget-content");
	Tip.show("努力加载中...", null, jQuery(".ui-dialog-title", winDialog)
			.last());
}
rh.vi.gwExtCardView.prototype.showRelateItems = function(data, _viewer,selectHandler) {
	// relateDiv
	var relateDiv = jQuery("<div></div>").attr("id", "relate");
	// headLabel
	var headLabel = jQuery("<div>请选择</div>").addClass(
			"rh-vi-gwExtCardView-relate-text").attr("id", "head_td").attr(
			"colspan", "2");
	var relateTable = jQuery("<table border= 1></table>").addClass(
			"rh-vi-gwExtCardView-relate-table");
	// 关联审批单
	var relateForm = jQuery("<tr><td><label><input type='radio' name='relate' value='Form' /><span>关联审批单</span><label></td></tr>");
	relateTable.append(relateForm);
	// 关联附件
	var relateFujian = jQuery("<tr><td><label><input type='radio' name='relate' value='FUJIAN' /><span>关联附件</span><label></td></tr>");
	relateTable.append(relateFujian);
	
	// 关联转发批文
	/*var relateForward = jQuery("<tr><td><label><input type='radio' name='relate' value='ZHUANFA' /><span>关联转发批文</span><label></td></tr>");
	relateTable.append(relateForward);*/
	
	var enterText = jQuery("<span>确认</span>").addClass("rh-icon-inner");
	var enterImg = jQuery("<span></span>").addClass("rh-icon-img btn-add");
	var enterBtn = jQuery("<a></a>").addClass(
			"rh-icon rhGrid-btnBar-a rh-vi-gwExtCardView-relate-a").attr("id",
			"enter_btn");
	relateDiv.append(headLabel);
	relateDiv.append(jQuery("<br />"));
	// 追加table
	relateDiv.append(relateTable);
	enterBtn.append(enterText);
	enterBtn.append(enterImg);
	relateDiv.append(enterBtn);

	jQuery("#relate").append(relateDiv);
	jQuery("#relate label").addClass("rh-vi-gwExtCardView-relate-label");
	jQuery("#relate input").addClass("rh-vi-gwExtCardView-relate-input");
	// 为按钮添加单击事件
	enterBtn.unbind("click").bind(
			"click",
			function() {
				var datas = {};
				var len = data.length;
				for ( var i = 0; i < len; i++) {
					datas[i] = data[i];
				}
				jQuery.extend(datas, {
					'datasLen' : len,
					'DATA_ID' : _viewer._pkCode
				});
				var relateType = jQuery(
						"#relate input:radio[name='relate']:checked").val();
				// 判断用户选择那个选项
				if (relateType == "FUJIAN") {
					jQuery.extend(datas, {
						'relateType' : 'FUJIAN'
					})
					FireFly.doAct(_viewer.servId, "getRelate", datas);
					_viewer.getItem("FUJIAN").refresh();
				} else if (relateType == "ZHUANFA") {
					jQuery.extend(datas, {
						'relateType' : 'ZHUANFA'
					})
					FireFly.doAct(_viewer.servId, "getRelate", datas);
					_viewer.getItem("ZHUANFA").refresh();
				} else if(relateType == "Form"){
					selectHandler.buildList(data);
				}
				jQuery("#relate").remove();				
			});
}

/**
 * 打印正文
 */
rh.vi.gwExtCardView.prototype.printZw = function(event,actItem){
	var self = this;
	var result = this._getPrintFileInfo();
	var gwSeal = new rh.vi.rhGwSeal(self.parHandler);
	gwSeal.printSealFile(result);
}

/**
 * 获取打印文件的信息
 */
rh.vi.gwExtCardView.prototype._getPrintFileInfo = function(){
	var reqdata = {};
	var formServId = this.parHandler.servId;
	reqdata.PK_CODE = this.parHandler._pkCode;
	reqdata.TMP_CODE = formServId;

	var result = FireFly.doAct(formServId, "getPrintFileInfo", reqdata,false);

	return result;
}
