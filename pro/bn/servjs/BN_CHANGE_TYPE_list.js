var _viewer = this;

	//变更类别为0则不显示
	var change_type=_viewer.getListData("CHANGE_TYPE");
	for(i=0;i<change_type._DATA_.length;i++){
		if("0"==change_type._DATA_[i].CHANGE_TYPE){
			jQuery(jQuery("td[icode='CHANGE_TYPE__NAME']")[i]).text("");
		}
	}
	
	//作废类别 为0则不显示
	var change_sc_type=_viewer.getListData("CHANGE_SC_TYPE");
	for(i=0;i<change_sc_type._DATA_.length;i++){
		if("0"==change_sc_type._DATA_[i].CHANGE_SC_TYPE){
			jQuery(jQuery("td[icode='CHANGE_SC_TYPE__NAME']")[i]).text("");
		}
	}
