//@ sourceURL=OA_GW_TYPE_FW_SEND_card_mb.js

// 系统来文
var _viewer = this;

// 屏蔽掉红头文件和文稿
$("li[data-filename='红头文件']").hide();
$("li[data-filename='文稿']").hide();

// 将整个意见列表都去掉,意见不可见
$('#' + _viewer.getPKCode() + '_MIND').hide();

