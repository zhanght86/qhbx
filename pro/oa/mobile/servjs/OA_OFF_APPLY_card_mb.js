//@ sourceURL=OA_OFF_APPLY_card_mb.js

// 办公用品申请
var _viewer = this;

// 追加详细列表
var cardItemCon = $("<div id='" + _viewer.getPKCode() + "_ITEM' class='mb-card-group'></div>");
// listview 列表
var listGroupCon = $("<ul data-role='listview' data-inset='false'>");
cardItemCon.append("<h4>申请用品明细</h4>");
var param = {};
param['_NOPAGE_'] = true;
param['_WHERE_'] = " and APPLY_ID = '" + _viewer.getPKCode() + "'";
param['_ORDER_'] = " OFFICE_ID desc";
FireFly.doAct('OA_OFF_APPLY_TRAITS', 'query', param).then(function(result) {
	console.debug(result);
	if (result['_DATA_'].length > 0) {
		$.each(result['_DATA_'], function(index, item) {
			listGroupCon.append("<li>" + item['OFFICE_NAME'] + "  (" + parseFloat(item['OFFICE_PRICE']) + "元/" + item['OFFICE_UNITE__NAME'] + ")<span class='ui-li-count'>" + item['OFFICE_AMOUNT'] + "元</span></li>");
			listGroupCon.listview().listview('refresh');
		});
	} else {
		cardItemCon.remove();
	}
});

cardItemCon.append(listGroupCon);
_viewer.form.mainContainer.append(cardItemCon);

