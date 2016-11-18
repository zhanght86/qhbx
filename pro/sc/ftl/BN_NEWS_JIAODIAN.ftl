<style type="text/css">
.pictureScroll #bannerT {}
.portal-box-title-top{position:absolute;top:8px;left:7px;width:7px;height:18px;background:url("/sc/img/pt_style/default/ddsb.png") transparent;}
.portal-box-title-bottom-line{left:0px;margin-left:-3px;width:87px;border-bottom: 2px #d20000 solid;top: 0px;position:relative;}
#bannerT {position:relative;border:0px solid #666; overflow:hidden; font-size:12px} 
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
<div id='PICTURE_SCROLL' class='portal-box pictureScroll' style="border-top-width:0px;padding-top:3px;">
<div class='portal-box-title ${titleBar}' style='position:relative;'>
<span class='portal-box-title-top'></span>
<span class="portal-box-title-label" style="background-color:white;margin-left:-3px;padding-left:20px;padding-right:10px;">${title}</span>
<div class="portal-box-title-bottom-line"></div>
</div>
<div class='portal-box-con'>
<div id="bannerT" style="height:${imgHeight};"> 
<div id="bannerT_bg" ></div> 

<div id="bannerT_list" style="width:47%;float:left;"> 
<#list _DATA_ as news>
<a href="javascript:newsViewPic('${news.NEWS_ID}');">
<#if (news.picture?? && news.picture.FILE_ID?length >0)>
	<img   height="${imgHeight!300}" src="/file/${news.picture.FILE_ID}?size=160x304" title="${news.NEWS_SUBJECT}" alt="${news.NEWS_SUBJECT}"/>
	
</#if>
</a>
</#list>
</div> 
<div id="bannerT_list" style="width:47%;float:left;"> 
<#list _DATA_ as news>
<a href="javascript:newsViewPic('${news.NEWS_ID}');">
<#if (news.picture?? && news.picture.FILE_ID?length >0)>
	<img   height="${imgHeight!300}" src="/file/${news.picture.FILE_ID}?size=160x304" title="${news.NEWS_SUBJECT}" alt="${news.NEWS_SUBJECT}"/>
	
</#if>
</a>
</#list>
</div> 
</div> 
</div>
</div>
<script type="text/javascript">
   var ta = na = 0, counta=0;
    function showAutoA() { 
		na = (na >=(counta - 1) ? 0 : ++na); 
		jQuery("#bannerT li").eq(na).click(); 
		ta = setTimeout("showAutoA()", 4000);
    } 
  jQuery(document).ready(function(){ 
	counta=jQuery("#bannerT_list a").length;
	jQuery("#bannerT_list a:not(:first-child)").hide();
	jQuery("#bannerT_info").html(jQuery("#bannerT_list a:first-child").find("img").attr("alt")); 
	jQuery("#bannerT_info").unbind("click").bind("click",
	function(){eval(jQuery("#bannerT_list a:first-child").attr("href"))});
	jQuery("#bannerT li").unbind("click").bind("click",function() {
		var i = jQuery(this).text() - 1;
		na = i; 
		if (i >= counta) return; 
		jQuery("#bannerT_info").html(jQuery("#bannerT_list a").eq(i).find("img").attr("alt")); 
		jQuery("#bannerT_info").unbind("click").bind("click", function(){eval(jQuery("#bannerT_list a").eq(i).attr("href"))});
		jQuery(this).parent().find("li").removeClass("linum");
		<#-- 解决ie8下局部页面css不渲染问题-->
		jQuery(this).parent().hide().show();
		jQuery(this).addClass("linum");
		jQuery("#bannerT_list a").filter(":visible").fadeOut(500).parent().children().eq(i).fadeIn(1000);
	}); 
	<#--使用setTimeout递归,实现定时调用问题 -->
	t= setTimeout("showAutoA()", 4000);
	jQuery("#bannerT").hover(function(){clearTimeout(ta)}, function(){ta= setTimeout("showAutoA()", 4000);});
	jQuery("#bannerT_list img").width(jQuery("#bannerT").parent().width()-1);
	});
	function newsViewPic(id){
		var url = "/cms/SY_COMM_INFOS/" + id + ".html";			
		window.open(url);
	}
</script> 
