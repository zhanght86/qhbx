var boxObj = this;

boxObj.find(".body_td_GW_TITLE").unbind("click").bind("click",function(){
	var trackObj = {};
	trackObj.SERV_ID = "OA_GW_TMPL_FW_GS";
	trackObj._PK_ = jQuery(this).parent().attr("dataid");
	var opts = {
		"url" : "OA_GW_TMPL_VIEW.doc.do?data=" + jQuery.toJSON(trackObj),
		"tTitle" : "公文发布",
		"params" : trackObj,
		"menuFlag" : 4
	};
	Tab.open(opts);
});
