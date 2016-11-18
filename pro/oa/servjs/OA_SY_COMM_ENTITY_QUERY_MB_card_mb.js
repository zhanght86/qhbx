var _viewer = this;

//查询按钮
_viewer.getBtn('entityQuery').bind("click",function() {
   queryEntity(_viewer.form.getAllItemsValue());
});

//查询按钮
_viewer.getBtn('cancelQuery').bind("click",function() {
   jQuery.each(_viewer.form.getAllItems(),function(){
   		var _this = this;
		_this.setValue("","");
   });
});	

/**
 * 查询公文
 */
function queryEntity(itemsValues){
	if (_viewer.getItem("ENTITY_CODE").getValue() != undefined) {
		itemsValues.ENTITY_CODE = _viewer.getItem("ENTITY_CODE").getValue();
	}
	if (_viewer.getItem("TITLE").getValue() != undefined) {
	    itemsValues.TITLE = _viewer.getItem("TITLE").getValue();
	}
	if (_viewer.getItem("SERV_NAME").getValue() != undefined) {
	    itemsValues.SERV_NAME = _viewer.getItem("SERV_NAME").getValue();
	}	
	if (_viewer.getItem("S_ODEPT").getValue() != undefined) {
	    itemsValues.S_ODEPT = _viewer.getItem("S_ODEPT").getValue();
	}
	if (_viewer.getItem("S_WF_STATE").getValue() != undefined) {
	    itemsValues.S_WF_STATE = _viewer.getItem("S_WF_STATE").getValue();
	}
	if (_viewer.getItem("S_EMERGENCY").getValue() != undefined) {
	    itemsValues.S_EMERGENCY = _viewer.getItem("S_EMERGENCY").getValue();
	}
	if (_viewer.getItem("S_ATIME_1").getValue() != undefined) {
	    itemsValues.S_ATIME_1 = _viewer.getItem("S_ATIME_1").getValue();
	}
	if (_viewer.getItem("S_ATIME_2").getValue() != undefined) {
	    itemsValues.S_ATIME_2 = _viewer.getItem("S_ATIME_2").getValue();
	}
	itemsValues.IS_MOBILE = true;
	var opts = {"url":"OA_SY_COMM_ENTITY_QUERY_MB.list.do","tTitle":"查询结果","params":itemsValues};
	Tab.open(opts);
}