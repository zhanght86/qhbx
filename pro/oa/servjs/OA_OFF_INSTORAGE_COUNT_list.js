var _viewer = this;

jQuery("#off-instorage-count-div").remove();

_viewer._searchText.remove(); //去除查询条件输入框
_viewer._advancedSearchBtn.remove(); //去除高级查询按钮
_viewer._searchSel.remove(); //去除查询条件的下拉列表

_viewer.getSearchBtn().unbind("click").bind("click",function(){
	var leftDate = dateInpLeft.val() || dateInpRight.val();
	var rightDate = dateInpRight.val() || dateInpLeft.val();
	_viewer.setParParams({"leftDate":leftDate,"rightDate":rightDate});
	var odeptCode = System.getVar("@ODEPT_CODE@");
	var extWhere = " and substr(S_ATIME,1,7) between '" + leftDate + "' and '" + rightDate + "' and S_FLAG = '1' and S_ODEPT = '" + odeptCode + "'";
	_viewer.setSearchWhereAndRefresh(extWhere,true);
});

var leftDateInp = System.getParParams()["leftDate"] || compareNewDate();
var rightDateInp = System.getParParams()["rightDate"] || "";

//添加日期选择控件，添加部门查询
var dateDiv = jQuery("<div style='overflow:auto;' id='off-instorage-count-div'></div>").appendTo(jQuery(".rhGrid-btnBar"));

var dateInpRight = jQuery("<input id='off-instorage-datepicker-right' class='Wdate ui-date-default' type='text' style='float:right;' value = ''/>");
dateInpRight.css({"max-width" : "100px","cursor" : "pointer","border":"1px solid #91BDEA","margin":"0px 10px 0px 10px"}).val(rightDateInp);
dateInpRight.attr("readonly",true).appendTo(dateDiv);
jQuery("<span style='float:right;dsplay:block;position:relative;top:3px;'>--</span>").appendTo(dateDiv);
var dateInpLeft = jQuery("<input id='off-instorage-datepicker-left' class='Wdate ui-date-default' type='text' style='float:right;' value = ''/>");
dateInpLeft.css({"max-width" : "100px","cursor" : "pointer","border":"1px solid #91BDEA","margin":"0px 10px 0px 0px"}).val(leftDateInp);
dateInpLeft.attr("readonly",true).appendTo(dateDiv);

dateInpLeft.bind("focus", function() { //给日期控件绑定事件
	WdatePicker({
		onpicked : function() {
			dateInpLeft.blur();
		},
		dateFmt : 'yyyy-MM',
		maxDate : '%y-%M'
	});
});

dateInpRight.bind("focus", function() { //给日期控件绑定事件
	WdatePicker({
		onpicked : function() {
			dateInpRight.blur();
		},
		dateFmt : 'yyyy-MM',
		maxDate : '%y-%M'
	});
});

jQuery(document).ready(function(){
	jQuery("<tr class='tBody-tr tBody-trOdd' id='' style = 'height:30px;font-weight:bold;background-color:#DDD;'> " 
		+ " <td class='rhGrid-td-center ' icode='COUNT' style='text-align:right;' colspan = '6'>统计总额</td>"
		+ " <td class='rhGrid-td-center ' icode='COUNT' style='text-align:right;' colspan = '1'>" + parseFloat(_viewer.grid.sumTr["OFFICE_AMOUNT"] || "0.00").toFixed(2) + "</td> "
		+ "</tr>").appendTo(_viewer.grid._table);
	jQuery("tfoot[class='rhGrid-tFoot']",_viewer.grid._table).remove();
});

//系统日期
function compareNewDate() {
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth() + 1 + "";
	if (month.length == 1) {
		month = "0" + month;
	}
	var newDate = year + "-" + month;
	return newDate;
}