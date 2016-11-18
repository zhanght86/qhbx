
<style>
.sc-chnl-wrapper{position:relative;width:100%;height:371px}
#carousel-wrapper{width:360px;height:240px;}
#chnlCarousel{position:relative;width:100%;height:100%}
#chnlCarousel ul{list-style:none;display:block;margin:0;padding:0;margin-left:1px;}
#chnlCarousel li{display:block;position:relative;float:left;width:360px;margin:0;padding:0;text-align:center}
#chnlCarousel li img{width:100%;height:100%;cursor:pointer}
#chnlCarousel .pager{position:absolute;bottom:0;z-index:1002;width:98%;height:25px;background:url("/sc/img/mask.png") repeat;text-align:right;}
#chnlCarousel .pager a{display:inline-block;width:29px;height:9px;margin:6px 5px 0 0;overflow:hidden;background:transparent url("/sc/img/li-bg.gif") no-repeat 0 -9px;text-decoration:none;text-indent:-999px}
#chnlCarousel .pager a.selected{background-position:0 0;text-decoration:underline}
#chnlCarousel .pager a span{display:none;}
.chnlImgInfo,.blankContent{position:absolute;bottom:15px;left:14px;height:8px;color:#fff;font-size:12px;font-weight:bold;z-index:1001;cursor:pointer}
</style> 
<script type="text/javascript" src="/sc/js/jquery.carouFredSel-6.2.1-packed.js"></script>

<div class="portal-box" style="width:361px;margin-left:0px;">
<div class='portal-box-title ' style='position:relative;background:url("/sc/img/gz_bg_1.png") no-repeat; background-size:472px 32px;background-size-color:#F5F5F5;'>
	
	<span class="portal-box-title-label" style="FONT-SIZE: 16px; FONT-FAMILY: Microsoft Yahei; COLOR: black; MARGIN-LEFT: 30px; LINE-HEIGHT: 2em">特别关注</span>
</div>
    <div class="sc-chnl-wrapper" style="height:${height}">
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
							<img src="/file/${obj.FILE_ID}?size=360x240" data-id="${obj.NEWS_ID}" title="${obj.NEWS_SUBJECT}" alt="${obj.NEWS_SUBJECT}"/>
						</li>
					</#list>	
				</ul>
				
			
				<div class="clearfix"></div>
				<div id="js-slider-pager" class="pager">
				
				</div>
            </div>
        </div>
        <div class="chnlImgInfo" style="float:left;z-index:1003;" data-id="${newsId}">${firstImgInfo!""}</div>
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
				width: 360,
				height: 304
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
