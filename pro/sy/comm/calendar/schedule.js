var defURL="SY_COMM_CALENDAR.@FLAG@.do";
var ClickEventBean;
var EventBean = function() {
    this.id = $("#CAL_ID").val();
    var typeName = "";
   var flagName = "";
    if ("1" == $("#CAL_TYPE").val()) {
        typeName = "[工作计划]";
   } else {
       if ("2" != $("#CAL_FLAG").val()) {
            flagName="[未发布]"
       }
        typeName = "[工作日志]";
   }
    this.title = typeName + $("#CAL_TITLE").val() + flagName;
    this.CAL_TITLE = $("#CAL_TITLE").val();
    this.content = $("#CAL_CONTENT").val();
    this.start = $("#CAL_START_DATE").val() + " " + $("#ev-st-sel").val();
    this.end = $("#CAL_END_DATE").val() + " " + $("#ev-et-sel").val();
    this.userCode = $("#USER_CODE").val();
    this.CAL_TYPE = $("#CAL_TYPE").val();
    this.CAL_FLAG = $("#CAL_FLAG").val();
    this.CAL_TITLE_CS = $("#CAL_TITLE_CS").val();
    this.PROJECT_JD = $("#PROJECT_JD").val();
    this.CAL_ADDR = $("#CAL_ADDR").val();
    this.CAL_OUTWORK = $("#CAL_OUTWORK").val();
    this.PROJECT_NAME = $("#PROJECT_NAME").val();
    this.PROJECT_CODE = $("#PROJECT_CODE").val();
    this.CAL_MANHOUR = $("#CAL_MANHOUR").val();
    this.CAL_COMMENT = $("#CAL_COMMENT").val();
    this.AUD_FLAG = $("#AUD_FLAG").val();
    this.CAL_LEVEL = $("#CAL_LEVEL").val();
    this.CAL_PM_COMMENT = $("#CAL_PM_COMMENT").val();
    this.CAL_PZ_COMMENT = $("#CAL_PZ_COMMENT").val();
    this.formatDate=function() {
        this.start = $.fullCalendar.parseDate(this.start);
        this.end = $.fullCalendar.parseDate(this.end);
        this._start = this.start;
        this._end = this.end;
        this._id = this.id;
    };
};
function updateEventBean(eventBean) {
	var typeName = "";
   var flagName = "";
    if ("1" == $("#CAL_TYPE").val()) {
        typeName = "[工作计划]";
   } else {
   	
       if ("2" != $("#CAL_FLAG").val()) {
            flagName="[未发布]"
       }
        typeName = "[工作日志]";
   }
    eventBean.title = typeName + $("#CAL_TITLE").val() + flagName;
    //eventBean.title = $("#CAL_TITLE").val();
    eventBean.CAL_TITLE = $("#CAL_TITLE").val();
    eventBean.content = $("#CAL_CONTENT").val();
    eventBean.start = $("#CAL_START_DATE").val() + " " + $("#ev-st-sel").val();
    eventBean.end = $("#CAL_END_DATE").val() + " " + $("#ev-et-sel").val();
    eventBean.userCode = $("#USER_CODE").val();
    eventBean.CAL_TYPE = $("#CAL_TYPE").val();
    eventBean.CAL_FLAG = $("#CAL_FLAG").val();
    eventBean.CAL_TITLE_CS = $("#CAL_TITLE_CS").val();
    eventBean.PROJECT_JD = $("#PROJECT_JD").val();
    eventBean.PROJECT_ADDR = $("#PROJECT_ADDR").val();
    eventBean.CAL_OUTWORK = $("#CAL_OUTWORK").val();
    eventBean.PROJECT_NAME = $("#PROJECT_NAME").val();
    eventBean.PROJECT_CODE = $("#PROJECT_CODE").val();
    eventBean.CAL_MANHOUR = $("#CAL_MANHOUR").val();
    return eventBean;
    
}

var EventInputComp = function() {
    var zIndex = 20;
    var eventCache = {};
    var seriaFlag;
    var updateFlag;
    var init = function() {
        var dateSeg = function() {
            var date = new Date();
            date.setMinutes(0);
            date.setHours(0);
            var endDate = new Date();
            endDate.setDate(date.getDate() + 1);
            endDate.setHours(0);
            endDate.setMinutes(0);
            var elemStr = "";
            var seg = 30;
            while (+date < +endDate) {
                elemStr += "<option value='@V@'>@V@</option>".replace(/@V@/g, $.fullCalendar.formatDate(date, 'HH:mm'));
                date.setMinutes(date.getMinutes() + seg);
            }
            return elemStr;
        }();
        $("#saveReleaseBtn").bind('click',{isRelease:true}, doSave);
        $("#saveBtn").bind('click',doSave);
        $("#deleteBtn").bind('click',doDelete);
        $("#CAL_START_DATE").datepicker();
        $("#CAL_END_DATE").datepicker();
        $("#ev-et-sel").append(dateSeg);
        $("#ev-st-sel").append(dateSeg);
    }(),
    /**
     * 呈现前期或关闭后期执行一次即可
     */
    clear=function() {
        var form = $("#ev-form");
        $("#CAL_ID").val("");
        $("#CAL_TITLE").val("");
        $("#CAL_CONTENT").val("");
        $("#CAL_TITLE_CS").val("");
        $("#PROJECT_JD").val("");
        $("#PROJECT_ADDR").val("");
        $("#CAL_CONTENT").val("");
        $("#CAL_FLAG").val("");
        $("#PROJECT_NAME").val("");
        $("#PROJECT_CODE").val("");
        $("#CAL_MANHOUR").val("");
        $("#AUD_FLAG").val("2");
        $("#CAL_COMMENT").val("");
        $("#CAL_OUTWORK").val("1");
        $("#CAL_LEVEL").val("20");
        $("#CAL_PM_COMMENT").val("");
        $("#CAL_PZ_COMMENT").val("");
    },
    /**
     * @param e 需要时间片数据
     */
    initData=function(e) {
        $("#CAL_START_DATE").val($.fullCalendar.formatDate(e.start,"yyyy-MM-dd"));
        $("#CAL_END_DATE").val($.fullCalendar.formatDate(e.end,"yyyy-MM-dd"));
        //select事件 Begin
        $("#ev-st-sel").change(function(e){
            $(this).find(":selected").each(function(){
                $("#CAL_START_TIME").val($(this).val());
                schDateChange();
                //$("#CAL_MANHOUR").val(rhDate.doDateDiff("H", $("#CAL_START_DATE").val() + " " + $("#CAL_START_TIME").val(), $("#CAL_END_DATE").val() + " " + $("#CAL_END_TIME").val(), 1));

            });
        });
        $("#ev-et-sel").change(function(e){
            $(this).find(":selected").each(function(){
                $("#CAL_END_TIME").val($(this).val());
                schDateChange();
                // $("#CAL_MANHOUR").val(rhDate.doDateDiff("H", $("#CAL_START_DATE").val() + " " + $("#CAL_START_TIME").val(), $("#CAL_END_DATE").val() + " " + $("#CAL_END_TIME").val(), 1));

            });
        });
        //select事件 End
        var s = $.fullCalendar.formatDate(e.start,"HH:mm");
        //默认选择09点上班
        if(s=="00:00") s="09:00"; 
        $("#CAL_START_TIME").val(s);
        $("#ev-st-sel").children("[value='" + s + "']").each(function(){
            $(this).attr("selected", true);
        });
        s = $.fullCalendar.formatDate(e.end,"HH:mm");
        //默认选择18点上班
        if(s=="00:00") s="18:00"; 
        $("#CAL_END_TIME").val(s);
        $("#ev-et-sel").children("[value='" + s + "']").each(function(){
            $(this).attr("selected", true);
        });
        if (e.id) {
            //置更新标记
            updateFlag = true;
            $("#CAL_ID").val(e.id);
            $("#CAL_TITLE").val(e.CAL_TITLE);
            $("#CAL_CONTENT").val(e.content);
            $("#CAL_TITLE_CS").val(e.CAL_TITLE_CS);
            $("#CAL_TYPE").val(e.CAL_TYPE);
            $("#PROJECT_JD").val(e.PROJECT_JD);
            $("#PROJECT_ADDR").val(e.PROJECT_ADDR);
            $("#CAL_OUTWORK").val(e.CAL_OUTWORK);
            $("#CAL_FLAG").val(e.CAL_FLAG);
            //$("#PROJECT_CODE").val(e.PROJECT_CODE);
            //$("#PROJECT_NAME").val(e.PROJECT_NAME);
            $("#CAL_MANHOUR").val(e.CAL_MANHOUR);
            //$("#CAL_COMMENT").val(e.CAL_COMMENT);
            $("#AUD_FLAG").val(e.AUD_FLAG);
            $("#CAL_LEVEL").val(e.CAL_LEVEL);
            $("#CAL_PM_COMMENT").val(e.CAL_PM_COMMENT);
            $("#CAL_PZ_COMMENT").val(e.CAL_PZ_COMMENT);
            var existV = $("option[value='" + e.PROJECT_CODE + "']", $("#PROJECT_CODE"));
            if (existV && existV.attr("value") && existV.attr("value") != "") {
                $("option[value='" + e.PROJECT_CODE + "']", $("#PROJECT_CODE")).attr("selected", "selected");
            } else  {
                var option = $("<option>");
                option.attr("value", e.PROJECT_CODE);
                option.attr("selected", "selected");
                option.text(e.PROJECT_NAME);
                $("#PROJECT_CODE").append(option);
            }
        }else{ 
            //更新标记为false为新增项s
        	$("#CAL_TYPE").val("2");
            $("#CAL_TITLE_CS").val("日常工作");
            updateFlag = false;
//            var lastBean = getData(FireFly.getContextPath() + "/" + "CO_SCHEDULE.getLastData.do");
//            var existV = $("option[value='" + lastBean.PROJECT_CODE + "']", $("#PROJECT_CODE"));
//            if (existV && existV.attr("value") && existV.attr("value") != "") {
//                $("option[value='" + lastBean.PROJECT_CODE + "']", $("#PROJECT_CODE")).attr("selected", "selected");
//            } else {
//                var option = $("<option>");
//                option.attr("value", lastBean.PROJECT_CODE);
//                option.attr("selected", "selected");
//                option.text(lastBean.PROJECT_NAME);
//                $("#PROJECT_CODE").append(option);
//            } 
            schDateChange();
            //$("#CAL_MANHOUR").val(rhDate.doDateDiff("H", $("#CAL_START_DATE").val() + " " + $("#CAL_START_TIME").val(), $("#CAL_END_DATE").val() + " " + $("#CAL_END_TIME").val(), 1));
        }
        //如果是已发布的隐藏"保存发布"按钮。
        e.CAL_FLAG && e.CAL_FLAG==2 ? $("#saveReleaseBtn").hide() : $("#saveReleaseBtn").show();
        ClickEventBean = e;
    };
    
    
    function schDateChange() {
        if ($("#CAL_START_DATE").val() == $("#CAL_END_DATE").val() && $("#CAL_START_TIME").val() <= "12:00" && $("#CAL_END_TIME").val() >= "13:00") {
             $("#CAL_MANHOUR").val(rhDate.doDateDiff("H", $("#CAL_START_DATE").val() + " " + $("#CAL_START_TIME").val(), $("#CAL_END_DATE").val() + " " + $("#CAL_END_TIME").val(), 1) - 1);
        } else {
             $("#CAL_MANHOUR").val(rhDate.doDateDiff("H", $("#CAL_START_DATE").val() + " " + $("#CAL_START_TIME").val(), $("#CAL_END_DATE").val() + " " + $("#CAL_END_TIME").val(), 1));
        }
    }
    
    var controlSave = true;
    /**
     * @param e 包含着时间片数据
     */
    function _showEventsInput(e) {
        if (this.getSeriaFlag()) {
            return;
        }
        controlSave = true;

        eventCache.inputData = e;
        initData(e);
        var diffDay = rhDate.doDateDiff("D", $.fullCalendar.formatDate(e.start,"yyyy-MM-dd"), $.fullCalendar.formatDate(new Date,"yyyy-MM-dd"),0);
        if (!updateFlag && diffDay > 30) {
            alert("30天以前的日志不能添加");
            return;
        }
        var p = $("body");
		var offset = p.offset();
		//alert("left: " +event.pageY + ", top: " + event.clientY );
		var cy = 400;
		if ($.browser.msie) {//todo:获取点击时的位置，非ie下的event获取有问题
			cy = event.clientY;
		}
        var posArray = [];
        posArray[0] = "";
        posArray[1] = cy-250;
        var operateDesc = updateFlag ? "更新" : "添加";
        calendarAdapter.getEventInputContainer().dialog({
            "closed":true,
            "resizable":false,
            "draggable":false,
            "hide": "explode",
            "width":700,
            "position":posArray,
            "maximizable":false,
            "closable" : true,
            "modal":false,
            "collapsible":false,
            "close":function(){
                clear();
            }
        });
        jQuery(".button-right").bind("click",function() {
        	calendarAdapter.getEventInputContainer().dialog("close");
        });
        if (e.AUD_FLAG == 3 || e.AUD_FLAG == 1 || diffDay > 30) {
            calendarAdapter.getEventInputContainer().attr("disabled", "true");
            $("img", calendarAdapter.getEventInputContainer()).hide();
            $("#saveReleaseBtn",  calendarAdapter.getEventInputContainer()).hide();
            $("#saveBtn",  calendarAdapter.getEventInputContainer()).hide();
            $("#deleteBtn",  calendarAdapter.getEventInputContainer()).hide();
        } else {
            calendarAdapter.getEventInputContainer().removeAttr("disabled");
            $("img", calendarAdapter.getEventInputContainer()).show();
            if (!e.CAL_FLAG && e.CAL_FLAG==2) {
                 $("#saveReleaseBtn",  calendarAdapter.getEventInputContainer()).show();
            }
            $("#saveBtn",  calendarAdapter.getEventInputContainer()).show();
            $("#deleteBtn",  calendarAdapter.getEventInputContainer()).show();
        }
    }
    /**
     * @param flag {true:清除缓存防止第二次时间片选中}
     */
    function _hideEventsInput(flag) {
        if (flag || updateFlag) {
            if (eventCache.inputData) {
                delete eventCache.inputData;//清除掉本次缓存
            }
        }
        clear();
        calendarAdapter.getEventInputContainer().dialog('close');
    };
    
    /**
     * @param event 存储相关状态数据
     */
    function doSave(event) {
    	if (controlSave) {
    		controlSave = false;
    	} else {
    		return;
    	}
        //validate 
        if ($("#CAL_START_DATE").val() != $("#CAL_END_DATE").val()) {
            alert("一次只能填写一天的日志");
            $("#PROJECT_CODE").focus();
            controlSave = true;
            return;
        }
//        if ($("#PROJECT_CODE").val() == "") {
//            alert("请选一个项目");
//            $("#PROJECT_CODE").focus();
//            controlSave = true;
//            return;
//        }
        if ($("#CAL_TITLE").val() == "") {
            alert("请填写标题");
            $("#CAL_TITLE").focus();
            controlSave = true;
            return;
        }

        if ($("#CAL_OUTWORK").val() == "") {
            alert("请选择工时类型");
            $("#CAL_OUTWORK").focus();
            controlSave = true;
            return;
        } else if ($("#CAL_OUTWORK").val() == "3") {
            alert("不能直接填写请假工时");
            $("#CAL_OUTWORK").focus();
            controlSave = true;
            return;
        }
        
        //是否已经是发布的
        if($("#CAL_FLAG").val() != "2"){
            //是否是点击的"发布保存"按钮
            $("#CAL_FLAG").val(event["data"]&&event.data.isRelease ? "2" : "1");
        }
        
        //提取表单数据转JSON
        var dataArray = [];
        $("#ev-form").find(":input").not(":button").not(":checkbox").each(function(){
            dataArray.push({"key" : $(this).attr("id"),"value": ($(this).val())});
        });
        //特殊处理checkbox[on]问题
        $("#ev-form").find(":checkbox").each(function(){dataArray.push({"key":$(this).attr("id"),"value":($(this).attr("checked")?"1":"2")});});
        //特殊处理PK
        if (updateFlag == true) {
	        dataArray.push({"key" : "_PK_","value": (jQuery("#CAL_ID").val())});
        }
        //Ajax提交
        jQuery.ajaxSetup({async: false});
        var saveParam = encodeURI(eventDataFetch.getSendData(dataArray));

        jQuery.post(defURL.replace(/@FLAG@/g,("save")), saveParam, function(e) {
            if (e.exception) {
                alert(e.msg);
            } else {
                if (updateFlag) {
                    var eventBean = updateEventBean(ClickEventBean);
                    calendarAdapter.updateEvent(eventBean);
                } else {
                    $("#CAL_ID").val(e.CAL_ID);
                   // alert(e.id);
                    //alert($("#CAL_CODE").val());
                    calendarAdapter.addedEvent(new EventBean());
                }
                _hideEventsInput(true);
            }
        }, "json");
        controlSave = true;
    };
    function doDelete() {
        //提取隐藏域或按钮自定义唯一ID
    	var audFlag = $("#AUD_FLAG").val();
    	if (audFlag == "3" || audFlag == "1") {
    		return;
    	}
        var evId = $("#CAL_ID").val();
        if (evId == "") {
        	 _hideEventsInput(true);
        	 return;
        }
        //Ajax提交
        jQuery.post(defURL.replace(/@FLAG@/g,"delete"),{"_PK_":evId}, function(e) {
            calendarAdapter.delEvent(evId);
            _hideEventsInput(true);
        },"text");
    };
    return {
        showEventsInput : _showEventsInput,
        hideEventsInput:_hideEventsInput,
        getSeriaFlag:function() {
            return seriaFlag;
        }
    };
};
var eventDataFetch=new function(){
    this.currUser;
    this.treeUser;
    this.setCurrUser=function(u) {
        this.currUser = u;
    };
    this.getCurrUser=function() {
        return this.currUser;
    };
    this.getTreeUser=function() {
        return this.treeUser;
    };
    this.getQryUser=function() {
        return this.treeUser || this.currUser;
    };
    this.setTreeUser=function(u) {
        this.treeUser = u;
    };
};
/**
 * 查询事务数据
 * json格式（如下）
 * <p>$.parseJSON('[{"title":"title","start":"2011-03-25 10:30","end":"2011-03-25 15:30","allDay":false}]');</p>
 * @param {} o 存储了日期段数据
 * @param {} callback 回调
 */
eventDataFetch.queryEvents=function(o, callback) {
    var rqsUrl = defURL.replace(/@FLAG@/g,"queryList");
    var rtnArray = null;
    jQuery.ajax({type:"post", cache:false, async:false, url:rqsUrl, data:this.getSendData(o, true), success:function(data){
        if (data && data["_DATA_"]) {
        	//alert(JsonToStr(data["_DATA_"][0]));
            rtnArray = data["_DATA_"];
            //rtnArray = [{'allDay':'false','id':'601', 'content':'','title':'[工作日志]fdfd[未发布]','start':'2012-03-21 11:00', 'end':'2012-03-21 12:00'}];
            callback(rtnArray);
        }
    },error:function(e){
        alert("call queryEvents method error: " + e["responseText"]);
    },dataType:"json"});
};
/**
 * 将形如[{"key":"abc","value":"dba"}]转换成类似abc=dba&def=fghd
 * @param o 基础数据
 * @param flag 转换分支标记
 * @return 字符串||空串(长度为零)
 */
eventDataFetch.getSendData=function(o,flag) {
    var str="";
    if (flag) {
        for (var s in o) {
            str += s + "=" + o[s] + "&";
        }
    } else {
        for (var i = 0, len = o.length; i < len; i++) {
            str += o[i]["key"] + "=" + o[i]["value"] + "&";
        }
    }

    str = str.concat(eventDataFetch.getQryWhere());
    str = str.replace(/&$/,"");
    return str;
};
/**
 * 构建附加查询参数
 * @return 查询参数
 */
eventDataFetch.getQryWhere=function() {
    var str = "";
    str += "user=" + eventDataFetch.getQryUser() + "&";
    return str;
};
/**
 * 总控对象,负责组合适配各组件协调工作
 */
var calendarAdapter = function() {
    var eventInputComp;
    /**录入容器*/
    var eventInputContainer = ".ev-input-container";
    /**
     * datePicker组件工具栏按钮事件回调
     * <p>
     * 日视图、周视图、月视图
     * </p>
     */
    function _btnEvent(e) {
        calendarAdapter.changeView(e.currentTarget.id.replace(/btn$/, ""));
    }
    return {
        setEventInputComp:function(comp) {
            this.eventInputComp = comp;
        },
        getEventInputComp:function() {
            return this.eventInputComp;
        },
        getEventsContainer : function() {
            $("#ev-mainbody")
            .css("width", $(window).width())
            .css("height", $(window).height());
            return $("#ev-events");
        },
        getCalendarContainer : function() {
            return $("#ev-lf-calendar");
        },
        getEventObj : function() {
            return this.getEventsContainer().data("fullCalendar");
        },
        getDatePicker : function() {
            return this.getCalendarContainer().data("datepicker");
        },
        /**可替换为任意Div元素*/
        getEventInputContainer:function(){
            return jQuery("#ev-input-container");
        },
        changeView : function(s) {
            this.getEventObj().changeView(s);
        },
        setDate : function(o) {
            this.getEventObj().setDate(o.year, o.month, o.day);
        },
        addMgrBtn : function() {
            var tmpObj = this.getCalendarContainer()
                    .find(".ui-datepicker-buttonpane :button");
            var elemArray = this.getEventObj().getViewElem();
            for (var i = 0, len = elemArray.length; i < len; i++) {
                var s = $(this.generalBtnHtml(elemArray[i]));
                s.click(_btnEvent);
                tmpObj.after(s);
            }
        },
        addedEvent:function(e) {
            this.getEventObj().renderEvent(e);
        },
        updateEvent:function(e) {
            this.getEventObj().updateEvent(e);
        },
        getEvent:function(e) {
            this.getEventObj().updateEvent(e);
        },
        delEvent:function(e) {
            this.getEventObj().removeEvents(e);
        },
        generalBtnHtml : function(obj) {
            var rtnHTML = '<button type="button" class="ui-datepicker-current ui-state-default ui-priority-secondary ui-corner-all ui-state-hover" id="@K@">@V@</button>';
            return rtnHTML.replace(/@K@/, obj.key + "btn").replace(/@V@/g,
                    obj.value);
        },
        getFetchEventData:function(){
            return (function(rangeStart, rangeEnd, callback) {
                eventDataFetch.queryEvents({start:$.fullCalendar.formatDate(rangeStart,'yyyy-MM-dd'), end:$.fullCalendar.formatDate(rangeEnd, 'yyyy-MM-dd')}, callback);
            });
        },
        printCalendarProperties:function(name, obj) {
            var tmpArray = [name];
            var count = 1;
            for (var s in obj) {
                tmpArray.push(s + ":" + obj[s]);
                if (count++ % 10 == 0) {
                    tmpArray.push("\n");
                }
            }
            alert("call printCalendarProperties method: " + tmpArray.join(","));
        }
    };
}();
