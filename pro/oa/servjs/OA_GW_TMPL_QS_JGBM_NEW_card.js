var _viewer = this;

(function(_viewer){
	//设置主送抄送的名称
	try{
		var mainToCodeItem = _viewer.form.getItem("CO_TO");
		if(mainToCodeItem && mainToCodeItem.type=='DictChoose' && !mainToCodeItem.isHidden){
			mainToCodeItem.setText(_viewer.form.getItem("CO_TO_NAME").getValue());
		}
	}catch(e){
		console.error("OA_GW_TMPL_QS_JGBM_NEW_card.js:" + e.message);
	}

	//备注显示样式
	_viewer.form.getItem("MEMO_TITLE").obj.parent().removeClass("disabled").css({"background":"url('')"});
	_viewer.form.getItem("MEMO_TITLE").obj.css({"color":"black","font-weight":"bolder","background-color":"white","font-size":"14px"});
	
	//重载传阅参数
	if (_viewer.wfCard != null) {
		_viewer.wfCard.beforeFenfa = function(sendObj){
			sendObj["includeSubOdept"] = false;
			return true;
		};
		_viewer.wfCard.beforeCmSimpleFenFa = function(actItem,params){
			params["userSelectDict"] = "SY_ORG_DEPT_USER";
			params["displaySendSchm"] = true;
			params["includeSubOdept"] = false;
			return true;
		};
	}
	var showMemoItem = _viewer.getItem("SHOW_MEMO");
	showMemoItem.obj.parent().removeClass("disabled").css({"background":"url('')"});
	showMemoItem.obj.css({"color":"black","font-weight":"bolder","background-color":"white","font-size":"14px"});
	var showMomeItems = new Array();
	var sUname = _viewer.getItem("S_UNAME");
	var sTname = _viewer.getItem("S_TNAME");
	var gwTime = _viewer.getItem("GW_BEGIN_TIME");
	var semergencyTname = _viewer.getItem("S_EMERGENCY");
	showMomeItems.push("<table border='0' width='100%'><tr>");
	showMomeItems.push("<td style='width:80px;'>" + sUname.getLabel().find("span[class='name']").html() + ":</td>");
	showMomeItems.push("<td style='width:100px;'>" + sUname.getValue() + "</td>");
	showMomeItems.push("<td style='width:80px;'>" + sTname.getLabel().find("span[class='name']").html() + ":</td>");
	showMomeItems.push("<td style='width:100px;'>" + sTname.getValue() + "</td>");
	showMomeItems.push("<td style='width:80px;'>" + gwTime.getLabel().find("span[class='name']").html() + ":</td>");
	showMomeItems.push("<td style='width:160px;'>" + gwTime.getValue() + "</td>");
	var emergencyType = semergencyTname.getValue();
	if (emergencyType <= 10) {
		showMomeItems.push("<td style='padding-left:50px;' id='" + _viewer.servId + "SHOW_MEMO_table_S_EMERGENCY' title='紧急程度:" + semergencyTname.getText() + "'><span class='span_emergency'></span></td>");
	} else if (emergencyType <= 20) {
		showMomeItems.push("<td style='padding-left:50px;' id='" + _viewer.servId + "SHOW_MEMO_table_S_EMERGENCY' title='紧急程度:" + semergencyTname.getText() + "'><span class='span_emergency comm_emergency__normal'></span></td>");
	} else {
		showMomeItems.push("<td style='padding-left:50px;' id='" + _viewer.servId + "SHOW_MEMO_table_S_EMERGENCY' title='紧急程度:" + semergencyTname.getText() + "'><span class='span_emergency comm_emergency__very'></span></td>");
	}
	showMomeItems.push("</tr></table>");
	showMemoItem.obj.append(showMomeItems.join(""));
	
	semergencyTname.change(function(){
		var emergencyTdObj = jQuery("td[id='" + _viewer.servId + "SHOW_MEMO_table_S_EMERGENCY']");
		emergencyTdObj.attr("title", "紧急程度:" + semergencyTname.getText());
		var emergencyTypes = semergencyTname.getValue();
		var thisSpanObj = emergencyTdObj.find("span");
		if ((emergencyTypes || 0) == 0 || emergencyTypes <= 10) {
			thisSpanObj.removeClass("comm_emergency__normal");
			thisSpanObj.removeClass("comm_emergency__very");
		} else if (emergencyTypes <= 20) {
			thisSpanObj.addClass("comm_emergency__normal");
			thisSpanObj.removeClass("comm_emergency__very");
		} else if (emergencyTypes > 20) {
			thisSpanObj.addClass("comm_emergency__very");
			thisSpanObj.removeClass("comm_emergency__normal");
		}
	});
	_viewer.form._validation["MEMO"]["txt"].message = "长度不能超过400字！";
	_viewer.form._validation["GW_MEMO"]["txt"].message = "长度不能超过80字！";
})(this);