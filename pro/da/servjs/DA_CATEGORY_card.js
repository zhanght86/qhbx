var _viewer = this;
//_viewer.getItem("SERV_ID").setValue("DA_CATEGORY");

_viewer.form.getItem("CODE_RULE_NAME").obj.unbind("click").bind("click",function(){
	_openDialog(event);
});

function _openDialog(event){
	var _self = _viewer;
	this.dialogId = "da_category-dial";
	this.winDialog = jQuery("<div class='categoryDialog' id='"+this.dialogId+"' title='打印查询规则'></div>");
	this.winDialog.appendTo(jQuery("body"));
	var hei = 180;
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
				submitData();
			},
			'清 除':function(){
				calcelData();
			},
			'关 闭':function(){
				colseData();
			}
		},
		
		open: function() { 
			_addCategory();
		},
		close: function() {
			jQuery("#" + dialogId).remove();
			//_viewer.refresh();
		}
	});
	//手动打开dialog
	var dialogObj = jQuery("#" + this.dialogId);
	dialogObj.dialog("open");
}
/**
 * 根据页面中的服务id查询服务定义中的值。放到dialog中显示
 * 
 */
 function _addCategory() {
	 //获取服务ID是否输入为空
 	var servName = _viewer.getItem("SERV_ID").getValue();
	 if(servName == ""){
		 jQuery("#da_category-dial").remove();
		 Tip.showError("选择服务");
		 return false;
	 }
	//查询服务定义信息
	 var datas = {};
	 datas["SERV_NAME"] = servName ;
		//datas[UIConst.WHERE] = " and SERV_ID='"+servName+"' and ITEM_HIDDEN=2";
		//var ServInfo = FireFly.doAct("SY_SERV_ITEM","finds",datas)
	var ServInfo = FireFly.doAct("DA_DIR","getServItem",datas);	 
	//如果返回值出错，则将焦点放在服务ID项，从新输入服务ID
	if(ServInfo["_OKCOUNT_"] ==0){
		 jQuery("#da_category-dial").remove();
		 Tip.showError("服务有误");
		 return false;
	}
	//渲染dilog内容
	var table = jQuery("<table class='rhGrid' id='category_table' align='center'></table>");
	var thead = jQuery("<thead style='background-color:#F6F6F6;font-family:黑体,宋体' id='category_thead'></thead>");
	var tbody = jQuery("<tbody class='rhGrid-tbody' id='category_tbody'></tbody>");
	var tr = jQuery("<tr><td colspan='4' style='height:20px;'></td></tr>");
	tr.appendTo(thead);
	var tTr = jQuery("<tr></tr>");
	//查询字段td
	var headTdFile = jQuery("<td style='width:150px;text-align:center;height:40px;font-size:14px;'>查询字段</td>");
	//select的TD
	var selectTd = jQuery("<td style='width:200px;height:40px;'></td>");
	var div = jQuery("<div  style='width:200px;height:24px;border:solid 1px #aaaaaa;'></div>");
	//select
	var select = jQuery("<select id ='selectValue' style='width:198px;height:22px;margin:1px;'></select>");
	var option = jQuery("<option value=''></option>");
	option.appendTo(select);
	//向option中添加 字段名称、字段code
	for(var i=0;i < ServInfo["_DATA_"].length; i++ ){
		  jQuery("<option value='"+ServInfo["_DATA_"][i]["ITEM_CODE"]+"'>"+ServInfo["_DATA_"][i].ITEM_NAME+"</option>").appendTo(select);
	}
	select.appendTo(div);
	div.appendTo(selectTd);
	//input的td
	//var inputTd = jQuery("<td style='width:100px;height:40px;'><input type='button' value='添加' id='addRule' style='width:50px;font-size:14px;height: 24px;'></input></td>");
	
	var inputTd = jQuery("<td style='width:75px;height:40px;'><input type='hidden' id='signVal' value=','></input></td>");
	//添加按钮td
	var subTd = jQuery("<td style='width:120px;height:40px;text-align:left;'><input type='button' value='添加' id='addRule' style='width:50px;font-size:14px;height: 24px;'></input></td>");		

	
	
	//第二个tr，规则显示
	var guizeTr = jQuery("<tr></tr>");
	var guizeTd = jQuery("<td style='text-align:center; font-size:14px;height:40px;'>打印查询规则</td>");
	//档号规则显示td
	var guizeInputTd = jQuery("<td colspan='3' style='height:40px;'><input type='text' value='' style='width:350px;' id='ruleText'></input>" +
											  					   "<input type='hidden' value='' id='ruleCode'</td>");
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
	table.appendTo(jQuery("#da_category-dial"));
	//如果页面中已经有了规则，则取过来。
	var name = _viewer.getItem("CODE_RULE_NAME").getValue() || "";
	var code = _viewer.getItem("CODE_RULE_VAL").getValue() || "";
	if(name != ""){
		jQuery("#ruleText").val(name);
		jQuery("#ruleCode").val(code);
	}
	jQuery("#addRule").unbind("click").bind("click",function(){
			var selectValue = jQuery("#selectValue").val() || "";
			var selectText = jQuery("#selectValue").find("option:selected").text() || "";
			var signVal = jQuery("#signVal").val() || "";
			var ruleText = jQuery("#ruleText").val() || "";
			var ruleCode = jQuery("#ruleCode").val() || "";
			if(selectValue == ""){
				alert("请选择查询字段");
				return false;
			}

			if(ruleText == ""){
				jQuery("#ruleText").val(selectText);
				jQuery("#ruleCode").val(selectValue);
			}
			else{
				jQuery("#ruleText").val(ruleText+signVal+selectText);
				jQuery("#ruleCode").val(ruleCode+signVal+selectValue);
			}
		 });
 }
 
//绑定取消按钮
function calcelData(){
	var ruleName = jQuery("#ruleText").val("");
	var ruleCode = jQuery("#ruleCode").val("");	
}

//确定按钮时间
function submitData(){
	var ruleName = jQuery("#ruleText").val() || "";
	var ruleCode = jQuery("#ruleCode").val() || "";
	_viewer.getItem("CODE_RULE_NAME").setValue(ruleName);
	_viewer.getItem("CODE_RULE_VAL").setValue(ruleCode);
	jQuery("#da_category-dial").remove();
}

function colseData(){
	jQuery("#da_category-dial").remove();
}
 