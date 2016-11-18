<style type="text/css">
.pictureScroll #bannerB {}
#bannerB {position:relative;border:0px solid #666; overflow:hidden; font-size:12px} 
#bannerB_list img {border:0px;} 
#bannerB_bg {position:absolute; bottom:0;background-color:#000;height:30px;filter: Alpha(Opacity=30);opacity:0.3;z-index:1000;cursor:pointer; } 
	<!--
	#bannerB_info{position:absolute; bottom:4px; left:5px;height:22px;color:#fff;z-index:1001;cursor:pointer} 
	-->
#bannerB_text {position:absolute;width:120px;z-index:1002; right:3px; bottom:3px;} 
#bannerB ul {position:absolute;list-style-type:none;filter: Alpha(Opacity=80);opacity:0.8; z-index:1002; 
margin:0; padding:0; bottom:3px; right:5px; height:20px} 
#bannerB ul li { padding:0 8px; line-height:18px;float:left;display:block;color:#FFF;border:#e5eaff 1px solid;background-color:#6f4f67;cursor:pointer; margin:0; font-size:16px;} 
#bannerB_list a{position:absolute;} 
#bannerB .linum {background-color:#be2424;color:#000}
.elipd{
overflow:hidden;
white-space:nowrap;
text-overflow:ellipsis;
-o-text-overflow:ellipsis;
-moz-text-overflow:ellipsis;
-webkit-text-overflow:ellipsis;
-icab-text-overflow: ellipsis;
-khtml-text-overflow: ellipsis;
}

#PICTURE_SCROLL {
    margin-left: 0px;
    width: 996px;
    margin-right: 0px;
}
</style> 
<div id='PICTURE_SCROLL' class='portal-box pictureScroll'>
<div class='portal-box-con'>
<div id="bannerB" style="height:170px;"> 
<div id="bannerB_bg" ></div> 
	<!--
	<div id="bannerB_info" class="elipd" style="width:65%"></div> 
	-->
<ul> 
<#list _DATA_ as news>
<#if (news.picture?? && news.NEWS_ID?length >0)>
	<#if (news_index = 0)>
		<li class="linum">${news_index+1}</li> 
	<#else>
	<li>${news_index+1}</li>
	</#if>
</#if>
</#list>
</ul> 
<div id="bannerB_list"> 
<#list _DATA_ as news>
<a href="javascript:void(0);">
<#if (news.picture?? && news.picture.FILE_ID?length >0)>
	<img   style='border: 0;height:${hei!186}; width:${width}' src="/file/${news.picture.FILE_ID}" title="${news.NEWS_SUBJECT}" alt="${news.NEWS_SUBJECT}"/>
</#if>
</a>
</#list>
</div> 
</div> 
</div>
</div>
<script type="text/javascript">
   var ka = ma = 0, countBa=0;
    function showAutoBa() { 
		ma = (ma >=(countBa-1) ? 0 : ++ma);
		jQuery("#bannerB li").eq(ma).click(); 
		ka = setTimeout("showAutoBa()", 4000);
    } 
  jQuery(document).ready(function(){ 
	countBa=jQuery("#bannerB_list a").length;
	jQuery("#bannerB_list a:not(:first-child)").hide();
	/**
	jQuery("#bannerB_info").html(jQuery("#bannerB_list a:first-child").find("img").attr("alt")); 
	jQuery("#bannerB_info").unbind("click").bind("click",
	function(){eval(jQuery("#bannerB_list a:first-child").attr("href"))});
	*/
	
	jQuery("#bannerB li").unbind("click").bind("click",function() {
		var i = jQuery(this).text() - 1;
		ma = i; 
		if (i >= countBa) return; 
		/**
		jQuery("#bannerB_info").html(jQuery("#bannerB_list a").eq(i).find("img").attr("alt")); 
		jQuery("#bannerB_info").unbind("click").bind("click", function(){eval(jQuery("#bannerB_list a").eq(i).attr("href"))});
		*/
		
		jQuery(this).parent().find("li").removeClass("linum");
		<#-- 解决ie8下局部页面css不渲染问题-->
		jQuery(this).parent().hide().show();
		jQuery(this).addClass("linum");
		jQuery("#bannerB_list a").filter(":visible").fadeOut(500).parent().children().eq(i).fadeIn(1000);
	}); 
	<#--使用setTimeout递归,实现定时调用问题 -->
	ka= setTimeout("showAutoBa()", 4000);
	
	jQuery("#bannerB").hover(function(){clearTimeout(ka)}, function(){ka= setTimeout("showAutoBa()", 4000);});
	jQuery("#bannerB_list img").width(jQuery("#bannerB").parent().width()-1);
	});
	function newsViewPicTop(href,id){
	if(href==""){
		var url = "/cms/BN_BANNER_PIC/" + id + ".html";	
		window.open(url);		
	}else{
		window.open(href);
	}
	
		
	}
</script> 
