/**
* http://www.bjjtgl.gov.cn/publish/portal0/tab41/info46395.htm
* 0.自2013年4月11日至2014年4月10日,按车牌尾号工作日高峰时段区域限行的机动车车牌尾号分为五组，每13周轮换一次限行日
* 1.自2013年4月8日至2013年7月6日，星期一至星期五限行机动车车牌尾号分别为：4和9、5和0、1和6、2和7、3和8；　
* 2.自2013年7月7日至2013年10月5日，星期一至星期五限行机动车车牌尾号分别为：3和8、4和9、5和0、1和6、2和7；　
* 3.自2013年10月6日至2014年1月4日，星期一至星期五限行机动车车牌尾号分别为：2和7、3和8、4和9、5和0、1和6；　
* 4.自2014年1月5日至2014年4月10日，星期一至星期五限行机动车车牌尾号分别为：1和6、2和7、3和8、4和9、5和0。
* 
* http://www.bjjtgl.gov.cn/publish/portal0/tab62/info52267.htm
* 0.自2014年4月11日至2015年4月10日,按车牌尾号工作日高峰时段区域限行的机动车车牌尾号分为五组，每13周轮换一次限行日　
* 1.自2014年4月14日至2014年7月12日，星期一至星期五限行机动车车牌尾号分别为：5和0、1和6、2和7、3和8、4和9；
* 2.自2014年7月13日至2014年10月11日，星期一至星期五限行机动车车牌尾号分别为：4和9、5和0、1和6、2和7、3和8；
* 3.自2014年10月12日至2015年1月10日，星期一至星期五限行机动车车牌尾号分别为：3和8、4和9、5和0、1和6、2和7；
* 4.自2015年1月11日至2015年4月10日，星期一至星期五限行机动车车牌尾号分别为：2和7、3和8、4和9、5和0、1和6；
*/

/**
* @reference  
* jQuery UI Datepicker 1.10.2
* http://jqueryui.com
*
*
*/
(function ($) {

  "use strict";  


 /* TOOLTIP PUBLIC CLASS DEFINITION
  * =============================== */

  var CarLimit = function (element, options) {
    	this.init('carlimit', element, options);
  }

  CarLimit.prototype = {

    constructor: CarLimit,
	
	init: function (type, element, options) {
      this.type = type;
      this.$element = $(element);
      this.options = this._getOptions(options);
      this.minDate = this.options.minDate;
      this.maxDate = this.options.maxDate;
	  //firstWeek自规定实行起minDate的第一周
//	  this.firstWeek = this._getFirstWeek(this.minDate);
	  //today
	  this.todayEl=$(this.options.todadySelector);
	  ///alert(this.formatDate("yy-mm-dd",new Date()));
	  //tomorrow
	  this.tomorrowEl=$(this.options.tomorrowSelector); 
	  this.fullWeekEl=$(this.options.fullWeekSelector); 
	  this.holidays=this.options.holidays;
	  
	  this._render();
	  
	 
    }, 
	_getOptions: function (options) {
      return $.extend({}, $.fn[this.type].defaults, this.$element.data(), options);
    },
//	_getFirstWeek: function(date){
//		//TODO 在IE8 下，此行报错
////		var minDate= new Date(this.options.minDate) ;
//		var minDate = rhDate.stringToDate(date);
//		return this._calculateWeek(minDate);
//	},
	/**
	* 渲染
	*/
	_render: function() {
		var today 	  = new Date(),
			nowWeek   = this._calculateWeek(today),//本周
		    tempArr	  = this.options.limitArr,	 //获取限行号数组
  			weekIndex = parseInt(nowWeek/ 13, 10);  //第几周期

		 //每13周轮换一次
		for (var i = 0; i < weekIndex; i++) {
			tempArr.unshift(tempArr.pop());
		}
		//本周期(周期：13周)内实行的限号
		this.limitArr = tempArr;
		this.renderSingleDay(this.todayEl,new Date());
		this.renderSingleDay(this.tomorrowEl,this.getTomorrow());
		this.renderFullWeek(this.fullWeekEl);
 
	},
	renderSingleDay:function(target,date){
		if (!date) {
			date=new Date();
		}
		 
		var i            = date.getDay(),
			dateStr      = this.formatDate(this.options.dateFormat,date),
		    isWorkDay    = this.isOnHoliday(date) > -1 ? false : true ,
			holidaysArr	 = this.holidays.holidaysArr;
		
		if ((i+6)%6==0) {//周六、周日不限行
			isWorkDay = false; 
		} 
		
		if (!isWorkDay) {//节假日或周六、周日 不限行
			target.find('.carlimit-title span').html("(" + this.options.dayNames[i] + ")").end()
						.find('.carlimit-num').html(this.options.nolimit);
		} else {
			target.find('.carlimit-title span').html("(" + this.options.dayNames[i] + ")").end()
						.find('.carlimit-num').html(this.limitArr[i - 1].first+"<span>和</span>"+this.limitArr[i - 1].second);
		}
	},
	renderFullWeek:function(target){
		var currDay  = new Date().getDay(),
			weekDays = this.getThisWeekDays(),
			holidays = this.holidays.holidaysArr;
		
		for(var i =0,len=this.limitArr.length; i<len;i++){
			var index = this.isOnHoliday(weekDays[i+1]);
			var divHtml="<div class='carlimit-item'>" +
						 "<div class='title'><span>" +
						//this.options.dayNames[i+1]+(index>-1? "("+holidays[index].name+")":"") +
						 this.options.dayNames[i+1] +
						"</span></div>" +
						"<div class='num'>" +
						(index>-1? this.options.nolimit:this.limitArr[i].first+"<span>/</span>"+this.limitArr[i].second) +
						"</div>" +
						"</div>";
			var $el = $(divHtml).appendTo(target);
			if(currDay == i+1){
				$el.addClass("today");
			}
		}
	},
	/**
	* 判断某天是否是节假日
	* @param s 某天 （string）
	* return 
	*/
	isOnHoliday : function(elem){
		var i 			= 0,
			holidaysArr = this.holidays.holidaysArr,
			len 		= holidaysArr.length;
		for ( ; i < len; i++ ) {
			for( var h in holidaysArr[i].holidays){
				if ( holidaysArr[i].holidays[h] === elem ) {
					return i;
			    }
			}
		}
		return -1;
	},
	getTomorrow:function(){
		var d = new Date(),
		date = d.getDate();
		d.setDate(date+1);
		return d;
	},
	/**
	* 获取本周日期
	* 从周日开始计算
	* @return 返回本周所有日期
	*/
	getThisWeekDays:function(){
		var	date	    = new Date(),
			offset 	    = date.getDay(),
			currTime    = date.getTime(),
			week	    = [];
		for(var i = offset; i >= 0; i--){
			week.push(this.formatDate(this.options.dateFormat,new Date(currTime - i * 24*60*60*1000)));
		}
		return week;
	},
	/**
	* 获取相对于今天的日期
	* @param offset 数字格式的字符串 比如"+1" ，"-1"
	* @return 相对于今天的offset日期
	*/
	getOffsetDay : function(offset){
		if(!(offset && offset.match(/\d/))){
			throw{
				name: 'exception',
				message : "格式错误"
			};
		} 
		
		var	date	    = new Date(),
			currTime    = date.getTime();
		return this.formatDate(this.options.dateFormat,new Date(currTime + offset * 24*60*60*1000));
	},
	/** 
	 * 计算某一日期处于第几周
	 * 参考：iso8601Week
	 * jQuery UI Datepicker 1.10.2
 	 * http://jqueryui.com
	 * Set as calculateWeek to determine the week of the year based on the ISO 8601 definition.
	 * @param  date  Date - the date to get the week for
	 * @return  number - the number of the week within the year that contains this date
	 */
	_calculateWeek: function(date) {
		var time = date.getTime(),
			checkDate = rhDate.stringToDate(this.minDate);;

		// Find Thursday of this week starting on Monday
//		checkDate.setDate(checkDate.getDate() + 4 - (checkDate.getDay() || 7));
		 
//		time = checkDate.getTime();
		 
//		checkDate.setMonth(4); // Compare with Jan 1
//		checkDate.setDate(11);
//		return Math.floor(Math.round((time - checkDate.getTime()) / 86400000) / 7) + 1;
		return Math.floor(Math.round((time - checkDate.getTime()) / 86400000) / 7);
	},
	/* Format a date object into a string value.
	 * The format can be combinations of the following:
	 * d  - day of month (no leading zero)
	 * dd - day of month (two digit)
	 * o  - day of year (no leading zeros)
	 * oo - day of year (three digit)
	 * D  - day name short
	 * DD - day name long
	 * m  - month of year (no leading zero)
	 * mm - month of year (two digit)
	 * M  - month name short
	 * MM - month name long
	 * y  - year (two digit)
	 * yy - year (four digit)
	 * @ - Unix timestamp (ms since 01/01/1970)
	 * ! - Windows ticks (100ns since 01/01/0001)
	 * "..." - literal text
	 * '' - single quote
	 *
	 * @param  format string - the desired format of the date
	 * @param  date Date - the date value to format
	 * @param  settings Object - attributes include:
	 * @return  string - the date in the above format
	 */
	formatDate: function (format, date) {
		if (!date) {
			return "";
		}

		var iFormat,
			 
			// Check whether a format character is doubled
			lookAhead = function(match) {
				var matches = (iFormat + 1 < format.length && format.charAt(iFormat + 1) === match);
				if (matches) {
					iFormat++;
				}
				return matches;
			},
			// Format a number, with leading zero if necessary
			formatNumber = function(match, value, len) {
				var num = "" + value;
				if (lookAhead(match)) {
					while (num.length < len) {
						num = "0" + num;
					}
				}
				return num;
			},
		 
			output = "",
			literal = false;

		if (date) {
			for (iFormat = 0; iFormat < format.length; iFormat++) {
				if (literal) {
					if (format.charAt(iFormat) === "'" && !lookAhead("'")) {
						literal = false;
					} else {
						output += format.charAt(iFormat);
					}
				} else {
					switch (format.charAt(iFormat)) {
						case "D":
						case "d":
							output += formatNumber("d", date.getDate(), 2);
							break;
						case "M": 
						case "m":
							output += formatNumber("m", date.getMonth() + 1, 2);
							break;
						case "Y":
						case "y":
							output += (lookAhead("y") ? date.getFullYear() :
								(date.getYear() % 100 < 10 ? "0" : "") + date.getYear() % 100);
							break;
						case "'":
							if (lookAhead("'")) {
								output += "'";
							} else {
								literal = true;
							}
							break;
						default:
							output += format.charAt(iFormat);
					}
				}
			}
		}
		return output;
	}
	
  
  }


 /* TOOLTIP PLUGIN DEFINITION
  * ========================= */

  var old = $.fn.carlimit

  $.fn.carlimit = function ( option ) {
	  
    return this.each(function () {
      var $this = $(this)
        , data = $this.data('carlimit')
        , options = typeof option == 'object' && option ;
      if (!data){
		 $this.data('carlimit', (data = new CarLimit(this, options)));
	  }
      if (typeof option == 'string') 
	  	 data[option]()
    })
  }

  $.fn.carlimit.Constructor = CarLimit;

  $.fn.carlimit.defaults = {
	  //limitArr:["4和9","5和0","1和6","2和7","3和8"]
	  //limitArr:["5和0","1和6","2和7","3和8","4和9"]
	    limitArr: [
			{
				first:5,
				second:0
			},
			{
				first:1,
				second:6
			},
			{
				first:2,
				second:7
			},
			{
				first:3,
				second:8
			},
			{
				first:4,
				second:9
			}
		],
		//dayNames: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday","Sunday"], // For formatting
		dayNames: ["周日","周一", "周二", "周三", "周四", "周五", "周六"], // For formatting
 		dateFormat: "yy-mm-dd", // See format options on parseDate
		firstDay: 0, // The first day of the week, Sun = 0, Mon = 1, ...
		minDate: null, // The earliest selectable date, or null for no limit
		maxDate: null, // The latest selectable date, or null for no limit
		holidays:[],
		nolimit:'不限行!',
		firstWeek:0//从最minDate开始 是第几周
  }


 /* Carlimit NO CONFLICT
  * =================== */

  $.fn.carlimit.noConflict = function () {
     $.fn.carlimit = old;
     return this;
  }

   $(function () {
	  var holidayArr;
	  var jqxhr=$.getJSON("/sc/comm/carlimit/js/holidays-2014.json")
	  			 .done(function(data){
					   
	  				holidayArr=data;
	  			 });
	 jqxhr.complete(function(){
		 $('.carlimit-wrapper').carlimit({
				minDate:'2014-04-14',
				maxDate:'2015-04-10',
				todadySelector:'.carlimit-today',
				tomorrowSelector:'.carlimit-tomorrow',
				fullWeekSelector:'.full-week',
				holidays:holidayArr
		   });
	 });
	 
    });
}(window.jQuery));


