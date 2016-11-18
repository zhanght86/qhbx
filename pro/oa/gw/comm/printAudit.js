function printAudit(obj){
	var _viewer = obj;
	
	var servId = _viewer.servId;
	var servSrcId = _viewer._data["SERV_SRC_ID"];
	var dataId = _viewer._pkCode;
	FireFly.doPrint(servId, servSrcId, dataId);
};