/**
 * 工作流引擎
 */
GLOBAL.namespace('mb.vi');
/**
 * 构造方法
 */
mb.vi.wfCardView = function(options) {
	var defaults = {
			'id': options.sId + '_viWfCardView',
			'sId': '', // 服务ID
			'pId': options.sId,
			'parHandler': null, // 主卡片的句柄
			'pkCode': ''
	};
	this.opts = $.extend(defaults, options);
	
	this.servId = this.opts.sId; // card 服务ID
	this.pkCode = this.opts.pkCode; // card 数据主键
	
	this._parHandler = this.opts.parHandler;
	this._nextStepNodeCode = '';
	
	// 从页面取得流程实例的信息
	this.procInstId = this._parHandler.itemValue('S_WF_INST');
	this.wfState = this._parHandler.itemValue('S_WF_STATE');
	this.reqdata = {};
	this.reqdata['PI_ID'] = this.procInstId; // 流程实例ID
	this.reqdata['INST_IF_RUNNING'] = this.wfState; // 流程是否在运行
	if (!(this.getNodeInstBean() == undefined)) {
		this.reqdata['NI_ID'] = this.getNodeInstBean().NI_ID;
	}
	//增加委托用户USER_CODE	，20160928 by ayf begin
	this.appendAgentUserFlagTo(this.reqdata);
	//20160928 by ayf end
	this.wfBtns = {};
	this.wfNextBtns = {};
	this.wfNextBtnNames = {};
	this._confirmSendFlag = true; // 送交标志 true:可以送交， false:不可以送交
};
/**
 * 渲染
 */
mb.vi.wfCardView.prototype.render = function() {
	// 绑定流程按钮
	this._bldBtnBar();
	// 处理文件控制
	this._doFiledControl();
	// 初始化意见
	this._initMind();
	// 确定是否意见为必填
	this._mindMust();
};
/**
 * 构建卡片按钮条，表单按钮，节点定义转换之后的按钮
 */
mb.vi.wfCardView.prototype._bldBtnBar = function() {
	var _self = this;
	_self._buttonData = _self.getButtonBean();
	
	var formSaveButtonFlag = false;
	
	console.log('---流程按钮和表单按钮统一---');
	$.each(this._buttonData, function(i, actItem) {
		var id,
			actCode = actItem.ACT_CODE,
			param = {};
//		console.debug(actItem);
		id = GLOBAL.getUnId(actItem.ACT_CODE, actItem.SERV_ID);
		param['id'] = id;
		param['data-icon'] = actItem.ACT_CSS ? actItem.ACT_CSS : 'default'; // TODO 这个应该是配置的
		
		/*if (actCode == 'save') { // 如果循环到了save
			formSaveButtonFlag = true;
			return;
		}
		
		if (actCode == 'finish' ||				// 办结
			actCode == 'cmSaveAndSend' ||		// 送交流转
			actCode == 'cmWfTrackFigure' ||		// 流程跟踪
			actCode == 'withdraw' ||			// 收回
			actCode == 'cmTuiHui' ||			// 退回
			actCode == 'cmQianShou' ||			// 阅毕
			actCode == 'undoFinish' ||			// 取消办结
			actCode == 'stopParallelWf') {		// 终止会签
			
		} else { // 流程收藏夹，打印，数据管理，变更历史等 暂时屏蔽
			return;
		}*/
		
		if (actCode == 'finish' ||				// 办结
			actCode == 'cmSaveAndSend' ||		// 送交流转
			actCode == 'cmSimpleFenFa' ||		// 分发
			actCode == 'withdraw' ||			// 收回
			actCode == 'toShouwen' ||			// 签收转收文登记
			actCode == 'cmTuiHui' ||			// 退回
			actCode == 'cmQianShou' ||			// 阅毕
			actCode == 'stopParallelWf' 		// 终止会签
			) {					
			
		} else { // 其他功能按钮 暂时屏蔽
			return;
		}
		
		var $liWrapper = $("<li></li>");
		var btn = $("<a href='#'></a>").appendTo($liWrapper);
		btn.attr(param).text(actItem.ACT_NAME);
		
		var actId = actItem.ACT_CODE,
			servId = actItem.SERV_ID;
		// 绑定事件
		_self._act(actId, servId, btn, actItem);
		// 添加到footer
		_self._parHandler.addBtn($liWrapper);
	});
	
	// TODO 这里没看明白，用的时候看原文
	if (!formSaveButtonFlag) { // 没有save,隐藏表单的
		$('#' + GLOBAL.getUnId('save', this.opts.pId)).parent().hide();
	}
};
/**
 * 相关权限 比如能否手写意见，当前人是否正在处理当前的流程
 */
mb.vi.wfCardView.prototype.getAuthBean = function() {
	var _self = this;
	return _self._parHandler._formData.authBean;
};
/**
 * 获取按钮
 * 在中华又加上了表单按钮
 */
mb.vi.wfCardView.prototype.getButtonBean = function() {
	var _self = this;
//	return _self._parHandler._formData.buttonBean;
	var btnsObj = _self._parHandler._servData.BTNS,
	    wfBtnsArray = _self._parHandler._formData.buttonBean;
	$.each(btnsObj, function(index, item) {
		wfBtnsArray.push(item);
	});
	return wfBtnsArray;
};
/**
 * 根据动作绑定相应的方法
 * @param actId 动作ID
 */
mb.vi.wfCardView.prototype._act = function(actId, servId, btn, actItem) {
	var _self = this;
	
	/*if (actItem.ACT_CODE == 'delete') {
		actItem.ACT_CODE == 'deleteDoc';
	}*/
	/*if (actItem.ACT_CODE == 'cmWfTrackFigure') { // 流程跟踪(图像化)，暂时以列表形式展示
		actItem.ACT_CODE == 'cmWfTracking';
	}*/
	if (actItem.ACT_CODE == 'cmSaveAndSend') { // 完成并发送 按钮
		btn.on('vclick', function(event) {
			// 意见是否已修改
			if (_self.mind.isModify()) {
//				alert('您的意见尚未保存，请保存后执行此操作！');
				if (confirm('意见已修改，是否自动保存？')) {
					// 保存意见
					$('#save-btn').trigger('vclick');
				} else {
					// 直接送交
					if (_self.mindMust.pass == 'NO') { // 先执行送交前的代码，这里只判断了'必填意见'
						alert(_self.mindMust.reason);
						return false;
					}
				}
//				return false;
			} else {
				if (_self.mindMust.pass == 'NO') { // 先执行送交前的代码，这里只判断了'必填意见'
					alert(_self.mindMust.reason);
					return false;
				}
			}
			
			var id = GLOBAL.getUnId(actItem.ACT_CODE, actItem.SERV_ID);
			// 弹出popup
			var popupWrp = $("<div data-role='popup' id='" + id + "_popup' data-dismissible='false'>" +
					"<a href='#' class='zhbx-btn-close ui-btn ui-corner-all ui-shadow ui-btn-a ui-icon-delete ui-btn-icon-notext ui-btn-right'>" +
					"Close" +
					"</a>" +
			"</div>");
			var popupListView = $("<ul data-role='listview' data-inset='true' style='min-width:210px;'>" +
					"<li data-role='divider'>" +
					"送交流转" +
					"</li>" +
			"</ul>").appendTo(popupWrp);
			// 添加送交按钮组
			_self._bldNextBtnBar(popupListView);
			popupWrp.on('vclick', 'a.zhbx-btn-close', function(event) {
				popupWrp.popup('close');
				event.preventDefault();
				event.stopImmediatePropagation();
			});
			popupWrp.on('popupafterclose', function() {
				$(this).remove();
			});
			$.mobile.activePage.append(popupWrp);
			popupWrp.popup().enhanceWithin();
			popupWrp.popup('open');
		});
	} else {
		btn.on('vclick', function(event) {
			event.preventDefault();
			event.stopImmediatePropagation();
			if (actItem.ACT_MEMO.length > 1 && actItem.ACT_MEMO != 'null') {
				eval(actItem.ACT_MEMO);
			} else {
				eval('_self.' + actItem.ACT_CODE + '(event, actItem)');
			}
		});
	}
	
	var wfBtnObj = {};
		wfBtnObj.layoutObj = btn;
		wfBtnObj.dataObj = actItem;
		
	_self.wfBtns[actItem.ACT_CODE] = wfBtnObj;
};
/**
 * 构建下一步卡片按钮条，主要针对送交流转
 */
mb.vi.wfCardView.prototype._bldNextBtnBar = function(target) {
	var _self = this;
	var _nextData = _self.getNextStepBean();
	
	$.each(_nextData, function(i, actItem) {
		var id, param={};
		id = GLOBAL.getUnId(actItem.NODE_CODE, 'cmSaveAndSend');
		param['id'] = id;
		param['data-icon'] = actItem.ACT_CSS ? actItem.ACT_CSS : 'default';
		param['data-rel'] = 'dialog';
		var $liWrapper = $("<li></li>");
		var btn = $("<a href='#'></a>").appendTo($liWrapper);
			btn.attr(param).text(actItem.NODE_NAME);
		$liWrapper.appendTo(target);
		
		if (typeof(actItem.NODE_USER) != 'undefined') { // 如果是返回XX的按钮，取得返回人的CODE
			btn.bind('vclick', function() {
				// TODO 自动保存一下意见
				
				if (_self.mindMust.pass == 'NO') { // 先执行送交前的代码，这里只判断了'必填意见'
					alert(_self.mindMust.reason);
					return false;
				}
				
				_self.reqdata['NODE_CODE'] = actItem.NODE_CODE;
				_self.reqdata['TO_USERS'] = actItem.NODE_USER;
				_self.reqdata['TO_TYPE'] = '3';
				// 把流程信息送交给oa
				FireFly.doAct('SY_WFE_PROC_DEF', 'toNext', _self.reqdata).done(function(result) {
					// 送交给oa得到oa返回的成功信息
					// TODO 如果这里的提示有问题，再处理
					alert('已经成功' + actItem.NODE_NAME);
					$.mobile.pageContainer.find('#' + _self._parHandler.pId).remove();
					$.mobile.back();
					setTimeout(_self._parHandler.back, 100);
				});
			});
		} else {
			btn.bind('vclick', function(event) {
				event.preventDefault();
				event.stopImmediatePropagation();
				_self._nextStepNodeCode = actItem.NODE_CODE;
				if (!(_self.getNodeInstBean() == undefined)) {
					_self.reqdata['NI_ID'] = _self.getNodeInstBean().NI_ID;
				}
				_self.reqdata['NODE_CODE'] = actItem.NODE_CODE;
				FireFly.doAct('SY_WFE_PROC_DEF', 'getNextStepUsersForSelect', _self.reqdata).done(function(result) {
					_self._openSelectOrg(actItem.NODE_CODE, result);
				});
			});
			
			var nextBtnObj = {};
			nextBtnObj.layoutObj = btn;
			nextBtnObj.dataObj = actItem;
			_self.wfNextBtns[actItem.NODE_CODE] = nextBtnObj;
			_self.wfNextBtnNames[actItem.NODE_NAME] = nextBtnObj;
		}
	});
};
/**
 * 获取流程下一步Bean
 */
mb.vi.wfCardView.prototype.getNextStepBean = function() {
	var _self = this;
	if (_self._parHandler._formData.nextSteps == undefined || 
			_self._parHandler._formData.nextSteps == 'undefined') {
		return '';
	}
	return _self._parHandler._formData.nextSteps;
};
/**
 * 节点实例bean
 */
mb.vi.wfCardView.prototype.getNodeInstBean = function() {
	var _self = this;
	return _self._parHandler._formData.nodeInstBean;
};
/**
 * 开启人员选择树
 */
mb.vi.wfCardView.prototype._openSelectOrg = function(nodeCode, result) {
	var _self = this;
	
	// TODO 自动保存一下意见
	
	if (_self.mindMust.pass == 'NO') { // 先执行送交前的代码，这里只判断了'必填意见'
		alert(_self.mindMust.reason);
		return false;
	}
	
	var rtnTreeData = result;
	var treeData = rtnTreeData.treeData;
	
	if (treeData == '[]') {
		alert('当前节点没有可供选择的人员，请检查工作流配置！');
		return false;
	}
	
	var sendDirectFlag = true; // 是否直接送交
	var oneUserCodes = '';
	
	var bindTreeTitle = '人员选择';
	this.binderType = rtnTreeData.binderType; // 送交类型
	
	if (_self.binderType == 'ROLE') {
		// 如果是角色，取到角色的CODE
		this._binderRoleCode = rtnTreeData.roleCode;
		bindTreeTitle = '角色选择';
		sendDirectFlag = false;
	} else { // 送人
//		var rtnOneValue = _self.treeDataHaseOneMan(treeData);
		var rtnOneValue = 'multi';
		if (rtnOneValue == 'multi') {
			sendDirectFlag = false;
		} else {
			oneUserCodes = rtnOneValue;
		}
	}
		if(rtnTreeData["ALL_ORG"] == "yes") {
		// 选中组织机构结构之后，回调方法：_self._confirmSend
		// 建议角色ID，从工作流定义里面获取。
		var multiSelect = rtnTreeData.multiSelect; // 是否能多选
		this.binderType = rtnTreeData.binderType; // 送交类型

		// 如果是角色，取到角色的CODE
		this._binderRoleCode = rtnTreeData.roleCode;
		var extendTreeSetting = "{'rhexpand':false";

		var selectType = "single";
		if (multiSelect == "true") {
			selectType = "multi";
			extendTreeSetting += "'cascadecheck':true,'checkParent':false,'showcheck':true,'childOnly':true";
		} else {
			extendTreeSetting += "'cascadecheck':false,'checkParent':false,'showcheck':false";
		}
		
		extendTreeSetting += "}";
		extendTreeSetting = StrToJson(extendTreeSetting);
		//判断送角色还是用户
		var configStr = "SY_ORG_DEPT_USER_ALL";
		if(rtnTreeData["binderType"] =="ROLE"){
			configStr = "SY_ORG_DEPT_ALL";
		}
		//configStr += ",{'TYPE':'" + selectType + "','MODEL':'default'}"; // multi
		configStr += ",{'TYPE':'" + selectType + "'}"; // multi
		var options = {
			"itemCode" : inputName,
			"config" : configStr,
			"hide" : "explode",
			"show" : "blind",
			"rebackCodes" : inputName,
			"replaceCallBack" : _self._confirmSendAll,
			"extendDicSetting" : extendTreeSetting,
			"dialogName" : bindTreeTitle,
			"parHandler" : _self
		};

		var dictView = new rh.vi.rhDictTreeView(options);
		dictView.show(event);

		// 是否自动选择最终用户
		if (rtnTreeData.autoSelect == 'true') {
			var users = _findAllUsers(treeData);
			var userIds = new Array();
			jQuery(users).each(function(index, obj) {
				userIds.push(obj.ID);
			});

			dictView.tree.selectNodes(userIds);
			dictView.tree.expandParent();
		}
		return;
	}
//	if (sendDirectFlag) { // 直接送人
//		var oneUserObj = StrToJson("{" + oneUserCodes + "}");
//		var userCodeArray = [];
//			userCodeArray.push(oneUserObj.ID);
//		_self._confirmSend(userCodeArray);
//	} else { // 显示组织机构树
		var len = $('#' + nodeCode + '_dialog').length;
		if (!len) {
			var extendTreeSetting = {
					cascadecheck: false,
					checkParent: false,
					rhexpand: true,
					showcheck: false
			};
			
			var multiSelect = rtnTreeData.multiSelect; // 是否能多选
			if (multiSelect == 'true') {
				extendTreeSetting['cascadecheck'] = true;
				extendTreeSetting['showcheck'] = true;
			}
			
			treeData = eval('(' + treeData + ')');
			extendTreeSetting['data'] = treeData;
			
			// TODO 封装page
			
			var pageWrp = $("<div data-role='page' id='" + nodeCode + "_dialog' data-theme='b'></div>");
			var headerWrp = $("<div data-role='header' data-positon='fixed' data-tap-toggle='false'>" +
//								"<a href='#' data-rel='back' class='ui-btn ui-btn-icon-left ui-btn-icon-notext ui-icon-back'>" +
//								"返回" +
//								"</a>" +
								"<a href='#' data-rel='back' data-icon='back' data-iconpos='notext'>返回</a>" +
								"<h1></h1>" +
								"</div>").appendTo(pageWrp);
			var contentWrp = $("<div role='main' class='ui-content'></div>").appendTo(pageWrp);
			var footerWrp = $("<div data-role='footer' data-position='fixed' data-tap-toggle='false'>" +
								"<div data-role='navbar'>" +
									"<ul>" +
										"<li>" +
											"<a href='#' id='cancel' data-rel='back' class='ui-link ui-btn ui-icon-cancel ui-btn-icon-top'>取消</a>" +
										"</li>" +
										"<li>" +
											"<a href='#' id='save' class='ui-link ui-btn ui-icon-confirm ui-btn-icon-top'>确认</a>" +
										"</li>" +
									"</ul></div>" + 
								"</div>").appendTo(pageWrp);
			// 渲染数据
			var $treeWrp = $("<div></div>").appendTo(contentWrp);
			
			// 确认
			footerWrp.on('vclick', '#save', function(event) {
				event.preventDefault();
				event.stopImmediatePropagation();
				var ids = [];
				if (multiSelect == 'true') { // checkbox
					var checks = $treeWrp.find('.bbit-tree-node-leaf .checkbox_true_full');
					$.each(checks, function(i, el) {
						var $parent = $(this).parent(),
							id = $parent.attr('itemid');
						ids.push(id);
					});
				} else { // radio
					var check = $treeWrp.find('.bbit-tree-node-leaf.bbit-tree-selected');
					if (check.length) {
						var id = check.attr('itemid');
						ids.push(id);
					}
				}
				_self._confirmSend(ids);
			});
			
			$treeWrp.treeview(extendTreeSetting);
			
			pageWrp.appendTo($.mobile.pageContainer).page(); // 显示树形选择页
			pageWrp.on('pagehide', function(event, ui) {
				$(this).remove();
			});
		}
		$.mobile.pageContainer.pagecontainer('change', pageWrp);
//	}
};
/**
 * 判断返回的组织资源树，是否只有一个人
 */
mb.vi.wfCardView.prototype.treeDataHaseOneMan = function(treeData) {
	if (treeData == '[]') {
		return 'multi';
	}
	
	var reg = new RegExp("\"NAME\":.*?\"usr\".*?usr:.*?\"", "g");
	
	if (treeData.match(reg).length == 1) {
		// 取到这个人，然后直接送这个人
		var userCode = treeData.match(reg);
		return userCode;
	}
	
	return 'multi';
};
mb.vi.wfCardView.prototype._confirmSend = function(idArray) {
	var _self = this;
	
	if (!_self._confirmSendFlag) { // 如果标志为false, 就是不能送交
		return false;
	} else {
		_self._confirmSendFlag = false;
	}
	
	// 送交的类型：1 送交部门+角色  , 3 送用户
	var toType = '3';
	if (_self.binderType == 'ROLE') {
		toType = '1';
		_self.reqdata['TO_DEPT'] = idArray[0].replace('dept:', ''); // 送交部门
		_self.reqdata['TO_ROLE'] = _self._binderRoleCode; // 送交角色
	} else {
		var userArray = [];
		
		if (idArray.length) {
			$(idArray).each(function(i, intrty) {
				if (intrty.indexOf('usr:') == 0) {
					userArray.push(intrty);
				}
			});
		}
		
		var userNameStr = userArray.toString().replace(new RegExp("usr:", "gm"), "");
		
		if (userNameStr.length <= 0) {
			alert('没有选中人员，请重新选择送交人员！');
			_self._confirmSendFlag = true;
			return false;
		}
		_self.reqdata['TO_USERS'] = userNameStr; // 送交人  替换掉所有的usr:
	}
	
	_self.reqdata['TO_TYPE'] = toType; // 类别
	if (!(this.getNodeInstBean() == undefined)) {
		_self.reqdata['NI_ID'] = _self.getNodeInstBean().NI_ID; // 当前节点实例ID
	}
	_self.reqdata['NODE_CODE'] = _self._nextStepNodeCode; // 下一个节点CODE
	FireFly.doAct('SY_WFE_PROC_DEF', 'toNext', _self.reqdata).done(function(result) {
		if (Tools.actIsSuccessed(result)) {
			alert('已经成功送交！');
			$.mobile.pageContainer.find('#' + _self._parHandler.pId).remove();
			$.mobile.back();
			setTimeout(_self._parHandler.back, 100);
		} else {
			alert('送交失败！');
		}
		setTimeout(function() {
			_self._confirmSendFlag = true;
		}, 1000);
	});
};
/**
 * 对页面字段进行控制，如显示、隐藏、必填等控制
 */
mb.vi.wfCardView.prototype._doFiledControl = function() {
	var _self = this;
	var _fileControlData = _self.getFieldControlBean();
	
//	var entirelyControl = _fileControlData.FIELD_CONTROL;		// 是否完全控制
	var exceptionFieldStr = _fileControlData.FIELD_EXCEPTION;  	// 可编辑
	var displayFieldStr = _fileControlData.FIELD_DISPLAY;		// 显示字段
	var hiddenFieldStr = _fileControlData.FIELD_HIDDEN;			// 隐藏字段
	var mustFieldStr = _fileControlData.FIELD_MUST;				// 必填字段
	var groupDis = _fileControlData.GROUP_DISPLAY || '';		// 显示分组
	var groupHide = _fileControlData.GROUP_HIDE || '';			// 隐藏分组
	
	var parServID = _self.opts.pId;
	
	// 可编辑字段
	/*if (entirelyControl == 'false') {
		_self._parHandler.form.disabledAll();
		if (exceptionFiledStr.length > 0) {
			$.each(exceptionFiledStr.split(','), function(i, item) {
				if (_self._parHandler.getItem(item)) {
					_self._parHandler.getItem(item).enabled();
				}
			});
		}
	}*/
	
	// 显示字段
	if (displayFieldStr && displayFieldStr.length > 0) {
		var disps = displayFieldStr.split(',');
		$.each(disps, function(i, itemCode) {
			var field = _self._parHandler.getItem(itemCode);
			if (field) {
				var $li = field.getContainer();
				if ($li.attr('model') != UIConst.ITEM_MOBILE_FORCEHIDDEN) { // 如果不是强制隐藏
					$li.show();
				}
			}
		});
	}
	// 隐藏字段
	if (hiddenFieldStr && hiddenFieldStr.length > 0) {
		$.each(hiddenFieldStr.split(','), function(i, item) {
			_self._parHandler.getItem(item).getContainer().hide();
		});
	}
	// 必填字段
	/*if (mustFieldStr && mustFieldStr.length > 0) {
		$.each(mustFieldStr.split(','), function(i, item) {
			_self._parHandler.form.setNotNull(item, true); // TODO 没有这个方法啊
		});
	}*/
	// 显示分组
	if (groupDis && groupDis.length > 0) {
		var groupArr = groupDis.split(',');
		$.each(groupArr, function(i, itemCode) {
			_self._parHandler.showGroup(itemCode); // TODO 这个方法大概也没有
		});
	}
	// 隐藏分组
	if (groupHide && groupHide.length > 0) {
		var groupArr = groupHide.split(',');
		$.each(groupArr, function(i, itemCode) {
			_self._parHandler.hideGroup(itemCode);
		});
	}
	
};
/**
 * 获取页面field控制的数据
 */
mb.vi.wfCardView.prototype.getFieldControlBean = function() {
	var _self = this;
	return _self._parHandler._formData.fieldControlBean;
};
/**
 * 初始化意见
 */
mb.vi.wfCardView.prototype._initMind = function() {
	var _self = this;
	var targetContainer = _self._parHandler.form.mainContainer;
	
	var group = $("<div id='"+_self._parHandler.pkCode+"_MIND' class='mb-card-group'></div>");
	
	var mindInputFieldCon = $("<div data-role='fieldcontain' data-theme='b'></div>"); // 意见输入框Field
	
	var mindFieldCon = $("<div data-role='fieldcontain' data-theme='b'></div>"); // 意见列表Field
		mindFieldCon.append("<label for='mind-list'>意见列表</label>");
		
	group.append(mindInputFieldCon).append(mindFieldCon); // 追加到意见group中
	group.insertAfter(targetContainer.find('.mb-card-group:first'));
	
	var param = {
			'servId': _self._parHandler.getServSrcId(),
			'dataId': _self.pkCode,
			'mind': _self._parHandler._mind, // TODO 确定意见是否是按照部门排起来的
			'wfCard': this,
			'pCon': [mindInputFieldCon, mindFieldCon]
	};
	
	this.mind = new mb.vi.mind(param); // TODO
	this.mind.show();
};
/**
 * 必填意见的判断，不通过mind去判断了，写到这里，直接查数据库
 */
mb.vi.wfCardView.prototype._mindMust = function() {
	var _self = this;
	
	var mindMustReq = {};
	mindMustReq.GENERAL = JsonToStr(_self.getMindCodeBean());
	mindMustReq.REGULAR = JsonToStr({'MIND_MUST': '2'});
	mindMustReq.TERMINAL = JsonToStr({'MIND_MUST': '2'});
	
	if (!(this.getNodeInstBean() == undefined)) {
		mindMustReq.NI_ID = _self.getNodeInstBean().NI_ID;
	}
	mindMustReq.DATA_ID = _self._parHandler.pkCode;
	
	FireFly.doAct('SY_COMM_MIND', 'checkFillMind', mindMustReq).done(function(result) {
		_self.mindMust = result
	});
};
/**
 * 意见 Code Bean
 */
mb.vi.wfCardView.prototype.getMindCodeBean = function() {
	var _self = this;
	
	return _self._parHandler._formData.mindCodeBean;
};
/**
 * 终止并发
 */
mb.vi.wfCardView.prototype.stopParallelWf = function() {
	var _self = this;
	
	FireFly.doAct('SY_WFE_PROC_DEF', 'stopParallelWf', _self.reqdata).done(function(result) {
		// 将此条信息的列表删除，避免点击过期的数据
		$.mobile.pageContainer.find('#' + _self._parHandler.pId).remove();
		$.mobile.back();
	});
};
/**
 * 获取按钮
 */
mb.vi.wfCardView.prototype._getBtn = function(actCode) {
	return this.wfBtns[actCode];
};
/**
 * 获取按钮 根据CODE
 */
mb.vi.wfCardView.prototype._getWfNextBtn = function(actCode) {
	return this.wfNextBtns[actCode];
};
/**
 * 获取按钮 根据名称
 */
mb.vi.wfCardView.prototype._getNextBtnByName = function(actName) {
	return this.wfNextBtnNames[actName];
};
/**
 * 转发
 */
mb.vi.wfCardView.prototype._zhuanFa = function(idArray, nameArray) {
	var _self = this;
	_self.reqdata['TARGET_USERS'] = idArray.join(',');
	
	_self.reqdata['SERV_ID'] = _self._parHandler.servId;
	_self.reqdata['DATA_ID'] = _self._parHandler.pkCode;
	_self.reqdata['DATA_TITLE'] = _self.getBindTitle() + '(分发)'; // _self._parHandler.itemValue('GW_TITLE');
	
	var result = rh_processData(WfActConst.SERV_PROC + '.cmZhuanFa.do', _self.reqdata);
	
	if (result.rtnstr == 'success') {
		Tip.show('转发成功', true);
	} else {
		Tip.show('转发失败', true);
	}
	_self._parHandler.refresh();
};
/**
 * 获取标题
 */
mb.vi.wfCardView.prototype.getBindTitle = function() {
	var _self = this;
	return _self._parHandler._formData.bindTitle;
};
/**
 * 签收 | 阅毕
 */
mb.vi.wfCardView.prototype.cmQianShou = function(event, actItem) {
	var _self = this;
	// 分发ID
	var sendId = actItem.SEND_ID;
	_self.reqdata.SEND_ID = sendId;
	FireFly.doAct('SY_COMM_SEND_SHOW_CARD', 'cmQianShou', _self.reqdata).done(function(result) {
		if (result.rtnstr == 'success') {
			$.mobile.pageContainer.find('#' + _self._parHandler.pId).remove();
			$.mobile.back();
		} else {
			alert('操作失败！');
		}
	});
};
/**
 * 流程跟踪
 */
mb.vi.wfCardView.prototype.cmWfTracking = function() {
	console.log('---流程跟踪--略---');
};
/**
 * 签收转收文登记
 */
mb.vi.wfCardView.prototype.toShouwen = function(event, actItem) {
	var _self = this;
	
	var reqData = {};
	reqData.TMPL_CODE = _self._parHandler.getByIdData("TMPL_CODE");
	
	FireFly.doAct("OA_GW_TYPE_FW_SEND","findUserSwMenu",reqData).done(function(result) {
		// TODO 中华只用到了绑定一个模板，所以下面的没有测试
		if (result.SW_TMPL_CODE) { //找到了模板编码上设置的收文模板
			_self.qianshouToShouwen(result.SW_TMPL_CODE);
			return;
		} else {
			var dataList = result._DATA_;
			if (dataList.length == 0) {
				alert('您还没有起草收文的权限。');
				return;
			} else if (dataList.length == 1) {
				var menuItem = dataList[0];
				_self.qianshouToShouwen(menuItem.SERV_NAME, menuItem.SERV_ID); // TODO
				return;
			}
			
			var id = 'qianshou-to-shouwen-popup';
			var popupWrp = $("<div data-role='popup' id='" + id + "_popup' data-dismissible='false'>" +
								"<a href='javascript:void(0);' class='zhbx-btn-close ui-btn ui-corner-all ui-shadow ui-btn-a ui-icon-delete ui-btn-icon-notext ui-btn-right'>" +
									"Close" +
								"</a>" +
							 "</div>");
			var popupListView = $("<ul data-role='listview' data-inset='true' style='min-width:210px;'>" +
									"<li data-role='divider'>" +
										"请选择收文类型" +
									"</li>" +
								  "</ul>").appendTo(popupWrp);
			$.each(dataList, function(index, item) {
				popupListView.append("<li><a href='javascript:void(0);' class='qianshou-to-shouwen-item-js' data-servid='" + item['SERV_ID'] + "'>" + item['SERV_NAME'] + "</a></li>");
			});
			$.mobile.activePage.append(popupWrp); // 追加popup到活动页
			popupWrp.popup().enhanceWithin(); // 初始化popup
			
			// 关闭绑定事件
			popupWrp.on('tap', 'a.zhbx-btn-close', function() {
				event.preventDefault();
				event.stopImmediatePropagation();
				popupWrp.popup('close');
			});
			// 列表绑定点击事件
			popupWrp.on('tap', 'a.qianshou-to-shouwen-item-js', function(event) {
				event.preventDefault();
				event.stopImmediatePropagation();
				
				var servId = $(this).attr('data-servid')
				,	servName = $(this).text();
				
				if (!confirm('是否转换成 ' + servName + '?')) {
					return;
				}
				try {
					_self.qianshouToShouwen(servId);
				} finally {
					popupWrp.popup('close');
				}
			});
		}
	});
};
/**
 * 签收转收文登记的转换方法
 */
mb.vi.wfCardView.prototype.qianshouToShouwen = function(newServId) {
	var _self = this;
	
	var params = {};
	params['SEND_ID'] = _self._parHandler.getByIdData('SEND_ID');
	params['nextServId'] = newServId;
	params['SERV_ID'] = _self._parHandler.getByIdData('TMPL_CODE');
	params['DATA_ID'] = _self._parHandler.getByIdData('GW_ID');
	
	$.mobile.loading('show', {
		text: '处理中...',
		textVisible: true,
		textonly: false
	});
	
	FireFly.doAct('OA_GW_TYPE_FW_SEND', 'toShouwen', params).done(function(result) {
		$.mobile.loading('hide');
		if (result != null && StringUtils.startWith(result._MSG_, 'OK')) {
			// TODO 直接回首页
			$.mobile.back();
			setTimeout(_self._parHandler.back, 100);
		} else {
			alert(result._MSG_);
		}
	});
};
/**
 * 办结
 */
mb.vi.wfCardView.prototype.finish = function() {
	var _self = this;
	FireFly.doAct('SY_WFE_PROC_DEF', 'finish', _self.reqdata).done(function(result) {
		alert('办结成功！');
		$.mobile.pageContainer.find('#' + _self._parHandler.pId).remove();
		$.mobile.back();
	});
};
/**
 * 取消办结
 */
mb.vi.wfCardView.prototype.undoFinish = function() {
	var _self = this;
	FireFly.doAct('SY_WFE_PROC_DEF', 'undoFinish', _self.reqdata).done(function(result) {
		// 返回到list页面
		$.mobile.back();
	});
};
/**
 * 收回
 */
mb.vi.wfCardView.prototype.withdraw = function(event, actItem) {
	var _self = this;
	var wdlist = actItem.wdlist;
	if (wdlist) {
		if (wdlist.length == 0) {
			alert('没有可以收回的流程。');
		} else if (wdlist.length == 1) {
			_self._sendWithdrawReq(wdlist[0]['NI_ID']);
		} else { // 如果有多个用户请求需要被收回
			// 弹出dialog
			var pageWrp = $("<div data-role='page' id='" + _self.procInstId + "_withdraw'></div>");
			var headerWrp = $("<div data-role='header' data-position='fixed' data-tap-toggle='false' data-theme='b'>" +
//								"<a href='#' data-rel='back' data-icon='back' data-inline='true' data-iconpos='notext'></a>" +
								"<a href='' data-rel='back' data-icon='back' data-iconpos='notext'>返回</a>" +
								"<h1></h1>" +
								"</div>").appendTo(pageWrp);
			var contentWrp = $("<div role='main' class='ui-content'></div>").appendTo(pageWrp);
			var tempArr = [];
			tempArr.push("<fieldset data-role='controlgroup'>");
			for (var i=0; i<wdlist.length; i++) {
				var niBean = wdlist[i];
//				tempArr.push("<input type='checkbox' id='" + niBean['NI_ID'] + "'");
//				tempArr.push("<label for='" + niBean['NI_ID'] + "'>" + niBean['TO_USER_NAME'] + "</label>");
				tempArr.push("<label for='" + niBean['NI_ID'] + "'>" + niBean['TO_USER_NAME'] + "</label>");
				tempArr.push("<input type='checkbox' id='" + niBean['NI_ID'] + "' />");
			}
			tempArr.push("</fieldset>");
			contentWrp.html(tempArr.join(""));
			
//			var footerWrp = $("<div	data-role='footer' data-position='fixed' data-tap-toggle='false'>" +
//								"<a href='#' id='withdrawCancel' data-role='button' data-rel='back' data-icon='delete' class='ui-btn-left'>取消</a>" +
//								"<span class='ui-title'></span>" +
//								"<a href='#' id='withdrawSave' data-role='button' data-icon='check' class='ui-btn-right'>确认</a>" +
//								"</div>").appendTo(pageWrp);
			var footerWrp = $("<div data-role='footer' data-position='fixed' data-tap-toggle='false' data-theme='b'>" +
								"<div data-role='navbar'>" +
								"<ul class='ui-grid-a'>" +
								"<li class='ui-block-a'>" +
								"<a href='#' id='withdrawCancel' data-icon='cancel'>取消</a>" +
								"</li>" +
								"<li class='ui-block-b'>" +
								"<a href='#' id='withdrawSave' data-icon='confirm'>确认</a>" +
								"</li>" +
								"</ul>" +
								"</div>" +
								"</div>").appendTo(pageWrp);
			
			// 确认
//			footerWrp.on('tap', '#widthdrawSave', function(event) {
			footerWrp.on('vclick', '#withdrawSave', function(event) {
				var ids = [];
				contentWrp.find("input:checked").each(function(i, obj) {
					ids.push(this.id);
				});
				_self._sendWithdrawReq(ids.join(","));
			});
			
			pageWrp.appendTo($.mobile.pageContainer).page();
			pageWrp.on('pagehide', function(event, ui) {
				$(this).remove();
			});
			$.mobile.changePage($('#' + _self.procInstId + '_withdraw'), {transition: 'slideup'});
		}
	}
};
/**
 * 发送收回请求
 */
mb.vi.wfCardView.prototype._sendWithdrawReq = function(nextNiIds) {
	var _self = this;
	var data = {};
	$.extend(data, _self.reqdata, {'nextNiIds': nextNiIds});
	FireFly.doAct('SY_WFE_PROC_DEF', 'withdraw', data).done(function(result) {
		$.mobile.pageContainer.find('#' + _self._parHandler.pId).remove();
		$.mobile.back();
	});
};
/**
 * 简单分发
 */
mb.vi.wfCardView.prototype.cmSimpleFenFa = function(event, actItem) {
	var _self = this;
	
	$('#cmSimpleFenFa_dialog').remove(); // 移除旧dialog
	
	var params = { // 异步加载子节点参数
			'DATA_ID': _self._parHandler.pkCode,
			'userSelectDict': 'SY_ORG_DEPT_USER_SUB',
			'displaySendSchm': false,
			'includeSubOdept': true	// 这个参数貌似没用，控制组织机构的是USER_SUB和USER这两个字典
	};
	
	//用户可重载参数
	if(this.beforeCmSimpleFenFa){
		var result = this.beforeCmSimpleFenFa(actItem,params);
		if(!result){
			return;
		}
	}
	
	// 异步加载子节点请求路径
	var dictUrl = FireFly.getContextPath() + '/SY_COMM_INFO.dict.do' + '?' + $.param(params);
	// treeview的输入参数
	var extendTreeSetting = {
		cascadecheck: true,
		checkParent: false,
		url: dictUrl,
		dictId: '@com.rh.core.serv.send.SimpleFenfaDict',
		rhexpand: false,
		showcheck: true,
		afterExpand: function() {
//			console.debug(this);
		}
	};
	var treeData = [];
	
	FireFly.getDict('@com.rh.core.serv.send.SimpleFenfaDict', '', '', '', '', params).then(function(tempData) {
		
		// 构造树形显示数据
		treeData.push(tempData['CHILD'][0]);
		if (tempData['CHILD'].length > 1) {
			treeData.push(tempData['CHILD'][1]);
		}
		extendTreeSetting['data'] = treeData;
		
		
		// 封装page
		var pageWrp = $("<div data-role='page' id='cmSimpleFenFa_dialog' data-theme='b'></div>");
		var headerWrp = $("<div data-role='header' data-positon='fixed' data-tap-toggle='false'>" +
				"<a href='#' data-rel='back' data-icon='back' data-iconpos='notext'>返回</a>" +
				"<h1></h1>" +
		"</div>").appendTo(pageWrp);
		var contentWrp = $("<div role='main' class='ui-content'></div>").appendTo(pageWrp);
		var footerWrp = $("<div data-role='footer' data-position='fixed' data-tap-toggle='false'>" +
							"<div data-role='navbar'>" +
							"<ul>" +
							"<li>" +
							"<a href='#' id='cancel' data-rel='back' class='ui-link ui-btn ui-icon-cancel ui-btn-icon-top'>取消</a>" +
							"</li>" +
							"<li>" +
							"<a href='#' id='save' class='ui-link ui-btn ui-icon-confirm ui-btn-icon-top'>确认</a>" +
							"</li>" +
							"</ul></div>" + 
					"</div>").appendTo(pageWrp);
		
		// 渲染数据
		var $treeWrp = $('<div></div>').appendTo(contentWrp);
		
		// 确认
		footerWrp.on('vclick', '#save', function(event) {
			event.preventDefault();
			event.stopImmediatePropagation();
			
			// TODO save方法
			var ids = [];
			var checks = $treeWrp.find('.bbit-tree-node-leaf .checkbox_true_full');
			$.each(checks, function(i, el) {
				var $parent = $(this).parent(),
					id = $parent.attr('itemid');
				ids.push(id);
			});
			
			// 发送
			var sendObj = {
					'SERV_ID': _self._parHandler.servId,
					'DATA_ID': _self._parHandler.pkCode,
					'DATA_TITLE': _self.getBindTitle()
			};
			sendObj.fromScheme = 'yes'; // 来源于方案
			sendObj.ifFirst = 'yes';
			sendObj._extWhere = " and DATA_ID = '" + _self._parHandler.pkCode + "'";
			sendObj.SEND_ITEM = [{'sendId': ids.join(',')}];
			sendObj.includeSubOdept = true; // 是否包含分发法案
			
			// 添加屏蔽层
			$.mobile.loading('show', {
				text: '处理中...',
				textVisible: true,
				textonly: false
			});
			
			setTimeout(function() { // 适当加上延迟提升用户感觉
				try {
					var data = $.toJSON(sendObj);
					FireFly.doAct('SY_COMM_SEND_SHOW_USERS', 'autoSend', {'data': data}).then(function(result) {
						// 去掉屏蔽层
						$.mobile.loading('hide');
						
						if (result['_MSG_'].indexOf('OK,') < 0) {
							alert('分发失败，请联系系统管理员！');
						} else {
							alert('分发成功，点击确认按钮返回！');
							$.mobile.back();
						}
					});
				} catch (e) {
					throw e;
				} finally {
				}
			},50);
		});
		
		$treeWrp.treeview(extendTreeSetting);
		
		pageWrp.appendTo($.mobile.pageContainer).page(); // 显示树形选择页
		
		pageWrp.on('pagehide', function(event, ui) {
			$(this).remove();
		});
		
//		$.mobile.pageContainer.pagecontainer('change', pageWrp);
		// 这里只能用Id,否则url不变，点击返回就直接回到了列表页
		$.mobile.changePage('#cmSimpleFenFa_dialog', {transition: 'fade'});
	});
};

/**
 * 简单分发之前调用，常用于重置params的值。
 */
mb.vi.wfCardView.prototype.beforeCmSimpleFenFa = function(actItem,params){
	return true;
};

/**
 * 如果有委托办理，则增加委托办理人标识,20160928 by ayf
 */
mb.vi.wfCardView.prototype.appendAgentUserFlagTo = function(reqdata){
	//增加委托用户USER_CODE
	var _agentUser = this._parHandler.getByIdData("_AGENT_USER_");
	if(_agentUser != ""){
		reqdata["_AGENT_USER_"] = _agentUser;
	}	
};

/**
 * 流程节点上组织资源上配置全部的流程送下个节点的人
 */
mb.vi.wfCardView.prototype._confirmSendAll = function(idArray, nameArray) {
	var _self = this;
	if (_self.callback.call(_self, idArray, nameArray)) { // 判断有没有出部门
		_self._parHandler.shield();
		// 送交的类型 1 送部门+角色 ， 3 送用户
		var toType = "3";

		if (_self.binderType == "ROLE") {
			toType = "1";
			_self.reqdata["TO_DEPT"] = idArray.join(","); // 送交部门
			_self.reqdata["TO_ROLE"] = _self._binderRoleCode; // 送交角色
		} else {
			var userArray = new Array();
			jQuery(idArray).each(function(indextrty, intrty) {
					userArray.push(intrty);
			});

			var userNameStr = userArray.toString().replace(
					new RegExp("usr:", "gm"), "");

			if (userNameStr.length <= 0) {
				alert("没有选中人员，请重新选择送交人员");
				_self._parHandler.shieldHide(); // 清除锁定页面

				return false;
			}
			_self.reqdata["TO_USERS"] = userNameStr; // 送交人 替换掉所有的usr:
		}
		_self.reqdata["TO_TYPE"] = toType; // 类别
		_self.reqdata["NI_ID"] = _self.getNodeInstBean().NI_ID; // 当前节点实例ID
		_self.reqdata["NODE_CODE"] = _self._nextStepNodeCode; // 下个节点CODE

		var result = rh_processData("SY_WFE_PROC_DEF.toNext.do", _self.reqdata);
		
		if(Tools.actIsSuccessed(result)){
			//是否启用委托，显示委托提示信息
			var userNames = _self.isExistAgent(idArray, toType, _self._parHandler.servId);
			if (userNames != "") {
				Tip.show("已经成功送交给" + userNames, true);
			} else {
				Tip.show("已经成功送交给" + nameArray.join(","), true);
			}
			top.Tip.clear();
			if(result._closeDlg == "false"){
				_self._parHandler.refresh();
			} else {
				// 关闭当前页面
				//
				_self._parHandler.setParentRefresh();
				_self._parHandler.backClick();	
			}
		}else{
			alert("送交失败。");
		}
		_self._parHandler.shieldHide();
	}
};