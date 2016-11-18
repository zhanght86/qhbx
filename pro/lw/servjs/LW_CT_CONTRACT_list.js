var _viewer = this;
//合同类别和金额在列表里显示为0时显示为空
jQuery(_viewer.grid.getTable()).find("td[icode='CT_TYPE__NAME']").each(
		function() {
			if ($(this).html() == '0') {
				$(this).html('');
			}
		});
jQuery(_viewer.grid.getTable()).find("td[icode='CT_EMONEY']").each(
		function() {
			if ($(this).html() == '0') {
				$(this).html('');
			}
		});