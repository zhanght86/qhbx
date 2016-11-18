<style type="text/css">
.pictureScroll-zt {margin:auto}
#zt-banner {position:relative; width:100%;height:196px !important;border:1px;overflow:hidden; font-size:12px} 
#zt-banner_list img {border:0px;height:100%;width:100%} 
#zt-banner_bg {position:absolute; bottom:0;height:30px;filter: Alpha(Opacity=30);opacity:0.3;z-index:1000;cursor:pointer;} 
#zt-banner_info{position:absolute; bottom:4px; left:5px;height:22px;color:#fff;z-index:1001;cursor:pointer} 
#zt-banner_text {position:absolute;width:120px;z-index:1002; right:3px; bottom:3px;} 
#zt-banner ul {position:absolute;list-style-type:none;filter: Alpha(Opacity=80);opacity:0.8; z-index:1002; 
margin:0; padding:0; bottom:0px; right:5px; height:20px} 
#zt-banner ul li { float:left;width: 12px;height: 12px;margin-left:4px;padding:2px;color:#FFF;background:url("/sc/img/li_bg_2.png") no-repeat 0 -16px;cursor:pointer;font-family: "宋体"; font-size:12px;text-align:center} 
#zt-banner ul li.selected{background-position:0 0}
#zt-banner_list a{position:absolute;height:100%;width:100%}
</style> 
<div id='zt-PICTURE_SCROLL' class='pictureScroll-zt'>  
	<div class="portal-box">  
	<div class='portal-box-title'>
    	<span class="portal-box-title-pre"></span>
        <span class="portal-box-title-label">专题活动</span>
		<span class='portal-box-title-fix'></span>
    </div>
	<div class='portal-box-con'>
		<div id="zt-banner" style="height:${height}!important">
			<div id="zt-banner_bg" ></div> 
			<div id="zt-banner_info"></div>
			<ul id='zt-banner_imgUl'></ul>
			<div id="zt-banner_list"></div>  
		</div>	
	</div>
	</div>
</div>
<script type="text/javascript">

	function getSuitableSizeImg(url){
		var w = $("#zt-banner").width();
		var timeStr = new Date();
		timeStr = timeStr.getTime();
		<#--url = url +"?size="+w+"x"+193;-->
		url = url +"?size="+w+"x"+200;
		url = url + "&t=" + timeStr;
		return url;
	}
	jQuery(document).ready(function(){
		var newsWhere = {};
		newsWhere["_WHERE_"] = " and SERV_ID='SY_COMM_CMS_CHNL' and FILE_CAT='LANMUTUPIAN' and DATA_ID in(select chnl_id from sy_comm_cms_chnl where chnl_pid='${CHNL_PID}')";
		var newsData = FireFly.doAct("SY_COMM_FILE","finds",newsWhere);
		
		var len =  newsData._DATA_.length;
		
		<#-- 暂时添加一个妇女节的图片到专题活动中  -->
		<#-->
		$("<li>1</li>").appendTo($("#zt-banner_imgUl"));
		$("<a href='#'><img onclick='openPhotoWall38();' src='/sc/img/img38.jpg' /></a>").appendTo($("#zt-banner_list"));
		-->
		<#-- 暂时添加一个妇女节的图片到专题活动中  -->
		
		
		if(len == 0){
			jQuery("#zt-banner_imgUl").remove();
		}else{
			var index = 0;
			$(newsData._DATA_).each(function(){
				var chnlId = this.DATA_ID;
				index=index+1;
				if(index > 5){
					return;
				}
				var imgUl = $("#zt-banner_imgUl");
				var bannerList = $("#zt-banner_list");
				var imgAdr = this.FILE_PATH;
				var imgPath = getSuitableSizeImg("/file/"+imgAdr.substr(imgAdr.lastIndexOf("/")+1));
				if(len > 1){
					<#-- 暂时添加一个妇女节的图片到专题活动中  -->
					$("<li>"+index+"</li>").appendTo(imgUl);
					<#--$("<li>"+(parseInt(index)+1)+"</li>").appendTo(imgUl);-->
					<#-- 暂时添加一个妇女节的图片到专题活动中  -->
				}
				var aStr = "<a href='#'><img onclick = openZhuanTi('"+chnlId+"') src='"+imgPath+"'/></a>";
				jQuery(aStr).appendTo(bannerList);
			});	
		}
		
		var count=jQuery("#zt-banner_list a").length; 
		jQuery("#zt-banner_list a:not(:first-child)").hide(); 
		jQuery("#zt-banner_info").html(jQuery("#banner_list a:first-child").find("img").attr('alt')); 
		jQuery("#zt-banner li").click(function() { 
			var ii = jQuery(this).text() - 1;
			nn = ii; 
			if (ii >= count) return; 
			jQuery("#zt-banner_info").html(jQuery("#zt-banner_list a").eq(ii).find("img").attr('alt')); 
			jQuery("#zt-banner_list a").filter(":visible").fadeOut(500).parent().children().eq(ii).fadeIn(1000); 
			jQuery(this).siblings(".selected").removeClass("selected").end().addClass("selected"); 
		});
		(function(){
			var zt_t = zt_n = 0,zt_ct = jQuery("#zt-banner_list a").length;
			jQuery("#zt-banner li").eq(0).trigger('click'); 
			if (zt_ct > 1) {
				zt_t = setInterval(function(){
					zt_n = zt_n >= (zt_ct - 1) ? 0 : ++zt_n;
					jQuery("#zt-banner li").eq(zt_n).trigger('click');
				}, 4000);
			}
			jQuery("#zt-banner").hover(function(){clearInterval(zt_t)}, function(){
				zt_t = setInterval(function(){
					zt_n = zt_n >=(zt_ct - 1) ? 0 : ++zt_n; 
					jQuery("#zt-banner li").eq(zt_n).trigger('click'); 
				},4000);
			});

		})();

	});
	
	var openZhuanTi = function(chnlId) {
		var params = {};
		params["flag"] = true;
		params["CHNLID"] = chnlId;
		params["ZT"]="1";
		<#--var options = {"url":"SY_COMM_INFOS_VIEW.list.do","tTitle":"信息浏览","params":params,"menuId":"3w5vjk5vdanbWQFrVTEsrR","menuFlag":3};-->
		var options = {"url":"SY_COMM_INFOS_VIEW.list.do","tTitle":"信息浏览","params":params,"menuFlag":3};
		Tab.open(options);
	};
	
	<#-- 暂时添加一个妇女节的图片到专题活动中  -->
	var openPhotoWall38 = function() {
		var odept = System.getVar("@ODEPT_CODE@");
		var newsId = System.getVar("@C_FUNVJIE_NEWSID@");
		var url = "SC_PHOTO_WALL.show.do?chnlId=3PXaHB1VRdCqqQ4XL1hfXV&sOdept="+odept+"&newsId="+newsId;
		var params = {};  
		var options = {"url":url,"params":params,"menuFlag":3,"tTitle":"照片墙"}; 
		Tab.open(options);
	};
	<#-- 暂时添加一个妇女节的图片到专题活动中  -->
</script> 
