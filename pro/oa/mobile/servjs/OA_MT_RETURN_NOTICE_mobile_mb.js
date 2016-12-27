//@ sourceURL=OA_MT_RETURN_NOTICE_mobile_mb.js

var _viewer = this;

console.debug(this);

// 去掉footer
//_viewer.footerWrp.remove();

// 添加保存按钮
_viewer.footerWrp.html('');
var footerNavBar = $("<div data-role='navbar' class='customNav'></div>").appendTo(_viewer.footerWrp);
var footerNavWrp = $("<ul></ul>").appendTo(footerNavBar);
var $liWrapper = $('<li></li>')
,   $btn = $('<a href="#"></a>').appendTo($liWrapper);
$btn.attr({'id': meetingId, 'data-icon': 'deliver'}).text('保存并回执');
$liWrapper.appendTo(footerNavWrp);


// 获取表单字段
var $statusCon = _viewer.form.getItem('STATUS').getContainer();
var $memoCon = _viewer.form.getItem('MEMO').getContainer();
var $userCodesCon = _viewer.form.getItem('USER_CODES').getContainer();


// 设置初始值
_viewer.form.getItem('STATUS').setValue(1); // 单选框设置初始值
var parHandler = _viewer.opts.parHandler; // 会议句柄
var meetingId = parHandler.itemValue('MEETING_ID')
,	title = parHandler.itemValue('TITLE');
_viewer.form.getItem('MEETING_ID').setValue(title);
$userCodesCon.parent().hide(); // 隐藏掉与会人员选择


// 重置可用状态
$statusCon.find('fieldset').removeClass('ui-disabled').attr('readonly', false).removeAttr('data-type').removeAttr('data-mini');
$memoCon.find('span').attr('contenteditable', true);
//$userCodesCon.parent().append('<div class="ui-grid-a">'+
//					    '<div class="ui-block-a">'+
//						'<a id="select-user" href="#" class="ui-link ui-btn ui-shadow ui-corner-all" data-role="button" role="button">'+
//						    '选择人员'+
//						'</a>'+
//					    '</div>'+
//					    '<div class="ui-block-b">'+
//					        '<a id="clear-user" href="#" data-role="button" class="ui-link ui-btn ui-shadow ui-corner-all" role="button">'+
//						    '清空人员'+
//						'</a>'+
//					    '</div>'+
//					'</div>');

// 针对pad添加一个空的label,让按钮显示出来
$userCodesCon.parent().append('<label></label><div class="ui-grid-a">'+
					    '<div class="ui-block-a">'+
						'<a id="select-user" href="#" class="ui-link ui-btn ui-shadow ui-corner-all" data-role="button" role="button">'+
						    '选择人员'+
						'</a>'+
					    '</div>'+
					    '<div class="ui-block-b">'+
					        '<a id="clear-user" href="#" data-role="button" class="ui-link ui-btn ui-shadow ui-corner-all" role="button">'+
						    '清空人员'+
						'</a>'+
					    '</div>'+
					'</div>');


// 绑定事件
$userCodesCon.parent().on('vclick', '#select-user', function(event) { // 选择人员
	event.preventDefault();
	event.stopImmediatePropagation();
	
	// TODO 因为在mobile项目中没有字典组件，这里单写一个，如果以后选择人的地方有很多，需要重写
	$('#select-user_dialog').remove();
	
	// 构造树形选择参数
	var extendTreeSetting = {
			cascadecheck: true,
			checkParent: false,
			url: FireFly.getContextPath() + '/SY_COMM_INFO.dict.do',
			dictId: 'SY_ORG_DEPT_USER_SUB',
			rhexpand: false,
			showcheck: true
	};
	
	FireFly.getDict('SY_ORG_DEPT_USER_SUB').then(function(tempData) {
		// 初始化初始树形数据
		extendTreeSetting['data'] = tempData['CHILD'];
		
		var pageWrp = $("<div data-role='page' id='select-user_dialog' data-theme='b'></div>");
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
			_viewer.ids = [], // 上次选人的记录已经被清空了
			_viewer.names = [];
			var checks = $treeWrp.find('.bbit-tree-node-leaf .checkbox_true_full');
			$.each(checks, function(i, el) {
				var $parent = $(this).parent(),
				id = $parent.attr('itemid');
				_viewer.ids.push(id);
				name = $parent.attr('title');
				_viewer.names.push(name);
			});

			_viewer.form.getItem('USER_CODES').setValue(''); // 先清空
			_viewer.form.getItem('USER_CODES').setValue(_viewer.names.join(','));
			$.mobile.back();
		});
		
		$treeWrp.treeview(extendTreeSetting);
		
		pageWrp.appendTo($.mobile.pageContainer).page(); // 显示树形选择页
		
		// 取消
		pageWrp.on('pagehide', function(event, ui) {
			$(this).remove();
		});
		
		$.mobile.changePage('#select-user_dialog', {transition: 'fade'});
	});
	
});
$userCodesCon.parent().on('vclick', '#clear-user', function(event) { // 清除人员
	event.preventDefault();
	event.stopImmediatePropagation();
	
	_viewer.form.getItem('USER_CODES').setValue(''); // 先清空
	_viewer.ids = [];
	_viewer.names = [];
});
$liWrapper.on('vclick', function(event) { // 保存并回执
	event.preventDefault();
	event.stopImmediatePropagation();
	
	var params = {};
	params['MEETING_ID'] = meetingId;
	params['MEMO'] = $memoCon.find('span').html();
	if ($statusCon.find('input:checked').val() == 1) { // 本人参加
		params['STATUS'] = 1;
	} else {
		params['STATUS'] = 2;
		params['USER_CODES'] = _viewer.ids.join(',');
	}
	FireFly.doAct('OA_MT_RETURN_NOTICE', 'save', params).then(function(result) {
		console.debug(result);
		if (result['_MSG_'].indexOf('OK,') < 0) {
			alert('回执保存失败，请联系系统管理员！');
		} else {
			alert('回执保存成功，点击确定键返回！');
//			$.mobile.pageContainer.find('#' + meetingId).remove(); TODO 删除过期数据，这句应该找不到，先测试
			$.mobile.back();
		}
	});
});
// 单选框，控制与会人员框是否显示
$statusCon.on('change', 'input', function(event) {
	event.preventDefault();
	event.stopImmediatePropagation();
	
	if ($statusCon.find('input:checked').val() == 1) { // 是本人参加
		$userCodesCon.parent().hide();
	} else { // 不是本人参加
		$userCodesCon.parent().show();
	}
});

// 刷新组件放最后面，否则有影响
$statusCon.find('input').checkboxradio("refresh");
