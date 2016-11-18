<style type="text/css">
.pictureScroll #bannerT {}
#bannerT {position:relative;width:362px;border:0px solid #666; overflow:hidden; font-size:12px} 
#bannerT_list img {border:0px;} 
#bannerT_bg {position:absolute; bottom:0;background-color:#000;height:30px;filter: Alpha(Opacity=30);opacity:0.3;z-index:1000;cursor:pointer; } 
#bannerT_info{position:absolute; bottom:4px; left:5px;height:22px;color:#fff;z-index:1001;cursor:pointer} 
#bannerT_text {position:absolute;width:120px;z-index:1002; right:3px; bottom:3px;} 
#bannerT ul {position:absolute;list-style-type:none;filter: Alpha(Opacity=80);opacity:0.8; z-index:1002; 
margin:0; padding:0; bottom:3px; right:5px; height:20px} 
#bannerT ul li { padding:0 8px; line-height:18px;float:left;display:block;color:#FFF;border:#e5eaff 1px solid;background-color:#6f4f67;cursor:pointer; margin:0; font-size:16px;} 
#bannerT_list a{position:absolute;} 
#bannerT .linum {background-color:#be2424;color:#000}
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
<div class='portal-box-title ${titleBar}'><span class="portal-box-title-label">图片新闻</span></div>
<div class='portal-box-con'>
<div id="bannerT" style="height:381px;"> 
<div id="bannerT_bg" ></div> 
<div id="bannerT_info" class="elipd" style="width:65%"></div> 
<ul> 
<li class="linum">1</li> 
<li>2</li> 
<li>3</li> 
<li>4</li> 
</ul> 
<div id="bannerT_list"> 
<#list _DATA_ as news>
<a href="javascript:newsViewPic('${news.NEWS_ID}');">
<#if (news.picture?? && news.picture.FILE_ID?length >0)>
	<img   height="${imgHeight!386}" src="/file/${news.picture.FILE_ID}" title="${news.NEWS_SUBJECT}" alt="${news.NEWS_SUBJECT}"/>
</#if>
</a>
</#list>
</div> 
</div> 
</div>
</div>
<script type="text/javascript">
   var t = n = 0, count=0;
    function showAuto() { 
		n = (n >=(count - 1) ? 0 : ++n); 
		jQuery("#bannerT li").eq(n).click(); 
		t = setTimeout("showAuto()", 4000);
    } 
  jQuery(document).ready(function(){ 
	count=jQuery("#bannerT_list a").length;
	jQuery("#bannerT_list a:not(:first-child)").hide();
	jQuery("#bannerT_info").html(jQuery("#bannerT_list a:first-child").find("img").attr("alt")); 
	jQuery("#bannerT_info").unbind("click").bind("click",
	function(){eval(jQuery("#bannerT_list a:first-child").attr("href"))});
	jQuery("#bannerT li").unbind("click").bind("click",function() {
		var i = jQuery(this).text() - 1;
		n = i; 
		if (i >= count) return; 
		jQuery("#bannerT_info").html(jQuery("#bannerT_list a").eq(i).find("img").attr("alt")); 
		jQuery("#bannerT_info").unbind("click").bind("click", function(){eval(jQuery("#bannerT_list a").eq(i).attr("href"))});
		jQuery(this).parent().find("li").removeClass("linum");
		<#-- 解决ie8下局部页面css不渲染问题-->
		jQuery(this).parent().hide().show();
		jQuery(this).addClass("linum");
		jQuery("#bannerT_list a").filter(":visible").fadeOut(500).parent().children().eq(i).fadeIn(1000);
	}); 
	<#--使用setTimeout递归,实现定时调用问题 -->
	t= setTimeout("showAuto()", 4000);
	jQuery("#bannerT").hover(function(){clearTimeout(t)}, function(){t= setTimeout("showAuto()", 4000);});
	jQuery("#bannerT_list img").width(jQuery("#bannerT").parent().width()-2);
	});
	function newsViewPic(id){
		var url = "/cms/SY_COMM_INFOS/" + id + ".html";			
		window.open(url);
	}
</script> 
