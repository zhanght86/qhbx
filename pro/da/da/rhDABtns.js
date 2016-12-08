GLOBAL.namespace("rh.da");

var printValArr = "";
var printNameArr = "";
rh.da.rhDABtns = function(options) {
	this._viewer = options.parHandler || null;
	this.selectAllFlag;
	this.dataIdsArr = new Array();
}


/**
 * 验证是否是数字
 */
rh.da.rhDABtns.prototype.isNumber = function(oNum){
	  if(!oNum) return false;

	  var strP=/^\d+$/; //正整数

	  if(!strP.test(oNum)) return false;

	  return true;	
}

/**
 * 初始化按钮
 * 
 * @param _viewer
 * @returns
 */
rh.da.rhDABtns.prototype.init = function(){
	var _self = this;
	//去掉保存按钮。列表页面中的“件号调整”要可编辑，必须把列表编辑功能打开。打开后，有保存按钮，要隐藏掉。
	_self._viewer.getBtn(UIConst.ACT_BATCH_SAVE).hide();
	/**件号调整 (一文一卷档案或者卷信息操作按钮)**/
	var jianTZ  = _self._viewer.getBtn("jianTZ");
	if(jianTZ.length > 0){
		jianTZ.unbind("click").bind("click",function(){
			_self.jianTZ();
		});	
		
		
		//判断如果件号有调整，则自动保存，
		jQuery(".batchModify").unbind("change").bind("change", function(event){
			//修改的那一行的主键
			var trPkCode = jQuery(event.target).parent().parent().attr("id");
			
			//修改的件号值
			var jianHao = jQuery(event.target).val();
			
			//判断件号是正确的数字
			if (_self.isNumber(jianHao)) {
				var datas = {};
				datas["dataValue"] = trPkCode + "--" + jianHao;
				var resultObj = FireFly.doAct(_self._viewer.servId,"updateJianHao",datas);
				if(resultObj["_MSG_"].indexOf("OK")>=0){ 
					_self._viewer.listBarTip("修改成功");
				}				
			} else {
				alert("请输入数字");
			}
		});
	}
	/**报表编目 (一文一卷档案或者卷信息操作按钮)**/
	var baoBiaoBM = _self._viewer.getBtn("baoBiaoBM");
	if(baoBiaoBM.length > 0){
		baoBiaoBM.unbind("click").bind("click",function(){
			_self.printDa();
		});
	}
	/**档案装盒 (一文一卷档案或者卷信息操作按钮)**/
	var danganZH = _self._viewer.getBtn("danganZH");
	if(danganZH.length > 0){
		danganZH.unbind("click").bind("click",function(){
			_self.danganZH();
		});
	}
	/**生成档号 (一文一卷档案或者卷信息操作按钮)**/
	var shengchengDH = _self._viewer.getBtn("shengchengDH") ;
	if(shengchengDH.length > 0 ){
		shengchengDH.unbind("click").bind("click",function(){
			_self.shengchengDH();
		});
	}
	/**清除档号 (一文一卷档案或者卷信息操作按钮)**/
	var clearDH = _self._viewer.getBtn("clearDH") ;
	if(clearDH.length > 0 ){
		clearDH.unbind("click").bind("click",function(){
			_self.clearDH();
		});
	}
	/** 合卷 按钮(暂不实现)**/
	var mergeVolume = _self._viewer.getBtn("mergeVolume");
	if(mergeVolume.length > 0){
		mergeVolume.unbind("click").bind("click",function(){
			_self.mergeVolume();
		});
	}
	/**拆卷 按钮(暂不实现) **/
	var tearVolume = _self._viewer.getBtn("tearVolume");
	if(tearVolume.length > 0 ){
		tearVolume.unbind("click").bind("click",function(){
			_self.tearVolume();
		});
	}
	/**组已有卷 (一文一卷档案或者卷信息操作按钮)**/
	var gdToVolume = _self._viewer.getBtn("gdToVolume");
	if(gdToVolume.length > 0 ){
		gdToVolume.unbind("click").bind("click",function(){
			_self.gdToVolume();
		});
	}
	/**组新卷 (一文一卷档案或者卷信息操作按钮)**/
	var gdToNewVolume = _self._viewer.getBtn("gdToNewVolume");
	if(gdToNewVolume.length > 0 ){
		gdToNewVolume.unbind("click").bind("click",function(){
			_self.gdToNewVolume();
		});
	}
	/**卷内件号调整 按钮(立卷档案卷内文件件号调整)**/
	var jianTZV = _self._viewer.getBtn("jianTZV");
	if(jianTZV.length > 0){
		jianTZV.unbind("click").bind("click",function(){
			_self.jianTZV();
		});
	}
	/**生成档号 按钮(立卷档案卷内文件 档号生成)**/
	var shengchengDHV = _self._viewer.getBtn("shengchengDHV");
	if(shengchengDHV.length > 0){
		shengchengDHV.unbind("click").bind("click",function(){
			_self.shengchengDHV();
		});
	}
	/**清除档号 按钮(立卷档案卷内文件 档号清除)**/
	var clearDHV = _self._viewer.getBtn("clearDHV");
	if(clearDHV.length > 0){
		clearDHV.unbind("click").bind("click",function(){
			_self.clearDHV();
		});
	}
	/**批量修改 按钮(一文一卷档案或者卷信息操作按钮) **/
	var batchUpdate = _self._viewer.getBtn("batchUpdate");
	if(batchUpdate.length > 0){
		batchUpdate.unbind("click").bind("click",function(){
			_self.batchUpdateData();
		});
	}
	/**档案移交 按钮(一文一卷或者立卷操作按钮)**/
	var DAYiJiao = _self._viewer.getBtn("DAYiJiao");
	if(DAYiJiao.length > 0 ){
		DAYiJiao.unbind("click").bind("click",function(){
			_self.DAYiJiao();
		});
	}
	/**移交至档案库**/
	//获取当前用户角色
	var userRole = System.getVar("@ROLE_CODES@");
	var yjDangAnKu = _self._viewer.getBtn("yjDangAnKu");
	//c180819418981e510118a62308ac00ee档案管理员角色code
	if(userRole.indexOf("c180819418981e510118a62308ac00ee") < 0){
		yjDangAnKu.remove();
	} else{
		if(yjDangAnKu.length > 0 ){
			yjDangAnKu.unbind("click").bind("click",function(){
				_self.yjDangAnKu();
			});
		}
	}
	/***全选按钮***/
	var selectAll = _self._viewer.getBtn("selectAll");
	if(selectAll.length > 0 ){
		selectAll.unbind("click").bind("click",function(){
			if ((selectAll.attr("isClick") || "") == "") {
				selectAll.attr("isClick",true);
			} else if (selectAll.attr("isClick") == "true") {
				selectAll.attr("isClick",false);
			} else if (selectAll.attr("isClick") == "false") {
				selectAll.attr("isClick",true);
			}
			_self.selectAll(selectAll.attr("isClick"));
		});
	}

};
/*****************按钮实现***********************************************************/
/* 打印模版 post请求 打印支持IE浏览器
 * @param 参数
 */
function openDaPostWindow(params) {
	var tempForm = document.createElement("form");
	tempForm.id = "tempForm1";
	tempForm.method = "post";
	tempForm.target = "newPage";
	tempForm.action = "/da/da/doPrintDA.jsp"; // 在此处设置你要跳转的url
	
	jQuery.each(params, function(index, item){
		var hiddInputItem = document.createElement("input");
		hiddInputItem.type = "hidden";
		hiddInputItem.name = index;
		hiddInputItem.value = params[index];
		tempForm.appendChild(hiddInputItem);
	});
	
	tempForm.attachEvent("onsubmit", function() {});
	document.body.appendChild(tempForm);
	tempForm.fireEvent("onsubmit");
	// 将form的target设置成和windows.open()的name参数一样的值，通过浏览器自动识别实现了将内容post到新窗口中
	tempForm.submit();
	document.body.removeChild(tempForm);
};
//打印编目
rh.da.rhDABtns.prototype.printDa = function(){
	var _self = this;
	_self.printDialog();
};
/**扩展类：DaServ.java
 * 件号调整 按钮
 */
rh.da.rhDABtns.prototype.jianTZ = function(){
	 var _self = this;
	 var data = _self._viewer.grid.getModifyTrDatas() || ""; 
	 if(data.length == 0 || data == null){
		 _self._viewer.listBarTipError("请修改件号");
		 return false;
	 }
	 var datas = {};
	 var datavalues = new Array();
	 for(var i = 0 ;  i < data.length ;i++ ){
		 datavalues.push(data[i]._PK_ + "--" + data[i].DA_NUM);
	 }
	 datas["dataValue"] = datavalues.join(",");
	 var resultObj = FireFly.doAct(_self._viewer.servId,"updateJianHao",datas);
	 if(resultObj["_MSG_"].indexOf("OK")>=0){
		 _self._viewer.refresh();
		 _self._viewer.listBarTip("修改成功");
	 }
};

/**扩展类：DaServ.java
 * 档案装盒 按钮
 */
rh.da.rhDABtns.prototype.danganZH = function(){
	var _self = this;
	var data = _self._viewer.grid.getSelectPKCodes();
	if(data.length == 0){
		_self._viewer.listBarTipError("请勾选数据");
		return false;
	}
	var datas = new Array();
	for(var i = 0 ; i < data.length ; i++){
		datas.push(data[i] + "--" + _self._viewer.grid.getRowItemValue(data[i],"PAGE_COUNT"));
	}
	var dataValue = {};
	dataValue["DATA_VALUE"] = datas.join(",");
	var pointData = FireFly.doAct(_self._viewer.servId,"selectMaxBoxNum");
			
	var sResult = prompt("当前最大盒号："+pointData["MAX_BOX_NUM"],pointData["MAX_BOX_NUM"]);
	if(isNaN(sResult)){
		_self._viewer.listBarTipError("盒号请输入数字");
		return false;
	} else if (sResult == null || sResult == ""){
		return false;
	}
	else{
		dataValue["INPUT_BOX_NUM"] = sResult;
		var rusultData = FireFly.doAct(_self._viewer.servId , "danganZH" , dataValue);
		if(rusultData["_MSG_"].indexOf("OK")>=0){
			_self._viewer.refresh();
			_self._viewer.listBarTip("装盒成功");
		}
	}
};

/**扩展类：DaServ.java
 * 生成档号 按钮
 * 支持 “全选”  按钮
 */
rh.da.rhDABtns.prototype.shengchengDH = function(){
	var _self = this;
	var datas = {};
	datas["dataValue"] = _self.selectedData("请勾选数据");
	if(datas["dataValue"] == "false"){
		return false;
	}
	var rusultData = FireFly.doAct(_self._viewer.servId,"shengchengDH",datas);
	if(rusultData["_MSG_"].indexOf("OK")>=0){
		//清空全选标记
		_self.clearSelectFlag();
		_self._viewer.refresh();
		_self._viewer.listBarTip("生成成功");
	}
};

/**扩展类：DaServ.java
 * 清除档号 按钮
 *支持 “全选”  按钮
 */
rh.da.rhDABtns.prototype.clearDH = function(){
	var _self = this;
	var datas = {};
	datas["dataValue"] = _self.selectedData("请勾选数据");
	if(datas["dataValue"] == "false"){
		return false;
	}
	var resultObj = FireFly.doAct(_self._viewer.servId,"clearDH",datas);
	if(resultObj["_MSG_"].indexOf("OK")>=0){
		_self.clearSelectFlag();
		_self._viewer.refresh();
		_self._viewer.listBarTip("清除成功");
	}
};

/** 暂不实现
*合卷  按钮
*/
rh.da.rhDABtns.prototype.mergeVolume = function(){
	var _self = this;
	var data = _self._viewer.grid.getSelectPKCodes();
	if(data.length == 0){
		_self._viewer.listBarTipError("请勾选数据");
		return false;
	}
	if(data.length == 1){
		_self._viewer.listBarTipError("至少勾选两卷才能合卷");
		return false;
	}
	var dataArr = new Array();
	for(var j = 0 ; j < data.length ; j++){
		dataArr.push(data[j] + "--" + _self._viewer.grid.getRowItemValue(data[j],"DA_NUM"));
	}
	var resultData = {};
	resultData["RESULT_DATA"] = dataArr.join(",");
	
	var extWhereStr = " AND V_ID in (^";
	for(var i = 0 ; i < data.length; i++){
		extWhereStr = extWhereStr + data[i] + "^,^";
	}
	extWhereStr = extWhereStr.substring(0,extWhereStr.length-2);
	extWhereStr = extWhereStr +")";
	
	var inputName = "volumeCode"; //_self._viewer.opts.sId
	var configStr =  "DA_VOLUME,{'TARGET':'V_ID','SOURCE':'V_ID~TITLE','PKHIDE':true,'EXTWHERE':'"+extWhereStr+"','TYPE':'single'}";
	var options = {
	"config" :configStr,
	"title":"请选择合卷后的卷名",
	"replaceCallBack":function(volObj){
			_self.submitData(resultData,volObj);
		}
	};
	var queryView = new rh.vi.rhSelectListView(options);
	queryView.show(event);
	
};
/**暂不实现
 * “合卷”按钮，选择合卷后的卷名称后的回调方法
 */
rh.da.rhDABtns.prototype.submitData = function(resultData,volObj){
	//合并后的卷名所对应的ID
	resultData["VOLUME_ID"] = volObj.V_ID;
	
}

/**暂不实现
*拆卷  按钮
*/
rh.da.rhDABtns.prototype.tearVolume = function(){
	alert("未完成");
};

/**扩展类：DaServ.java
* 需要修改DA_DANGAN表中的V_ID,P_ID,V_NUM,DA_CODE(DA_CODE(DA_VOLUME)+MARK+DA_NUM)
*组已有卷  按钮
*支持 “全选”  按钮
*/
rh.da.rhDABtns.prototype.gdToVolume = function(){
	var _self = this;
	var dataObj = {};
	dataObj["DANG_AN_PK"] = _self.selectedData("请选择数据");
	if(dataObj["DANG_AN_PK"] == "false"){
		return false;
	}
	var fileType=1;
	if(_self._viewer.servId.indexOf("_PLAT")>=0){
		fileType =2;
	}
	var lastIndex = _self._viewer.servId.lastIndexOf("_");
	var servStr = _self._viewer.servId.substring(0,lastIndex);
	var extWhereStr =" and S_ODEPT=^"+System.getVar("@ODEPT_CODE@")+"^ and DA_FILE_TYPE=" + fileType + " and SERV_ID =^DA_VOLUME_"+servStr+"^ ";
	//查询选择，选择已有的卷
	var configStr = "DA_VOLUME,{'TARGET':'V_ID~P_ID~DA_CODE','SOURCE':'V_ID~TITLE~DA_CODE~P_ID','PKHIDE':true,'EXTWHERE':'"
			+ extWhereStr + "','TYPE':'single','HIDE':'P_ID'}";
	var options = {
	"config" :configStr,
	"title":"请选择所属卷",
	"replaceCallBack":function(volObj){
			_self.selectVolume(volObj,dataObj);
		}
	};
	var queryView = new rh.vi.rhSelectListView(options);
	queryView.show(event);
};
rh.da.rhDABtns.prototype.selectVolume = function(volObj,dataObj){
	var _self  = this;
	dataObj["V_ID"] = volObj.V_ID;
	dataObj["P_ID"] = volObj.P_ID;
	dataObj["V_DA_CODE"] = volObj.DA_CODE;
	var resultData = FireFly.doAct(_self._viewer.servId,"gdToVolume",dataObj);
	if(resultData["_MSG_"].indexOf("OK") >=0){
		_self.clearSelectFlag();
		_self._viewer.refresh();
		_self._viewer.listBarTip("组卷成功");
	}
};
/**
*组新卷 按钮
* 支持 “全选” 按钮选取所有数据
*/
rh.da.rhDABtns.prototype.gdToNewVolume = function(){
	var _self = this;
	//保存勾选的数据的ID,所对应的服务
	var dataObj = {};
	dataObj["DATA_ID_DA_NUM"] = _self.selectedData("请勾选数据");
	if(dataObj["DATA_ID_DA_NUM"] == "false"){
		return false;
	}
	dataObj["DATA_SERV_ID"] = _self._viewer.servId;
	//要查询分类为“立卷”的目录(查询DA_CATEGORY表中ARCH_STRATEGY为2的CAT_ID)
	var serName = {};
	serName["THIS_SERV_ID"] = _self._viewer.servId;
	serName["USER_ODEPT"] = System.getVar("@ODEPT_CODE@");
	//只显示当前服务对应的卷服务目录
	var whereObj = FireFly.doAct("DA_DIR","findServDir",serName);
	var whereStr = whereObj["WHERE_STR"].split(",");
	//树形选择  ：
	var configStr = 'DA_DIR_MANAGE,{"EXTWHERE":" and DIR_ID IN ('+whereObj.WHERE_STR+')"}' ;
	var options={
			"config":configStr,
			"replaceCallBack":function(idArray,nameArray){
			_self.toVolumn(idArray,nameArray , dataObj);
		}
	};
	var dictView = new rh.vi.rhDictTreeView(options);
	dictView.show(event);
	//全选状态置为false,清空数组
	_self.clearSelectFlag();
	

};
rh.da.rhDABtns.prototype.toVolumn = function(idArray,nameArray,dataObj){
	var _self = this;
	dataObj["TERR_DIR_ID"] = idArray.join(",");
	dataObj["callBackHandler"] = _self._viewer;
	var param = dataObj;
	//打开标签
	Tab.open({
		"url" : "DA_VOLUME_"+_self._viewer.servId+".card.do",
		"tTitle" : "添加新卷",
		"params" : param,
		"menuFlag" : 4
	});
};
/**扩展类：DAInVolume.java
 * 顺序号调整
 */
rh.da.rhDABtns.prototype.jianTZV = function(){
	var _self = this;
	var parhandlerObj = _self._viewer.getParHandler();
	var pDaCode = parhandlerObj.getItem("DA_CODE").getValue();
	var datas = _self._viewer.grid.getSelectPKCodes();
	if(datas.length == 0){
		_self._viewer.listBarTipError("请修改件号");
		return false;
	}
	var data = new Array();
	for(var i = 0 ; i < datas.length; i++){
		data.push(datas[i] + "--" + _self._viewer.grid.getRowItemValue(datas[i],"DA_NUM"));
	}
	var dataObj = {};
	dataObj["PK_DA_NUM"] = data.join(",");
	dataObj["PDA_CODE"] = parhandlerObj.getItem("DA_CODE").getValue();
	dataObj["P_ID"] = parhandlerObj.getItem("P_ID").getValue();
	var resultObj = FireFly.doAct(_self._viewer.servId,"jianTZV",dataObj);
	if(resultObj["_MSG_"].indexOf("OK") >=0){
		_self._viewer.refresh();
		_self._viewer.listBarTip("修改成功");
	}
};
/**扩展类：DAInVolume.java
 * 卷内文件生成档号
 */
rh.da.rhDABtns.prototype.shengchengDHV = function(){
	var _self = this;
	var datas = _self._viewer.grid.getSelectPKCodes();
	if(datas.length == 0){
		_self._viewer.listBarTipError("请勾选数据");
		return false;
	}
	var data = new Array();
	for(var i = 0 ;i < datas.length ;i++){
		data.push(datas[i] + "--" + _self._viewer.grid.getRowItemValue(datas[i],"DA_NUM"));
	}
	var dataObj = {};
	dataObj["PK_DA_NUM"] = data.join(",");
	dataObj["PDA_CODE"] = _self._viewer.getParHandler().getItem("DA_CODE").getValue();
	var resultObj = FireFly.doAct(_self._viewer.servId,"shengchengDHV",dataObj);
	if(resultObj["_MSG_"].indexOf("OK") >= 0){
		_self._viewer.refresh();
		_self._viewer.listBarTip("成功生成档号");
	}
};
/**扩展类：DAInVolume.java
 * 清除卷内文件档号
 */
rh.da.rhDABtns.prototype.clearDHV = function(){
	var _self = this;
	var dataObj = _self._viewer.grid.getSelectPKCodes();
	if(dataObj.length == 0){
		_self._viewer.listBarTipError("请勾选数据");
		return false;
	}
	var data = new Array();
	for(var i = 0 ; i < dataObj.length; i++){
		data.push(dataObj[i]);
	}
	var datas = {};
	datas["RESULT_DATA"] = data.join(",");
	var resultData = FireFly.doAct(_self._viewer.servId,"clearDHV",datas);
	if(resultData["_MSG_"].indexOf("OK") >= 0 ){
		_self._viewer.refresh();
		_self._viewer.listBarTip("清除成功");
	}
};
/**扩展类：DaServ.java
 * 批量修改 功能 
 * 支持 “全选” 按钮选取所有数据
 */
rh.da.rhDABtns.prototype.batchUpdateData = function(){
	var _self = this;
	var dataStr = _self.selectedData("请勾选数据");
	if(dataStr == "false"){
		return false;
	}
	_self.openDialog(event , dataStr);
	
};
/**
 * "批量修改"按钮弹出的dialog
 * @param event
 * @returns
 */
rh.da.rhDABtns.prototype.openDialog = function (event,dataStr){
	var _self = this;
	this.winDialog = jQuery("<div class='batchUpdate_dialog' id='batchUpdate_dialog' title='批 量 修 改'></div>");
	this.winDialog.appendTo(jQuery("body"));
	jQuery("#batchUpdate_dialog").dialog({
		autoOpen: false,
		height: 200,
		width: 400,
		modal: true,
		resizable:false,
		position:[500,100],
		buttons:{
			'确 定':function(){ 
			_self.updateData(dataStr); 
			}, 
			'取 消':function(){ 
				jQuery("#batchUpdate_dialog").remove();
			} 
		},
		open: function() { 
			_self.dialogData();
		},
		close: function() {
			jQuery("#batchUpdate_dialog").remove();
		}
	});
	//手动打开dialog
	var dialogObj = jQuery("#batchUpdate_dialog");
	dialogObj.dialog("open");
	jQuery(".ui-dialog-titlebar").last().css("display","block");//设置标题显示
};
/**"批量修改"按钮弹出的dialog中展示的数据
 *向dialog中填充数据
 */
rh.da.rhDABtns.prototype.dialogData = function(){
	var _self = this;
	var table = jQuery("<table class='rhGrid' id='batchUpdate_table' align='center'></table>");
	var thead = jQuery("<thead style='background-color:#F6F6F6;font-family:黑体,宋体' id='batchUpdate_thead'></thead>");
	var tbody = jQuery("<tbody class='rhGrid-tbody' id='batchUpdate_tbody'></tbody>");
	var field = jQuery("<tr>" +
							"<td style='width:150px;text-align:center;height:40px;font-size:14px;padding-left:30px;padding-top:20px;'>著录项 :</td>" +
							"<td style='padding-top:20px;'>" +
								"<div style='width:140px;height:24px;border:solid 1px #aaaaaa;'>"+
									"<select id ='selectField' style='width:138px;margin:1px;'>" +
										"<option value=''></option>" +
									"</select>" +
								 "</div>"+
							"</td>" +
					   "</tr>");
	var modifyBefore = jQuery("<tr>" +
								   "<td style='width:150px;text-align:center;height:40px;font-size:14px;padding-left:30px;'>要替换的值 :</td>" +
								   "<td>" +
								   		"<input type='text' name='modifyBefore' id='modifyBefore' value=''  ></input>" +
								   "</td>" +
							"</tr>");
	var modifyAfter = jQuery("<tr>" +
								   "<td style='width:150px;text-align:center;height:40px;font-size:14px;padding-left:30px;'>替换为 :</td>" +
								   "<td>" +
								   		"<input type='text' name='modifyAfter' id='modifyAfter' value=''  ></input>" +
								   "</td>" +
							"</tr>");
	field.appendTo(thead);
	modifyBefore.appendTo(thead);
	modifyAfter.appendTo(thead);
	thead.appendTo(table);
	table.appendTo(jQuery("#batchUpdate_dialog"));
	var datas = {};
	datas["SERV_NAME"] = _self._viewer.servId;
	var itemDatas = FireFly.doAct(_self._viewer.servId,"getItemName",datas);
	for(var i = 0 ; i < itemDatas["_DATA_"].length ; i++){
		jQuery("<option value='"+itemDatas["_DATA_"][i]["ITEM_CODE"]+"'>"+itemDatas["_DATA_"][i]["ITEM_NAME"]+"</option>").appendTo(jQuery("#selectField"));
	}
};
/**
 * dialog的确定按钮回调方法
 */
rh.da.rhDABtns.prototype.updateData = function(dataStr){
	var _self = this;
	var datas = {};
	datas["MODIFY_FIELD"] = jQuery("#selectField").val();
	datas["MODIFY_BEFORE"] = jQuery("#modifyBefore").val();
	datas["MODIFY_AFTER"] = jQuery("#modifyAfter").val();
	datas["DATA_IDS"] = dataStr;
	var resultObj = FireFly.doAct(_self._viewer.servId,"batchUpdateData",datas);
	if(resultObj["_MSG_"].indexOf("OK") >= 0){
		jQuery("#batchUpdate_dialog").remove();
		//将全选标志改为flase。并将dataIdsArr清空
		_self.clearSelectFlag();
		_self._viewer.refresh();
		_self._viewer.listBarTip("修改成功");
		
	}
};
/**扩展类：DaServ.java
 * "档案移交"按钮。
 * 支持 “全选” 按钮选取所有数据
 */
rh.da.rhDABtns.prototype.DAYiJiao = function(){
	var _self = this;
	var data = {};
	data["DATA_INFO"] = _self.selectedData("请勾选数据");
	if(data["DATA_INFO"] == "false"){
		return false;
	}
	var yijiaoInvoice = {};
	yijiaoInvoice["YJ_TITLE"] = "移交档案  "+System.getVar("@DATE@");
	yijiaoInvoice["YJ_MAIL"] = System.getVar("@USER_EMAIL@");
	yijiaoInvoice["YJ_PHONE"] = System.getVar("@OFFICE_PHONE@");
	var yijiaodan = FireFly.cardAdd("DA_YIJIAO",yijiaoInvoice);
	data["YJ_ID"] = yijiaodan["YJ_ID"];
	//data["DATA_INFO"] = dataPKArr.join(",");
	data["SERV_ID"] = _self._viewer.servId;
	FireFly.doAct(_self._viewer.servId,"relationDA",data);
	//全选状态置为false,清空数组
	_self.clearSelectFlag();
	//刷新
	_self._viewer.refresh();
	var option = {"url":"DA_YIJIAO.card.do?pkCode="+yijiaodan["YJ_ID"], "tTitle":"档案移交","menuFlag":4};
	Tab.open(option);
};
/**扩展类：DaServ.java
 * 打印编目  按钮
 */
rh.da.rhDABtns.prototype.printDialog = function(){
	var _self = this;
	this.winDialog = jQuery("<div class='print_dialog' id='print_dialog' title='打 印 查 询'></div>");
	this.winDialog.appendTo(jQuery("body"));
	jQuery("#print_dialog").dialog({
		autoOpen: false,
		height: 300,
		width: 450,
		modal: true,
		resizable:false,
		position:[200,100],
		buttons:{
			'确 定':function(){ 
			_self.printData(); 
			}, 
			'取 消':function(){ 
				jQuery("#print_dialog").remove();
			} 
		},
		open: function() { 
			_self.dialogHtml();
		},
		close: function() {
			jQuery("#print_dialog").remove();
		}
	});
	//手动打开dialog
	var dialogObj = jQuery("#print_dialog");
	dialogObj.dialog("open");
	jQuery(".ui-dialog-titlebar").last().css("display","block");//设置标题显示
};
/**
 * 打印编目查询条件页面
 */
rh.da.rhDABtns.prototype.dialogHtml = function(){
	var _self = this;
	//去除当前服务的后缀
	var lastStr = _self._viewer.servId.lastIndexOf("_");
	var serv = _self._viewer.servId.substr(0,lastStr);
	var data = {};
	data[UIConst.WHERE] =" and SERV_ID='" + serv + "'";
	//取规则
	var categoryInfo = FireFly.doAct("DA_CATEGORY","finds",data);
	var printName = categoryInfo["_DATA_"][0]["CODE_RULE_NAME"];
	var printVal = categoryInfo["_DATA_"][0]["CODE_RULE_VAL"];
	var table = jQuery("<table class='rhGrid' id='category_table' align='center'></table>");
	var thead = jQuery("<thead style='background-color:#F6F6F6;font-family:黑体,宋体' id='category_thead'></thead>");
	var tTrCat = jQuery("<tr><td colspan='2'><input type='hidden' name='daCategoryName' id='daCategoryName' value='"+categoryInfo["_DATA_"][0]["CAT_NAME"]+"'></input></td></tr>");
	var tTr = jQuery("<tr></tr>");
	var checkBox = jQuery("<td style='height:40px;width:100px;' align='right' valign='center'>打印类型：</td>" +
						  "<td>" +
						  		"<input type='radio' checked ='true' name='printType' value='beikao' vertical-align='middle'>备考表</input>&nbsp;&nbsp;" +
						  		"<input type='radio' name='printType' value='list'>目录</input>&nbsp;&nbsp;" +
						  		"<input type='radio' name='printType' value='jianCover'>封皮(件/卷)</input>&nbsp;&nbsp;" +////为中华保险量身定做封皮一文一打
								//"<input type='radio' name='printType' value='frontCover'>封皮(盒/卷)</input>&nbsp;&nbsp;&nbsp;&nbsp;" +//一盒一打
								"<input type='radio' name='printType' value='jibei'>脊背(长)</input>&nbsp;&nbsp;" +
								"<input type='radio' name='printType' value='jibeishort'>脊背(短)</input>&nbsp;&nbsp;" +
						  "</td>");
	checkBox.appendTo(tTr);
	tTrCat.appendTo(thead);
	tTr.appendTo(thead);
	thead.appendTo(table);
	table.appendTo(jQuery("#print_dialog"));
	 printValArr = printVal.split(",");
	 printNameArr = printName.split(",");
	for(var i = 0 ; i < printValArr.length; i++){
		if("DA_TERM" == printValArr[i]){
			var datermDict = FireFly.getDict("DA_MANAGE_TIME");
			jQuery("<tr><td style='height:40px;' align='right'>"+printNameArr[i]+"：</td><td><div  style='width:140px;height:24px;border:solid 1px #aaaaaa;'><select  name='"+printValArr[i]+"' id='"+printValArr[i]+"' style='width:138px;height:22px;margin:1px;' ></select></div></td></tr></br>").appendTo(thead);
			for(var j = 0 ; j < datermDict[0].CHILD.length ; j++){
				jQuery("<option value='"+datermDict[0].CHILD[j].ITEM_NAME+"'>"+datermDict[0].CHILD[j].ITEM_NAME+"</option>").appendTo(jQuery("#"+printValArr[i]));
			}
		} else if ("GD_DEPT_NAME" == printValArr[i]){
			jQuery("<tr><td style='height:40px;' align='right'>"+printNameArr[i]+"：</td><td><input type='text' name='"+printValArr[i]+"' id='"+printValArr[i]+"' value=''></input></td></tr></br>").appendTo(thead);
			jQuery("#"+printValArr[i]).unbind("click").bind("click" , function(){
				var extWhereStr = " and S_ODEPT=^"+System.getVar("@ODEPT_CODE@")+"^ ";
				//查询选择
				var configStr =  "DA_YEAR_DEPT,{'TARGET':'DEPT_NAME~','SOURCE':'YD_YEAR~DEPT_NAME~YD_SORT','PKHIDE':true,'TYPE':'single','EXTWHERE':'"+extWhereStr+"','HIDE':'YD_ID'}";
				var options = {
				"config" :configStr,
				"title":"请选择归档部门",
				"replaceCallBack":function(idArray,nameArray){
						jQuery("#GD_DEPT_NAME").val(idArray.DEPT_NAME);
					}
				};
				var queryView = new rh.vi.rhSelectListView(options);
				queryView.show(event);	
			});
		} 
		else {
			jQuery("<tr><td style='height:40px;' align='right'>"+printNameArr[i]+"：</td><td><input type='text' name='"+printValArr[i]+"' id='"+printValArr[i]+"' value=''></input></td></tr></br>").appendTo(thead);
		}
	}
};
rh.da.rhDABtns.prototype.printData = function(){
	var _self = this;
	var param = {};
	param["CAT_NAME"] = jQuery("#daCategoryName").val() || "";
	/***为中华保险去掉盒号、卷号选项，其他公司会有***/
	param["PRINT_TYPE"] = jQuery("input[name='printType']:checked").val() || "";
	param["CUR_SERV_ID"] = _self._viewer.servId;
	var data = new Array();
	for(var i = 0 ; i < printValArr.length ; i++){
		var linshi = jQuery("#"+printValArr[i]).val() || "";
		if(linshi != ""){
			param[printValArr[i]] = linshi;
			data.push(printValArr[i]);
		}
	}
	param["ZI_DUAN"] = data.join(",");
	var navTreeObj = _self._viewer.getNavTreeObj("@com.rh.da.dir.ExtSubDirDict");
	param["PID"] = navTreeObj.getCurrentNode().ID;
	jQuery("#print_dialog").remove();
	openDaPostWindow(param);
};
/**
 * 移交至档案库 按钮
 * 支持 “全选” 按钮选取所有数据
 */
rh.da.rhDABtns.prototype.yjDangAnKu = function(){
	var _self = this;
	var data = {};
	data["DATAS_PK"] = _self.selectedData("请勾选要移交的数据");
	if(data["DATAS_PK"] == "false"){
		return false;
	}
	var resultBean = FireFly.doAct(_self._viewer.servId,"yjToDAKu",data);
	if(resultBean["_MSG_"].indexOf("OK") >=0){
		//全选状态置为false,清空数组
		_self.clearSelectFlag();
		//刷新
		_self._viewer.refresh();
		_self._viewer.listBarTip("移动成功");
	}
};
/**
 *  全选 按钮
 */
rh.da.rhDABtns.prototype.selectAll = function(isClick){
	var _self = this;
	_self.selectAllFlag = isClick;
	_self.bindCheckBox();
	_self._viewer.afterRender = function(){
		_self.bindCheckBox();
	}
};
//为checkbox绑定事件
rh.da.rhDABtns.prototype.bindCheckBox = function(){
	var _self = this;
	jQuery("input.rhGrid-thead-checkbox",_self._viewer.grid._table).hide();
	//循环tr,如果是全选 ：1.将每个checkbox选中  。2.为每个checkbox绑定点击事件
	if(_self.selectAllFlag == "true"){
		jQuery.each(_self._viewer.grid.getBodyTr(),function(i,n){
			//取出每个tr中的checkbox。
			var checkboxObj = jQuery(this).find('input[class="rowIndex"]').eq(0);
			//将不勾选的数据转换成字符串
			var pkStr = _self.dataIdsArr.join(",");
			//1.将每个checkbox选中
			if(pkStr.indexOf(n.id) < 0){
				checkboxObj.attr("checked",true);
			} else {
				checkboxObj.attr("checked",false);
			}
			//2.为每个checkbox绑定点击事件。
			checkboxObj.unbind("click").bind("click",
					{"checkboxObj":checkboxObj,"dataId":jQuery(this).attr("id")}, function(event){
				//如果选中checkbox，则点击后将数据ID从dataIdsArr中去除。
				if(event.data.checkboxObj.attr("checked")){
					//alert("选中");
					//设置选中状态
					checkboxObj.attr("checked",true);
					//将 dataIdsArr 转换成字符串，
					var dataIdsStr = _self.dataIdsArr.join(",");
					var datapk = "";
					//当前数据ID是否包含在字符串中
					if(dataIdsStr.indexOf(event.data.dataId) == 0){
						datapk = event.data.dataId + ",";
					} 
					if(dataIdsStr.indexOf(event.data.dataId) > 0){
						datapk = ","+event.data.dataId;
					}
					//将指定数据ID从字符串中删除
					var result = dataIdsStr.replace(datapk , "");
					//将最终的字符串拆分成数组赋值给dataIdsArr
					_self.dataIdsArr = result.split(",");
				//如果取消选中状态，则将数据ID添加到dataIdsArr中
				} else{
					checkboxObj.attr("checked",false);
					_self.dataIdsArr.push(event.data.dataId);
					//alert("取消选中");
				}
			});
		});
	} else {//否则去除单选框，解绑点击事件。
		jQuery("input.rhGrid-thead-checkbox",_self._viewer.grid._table).show();
		jQuery.each(_self._viewer.grid.getBodyTr() , function(){
			var checkboxObj = jQuery(this).find('input[class="rowIndex"]').eq(0);
			checkboxObj.attr("checked",false);
			checkboxObj.unbind("click");
			_self.dataIdsArr.length=0;
		});
	}
};
//获取查询条件
rh.da.rhDABtns.prototype.selectedData = function(tipInfo){
	var _self = this;
	var dataIDStr = "";
	//如果全选状态为true，则获取当权页面查询条件，并去除 dataIdsArr 中存储的ID
	if(_self.selectAllFlag == "true"){
		var navTreeObj = _self._viewer.getNavTreeObj("@com.rh.da.dir.ExtSubDirDict");
		//var treeWhere =  _self._viewer.whereData._treeWhere[0].DICT_VALUE || "";//树形展示条件
		var searchWhere =  _self._viewer.whereData._searchWhere || "";
		var where = navTreeObj.getCurrentNode().ID + "^"+ " and S_ODEPT='"+System.getVar("@ODEPT_CODE@")+"' " + searchWhere +" " ;
		if(_self.dataIdsArr.length > 0){
			if(_self._viewer.servId.indexOf("DA_VOLUME") == 0){
				where += " and V_ID not in ('" ;
			} else {
				where += " and DANGAN_ID not in ('";
			}
			var notInId = _self.dataIdsArr.join("','");
			where += notInId + "') ";
		}
		dataIDStr = "whereStr:" + where;
	} else {//如果不是全选状态，则判断是否勾选了数据，如果没有勾选数据则提示
		var datas = _self._viewer.grid.getSelectPKCodes();
		if(datas.length == 0){
			_self._viewer.listBarTipError(tipInfo);
			return "false";
		}
		var data = new Array();
		for(var i = 0 ; i < datas.length; i++){
			data.push(datas[i]);
		}
		dataIDStr = "idStr:"+data.join(",");
	}
	return dataIDStr;
};
/**清空全选状态，标志位设为false**/
rh.da.rhDABtns.prototype.clearSelectFlag = function(){
	//全选状态置为false,清空数组
	var _self = this;
	_self.dataIdsArr.length=0;
	_self.selectAllFlag = "false";
}
