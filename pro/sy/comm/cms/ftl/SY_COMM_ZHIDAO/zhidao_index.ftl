<!DOCTYPE html>
<!--STATUS OK-->
<html>
<head>
<#include "/SY_COMM_ZHIDAO/config_constant.ftl">
<script type="text/javascript"> var FireFlyContextPath =''; </script>
<script type="text/javascript" src="/sy/base/frame/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="/sy/base/frame/constant.js"></script>
<script type="text/javascript" src="/sy/base/frame/tools.js"></script>
<script type="text/javascript" src="/sy/base/frame/platform.js"></script>
<script type="text/javascript" src="/sy/base/frame/coms/rh.ui.menu.js"></script>
<script type="text/javascript" src="/sc/js/sc_projectJs.js"></script>
<script type="text/javascript" src="/sy/base/frame/coms/rh.ui.openTab.js"></script>
<script type="text/javascript" src="/sy/comm/cms/js/zhidao.js"></script>
<script type="text/javascript" src="/sy/comm/zhidao/js/user_img.js"></script>
<link rel="stylesheet" type="text/css" href="/sy/theme/default/common.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/module_index_777ae95d.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/baidu_style_files/framework_7d9033ac.css">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>
	软虹知道 - 企业互动问答平台
</title>
<meta name="description" content="软虹知道是 基于搜索的互动式知识问答分享平台。用户可以根据自身的需求，有针对性地提出问题；同时，这些答案又将作为搜索结果，满足有相同或类似问题的用户需求。">
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/baidu_style_files/module_fe8dd525.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/baidu_style_files/module_c595d1cf.css">
<script type="text/javascript" src="/sy/comm/zhidao/baidu_style_files/base_aa43e0c6.js"></script>
<script type="text/javascript" src="/sy/comm/zhidao/baidu_style_files/module_7087aaee.js"></script>
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/baidu_style_files/bdimBubble.css">
<!-----出题需要引入的文件----开始----->
<link rel="stylesheet" type="text/css" href="/sy/plug/search/rhSearchResult.css">
<link rel="stylesheet" href="/sy/comm/zhidao/zhihu_style_files/c60843d5fb7d7aeb2bfa3619734d9b18.css" type="text/css" media="screen,print">
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/zhidao_ask.css">
<script type="text/javascript" src="/sy/comm/zhidao/js/zhidao_ask.js"></script>
<script>
	var center_temp_id = '${userZhidao_center_tmpl_id}';
</script>
<!-----出题需要引入的文件----开始----->
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/userzhidao_center.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/zhidao_index.css">

<script type="text/javascript" src="/sy/comm/zhidao/js/zhidao_index.js"></script>
<script>
	var center_temp_id = '${userZhidao_center_tmpl_id}';
	var noAnswer = '${noAnswer!''}';
	//打开"文库"的方法
function openWenkuTab(){
	var menuItemWenku = {"ID":"SY_DOC_CENTER__ruaho","NAME":"文档中心","INFO":"cms/SY_COMM_CMS_CHNL/WENKU_3hWpJcmkZcNHarHMgIvMm2/index.html?refreshClickFlag=true","MENU":"3"};
	Menu.linkNodeClick(menuItemWenku);
}
</script>

<!---------------返回顶部的Js/CSS------开始--------------->
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/zd-goto-top.css">
<script type="text/javascript" src="/sy/comm/zhidao/js/zd-goto-top.js"></script>
<!---------------返回顶部的Js/CSS------结束--------------->

<!----------------意见反馈Js/CSS--------开始----------------->
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/zhidao_feedback.css">
<script type="text/javascript" src="/sy/comm/zhidao/js/zhidao_feedback.js"></script>
<!----------------意见反馈Js/CSS--------结束----------------->
</head>
<body class="layout-center">
	<div class='layout-center-div' style="background-image:none;background-color:#FFF!important;">
		<div class="line"><!-- userbar --></div>
		<div id="body" class="container" style="padding:20px;width:1160px;overflow:hidden;">
			 <div class="bannerTop" id="banner0" style="overflow:hidden;">
				<div style="width:381px;height:250px;float:left;margin-right:10px;">
					<div class="zhidao_index_document_category_img"></div>
					<div style="width:380px;height:80px;background-color:#00C0FF;padding:20px 0px;">
					
					<@channel_list debugName="二级栏目导航" count="15" channelId="${root_channel_id}">
						<#if tag_list?size != 0>
							<#list tag_list as chnlObj>
								<li>
							  		<@ask_count debugName="栏目下问题统计" chnlId="${chnlObj.CHNL_ID}">
										<a class="zhidao_index_document_category_item" href="<@chnlUrl chnlObj.CHNL_ID 1/>" style="font-weight:700;font-size:12px;font-family:'Microsoft YaHei';color:white;text-decoration:none;">${chnlObj.CHNL_NAME}(${count})</a>
									</@ask_count> 
								</li>
							</#list>
						</#if>
					</@channel_list>
					</div>
				</div>
				<div style="width:510px;height:250px;float:left;margin-right:10px;">
					<div style="width:510px;height:120px;">
						<a class="zhidao_index_myfollow_img" href="javascript:openWenkuTab();"></a>
						<a class="zhidao_index_my_ask_img" target="_self" href="javascript:openMyAsk();" style="margin-right:10px;"></a>
						<a class="zhidao_index_my_answer_img" style="margin-right:0px;" target="_self" href="javascript:openAnswer();"></a>
					</div>
					<div style="width:510px;height:120px;margin-top:10px;margin-right:10px;">
						<a class="zhidao_index_spec_img" href="<@tmplUrl specialist_tmpl_id />"></a>
						<a class="zhidao_index_center_img" href="javascript:myZhiDaoCenter('${userZhidao_center_tmpl_id}');" target="_self"></a>
					</div>
				</div>
				<div class="wenku_index_upload_img" target="_self">
						<span id="upload_document_counter">出题<span id="ask_num"></span>&nbsp;/回复<span id="answer_num"></span></span>
						<span id="zhidao_index_font"></span>
						<a class="zhidao_my_want_ask" href="javascript:loadTiWen();"></a>
				</div>
				<script>
					//页面加载完成后查询出所有已解答的问题数量
					jQuery(document).ready(function(){
						//查询所有的问题个数
						var questionCount = parent.FireFly.doAct("SY_COMM_ZHIDAO","getAskCount",{},false)["_OKCOUNT_"];
						var answerCount = parent.FireFly.doAct("SY_COMM_ZHIDAO","getAnswerCount",{},false)["_OKCOUNT_"];
						jQuery("#ask_num").text(questionCount);
						jQuery("#answer_num").text(answerCount);
					});
				</script>
			</div>
			<div id="main-content" class="main-content-css" style="height:290px;">
				<div class="info-left-content info-left-content-css" style="width:900px;margin-right:12px;height:335px;">
					<div id="push-login" class=" push-login-css" style="margin-top:20px;">
						<div class="content">
							<div class="set-bar documentCenter" style="height:30px;">
								<span class="wenku_index_pen"></span>
								<span class="documentCenter_font">与我有关主题</span>
								<span class="js-follow documentHotFont documentHotFontSelect" num="1">
									我提出的主题
									<i class="selectXiaoJianTou" style="left:53px;"></i>
								</span>
								<span id="my-follow-question" class="js-follow documentHotFont" num="2">
									我关注的主题
									<i class="selectXiaoJianTou" style="display:none;"></i>
								</span>
							</div> 
							<div id="my_ask_list" class="question-list" alog-group="push-data">
								<span id="my_ask_list">
								</span>
							</div>
							<div id="my_follow_list" class="question-list" alog-group="push-data" style="display:none">
								<span id="my_follow_list">
								</span>
							</div>
						</div>
					</div>
				</div>
				<!------------热点推荐-------结束------------------>
				<div class="content-right" style="width:246px;border:1px solid #E5E5E5;margin-top:20px; height:313px;">
					<div id="user-info" style="width:auto;margin:10px 10px 30px 10px;">
						<div class="user" style="margin-top:15px;height:auto;overflow:hidden;">
							<div class="user-img" style="margin-left:5px;">
								<img id="imgUrl" style="width:auto; height:70px;" src='/sy/theme/default/images/common/rh-lady-icon.png'>
							</div>
							<div style="width:120px;float:left; margin-left:15px;">
								<h3 id="js-current-name"></h3>
								<a href="javascript:myZhiDaoCenter(center_temp_id);" style="font-size:12px;padding-left:0;" class="zm-side-nav-link"> <i class="zg-icon zg-icon-sidenav-find"></i> 我的积分&nbsp;<span id="integralCount"></span> </a>
							</div>
							<div class="user-msg user-msg-css" style="margin-left:0;">
							  <ul id="zg-sidebar-nav" style="overflow:hidden;">
						        <li class="zm-side-nav-li"> <a href="javascript:myZhiDaoCenter(center_temp_id);" style="font-size:12px;" class="zm-side-nav-link"> <i class="zg-icon zg-icon-sidenav-draft"></i> 出题&nbsp;<span id="askCount"></span> </a> </li>
						        <li class="zm-side-nav-li"> <a href="javascript:myZhiDaoCenter(center_temp_id);" style="font-size:12px;" class="zm-side-nav-link"> <i class="zg-icon zg-icon-sidenav-fav"></i> 回复&nbsp;<span id="answerCount"></span></a> </li>
						        <li class="zm-side-nav-li"> <a href="javascript:myZhiDaoCenter(center_temp_id);" style="font-size:12px;" class="zm-side-nav-link"> <i class="zg-icon zg-icon-sidenav-follow"></i> 关注了<span id="followCounter"></span>人 </a> </li>
						        <li class="zm-side-nav-li"> <a href="javascript:myZhiDaoCenter(center_temp_id);" style="font-size:12px;" class="zm-side-nav-link"> <i class="zg-icon zg-icon-sidenav-invite"></i> 关注了<span id="quesFollowCounter"></span>主题 </a> </li>
						        <li class="zm-side-nav-li"> <a href="javascript:myZhiDaoCenter(center_temp_id);" style="font-size:12px;" class="zm-side-nav-link"> <i class="zg-icon zg-icon-sidenav-all"></i> 关注了<span id="categoryCount"></span>分类 </a> </li>
						        <li class="zm-side-nav-li"> <a href="javascript:myZhiDaoCenter(center_temp_id);" style="font-size:12px;" class="zm-side-nav-link"> <i class="zg-icon zg-icon-sidenav-plaza"></i> 被<span id="followedCounter"></span>人关注 </a> </li>
						        <li class="zm-side-nav-li"> <a href="javascript:myZhiDaoCenter(center_temp_id);" style="font-size:12px;" class="zm-side-nav-link"> <i class="zg-icon zg-icon-sidenav-debuts"></i> 赞同&nbsp;<span id="likeVoteCount"></span> </a> </li>
						        <!--<li class="zm-side-nav-li"> <a href="javascript:myZhiDaoCenter(center_temp_id);" style="font-size:12px;" class="zm-side-nav-link"> <i class="zg-icon zg-icon-sidenav-publicedit"></i>分享&nbsp;<span id="shareCount"></span></a> </li>-->
						      </ul>
							</div>
						</div>
						<div class="user-record" style="margin-top:0px;">
							<p class="help-num">新的一天从答题助人开始</p>
							<p class="help-more" style="margin-top:0px;"><a id="js-go-answer" target="_self" href="javascript:void(0);" style="text-decoration:none;" alog-alias="user-helpmore">去答题，开启知道之旅吧!</a></p>
						</div>
					</div>
					<script>
					/* 我的知道信息 */
				 	var result = parent.FireFly.doAct("SY_COMM_ZHIDAO","getMyInfo",{"S_FLAG":1},false,false);
					var user = result["USER"];
					var userIntegral = result["USER_INTEGRAL"];
					var quesCount = result["QUESTION_COUNT"];
					var answCount = result["ANSWER_COUNT"];
					var imgUrl = user.USER_IMG;
					if (imgUrl != undefined && imgUrl != '') {
						imgUrl = imgUrl.split("?")[0]+"?size=70x70&t="+(new Date()).getTime();
					}
					jQuery("#imgUrl").attr("src",imgUrl);
					var allCount = parent.FireFly.doAct("SY_COMM_ZHIDAO","getAllCount",{"userId":user.USER_CODE},false,false);
					jQuery("#askCount").text(allCount["askCount"]);
					jQuery("#answerCount").text(allCount["answerCount"]);
					jQuery("#followCounter").text(allCount["followCounter"]);
					jQuery("#quesFollowCounter").text(allCount["quesFollowCounter"]);
					jQuery("#categoryCount").text(allCount["categoryCount"]);
					jQuery("#followedCounter").text(allCount["followedCounter"]);
					jQuery("#integralCount").text(allCount["integralCount"]);
					jQuery("#likeVoteCount").text(allCount["likeVoteCount"]);
					jQuery("#shareCount").text(allCount["shareCount"]);
					jQuery("#js-current-name").text(user["USER_NAME"]);
					/* 我的知道信息 */
					</script>
				</div>
			</div>
			<div id="info-content">
				<div class="info-left info-left-css" style="margin-top:0px;">
					<!--------------------等待您来回复----开始--------------------->
					<div class="info-left-content info-left-content-css" style="width:900px;overflow:hidden;height:230px;">
						<div id="push-login" class=" push-login-css">
							<div class="content">
								<div class="set-bar documentCenter" style="height:30px;">
									<span class="wenku_index_pen"></span>
									<span class="documentCenter_font">等待您来回复</span>
									<span class="js-waiting documentHotFont documentHotFontSelect" num="1" style="width:123px;">
										全部主题
										<i class="selectXiaoJianTou"></i>
									</span>
									<span id="inviteMe" class="js-waiting documentHotFont" num="2" style="width:136px;">
										请我回复的主题
										<i class="selectXiaoJianTou" style="left:62px; display:none;"></i>
									</span>
									<div id="all-opt-sort" class="opt-sort"> 
							          	<!---不能点击sort-default---可以点击sort-ansnum------------->
								        <!--
								        <a class="sort-ansnum sort-normal" style="margin-right:5px;" id="all_question" href="javascript:void(0);">全部 </a>
								        <span style="visibility: visible;float:left;line-height:28px;" class="sort-pipe" id="pipe-after">|</span>
								        <a class="sort-ansnum sort-normal" style="margin-right:5px; margin-left:5px;" id="noAnswer_question" href="javascript:void(0);">零回复&nbsp;</a> 
								        -->
								        <a class="btn-refresh" id="refresh_question" style="margin-right:0;margin-top:4px;" href="javascript:void(0);" rel="nofollow"></a> 
							        </div>
							        <div id="invite-opt-sort" class="opt-sort" style="display:none;"> 
								        <!--
								        <a class="sort-ansnum sort-normal" style="margin-right:5px;" id="invite_question" href="javascript:void(0);">全部 </a>
								        <span style="visibility: visible;float:left;line-height:28px;" class="sort-pipe" id="invite-pipe-after">|</span>
								        <a class="sort-ansnum sort-normal" style="margin-right:5px; margin-left:5px;" id="invite-noAnswer_question" href="javascript:void(0);">零回复&nbsp;</a> 
								        -->
								        <a class="btn-refresh" id="invite-refresh_question" style="margin-right:0;margin-top:4px;" href="javascript:void(0);" rel="nofollow"></a> 
							        </div>
								</div>
								<!--<h3 class="zhidao_index_h3"></h3>-->
								<div id="question-list-div" class="question-list" alog-group="push-data">
									<@ask_list count="14" debugName="最新出题" noAnswer="${noAnswer!''}"> 
									<#list tag_list as ques>
									<dl data-qid="535450661" class="">
										<dt>
											<span class="red-mark-a"></span>
											<a title="${ques.Q_TITLE}" href="javascript:view('${ques.Q_ID}','${ques.Q_TITLE}');"
											target="_self">
												<#if ques.Q_TITLE?length gt 27>
													${ques.Q_TITLE[0..27]}...
												<#else>
													${ques.Q_TITLE}	
												</#if>
												
											</a>
											<span class="answer-num">
												${ques.Q_ANSWER_COUNTER}回复
											</span>
										</dt>
									</dl>
									</#list>
									</@ask_list>
								</div>
							</div>
						</div>
					</div>
					<!--------------------等待您来回复----开始--------------------->
					
					<!--------------------原页面头部最新动态----开始--------------------->
					<div class="info-left-content info-left-content-css" style="width:900px;">
						<div class="content-left" style="height:340px; border-right:0px;width:900px;">
							<div class="documentCenter" style="height:30px;">
								<span class="wenku_index_pen"></span>
								<span class="documentCenter_font">我关注的人</span>
							</div>
							<div id="my-follow-person" class="top-css" style="margin-top:10px;">
							</div>
							<div class="img-list-outside" style="margin-top:10px;">
								<div class="js-up-click up-css"></div>
								<div id="img-list" class="img-list-content">
								</div>
								<div class="js-down-click down-css"></div>
							</div>
						</div>
					</div>
				</div>
				<div class="info-right info-right-css" style="border: 1px solid #E5E5E5;">
					
					
					
					
					
					<div class="mod rank tabs ui-tabs-aside user rank3" style="background-color:white;border:0px;padding-left:0px;padding-right:0px;padding-top:0px;">
						<div class="hd topper" style="overflow:hidden;">
							<ul class="tabControl" style="margin-left:auto;margin-right:auto;border-top:1px solid #E5E5E5;border-left:1px solid #E5E5E5;border-right:1px solid #E5E5E5;width:214px;height:28px;">
								<li style="margin:0px;" class="item show current" num="1"><a style="color:black;">总积分</a></li>
								<li class="gap"></li>
								<li style="margin:0px;" class="item show" num="2"><a style="color:black;">本月</a></li>
								<li class="gap"></li>
								<li style="margin:0px;" class="item show" num="3"><a style="color:black;">本周</a></li>
							</ul>
						</div>
						<div class="bd" style="padding-left:30px; padding-right:30px;padding-top:10px;height:240px;">
							<ul class="tabContent clearfix">
								<li class="current show_1" id="content_1">
									<ul>
									<@zhidao_integralTop debugName="总积分排行" type="total" count="5"> 
									<#list tag_list as integral>
									<li>
										<dl style="padding-top:10px;padding-bottom:10px;">
											<dt class="rank-color">
												<div class="custom-left">
												   	 <a style="display:block;width:60px;height:60px;" rel="nofollow" href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${integral.USER.USER_CODE}')">
												        <!--<img src="/sy/theme/default/images/common/rh-lady-icon.png" data-original="<@setUserImg integral.USER/>" style="1px solid #e5e5e5" width="60px;" height="60px;">-->
												    	<@formatUserImg integral.USER.USER_CODE integral.USER '' '' '60px' '60px' />
												    </a>
												</div>
											</dt>
											<dd style="width:80px; float:left;">
												<div class="custom-right">
													<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${integral.USER.USER_CODE}')">${integral.USER_NAME}</a>
													<p style="font-size:12px;">积分：<strong style="color:red;">${integral.INTEGRAL_VALUE}</strong></p>
												</div>
											</dd>
										</dl>
									</li>
									</#list>
									</@zhidao_integralTop>
									</ul>
								</li>
								<li class="disabled show_2" id="content_2">
									<ul>
										<@zhidao_integralTop debugName="本月积分排行" type="month" count="5">
										<#list tag_list as integral>
										<li>
										   <dl style="padding-top:10px;padding-bottom:10px;">
												<dt class="rank-color">
													<div class="custom-left">
														   	 <a style="display:block;width:60px;height:60px;" rel="nofollow" href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${integral.USER.USER_CODE}');">
														        <!--<img src="/sy/theme/default/images/common/rh-lady-icon.png" data-original="<@setUserImg integral.USER/>" style="1px solid #e5e5e5" width="60px;" height="60px;">-->
														        <@formatUserImg integral.USER.USER_CODE integral.USER '' '' '60px' '60px' />
														    </a>
													</div>
												</dt>
												<dd style="width:80px; float:left;">
													<div class="custom-right">
															<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${integral.USER.USER_CODE}')">${integral.USER.USER_NAME}</a>
															<p style="font-size:12px;">积分：<strong style="color:red;">${integral.SCORE}</strong></p>
														</div>	
												</dd>
											 </dl>
											</li>
										</#list>
										</@zhidao_integralTop>
										</ul>
								 </li>
								 <li class="disabled show_3" id="content_3">
									<ul>
										<@zhidao_integralTop debugName="本周积分排行" type="week" count="5"> 
										<#list tag_list as integral>
										<li>
										   <dl style="padding-top:10px;padding-bottom:10px;">
												<dt class="rank-color">
													<div class="custom-left">
														   	 <a style="display:block;width:60px;height:60px;" rel="nofollow" href="javascript:askQuestion('${integral.USER.USER_CODE}');">
														        <img src="<@setUserImg integral.USER/>?size=60x60" style="height:60px;width:auto;">
														    </a>
													</div>
												</dt>
												<dd style="width:80px; float:left;">
													<div class="custom-right">
															<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${integral.USER.USER_CODE}')">${integral.USER_NAME}</a>
															<p style="font-size:12px;">积分：<strong style="color:red;">${integral.SCORE}</strong></p>
													</div>	
												</dd>
											 </dl>
											</li>
										</#list>
										</@zhidao_integralTop>
										</ul>
									</li>
							</ul>
						</div>
					</div>
				</div>
				
			</div>
		</div>
	</div>
	<#include "/SY_COMM_ZHIDAO/zhidao_footer.ftl">
</body>
<script>
	jQuery(document).ready(function(){
		//tab切换的图片加载事件
		jQuery(window).bind("scroll",function(){
			var scrollTop = window.pageYOffset  
			                || document.documentElement.scrollTop  
			                || document.body.scrollTop  
			                || 0;
			if(scrollTop >= 500){
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
<!---------------------控制外层滚动条-------开始------------------->
<script>
	//将当前iFrame中的Id设置成全局变量，因为在Tab中要用到它来定位高度
	var pid = $(window.top.document).find(".ui-tabs-selected").attr("pretabid");
	var windowName = $("#"+pid,window.top.document).children("iframe").attr("id");
	GLOBAL.setFrameId(windowName);
	jQuery(document).ready(function(){
		//Tab对象的方法，用来定位页面的高度
		//alert(GLOBAL.getFrameId());
		Tab.setFrameHei(10);
		Tab.setFrameHei();
	});
	
	jQuery(window).load(function(){
		var iframeId = GLOBAL.getFrameId(),
		    $parent = parent.jQuery("#"+iframeId).parent();
		parent.jQuery(".loading" , $parent).remove();
	});
</script>
</html>