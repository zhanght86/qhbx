<link rel="stylesheet" type="text/css" href="/sc/css/system_reset.css"/>
<link rel="stylesheet" type="text/css" href="/sc/comm/gallery/css/gallery-min.css"/>
<script type="text/javascript" src="/sc/js/jquery.carouFredSel-6.2.1-packed.js"></script>
<div class="portal-box" style="width:99%;border-bottom-color:#d1d6d2;margin-left:0px;margin-top:6px;">
	<div class="sc-art-gallery-wrapper" style="height:${height}">
	    <div class="sc-slider-wrapper">
	        <div id="wrapper" style="top:0px;">
			
	            <div id="thumbnails">
	            <#if _DATA_?size !=0 >
	            	<#if _DATA_?size < 4 >
	            		<#assign len = _DATA_?size>
	            		<#list _DATA_ as obj>
		               	  <img src="/file/${obj.FILE_ID}" chnlId="${obj.CHNL_ID}" data-id="${obj.NEWS_ID}" title="${obj.NEWS_SUBJECT}" class="js-sc-gallery" alt="${obj.NEWS_SUBJECT}" width="130" height="120" />
		               	</#list>
		               	<#list 1..(4-len) as obj>
		               	  <img src="/sc/comm/gallery/img/blank.gif" alt="blank" width="130" height="120" />
		               	</#list>
	            	<#else>
		            	<#list _DATA_ as obj>
		               	  <img src="file/${obj.FILE_ID}" chnlId="${obj.CHNL_ID}" data-id="${obj.NEWS_ID}" title="${obj.NEWS_SUBJECT}" class="js-sc-gallery" alt="${obj.NEWS_SUBJECT}" width="130" height="120" />
		               	</#list>
	               	</#if>
	           <#else>
	           	  		<#list 1..4 as i>
		               	  <img src="/sc/comm/gallery/img/blank.gif" alt="blank" width="130" height="120" />
		               	</#list>
	           </#if>    	
	            </div>
	        </div>
	        <a id="prev" href="javascript:void(0);"></a>
	        <a id="next" href="javascript:void(0);"></a>
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
			var newsId = $(this).attr("data-id");
			var chnlId = $(this).attr("chnlId");
			var params = {};
			params["flag"] = true;
			params["ZT"]="1";
			var where = " and CHNL_ID = '" +chnlId+"'";
			<#--var options = {"url":"SY_COMM_INFOS_VIEW.list.do","tTitle":"信息浏览","params":params,"menuId":"3w5vjk5vdanbWQFrVTEsrR","menuFlag":3};window.open("/cms/SY_COMM_INFOS/" + newsId + ".html");-->
			var options = {"url":"BN_GALLERY.list.do","tTitle":"专题频道","params":params,"menuFlag":3};
			var options = jQuery.toJSON(options);
			options = options.replace(/\"/g,"'");
			window.open("/sy/comm/page/page.jsp?openTab="+(options)+"&where="+encodeURIComponent(where));
			
		});
 	function getAdjustImgSize(imgUrl,maxSize){
			var oHeight,
				oWidth,
				ratio,
				newWidth,
				newHeight,
				maxWidth = maxSize.width,
				maxHeight= maxSize.height,
				maxRatio = maxWidth/maxHeight,
				img = new Image();	
		 
			img.src = imgUrl;
			
			oWidth  = img.width;
			oHeight = img.height;
			ratio = oWidth / oHeight;
			
			if(oWidth > maxWidth && oHeight < maxHeight){
				newWidth = maxWidth;
				newHeight = newWidth / ratio;
			}
			if(oWidth >= maxWidth && oHeight >= maxHeight){
				if(maxRatio > ratio){
					newHeight = maxHeight;
					newWidth = newHeight*ratio;
				}else{
					newWidth = maxWidth;
					newHeight = newWidth/ratio; 
				}
			}
			if(oWidth < maxWidth && oHeight > maxHeight){
					newHeight = maxHeight;
					newWidth = newHeight*ratio;
			}
			if(oWidth < maxWidth && oHeight < maxHeight){
					newHeight = maxHeight;
					newWidth = maxWidth;
			}
			newWidth = Math.round(newWidth);
			newHeight = Math.round(newHeight);
			return {
				width : newWidth,
				height: newHeight
			};

	}
	$(function() {
	 	$('#thumbnails').carouFredSel({
	 		responsive: true,
			items: {
				width: 130,
				height: 120,
				visible: 4
			},
			auto: {
				items: 1 
			},
			scroll:{
				duration:1000
			},
			prev: '#prev',
			next: '#next'
		});
	});
});



</script>