/** 工作台页面渲染引擎 */
GLOBAL.namespace("mb.vi");
/**
 * 构造方法
 */
mb.vi.deskView = function(options) {
	var defaults = {
			"id":options.id + "-mbDeskView"
		};
	this.opts = jQuery.extend(defaults,options);
	this._outNetData = [];//外网提醒地址
	this.iconData = {}; // 手机端显示的菜单
};
/**
 * 展示方法
 */
mb.vi.deskView.prototype.show = function() {
	this._initMainData();    // 初始化数据
	this._bldLayout();       // 构造布局
	this._afterLoad();		 // 加载后执行
};
/**
 * 初始化数据
 */
mb.vi.deskView.prototype._initMainData = function() {
	var _self = this;
	var datas = FireFly.getMenu().TOPMENU; // 获取菜单
	
	// 有数据，将叶子节点菜单添加到iconData中
	if (datas) { 
		var i = 0;
		var len = datas.length;
		for (i; i < len; i++) {
			this._getLeafData(datas[i]);
		}
	}
	
	//获取个性化的设置
	var userCode = System.getUser("USER_CODE");
	var desk = {};
	desk["_NOPAGE_"] = true;
	desk["_searchWhere"] = " and S_USER='" + userCode + "'"
	var icons = FireFly.getListData("SY_ORG_USER_DESK_MB",desk);
	_self.backImg = null;
	_self.deskApps = null;
	if (icons._DATA_[0]) {
		this.deskSetPK = icons._DATA_[0].SD_ID;
		var apps = icons._DATA_[0].SD_APPS;
		var backImg = icons._DATA_[0].SD_BACK_IMG;
		if (jQuery.trim(apps).length > 0) {
			_self.deskApps = jQuery.trim(apps).split(","); // 用户自定义app
		}
		if (jQuery.trim(backImg).length > 0) {
			_self.backImg = jQuery.trim(backImg); // 用户自定义背景
		}
	}
};
/**
 * 构造布局
 */
mb.vi.deskView.prototype._bldLayout = function() {
	var _self = this;
	//构造头信息
	var topCon = $("<div></div>").addClass("page-header");
	var topBar = $('<h1></h1>').appendTo(topCon);
	var logo = $('<span></span>').addClass('top-logo').appendTo(topBar);
	var btnSearch = $('<span class="glyphicon glyphicon-search"></span>').appendTo(topBar);
	var btnConfig = $('<span class="glyphicon glyphicon glyphicon-asterisk"></span>').appendTo(topBar);
	
	topCon.appendTo($('body'));
};
/**
 * 加载后执行
 */
mb.vi.deskView.prototype._afterLoad = function() {};
/**
 * 获取数据的所有叶子节点数据，并存入iconData中
 */
mb.vi.deskView.prototype._getLeafData = function(data) {
	var _self = this;
	var child = data.CHILD;
	if (child) {
		var i = 0;
		var len = child.length;
	    for (i; i < len; i++) {
	    	_self._getLeafData(child[i]);
	    }
	} else {
		var id = data.ID;
		var name = data.NAME;
		var area = data.AREA || "";
		if ((area == '3') || (area == '4')) {//手机显示
			_self.iconData[id] = data; // 将数据存入手机显示data中
		}
	}
};


