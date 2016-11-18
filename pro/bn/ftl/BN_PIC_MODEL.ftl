<div id="tinyPanel-wrapper" class="tiny-vertical" style="height:${hei};width:auto;margin:4px auto;position:relative;">
	
    <a title="${CHNL_NAME}" id="webPre" style="filter:alpha(opacity=0);color:#1b1b1b;background-color:black;position:absolute;top:0px;left:0px;width:100%;height:${hei}; display: none;" href="javascript:openContacturl('W');">${CHNL_NAME}</a>
	<div style="width:auto;">
		<a href="javascript:openContacturl('W');">
			 <img style='border: 0;width:${width}; height:${hei};' src="${src}" alt="" />
		</a>
	</div>
</div>

<script>
jQuery(document).ready(function(jQuery) {
		var boxHtml = jQuery("#${id}__temp #tinyPanel-wrapper").html();
		jQuery("#${id}__temp").html("<div id='tinyPanel-wrapper' class='tiny-vertical' style='height:${hei};width:auto;margin:4px auto;position:relative;'>" + boxHtml + "</div>");
	});
function openContacturl(href) {
				var url = "SY_COMM_TEMPL.show.do?pkCode=OA_LDR_ACTION";
				var tTitle = "";
				url = "${url}";
				tTitle = "${CHNL_NAME}";
				var options = {"url":url,"tTitle":tTitle,"menuFlag":3};
				var tabP = jQuery.toJSON(options);
				tabP = tabP.replace(/\"/g,"'");
				window.open("/sy/comm/page/page.jsp?openTab="+(tabP));
				
}
function openLocation() {
	var opts = {"url":"SY_COMM_WORKLOC.show.do?func=view","tTitle":"¹¤Î»Í¼","params":'',"menuFlag":3};
    parent.Tab.open(opts); 
}
</script>