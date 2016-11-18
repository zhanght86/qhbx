(function(_viewer){

	_viewer.beforeSave = function(){
		var ctBtime = _viewer.getItem("CT_BTIME").getValue();
		var ctEtime = _viewer.getItem("CT_ETIME").getValue();
		if(rhDate.doDateDiff("T",ctBtime,ctEtime,0) < 0){
			alert("起始时间不能大于终止时间");
			 Tip.clearLoad();
			return false;
		}
	};
	
	//在合同起草审批单中要隐藏
	_viewer.getItem("CT_CHANGE_NAME").hide();
	_viewer.getItem("CT_CHANGE_DESC").hide();
	_viewer.getBtn("confirmChange").hide();

	var CT_STATE = _viewer.getItem("CT_STATE").getValue();
	if("1"==CT_STATE){
		_viewer.getItem("CT_STATE").show();
	}else{
	//如果为新合同，则隐藏“变更后合同”关联标签
		_viewer.tabHide("LW_CT_CONTRACT_CHANGE");
	}
	
	
	
	var servId = _viewer.servId;
	var pk =  _viewer.getItem("CT_ID").getValue();
	
//	if (_viewer.wfCard) {
//		_viewer.wfCard._beforeSongjiao = function(){
//			var data = {};
//			data["CT_ID"] = pk;
//			var result = FireFly.doAct("LW_CT_SIGNATORY", "finds", data)._DATA_;
//			if (result.length == 0) {
//				alert("签约对方情况还没有填写！");
//				return false;
//			}
//		};
//	}
	
	// 人民币小写转大写
	var ctEmoney = _viewer.getItem("CT_EMONEY");
	var ctCmoney = _viewer.getItem("CT_CMONEY");
	try {
		ctEmoney.getObj().blur(function(){
			var val = jQuery(this).val();
			if (val && val.length > 0) {
				try {
					ctCmoney.setValue(Format.RMBCapital(val));
				} catch (e) {
					alert(e);
				}
			}
		});
	} catch(e) {
		alert(e);
	}
	
	var zhengWen = _viewer.form.getAttachFile("ZHENGWEN");
	// 上传文件之前检测是否已经有了正文
//	zhengWen.beforeUploadCallback = function() {
//		if (!jQuery.isEmptyObject(zhengWen.getFileData())) {
//			if (confirm("已经有一个正文，是否覆盖？")) {
//				zhengWen.setFileId(getZhengWenFileId());
//				return true;
//			} else {
//				return false;
//			}
//		}
//		return true;
//	};
	
	// 能上传文件则可以选择合同范本
	if (zhengWen.calcAcl(zhengWen._acl)[0]) {
		// 选择合同范本
		var addTmplBtn = jQuery("<span class='ct-template'><a class='rh-icon rhGrid-btnBar-a' actcode='addTmpl'>" +
				"<span class='rh-icon-inner' style='padding:0 6px 2px 0px;'>合同范本</span></a></span>");
		zhengWen.getObj().find(".modifyBtn").first().after(addTmplBtn);
		addTmplBtn.click(function(){
			var isCover = false;
//			if (!jQuery.isEmptyObject(zhengWen.getFileData())) {
//				if (confirm("已经有一个正文，是否覆盖？")) {
//					isCover = true;
//				} else {
//					return;
//				}
//			}
			var inputName = "xxxx";
			var configStr = "LW_CT_TEMPLATE,{'PKHIDE':true,'TARGET':'TP_ID~TP_NAME~TP_TYPE~TP_MEMO~S_ATIME','SOURCE':'TP_ID~TP_NAME~TP_TYPE~TP_MEMO~S_ATIME','TYPE':'single','HIDE':'TP_ID'}";
			var options = {
					"itemCode":inputName,
					"config" :configStr,
					"rebackCodes":inputName,
					"parHandler":this,
					"formHandler":this,
				    "replaceCallBack":function(tpl){
				    	var data = {};
				    	data[UIConst.PK_KEY] = pk;
				    	data["TP_ID"] = tpl.TP_ID; 
				    	// 覆盖文件
//				    	if (isCover) {
//				    		data["FILE_ID"] = getZhengWenFileId();
//				    	}
				    	if (_viewer.wfCard && _viewer.wfCard.getNodeInstBean()) {
							var wfNIId = _viewer.wfCard.getNodeInstBean().NI_ID;
							if (wfNIId && wfNIId.length > 0) {
								data["WF_NI_ID"] = wfNIId;
							}
				    	}
				    	var result = FireFly.doAct(servId, "copyTemplate", data);
				    	if (result[UIConst.RTN_MSG].indexOf(UIConst.RTN_ERR) >= 0) {
				    		Tip.showError(result[UIConst.RTN_MSG], true);
				    	} else {
				    		Tip.show(result[UIConst.RTN_MSG], true);
				    		//为避免先选择合同范本后，再上传正文无DATA_ID，在卡片初始化时，自动给正文对象附上DATA_ID
				    		zhengWen.setDataId(pk);
				    		zhengWen.refresh();
				    	}
				    }	
				};
				var queryView = new rh.vi.rhSelectListView(options);
				queryView.show();
		});
	}
	
	// 给清稿按钮注册点击事件
	if (_viewer.wfCard) {
		var cleanCopyBtn = _viewer.wfCard._getBtn("cleanCopy");
		if (cleanCopyBtn) {
			cleanCopyBtn.layoutObj.unbind("click").click(function(){
				if (jQuery.isEmptyObject(zhengWen.getFileData())) {
					Tip.showError("该合同没有正文，请拟制正文！", true);
				} else {
					if (confirm("请确定是否对合同正文进行“清稿”处理：\n提示：主办部门每次对会签部门意见汇总处理后，再次送其他部门或领导会签或审批时，应进行“清稿”处理，生成不带痕迹的合同文本，便于审批时查阅。")) {
						var data = {};
						data[UIConst.PK_KEY] = pk;
						FireFly.doAct(servId, "cleanCopy", data, true, true, function(result){
							if(result.FILE_COPY_ID && result.FILE_COPY_ID.length > 0) {
								try {
									doCleanCopy(result._DATA_);
								} catch(e) {
									var data = {};
									data["FILE_ID"] = result.FILE_COPY_ID;
									data["CT_CVERSION"] = result.CT_CVERSION;
									FireFly.doAct(servId, "doCleanCopyFailure", data, false);
									Tip.clear();
									if (e.message.indexOf("IE") >= 0) {
										Tip.showError("只有IE浏览器才支持清稿操作！", true);
									} else {
										Tip.showError(e, true);
									}
								} 
							} 
						});
					}
				}
			});
		}
	}
	
	// 清稿
	function doCleanCopy(fileBeans) {
		//有多个正文 需要每个正文都同时清稿
		for ( var i = 0; i < fileBeans.length; i++) {
			var fileBean = fileBeans[i];//在传入的bean中获取其中一个
			var fileId = fileBean.FILE_ID;
			var ZotnClient = FireFly.getZotnClient();
			if (ZotnClient) {
				Tip.show("正在进行合同清稿，请稍候...", true);
				//不能通过 fileId 去判断 是否是 word文件， 因为杨瑜测试的时候， 先传了一个pdf, 再传一个word,出了问题了，第二次上传的文件还是用的之前pdf的那个id
				//if (fileId.toUpperCase().indexOf(".DOC") > 0 || fileId.toUpperCase().indexOf(".DOCX") > 0) {
				if (fileBean.FILE_MTYPE.toUpperCase().indexOf("WORD") > 0) {
					var downloadUrl = FireFly.getHostURL() + "/file/" + fileId;
					var uploadUrl = FireFly.getHostURL() + "/file/" + fileId + "?keepMetaData=true&data={'SERV_ID':'" + fileBean.SERV_ID +"','DATA_ID':'" + fileBean.DATA_ID + "','FILE_CAT':'" + fileBean.FILE_CAT + "','FILE_MTYPE':'" + fileBean.FILE_MTYPE + "'}";
					try {
						var _fileName = ZotnClient.DownloadFile(downloadUrl, fileBean.FILE_NAME, false, false, true, false);
						ZotnClient.UploadFile(uploadUrl, _fileName, false, false, false);
						Tip.show("清稿成功！", true);
						var xiuGaiGaoFile = _viewer.form.getAttachFile("XIUGAIGAO");
						if (xiuGaiGaoFile) {
							xiuGaiGaoFile.refresh();
						}
					} catch(e) {
						throw new Error(e.message);
					}
				} else {
					//throw new Error("只能对word文档执行清稿操作！");
					alert("只能对word文档执行清稿操作！");
				}
			} else {
				alert("获取ZotnClient对象失败！");
				return;
			}
		}
	}
	
	/**
	 * 获取正文文件ID
	 */
	function getZhengWenFileId() {
		var fileDatas = zhengWen.getFileData();
		for (var fileId in fileDatas) {
			alert(fileId);
			return fileId;
		}
		return null;
	}
	
	//添加说明信息，由于在卡片中缺省值长度不够，无法写入要求的长度字符串，故在此做操作 hdy 2013-6-27 10:26
	var momeObj = jQuery.parseJSON(System.getVar("@C_SY_LIST_MEMO_CONFIG_OBJ@") || "{}");
	if ("" != (momeObj[_viewer.servId] || "")) {
		_viewer.getItem("MEMO_TITLE").setValue(momeObj[_viewer.servId]);
	}
	//备注显示样式
	_viewer.form.getItem("MEMO_TITLE").obj.parent().removeClass("disabled").css({"background":"url('')"});
	_viewer.form.getItem("MEMO_TITLE").obj.css({"color":"black","font-weight":"bolder","background-color":"white","font-size":"14px"});
	
	//重载传阅参数
	if (_viewer.wfCard != null) {
		_viewer.wfCard.beforeCmSimpleFenFa = function(actItem,params){
			params["userSelectDict"] = "SY_ORG_DEPT_USER";
			params["displaySendSchm"] = true;
			params["includeSubOdept"] = false;
			return true;
		}
	}
	
	//如果文件被锁定，则隐藏清稿按钮
	//判断流程是否开始
	if(_viewer.wfCard && _viewer.wfCard.isWorkflow()){
		var params = {
			//获取流程节点
			"S_WF_INST":_viewer.itemValue("S_WF_INST"),
		}
		var isLocked = FireFly.doAct(_viewer.servId,"isLocked",params);
		//获取文件是否被锁定，如果锁定
		if(isLocked.INST_LOCK=="1"){
			//得到清稿按钮
	//		var cleaCopy = jQuery("div[class='funcBtnDivCon']").find("a[class='rh-icon rhGrid-btnBar-a rhCard-btnBar-wf']");
			var cleaCopy = jQuery("#LW_CT_CONTRACT-cleanCopy");
			//隐藏清稿按钮
			cleaCopy.hide();  
		}
	}
	
	
	// 如果当前节点为编号节点，则显示编号
//	if(_viewer.wfCard.isWorkflow() && _viewer.wfCard.getCustomVarContent("gwCode")){
		//设置机关代字
//		var yearCode = _viewer.form.getItem("CT_WORD");
//	alert("获取机关代字："+yearCode);
////		_viewer.form.getItem("CT_WORD").getContainer().remove();
////		yearCode.obj.css({"display":"inline-block","width":"100px","border":"1px solid #91BDEA"}).appendTo(td21);
////		var gwCode = jQuery("<span></span>").appendTo(td21);
////		jQuery("<font>（</font>").appendTo(gwCode);
//		_viewer.form.getItem("CT_YEAR").getContainer().remove();
////		_viewer.form.getItem("CT_YEAR").obj.css({"width":"40px","background":"transparent","border-bottom":"1px solid #91BDEA","height":"20px","line-height":"20px"}).appendTo(gwCode);
////		jQuery("<font>）</font>").appendTo(gwCode);
//		_viewer.form.getItem("CT_SERIAL").getContainer().remove();
////		_viewer.form.getItem("CT_SERIAL").obj.css({"width":"40px","background":"transparent","border-bottom":"1px solid #91BDEA","height":"20px","line-height":"20px"}).appendTo(gwCode);
//		jQuery("<font>号</font>").appendTo(gwCode);

		
//		yearCode.addOptions(tmpl["CT_WORDS"]); //将动态获取的机关代字设入下拉框
//		if (_viewer.getByIdData("CT_WORD") != "") {
//			yearCode.setValue(_viewer.byIdData["CT_WORD"]); //设置保存的机关代字
//		}
//
//		yearCode.obj.bind("change", function(event) { 	//机关代字选择事件
//			getMaxCode();
//		});
//		_viewer.form.getItem("CT_YEAR").obj.bind("change", function(event) { //年度修改事件
//			getMaxCode();
//		});
//		function getMaxCode() { //获取机关代字最大值
//			var param = {};
//			param["CT_SERIAL"] = yearCode.getValue();
//			param["CT_WORD"] = _viewer.form.getItem("CT_WORD").getValue();
//			param["_PK_"] = _viewer.getPKCode();
//			param["TMPL_TYPE_CODE"] = _viewer.form.getItem("TMPL_TYPE_CODE").getValue();
//			var yearNumber;
//			if ((param["CT_SERIAL"] == _viewer.getByIdData("CT_SERIAL")) 
//					&& (param["CT_YEAR"] == _viewer.getByIdData("CT_YEAR"))) { //机关代字修改时，选中缺省的代字和年度，保留存储的编号
//				yearNumber = _viewer.getByIdData("CT_SERIAL");
//			} else {
//				var tmpl = FireFly.doAct(_viewer.servId, "getMaxCode", param, false);
//				yearNumber = tmpl["CT_SERIAL"];
//			}
//			_viewer.form.getItem("CT_SERIAL").setValue(yearNumber);
//		}
//	}
	
	//关联交易选择否时 关联详细描述不显示
	if(_viewer.getItem("CT_IS_AT").getValue()==2){
		_viewer.getItem("CT_AT_MEMO").hide();
	}
	
	var xgwj=jQuery("#LW_CT_CONTRACT-GW_RELATE_div").find("div[class='fl wp']").find("span[class='rh-icon-inner']");
	xgwj.html("选择系统相关文件");
	
	var fujian=jQuery("#LW_CT_CONTRACT-FUJIAN").find("span[class='btnName']");
	fujian.html("上传本地文件");
	var zw=jQuery("#LW_CT_CONTRACT-ZHENGWEN").find("span[class='btnName']");
	zw.html("上传本地文件");
	
//	var relateWenjian = jQuery("#OA_SY_COMM_ENTITY_QUERY").find("div[class='content-main']").find("td[icode='TITLE']");
//	relateWenjian.unbind("click").bind("click",function(){
//		var configStr =  "{'servId':'OA_SY_COMM_ENTITY_QUERY','SOURCE':'ENTITY_ID,ENTITY_CODE,TITLE,SERV_ID,S_ATIME,DATA_ID','servName':'综合查询','SHOWITEM':'TITLE','TARGET_SERV_ID_ITEM':'SERV_ID','TARGET_ID_ITEM':'DATA_ID','HIDE':'ENTITY_ID,DATA_ID,SERV_ID','DATAFLAG':'false'}";
//		var options = {
//				"config" :configStr,
//				"title":"综合查询",
//				"replaceCallBack":function(nameArray,strWhere,idArray){
//						alert(66);
//						jQuery("#LW_CT_CONTRACT-GW_RELATE_div").val(idArray.ENTITY_CODE);
//					}
//				};
//	}
	// 初始化盖章按钮
	var gwSeal = new rh.vi.gwSeal({servId:_viewer.servId,parHandler:_viewer});
	gwSeal.init();
})(this);