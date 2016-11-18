<link rel="stylesheet" type="text/css" href="/sc/css/system_reset.css"/>
<link rel="stylesheet" type="text/css" href="/sc/comm/gallery/css/gallery-min.css"/>
<script type="text/javascript" src="/sc/js/jquery.carouFredSel-6.2.1-packed.js"></script>
<div class="portal-box" style="width:99.3%">
	<div class="sc-art-gallery-wrapper" style="height:${height}">
		<div class="sc-gallery-error">尚未上传图片集</div>
	    <div class="portal-box-title sc-header">
	        <span class="sc-header-title">专题频道</span>
	        <span class="sc-header-arrow"></span>
	    </div> 
	    <div class="sc-slider-wrapper">
	        <div id="wrapper">
	            <div id="thumbnails">
	            <#if _DATA_?size !=0 >
	            	<#if _DATA_?size < 5 >
	            		<#assign len = _DATA_?size>
	            		<#list _DATA_ as obj>
		               	  <img src="/file/${obj.FILE_ID}?size=160x115" chnl-id="${obj.CHNL_ID}" data-id="${obj.NEWS_ID}" title="${obj.NEWS_SUBJECT}" class="js-sc-gallery" alt="${obj.NEWS_SUBJECT}" width="160" height="115" />
		               	</#list>
		               	<#list 1..(5-len) as obj>
		               	  <img src="/sc/comm/gallery/img/blank.gif" alt="blank" width="160" height="115" />
		               	</#list>
	            	<#else>
		            	<#list _DATA_ as obj>
		               	  <img src="file/${obj.FILE_ID}?size=160x115" data-id="${obj.NEWS_ID}" title="${obj.NEWS_SUBJECT}" class="js-sc-gallery" alt="${obj.NEWS_SUBJECT}" width="160" height="115" />
		               	</#list>
	               	</#if>
	           <#else>
	           	  		<#list 1..5 as i>
		               	  <img src="/sc/comm/gallery/img/blank.gif" alt="blank" width="160" height="115" />
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
			var chnlId = $(this).attr("chnl-id");
			var params = {};
			params["flag"] = true;
			params["ZT"]="1";
			var where = " and CHNL_ID = '" +chnlId+"'";
			<#--var options = {"url":"SY_COMM_INFOS_VIEW.list.do","tTitle":"信息浏览","params":params,"menuId":"3w5vjk5vdanbWQFrVTEsrR","menuFlag":3};window.open("/cms/SY_COMM_INFOS/" + newsId + ".html");-->
			var options = {"url":"SY_COMM_INFOS_VIEW.list.do","tTitle":"信息浏览","params":params,"menuFlag":3};
			var options = jQuery.toJSON(options);
			alert(jQuery.toJSON(params));
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
				width: 160,
				height: 115,
				visible: 5
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