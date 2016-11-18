//scheduler.attachEvent("onClick", function (event_id, native_event_object){
//	alert("onClick");
//});
//scheduler.attachEvent("onDblClick", function (event_id, native_event_object){
//	alert("onDblClick");
//});
//scheduler.attachEvent("onBeforeDrag", function (event_id, mode, native_event_object){
//	alert("onBeforeDrag");
//    return true;
//});
//scheduler.attachEvent("onContextMenu", function (event_id, native_event_object){
//	alert("onContextMenu");
//});
scheduler.attachEvent("onMouseMove", function (event_id, native_event_object){
	//alert("onMouseMove");
});
scheduler.attachEvent("onEventChanged", function(event_id,event_object){
	//alert("onEventChanged");
	Calendar.afterEventChanged(event_object);
	resizeHeight();
	//top bottom left
	dhtmlx.message.defPosition="top";
	dhtmlx.message(
		"你改变了 一个日程事件<br/><b>"+event_object.text+"</b>"
	);
	//type:alert alert-warning alert-error confirm confirm-warning confirm-error notice
	/*
	dhtmlx.message({ 
	    type:"confirm-warning", 
	    text:"Are you sure you want to do it?",
	    lifetime:1000,
    	callback: function() {}
	});
	dhtmlx.confirm({
	    title:"Confirm",
	    text:"Continue?"
	});
	dhtmlx.alert({
	    title:"Alert",
	    type:"alert-error",
	    text:"You can't do this"
	});
	dhtmlx.alert("some text");
	dhtmlx.alert({
	      type:"alert-error",
	      text:"some text",
	      title:"Error!",
	      ok:"Yes"
	      cancel：""
	});
	*/
});
scheduler.attachEvent("onBeforeEventDelete", function(event_id,event_object){
	//alert("onBeforeEventDelete");
	//删除图标或按钮触发的删除操作
	Calendar.deleteData(event_object, false);
	return false;
});
scheduler.attachEvent("onEventDeleted", function(event_id){
	//alert("onEventDeleted");
	resizeHeight();
});
scheduler.attachEvent("onBeforeViewChange", function (old_mode, old_date, mode , date){
	//alert("onBeforeViewChange");
	//切换视图前清空数据
	Calendar.clearEventData();
	if(mode == 'unit'){
		Calendar.setCurrentUsers({"code":Calendar.getUserCode(),"name":Calendar.getUserName()});
	}
	Calendar._setHeaderExtInfo("  " + Calendar.getCurrentUsers().name);
	return true;
});
scheduler.attachEvent("onViewChange", function (mode , date){
	//alert("onViewChange");
	Calendar.currentMode = this._mode;
	//切换视图后重新装载数据
	var data = {
		'isSelf': 'false',
		'queryUserStr': Calendar.getCurrentUsers().code,
		'servId': MapBean.extServerId
	};
	if(scheduler.initEnd){
		scheduler.parse(Calendar.loadEventData(data), "json");
	}
	Calendar.resizeHeight();
});
//scheduler.attachEvent("onLightbox", function (event_id){
//	alert("onLightbox");
//});
//scheduler.attachEvent("onAfterLightbox", function (){
//	alert("onAfterLightbox");
//});
//scheduler.attachEvent("onLightboxButton ", function (id, node, e){
//	alert("onLightboxButton");
//});
//scheduler.attachEvent("onEventSave",function(id,data,is_new_event){
//	alert("onEventSave");
//	if (!data.text) {
//		alert("Text must not be empty");
//		return false;
//	}
//	if (!data.text.length<20) {
//		alert("Text too small");
//		return false;
//	}
//	return true;
//});
//scheduler.attachEvent("onEventCancel", function(event_id, is_new_event){
//	alert("onEventCancel");
//});
//scheduler.attachEvent("onBeforeEventDisplay", function(event_id,view){
//	alert("onBeforeEventDisplay");
//});
//shows the event with id=someId in the Week view
//scheduler.showEvent(someId,"week");
// 
//shows the event with id=someId in the currently active view
//scheduler.showEvent(someId);
scheduler.attachEvent("onAfterEventDisplay", function(event_id,view){
	//程序不走
	//alert("onAfterEventDisplay");
});
//scheduler.attachEvent("onEventCreated", function(event_id,event_object){
	//alert("onEventCreated");
//});
//scheduler.attachEvent("onBeforeEventChanged", function(event_object, native_event, is_new, unmodified_event){
//	alert("onBeforeEventChanged");
//    return true;
//});
//scheduler.attachEvent("onEventLoading", function(event_object){
//	alert("onEventLoading");
//});
//scheduler.attachEvent("onTemplatesReady", function(){
//	alert("onTemplatesReady");
//});
//scheduler.attachEvent("onEventIdChange", function(old_event_id,new_event_id){
//	alert("onEventIdChange");
//});
//scheduler.attachEvent("onSchedulerResize", function(){
//	alert("onSchedulerResize");
//	return false;
//});
//scheduler.attachEvent("onOptionsLoad", function (){
//	alert("onOptionsLoad");
//});
//scheduler.attachEvent("onLimitViolation", function (){
//	alert("onLimitViolation");
//});
scheduler.attachEvent("onEventCollision", function (ev, evs){
	alert("你的日程覆盖了另一个日程（可针对类型定制）");
	return true;
});
//scheduler.attachEvent("onXScaleClick", function (index, value,e){
//	alert("onXScaleClick");
//});
//scheduler.attachEvent("onXScaleDblClick", function (index, value, e){
//	alert("onXScaleDblClick");
//});
//scheduler.attachEvent("onYScaleClick", function (index, value, e){
//	alert("onYScaleClick");
//});
//scheduler.attachEvent("onYScaleDblClick", function (index, value, e){
//	alert("onYScaleDblClick");
//});
//scheduler.attachEvent("onCellClick", function (x_index, y_index, x_value, y_value, e){
//	alert("onCellClick");
//});
//scheduler.attachEvent("onCellDblClick", function (x_index, y_index, x_value, y_value, e){
//	alert("onCellDblClick");
//});
//scheduler.attachEvent("onLocationError", function (id){
//    alert("Bad location");
//});
//scheduler.attachEvent("onExternalDragIn", function (id, source){
//	alert("onExternalDragIn");
//    scheduler.getEvent(id).text = source.innerHTML;
//    return true;
//});
//用户定制事件渲染效果
scheduler.renderEvent = function(container, ev, w, h, bg_color, color, contentA, contentB, bottom, schedulerHandler) {
	if (bottom) {
		bg_color = "background-color:#1796b0;"
		contentA = '<div style="' + bg_color + '" class="dhx_menu_head"></div>';
		contentB = '<div class="dhx_menu_icon icon_details" style="' + bg_color + '" title="详细"></div>';
		contentB += '<div class="dhx_menu_icon icon_delete" style="' + bg_color + '" title="删除"></div>';
		h = 60;
	}
	var isDenied = false;
	isDenied = _checkEventReadonly(ev, schedulerHandler);
	var inner_html = '<div class="';
	//dhx_event_move or dhx_move_denied
	if(isDenied){inner_html +='dhx_move_denied';}else{inner_html +='dhx_event_move';}
	inner_html +=' dhx_header" style=" width:' + (w - 6) + 'px;' + bg_color + '" >&nbsp;</div>';
	inner_html += '<div class="';
	//dhx_event_move or dhx_move_denied
	if(isDenied){inner_html +='dhx_move_denied';}else{inner_html +='dhx_event_move';}
	inner_html +=' dhx_title" style="' + bg_color + '' + color + '">' + contentA + '</div>';
	inner_html += '<div class="dhx_body" style=" width:' + (w - (schedulerHandler._quirks ? 4 : 14)) + 'px; height:' + (h - (schedulerHandler._quirks ? 20 : 30) + 1) + 'px;' + bg_color + '' + color + '">' + contentB + '</div>'; // +2 css specific, moved from render_event
	//dhx_event_resize or dhx_resize_denied
	if(isDenied){var footer_class = "dhx_resize_denied dhx_footer";}else{var footer_class = "dhx_event_resize dhx_footer";}
	if (bottom) {
		footer_class = "dhx_resize_denied " + footer_class;
	}
	inner_html += '<div class="' + footer_class + '" style=" width:' + (w - 8) + 'px;' + (bottom ? ' margin-top:-1px;' : '') + '' + bg_color + '' + color + '" ></div>';
	container.innerHTML = inner_html;
	return true;
};
//用户定制月事件渲染效果
scheduler.beforeRenderMonthEvent = function(ev, schedulerHandler){
	var isDenied = false;
	isDenied = _checkEventReadonly(ev, schedulerHandler);
	return isDenied;
};