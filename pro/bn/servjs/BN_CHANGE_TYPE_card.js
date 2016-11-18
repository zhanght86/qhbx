var _viewer = this;

colHide();

_viewer.getItem("CHANGE_TYPE").change(function(){
	appeartext();
	colHide();
});
function colHide(){
	var changeType = _viewer.getItem("CHANGE_TYPE").getValue();
	if(changeType == "3"){
		jQuery("#BN_CHANGE_TYPE-S_ATIME_label .name").html("变更时间");
		_viewer.getItem("CHANGE_PER_TYPE").show();
		_viewer.getItem("CHANGE_PERSON").show();
		_viewer.getItem("CHANGE_SC_TYPE").hide();
	}else if(changeType == "4"){
		jQuery("#BN_CHANGE_TYPE-S_ATIME_label .name").html("变更时间");
		_viewer.getItem("CHANGE_PER_TYPE").hide();
		_viewer.getItem("CHANGE_SC_TYPE").show();
		_viewer.getItem("CHANGE_PERSON").hide();
	}else if(changeType == "2"){
		jQuery("#BN_CHANGE_TYPE-S_ATIME_label .name").html("停用时间");
		_viewer.getItem("CHANGE_PER_TYPE").hide();
		_viewer.getItem("CHANGE_SC_TYPE").hide();
		_viewer.getItem("CHANGE_PERSON").hide();
	}else if(changeType == "1"){
		jQuery("#BN_CHANGE_TYPE-S_ATIME_label .name").html("启用时间");
		_viewer.getItem("CHANGE_PER_TYPE").hide();
		_viewer.getItem("CHANGE_SC_TYPE").hide();
		_viewer.getItem("CHANGE_PERSON").hide();
	}else{
		jQuery("#BN_CHANGE_TYPE-S_ATIME_label .name").html("修改时间");
		_viewer.getItem("CHANGE_PER_TYPE").hide();
		_viewer.getItem("CHANGE_SC_TYPE").hide();
		_viewer.getItem("CHANGE_PERSON").hide();
	}
}

function appeartext(){
	_viewer.getItem("CHANGE_PER_TYPE").setValue("0");
	_viewer.getItem("S_ATIME").setValue("");
	jQuery(jQuery("#BN_CHANGE_TYPE-CHANGE_PERSON__NAME").parent()).find("span:last").click();//变更人员
	_viewer.getItem("CHANGE_SC_TYPE").setValue("0");
}


