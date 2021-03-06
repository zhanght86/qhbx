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
	//_viewer.form.getItem("GW_YEAR_NUMBER").getContainer().hide();
	_viewer.form.getItem("GW_YEAR_NUMBER").setValue(_viewer.form.getItem("GW_CODE").getValue());
	// 如果已编号，则显示编号
	if(_viewer.getByIdData("GW_YEAR_CODE") != "" && _viewer.form.getItem("GW_YEAR_NUMBER").getValue().length > 0){
		var yearCode = "";
		if (_viewer.getByIdData("GW_YEAR_CODE") != "") {
			yearCode = _viewer.byIdData["GW_YEAR_CODE"]; //设置保存的机关代字
		}
		//设置机关代字
		var gwCode = jQuery("<span>" + yearCode + "</span>").appendTo(td21);
		jQuery("<font>（</font>" + _viewer.form.getItem("GW_YEAR").getValue() + "<font>）</font>" + _viewer.form.getItem("GW_YEAR_NUMBER").getValue() + "号</font>").appendTo(gwCode);
		_viewer.form.getItem("GW_YEAR_CODE").getContainer().remove();
		_viewer.form.getItem("GW_YEAR").getContainer().remove();
		//_viewer.form.getItem("GW_YEAR_NUMBER").getContainer().remove();
		
	}
});







// 构建审批单头部
var gwHeader = new rh.vi.gwHeader({
	"servId" : _viewer.opts.sId,
	"parHandler" : _viewer
});

var gwCodeOpts = {
		autoMax : false,   //是否自动获取编号最大值
		showMaxBtn : false, //是否显示获取最大编号值按钮
		canEditCode : true,    //是否能编辑
		showCodeInput : true,   //是否显示代字输入选择框
		defValue : "",      //代字的默认值
		canEditNumber : true
	};	

gwHeader.init(gwCodeOpts);

// 如果存在主键，则可以构建文件编号框
if (_viewer._pkCode) {

	var gwExtCard = new rh.vi.gwExtCardView({
		"parHandler" : _viewer
	});
	gwExtCard.init();
}

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