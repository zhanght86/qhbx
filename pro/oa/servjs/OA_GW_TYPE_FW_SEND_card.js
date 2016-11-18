var _viewer = this;

//构建审批单头部
var gwHeader = new rh.vi.gwHeader({
	"servId" : _viewer.opts.sId,
	"parHandler" : _viewer
});
gwHeader.init();

var gwExtCard = new rh.vi.gwExtCardView({
	"parHandler" : _viewer
});
gwExtCard.init();

/**
 * 如果文件已签收（或退回），则隐藏按钮，禁止用户操作
 */
var canOptSend = _viewer.getByIdData("CAN_OPT_SEND");
if(canOptSend != undefined && canOptSend != "true"){
	_viewer.getBtn('toShouwen').hide();
	_viewer.getBtn('tuihui').hide();
}

//当前正在办理，又有正文存在，则设置文件的权限为 只读
var fileItem = _viewer.getItem("ZHENGWEN");  //获取到正文 对象
if (fileItem) { //隐藏除正文之外的所有文件。
	fileItem.obj.find(".file").each(function(i, item){
		if (jQuery(item).text().indexOf("正文") < 0) { //文稿
			jQuery(item).hide();
		}
	});
}

/**
 * @param newServId 新服务ID
 */
function qianshouToShouwen(newServId){
	var params = {};
	params["SEND_ID"] = _viewer.getByIdData("SEND_ID");
	params["nextServId"] = newServId;
	params["SERV_ID"] = _viewer.getByIdData("TMPL_CODE");
	params["DATA_ID"] = _viewer.getByIdData("GW_ID");
	
	try{
		_viewer.shield();
		var result = rh_processData("OA_GW_TYPE_FW_SEND.toShouwen.do", params);
		if(result != null && StringUtils.startWith(result._MSG_,'OK')) {
			var options = {"url": newServId + ".card.do?pkCode="+result["_PK_"],"tTitle":result.GW_TITLE,"params":{},"menuFlag":3};
			Tab.open(options); //打开收文界面
			_viewer.backClick(); //关闭发文界面
		}
	}finally{
		_viewer.shieldHide();
	}
}


/**
 * 签收并转收文登记功能
 */
_viewer.getBtn('toShouwen').unbind("click").bind("click",function(event) {
	var reqData = {};
	reqData.TMPL_CODE = _viewer.getByIdData("TMPL_CODE");
	
	var result = FireFly.doAct("OA_GW_TYPE_FW_SEND","findUserSwMenu",reqData,false,false);
	
	if (result.SW_TMPL_CODE) { //找到了模板编码上设置的收文模板
		qianshouToShouwen(result.SW_TMPL_CODE);
		return;
	} else {
		var cnt = new Array();
		cnt.push('<div id="selectShouwenDiv" title="请选择收文类型">');
		cnt.push('<div style="padding:10px"><ul>');
		var dataList = result._DATA_;
		
		if(dataList.length == 0){
			alert("您还没有起草收文的权限。");
			return;
		}else if(dataList.length == 1){
			var menuItem = dataList[0];
			qianshouToShouwen(menuItem.SERV_NAME,menuItem.SERV_ID);
			return;
		}
		
		for(var i=0;i<dataList.length;i++){
			var menuItem = dataList[i];
			cnt.push('<li class="block cp" style="margin-left:10px; margin-right: 10px;" SERV_ID="');
			cnt.push(menuItem.SERV_ID);
			cnt.push('">');
			cnt.push('<div class="mt10 ml20"><span><u>');
			cnt.push(menuItem.SERV_NAME);
			cnt.push('</u></span></div>');
			cnt.push('</li>');
		}
		
		cnt.push('</ul></div></div>');
		var winDialog = jQuery(cnt.join(""));
		winDialog.find("li").click(function(){
			var name = jQuery(this).text();
			if(!confirm("是否转换成  " + name + " ?")){
				return;
			}
			var newServId = jQuery(this).attr("SERV_ID");
			try{
				qianshouToShouwen(newServId);
			}finally{
				winDialog.remove();
			}
		});
		
		winDialog.appendTo(jQuery("body"));
		jQuery("#selectShouwenDiv").dialog({
			autoOpen : false,
			width : 400,
			height : 260,
			modal : true,
			resizable : false,
			position : ["",120],
			close : function() {
				winDialog.remove();
			}
		});
		jQuery("#selectShouwenDiv").dialog("open");
		jQuery(".ui-dialog-titlebar").last().css("display", "block");
	}
});


/**
 * 退回 ， 填写退回说明 -> 添加退回的待办 -> 完成分发（设置返回的字段值） -> 待办变成已办
 */
_viewer.getBtn('tuihui').unbind("click").bind("click",function(event) {
	var _self = _viewer;
	var sendId = _viewer.getByIdData("SEND_ID");
	
	// 退回回执
	var winDialog = jQuery("<div></div>").attr("id", "tuiHuiDiv")
		.attr("title","请填写退回原因");

	var huiZhiContent = jQuery("<div></div>").appendTo(winDialog);
	jQuery("<textarea type='textarea' style='width:95%;height:200px;margin-left:1%;padding:5px' id ='huiZhiContent' name ='huiZhiContent'/>")
			.appendTo(huiZhiContent);

	var posArray = [];
	if (event) {
		var cy = event.clientY;
		posArray[0] = "";
		posArray[1] = cy + 120;
	}
	winDialog.appendTo(jQuery("body"));
	jQuery("#tuiHuiDiv").dialog({
		autoOpen : false,
		width : 400,
		height : 260,
		modal : true,
		resizable : false,
		position : posArray,
		close : function() {
			winDialog.remove();
		},buttons:{"确定":function(){
			var params = {};
			params["TODO_CONTENT"] = jQuery("#huiZhiContent").val();
			params["SEND_ID"] = sendId;
			var result = rh_processData("SY_COMM_SEND_SHOW_CARD.cmTuiHui.do",
					params);
			if (result.rtnstr == "success") {
				Tip.show("退回成功", true);
				if (parent.Portal.getBlock("SY_COMM_TODO")) {
					parent.Portal.getBlock("SY_COMM_TODO").refresh();
				}
				_viewer.backClick();
				setTimeout(function() {
					jQuery("a.rhCard-refresh").click();
				}, 100);
			} else {
				Tip.show("退回失败", true);
			}
		},"取消":function(){
			winDialog.remove();
		}}
	});
	jQuery("#tuiHuiDiv").dialog("open");
	jQuery(".ui-dialog-titlebar").last().css("display", "block");
});

//使用吉大正元印章系统查看盖章文件：预览功能。
var zwItem = _viewer.getItem("ZHENGWEN");
if (zwItem) {
	var zwItemObj = zwItem.getObj();
	jQuery(".view_file", zwItemObj).each(function() {
		var fileBean = jQuery(this).data("fileBean");
		if (fileBean && fileBean["ITEM_CODE"] == "ZHENGWEN") { //如果文件子类型也为正文，则
			var existSealPdf = _viewer.getByIdData("EXIST_SEAL_PDF_FILE");
			if(existSealPdf == "true"){ //如果该文件为已经改过章的文件
				jQuery(this).unbind().click(function(){ 
					//覆盖原有打开文件的方法，使用印章系统的页面打开文件，并增加水印。
					var viewFileObj = jQuery(this);
					var param = {"fileId":viewFileObj.data("id")};
					var result = FireFly.doAct("OA_GW_SEAL_FILE","getViewSealFileInfo",param,false);	
					if(result.URL){
						window.open(result.URL);
					} else if (result._MSG_ && result._MSG_.indexOf("ERROR,") == 0){ //如果后台出错
						alert(result._MSG_);
					}
				});	
			}
		}
	});
}


//html模板中将输入框width设为100%
jQuery(".right").css({"width":"100%"});

//html模板中div width设为100%
jQuery(".right div").css({"width":"100%"});

//输入框宽度
jQuery(".right input").css({"width":"100%","border":"1px #CCC solid","margin-left":"0px"});

//表格边距
jQuery(".rh-tmpl-tabel td").css({"padding":"3px 6px 3px 5px"});

// 文件的td样式不要padding
jQuery(".file td").css({"padding":"0"});

//文件头部表格内部输入框
jQuery(".gwHeaderTable td span").css({"line-height":"2"});

jQuery(".ui-textarea-default").css({"margin-left":"0px","width":"100%"});