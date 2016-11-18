var _viewer = this;
if(_viewer.getPKCode()) {//初始化用印文件列表
	//获取用印文件
	FireFly.doAct("BN_SEAL_FILE_LIST","finds",{"APPLY_ID":_viewer.getPKCode(),"FILE_TYPE":"0"},false).then(function(result){
		if (result && result["_DATA_"]) {
			var fileData = result["_DATA_"];
			var fileContainer = _viewer.form.getItem("SEAL_FILE_LIST").text;
			for(var i=0; i<fileData.length; i++){
				var index = fileData[i].FILE_OBJ.indexOf(",");
				var fileId = fileData[i].FILE_OBJ.substring(0,index);
				var fileName = fileData[i].FILE_OBJ.substring(index+1,fileData[i].FILE_OBJ.length-1);
				$("<a href='"+FireFlyContextPath + "/file/" + fileId + "?mobile=1' class='sc-file-link' data-ajax='false' data-filename='"+fileId+"'>" + fileName + "</a>").appendTo(fileContainer);
			}
		}
	});
}
