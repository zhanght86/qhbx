<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="com.rh.core.serv.ServMgr" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	Bean typeBean = new Bean();
	Bean resultBean = ServMgr.act("SY_COMM_CAL_TYPE", ServMgr.ACT_FINDS, typeBean);
	List<Bean> typeList = resultBean.getList("_DATA_");
	
	String initMode = "week";
	if(!request.getParameter("initMode").isEmpty()){
	    initMode = request.getParameter("initMode");
	}
	
	String initDate = "";
	if(!request.getParameter("initDate").isEmpty()){
	    initDate = request.getParameter("initDate");
	}
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title>日程提醒</title>
	<%@ include file= "/sy/base/view/inHeader.jsp" %>
	<!-- 根据小卡片页服务类型可选资源 -start- -->
		<script type="text/javascript" src="/sy/base/frame/coms/DatePicker/WdatePicker.js"></script>
	    <script type="text/javascript" src="/sy/base/frame/coms/file/swfupload.js"></script>
		<script type="text/javascript" src="/sy/base/frame/coms/file/js/swfupload.queue.js"></script>
		<script type="text/javascript" src="/sy/base/frame/coms/file/js/fileprogress.js"></script>
		<script type="text/javascript" src="/sy/base/frame/coms/file/js/handlers.js"></script>
		<script type="text/javascript" src="/sy/base/frame/coms/comment/comment.js"></script>
		<script type="text/javascript" src="/sy/base/frame/coms/comment/jquery.timeago_zh.js"></script>
		<link rel="stylesheet" type="text/css" href="/sy/base/frame/coms/comment/comment.css" />
		<link rel="stylesheet" href="/sy/theme/default/common.css" type="text/css" charset="utf-8">
	<!-- 根据小卡片页服务类型可选资源 -end- -->
	<!-- scheduler插件加载资源  -start- -->
		<script src="/sy/comm/calendar/dhtmlx/dhtmlxscheduler.js" type="text/javascript" charset="utf-8"></script>
		<!--（选用）使用terrace主题后需特别加载此js，它会在默认主题的基础上用js动态调整其中一些元素的位置、尺寸-->
		<script src="/sy/comm/calendar/dhtmlx/dhtmlxscheduler_dhx_terrace.js" type="text/javascript" charset="utf-8"></script>		
		<link rel="stylesheet" href="/sy/comm/calendar/dhtmlx/dhtmlxscheduler_dhx_terrace.css" type="text/css" charset="utf-8">
		<script src="/sy/comm/calendar/dhtmlx/locale/locale_cn.js" type="text/javascript" charset="utf-8"></script>
		<!--<script src="/sy/comm/calendar/dhtmlx/ext/dhtmlxscheduler_expand.js" type="text/javascript" charset="utf-8"></script>-->
		<script src="/sy/comm/calendar/dhtmlx/ext/dhtmlxscheduler_minical.js" type="text/javascript" charset="utf-8"></script>
		<script src="/sy/comm/calendar/dhtmlx/ext/dhtmlxscheduler_collision.js"></script>
		<script src="/sy/comm/calendar/dhtmlx/ext/dhtmlxscheduler_limit.js"></script>
		<script src="/sy/comm/calendar/dhtmlx/ext/dhtmlxscheduler_units.js" type="text/javascript" charset="utf-8"></script>
		<!--ruaho基础封装-->
		<script src="/sy/comm/calendar/dhtmlx/custom/base.js" type="text/javascript" charset="utf-8"></script>
		<link rel="stylesheet" href="/sy/comm/calendar/dhtmlx/custom/base.css" type="text/css" charset="utf-8">
	<!-- scheduler插件加载资源  -end- -->
</head>
<body>
	<div id="outer">
		<div class="conHeader" style="padding-top:0px;z-index:0;">
			<div class="conHeaderTitle rh-right-radius-head">
				<span class="conHeaderTitle-span">日程提醒</span>
				<span class="conHeanderTitle-refresh" id="calendar_back">返回</span>
				<span class="conHeanderTitle-refresh" id="calendar_refresh">刷新</span>
				<div class="rh-norQuery">
				</div>
			</div>
		</div>
		<div id="left_container">
			<div id="mini_calendar"></div>
		</div>
		<div id="right_scheduler" class="dhx_cal_container">
			<div class="dhx_cal_navline">
			    <div class="dhx_cal_prev_button">&nbsp;</div>
			    <div class="dhx_cal_next_button">&nbsp;</div>
			    <div class="dhx_cal_today_button"></div>
			    <div class="dhx_cal_date"></div>
			    <div class="dhx_cal_tab" name="day_tab" style="right:204px;"></div>
			    <div class="dhx_cal_tab" name="week_tab" style="right:140px;"></div>
			    <div class="dhx_cal_tab" name="month_tab" style="right:76px;"></div>
			</div>
			<div class="dhx_cal_header"></div>
			<div class="dhx_cal_data"></div>    
		</div>
	</div>
</body>
<!-------------------------------------------------------------------------功能自定义 开始----------------------------------------------------------------------------------------->
<style type="text/css" media="screen">
	    /*日程_类型样式*/
	    <%for(Bean type: typeList){
	    	out.println(".dhx_cal_event.event_" + type.getStr("TYPE_CODE") +" div{background-color: #" + type.getStr("TYPE_COLOR")+" ;color: black !important;}");
	    	out.println(".dhx_cal_event_line.event_"+type.getStr("TYPE_CODE")+"{background-color: #"+type.getStr("TYPE_COLOR")+" !important;color: black !important;}");
	    	out.println(".dhx_cal_event_clear.event_"+type.getStr("TYPE_CODE")+"{color: #"+type.getStr("TYPE_COLOR")+" !important;}");
	    }%>	     	
</style>
<script>
	var MB = {
		"serv": "SY_COMM_CAL",
		"pk": "CAL_ID",
		"sdt": "CAL_START_TIME",
		"edt": "CAL_END_TIME",
		"detail": "miniCard", //miniCard、newTab、
		"widthBlank":100 //弹出卡片需要与两边留的空白宽度和
	}
</script>
<script src="/sy/comm/calendar/dhtmlx/custom/calendar.js" type="text/javascript" charset="utf-8"></script>
<link rel="stylesheet" href="/sy/comm/calendar/dhtmlx/custom/calendar.css" type="text/css" charset="utf-8">
<!-------------------------------------------------------------------------功能自定义 结束----------------------------------------------------------------------------------------->
<script>
	//刷新按钮
	jQuery('#calendar_refresh').bind("click",function(event){
		window.location.reload();
	});
	//返回按钮
	jQuery('#calendar_back').bind("click",function(event){
		if (GLOBAL.getFrameId()) {
			Tab.close();
		}
	});
	/*-------------------------------------------------------------------公共-初始化-开始--------------------------------------------------------------------------*/
	/*公共_覆盖时间区域的样式和是否阻止其上的操作-开始*/
		var opts = {
			start_date: new Date(1900,0,0),
			end_date: new Date(),
			css: "block"
		}
		if(RHTimeCenter.before_can_modify == false){
			opts.type = "dhx_time_block" ;//the hardcoded value
		}
		scheduler.addMarkedTimespan(opts);
	/*公共_覆盖时间区域的样式和是否阻止其上的操作-结束*/
	/*公共_初始化页面高度-开始*/
		if(!parent.System.getTempParams(MB.serv + "_Hei") || !parent.System.getTempParams(MB.serv + "_Hei").initHeight){
			//parent.System.setTempParams(MB.serv + "_Hei", {"initHeight":parent.jQuery("#" + MB.serv + "-show-do-tabFrame").height()});
			parent.System.setTempParams(MB.serv + "_Hei", {"initHeight":parent.jQuery("iframe[name^='SY_COMM_CAL-show-do']").height()});
		}
		var initHeight = parent.System.getTempParams(MB.serv + "_Hei").initHeight;
		jQuery("#right_scheduler").height(initHeight);
	/*公共_初始化页面高度-结束*/
	//公共_初始化视图模式
	///RHTimeCenter.currentMode = "week";
	RHTimeCenter.setCurrentUsers({"code":RHTimeCenter.getUserCode(),"name":RHTimeCenter.getUserName()});
	
	//公共_开始初始化scheduler
	var initDate = "<%=initDate%>";
	if(initDate == ""){
		initDate = null;
	} else {
		//initDate = new Date(initDate);
		initDate = new Date(Date.parse(initDate.replace(/-/g,"/")));
	}
	scheduler.init('right_scheduler',initDate,"<%=initMode%>");
	/*公共_迷你日历控件挂载-开始*/
		var miniCalendar = scheduler.renderCalendar({
			container:"mini_calendar", 
			navigation:true,
			handler:function(date){
				scheduler.setCurrentView(date, RHTimeCenter.getCurrentMode());
				//scheduler.destroyCalendar();
			}
		});
		scheduler.linkCalendar(miniCalendar);
	/*公共_迷你日历控件挂载-结束*/
	//公共_初始化结束标识
	scheduler.initEnd = true;
	//公共_刷新视图、初始加载数据
	RHTimeCenter.updateView(null, RHTimeCenter.getCurrentMode());
	//公共_初始设定头部信息
	RHTimeCenter._setHeaderExtInfo("  " + RHTimeCenter.getCurrentUsers().name);
	/*-------------------------------------------------------------------公共-初始化-结束--------------------------------------------------------------------------*/
</script>
</html>