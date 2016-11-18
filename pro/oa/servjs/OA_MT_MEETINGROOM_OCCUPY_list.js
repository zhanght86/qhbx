var _viewer = this;

//设定样式，移除不需要的元素
jQuery(".rhGrid-thead-box", _viewer.grid._table).hide();
jQuery(".checkTD", _viewer.grid._table).hide();
jQuery(".tBody-tr", _viewer.grid._table).css({"height" : "35px"});
jQuery("#meetintroom-datepicker").remove();
jQuery(".searchDiv").remove();

//得到父页面传递数据
var params = _viewer.params;
var floorId = params.GET_FLOOR_ID || "";
var newDate = params.NEW_DATE || rhDate.getDateTime("DATE");
var floorName = params.FLOOR_NAME || "";

// 日期数值
var formitDateNum = formitNewDateNum();
var newDateNum = newDate.replace(/-/gi, "");
var formitTimeNum = formitNewTimeNum();

// 遍历各个时间区域列表
iterTable("td[icode='FLOOR_ID']", "MORNING");
iterTable("td[icode='IMAGE']", "AFTERNOON");
iterTable("td[icode='BUILDING_AREA']", "EVENING");

/**
 * 遍历表格数据
 * 
 * @param tdName
 */
function iterTable(tdName, timeArea) {
	//获得遍历对象
	var names = jQuery(tdName, _viewer.grid.getTable());
	//遍历数据
	jQuery.each(names,function(i, n) {
		var meetingRoomId = jQuery(this).parent().find("[icode='MR_ID']").html();
		var meetingRoomType = jQuery(this).parent().find("[icode='MR_TYPE']").html();
		if ("BLUE" == jQuery(n).html() && _viewer.links.callFlag == null) {//modified by wc
			if (newDateNum > formitDateNum) {
				// 如果返回值为 BLUE，列表项添加单击事件，绑定会议室预定业务功能
				jQuery(n).text("").bind("click",function(event) {
					var whichTimeArea = inWhichArea(timeArea);// 返回相对时间区域
					//打开新标签
					openNewTabFun(meetingRoomId, meetingRoomType, floorId, newDate, floorName, timeArea);
				});
			} else if (newDateNum == formitDateNum) {
				if ("MORNING" == timeArea) {
					if (formitTimeNum > 1200) {
						jQuery(n).text("").attr("title", "早于系统时间，不可预订").bind("click",function(event) {
								_viewer.listBarTipError("早于系统时间，不可预订");
							});
					} else {
						jQuery(n).text("").bind("click",function(event) {
							// 返回相对时间区域
							var whichTimeArea = inWhichArea(timeArea);
							//打开新标签
							openNewTabFun(meetingRoomId, meetingRoomType, floorId, newDate, floorName, timeArea);
						});
					}
				} else if ("AFTERNOON" == timeArea) {
					if (formitTimeNum > 1800) {
						jQuery(n).text("").attr("title", "早于系统时间，不可预订").bind("click",function() {
								_viewer.listBarTipError("早于系统时间，不可预订");
							});
					} else {
						// 如果返回值为 BLUE，列表项添加单击事件，绑定会议室预定业务功能
						jQuery(n).text("").bind("click",function(event) {
							var whichTimeArea = inWhichArea(timeArea);// 返回相对时间区域
							//打开新标签
							openNewTabFun(meetingRoomId, meetingRoomType, floorId, newDate, floorName, timeArea);
						});
					}
				} else if ("EVENING" == timeArea) {
					if (formitTimeNum > 2359) {
						jQuery(n).text("").attr("title", "早于系统时间，不可预订").bind("click",function() {
								_viewer.listBarTipError("早于系统时间，不可预订");
							});
					} else {
						// 如果返回值为 BLUE，列表项添加单击事件，绑定会议室预定业务功能
						jQuery(n).text("").bind("click",function(event) {
							var whichTimeArea = inWhichArea(timeArea);// 返回相对时间区域
							//打开新标签
							openNewTabFun(meetingRoomId, meetingRoomType, floorId, newDate, floorName, timeArea);
						});
					}
				}
			} else {
				jQuery(n).text("").attr("title", "早于系统时间，不可预订").bind("click",function() {
						_viewer.listBarTipError("早于系统时间，不可预订");
					});
			}
		}
		
		/*add by wangchen*/
		if("BLUE" == jQuery(n).html() && _viewer.links.callFlag != null && _viewer.links.callFlag == "OA_MT_MEETING") {
			if(newDateNum >= formitDateNum){
				jQuery(n).text("").unbind("click").bind("click",function(event){
					if(!confirm("确认并关闭此窗口？")){
						return;
					}
					var sTime;//选中的单元格的起始时间
					var eTime;//选中的单元格的结束时间 
					if(jQuery(n).attr("icode") == "FLOOR_ID"){
						sTime = newDate + " " + "09:00";
						eTime =  newDate + " " + "12:00";
					}else if(jQuery(n).attr("icode") == "IMAGE"){
						sTime = newDate + " " + "13:00";
						eTime =  newDate + " " + "18:00";
					}else if(jQuery(n).attr("icode") == "BUILDING_AREA"){
						sTime = newDate + " " + "18:00";
						eTime =  newDate + " " + "23:59";
					}
					
					var startTime = _viewer.params.OA_MT_MEETING_BEGIN_TIME==""?"0":_viewer.params.OA_MT_MEETING_BEGIN_TIME;//服务传来的初始开始时间
					var endTime = _viewer.params.OA_MT_MEETING_END_TIME==""?"0":_viewer.params.OA_MT_MEETING_END_TIME;//服务传来的初始结束时间
					if(formitDateTime(startTime) < formitDateTime(sTime) || formitDateTime(startTime) > formitDateTime(eTime)){
						startTime = sTime;
					}
					if(formitDateTime(endTime) < formitDateTime(sTime) || formitDateTime(endTime) > formitDateTime(eTime)){
						endTime = eTime;
					}
					var handler1 = _viewer.params.handler.handler_1;//OA_MT_MEETING服务的句柄 
					var handler2 = _viewer.params.handler.handler_2;//OA_MT_MEETINGROOM_V服务的句柄 
					handler1.getItem("BEGIN_TIME").setValue(startTime);
					handler1.getItem("END_TIME").setValue(endTime);
					handler1.getItem("PLACE").setValue(jQuery(n).parent().find("td[icode='NAME']").html());
					handler1.getItem("MR_ID").setValue(jQuery(n).parent().find("td[icode='MR_ID']").html());
					parent.jQuery("a[href='#" + handler1.servId +　"-tabDiv']").click();
					parent.Tab.closeDirect(handler2.servId + "-tabFrame");//关闭一个tab标签,有父窗口执行（传主键关卡片或其他，不传关列表）
					parent.Tab.closeDirect(_viewer.servId + "-tabFrame");//关闭一个tab标签,有父窗口执行（传主键关卡片或其他，不传关列表）
				});
			}else if(newDateNum < formitDateNum){
				jQuery(n).text("").attr("title", "早于系统时间，不可预订").bind("click",function(event){
					_viewer.listBarTipError("早于系统时间，不可预订");
				});
			}
		}
		
		//红色部分
		if (jQuery(n).html().indexOf(",") > 0) {
			jQuery(n).css({"background-color":"red"});
			// 如果返回值为RED，列表项添加mouseover事件，绑定相应预订信息查看功能
			var oldObjVal = jQuery(n).html();
			jQuery(n).text("");
			jQuery(n).bind("mouseover",function(event) {
				// 获得上、下、晚上列表项的会议室预定信息ID集合
				var bookingIds = oldObjVal.substring(4, oldObjVal.length);
				var bookingIdStr = bookingIds.split(",");
				var tdHeight = jQuery(".tBody-tr").height();
				getDialog(event,bookingIdStr,(tdHeight * bookingIdStr.length + 40));

				// 将对话框中【关闭】、列表头部【查询项】、【页面显示】隐藏
				var bookingObg = jQuery("#BOOKING_OCCUPY_DIALOG");
				bookingObg.find("[class='rhGrid-btnBar']").remove();
				bookingObg.find("[class='rhGrid-page']").remove();
				jQuery(".ui-dialog-titlebar-close").remove();

				// 添加列表项mouseleave时间，移除dialog对话框
			}).bind("mouseleave",function(event) {
				jQuery("#BOOKING_OCCUPY_DIALOG").remove();
			});
		}
		if ("GRAY" == jQuery(n).html()) {
			jQuery(n).html("该会议室在此时间段不可用").css({"color" : "gray","background-color":"#ebebeb"});
		}
	});
}

//页面加载成功，添加日期控件
jQuery(document).ready(function() {
	jQuery("#oa-meetintroom-div").remove();
	var dateDiv = jQuery("<div style='overflow:auto;' id='oa-meetintroom-div'></div>").appendTo(jQuery(".rhGrid-btnBar"));
	var leftDiv = jQuery("<div><<</div>").attr("title","上一天")
		.css({"font-weight":"bold","cursor" : "pointer","margin" : " 5px 10px 0px 10px","max-width" : "20px","float":"left","color":"blue"})
		.appendTo(dateDiv);
	var dateInp = jQuery("<input id='meetintroom-datepicker' class='Wdate ui-date-default' type='text' style='float:left;'/>");
	dateInp.css({"max-width" : "100px","cursor" : "pointer","border":"1px solid #91BDEA"}).val(newDate);
	dateInp.appendTo(dateDiv);
	var rightDiv = jQuery("<div>>></div>").attr("title","下一天")
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
});


/*
 * 构建弹出框页面布局
 */
function getDialog(event, bookingID, trHieght) {
	var _self = this;
	var theadHieght = jQuery(".rhGrid-thead").height();
	// 构造dialog
	var dialogId = "BOOKING_OCCUPY_DIALOG"; // 设置Dialog的id
	var winDialog = jQuery("<div style='overflow:hidden'></div>").attr("id", dialogId).attr("title", "");
	winDialog.appendTo(jQuery("body"));
	var bodyWid = jQuery("body").width() * 2 / 3;
	var hei = trHieght + theadHieght + 2;
	var wid = bodyWid;
	var posArray = [ 30, 30 ];
	if (event) {
		var cx = event.clientX;
		var cy = event.clientY;
		posArray[0] = cx + 50;
		posArray[1] = (cy - (hei + theadHieght)) > 10 ? cy - (hei + theadHieght) : cy + 30 ;
	}
	jQuery("#" + dialogId).dialog({
		autoOpen : false,
		height : hei,
		width : wid,
		modal : false,
		resizable : false,
		position : posArray,
		open : function() {},
		close : function() {}
	});
	var dialogObj = jQuery("#" + dialogId);
	dialogObj.dialog("open");
	dialogObj.focus();
	jQuery(".ui-dialog-titlebar").last().empty();//css("display", "block");// 设置标题显示
	dialogObj.parent().addClass("rh-bottom-right-radius rhSelectWidget-content");
	Tip.showLoad("努力加载中...", null, jQuery(".ui-dialog-title", winDialog).last());
	var divMt = jQuery("<div></div>").appendTo(dialogObj);
	// 重置dialog中的list高度，因为只有一条数据，不用重新计算列表高度，故覆盖系统方法
	this._resetHeiWid = function() {};
	//将bookingId放到sql语句中
	var bookingInSql = "";
	for (var i = 0; i < bookingID.length; i++) {
		bookingInSql += "'" + bookingID[i] + "',";
	}
	bookingInSql = bookingInSql.substring(0, bookingInSql.length - 1);
	var invokServ = {
		"sId" : "OA_MT_BOOKING_OCCUPY",
		"pCon" : divMt,
		"showTitleBarFlag" : "false",
		"extWhere" : "AND BOOKING_ID in (" +bookingInSql + ")",
		"_SELECT_" : "TITLE,S_USER,S_DEPT,START_TIME,END_TIME,MEMO,CONTACT_TEL",
		"selectView" : true,
		"resetHeiWid" : this._resetHeiWid, // 重置高度
		"parHandler" : this,
		"_HIDE_":""
	};
	this.listView = new rh.vi.listView(invokServ);
	this.listView.show();
	//去除列表阴影效果
	jQuery(".rh-bottom-right-radius", divMt).css({"-webkit-box-shadow":"0px 0px 0px","box-shadow":"0px 0px 0px"});
	jQuery(".content-mainCont", divMt).css({"height":hei});
	jQuery(".rhGrid-btnBar", divMt).remove();
};

// 格式化当前日期 yyyy-MM-dd，返回日期整数值yyyyMMdd
function formitNewDateNum() {
	return System.getVar("@DATE@").replace(/-/gi, "");
}

// 格式化当前时间 HH:mm，返回日期整数值HH:mm
function formitNewTimeNum() {
	var date = new Date();
	var hh = date.getHours() + "";
	var mm = date.getMinutes() + "";
	if (mm.length == 1) {
		mm = "0" + mm;
	}
	var fullDateNum = hh + "-" + mm;
	return fullDateNum.replace(/-/gi, "");
}

//获得某个时间区域的常量值
function inWhichArea(timeArea){
	var areaStr = new Array();
	areaStr[0] = "MORNING";
	areaStr[1] = "AFTERNOON";
	areaStr[2] = "EVENING";
	for (var i = 0; i < areaStr.length; i++) {
		if (areaStr[i] == timeArea) {
			return areaStr[i];
			break;
		}
	}
}

//打开新的标签
function openNewTabFun(meetingRoomId, meetingRoomType, floorId, newDate, floorName, timeArea){
	Tab.open({
		"url" : "OA_MT_BOOKING.card.do",
		"params" : {
			"links" : {
				"MR_ID" : meetingRoomId,
				"MEETING_TYPE" : meetingRoomType,
				"GET_FLOOR_ID" : floorId,
				"NEW_DATE" : newDate,
				"FLOOR_NAME" : floorName,
				"timeArea" : timeArea
			},
			"callBackHandler" : _viewer,
			"closeCallBackFunc" : function() {
				_viewer.refresh();
			}
		},
		"tTitle" : "会议室预定",
		"menuFlag" : 4
	});
}

//add by wangchen
function formitDateTime(dateTime){
	dateTime = dateTime.replace(/-/gi, "");
	dateTime = dateTime.replace(" ", "");
	return dateTime.replace(/:/gi, "");
}

function wdatePickerOnpicked(inputVal) {
	if(_viewer.links.callFlag != null && _viewer.links.callFlag == "OA_MT_MEETING"){
		Tab.open({
			"url" : "OA_MT_MEETINGROOM_OCCUPY.list.do",
			"params" : {
				"links" : {"callFlag":_viewer.links.callFlag},//需要传到后台的参数放到里面 add by wangchen
				"GET_FLOOR_ID" : floorId || "",
				"NEW_DATE" : inputVal,
				"FLOOR_NAME" : floorName || "",
				"OA_MT_MEETING_BEGIN_TIME" : _viewer.params.OA_MT_MEETING_BEGIN_TIME,//OA_MT_MEETINGROOM服务传递的会议开始时间 add by wangchen
				"OA_MT_MEETING_END_TIME" : _viewer.params.OA_MT_MEETING_BEGIN_TIME,//OA_MT_MEETINGROOM服务传递的会议结束时间 add by wangchen
				"OA_MT_MEETING_pSid" : _viewer.params.OA_MT_MEETING_pSid,//OA_MT_MEETING服务传递的传给本服务的服务ID add by wangchen
				"handler" : {
							"handler_1":_viewer.params.handler.handler_1,//OA_MT_MEETING文档句柄供本页面回调用 add by wangchen
							"handler_2":_viewer.params.handler.handler_2//OA_MT_MEETINGROOM_V服务的句柄 add by wangchen
							},
				"_extWhere" : (floorId || "") == "" ? "" : " and FLOOR_ID ='" + floorId + "'"
			},
			"tTitle" : (floorName || "" ) + "会议室占用情况",
			"menuFlag" : 4
		});
		jQuery("#meetintroom-datepicker").remove();
	}else{
		Tab.open({
			"url" : "OA_MT_MEETINGROOM_OCCUPY.list.do",
			"params" : {
				"GET_FLOOR_ID" : floorId || "",
				"NEW_DATE" : inputVal,
				"FLOOR_NAME" : floorName || "",
				"_extWhere" : (floorId || "") == "" ? "" : " and FLOOR_ID ='" + floorId + "'"
			},
			"tTitle" : (floorName || "" ) + "会议室占用情况",
			"menuFlag" : 4
		});
		jQuery("#meetintroom-datepicker").remove();
	}
}
	