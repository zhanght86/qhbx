var _viewer = this;

jQuery("#off-purchase-div").remove();//先删除，防止页面刷新时出现追加现象
jQuery(".searchDiv").remove(); //删除系统查询模块

var paramsDate = _viewer.params || {};
if (!paramsDate["NEW_DATE"]) {
	paramsDate["NEW_DATE"] = compareNewDate();
}

_viewer.grid.getBodyTr().each(function() {
	bindTitleColumEvent(jQuery(this).find("td[icode='APPLY_TITLE']").find("a").eq(0), jQuery(this).find("td[icode='APPLY_ID']").html());
});

//添加日期选择控件，添加部门查询
var dateDiv = jQuery("<div style='overflow:auto;' id='off-purchase-div'></div>").appendTo(jQuery(".rhGrid-btnBar"));
var rightDiv = jQuery("<div>>></div>").attr("title","下一月")
	.css({"font-weight":"bold","cursor" : "pointer","margin" : " 5px 10px 0px 10px","max-width" : "20px","float":"right","color":"blue"})
	.appendTo(dateDiv);
var dateInp = jQuery("<input id='off-purchase-datepicker' class='Wdate ui-date-default' type='text' style='float:right;' value = ''/>");
dateInp.css({"max-width" : "100px","cursor" : "pointer","border":"1px solid #91BDEA"}).val(paramsDate["NEW_DATE"]);
dateInp.attr("readonly",true).appendTo(dateDiv);
var leftDiv = jQuery("<div><<</div>").attr("title","上一月")
	.css({"font-weight":"bold","cursor" : "pointer","margin" : " 5px 10px 0px 10px","max-width" : "20px","float":"right","color":"blue"})
	.appendTo(dateDiv);

dateInp.bind("focus", function() { //给日期控件绑定事件
	WdatePicker({
		onpicked : function() {
			wdatePickerOnpicked(dateInp.val());
		},
		dateFmt : 'yyyy-MM',
		maxDate : '%y-%M'
	});
});

//[上一月]按钮事件绑定
leftDiv.bind("click",function(){
	var inpVal = nextMonth(-1, dateInp.val() + "-01");
	dateInp.val(inpVal);
	wdatePickerOnpicked(dateInp.val());
});

//[下一月]按钮事件绑定
rightDiv.bind("click",function(){
	var inpVal = nextMonth(1, dateInp.val() + "-01");
	dateInp.val(inpVal);
	wdatePickerOnpicked(dateInp.val());
});

//给列表[发放]按钮绑定事件
_viewer.grid.getBtn("notice").unbind("click").bind("click",function() {
	var applyId = _viewer.grid.getRowItemValue(jQuery(this).attr("rowpk"),"APPLY_ID");
	var userCode = _viewer.grid.getRowItemValue(jQuery(this).attr("rowpk"),"S_USER");
	var userName = _viewer.grid.getRowItemValue(jQuery(this).attr("rowpk"),"S_USER__NAME");
	var deptCode = _viewer.grid.getRowItemValue(jQuery(this).attr("rowpk"),"S_DEPT");
	var deptNAME = _viewer.grid.getRowItemValue(jQuery(this).attr("rowpk"),"S_DEPT__NAME");
	Tab.open({
		"url" : "OA_OFF_APPLY_GIVE.card.do?pkCode=" + applyId + "&NEW_DATE=" + dateInp.val(),
		"params" : {
			"DEPT_CODE" : deptCode,
			"USER_CODE" : userCode,
			"DEPT_NAME" : deptNAME,
			"USER_NAME" : userName,
			"NEW_DATE" : rhDate.getTime(),
			"links" : {
				"NEW_DATE" : dateInp.val()
			}
		},
		"tTitle" : "办公用品发放单",
		"menuFlag" : 4
	});
});

//遍历数据
_viewer.grid.getBodyTr().each(function() {
	var thisOutFlag = jQuery(this).find("[icode='OUTSTORAGE_STATUS']").html();
	if (thisOutFlag == "2") { //已出库
		jQuery(this).find("td[icode='notice']").css({"color":"red","text-align":"center"}).html("已出库");
	} else if (thisOutFlag == "3") { //未完全出库
		jQuery(this).find("td[icode='notice']").find("span[class='rh-icon-inner']").html("继续出库");
	}
	
});
	
//新打开一个标签
function wdatePickerOnpicked(dateInp) {
	var extWhere = " and  substr(S_MTIME,1,7) = '"+ dateInp +"' and OUTSTORAGE_STATUS = '1'";
	Tab.open({
		"url" : "OA_OFF_APPLY_FINISH.list.do",
		"params" : {
			"NEW_DATE" : dateInp,
			"_extWhere": extWhere
		},
		"tTitle" : "",
		"menuFlag" : 4
	});
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

//获得某个日期的下一个月
function nextMonth(month, oldDate) {
	dtEnd = rhDate.stringToDate(oldDate);
	var newMonth = (dtEnd.getMonth() + 1) + month;
	var newYear = 0;
	if (newMonth == 13) {
		newYear = dtEnd.getFullYear() + 1;
		newMonth = "01";
	} else if (newMonth == 0) {
		newYear = dtEnd.getFullYear() - 1;
		newMonth = 12;
	} else {
		if ((newMonth + "").length ==1) {
			newMonth = "0" + newMonth;
		}
		newYear = dtEnd.getFullYear();
		newMonth = newMonth;
	}
	return   newYear + "-" + newMonth;
}

//给列表绑定事件
function bindTitleColumEvent(thisBindObj, pkCode){
	thisBindObj.unbind("click").bind("click", function(){
		Tab.open({
			"url" : "OA_OFF_APPLY.card.do?pkCode=" + pkCode,
			"tTitle" : "办公用品申请单查看",
			"menuFlag" : 4
		});
	});
}
