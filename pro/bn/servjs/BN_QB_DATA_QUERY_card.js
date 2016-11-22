var _viewer = this;
var titleDiv = jQuery("<div class='BN_COMM_HEAD'></div>").css({"width":"100%","padding":"10px 0px"});
var table = jQuery("<table width='98%'></table>").appendTo(titleDiv);
var tr1 = jQuery("<tr></tr>").appendTo(table);
jQuery("<td align='center'><font style='font-family: 方正小标宋简体;font-size: 28px;font-weight:bold;'>前海再保险股份有限公司</font></td>").appendTo(tr1);
var tr2 = jQuery("<tr></tr>").appendTo(table);
jQuery("<td align='center'><font style='font-family: 方正小标宋简体;font-size: 20px;font-weight:bold;'>内部工作签报</font></td>").appendTo(tr2);
_viewer.form.obj.prepend(titleDiv);
//去掉只读的灰色背景色
jQuery(_viewer.form.getItem("APPROVAL_PERSON").getObj()).removeClass("disabled");
jQuery(_viewer.form.getItem("APPROVAL_PERSON").getObj().parent()).removeClass("disabled");
jQuery(_viewer.form.getItem("QB_TITLE").getObj()).removeClass("disabled");
jQuery(_viewer.form.getItem("QB_TITLE").getObj().parent()).removeClass("disabled");
jQuery(_viewer.form.getItem("S_DEPT").getObj()).removeClass("disabled");
jQuery(_viewer.form.getItem("S_DEPT").getObj().parent()).removeClass("disabled");
jQuery(_viewer.form.getItem("S_UNAME").getObj()).removeClass("disabled");
jQuery(_viewer.form.getItem("S_UNAME").getObj().parent()).removeClass("disabled");
jQuery(_viewer.form.getItem("CONTACT_PHONE").getObj()).removeClass("disabled");
jQuery(_viewer.form.getItem("CONTACT_PHONE").getObj().parent()).removeClass("disabled");
jQuery(_viewer.form.getItem("START_TIME").getObj()).removeClass("disabled");
jQuery(_viewer.form.getItem("START_TIME").getObj().parent()).removeClass("disabled");
jQuery(_viewer.form.getItem("END_TIME").getObj()).removeClass("disabled");
jQuery(_viewer.form.getItem("END_TIME").getObj().parent()).removeClass("disabled");
//渲染意见列表
var mindDiv = $("#" + _viewer.servId + "-MIND_LIST_div").find("span[class='ui-staticText-default disabled']");
_viewer.form.getItem("MIND_LIST").enabled();
//获取意见
FireFly.doAct("BN_QB_MIND","query",{"_SELECT_":"*","_ORDER_":" MIND_SORT ASC ","_WHERE_":" and DATA_ID='"+_viewer.getPKCode()+"' and SERV_ID='"+_viewer.servId+"'"},false,false,function(result){
	var data = result["_DATA_"];
	if(data){
		var table = jQuery("<table></table>").attr("style","width:95%;margin-left:10px;").appendTo(mindDiv);
		for(var i=0; i<data.length; i++){
			var mind = data[i];
			var tr = jQuery("<tr height='30'></tr>").appendTo(table);
			var td1 = jQuery("<td width='70%'>"+mind.MIND_CONTENT+"</td>").appendTo(tr);
			var td2 = jQuery("<td width='10%'>"+mind.S_UNAME+"</td>").appendTo(tr);
			var td3 = jQuery("<td width='20%'>"+mind.MIND_TIME+"</td>").appendTo(tr);
		}
	}
});