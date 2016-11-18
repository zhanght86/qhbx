GLOBAL.namespace("rh.seal");
Load.scriptJS("/sy/util/office/zotnClientLib_NTKO.js");
rh.seal.sealFileOper = function(options) {
	this.servId = "BN_SEAL_FILE_LIST";
	this.applyId = options.applyId || "";
	this.parHandler = options.parHandler || null;
	this.parentNode = options.parentNode || null;
	this.applyType = options.applyType || "";
	this.canDownLoadFile = options.canDownLoadFile || "false";
	this.canSeal = options.canSeal || "false";
	this.canAddSeal = options.canAddSeal || "false";
	this.canEditable = options.canEditable || "false";
	this.canPrint = options.canPrint || "false";
}

rh.seal.sealFileOper.prototype.render = function() {
	this._data = FireFly.doAct(this.servId,"finds",{"APPLY_ID":this.applyId,"FILE_TYPE":"0"});
	this.parentDom = $("#" + this.parHandler.servId + "-" + this.parentNode + "_div").find("span[class='ui-staticText-default disabled']");
	
	//构建说明区域
	this.memo();
	
	//构建文件列表区域
	this.createFileListLayout();
}

rh.seal.sealFileOper.prototype.memo = function() {
	$("<div></div>").html("用印材料相关说明：").attr("style","font-weight:bold;color:#cd0a0a").appendTo(this.parentDom);
	$("<div></div>").html("&nbsp;&nbsp;1. 上传文件格式必须为doc；").attr("style","color:#cd0a0a").appendTo(this.parentDom);
	$("<div></div>").html("&nbsp;&nbsp;2. 单个附件大小不得超过10M；").attr("style","color:#cd0a0a").appendTo(this.parentDom);
	$("<div></div>").html("&nbsp;&nbsp;3. 复选框选中，表示印章将加盖骑缝章；").attr("style","color:#cd0a0a").appendTo(this.parentDom);
}

rh.seal.sealFileOper.prototype.createFileListLayout = function() {
	if(this._data && this._data["_DATA_"]) {
		var sealFileList = this._data["_DATA_"];
		for(var i = 0; i < sealFileList.length; i++) {
			this.createFileLayout(sealFileList[i],i+1);
		}
	}
}

rh.seal.sealFileOper.prototype.createFileLayout = function(fileData,index) {
	var _self = this;
	var table = $("<table></table>").attr("style","width:100%;border:1px solid #cccccc").appendTo(this.parentDom);
	//构建第一行内容
	var tr = $("<tr></tr>").attr("style","width:100%").appendTo(table);
	var td = $("<td></td>").attr({"style":"background-color:rgb(250, 229, 203);","colspan":"2"}).appendTo(tr);
	var fileTable = $("<table></table>").attr("style","width:100%;").appendTo(td);
	var fileTr = $("<tr></tr>").attr("style","width:100%").appendTo(fileTable);
	var fileObj = fileData.FILE_OBJ.split(",");
	var fileName = $("<td></td>").attr("style","color:#222222;cursor: pointer;").html(index + ". " + fileObj[1].substring(0,fileObj[1].indexOf(";"))).appendTo(fileTr);
	fileName.unbind("dblclick").bind("dblclick",function(){
		_self.viewSealFile(fileData);
	});
	var fileOper = $("<td></td>").attr({"width":"300px","style":"cursor: pointer;"}).appendTo(fileTr);
	var viewFile = $("<span style='color:red;font-weight:bold;padding:4px;white-space:nowrap;'>[查看]</span>").appendTo(fileOper);
	viewFile.unbind("click").bind("click",function(){
		_self.viewOnlineFile(fileData);
	});
	if(this.canAddSeal == "true") {
		var addSeal = $("<span style='color:red;font-weight:bold;padding:4px;white-space:nowrap;'>[添加印章]</span>").appendTo(fileOper);
		addSeal.unbind("click").bind("click",function(){
			Tab.open({
			"url" : "BN_SEAL_USE_LIST.card.do",
			"tTitle" : "添加印章",
			"menuFlag" : 4,
			"params" : {
				"APPLY_ID" : fileData.APPLY_ID,
				"FILE_ID" : fileData.FILE_ID,
				"callBackHandler" : _self.parHandler,
				"closeCallBackFunc" : function() {
					_self.parHandler.refresh();
				}
			}
		});
		});
	}
	if(this.canDownLoadFile == "true") {
		var download = $("<span style='color:red;font-weight:bold;padding:4px;white-space:nowrap;'>[下载]</span>").appendTo(fileOper);
		download.unbind("click").bind("click",function(){
			_self.downFile(fileData);
		});
	}
	if(this.canEditable == "true") {
		var deleteFile = $("<span style='color:red;font-weight:bold;padding:4px;white-space:nowrap;'>[删除]</span>").appendTo(fileOper);
		deleteFile.unbind("click").bind("click",function(){
			_self.deleteFile(fileData);
		});
	}
	if(this.canPrint == "true" || System.getVar("@LOGIN_NAME@") == "admin") {
		var printFile = $("<span title='打印份数为"+fileData.FILE_PRINT_NUM+"' style='color:red;font-weight:bold;padding:4px;white-space:nowrap;'>[打印]</span>").appendTo(fileOper);
		printFile.unbind("click").bind("click",function(){
			_self.printFile(fileData);
		});
	}
	//构建用印文件相关说明
	var trPrint = $("<tr></tr>").attr("style","width:100%").appendTo(table);
	$("<td>打印份数："+fileData.FILE_PRINT_NUM+"</td>").attr("colspan","2").appendTo(trPrint);
	var trDemo = $("<tr></tr>").attr("style","width:100%").appendTo(table);
	$("<td>盖章位置描述："+fileData.FILE_SEAL_MEMO+"</td>").attr("colspan","2").appendTo(trDemo);
	//构建第四行内容
	var tr = $("<tr></tr>").attr("style","width:100%").appendTo(table);
	//var td = $("<td></td>").attr("colspan","2").html("<div style='color:#cd0a0a'>&nbsp;印章列表（选中表示将加盖骑缝章）</div>").appendTo(tr);
	var td = $("<td></td>").attr("colspan","2").appendTo(tr);
	this.createSealListLayout(fileData,td);
	
}

rh.seal.sealFileOper.prototype.createSealListLayout = function(fileData,parent) {
	var _self = this;
	this._sealData = FireFly.doAct("BN_SEAL_USE_LIST","finds",{"FILE_ID":fileData.FILE_ID,"APPLY_ID":fileData.APPLY_ID});
	if(this._sealData && this._sealData["_DATA_"]) {
		var sealTable = $("<table></table>").attr("style","width:100%;").appendTo(parent);
		var sealList = this._sealData["_DATA_"];
		for(var i = 0; i < sealList.length; i++) {
			var seal = sealList[i];
			var sealTr = $("<tr></tr>").appendTo(sealTable);
			var sealTd = $("<td></td>").attr("width","1%").appendTo(sealTr);
			var sealTd = $("<td></td>").attr("width","40%").appendTo(sealTr);
			var qfChk = $("<input type='checkbox' style='height:13px;cursor: pointer;' title='是否加盖骑缝章'/>").attr({"id":"IF_QIF_SEAL"+seal.SL_ID,"name":"IF_QIF_SEAL"+fileData.FILE_ID,"value":seal.SL_ID}).appendTo(sealTd);
			if(seal.IF_QIF_SEAL == 1) {
				qfChk.attr("checked",true);
			}
			if(seal.USE_STATUS==1){
				qfChk.attr("disabled",true);
			}
			qfChk.attr("sealName",seal.SEAL_NAME);
			qfChk.unbind("click").bind("click",function(event){
				var alertStr = "是否加盖'" + $(event.target).attr("sealName") + "'为骑缝章?";
				var ifQfSeal = 1;
				if(!$(event.target).attr("checked")) {
					alertStr = "是否取消加盖'" + $(event.target).attr("sealName") + "'为骑缝章?";
					ifQfSeal = 0;
				}
				if(confirm(alertStr)) {
					var slId = $(event.target).attr("value");
					_self.parHandler.cardBarTipLoad("加盖指定印章为骑缝章！");
					FireFly.doAct("BN_SEAL_USE_LIST","save",{"_PK_":slId,"IF_QIF_SEAL":ifQfSeal,"FILE_ID":fileData.FILE_ID});
					$("table",parent).remove();
					_self.createSealListLayout(fileData, parent);
					_self.parHandler.cardClearTipLoad();
				} else {
					$("table",parent).remove();
					_self.createSealListLayout(fileData, parent);
					_self.parHandler.cardClearTipLoad();
				}
			});
			if(this.canEditable != "true") {
				qfChk.attr("disabled",true);
			}
			//用印方式
			var sealType = seal.SEAL_TYPE==1?"电子章":"实物章";
			var sealName = $("<span></span>").html("&nbsp;"+(i+1) + ". " + seal.SEAL_NAME+ "（" + sealType + "）").appendTo(sealTd);
			var sealTd = $("<td></td>").attr("width","59%").appendTo(sealTr);
			if(this.canSeal == "true" && seal.SEAL_TYPE==1 && seal.USE_STATUS==0) {
				var sealing = $("<span style='color:red;cursor:pointer;font-weight:bold;padding-left:4px;white-space:nowrap;'>[盖章]</span>").appendTo(sealTd);
				sealing.attr("value",seal.SEAL_ID);
				sealing.attr("slId",seal.SL_ID);
				sealing.unbind("click").bind("click",function(event){
					var isQfz = $("#IF_QIF_SEAL"+$(event.target).attr("slId")).attr("checked")?true:false;
					_self.fileSeal(fileData,$(event.target).attr("value"),isQfz);
				});
			}
			if(this.canEditable == "true" && seal.USE_STATUS==0){
				var deleteOper = $("<span>[删除]</span>").attr({"style":"color:red;font-weight:bold;cursor: pointer;padding-left:4px;white-space:nowrap;","value":seal.SL_ID}).appendTo(sealTd);
					deleteOper.attr("name",seal.SEAL_NAME);
					deleteOper.unbind("click").bind("click",function(event){
						if(confirm("是否要删除印章:" + $(event.target).attr("name") + "?")) {
							var slId = $(event.target).attr("value");
							_self.parHandler.cardBarTipLoad("删除指定印章！");
							FireFly.doAct("BN_SEAL_USE_LIST","delete",{"_PK_":slId});
							$("table",parent).remove();
							_self.createSealListLayout(fileData, parent);
							_self.parHandler.cardClearTipLoad();
						}
					});
			}
		}
	}
}

rh.seal.sealFileOper.prototype.viewSealFile = function(fileData) {
	var _self = this;
	var readOnly = false;
	if(this.canEditable != "true") {
		readOnly = true;
	}
	Tab.open({
		"url" : "BN_SEAL_FILE_LIST.card.do?pkCode=" + fileData.FILE_ID,
		"tTitle" : "用印文件",
		"menuFlag" : 4,
		"params" : {
			"APPLY_ID" : fileData.APPLY_ID,
			"APPLY_TYPE" : fileData.APPLY_TYPE,
			"READ_ONLY" : readOnly,
			"callBackHandler" : this.parHandler,
			"closeCallBackFunc" : function() {
				_self.parHandler.refresh();
			}
		}
	});
}

rh.seal.sealFileOper.prototype.viewOnlineFile = function(fileData) {
	var fileObj = fileData.FILE_OBJ.split(",");
	readOfficeFile(fileObj[1].substring(0,fileObj[1].indexOf(";")), "/file/" + fileObj[0], false);
}

rh.seal.sealFileOper.prototype.printFile = function(fileData) {
	var fileObj = fileData.FILE_OBJ.split(",");
	var params = {"fileId":fileObj[0],"dataId":this.parHandler._pkCode,"servId":this.parHandler.servId,"printNum":fileData.FILE_PRINT_NUM};
	readOfficeFileExt(fileObj[1].substring(0,fileObj[1].indexOf(";")), "/file/" + fileObj[0], false, true, params);
}

rh.seal.sealFileOper.prototype.fileSeal = function(fileData,sealId,isQfz) {
	var _self = this;
	FireFly.doAct("BN_SEAL_BASIC_INFO_LIST","byid",{"_PK_":sealId},false,false,function(data){
		var fileObj = fileData.FILE_OBJ.split(",");
		var uploadUrl = "/file/" + fileObj[0] + "?keepMetaData=true";
		uploadUrl = uploadUrl + "&model=saveHist";
		var _officeParams = {"revision":false,"isSeal":true,"isQfz":isQfz,"dataId":_self.parHandler._pkCode,"fileId":fileObj[0],"servId":_self.parHandler.servId,"sealID":data.ID,"sealIndex":data.EKEY_ADDRESS,"eKeySN":data.SEAL_CODE};
		editOfficeFileExt(fileObj[1].substring(0,fileObj[1].indexOf(";")), "/file/" + fileObj[0], uploadUrl, _officeParams);
	});
}

rh.seal.sealFileOper.prototype.downFile = function(fileData) {
	var fileInfo = fileData.FILE_OBJ.split(",");
	window.open("/file/" + fileInfo[0]);
	//zotnClientNTKO.DownloadFile("/file/" + fileInfo[0], fileInfo[1].substring(0,fileInfo[1].indexOf(";")), false, true, true, false);
}

rh.seal.sealFileOper.prototype.deleteFile = function(fileData) {
	if(confirm("确定删除该用印文件吗？")){
		FireFly.doAct("BN_SEAL_FILE_LIST","delete",{"_PK_":fileData.FILE_ID});
		this.parHandler.refresh();
	}
}