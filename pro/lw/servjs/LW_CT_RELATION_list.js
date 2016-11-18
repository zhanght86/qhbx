(function(_viewer){
	var viewContractBtn = _viewer.grid.getBtn("viewContract");
	if (viewContractBtn) {
		viewContractBtn.unbind("click").bind("click", function(event) {
			var pk = jQuery(this).attr("rowpk");
			var rtCtId = _viewer.grid.getRowItemValue(pk, 'RT_CT_ID');
			Tab.open({'url': 'LW_CT_CONTRACT_BOOK.card.do?pkCode=' + rtCtId + '&readOnly=true','tTitle':'关联合同详细信息','menuFlag':3,'icon':'fash'});
			event.stopPropagation();
			return false;
		});
	}
})(this);