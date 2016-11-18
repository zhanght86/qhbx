/** 列表页面渲染引擎 */
GLOBAL.namespace("mb.vi");
/*待解决问题：
 * 
 * */
mb.vi.docCard = function(options) {
	var defaults = {
		"id":options.sId + "-mbDocCardView",
		"pkCode":"",
		"sId":"",//服务ID
		"aId":"", //操作ID
		"pCon":null,
		"linkWhere":"",
		"extWhere":""
	};
	this.opts = jQuery.extend(defaults,options);
	this._id = this.opts.id;;
	this.servId = this.opts.sId;
	this.pkCode = this.opts.pkCode;
	this._data = null;	
};
/*
 * 渲染列表主方法
 */
mb.vi.docCard.prototype.show = function( k) {
	//this._layout();
	this._bldCard();
	this._afterLoad();
};
/*
 * 刷新
 */
mb.vi.docCard.prototype.refresh = function() {
	var _self = this;
};
/*
 * 构建列表页面布局
 */
mb.vi.docCard.prototype._layout = function() {
	var _self = this;
	this.listContainer = jQuery("<div></div>").addClass("mbSearch-container").appendTo(this._pCon);//列表外容器
};

mb.vi.docCard.prototype._bldCard = function(){
	var _self = this;
	//获取内容填充进卡片
	this._byIdDoc();
};
mb.vi.docCard.prototype._byIdDoc = function(){
	var data = {};
	data["_PK_"] = this.pkCode;
	this.data = FireFly.doAct("SY_COMM_WENKU_DOCUMENT", "byid", data);
    window.location.href = FireFly.getContextPath() + "/file/" + this.data.DOCUMENT_FILE + "?act=preview";
};

mb.vi.docCard.prototype._getUnId = function(id) {
};

/*
 * 加载后执行
 */
mb.vi.docCard.prototype._afterLoad = function() {
	var _self = this;

};
