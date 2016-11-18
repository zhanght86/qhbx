/** 通讯录列表使用的js方法定义：开始 */
var _viewer = this;
_viewer.getBtn("sendNotify").bind("click", function() {
	var info = window.prompt("请输入要发送的信息：", "");
	if (info == "" || info == null) {
		alert("没有输入有效的内容！");
	} else {
		var pkAarry = _viewer.grid.getSelectPKCodes();
		var strs = pkAarry.join(",");
		var datas = {};
		datas[UIConst.PK_KEY]=strs;
		datas = jQuery.extend(datas, this.links);
		datas["SEND_MSG"] = info;
		FireFly.doAct(_viewer.servId, "sendNotify", datas, false);
	}
});

jQuery("td[icode='USER_NAME']",_viewer.grid.getTable()).find("a").bind("mouseover", function(event) {
	var userCode = jQuery(this).parent().parent().find("[icode='USER_CODE']").html();
	new rh.vi.userInfo(event, userCode);
}).attr("onclick","");

