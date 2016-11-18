var _viewer = this;

//备注显示样式
_viewer.form.getItem("MEMO_TITLE").obj.parent().removeClass("disabled").css({"background":"url('')"});
_viewer.form.getItem("MEMO_TITLE").obj.css({"color":"black","font-weight":"bolder","background-color":"white","font-size":"14px"});

/*var timer = setInterval(function(){
		var tabObjHeight = jQuery("#OA_OFF_APPLY-winTabs").height();
		var parHandlerPkCode = _viewer.getItem("APPLY_ID").getValue();
		var thisIframObj = parent.jQuery("#" + _viewer.servId + "-card-dopkCode" +  parHandlerPkCode + "-" + parHandlerPkCode + "-tabFrame");
		thisIframObj.css({"height": tabObjHeight + 50});
	},10);*/