(function(_viewer){
	var servId = _viewer.servId;
	var pk =  _viewer.getItem("CT_ID").getValue();
	
	// 人民币小写转大写
	var ctEmoney = _viewer.getItem("CT_EMONEY");
	var ctCmoney = _viewer.getItem("CT_CMONEY");
	try {
		ctEmoney.getObj().blur(function(){
			var val = jQuery(this).val();
			if (val && val.length > 0) {
				ctCmoney.setValue(Format.RMBCapital(val));
			}
		});
	} catch(e) {
		alert(e);
	}
	
	// 选择合同范本
	var addTmplBtn = jQuery("<a class='rh-icon rhGrid-btnBar-a' actcode='addTmpl' style='position:absolute;display:inline-block;'>" +
			"<span class='rh-icon-inner' style='padding:0 6px 2px 0px;'>合同范本</span></a>");
	var zhengWen = _viewer.form.getAttachFile("ZHENGWEN");
	// 上次文件之前检测是否已经有了正文
	zhengWen.beforeUploadCallback = function() {
		if (!jQuery.isEmptyObject(zhengWen.getFileData())) {
			if (confirm("已经有一个正文，是否覆盖？")) {
				zhengWen.clear();
				zhengWen.setFileId(getZhengWenFileId());
				return true;
			} else {
				return false;
			}
		}
		return true;
	};
	zhengWen.getObj().find(".uploadBtn").first().after(addTmplBtn);
	addTmplBtn.click(function(){
		var isCover = false;
		if (!jQuery.isEmptyObject(zhengWen.getFileData())) {
			if (confirm("已经有一个正文，是否覆盖？")) {
				isCover = true;
			} else {
				return;
			}
		}
		var inputName = "xxxx";
		var configStr = "LW_CT_TEMPLATE,{'PKHIDE':true,'TARGET':'TP_ID~TP_NAME~TP_TYPE~TP_MEMO~S_ATIME','SOURCE':'TP_ID~TP_NAME~TP_TYPE~TP_MEMO~S_ATIME','TYPE':'single'}";
		var options = {
				"itemCode":inputName,
				"config" :configStr,
				"rebackCodes":inputName,
				"parHandler":this,
				"formHandler":this,
			    "replaceCallBack":function(tpl){
			    	var data = {};
			    	data[UIConst.PK_KEY] = pk;
			    	data["TP_ID"] = tpl.TP_ID; 
			    	// 覆盖文件
			    	if (isCover) {
			    		data["FILE_ID"] = getZhengWenFileId();
			    	}
			    	var result = FireFly.doAct(servId, "copyTemplate", data);
			    	if (result[UIConst.RTN_MSG].indexOf(UIConst.RTN_ERR) >= 0) {
			    		Tip.showError(result[UIConst.RTN_MSG], true);
			    	} else {
			    		Tip.show(result[UIConst.RTN_MSG], true);
			    		zhengWen.refresh();
			    	}
			    }	
			};
			var queryView = new rh.vi.rhSelectListView(options);
			queryView.show();
	});
	
	/**
	 * 获取正文文件ID
	 */
	function getZhengWenFileId() {
		var fileDatas = zhengWen.getFileData();
		for (var fileId in fileDatas) {
			return fileId;
		}
		return null;
	}
})(this);