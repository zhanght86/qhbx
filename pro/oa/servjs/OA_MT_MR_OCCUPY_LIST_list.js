var _viewer = this;

jQuery(".searchDiv").remove();

_viewer.grid.getBodyTr().unbind("dbclick");

var thisListPkCodes = _viewer.grid.getPKCodes();
for (var i = 0; i < thisListPkCodes.length; i++) {
	var data = {
				"MR_ID":_viewer.grid.getRowItemValue(thisListPkCodes[i], "MR_ID"),
				"MEETING_TYPE":_viewer.grid.getRowItemValue(thisListPkCodes[i], "MR_TYPE")
			   };
	addOccpuyCss(_viewer.grid.getRowItemValue(thisListPkCodes[i], "OCCUPY_DETAIL"), 
		_viewer.grid.getRowItem(thisListPkCodes[i], "OCCUPY_DETAIL"), data);
}

//添加列表说明
jQuery("div[id='" + _viewer.servId + "_memo_div']").remove();
var memoArray = new Array();
memoArray.push("<div id='" + _viewer.servId + "_memo_div' ");
memoArray.push("style='overflow:hidden;margin-top:10px;font-size:14px;font-weight:bold;'><div style='float:left;'>");
memoArray.push("<span style='width:23px;height:23px;background-color:#c1e4fc;");
memoArray.push("'>&nbsp;&nbsp;&nbsp;&nbsp;</span>");
memoArray.push("<span style='margin-left:5px;'>表示此时间段会议室未预定,鼠标点击可进入会议室预定申请单预定;</span>");
memoArray.push("</div><div style='float:left;'>");
memoArray.push("<span style='margin-left:5px;width:23px;height:23px;background-color:#eb657e;");
memoArray.push("'>&nbsp;&nbsp;&nbsp;&nbsp;</span>");
memoArray.push("<span style='margin-left:5px;'>表示此时间段会议室已预定,鼠标滑动或点击红色区域可查看占用情况;</span>");
memoArray.push("</div><div>");
_viewer.grid.getTable().after(memoArray.join(""));

var params = _viewer.params;
var newDate = params.NEW_DATE || rhDate.getDateTime("DATE");

//页面加载成功，添加日期控件
jQuery("#oa-meetintroom-div").remove();
var dateDiv = jQuery("<div style='overflow:auto;' id='oa-meetintroom-div'></div>").appendTo(jQuery(".rhGrid-btnBar"));
var leftDiv = jQuery("<div>&lt;&lt;</div>").attr("title","上一天")
	.css({"font-weight":"bold","cursor" : "pointer","margin" : " 5px 10px 0px 10px","max-width" : "20px","float":"left","color":"blue"})
	.appendTo(dateDiv);
var dateInp = jQuery("<input id='meetintroom-datepicker' class='Wdate ui-date-default' type='text' style='float:left;'/>");
dateInp.css({"max-width" : "100px","cursor" : "pointer","border":"1px solid #91BDEA"}).val(newDate);
dateInp.appendTo(dateDiv);
var rightDiv = jQuery("<div>&gt;&gt;</div>").attr("title","下一天")
.css({"font-weight":"bold","cursor" : "pointer","margin" : " 5px 10px 0px 10px","max-width" : "20px","float":"left","color":"blue"})
.appendTo(dateDiv);
//前一天按钮绑定事件
leftDiv.bind("click",function(){
	var inpVal = rhDate.nextDate(dateInp.val(),1);
	dateInp.val(inpVal);
	wdatePickerOnpicked(dateInp.val());
});
//下一天按钮绑定事件
rightDiv.bind("click",function(){
	var inpVal = rhDate.nextDate(dateInp.val(),-1);
	dateInp.val(inpVal);
	wdatePickerOnpicked(dateInp.val());
});
dateInp.bind("focus", function() {
	WdatePicker({
		onpicked : function() {
			wdatePickerOnpicked(dateInp.val());
		}
	});
});

//日期点击事件
function wdatePickerOnpicked(date) {
	Tab.open({
		"url" : _viewer.servId + ".list.do",
		"params" : {
			"NEW_DATE":date
		},
		"tTitle" :"会议室占用情况",
		"menuFlag" : 4
	});
}

//渲染占用情况
function addOccpuyCss(occupyDetail, jQueryObj, data){
	jQueryObj.css({"width":"540px", "min-width":"540px"});
	var thisOccpuy = jQuery.parseJSON(occupyDetail);
	jQueryObj.html("");
	var parentDiv = jQuery("<div class='rh-meetingroom-kedu'></div>");
	for (var i = 0; i < thisOccpuy.length; i++) {
		var thisObj = thisOccpuy[i];
		var thisOccupyDiv = jQuery("<div></div>");
		if (thisObj["isOccupy"] == "true") {
			thisOccupyDiv.attr("title",thisObj.bookTime + "\n" + thisObj.TITLE + "\n" + thisObj.USER_NAME);
			thisOccupyDiv.addClass("rh-meetingroom-isOccupy");
			readBooking(thisOccupyDiv, thisObj);
		} else {
			thisOccupyDiv.attr("title",thisObj.bookTime);
			thisObj.MR_ID = data.MR_ID;
			thisObj.MEETING_TYPE = data.MEETING_TYPE;
			thisOccupyDiv.addClass("rh-meetingroom-noOccupy");
			addBooking(thisOccupyDiv, thisObj);
		}
		thisOccupyDiv.css({"width":thisObj["width"]+"px"});
		thisOccupyDiv.appendTo(parentDiv);
	}
	parentDiv.appendTo(jQueryObj);
}

//绑定单击事件
function addBooking(jQueryObj, thisObj){
	jQueryObj.unbind("click").bind("click", thisObj, function(event){
		Tab.open({
			"url" : "OA_MT_BOOKING.card.do",
			"params" : {
				"links" : {
					"MR_ID" : event.data["MR_ID"],
					"MEETING_TYPE" : event.data["MEETING_TYPE"],
					"START_TIME" : dateInp.val() + " " + event.data["START_HOUR"] + ":" + event.data["START_MINUTE"],
					"END_TIME" : dateInp.val() + " " + event.data["END_HOUR"] + ":" + event.data["END_MINUTE"]
				},
				"callBackHandler" : _viewer,
				"closeCallBackFunc" : function() {
					_viewer.refresh();
				}
			},
			"tTitle" : "会议室预定",
			"menuFlag" : 4
		});
    });
}

//绑定单击事件
function readBooking(jQueryObj, data){
	jQueryObj.unbind("click").bind("click", data, function(event){
		Tab.open({
			"url" : "OA_MT_BOOKING.card.do?pkCode=" + event.data.BOOKING_ID,
			"params" : {
				"callBackHandler" : _viewer,
				"closeCallBackFunc" : function() {
					_viewer.refresh();
				}
			},
			"tTitle" : "会议室预定",
			"menuFlag" : 4
		});
    });
}