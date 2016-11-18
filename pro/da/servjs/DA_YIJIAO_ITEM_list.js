var _viewer = this;
//为“添加档案”绑定事件
var addItem = _viewer.getBtn("addItem");
if(addItem.length > 0){
	addItem.unbind("click").bind("click",function(){
		openDialog(event);
	});
}
//为标题绑定链接
var chkboxs = _viewer.grid.getCheckBox();
jQuery(chkboxs).each(function(){
	var chkboxTd = jQuery(this);
		_viewer.grid.getRowByElement(this).unbind("dblclick").bind("dblclick",function(){
			var servId = _viewer.grid.getRowItemValueByElement(chkboxTd,"DA_SERV");
			var dataId = _viewer.grid.getRowItemValueByElement(chkboxTd,"DA_ID");
			var title =_viewer.grid.getRowItemValueByElement(chkboxTd,"DA_TYPE");
			var options = {"url":servId + ".card.do?pkCode=" + dataId +"&readOnly=true", "tTitle":title,"menuFlag":4};
			Tab.open(options);
		});
	
});


/**
 * 用户点击“移交查询”按钮后，弹出的窗口。
 * @param event
 * @returns
 */
function openDialog(event){
	this.winDialog = jQuery("<div class='open_a_dialog' id='open_a_dialog' title='移 交  查 询'></div>");
	this.winDialog.appendTo(jQuery("body"));
	jQuery("#open_a_dialog").dialog({
		autoOpen: false,
		height: 400,
		width: 500,
		modal: true,
		resizable:false,
		position:[200,100],
		buttons:{
			'确 定':function(){ 
				submitData(); 
			}, 
			'取 消':function(){ 
				jQuery("#open_a_dialog").remove();
			} 
		},
		open: function() { 
			selectWin();
		},
		close: function() {
			jQuery("#open_a_dialog").remove();
		}
	});
	//手动打开dialog
	var dialogObj = jQuery("#open_a_dialog");
	dialogObj.dialog("open");
	jQuery(".ui-dialog-titlebar").last().css("display","block");//设置标题显示
}

/**
 * 查询窗口:用户输入查询条件
 */
function selectWin(){
	var table = jQuery("<table class='rhGrid' id='boQuery_table' align='center'></table>");
	var thead = jQuery("<thead style='background-color:#F6F6F6;font-family:黑体,宋体' id='boQuery_thead'></thead>");
	var categorySelect =  jQuery(
		"<tr>" +
			"<td style='width:200px;text-align:center;height:40px;font-size:14px;padding-top:20px;'>选择分类 :</td>" +
			"<td style='padding-top:20px;'>" +
				"<div style='width:140px;height:24px;border:solid 1px #aaaaaa;'>"+
					"<select id ='categorySelect' style='width:138px;height:22px;margin:1px;'>" +
					"</select>" +
				"</div>"+
			"</td>" +
	    "</tr>");
	var titleInput = jQuery(
		"<tr>" +
			   "<td style='width:200px;text-align:center;height:40px;font-size:14px;'>标    题:</td>" +
			   "<td>" +
			   		"<input type='text' name='titleInput' id='titleInput' value=''  ></input>" +
			   "</td>" +
		"</tr>");
	var yearInput = jQuery(
		"<tr>" +
			   "<td style='width:200px;text-align:center;height:40px;font-size:14px;'>年    度 :</td>" +
			   "<td>" +
			   		"<input type='text' name='yearInput' id='yearInput' value=''  ></input>" +
			   "</td>" +
		"</tr>");
	var daCodeInput = jQuery(
			"<tr>" +
				   "<td style='width:200px;text-align:center;height:40px;font-size:14px;'>档    号 :</td>" +
				   "<td>" +
				   		"<input type='text' name='daCodeInput' id='daCodeInput' value=''  ></input>" +
				   "</td>" +
			"</tr>");
	var boxNumInput = jQuery(
			"<tr>" +
				   "<td style='width:200px;text-align:center;height:40px;font-size:14px;'>盒    号 :</td>" +
				   "<td>" +
				   		"<input type='text' name='boxNumInput' id='boxNumInput' value=''  ></input>" +
				   "</td>" +
			"</tr>");
	var daNumInput = jQuery(
			"<tr>" +
				   "<td style='width:200px;text-align:center;height:40px;font-size:14px;'>件    号 :</td>" +
				   "<td>" +
				   		"<input type='text' name='daNumInput' id='daNumInput' value=''  ></input>" +
				   "</td>" +
			"</tr>");
	categorySelect.appendTo(thead);
	titleInput.appendTo(thead);
	yearInput.appendTo(thead);
	daCodeInput.appendTo(thead);
	boxNumInput.appendTo(thead);
	daNumInput.appendTo(thead);
	thead.appendTo(table);
	table.appendTo(jQuery("#open_a_dialog"));
	var catObj = FireFly.doAct("DA_CATEGORY",'selectAllCat');
	for(var i=0 ; i< catObj["_DATA_"].length ; i++){
		jQuery("<option value='"+catObj["_DATA_"][i]["SERV_ID"]+"'>"+catObj["_DATA_"][i]["CAT_NAME"]+"</option>").appendTo(jQuery("#categorySelect"));
	}
	//暂不支持所有分类 jQuery("<option value='all'>所有分类</option>").appendTo(jQuery("#categorySelect"));
}
/**
 * 提交查询条件
 */
function submitData(){
	var extWhereStr = "";
	var catServId = jQuery("#categorySelect").val() || "";
	var title = jQuery("#titleInput").val() || "";
	var daYear = jQuery("#yearInput").val() || "";
	var daCode = jQuery("#daCodeInput").val() || "";
	var boxNum = jQuery("#boxNumInput").val() || "";
	var daNum = jQuery("#daNumInput").val() || "";
	var catObj = {};
	catObj["SERV_ID"] = catServId + "_ARCH";
	catObj["SERV_NAME"] = jQuery("#categorySelect").find("option:selected").text();
	if(title != ""){
		extWhereStr = " and TITLE like ^%" + title + "%^";
	}
	if(daYear != ""){
		extWhereStr += " and DA_YEAR like ^%" + daYear + "%^";
	}
	if(daCode != ""){
		extWhereStr += " and DA_CODE like ^%" + daCode + "%^";
	}
	if(boxNum != ""){
		extWhereStr += " and BOX_NUM like ^%" + boxNum + "%^";
	}
	if(daNum != ""){
		extWhereStr += " and DA_NUM like ^%" + daNum + "%^";
	}//只查询已归档文件
	var configStr = "";
	extWhereStr += " and S_ODEPT=^"+System.getVar("@ODEPT_CODE@")+"^ ";
	//当前列表已有数据
	var listData = _viewer["_listData"]["_DATA_"];
	if(catServId.indexOf("DA_VOLUME") < 0){
		extWhereStr += " and DA_FILE_TYPE=1 and V_ID is null ";
		if(listData.length > 0 ){
			extWhereStr += "and DANGAN_ID not in (";
			for(var i = 0 ; i < listData.length; i++){
				extWhereStr += "^"+listData[i]["DA_ID"]+"^,";
			}
			extWhereStr = extWhereStr.substring(0,extWhereStr.length-1)+")";
		}
		configStr =   catServId+"_BASE"+",{'TARGET':'DANGAN_ID','SOURCE':'DANGAN_ID~TITLE~DA_YEAR~DA_CODE~BOX_NUM~DA_NUM','EXTWHERE':'"+extWhereStr+"','PKHIDE':true,'TYPE':'multi'}";
	}
	if(catServId.indexOf("DA_VOLUME") >= 0){
		extWhereStr += " and DA_FILE_TYPE=1 and SERV_ID = ^"+catServId+"^ ";
		if(listData.length > 0 ){
			extWhereStr += " and V_ID not in ( ";
			for(var i = 0 ; i < listData.length; i++){
				extWhereStr += "^"+listData[i]["DA_ID"]+"^,";
			}
			extWhereStr = extWhereStr.substring(0,extWhereStr.length-1)+")";
		}
		
		configStr = "DA_VOLUME_V,{'TARGET':'V_ID','SOURCE':'V_ID~TITLE~DA_YEAR~DA_CODE~BOX_NUM~DA_NUM','EXTWHERE':'"+extWhereStr+"','PKHIDE':true,'TYPE':'multi'}"; 
	}
	var options = {
	"config" :configStr,
	"title":"选择要移交的档案",
	"replaceCallBack":function(Obj){
		submitSelectedDA(catObj,Obj);
		}
	};
	var queryView = new rh.vi.rhSelectListView(options);
	queryView.show(event);
	jQuery("#open_a_dialog").remove();
}

/**
 * 处理要借阅的档案的信息
 * @param catServId 类型
 * @param volObj
 */
function submitSelectedDA(catObj,Obj){
	var datas = {};
	datas["YJ_ID"] = _viewer.getParHandler()._pkCode;
	datas["DA_TITLE"] = Obj.TITLE;
	datas["DA_TYPE"] = catObj.SERV_NAME;
	datas["DA_ID"] = Obj.DANGAN_ID;
	datas["DA_SERV"] = catObj.SERV_ID;
	if(catObj.SERV_ID.indexOf("DA_VOLUME") >= 0){
		datas["DA_ID"] = Obj.V_ID;
	}
	var resultObj = FireFly.doAct(_viewer.servId,"insertItemInfo",datas);
	if(resultObj["_MSG_"].indexOf("OK") >= 0){
		_viewer.getParHandler().refresh();
		_viewer.listBarTip("添加成功");
	}
}