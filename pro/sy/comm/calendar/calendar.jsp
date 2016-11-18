<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--calendar.jsp日程页面-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.rh.core.base.Context" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
String user = request.getParameter("user");
String type = request.getParameter("type");
String yearmonthday = request.getParameter("yearMonthDay");
String userCode = Context.getUserBean(request).getCode();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>日程页面</title>
    <%@ include file= "/sy/base/view/inHeader.jsp" %>
    <link rel="stylesheet" type="text/css" href="/sy/comm/calendar/fullcalendar.css" />
    <script type="text/javascript" src="/sy/comm/calendar/jquery.ui.datepicker.js"></script>
    <script type="text/javascript" src="/sy/comm/calendar/jquery.ui.datepicker-zh-CN.js"></script>
    <script type="text/javascript" src="/sy/comm/calendar/fullcalendar.js"></script>
    <script type="text/javascript" src="/sy/comm/calendar/gcal.js"></script>
    <script type="text/javascript" src="/sy/comm/calendar/schedule.js"></script>

    <script type="text/javascript">
    var allHei = 1090;//jQuery("body").height()
    Tab.setFrameHei(allHei);
    function setTitleColor(e) {//设置背景颜色
        jQuery(".fc-event-title:contains('[工作计划]')").parent().css("color","yellow");
        jQuery(".fc-event-title:contains('[未发布]')").parent().css("color","red");
     }
    $(document).ready(function() {
        var currUser = "<%=userCode%>";
        var user = "<%=userCode%>";
        if (user != "null" && user != "") {
           currUser = user;
        }
        eventDataFetch.setCurrUser(currUser);
        eventDataFetch.setTreeUser(currUser);
        var lastView;

        var type = "<%=type%>";
        if (type != "null" && type != "") {
            lastView = type;
        }
        var date = new Date();
        var y = date.getFullYear();
        var m = date.getMonth();
        var d = date.getDate();
        calendarAdapter.setEventInputComp(new EventInputComp());

        reloadCanlendar();
        calendarAdapter.getCalendarContainer().datepicker({
            showButtonPanel : true,
            changeMonth : true,
            changeYear : true,
            showWeek: true,
            showOtherMonths : true,
            selectOtherMonths : true,
            regional : $.datepicker.regional["zh-CN"],
            onChangeMonthYear : function(y, m, t) {
                calendarAdapter.getEventObj().gotoDate(y, (m > 0 ? m - 1 : 0), t.selectedDay);

            },
            onSelect : function(d, t) {
                calendarAdapter.setDate({year:t.currentYear,month:t.currentMonth,day:t.currentDay});

            },
            updateEvent:function(e) {
                calendarAdapter.addMgrBtn();
            }
        });
        var yearmonthday = "<%=yearmonthday%>";
        if (yearmonthday && yearmonthday != "null") {
            var s = yearmonthday.split("-");
            var clickDate = new Date(s[0], --s[1], s[2]);
            calendarAdapter.setDate({"year":clickDate.getFullYear(),"month":clickDate.getMonth(),"day":clickDate.getDate()});
        }

        function reloadCanlendar(){
            calendarAdapter.getEventsContainer().empty();
            calendarAdapter.getEventsContainer().fullCalendar({
                height:$(window).height(),
                defaultView:lastView,
                selectable : true,
                selectHelper : true,
                select : function(start, end, allDay, jsEvent, view) {
                    if(eventDataFetch.getTreeUser() != eventDataFetch.getCurrUser())return;
                    calendarAdapter.getEventInputComp().showEventsInput({"start":start,"end":end,"allDay":allDay});
                },
                /**
                 * @return {true:呈现当前事务,false:不呈现当前事务,字符串:可自定义事务内容呈现}
                 */
                eventRender:function(e, elem) {
                    //var rtnStr = "<div style='background-color:red;'>HelloWorld</div>";       
                    setTitleColor(e);
                    return true;
                },
                /**
                 * @param e 事务数据
                 * @param ev 事件对象
                 */
                eventClick:function(e, jsEvent, view) {
                    //calendarAdapter.printCalendarProperties("event\n", e);
                    if(eventDataFetch.getTreeUser() != eventDataFetch.getCurrUser())return;
                    calendarAdapter.getEventInputComp().showEventsInput(e);
                },
                eventSources:[
                    calendarAdapter.getFetchEventData()
                ]
            });
        };
        $("body").show();
    });
</script>
<style type="text/css">
.dataLabel {
	height:18px;
	width:120px;
	float:left;
	display: inline;
	padding:2px 5px 0 0;
}
.ui-datepicker-prev {float:left;}
.ui-datepicker-next {float:right;}
.ui-datepicker-title{float:left;}
.ui-datepicker-calendar {font-size:14px;width:100%;text-align:right;}
.ui-datepicker-calendar td A{text-align:right;padding:0.2em 0.2em;display:block;text-decoration:none;}
.ui-state-default {}
.ui-datepicker select.ui-datepicker-month { }
.layout{
margin:5px 0;
height:310px;
width:100%;
overflow:auto;
}
p {
clear: both;
}
.cardBtnBgClass {background-color:#eee;line-height:30px;}
.button-o {margin-left:5px;}
.button-right {float:right;}
.labelNotNull {
	color:#760000;
	font-weight:bold;
}
div .itemNotNull {

}
select {border:1px #99BBE8 solid;}
.ui-helper-clearfix{zoom:0;}
#closeBtn {*margin-top:-29px;}
</style>
</head>
<body style="background: #FFFFFF;display: none;">
    <div id="ev-mainbody" style="width:90%">
        <div id="ev-lf" style="margin:2px 20px 2px 10px;float:left; width:20%">
            <div id="ev-lf-calendar"></div>
		    <!--div style="margin-top:5px;border:#99BBE8 1px solid;">
	            <br>
	            <font color="red">红色</font>：表示为未发布工作日志<br><br>
	            <font color="yellow">黄色</font>：表示为工作计划<br><br>
		    </div-->
            <!-- 
            <div style="margin-top:5px;border:#99BBE8 1px solid;">&nbsp; <input type="text" id="txtSearch" />&nbsp; <input type="button" value="查 找" id="btnSearch" /> &nbsp;
                <div id="orgUserContainer" class="layout"></div>
               </div>
             -->
        </div>
        <div id="ev-ct" style="margin:2px;width:75%;float:left; ">
            <div id="ev-events" ></div>
        </div>
    </div>
    <div id="ev-input-container"  style="width:800px;display:none;" >
        <div class="cardBtnBgClass" style="height:30px;">&nbsp;
            <a href="javascript:void(0)" class="button-o" id="saveReleaseBtn" plain="true" title=" 保存发布  " iconCls="icon-save" >保存发布</a>
            <a href="javascript:void(0)" class="button-o" id="saveBtn" plain="true" title=" 保存  " iconCls="icon-save">保存</a>
            <a href="javascript:void(0)" class="button-o" id="deleteBtn" plain="true" title=" 删除  " iconCls="icon-cancel">删除</a>
            <a href="javascript:void(0)" class="button-o button-right" id="closeBtn" plain="true" title=" 关闭  " iconCls="icon-cancel">关闭</a>
        </div>
        <form id="ev-form" action="#" method="post" onsubmit="return false;">
            <div style="margin:10px;">
                <p>
                    <label class="dataLabel labelNotNull">时间：</label>
                    <span>
                        <span>
                            <input id="CAL_START_DATE" style="width:75px;" onchange="schDateChange()" />
                            <select id="ev-st-sel"  ></select>
                            <input type="hidden" id="CAL_START_TIME" size="8"/>
                        </span>
                        <span>至</span>
                        <span>
                            <input id="CAL_END_DATE" style="width:75px;" onchange="schDateChange()"/>
                            <select id="ev-et-sel"></select>
                            <input type="hidden" id="CAL_END_TIME" size="8"/>
                        </span>
                    </span>
                </p>
                <!--p>
                    <label class="dataLabel">类型：</label>
                    <span>
                        <select id="CAL_TYPE" class="on-blur">
                            <option value="2" selected="selected">工作记录</option>
                            <option value="1">工作计划</option>
                        </select>
                    </span>
                </p-->
                <p>
                    <label class="dataLabel labelNotNull">标题：</label>
                    <!-- select  id="CAL_TITLE_CS" name="CAL_TITLE_CS" class="textPlane" ><option value="日常工作" selected="selected">日常工作</option><option value="开发">开发</option><option value="测试">测试</option><option value="文档">文档</option><option value="数据">数据</option><option value="客户支持">客户支持</option><option value="会谈/访谈">会谈/访谈</option><option value="会议">会议</option><option value="人员招聘">人员招聘</option><option value="外出办事">外出办事</option><option value="往返途中">往返途中</option><option value="学习">学习</option><option value="计划">计划</option></select--> 
                    <input type="text" id="CAL_TITLE" style="width:415px;" />
                </p>
                <p>
                    <label class="dataLabel">日程内容：</label>
                    <textarea id="CAL_CONTENT" style="height:150px;width:500px;" ></textarea>
                </p>
                <p style="clear:both;"></p>
                <!--p id="proj_item">
                    <label class="dataLabel">所在项目：</label>
                    <select id="PROJECT_CODE", name="PROJECT_CODE">
                          <option value="">-------------------------</option>
                    </select>
                </p--> 
                <p style="display: none">
                    <label class="dataLabel labelNotNull">项目阶段：</label>
                    <select id="PROJECT_JD" name="PROJECT_JD" class="textPlane" value=""><option value="">------</option><option value="1">销售阶段</option><option value="2">实施阶段</option><option value="3">运维阶段</option><option value="4">研发阶段</option><option value="5">其它</option></select>
                </p>
                <p style="display: none">
                    <label class="dataLabel labelNotNull">办公地点：</label>
                    <select id="CAL_ADDR" name="CAL_ADDR" class="textPlane" value="">
                        <option value="">------</option><option value="1">公司</option><option value="2">项目所在地</option><option value="3">家</option>
                    </select>
                </p>

                <!--p>
                    <label class="dataLabel  labelNotNull">工时类型：</label>
                    <select class="textPlane" id="CAL_OUTWORK" name="CAL_OUTWORK"><option value="1" selected >正常工时</option><option value="2">加班工时 </option><option value="3">请假工时</option></select>
                </p>
                <p>
                    <label class="dataLabel">工时(小时)：</label>
                           <input style="width:75px;" type="text" id="CAL_MANHOUR" name="CAL_MANHOUR" value="" readonly="readonly"/>
                 </p-->
                <!--<p>
                    <label class="dataLabel">等级：</label>
                    <select class="textPlane" id="SCH_LEVEL" name="SCH_LEVEL" disabled="disabled"><option value="20" selected>中 </option><option value="30">好</option><option value="10" >差</option></select>

                <p>
                    <label class="dataLabel">经理批注：</label>
                    <input type="text" id="CAL_PM_COMMENT" name="CAL_PM_COMMENT" size="80" readonly="readonly"/>
                </p>
                <p>
                    <label class="dataLabel">总监批注：</label>
                    <input type="text" id="CAL_PZ_COMMENT" name="CAL_PM_COMMENT" size="80" readonly="readonly"/>
                </p>
                <!--  <p>
                    <label class="dataLabel">审核状态：</label>
                    <select id="AUD_FLAG" name="AUD_FLAG" class="textPlane" value="" disabled="disabled">
                        <option value="">------</option><option value="1">总监已审批</option><option value="2" selected="selected">未审批</option><option value="3">经理已审批</option>
                    </select>
                          
                 </p>-->
            </div>
            <input id="CAL_FLAG" type="hidden" value=""/>
            <input id="CAL_ID" type="hidden"/>
            <input type="hidden" id="USER_CODE" value="<%=userCode%>" />
        </form>
    </div>
</body>
</html>