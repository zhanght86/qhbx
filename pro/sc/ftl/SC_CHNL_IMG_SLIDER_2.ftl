<style>
.sc-chnl-wrapper{position:relative;width:100%;height:371px}
#carousel-wrapper{position:absolute;left:50%;top:50%;width:515px;height:290px;margin:-155px 0 0 -262.5px}
#chnlCarousel{position:relative;width:100%;height:100%}
#chnlCarousel ul{list-style:none;display:block;margin:0;padding:0}
#chnlCarousel li{display:block;position:relative;float:left;width:515px;height:290px;margin:0;padding:0;text-align:center}
#chnlCarousel li img{width:100%;height:100%;cursor:pointer}
#chnlCarousel .pager{position:absolute;bottom:0;z-index:102;width:100%;height:25px;background:url("/sc/img/mask.png") repeat;text-align:right;}
#chnlCarousel .pager a{display:inline-block;width:29px;height:9px;margin:6px 5px 0 0;overflow:hidden;background:transparent url("/sc/img/li-bg.gif") no-repeat 0 -9px;text-decoration:none;text-indent:-999px}
#chnlCarousel .pager a.selected{background-position:0 0;text-decoration:underline}
#chnlCarousel .pager a span{display:none;}
.chnlImgInfo,.blankContent{position:absolute;bottom:0px;left:26px;height:18px;color:#000;font-size:14px;font-weight:bold;z-index:101;cursor:pointer}
</style> 
<div class="portal-box">
    <#-- <div class="sc-chnl-wrapper" style="height:${height}"> -->
     <div class="sc-chnl-wrapper" style="height:313px">
     <#if _DATA_?size !=0 >
        <div id="carousel-wrapper">
            <div id="chnlCarousel">
           
            	<#assign 
            		firstImgInfo = _DATA_[0].NEWS_SUBJECT
            		newsId = _DATA_[0].NEWS_ID
            		>
            	<ul>
            		<#list _DATA_ as obj>
						<li>
							<img src="/file/${obj.FILE_ID}?size=540x304" data-id="${obj.NEWS_ID}" title="${obj.NEWS_SUBJECT}" alt="${obj.NEWS_SUBJECT}"/>
						</li>
					</#list>	
				</ul>
				
			
				<div class="clearfix"></div>
				<div id="js-slider-pager" class="pager"></div>
            </div>
        </div>
        <div class="chnlImgInfo" data-id="${newsId}">${firstImgInfo!""}</div>
        <#else>
        	<div class="blankContent">暂无内容!</div>
    	</#if>
	</div> 
</div>
<script>
jQuery(document).ready(function(){
	
	if($('#chnlCarousel ul').length){
	
		$('#chnlCarousel ul').on("click","img",function(){
			var newsId = $(this).attr("data-id");
			window.open("/cms/SY_COMM_INFOS/" + newsId + ".html");
		});
		$('.chnlImgInfo').on("click",function(){
			var newsId = $(this).attr("data-id");
			window.open("/cms/SY_COMM_INFOS/" + newsId + ".html");
		});
		
		$('#chnlCarousel ul').carouFredSel({
	   		items: {
	   			visible: 1,
				width: 515,
				height:290 
			},
	  		scroll:{
	  			fx: 'directscroll',
				duration:1500,
				easing: 'quadratic',
				onBefore: function( data ) { 
					 var $imgItem = data.items.visible.first().find("img"),
						 dataId = $imgItem.attr("data-id"),
					 	 title = $imgItem.attr("title");
					 $(".chnlImgInfo").attr("data-id",dataId).html(title);
				}
			},
			pagination  : "#js-slider-pager"
		});
	}
 
 
});
</script> 
