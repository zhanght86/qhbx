scheduler.attachEvent("onEventAdded", function(event_id,event_object){
	alert("onEventAdded" + scheduler._mode + " USER:" + event_object.S_USER);//此event_object.S_USER可用于unit视图移动某个事件时判断是否是新建的且不是新建的话是否可以改变创建人
	Calendar.resizeHeight();
	//不启用edit_on_create和details_on_create两个设定值而自己定制以实现拖完即弹窗口的功能
	ExtCalendar.showCalendarCard(event_id, event_object);
});

/*可自定义用户弹出窗或其他动作*/
scheduler.attachEvent("onBeforeLightbox", function (event_id){
	//alert("onBeforeLightbox");
	//showLightbox
	ExtCalendar.showCalendarCard(event_id, scheduler.getEvent(event_id));
	return false;
});