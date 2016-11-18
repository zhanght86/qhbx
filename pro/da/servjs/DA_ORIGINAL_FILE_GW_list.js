var _viewer = this;




_viewer.fetchData = function() {
	//选择获取哪年的数据，
	//弹出页面，选择获取数据的一些条件
	var winDialog = jQuery("<div></div>").attr("id", "fetchDataQueryDiv").attr("title","获取原始文件条件");
	var table = jQuery("<table class='rhGrid' align='center' id='queryTable'></table>").appendTo(winDialog);
	var tbody = jQuery("<tbody class='rhGrid' style='background-color:#F6F6F6;font-family:黑体,宋体'></tbody>").appendTo(table);
	var tr1 = jQuery("<tr></tr>").appendTo(tbody);
	var tr2 = jQuery("<tr></tr>").appendTo(tbody);
	var tr3 = jQuery("<tr></tr>").appendTo(tbody);
	var tr5 = jQuery("<tr></tr>").appendTo(tbody);
	var tr4 = jQuery("<tr></tr>").appendTo(tbody);
	var tr1td1 = jQuery("<td  style='width:200px;text-align:center;height:40px;font-size:14px;padding-top:20px;padding-left: 50px;'>年&nbsp;&nbsp;&nbsp;&nbsp;度</td>").appendTo(tr1);
	var tr1td2 = jQuery("<td style='padding-top:20px;' ></td>").appendTo(tr1);
	var tr2td1 = jQuery("<td style='width:200px;text-align:center;height:40px;font-size:14px;padding-left: 50px;'>公文类型</td>").appendTo(tr2);
	var tr2td2 = jQuery("<td></td>").appendTo(tr2);
	var tr5td1 = jQuery("<td style='width:200px;text-align:center;height:40px;font-size:14px;padding-left: 50px;'>公文编号</td>").appendTo(tr5);
	var tr5td2 = jQuery("<td><input ID='GW_CODE' type='text' size='32'></td>").appendTo(tr5);
	var tr3td1 = jQuery("<td style='width:200px;text-align:center;height:40px;font-size:14px;padding-left: 50px;'>起草时间</td>").appendTo(tr3);
	var tr3td2 = jQuery("<td><input type='text' id='begintime' class='Wdate ui-date-default' style='width:95px;height:22px;border:1px solid #aaaaaa;cursor: pointer;vertical-align:center;line-height:22px;' onfocus=\"WdatePicker({startDate:'%y-01-01',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:false})\"></input>&nbsp;<span style='font-size:12px;'>至</span>&nbsp;<input  type='text' id='endtime' class='Wdate ui-date-default' style='width:95px;height:22px;border:1px solid #aaaaaa;cursor: pointer;vertical-align:center;line-height:22px;' onfocus=\"WdatePicker({startDate:'%y-01-01',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:false})\"></input></td>").appendTo(tr3);
	var tr4td1 = jQuery("<td colspan='2' align='center' style='font-size:10px;padding-top:30px;'>每次将取"+System.getVar("@C_DA_FETCH_DATA_COUNT@")+"条数据</td>").appendTo(tr4);
	
	var yearDict = FireFly.getDict("DA_YEAR");
	
	var selectStr = "<div style='width:220px;height:22px;border:solid 1px #aaaaaa;'><select id ='GW_YEAR' name ='GW_YEAR' style='width:218px;height:20px;margin:1px;'>";
	jQuery.each(yearDict[0].CHILD,function(i,item){
		selectStr += "<option value=" + item.ITEM_CODE + ">" + item.ITEM_NAME + "</option>";
	});
	
	selectStr += "</select></div>";
	jQuery(selectStr).appendTo(tr1td2);
	
	var fawenTypes = FireFly.getDict("OA_GW_TMPL_FW_CODE");
	var shouwenTypes = FireFly.getDict("OA_GW_TMPL_SW_CODE");
	var fawenTypesSelectStr = "<div style='width:220px;height:22px;border:solid 1px #aaaaaa;'><select id ='TMPL_CODE' name ='TMPL_CODE' style='width:218px;height:20px;margin:1px;'>";
	//fawenTypesSelectStr += "<option value=''></option>";
	jQuery.each(fawenTypes[0].CHILD,function(i,item){
		fawenTypesSelectStr += "<option value=" + item.ITEM_CODE + ">" + item.ITEM_NAME + "</option>";
	});
	jQuery.each(shouwenTypes[0].CHILD,function(i,item){
		fawenTypesSelectStr += "<option value=" + item.ITEM_CODE + ">" + item.ITEM_NAME + "</option>";
	});	
	
	fawenTypesSelectStr += "</select></div>";
	jQuery(fawenTypesSelectStr).appendTo(tr2td2);
	
	var posArray = [];
	if (event) {
		var cy = event.clientY;
		posArray[0] = "";
		posArray[1] = cy; // + 120;
	}
	winDialog.appendTo(jQuery("body"));
	jQuery("#fetchDataQueryDiv").dialog({
		autoOpen : false,
		width : 600,
		height : 300,
		modal : true,
		resizable : false,
		position : posArray,
		close : function() {
			winDialog.remove();
		},buttons:{"确定":function(){
			var params = {};
			params["GW_YEAR"] = jQuery("#GW_YEAR").val();
			params["TMPL_CODE"] = jQuery("#TMPL_CODE").val();
			params["GW_CODE"] = jQuery("#GW_CODE").val();
			params["begintime"] = jQuery("#begintime").val();
			params["endtime"] = jQuery("#endtime").val();
			
			_viewer.shield();
			
			var resultData = FireFly.doAct(_viewer.servId, "fetchData", params);	

			if (resultData[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {			
				if (resultData.MOREOVER_COUNT > 0) {
					_viewer.refresh();
					alert("还有 " + resultData.MOREOVER_COUNT + "条数据未获取，请继续获取数据。");
					_viewer.shieldHide();
				} else {
					_viewer.shieldHide();
					_viewer.refresh();
					alert("" + resultData.FETCH_COUNT + "条数据获取。");
					winDialog.remove();
				} 
			} else {
				_viewer.shieldHide();
				alert("获取数据出错： " + resultData[UIConst.RTN_MSG]);
				winDialog.remove();				
			}
		},"取消":function(){
			winDialog.remove();
		}}
	});
	jQuery("#fetchDataQueryDiv").dialog("open");
	jQuery(".ui-dialog-titlebar").last().css("display", "block");
}

