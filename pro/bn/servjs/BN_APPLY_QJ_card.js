var _viewer = this;
_viewer.getItem("QJ_END_TIME").change(function(){
	//if(_viewer.getItem("QJ_START_TIME") != ""){
	if(_viewer.getItem("QJ_START_TIME").getValue().length>0){
	
		var dateDiff = rhDate.doDateDiff("D",_viewer.getItem("QJ_START_TIME").getValue(),_viewer.getItem("QJ_END_TIME").getValue(),0);
	_viewer.getItem("QJ_NUMBER").setValue(dateDiff);
		}
	
	});
	
