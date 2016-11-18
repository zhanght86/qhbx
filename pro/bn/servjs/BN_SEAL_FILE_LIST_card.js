var _viewer = this;
/*var defaultTypes = "*.jpg;*.jpeg;*.png;*.gif;*.doc;*.docx;*.wps;*.xls;*.xlsx;*.ppt;*.pptx;*.rar;*.zip;*.7z;*.gz;*.tar;*.txt;*.chm;*.pdf;*.xml;";
this._sealData = FireFly.doAct("BN_SEAL_USE_LIST","finds",{"FILE_ID":_viewer.getItem("FILE_ID").getValue(),"APPLY_ID":_viewer.getItem("APPLY_ID").getValue()});
if(this._sealData._DATA_.length>0){
	if(this._sealData._DATA_[0].SEAL_TYPE==2){
		
		_viewer.getItem("FILE_OBJ")._file._upload._opts.file_types=defaultTypes;
		_viewer.getItem("FILE_OBJ")._file._upload._opts.file_types_description=defaultTypes;
		//_viewer.form.getAttachFile("FILE_OBJ").getUpload()._opts.file_types=defaultTypes;
		//_viewer.form.getAttachFile("FILE_OBJ").getUpload()._opts.file_types_description=defaultTypes;
	}else if(this._sealData._DATA_[0].SEAL_TYPE==1){
		_viewer.getItem("FILE_OBJ")._file._upload._opts.file_types="*.doc;";
		_viewer.getItem("FILE_OBJ")._file._upload._opts.file_types_description="*.doc;";
		//_viewer.form.getAttachFile("FILE_OBJ").getUpload()._opts.file_types="*.doc;";
		//_viewer.form.getAttachFile("FILE_OBJ").getUpload()._opts.file_types_description="*.doc;";
	}
//_viewer.getItem("FILE_OBJ").afterRender();	
}*/

//if(this._sealData._DATA_[0].SEAL_TYPE==2){
	//_viewer.getItem("FILE_OBJ")._file._config.TYPES=defaultTypes；
	//_viewer.form.getAttachFile("FILE_OBJ").getUpload()._opts.file_types=defaultTypes;
	//_viewer.form.getAttachFile("FILE_OBJ").getUpload()._opts.file_types_description=defaultTypes;
//}else(this._sealData._DATA_[0].SEAL_TYPE==1){
	//_viewer.getItem("FILE_OBJ")._file._config.TYPES="*.doc;";
	//_viewer.form.getAttachFile("FILE_OBJ").getUpload()._opts.file_types="*.doc;";
	//_viewer.form.getAttachFile("FILE_OBJ").getUpload()._opts.file_types_description="*.doc;";
//}
//隐藏文件名的链接
if(_viewer.getItem("FILE_OBJ") && _viewer.getItem("FILE_OBJ").getValue()){
	jQuery(jQuery("#BN_SEAL_FILE_LIST-FILE_OBJ_NAME")[0].children[0].children[0]).removeAttr("href");
}

if(_viewer.params && _viewer.params["APPLY_ID"]) {
	_viewer.getItem("APPLY_ID").setValue(_viewer.params["APPLY_ID"]);
}

if(_viewer.params && _viewer.params["APPLY_TYPE"]) {
	_viewer.getItem("APPLY_TYPE").setValue(_viewer.params["APPLY_TYPE"]);
}

if(_viewer.params) {
	var readOnly = _viewer.params["READ_ONLY"];
	if(readOnly == true) {
		_viewer.readCard();
	}
}
