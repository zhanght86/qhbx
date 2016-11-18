var _viewer = this;

jQuery("#off-purchase-count-div").remove();//先删除，防止页面刷新时出现追加现象

_viewer._searchText.remove(); //去除查询条件输入框
_viewer._advancedSearchBtn.remove(); //去除高级查询按钮
_viewer._searchSel.remove(); //去除查询条件的下拉列表

var leftDateInp = ""; //开始时间
var rightDateInp = ""; //结束时间
if (!_viewer.getParams()) {
	leftDateInp = compareNewDate();
	rightDateInp = compareNewDate();
} else {
	var thisLinks = _viewer.getParams().links || {"leftDate": compareNewDate(), "rightDate":compareNewDate()};
	leftDateInp = thisLinks.leftDate;
	rightDateInp = thisLinks.rightDate;
}

//添加日期选择控件，添加部门查询
var dateDiv = jQuery("<div style='overflow:auto;' id='off-purchase-count-div'></div>").appendTo(jQuery(".rhGrid-btnBar"));

var dateInpRight = jQuery("<input id='off-purchase-datepicker-right' class='Wdate ui-date-default' type='text' style='float:right;' value = ''/>");
dateInpRight.css({"max-width" : "100px","cursor" : "pointer","border":"1px solid #91BDEA","margin":"0px 10px 0px 10px"}).val(rightDateInp);
dateInpRight.attr("readonly",true).appendTo(dateDiv);
jQuery("<span style='float:right;dsplay:block;position:relative;top:3px;'>--</span>").appendTo(dateDiv);
var dateInpLeft = jQuery("<input id='off-purchase-datepicker-left' class='Wdate ui-date-default' type='text' style='float:right;' value = ''/>");
dateInpLeft.css({"max-width" : "100px","cursor" : "pointer","border":"1px solid #91BDEA","margin":"0px 10px 0px 0px"}).val(leftDateInp);
dateInpLeft.attr("readonly",true).appendTo(dateDiv);

//添加到导出参数
_viewer.whereData["leftDate"] = leftDateInp;
_viewer.whereData["rightDate"] = rightDateInp;

dateInpLeft.bind("focus", function() { //给日期控件绑定事件
	WdatePicker({
		onpicked : function() {
			dateInpLeft.blur();
		},
		dateFmt : 'yyyy-MM'
	});
});

dateInpRight.bind("focus", function() { //给日期控件绑定事件
	WdatePicker({
		onpicked : function() {
			dateInpRight.blur();
		},
		dateFmt : 'yyyy-MM'
	});
});

_viewer.getSearchBtn().unbind("click").bind("click",function(){
	var leftDate = dateInpLeft.val() || dateInpRight.val();
	var rightDate = dateInpRight.val() || dateInpLeft.val();
	_viewer.setParParams({"leftDate":leftDate,"rightDate":rightDate});
	dateInpLeft.val(leftDate);
	dateInpRight.val(rightDate);
	if (!settingTitle(leftDate, rightDate)) {
		_viewer.listBarTipError("请选择日期");
		return;
	}
	wdatePickerOnpicked(leftDate, rightDate);
});

jQuery(document).ready(function(){
	jQuery("<tr class='tBody-tr tBody-trOdd' id='' style = 'height:30px;font-weight:bold;background-color:#DDD;'> " 
		+ "<td class='rhGrid-td-center ' icode='AMOUNT' style='text-align:right;' colspan = '9'>汇总金额</td>"
		+ " <td class='rhGrid-td-center ' icode='AMOUNT' style='text-align:right;' colspan = '1'>" + parseFloat(_viewer.grid.sumTr["OFFICE_AMOUNT"] || "0.00").toFixed(2) + "</td> "
		+ "</tr>").appendTo(_viewer.grid._table);
	jQuery("tfoot[class='rhGrid-tFoot']",_viewer.grid._table).remove();
});

//新打开一个标签
function wdatePickerOnpicked(leftDate, rightDate) {
	if (!settingTitle(leftDate, rightDate)) {
		return false;
	}
	Tab.open({
		"url" : "OA_OFF_PURCHASE.list.do",
		"params" : {
			"links":{
				"leftDate" : leftDate,
				"rightDate" : rightDate
			},
			"leftDate" : leftDate,
			"rightDate" : rightDate
		},
		"tTitle" : "申请用品汇总",
		"menuFlag" : 4
	});
}

//设置标题
function settingTitle(leftDate, rightDate){
	if (leftDate == "") {
		if (rightDate == "") {
			return false;
		} else {
			return rightDate;
		}
	} else {
		if (rightDate == "") {
			return leftDate;
		} else {
			return leftDate + "—" + rightDate;
		}
	}
}

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