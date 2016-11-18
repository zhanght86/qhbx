var _viewer = this;

jQuery.each(this.grid.getBodyTr(),function(i,n){
	var s_user = jQuery(this).find("td[icode='S_USER']").text();
	//alert(s_user);
	var taskReceiver = jQuery(this).find("td[icode='TASK_RECEIVER']").text();
	//alert(taskReceiver);
	if(taskReceiver != System.getVar("@USER_CODE@")&&s_user !=System.getVar("@USER_CODE@")){
		jQuery(this).hide();
	}
});