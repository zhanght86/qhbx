/**
 * 移动版表格组件
 */
GLOBAL.namespace('mb.ui');
mb.ui.grid = function(options) {
	var defaults = {
			'id': options.sId + 'mbGrid',
			'parHandler': null
	};
	this._opts = jQuery.extend(defaults, options);
	this._id = this._opts.id;
	this._parHandler = this._opts.parHandler;
	this._pCon = this._parHandler.contentWrp;
	
	this._lData = options.listData._DATA_ || {}; // 数据
	this._lPage = options.listData._PAGE_ || {}; // 分页数据
	this._data = options.mainData || {}; // 主数据
	this._cols = options.listData._COLS_ || {};
	this._items = this._data.ITEMS;
	
	this.lTitle = UIConst.ITEM_MOBILE_LTITLE;				/* 1移动列表标题 */
	this.lItem = UIConst.ITEM_MOBILE_LITEM;					/* 2移动列表项 */
	this.cItem = UIConst.ITEM_MOBILE_CITEM;					/* 3移动卡片项 */
	this.cHidden = UIConst.ITEM_MOBILE_CHIDDEN;				/* 9移动卡隐藏项 */
	this.lTime = UIConst.ITEM_MOBILE_LTIME;					/* 4列表时间项 */
	this.lImg = UIConst.ITEM_MOBILE_LIMG;					/* 5列表图片项 */
	
	this.lData = {}; // 以pk为key的列表项数据集合
};
/**
 * 表格渲染方法，入口
 */
mb.ui.grid.prototype.render = function() {
	var _self = this;
	this._bldGrid().appendTo(this._pCon); // 构建列表，放入容器
	this._bldPage().appendTo(this._pCon); // 构建分页，放入容器
	this._afterLoad(); // 渲染后执行方法
};
/**
 * 构建列表，包括列表项
 */
mb.ui.grid.prototype._bldGrid = function() {
	this.listViewWrp = jQuery("<ul data-role='listview' data-inset='true'></ul>").attr('id', this._id);
	this.listViewWrp.append(this._bldItems()); // 将构建好的列表项放入
	return this.listViewWrp;
};
/**
 * 构建列表项
 */
mb.ui.grid.prototype._bldItems = function() {
	var _self = this;
	var preAllNum = parseInt(this._lPage.SHOWNUM) * (parseInt(this._lPage.NOWPAGE) - 1);
	var trs = [];
	var excludeServ = {}; // 需要屏蔽的服务
	jQuery.each(this._lData, function(i, obj) {
		var nextPageNum = preAllNum + i;
		_self.lData[obj._PK_] = obj;
		if (!excludeServ[obj.SERV_ID]) { // 如果服务不在屏蔽范围内
			trs.push("<li id='" + obj._PK_ + "' serv='" + obj.SERV_ID + "'>");
			trs.push(_self._bldBlock(nextPageNum, i, obj));
			trs.push("</li>");
		}
	});
	return trs.join('');
};
/**
 * 构建列表项区块
 */
mb.ui.grid.prototype._bldBlock = function(nextPageNum, index, trData) {
	var _self = this;
	
	var tdRight = [],
		titleArr = [], 	// 标题
		deptArr = [], 	// 主办部门
		stateArr = [], 	// 办理环节
		timeArr = [], 	// 时间
		emergencyArr = []; // 紧急程度
	
	jQuery.each(this._cols, function(i, m) {
		var itemCode = m.ITEM_CODE; // 字段code
		var itemName = m.ITEM_NAME; // 字段名称
		var listFlag = m.ITEM_LIST_FLAG; // 字段是否列表显示
		
		var value = trData[itemCode]; // 字段中的值
		
		if (listFlag == 1) { // 是否显示在列表中
			// 通过item获取cols的详细信息
			var code = itemCode;
			var _code = '';
			if (itemCode.indexOf('__NAME') > 0) { // 例如这样的字段名"TODO_OPERATION__NAME"
				var code = itemCode.substring(0, itemCode.indexOf('__NAME'));
				_code = code;
				if(value == undefined){//处理value为 undefined  20160927 ,by ayf 
					value =  trData[_code]
				}
			}
			if (itemCode.indexOf('__IMG') > 0) {
				return true;
			}
			var tempN = _self._items[code]; // 获取字段的配置
			
			var mbType = tempN.ITEM_MOBILE_TYPE; // 列表项的移动显示方式
			
			if (mbType == _self.lTitle) { // 标题项
				titleArr.push("<h2 ");
				if (_code.length > 0) {
					titleArr.push(" _code='");
					titleArr.push(_code);
					titleArr.push("'");
				}
				titleArr.push(">");
				titleArr.push(value);
				titleArr.push("</h2>");
			} else if (mbType == _self.lTime) { // 时间差项
//				value = _self._timeDiff(value); // 计算时间差	TODO 中华保险这边没提这个需求，暂时不给实现
				timeArr.push("<h6 code='" + itemCode + "' class='zhbx-list-time'>" + value + "</h6>");
			} else if (mbType == _self.lItem) { // 移动列表项
				// zhbx有三种情况：紧急程度、办理环节、主办部门
				if (_code == 'S_EMERGENCY') { // 紧急程度
					switch (value) {
					case '2' :
					case '20' :
					case '紧急' :
						emergencyArr.push("<img class='ui-li-icon' src='/oa/mobile/images/yellowstar.png' />");
						break;
					case '3' :
					case '30' :
					case '特急' :
						emergencyArr.push("<img class='ui-li-icon' src='/oa/mobile/images/redstar.png' />");
						break;
					case '1' :
					case '一般' :
					default :
						emergencyArr.push("<img class='ui-li-icon' src='/oa/mobile/images/whitestar.png' />");
					}
				} else if (itemCode == 'S_WF_USER_STATE') { // 办理环节
//					格式如：[{"D":"稽核部总经理","U":"c180c0c718f36e8e0118f36ec6f100bc","N":"周有扣","O":"N"}] 节点名、用户id、用户name、是否办结
//					stateArr.push(value);
					var userStateList = StrToJson(value); // 办理环节列表
					/*
					 * TODO这个取办理环节的，我觉得有问题，有时间检查一下
					var userStateStr = ""; // 当前办理环节
					$.each(userStateList, function(index, obj) {
						if (obj["O"] == 'N') { // 未办结
							userStateStr = obj["D"];
						}
					});
					*/
					var userStateStr = userStateList[0] ? userStateList[0]["D"] : '';
					stateArr.push(userStateStr);
					
				} else if (itemCode == 'S_TDEPT__NAME') { // 主办部门
//					格式如：中华联合保险控股股份有限公司/财险总公司
					value = value.substring(value.lastIndexOf('/')+1, value.length);
					deptArr.push(value);
				}
			}
		}
	});
	
	tdRight.push("<a href='#'>");
	tdRight.push(emergencyArr.join(''));
	
	if (stateArr && deptArr && stateArr.length>0) { // 存在办理环节和主办部门
		tdRight.push("<h6 class='zhbx-list-time'>");
		tdRight.push(stateArr.join(''));
		tdRight.push("/");
		tdRight.push(deptArr.join(''));
		tdRight.push("</h6>");
	} else { // 不存在就添加时间
		tdRight.push(timeArr.join(''));
	}
	tdRight.push(titleArr.join(''));
	tdRight.push("</a>");
	return tdRight.join('');
};
/**
 * 获取时间差
 */
mb.ui.grid.prototype._timeDiff = function(time) {
	var res = '';
	res = rhDate.dateDiff('d', time);
	if (res == 0) { // 当天
		res = rhDate.dateDiff('h', time);
		if (res == 0) { // 当小时
			res = rhDate.dateDiff('n', time);
			if (res == 0) { // 当分钟
				res = rhDate.dateDiff('s', time) + '秒钟前';
			} else {
				res += '分钟前';
			} 
		} else {
			res += '小时前';
		}
	} else if (res <= 2) {
		res += '天前';
	} else {
		res = time;
	}
	return res;
};
/**
 * 构建翻页
 */
mb.ui.grid.prototype.getBlocks = function() {
	var _self = this;
	return this.listViewWrp.find('li');
};
/**
 * 行点击事件，供外部调用
 */
mb.ui.grid.prototype.click = function(func, parSelf) {
	var _self = this;
	this.listViewWrp.on('vclick', 'li', function(event) {
		event.preventDefault();
		event.stopImmediatePropagation();
		var pkCode = $(this).attr('id');
		func.call(parSelf, pkCode, _self.lData[pkCode]);
		return false;
	});
};
/**
 * 构建翻页
 */
mb.ui.grid.prototype._bldPage = function() {
	var _self = this;
	this.more = $('<div></div>').addClass('mbGrid-more'); // TODO 添加样式mbGrid-more
	this.more.html("<span>查看更多</span>");
	this.more.bind('vclick', function(event) {
		event.preventDefault();
		event.stopImmediatePropagation();
		_self._nextPage(); // 加载下一页
		return false;
	});
	jQuery('<span></span>').addClass('mbGrid-more-icon mb-down-nav').appendTo(this.more);
	return this.more;
};
/**
 * 记录加载完毕提示
 */
mb.ui.grid.prototype._recordOverTip = function() {
	var _self = this;
	this.overTip = jQuery('<div></div>').addClass('mbGrid-overTip');
	var dataLen = this._lData.length || 0;
	var tipText = '全部数据已加载！';
	if (dataLen == 0) {
		tipText = '无相关记录！';
	}
	this.overTip.text(tipText);
	/*
	 * 回到顶部有问题，暂时先屏蔽掉
	var toTop = jQuery('<span>回到顶部</span>').addClass('mbGrid-toTop').appendTo(this.overTip);
	toTop.bind('tap', function() {
		$.mobile.silentScroll();
	});
	*/
	return this.overTip.appendTo(this._pCon);
};
/**
 * 下一页
 */
mb.ui.grid.prototype._nextPage = function() {
	var nextPage = parseInt(this._lPage.NOWPAGE) + 1;
	var pages = parseInt(this._lPage.PAGES);
	this._lPage.NOWPAGE = '' + ((nextPage > pages) ? pages : nextPage);
	var data = {'_PAGE_':this._lPage};
	this._parHandler.morePend(data);
};
/**
 * 将更多添加到列表
 */
mb.ui.grid.prototype._morePend = function(listData) {
	this._lData = listData._DATA_ || {};
	this._lPage = listData._PAGE_ || {};
	this.listViewWrp.append(this._bldItems());
	this.listViewWrp.listview().listview('refresh');
	this._afterLoad();
};
/**
 * 加载后执行
 */
mb.ui.grid.prototype._afterLoad = function() {
	var _self = this;
	var nowPage = this._lPage.NOWPAGE;
	var pages = this._lPage.PAGES;
	if (nowPage == pages) {
		_self.more.hide();
		_self._recordOverTip();
	}
	this.listViewWrp.listview().listview('refresh');
};
