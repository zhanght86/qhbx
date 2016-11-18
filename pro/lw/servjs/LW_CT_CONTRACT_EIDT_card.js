(function(_viewer){
	if(_viewer.getItem("CT_IS_AT").getValue()==2){
		_viewer.getItem("CT_AT_MEMO").hide();
	}
	
	_viewer.beforeSave = function(){
		var ctBtime = _viewer.getItem("CT_BTIME").getValue();
		var ctEtime = _viewer.getItem("CT_ETIME").getValue();
		if(rhDate.doDateDiff("T",ctBtime,ctEtime,0) < 0){
			alert("起始时间不能大于终止时间");
			 Tip.clearLoad();
			return false;
		}
	};
	
	var servId = _viewer.servId;
	var pk =  _viewer.getItem("CT_ID").getValue();
	
	// 人民币小写转大写
	var ctEmoney = _viewer.getItem("CT_EMONEY");
	var ctCmoney = _viewer.getItem("CT_CMONEY");
	try {
		ctEmoney.getObj().blur(function(){
			var val = jQuery(this).val();
			if (val && val.length > 0) {
				try {
					ctCmoney.setValue(Format.RMBCapital(val));
				} catch (e) {
					alert(e);
				}
			}
		});
	} catch(e) {
		alert(e);
	}
	var zhengWen = _viewer.form.getAttachFile("ZHENGWEN");
	// 上传文件之前检测是否已经有了正文
	zhengWen.beforeUploadCallback = function() {
		if (!jQuery.isEmptyObject(zhengWen.getFileData())) {
			if (confirm("已经有一个正文，是否覆盖？")) {
				zhengWen.setFileId(getZhengWenFileId());
				return true;
			} else {
				return false;
			}
		}
		return true;
	};
	
	
	//添加说明信息，由于在卡片中缺省值长度不够，无法写入要求的长度字符串，故在此做操作 hdy 2013-6-27 10:26
	var momeObj = jQuery.parseJSON(System.getVar("@C_SY_LIST_MEMO_CONFIG_OBJ@") || "{}");
	if ("" != (momeObj[_viewer.servId] || "")) {
		_viewer.getItem("MEMO_TITLE").setValue(momeObj[_viewer.servId]);
	}
	
	
	
})(this);