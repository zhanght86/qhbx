/**
 * 列表页面渲染引擎
 */
GLOBAL.namespace('mb.vi');
/**
 * 构造方法
 */
mb.vi.listView = function(options) {
	var defaults = {
		"id": "listview",
		"sId": "", // 服务ID
		"aId": "", // 操作ID
		"pCon": null,
		"pId": null,
		"extWhere": "",
		"type": null,
		"parHandler": null, // 主卡片的句柄
		"readOnly": false // 页面只读标识
	};
	this.opts = $.extend(defaults, options);
	this.id = this.opts.id;
	this.servId = this.opts.sId;
	this.headerTitle = this.opts.headerTitle;
	this._data = null;
	this._extendWhere = this.opts.extWhere; // 扩展查询条件
	this._readOnly = this.opts.readOnly;
	this._params = this.opts.params || "";
	this._secondStep = this.opts.secondStep; // 三级页面打开类型：card || 普通页面
	
	this.topTitle = null;
	this.PAGE = {};
};
mb.vi.listView.prototype.show = function() {
	var _self = this;
	var dfd = $.Deferred(); // 延时对象
	_self._layout(); // 构造布局
	_self._afterLoad(); // 加载后执行
	$.mobile.loading("show", {
		text: "加载中...",
		textVisible: true,
		textonly: false
	});
	_self._initMainData().then(function() {
		// 成功
		_self._bldGrid();
	}, function(reason) {
		// 失败
		console.log(reason);
	});
	$.mobile.loading('hide');
	 
	// 这段压缩的时候报错
	/*.catch(function(exception) {
		console.log(exception);
	}).finally(function() {
		$.mobile.loading("hide");
	});*/
};
/**
 * 构建列表页面布局
 */
mb.vi.listView.prototype._layout = function() {
	var _self = this;
	this.pageWrp = $('#' + this.id);
	this.headerWrp = $('#' + this.id + '_header');
	this.headerWrp.find('h2').html(this.headerTitle);
	this.contentWrp = $('#' + this.id + '_content');
	this.contentWrp.empty();
};
/**
 * 加载下一页数据
 */
mb.vi.listView.prototype.morePend = function(options) {
	var _self = this;
	if (options && options._PAGE_) {
		_self.PAGE["_PAGE_"] = options._PAGE_;
	}
	var data = {};
	if (this._extendWhere.length > 0) {
		data["_extWhere"] = this._extendWhere;
	}
	data = $.extend({}, _self.PAGE, data); // 合并分页信息
	if (options && options._NOPAGEFLAG_ && (options._NOPAGEFLAG_ == 'true')) { // 删除分页信息
		delete data._PAGE_;
		delete data._NOPAGEFLAG_;
	}
	
	console.log('当前查询的列表数据的服务ID: ' + this.servId);
	FireFly.getPageData(this.servId, data).then(function(result) {
		_self._listData = result;
		_self.grid._morePend(_self._listData);
		_self.grid.click(_self._openLayer, _self);
		_self._afterLoad();
		
	});
};
/**
 * 转换页面到新构建的列表页面
 */
mb.vi.listView.prototype._afterLoad = function() {
	$.mobile.pageContainer.pagecontainer("change", this.pageWrp);
};
mb.vi.listView.prototype._initMainData = function() {
	var _self = this;
	// 获取服务定义
	function getServData() {
		return FireFly.getCache(_self.servId, FireFly.servMainData).then(function(result) {
			// 获取表结构
			_self._data = result;
			// 服务名称
			_self.servName = result["SERV_NAME"];
		});
	};
	// 获取表中一页的数据
	function getPageData() {
		var options = {
			"_PAGE_": {"SHOWNUM": "20", "NOWPAGE": "1"}	
		};
		if (_self._extendWhere.length > 0) {
			options["_extWhere"] = _self._extendWhere;
		}
		// 获取表数据
		return FireFly.getPageData(_self.servId, options).then(function(result) {
			_self._listData = result;
		});
	};
	// 先执行getServData,成功后再执行getPageData，然后回写返回值
	return getServData().then(getPageData);
};
/**
 * 构建列表页面布局
 */
mb.vi.listView.prototype._bldGrid = function() {
	var temp = {"id": this.servId, "mainData": this._data, "parHandler": this};
		temp["listData"] = this._listData;
	
	this.grid = new mb.ui.grid(temp);
	this.grid.render();
	
	this.grid.click(this._openLayer, this);
};
/**
 * 列表页单击后打开方式
 */
mb.vi.listView.prototype._openLayer = function(pkCode, node) {
	var param = {},
		data = {};
	if (this._secondStep == "card") { // 三级打开方式为卡片
		
		if (node['TODO_CODE'] == 'TODO_REMIND') { // 如果是提醒类待办
			// dialog ID
			var alertId = 'alertId'; 
			// dialog 配置项
			var setting = {
					'selectCallBack': function() {
						var data = {};
						data[UIConst.PK_KEY] = node['TODO_ID'];
						data["TODO_ID"] = node['TODO_ID'];
						FireFly.doAct("SY_COMM_TODO","endReadCon",data).then(function(result) {
							if (result['_MSG_'].indexOf('OK') >= 0) {
								// 删除脏数据
								$.mobile.pageContainer.find('#' + node['_PK_']).remove();
								// 关闭对话框
								$('body').find('#' + alertId).dialog('close');
							} else {
								alert('待办提醒处理失败,请联系管理员!');
							}
						});
					},
					'cancelCallBack': function() {
						// 关闭对话框
						$('body').find('#' + alertId).dialog('close');
					}
			};
			// alert帮助类,打开alert
			AlertHelper.openAlert(alertId, $('body'), node['TODO_TITLE'], node['TODO_CONTENT'], setting);
			
		} else { // 非提醒类待办
			data["sId"] = node["SERV_ID"]; // 服务ID
			data["pkCode"] = node["TODO_OBJECT_ID1"]; // 文儿ID
			data["ownerCode"] = node["OWNER_CODE"]; // 待办所属人
			
			if (node['SERV_ID'] == 'OA_GW_TYPE_FW_SEND') { // 如果是系统来文
				data["niId"] = ''; // 流程ID
				data['sendId'] = node['TODO_OBJECT_ID2']; // 分发ID
			} else {
				data["niId"] = node["TODO_OBJECT_ID2"]; // 流程ID
				data['sendId'] = ''; // 分发ID
			}
//		    data["niId"] = node["TODO_OBJECT_ID2"]; // 流程ID
//			data['sendId'] = node['TODO_OBJECT_ID2']; // 分发ID
			data["readOnly"] = false;
			data["pId"] = pkCode;
			data["act"] = UIConst.ACT_CARD_READ;
			(function(params) {
				var cardView = new mb.vi.cardView(params); // TODO 没有写cardView
				cardView.show();
			}(data));
		}
	} else if (this._secondStep == "toread") { // 待阅的打开方式
		// 打开待办的时候先判断一下是否有主键，许多问题都是因此引起的
		if (!node['TODO_OBJECT_ID2']) {
			alert('---此条信息无主键，请联系管理员！---');
		}
		
		data["sId"] = node["SERV_ID"];
		data["pkCode"] = node["TODO_OBJECT_ID1"];
		data["ownerCode"] = node["OWNER_CODE"];
		data["sendId"] = node["TODO_OBJECT_ID2"]; // 要送交人
		data["readOnly"] = false;
		data["pId"] = pkCode;
		data["act"] = UIConst.ACT_CARD_READ;
		data['secondStep'] = 'toread'; // 待阅，这个标记在打开待阅时控制意见输入框使用
		(function(params) {
			var cardView = new mb.vi.cardView(params);
				cardView.show();
		}(data));
	} else if (this._secondStep == "agency") { // 委托事务的打开方式
		if (node['TODO_CODE'] == 'TODO_REMIND') { // 如果是提醒类待办
			// dialog ID
			var alertId = 'alertId'; 
			// dialog 配置项
			var setting = {
					'selectCallBack': function() {
						var data = {};
						data[UIConst.PK_KEY] = node['TODO_ID'];
						data["TODO_ID"] = node['TODO_ID'];
						FireFly.doAct("SY_COMM_TODO","endReadCon",data).then(function(result) {
							if (result['_MSG_'].indexOf('OK') >= 0) {
								// 删除脏数据
								$.mobile.pageContainer.find('#' + node['_PK_']).remove();
								// 关闭对话框
								$('body').find('#' + alertId).dialog('close');
							} else {
								alert('待办提醒处理失败,请联系管理员!');
							}
						});
					},
					'cancelCallBack': function() {
						// 关闭对话框
						$('body').find('#' + alertId).dialog('close');
					}
			};
			// alert帮助类,打开alert
			AlertHelper.openAlert(alertId, $('body'), node['TODO_TITLE'], node['TODO_CONTENT'], setting);
			
		} else { // 非提醒类待办
			data["sId"] = node["SERV_ID"]; // 服务ID
			data["pkCode"] = node["TODO_OBJECT_ID1"]; // 文儿ID
			data["ownerCode"] = node["OWNER_CODE"]; // 待办所属人
			
			if (node['SERV_ID'] == 'OA_GW_TYPE_FW_SEND') { // 如果是系统来文
				data["niId"] = ''; // 流程ID
				data['sendId'] = node['TODO_OBJECT_ID2']; // 分发ID
			} else {
				data["niId"] = node["TODO_OBJECT_ID2"]; // 流程ID
				data['sendId'] = ''; // 分发ID
			}
//		    data["niId"] = node["TODO_OBJECT_ID2"]; // 流程ID
//			data['sendId'] = node['TODO_OBJECT_ID2']; // 分发ID
			data["readOnly"] = false;
			data["pId"] = pkCode;
			data["act"] = UIConst.ACT_CARD_READ;
			if(node['_AGENT_USER_']!=undefined ){
	   			data['_AGENT_USER_'] = node['_AGENT_USER_'];
	   		}
			(function(params) {
				var cardView = new mb.vi.cardView(params); 
				cardView.show();
			}(data));
		}
	} else if (this._secondStep == "unfinish") { // 已办的打开方式
		data["sId"] = node["SERV_ID"];
		data["pkCode"] = node["DATA_ID"];
		data["ownerCode"] = node["S_USER"];
		data["pId"] = pkCode;
		data["readOnly"] = false;
		data["act"] = UIConst.ACT_CARD_READ;
		(function(params) {
			var cardView = new mb.vi.cardView(params);
				cardView.show();
		}(data));
	} else if (this._secondStep == "readNews") { // 通知公告的打开方式
		data["sId"] = this.servId;
		data["pkCode"] = pkCode;
		data["headerTitle"] = this.headerTitle;
		(function(params) {
			var newsView = new mb.vi.newsView(params); // TODO 没写
				newsView.show();
		}(data));
	} 
};












