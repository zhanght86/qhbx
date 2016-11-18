/** 列表页面渲染引擎 */
GLOBAL.namespace("mb.vi");
/*待解决问题：
 * 
 * */
mb.vi.doc = function(options) {
	var defaults = {
		"id":options.sId + "-mbSearchView",
		"sId":"",//服务ID
		"aId":"", //操作ID
		"pCon":null,
		"pId":null,
		"linkWhere":"",
		"extWhere":""
	};
	this.opts = jQuery.extend(defaults,options);
	this._id = this.opts.id;;
	this.servId = this.opts.sId;
    this._pCon = this.opts.pCon;
	this._data = null;	
	this._searchWhere = "";//查询条件
	this._originalWhere = "";//服务定义条件
	this._extendWhere = "";//扩展条件
	this._linkWhere = this.opts.linkWhere;//关联功能过滤条件
		
	
	this.nowPage = 1;
	this.keyStr = "";
	
};
/*
 * 渲染列表主方法
 */
mb.vi.doc.prototype.show = function( k) {
	//this._initMainData();
	this._layout();
	this._query(k);
	this._bldGrid();
	this._bldPage();
	this._afterLoad();
};
mb.vi.doc.prototype._initMainData = function() {
	this._query();
};
/*
 * 刷新
 */
mb.vi.doc.prototype.refresh = function() {
	var _self = this;
	this.input.val("");
	this.btn.click();
};
/*
 * 构建列表页面布局
 */
mb.vi.doc.prototype._layout = function() {
	var _self = this;
	this.searchBar = jQuery("<div></div>").addClass("mbTopBar").appendTo(this._pCon);
    this._bldSearchBar();
    this._bldSeperateArea();
	this.listContainer = jQuery("<div></div>").addClass("mbSearch-container").appendTo(this._pCon);//列表外容器
};
/*
 * 构建列表页面布局
 */
mb.vi.doc.prototype._bldSearchBar = function() {
	var _self = this;
	//默认布局
	this.back = jQuery("<div>返回</div>").addClass("mbTopBar-back mbTopBar-search-back").appendTo(this.searchBar); 
   // this.back.html(UIConst.FONT_STROKE_BACK);
	this.back.bind("click",function() {
		_self.back.addClass("mbTopBar-backActive");
    	//_self.back.html(UIConst.FONT_STROKE_LOAD);
//    	history.go(-1);
		window.location.href = FireFly.getContextPath() + "/sy/comm/desk-mb/desk-mb.jsp";
    });
    
	//keyword input
	this.input = jQuery("<input type='text' value=''></input>").attr("id","inputTxt").addClass("mbSearch-input mb-bottom-right-radius-6").appendTo(this.searchBar);
	//suggest input
	this.suggest = jQuery("<div style='color:black; width: 640px; z-index: 10; position: absolute; left: 75px; top: 40px; display: none;'></div>").attr("id","suggest").appendTo(this.searchBar);
	this.input.keypress(function(event) {
        if (event.keyCode == '13') {
            _self.btn.click();
            return false;
        }
    });
	this.btn = jQuery("<span>查询</span>").addClass("mbTopBar-blueBtn mbSearch-searchBtn").appendTo(this.searchBar);
    //this.btn.html(UIConst.FONT_REG_QUERY);
	this.btn.unbind("click").bind("click",function() {
		if (jQuery("#inputTxt").is(":visible") == true) {
			if (jQuery("#inputTxt").val().length > 0) {
				_self._searchQuery();
			} else {
				jQuery("#inputTxt").hide();				
				jQuery(".mbTopBar-title").show();
			}
		} else {
			jQuery(".mbTopBar-title").hide();
			jQuery("#inputTxt").show().focus();
		}
	});
	jQuery("<div style='width:100%;text-align:center;'></div>").text("文档中心").addClass("mbTopBar-title").appendTo(this.searchBar);
};
/*
 * 构建分类区
 */
mb.vi.doc.prototype._bldSeperateArea = function() {
	var _self = this;
	this.seperate = jQuery("<nav style='display: block;margin-top:44px;width:100%;position:relative'></nav>"); 
	this.sepUl = jQuery("<ul class='doc_nav'></ul>");
	jQuery("<li class='doc_nav_li doc_nav_li--on' wid='0'><a href='#'>首页</a></li>").appendTo(this.sepUl);
	jQuery("<li class='doc_nav_li' wid='25%' tpe='order'><a href='#'>排行</a></li>").appendTo(this.sepUl);
	jQuery("<li class='doc_nav_li' wid='50%' tpe='catalog'><a href='#'>分类</a></li>").appendTo(this.sepUl);
	jQuery("<li class='doc_nav_li' wid='75%' tpe='per'><a href='#'>个人</a></li>").appendTo(this.sepUl);
	this.sepUl.appendTo(this.seperate);
	jQuery("<div id='doc_nav_line' class='doc_nav_line'><b></b></div>").appendTo(this.seperate);
	this.seperate.appendTo(this._pCon);
	jQuery(".doc_nav_li").bind("click",function(event) {
		var obj = jQuery(this);
		jQuery(".doc_nav_li--on").removeClass("doc_nav_li--on");
		obj.addClass("doc_nav_li--on");
		var leftWid = obj.attr("wid");
		jQuery("#doc_nav_line").animate({left: leftWid});
		//根据类别查询数据
		var type = obj.attr("tpe");
		if (type == "per") {
			_self._ajaxQuery(System.getVar("@USER_CODE@"));
		} else if (type == "order") {
			_self._ajaxQuery(null,"DOCUMENT_READ_COUNTER desc");
		} else if (type == "catalog") {
			_self._catalogList();
		} else {
			_self._ajaxQuery();
		}
	});
};
/**ajax search */
mb.vi.doc.prototype._searchQuery = function(){
	var _self = this;
	var keyWord = this.input.val();
	var url = FireFly.getContextPath() + "/sy/plug/search/mbSearchResult.jsp?back=doc&k=" + keyWord;	
	window.location.href = url;	    		   
    return false;
};
mb.vi.doc.prototype._ajaxQuery = function(userCode,order,channelId){
	var _self = this;
	_self.nowPage = 1;
	_self.userCode = userCode;
	_self.order = order;
	_self.channelId = channelId;
	_self._query(userCode,order,channelId);
	_self._bldGrid();
	_self._bldPage();
	_self._afterLoad();
};
mb.vi.doc.prototype._catalogList = function(userCode,order){
	var _self = this;
	this.listContainer.empty();
	this._table = jQuery("<table style='display:none;'></table>").attr("id",this._id).addClass("mbSearch-grid").appendTo(this.listContainer);
    var data = FireFly.getDict("SY_COMM_WENKU_CHNL_MANAGE",null," AND SITE_ID=\'SY_COMM_CMS\'");
    data = data[0].CHILD;
    data = data[0].CHILD;
	var trs = [];
	//默认布局
	var i = 0;
	for (i; i < data.length;i++) {
		var trData = data[i];
		trs.push("<tr><td class='mbGrid-catalog' colSpan=2>" + trData.NAME + "</td></tr>");
		if (trData.CHILD) {
			var childs = trData.CHILD;
			for (var j = 0; j < childs.length;j++) {
				var node = childs[j];
				trs.push(_self._bldCatalogTr(node));
			}
		}
	}
	this.listContainer.find(".mbSearch-grid").append(trs.join(""));
	this._table.css("display:","").fadeIn("slow");
};
mb.vi.doc.prototype._bldCatalogTr = function(node) {
	var _self = this;
	//默认布局
	var tr = [];
	tr.push("<tr class='mbGrid-tr mbGrid-tr-catalog' cid='" + node["ID"] + "'>");
	var i = 0;
	var tdLeft = [];
	tdLeft.push("<td class='mbGrid-td-left'>");
	tdLeft.push("</td>");
	var tdRight = [];
	tdRight.push("<td class='mbGrid-td-right'");
	
	var one = [];
	var two = [];
	var attList = [];
	one.push("<div>");//第一行
	two.push("<div>");//第二行
	var icon = [];
	icon.push("<span class='mb-icon-span mb-right-nav'></span>");
	if (node["NAME"]) {//标题
		one.push("<span class='mbGrid-td-weight'>");
		var tit = node["NAME"];
		if (tit.length > 9) {
			tit = tit.substring(0,10) + ".."
		}
		one.push(tit);
		one.push("</span>");
	} 
	
	
	one.push("</div>");
	two.push("</div>");
	
	tdRight.push(" uid='");
	tdRight.push(node["id"]);
	tdRight.push("' url='");
	tdRight.push(node["url"]);
	tdRight.push("'>");
	tdRight.push(one.join(""));
	tdRight.push(two.join(""));
	tdRight.push(icon.join(""));
	tdRight.push("</td>");
	
	tr.push(tdLeft.join(""));
	tr.push(tdRight.join(""));
	tr.push("</tr>");
	return tr.join("");
};
/*
 * userCode:用户编码
 * order:排序字段
 */
mb.vi.doc.prototype._query = function(userCode,order,channelId){
	var keywords = "";
	var inputVal = "";
	
	var data = {};
	data["page"] = this.nowPage;
	data["count"] = 20;
	if (userCode) {
		data["userId"] = userCode;
	}
	if (order) {
		data["order"] = order;
	}
	if (channelId) {
		data["channelId"] = channelId;
	}
	this.data = FireFly.doAct("SY_COMM_WENKU", "getDocumentList", data);
    this._DATA = this.data._DATA_;
};
/*
 * 构建列表页面布局
 */
mb.vi.doc.prototype._bldGrid = function() {
	var _self = this;
	this.listContainer.empty();
	this._table = jQuery("<table style='display:none;'></table>").attr("id",this._id).addClass("mbSearch-grid").appendTo(this.listContainer);
	this._bldTrs(this._DATA);
	this._table.css("display:","").fadeIn("slow");
};
mb.vi.doc.prototype._bldTrs = function(data) {
	var _self = this;
    var trs = [];
	//默认布局
	var i = 0;
	for (i; i < data.length;i++) {
		var trData = data[i];
		trs.push(_self._bldTr(trData));
	}
	this.listContainer.find(".mbSearch-grid").append(trs.join(""));
};
mb.vi.doc.prototype._bldTr = function(node) {
	var _self = this;
	//默认布局
	var tr = [];
	tr.push("<tr class='mbGrid-tr' uid='" + node["DOCUMENT_ID"] + "'>");
	var i = 0;
	var tdLeft = [];
	tdLeft.push("<td class='mbGrid-td-left'>");
	tdLeft.push("<a class='mbGrid-doc-img " + _self._getDocType(node["DOCUMENT_FILE_SUFFIX"]) + "'></a>");//第一行
	tdLeft.push("</td>");
	var tdRight = [];
	tdRight.push("<td class='mbGrid-td-right'");
	
	var one = [];
	var two = [];
	var attList = [];
	one.push("<div>");//第一行
	two.push("<div>");//第二行
	attList.push("<div>");//附件列表
	var icon = [];
	icon.push("<span class='mb-icon-span mb-right-nav'></span>");
	if (node["DOCUMENT_TITLE"]) {//标题
		one.push("<span class='mbGrid-td-weight'>");
		var tit = node["DOCUMENT_TITLE"];
		if (tit.length > 9) {
			tit = tit.substring(0,10) + ".."
		}
		one.push(tit);
		one.push("</span>");
	} 
	if (node["DOCUMENT_ID"]) {//摘要
		two.push("<span class='mbGrid-td-span'>");
		//two.push(node["DOCUMENT_ID"]);
		two.push(node["S_UNAME"] + " ");
		two.push(node["DOCUMENT_READ_COUNTER"] + "人阅读 ");
		two.push("大小：" + _self._getDocSize(node["DOCUMENT_FILE_SIZE"]));
		two.push("</span>");		
	}
	
	//显示附件列表
//	jQuery.each(node["attachment"],function(index, att) {
//		var attUrl = att["att_path"];
//		
//		 if (attUrl.substr(0,6) == "/file/") {
//			 attUrl = attUrl.substring(6, attUrl.length);
//		 }
//		
//		attUrl = FireFly.getContextPath() + "/file/" + encodeURIComponent(attUrl) + "?act=preview";
//		attList.push("<a class='aOpenFile' href='# ' url='" + attUrl +"' >");
//		attList.push(att["att_title"]);
//		attList.push("</a>");
//		attList.push("<br>");
//	});
	
	one.push("</div>");
	two.push("</div>");
	attList.push("</div>");
	
	tdRight.push(" uid='");
	tdRight.push(node["id"]);
	tdRight.push("' url='");
	tdRight.push(node["url"]);
	tdRight.push("'>");
	tdRight.push(one.join(""));
	tdRight.push(two.join(""));
	tdRight.push(attList.join(""));
	tdRight.push(icon.join(""));
	tdRight.push("</td>");
	
	tr.push(tdLeft.join(""));
	tr.push(tdRight.join(""));
	tr.push("</tr>");
	return tr.join("");
};
mb.vi.doc.prototype._openLayer = function(uid) {
	var readOnly = false;//this.readOnly;
	this.url = FireFly.getContextPath() + "/sy/comm/wenku/mb/mbDocCardView.jsp?sId=&pkCode=" + uid;		
	window.location.href = this.url;
};
/*
 * 构建列表页面布局
 */
mb.vi.doc.prototype.morePend = function(options) {
	var _self = this;
	this.nowPage = parseInt(this.nowPage) + 1;
	this._query(this.userCode,this.order,this.channelId);
	this._bldTrs(this._DATA);
	this._bldPage();
	this._afterLoad();
};

mb.vi.doc.prototype._getUnId = function(id) {

};
/*
 * 构建翻页
 */
mb.vi.doc.prototype._bldPage = function() {
	var _self = this;
	var res = this._DATA + "";
	jQuery(".mbGrid-more").remove();
	if (res.length == 0) {
		this._recordOverTip();
	} else {
		this._bldMore();
	}
};
/*
 * 构建翻页
 */
mb.vi.doc.prototype._bldMore = function() {
	var _self = this;
	this.more = jQuery("<div></div>").addClass("mbGrid-more");
	this.more.html("<span>查看更多</span>");
	jQuery("<span></span>").addClass("mbGrid-more-icon mb-down-nav").appendTo(this.more);
	this.more.appendTo(this.listContainer);
	this.listContainer.undelegate( "click" ).delegate(".mbGrid-more","click",function() {
		_self.morePend();
		return false;
	});
};

/*
 * 加载完毕提示
 */
mb.vi.doc.prototype._recordOverTip = function() {
	var _self = this;
	this.overTip = jQuery("<div></div>").addClass("mbGrid-more mbGrid-overTip mb-radius-bottom-20 mb-shadow-down-6");
	this.overTip.text("全部数据已加载！");
	var toTop = jQuery("<span>回到顶部</span>").addClass("mbGrid-toTop").appendTo(this.overTip);
	this.overTip.appendTo(this.listContainer);
	this.listContainer.delegate(".mbGrid-toTop","click",function() {
		window.scrollTo(0,0);
	});
};
/*
 * 加载完毕提示
 */
mb.vi.doc.prototype._replaceKey = function(allValue) {
	var _self = this;
	var value = this.input.val();
	if (value.length > 0) {
		//value = eval("allValue.replace(/" + value + "/g,\"<label class='mbSearch-redKey'>" + value + "</label>");
		//alert(value);
		return value;
	} else {
		return "";
	}
};
/*
 * 构建翻页
 */
mb.vi.doc.prototype.getBlocks = function() {
	var _self = this;
	return _self.listContainer.find(".mbGrid-td-right");
};
/*
 * 构建翻页
 */
mb.vi.doc.prototype.trClick = function() {
	var _self = this;
	_self.listContainer.delegate(".mbGrid-tr","mousedown",function(event) {
		var node = jQuery(this);
    	if (node.hasClass("mbGrid-tr-catalog")) {
    		_self._ajaxQuery(null,null,node.attr("cid"));
    		return false;
    	} else {
    		var uid = node.attr("uid");
			_self._openLayer(uid);
		    return false; 
    	}
	});
};
/*
 * 加载后执行
 */
mb.vi.doc.prototype._afterLoad = function() {
	var _self = this;
//    var nowPage = this._lPage.NOWPAGE;
//    var pages = this._lPage.PAGES;
//    if (nowPage == pages) {
//    	_self.listContainer.find(".mbGrid-more").hide();
//    	_self._recordOverTip();
//    }
    this.trClick();
//    //匹配高亮
//    var value = this.input.val()
//    if (value.length > 0) {
//    	jQuery(".mbSearch-container").textSearch(value,{markColor: "red"});
//    }
//    
//    //附件
//    jQuery(".aOpenFile").each(function(i,n) {
//    	var url = jQuery(n).attr("url");
//    	jQuery(n).bind("mousedown",function(event) {
//    		event.stopPropagation();
//    		window.open(url);
//    		
//    	});
//    });

};
/*
 * 获取文档类别
 */
mb.vi.doc.prototype._getDocType = function(stuff) {
	var _self = this;
	var stuffArray = ["doc","docx","txt","pdf","epub","ppt","pptx","xls","xlsx"];
	var stuffClass = "doc_";
    if (jQuery.inArray(stuff,stuffArray) == -1) {
    	stuffClass += "unknown";
    } else {
    	stuffClass += stuff;
    }
    return stuffClass;
};
/*
 * 获取文档类别
 */
mb.vi.doc.prototype._getDocSize = function(size) {
	var _self = this;
	var retSize = "";
	size = size/1024;
	retSize = Math.round(size*100)/100;
	if (retSize >= 1024) {
		retSize = retSize/1024;
		retSize = Math.round(retSize*100)/100;
		retSize += "MB";
	} else {
		retSize += "KB";
	}
    return retSize;
};
