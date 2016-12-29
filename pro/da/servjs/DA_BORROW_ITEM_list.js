var _viewer = this;
debugger;
//添加档案 按钮显示条件。没有变量或者已经办结则隐藏。
if (_viewer.getParHandler() != null) {
//var addItem = _viewer.getParHandler().wfCard.getCustomVarContent("addItem");
var s_user = _viewer.getParHandler().getItem("S_USER").getValue();
if( _viewer.getParHandler().itemValue("S_WF_STATE") == 2 || s_user !=System.getVar("@USER_CODE@")){
	_viewer.getBtn("boQuery").hide();
	_viewer.getBtn("delete").hide();
}
}
//“操作”一列显示条件.工作流节点中定义该变量
if (_viewer.getParHandler() != null) {
var borrowCheck = _viewer.getParHandler().wfCard.getCustomVarContent("BorrowCheck");
if(borrowCheck != 1 || _viewer.getParHandler().itemValue("S_WF_STATE") == 2){
	jQuery("[icode='OPERATION_S']",_viewer.grid._table).css("display","none");
	_viewer.grid.getTdItems("OPERATION_S").css("display","none");
}
//1.循环添加“操作”一列的按钮。2.根据条件绑定链接
jQuery.each(this.grid.getBodyTr(),function(i,n){
	var boFlag = jQuery(this).find('td[icode="BO_FLAG"]').text();
	//找到当前行对应的操作列
	var optTdObj = jQuery(this).find('td[icode="OPERATION_S"]');
	//如果“申请中，已收回”，显示“同意借阅”按钮
	if(boFlag ==1 || boFlag==3){
		_bldRowBtn(jQuery(this).attr("id"),"boAgree","同意借阅","edit",boAgree,jQuery(this)).appendTo(optTdObj);
	}
	//如果已同意，显示 “收回档案”按钮
	if(boFlag == 2){
		_bldRowBtn(n.id,"boRetrive","收回档案","edit",boRetrive,n).appendTo(optTdObj);
	}
	//当前用户是否是拥有“c180819418981e510118a62308ac00ee”角色
	var isDAManager = (System.getVar("@ROLE_CODES@").indexOf("c180819418981e510118a62308ac00ee") >= 0) ;
	if(boFlag == 2 || isDAManager){ //如果状态是 “已同意”或者是系统管理员则绑定连接
		jQuery(this).find('td[icode="DA_TITLE"]').unbind("click").bind("click",function(){
			var servId = jQuery(n).find('td[icode="DA_SERV"]').text();
			var dataId = jQuery(n).find('td[icode="DA_ID"]').text();
			var title = jQuery(n).find('td[icode="DA_TYPE"]').text();
			var options = {"url":servId + ".card.do?pkCode=" + dataId +"&readOnly=true", "tTitle":title,"menuFlag":4};
			Tab.open(options);
		});
	}
});
//添加档案  按钮
var actCodeBtnObj = _viewer.getBtn('boQuery');	
actCodeBtnObj.unbind("click").bind("click",function(event) {
	openDialog(event);
});
//同意借阅 按钮
function boAgree(event){
	var PKCode = event.data.id;
	var data = {};
	data["_PK_"] = PKCode;
	data["BO_FLAG"] = 2;
	data["OPER_TIME"] = rhDate.getCurentTime();
	FireFly.cardModify(_viewer.servId,data);
	_viewer.refresh();
	_viewer.getParHandler().cardBarTip("借阅成功");
}
//借阅收回  按钮
function boRetrive(event){
	var PKCode = event.data.id;
	var data = {};
	data["_PK_"] = PKCode;
	data["BO_FLAG"] = 3 ;
	data["OPER_TIME"] = rhDate.getCurentTime();
	FireFly.cardModify(_viewer.servId,data);
	_viewer.refresh();
	_viewer.getParHandler().cardBarTip("收回成功");
}
/**
 * 用户点击“借阅查询”按钮后，弹出的窗口。
 * @param event
 * @returns
 */
function openDialog(event){
	this.winDialog = jQuery("<div class='open_a_dialog' id='open_a_dialog' title='借 阅 查 询'></div>");
	this.winDialog.appendTo(jQuery("body"));
	jQuery("#open_a_dialog").dialog({
		autoOpen: false,
		height: 400,
		width: 500,
		modal: true,
		resizable:false,
		position:[event.clientX,event.clientY-200],
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
//GW_CODE文号这个字段可能除文书档案类型外，其他档案没有这个字段，暂时屏蔽
//	var gwCodeInput = jQuery(
//			"<tr>" +
//				   "<td style='width:200px;text-align:center;height:40px;font-size:14px;'>文    号 :</td>" +
//				   "<td>" +
//				   		"<input type='text' name='gwCodeInput' id='gwCodeInput' value=''  ></input>" +
//				   "</td>" +
//			"</tr>");
	categorySelect.appendTo(thead);
	titleInput.appendTo(thead);
	yearInput.appendTo(thead);
	daCodeInput.appendTo(thead);
	boxNumInput.appendTo(thead);
	daNumInput.appendTo(thead);
//GW_CODE文号这个字段可能除文书档案类型外，其他档案没有这个字段，暂时屏蔽
//	gwCodeInput.appendTo(thead);
	thead.appendTo(table);
	table.appendTo(jQuery("#open_a_dialog"));
	/**处理分类：只显示一文一件分类，不显示立卷类**/
	var catObj = FireFly.doAct("DA_CATEGORY",'selectYiWenYiJuan');
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
//GW_CODE文号这个字段可能除文书档案类型外，其他档案没有这个字段，暂时屏蔽
//	var gwCode = jQuery("#gwCodeInput").val() || "";
	var catObj = {};
	catObj["SERV_ID"] = catServId;
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
	}
//GW_CODE文号这个字段可能除文书档案类型外，其他档案没有这个字段，暂时屏蔽
//	if(gwCode != ""){
//		extWhereStr += " or GW_CODE like ^%" + gwCode + "%^";
//	}
	//增加查询条件：当前机构、档案公开级别为 只公开标题的、档案库中的文件
	extWhereStr += " and S_ODEPT=^"+System.getVar("@ODEPT_CODE@")+"^ and DA_OPEN_LEVEL=2 and DA_FILE_TYPE = 2 ";
	var configStr =   catServId+"_BASE,{'TARGET':'DANGAN_ID','SOURCE':'DANGAN_ID~TITLE~DA_YEAR~DA_CODE~BOX_NUM~DA_NUM','EXTWHERE':'"+extWhereStr+"','PKHIDE':true,'TYPE':'multi'}";
	var options = {
	"config" :configStr,
	"title":"选择要借阅的档案",
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
	datas["BO_ID"] = _viewer.getParHandler()._pkCode;
	datas["DA_TITLE"] = Obj.TITLE;
	datas["DA_TYPE"] = catObj.SERV_NAME;
	datas["DA_ID"] = Obj.DANGAN_ID;
	datas["DA_SERV"] = catObj.SERV_ID;
	var resultObj = FireFly.doAct(_viewer.servId,"insertBorrowDA",datas);
	if(resultObj["_MSG_"].indexOf("OK") >= 0){
		_viewer.getParHandler().refresh();
		_viewer.listBarTip("添加成功");
	}
}
/**
 * 构建行按钮
 * @param pkCode 数据主键
 * @param actCode 按钮编码
 * @param actName 按钮名称
 * @param imgClass 按钮样式
 * @param func 按钮操作
 * @param obj TR对象
 * @return
 */
function _bldRowBtn(pkCode, actCode, actName, imgClass, func, obj){
	var btn = jQuery('<a class="rh-icon rhGrid-btnBar-a" id="DA_BORROW_ITEM-' + actCode 
			+ '" actcode="' + actCode + '"><span class="rh-icon-inner">' 
			+ actName + '</span><span class="rh-icon-img btn-' + imgClass + '"></span></a>');
	btn.bind("click",{"id":pkCode,"trObj":obj},func);
	return btn;
}

}

//意见与子服务颠倒位置：将主单的意见放在最下面，并且重置元素高度
//if (_viewer.getParHandler()) {
//	setTimeout(function(){
//		var tableSize = _viewer.grid.getBodyTr().length;
//			jQuery("#MIND_FIELDSET").before(jQuery("#DA_BORROW-mainTab").find(".rhCard-tabs"));
//			jQuery("#DA_BORROW-winDialog").find(".ui-tabs").css({
//				"min-height": "150px"
//			});
//			if (tableSize == 1) {
//				jQuery("#MIND_FIELDSET").css({"margin-top":"50px"});
//			} else {
//				jQuery("#MIND_FIELDSET").css({"margin-top": 50 + (tableSize - 1) * 30 + "px"});
//			}
//		}
//	, 0);
//	var winDialogObj = jQuery("#DA_BORROW-winDialog");
//	var timer = setInterval(function(){
//		winDialogObj.css({"height": "inherit"});
//	},10);
//}