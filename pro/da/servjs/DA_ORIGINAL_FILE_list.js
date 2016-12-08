var _viewer = this;


//获取数据
var fetchDataBtn  = _viewer.getBtn("fetchData");
if(fetchDataBtn.length > 0){
	fetchDataBtn.unbind("click").bind("click",function(){
		_viewer.fetchData(); //获取数据有可能是从不同类型，或者系统中来的数据，由各个服务对应的js去处理
	});	
}


//对选择的数据不归档 ， 只是修改数据的 O_FLAG 的值 为 4
var gdSelectNotBtn  = _viewer.getBtn("gdSelectNot");
if(gdSelectNotBtn.length > 0) {
	gdSelectNotBtn.unbind("click").bind("click",function(){
		gdSelectNot();
	});	
}

function gdSelectNot() {
	var pkArray = _viewer.grid.getSelectPKCodes();
	if (pkArray.length > 0) {
		var param = {};
		
		param.O_IDS = pkArray.join(",");
		var result = FireFly.doAct(_viewer.servId, "gdSelectNot", param, false);	
		
		if(result[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0){
			alert("操作成功");
			_viewer.refresh();
		} else {
			alert(result[UIConst.RTN_MSG]);
		}
	} else {
		alert("请选择要不归档的文件!");
	}	
}

//全部不归档
var gdAllNotBtn  = _viewer.getBtn("gdAllNot");
if(gdAllNotBtn.length > 0) {
	gdAllNotBtn.unbind("click").bind("click",function(){
		gdAllNot();
	});	
}

function gdAllNot() {
	var param = {};
	
	var result = FireFly.doAct(_viewer.servId, "gdAllNot", param, false);	
	
	if(result[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0){
		alert("操作成功");
		_viewer.refresh();
	} else {
		alert(result[UIConst.RTN_MSG]);
	}
}



//对全部的未归档的，都归
var gdAllBtn  = _viewer.getBtn("gdAll");
if(gdAllBtn.length > 0) {
	gdAllBtn.unbind("click").bind("click",function(){
		gdAll();
	});	
}

function gdAll() {
	//弹出页面，填写 保管期限， 和目录信息
	_openDialog(event, "gdSelect_dialog", "归档信息", "gdAll");
	
	_fillGdInfo("gdSelect_dialog");
}



//对选择的数据归档
var gdSelectBtn  = _viewer.getBtn("gdSelect");
if(gdSelectBtn.length > 0){
	gdSelectBtn.unbind("click").bind("click",function(){
		gdSelect();
	});	
}


function gdSelect() {
	var pkArray = _viewer.grid.getSelectPKCodes();
	if (pkArray.length > 0) {
	} else {
		alert("请选择要归档的文件!");
		
		return;
	}

	//弹出页面，填写 保管期限， 和目录信息
	_openDialog(event,"gdSelect_dialog" , "归档信息", "gdSelect");
	
	_fillGdInfo("gdSelect_dialog");
}

/**
 * 往弹出框中添加内容
 * @param dialogId 弹出框的ID
 */
function _fillGdInfo(dialogId) {
	//渲染dilog内容
	var dialogCon = jQuery("#" + dialogId);
	var table = jQuery("<table class='rhGrid' id='"+dialogId+"_table' align='center'></table>").appendTo(dialogCon);
	var thead = jQuery("<thead style='background-color:#F6F6F6;font-family:黑体,宋体' id='"+dialogId+"_thead'></thead>").appendTo(table);
	var tbody = jQuery("<tbody class='rhGrid-tbody' id='"+dialogId+"_tbody'></tbody>").appendTo(table);
	var termTr = jQuery("<tr></tr>").appendTo(tbody);
	var termTd = jQuery("<td style='width:80px;text-align:center;height:40px;font-size:14px;padding-top: 20px;'>保管期限</td>").appendTo(termTr);
	var termSelectTd = jQuery("<td style='width:200px;height:40px;padding-top: 20px;'></td>").appendTo(termTr);
	
	//保管期限 的 下拉框
	var termDict = FireFly.getDict("DA_MANAGE_TIME");
	
	var selectStr = "<select id ='" + dialogId + "DA_TERM' name ='DA_TERM'>";
	//selectStr += "<option value=''></option>";
	jQuery.each(termDict[0].CHILD,function(i,item){
		selectStr += "<option value=" + item.ITEM_CODE + ">" + item.ITEM_NAME + "</option>";
	});
	
	selectStr += "</select>";
	jQuery(selectStr).appendTo(termSelectTd);	
	
	
	var dirTr = jQuery("<tr></tr>").appendTo(tbody);
	var dirTd = jQuery("<td style='width:80px;text-align:center;height:40px;font-size:14px;padding-top: 20px;'>归档目录</td>").appendTo(dirTr);
	var dirInputTd = jQuery("<td style='width:200px;height:40px;padding-top: 20px;'></td>").appendTo(dirTr);
	
	var inputName = jQuery("<input id='"+dialogId+"DIR_NAME' type=text value='' readonly=true size='30'>").appendTo(dirInputTd);
	var linkOpenObj = jQuery("<a title='点击选择' >选择</a>").insertAfter(inputName);
	var inputCode = jQuery("<input id='"+dialogId+"DIR_CODE' type=hidden value=''>").appendTo(dirInputTd);
	jQuery("<input id='"+dialogId+"V_ID' type=hidden value=''>").appendTo(dirInputTd);
	jQuery("<input id='"+dialogId+"V_NUM' type=hidden value=''>").appendTo(dirInputTd);
	
	linkOpenObj.unbind("click").bind("click",function(event){
		var relatParam = {};
		relatParam.FILE_SERV = _viewer.servId;
		
		var result = FireFly.doAct("DA_ORIGINAL_RELATION", "finds", relatParam, false);
		var fixStr = "";
		if (result._DATA_.length > 0) {
			var fieldStr = result._DATA_[0].FIX_STR;
			
			if (fieldStr.length > 0) {
				fixStr = " and SERV_ID like '%" + fieldStr + "%'";
			}
		}
		
		var configStr = 'DA_DIR_MANAGE, {"TYPE":"single","EXTWHERE":" and S_ODEPT=\'' + System.getVar("@ODEPT_CODE@") + '\'' + fixStr +'"}';
		var extendTreeSetting = {"childOnly":true};
	
		var options = {
			"itemCode" : "itemCode",
			"config" : configStr,
			"hide" : "explode",
			"show" : "blind",
			"rebackCodes" : "itemCode",
			"replaceCallBack" : confirmSelectDir,
			"extendDicSetting" : extendTreeSetting,
			"dialogName" : "",
			"parHandler" : this
		};

		var dictView = new rh.vi.rhDictTreeView(options);
		dictView.show(event);
	});
	
}

/**
 * 弹出dialog
 * @param event 
 * @param IDdialog  dialog的ID，
 * @param title  标题
 * @returns
 */ 
function _openDialog(event , IDdialog , title, method){
	var _self = _viewer;
	this.dialogId = IDdialog;
	this.winDialog = jQuery("<div class='categoryDialog' id='"+this.dialogId+"' title='"+ title +"'></div>");
	this.winDialog.appendTo(jQuery("body"));
	var hei = 300;
	var wid = 600;
	
	var posArray = [];
	if (event) {
		var cy = event.clientY;
		posArray[0] = "";
		posArray[1] = cy + 100;
	}	
	
	//生成jqueryUi的dialog
	jQuery("#" + this.dialogId).dialog({
		autoOpen: false,
		height: hei,
		width: wid,
		modal: true,
		resizable:false,
		position:posArray,
		open: function() { 
		},
		close: function() {
			jQuery("#" + dialogId).remove();
		},buttons:{"确定":function(){
			var param = {};

			if (method == "gdSelect") { //对选择的归档，需要传这个
				var pkArray = _viewer.grid.getSelectPKCodes();
				
				param.O_IDS = pkArray.join(",");
			} else if (method == "gdAll") { //全部归档，则取当前页面的查询条件
				if (_viewer.whereData[_viewer.SEARCH_WHERE] != undefined) { 
					param.SEARCH_WHERE = _viewer.whereData[_viewer.SEARCH_WHERE];
				}
			}
			
			param.P_ID = jQuery("#gdSelect_dialogDIR_CODE").val();
			//jQuery("#gdSelect_dialogDIR_NAME").val();
			
			param.V_ID = jQuery("#gdSelect_dialogV_ID").val();
			param.V_NUM = jQuery("#gdSelect_dialogV_NUM").val();
			param.DA_TERM = jQuery("#gdSelect_dialogDA_TERM").val();
			
			var result = FireFly.doAct(_viewer.servId, method, param, false);	
			
			if(result[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0){
				alert("归档成功");
				_viewer.refresh();
				winDialog.remove();
			} else {
				alert(result[UIConst.RTN_MSG]);
			}
			
		},"取消":function(){
			winDialog.remove();
		}}
	});
	//手动打开dialog
	var dialogObj = jQuery("#" + this.dialogId);
	dialogObj.dialog("open");
	dialogObj.focus();
	jQuery(".ui-dialog-titlebar").last().css("display","block");//设置标题显示
}


/**
* 
* @param idArray 选择的目录的ID
* @param nameArray 选择的目录的名字
*/
function confirmSelectDir(idArray, nameArray) {
	var dirID = idArray[0];
	
	//判断如果选的类别是传统立卷的
	var dirParam = {"_PK_":dirID};
	var dirBean = FireFly.doAct("DA_DIR", "byid", dirParam , false);
	
//	if (dirBean.ARCH_STRATEGY == "2") { //传统立卷的 ， 就得选出是哪个卷 , 现在是已经知道了PID,列出下面的卷就行了
//	    var extWhereStr = "and P_ID = ^" + dirID + "^ and S_FLAG=1 and DA_FILE_TYPE = 1"; //未整理的卷
//		var inputName = "volumeCode";  //dirBean.SERV_ID
//		var configStr = "DA_VOLUME,{'TARGET':'V_ID~DA_NUM','SOURCE':'V_ID~DA_NUM~TITLE','PKHIDE':true,'EXTWHERE':'"+extWhereStr+"','TYPE':'single'}";
//		var options = {"itemCode":inputName,
//		"config" :configStr,
//		"rebackCodes":inputName,
//		"parHandler":this,
//		"formHandler":this,
//		"title":"选择要归到哪个卷",
//		"replaceCallBack":function(volObj){
//			    assignGdValue(dirID, nameArray[0], volObj.V_ID, volObj.DA_NUM);
//			}
//		};
//		var queryView = new rh.vi.rhSelectListView(options);
//		queryView.show(event);	
//		
//		return;
//	}
	
	assignGdValue(dirID, nameArray[0]);
}


/**
* 提交归档
* @param dirID 目录ID
* @param volID 立卷ID
* @param volNum 卷号
*/
function assignGdValue(dirID, dirName, volID, volNum) {
	jQuery("#gdSelect_dialogDIR_CODE").val(dirID);
	jQuery("#gdSelect_dialogDIR_NAME").val(dirName);
	
	if (volID) {
		jQuery("#gdSelect_dialogV_ID").val(volID);
		jQuery("#gdSelect_dialogV_NUM").val(volNum);		
	}
}


