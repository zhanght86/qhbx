var _viewer = this;



////恢复文件
//var recover =  _viewer.grid.getBtn("recover");
//recover.bind("click",function(){	
//	 var PKCode = jQuery(this).attr("rowpk");
//	 var serId = _viewer.servId;
//	 var content = FireFly.byId(serId,PKCode);
//	 FireFly.doAct(serId,"recoverHisFile",content,true,false);
//	 _viewer.refresh();
//	 _viewer.getParHandler().refresh();	
//});
// 查看历史文件
_viewer.grid.unbindTrdblClick();
//_viewer.grid.dblClick(function(value, node) {
//}, _viewer);
	//点击文件标题  查看历史文件\
	var _table = _viewer.grid.getTable();
	jQuery("td[icode='FILE_NAME']", _table).bind("click",function(){		
		var pk = jQuery(this).parent().attr("id");		
		var fileId = _viewer.grid.getRowItemValue(pk, "FILE_ID");
		var fileName = _viewer.grid.getRowItemValue(pk, "FILE_NAME");
		var file = new rh.ui.File();
		file.viewFile(pk, fileName);		
	});

	/* 对[操作]列添加操作链接 */
_viewer.grid.getBodyTr().each(function() {
			// 获取每一列的[操作]列
			var operate = jQuery(this).find("[icode='OPERATE']");
			var pCon = jQuery("<div></div>");
			var pkcode = jQuery(this).find("[icode='HISTFILE_ID']").html();
			// alert(newId);
			appendAObjs(pkcode, pCon);
			pCon.appendTo(operate);
		});
/* 向[操作]列添加操作链接代码 */
function appendAObjs(pkcode, pCon) {
//	debugger;
	var fileId = _viewer.grid.getRowItemValue(pkcode, "FILE_ID");
	var fileName = _viewer.grid.getRowItemValue(pkcode, "FILE_NAME");	
	var viewbtn = jQuery("<a href='javascript:void(0);'></a>").css({"margin" : "0px 3px 0px 5px"});
	var recoverbtn = jQuery("<a href='javascript:void(0);'></a>").css({"margin" : "0px 3px 0px 5px"});
	var downbtn = jQuery("<a href='javascript:void(0);'></a>").css({"margin" : "0px 3px 0px 5px"});
	viewbtn.html("  查看   ").css({"color" : "#008000"}).unbind("click").bind("click", function(event) {
        var file = new rh.ui.File();
		file.viewFile(pkcode, fileName);	
		});
	recoverbtn.html("  恢复文件   ").css({"color" : "#008000"}).unbind("click").bind("click", function(event) {
		if(!confirm("是否确定恢复文件？")){
			return;
		}
		var serId = _viewer.servId;
		var content = FireFly.byId(serId, pkcode);
		FireFly.doAct(serId,"recoverHisFile",content,true,false);
		_viewer.refresh();
		_viewer.getParHandler().refresh();	
	});			
   	downbtn.html("  下载   ").css({"color" : "#008000"}).unbind("click").bind("click", function(event) {
		var file =new rh.ui.File();
		file.downloadFile(pkcode, fileName);
		});	
		
	viewbtn.appendTo(pCon);
	recoverbtn.appendTo(pCon);
	downbtn.appendTo(pCon);
}		