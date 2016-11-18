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
		<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/userzhidao_center.css">
		<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/zhidao_index.css">
		
		<script type="text/javascript" src="/sy/comm/zhidao/js/zhidao_index.js"></script>
		<script>
			var center_temp_id = '${userZhidao_center_tmpl_id}';
			var noAnswer = '${noAnswer!''}';
		</script>
		
		<!---------------返回顶部的Js/CSS------开始--------------->
		<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/zd-goto-top.css">
		<script type="text/javascript" src="/sy/comm/zhidao/js/zd-goto-top.js"></script>
		<!---------------返回顶部的Js/CSS------结束--------------->
	</head>
	<body class="layout-center">
		<div class='layout-center-div'>
		<div class="line">
			<!-- userbar -->
		</div>

		<#include "/SY_COMM_ZHIDAO/zhidao_search.ftl">
		<div id="body" class="container">
			<#include "/SY_COMM_ZHIDAO/zhidao_navigation.ftl">
			<script type="text/javascript">
				jQuery("#menu").find(".first").addClass("current");
			</script>
			<div id="sub-menu">
				<ul>
			
				</ul>
			</div>
			 
			 
			<div id="main-content" class="main-content-css">
				<div class="content-left" style="height:275px; border-right:0px;width:70%;">
					<div id="my-follow-person" class="top-css">
						
						<!---------------Template1---JS--------开始-------------->
						<!--
						<div class="top-content-css">
							<div class="top-content-img-css">
								<div class="top-content-inside-css">
									<img class="content-img" src="/sy/theme/default/images/common/user0.png" />
								</div>
								<div style="color:#666666;">
									<div style="font-size:18px;">张金喜</div>
									<div>事业二部/产品部</div>
									<div>程序员/软件员</div>
									<div>Java/JS</div>
								</div>
							</div>
							<div class="top-content-font-css">
								<dl>
									<div class="top-wenda-css wen-img-css" style="height:20px;width:20px;"></div><dd class="top-content-dd">传北京打车起步价上涨50%，钱都交到...</dd>
									<div class="top-wenda-css da-img-css" style="height:30px;width:20px;"></div><dt class="top-content-dt">北京市交通排堵保畅今年的工作方案发布，针对"打车难"，将推广电话叫车、网络订车服务模式；...</dt>
								</dl>
								<div style="height:30px;"></div>
								<dl>
									<div class="top-wenda-css wen-img-css" style="height:20px;width:20px;"></div><dd class="top-content-dd">传北京打车起步价上涨50%，钱都交到...</dd>
									<div class="top-wenda-css da-img-css" style="height:30px;width:20px;"></div><dt class="top-content-dt">北京市交通排堵保畅今年的工作方案发布，针对"打车难"，将推广电话叫车、网络订车服务模式；...</dt>
									<div class="top-wenda-css" style="height:20px; color:#999999; font-size:12px;">回答者：</div><dd style="color:#999999; font-size:12px;">张金喜</dd>
								</dl>
							</div>
						</div>
						-->
						<!---------------Template1---JS--------结束-------------->
					</div>
					<div class="img-list-outside">
						<div class="js-up-click up-css"></div>
						<div id="img-list" class="img-list-content">
						</div>
						<div class="js-down-click down-css"></div>
					</div>
					
				</div>
				<div class="content-middle"></div>
				<div class="content-right" style="width:28%;">
					
					<div id="user-info" style="width:auto;">
						<div class="user">
							<div class="user-img user-img-css">
									<img id="imgUrl" style="width:70px; height:70px;" src='/sy/theme/default/images/common/user0.png'>
							</div>
							<div style="width:120px;float:left; margin-left:40px;">
								<h3 id="js-current-name" style="text-align:center;"></h3>
								<a href="#" class="zm-side-nav-link"> <i class="zg-icon zg-icon-sidenav-find"></i> 我的积分&nbsp;<span id="integralCount"></span> </a>
							</div>
							<div class="user-msg user-msg-css">
							  <ul id="zg-sidebar-nav" class="zm-side-nav">
						        <li class="zm-side-nav-li"> <a href="#" class="zm-side-nav-link"> <i class="zg-icon zg-icon-sidenav-draft"></i> 提问&nbsp;<span id="askCount"></span> </a> </li>
						        <li class="zm-side-nav-li"> <a href="#" class="zm-side-nav-link"> <i class="zg-icon zg-icon-sidenav-fav"></i> 回答&nbsp;<span id="answerCount"></span></a> </li>
						        <li class="zm-side-nav-li"> <a href="#" class="zm-side-nav-link"> <i class="zg-icon zg-icon-sidenav-follow"></i> 关注了<span id="followCounter"></span>人 </a> </li>
						        <li class="zm-side-nav-li"> <a href="#" class="zm-side-nav-link"> <i class="zg-icon zg-icon-sidenav-invite"></i> 关注了<span id="quesFollowCounter"></span>问题 </a> </li>
						        <li class="zm-side-nav-li"> <a href="#" class="zm-side-nav-link"> <i class="zg-icon zg-icon-sidenav-all"></i> 关注了<span id="categoryCount"></span>分类 </a> </li>
						        <li class="zm-side-nav-li"> <a href="#" class="zm-side-nav-link"> <i class="zg-icon zg-icon-sidenav-plaza"></i> 被<span id="followedCounter"></span>人关注 </a> </li>
						        <li class="zm-side-nav-li"> <a href="#" class="zm-side-nav-link"> <i class="zg-icon zg-icon-sidenav-debuts"></i> 赞同&nbsp;<span id="likeVoteCount"></span> </a> </li>
						        <!--
						        <li class="zm-side-nav-li"> <a href="#" class="zm-side-nav-link"> <i class="zg-icon zg-icon-sidenav-find"></i> 感谢<span>XX</span> </a> </li>
						        -->
						        <li class="zm-side-nav-li"> <a href="#" class="zm-side-nav-link"> <i class="zg-icon zg-icon-sidenav-publicedit"></i>分享&nbsp;<span id="shareCount"></span></a> </li>
						      </ul>
							</div>
						</div>
						<div class="user-record">
							<!--
							<div class="ask-reply-button">
								<div class="button-left">
									
								</div>
								<div class="button-center">
									
								</div>
								<div class="button-right">
								</div>
							</div>
							-->
							<p class="help-num">新的一天从答题助人开始</p>
							<p class="help-more" style="margin-top:0px;"><a id="js-go-answer" target="_self" href="javascript:void(0);" alog-alias="user-helpmore">去答题，开启知道之旅吧!</a></p>
						</div>
					</div>
			<!-- my zhidao info -->
								<script>
								/* 我的知道信息 */
							 	var result = parent.FireFly.doAct("SY_COMM_ZHIDAO","getMyInfo",{"S_FLAG":1},false,false);
								var user = result["USER"];
								var userIntegral = result["USER_INTEGRAL"];
								var quesCount = result["QUESTION_COUNT"];
								var answCount = result["ANSWER_COUNT"];
								var imgUrl = user.USER_IMG;
								jQuery("#imgUrl").attr("src",imgUrl);
								
								var allCount = parent.FireFly.doAct("SY_COMM_ZHIDAO","getAllCount",{"userId":user.USER_CODE},false,false);
								
								//console.debug(allCount);
								
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
								
								//用户
								/*var userImg = $(".user-img");
								var imgBtn = jQuery("<a alog-alias='user-info-name'></a>").appendTo(userImg);
								jQuery("<img src='" + user.USER_IMG + "'>").appendTo(imgBtn);*/
								
								//var userMsg = $(".user-msg");
								//var p1 =jQuery("<p></p>").appendTo(userMsg);
								//var userName = jQuery("<a alog-alias='user-info-name' href='#' class='user-name' title='test_user'>").appendTo(p1);
								//userName.text(user.USER_NAME);
								
								
								
								
							    //积分
							    /*var integral = jQuery("<li class='wealth'></li>").appendTo($(".userData"));
							    var integralBtn = jQuery("<a href='#' target='_self'></a>").appendTo(integral);
							    integralBtn.append("<b>" + userIntegral + "</b><br> <span>积分值</span>");
							    integralBtn.bind("click", function() {
							    	myWenkuIntegral();
						        });*/
							    
								//知道统计
								/*var myZhidao = jQuery(".button-center");
								var myAsk = jQuery("<a alog-alias='user-ask' href='javascript:openMyAsk();' target='_self'>").appendTo(myZhidao);
								jQuery("<p class='center-num'>" + quesCount +"</p>").appendTo(myAsk);
								jQuery("<p class='center-info'>我的提问</p>").appendTo(myAsk);
								
								jQuery("<span></span>").appendTo(myZhidao);
								
								var myAnswer = jQuery("<a alog-alias='user-answer' href='javascript:openAnswer();' target='_self'>").appendTo(myZhidao);
								jQuery("<p class='center-num'>" + answCount + "</p>").appendTo(myAnswer);
								jQuery("<p class='center-info'>我的回答</p>").appendTo(myAnswer);*/
							 
							    
								/* 我的知道信息 */
								</script>
								
					
					
					
				</div>
			</div>
			<div id="info-content">
				<!---------------主页面左半部分-------开始------------------------->
				<div class="info-left info-left-css">
					<!--------------------等待您来回答----开始--------------------->
					<div class="info-left-content info-left-content-css">
						<div id="push-login" class=" push-login-css">
							<h3 class="zhidao_index_h3">
								等待您来回答
							</h3>
							<a name="key" class="anchor">
							</a>
						
							<div class="content">
								<div class="set-bar">
									<span class="js-waiting default selected" num="1">全部问题</span>
									<span id="inviteMe" class="js-waiting default" num="2">请我回答的问题</span>
									<div id="all-opt-sort" class="opt-sort"> 
							          	<!---不能点击sort-default---可以点击sort-ansnum------------->
								        <a class="sort-ansnum sort-normal" style="margin-right:5px;" id="all_question" href="javascript:void(0);">全部 </a>
								        <span style="visibility: visible" class="sort-pipe" id="pipe-after">|</span>
								        <a class="sort-ansnum sort-normal" style="margin-right:5px; margin-left:5px;" id="noAnswer_question" href="javascript:void(0);">零回答&nbsp;</a> 
								        <a class="btn-refresh" id="refresh_question" style="margin-right:0;" href="javascript:void(0);" rel="nofollow"></a> 
							        </div>
							        <div id="invite-opt-sort" class="opt-sort" style="display:none;"> 
								        <a class="sort-ansnum sort-normal" style="margin-right:5px;" id="invite_question" href="javascript:void(0);">全部 </a>
								        <span style="visibility: visible" class="sort-pipe" id="invite-pipe-after">|</span>
								        <a class="sort-ansnum sort-normal" style="margin-right:5px; margin-left:5px;" id="invite-noAnswer_question" href="javascript:void(0);">零回答&nbsp;</a> 
								        <a class="btn-refresh" id="invite-refresh_question" style="margin-right:0;" href="javascript:void(0);" rel="nofollow"></a> 
							        </div>
								</div>
								<!--<h3 class="zhidao_index_h3"></h3>-->
								<div id="question-list-div" class="question-list" alog-group="push-data">
									<@ask_list count="10" debugName="最新提问" noAnswer="${noAnswer!''}"> 
									<#list tag_list as ques>
									<dl data-qid="535450661" class="">
										<dt>
											<a title="${ques.Q_TITLE}" href="javascript:view('${ques.Q_ID}','${ques.Q_TITLE}');"
											target="_self">
												<#if ques.Q_TITLE?length gt 36>
													${ques.Q_TITLE[0..36]}...
												<#else>
													${ques.Q_TITLE}	
												</#if>
												
											</a>
											<span class="answer-num">
												${ques.Q_ANSWER_COUNTER}回答
											</span>
										</dt>
									</dl>
									</#list>
									</@ask_list>
								</div>
							</div>
						</div>
					</div>
					<!--------------------等待您来回答----开始--------------------->
					
					<!-----------------------------三版-------我关注的--------------开始---------------------------->
					<!--------------------等待您来回答----开始--------------------->
					<div class="info-left-content info-left-content-css">
						<div id="push-login" class=" push-login-css" style="margin-top:20px;">
							<h3 class="zhidao_index_h3">
								我关注的
							</h3>
							<a name="key" class="anchor">
							</a>
							<div class="content">
								<div class="set-bar">
									<span class="default js-follow selected" num="1">我提出的问题</span>
									<span class="default js-follow" num="2">我关注的问题</span>
								</div> 
								<!--------------我提出的问题------开始---------------------->
								<div id="my_ask_list" class="question-list" alog-group="push-data">
									<span id="my_ask_list">
									</span>
								</div>
								<!--------------我提出的问题------结束---------------------->
								
								
								<!--------------我关注的问题------开始---------------------->
								<div id="my_follow_list" class="question-list" alog-group="push-data" style="display:none">
									<span id="my_follow_list">
									</span>
								</div>
							</div>
						</div>
					</div>
					<!--------------------等待您来回答----开始--------------------->
					<!-----------------------------三版-------我关注的--------------结束---------------------------->
					
					
					
					<!-----------------------------三版-------热点推荐--------------开始---------------------------->
					<!------------热点推荐-------开始------------------>
					<div class="info-left-content info-left-content-css">
						<div id="push-login" style="margin-top:20px;width:650px;">
							<h3 class="zhidao_index_h3">
								热点推荐
							</h3>
							<a name="key" class="anchor">
							</a>
						
							<div class="content" style="height:470px;">
								<div class="set-bar">
									<span class="default js-share selected" num="1">知道热点</span>
									<span class="default js-share" num="2">同事分享</span>
								</div>
								
								<!--------------知道热点-------开始---------->
								<div id="hot-div" class="question-list" alog-group="push-data">
									<@ask_list count="10" debugName="知道热点" order="Q_COMMENT_COUNTER DESC"> 
									<#list tag_list as ques>
									<dl data-qid="535450661" class="">
										<dt>
											<a title="${ques.Q_TITLE}" href="javascript:view('${ques.Q_ID}','${ques.Q_TITLE}');"
											target="_self">
												
												<#if ques.Q_TITLE?length gt 20>
																${ques.Q_TITLE[0..20]}...
																<#else>
																${ques.Q_TITLE}	
												</#if>
												
											</a>
											<span class="answer-num">
												<span class="answer-num-span">${ques.Q_ANSWER_COUNTER}回答</span>
												<span class="answer-num-span">XXX人浏览</span>
												<span class="answer-num-span">${ques.Q_READ_COUNTER}次浏览</span>
												<span class="answer-num-span">${ques.Q_COMMENT_COUNTER}条评论</span>
											</span>
										</dt>
									</dl>
									</#list>
									</@ask_list>
								</div>
								<!--------------知道热点-------结束---------->
								
								<!--------------同事分享-------开始---------->
								<div id="zh-pm-detail-item-wrap" class="question-list navigable" alog-group="push-data" style="display:none;border-top:0px;">
									<@zhidao_workmate_share count="5" debugName="同事分享">
									<#if (shareList?size == 0)>
										<h3 style='height:70px;line-height:70px;margin-left:250px;'>抱歉!现在没有同事向您分享!</h3>
									<#else>
										<#list shareList as share>
										<!--------------知乎的分享-----开始------------------->
										<div class="zm-pm-item"> 
											<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${share.S_USER}');" class="zm-item-link-avatar50"> 
												<img src="${share.S_USER__IMG}" class="zm-item-img-avatar50"> 
											</a>
										  	<div class="zm-pm-item-main">
												<div style="display:none" class="pm-touser"></div>
												<a class="pm-touser" id="pmto-1413024100" href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${share.S_USER}');" title="${share.S_USER__NAME}">${share.S_USER__NAME}</a> ：我想和你分享：
													${share.SHARE_CONTENT} 
												<#if share.SERV_ID == 'SY_COMM_ZHIDAO_ANSWER'>
													<a rel="nofollow" target="_self" class=" external" href="javascript:viewAnswer('${share.Q_ID}','${share.SHARE_CONTENT}','${share.DATA_ID}');">
												<#else>
													<a rel="nofollow" target="_self" class=" external" href="javascript:view('${share.Q_ID}','${share.SHARE_CONTENT}');">
												</#if>
													<span class="invisible">http://</span>
													<span class="visible">cms/tmpl/</span>
													<span class="invisible">${share.DATA_ID}.html</span>
													<span class="ellipsis"></span>
													<i class="icon-external"></i>
												</a> 
											</div>
										  	<div class="zg-gray zu-pm-item-meta"> 
												<span class="zg-gray zg-left timeago">${share.S_ATIME?substring(0,19)}</span>
												<a name="report" href="javascript:;" class="zg-link-litblue zu-autohide">举报</a> 
										  		<span class="zg-bull zu-autohide">|</span> 
										  		<a name="reply" href="javascript:;" class="zg-link-litblue zu-autohide">回复</a> 
										  		<span class="zg-bull zu-autohide">|</span> 
										  		<a name="delete" href="javascript:;" class="zg-link-litblue zu-autohide">删除</a>  
											</div>
										</div>
										<!--------------知乎的分享-----结束------------------->
										</#list>
									</#if>
									</@zhidao_workmate_share>
								</div>
								<!--------------同事分享-------结束---------->
								
								
								
								
							</div>
						</div>
					</div>
					<!------------热点推荐-------结束------------------>
					<!-----------------------------三版-------热点推荐--------------结束---------------------------->
				</div>
				
				<!--------------右半部分------------------>
				<div class="info-right info-right-css">
					<!------------公告区-----开始---------------->
					<div id="announcement">
						<h3>
							<a alog-alias="announ-more" rel="nofollow" href="#" style="margin-right:20px;"
							target="_self">
								更多
							</a>
							公告区
						</h3>
						<ul alog-group="hm-gg-title" class="zhidao_index_ul">
							<@zhidao_notice_list debugName="知道公告" count="16"> 
								<#list tag_list as notice>
								<li>
									<#if notice_index gt 1>
										<a target="_self"
										 onclick="javascript:openNotice('${notice_tmpl_id}','${notice.NOTICE_ID}');"
										 style="color:#090;" rel="nofollow" href="#">
									<#else>
										<a target="_self"
										 onclick="javascript:openNotice('${notice_tmpl_id}','${notice.NOTICE_ID}');"
										 style="color:#E86321;" rel="nofollow" href="#">
									</#if>
										${notice.NOTICE_TITLE}
									</a>
								<#if notice_index == 0>
									<span></span>
								</#if>
								</li>
								</#list>
							</@zhidao_notice_list>	
						</ul>
					</div>
					<!------------公告区-----结束---------------->
					
					<!------------优秀专家排行-----开始---------------->
					<div class="rank-block" id="expert-rank" style="width:100%; margin-top:20px;">
						<h3>
							专家最新知道
						</h3>
						<a class="more-expert" href="<@tmplUrl specialist_tmpl_id />">
							更多
						</a>
						<br />	
						<div id="threeSpec">
							<@zhidao_three_spec debugName="专家最新知道">
							<!--------------公司领导-------------------->
							<div id="leader" class="recommend-user" style="width:250px; float:left;">
								<div class="inner" style="height:110px;">
							      <div class="inner-left">
							        <div class="leader-logo"></div>
							        <a style="width:80px; height:80px;display:block;" data-img="#" target="_self" href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${threeSpec.leader.S_USER}')" rel="nofollow" alog-action="famous-img">
							        	<@formatUserImg threeSpec.leader '' '' '' '' '65px' '65px' />
							        </a>
							      </div>
							      <div class="inner-right" style="width:100px;">
							      	<h1 style="height:40px;width:100%;font-size:14px; font-weight:700; color:rgb(51,51,51);">${threeSpec.leader.S_USER__NAME}</h1>
							      	<div>${threeSpec.leader.S_DEPT_NAME_1}</div>
							      	<div>${threeSpec.leader.S_DEPT_NAME_2}</div>
							      </div>
							    </div>
							    <div style="height:auto;">
							    	<div class="ask_font"></div><div threeQID='${threeSpec.leader.Q_ID}'>${threeSpec.leader.Q_TITLE}</div>
							    	<div class="answer_font"></div><div>${threeSpec.leader.A_CONTENT}</div>
							    </div>
							</div>
							
							<!--------------职能部门-------------------->
							<div id="dept" class="recommend-user" style="width:250px; float:left;">
								<div class="inner" style="height:110px;">
							     <div class="inner-left">
							        <div class="dept-logo"></div>
							        <a data-img="#" target="_self" href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${threeSpec.dept.S_USER}')" rel="nofollow" alog-action="famous-img">
							        	<@formatUserImg threeSpec.dept '' '' '' '' '65px' '65px' />
							        </a>
							      </div>
							      <div class="inner-right" style="width:100px;">
							      	<h1 style="height:40px;width:100%;font-size:14px; font-weight:700; color:rgb(51,51,51);">${threeSpec.dept.S_USER__NAME}</h1>
							      	<div>${threeSpec.dept.S_DEPT_NAME_1}</div>
							      	<div>${threeSpec.dept.S_DEPT_NAME_2}</div>
							      </div>
							    </div>
							    <div style="height:auto;">
							    	<div class="ask_font"></div><div threeQID='${threeSpec.dept.Q_ID}'>${threeSpec.dept.Q_TITLE}</div>
							    	<div class="answer_font"></div><div>${threeSpec.dept.A_CONTENT}</div>
							    </div>
							</div>
							
							<!--------------业务专家-------------------->
							<div id="busi" class="recommend-user" style="width:250px; float:left;">
								<div class="inner" style="height:110px;">
							     <div class="inner-left">
							        <div class="busi-logo"></div>
							        <a data-img="#" target="_self" href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${threeSpec.busi.S_USER}')" rel="nofollow" alog-action="famous-img">
							        	<@formatUserImg threeSpec.busi '' '' '' '' '65px' '65px' />
							        </a>
							      </div>
							      <div class="inner-right" style="width:100px;">
							      	<h1 style="height:40px;width:100%;font-size:14px; font-weight:700; color:rgb(51,51,51);">${threeSpec.busi.S_USER__NAME}</h1>
							      	<div>${threeSpec.busi.S_DEPT_NAME_1}</div>
							      	<div>${threeSpec.busi.S_DEPT_NAME_2}</div>
							      </div>
							    </div>
							    <div style="height:auto;">
							    	<div class="ask_font"></div><div threeQID='${threeSpec.busi.Q_ID}'>${threeSpec.busi.Q_TITLE}</div>
							    	<div class="answer_font"></div><div>${threeSpec.busi.A_CONTENT}</div>
							    </div>
							</div>
							</@zhidao_three_spec>
						</div>						
					</div>
					<!------------优秀专家排行-----结束---------------->
					<!----------------积分排行-----------开始----------------->
					<div class="mod rank tabs ui-tabs-aside user rank3" style="background-color:white;border:0px;padding-left:0px;">
						<h3 style="color:#333333; font:500 14px/16px '微软雅黑';">积分排行</h3>
						<div class="hd topper">
							<ul class="tabControl">
								<li class="current show" num="1">
									<a>总积分</a>
								</li>
								<li class="disabled show" num="2">
									<a>本月</a>
								</li>
								<li class="disabled show" num="3">
									<a>本周</a>
								</li>
							</ul>
						</div>
						<div class="bd">
							<ul class="tabContent clearfix">
								<li class="current show_1" id="content_1">
									<ul>
									<@zhidao_integralTop debugName="总积分排行" type="total" count="5"> 
									<#list tag_list as integral>
									<li>
										<dl style="padding-top:10px;">
											<dt class="rank-color">
												<div class="custom-left">
												   	 <a rel="nofollow" href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${integral.USER.USER_CODE}')">
												        <!--<img src="/sy/theme/default/images/common/rh-lady-icon.png" data-original="<@setUserImg integral.USER/>" style="1px solid #e5e5e5" width="60px;" height="60px;">-->
												    	<@formatUserImg integral.USER '' '' '60px' '60px' '45px' '45px' />
												    </a>
												</div>
											</dt>
											<dd style="width:80px; float:left;">
												<div class="custom-right">
													<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${integral.USER.USER_CODE}')">${integral.USER_NAME}</a>
													<p>积分：${integral.INTEGRAL_VALUE}</p>
												</div>
											</dd>
											<dd style="width:60px; float:left;">
												<div>
													<a alog-action="user-rank-help" href="#" onclick="javascript:loadTiWen('${integral.USER_NAME}','${integral.USER.USER_CODE}');" 
													class="btn btn-24-white fixed-ask">
														<em>
															<b style="padding:0 5px;">
																<span style="color:#589B00;">向TA求助</span>
															</b>
														</em>
													</a>
													<div style="height:10px; width:100%;"></div>
													<a class="zd-right zd-btn zd-btn-follow zd-rich-follow-btn zd-btn-unfollow" 
														name="focus" href="#">
														取消关注
													</a>
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
										   <dl style="padding-top:10px;">
												<dt class="rank-color">
													<div class="custom-left">
														   	 <a rel="nofollow" href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${integral.USER.USER_CODE}');">
														        <!--<img src="/sy/theme/default/images/common/rh-lady-icon.png" data-original="<@setUserImg integral.USER/>" style="1px solid #e5e5e5" width="60px;" height="60px;">-->
														        <@formatUserImg integral.USER '' '' '60px' '60px' '45px' '45px' />
														    </a>
													</div>
												</dt>
												<dd style="width:80px; float:left;">
													<div class="custom-right">
															<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${integral.USER.USER_CODE}')">${integral.USER.USER_NAME}</a>
															<p>积分：${integral.SCORE}</p>
														</div>	
												</dd>
												<dd style="width:60px; float:left;">
													<div>
														<a alog-action="user-rank-help" href="#" onclick="javascript:loadTiWen('${integral.USER_NAME}','${integral.USER.USER_CODE}');" 
															class="btn btn-24-white fixed-ask">
															<em>
																<b style="padding:0 5px;">
																	<span style="color:#589B00;">向TA求助</span>
																</b>
															</em>
														</a>
														<div style="height:10px; width:100%;"></div>
														<a class="zd-right zd-btn zd-btn-follow zd-rich-follow-btn zd-btn-unfollow" 
															name="focus" href="#">
															取消关注
														</a>
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
										   <dl style="padding-top:10px;">
												<dt class="rank-color">
													<div class="custom-left">
														   	 <a rel="nofollow" href="javascript:askQuestion('${integral.USER.USER_CODE}');">
														        <!--<img src="/sy/theme/default/images/common/rh-lady-icon.png" data-original="<@setUserImg integral.USER/>" style="1px solid #e5e5e5" width="60px;" height="60px;">-->
														    	<@formatUserImg integral.USER '' '' '60px' '60px' '45px' '45px' />
														    </a>
													</div>
												</dt>
												<dd style="width:80px; float:left;">
													<div class="custom-right">
															<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${integral.USER.USER_CODE}')">${integral.USER_NAME}</a>
															<p>积分：${integral.SCORE}</p>
													</div>	
												</dd>
												<dd style="width:60px; float:left;">
													<div>
														<a alog-action="user-rank-help" href="#" onclick="javascript:loadTiWen('${integral.USER_NAME}','${integral.USER.USER_CODE}');" 
														class="btn btn-24-white fixed-ask">
															<em>
																<b style="padding:0 5px;">
																	<span style="color:#589B00;">向TA求助</span>
																</b>
															</em>
														</a>
														<div style="height:10px; width:100%;"></div>
														<a class="zd-right zd-btn zd-btn-follow zd-rich-follow-btn zd-btn-unfollow" 
															name="focus" href="#">
															取消关注
														</a>
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
					<!---------------积分排行----结束---------------------->
				<!---------------主页面右半部分-------结束------------------------->
			</div>
		</div>
		<!---------------返回顶部的div------开始--------------->
		<div id="zd-goto-top"></div>
		<!---------------返回顶部的div------结束--------------->
		
		
		 <#include "/SY_COMM_ZHIDAO/zhidao_footer.ftl">
	</div>
<!------------------原来文库的三个tab页切换的功能-------开始------------------------------>
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
<!------------------原来文库的三个tab页切换的功能-------开始------------------------------>
</body>
</html>
