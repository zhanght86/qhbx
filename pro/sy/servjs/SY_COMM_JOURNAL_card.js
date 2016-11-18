/**
 * 期刊管理
 */
var _viewer = this;
jQuery(document).ready(function(){
	var servId = _viewer.servId;
	_viewer.afterSave = function() {
		// 保存关联信息
		var data = {};
		data["JU_ID"] = _viewer.getItem("JU_ID").getValue();
		data["NEWS_ID"] = listView.grid.getSelectPKCodes().join(",");
		FireFly.doAct(servId, "saveJournalNews", data, false);
	};
	
	var previewBtn = _viewer.getBtn('preview');
	previewBtn.click(function(){
		var journalContentItem = _viewer.getItem("JU_CONTENT");
		var content = journalContentItem.getValue();
		if(content.length == 0) {// 校验通过才可预览
			alert("期刊内容为空！");
		} else {
			var OpenWindow = window.open("about:blank");
			OpenWindow.document.write("<div style='width:794px;margin:0 auto;padding:10px 10px;border:1px solid #ddd;'>" + content + "</div>");
		}
	});
	
	
	var newsCheck = _viewer.getItem("JU_CHECK");
	var newsChecker = _viewer.getItem("JU_CHECKER");

		newsCheck.obj.change(function(){
			// 简单审核时审核人必须输入
			newsCheck.setOtherItemNotNull([1], newsChecker, true);
			// 并且可填
			newsCheck.setOtherItemEnabled([1], newsChecker);
		});
	
	
});
