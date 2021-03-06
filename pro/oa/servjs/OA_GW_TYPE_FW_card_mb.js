var _viewer = this;

var titleDiv = jQuery("<div></div>").css({"width":"100%","margin":"0px 0px"});
var table = jQuery("<table width='100%'></table>").appendTo(titleDiv);
var tr1 = jQuery("<tr></tr>").appendTo(table);
var tr2 = jQuery("<tr></tr>").appendTo(table); //公文编号
var td11 = jQuery("<td width='20%'></td>").appendTo(tr1);
var td12 = jQuery("<td width='60%' align='center'></td>").appendTo(tr1);
var td13 = jQuery("<td width='20%' align='left'></td>").appendTo(tr1);
var td21 = jQuery("<td width='100%' colspan=3 align='center'></td>").appendTo(tr2);
var title1 = jQuery("<font style='color: #FF0000;font-family: 方正小标宋简体;font-size: 24px;'></font>").appendTo(td12);
/*_viewer.form.getItem("GW_SECRET").getContainer().hide();
_viewer.form.getItem("GW_SECRET_PERIOD").getContainer().hide();
_viewer.form.getItem("S_EMERGENCY").getContainer().hide();*/
_viewer.form.mainContainer.prepend(titleDiv);
_viewer.form.mainContainer.find(".mb-card-group-title").first().remove();
//获取公文模版信息
FireFly.doAct(this.servId, "getTmpl", {"GW_YEAR_CODE":"true"}, false).then(function(result){
	var tmpl = result;
	title1.html(tmpl["TMPL_TITLE"]); //设置公文标题
	_viewer.form.getItem("GW_YEAR_CODE").getContainer().hide();
	_viewer.form.getItem("GW_YEAR").getContainer().hide();
	_viewer.form.getItem("GW_YEAR_NUMBER").getContainer().hide();
	// 如果已编号，则显示编号
	if(_viewer.getByIdData("GW_YEAR_CODE") != "" && _viewer.form.getItem("GW_YEAR_NUMBER").getValue().length > 0){
		var yearCode = _viewer.getByIdData("GW_YEAR_CODE"); //设置保存的机关代字
		//设置机关代字
		var gwCode = jQuery("<span>" + yearCode + "</span>").appendTo(td21);
		jQuery("<font>（</font>" + _viewer.form.getItem("GW_YEAR").getValue() + "<font>）</font>" + _viewer.form.getItem("GW_YEAR_NUMBER").getValue() + "号</font>").appendTo(gwCode);
		_viewer.form.getItem("GW_YEAR_CODE").getContainer().remove();
		_viewer.form.getItem("GW_YEAR").getContainer().remove();
		_viewer.form.getItem("GW_YEAR_NUMBER").getContainer().remove();
	}
});



//设置拟稿部门
_viewer.getItem("S_TNAME").text.html(_viewer._formData["S_TNAME"]);
//设置主送、抄送
var mainToCodeItem = _viewer.form.getItem("GW_MAIN_TO_CODE");
if(mainToCodeItem && mainToCodeItem.type=='dict'){
		mainToCodeItem.setValue(mainToCodeItem.getText(),_viewer.form.getItem("GW_MAIN_TO").getValue());
}
	
var copyToCodeItem = _viewer.form.getItem("GW_COPY_TO_CODE");
if(copyToCodeItem && copyToCodeItem.type=='dict'){
		copyToCodeItem.setValue(copyToCodeItem.getText(),_viewer.form.getItem("GW_COPY_TO").getValue());
}




//绑定[是否公开]事件
_viewer.getItem("ISOPEN").afterSetValue = function(){
	//是否公开值
	var thisObjVal = _viewer.getItem("ISOPEN").getValue();
	if ((thisObjVal || UIConst.NO) == UIConst.NO) {
		_viewer.getItem("OPEN_TYPE").setValue("0","不公开",true);
		_viewer.getItem("OPEN_TYPE").disabled();
	} else {
		_viewer.getItem("OPEN_TYPE").setValue("","",true);
		_viewer.getItem("OPEN_TYPE").enabled();
	}
};

//绑定[公开类型]事件
_viewer.getItem("OPEN_TYPE").afterSetValue = function(){
	//是否公开值
	var thisObjVal = _viewer.getItem("OPEN_TYPE").getValue();
	if ((thisObjVal || "0") == "0") {
		_viewer.getItem("ISOPEN").setValue("2","否",true);
	} else {
		_viewer.getItem("ISOPEN").setValue("1","是",true);
	}
};

//当前用户是总公司的，则显示公开类型，否则隐藏
//var odeptLevel = System.getVar("@ODEPT_LEVEL@");
//if (odeptLevel != "2") {
//	_viewer.getItem("ISOPEN").setValue("2","否");
//	_viewer.getItem("ISOPEN").disabled();
//	_viewer.getItem("OPEN_TYPE").setValue("0","不公开");
//	_viewer.getItem("OPEN_TYPE").disabled();
//}

//主送中两个项，要填写其中一项
//主送本单位绑定事件
_viewer.getItem("GW_MAIN_TO_CODE").obj.blur(function(){
	//获取主送本单位数值
	var gwMindToCodeVal = _viewer.getItem("GW_MAIN_TO_CODE").getValue() || "";
	//获取主送其他单位数值
	var gwMindToExtVal = _viewer.getItem("GW_MAIN_TO_EXT").getValue() || "";
	//如果主送其他单位和主送本单位没有数值
	if ("" == gwMindToExtVal && "" == gwMindToCodeVal) {
		_viewer.getItem("GW_MAIN_TO_CODE").getContainer().showError("该项必须输入！");
		_viewer.getItem("GW_MAIN_TO_EXT").getContainer().showError("该项必须输入！");
	} else {
		_viewer.getItem("GW_MAIN_TO_CODE").getContainer().showOk();
		_viewer.getItem("GW_MAIN_TO_EXT").getContainer().showOk();
	}
});

var mainToExtObj = _viewer.getItem("GW_MAIN_TO_EXT");

if(mainToExtObj && mainToExtObj.obj){
	//主送其他单位绑定事件
	mainToExtObj.obj.blur(function(){
		//获取主送本单位数值
		var gwMindToCodeVal = _viewer.getItem("GW_MAIN_TO_CODE").getValue() || "";
		//获取主送其他单位数值
		var gwMindToExtVal = _viewer.getItem("GW_MAIN_TO_EXT").getValue() || "";
		//如果主送其他单位和主送本单位没有数值
		if ("" == gwMindToExtVal && "" == gwMindToCodeVal) {
			_viewer.getItem("GW_MAIN_TO_CODE").getContainer().showError("该项必须输入！");
			_viewer.getItem("GW_MAIN_TO_EXT").getContainer().showError("该项必须输入！");
		} else {
			_viewer.getItem("GW_MAIN_TO_CODE").getContainer().showOk();
			_viewer.getItem("GW_MAIN_TO_EXT").getContainer().showOk();
		}
	});
}

//保存前校验主送单位是否有数据
_viewer.beforeSave = function() {
	//获取主送本单位数值
	var gwMindToCodeVal = _viewer.getItem("GW_MAIN_TO_CODE").getValue() || "";
	//获取主送其他单位数值
	var gwMindToExtVal = _viewer.getItem("GW_MAIN_TO_EXT").getValue() || "";
	if ("" == gwMindToExtVal && "" == gwMindToCodeVal) {
		_viewer.getItem("GW_MAIN_TO_CODE").getContainer().showError("该项必须输入！");
		_viewer.getItem("GW_MAIN_TO_EXT").getContainer().showError("该项必须输入！");
		_viewer.cardBarTipError("校验未通过");
		return false;
	}
}