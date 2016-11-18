/*文件dhtmlxscheduler.js中修改的源码部分备注(可搜索”wangchen“查找)
0、弹出提示的坐标 1090行
1、删除图标的confirm改用自用 2106行
2、双击操作加入class“dhx_move_denied”，防止禁止拖动操作影响该操作 行2185
3、用户定制的视图头部扩展信息 行2577
4、禁掉页面滚动条，自动计算高度 行2669
5、视图中可以自定义到每个事件的样式及是否可拖动 3808、3850
*/
/*-------------------------------------------------------------------------------------初始配置-开始--------------------------------------------------------------------------------*/
scheduler.config.xml_date="%Y-%m-%d %H:%i"//{string} used to define format of data in data xml
scheduler.config.hour_size_px=66; //尺寸和背景图周末呢背景图相匹配才会获得好的效果//{numeric} height of 1 hour in pixels;
scheduler.config.time_step=5;//{numeric} minimal date size step in minutes;
scheduler.config.start_on_monday=false;//{boolean} if true week starts from Monday ( if false, week start from Sunday);
scheduler.config.first_hour=8;//{numeric} hour from which day and week scales start;
scheduler.config.last_hour=24;//{numeric} hour from which day and week scales end.
scheduler.config.limit_time_select=false;//{boolean} adjust time selects to first ahd last hour settings
scheduler.config.scroll_hour=16;//{numeric} hour which will be at top of screen, after view mode update
scheduler.config.mark_now=true;//是否在日、月视图启用当前时间标识(红虚线)
scheduler.config.readonly=false;//{boolean}只读功能 if set to true - events in scheduler can't be created|changed|deleted; 1)
scheduler.config.show_loading=true;//{boolean} 进度条（前台动态装载时）shows the progress during data loading, useful for dynamic loading mode
scheduler.config.drag_resize=true;//{boolean} 是否允许拖拽调整事件allows resizing events by dnd;
scheduler.config.drag_move=true;//{boolean} 是否允许拖拽移动事件allows moving events by dnd;
scheduler.config.drag_create=true;//{boolean}是否允许拖拽建事件 allows creating new events by dnd;
scheduler.config.dblclick_create=false;//{boolean}是否允许双击建事件 allows creating new events by double click;
scheduler.config.edit_on_create=false;//{boolean} 是否阻止自动触发弹窗（默认窗和客户自定窗都有效）shows form on new event creation;
scheduler.config.details_on_create=true;//{boolean} uses extended form on new event creation by drag or by dbl-click (option doesn't affect monthly view - where details form is the only way to change data);
scheduler.config.details_on_dblclick=true;//{boolean}是否开启双击事件进入弹窗而不快捷添加 uses extended form on event double-click (double-click on existing event).
scheduler.config.server_utc=false;//{boolean} if enabled component will convert server side dates from utc to local timezone, and backward during data sending to server;
scheduler.config.positive_closing=true;//{boolean} if outside click occurs during form edit, then form will be saved and event will be updated.
scheduler.config.update_render=true;//{boolean} if set to true - each time when item updated - all view will be refreshed instead of updated item only (it allows smooth size recalculation but much more cpu consuming) 
scheduler.config.multi_day=true;//{boolean} enables rendering of multi-day events in daily and weekly views
scheduler.config.full_day=true;//{boolean} if set to true - entry fields in 'Time period' section of details form are blocked, and time period is set to a full day from 00.00 the current cell date untill 00.00 next day)
scheduler.config.event_duration=5;//（单位：分钟，月视图）{integer} used to set the initial duration of the event)
scheduler.config.auto_end_date=true;//{boolean} if set to true - when you change start event time or date, the end event time and date will be changed automatically in order to make the event duration value 60 minutes)
scheduler.config.drag_lightbox=true;//{boolean} when set to true lightbox can be dragged by header, it is enabled by default
scheduler.config.preserve_scroll=true;
scheduler.xy.menu_width = 25;
scheduler.config.collision_limit = 1;//事件重复数
scheduler.config.prevent_cache = true;//禁用缓存
dhtmlx.message.defPosition="left";//top bottom left
//scheduler.blockTime({start_date: new Date(1900,0,0),end_date: new Date()});
/*-------------------------------------------------------------------------------------初始配置-结束--------------------------------------------------------------------------------*/
/*-------------------------------------------------------------------------------------事件监听-开始--------------------------------------------------------------------------------*/
/*------------------------------------------------1---------------------------------------------------------*/
//1.0监听事件将被拖拽 有返回
scheduler.attachEvent("onBeforeDrag", function (event_id, drag_mode, native_event_object){
	//dhtmlx.message("事件将被拖拽");
	var dragFlag = true;
	/*用户代码-开始*/
		if(RHTimeCenter.doDrag){
			dragFlag = RHTimeCenter.doDrag(scheduler.getEvent(event_id), drag_mode, scheduler._mode);
		}
	/*用户代码-结束*/
	return dragFlag;//返回false将阻止后续事件
});

//1.1监听初次插入事件（鼠标mousedown并move）无返回
scheduler.attachEvent("onEventCreated", function(event_id, native_event_object){
	//dhtmlx.message("插入了事件");
	/*用户代码-开始*/
		if(RHTimeCenter.doEventCreated){
			RHTimeCenter.doEventCreated(scheduler.getEvent(event_id));
		}
	/*用户代码-结束*/
	RHTimeCenter.resizeHeight();
});

//1.2监听事件超过允许的覆盖数 （可针对类型定制）有返回
scheduler.attachEvent("onEventCollision", function (ev, evs){
	//dhtmlx.message("你的日程超过了允许的覆盖数");
	var collisionFlag = false;
	/*用户代码-开始*/
		if(RHTimeCenter.doCollision){
			collisionFlag = RHTimeCenter.doCollision(ev, evs);
		}
	/*用户代码-结束*/
	return collisionFlag;//返回false将阻止后续事件
});

//1.3-监听事件将被改变（添加，修改（move，resize前））有返回
scheduler.attachEvent("onBeforeEventChanged", function(event_object, native_event, is_new, unmodified_event){
	//dhtmlx.message("事件将被改变");
	var modifyFlag = true;
	//防止在非月视图中意外添加跨天的事件（系统以前偶尔出现）
	if(
		is_new 
		&& RHTimeCenter.getCurrentMode() != "month" 
		&& (event_object.end_date - event_object.start_date)/60/60/1000 >= 24
	){
		return false;
	}
	/*用户代码-开始*/
		if(RHTimeCenter.doBeforeEventChanged){
			modifyFlag = RHTimeCenter.doBeforeEventChanged(event_object, is_new, unmodified_event);
		}
	/*用户代码-结束*/
	return modifyFlag;//返回false将阻止后续事件
});

//1.4-监听事件被改变 无返回 
scheduler.attachEvent("onEventChanged", function(event_id,event_object){
	//dhtmlx.message("事件被改变");
	if(RHTimeCenter.doEventChanged){//有用户自定义方法则不执行默认方法
		/*用户代码-开始*/
			RHTimeCenter.doEventChanged(event_object);
		/*用户代码-结束*/
	}else{
		RHTimeCenter._doEventChanged(event_object);
	}
	RHTimeCenter.resizeHeight();
});

//1.5-监听添见了新事件 无返回
scheduler.attachEvent("onEventAdded", function(event_id,event_object){
	//dhtmlx.message("添加了事件" + " 用户：" + event_object.S_USER);
	if(RHTimeCenter.showDetail){
	/*用户代码-开始*/
		//alert("onEventAdded" + scheduler._mode + " USER:" + event_object.S_USER);//此event_object.S_USER可用于unit视图移动某个事件时判断是否是新建的且不是新建的话是否可以改变创建人
		//不启用edit_on_create和details_on_create两个设定值而自己定制以实现拖完即弹窗口的功能
		RHTimeCenter.showDetail(event_object);
	/*用户代码-结束*/
	}else{
		RHTimeCenter._showDetail(event_object);
	}
	RHTimeCenter.resizeHeight();
});
/*------------------------------------------------1---------------------------------------------------------*/
/*------------------------------------------------2---------------------------------------------------------*/
//2.0监听事件被删除前 有返回
scheduler.attachEvent("onBeforeEventDelete", function(id,ev,currDetailObj){
	//dhtmlx.message("事件将被删除");
	if(!ev){
		var ev = scheduler.getEvent(id);
	}
	/*用户代码:判断点击垃圾桶图标前动作-开始*/
	if(!RHTimeCenter.doBeforeEventDelete(ev)){
		return false;
	}
	/*用户代码:判断点击垃圾桶图标前动作-结束*/
	RHTimeCenter._confirmDel(id,ev,currDetailObj);
	return false;//返回false将阻止后续事件
});

//2.1监听事件被删除 无返回
scheduler.attachEvent("onEventDeleted", function(event_id,event_object,delBean){
	//dhtmlx.message("事件已被删除");
	if(RHTimeCenter.doDelete){//有用户自定义方法则不执行默认方法
		/*用户代码-开始*/
			RHTimeCenter.doDeleteData(event_object,delBean);
		/*用户代码-结束*/
	}else{
		RHTimeCenter._doDeleteData(event_object,delBean);
	}
	RHTimeCenter.resizeHeight();
});
/*------------------------------------------------2---------------------------------------------------------*/
/*------------------------------------------------3---------------------------------------------------------*/
//3.0监听事件将被扩展处理 （可自定义用户弹出窗或其他动作）有返回
scheduler.attachEvent("onBeforeLightbox", function (event_id){
	//dhtmlx.message("事件将被扩展处理");
	if(RHTimeCenter.showDetail){
	/*用户代码-开始*/
		RHTimeCenter.showDetail(scheduler.getEvent(event_id));
	/*用户代码-结束*/
	}else{
		RHTimeCenter._showDetail(scheduler.getEvent(event_id));
	}
	return false;//返回false将阻止后续事件
});
/*------------------------------------------------3---------------------------------------------------------*/
/*------------------------------------------------4---------------------------------------------------------*/
//4.0监听视图将被改变（切换视图、迷你日历选择产生的改变视图动作会导致此事件的触发）有返回
scheduler.attachEvent("onBeforeViewChange", function (old_mode, old_date, mode, date){
	//dhtmlx.message("视图将被改变");
	var changeViewFlag = true;
	scheduler.config.event_duration=mode == 'month'?1440:5;
	/*用户代码-开始*/
		if(RHTimeCenter.doBeforeViewChange){//有用户自定义方法则不执行默认方法
			changeViewFlag = RHTimeCenter.doBeforeViewChange(old_mode, old_date, mode, date);
		}
	/*用户代码-结束*/
	return changeViewFlag;//返回false将阻止后续事件
});

//4.1监听视图被改变 （切换视图、迷你日历选择产生的改变视图动作会导致此事件的触发） 无返回
scheduler.attachEvent("onViewChange", function (mode , date){
	//dhtmlx.message("视图被改变");
	/*用户代码-开始*/
		if(RHTimeCenter.doViewChange){//有用户自定义方法则不执行默认方法
			changeViewFlag = RHTimeCenter.doViewChange(mode, date);
		}
	/*用户代码-结束*/
	RHTimeCenter.resizeHeight();
});
/*------------------------------------------------4---------------------------------------------------------*/
/*------------------------------------------------5---------------------------------------------------------*/
//5.0监听日历scheduler尺寸发生改变时 有返回
scheduler.attachEvent("onSchedulerResize", function(){
	//dhtmlx.message("日历scheduler尺寸发生改变");
	var resizeFlag = true;
	/*用户代码-开始*/
	/*用户代码-结束*/
	return resizeFlag;//返回false将阻止后续事件
});
/*------------------------------------------------5---------------------------------------------------------*/
/*------------------------------------------------6---------------------------------------------------------*/
//6.0监听事件被单击时 有返回
scheduler.attachEvent("onClick", function (event_id, native_event_object){
	//dhtmlx.message("事件被单击");
	var clickFlag = true;
	/*用户代码-开始*/
	/*用户代码-结束*/
	return clickFlag;//返回false将阻止后续事件
});

//6.1监听事件被双击时 有返回
scheduler.attachEvent("onDblClick", function (event_id, native_event_object){
	//dhtmlx.message("事件被双击");
	var dblClickFlag = true;
	/*用户代码-开始*/
	/*用户代码-结束*/
	return dblClickFlag;//返回false将阻止后续事件
});
/*------------------------------------------------6---------------------------------------------------------*/
/*------------------------------------------------7---------------------------------------------------------*/
//7.0监听页面快捷菜单将被调用时
scheduler.attachEvent("onContextMenu", function (event_id, native_event_object){
	//dhtmlx.message("快捷菜单将被调用");
	var contextMenuFlag = false;
	/*用户代码-开始*/
	/*用户代码-结束*/
	return contextMenuFlag;
});
/*------------------------------------------------7---------------------------------------------------------*/
/*------------------------------------------------8---------------------------------------------------------*/
//执行showEvent方法前 有返回
scheduler.attachEvent("onBeforeEventDisplay", function(event_id,view){
	//dhtmlx.message("执行showEvent方法前");
	var eventDisplayFlag = false;
	/*用户代码-开始*/
	/*用户代码-结束*/
	return eventDisplayFlag;
});
//执行showEvent方法后 无返回
scheduler.attachEvent("onAfterEventDisplay", function(event_id,view){
	//dhtmlx.message("执行showEvent方法后");
	/*用户代码-开始*/
	/*用户代码-结束*/
});
/*------------------------------------------------8---------------------------------------------------------*/
/*-------------------------------------------------------------------------------------事件监听-结束--------------------------------------------------------------------------------*/
var RHTimeCenter = new Object();
//当前时间前可否添加、修改、或移动事件
RHTimeCenter.before_can_modify = true;

//转成日期格式
RHTimeCenter.convertToDate = scheduler.date.date_to_str("%Y-%m-%d");

//转成时间格式
RHTimeCenter.convertToTime = scheduler.date.date_to_str("%H:%i");

//转成日期时间格式
RHTimeCenter.convertToDateTime = scheduler.date.date_to_str("%Y-%m-%d %H:%i");

//转成UTC格式
RHTimeCenter.convertToUtc = scheduler.date.str_to_date("%Y-%m-%d %H:%i");

//获取当前视图的开始时间
RHTimeCenter.getStartDateTime = function(){
	return RHTimeCenter.convertToDateTime(scheduler._min_date);
};

//获取当前视图的结束时间
RHTimeCenter.getEndDateTime = function(){
	return RHTimeCenter.convertToDateTime(scheduler._max_date);
};

//公共_获取当前用户code
RHTimeCenter.getUserCode = function(){
	return parent.System.getVar("@USER_CODE@");
};

//公共_获取当前用户name
RHTimeCenter.getUserName = function(){
	return parent.System.getVar("@USER_NAME@");
};

//公共_获取当前视图编码
RHTimeCenter.getCurrentMode = function(){
	return scheduler._mode;
};

//公共_切换并更新视图
RHTimeCenter.updateView = function(date, view){
	if(!view || view == ""){
		view = RHTimeCenter.getCurrentMode();
	}
	scheduler.setCurrentView(date, view);
}

//公共_获取当前查询用户
RHTimeCenter.getCurrentUsers = function(){
	var userObj = this.currentUsers || {};
	return userObj;
}

//公共_记录当前查询用户（格式{"code":编码,name"":名称}）
RHTimeCenter.setCurrentUsers = function(userObj){
	this.currentUsers = null;
	this.currentUsers = userObj || {};
}

//公共_获取事件
RHTimeCenter.getEvent = function(eventId){
	return scheduler.getEvent(eventId)
};

//公共_事件被改变后的处理
RHTimeCenter._doEventChanged = function(eventObj){
	if(eventObj[MB.pk]){
		var data = {
				"_PK_": eventObj[MB.pk]
		};
		data[MB.sdt] = RHTimeCenter.convertToDateTime(scheduler.getEventStartDate(eventObj.id));
		data[MB.edt] = RHTimeCenter.convertToDateTime(scheduler.getEventEndDate(eventObj.id));
		FireFly.cardModify(MB.serv, data);
	}
};

//公共_动态调整外层iframe高度
RHTimeCenter.resizeHeight = function(){
  	var mode = RHTimeCenter.getCurrentMode();
  	var outHeight = 0;
	var frameId = MB.serv + "-tabFrame";
	if(!mode || mode != "month"){//也包括自定义视图
		outHeight = RHTimeCenter._computeDayAndWeekScheduler();//alert(mode + "---" + outHeight);
		jQuery(".dhx_cal_data").height(jQuery(".dhx_scale_holder").height());
		jQuery("#right_scheduler").height(outHeight);
		RHTimeCenter._setFrameHei(outHeight + 80);//80是顶部bar+底部悬空高度
	}
	if(mode == "month"){
		outHeight = RHTimeCenter._computeMonthScheduler();//alert(mode + "---" + outHeight);
		jQuery(".dhx_cal_data").height(jQuery("#right_scheduler table").height());
		jQuery("#right_scheduler").height(outHeight);
		RHTimeCenter._setFrameHei(outHeight + 80);
	}
};

//公共_计算非月视图页外层
RHTimeCenter._computeDayAndWeekScheduler = function(){
	var _navH = jQuery(".dhx_cal_navline").height();
	var _headH = jQuery(".dhx_cal_header").height();
	var _mdH = jQuery(".dhx_multi_day").height();	
	var _shH = jQuery(".dhx_scale_holder").height();
	return _navH + _headH + _mdH + _shH + 30; //最后是多留的空白（防止日历下方过于贴边）
};

//公共_计算月视图外侧高度
RHTimeCenter._computeMonthScheduler = function(){
	var _navH = jQuery(".dhx_cal_navline").height();
	var _headH = jQuery(".dhx_cal_header").height();		
	var _tabH = jQuery("#right_scheduler table").height();
	return _navH + _headH + _tabH + 30;
};

//公共_调整父窗口iframe、div高度
RHTimeCenter._setFrameHei = function(replaceHei) {
	var docHei = document.documentElement.scrollHeight;
	if (replaceHei) {
		docHei = replaceHei;
	}
	
	var id = MB.serv + "-show-do";
	//if (parent.document.getElementById(id)) {
		//parent.document.getElementById(id).style.height = docHei + "px";
	//}
	var iframeObj = parent.jQuery("iframe[name^='" + id + "']");
	
	if(iframeObj.length > 0){
		iframeObj.height(docHei);
	}
	//parent.jQuery("#" + id).parent().height("100%");
	if(iframeObj.length > 0){
		jQuery(iframeObj[0]).parent().height("100%");
	}
};

//公共_定制视图头部扩展信息
RHTimeCenter._setHeaderExtInfo = function(html){
	scheduler.headerExtInfo = html;
};

////内部_决定每个事件是否只读(此方法目前不启用，而改在beforedrag中添加逻辑代码)
RHTimeCenter._checkEventReadonly = function(event, schedulerHandler){
	if(RHTimeCenter.doReadonly){
		return RHTimeCenter.doReadonly(event, schedulerHandler);
	}
	return false;
};

//公共_周末样式
scheduler.templates.week_date_class=function(date,today){
	if(scheduler._mode == "unit"){
		date = scheduler._date;//若是自定义视图取当天
	}
	if (date.getDay()==0 || date.getDay()==6){
		return "weekday";
	}
	return "";
}

//按类型区分事件css
scheduler.templates.event_class=function(start, end, event){
	if(event.type) {
		return "event_"+event.type;
	}
	var curr_date = new Date();
	return "event_default";
}

//勿动（修改源码）
scheduler.renderEvent = function(container, ev, w, h, bg_color, color, contentA, contentB, bottom, schedulerHandler) {
	var topRadius = "";
	if (bottom) {
		bg_color = "background-color:#1796b0;"
		contentA = '<!--<div style="' + bg_color + '" class="dhx_menu_head"></div>-->';
		contentB = '<div class="dhx_menu_icon icon_details" style="' + bg_color + '" title="详细"></div>';
		if(!RHTimeCenter.doBeforeShowSelectMenu || RHTimeCenter.doBeforeShowSelectMenu(ev)){
			contentB += '<div class="dhx_menu_icon icon_delete" style="' + bg_color + '" title="删除"></div>';
		}
		h = 66;
		topRadius = ";border-top-right-radius: 4px;border-top-left-radius: 4px;"
	}
	var isDenied = false;
	if(RHTimeCenter && RHTimeCenter._checkEventReadonly){
		isDenied = RHTimeCenter._checkEventReadonly(ev, schedulerHandler);
	}
	var inner_html = '<div class="';
	//dhx_event_move or dhx_move_denied
	if(isDenied){inner_html +='dhx_move_denied';}else{inner_html +='dhx_event_move';}
	inner_html +=' dhx_header" style=" width:' + (w - 6) + 'px;' + bg_color + '" >&nbsp;</div>';
	inner_html += '<div class="';
	//dhx_event_move or dhx_move_denied
	if(isDenied){inner_html +='dhx_move_denied';}else{inner_html +='dhx_event_move';}
	if(bottom){
		inner_html +=' dhx_title" style="' + bg_color + '' + color + ';display:none;">' + contentA + '</div>';
	}else{
		inner_html +=' dhx_title" style="' + bg_color + '' + color + '">' + contentA + '</div>';
	}
	inner_html += '<div class="dhx_body" style=" width:' + (w - (schedulerHandler._quirks ? 4 : 14)) + 'px; height:' + (h - (schedulerHandler._quirks ? 20 : 30) + 1) + 'px;' + bg_color + '' + color + topRadius + '">' + contentB + '</div>'; // +2 css specific, moved from render_event
	//dhx_event_resize or dhx_resize_denied
	if(isDenied){var footer_class = "dhx_resize_denied dhx_footer";}else{var footer_class = "dhx_event_resize dhx_footer";}
	if (bottom) {
		footer_class = "dhx_resize_denied " + footer_class;
	}
	inner_html += '<div class="' + footer_class + '" style=" width:' + (w - 8) + 'px;' + (bottom ? ' margin-top:-1px;' : '') + '' + bg_color + '' + color + '" ></div>';
	container.innerHTML = inner_html;
	return true;
};

//用户定制月事件渲染效果，勿动（修改源码）
scheduler.beforeRenderMonthEvent = function(ev, schedulerHandler){
	var isDenied = false;
	if(RHTimeCenter && RHTimeCenter._checkEventReadonly){
		isDenied = RHTimeCenter._checkEventReadonly(ev, schedulerHandler);
	}
	return isDenied;
};
/*----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
//解析事件数据并渲染
RHTimeCenter.addDatas = function(data){
	if(!data){
		return {};
	}
	scheduler.parse(data, "json");
	return scheduler.getEvents();
};
RHTimeCenter.updateDatas = function(data){
	RHTimeCenter.clearDatasWithoutRender();
	scheduler.parse(data, "json");
	return scheduler.getEvents();
};
RHTimeCenter.clearDatasAndRender = function(){
	if(scheduler.getEvents.length > 0){
		scheduler.clearAll();
	}
};
RHTimeCenter.clearDatasWithoutRender = function(){
	if(scheduler.getEvents.length > 0){
		scheduler._events = {};
	}
};
RHTimeCenter.loadDatas = function(options, isRender){
	//dhtmlx.message("视图切换");
	//options{servId, act, params, isRender, startDateTime, endDateTime}
	var opts = {
		'servId': MB.serv,
		'act': "getDatas",
		'startDateTime': RHTimeCenter.convertToDateTime(scheduler._min_date),
		'endDateTime': RHTimeCenter.convertToDateTime(scheduler._max_date),
		'queryUserStr': RHTimeCenter.getCurrentUsers().code	//待查用户串
	};
	jQuery.extend(opts, options);
	var result = FireFly.doAct(opts.servId, opts.act, opts);
	isRender = isRender || true;
	if(result != null && result._DATA_){
		if(isRender){
			RHTimeCenter.updateDatas(result._DATA_);
		}
		return result._DATA_;
	}
	return {};
};
RHTimeCenter.updateEvent = function(id){
	scheduler.updateEvent(id);
};
RHTimeCenter._showDetail = function(eventObj){
	//dhtmlx.message("显示详细事件");
	if(!eventObj.detailType){
		//打开迷你卡片页面
		var servId = eventObj.SERV_ID || eventObj.SERV_SRC_ID || MB.serv;
		var options = {
			"sId": servId,
			"widHeiArray": [jQuery("body").width() - MB.widthBlank],
			"xyArray": [MB.widthBlank / 2,Math.max(parent.document.documentElement.scrollTop, parent.document.body.scrollTop) + 30],
			"sdt": scheduler.getEventStartDate(eventObj.id),
			"edt": scheduler.getEventEndDate(eventObj.id)
		};
		var opts = {"act":UIConst.ACT_CARD_ADD,"parHandler":null}
		if(eventObj[MB.pk] && eventObj[MB.pk]!=""){
			opts[UIConst.PK_KEY] = eventObj[MB.pk];
			opts["act"] = UIConst.ACT_CARD_MODIFY;
		}
		//保存按钮对象
		var saveBtn = {
			ACT_CODE: "saveEve",
			ACT_CSS: "save",
			ACT_EXPRESSION: "",
			ACT_EXPRESSION_FLAG: "",
			ACT_MEMO: "RHTimeCenter.currDetailObj.getBtn('save').click();",
			ACT_NAME: "保存日程",
			ACT_ORDER: "0",
			ACT_TYPE: "2",
			ACT_WS_FLAG: "2",
			ACT_WS_RESULT: "",
			S_FLAG: "1"
		}
		//删除按钮对象
		var deleteBtn = {
			ACT_CODE: "deleteEve",
			ACT_CSS: "delete",
			ACT_EXPRESSION: "",
			ACT_EXPRESSION_FLAG: "",
			ACT_MEMO: "scheduler.callEvent('onBeforeEventDelete', ['" + eventObj.id + "', null, RHTimeCenter.currDetailObj]);",
			ACT_NAME: "删除日程",
			ACT_ORDER: "0",
			ACT_TYPE: "2",
			ACT_WS_FLAG: "2",
			ACT_WS_RESULT: "",
			S_FLAG: "1"
		}
		//初始化卡片引擎
		var cardObj = new rh.vi.cardView(jQuery.extend(opts,options));
		delete cardObj._data.BTNS.saveEve;// 将缓存中取得的对象属性归零
		delete cardObj._data.BTNS.deleteEve;// 将缓存中取得的对象属性归零

		//保存修改权限
		if(RHTimeCenter.checkModifyAcl(eventObj)){
			cardObj._data.BTNS.saveEve = saveBtn;
		}
		
		//删除权限
		if(RHTimeCenter.checkDelAcl(eventObj)){
			cardObj._data.BTNS.deleteEve = deleteBtn;
		}
		
		//展示卡片
		cardObj.show();
		cardObj.refreshA.hide();
		RHTimeCenter.currDetailObj = cardObj;//记录当前打开的详细页容器对象
		if(cardObj.getItem("CAL_START_TIME")){
			cardObj.getItem("CAL_START_TIME").setValue(RHTimeCenter.convertToDateTime(scheduler.getEventStartDate(eventObj.id)));
		}
		if(cardObj.getItem("CAL_END_TIME")){
			cardObj.getItem("CAL_END_TIME").setValue(RHTimeCenter.convertToDateTime(scheduler.getEventEndDate(eventObj.id)));
		}
		//添加的情形处理
		if(!eventObj[MB.pk]){
			//追加绑定返回按钮
			cardObj.backA.bind("mousedown",{"handler":cardObj},function(event){
				//未保存则删除前台的事件
				if(event.data.handler.getPKCode() == ""){
					RHTimeCenter._doDeleteEve(eventObj.id, eventObj);
				}
			});
		}
		//保存之后
		if(RHTimeCenter.afterSave){
			cardObj.afterSave = RHTimeCenter.afterSave(eventObj);
		}else{
			cardObj.afterSave = RHTimeCenter._afterSave(eventObj);
		}
	}else if(eventObj.detailType == "tab"){
		var params = {
			"links":{},//需要传到后台的参数放到里面
			"handler": null//当前文档句柄供新页面回调用
		};
		var options = {
			"url": MB.serv + ".card.do",
			"tTitle":"无标题",
			"params":params,
			"menuFlag":3
		};
		Tab.open(options);
	}
};
//保存之后
RHTimeCenter._afterSave = function(eventObj){
	var saveFunc = function(resultData){
		eventObj.text = resultData.text;
		eventObj.CAL_ID = resultData.CAL_ID;
		eventObj.CAL_TITLE = resultData.CAL_TITLE;
		eventObj.CAL_START_DATE = resultData.CAL_START_DATE;
		eventObj.CAL_START_TIME = resultData.CAL_START_TIME;
		eventObj.CAL_END_DATE = resultData.CAL_END_DATE;
		eventObj.CAL_END_TIME = resultData.CAL_END_TIME;
		eventObj.CAL_TYPE = resultData.CAL_TYPE;
		eventObj.CAL_CONTENT = resultData.CAL_CONTENT;
		eventObj.start_date = RHTimeCenter.convertToDateTime(resultData.CAL_START_TIME);
		eventObj.end_date = RHTimeCenter.convertToDateTime(resultData.CAL_END_TIME);
		eventObj.type = resultData.CAL_TYPE;
		eventObj.S_USER = resultData.S_USER;
		eventObj.S_USER__NAME = resultData.S_USER__NAME;
		RHTimeCenter.updateEvent(eventObj.id);
	};
	return saveFunc;
};
//公共_删除提示
RHTimeCenter._confirmDel = function(id, ev, currDetailObj){
	var _delEvent = function(bool){
		if(bool){
			var delBean = {};
			if(ev[MB.pk]){
				delBean.flag = false;
				delBean.currDetailObj = currDetailObj;
				scheduler.callEvent("onEventDeleted", [id, ev, delBean]);//后端
			}else{
				delBean.flag = true;
				if(currDetailObj.backA){
					currDetailObj.backClick();
				}
				if(RHTimeCenter.currDetailObj){
					delete RHTimeCenter.currDetailObj;//归零
				}
			}
			if(delBean.flag){
				RHTimeCenter._doDeleteEve(id, ev);//前端
			}
		}
	};
	dhtmlx.message({
		type:"confirm-warning",
		text:"是否确实要删除该日程？",
		title:"提醒!",
		ok:"是",
		cancel:"否",
		callback: _delEvent
	});
};
//前端
RHTimeCenter._doDeleteEve = function(id,ev){
	//dhtmlx.message("删除event");
	delete scheduler._events[id];
	scheduler.unselect(id);
	scheduler.event_updated(ev);
};
//后端
RHTimeCenter._doDeleteData = function(ev,delBean){
	//dhtmlx.message("删除数据");
	var data = {};
	data[UIConst.PK_KEY] =  ev[MB.pk];
	var resultData = FireFly.doAct(MB.serv, UIConst.ACT_DELETE, data, true, false);
	if(resultData[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) >= 0){
		delBean.flag = true;
		if(delBean.currDetailObj){
			delBean.currDetailObj.backClick();
			delete RHTimeCenter.currDetailObj;//归零
		}
	}
};
//判断事件的可更改
RHTimeCenter.checkModifyAcl = function(event){
	var flag = event.can_Modify;
	if(flag && flag == "false"){
		return false;
	}
	return true;
};
//判断事件的可删除性
RHTimeCenter.checkDelAcl = function(event){
	var flag = event.can_Del;
	if(flag && flag == "false"){
		return false;
	}
	return true;
};
/*获取条件事件
RHTimeCenter.getEvent = function(conditionStr){
	/*	
	CAL_ID: "1hSlBgwF5fXVmSSkcTAt0O"
	CAL_TITLE: "测试001"
	CAL_TYPE: "MEETING"
	END_TIME: "2013-01-29 00:00"
	START_TIME: "2013-01-27 00:00"
	S_USER: "wangchen"
	_PK_: "1hSlBgwF5fXVmSSkcTAt0O"
	_ROWNUM_: "0"
	_eday: 2
	_first_chunk: true
	_last_chunk: true
	_length: 2
	_sday: 0
	_sorder: 1
	_sweek: 0
	_timed: false
	end_date: Tue Jan 29 2013 00:00:00 GMT+0800 (中国标准时间)
	id: 1359615319894
	start_date: Sun Jan 27 2013 00:00:00 GMT+0800 (中国标准时间)
	text: "本人:测试001"
	type: "MEETING"
	*//*
	//例：conditionStr = "ev._timed && ev._eday < 5"
	conditionStr = conditionStr || "1==2";
	var events = scheduler.getEvents();
	for(var i=0;i<events.length;i++){
		var ev = events[i];
		if(eval(conditionStr)){
			events.splice(i,1);
		}
	}
	return events;
};
*/
/*添加事件并渲染
RHTimeCenter.addEvent = function(startDateTime, endDateTime, text, id, otherDataBean){
	scheduler.addEvent(this.convertToUtc(startDateTime), this.convertToUtc(endDateTime), id, otherDataBean);
};*/