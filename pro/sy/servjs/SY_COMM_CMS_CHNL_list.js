var _viewer = this;
_viewer.onRefreshGridAndTree();


var selfcheckBtn = _viewer.getBtn('selfCheck');
selfcheckBtn.click(function(){
	
	
	var servId = _viewer.servId;
	var data = {};
	FireFly.doAct(servId, "selfCheck", data, true, true, function(result){
//		alert(result);
	});
	
});