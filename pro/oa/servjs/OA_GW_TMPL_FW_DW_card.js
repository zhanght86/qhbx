var _viewer = this;

// 只支持IE浏览器
function openPostWindow(servid, servSrcId, content, dataId) {
	var tempForm = document.createElement("form");
	tempForm.id = "tempForm1";
	tempForm.method = "post";
	tempForm.target = "newPage";
	tempForm.action = "/sy/comm/print/doPrint2.jsp"; // 在此处设置你要跳转的url

	var hiddInput_id = document.createElement("input");
	hiddInput_id.type = "hidden";
	hiddInput_id.name = "servId";
	hiddInput_id.value = servid;
	tempForm.appendChild(hiddInput_id);

	var hiddInput_servSrcId = document.createElement("input");
	hiddInput_servSrcId.type = "hidden";
	hiddInput_servSrcId.name = "servSrcId";
	hiddInput_servSrcId.value = servSrcId;
	tempForm.appendChild(hiddInput_servSrcId);

	var hiddInput_dataId = document.createElement("input");
	hiddInput_dataId.type = "hidden";
	hiddInput_dataId.name = "dataId";
	hiddInput_dataId.value = dataId;
	tempForm.appendChild(hiddInput_dataId);

	var hiddInput_content = document.createElement("input");
	hiddInput_content.type = "hidden";
	hiddInput_content.name = "Content";
	hiddInput_content.value = content;
	tempForm.appendChild(hiddInput_content);
	tempForm.attachEvent("onsubmit", function() {
				openWindow();
			});
	document.body.appendChild(tempForm);
	tempForm.fireEvent("onsubmit");
	// 将form的target设置成和windows.open()的name参数一样的值，通过浏览器自动识别实现了将内容post到新窗口中
	tempForm.submit();
	document.body.removeChild(tempForm);
}
_viewer.getBtn("TestPrint").unbind("click").bind("click", function() {
	var dataId = _viewer.getPKCode();
	var servId = _viewer.servId;
	var servSrcId = _viewer.getServSrcId();
	var data = {};
	data["_WHERE_"] = " and SERV_ID ='" + servId + "'";
	// debugger;
	var out = FireFly.doAct("SY_COMM_PRINT_TMPL", "finds", data);
	// debugger;
	var ftlcontent = out["_DATA_"];
	var content = "";
	for (var i = 0; i < ftlcontent.length; i++) {
		var ser = ftlcontent[i]
		content = ser["PT_CONTENT"];
	}
	openPostWindow(servId, servSrcId, content, dataId);
		// window.open('about:blank',"newPage",'height=400, width=400, top=0,
		// left=0, toolbar=yes, menubar=yes, ' +
		// 'scrollbars=yes, resizable=yes,location=yes, status=yes').focus();

		// window.open("/sy/comm/print/doPrint.jsp?servId=" + servId +
		// "&dataId=" + dataId + "&Content=" + content);
		// window.open("/sy/comm/print/doPrint.jsp?servId=" + servId +
		// "&servSrcId=" + servSrcId + "&dataId=" + dataId + "&ftlFile=" +
		// ftlFile);
	});
	
/**
 * 批量打印
 */
var printDaGdInfoObj = _viewer.wfCard._getBtn('printDaGdInfo');
if (printDaGdInfoObj) {
	// 动态装载意见代码
	Load.scriptJS("/oa/zh/batch-print.js");
	new rh.oa.batchPrint({
		"parHandler": _viewer,
		"actObj":printDaGdInfoObj,
		"printPic":true,
		"printAudit":true
	});
}
