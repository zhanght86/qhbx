var _viewer = this;
_viewer.getItem("FUJIAN").afterUploadCallback = function(fileData){
	_viewer.getItem("LINK_NAME").setValue(fileData[0]["FILE_NAME"]);
	/*_viewer.getItem("FILE_SIZE").setValue(fileData[0]["FILE_SIZE"]); */
	_viewer.getItem("FILE_ID").setValue(fileData[0]["FILE_ID"]);
}