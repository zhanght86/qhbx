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


<#include "/SY_COMM_WENKU/config_constant.ftl">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<title>文库――让每个人平等地提升自我</title>
<meta property="wb:webmaster" content="3dc25059492736e0">
<meta name="description" content="在线互动式文档分享平台，在这里，您可以和千万网友分享自己手中的文档，全文阅读其他用户的文档，同时，也可以利用分享文档获取的积分下载文档 ">


</head>
<body style='background-color:white;'>

	<!-- page begin -->
	<div id="doc" class="page">
		<#include "/SY_COMM_WENKU/baiduwenku_header.ftl">
		<#include "/SY_COMM_WENKU/baiduwenku_navigation.ftl">
		<div id="bd">
			<div class="bd-wrap">
				<div class="body">
					<div id="banner0" class="bannerTop">
						<ul>
							<li class="s"><a href="#hotRead" class="toHot hotArea" style='margin-top:50px'></a>
								<p class="p_s">从千万份文档中找到所需，每天阅读，让你的心灵更广阔。</p> <a href="#hotRead"
								class="hotS hotArea"></a></li>
							<li class="t"><a href="#" onclick="javascript:myWenkuCenter();" style='margin-top:50px'></a>
								<p class="p_t">收藏、下载、管理、同步等操作便捷随心，你的随身知识库。</p> <a href="#" onclick="javascript:myWenkuCenter();"
								class="hotT roomShare"></a></li>
							<li class="f"><a href="javascript:createDoclist();" style='margin-top:53px'></a>
								<p class="p_f">以系统、权威的视角，将文档串联起来，站在更高处，提升阅读的价值和体验。</p> <a href="javascript:createDoclist();"
								class="hotC"></a></li>
							<li class="l"><a style='margin-top:50px' href="#"  onclick="javascript:upload('${upload_tmpl_id}','${site_id}')"
								class="roomShare"></a>
								<p class="p_l">将文档与所有员工共同分享，你将知识的力量传递得更远。</p> <a href="#" onclick="javascript:upload('${upload_tmpl_id}','${site_id}')"
								class="hotL roomShare"></a></li>
						</ul>
					</div>
					<div id="contentWrap1" class="mt10 mainbody">
						<div class="aside">
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
												<img id='currentUsImg' width="50" height="50" src="" class="rh-user-info-circular-bead">
											</td>
											<td><span id='currentUsName' style='font-weight: 600;color: #666;float: left;'></span></td>
										</tr>
									</table>
								</div>
								<ul class="userData clearfix">
								</ul>
								<div id="browse">
									<h4>
										<a href="#" id="clearBrowse"></a>
										最近浏览
									</h4>
									<ul id="browse-wrap" class="record">
									</ul>
								</div>
								<div id="mywk">
									<h4>
										<a 
										onclick="javascript:myDocuments();"
										href="#" class="more">查看更多</a>
										<a
											onclick="javascript:myDocuments();"
											href="#" class="c-gray3">我的文库</a>
									</h4>
									<ul id="mydoc" class="record">
									</ul>
								</div>
							</div>
							
							<div class="mod area">
								<div class="hd clearfix">
									<h3>公告区</h3>
									<!-- 
									<a href="http://hi.baidu.com/wenkuduxiaoyue" target="_blank">更多</a>
									 -->
								</div>
								<div class="inner clearfix">
									<ul class="publist">
										<@wenku_notice_list debugName="文库公告" count="6"> 
										<#list tag_list as notice>
											<#if notice_index==0 >
											<li>
											<a href="#" class=" new logSend"
												onclick="javascript:openNotice('${notice_tmpl_id}','${notice.NOTICE_ID}');">
												<#if notice.NOTICE_TITLE?length gt 10>
													${notice.NOTICE_TITLE[0..10]}...
													<#else>${notice.NOTICE_TITLE}	
												</#if>
												</a><span></span>
											</li>
											<#elseif notice_index==1>
											<li>
												<a href="#" class=" new logSend" 
													onclick="javascript:openNotice('${notice_tmpl_id}','${notice.NOTICE_ID}');">
												<#if notice.NOTICE_TITLE?length gt 10>
														${notice.NOTICE_TITLE[0..10]}...
														<#else>${notice.NOTICE_TITLE}	
													</#if>
												</a>
											 </li>
											<#else>
												<li>
													<a href="#" class=" logSend"
														onclick="javascript:openNotice('${notice_tmpl_id}','${notice.NOTICE_ID}');">
													<#if notice.NOTICE_TITLE?length gt 10>
														${notice.NOTICE_TITLE[0..10]}...
														<#else>${notice.NOTICE_TITLE}	
													</#if>
													</a>
											  </li>
											</#if>
											</#list>
									  </@wenku_notice_list>
									</ul>
								</div>
							</div>
							<div class="mod rank">
								<h3>文档排行榜</h3>
								<dl>
									<@wenku_readTop debugName="文档排行" siteId="${site_id}" count="5"> 
									<#list tag_list as doc>
									<#if doc_index < 3>
									<dt class="rank-color">${doc_index + 1}</dt>
									<dd>
										<#if doc.DOCUMENT_FILE_PAGES gt 0>
										<span>${doc.DOCUMENT_FILE_PAGES}页</span>
										</#if>
										<a
											href="#"
											onclick="javascript:view('${doc.DOCUMENT_ID}','${doc.DOCUMENT_TITLE}')"; 
											title="${doc.DOCUMENT_TITLE}"
											data-logsend="{&quot;send&quot;:[&quot;home&quot;,&quot;homeclk&quot;,{&quot;subtype&quot;:&quot;docTop&quot;}]}"
											class="logSend" target="_self">
											<#if doc.DOCUMENT_TITLE?length gt 10>
																${doc.DOCUMENT_TITLE[0..10]}...
																<#else>${doc.DOCUMENT_TITLE}	
											</#if>
											</a>
									</dd>
									<#else>
									<dt>${doc_index + 1}</dt>
									<dd>
									   <#if doc.DOCUMENT_FILE_PAGES gt 0>
										<span>${doc.DOCUMENT_FILE_PAGES}页</span>
										</#if>
										<a
											href="#"
											title="${doc.DOCUMENT_TITLE}"
											class="logSend" target="_self">
											<#if doc.DOCUMENT_TITLE?length gt 10>
																${doc.DOCUMENT_TITLE[0..10]}...
																<#else>${doc.DOCUMENT_TITLE}	
											</#if>
											</a>
									</dd>
									</#if>
									</#list>
									</@wenku_readTop>
								
								</dl>
							</div>
							<div class="mod rank">
								<h3>文辑排行榜</h3>
								<dl>
								
								<@doclist_readTop debugName="文辑排行" siteId="${site_id}" count="5"> 
									<#list tag_list as doclist>
									<#if doclist_index < 3>
									<dt class="rank-color">${doclist_index + 1}</dt>
									<dd>
										<span>${doclist.LIST_DOCUMENT_COUNTER}篇</span><a
											href="#"
											onclick="javascript:viewDoclist('${doclist.LIST_ID}','${doclist.LIST_TITLE}')"; 
											title="${doclist.LIST_TITLE}"
											class="logSend" target="_self">${doclist.LIST_TITLE}</a>
									</dd>
									<#else>
										<dt>${doclist_index + 1}</dt>
									<dd>
										<span>${doclist.LIST_DOCUMENT_COUNTER}篇</span><a
											href="#"
											onclick="javascript:myDoclist();"
											title="${doclist.LIST_TITLE}"
											class="logSend" target="_self">${doclist.LIST_TITLE}</a>
									</dd>
									
									</#if>
									</#list>
									</@doclist_readTop>
									
								</dl>
							</div>
							<div class="mod rank tabs ui-tabs-aside user rank3">
								<h3>积分排行榜</h3>
								<div class="hd topper">
									<ul class="tabControl">
										<li class="disabled show" num="1">
											<a>总积分</a>
										</li>
										<li class="current show" num="2">
											<a>本月</a>
										</li>
										<li class="disabled show" num="3">
											<a>本周</a>
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
														        <img src="/sy/theme/default/images/common/rh-lady-icon.png" data-original="<@setUserImg integral.USER/>" style="1px solid #e5e5e5" width="50" height="50">
														    </a>
														</div>
													</dt>
													<dd>
														<div class="custom-right">
															<a href="javascript:othersDocuments('${user_center_tmpl_id}','${integral.USER.USER_CODE}')">${integral.USER_NAME}</a>
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
																        <img src="/sy/theme/default/images/common/rh-lady-icon.png" data-original="<@setUserImg integral.USER/>" style="1px solid #e5e5e5" width="50" height="50">
																    </a>
																</div>
														</dt>
														<dd>
															<div class="custom-right">
																	<a href="javascript:othersDocuments('${user_center_tmpl_id}','${integral.USER.USER_CODE}')">${integral.USER_NAME}</a>
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
																        <img src="/sy/theme/default/images/common/rh-lady-icon.png" data-original="<@setUserImg integral.USER/>" style="1px solid #e5e5e5" width="50" height="50">
																    </a>
																</div>
														</dt>
														<dd>
															<div class="custom-right">
																	<a href="javascript:othersDocuments('${user_center_tmpl_id}','${integral.USER.USER_CODE}')">${integral.USER_NAME}</a>
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
			<!-- 			
							<div class="mod tousu">
								如要投诉或提出意见建议，请与<br> <a target="_blank"
									href="#">文库管理员</a>联系。
							</div>
			 -->					
						</div>
						<div class="main">
							<div class="mod featureClass ui-imgtabs">
								<!--  精彩专题 -->
							</div>
							<div class="mod recommendation hotRead" id="recommendation">
							</div>
							<div class="mod hotRead" id="hotRead-mod">
								<div class="hd" id="hotRead">
									<h2 class="pb10 clearfix">
										<span class="title">热门文档</span>
									</h2>
								</div>
								<div class="bd">
									<div class="mod classBox">
										<div class="hd logClass" cid="76" num="12">
											<h3 class="ph15">
												<span><a href="file:///hotlist?page=1&amp;c=76"
													target="_blank" class="catalog-title">热门阅读</a></span>
											</h3>
										</div>
										<div class="bd">
											<div class="gd-g classRow catalog-76">
												<@wenku_readTop debugName="热门阅读" siteId="${site_id}" count="12"> 
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
								<div id="hotRead" class="hd">
									<h2 class="pb10 clearfix">
										<span class="title">分类文档</span>
									</h2>
								</div>
									
									
									
					<@channel_list debugName="获取文库首页栏目" channelId="${root_channel_id}"> 				
					<#list tag_list as channel>
					<@wenku_list debugName="栏目下文档:${channel.CHNL_NAME}" channelId="${channel.CHNL_ID}" count="6"> 
					
									<div class="mod classBox">
									<#if (tag_list?size gt 0) >
									
										<div class="hd logClass" cid="126" num="12">
											<h3 class="ph15">
												<span><a href="<@chnlUrl channel.CHNL_ID 1 />"
													target="_self" class="catalog-title">${channel.CHNL_NAME}</a></span>
											</h3>
										</div>
									</#if>
										<div class="bd">
											<div class="gd-g classRow">
											<#list tag_list as doc>
												<div class="gd-g-u gd-u-1-6 classItem">
													<@setDocPic doc '' />
												</div>
												</#list>
												</@wenku_list>
											</div>
											<div class="gd-g classRow"></div>
										</div>
									</div>
									</#list>
									</@channel_list>
									
									
									<!-- 
									<div class="banner sort_6 logSend"
										data-logsend="{&quot;send&quot;:[&quot;home&quot;,&quot;homeclk&quot;,{&quot;subtype&quot;:&quot;bottomsetclass&quot;}]}">
										<a href="javascript:;" title="添加更多分类" class="logSend"
											data-logsend="{&quot;send&quot;:[&quot;home&quot;,&quot;homeclk&quot;,{&quot;subtype&quot;:&quot;bottomsetclass&quot;}]}">添加更多</a>
									</div>
									 -->
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
	var integral = jQuery("<li class='wealth'></li>").appendTo($(".userData"));
	var integralBtn = jQuery("<a href='#' target='_self'></a>").appendTo(integral);
	integralBtn.append("<b>" + userIntegral + "</b><br> <span>积分值</span>");
	integralBtn.bind("click", function() {
		myWenkuIntegral();
	});
	//公共文档数量
	var pubDocLi = jQuery("<li class='public'></li>").appendTo($(".userData"));
	var pubDocBtn = jQuery("<a href='#' target='_self'></a>").appendTo(pubDocLi);
	pubDocBtn.append("<b>" + pubDocs + "</b><br> <span>公共文档</span>");
	pubDocBtn.bind("click", function() {
	    myDocuments();
	});
    //文辑数量
	//	$(".userData").append("<li class='doc'><a href='#' target='_blank'><b>" + doclistCount + "</b><br> <span>文辑</span></a></li>");
	var myDoclistLi = jQuery("<li class='doc'></li>").appendTo($(".userData"));
	var myDoclistBtn = jQuery("<a href='#' target='_self'></a>").appendTo(myDoclistLi);
	myDoclistBtn.append("<b>" + doclistCount + "</b><br> <span>文辑</span>");
	myDoclistBtn.bind("click", function() {
   		myDoclist();
    });

	//浏览历史
	jQuery.each(readHisArray,function(i,n) {
		var li = jQuery("<li></li>").appendTo($("#browse-wrap"));
		var page = jQuery("<span></span>").appendTo(li);
	//page.text("1页");
		var title = n.DATA_DIS_NAME;
		if (title.length > 12) {
			title = title.substring(0, 12) + "...";
		}
		var btn = jQuery("<a href='#' title='" + n.DATA_DIS_NAME + "' target='_self' class='logSend'></a>").appendTo(li);
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
		var btn = jQuery("<a href='#' title='" + n.DOCUMENT_TITLE + "' target='_self' class='logSend'></a>").appendTo(li);
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
</body>
</html>