<style type="text/css">
.pictureScroll #bannerDb {}
#bannerDb {position:relative;border:0px solid #666; overflow:hidden; font-size:12px} 
#bannerDb_list img {border:0px;} 
#bannerDb_bg {position:absolute; bottom:0;background-color:#000;height:30px;filter: Alpha(Opacity=30);opacity:0.3;z-index:1000;cursor:pointer; } 
#bannerDb_info{position:absolute; bottom:4px; left:5px;height:22px;color:#fff;z-index:1001;cursor:pointer} 
#bannerDb_text {position:absolute;width:120px;z-index:1002; right:3px; bottom:3px;} 
#bannerDb ul {position:absolute;list-style-type:none;filter: Alpha(Opacity=80);opacity:0.8; z-index:1002; 
margin:0; padding:0; bottom:3px; right:5px; height:20px} 
#bannerDb ul li { padding:0 8px; line-height:18px;float:left;display:block;color:#FFF;border:#e5eaff 1px solid;background-color:#6f4f67;cursor:pointer; margin:0; font-size:16px;} 
#bannerDb_list a{position:absolute;} 
#bannerDb .linumDb {background-color:#be2424;color:#000}
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
<div id="bannerDb" style="height:${hei}px;"> 
<div id="bannerDb_bg" ></div> 
<div id="bannerDb_info" class="elipd" style="width:65%;"></div> 
<ul> 
<#list _DATA_ as news>
<#if (news.picture?? && news.NEWS_ID?length >0)>
	<#if (news_index = 0)>
		<li class="linumDb">${news_index+1}</li> 
	<#else>
	<li>${news_index+1}</li>
	</#if>
</#if>
</#list>
</ul> 
<div id="bannerDb_list"> 
<#list _DATA_ as news>
<a href="javascript:newsViewPicBottom('${news.OBJECT_2}','${news.NEWS_ID}');">
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
   var hb = ab = 0, countb=0;
    function showAutoDb() { 
		ab = (ab >=(countb-1) ? 0 : ++ab);
		jQuery("#bannerDb li").eq(ab).click(); 
		hb = setTimeout("showAutoDb()", 4000);
    } 
  jQuery(document).ready(function(){ 
	countb=jQuery("#bannerDb_list a").length;
	jQuery("#bannerDb_list a:not(:first-child)").hide();
	jQuery("#bannerDb_info").html(jQuery("#bannerDb_list a:first-child").find("img").attr("alt")); 
	jQuery("#bannerDb_info").unbind("click").bind("click",
	function(){eval(jQuery("#bannerDb_list a:first-child").attr("href"))});
	jQuery("#bannerDb li").unbind("click").bind("click",function() {
		var i = jQuery(this).text() - 1;
		ab = i; 
		if (i >= countb) return; 
		jQuery("#bannerDb_info").html(jQuery("#bannerDb_list a").eq(i).find("img").attr("alt")); 
		jQuery("#bannerDb_info").unbind("click").bind("click", function(){eval(jQuery("#bannerDb_list a").eq(i).attr("href"))});
		jQuery(this).parent().find("li").removeClass("linum");
		<#-- 解决ie8下局部页面css不渲染问题-->
		jQuery(this).parent().hide().show();
		jQuery(this).addClass("linum");
		jQuery("#bannerDb_list a").filter(":visible").fadeOut(500).parent().children().eq(i).fadeIn(1000);
	}); 
	<#--使用setTimeout递归,实现定时调用问题 -->
	k= setTimeout("showAutoDb()", 4000);
	
	jQuery("#bannerDb").hover(function(){clearTimeout(hb)}, function(){hb= setTimeout("showAutoDb()", 4000);});
	jQuery("#bannerDb_list img").width(jQuery("#bannerDb").parent().width()-1);
	});
	function newsViewPicBottom(href,id){
	if(href==""){
		var url = "/cms/BN_BANNER_PIC/" + id + ".html";	
		window.open(url);		
	}else{
		window.open(href);
	}
	
		
	}
</script> 
