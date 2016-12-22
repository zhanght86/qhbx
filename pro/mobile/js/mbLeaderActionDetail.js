/** 领导日程 */
GLOBAL.namespace("mb.vi");
/*待解决问题：
 * 
 */
 
mb.vi.ldrActionDetail = function(options) {
	var defaults = {
		"sId":""//服务ID
	};
	this.opts = jQuery.extend(defaults,options);
	this.servId = this.opts.sId;
};


mb.vi.ldrActionDetail.prototype.show = function() {
	var _self = this;
	//一周开始日期
	var weekStartDate = this.getWeekStartDate();
	//一周结束日期
	var weekEndDate = this.getWeekEndDate();
	//获取领导user_code
	var leaderCode = this.opts.data["USER_CODE"];
	this._initMainData(weekStartDate,weekEndDate,leaderCode);
	this._layout();
	this._renderLeaderName();
	this._weekButton(leaderCode);
	this._afterLoad();
	var alreadyRendered = $.mobile.window.data("alreadyRendered");
	if (alreadyRendered) {
		this._refresh();
	} else {
		$.mobile.window.data("alreadyRendered" , true);
	}
};

/*
 *获取领导活动数据 
 */
mb.vi.ldrActionDetail.prototype._initMainData = function(weekStartDate,weekEndDate,leaderCode) {
	var _self = this; 
	//构造查询条件
	var serchWhere = "and instr(','||ldr_id||',',',"+leaderCode+",')>0  and (";
	//活动时间开始于周一之前，结束于周日之前
	serchWhere += " (BEGIN_TIME <='"+weekStartDate+" 00:00:00' and END_TIME >='"+weekStartDate+" 00:00:00' and END_TIME <='"+weekEndDate+" 23:59:59') ";
	//活动时间开始于周一之前，结束于周日之后
	serchWhere += " or (BEGIN_TIME <='"+weekStartDate+" 00:00:00' and END_TIME >='"+weekEndDate+" 23:59:59')";
	//活动时间开始于周一之后、周日之前，结束于周日之前
	serchWhere += " or (BEGIN_TIME >='"+weekStartDate+" 00:00:00' and END_TIME <='"+weekEndDate+" 23:59:59')"; 
	//活动时间开始于周一之后、周日之前，结束于周日之后
	serchWhere += " or (BEGIN_TIME >='" + weekStartDate + " 00:00:00' and BEGIN_TIME <='" + weekEndDate + " 23:59:59' and END_TIME >='" + weekEndDate + " 23:59:59'))";
	//添加排序
	
	var param = {
		"_SELECT_" : "*",
	    "_WHERE_" : serchWhere
	};
	var allActions = "";
    return FireFly.doAct("OA_LDR_ACTION", "finds", param).then(function(result){
		console.log("action result",result);
		if(result["_DATA_"].length > 0){
			var actions = result["_DATA_"];
			for(var i=0;i<actions.length;i++){
				//活动开始时间
				var startTime = actions[i]["BEGIN_TIME"] + ":00";
				//活动结束时间
				var endTime = actions[i]["END_TIME"] + ":00"; 
				
				
				if(startTime < weekStartDate+" 00:00:00"){
					startTime = weekStartDate+" 00:00:00";
				}
				if(endTime > weekEndDate + " 23:30:00"){
					endTime =  weekEndDate + " 23:30:00";
				}
				
				//开始渲染的格子号码
                var secondDiff1 = rhDate.doDateDiff("S",weekStartDate+" 00:00:00",startTime,0);
				var beginDiv   = 1 + Math.ceil(secondDiff1 / (30 * 60));
				//var beginBlock = 1 + Math.floor(secondDiff1 / (12 * 60 * 60));
				var beginBlock = Math.ceil(beginDiv / 24);
				
				//结束渲染的格子号码
                var secondDiff2 = rhDate.doDateDiff("S",endTime,weekEndDate + " 23:59:59",0);
				var endDiv = 336  - Math.floor(secondDiff2 / (30 * 60)) ;
				//var endBlock = 14 - Math.ceil(secondDiff2 / (12 * 60 * 60)) ;
				var endBlock = 0;
				if(endDiv%24==1){
				 	endBlock = Math.floor(endDiv / 24);
				}else{
					endBlock = Math.ceil(endDiv / 24);
				}
				
				
				//设定颜色
				var tdcolor = "";
				if(actions[i]["ACT_TYPE"]=="会议"){
					tdcolor = "#0000fe";
				}else if(actions[i]["ACT_TYPE"]=="活动"){
					tdcolor = "#ff9900";
				}else if(actions[i]["ACT_TYPE"]=="出差"){
					tdcolor = "#fcff00";
				}else if(actions[i]["ACT_TYPE"]=="休假"){
					tdcolor = "#fe0000";
				}else{
					tdcolor = "#ffffff";
				}
				mb.vi.ldrActionDetail.prototype._renderAction(beginBlock,endBlock,tdcolor,beginDiv,endDiv);
			}
			$.mobile.window.data("action_", result["_DATA_"]);
	   		allActions = result["_DATA_"];
			mb.vi.ldrActionDetail.prototype._toOneDayPage(weekStartDate,allActions);
		}  
	});
};

/*
 * 构建列表页面布局
 */
mb.vi.ldrActionDetail.prototype._layout = function() {
	var _self = this;
	this.headerWrp = $("#ldrActionDetail_header");
	this.listWrp = $("#ldrActionDetailList");
	 
};

/*
 * 绑定标题 领导姓名数据 渲染table
 */
mb.vi.ldrActionDetail.prototype._renderLeaderName = function() {
	var _self = this;
	var userName = this.opts.data["USER_NAME"];
	this.headerWrp.find("h1").html(userName?userName:"");
	$("#actionTable").empty();
	$("#actionTable").append("<tr height='30px' id='firstTd'><td></td><td align='center' width='96px';>上午</td><td align='center' width='96px';>下午</td></tr>"
	    +"<tr id='tr1' height='30px'><td align='center'>星期一</td><td id='day1'><div id='dday1' style='position:relative;width:96px;height:30px'></div></td><td id='day2'><div id='dday2' style='position:relative;width:96px;height:30px'></div></td><tr>"
		+"<tr id='tr2' height='30px'><td align='center'>星期二</td><td id='day3'><div id='dday3' style='position:relative;width:96px;height:30px'></div></td><td id='day4'><div id='dday4' style='position:relative;width:96px;height:30px'></div></td><tr>"
		+"<tr id='tr3' height='30px'><td align='center'>星期三</td><td id='day5'><div id='dday5' style='position:relative;width:96px;height:30px'></div></td><td id='day6'><div id='dday6' style='position:relative;width:96px;height:30px'></div></td><tr>"
		+"<tr id='tr4' height='30px'><td align='center'>星期四</td><td id='day7'><div id='dday7' style='position:relative;width:96px;height:30px'></div></td><td id='day8'><div id='dday8' style='position:relative;width:96px;height:30px'></div></td><tr>"
		+"<tr id='tr5' height='30px'><td align='center'>星期五</td><td id='day9'><div id='dday9' style='position:relative;width:96px;height:30px'></div></td><td id='day10'><div id='dday10' style='position:relative;width:96px;height:30px'></div></td><tr>"
		+"<tr id='tr6' height='30px'><td align='center'>星期六</td><td id='day11'><div id='dday11' style='position:relative;width:96px;height:30px'></div></td><td id='day12'><div id='dday12' style='position:relative;width:96px;height:30px'></div></td><tr>"
		+"<tr id='tr7' height='30px'><td align='center'>星期日</td><td id='day13'><div id='dday13' style='position:relative;width:96px;height:30px'></div></td><td id='day14'><div id='dday14' style='position:relative;width:96px;height:30px'></div></td><tr>"
	);
	
};



/*
 * 渲染td颜色
 */
mb.vi.ldrActionDetail.prototype._renderAction = function(beginBlock,endBlock,color,beginDiv,endDiv) {
	if(beginBlock == endBlock){
		var ddivId = "#dday"+beginBlock;
		var divW = 4*(endDiv-beginDiv);
		var floatLeft = 4*(beginDiv%24 - 1);
		$(ddivId).append("<div style='background-color:"+color+";width:"+divW+"px;height:30px;overflow:hidden;left:"+floatLeft+"px;position:absolute;float:left'></div>");
	//如果多个格子	
	}else{
		//第一个格子
		var bdivId = "#dday"+beginBlock;
		var bdivW = 4*(beginBlock*24-beginDiv+1);
		var bfloatLeft = 4*((beginDiv)%24 - 1);
		$(bdivId).append("<div style='background-color:"+color+";width:"+bdivW+"px;height:30px;overflow:hidden;left:"+bfloatLeft+"px;position:absolute;float:left'></div>");
		//中间的格子
		for(var n=beginBlock+1;n<=endBlock-1;n++){
			var divId = "#dday" + n;
			$(divId).css("background-color",color);
		}
		//最后的格子
		var edivId = "#dday"+endBlock;
		var edivW = 4*(24-(endBlock*24-endDiv)) - 4;
		$(edivId).append("<div style='background-color:"+color+";width:"+edivW+"px;height:30px;left:0px;position:absolute;float:left'></div>");
	}
};


/*
 * 点击跳转到当天
 */
mb.vi.ldrActionDetail.prototype._toOneDayPage = function(weekStartDate,allActions) {
	//本周全部活动
	var monday =  new Array(); 
	var Tuesday = new Array(); 
	var Wednesday = new Array(); 
	var Thursday = new Array(); 
	var Friday = new Array(); 
	var Saturday = new Array(); 
	var Sunday = new Array(); 
	if(allActions.length>0){
		for(var i=0;i<allActions.length;i++){
			var actionBeginTime = allActions[i]["BEGIN_TIME"] + ":00";
			var actionEndTime = allActions[i]["END_TIME"] + ":00";
			var hourBegin = rhDate.doDateDiff("H",weekStartDate+" 00:00:00",actionBeginTime,0);
			var weekStart = Math.floor(hourBegin/24) +  1;
			var hourEnd = rhDate.doDateDiff("H",weekStartDate+" 00:00:00",actionEndTime,0);
			var weekEnd = Math.ceil(hourEnd/24);
			if(weekStart==1||weekEnd==1){
				monday.push(allActions[i]);
			}
			if(weekStart==2||weekEnd==2){
				Tuesday.push(allActions[i]);
			}
			if(weekStart==3||weekEnd==3){
				Wednesday.push(allActions[i]);
			}
			if(weekStart==4||weekEnd==4){
				Thursday.push(allActions[i]);
			}
			if(weekStart==5||weekEnd==5){
				Friday.push(allActions[i]);
			}
			if(weekStart==6||weekEnd==6){
				Saturday.push(allActions[i]);
			}
			if(weekStart==7||weekEnd==7){
				Sunday.push(allActions[i]);
			}
		}
		if(monday.length>0){
			$("#tr1").on("vclick" , "" , function(){
	    		 $("#oneLdrAction_header").find("h1").html(weekStartDate);
	    		 var data = monday;
	    		 var act = new mb.vi.oneLdrActionDetail({"data": monday});
				 act.show();
	    	});
		}
		
		if(Tuesday.length>0){
		    var week2 = rhDate.nextDate(weekStartDate,-1);
			$("#tr2").on("vclick" , "" , function(){
	    		 $("#oneLdrAction_header").find("h1").html(week2);
	    		 var data = monday;
	    		 var act = new mb.vi.oneLdrActionDetail({"data": Tuesday});
				 act.show();
	    	});
		}
		
		if(Wednesday.length>0){
			var week3 = rhDate.nextDate(weekStartDate,-2);
			$("#tr3").on("vclick" , "" , function(){
	    		 $("#oneLdrAction_header").find("h1").html(week3);
	    		 var data = monday;
	    		 var act = new mb.vi.oneLdrActionDetail({"data": Wednesday});
				 act.show();
	    	});
		}
		
		if(Thursday.length>0){
			var week4 = rhDate.nextDate(weekStartDate,-3);
			$("#tr4").on("vclick" , "" , function(){
	    		 $("#oneLdrAction_header").find("h1").html(week4);
	    		 var data = monday;
	    		 var act = new mb.vi.oneLdrActionDetail({"data": Thursday});
				 act.show();
	    	});
		}
		
		if(Friday.length>0){
			var week5 = rhDate.nextDate(weekStartDate,-4);
			$("#tr5").on("vclick" , "" , function(){
	    		 $("#oneLdrAction_header").find("h1").html(week5);
	    		 var data = monday;
	    		 var act = new mb.vi.oneLdrActionDetail({"data": Friday});
				 act.show();
	    	});
		}
		
		if(Saturday.length>0){
			var week6 = rhDate.nextDate(weekStartDate,-5);	
			$("#tr6").on("vclick" , "" , function(){
	    		 $("#oneLdrAction_header").find("h1").html(week6);
	    		 var data = monday;
	    		 var act = new mb.vi.oneLdrActionDetail({"data": Saturday});
				 act.show();
	    	});
		}
		
		if(Sunday.length>0){
			var week7 = rhDate.nextDate(weekStartDate,-6);	
			$("#tr7").on("vclick" , "" , function(){
	    		 $("#oneLdrAction_header").find("h1").html(week7);
	    		 var data = monday;
	    		 var act = new mb.vi.oneLdrActionDetail({"data": Sunday});
				 act.show();
	    	});
		}
	}
}




/*
 * 本周  上周  下周 按钮 绑定事件
 */
mb.vi.ldrActionDetail.prototype._weekButton = function(leaderCode) {
	var _self = this;
	
	$("#lastWeek").unbind("click").bind("click", function() {
		$("#actionTable").empty();
		$("#actionTable").append("<tr height='30px' id='firstTd'><td></td><td align='center' width='96px';>上午</td><td align='center' width='96px';>下午</td></tr>"
	    	+"<tr id='tr1' height='30px'><td align='center'>星期一</td><td id='day1'><div id='dday1' style='position:relative;width:96px;height:30px'></div></td><td id='day2'><div id='dday2' style='position:relative;width:96px;height:30px'></div></td><tr>"
			+"<tr id='tr2' height='30px'><td align='center'>星期二</td><td id='day3'><div id='dday3' style='position:relative;width:96px;height:30px'></div></td><td id='day4'><div id='dday4' style='position:relative;width:96px;height:30px'></div></td><tr>"
			+"<tr id='tr3' height='30px'><td align='center'>星期三</td><td id='day5'><div id='dday5' style='position:relative;width:96px;height:30px'></div></td><td id='day6'><div id='dday6' style='position:relative;width:96px;height:30px'></div></td><tr>"
			+"<tr id='tr4' height='30px'><td align='center'>星期四</td><td id='day7'><div id='dday7' style='position:relative;width:96px;height:30px'></div></td><td id='day8'><div id='dday8' style='position:relative;width:96px;height:30px'></div></td><tr>"
			+"<tr id='tr5' height='30px'><td align='center'>星期五</td><td id='day9'><div id='dday9' style='position:relative;width:96px;height:30px'></div></td><td id='day10'><div id='dday10' style='position:relative;width:96px;height:30px'></div></td><tr>"
			+"<tr id='tr6' height='30px'><td align='center'>星期六</td><td id='day11'><div id='dday11' style='position:relative;width:96px;height:30px'></div></td><td id='day12'><div id='dday12' style='position:relative;width:96px;height:30px'></div></td><tr>"
			+"<tr id='tr7' height='30px'><td align='center'>星期日</td><td id='day13'><div id='dday13' style='position:relative;width:96px;height:30px'></div></td><td id='day14'><div id='dday14' style='position:relative;width:96px;height:30px'></div></td><tr>"
		);
		var thisMondy = $("#weekStartDay").html();
		var thisSundy = $("#weekEndDay").html();
		var lastMondy = mb.vi.ldrActionDetail.prototype.getLastWeekStartDate(thisMondy);
        var lastSunday = mb.vi.ldrActionDetail.prototype.getLastWeekEndDate(thisSundy);
		$("#weekStartDay").html(lastMondy);
		$("#weekEndDay").html(lastSunday);
		mb.vi.ldrActionDetail.prototype._initMainData(lastMondy,lastSunday,leaderCode);
	});
	
	
	$("#thisWeek").unbind("click").bind("click", function() {
		$("#actionTable").empty();
		$("#actionTable").append("<tr height='30px' id='firstTd'><td></td><td align='center' width='96px';>上午</td><td align='center' width='96px';>下午</td></tr>"
	    	+"<tr id='tr1' height='30px'><td align='center'>星期一</td><td id='day1'><div id='dday1' style='position:relative;width:96px;height:30px'></div></td><td id='day2'><div id='dday2' style='position:relative;width:96px;height:30px'></div></td><tr>"
			+"<tr id='tr2' height='30px'><td align='center'>星期二</td><td id='day3'><div id='dday3' style='position:relative;width:96px;height:30px'></div></td><td id='day4'><div id='dday4' style='position:relative;width:96px;height:30px'></div></td><tr>"
			+"<tr id='tr3' height='30px'><td align='center'>星期三</td><td id='day5'><div id='dday5' style='position:relative;width:96px;height:30px'></div></td><td id='day6'><div id='dday6' style='position:relative;width:96px;height:30px'></div></td><tr>"
			+"<tr id='tr4' height='30px'><td align='center'>星期四</td><td id='day7'><div id='dday7' style='position:relative;width:96px;height:30px'></div></td><td id='day8'><div id='dday8' style='position:relative;width:96px;height:30px'></div></td><tr>"
			+"<tr id='tr5' height='30px'><td align='center'>星期五</td><td id='day9'><div id='dday9' style='position:relative;width:96px;height:30px'></div></td><td id='day10'><div id='dday10' style='position:relative;width:96px;height:30px'></div></td><tr>"
			+"<tr id='tr6' height='30px'><td align='center'>星期六</td><td id='day11'><div id='dday11' style='position:relative;width:96px;height:30px'></div></td><td id='day12'><div id='dday12' style='position:relative;width:96px;height:30px'></div></td><tr>"
			+"<tr id='tr7' height='30px'><td align='center'>星期日</td><td id='day13'><div id='dday13' style='position:relative;width:96px;height:30px'></div></td><td id='day14'><div id='dday14' style='position:relative;width:96px;height:30px'></div></td><tr>"
		);
		var start = mb.vi.ldrActionDetail.prototype.getWeekStartDate();
		var end = mb.vi.ldrActionDetail.prototype.getWeekEndDate();
		$("#weekStartDay").html(start);
		$("#weekEndDay").html(end);
		mb.vi.ldrActionDetail.prototype._initMainData(start,end,leaderCode);
	});
	
	
	$("#nextWeek").unbind("click").bind("click", function() {
		$("#actionTable").empty();
		$("#actionTable").append("<tr height='30px' id='firstTd'><td></td><td align='center' width='96px';>上午</td><td align='center' width='96px';>下午</td></tr>"
	    	+"<tr id='tr1' height='30px'><td align='center'>星期一</td><td id='day1'><div id='dday1' style='position:relative;width:96px;height:30px'></div></td><td id='day2'><div id='dday2' style='position:relative;width:96px;height:30px'></div></td><tr>"
			+"<tr id='tr2' height='30px'><td align='center'>星期二</td><td id='day3'><div id='dday3' style='position:relative;width:96px;height:30px'></div></td><td id='day4'><div id='dday4' style='position:relative;width:96px;height:30px'></div></td><tr>"
			+"<tr id='tr3' height='30px'><td align='center'>星期三</td><td id='day5'><div id='dday5' style='position:relative;width:96px;height:30px'></div></td><td id='day6'><div id='dday6' style='position:relative;width:96px;height:30px'></div></td><tr>"
			+"<tr id='tr4' height='30px'><td align='center'>星期四</td><td id='day7'><div id='dday7' style='position:relative;width:96px;height:30px'></div></td><td id='day8'><div id='dday8' style='position:relative;width:96px;height:30px'></div></td><tr>"
			+"<tr id='tr5' height='30px'><td align='center'>星期五</td><td id='day9'><div id='dday9' style='position:relative;width:96px;height:30px'></div></td><td id='day10'><div id='dday10' style='position:relative;width:96px;height:30px'></div></td><tr>"
			+"<tr id='tr6' height='30px'><td align='center'>星期六</td><td id='day11'><div id='dday11' style='position:relative;width:96px;height:30px'></div></td><td id='day12'><div id='dday12' style='position:relative;width:96px;height:30px'></div></td><tr>"
			+"<tr id='tr7' height='30px'><td align='center'>星期日</td><td id='day13'><div id='dday13' style='position:relative;width:96px;height:30px'></div></td><td id='day14'><div id='dday14' style='position:relative;width:96px;height:30px'></div></td><tr>"
		);
		var thisMondy2 = $("#weekStartDay").html();
		var thisSundy2 = $("#weekEndDay").html();
		var nextMondy = mb.vi.ldrActionDetail.prototype.getNextWeekStartDate(thisMondy2);
		var nextSundy = mb.vi.ldrActionDetail.prototype.getNextWeekEndDate(thisSundy2);
      	$("#weekStartDay").html(nextMondy);
		$("#weekEndDay").html(nextSundy);
		mb.vi.ldrActionDetail.prototype._initMainData(nextMondy,nextSundy,leaderCode);
    });
};


/*
 * 获取本周一
 */
mb.vi.ldrActionDetail.prototype.getWeekStartDate = function() { 
	var now = new Date(); //当前日期 
	var nowDayOfWeek = now.getDay(); //今天本周的第几天 
	var nowDay = now.getDate(); //当前日 
	var nowMonth = now.getMonth(); //当前月 
	var nowYear = now.getYear(); //当前年 
	nowYear += (nowYear < 2000) ? 1900 : 0; // 
	if(nowDayOfWeek==0){
		var weekStartDate = new Date(nowYear, nowMonth, nowDay - 6); 
	}else{
		var weekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek + 1); 
	}
	$("#weekStartDay").html(this.formatDate(weekStartDate));
	return this.formatDate(weekStartDate); 
}; 

/*
 * 获取本周日
 */
mb.vi.ldrActionDetail.prototype.getWeekEndDate = function () { 
	var now = new Date(); //当前日期 
	var nowDayOfWeek = now.getDay(); //今天本周的第几天 
	var nowDay = now.getDate(); //当前日 
	var nowMonth = now.getMonth(); //当前月 
	var nowYear = now.getYear(); //当前年 
	nowYear += (nowYear < 2000) ? 1900 : 0; // 
	if(nowDayOfWeek==0){
		var weekEndDate = new Date(nowYear, nowMonth, nowDay); 
	}else{
		var weekEndDate = new Date(nowYear, nowMonth, nowDay + (6 - nowDayOfWeek + 1)); 
	}
	$("#weekEndDay").html(this.formatDate(weekEndDate));
	return this.formatDate(weekEndDate); 
}; 

/*
 * 获取上周一 
 */
mb.vi.ldrActionDetail.prototype.getLastWeekStartDate = function(thisMonday) { 
	var lastMondy = thisMonday.substr(0,10).split("-");
	var lastDay = lastMondy[2]; //上周 日 
	var lastMonth =lastMondy[1]; //上周 月 
	var lastYear = lastMondy[0]; //上周 年 
	var thisWeekStartDate = new Date(lastYear, lastMonth-1, lastDay); 
	var lastWeekStartDate = new Date(thisWeekStartDate.setDate(thisWeekStartDate.getDate()-7));
	return this.formatDate(lastWeekStartDate); 
}; 

/*
 * 获取上周日
 */
mb.vi.ldrActionDetail.prototype.getLastWeekEndDate = function (thisSundy) { 
	var lastSundy = thisSundy.substr(0,10).split("-");
	var lastDay = lastSundy[2]; //上周 日 
	var lastMonth =lastSundy[1]; //上周 月 
	var lastYear = lastSundy[0]; //上周 年 
	var thisWeekEndDate =new Date(lastYear, lastMonth-1, lastDay);
	var lastWeekEndDate = new Date(thisWeekEndDate.setDate(thisWeekEndDate.getDate()-7));
	return this.formatDate(lastWeekEndDate); 
}; 

/*
 * 获取下周一 
 */
mb.vi.ldrActionDetail.prototype.getNextWeekStartDate = function(thisMonday) { 
	var nextMondy = thisMonday.substr(0,10).split("-");
	var nextrDay = nextMondy[2]; //上周 日 
	var nextMonth = nextMondy[1]; //上周 月 
	var nextYear = nextMondy[0]; //上周 年 
	var thisWeekStartDate = new Date(nextYear, nextMonth-1, nextrDay); 
	var nextWeekStartDate = new Date(thisWeekStartDate.setDate(thisWeekStartDate.getDate()+7));
	return this.formatDate(nextWeekStartDate); 
}; 

/*
 * 获取下周日
 */
mb.vi.ldrActionDetail.prototype.getNextWeekEndDate = function (thisSundy) { 
	var nextSundy = thisSundy.substr(0,10).split("-");
	var nextDay = nextSundy[2]; //上周 日 
	var nextMonth = nextSundy[1]; //上周 月 
	var nextYear = nextSundy[0]; //上周 年 
	var thisWeekEndDate =new Date(nextYear, nextMonth-1, nextDay);
	var nextWeekEndDate = new Date(thisWeekEndDate.setDate(thisWeekEndDate.getDate()+7));
	return this.formatDate(nextWeekEndDate); 
}; 





/*
 * 格式化日期：yyyy-MM-dd
 */
mb.vi.ldrActionDetail.prototype.formatDate = function (date) { 
	var myyear = date.getFullYear(); 
	var mymonth = date.getMonth()+1; 
	var myweekday = date.getDate(); 
	if(mymonth < 10){ 
		mymonth = "0" + mymonth; 
	} 
	if(myweekday < 10){ 
		myweekday = "0" + myweekday; 
	} 
	return (myyear+"-"+mymonth + "-" + myweekday); 
};

/*
 * 计算两个日期之间相差的秒数
 */
//TimeDifference = function (dateOne,dateTwo) { 
	//var dateTime1 = dateOne.substr(0,10).split("-");
	//var dateTime2 = dateTwo.substr(0,10).split("-");
	//var date1 = new Date(dateTime1[1] + - + dateTime1[2] + - + dateTime1[0]);
	//var date2 = new Date(dateTime2[1] + - + dateTime2[2] + - + dateTime2[0]);
	//日期之间相差的秒数
	//var dateDiff= parseInt(Math.abs(date1-date2)/1000);
	//小时数，分钟数和秒数相加得到总的秒数
	//dateOne.substr(11,2)截取字符串得到时间的小时数
	//parseInt(dateOne.substr(11,2))*60把小时数转化成为分钟
	//var second1=parseInt(dateOne.substr(11,2))*60*60+parseInt(dateOne.substr(14,2))*60+parseInt(dateOne.substr(17,2));
	//var second2=parseInt(dateTwo.substr(11,2))*60*60+parseInt(dateTwo.substr(14,2))*60+parseInt(dateTwo.substr(17,2));
	
	//时间之间相差的秒数
	//var timeDiff = parseInt(Math.abs(second1-second2));
	//相差的总秒数
	//var sumDiff = dateDiff+timeDiff;
	//return sumDiff; 
//};


/*
 * 加载后执行
 */
mb.vi.ldrActionDetail.prototype._afterLoad = function() {
	$.mobile.pageContainer.pagecontainer( "change","#ldrActionDetail");
};
 