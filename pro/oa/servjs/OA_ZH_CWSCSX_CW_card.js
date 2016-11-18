var _viewer = this;
(function(_viewer){	
	// 人民币小写转大写
	var ctEmoney = _viewer.getItem("CW_SXJE");
	var ctCmoney = _viewer.getItem("CW_SXJE_DX");
	try {
		ctEmoney.getObj().blur(function(){
			var val = jQuery(this).val();
			if (val && val.length > 0) {
				try {
					ctCmoney.setValue(Format.RMBCapital(val));
				} catch (e) {
					alert(e);
				}
			}
		});
	} catch(e) {
		alert(e);
	}
	//备注显示样式
	_viewer.form.getItem("MEMO_TITLE").obj.parent().removeClass("disabled");
	_viewer.form.getItem("MEMO_TITLE").obj.css({"color":"black","font-weight":"bolder","background-color":"white","font-size":"14px"});
})(this);

