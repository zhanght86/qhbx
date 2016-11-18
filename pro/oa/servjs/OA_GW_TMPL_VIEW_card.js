var _viewer = this;
var iframeId = "#OA_GW_TMPL_VIEW-DOC_IFRAME";
jQuery(iframeId + "_div .left").hide();
jQuery(iframeId + "_div .right").css("width","100%");
jQuery(".rhCard-btnBar").hide();

jQuery(iframeId).ready(function(){
	var iframeObj = jQuery(iframeId);
	if(iframeObj.length > 0){
		var iframeHeight = iframeObj[0].contentWindow.outerHeight;
//		alert(iframeHeight);
		iframeObj.height(iframeHeight);
	}
});