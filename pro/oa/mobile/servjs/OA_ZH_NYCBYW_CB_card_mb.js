//@ sourceURL=OA_ZH_NYCBYW_CB_card_mb.js

var _viewer = this;

// 获取两字段对象
var cbYwTypeItem = _viewer.getItem('CB_YW_TYPE')
,	cbBxxsItem = _viewer.getItem('CB_BXXS');
// 隐藏两对象容器
cbYwTypeItem.getContainer().hide();
cbBxxsItem.getContainer().hide();
// 获取两字段的值
var cbYwTypeValues = cbYwTypeItem.getValue().split(',')
,	cbBxxsValues = cbBxxsItem.getValue().split(',');

// 构造新容器
//var con = $("<div data-role='fieldcontain' data-theme='b' class='ui-field-contain'><label for='CB_YW_TYPE-CB_BXXS'>业务类型及表现形式</label></div>");
var con = $("<div data-role='fieldcontain' data-theme='b' class='ui-field-contain'><label for='CB_YW_TYPE-CB_BXXS'>业务类型及表现形式</label><div data-role='fieldcontain' class='box'></div></div>");

// 向新容器中添加复选框
$.each(_viewer._servData.DICTS.CB_YW_TYPE, function(index, item) {
	con.find('.box').append("<div data-role='fieldcontain'><label id='" + item['ID'] + "'><input disabled='' type='checkbox' />" + item['NAME'] + "</label><fieldset data-role='controlgroup'></fieldset></div>");
});
$.each(_viewer._servData.DICTS.CB_BXXS, function(index, item) {
	var id = item['ID'];
	var parId = id.substring(0, id.indexOf('-'));
	con.find('#' + parId).siblings('fieldset').append("<label id='" + item['ID'] + "'><input disabled='' type='checkbox' />" + item['NAME'] + "</label>");
});

// 将两字段值回写到复选框中
$.each(cbYwTypeValues, function(index, item) {
	con.find('#' + item).find('input').attr('checked', 'checked');
});
$.each(cbBxxsValues, function(index, item) {
	con.find('#' + item).find('input').attr('checked', 'checked');
});

// 将新容器加入到页面中
cbYwTypeItem.getContainer().after(con);

setTimeout(function() {
	var wid = con.width() > 700 ? con.width() * 0.78 : con.width();
	con.find('.box').css({'float': 'right', 'width': wid});
	con.find('.ui-controlgroup-controls').css('width', '100%');
}, 100);

