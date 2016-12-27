/**
 * 工作台页面渲染引擎
 */
GLOBAL.namespace('mb.vi');
/**
 * 构造方法
 */
mb.vi.deskView = function(options) {
	var defaults = {
			id : 'mbDesk'
	};
	this.opts = $.extend(defaults, options);
	this.id = this.opts.id;
	this.iconData = {};
	
	this.count = 0;
};

mb.vi.deskView.prototype.show = function() {
	var _self = this;
	// 1.初始化桌面数据
	this._initMainData().done(function() {
		// 2.设置布局
		_self._bldLayout();
		_self._afterLoad();
		// 3.设置兼岗信息
		if (System.getUser('JIAN_CODES') && System.getUser('JIAN_CODES').length > 0) {
			_self._getJiangang();
			_self.headerWrp.on('vclick', '.zhbx-username', function(event) {
				event.preventDefault();
				event.stopImmediatePropagation();
				$('.jiangang').slideToggle('fast');
			});
		}
	});
};
/**
 * 初始化数据
 */
mb.vi.deskView.prototype._initMainData = function() {
	var _self = this;
	return FireFly.getMenu().then(function(result) {
//		var menuDatas = result.TOPMENU,
//			menuLen = menuDatas.length;
		var menuDatas = result._DATA_,
			menuLen = menuDatas.length;
		for (var i = 0; i < menuLen; i++) {
			_self._getLeafData(menuDatas[i]);
		}
	});
};
/**
 * 获取移动端菜单
 */
mb.vi.deskView.prototype._getLeafData = function(menuData) {
	var _self = this;
	var child = menuData.CHILD;
	if (child) {
		for (var i = 0, len = child.length; i < len; i++) {
			_self._getLeafData(child[i]);
		}
	} else {
//		var id = menuData.ID,
//			area = menuData.AREA || '';
		var id = menuData.MENU_ID,
			area = menuData.MENU_AREA || '';
		if (area == '4') { // 手机显示
			_self.iconData[id] = menuData;
		}
	}
};
/**
 * 设置布局
 */
mb.vi.deskView.prototype._bldLayout = function() {
	var _self = this;
	
	this.headerWrp = $('#' + this.id + '_header');
	this.contentWrp = $('#' + this.id + '_content');
	this.gridCon = $('#' + this.id + '_grid');
	this.gridCon.appendTo(this.contentWrp);
	var index = 0;
	$.each(_self.iconData, function(i, item) {
		_self._bldOneApp(index, item).appendTo(_self.gridCon);
		index++;
	});
};
/**
 * 根据item组合一个app
 */
mb.vi.deskView.prototype._bldOneApp = function(index, item) {
	var _self = this;
	if (item == null) {
		return;
	}
	var id = item['MENU_ID']
	var classSuffix = String.fromCharCode(parseInt(97) + parseInt((index%3))); // 后缀的字母，分别是a/b/c
	var $item = $("<div class='ui-block-"+classSuffix+"'><a href='#' id='" + id + "'></a></div>");
		$item.data('sid', item['MENU_INFO']);
		$item.data('title', item['DS_NAME']);
	
	var $block = $("<div class='mb-desk-app'><span class='sc-desk-app-icon zhbx-desk-"+item['DS_ICON']+"'></span><div class='mb-desk-app-text'>"+item['DS_NAME']+"</div></div>").appendTo($item.children());
	
	$item.on('vclick', function(event) {
		event.preventDefault();
		event.stopImmediatePropagation();
		
		var data = {};
			data['sId'] = $(this).data('sid');
			data['headerTitle'] = $(this).data('title');
		
		  if(id=="2yC9rhbMx4cGb3Un8Rz1ok__zhbx"){//待办事务
		   data["secondStep"] = "card";
		   data["extWhere"] = " and TODO_CODE_NAME in('总公司发文','分公司发文','自定义签报','工作通知书','总公司收文','分公司收文','合同审批单')";
	   }
	   if(id=="1cpQczPtF0qHMD7vIYLaHg__zhbx"){//公司要闻
		   data["secondStep"] = "readNews";
	   }
	   if(id=="233CBN9AAJdoHYRQwk5oJ4e__zhbx"){//通知公告
		   data["secondStep"] = "readNews";
	   }
	   if(id=="3ltvUv5HpbSoXKJIoAOtvvq__zhbx") {//委托事务
			data["secondStep"] = "card";
			data["extWhere"] = " and TODO_CODE in('OA_GW_TMPL_FW_GS','OA_GW_TMPL_FW_GW','OA_GW_TMPL_SW_GS','OA_GW_TMPL_SW_DW','OA_GW_TMPL_QB_GZ','OA_GW_TMPL_FW_DW','LW_CT_CONTRACT') and TODO_SEND_TIME > '@DATE_DIFF_DD30_N@'";


	   }
	   if(id=="08kPT9Gkp7SGQIFEHz3REI__zhbx"){//已办事务
		   data["secondStep"] = "unfinish";
            data["extWhere"] = "{'SERV_ID':'OA_GW_TMPL_FW_GS,OA_GW_TMPL_FW_GW,OA_GW_TMPL_SW_GS,OA_GW_TMPL_SW_DW,OA_GW_TMPL_QB_GZ,OA_GW_TMPL_FW_DW,LW_CT_CONTRACT'}";
	   }
	   if(id=="0rpCcZFMZd9r4EF7Cd027G__zhbx"){//通讯录
		   $(function(){
			   var contacts = new mb.vi.contacts(data);
			   contacts.show();
		   });
		   return;
	   }
	    if(id=="0mFQGZOmh4Z93Gs8GC8UGe__zhbx"){//领导日程
		   $(function(){
			   var ldrAction = new mb.vi.ldrAction(data);
			   ldrAction.show();
		   });
		   return;
	   }
	   if (id == "2CkDrnkox9gqncIQX4qTZi__zhbx") { //查询
		   $.mobile.changePage("#search");
		   return;
	   }
	   if (id == "1e7RLu33ddDGeOneBia84m__zhbx") { //密码修改
		   $.mobile.changePage("#pwdEdit");
		   return;
	   }
	   if(id=="3ygaSYOQp6UdWUwAbn7W1Qf__zhbx"){//党群工作
		   data["secondStep"] = "chain";
		   data["id"] = "listviewDq";
	   }	   
	   if(id=="24fsG3FcR5CpPSxjR8NDGJ__zhbx"){//待阅事务
		   data["secondStep"] = "toread";
	   }
	   if (id == "3eWN1gBmp6x8ZH4M6XuMBA__zhbx") { //OA短信
		   $.mobile.changePage("#message");
		   return;
	   }
	   if (id == "274W9Q1Gx9B86Y0oz567xCH__zhbx") {//关于
		   $.mobile.changePage("#about");
		   return;
	   }
	   
	   var listview = new mb.vi.listView(data);
	   	   listview.show();
   });
   
   if (id == "2yC9rhbMx4cGb3Un8Rz1ok__zhbx") {
	   $("<span class='mb-desk-new-count'></span>").attr("countserv","SY_COMM_TODO").appendTo($block);
   }
   if (id == "24fsG3FcR5CpPSxjR8NDGJ__zhbx") {
	   $("<span class='mb-desk-new-count'></span>").attr("countserv","SY_COMM_TODO_READ").appendTo($block);
	   
   }
	if (id == '3ltvUv5HpbSoXKJIoAOtvvq__zhbx') { // 如果是委托添加计数器
		$("<span class='mb-desk-new-count'></span>").attr('countserv', 'SY_COMM_TODO_AGENT').appendTo($block);
	}
	return $item;
};
/**
 * 加载后执行
 */
mb.vi.deskView.prototype._afterLoad = function() {
	var _self = this;
	//this.headerWrp.find('.zhbx-username span').html('您好，' + System.getUser('USER_NAME'));
	
	if (FireFly.isEnableConnect()) { // 如果联网状态
		this._getCounts();
	}
	if (this.timeout) {
		clearTimeout(this.timeout);
	}
	this.timeout = setTimeout(function() {
		if (FireFly.isEnableConnect()) {
			_self._getCounts();
		}
	}, 3*60*1000); // 3分钟检查一次
};
/**
 * 获取提醒消息个数
 */
mb.vi.deskView.prototype._getCounts = function() {
	return FireFly.doAct('SY_COMM_TODO', 'getTodoCountMb').then(function(result) {
		if (result && result._DATA_) {
			var data = result._DATA_;
			$('.mb-desk-new-count').each(function(i, n) {
				var count = 0,
					countserv = $(this).attr('countserv');
				if (countserv == 'SY_COMM_TODO') { // 待办
					count = data[1];
				} else if (countserv == 'SY_COMM_TODO_READ') { // 待阅
					count = data[2];
				}else if(countserv == 'SY_COMM_TODO_AGENT'){//委托  20160912 by ayf end begin
					count = data[3];
				}// 20160912 by ayf end
				if (count && count > 0) {
					count = count > 99 ? '99+' : count;
					$(this).text(count).show();
				} else {
					$(this).hide();
				}
			});
		}
	});
};
/**
 * 设置兼岗信息
 */
mb.vi.deskView.prototype._getJiangang = function() {
	var _self = this;
	return FireFly.doAct('SY_ORG_LOGIN', 'getJianUsers').then(function(result) {
		if (result && result['_DATA_']) {
			$('#'+_self.id).find('.jiangang').remove();
			
			var listData = result['_DATA_'];
			// 如果不存在兼岗信息，则渲染
			var liArr = [], 
				tipsFlag = false;
			$.each(listData, function(i, obj) {
				liArr.push("<li data-code='"+obj['USER_CODE_HEX']+"'><h2>"+obj['USER_NAME']+"</h2><p>"+obj['ODEPT_NAME']+' '+obj['TDEPT_NAME']+"</p><span class='ui-li-count'>"+obj['TODO_COUNT']+"</span></li>");
				// 如果TODO_COUNT>0,则jiangang-tips提示
				if (parseInt(obj['TODO_COUNT'],10) > 0) {
					tipsFlag = true;
				}
			});
			// 设置兼岗标识
			var $deskTitle = $('#'+_self.id).find('.zhbx-username'); // TODO 没有zhbx-desk-title
			if (!$('.jiangang-tips').length) {
				$deskTitle.append("<li class='jiangang-tips'></li>");
			}
			if (tipsFlag) {
				$('#'+_self.id).find('.jiangang-tips').addClass('info');
			} else {
				$('#'+_self.id).find('.jiangang-tips').removeClass('info');
			}
			var jiangangCtn = $("<div class='jiangang'></div>").appendTo($('#'+_self.id));
			var $ul = $("<ul data-role='listview' data-inset='true'></ul>").appendTo(jiangangCtn);
			$ul.append(liArr.join(''));
			jiangangCtn.css("height", $.mobile.getScreenHeight() - 53).enhanceWithin(); // 组件初始化
			$ul.on('vclick', 'li', function(event) {
				event.preventDefault();
				event.stopImmediatePropagation();
				_self.changeUserCode = $(this).data("code");
				
				$.mobile.loading('show', {
					text: "加载中...",
					textVisible: true,
					textonly: false
				});
				
				FireFly.doAct('SY_ORG_LOGIN', 'changeUser', {'TO_USER_CODE':_self.changeUserCode}).then(function() {
//					$.mobile.window.deskView._refresh();
					// TODO 需要加密useCode
//					window.location.href = homeUrl + "?userCode=" + _self.changeUserCode + "&homeUrl=" + homeUrl;
					var hrefUrl = "/oa/mobile/jsp/login_sso_mb.jsp?userCode=" + _self.changeUserCode;
					window.location.href = hrefUrl;
				});
				$.mobile.loading('hide');
				
				// 这段压缩的时候报错
				/*.finally(function() {
					$.mobile.loading('hide');
				});*/
			});
		}
	});
};
/**
 * 清空桌面
 */
mb.vi.deskView.prototype._clear = function() {
	this.contentWrp.empty();
};
/**
 * 刷新桌面
 */
mb.vi.deskView.prototype._refresh = function() {
	this._clear();
	this.show();
};
/**
 * TODO 
 * 这个方法看着有问题，用的时候再看
 */
mb.vi.deskView.prototype.refresh = function() {
	this._afterLoad();
};












