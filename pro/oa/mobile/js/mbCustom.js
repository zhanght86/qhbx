/**
 * 设备初始化
 */

/**
 * 在jqm没用起作用之前进行一个初始化配置
 */
$(document).on('mobileinit', function() {
	
	// 微信相关注册
	wx.config({
		debug: false,
		appId: 'wxf8b4f85f3a794e77',
		timestamp: 1420774989,
		nonceStr: '2nDgiWM7gCxhL8v0',
		signature: '1f8a6552c1c99991fc8bb4e2a818fe54b2ce7687',
		jsApiList: [
					'checkJsApi',
					'onMenuShareTimeline',
					'onMenuShareAppMessage',
					'onMenuShareQQ',
					'onMenuShareWeibo',
					'hideMenuItems',
					'showMenuItems',
					'hideAllNonBaseMenuItem',
					'showAllNonBaseMenuItem',
					'translateVoice',
					'startRecord',
					'stopRecord',
					'onRecordEnd',
					'playVoice',
					'pauseVoice',
					'stopVoice',
					'uploadVoice',
					'downloadVoice',
					'chooseImage',
					'previewImage',
					'uploadImage',
					'downloadImage',
					'getNetworkType',
					'openLocation',
					'getLocation',
					'hideOptionMenu',
					'showOptionMenu',
					'closeWindow',
					'scanQRCode',
					'chooseWXPay',
					'openProductSpecificView',
					'addCard',
					'chooseCard',
					'openCard'
		            ]
	});
	wx.ready(function() {
		
	});
	wx.error(function(res) {
		console.log(res);
	});
	
	
	
	// 设置系统级信息
	var 
		userCode  = orgMapJson["@USER_CODE@"],
		userName  = orgMapJson["@USER_NAME@"],
		userImg   = orgMapJson["@USER_IMG@"],
		deptCode  = orgMapJson["@DEPT_CODE@"],
		deptName  = orgMapJson["@DEPT_NAME@"],
		odeptCode = orgMapJson["@ODEPT_CODE@"],
		jianCode  = orgMapJson["@JIAN_CODES@"],
		odeptCodePath = orgMapJson["@ODEPT_CODE_PATH@"];
	System.setUser("USER_CODE", userCode);
	System.setUser("USER_NAME", userName);
	System.setUser("USER_IMG", userImg);
	System.setUser("DEPT_CODE", deptCode);
	System.setUser("DEPT_NAME", deptName);
	System.setUser("ODEPT_CODE", odeptCode);
	System.setUser("JIAN_CODES", jianCode);
	System.setUser("ODEPT_CODE_PATH", odeptCodePath);
	
	/* 标识是否总公司
	if(odeptCode=="24"){//总公司
		$.mobile.window.isBranch = false;
	}else{//分公司
		$.mobile.window.isBranch = true;
	}
	*/
	
	
	// 全局转场效果
	$.mobile.changePage.defaults.transition = 'none';	// 换页效果无
	$.mobile.defaultPageTransition = 'none'; 			// 页面切换效果无
	$.mobile.defaultDialogTransition = 'none';			// 对话框出现效果无
	$.mobile.buttonMarkup.hoverDelay = 0;				// 按钮的反应延时
	
	// list分组
	// 省略
	
	/**
	 * 查找最近的链接元素
	 */
	function findClosestLink(ele) {
		while (ele) {
			if ((typeof ele.nodeName === 'string') && ele.nodeName.toLowerCase() === 'a') {
				break;
			}
			ele = ele.parentNode;
		}
		return ele;
	};
	/**
	 * 重写-设置活动页高度方法
	 */
	$.mobile.resetActivePageHeight = function(height) {
		var page = $('.' + $.mobile.activePageClass), // 当前活动页
			pageHeight = page.height(),
			pageOuterHeight = page.outerHeight(true);
		
		height = (typeof height === 'number') ? height : $.mobile.getScreenHeight();
		
		// 页面高度设定死
		// 注释掉，否则ios系统中页面没加载完就截断了，android设备上的photoSwipe插件图片跟不上页面滚动速度
//		page.css('min-height', height - (pageOuterHeight - pageHeight));
//		page.css('max-height', height - (pageOuterHeight - pageHeight));
	};
	/**
	 * 监听a标签点击事件，如果是返回事件，添加返回效果
	 */
	$.mobile.document.on('vclick', 'a', function(event) {
		var link = findClosestLink(event.target);
		$link = $(link);
		if ($link.is(":jqmData(rel='back')")) {
			$.mobile.loading('show', {
				text : '返回中...',
				textVisible : true,
				textonly : false
			});
		}
	});
	
	/**
	 * desktop
	 * 桌面初始化之前
	 */
	$.mobile.document.on('pagebeforeshow', '#mbDesk', function() {
		if ($.mobile.window.deskView) { // 如果已经渲染过桌面，直接刷新
			$.mobile.window.deskView.refresh(); 
		} else {
			var deskView = new mb.vi.deskView(); // 创建deskView对象
			deskView.show(); // 展示
			$.mobile.window.deskView = deskView;
		}
	});
	
	/**
	 * search
	 * 查询页面初始化
	 */
	$.mobile.document.on('pageinit', '#search', function() {
		$(this).on("focus change", "#searchtext", function() {
			var val, lastval;
			val = $(this).val().toLowerCase();
			lastval = $(this).data('lastval') + "";
			if (lastval && lastval === val) {
				return;
			}
			var $ul = $('#autocomplete');
			$ul.html('');
		}).on('vclick', '#searchbtn', function() {
			var $ul = $('#autocomplete'),
				$input = $('#searchtext'),
				value = $input.val(),
				html = '',
				params = {
					'SEARCH_FLAG': 'true'
				};
			value && $.extend(params, {"_searchWhere":" and TITLE like '%" + value + "%'"});
			$ul.html('');
			// TODO 这里使用的查询服务没有加流经的过滤条件
			FireFly.getPageData('OA_SY_COMM_ENTITY_QUERY', params).then(function(result) {
				var data = result['_DATA_'];
				if (data && data.length>0) {
					$.each(data, function(i, obj) {
						var $li = $("<li data-sid='"+ obj["SERV_ID"] +"' data-pk='"+ obj["DATA_ID"] +"'><span>"+ obj["TITLE"] +"</span></li>");
						$li.appendTo($ul);
						$li.on('click', function() {
							var userCode = System.getUser("USER_CODE");
							var temp = {
									act: UIConst.ACT_CARD_READ,
									sId: $(this).data('sid'),
									pkCode: $(this).data('pk'),
									ownerCode: userCode,
									niId: null,
									readOnly: false
							};
							(function(param) {
								var cardView = new mb.vi.cardView(param);
									cardView.show();
							})(temp);
						});
					});
				} else {
					html += "<li>没有匹配的搜索结果！</li>";
					$ul.html(html);
				}
				$ul.listview('refresh');
				$ul.trigger('updatelayout');
			});
		});
	});
	
	/**
	 * newsview
	 * 新闻浏览页面隐藏时调用
	 */
	$.mobile.document.on('pagehide', '#newsview', function(event, ui) {
		var $target = $(ui.nextPage);
		if ($target.is("#listview")) {
			$(this).find(".ui-title").empty().end()
			   .find(".ui-content").empty();
		}
	});
	
	/**
	 * cardview
	 * 卡片页面显示前，显示loading图片
	 * 卡片页面点击返回时调用
	 */
	$.mobile.document.on('pagebeforeshow', '#cardview', function(event, ui) {
		$.mobile.loading('show');
	}).on('pagehide', '#cardview', function(event, ui) {
		var $target = $(ui.nextPage);
		if ($target.is('#listview,#search')) { // 如果返回至listview或search页面
			$(this).find('.ui-title').empty().end()
					.find('.ui-content').empty().end()
					.find('.ui-footer').empty();
		}
	});
	
	/**
	 * listview
	 * 列表页面点击返回时调用
	 */
	$.mobile.document.on('pagehide', '#listview', function(event, ui) {
		var $target = $(ui.nextPage);
    	if($target.is("#mbDesk")) {//返回至listview页面
    		$(this).find(".ui-title").empty().end()
			       .find(".ui-content").empty();
    	}
	});
	
	/**
	 * cardToCardView
	 * 页面点击返回时调用
	 */
	$.mobile.document.on('pagehide', '#cardToCardView', function(event, ui) {
		var $target = $(ui.nextPage);
		if ($target.is('#cardview')) { // 返回至卡片页面
			$(this).find('.ui-title').empty().end()
				   .find('.ui-content').empty().end()
				   .find('.ui-footer').empty();
		}
	});
});































