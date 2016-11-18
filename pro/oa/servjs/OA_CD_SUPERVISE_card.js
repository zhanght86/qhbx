var _viewer = this;
var titleDiv = jQuery("<div></div>").css({"width":"90%","margin":"10px 20px"});
var table = jQuery("<table width='100%'></table>").appendTo(titleDiv);
var tr1 = jQuery("<tr></tr>").appendTo(table);
var td11 = jQuery("<td width='30%'></td>").appendTo(tr1);
var td12 = jQuery("<td width='40%' align='center'></td>").appendTo(tr1);
var td13 = jQuery("<td width='30%' align='left'></td>").appendTo(tr1);
var title1 = jQuery("<font id=title1 style='color: #FF0000;font-family: 方正小标宋简体;font-size: 24px;'></font>").appendTo(td12);
_viewer.form.getItem("SUPE_SCRT").getContainer().css({"width":"100%","margin":"0px"}).appendTo(td13);
//_viewer.form.getItem("SUPE_EMGY").getContainer().css({"width":"100%","margin":"0px"}).appendTo(td13);

jQuery("<tr><br></tr>").appendTo(table);

var tr2 = jQuery("<tr></tr>").appendTo(table); //编号
var td21 = jQuery("<td width='100%' colspan=3 align='center'></td>").appendTo(tr2);
var yearCode = _viewer.form.getItem("SUPE_CODE");
_viewer.form.getItem("SUPE_CODE").getContainer().remove();
yearCode.obj.css({"float":"none"}).appendTo(td21);
var gwCode = jQuery("<span></span>").appendTo(td21);
jQuery("<font>（</font>").appendTo(gwCode);
_viewer.form.getItem("SUPE_YEAR").getContainer().remove();
_viewer.form.getItem("SUPE_YEAR").obj.css({"float":"none"}).appendTo(gwCode);
jQuery("<font>）</font>").appendTo(gwCode);
_viewer.form.getItem("SUPE_NUM").getContainer().remove();
_viewer.form.getItem("SUPE_NUM").obj.css({"float":"none"}).appendTo(gwCode);
jQuery("<font>号</font>").appendTo(gwCode);

_viewer.form.obj.find("fieldset").first().prepend(titleDiv);

title1.html("督&nbsp;办&nbsp;单");

getMaxCode();

//获取代字最大值
function getMaxCode() { 
	var param = {};
	param["SUPE_CODE"] = yearCode.getValue();
	param["SUPE_YEAR"] = _viewer.form.getItem("SUPE_YEAR").getValue();
	var yearNumber = _viewer.getByIdData("SUPE_NUM");
	if (yearNumber<=0) {
		var tmpl = FireFly.doAct(_viewer.servId, "getMaxCode", param, false);
		yearNumber = tmpl["SUPE_NUM"];
	}
	_viewer.form.getItem("SUPE_NUM").setValue(yearNumber);
}

_viewer._parentRefreshFlag = true;