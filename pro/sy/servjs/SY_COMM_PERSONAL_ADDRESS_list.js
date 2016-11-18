var _viewer = this;
var addGroup = _viewer.getBtn("addGroup");
addGroup.unbind("click").bind("click",function(){
	var options = {"url":"SY_COMM_ADDRESS_GROUP.card.do","tTitle":"添加组","menuFlag":"4","params":
	{
		"callBackHandler" : _viewer,
		"closeCallBackFunc" : function() {
			_viewer.refreshTreeAndGrid();
		}
	}
	
	};
	Tab.open(options);
});

