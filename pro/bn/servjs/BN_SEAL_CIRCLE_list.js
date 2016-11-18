var _viewer = this;
var check=_viewer.grid.getBtn("check");
check.unbind().click(function(event){
	var pk = jQuery(this).attr("rowpk");
	var servId = _viewer.grid.getRowItemValue(pk, 'SERV_ID');
	var dataId = _viewer.grid.getRowItemValue(pk, 'DATA_ID');
	var url = servId+".card.do?pkCode="+dataId;
	var opts = {
			"url" : url,
			"tTitle" : "印章生命周期", 
			"params" : {},
			"menuFlag" : 4
	};
	Tab.open(opts);
});