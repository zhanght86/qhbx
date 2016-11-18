<style type="text/css">
.pictureScroll #bannerC {}
#bannerC {position:relative;border:0px solid #666; overflow:hidden; font-size:12px} 
#bannerC_list img {border:0px;} 
#bannerC_bg {position:absolute; bottom:0;background-color:#000;height:30px;filter: Alpha(Opacity=30);opacity:0.3;z-index:1000;cursor:pointer; } 
#bannerC_info{position:absolute; bottom:4px; left:5px;height:22px;color:#fff;z-index:1001;cursor:pointer} 
#bannerC_text {position:absolute;width:120px;z-index:1002; right:3px; bottom:3px;} 
#bannerC ul {position:absolute;list-style-type:none;filter: Alpha(Opacity=80);opacity:0.8; z-index:-1; 
margin:0; padding:0; bottom:3px; right:5px; height:20px} 
#bannerC ul li { padding:0 8px; line-height:18px;float:left;display:block;color:#FFF;border:#e5eaff 1px solid;background-color:#6f4f67;cursor:pointer; margin:0; font-size:16px;} 
#bannerC_list a{position:absolute;} 
#bannerC .linum {background-color:#be2424;color:#000}
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
</style> 
<div id='PICTURE_SCROLL' class='portal-box pictureScroll'>
<div class='portal-box-con'>
<div id="bannerC" style="height:${hei}px;"> 
<div id="bannerC_bg" ></div> 
<div id="bannerC_info" class="elipd" style="width:65%;"></div> 
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
<div id="bannerC_list"> 
<#list _DATA_ as news>
<a href="javascript:newsViewPicBot('${news.OBJECT_2}','${news.NEWS_ID}');">
<#if (news.picture?? && news.picture.FILE_ID?length >0)>
	<img   style='border: 0;height:${hei!186}; width:${width}' src="/file/${news.picture.FILE_ID}?size=979x170" title="${news.NEWS_SUBJECT}" alt="${news.NEWS_SUBJECT}"/>
</#if>
</a>
</#list>
</div> 
</div> 
</div>
</div>
<script type="text/javascript">
   var j = l = 0, countB=0;
    function showAutoC() { 
		l = (l >=(countB-1) ? 0 : ++l);
		jQuery("#bannerC li").eq(l).click(); 
		j = setTimeout("showAutoC()", 4000);
    } 
  jQuery(document).ready(function(){ 
	countB=jQuery("#bannerC_list a").length;
	jQuery("#bannerC_list a:not(:first-child)").hide();
	jQuery("#bannerC_info").html(jQuery("#bannerC_list a:first-child").find("img").attr("alt")); 
	jQuery("#bannerC_info").unbind("click").bind("click",
	function(){eval(jQuery("#bannerC_list a:first-child").attr("href"))});
	jQuery("#bannerC li").unbind("click").bind("click",function() {
		var i = jQuery(this).text() - 1;
		n = i; 
		if (i >= countB) return; 
		jQuery("#bannerC_info").html(jQuery("#bannerC_list a").eq(i).find("img").attr("alt")); 
		jQuery("#bannerC_info").unbind("click").bind("click", function(){eval(jQuery("#bannerC_list a").eq(i).attr("href"))});
		jQuery(this).parent().find("li").removeClass("linum");
		<#-- 解决ie8下局部页面css不渲染问题-->
		jQuery(this).parent().hide().show();
		jQuery(this).addClass("linum");
		jQuery("#bannerC_list a").filter(":visible").fadeOut(500).parent().children().eq(i).fadeIn(1000);
	}); 
	<#--使用setTimeout递归,实现定时调用问题 -->
	k= setTimeout("showAutoC()", 4000);
	
	jQuery("#bannerC").hover(function(){clearTimeout(j)}, function(){j= setTimeout("showAutoC()", 4000);});
	jQuery("#bannerC_list img").width(jQuery("#bannerC").parent().width()-1);
	});
	function newsViewPicBot(href,id){
	if(href==""){
		var url = "/cms/BN_BANNER_PIC/" + id + ".html";	
		window.open(url);		
	}else{
		window.open(href);
	}
	
		
	}
</script> 
