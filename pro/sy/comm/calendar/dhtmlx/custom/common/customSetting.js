	scheduler.config.collision_limit = 100;//事件重复数
	scheduler.config.prevent_cache = true;//禁用缓存
	/**SIZE*/
//	scheduler.xy.nav_height=30;//height of top area with navigation buttons and tabs
//	scheduler.xy.bar_height=20;//height of event bars in month view
//	scheduler.xy.scroll_width=18;//expected width of scrollbar
//	scheduler.xy.scale_width=50;//width of y-scale
//	scheduler.xy.scale_height=20;//height of x-scale
//	scheduler.xy.menu_width=20;//width of selection menu on the day|week views
	/* Following options are available only with dhtmlxscheduler_map_view.js on Map tab */
//	scheduler.xy.map_date_width = 180;// width of date column
//	scheduler.xy.map_description_width = 400;// width of event description column
	/**DATE*/
//	scheduler.config.default_date="%Y-%m-%d %H:%i"//{string} header of day and week views;
//	scheduler.config.month_date="%Y-%m-%d %H:%i"//{string} header of month view;
//	scheduler.config.week_date="%Y-%m-%d %H:%i"//{string} subheader, days of week in month view;
//	scheduler.config.day_date="%Y-%m-%d %H:%i"//{string} subheader, day label in day and week views;
//	scheduler.config.hour_date="%h:%i %A"//{string} vertical scale for day and week views;
//	scheduler.config.month_day="%Y-%m-%d %H:%i"//{string} each day block in month view;
//	scheduler.config.api_date="%Y-%m-%d %H:%i"//{string} used to set dates in events using api methods.
	scheduler.config.xml_date="%Y-%m-%d %H:%i"//{string} used to define format of data in data xml
	/**SCALE*/
	scheduler.config.hour_size_px=66; //尺寸和背景图周末呢背景图相匹配才会获得好的效果//{numeric} height of 1 hour in pixels;
	scheduler.config.time_step=10;//{numeric} minimal date size step in minutes;
	scheduler.config.start_on_monday=false;//{boolean} if true week starts from Monday ( if false, week start from Sunday);
	scheduler.config.first_hour=8;//{numeric} hour from which day and week scales start;
	scheduler.config.last_hour=24;//{numeric} hour from which day and week scales end.
	scheduler.config.limit_time_select=false;//{boolean} adjust time selects to first ahd last hour settings
	scheduler.config.scroll_hour=16;//{numeric} hour which will be at top of screen, after view mode update
	scheduler.config.mark_now=true;//{boolean} when it's set to true, the day and week views will have a marker showing the current time. Starting from version 3.5 requires that the extension file dhtmlxscheduler_limit.js be included
	//隐藏侧面选择菜单并且阻止其单击事件（官方推荐做法）
	/*If you don't need selection menus, you can fully disable them and reuse available space by using*/ 
	scheduler.attachEvent("onClick",function(){ return true; });
	scheduler.xy.menu_width = 25;
	/*If you don't need selection menus, you can fully disable them and reuse available space by using*/
	/**HOTKEY*/
//	scheduler.keys.edit_save = 13;//confirm edit operation - Enter
//	scheduler.keys.edit_cancel = 27;//reject edit operation - Escape
	/**CONTROL*/
	scheduler.config.readonly=false;//{boolean}只读功能 if set to true - events in scheduler can't be created|changed|deleted; 1)
	scheduler.config.show_loading=true;//{boolean} 进度条（前台动态装载时）shows the progress during data loading, useful for dynamic loading mode
	scheduler.config.drag_resize=true;//{boolean} 是否允许拖拽调整事件allows resizing events by dnd;
	scheduler.config.drag_move=true;//{boolean} 是否允许拖拽移动事件allows moving events by dnd;
	scheduler.config.drag_create=true;//{boolean}是否允许拖拽建事件 allows creating new events by dnd;
	scheduler.config.dblclick_create=false;//{boolean}是否允许双击建事件 allows creating new events by double click;
	scheduler.config.edit_on_create=false;//{boolean} 是否阻止自动触发弹窗（默认窗和客户自定窗都有效）shows form on new event creation;
	scheduler.config.details_on_create=false;//{boolean} uses extended form on new event creation by drag or by dbl-click (option doesn't affect monthly view - where details form is the only way to change data);
	scheduler.config.details_on_dblclick=true;//{boolean}是否开启双击事件进入弹窗而不快捷添加 uses extended form on event double-click (double-click on existing event).
	/**OTHERS*/
	scheduler.config.server_utc=false;//{boolean} if enabled component will convert server side dates from utc to local timezone, and backward during data sending to server;
	scheduler.config.positive_closing=true;//{boolean} if outside click occurs during form edit, then form will be saved and event will be updated.
	scheduler.config.update_render=true;//{boolean} if set to true - each time when item updated - all view will be refreshed instead of updated item only (it allows smooth size recalculation but much more cpu consuming) 
	scheduler.config.multi_day=true;//{boolean} enables rendering of multi-day events in daily and weekly views
	scheduler.config.full_day=true;//{boolean} if set to true - entry fields in 'Time period' section of details form are blocked, and time period is set to a full day from 00.00 the current cell date untill 00.00 next day)
	scheduler.config.event_duration=1440;//（单位：分钟，月视图）{integer} used to set the initial duration of the event)
	scheduler.config.auto_end_date=true;//{boolean} if set to true - when you change start event time or date, the end event time and date will be changed automatically in order to make the event duration value 60 minutes)
	scheduler.config.drag_lightbox=true;//{boolean} when set to true lightbox can be dragged by header, it is enabled by default
	scheduler.config.preserve_scroll=true;//{boolean} when set to true current scroll position is preserved when navigating between dates on the same view, it is enabled by default
	/**日期类型转换函数*/
	var convertToDate = scheduler.date.date_to_str("%Y-%m-%d");
	var convertToTime = scheduler.date.date_to_str("%H:%i");
	var convertToUtc = scheduler.date.str_to_date("%Y-%m-%d %H:%i");