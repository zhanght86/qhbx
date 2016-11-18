var _viewer = this;
jQuery(document).data("_cardViewer",_viewer);
var SUPPORT_EXT_NAME =  "*.doc;*.docx;*.excel;*.pdf;*.wps;*.xls;*.xlsx;*.jpg;*.gif;*.tiff;*.png;*.txt;*.bmp;*.jpeg;";

var fileItem = _viewer.getItem("ITEM_FILE"); //文件列表字段
if(fileItem){
	jQuery(document).data("itemReadType",_viewer.itemValue("READ_TYPE"));
	
	fileItem.afterFlashLoaded = function(){
		jQuery(document).data("_falshLoaded","true");
		changeFileTypes(fileItem);
	}
	
	//上传之前，传递参数
	fileItem.beforeUploadCallback = function(file){
		//后台配置监听，处理安全文件及其对应管理
		fileItem.addPostParam("listener","oaLibItem");
		fileItem.addPostParam("READ_TYPE",_viewer.itemValue("READ_TYPE"));
		return true;
	}
	
	//上传文档之后，判断卡片是否保存，如果没保存，则自动保存。
	fileItem.afterQueueComplement = function(){
		if(_viewer.getByIdData("_ADD_") == "true"){
			var fileData = fileItem.getFileData();
			var fileBean = null;
			for(attr in fileData){
				fileBean = fileData[attr];
			}
			if(fileBean){ //上传文件之后，保存卡片，文件标题作为卡片标题
				if(_viewer.itemValue("TITLE") == ""){
					_viewer.getItem("TITLE").setValue(fileBean.DIS_NAME);
				}
			}
			
			//自动保存文件。增加延时，避免出现上传对话框还没有关闭的bug。
			setTimeout(function(){
				_viewer.saveForm();
			},200);		
		}
	}
}

function changeFileTypes(fileItem){
	if (jQuery(document).data("itemReadType") == 1) {
		fileItem.setFileTypes(SUPPORT_EXT_NAME, SUPPORT_EXT_NAME);
	} else {
		fileItem.setFileTypes("*.*", "all files");
	}
}


//保存文件之前，判断是否修改了阅读方式，如果改成只读，则判断文件类型是否能转换成安全文档。
_viewer.beforeSave = function() {
	var newType = _viewer.itemValue("READ_TYPE");
	var oldType = _viewer.getByIdData("READ_TYPE");
	
	if(newType != oldType && newType ==  1){ //由下载改成只读，判断文件类型是否符合要求
		var fileData = fileItem.getFileData();
		for(var key in fileData){
			var extName = Tools.getFileSuffix(fileData[key].FILE_NAME);
			if(SUPPORT_EXT_NAME.indexOf("*." + extName.toLowerCase() + ";") >= 0){
				continue;
			}else{
				alert("安全文档不支持" + fileData[key].DIS_NAME + "(" + extName + ")类型文件，不能改成只读。");
				Tip.clearLoad();
				return false;
			}
		}
	}
	
	return true;
}

//修改文件类型后，修改允许上传的文件类型。
jQuery("input[type='radio']",_viewer.getItem("READ_TYPE").obj).click(function(){
	jQuery(document).data("itemReadType",_viewer.itemValue("READ_TYPE"));
	if(jQuery(document).data("_falshLoaded") == "true"){
		changeFileTypes(fileItem);
	}
});