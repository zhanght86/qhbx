<link rel="stylesheet" type="text/css" href="/sc/comm/gallery/css/gallery-min.css"/>
<div class='portal-box-title' style="height:${height}">
		
	<div class="sc-slider-wrapper">
		<div id="wrapper">
			<div id="thumbnails">
			<#if _DATA_?size !=0 >
				<img src="/file/${_DATA_.FILE_ID}?size=160x115" data-id="${_DATA_.NEWS_ID}" chnl-id="${_DATA_.CHNL_ID}" title="${_DATA_.NEWS_SUBJECT}" class="js-sc-gallery" alt="${_DATA_.NEWS_SUBJECT}" width="160" height="115" />
		   <#else>
				<img src="/sc/comm/gallery/img/blank.gif" alt="blank" width="160" height="115" />
		   </#if>    	
			</div>
		</div>
	</div>
</div> 
<script>
$(document).ready(function(){
 	$('#thumbnails img').on({
 		load:function(){
 		 	
 		},
 		error: function(){
	 		$(this).attr('src',"/sc/comm/gallery/img/blank.gif");
	 	}
 	}); 
	$('#thumbnails').on("click","img",function(){
	var chnlId = $(this).attr("chnl-id");
			var params = {};
		params["flag"] = true;
		params["CHNLID"] = chnlId;
		params["ZT"]="1";
		var options = {"url":"SY_COMM_INFOS_VIEW.list.do","tTitle":"信息浏览","params":params,"menuFlag":3};
		Tab.open(options);
		});
 	
});



</script>