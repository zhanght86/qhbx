var _viewer = this;

//编辑文稿
_viewer.getBtn("editword").unbind("click").bind("click",function() {
	 var content=_viewer.getItem ("ZHENGWEN").getValue();
	 if(content != ''){
	 var file = jQuery("td[code='ZHENGWEN'] .fileName a").attr("href");
	 var fileid=file.substring(6,file.search(/act/)-1);
	 var fileName = jQuery(" td[code='ZHENGWEN'] .fileName a").attr("title");
	 rh.ui.File.prototype.editFile(fileid,fileName);
	 } else{
	  alert("没有保存不能编辑");
	 }
	 
});
//套红头
_viewer.getBtn("createbody").unbind("click").bind("click",function() {
	rh.vi.gwExtCardView.prototype.redHat(event, "redHat",opts);
	 
});