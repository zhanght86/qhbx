var _viewer = this;

var previewBtn = _viewer.getBtn('preview');
//预览
previewBtn.unbind("click").click(function(){
	if(!_viewer.form.validate()) {// 校验通过才可预览
		_viewer.cardBarTipError("校验未通过");
		return false;
	}

	var servId = _viewer.servId;
	var fileId = _viewer.itemValue("DOCUMENT_FILE");
	var index = fileId.indexOf(",",0);
	if (-1 < index) {
		fileId = fileId.substring(0, index);
	}
	var data = {};
	//var url = "/file/" + fileId + "?act=preview";
	//var url = "/wenku/content/" + _viewer.itemValue("DOCUMENT_ID") + ".html";
	var url = "/cms/SY_COMM_WENKU_DOCUMENT/" + _viewer.itemValue("DOCUMENT_ID") + ".html";
	//window.open(url);
	
	var opts={'scrollFlag':true , 'url':url,'tTitle':'文档预览','menuFlag':3};
	Tab.open(opts);
	
});


//文件上传区块
var uploadDiv = jQuery("<div></div>");
//删除定位框
var par = _viewer.getItem("DOCUMENT_UPLOAD").getBlank();
uploadDiv.insertBefore(par);
par.remove();

//添加时显示上传按钮
if (_viewer._actVar == UIConst.ACT_CARD_ADD){
	//文件上传
	var config = {"SERV_ID":"SY_COMM_WENKU_DOCUMENT", "FILE_CAT":"SHARE_FILE", "TYPES":"*.pdf;*.doc;*.docx;*.wps;*.xls;*.xlsx;*.ppt;*.pptx;*.txt;*.flv;*.mp3;"};
	var rhfile = new rh.ui.File({"config":config}); 

	uploadDiv.append(rhfile.obj);
	
	rhfile.initUpload();
	
	rhfile.fillData([]);
	
	//before upload
	rhfile.beforeUpload = function(){
	};  
	//callback
	rhfile.fillData = function(val) {
		for ( var i = 0; i < val.length; i++) {
			var file = val[i];
			var fileId = file["FILE_ID"];
			var fileName = file["FILE_NAME"];
			_viewer.getItem('DOCUMENT_TITLE').setValue(fileName);
			_viewer.getItem('DOCUMENT_FILE').setValue(fileId);
//			alert("上传成功:" + fileName);
			rhfile.obj.hide();
			uploadDiv.append("上传成功:" + fileName);
		}
	};
	
} else if (_viewer._actVar == UIConst.ACT_CARD_MODIFY) {
	//不允许重新上传文件
	var fileId = _viewer.itemValue("DOCUMENT_FILE");
	var index = fileId.indexOf(",",0);
	if (-1 < index) {
		fileId = fileId.substring(0, index);
	}
	var fileName ="";
		var param ={};
		param["_PK_"] =  fileId;
		var fileBean = FireFly.doAct("SY_COMM_FILE", "byid", param);
		if (fileBean &&  fileBean.FILE_NAME) {
			fileName = fileBean.FILE_NAME;
		}
	 uploadDiv.html(fileName);
}





//显示栏目下内容模版继承关系
var docTmpl = _viewer.getItem('TMPL_EXTENDS').getValue();
 if(1 == docTmpl) {
	 var value = _viewer.getItem('TMPL_ID').getText();
	_viewer.getItem('TMPL_ID').setText("--继承-- " + value);
}
 
//显示栏内容评论继承关系
 var commentExtends = _viewer.getItem('DOCUMENT_COMMENT_EXTENDS').getValue();
  if(1 == commentExtends) {
 	 var text = _viewer.getItem('DOCUMENT_COMMENT_STATUS').getText();
	 var value = _viewer.getItem('DOCUMENT_COMMENT_STATUS').getValue();
 	 
 	 _viewer.getItem('DOCUMENT_COMMENT_STATUS').obj.find("option[value='" + value + "']").text("--继承-- " + text);
 }
/* 
//新闻审核状态不存在，自动设置为“已审核”（对以前的数据补充）
(function autoChecked(){
	var newsChecked = _viewer.getItem("NEWS_CHECKED");
	if(!newsChecked.getValue()){
		newsChecked.setValue("2");
	}
})();
*/
