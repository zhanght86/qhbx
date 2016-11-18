/*封装的公共方法*/

var Calendar = new Object();

//公共_获取当前用户code
Calendar.getUserCode = function(){
	return parent.System.getVar("@USER_CODE@");
}

//公共_获取当前用户name
Calendar.getUserName = function(){
	return parent.System.getVar("@USER_NAME@");
}

//公共_读取后台数据
Calendar.loadEventData = function(options){
	var opts = {
		'startDate': convertToDate(scheduler._min_date),
		'startTime': convertToTime(scheduler._min_date),
		'endDate': convertToDate(scheduler._max_date),
		'endTime': convertToTime(scheduler._max_date),
		'isSelf':'false',
		'queryUserStr': Calendar.getCurrentUsers().code
	};
	var servId = options.servId || "SY_COMM_CAL";
	jQuery.extend(opts, options);
	if(opts.queryUserStr == Calendar.getUserCode()){
		opts.isSelf = "true";
	}
	var result = FireFly.doAct(servId, "loadEventData", opts);
	if(result!=null && result._DATA_){
		return result._DATA_
	}
	return {};
};

//公共_切换并更新视图
Calendar.updateView = function(date, view){
	if(!view || view == ""){
		view = this.currentMode;
	}
	scheduler.setCurrentView(date, view);
}

//公共_清空数据
Calendar.clearEventData = function(){
	if(scheduler.getEvents.length > 0){
		scheduler.clearAll();
	}
}

//公共_获取当前显示用户
Calendar.getCurrentUsers = function(){
	var userObj = this.currentUsers || {};
	return userObj;
}

//公共_记录当前显示用户（格式{"code":编码,name"":名称}）
Calendar.setCurrentUsers = function(userObj){
	this.currentUsers = null;
	this.currentUsers = userObj || {};
}

//公共_动态调整外层iframe高度
Calendar.resizeHeight = function(){
  	var mode = this.currentMode;
  	var outHeight = 0;
	var frameId = MapBean.extServerId + "-tabFrame";
	if(!mode || mode != "month"){//也包括自定义视图
		outHeight = this._computeDayAndWeekScheduler();//alert(mode + "---" + outHeight);
		jQuery(".dhx_cal_data").height(jQuery(".dhx_scale_holder").height());
		jQuery("#right_scheduler").height(outHeight);
		this._setFrameHei(outHeight + 80);//80是顶部bar+底部悬空高度
	}
	if(mode == "month"){
		outHeight = this._computeMonthScheduler();//alert(mode + "---" + outHeight);
		jQuery(".dhx_cal_data").height(jQuery("#right_scheduler table").height());
		jQuery("#right_scheduler").height(outHeight);
		this._setFrameHei(outHeight + 80);
	}
};

//公共_计算非月视图页外层
Calendar._computeDayAndWeekScheduler = function(){
	var _navH = jQuery(".dhx_cal_navline").height();
	var _headH = jQuery(".dhx_cal_header").height();
	var _mdH = jQuery(".dhx_multi_day").height();	
	var _shH = jQuery(".dhx_scale_holder").height();
	return _navH + _headH + _mdH + _shH + 30; //最后是多留的空白（防止日历下方过于贴边）
};

//公共_计算月视图外侧高度
Calendar._computeMonthScheduler = function(){
	var _navH = jQuery(".dhx_cal_navline").height();
	var _headH = jQuery(".dhx_cal_header").height();		
	var _tabH = jQuery("#right_scheduler table").height();
	return _navH + _headH + _tabH + 30;
};

//公共_调整父窗口iframe、div高度
Calendar._setFrameHei = function(replaceHei) {
	var docHei = document.documentElement.scrollHeight;
	if (replaceHei) {
		docHei = replaceHei;
	}
	var id = MapBean.extServerId + "-show-do-tabFrame";
	if (parent.document.getElementById(id)) {
		parent.document.getElementById(id).style.height = docHei + "px";
	}
	parent.jQuery("#" + id).parent().height("100%");
};

//公共_删除单条日程
Calendar.deleteData = function(eventObj, isCard){
	//删除前端事件
	var delForeEndEvent = function(bool){
		if(bool){
			var id = eventObj.id;
			var ev = scheduler._events[id];
			delete scheduler._events[id];
			scheduler.unselect(id);
			scheduler.event_updated(ev);
			if(Calendar.cardOpts.currCard){
				Calendar.cardOpts.currCard.backA.click();
			}
			dhtmlx.message("日程成功删除");
		}
  	}

	//删除后端事件
	var delBackEndEvent = function(bool) {
		if(bool){
			if(isCard){
				Calendar.cardOpts.currCard.cardBarTipLoad("提交中...");
				setTimeout(function() {
					var data = {};
					data[UIConst.PK_KEY]= eventObj.CAL_ID;
					var resultData = FireFly.listDelete(MapBean.extServerId, data, true);
					if(resultData._DATA_ == "1"){
						delForeEndEvent(true);
					}
					Calendar.cardOpts.currCard.cardClearTipLoad();
					Calendar.cardOpts.currCard.backA.click();
				},0);
			}else{
				var data = {};
				data[UIConst.PK_KEY]=  eventObj.CAL_ID;
				var resultData = FireFly.doAct(MapBean.extServerId, UIConst.ACT_DELETE, data, true, false);
				if(resultData._DATA_ == "1"){
					delForeEndEvent(true);
				}
			}
      	}
	}
  	
	var callbackFunc = delForeEndEvent;
	if(eventObj.CAL_ID){
		callbackFunc = delBackEndEvent;
	}
	dhtmlx.message({
	      type:"confirm-warning",
	      text:"是否确实要删除该日程？",
	      title:"提醒!",
	      ok:"是",
	      cancel:"否",
	      callback: callbackFunc
	});
};

//公共_事件被改变后的处理
Calendar.afterEventChanged = function(eventObj){
	if(eventObj.CAL_ID){
		var data = {
				"_PK_":eventObj.CAL_ID,
				"CAL_START_DATE":convertToDate(scheduler.getEventStartDate(eventObj.id)),
				"CAL_START_TIME":convertToTime(scheduler.getEventStartDate(eventObj.id)),
				"CAL_END_DATE":convertToDate(scheduler.getEventEndDate(eventObj.id)),
				"CAL_END_TIME":convertToTime(scheduler.getEventEndDate(eventObj.id))
		};
		FireFly.cardModify(MapBean.extServerId, data);
	}
}

//公共_定制视图头部扩展信息
Calendar._setHeaderExtInfo = function(html){
	scheduler.headerExtInfo = html;
};