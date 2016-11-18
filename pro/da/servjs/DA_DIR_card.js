var _viewer = this;
//_viewer.getItem("P_ID")._cancel.click();//新疆分公司，在AA分类下面增加 BBB，切换到四川公司，如果不选择分类点击 添加 按钮，会有“父目录”显示“AA”的小bug
/**当档案分类变化时候，清空 档号规则、子目录名称等值。需要重新选择值**/
_viewer.form.getItem("CAT_ID").obj.change(function(){
	_viewer.getItem("CODE_RULE_NAME").setValue("");
	_viewer.getItem("CODE_RULE_VAL").setValue("");
	_viewer.getItem("SUB_DIR_NAME").setValue("");
	_viewer.getItem("SUB_DIR_STRUC").setValue("");
});
var datas = {};
/**查询当前机构的全宗,为“全宗名”设置默认全宗号 **/
datas["_searchWhere"]=" and ODEPT_CODE ='" + getOdeptCode() + "'";
var fondsObj = FireFly.getListData("DA_FONDS_NUMBER",datas);
_viewer.getItem("FONDS_ID").setValue(fondsObj["_DATA_"][0]["FONDS_ID"]);
_viewer.getItem("FONDS_NAME").setValue(fondsObj["_DATA_"][0]["FONDS_NAME"]);
_viewer.getItem("S_ODEPT").setValue(getOdeptCode());

/**子目录的“档案分类”继承父服务的档案分类**/
var pID = _viewer.getItem("P_ID").getValue() || "";
if(pID != ""){
	var daDirObj = FireFly.byId(_viewer.servId,pID);
	_viewer.getItem("CAT_ID").setValue(daDirObj.CAT_ID);
	_viewer.getItem("CAT_ID").disabled();
}

/**
var fondsName = _viewer.getItem("FONDS_ID").getText();
_viewer.beforeSave = function(){
	_viewer.setExtendSubmitData({"FONDS_NAME":fondsName,"S_ODEPT":getOdeptCode()});
};
*/
/**
 * 得到当前加载的树所属机构的机构code
 */
function getOdeptCode(){
	
	var whereStr = _viewer.getParHandler().whereData._extWhere || "";
	var odept="";
	if(whereStr==""){
		odept = System.getVar("@ODEPT_CODE@");
	}else{
		var odepts = whereStr.split("'");
		 odept = odepts[1];
	}
	return odept;
}
/**
 * 为档号规则绑定事件
 */
_viewer.form.getItem("CODE_RULE_NAME").obj.focus(function(){
	_openDialog(event,"da_category-dial" , "档号规则定义" , "code_rule");
	_addRuleName("da_category-dial","code_rule" , "CODE_RULE_NAME" , "CODE_RULE_VAL");
});
/**
 * 为子目录名称绑定事件
 */
_viewer.form.getItem("SUB_DIR_NAME").obj.focus(function(){
	_openDialog(event,"da_sub_dir_name" , "目录展示规则定义" , "da_dir");
	_addDirName("da_sub_dir_name","da_dir" , "SUB_DIR_NAME" , "SUB_DIR_STRUC");
});
/**
 * 弹出dialog
 * @param event 
 * @param IDdialog  dialog的ID，
 * @param title  标题
 * @returns
 */ 
function _openDialog(event , IDdialog , title , str){
	var _self = _viewer;
	this.dialogId = IDdialog;
	this.winDialog = jQuery("<div class='categoryDialog' id='"+this.dialogId+"' title='"+ title +"'></div>");
	this.winDialog.appendTo(jQuery("body"));
	var hei = 200;
	var wid = 600;
	//生成jqueryUi的dialog
	jQuery("#" + this.dialogId).dialog({
		autoOpen: false,
		height: hei,
		width: wid,
		modal: true,
		resizable:false,
		position:'center',
		buttons:{
			'确 定':function(){
				submitData(IDdialog , str);
			},
			'清 除':function(){
				calcelData(IDdialog,str);
			}
		},
		open: function() { 
		},
		close: function() {
			jQuery("#" + dialogId).remove();
			//_viewer.refresh();
		}
	});
	//手动打开dialog
	var dialogObj = jQuery("#" + this.dialogId);
	dialogObj.dialog("open");
	dialogObj.focus();
	jQuery(".ui-dialog-titlebar").last().css("display","block");//设置标题显示
}

/**
 * 在弹出的dialog中增加 规则定义功能
 * @param IDdialog  dialog的ID
 * @param str     为ID增加字符
 * @param subName  规则中文显示
 * @param subValue  规则英文显示
 * @returns {Boolean}
 */
function _addRuleName(IDdialog , str , subName , subValue) {
	//获取服务ID是否输入为空
	var catID = _viewer.getItem("CAT_ID").getValue() || "";
	if(catID == ""){
		jQuery("#"+IDdialog).remove();
		Tip.showError("选择\"档案分类\"");
		return false;
	}
	var catObj = FireFly.byId("DA_CATEGORY" , catID);
	var servName = catObj["SERV_ID"];
	//查询服务定义信息
	var datas={};
	datas["SERV_NAME"] = servName ;
	//datas[UIConst.WHERE] = " and SERV_ID='"+servName+"' and ITEM_HIDDEN=2";
	//var ServInfo = FireFly.doAct("SY_SERV_ITEM","finds",datas)
	var ServInfo = FireFly.doAct("DA_DIR","getServItem",datas);	 
	
	//如果返回值出错，则将焦点放在服务ID项，从新输入服务ID
	if(ServInfo["_OKCOUNT_"] ==0){
		 jQuery("#"+IDdialog).remove();
		 Tip.showError("档案所选分类无字段");
		 return false;
	}
	//渲染dilog内容
	var table = jQuery("<table class='rhGrid' id='"+str+"category_table' align='center'></table>");
	var thead = jQuery("<thead style='background-color:#F6F6F6;font-family:黑体,宋体' id='"+str+"category_thead'></thead>");
	var tbody = jQuery("<tbody class='rhGrid-tbody' id='"+str+"category_tbody'></tbody>");
	var tTr = jQuery("<tr></tr>");
	//累加字段td
	var headTdFile = jQuery("<td style='width:80px;text-align:center;height:40px;font-size:14px;padding-top: 20px;'>累加字段</td>");
	//select的TD
	var selectTd = jQuery("<td style='width:200px;height:40px;padding-top: 20px;'></td>");
	var div = jQuery("<div  style='width:200px;height:24px;border:solid 1px #aaaaaa;'></div>");
	
	//select
	var select = jQuery("<select id ='"+str+"selectValue' style='width:198px;height:22px;margin:1px;'></select>");
	var option = jQuery("<option value=''></option>");
	option.appendTo(select);
	//向option中添加 字段名称、字段code
	for(var i=0;i < ServInfo["_DATA_"].length; i++ ){
		  jQuery("<option value='"+ServInfo["_DATA_"][i]["ITEM_CODE"]+"'>"+ServInfo["_DATA_"][i].ITEM_NAME+"</option>").appendTo(select);
	}
	select.appendTo(div);
	div.appendTo(selectTd);
	//分隔符的td
	var fengeTd = jQuery("<td style='width:100px;height:40px;text-align:center;font-size:14px;padding-top: 20px;'>累加分隔符</td>");
	//input的td
	var inputTd = jQuery("<td style='width:100px;height:40px;padding-top: 20px;'><input type='text' id='"+str+"signVal' value='' style='width:100px;'></input></td>");
	//添加按钮td
	var subTd = jQuery("<td style='width:120px;height:40px;text-align:center;padding-top: 20px;'><input type='button' value='添加' id='"+str+"addRule' style='width:50px;font-size:14px;height: 24px;'></input></td>");		
	//第二个tr，规则显示
	var guizeTr = jQuery("<tr></tr>");
	var guizeTd = jQuery("<td style='text-align:center; font-size:14px;height:40px;'>档号规则</td>");
	//档号规则显示td
	var guizeInputTd = jQuery("<td colspan='4' style='height:40px;'><input type='text' value='' style='width:400px;' id='"+str+"ruleText'></input>" +
											  					   "<input type='hidden' value='' id='"+str+"ruleCode'</td>");
	/**第一行td----开始**/
	headTdFile.appendTo(tTr);
	selectTd.appendTo(tTr);
	fengeTd.appendTo(tTr);
	inputTd.appendTo(tTr);
	subTd.appendTo(tTr);
	tTr.appendTo(thead);
	/**第一行td----结束**/
	/**第二行td插入tr----开始**/
	guizeTd.appendTo(guizeTr);
	guizeInputTd.appendTo(guizeTr);
	guizeTr.appendTo(thead);
	/**第二行td插入tr----结束**/

	thead.appendTo(table);	
	table.appendTo(jQuery("#"+IDdialog));
	var name = _viewer.getItem(subName).getValue() || "";
	var code = _viewer.getItem(subValue).getValue() || "";
	if(name != ""){
		jQuery("#"+str+"ruleText").val(name);
		jQuery("#"+str+"ruleCode").val(code);
	}
	/****弹出的dialog，“添加” 按钮判断***/
	jQuery("#"+str+"addRule").unbind("click").bind("click",function(){
		var selectValue = jQuery("#"+str+"selectValue").val() || "";
		var selectText = jQuery("#"+str+"selectValue").find("option:selected").text() || "";
		var signVal = jQuery("#"+str+"signVal").val() || "";
		var ruleText = jQuery("#"+str+"ruleText").val() || "";
		var ruleCode = jQuery("#"+str+"ruleCode").val() || "";
		if(selectValue == ""){
			alert("请选择字段");
			return false;
		}
		if(ruleText == ""){
			jQuery("#"+str+"ruleText").val("["+selectText+"]");
			jQuery("#"+str+"ruleCode").val("#"+selectValue+"#");
		}
		else{
			if(signVal == ""){
				alert("输入分隔符");
				return false;
			}
			jQuery("#"+str+"ruleText").val(ruleText+signVal+"["+selectText+"]");
			jQuery("#"+str+"ruleCode").val(ruleCode+signVal+"#"+selectValue+"#");
		}
	 });
}



function _addDirName(IDdialog , str , subName , subValue) {
	//获取服务ID是否输入为空
	var catID = _viewer.getItem("CAT_ID").getValue() || "";
	if(catID == ""){
		jQuery("#"+IDdialog).remove();
		Tip.showError("选择\"档案分类\"");
		return false;
	}
	var catObj = FireFly.byId("DA_CATEGORY" , catID);
	var servName = catObj["SERV_ID"];
	//查询服务定义信息
	var datas={};
	datas["SERV_NAME"] = servName ;
	//datas[UIConst.WHERE] = " and SERV_ID='"+servName+"' and ITEM_HIDDEN=2";
	//var ServInfo = FireFly.doAct("SY_SERV_ITEM","finds",datas)
	var ServInfo = FireFly.doAct("DA_DIR","getServItem",datas);	 
	
	//如果返回值出错，则将焦点放在服务ID项，从新输入服务ID
	if(ServInfo["_OKCOUNT_"] ==0){
		 jQuery("#"+IDdialog).remove();
		 Tip.showError("档案所选分类无字段");
		 return false;
	}
	//渲染dilog内容
	var table = jQuery("<table class='rhGrid' id='"+str+"category_table' align='center'></table>");
	var thead = jQuery("<thead style='background-color:#F6F6F6;font-family:黑体,宋体' id='"+str+"category_thead'></thead>");
	var tbody = jQuery("<tbody class='rhGrid-tbody' id='"+str+"category_tbody'></tbody>");
	var tTr = jQuery("<tr></tr>");
	//累加字段td
	var headTdFile = jQuery("<td style='width:170px;text-align:right;height:40px;font-size:14px;padding-right: 18px;padding-top: 20px;'>目录展示字段</td>");
	//select的TD
	var selectTd = jQuery("<td style='padding-top: 20px;'></td>");
	var div = jQuery("<div  style='width:253px;height:24px;border:solid 1px #aaaaaa;'></div>");
	//select
	var select = jQuery("<select id ='"+str+"selectValue' style='width:251px;height:22px;margin:1px;'></select>");
	var option = jQuery("<option value=''></option>");
	option.appendTo(select);
	//向option中添加 字段名称、字段code
//	jQuery.each(ServInfo["_DATA_"], function(i, item){
//		//alert(item.ITEM_CODE);
//	});
	for(var i=0;i < ServInfo["_DATA_"].length; i++ ){
		  jQuery("<option value='"+ServInfo["_DATA_"][i]["ITEM_CODE"]+"'>"+ServInfo["_DATA_"][i].ITEM_NAME+"</option>").appendTo(select);
	}
	select.appendTo(div);
	div.appendTo(selectTd);
	//input的td
	var inputTd = jQuery("<td style='width:30px;height:40px;padding-top: 20px;'><input type='hidden' id='"+str+"signVal' value='-'></input></td>");
	//添加按钮td
	var subTd = jQuery("<td style='width:145px;height:40px;text-align:left;padding-top: 20px;'><input type='button' value='添加' id='"+str+"addRule' style='width:50px;font-size:14px;height: 24px;'></input></td>");		
	//第二个tr，规则显示
	var guizeTr = jQuery("<tr></tr>");
	var guizeTd = jQuery("<td style='text-align:right; font-size:14px;height:40px;padding-right: 30px;width:50px;'>目录展示项</td>");
	//档号规则显示td
	var guizeInputTd = jQuery("<td colspan='3' style='height:40px;'><input type='text' value='' style='width:330px;' id='"+str+"ruleText'></input>" +
											  					   "<input type='hidden' value='' id='"+str+"ruleCode'</td>");
	/**第一行td----开始**/
	headTdFile.appendTo(tTr);
	selectTd.appendTo(tTr);
	inputTd.appendTo(tTr);
	subTd.appendTo(tTr);
	tTr.appendTo(thead);
	/**第一行td----结束**/
	/**第二行td插入tr----开始**/
	guizeTd.appendTo(guizeTr);
	guizeInputTd.appendTo(guizeTr);
	guizeTr.appendTo(thead);
	/**第二行td插入tr----结束**/

	thead.appendTo(table);	
	table.appendTo(jQuery("#"+IDdialog));
	var name = _viewer.getItem(subName).getValue() || "";
	var code = _viewer.getItem(subValue).getValue() || "";
	if(name != ""){
		jQuery("#"+str+"ruleText").val(name);
		jQuery("#"+str+"ruleCode").val(code);
	}
	
	/****弹出的dialog，“添加” 按钮判断***/
	jQuery("#"+str+"addRule").unbind("click").bind("click",function(){
		var selectValue = jQuery("#"+str+"selectValue").val() || "";
		var selectText = jQuery("#"+str+"selectValue").find("option:selected").text() || "";
		var signVal = jQuery("#"+str+"signVal").val() || "";
		var ruleText = jQuery("#"+str+"ruleText").val() || "";
		var ruleCode = jQuery("#"+str+"ruleCode").val() || "";
		if(selectValue == ""){
			alert("请选择字段");
			return false;
		}
		if(ruleText == ""){
			jQuery("#"+str+"ruleText").val("["+selectText+"]");
			jQuery("#"+str+"ruleCode").val("#"+selectValue+"#");
		}
		else{
			if(signVal == ""){
				alert("输入分隔符");
				return false;
			}
			jQuery("#"+str+"ruleText").val(ruleText+signVal+"["+selectText+"]");
			jQuery("#"+str+"ruleCode").val(ruleCode+signVal+"#"+selectValue+"#");
		}
	 });
}
function calcelData(IDdialog , str){
	var ruleName = jQuery("#"+str+"ruleText").val("");
	var ruleCode = jQuery("#"+str+"ruleCode").val("");
}
//确定按钮
function submitData(IDdialog , str){
	var ruleName = jQuery("#"+str+"ruleText").val() || "";
	var ruleCode = jQuery("#"+str+"ruleCode").val() || "";
	var subName = "";
	var subValue = "";
	if(str == "code_rule"){
		subName = "CODE_RULE_NAME";
		subValue = "CODE_RULE_VAL";
	}
	if(str == "da_dir"){
		subName = "SUB_DIR_NAME";
		subValue = "SUB_DIR_STRUC";
	}
	_viewer.getItem(subName).setValue(ruleName);
	_viewer.getItem(subValue).setValue(ruleCode);
	jQuery("#"+IDdialog).remove();
}