<link rel="stylesheet" type="text/css" href="/sc/comm/gallery/css/gallery-min.css"/>
<link rel="stylesheet" type="text/css" href="/sc/css/system_reset.css"/>
<script type="text/javascript" src="/sc/js/jquery.carouFredSel-6.2.1-packed.js"></script>
<div class="portal-box" style="width:100%;border-bottom-color:#d1d6d2;">
<div style="float:left; width:10%; height:96px; padding-left:10px; padding-top:26px; overflow:hidden; background:url(/sy/comm/page/images/cl.png) no-repeat center;"><h5 style="float:left;width:96px; height:54px; line-height:54px; color:#4a0000; font-size:16px; font-weight:bold; font-family:Arial,Microsoft YaHei;">图片新闻</h5></div>
	<div class="sc-art-gallery-wrapper" style="height:${height}">
	
		<div class="sc-gallery-error">尚未上传图片集</div>
	     
	    <div class="sc-slider-wrapper" >
	        
			<div id="wrapper" style="top:0px;">
			
	            <div id="thumbnails1" style="width:120%">
	            <#if _DATA_?size !=0 >
	            	<#if _DATA_?size < 6 >
	            		<#assign len = _DATA_?size>
	            		<#list _DATA_ as obj>
		               	  <img src="/file/${obj.FILE_ID}?size=131x120" data-id="${obj.NEWS_ID}" title="${obj.NEWS_SUBJECT}" class="js-sc-gallery" alt="${obj.NEWS_SUBJECT}" width="131" height="120" />
		               	</#list>
		               	<#list 1..(6-len) as obj>
		               	  <img src="/sc/comm/gallery/img/blank.gif" alt="blank" width="131" height="120" />
		               	</#list>
	            	<#else>
		            	<#list _DATA_ as obj>
		               	  <img src="/file/${obj.FILE_ID}?size=131x120" data-id="${obj.NEWS_ID}" title="${obj.NEWS_SUBJECT}" class="js-sc-gallery" alt="${obj.NEWS_SUBJECT}" width="131" height="120" />
		               	</#list>
	               	</#if>
	           <#else>
	           	  		<#list 1..3 as i>
		               	  <img src="/sc/comm/gallery/img/blank.gif" alt="blank" width="131" height="120" />
		               	</#list>
	           </#if>    	
	            </div>
	        </div>
	        <a id="prev1" href="javascript:void(0);"></a>
	        <a id="next1" href="javascript:void(0);"></a>
	    </div>
	</div> 
</div>
<script>
$(document).ready(function(){
 	$('#thumbnails1 img').on({
 		load:function(){
 		 	 
 		},
 		error: function(){
	 		$(this).attr('src',"/sc/comm/gallery/img/blank.gif");
	 	}
 	}); 
	$('#thumbnails1').on("click","img",function(){
			var newsId = $(this).attr("data-id");
			var chnlId = $(this).attr("chnlId");
			var params = {};
			params["flag"] = true;
			params["ZT"]="1";
			var where = " and CHNL_ID = '" +chnlId+"'";
			window.open("/cms/SY_COMM_INFOS/" + newsId + ".html");
			<#--var options = {"url":"SY_COMM_INFOS_VIEW.list.do","tTitle":"信息浏览","params":params,"menuId":"3w5vjk5vdanbWQFrVTEsrR","menuFlag":3};
			
			var options = {"url":"BN_GALLERY.list.do","tTitle":"专题频道","params":params,"menuFlag":3};-->
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
	 	$('#thumbnails1').carouFredSel({
	 		responsive: true,
			items: {
				width: 131,
				height: 120,
				visible: 6
			},
			auto: {
				items: 1 
			},
			scroll:{
				duration:1000
			},
			prev: '#prev1',
			next: '#next1'
		});
	});
});



</script>