var _viewer = this;

/**
 * 将数据变为只读
 */
_viewer.grid.getCheckBox().each(function(){
	var rowItem = jQuery(this);
	var pkCode = _viewer.grid.getRowPkByElement(rowItem);
	
	//将已删除的数据变为只读	
	var sFlag = _viewer.grid.getRowItemValue(pkCode,"S_FLAG");
	if(sFlag == 2){
		jQuery(this).hide();
		return;
	}
});