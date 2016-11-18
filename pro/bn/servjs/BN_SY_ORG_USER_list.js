var _viewer = this;

//标识退休人员的记录为灰色背景
var userStateCells = _viewer.grid.getTdItems("USER_STATE");
userStateCells.each(function() {
	var cellObj = jQuery(this);
	var rowObj = cellObj.parent();
	
	
	var flagObj = jQuery("td[icode='S_FLAG']",rowObj);
	var flag = flagObj.text();
	
	
	var val = cellObj.text();
	if(val == 3){
		rowObj.css({'background-color':'gray'});
		var userNameObj = jQuery("td[icode='USER_NAME']",rowObj);
		var userName = userNameObj.text();
		if(userName){
			userNameObj.text(userName + "（退休）");
		}
	}
	if(val == 2){
		rowObj.css({'background-color':'gray'});
		var userNameObj = jQuery("td[icode='USER_NAME']",rowObj);
		var userName = userNameObj.text();
		if(userName){
			userNameObj.text(userName + "（离职）");
		}
	}
});

// 查看日志按钮绑定事件
_viewer.getBtn("viewLog").unbind("click").bind("click",function(){
	Tab.open({'url' : 'BN_IMP_LOG.list.do','tTitle' : 'excel导入结果查询','menuFlag' : 4});
});
// excel导入按钮绑定事件
_viewer.getBtn("excelImp").unbind("click").bind("click",function(){
	var config = {"SERV_ID":_viewer.servId, "FILE_CAT":"EXCEL_UPLOAD", "FILENUMBER":1, 
		    		"VALUE":15, "TYPES":"*.xls;*.xlsx", "DESC":"导入Excel文件"};
	var file = new rh.ui.File({
		"config" : config
	});
		    	
	var importWin = new rh.ui.popPrompt({
		title:"请选择文件",
		tip:"请选择要导入的Excel文件：",
		okFunc:function() {
			var fileData = file.getFileData();
			if (jQuery.isEmptyObject(fileData)) {
				alert("请选择文件上传");
				return;
			}
			var fileId = null;
			for (var key in fileData) {
				fileId = key;
			}
			if (fileId == null){
				alert("请选择文件上传");
				return;
			}
			var param = {};
			param["fileId"] = fileId;
			//提交  导入 只将fileId传入即可
			rh_processData(_viewer.servId + ".excelImp.do", param, true);
			importWin.closePrompt();
			_viewer.refreshGrid();
			file.destroy();
		},
		closeFunc:function() {
		    file.destroy();
		}
	});
	importWin._layout(event,undefined,[450,230]);
		    	
	var container = jQuery("#" + importWin.dialogId);
	container.empty();
	importWin.tipBar = jQuery("<div></div>").text(importWin.tip).css({"height":"40px","font-weight":"normal","margin":"15px 15px 0px 15px","color":"red"});
	container.append(importWin.tipBar);
	container.append(file.obj);
	file.obj.css({'margin-left':'5px'});
	file.initUpload();
});