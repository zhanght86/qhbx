<!DOCTYPE html>
<html>
<head>
<!----------------唯一放在前面的jQuery核心库-----开始------------------>
<script type="text/javascript" src="/sy/base/frame/jquery-1.8.2.min.js"></script>
<!----------------唯一放在前面的jQuery核心库-----结束------------------>
<!----------------------以下是页面中所有用到的CSS-----------开始--------------------------->
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/framework_7d9033ac.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/module_index_777ae95d.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/index_50da8c9a.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/css/baiduwenku_index.css">
<style type="text/css">
.docBubble { z-index: 500;}
#baidu-top-tip .close { background: url(/sy/comm/wenku/baidu_style_files/close_bg.png) no-repeat 0 -1px !important;}
#doc .ic-pptx {background:url(/sy/comm/wenku/baidu_style_files/module_static_z20976ac6.png) no-repeat -1098px 0;}
.bottom-topic .topic-docList-item .doc-value,.bottom-topic .topic-docList-item .doc-rate-count,.bottom-topic .topic-docList-item .doc-page-num
{ display: none !important;}

.user-bar .mn-lk,.ui-crumbs li {
	background-image:
		url("/sy/comm/wenku/baidu_style_files/module_static_z69a01da9.png");
}

.user-bar .mn-lk {
	background-position: right 6px;
}

.ui-crumbs li {
	background-position: right -5px;
}

div#hd .top-search-box {
	position: relative;
}

.reader-tools-bar-wrap .reader-download {
	overflow: visible !important;
}

.reader-tools-bar-wrap .reader-download span {
	top: -6px !important;
	right: -90px !important;
	height: 35px !important;
}
#banner0 li p{
	cursor:pointer;
}
</style>

<!----------------------以下是页面中所有用到的CSS-----------结束--------------------------->

<#include "global.ftl"/>
<#include "/SY_COMM_WENKU/config_constant.ftl">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<title>文库――让每个人平等地提升自我</title>
<meta property="wb:webmaster" content="3dc25059492736e0">
<meta name="description" content="在线互动式文档分享平台，在这里，您可以和千万网友分享自己手中的文档，全文阅读其他用户的文档，同时，也可以利用分享文档获取的积分下载文档 ">


</head>
<body style='background:none;background-color:white;'>

	<!-- page begin -->
	<div id="doc" class="page" style="padding:20px;width:1160px;background-color:#FFF!important;">
		<!-- #include "/SY_COMM_WENKU/baiduwenku_header.ftl" -->
		<!-- #include "/SY_COMM_WENKU/baiduwenku_navigation.ftl" -->
		<div id="bd">
			<div class="bd-wrap">
				<div class="body" style="background-color:#FFF!important;">
					<div id="banner0" class="bannerTop">
						
						<div style="width:381px;height:250px;float:left;margin-right:10px;">
							<div class="wenku_index_document_category_img"></div>
							<div style="width:380px;height:80px;background-color:#00C0FF;padding:20px 0px;">
								<@channel_list debugName="首页导航" channelId="${root_channel_id}"> 
									<#list tag_list as channel>
										<a href="<@chnlUrl channel.CHNL_ID 1 />" target="_self" class="wenku_index_document_category_item">${channel.CHNL_NAME}(<span class="${channel.CHNL_ID}">0</span>)</a>
									</#list>
								</@channel_list>
							</div>
						</div>
						<div style="width:510px;height:250px;float:left;margin-right:10px;">
							<div style="width:510px;height:120px;">
								<a href="#" onclick="javascript:myWenkuCenter();" target="_self" class="wenku_index_manager_img"></a>
								<a href="<@tmplUrl hot_document_tmpl_id />" class="wenku_index_hot_document_img"></a>
								<a href="#" onclick="javascript:myDocuments();" target="_self" class="wenku_index_my_documents_img"></a>
							</div>
							<div style="width:510px;height:120px;margin-top:10px;margin-right:10px;">
								<a href="<@tmplUrl new_document_tmpl_id />" class="wenku_index_new_document_img"></a>
								<a href="javascript:openZhidaoTab();" class="wenku_index_document_search_img"></a>
							</div>
						</div>
						<div target="_self" class="wenku_index_upload_img">
							<@wenku_total debugName="文档统计" siteId="${site_id}">
								<span id="upload_document_counter">当前已有  ${total} 份文档</span>
								<span id="upload_document_font"></span>
								<a href="#" onclick="javascript:upload('${upload_tmpl_id}','${site_id}')" id="upload_btn"></a>
							</@wenku_total>
						</div>
						
						
					</div>
					<div id="contentWrap1" class="mt10 mainbody">
						<div class="aside" style="margin-left:9px;">
							<div class="mod userInfo">
								<div class="clearfix userTitle">
									您可以<a 
										onclick="javascript:upload('${upload_tmpl_id}','${site_id}');"
										href="#" target="_self">上传文档</a>，
										<a 
										onclick= "javascript:myDocuments();";
										href="#" target="_self">分类管理文档</a>，打造你的随身知识库。
								</div>
								<div class='mt10'>
									<table style='border-collapse:separate;border-spacing:5px;'>
										<tr>
											<td>
												<img id='currentUsImg' style="width:auto;height:50px;border:1px solid #e8e8e8;" src="" >
											</td>
											<td><span id='currentUsName' style='font-weight: 600;color: #666;float: left;'></span></td>
										</tr>
									</table>
								</div>
								<ul class="userData clearfix">
								</ul>
								<div id="browse">
									<h4 style="width:207px;padding-left:0px;margin-left:10px;background-color:rgb(230,233,241);height:30px;line-height:30px;font-size:16px;color:black;font-family:楷体;font-weight:700;">
										<span class="wenku_note_img"></span>
										最近浏览
									</h4>
									<ul id="browse-wrap" class="record">
									</ul>
								</div>
								<div id="mywk">
									<h4 style="width:207px;padding-left:0px;margin-left:10px;background-color:rgb(230,233,241);height:30px;line-height:30px;font-size:16px;color:black;font-family:楷体;font-weight:700;">
										<span class="wenku_note_img"></span>
										我的文库
									</h4>
									<ul id="mydoc" class="record">
									</ul>
								</div>
							</div>
							
							<div class="mod rank">
								<h3 style='font-size:16px;color:#2A8BD1;font-family:楷体;'>文档排行榜...</h3>
								<dl>
									<@wenku_readTop debugName="文档排行" siteId="${site_id}" count="5"> 
									<#list tag_list as doc>
									<dt class="rank-color" style='font-size:12px;color:black;font-weight:none;'>${doc_index + 1}.</dt>
									<dd>
										<#if doc.DOCUMENT_FILE_PAGES gt 0>
										<span style='font-size:12px;color:black;'>${doc.DOCUMENT_FILE_PAGES}页</span>
										</#if>
										<a
											href="#"
											onclick="javascript:view('${doc.DOCUMENT_ID}','${doc.DOCUMENT_TITLE}')"; 
											title="${doc.DOCUMENT_TITLE}"
											data-logsend="{&quot;send&quot;:[&quot;home&quot;,&quot;homeclk&quot;,{&quot;subtype&quot;:&quot;docTop&quot;}]}"
											class="logSend" target="_self"
											style='font-size:12px;color:black;'>
											<#if doc.DOCUMENT_TITLE?length gt 10>
																${doc.DOCUMENT_TITLE[0..10]}...
																<#else>${doc.DOCUMENT_TITLE}	
											</#if>
											</a>
									</dd>
									</#list>
									</@wenku_readTop>
								
								</dl>
							</div>
							<div class="mod rank tabs ui-tabs-aside user rank3" style="min-height:440px;">
								<h3 style='font-size:16px;color:#2A8BD1;font-family:楷体;'>积分排行榜...</h3>
								<div class="hd topper">
									<ul class="tabControl">
										<li class="disabled show" num="1">
											<a style="color:black;">总积分</a>
										</li>
										<li class="current show" num="2">
											<a style="color:black;">本月</a>
										</li>
										<li class="disabled show" num="3">
											<a style="color:black;">本周</a>
										</li>
									</ul>
								</div>
								<div class="bd">
									<ul class="tabContent clearfix">
										<li class="disabled show_1" id="content_1">
											<ul>
											<@wenku_integralTop debugName="总积分排行" type="total" count="5"> 
											<#list tag_list as integral>
											<li>
												<dl style="padding-top:10px;">
													<dt class="rank-color">
														<div class="custom-left">
														   	 <a rel="nofollow" href="javascript:othersDocuments('${user_center_tmpl_id}','${integral.USER.USER_CODE}')">
														        <img src="<@setUserImg integral.USER/>?size=50x50" style="1px solid #e5e5e5;width:auto;height:50px;">
														    </a>
														</div>
													</dt>
													<dd>
														<div class="custom-right">
															<a href="javascript:othersDocuments('${user_center_tmpl_id}','${integral.USER.USER_CODE}')" style="color:black;">${integral.USER_NAME}</a>
															<p>积分：${integral.INTEGRAL_VALUE}</p>
														</div>	 
													</dd>
												</dl>
											</li>
												</#list>
												</@wenku_integralTop>
											</ul>
										</li>
										<li class="current show_2" id="content_2">
											<ul>
												<@wenku_integralTop debugName="本月积分排行" type="month" count="5"> 
												<#list tag_list as integral>
												<li>
												   <dl style="padding-top:10px;">
														<dt class="rank-color">
															<div class="custom-left">
																   	 <a rel="nofollow" href="javascript:othersDocuments('${user_center_tmpl_id}','${integral.USER.USER_CODE}')">
																        <img src="<@setUserImg integral.USER/>?size=50x50" style="1px solid #e5e5e5;width:auto;height:50px;">
																    </a>
																</div>
														</dt>
														<dd>
															<div class="custom-right">
																	<a href="javascript:othersDocuments('${user_center_tmpl_id}','${integral.USER.USER_CODE}')" style="color:black;">${integral.USER_NAME}</a>
																	<p>积分：${integral.SCORE}</p>
																</div>	
														</dd>
													 </dl>
													</li>
												</#list>
												</@wenku_integralTop>
												</ul>
										 </li>
										 <li class="disabled show_3" id="content_3">
											<ul>
												<@wenku_integralTop debugName="本周积分排行" type="week" count="5"> 
												<#list tag_list as integral>
												<li>
												   <dl style="padding-top:10px;">
														<dt class="rank-color">
															<div class="custom-left">
																   	 <a rel="nofollow" href="javascript:othersDocuments('${user_center_tmpl_id}','${integral.USER.USER_CODE}')">
																        <img src="<@setUserImg integral.USER/>?size=50x50" style="1px solid #e5e5e5;width:auto;height:50px;">
																    </a>
																</div>
														</dt>
														<dd>
															<div class="custom-right">
																	<a href="javascript:othersDocuments('${user_center_tmpl_id}','${integral.USER.USER_CODE}')" style="color:black;">${integral.USER_NAME}</a>
																	<p>积分：${integral.SCORE}</p>
																</div>	
														</dd>
													 </dl>
													</li>
												</#list>
												</@wenku_integralTop>
												</ul>
											</li>
									</ul>
								</div>
							</div>
						</div>
						<div class="main">
							<div class="mod featureClass ui-imgtabs">
								<!--  精彩专题 -->
							</div>
							<div class="mod recommendation hotRead" id="recommendation">
							</div>
							<div class="mod hotRead" id="hotRead-mod">
								<div class="hd" id="hotRead">
									<h2 class="pb10 clearfix documentCenter">
										<span class="wenku_index_pen"></span>
										<span class="documentCenter_font">文档中心</span>
										<span id="hotSpan" class="documentHotFont documentHotFontSelect" style="margin-right:20px;">
											热门文档
											<i class="selectXiaBiaoKuang"></i>
											<i class="selectXiaoJianTou"></i>
										</span>
										<span id="newSpan" class="documentHotFont">
											最新文档
											<i class="selectXiaBiaoKuang" style="display:none;"></i>
											<i class="selectXiaoJianTou" style="display:none;"></i>
										</span>
									</h2>
								</div>
								<div id="hotDiv" class="bd">
									<div class="mod classBox">
										<div style="width:auto;height:30px;"></div>
										<div class="bd">
											<div class="gd-g classRow catalog-76">
												<@wenku_readTop debugName="热门阅读" siteId="${site_id}" count="7"> 
													<#list tag_list as doc>
														<div class="gd-g-u gd-u-1-6 classItem">
															<@setDocPic doc '' />
														</div>
													</#list>
												</@wenku_readTop>
											</div>
										    <div class="gd-g classRow catalog-76"></div>
										</div>
									</div>
								</div>
								
								<div id="newDiv" class="bd" style="display:none;">
									<div class="mod classBox">
										<div style="width:auto;height:30px;"></div>
										<div class="bd">
											<div class="gd-g classRow catalog-76">
												<@wenku_list debugName="最新文档"  page="1" order="S_CTIME DESC" count="7">
													<#list tag_list as doc>
														<div class="gd-g-u gd-u-1-6 classItem">
															<@setDocPic doc '' />
														</div>
													</#list>
												</@wenku_list>
											</div>
										    <div class="gd-g classRow catalog-76"></div>
										</div>
									</div>
								</div>
								
								<!----------------------这里放最新阅读-----开始------------------------->	
								<!-写一个新的方法-->
								<!----------------------这里放最新阅读-----结束------------------------->	
															
								<div id="hotRead" class="hd">
									<h2 class="pb10 clearfix documentCategory">
										<span class="wenku_index_pen"></span>
										<span class="documentCenter_font">文档分类</span>
									</h2>
								</div>
									
									
									
					<@channel_list debugName="获取文库首页栏目" channelId="${root_channel_id}"> 				
					<#list tag_list as channel>
					<@wenku_list debugName="栏目下文档:${channel.CHNL_NAME}" channelId="${channel.CHNL_ID}" count="7"> 
						<div class="mod classBox">
						<#if (tag_list?size gt 0) >
							<div class="hd logClass" cid="126" num="12">
								<h3 class="ph15">
									<span>
										<a href="<@chnlUrl channel.CHNL_ID 1 />" target="_self" class="catalog-title categoryTitle">
											${channel.CHNL_NAME}
										</a>
									</span>
								</h3>
							</div>
						<div class="bd" style="border-bottom:1px solid #E5E5E5;">
							<div class="gd-g classRow">
								<#list tag_list as doc>
									<div class="gd-g-u gd-u-1-6 classItem">
										<@setDocPic doc '' />
									</div>
								</#list>
							</div>
						<div class="gd-g classRow"></div>
						</div>
						</#if>
					</@wenku_list>
					</div>
					</#list>
					</@channel_list>
							</div>
						</div>
					</div>
				</div>
					
				</div>
			</div>
		</div>
		<#include "/SY_COMM_WENKU/baiduwenku_footer.ftl">
	</div>
	<div id="change2pad"
		style="position: relative; top: -25px; font-size: 14px; text-align: center; padding: 10px 0; display: none;">
		<a style="text-decoration: none;" href="javascript:void(0)">Pad版</a><span
			style="color: #ccc; padding: 5px;"> | </span>传统版
	</div>
	<div data-guid="TANGRAM$3" id="tangram-suggestion--TANGRAM$3-main"
		class="tangram-suggestion-main"
		style="position: absolute; display: none; left: 294px; top: 73px; width: 408px;">
		<div id="tangram-suggestion--TANGRAM$3" class="tangram-suggestion"
			style="position: relative; top: 0px; left: 0px"></div>
	</div>
	<div style="left: 925px;" id="docBubble" class="docBubble">
		<i class="triangle-t"></i><i title="关闭" class="close markSend">关闭</i>
		<div class="tl">
			<div class="inner"></div>
		</div>
		<div class="tr"></div>
		<div class="bl"></div>
	</div>

<!-----------------------以下是这个页面中的所有JS--------开始------------------------------>
<script type="text/javascript"> var FireFlyContextPath =''; </script>
<script type="text/javascript" src="/sy/base/frame/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="/sy/base/frame/constant.js"></script>
<script type="text/javascript" src="/sy/base/frame/tools.js"></script>
<script type="text/javascript" src="/sy/base/frame/platform.js"></script>
<script type="text/javascript" src="/sy/base/frame/coms/rh.ui.menu.js"></script>
<script type="text/javascript" src="/sy/comm/cms/js/wenku.js"></script>
<script type="text/javascript" src="/sy/comm/wenku/js/lazy-load-img.js"></script>
<script>
	jQuery(document).ready(function(){
		jQuery("#banner0 li .p_t").click(function(){myWenkuCenter()});
		jQuery("#banner0 li .p_f").click(function(){createDoclist()});
		jQuery("#banner0 li .p_l").click(function(){upload('${upload_tmpl_id}','${site_id}')});
	});
</script>
<script type='text/javascript'>
	var imgSrc = parent.System.getVar("@USER_IMG@");
	if (imgSrc != undefined && imgSrc != '') {
		imgSrc = imgSrc.split("?")[0]+"?size=50x50";
	} else {
		imgSrc = "/sy/theme/default/images/common/rh-lady-icon.png?size=50x50";
	}
	$("#currentUsImg").attr("src",imgSrc);
	$("#currentUsName").text(parent.System.getVar("@USER_NAME@"));
</script>
<!-- my wenku info -->
<script>
	/* 我的文库信息 */
	var result = parent.FireFly.doAct("SY_COMM_WENKU_DOCUMENT","getMyInfo",{"DOCUMENT_STATUS":2},false,false);
	var userIntegral = result["USER_INTEGRAL"];
	var pubDocs = result["PUB_DOC_COUNT"];
	var doclistCount = result["DOCLIST_COUNT"];
	var readHisArray = result["READ_HIS"];
	var myDocArray = result["MY_DOC_LIST"];

	//积分
	var integral = jQuery("<li class='wealth' style='width:50%;'></li>").appendTo($(".userData"));
	var integralBtn = jQuery("<a href='#' target='_self'></a>").appendTo(integral);
	integralBtn.append("<b>" + userIntegral + "</b><br> <span>积分值</span>");
	integralBtn.bind("click", function() {
		myWenkuIntegral();
	});
	//公共文档数量
	var pubDocLi = jQuery("<li class='doc' style='width:50%;'></li>").appendTo($(".userData"));
	var pubDocBtn = jQuery("<a href='#' target='_self'></a>").appendTo(pubDocLi);
	pubDocBtn.append("<b>" + pubDocs + "</b><br> <span>公共文档</span>");
	pubDocBtn.bind("click", function() {
	    myDocuments();
	});
    //文辑数量
	//	$(".userData").append("<li class='doc'><a href='#' target='_blank'><b>" + doclistCount + "</b><br> <span>文辑</span></a></li>");
	
/*	
	var myDoclistLi = jQuery("<li class='doc'></li>").appendTo($(".userData"));
	var myDoclistBtn = jQuery("<a href='#' target='_self'></a>").appendTo(myDoclistLi);
	myDoclistBtn.append("<b>" + doclistCount + "</b><br> <span>文辑</span>");
	myDoclistBtn.bind("click", function() {
   		myDoclist();
    });
*/

	//浏览历史
	jQuery.each(readHisArray,function(i,n) {
		var li = jQuery("<li></li>").appendTo($("#browse-wrap"));
		var page = jQuery("<span></span>").appendTo(li);
	//page.text("1页");
		var title = n.DATA_DIS_NAME;
		if (title.length > 12) {
			title = title.substring(0, 12) + "...";
		}
		var btn = jQuery("<a href='#' title='" + n.DATA_DIS_NAME + "' target='_self' class='logSend' style='font-size:12px;color:black;'></a>").appendTo(li);
		btn.text(title);
		btn.bind("click", function() {
			view(n.DATA_ID, n.DATA_DIS_NAME);
        });
	});

	//用户文档
	jQuery.each(myDocArray,function(i,n) {
		var li = jQuery("<li></li>").appendTo($("#mydoc"));
		var page = jQuery("<span></span>").appendTo(li);
		if (n.DOCUMENT_FILE_PAGES > 0) {
			page.text( n.DOCUMENT_FILE_PAGES + "页");	
		} 
		var title = n.DOCUMENT_TITLE;
		if (title.length > 12) {
			title = title.substring(0, 12) + "...";
		}
		var btn = jQuery("<a href='#' title='" + n.DOCUMENT_TITLE + "' target='_self' class='logSend' style='font-size:12px;color:black;'></a>").appendTo(li);
		btn.text(title);
		btn.bind("click", function() {
			view(n.DOCUMENT_ID, n.DOCUMENT_TITLE);
        });
	});

/* 我的文库信息 */
</script>
<!--------------给用户图片绑定的延时加载事件-----开始---------------->
<script>
	jQuery(document).ready(function(){
		
		//tab切换的图片加载事件
		jQuery(window).bind("scroll",function(){
			var scrollTop = window.pageYOffset  
			                || document.documentElement.scrollTop  
			                || document.body.scrollTop  
			                || 0;
			if(scrollTop >= 1000){
				jQuery(".tabContent li img").each(function(index,img){
					if(jQuery(img).attr("src") == '/sy/theme/default/images/common/rh-lady-icon.png' && jQuery(img).attr("data-original") != '/sy/theme/default/images/common/rh-lady-icon.png'){
						jQuery(img).attr("src",jQuery(img).attr("data-original"));
					}
				});
			}
		});
		
		//tab切换的方法
		jQuery(".show").mouseover(function(){
			jQuery(".current").removeClass("current").addClass("disabled");
			var num = jQuery(this).addClass("current").attr("num");
			jQuery("#content_"+num).addClass("current");
		})
		
		//最新文档和热门文档的切换
		jQuery("#hotSpan").bind("mouseover",function(){
			jQuery("#newSpan").removeClass("documentHotFontSelect");
			jQuery("#newSpan i").hide();
			jQuery(this).addClass("documentHotFontSelect");
			jQuery("#hotSpan i").show();
			jQuery("#newDiv").hide();
			jQuery("#hotDiv").show();
		});
		jQuery("#newSpan").bind("mouseover",function(){
			jQuery("#hotSpan").removeClass("documentHotFontSelect");
			jQuery("#hotSpan i").hide();
			jQuery(this).addClass("documentHotFontSelect");
			jQuery("#newSpan i").show();
			jQuery("#hotDiv").hide();
			jQuery("#newDiv").show();
		});
	});
</script>
<!--------------给用户图片绑定的延时加载事件-----结束---------------->
<!-----------------------以上是这个页面中的所有JS--------结束------------------------------>
<!---------------如果是IE7就加上一段JS将重要显示的位置调整一下---开始----------------->
<!--[if IE 7]>
<script>
	jQuery(document).ready(function(){
		jQuery(".im").css("margin-left","-86px");
	});
</script>
<![endif]-->
<!---------------如果是IE7就加上一段JS将重要显示的位置调整一下---结束----------------->

<!---------------------控制外层滚动条-------开始------------------->
<script>
	//将当前iFrame中的Id设置成全局变量，因为在Tab中要用到它来定位高度
	var pid = $(window.top.document).find(".ui-tabs-selected").attr("pretabid");
	var windowName = $("#"+pid,window.top.document).children("iframe").attr("id");
	GLOBAL.setFrameId(windowName);
	jQuery(document).ready(function(){
		//Tab对象的方法，用来定位页面的高度
		Tab.setFrameHei(10);
		Tab.setFrameHei();
	});
	
	jQuery(document).ready(function() {
		var datas = {};
	    datas["_NOPAGE_"] = true;
		datas["_SELECT_"] = "DOCUMENT_CHNL , COUNT(DOCUMENT_ID) DOC_COUNT";
		datas["_searchWhere"] = " GROUP BY DOCUMENT_CHNL";
		
		var result = parent.FireFly.getListData("SY_COMM_WENKU_DOCUMENT", datas)._DATA_;
		jQuery(result).each(function(index, item) {
			jQuery("." + item["DOCUMENT_CHNL"]).html(item["DOC_COUNT"]);
		});
	});
</script>
<!---------------------控制外层滚动条-------结束------------------->
</body>
</html>