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
_viewer.form.getItem("APPLY_SCRT").getContainer().css({"width":"100%","margin":"0px"}).appendTo(td13);

_viewer.form.getItem("S_EMERGENCY").getContainer().css({"width":"100%","margin":"0px"}).appendTo(td13);
_viewer.form.obj.find("fieldset").first().prepend(titleDiv);