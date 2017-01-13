/**
 * 意见
 */
GLOBAL.namespace("mb.vi");
/**
 * 构造方法
 */
mb.vi.mind = function(options) {
	var defaults = {
			'parHandler': null,
			'wfCard': null,
			'servId': null,
			'dataId': null,
			'pCon': null
	};
	this.opts = $.extend(defaults, options);
	this._mind = this.opts.mind;				// mindDatas,mindInput
	this._servId = this.opts.servId;			// servId
	this._dataId = this.opts.dataId;			// dataId
	this._parHandler = this.opts.parHandler;
	this._wfCard = this.opts.wfCard;			// wfCard
	this._pCon = this.opts.pCon;				// mindfieldcontain
	this._mindInput = '';
	this._mindList = '';
	this._odeptList = '';						// 填写过意见的所有机构
	this.oldMindText = '';
	
	// 意见是否已修改，包括意见内容
	this.isModify = function() {
		return this._textIsModify();
	};
	// 意见内容是否已修改
	this._textIsModify = function() {
		var newVal = $('#mind-content').val() || '';
		if (newVal == this.oldMindText || newVal == '') {
			return false;
		}
		return true;
	};
};
mb.vi.mind.prototype.show = function() {
	this._initMainData();
	this._layout();
	 this._bindEvent();
	this._afterLoad();
};
/**
 * 初始化意见数据
 */
mb.vi.mind.prototype._initMainData = function() {
	var _self = this;
	this._mindInput = this._mind["mindInput"];
	this._mindList = this._mind["mindDatas"]["mindList"];
	this._currMind = this._mind["mindDatas"]["_DATA_"];
	if (this._currMind && this._currMind.length) {
		$.each(this._currMind, function(index, item) {
			// 如果用户相同，证明是当前用户填写的意见，防止并发同部门的情况下，意见可以互相删除
			if (item['S_USER'] == System.getUser('USER_CODE')) {
				_self.pkCode = item['MIND_ID'];
				_self.mindContent = item['MIND_CONTENT'];
				_self.oldMindText = item['MIND_CONTENT'] || '';
			}
		});
	}
};
mb.vi.mind.prototype._layout = function() {
	var _self = this;
	var generalMind = _self._mindInput.generalMind;
	
	// 如果有普通意见的意见编码，就添加意见输入框
	// 并且 不是从待阅中打开的
	var secondStep = _self._wfCard._parHandler.opts.secondStep || '';
	if (generalMind.CODE_ID && secondStep != 'toread') {
		// 绘制textarea
		// TODO 此时的_pCon应该是fieldcontain
		$(_self._pCon[0]).append("<label for='mind-content'>办理意见</label>" +
							"<textarea id='mind-content'>" + (_self.mindContent ? _self.mindContent : '') + "</textarea>");
		
		$(_self._pCon[0]).append("<label></label><div class='ui-grid-a'>" +
								"<div class='ui-block-a'>" +
									"<a href='#use-mind-popup_popup' class='use-mind-js' data-rel='popup' data-role='button'>常用意见</a>" +
								"</div>" +
								"<div class='ui-block-b'>" +
									"<a id='save-btn' href='#' data-role='button'>保存意见</a>" +
								"</div>" +
							"</div>");
		// TODO 查询常用意见，并绘制弹出框
		var cyyjParam = {}; // 常用意见查询参数
			cyyjParam['_WHERE'] = " and 1=1 and (S_USER = '" + System.getUser('USER_CODE') + "' or S_PUBLIC = '1') and S_ODEPT = '" + System.getUser('ODEPT_CODE') + "' and TYPE_CODE = 'MIND'";
			cyyjParam['_NOPAGE_'] = true;
		FireFly.doAct('SY_COMM_USUAL', 'query', cyyjParam).then(function(result) {
			if (result['_OKCOUNT_'] >= 0) {
				_self.useMindDatas = result['_DATA_'];
				
				var id = 'use-mind-popup';
				var popupWrp = $("<div style='margin: 20px 0;' data-role='popup' id='" + id + "_popup' data-dismissible='false' data-transition='pop'>" +
									"<a href='javascript:void(0);' class='zhbx-btn-close ui-btn ui-corner-all ui-shadow ui-btn-a ui-icon-delete ui-btn-icon-notext ui-btn-right'>" +
										"Close" +
									"</a>" +
								 "</div>");
				var popupListView = $("<ul data-role='listview' data-inset='true' style='min-width:210px;'>" +
										"<li data-role='divider'>" +
											"常用意见" +
										"</li>" +
									  "</ul>").appendTo(popupWrp);
				if (_self.useMindDatas.length > 0) { // 本人有常用意见
					$.each(_self.useMindDatas, function(index, item) {
						popupListView.append("<li><a href='javascript:void(0);' class='use-mind-item-js'>" + item['TITLE'] + "</a></li>");
					});
				} else { // 本人没有常用意见
					popupListView.append("<li style='text-align: center;'>无相关记录!</li>");
				}
				$.mobile.activePage.append(popupWrp); // 追加popup到活动页
				popupWrp.popup().enhanceWithin(); // 初始化popup
				
				// 关闭绑定事件
//				popupWrp.on('vclick', 'a.zhbx-btn-close', function() {
				popupWrp.on('tap', 'a.zhbx-btn-close', function() {
					event.preventDefault();
					event.stopImmediatePropagation();
					popupWrp.popup('close');
				});
				// 列表绑定点击事件
//				popupWrp.on('vclick', 'a.use-mind-item-js', function(event) {
				popupWrp.on('tap', 'a.use-mind-item-js', function(event) {
					event.preventDefault();
					event.stopImmediatePropagation();
					$('#mind-content').val($(this).text());
					popupWrp.popup('close');
				});
			}
		});
	}

	//意见列表
	var 	mindList = this._mindList,
		mindListLen = mindList.length;
	if (mindListLen > 0) {
		var mindListHtml='<div class="mind-list-item-js ui-collapsible ui-collapsible-inset ui-corner-all ui-collapsible-themed-content" ><div class="ui-field-contain ui-li-static ui-body-inherit ui-collapsible-content ui-body-inherit" >';
		for (var i=0; i<mindListLen; i++) {
			mindListHtml += _self._renderMindBean(mindList[i]).html();
		}
		mindListHtml +="</div></div><br /><br /><br />";
	}
	if(mindListHtml){
		//判断是否存在意见列表
		if (jQuery(_self._pCon[1]).children("li").length <= 0) {
			jQuery(_self._pCon[1]).append(mindListHtml);
		} else {
			jQuery(_self._pCon[1]).empty();
			jQuery(_self._pCon[1]).append(mindListHtml);
		}
	}
};
/**
 * 渲染不同机构的collapse
 */
mb.vi.mind.prototype._renderOdeptBean = function(odeptBean, index) {
	var _self = this;
	
	var $odeptColl = $("<div id='" + odeptBean['DEPT_CODE'] + "_list' class='mind-list-item-js' data-role='collapsible'><h4>" + odeptBean['DEPT_NAME'] + "</h4></div>");
	var $ulCon = $("<ul data-role='listview'></ul>").appendTo($odeptColl);
	
	// 添加空的label，解决pad上页面混乱的问题
	if (index >= 1) {
		$(_self._pCon[1]).append('<label></label>');
	}
	$(_self._pCon[1]).append($odeptColl);
	return $ulCon;
};
/**
 * 渲染意见
 */
mb.vi.mind.prototype._renderMindBean = function (mindBean,isNew) {
	//意见列表
	var $mindLi = '';
	if(mindBean){
		var canDelete = false;
		//if(isNew || this.pkCode == mindBean["MIND_ID"]){
		if(this.opts.wfCard.reqdata.NI_ID == mindBean["WF_NI_ID"] && mindBean["S_USER"] == System.getUser("USER_CODE")){
			canDelete = true;
		}
		var dateTime = mindBean["MIND_TIME"];
		time = dateTime.toString().substring(0,10);
		var d = new Date();
		var t = "";
		//同年
		if(time.substring(0,4) == d.getFullYear()){
			t = dateTime.substring(5,16);
		}else{
			t = time;
		}
		$mindLi = $("<li  id = '" + mindBean['MIND_ID'] +"'></li>").append("<h5>" + mindBean['WF_NI_NAME'] + "</h5>");
		var $pCon = $("<p></p>").append("<strong>" + mindBean['S_UNAME'] + "</strong>")
					.append("&nbsp;(" + mindBean['S_DNAME'] + ")&nbsp;：" + mindBean['MIND_CONTENT'] + "(" + mindBean['MIND_TIME'] + ")"+(canDelete ? "<span id='deleteMind'>&nbsp;&nbsp;删除</span>" : "<span>&nbsp;</span>"));
		$mindLi.append($pCon);
	}
	var mindDiv = jQuery("<div></div>");
	 $mindLi.appendTo(mindDiv);
	return mindDiv;
};

mb.vi.mind.prototype._bindEvent = function() {
	var _self = this;
	$('#' + _self._dataId + '_MIND').on('vclick', '#deleteMind', function(event) {debugger;
			event.preventDefault();
		event.stopImmediatePropagation();
		var $that = $(this).parent().parent();
		if(confirm('是否删除?')){
	    		 var id = $that.attr("id");
	      	  	 FireFly.doAct("SY_COMM_MIND", "delete", {"_PK_": id}).done(function(result){
		  		 	var rtnMsg = result[UIConst.RTN_MSG];
	 				if (StringUtils.startWith(rtnMsg, UIConst.RTN_OK)) {
			  			_self.pkCode = null;
			  			$("#mind-content").val("");
			  			_self.oldMindText = "";
			  			$that.fadeOut(500,function(){
			  				$that.remove();
			  			});
						_self._hasCommited=false;
			  		 }
		  		 });
	    }
	}).on('vclick', '#save-btn', function(event) { // 给保存按钮绑定事件
		event.preventDefault();
		event.stopImmediatePropagation();
		
		var currMind = _self._wfCard.getMindCodeBean();
		if (!currMind.CODE_ID) { // 没有意见输入框，则返回不保存
			return false;
		}
		var mindContent = $('#mind-content').val();
		if (mindContent == undefined || mindContent.length <= 0) {
			alert('意见不能为空！');
			return false;
		}
		var data = {};
			data['MIND_CODE'] = currMind.CODE_ID;
			data['MIND_CONTENT'] = mindContent;
			data['MIND_CODE_NAME'] = currMind.CODE_NAME;
			data['SERV_ID'] = _self._servId;
			data['DATA_ID'] = _self._dataId;
			data['WF_NI_ID'] = _self._wfCard.getNodeInstBean().NI_ID;
			data['WF_NI_NAME'] = _self._wfCard.getNodeInstBean().NODE_NAME;
			data['MIND_DIS_RULE'] = currMind.MIND_DIS_RULE; // 显示规则
			data['MIND_TYPE'] = '1'; // 1:文字意见;2:手写意见
			data['S_FLAG'] = 2;
			data['S_CMPY'] = 'zhbx';
			
		if (_self.pkCode) { // 如果存在则更新，否则保存
			data['_PK_'] = _self.pkCode;
		}
		$.mobile.loading('show', {
			text: "加载中...",
			textVisible: true,
			textonly: false
		});
		FireFly.doAct('SY_COMM_MIND', 'save', data).done(function(result) {
			$.mobile.loading('hide');
			var rtnMsg = result[UIConst.RTN_MSG];
			if (StringUtils.startWith(rtnMsg, UIConst.RTN_OK)) {
			_self.pkCode = result["MIND_ID"];
			_self.oldMindText = result["MIND_CONTENT"] || "";
			if($("#" + _self.pkCode ).length) {
				var $mindText = $("#" + _self.pkCode ).find(".sc-mind-text");
				$mindText.html(result["MIND_CONTENT"]);
			} else {
				_self._wfCard._parHandler._refresh();
			}
			// zjx -- 重新判断是否为必填意见
			_self._wfCard._mindMust();
		} else {
			navigator.notification.alert("意见保存失败，请检查!",null,'提示', '确定');
		}
		});
	});
};

/**
 * 加载后执行操作
 */
mb.vi.mind.prototype._afterLoad = function() {
	// 如果没有意见输入框也没有意见列表，隐藏掉意见分组框
	this._hideMindGroup();
};
/**
 * 当无意见列表和输入框时隐藏意见分组框
 */
mb.vi.mind.prototype._hideMindGroup = function() {
	var _self = this;
	if ($(_self._pCon[0]).html() == '' && $(_self._pCon[1]).html() == '') { // 如果既没有输入框也没有列表
		$('#' + _self._dataId + '_MIND').hide(); // 隐藏掉意见分组框
	}
};