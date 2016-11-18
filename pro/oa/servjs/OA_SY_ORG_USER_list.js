var _viewer = this;

//标识退休人员的记录为灰色背景
var userStateCells = _viewer.grid.getTdItems("USER_STATE");
userStateCells.each(function() {
	var cellObj = jQuery(this);
	var rowObj = cellObj.parent();
	
	
	var flagObj = jQuery("td[icode='S_FLAG']",rowObj);
	var flag = flagObj.text();
	
	
	var val = cellObj.text();
	if(val == 3){
		rowObj.css({'background-color':'gray'});
		var userNameObj = jQuery("td[icode='USER_NAME']",rowObj);
		var userName = userNameObj.text();
		if(userName){
			userNameObj.text(userName + "（退休）");
		}
	}
	if(val == 2){
		rowObj.css({'background-color':'gray'});
		var userNameObj = jQuery("td[icode='USER_NAME']",rowObj);
		var userName = userNameObj.text();
		if(userName){
			userNameObj.text(userName + "（离职）");
		}
	}
});