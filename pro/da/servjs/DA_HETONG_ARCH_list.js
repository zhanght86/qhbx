var _viewer = this;

//来自pro/da/da/SubDirTree.js的方法daListServ("","","")
daListServ(_viewer , "文书档案库" , "subDeptDA");
var daBtn = new rh.da.rhDABtns({"servId" : _viewer.opts.sId, "parHandler" : _viewer});
daBtn.init();


/**
 * 创建导入按钮
 */
var impExcelBtn  = _viewer.getBtn("impExcel");
if(impExcelBtn.length > 0) {
	impExcelBtn.unbind("mouseover").bind("mouseover", function(){
		var config = {
			"SERV_ID": _viewer.servId,
			"FILE_CAT": "SERV_UPLOAD",
			"FILENUMBER": 1,
			"VALUE": 15,
			"TYPES": "*.xls;",
			"DESC": "excel导入",
			"TEXT":"excel导入",
		};
		var file = new rh.ui.File({
			"config" : config
		});
		
		file.obj.find("table").eq(0).find("tr").each(function(i){
			if (i > 0) {
				jQuery(this).hide();
			}
		});
		
		file.obj.find("span[class = 'rh-icon-inner']")
			.append("<span style='margin-right: 4px;display: block;width: 16px;height: 16px; float: left; position: relative; top: 5px;' class='btn-imp'></span>");
		impExcelBtn.parent().append(file.obj);
		impExcelBtn.remove();
		file.initUpload();
		
		//上传后调用
		file.afterQueueComplete = function(fileData){
			file.progressDialog.dialog("close");
			var fileData = file.getFileData();
			var fileId = "";
			for (var key in fileData) {
				fileId = key;
			}
			var navTree = _viewer.getNavTreeObj("@com.rh.da.dir.ExtSubDirDict");
			var out = FireFly.doAct(_viewer.servId, "impExcel", {"FILE_ID":fileId, "P_ID":navTree.getCurrentNode().ID});
			if (out["_MSG_"].indexOf("OK,") >= 0) {
				_viewer.listBarTip("导入成功");
				_viewer.refresh();
			} else {
				_viewer.listBarTipError("导入失败");
			}
		};
	});
}
