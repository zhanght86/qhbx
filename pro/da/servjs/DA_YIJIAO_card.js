var _viewer = this;
/**档案接收**/
if(_viewer.wfCard){
	var getYJItemObj = _viewer.wfCard._getBtn("getYJItem");	
	if(getYJItemObj){
		var getYJItem = getYJItemObj.layoutObj;
		getYJItem.unbind("click").bind("click",function(){
			var param = {};
			param["YJ_ID"] = _viewer.getPKCode();
			var resultObj = FireFly.doAct(_viewer.servId,"daReceive",param);
			if(resultObj["_MSG_"].indexOf("OK") >= 0){
				_viewer.refresh();
				_viewer.cardBarTip("成功接收");
			}
		});
	}
}
