var _viewer = this;

colHi();
_viewer.getItem("CHANGE_TYPE").change(function(){
	colHi();
});
function colHi(){
	var changeType = _viewer.getItem("CHANGE_TYPE").getValue();
	if(changeType == "3"){
		jQuery("#BN_CHANGE_TYPE_SUB-S_ATIME_label .name").html("变更时间");
		
	}else if(changeType == "4"){
		jQuery("#BN_CHANGE_TYPE_SUB-S_ATIME_label .name").html("变更时间");
		
	}else if(changeType == "2"){
		jQuery("#BN_CHANGE_TYPE_SUB-S_ATIME_label .name").html("停用时间");
		
	}else if(changeType == "1"){
		jQuery("#BN_CHANGE_TYPE_SUB-S_ATIME_label .name").html("启用时间");
		
	}
}