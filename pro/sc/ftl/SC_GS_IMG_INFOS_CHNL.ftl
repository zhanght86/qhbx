<style type="text/css">
.pt-pic-scroll-wrapper {margin:auto}
#imgNews-banner {position:relative; width:100%;height:371px !important;border:1px;overflow:hidden; font-size:12px} 
#imgNews-banner_list img {border:0px;height:304px;width:100%} 
#imgNews-banner_bg {position:absolute; bottom:0;height:30px;filter: Alpha(Opacity=30);opacity:0.3;z-index:1000;cursor:pointer;} 
#imgNews-banner_info{position:absolute; bottom:15px; left:12px;height:22px;color:#000;font-size:16px;font-weight:bold;z-index:1001;cursor:pointer} 
#imgNews-banner_text {position:absolute;width:120px;z-index:1002; right:3px; bottom:3px;} 
#imgNews-banner ul {float:right;margin-top:8px;} 
#imgNews-banner ul li {float:left;width: 29px;height: 9px;margin: 0 5px 0 0;background:transparent url("/sc/img/li-bg.gif") no-repeat 0 -9px;}
#imgNews-banner ul li.selected{background-position:0 0}
#imgNews-banner_list a{position:static;height:100%;display:block;}
.imgNews-mask{position:absolute;bottom:56px;z-index:1002;width:540px;height:25px;margin: 0 10px;background:url("/sc/img/mask.png")repeat; }
</style> 
<div id='IMG-NEWS-PICTURE_SCROLL' class='pt-pic-scroll-wrapper'>  
	<div class="portal-box" style='position: relative;'>  
		<div id="imgNews-banner" style="height:${height}!important">
			<div id="imgNews-banner_bg" ></div> 
			<div id="imgNews-banner_info"></div>
			<div class='imgNews-mask'><ul id = 'imgNewsUl'></ul></div>
			<div id="imgNews-banner_list" style='padding:10px;height:100%;'></div> 
			<#if (_DATA_?size == 0)>
				<div>无法显示图片</div>
			<#else>
			<#list _DATA_ as imgNews>
				<script>
					var param = {
						"_WHERE_" : " and SERV_ID='SY_COMM_INFOS_BASE' and FILE_CAT='FENGMIANJI' and DATA_ID='${imgNews.NEWS_ID}'",
						"_ORDER_" : "S_MTIME ASC"
					};
					var result = FireFly.doAct("SY_COMM_FILE","finds",param);
					if(result._DATA_.length>0){
						var imgAdr = result._DATA_[0].FILE_PATH;
						var imgPath = "/file/"+imgAdr.substr(imgAdr.lastIndexOf("/")+1)+"?size=540x304";
						$("<li></li>").appendTo("#imgNewsUl");
						$("<a href='#' target='_blank'><img onclick=window.open('/cms/SY_COMM_INFOS/" + newsid + ".html')"+" src='"+imgPath+"' title='${imgNews.NEWS_SUBJECT}' alt='${imgNews.NEWS_SUBJECT}' /></a>").appendTo("#imgNews-banner_list");
					}
				</script>  
			</#list>
			</#if> 
		</div>	
		<div class='portal-box-title' style="overflow: visible; filter: Alpha(Opacity=0);opacity: 0;position: absolute;top:0"></div>
	</div>
</div>
<script type="text/javascript">
	jQuery(document).ready(function(){
		var count = jQuery("#imgNews-banner_list a").length; 
		
		jQuery("#imgNews-banner_list a:not(:first-child)").hide(); 
		jQuery("#imgNews-banner_info").html(jQuery("#banner_list a:first-child").find("img").attr('alt')); 

		jQuery("#imgNews-banner li").click(function() { 
			var i = jQuery(this).index();
			if (i >= count) {
				return; 
			}
			jQuery("#imgNews-banner_info").html(jQuery("#imgNews-banner_list a").eq(i).find("img").attr('alt')); 
			
			jQuery("#imgNews-banner_list a").filter(":visible").fadeOut(500);
			jQuery("#imgNews-banner_list a").eq(i).fadeIn(1000); 
			
			jQuery(this).siblings().removeClass("selected");
			jQuery(this).addClass("selected");
		});
		(function(){
			var imgTimer,
				imgIndex = 0,
				imgCount = jQuery("#imgNews-banner_list a").length;
			
			jQuery("#imgNews-banner li").eq(0).trigger('click');
			
			imgTimer = setInterval(function(){
				imgIndex = imgIndex >=(imgCount - 1) ? 0 : ++imgIndex; 
				jQuery("#imgNews-banner li").eq(imgIndex).trigger('click'); 
			},4100); 
			
			jQuery("#imgNews-banner").hover(
				function(){
					clearInterval(imgTimer)
				}, 
				function(){
					imgt = setInterval(function(){
						imgIndex = imgIndex >=(imgCount - 1) ? 0 : ++imgIndex; 
						jQuery("#imgNews-banner li").eq(imgIndex).trigger('click'); 
					},4100);
				}
			);

		})();
	});
</script> 
