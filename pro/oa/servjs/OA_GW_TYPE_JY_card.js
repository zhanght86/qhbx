/**
var _viewer = this;
var titleDiv = jQuery("<div></div>").css({"width":"90%","margin":"10px 20px"});
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
_viewer.form.getItem("GW_EMERGENCY").getContainer().css({"width":"100%","margin":"0px"}).appendTo(td13);

_viewer.form.obj.find("fieldset").first().prepend(titleDiv);

//修改样式设定
jQuery(".ui-corner-5").css({"background-color":"#FFF"});
_viewer.form.obj.find("input[type=text]").css({"border-bottom":"1px red solid","border-top":"0px","border-left":"0px","border-right":"0px"});

//获取公文模版信息
var tmpl = FireFly.doAct(this.servId, "getTmpl", {}, false);
title1.html(tmpl["TMPL_TITLE"]); //设置公文标题
*/


var _viewer = this;
// 构建审批单头部
var gwHeader = new rh.vi.gwHeader({
	"servId" : _viewer.opts.sId,
	"parHandler" : _viewer
});
gwHeader.init();

// 如果存在主键，则可以构建文件编号框
if (_viewer._pkCode) {

	var gwExtCard = new rh.vi.gwExtCardView({
		"parHandler" : _viewer
	});
	gwExtCard.init();
}
