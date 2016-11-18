var _viewer = this;
//文件名去掉链接
_viewer.grid.getCheckBox().each(function(i,item){	
	jQuery(jQuery("td[icode='FILE_OBJ']")[i].children).removeAttr("href");
});
if(_viewer.opts.parHandler){
	if(_viewer.opts.parHandler.wfCard){
		//取得工作流变量
		//允许下载用印文件
		var canDownLoadFile = _viewer.opts.parHandler.wfCard.getCustomVarContent("canDownLoadFile") || "false";
		//允许盖章
		var canSeal = _viewer.opts.parHandler.wfCard.getCustomVarContent("canSeal") || "false";
		//允许添加印章
		var canAddSeal = _viewer.opts.parHandler.wfCard.getCustomVarContent("canAddSeal") || "false";
		//允许编辑用印文件
		var canEditable = _viewer.opts.parHandler.wfCard.getCustomVarContent("canEditable") || "false";
		//允许打印用印文件
		var canPrint = _viewer.opts.parHandler.wfCard.getCustomVarContent("canPrint") || "false";
		//退回起草人也应该是起草节点
		var qiCao = _viewer.opts.parHandler.wfCard.getCustomVarContent("qiCao") || "false";
		
		//是否为起草节点和退回起草人节点
		if(!_viewer.opts.parHandler.wfCard.isDraftNode() && !(qiCao == "true")) {
			//非起草节点及退回起草人节点，不允许添加、删除、修改用印文件信息
			jQuery("#"+_viewer.servId).find(".rhGrid-btnBar").hide();
			_viewer.grid.hideCheckBoxColum();
			_viewer._cardRead=true;
			//列表置为只读
			jQuery(_viewer.grid._table).find("input[type='text']").each(function(){
				jQuery(this).parent().html(jQuery(this).val());
			});
		}
		jQuery.each(_viewer.grid.getBodyTr(), function(i, n) {
			var opTd = jQuery(n).find("td[icode='OPERATE_COLUMN']");
			var btnDiv = jQuery("<div width='90px'></div>").appendTo(opTd);
			var rowId = jQuery(n).attr("id");
			var applyId = _viewer.grid.getRowItemValue(rowId, "APPLY_ID");
			var fileId = _viewer.grid.getRowItemValue(rowId, "FILE_ID");
			var fileObj = _viewer.grid.getRowItemValue(rowId, "FILE_OBJ");
			var printNum = _viewer.grid.getRowItemValue(rowId, "FILE_PRINT_NUM");
			if(canAddSeal == "true"){
				var addSeal = $("<span style='float:left;color:red;font-weight:bold;padding:4px;white-space:nowrap;'>[添加印章]</span>").appendTo(btnDiv);
				addSeal.unbind("click").bind("click",function(){
					Tab.open({
					"url" : "BN_SEAL_USE_LIST.card.do",
					"tTitle" : "添加印章",
					"menuFlag" : 4,
					"params" : {
						"APPLY_ID" : applyId,
						"FILE_ID" : fileId,
						"callBackHandler" : _viewer,
						"closeCallBackFunc" : function() {
							_viewer.refresh();
						}
					}
					});
				});
			}
			//获取印章
			var _sealData = FireFly.doAct("BN_SEAL_USE_LIST","finds",{"FILE_ID":fileId,"APPLY_ID":applyId});
			if(_sealData && _sealData["_DATA_"]) {
				var sealList = _sealData["_DATA_"];
				var sealNames = "";
				var sealULIds = "^";
				for(var i = 0; i < sealList.length; i++) {
					if(i>0){
						sealNames += "，";
						sealULIds += "^,^";
					}
					sealNames += sealList[i].SEAL_NAME;
					if(sealList[i].SEAL_TYPE==1){
						sealNames += "(电子章)";
					}else{
						sealNames += "(实物章)";
					}
					sealULIds += sealList[i].SL_ID;
				}
				sealULIds += "^";
				jQuery(n).find("td[icode='SEAL_NAME']").html(sealNames);
				if(canEditable == "true"){
					//删除印章按钮
					var delSeal = $("<span style='float:left;color:red;font-weight:bold;padding:4px;white-space:nowrap;'>[删除印章]</span>").appendTo(btnDiv);
					delSeal.unbind("click").bind("click",function(){
						var configStr = "BN_SEAL_USE_LIST,{'TARGET':'SL_ID~SEAL_ID~SEAL_NAME~IF_QIF_SEAL','HIDE':'SL_ID~SEAL_ID','HIDEOPTION':'ID','TYPE':'single','SOURCE':'SL_ID~SEAL_ID~SEAL_NAME~IF_QIF_SEAL','EXTWHERE':' and SL_ID in("+sealULIds+")'}";	
						var options = {"config" :configStr,"rebackCodes":"SL_ID~SEAL_ID~SEAL_NAME~IF_QIF_SEAL","parHandler":_viewer,"formHandler":_viewer,"replaceCallBack":function(result){
							if(result.SL_ID){
								//删除所选印章
								if(confirm("是否要删除印章:" + result.SEAL_NAME + "?")) {
									FireFly.doAct("BN_SEAL_USE_LIST","delete",{"_PK_":result.SL_ID});
									_viewer.refresh();
								}
							}else{
								alert("未能找到印章信息");
							}
						},"hideAdvancedSearch":true};
						var queryView = new rh.vi.rhSelectListView(options);
						queryView.show(null,[50,0]);
					});
				}
				if(canSeal == "true"){
					//添加盖章按钮
					var toSeal = $("<span style='float:left;color:red;font-weight:bold;padding:4px;white-space:nowrap;'>[文件盖章]</span>").appendTo(btnDiv);
					toSeal.unbind("click").bind("click",function(){
						var configStr = "BN_SEAL_USE_LIST,{'TARGET':'SEAL_ID~SEAL_NAME~IF_QIF_SEAL','HIDE':'SEAL_ID','HIDEOPTION':'ID','TYPE':'single','SOURCE':'SEAL_ID~SEAL_NAME~IF_QIF_SEAL','EXTWHERE':'and SEAL_TYPE=1 and USE_STATUS=0 and SL_ID in("+sealULIds+")'}";	
						var options = {"config" :configStr,"rebackCodes":"SEAL_ID~SEAL_NAME~IF_QIF_SEAL","parHandler":_viewer,"formHandler":_viewer,"replaceCallBack":function(result){
							if(result.SEAL_ID){
								//获取印章信息
								FireFly.doAct("BN_SEAL_BASIC_INFO_LIST","byid",{"_PK_":result.SEAL_ID},false,false,function(data){
									var fileObjArr = fileObj.split(",");
									var uploadUrl = "/file/" + fileObjArr[0] + "?keepMetaData=true";
									uploadUrl = uploadUrl + "&model=saveHist";
									var _officeParams = {"revision":false,"isSeal":true,"isQfz":result.IF_QIF_SEAL,"dataId":_viewer.opts.parHandler._pkCode,"fileId":fileObjArr[0],"servId":_viewer.opts.parHandler.servId,"sealID":data.ID,"sealIndex":data.EKEY_ADDRESS,"eKeySN":data.SEAL_CODE};
									editOfficeFileExt(fileObjArr[1].substring(0,fileObjArr[1].indexOf(";")), "/file/" + fileObjArr[0], uploadUrl, _officeParams);
								});
							}else{
								alert("未能找到印章信息");
							}
						},"hideAdvancedSearch":true};
						var queryView = new rh.vi.rhSelectListView(options);
						queryView.show(null,[50,0]);
					});
				}
				if(canPrint == "true" || System.getVar("@LOGIN_NAME@") == "admin"){
					//打印用印文件
					var printFile = $("<span style='float:left;color:red;font-weight:bold;padding:4px;white-space:nowrap;'>[打印文件]</span>").appendTo(btnDiv);
					printFile.unbind("click").bind("click",function(){
						var fileObjArr = fileObj.split(",");
						var params = {"fileId":fileObjArr[0],"dataId":_viewer.opts.parHandler._pkCode,"servId":_viewer.opts.parHandler.servId,"printNum":printNum};
						readOfficeFileExt(fileObjArr[1].substring(0,fileObjArr[1].indexOf(";")), "/file/" + fileObjArr[0], false, true, false, params);
					});
				}
				if(canDownLoadFile == "true"){
					//下载用印文件
					var downLoadFile = $("<span style='float:left;color:red;font-weight:bold;padding:4px;white-space:nowrap;'>[下载文件]</span>").appendTo(btnDiv);
					downLoadFile.unbind("click").bind("click",function(){
						var fileObjArr = fileObj.split(",");
						window.open("/file/" + fileObjArr[0]);
					});
				}
			}
		});
	}
}
