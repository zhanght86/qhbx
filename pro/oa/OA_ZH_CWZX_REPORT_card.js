 _viewer= this;
_viewer.getItem("ZHENGWEN").afterUploadCallback= function(fileData){
	for(var id in fileData){
		_viewer.getItem("RT_NAME").setValue(fileData[id]["DIS_NAME"]);
		_viewer.getItem("RT_UPTIME").setValue(fileData[id]["S_MTIME"]);
		_viewer.getItem("FILE_ID").setValue(fileData[id]["FILE_ID"]);
	}
};
//修改过文件
_viewer.getItem("ZHENGWEN").afterModifyFile= function(datas){
for(var id in datas){
	_viewer.getItem("RT_NAME").setValue(datas[id]["DIS_NAME"]);
	_viewer.saveForm();
	_viewer.refresh();
}
};


 