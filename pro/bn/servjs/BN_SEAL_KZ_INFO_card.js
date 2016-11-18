var _viewer = this;
var sealType = _viewer.getItem("SEAL_TYPE1")
sealType.change(function(){	
	var specWhere = "";
	var sealType1 = _viewer.getItem("SEAL_TYPE1").getValue() || "";
	
	if(sealType1 != ""){
		specWhere += " and SEAL_TYPE1='"+sealType1+"'";
	}
	
	var result = FireFly.doAct("BN_SEAL_STYLE","query",{"_extWhere":specWhere});
	var list = result._DATA_;
	if(list.length>0){
		for(var i=0;i<list.length;i++){
			var data = list[i];
			jQuery("#BN_SEAL_KZ_INFO-SEAL_FONT").val(data.SEAL_FONT);
			jQuery("#BN_SEAL_KZ_INFO-SEAL_QUALITY").val(data.SEAL_QUALITY);
			jQuery("#BN_SEAL_KZ_INFO-SEAL_WIDTH").val(data.SEAL_WIDTH);
			jQuery("#BN_SEAL_KZ_INFO-SEAL_HEIGHT").val(data.SEAL_HEIGHT);
			jQuery("#BN_SEAL_KZ_INFO-SEAL_FORM").val(data.SEAL_FORM);
			jQuery("#BN_SEAL_KZ_INFO-SEAL_COLOR").val(data.SEAL_COLOR);
			
		}
	}else{
		jQuery("#BN_SEAL_KZ_INFO-SEAL_FONT").val("");
		jQuery("#BN_SEAL_KZ_INFO-SEAL_QUALITY").val("");
		jQuery("#BN_SEAL_KZ_INFO-SEAL_WIDTH").val("");
		jQuery("#BN_SEAL_KZ_INFO-SEAL_HEIGHT").val("");
		jQuery("#BN_SEAL_KZ_INFO-SEAL_FORM").val("");
		jQuery("#BN_SEAL_KZ_INFO-SEAL_COLOR").val("");
	}
});