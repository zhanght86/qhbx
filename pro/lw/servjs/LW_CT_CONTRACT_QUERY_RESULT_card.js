(function(_viewer){
	//关联交易选择否时 关联详细描述不显示
	if(_viewer.getItem("CT_IS_AT").getValue()==2){
		_viewer.getItem("CT_AT_MEMO").hide();
	}
})(this);