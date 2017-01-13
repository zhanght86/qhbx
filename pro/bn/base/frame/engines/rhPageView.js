/** 首页页面渲染引擎 */
GLOBAL.namespace("rh.vi");
/*待解决问题：
 * 
 * */
rh.vi.pageView = function(options) {
	var defaults = {
		"id":options.id + "-viPageView",
		"styleDef":""
	};
	this.opts = jQuery.extend(defaults,options);
	this.homeOpts = this.opts.home || null;
	this.defaultTab = StrToJson(this.opts.defaultTab) || null;// 默认打开的tab
	this._tabColor = this.opts.tabColor || "";
	this._banner = GLOBAL.cookieBanner;
};
/*
 * 渲染主方法
 */
rh.vi.pageView.prototype.show = function() {
	this._initMainData();
	this._layout();
	this._bldMenu();
	this._bldBanner();
	this._bldTabs();
	this._topPannel();
	this._bldBottomPannel();
	this._resetHeiWid();
	this._afterLoad();	
};
/*
 * 准备数据
 */
rh.vi.pageView.prototype._initMainData = function() {
	var _self = this;
	//tab颜色设置信息
	System.setConf("SY_TAB_COLOR",this._tabColor);
	//节假日风格定义优先级高于其它
	var fesl = System.getVar("@C_SY_STYLE_FEST@") || "";
	if (fesl.length > 0) {
		_self._serStyle(StrToJson(fesl));
		return false;
	}
};
rh.vi.pageView.prototype._serStyle = function(res) {
	var def  =  this.opts.styleDef;
	def = StrToJson(def);
	var temp = {};
	if (res && res.SS_ID) {
		temp["SS_ID"] = res.SS_ID;
	}
	if ((res.SS_STYLE_MENU == "" || res.SS_STYLE_MENU == null) && def) {
		temp["SS_STYLE_MENU"] = def.SS_STYLE_MENU;		
	} else {
		temp["SS_STYLE_MENU"] = res.SS_STYLE_MENU;	
	}
	if ((res.SS_STYLE_BACK == "" || res.SS_STYLE_BACK == null) && def) {
		temp["SS_STYLE_BACK"] = def.SS_STYLE_BACK;		
	} else {
		temp["SS_STYLE_BACK"] = res.SS_STYLE_BACK;	
	}
	if ((res.SS_STYLE_BLOCK == "" || res.SS_STYLE_BLOCK == null) && def) {
		temp["SS_STYLE_BLOCK"] = def.SS_STYLE_BLOCK;		
	} else {
		temp["SS_STYLE_BLOCK"] = res.SS_STYLE_BLOCK;	
	}
	jQuery(".pageBody").addClass(temp.SS_STYLE_MENU);
	GLOBAL.style = temp;
};
/*
 * 构建列表页面布局
 */
rh.vi.pageView.prototype._layout = function() {
	
};
/*
 * 初始化菜单
 */
rh.vi.pageView.prototype._bldMenu = function() {
	
};
/*
 * 构造banner
 */
rh.vi.pageView.prototype._bldBanner = function() {
	var userCode = jQuery(".user_info");
	//alert(System.getVar("@JIAN_CODES@").length > 0);
	if (System.getVar("@JIAN_CODES@").length > 0) {
		var jianGang = jQuery("<div id='jiangang' style='display:inline-block;position:relative;'></div>").html("(<a href='javascript:void(0);' style='margin-left:15px;' >兼岗</a>)").addClass("left-topBar-jian-text").appendTo(userCode);
		jianGang.bind("click", function() {
			var obj = jQuery(".left-topBar-jian-list");
			if (obj.length == 1) {
				if (jQuery(".left-topBar-jian-list:visible").length == 1) {
					jQuery(".left-topBar-jian-list:visible").hide("normal");
				} else {
					jQuery(".left-topBar-jian-list").show("normal");
				}
			} else {
				var jianList = jQuery("<ul></ul>").addClass("left-topBar-jian-list");
				var data = FireFly.doAct("SY_ORG_LOGIN","getJianUsers",null,false);
				jQuery.each(data._DATA_,function(i,n) {
					var content = jQuery("<a href='#'>" + n.DEPT_NAME+"  "+n.USER_NAME + "  (" + n.TODO_COUNT + "条)" + "</a>");
					content.bind("click",function(event) {
						var result = FireFly.doAct("SY_ORG_LOGIN","changeUser",{"TO_USER_CODE":n.USER_CODE},false);
						if (result[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {
							var res = confirm("当前页面将刷新，确定继续吗？");
							if (res === true) {
								window.location.reload();
								//_self._refresh();
							}
					    } 
						event.stopPropagation();
					});
					jQuery("<li></li>").append(content).appendTo(jianList);
				});
				jianList.appendTo(jianGang);
				jianList.show("normal");
				jianList.bind("mouseleave",function(event) {
					jianList.hide("slow");
				});
			}
		});
	}
};
/*
 * 构造tabs
 */
rh.vi.pageView.prototype._bldTabs = function() {
	var _self = this;
	//初始化tabs
	jQuery("#homeTabs").tabs({});
	var platformPage = jQuery("#platformPage");
	var platformFrame = jQuery("#platformFrame");
	var tabUL = jQuery(".tabUL");
	//jQuery("<a href='javascript:void(0)' id='loginOut' title='退出' class='rh-head-icons'/>").appendTo(tabUL);
	
	jQuery("#loginOut").on("click", function(event) {//退出
	       var resultData = FireFly.logout();
	       if (resultData[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {
	    	   if (_self.opts.rhClient == "true" || _self.opts.rhClient == true) {
	    		   document.title = "RhClientAction_Close";
	    	   } else {
	    		   // 由logout.jsp去决定退出该导向到哪个页面
	    		   Tools.redirect(FireFly.getContextPath() + "/logout.jsp");  
	    	   }
	       }
	       event.stopPropagation();
	       return false;
	});

	//tab下边框浮动图片
	//var bottom = jQuery("<div></div>").addClass("rh-head-bottom");
	//bottom.prependTo(jQuery(".ui-widget-header"));
	//bottom.appendTo(jQuery(".tabDiv"));
	if (jQuery.isEmptyObject(this.defaultTab)) {
	} else {
		var tabs = this.defaultTab;
		var len = tabs.length;
		for (var i = 0; i < len; i++) {
			var tabId = tabs[i]["TAB_ID"];
			var tabName = tabs[i]["TAB_NAME"];
			var tabUrl = tabs[i]["TAB_URL"];
			var tabIcon = tabs[i]["TAB_ICON"];
			var tabOpen = tabs[i]["TAB_OPEN"];
			var tabRefresh = tabs[i]["TAB_REFRESH"];
			var tabContainer = jQuery("#" + tabId);
			var iframeObj = jQuery("<iframe id='" + tabId + "' name='" + tabId + "' border='0' scrolling=no width=100% height=100% frameborder='0'></iframe>").addClass("tabFrame").appendTo(tabContainer);
			//信息平台Tab初始化
			(function(iframeObj, tabId, tabUrl, tabRefresh){
				jQuery("a[href='#" + tabId + "']").bind("click",function() {
					// 点击之后才加载
					var src = iframeObj.attr("src");
					if ((!src || src.length == 0) || tabRefresh == "true") {
						iframeObj.attr("src", FireFly.contextPath + tabUrl);
					}
					jQuery(".left-homeMenu").hide();
					//jQuery("#" + tabId).width("100%");
					//jQuery("#" + tabId).height(GLOBAL.getDefaultFrameHei());
					Tab.setFrameHei();
					Tab.barRevert();
				});
			})(iframeObj, tabId, tabUrl, tabRefresh);
			if (tabOpen == "true") {
				jQuery("a[href='#" + tabId + "']").click();
			}
			//insertTabIcon(jQuery("a[href='#" + tabId + "']").find("span"), tabIcon);//增加前置图标
		}
	}
};
/*
 * 构造下拉面板的主方法
 */
rh.vi.pageView.prototype._topPannel = function(){
};
/*
 * 构建下拉面板的初始化操作
 */
rh.vi.pageView.prototype._preBldTopPannel = function(){
};
/*
 * 构造消息下拉面板
 */
rh.vi.pageView.prototype._bldTopPannel = function() {
};
/*
 * 调用后台获取数据，并增加到下拉面板
 */
rh.vi.pageView.prototype.alertDataInsert = function(){
};
/*
 * 下拉面板中添加【更多】链接
 */
rh.vi.pageView.prototype._addMore = function(){
};

/*
 * 下拉面板响应事件操作
 */
rh.vi.pageView.prototype._posBldTopPannel = function(){
};

/*
 * 下拉面板增加一条提醒
 */
rh.vi.pageView.prototype._addAlert = function(n) {
};
/*
 * 点击下拉面板某一标题跳转
 */
rh.vi.pageView.prototype._goInToDo = function(sId, title, url, pkCode) {
};

/**
 * 构造风格样式设置弹出框
 */
rh.vi.pageView.prototype._bldBoxHtml = function() {
	var appboxHtml = '';
	return appboxHtml;
};
rh.vi.pageView.prototype._activeTab = function (obj) {
	jQuery(".rh-dialog-con-right").show();
	jQuery(".rhDesk-btnActive").removeClass("rhDesk-btnActive ");
    jQuery(".rhDesk-lineActive").removeClass("rhDesk-lineActive ");
 	obj.addClass("rhDesk-btnActive");
 	obj.next().addClass("rhDesk-lineActive");
};
/*
 * 构造风格定义面板
 */
rh.vi.pageView.prototype._bldBottomPannelLayout = function() {
};
/*
 * 构造风格定义面板
 */
rh.vi.pageView.prototype._bldBottomPannel = function() {
};
/*
 * 风格样式保存
 */
rh.vi.pageView.prototype._styleSave = function(opts,refreshFlag) {
};
/*
 * 风格样式删除
 */
rh.vi.pageView.prototype._styleDelete = function() {
};
/*
 *修改高度
 */
rh.vi.pageView.prototype._resetHeiWid = function() {
	
};
/*
 * 刷新当前页面
 */
rh.vi.pageView.prototype._refresh = function() {
	window.top.location.reload();
};
/*
 *页面渲染后
 */
rh.vi.pageView.prototype._afterLoad = function() {
};

/**
 * 追加参数
 * @param {Object} params
 */
rh.vi.pageView.prototype.addOpenTabParams = function(params){
	var _self = this;
	_self._openTabParams = params;
};

/**
 * 格式化参数
 */
rh.vi.pageView.prototype.extendOpenTabParams = function(){
	var _self = this;
	if (_self._openTabParams && _self._openTabParams != "null") { //页面url有扩展参数
		var openTabObj  = StrToJson(_self._openTab);
		for (var i in openTabObj) {
			if (i == "params") {
				openTabObj[i] = jQuery.extend(openTabObj[i], _self._openTabParams);
			}
		}
		_self._openTab = jQuery.toJSON(openTabObj);
	}
};
