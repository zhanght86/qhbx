//@ sourceURL=DA_BORROW_card_mb.js

// 档案借阅
var _viewer = this;

//追加详细列表
var cardItemCon = $("<div id='" + _viewer.getPKCode() + "_ITEM' class='mb-card-group'></div>");
// listview 列表
var listGroupCon = $("<ul data-role='listview' data-inset='false'>");
cardItemCon.append("<h4>档案借阅信息</h4>");
var param = {};
param['_NOPAGE_'] = true;
param['_WHERE_'] = " and BO_ID = '" + _viewer.getPKCode() + "'";
FireFly.doAct('DA_BORROW_ITEM', 'query', param).then(function(result) {
	console.debug(result);
	if (result['_DATA_'].length > 0) {
		$.each(result['_DATA_'], function(index, item) {
			listGroupCon.append("<li style='white-space: normal;'>" + item['DA_TITLE'] + "<span style='font-size:.75em;'>(" + item['BO_FLAG__NAME'] + ")</span></li>");
			listGroupCon.listview().listview('refresh');
		});
	} else {
		cardItemCon.remove();
	}
});

cardItemCon.append(listGroupCon);
_viewer.form.mainContainer.append(cardItemCon);
