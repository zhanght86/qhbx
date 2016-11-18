/**
 * 期刊采编
 */
var _viewer = this;
jQuery(document).ready(function(){
	var servId = _viewer.servId;
	insertInfos();// 插入指定的信息到期刊里

	/**
	 * 设置编辑器内容
	 */
	function insertInfos() {
		var insertBtn = _viewer.getBtn("insert");
		insertBtn.click(function(){
			var selectPks = _viewer.grid.getSelectPKCodes();
			var len = selectPks.length;
			if (len == 0) {
				alert("请选择要插入的消息！");
				return;
			} else {
				var chnlId = cardView.getItem("CHNL_ID").getValue();
				if (chnlId.length == 0) {
					alert("请选择要发布的栏目！");
					return;
				}
				var data = {};
				data[UIConst.PK_KEY] = selectPks.join(",");
				data["CHNL_ID"] = chnlId;
				FireFly.doAct(servId, "collect", data, true, true, function(data){
					var editor = cardView.getItem("JU_CONTENT").getEditor();
					editor.setContent(data["_DATA_"]);
				});
			}
		});
	}
});