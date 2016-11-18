<div id="tinyPanel-wrapper" class="tiny-vertical" style="height:${hei};width:auto;margin:4px auto;position:relative;">
	<div style="width:auto;">
		<a href="javascript:openLinkurl('${url}');">
			 <img style='border: 0;width:${width}; height:${hei};' src="${src}" alt="" />
		</a>
	</div>
</div>

<script>
jQuery(document).ready(function(jQuery) {
		var boxHtml = jQuery("#${id}__temp #tinyPanel-wrapper").html();
		jQuery("#${id}__temp").html("<div id='tinyPanel-wrapper' class='tiny-vertical' style='height:${hei};width:auto;margin:4px auto;position:relative;'>" + boxHtml + "</div>");
	});
function openLinkurl(href) {
	if(href=="empty"){
		
	}else{
		window.open(href);
	}
			
}

</script>