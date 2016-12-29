var _viewer = this;
var titleDiv = jQuery("<div></div>").css({"width":"100%","margin":"10px 0px"});
var table = jQuery("<table width='100%'></table>").appendTo(titleDiv);
var tr1 = jQuery("<tr></tr>").appendTo(table);
var tr2 = jQuery("<tr></tr>").appendTo(table); //公文编号
var td11 = jQuery("<td width='20%'></td>").appendTo(tr1);
var td12 = jQuery("<td width='60%' align='center'></td>").appendTo(tr1);
var td13 = jQuery("<td width='20%' align='left'></td>").appendTo(tr1);
var td21 = jQuery("<td width='100%' colspan=3 align='center'></td>").appendTo(tr2);
var title1 = jQuery("<font style='color: #FF0000;font-family: 方正小标宋简体;font-size: 24px;'></font>").appendTo(td12);
_viewer.form.getItem("GW_SECRET").getContainer().css({"width":"100%","margin":"0px"}).appendTo(td13);
_viewer.form.getItem("GW_SECRET_PERIOD").getContainer().css({"width":"100%","margin":"0px"}).appendTo(td13);
_viewer.form.getItem("S_EMERGENCY").getContainer().css({"width":"100%","margin":"0px"}).appendTo(td13);
_viewer.form.obj.find("fieldset").first().prepend(titleDiv);
//获取公文模版信息
var tmpl = FireFly.doAct(this.servId, "getTmpl", {"GW_YEAR_CODE":"true"}, false);
title1.html(tmpl["TMPL_TITLE"]); //设置公文标题
_viewer.form.getItem("GW_YEAR_CODE").hide();
_viewer.form.getItem("GW_YEAR").hide();
_viewer.form.getItem("GW_YEAR_NUMBER").hide();


// 如果当前节点为编号节点，则显示编号，否则编号后才显示
var canCode = _viewer.wfCard.isWorkflow() && _viewer.wfCard.getCustomVarContent("gwCode");
if(canCode || (_viewer.byIdData["GW_YEAR_CODE"] && _viewer.byIdData["GW_YEAR_CODE"].length >0)){
	//设置机关代字
	var yearCode = _viewer.form.getItem("GW_YEAR_CODE");
	yearCode.getContainer().remove();
	yearCode.obj.css({"display":"inline-block","width":"100px","border":"1px solid #91BDEA"}).appendTo(td21);
	if(!canCode){
		yearCode.disabled();
	}
	var gwCode = jQuery("<span></span>").appendTo(td21);
	jQuery("<font>（</font>").appendTo(gwCode);
	_viewer.form.getItem("GW_YEAR").getContainer().remove();
	_viewer.form.getItem("GW_YEAR").obj.css({"width":"40px","background":"transparent","border-bottom":"1px solid #91BDEA","height":"20px","line-height":"20px"}).appendTo(gwCode);
	jQuery("<font>）</font>").appendTo(gwCode);
	if(!canCode){
		_viewer.form.getItem("GW_YEAR").disabled();
	}
	_viewer.form.getItem("GW_YEAR_NUMBER").getContainer().remove();
	_viewer.form.getItem("GW_YEAR_NUMBER").obj.css({"width":"40px","background":"transparent","border-bottom":"1px solid #91BDEA","height":"20px","line-height":"20px"}).appendTo(gwCode);
	jQuery("<font>号</font>").appendTo(gwCode);
	if(!canCode){
		_viewer.form.getItem("GW_YEAR_NUMBER").disabled();
	}
	yearCode.addOptions(tmpl["GW_YEAR_CODES"]); //将动态获取的机关代字设入下拉框
	if (_viewer.getByIdData("GW_YEAR_CODE") != "") {
		yearCode.setValue(_viewer.byIdData["GW_YEAR_CODE"]); //设置保存的机关代字
	}

	yearCode.obj.bind("change", function(event) { 	//机关代字选择事件
		getMaxCode();
	});
	_viewer.form.getItem("GW_YEAR").obj.bind("change", function(event) { //年度修改事件
		getMaxCode();
	});
	function getMaxCode() { //获取机关代字最大值
		var param = {};
		param["GW_YEAR_CODE"] = yearCode.getValue();
		param["GW_YEAR"] = _viewer.form.getItem("GW_YEAR").getValue();
		param["_PK_"] = _viewer.getPKCode();
		param["TMPL_TYPE_CODE"] = _viewer.form.getItem("TMPL_TYPE_CODE").getValue();
		var yearNumber;
		if ((param["GW_YEAR_CODE"] == _viewer.getByIdData("GW_YEAR_CODE")) 
				&& (param["GW_YEAR"] == _viewer.getByIdData("GW_YEAR"))) { //机关代字修改时，选中缺省的代字和年度，保留存储的编号
			yearNumber = _viewer.getByIdData("GW_YEAR_NUMBER");
		} else {
			var tmpl = FireFly.doAct(_viewer.servId, "getMaxCode", param, false);
			yearNumber = tmpl["GW_YEAR_NUMBER"];
		}
		_viewer.form.getItem("GW_YEAR_NUMBER").setValue(yearNumber);
	}
}
var gwHeader = new rh.vi.gwHeader({
	"servId" : _viewer.opts.sId,
	"parHandler" : _viewer
});
var gwExtCard = new rh.vi.gwExtCardView({
	"parHandler" : _viewer
});
gwExtCard.init();

if (_viewer.wfCard && _viewer.wfCard._getBtn("fileToWenku")) {
	_viewer.wfCard._getBtn("fileToWenku").layoutObj.unbind("click").bind("click",function(event){
		var jsFileUrl = FireFly.getContextPath() + "/oa/servjs/OA_FILE_TO_WENKU.js";
	    jQuery.ajax({
	        url: jsFileUrl,
	        type: "GET",
	        dataType: "text",
	        async: false,
	        data: {},
	        success: function(data){
	            try {
	                var servExt = new Function(data);
	                servExt.apply(_viewer);
	            } catch(e){
	            	throw e;
	            }
	        },
	        error: function(){;}
	    });
	});
	
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
	var zwItemObj = zwItem.getObj();
	if(System.getVar("@C_MOBILE_SERVER@") == "true"){ //如果是移动办公系统，则不处理
		return;
	}
	//如果是电脑，则用印章系统查看带水印的文件
	if(System.getVar("@C_DEVICE_TYPE@") == "DESKTOP"){
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
}
/**
 * 批量打印
 */
var printDaGdInfoObj = _viewer.wfCard._getBtn('printDaGdInfo');
if (printDaGdInfoObj) {
	// 动态装载意见代码
	Load.scriptJS("/oa/zh/batch-print.js");
	new rh.oa.batchPrint({
		"parHandler": _viewer,
		"actObj":printDaGdInfoObj,
		"printPic":true,
		"printAudit":true
	});
}
// 初始化盖章按钮
var gwSeal = new rh.vi.gwSeal({servId:_viewer.servId,parHandler:_viewer});
gwSeal.init();

//if(_viewer.byIdData["_ADD_"] == "true"){
//	var fileData = _viewer.form._loadFile("ZHENGWEN");
//	_viewer.getItem("ZHENGWEN").fillData(fileData);
//}
