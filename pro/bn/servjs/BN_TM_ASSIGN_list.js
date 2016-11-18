var _viewer = this;var param = {};
if (_viewer.getParHandler() != null) {
	var taskOwner = _viewer.getParHandler().getItem("S_USER").getValue();
	jQuery.each(this.grid.getBodyTr(), function(i, n) {
		var taskState = jQuery(this).find("td[icode='TASK_STATE']").text();
		if (taskState != '3' || taskOwner != System.getVar("@USER_CODE@")) {
			// _viewer.grid.getBtn("remind").hide();
			jQuery(this).find("td[icode='remind']").html('&nbsp;');
		}
	});
}

jQuery(_viewer.grid.getTable()).find("td[icode='TASK_STATE__NAME']").each(
		function() {
			if ($(this).html() == '0') {
				$(this).html('');
			}
		});
// 催办
var remindBtn = _viewer.grid.getBtn("remind");
if (remindBtn) {
	remindBtn.unbind("click").bind("click", function() {
		var id = jQuery(this).attr("rowPk");

		var param = {};
		var acptUser = _viewer.grid.getRowItemValue(id, "TASK_USER");
		param["ACPT_USER"] = acptUser;
		param["ASSIGN_ID"] = id;
		param["TASK_ID"] = _viewer.grid.getRowItemValue(id, "TASK_ID");
		FireFly.doAct("BN_TM_ASSIGN", "insertDB", param);
		_viewer.refresh();
	});
}
if (_viewer.getParHandler() != null) {
	var taskOwner = _viewer.getParHandler().getItem("S_USER").getValue();
	if (taskOwner != System.getVar("@USER_CODE@")) {
		_viewer.grid.getBtn("back").hide();
	}

	var backBtn = _viewer.grid.getBtn("back");
	if (backBtn) {
		backBtn.unbind("click")
				.bind("click",function() {
					var id = jQuery(this).attr("rowPk");
					
					param["ASSIGN_ID"] = _viewer.grid.getRowItemValue(
							id, "ID");
					param["TASK_ID"] = _viewer.grid.getRowItemValue(id,
							"TASK_ID");
					param["TASK_NAME"] = _viewer.getParHandler()
							.getItem("TASK_NAME").getValue();
					param["TASK_USER"] = _viewer.grid.getRowItemValue(
							id, "TASK_USER");
					
							rehandleTask(id);
							_viewer.refresh();
							
						});
	}

	function rehandleTask(varPk) {
		// 弹出页面，任务分配人在对已完成任务审核不通过时，填写重新办理的理由和要求
		var winDialog = jQuery("<div></div>").attr("id", "rehandleTaskDiv")
				.attr("title", "任务重新处理要求");
		var table = jQuery(
				"<table class='rhGrid' align='center' id='queryTable'></table>")
				.appendTo(winDialog);
		var tbody = jQuery(
				"<tbody class='rhGrid' style='background-color:#F6F6F6;font-family:黑体,宋体'></tbody>")
				.appendTo(table);
		var tr1 = jQuery("<tr></tr>").appendTo(tbody);
		var tr2 = jQuery("<tr></tr>").appendTo(tbody);

		var tr1td1 = jQuery(
				"<td  style='width:200px;text-align:center;height:40px;font-size:14px;padding-top:20px;padding-left: 50px;'>任务重办起草时间</td>")
				.appendTo(tr1);
		var tr1td2 = jQuery(
				"<td style='padding-top:20px;' ><span id='date' style=font-size:14px>"
						+ rhDate.getCurentTime() + "</span></td>")
				.appendTo(tr1);
		var tr2td1 = jQuery(
				"<td  style='width:200px;text-align:center;height:40px;font-size:14px;padding-top:20px;padding-left: 50px;'>任务重办需求说明</td>")
				.appendTo(tr2);
		var tr2td2 = jQuery(
				"<td><textarea id='REHANDLE_REQUIREMENT' cols=70 rows=6 value='' ></textarea></td>")
				.appendTo(tr2);

		var posArray = [];
		if (event) {
			var cy = event.clientY;
			posArray[0] = "";
			posArray[1] = cy;
		}
		winDialog.appendTo(jQuery("body"));
		jQuery("#rehandleTaskDiv").dialog(
				{
					autoOpen : false,
					width : 800,
					height : 250,
					modal : true,
					resizable : false,
					position : posArray,
					close : function() {
						winDialog.remove();
					},
					buttons : {
						"确定" : function() {
							//_viewer.shieldHide();
							debugger;
							var params = {};
							params.REHANDLE_REQUIREMENT = jQuery("#REHANDLE_REQUIREMENT").val();
							params.IDSTR = varPk;
							var resultObj = FireFly.doAct(_viewer.servId, "rehandleTask",params);
							if(resultObj["_MSG_"].indexOf("OK") >= 0){
								FireFly.doAct("BN_TM_ASSIGN", "sendToDoBean",param);
							}
							winDialog.remove();
						},
						"取消" : function() {
							winDialog.remove();
						}
					}
				});
		jQuery("#rehandleTaskDiv").dialog("open");
		jQuery(".ui-dialog-titlebar").last().css("display", "block");
	}
}
// 列表行的按钮
var beginBtn = _viewer.grid.getBtn("begin");

if (beginBtn) {
	beginBtn.unbind("click").bind("click", function() {
		// 获取列表行的ID
		var id = jQuery(this).attr("rowPk");
		var param = {};
		param["ASSIGN_ID"] = _viewer.grid.getRowItemValue(id, "ID");
		param["TASK_ID"] = _viewer.grid.getRowItemValue(id, "TASK_ID");
		FireFly.doAct(_viewer.servId, "beginFeedBack", param);
		_viewer.refresh();
		// 点击开始进行，打开子单卡片页面并修改状态为进行中
		_viewer.listBarTipLoad("卡片打开中...");
		_viewer._openCardView(UIConst.ACT_CARD_MODIFY, id);
	});
}

jQuery(".rhGrid-btnBar").hide();
jQuery("th[icode='remind']").remove();
jQuery("th[icode='back']").remove();
jQuery("th[icode='begin']").attr("colspan", "3").text("操作");

jQuery.each(this.grid.getBodyTr(), function(i, n) {
	var s_user = _viewer.getParHandler().getItem("S_USER").getValue();
	//alert(s_user);
	var taskReceiver = jQuery(this).find("td[icode='TASK_USER']").text();
	//(taskReceiver);
	if (taskReceiver != System.getVar("@USER_CODE@")
			&& s_user != System.getVar("@USER_CODE@")) {
		jQuery(this).hide();
	}
});