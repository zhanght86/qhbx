/** 手机卡片页面渲染引擎 */
GLOBAL.namespace("mb.vi");
mb.vi.cardView = function(options) {
   var defaults = {
		"id":"cardview",
		"act":"cardRead" //只读
   };
   this.opts = jQuery.extend(defaults,options);
   
   this.id = this.opts.id;  				//card page id
   this.servId = this.opts.sId;				//card 服务ID
   this.pkCode = this.opts.pkCode;  		//card 数据主键
   this.pId = this.opts.pId					//list item id
   this.cardAct = this.opts.act;			//card 操作
   this.secondStep = this.opts.secondStep;
   this.niId = this.opts.niId;				//流程节点ID
   this.sendId = this.opts.sendId;			//分发ID
   this.ownerCode = this.opts.ownerCode;	//数据所有人
};

/*
 * 显示卡片页面，主方法
 */
mb.vi.cardView.prototype.show = function() {
   var _self = this;
   _self._bldPageLayout();
   _self._afterLoad();
   $.mobile.loading( "show", {
	   text: "加载中……",
	   textVisible: true,
	   textonly: false 
   });
   _self._initMainData().then(function(){
	   	_self._bldCardLayout();	
   		//TODO 有待提取方法
	   _self._resetBtn();
	   _self.pageWrp.enhanceWithin();
   }).catch(function(err){
		console.log(err);
   }).finally(function(){
		$.mobile.loading( "hide" );
   }); 
};
mb.vi.cardView.prototype._render = function() {
   var _self = this;
   $.mobile.loading( "show", {
	   text: "加载中……",
	   textVisible: true,
	   textonly: false 
   });

   _self._initMainData().then(function(){
	   	_self._bldCardLayout();	
   		//TODO 有待提取方法
	   _self._resetBtn();
	   _self.pageWrp.enhanceWithin();
   }).catch(function(err){
//		console.log(err);
   }).finally(function(){
		$.mobile.loading( "hide" );
   });
};
mb.vi.cardView.prototype._refresh = function() {
	 var _self = this;
	 _self.contentWrp.empty();
	 _self.footerWrp.empty();
	 _self._render();
};
/**
 * 初始化数据
 */
mb.vi.cardView.prototype._initMainData = function() {
	var _self = this , param ={};
	var cachedServData = FireFly.cache[this.servId + "-" + FireFly.servMainData]; 
	
	if(cachedServData) {
		_self._servData = cachedServData;
	} else {//获取serv data
		param["LOAD_SERV_DATA"] = true;
	} 

	if (this.niId) {
		param["NI_ID"] = this.niId;
		param["_AGENT_USER_"] = this.ownerCode;
	}
	if (this.sendId) {
		param["SEND_ID"] = this.sendId;
	}
	//一次性获取serv/form/mind数据,减少与服务器连接
	return FireFly.byId4Card(this.servId, this.pkCode,param).then(function(result){
		_self._data = result;
		if (result["serv"] ) {
			_self._servData = result["serv"];
			FireFly.setCache(_self.servId,FireFly.servMainData, result["serv"]);
		} 
		_self._formData = result["form"];//form
		 
		_self._fileData = result["file"]? result["file"]["_DATA_"] : [];//文件
		 
		_self._mind = _self._data["mind"] || {}; //意见

		//添加服务名称
		_self.servName = _self._servData.SERV_NAME || "";
		_self.headerWrp.find("h1").text(_self.servName);	
	});
     
};
/**
 * 设置布局
 */
mb.vi.cardView.prototype._bldPageLayout = function() {
	var _self = this;
	this.pageWrp = $("#"+this.id);
	this.headerWrp = $("#"+this.id+"_header");
	/*this.headerWrp.on("vclick",".js-refresh", function(event){
		event.preventDefault();
		event.stopImmediatePropagation();
		_self._refresh();
	});*/
	this.contentWrp = $("#"+this.id+"_content");
	this.footerWrp = $("#"+this.id+"_footer");
};

/**
 * 构建卡片布局，包括form和下方关联的功能
 */
mb.vi.cardView.prototype._bldCardLayout = function() {
	var _self = this;
	//构建按钮
	this._bldBtnBar();
	//构建form
	this._bldForm();
};

/**
 * 构建按钮
 */
mb.vi.cardView.prototype._bldBtnBar = function() {
	this.btns = {};
    //每屏按钮数量限制为3
    this.btnCountLimit = 4;
    this.footerNavBar = $("<div data-role='navbar' class='customNav'></div>").appendTo(this.footerWrp);
    this.footerNavWrp = $("<ul></ul>").appendTo(this.footerNavBar);
};

/**
 * 添加按钮,每屏显现4个按钮：3个功能按钮1个更多，其余按钮点击更多现实在popup页面内
 */
mb.vi.cardView.prototype.addBtn = function(nodeCon) {
    var _self = this;
    var count = this.footerWrp.find("li").length;
    
	if (count < this.btnCountLimit ) {//添加到导航
		nodeCon.appendTo(this.footerNavWrp);
	} else if (count == this.btnCountLimit) {//更多按钮
		var nextWrp = jQuery("<li class='next'><a href='#' data-icon='arrow-r'>下一页</a></li>");
			nextWrp.appendTo(this.footerNavWrp);

		this.footerNavBar = jQuery("<div data-role='navbar' class='customNav ui-hide'></div>").appendTo(this.footerWrp);
	    this.footerNavWrp = jQuery("<ul></ul>").appendTo(this.footerNavBar);	
		var prevWrp = jQuery("<li class='prev ui-hide'><a href='#' data-icon='arrow-l'>上一页</a></li>");
			prevWrp.appendTo(this.footerNavWrp);
		
		this.footerWrp.on("vclick",".next,.prev", function(){
			$(this).closest(".customNav").parent().find(".customNav").toggleClass("ui-hide");
		});
		nodeCon.appendTo(this.footerNavWrp);
	} else {
		nodeCon.appendTo(this.footerNavWrp); 
	}
};
/**
 * 构建卡片form，调用mb.ui.form组件
 */
mb.vi.cardView.prototype._bldForm = function() {
    var _self = this;
	
    //1.渲染form
    var opts = {
		"sId":this.servId,
		"pkCode":this.pkCode,
		"data" : this._servData,
		"parHandler":this,
		"pCon":this.contentWrp 
	}
	this.form = new mb.ui.form(opts);
    this.form.render();
    //1.1 card只读时,填充form数据
    if (this.cardAct == UIConst.ACT_CARD_READ) {
    	this.form.fillData(this._formData,this._fileData);
    }
    //2.工作流控制
	if (typeof(_self.itemValue("S_WF_INST")) != "undefined" && 
			   _self.itemValue("S_WF_INST").length > 0 ) {
	    var wfparam = {};
		    wfparam["sId"] = this.servId;
		    wfparam["pkCode"] = this.pkCode;
		    wfparam["parHandler"] = this;
        if(this.secondStep!="toread"){
            this.wfCard = new mb.vi.wfCardView(wfparam);
            this.wfCard.render();
        }
    }
	//3.加载外部脚本
	this._excuteProjectJS();
    
    
};
/**
 * 页面构造完成后，引擎执行动作
 */
mb.vi.cardView.prototype._afterLoad = function() {
	var _self = this;
	this._guideTo(this.pkCode);
};
/**
 * 如果没有按钮,清除navbar
 */
mb.vi.cardView.prototype._resetBtn = function() {
	var len = this.footerWrp.find("li").length;
	if(!len) {
		this.footerWrp.empty();
	}
}
/**
 * 跳转至该页面
 */
mb.vi.cardView.prototype._guideTo = function(id) {
	this.pageWrp.page().enhanceWithin();
	$.mobile.pageContainer.pagecontainer( "change", this.pageWrp );
};

/**
 * 刷新卡片页面
 */
mb.vi.cardView.prototype.refresh = function() {
	this.contentWrp.empty();
	this._bldCardLayout();
	this._afterLoad();
};
/**
 * 刷新卡片页面
 */
mb.vi.cardView.prototype.back = function() {
	 $.mobile.back();
};
/**
 * 卡片加载后执行工程级js方法
 */
mb.vi.cardView.prototype._excuteProjectJS = function() {
	
	
	var _self = this;
    this._servPId = this._servData.SERV_PID || "";//父服务ID
    var loadArray = this._servData.SERV_CARD_LOAD_NAMES.split(",");
    for (var i = 0;i < loadArray.length;i++) {
    	if (loadArray[i] == "") {
    		return;
    	}
    	load(loadArray[i]);
    }
	
	function load(value) {
		var pathFolder = value.split("_");
		var lowerFolder = pathFolder[0].toLowerCase();
	    var jsFileUrl = FireFly.getContextPath() + "/" + lowerFolder + "/servjs/" + value + "_card_mb.js";
		jQuery.ajax({
	        url: jsFileUrl,
	        type: "GET",
	        dataType: "text",
	        async: false,
	        data: {},
	        success: function(data){
	            try {
	                var servExt = new Function(data);
	                servExt.apply(_self);
	            } catch(e){}
	        },
	        error: function(){
				
	        }
	    });			
	};
};

/**
 * 隐藏指定ID的分组
 * @param id 分组框ITEM_CODE
 */
mb.vi.cardView.prototype.hideGroup = function(itemCode) {
	jQuery("#" + this.pkCode + "_" + itemCode).hide();
};
/**
 * 显示指定ID的分组
 * @param itemCode 分组框ITEM_CODE
 */
mb.vi.cardView.prototype.showGroup = function(itemCode) {
	jQuery("#" + this.pkCode + "_" + itemCode).show();
};

/**
 * 获取当前卡片主键值
 */
mb.vi.cardView.prototype.getPKCode = function() {
    return this.pkCode;
};
 
/**
 * 获取字段对象的方法
 * @param itemCode 字段编码
 */
mb.vi.cardView.prototype.getItem = function(itemCode) {
	var _self = this;
    var item = this.form.getItem(itemCode);
    return item;
};
/**
 *  获取字段值
 *  @param itemCode 字段编码
 */
mb.vi.cardView.prototype.itemValue = function(itemCode) {
	var _self = this;
    return this.form.itemValue(itemCode);
};	

/**
 * 取得SERV_SRC_ID的值，引用自服务（共享附件设置）
 */
mb.vi.cardView.prototype.getServSrcId = function(){
	var result = this._servData.SERV_SRC_ID;
	return result || "";
};
/**
 * 获取业务数据，修改时获取的是业务数据，添加的时候获取的是默认值
 */
mb.vi.cardView.prototype.getByIdData = function(itemCode) {
    return this._formData[itemCode] || "";
};
/**
 * 获取字段中的配置 
 * @param itemCode 字段编码
 */
mb.vi.cardView.prototype.getItemConfig = function(itemCode) {
	var _self = this;
    var itemConfig = _self._servData.ITEMS[itemCode].ITEM_INPUT_CONFIG;
    return itemConfig;
};

//TODO this._data更改为this._servData

/**
 * 设置服务数据
 * @param data 服务数据
 */
mb.vi.cardView.prototype.setServData = function(data) {
	if(!this._servData) {
		this._servData = data;
	}
};
/**
 * 获取服务数据
 * @param data 服务数据
 */
mb.vi.cardView.prototype.getServData = function() {
	return this._servData || undefined;
};
/**
 * 获取服务项数据
 * @param data 服务数据
 */
mb.vi.cardView.prototype.getServItemValue = function(key) {
	return this._servData ? this._servData[key] : undefined;
};
