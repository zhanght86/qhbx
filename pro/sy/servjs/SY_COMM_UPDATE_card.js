var _viewer = this;
//隐藏保存按钮
var saveBtn = _viewer.getBtn("save");
if(saveBtn){
	saveBtn.hide();
}
//检索按钮
var searchBtn=_viewer.getBtn("UPDATE_SEARCH");
if(searchBtn){
	searchBtn.unbind("click").bind("click",function(){
		var param={};
		param["UPDATE_BEGIN_DATE"] = _viewer.form.getItem("UPDATE_BEGIN_DATE").getValue();
		param["UPDATE_END_DATE"] = _viewer.form.getItem("UPDATE_END_DATE").getValue();
		param["UPDATE_FILE_LIST"] = _viewer.form.getItem("UPDATE_FILE_LIST").getValue();
		FireFly.doFormAct(_viewer.servId,"searchFile",param,"_blank");
	});
}
//更新按钮
var updateBtn = _viewer.getBtn("UPDATE_UPLOAD");
if(updateBtn){
	updateBtn.unbind("click").bind("click",function(){
		var file = _viewer.form.getFile("UPDATE_FILE");
		if(file){
			if(file.isNull()){
				alert("请上传程序更新包");
			} else {
				var fileId = file.getFileId();
				if (fileId && fileId.length > 0 && fileId.indexOf(",") >= 0) {
					var id_name = fileId.split(",");
					fileId = id_name[0];
				}
				FireFly.doFormAct(_viewer.servId,"showUpdateFile",{"FILE_ID":fileId},"_blank");
			}
		} else {
			alert("未找到上传文件字段");
		}
		
	});
}