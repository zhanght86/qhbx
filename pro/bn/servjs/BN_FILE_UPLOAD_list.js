var _viewer = this;
_viewer.grid.getCheckBox().each(function(i,item){	
	var chkboxObj = jQuery(this);
	var user = _viewer.grid.getRowItemValueByElement(chkboxObj,"S_USER");
	if(user != System.getVar("@USER_CODE@")){
		jQuery(this).css('display','none');
	}
});