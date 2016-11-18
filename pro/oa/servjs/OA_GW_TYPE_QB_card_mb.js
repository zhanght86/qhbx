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
_viewer.form.getItem("GW_SECRET").getContainer().hide();
_viewer.form.getItem("GW_SECRET_PERIOD").getContainer().hide();
_viewer.form.getItem("S_EMERGENCY").getContainer().hide();
_viewer.form.mainContainer.prepend(titleDiv);
_viewer.form.mainContainer.find(".mb-card-group-title").first().remove();
//获取公文模版信息
FireFly.doAct(this.servId, "getTmpl", {"GW_YEAR_CODE":"true"}, false).then(function(result){
	var tmpl = result;
	title1.html(tmpl["TMPL_TITLE"]); //设置公文标题
	_viewer.form.getItem("GW_YEAR_CODE").getContainer().hide();
	_viewer.form.getItem("GW_YEAR").getContainer().hide();
	_viewer.form.getItem("GW_YEAR_NUMBER").getContainer().hide();
	_viewer.form.getItem("GW_YEAR_NUMBER").setValue("2");
	// 如果已编号，则显示编号
	if(_viewer.getByIdData("GW_YEAR_CODE") != "" && _viewer.form.getItem("GW_YEAR_NUMBER").getValue().length > 0){
		var yearCode = "";
		if (_viewer.getByIdData("GW_YEAR_CODE") != "") {
			yearCode = _viewer.byIdData["GW_YEAR_CODE"]; //设置保存的机关代字
		}
		//设置机关代字
		var gwCode = jQuery("<span>" + yearCode + "</span>").appendTo(td21);
		jQuery("<font>（</font>" + _viewer.form.getItem("GW_YEAR").getValue() + "<font>）</font>").appendTo(gwCode);
		jQuery(_viewer.form.getItem("GW_YEAR_NUMBER").getValue() + "<font>号</font>").appendTo(gwCode);
		_viewer.form.getItem("GW_YEAR_CODE").getContainer().remove();
		_viewer.form.getItem("GW_YEAR").getContainer().remove();
		_viewer.form.getItem("GW_YEAR_NUMBER").getContainer().remove();
	}
});
