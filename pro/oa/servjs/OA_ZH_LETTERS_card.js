var _viewer = this;

////字典表联动,保障人字典
//_viewer.getItem("LT_REPORTER")._choose.unbind("click").bind("click",function(event){
//	var configStr = "SY_ORG_DEPT_USER_ALL,{'TYPE':'multi'}";
//		var options = {
//			"config" :configStr,
//			"hide" : "explode",
//			"show" : "blind",
//			"replaceCallBack":function(idArray,nameArray){//回调，idArray为选中记录的相应字段的数组集合
//			 userDictCallBack(idArray,nameArray);
//		}
//	};
//	//2.显示树形
//	var dictView = new rh.vi.rhDictTreeView(options);
//	dictView.show(event);
//	deptCode = "";
//});
//
////绑定[取消]按钮单击事件
//_viewer.getItem("LT_REPORTER")._cancel.unbind("click").bind("click",function(){
//	_viewer.getItem("LT_REPORTER").setValue("");
//	_viewer.getItem("LT_REPORTER").setText("");
//	_viewer.getItem("LT_REPORT_AGENCIES").setValue("");
//	_viewer.getItem("LT_REPORT_AGENCIES").setText("");
//});
//
////用户字典弹出选择回调的方法
//function userDictCallBack(idArray,nameArray) {
//	_viewer.getItem("LT_REPORTER").setValue(idArray.join(","));
//	_viewer.getItem("LT_REPORTER").setText(nameArray.join(","));
//	//点击字典选项
//	if (idArray.length > 0) {
//		//格式化获取数据
//		var paramArray = new Array();
//		for (var i = 0; i < idArray.length; i++) {
//			paramArray.push("'" + idArray[i] + "'");
//		}
//		//后台获取对应机构数据
//		var outDeptBean = FireFly.doAct(_viewer.servId,"linkDicBean",{"DICT_SERV_ID":"SY_ORG_DEPT","USER_CODE":paramArray.join(",")});
//		//获取机构数据
//		var outArray = outDeptBean["_DATA_"] || "";
//		//获取格式化之后的机构代码和名称
//		var deptCodeArray = new Array();
//		var deptNameArray = new Array();
//		if (outArray != "") {
//			for (var i = 0; i < outArray.length; i++) {
//				deptCodeArray.push(outArray[i]["DEPT_CODE"]);
//				deptNameArray.push(outArray[i]["DEPT_NAME"]);
//			}
//		}
//		_viewer.getItem("LT_REPORT_AGENCIES").setValue(deptCodeArray.join(","));
//		_viewer.getItem("LT_REPORT_AGENCIES").setText(deptNameArray.join(","));
//	}
//}
//   //生成正文
//		var buildTextBtn = _viewer.wfCard._getBtn("buildText");
//		var fileArray = new Array();
//		var zhengwen = _viewer.getItem("ZHENGWEN").getValue();
//		var wengaoid ="";
//		for(var id in zhengwen){
//		 fileArray.push(zhengwen[id]["FILE_ID"]);
//		 if(zhengwen[id]["DIS_NAME"]=="文稿"){
//		  wengaoid = zhengwen[id]["FILE_ID"];
//		  //隐藏文稿
//		_viewer.getItem("ZHENGWEN").obj.find("[icode='"+wengaoid+"']").css("display","none");
//		 }
//		}
//		if(fileArray.length>0){
//		 if(buildTextBtn){
//		 buildTextBtn.layoutObj.css("color","#a9a9a9");
//		 }
//		} else {
//		if(buildTextBtn){
//		 buildTextBtn.layoutObj.unbind("click").click(function(event){
//			  //copy正文稿
//	 	   var servDataId = _viewer._pkCode;	
//		   var servId = _viewer.servId;
//		   var param = {};
//		   param["serv"] = servId;
//		   param["_PK_"] = servDataId;
//		  var outBean = FireFly.doAct(servId,"copyZhengwen",param,true,false,function(){
//		  _viewer.refresh();
//		  });  
//		   //生成正文
//		  redHead(event);
//		 });
//		}
//		}
//			
////		//编辑正文
////		var editTextBtn = _viewer.wfCard._getBtn("editText");
////		if(editTextBtn){
////		editTextBtn.layoutObj.unbind("click").bind("click",function(){
////		 redHead(event);
////		 _viewer.refresh();
////		});
////		}
//	/**
//	 * 套红头 ， 套红头之前，需要先选择成文模板 ， 点按钮出一个dialog ，可以选择成文模板，确定的时候，再提交
//	 */
//	var redHead = function(event) {
//		// 查询成文模板的列表
//		var reqCwData = {};
//		var gwYearCodeItemValue = _viewer.itemValue("GW_YEAR_CODE");
//		if (gwYearCodeItemValue == undefined || gwYearCodeItemValue == "") {
//			alert("没有生成机关代字，不能套红头");
//			return false;
//		};
//		reqCwData["GW_YEAR_CODE"] = gwYearCodeItemValue;
//		var cwTmplRtnList = FireFly.doAct("OA_GW_CODE_CW_TMPL",
//				"getCwTmplListByCode", reqCwData);
//		var cwListObj = cwTmplRtnList.cwList;
//		if (cwListObj.length == 1) { // 如果只有一个模板，直接套
//			var orgFileId = cwListObj[0].FILE_ID;
//			var realFileId = orgFileId.split(",")[0];
//			_doRedHeadConfirm(realFileId);
//		} else { // 多个，则选择		
//			var htmlArr = new Array();
//			htmlArr.push("<div class='selectDialog' id='chengWenTmplListDiv' title='成文模板'>");
//			htmlArr.push("<div class='ml20 mt20'>");
//			htmlArr.push("<div class='fl'>请选择一个成文模板:</div>");
//			htmlArr.push("<div class='fl ml5'><select id='templateSelectName' class='ui-se1lect-default2' name='templateSelectName'>");			
//			jQuery.each(cwTmplRtnList.cwList, function(i, cwTmp) {
//				// 将FILE_ID 进行截串
//				var orgFileId = cwTmp.FILE_ID;
//				var realFileId = orgFileId.split(",")[0];
//				htmlArr.push("<option value='" + realFileId + "'>");
//				htmlArr.push(cwTmp.CW_NAME);
//				htmlArr.push("</option>");
//			});
//			htmlArr.push("</select></div></div></div>");		
//			var winDialog = jQuery(htmlArr.join(""));
//			
//			htmlArr = null;
//			
//			var posArray = [];
//			if (event) {
//				var cy = event.clientY;
//				posArray[0] = "";
//				posArray[1] = cy - 120;
//			}
//			winDialog.appendTo(jQuery("body"));
//			jQuery("#chengWenTmplListDiv").dialog({
//				autoOpen : false,
//				width : 400,
//				height : 260,
//				modal : true,
//				resizable : false,
//				position : posArray,
//				open : function() {
//
//				},
//				close : function() {
//					jQuery(this).remove();
//				},buttons: {
//					"确认": function() { // 取到选中的成文模板
//						_doRedHeadConfirm(jQuery("#templateSelectName").val());
//						jQuery(this).remove();
//					},
//					"关闭": function() {
//						jQuery(this).remove();
//					}
//				}
//				
//			});
//			jQuery("#chengWenTmplListDiv").dialog("open");
//			jQuery(".ui-dialog-titlebar").last().css("display", "block");
//			winDialog = null;
//		}
//	}
//
//	/**
//	 * 套红头
//	 */
//	var _doRedHeadConfirm = function(cwTmplFileId) {
//		var servDataId = _viewer._pkCode;
//
//		var servId = _viewer.servId; //
//      
//		var fileInfo = _viewer.getByIdData("redheadFileInfo") || {};
//		var source = fileInfo.wengaoId; //文稿文件ID
//		var target = fileInfo.zhengwenId; //上传红头文件的ID
//		if(!source || source.length == 0){
//			Tip.show("无效的文稿");
//			return;
//		}
//
//		var result = _viewer.byIdData;
//		var redhatUrl = "/oa/gw/office/doXFRedHead.jsp?gwId=" + servDataId
//				+ "&servId=" + servId + "&source=" + source + "&target="
//				+ target + "&cwTmplFileId=" + cwTmplFileId
//				+ "&wfNiId=" + _viewer.wfCard.getNodeInstBean().NI_ID ;
////		if(_opts && _opts.readonly){
////			redhatUrl += "&readonly=true";
////		}
//		
//		window["_redHatArgs"] = result;
//		
//		var winHtml = new Array();
//		winHtml.push("<div style='width:400;height:100;' title='套红头' id='");
//		winHtml.push("gwRedHead'><iframe style='width:99%;height:99%;border:0px;' id='redhead-includeJSP' src='");
//		//winHtml.push(redhatUrl);
//		winHtml.push("' />");
//		winHtml.push("</div>");
//		jQuery("body").append(winHtml.join(""));
//		winHtml = null;
//		
//		jQuery("#gwRedHead").dialog({modal: false,width:400,height:100,position: { my: "top", at: "top", of: ".form-container" }});
//		
//		jQuery("#redhead-includeJSP").attr("src",redhatUrl);
//		
//		jQuery(document).data("_viewer",_viewer);
//		//回调事件
//		if(typeof(window["closeGwRedHead"]) != "function"){
//			window["closeGwRedHead"] = function(opts){
//				try{
//					//关闭Dialog
//					jQuery("#gwRedHead").dialog( "close" );
////					if(opts && opts.readonly){
////						//只读不刷新页面
////					}else{
//						//刷新页面
//						jQuery(document).data("_viewer").refresh();						
////					}
//					
//					// 销毁iframe
//					var el = document.getElementById("redhead-includeJSP");
//					if(el){
//					    el.src = 'about:blank';
//					    try {
//					    	var iframe = el.contentWindow;
//					        iframe.document.write('');
//					        iframe.document.clear();
//					        iframe.close();
//					    } catch(e) {
//					    }
//					}
//				}catch(e){
//					throw e;
//				}
//			}
//		}
//	}
//	//转换成中文日期，格式【二〇一三年十〇月十六日】
//var cnDate =function(date) 
//	{ 
//		var cn = ["〇","一","二","三","四","五","六","七","八","九"]; 
//		var s = []; 
//		var YY = date.getFullYear().toString(); 
//		for (var i=0; i<YY.length; i++) 
//		if (cn[YY.charAt(i)]) 
//		s.push(cn[YY.charAt(i)]); 
//		else 
//		s.push(YY.charAt(i)); 
//		s.push("年"); 
//		var MM = date.getMonth() + 1; 
//		if (MM<10) 
//		s.push(cn[MM]); 
//		else if (MM<20) 
//		s.push("十" + cn[MM% 10]); 
//		s.push("月"); 
//		var DD = date.getDate(); 
//		if (DD<10) 
//		s.push(cn[DD]); 
//		else if (DD<20) 
//		s.push("十" + cn[DD% 10]); 
//		else 
//		s.push("二十" + cn[DD% 10]); 
//		s.push("日"); 
//		return s.join(''); 
//   }
//   var lttime = _viewer.itemValue("LT_TIME");
//   var cnstrdate = "";
//   if(lttime !=""){
//   var dateobj = rhDate.strToDate(lttime);
//   cnstrdate = cnDate(dateobj);
//   }
	var fileItem = _viewer.getItem("ZHENGWEN");
	if(fileItem){
		fileItem.beforeEditFile = function(){
			window["_replaceBookmark"] = {"LT_CONTENT":_viewer.itemValue("LT_CONTENT"),
			"LT_TIME":_viewer.itemValue("LT_TIME"),
			"LT_DEPT":_viewer.getByIdData("LT_DEPT__NAME"),
			"LT_CODE":_viewer.itemValue("LT_CODE"),
			"LT_ADRESS":_viewer.itemValue("LT_ADRESS"),
			"GW_CODE":_viewer.itemValue("GW_CODE")};
			fileItem.replaceBookmark = "_replaceBookmark";
			return true;
		}
	}

// //备注显示样式
_viewer.form.getItem("MEMO_TITLE").obj.parent().removeClass("disabled");
_viewer.form.getItem("MEMO_TITLE").obj.css({"color":"black","font-weight":"bolder","background-color":"white","font-size":"14px"});