var _viewer = this;

// 构建审批单头部
var gwHeader = new rh.vi.gwHeader({
	"servId" : _viewer.opts.sId,
	"parHandler" : _viewer
});
gwHeader.init();
/*var gwCodeOpts = {
		autoMax : false,   //是否自动获取编号最大值
		showMaxBtn : false, //是否显示获取最大编号值按钮
		canEditCode : true,    //是否能编辑
		showCodeInput : true,   //是否显示代字输入选择框
		defValue : "",      //代字的默认值
		canEditNumber : true
	};	

gwHeader.init(gwCodeOpts);*/

// 如果存在主键，则可以构建文件编号框
if (_viewer._pkCode) {

	var gwExtCard = new rh.vi.gwExtCardView({
		"parHandler" : _viewer
	});
	gwExtCard.init();
}

/** 追加打印份数  **/
if(_viewer.wfCard && _viewer.wfCard._getBtn("appendPrintNum")){
	_viewer.wfCard._getBtn("appendPrintNum").layoutObj.unbind("click").click(function(){
		var printInfo = gwExtCard._getPrintFileInfo();
		//是否有错误
		if(Tools.actIsSuccessed(printInfo)){
			var rhGwSeal = new rh.vi.rhGwSeal();
			rhGwSeal.appendPringNum(printInfo);
		}else{
			alert(printInfo._MSG_);
		}
	});
}

//使用吉大正元印章系统查看盖章文件：预览功能。
var zwItem = _viewer.getItem("ZHENGWEN");
if (zwItem) {
	if(System.getVar("@C_MOBILE_SERVER@") == "true"){ //如果是移动办公系统，则不处理
		return;
	}	
	var zwItemObj = zwItem.getObj();
	//如果是电脑，则用印章系统查看带水印的文件
	if(System.getVar("@C_DEVICE_TYPE@") == "DESKTOP"){
		jQuery(".view_file", zwItemObj).each(function() {
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
		});
	}
}
//更改相关文件样式为默认样式
jQuery("#OA_GW_TMPL_SW_GS-XGWJ_div").children().children().removeAttr("style");
jQuery("#OA_GW_TMPL_SW_GS-XGWJ_div").children().children().attr("style","border:0px solid #000;min-height:27px;");
//html模板中将输入框width设为100%
jQuery(".right").css({"width":"100%"});

//html模板中div width设为100%
jQuery(".right div").css({"width":"100%","padding-bottom":"0px","padding-right":"2px"});

//表格边距
jQuery(".rh-tmpl-tabel td").css({"padding":"3px 8px 3px 5px"});

//文件头部表格内部输入框
jQuery(".gwHeaderTable td span").css({"line-height":"2"});

jQuery(".ui-textarea-default").css({"margin-left":"0px","width":"100%"});

jQuery(".ui-select-default").css({"padding":"0px"});

jQuery(".ui-text-default").css({"margin":"0px"});

// 重置表格高度
jQuery(".ui-staticText-default").css({"height":"auto"});

//针对公文编号做处理
jQuery("#GW_YEAR-left").css({"padding":"0px"});
jQuery("#GW_YEAR-right").css({"padding":"0px"});
jQuery("td[code='GW_YEAR_CODE']").css({"padding":"0px"}).find("input").css({"height":"20px","line-height":"20px"});
jQuery("td[code='GW_YEAR']").css({"padding":"0px"}).find("input").css({"height":"20px","line-height":"20px"});
jQuery("td[code='GW_YEAR_NUMBER']").css({"padding":"0px"}).find("input").css({"height":"20px","line-height":"20px"});

//动态执行加载公文编码在ie9下的css，此处需动态加载一次，不然select元素只出现一个汉字，显示不全
_viewer.form.getItem("GW_YEAR_CODE").obj.width(_viewer.form.getItem("GW_YEAR_CODE").obj.width());
/*--------------------------------样式结束-----------------------------------------*/

//分发本级及下级机构也可以
if (_viewer.wfCard != null && _viewer.wfCard._getBtn("cmSimpleFenFa")) {
	_viewer.wfCard.beforeFenfa = function(sendObj){
		sendObj["includeSubOdept"] = false;
		return true;
	};
	_viewer.wfCard.beforeCmSimpleFenFa = function(actItem,params){
		params["userSelectDict"] = "SY_ORG_DEPT_USER_SUB";
		params["displaySendSchm"] = true;
		params["includeSubOdept"] = true;  //分发本级及下级机构也可以   配置项
		return true;
	}
}

//if(_viewer.byIdData["_ADD_"] == "true"){
//	var fileData = _viewer.form._loadFile("ZHENGWEN");
//	_viewer.getItem("ZHENGWEN").fillData(fileData);
//}