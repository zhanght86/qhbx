var _viewer = this;
(function(_viewer){	
	//备注显示样式
	_viewer.form.getItem("MEMO_TITLE").obj.parent().removeClass("disabled");
	_viewer.form.getItem("MEMO_TITLE").obj.css({"color":"black","font-weight":"bolder","background-color":"white","font-size":"14px"});
})(this);

