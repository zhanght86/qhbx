var _viewer = this;
/*
 * 保存之后。将文件信息更新到名称和大小。

_viewer.beforeSave  =  function(){

	FireFly.doAct("SY_COMM_FILE","query",{"_WHERE_":" AND DATA_ID='"+_viewer.getItem("ID").getValue()+"' AND SERV_ID='BN_FILE'"},false,false,function(result){
		var data = result["_DATA_"][0];
		_viewer.getItem("FILE_NAME").setValue(data.FILE_NAME);
		_viewer.getItem("FILE_SIZE").setValue(data.FILE_SIZE);
		_viewer.getItem("FILE_ID").setValue(data._PK_);
	});
}*/
_viewer.getItem("FUJIAN").afterUploadCallback = function(fileData){
	_viewer.getItem("FILE_NAME").setValue(fileData[0]["FILE_NAME"]);
	_viewer.getItem("FILE_SIZE").setValue(fileData[0]["FILE_SIZE"]);
	_viewer.getItem("FILE_ID").setValue(fileData[0]["FILE_ID"]);
}
