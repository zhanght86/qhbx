//@ sourceURL=OA_MT_MEETING_card_mb.js

// 会议通知
var _viewer = this;

var meetingId = _viewer.itemValue('MEETING_ID'); // 会议通知主键

// 不在已办里面，页面按钮数为零
// 控制是否添加填写回执单按钮
//console.log(_viewer.wfCard._parHandler.opts.secondStep);
//console.log(_viewer.footerNavWrp.find('li').length);
//if (_viewer.wfCard._parHandler.opts.secondStep != 'unfinish' && _viewer.footerNavWrp.find('li').length == 0) {
// 在待办打开的时候，wfCard是为空的
if (!_viewer.wfCard) { // wfCard为空时追加回执按钮
	var $liWrapper = $('<li></li>')
	,   $btn = $('<a href="#"></a>').appendTo($liWrapper);
	
	$btn.attr({'id': meetingId, 'data-icon': 'deliver'}).text('填写回执单');
	$liWrapper.appendTo(_viewer.footerNavWrp);
	
	$liWrapper.on('vclick', function(event) {
		event.preventDefault();
		event.stopImmediatePropagation();
		
		var data = {};
		
		data['sId'] = 'OA_MT_RETURN_NOTICE';
		data['pkCode'] = '';
		data['pId'] = _viewer.id;
		data['jsFileUrl'] = 'oa/mobile/servjs/OA_MT_RETURN_NOTICE_mobile_mb.js';
		data['parHandler'] = _viewer;
		
		(function(params) {
			var cardView = new mb.vi.cardToCardView(params);
			cardView.show();
		}(data));
	});
}
