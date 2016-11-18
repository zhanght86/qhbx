<!DOCTYPE html>
<html dropeffect="none" class=" no-touch no-mobile" lang="zh-CN">
<head style="background-color:#FFF!important;">
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<meta charset="utf-8">
	
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<#include "global.ftl"/>
	<#include "/SY_COMM_ZHIDAO/config_constant.ftl" />
	<title>软虹 - 知道</title>
	<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/baidu_style_files/module_fe8dd525.css">
	<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/baidu_style_files/module_c595d1cf.css">
	<link rel="stylesheet"	href="/sy/comm/zhidao/zhihu_style_files/z1303291140368123.css"	type="text/css" media="screen,print">
	<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/zhidao_content.css">
	
	<script type="text/javascript" src="/sy/comm/zhidao/zhihu_style_files/zhidao_comment.js"></script>
	<script type="text/javascript" src="/sy/comm/cms/js/zhidao.js"></script>
	<script type="text/javascript" src="/sy/comm/zhidao/js/zhidao_content.js"></script>
	<script type="text/javascript" src="/sy/base/frame/jquery.tmpl.js"></script>
	<!---------------返回顶部的Js/CSS------开始--------------->
	<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/zd-goto-top.css">
	<script type="text/javascript" src="/sy/comm/zhidao/js/zd-goto-top.js"></script>
	<!---------------返回顶部的Js/CSS------结束--------------->
	<!---------------知道分享的Js/CSS------开始--------------->
	<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/zhidao_share.css">
	<script type="text/javascript" src="/sy/comm/zhidao/js/zhidao_share.js"></script>
	<!---------------知道分享的Js/CSS------结束--------------->
	<!----------------举报的JS------------开始-------->
	<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/zhidao_report.css">
	<script type="text/javascript" src="/sy/comm/zhidao/js/zhidao_report.js"></script>
	<!----------------举报的JS------------结束-------->
	<!------------赞同、反对的样式--------开始------------------->
	<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/zhihu_style_files/c60843d5fb7d7aeb2bfa3619734d9b18.css">
	<!------------赞同、反对的样式--------结束------------------->
	<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/favorites.css">
	<script type="text/javascript" src="/sy/comm/zhidao/js/favorites.js"></script>
	<script>
		var Q_ID = "${data.Q_ID}" ,
		  userId="${data.S_USER}",
		  tempId = "${userZhidao_center_tmpl_id}",
		  Q_TITLE = "${data.Q_TITLE}",
		  anony = "${data.Q_ANONY}",
		  loginUserId=parent.System.getVar("@USER_CODE@");
	</script>
<#noparse>
<script id="js-related-questions-Template" type="text/x-jquery-tmpl">
	<li>
		<a class="question_link" href="javascript:view('${Q_ID}','${Q_TITLE}');">
			${parent.Format.limit(14,Q_TITLE)}
		</a>
		<span class="num">${Q_ANSWER_COUNTER} 个回复</span>
		<!--<span class="time">parent.$.timeago(parent.Format.substr(0,10,S_MTIME))</span>-->
	</li>
</script>
<script id="js-wait-answer-questions-Template" type="text/x-jquery-tmpl">
	<li>
		<a class="question_link" href="javascript:view('${Q_ID}','${Q_TITLE}');">
			${parent.Format.limit(14,Q_TITLE)}
		</a>
		<span class="num">${Q_ANSWER_COUNTER} 个回复</span>
	</li>
</script>
<script id="favorites-dialog-Template" type="text/x-jquery-tmpl">
    <div class="dialog-header">
		<span class="dialog-title">收藏</span>
	    <a href="#" class="js-dialog-close dialog-close">&times;</a>
	</div>
	<div class="dialog-body">
		<div class="form-horizontal">
	    	<div class="control-group">
			    <label class="control-label" for="title">标题</label>
			    <div class="controls">
			      <input type="text" id="title" class="input-xxlarge" placeholder="" value="${title}" />
			      <span class="notnull">*</span>
			    </div>
			</div>
			<div class="control-group">
			    <label class="control-label" for="tags">标签</label>
			    <div class="controls">
			      <input type="text" id="tags" class="input-xlarge" placeholder="">
			      <span>多个标签用空格分隔</span>
			    </div>
			</div>
			<div class="tag-group">
			    	{{each items}}
		    			<a href="#" class="item">${$value.MARK_NAME}</a>
		   			{{/each}}
			</div>
		</div>
		<div class="dialog-info">
		      <span>收藏成功</span>
		     <a href='javascript:void(0);' class="js-goto">去我的收藏夹</a>
		</div>
	</div>
	<div class="dialog-footer">
		<a href="#" class="js-dialog-save btn savebtn">保存</a>
		<a href="#" class="js-dialog-close btn closebtn">关闭</a>
	</div>
</script>
</#noparse>	
</head>

<body style="background-color:#FFF!important;">
	<div class="layout-center">
		<div class="layout-center-div">
			<div class="line"> </div>
			<div id="body" class="container" style="width:1200px;">
				<#--<#include "/SY_COMM_ZHIDAO/zhidao_navigation.ftl">-->
		  	</div>
		</div>
	</div>
	<div class="zu-global-notify" id="zh-global-message" style="display: none">
		<div class="zg-wrap">
			<div class="zu-global-nitify-inner">
				<a class="zu-global-notify-close" href="javascript:;" title="关闭" name="close">x</a> 
				<span class="zu-global-notify-icon"></span> 
				<span class="zu-global-notify-msg"></span>
			</div>
		</div>
	</div>
	<div class="zu-global-notify zu-global-notify-info"	id="zh-question-redirect-info" style="display: none"></div>
	<div class="zu-global-notify zu-global-notify-info"	id="zh-question-notification-summary" style="display: none;">
		<div class="zg-wrap">
			<div class="zu-global-nitify-inner">
				<span class="zu-global-notify-icon"></span>
				<ul class="zu-question-notify-wrap"></ul>
			</div>
		</div>
	</div>
	<div role="main" class="zg-wrap zu-main question-page" id="zh-single-question-page" style="width:1160px;margin:0 20px 20px 20px;">
		<div class="zu-main-content">
		
		
<!-----------------------------------------整个问题的content部分-------------开始-------------------------------------------------------->
			<div class="zu-main-content-inner" id="zh-single-question">
				<div id="zh-message-container" class="zg-info-message" style="display: none;"></div>
				
				<div class="zm-editable-status-normal" id="zh-question-title">
						
					<h2 class="zm-item-title zm-editable-content">
						<span class="zm-title-img bg-img"></span>
						<span id="js-qTitle-span">${data.Q_TITLE}</span>
						<#if data.S_USER!="1">
						<a class="zu-edit-button" name="questionEdit">
							<input type="hidden" name="editQ_ID" value="${data.Q_ID}">
							<input type="hidden" name="editQ_USER" value="${data.S_USER}">
							<!--<i class="zu-edit-button-icon"></i>修改--><#-- 首创先屏掉修改功能 -->
						</a>
						</#if>
						<script type='text/javascript'>
								
						</script>
					</h2>
				</div>
				<div id="zh-question-detail" class="zm-item-rich-text zm-editable-status-normal" data-resourceid="474145" data-action="/question/detail">
					<div id="ques-content" class="zm-editable-content">
						${data.Q_CONTENT}
					</div>
				</div>

				<div class="zm-item-meta zm-item-comment-el" id="zh-question-meta-wrap">
					<span class="answer-date-link-wrap meta-item"> 
						<#if data.S_ATIME?length gt 10> 
							${data.S_ATIME[0..10]}
						<#else> 
							${data.S_ATIME} 
						</#if> 
					</span>
					
					 <#if data.Q_COMMENT_COUNTER gt 0 >&nbsp;
						 <a id="ask-${data.Q_ID}" href="javascript:;" name="addcomment" class="comment meta-item">
							<i class="z-icon-comment"></i> ${data.Q_COMMENT_COUNTER}条评论
						 </a>
					 <#else>
						  <a id="ask-${data.Q_ID}" href="javascript:;" name="addcomment" class="comment meta-item"> 
							<i class="z-icon-comment"></i> 添加评论
						  </a> 
					</#if>
					<a href="javascript:void(0);" name="invite" class="meta-item">
						<i class="zg-icon zg-icon-sidenav-find" style="height:20px;vertical-align:bottom;"></i>					
						邀请回复
					</a>
					<span id="show_msg" style="font-size:20px; color:red;"></span>
					
					<!--
					<a href="javascript:void(0);" dataId="${data.Q_ID}" servId="SY_COMM_ZHIDAO_QUESTION" class="meta-item js-zhidao-share" name="share">
						<i class="z-icon-share"></i>分享
					</a>
					-->
					<!--
					<a href="javascript:void(0);" dataId="${data.Q_ID}" servId="SY_COMM_ZHIDAO_QUESTION" class="meta-item js-zhidao-report" name="report">
						<i class="z-icon-no-help"></i>举报
					</a>
					-->
					<a href="javascript:void(0);" dataId="${data.Q_ID}" disname="${data.Q_TITLE}" servId="SY_COMM_ZHIDAO_QUESTION" owner="${data.S_USER}" class="meta-item js-favorite">
						<i class="z-icon-collect"></i>收藏
					</a>
					<!-- <div class="favorite-dialog"></div> -->
					<div class="panel-container">
						<div class="question-invite-panel" style="display: none;"></div>
						<div id="comment-${data.Q_ID}" class="zm-comment-box" data-count="2"></div>
						
							<!--加载评论  -->
					   <script type="text/javascript">
					   bindAskComment('${data.Q_ID}','ask-${data.Q_ID}','comment-${data.Q_ID}');
						</script>

					</div>
				</div>
				
				
				<@zhidao_answer_list debugName="回复列表" id="${data.Q_ID}" sort="${sort}">	
				<div class="zh-question-answer-summary-wrap zm-item-rich-text" id="zh-question-answer-summary-wrap" style="display: none;">
					<h3>
						<a href="http://www.zhihu.com/question/20014415" target="_blank" class="zg-right zg-link-litblue" style="font-weight: normal">
							什么是答案总结？
						</a>
						答案总结
					</h3>
					<div id="zh-question-answer-summary" class="zg-section zm-editable-status-normal"
						data-resourceid="474145" data-action="/question/summary">
						<div style="display: none;" class="zm-editable-content">
							<a href="javascript:;" class="zu-edit-button" name="edit">
								<i class="zu-edit-button-icon"></i>修改
							</a>
						</div>
					</div>
				</div>
				<div id="zh-question-answer-wrap" class="zh-question-answer-wrapper navigable" data-widget="navigable"
					data-navigable-options="{&quot;items&quot;: &quot;&gt;.zm-item-answer&quot;}">
					<span id="zh-question-answer-insert-placeholder"></span>
				
					<#list tag_list as answer>
					
					<#if (answer.A_BEST!="1") && (answer.A_ADMIN_BEST!="1")>
						<div class="zh-answers-title clearfix js-more-other-answer" id="zh-question-filter-wrap">
							<div class="zh-answers-filter-div">
		
							<#if sort == 'A_BEST DESC,S_ATIME ASC'>					
							<div tabindex="0" id="zh-answers-filter" class="zh-answers-filter">
								<span class="lbl">按时间排序</span> <i
									class="zg-icon zg-icon-double-arrow"></i>
							</div>
							<div style="display: none;" class="zh-answers-filter-popup">
									<div tabindex="0" class="active" data-key="vote">
										<span class="lbl">按时间排序</span>
										<i class="zg-icon zg-icon-double-arrow"></i>
									</div>
								<div tabindex="0" class="selectable" data-key="added_time">
									<a href="/cms/SY_COMM_ZHIDAO_QUESTION/${data.Q_ID}.html?id=${id}">
										<span class="lbl">按票数排序</span>
									</a>	
										<i class="zg-icon zg-icon-double-arrow"></i>
								</div>
							</div>
							<#else>
		
						  <div tabindex="0" id="zh-answers-filter" class="zh-answers-filter">
								<span class="lbl">按票数排序</span> <i
									class="zg-icon zg-icon-double-arrow"></i>
							</div>
							<div style="display: none;" class="zh-answers-filter-popup">
									<div tabindex="0" class="active" data-key="vote">
										<span class="lbl">按票数排序</span>
										<i class="zg-icon zg-icon-double-arrow"></i>
									</div>
								<div tabindex="0" class="selectable" data-key="added_time">
									<a href="/cms/SY_COMM_ZHIDAO_QUESTION/${data.Q_ID}.html?id=${id}&sort=A_BEST DESC,A_ADMIN_BEST DESC,S_ATIME ASC">
										<span class="lbl">按时间排序</span>
									</a>	
										<i class="zg-icon zg-icon-double-arrow"></i>
								</div>
							</div>
		
		
							</#if>
							
							</div>
							<script type="text/javascript">
								$(".zh-answers-filter-div").hover(
								  function () {
								   		jQuery(".zh-answers-filter-popup").show();
								  },
								  function () {
								   	    jQuery(".zh-answers-filter-popup").hide();
								  }
								);
							</script>
							<h3 id="zh-question-answer-num">其它${tag_list?size-answer_index} 个回复</h3>
							
							
							
						</div>
					</#if>
					
					<div class="zm-item-answer zd-nomal-answer" index="${answer_index}" isbest="${answer.A_BEST}">
						<#if (answer_index==0 || answer_index==1) && (answer.A_BEST=="1") && (answer.A_ADMIN_BEST=="1")>
							<span class="best-answer answer-best-recommend bg-img" who="askerAndAdmin" title="出题者、管理员采纳"></span>
							<span class="zd-best-answer-tips" title="出题者、管理员采纳">出题者、管理员采纳</span>
							<div style="width:100%; height:50px;"></div>
						<#else>
							<#if (answer_index==0 || answer_index==1) && (answer.A_BEST=="1")>
								<span class="best-answer zd-best-answer bg-img" who="asker" title="出题者采纳"></span>
								<span class="zd-best-answer-tips" title="出题者采纳" style="left:15%;">出题者采纳</span>
								<div style="width:100%; height:50px;"></div>
							<#else>
								<#if (answer_index==0 || answer_index==1) && (answer.A_ADMIN_BEST=="1")>
									<span class="best-answer answer-recommend bg-img" who="admin" title="管理员采纳"></span>
									<span class="zd-best-answer-tips" title="管理员采纳" style="left:15%">管理员采纳</span>
									<div style="width:100%; height:50px;"></div>
								</#if>
							</#if>
						</#if>
						<a class="zg-anchor-hidden" name="${answer.A_ID}" id="${answer.A_ID}"></a>

						

						<div class="zm-item-vote">
							<a name="expand" class="zm-item-vote-count" href="javascript:;" data-votecount="9">9</a>
						</div>
						<div class="answer-head">
							<div class="zm-item-answer-author-info" style="position:relative;">
								<h3 class="zm-item-answer-author-wrap">
									<#if answer.A_ANONY == 1>
										<a class="zm-item-link-avatar"> 
											<img src="/sy/theme/default/images/common/rh-male-icon.png" class="zm-list-avatar">
										</a> 
										<a class="js-userName" title="匿名用户">匿名用户</a>	
									<#else>
										<a class="zm-item-link-avatar" style="width:35px;height:35px;" href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${answer.S_USER}')" target="_self">
											<#if answer.S_USER__IMG?length gt 0>
												<#list answer.S_USER__IMG?split("?") as src>
													<#if src_index == 0>
														<img src="${src}?size=35x35" class="zm-list-avatar" style="width:auto;height:35px;border-radius:0;">
													</#if>
												</#list>
											<#else>
												<img src="/sy/theme/default/images/common/rh-male-icon.png?size=35x35" style="width:auto;height:35px;border-radius:0;" class="zm-list-avatar">
											</#if>
										</a> 
										<a class="js-userName" href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${answer.S_USER}')"  title="${answer.S_USER__NAME}" target="_self">
											${answer.S_USER__NAME}
										</a>
										<strong title="${answer.S_DEPT__NAME}"	class="zu-question-my-bio">
											${answer.S_DEPT__NAME}
										</strong>
									</#if>
								</h3>
								<!-------箭头-------->
								<div class="zm-votebar">
									<button id="like-${answer.A_ID}" title="赞同" aria-pressed="false" class="up" style="float:left;">
										<span class="img"></span>赞同
									</button>
									<span style="float:left;background:none repeat scroll 0 0 #E7F3F9; height:22px; line-height:22px;border-radius:0px 3px 3px 0px;color:rgb(153,153,153); width:32px; text-align:center;">${answer.A_LIKE_VOTE}</span>
									<button id="unlike-${answer.A_ID}" title="反对，不会显示你的姓名"	aria-pressed="false" class="down" style="margin:0 0 0 5px; float:left;">
										<span class="img" style="left:32px;top:8px;"></span>反对，不会显示你的姓名
									</button>
									<span style="float:left;background:none repeat scroll 0 0 #E7F3F9; height:22px; line-height:22px;border-radius:0px 3px 3px 0px;color:rgb(153,153,153); width:32px; text-align:center;">${answer.A_DISLIKE_VOTE}</span>
								</div>
								<script type="text/javascript">
								//赞同按钮
									var likeBtn = jQuery("#like-${answer.A_ID}");
									likeBtn.bind("click", function() {
										var data = {};
										data["_PK_"] = '${answer.A_ID}';
										var likeVoteCount = ${answer.A_LIKE_VOTE};
										var resultData = parent.FireFly.doAct("SY_COMM_ZHIDAO_ANSWER", "increaseLikevote", data);
										if (resultData["_MSG_"] && resultData["_MSG_"].indexOf("OK") == 0) {
											var afterVote = Number(likeVoteCount) + 1;
//											Tip.show("操作成功!");
											alert("操作成功，感谢您的评价！");
											
											//更改赞同数
											jQuery(this).next().text(afterVote);
											//jQuery("#vote-${answer.A_ID}").find("span").first().text(afterVote + " 票");
										} else {
//											Tip.showError(resultData["_MSG_"], true);
											alert("您已经做过评价，不能重复操作！");
										}
									});							
									
									//反对按钮
									var unlikeBtn = jQuery("#unlike-${answer.A_ID}");
									unlikeBtn.bind("click", function() {
										var data = {};
										data["_PK_"] = '${answer.A_ID}';
										var unlikeVoteCount = ${answer.A_DISLIKE_VOTE};
										var resultData = parent.FireFly.doAct("SY_COMM_ZHIDAO_ANSWER", "increaseUnlikevote", data);
										if (resultData["_MSG_"] && resultData["_MSG_"].indexOf("OK") == 0) {
											var afterVote = Number(unlikeVoteCount) + 1;
//											Tip.show("操作成功!");
											alert("操作成功，感谢您的评价！");
											//更改反对数
											jQuery(this).next().text(afterVote);
										} else {
//											Tip.showError(resultData["_MSG_"], true);
											alert("您已经做过评价，不能重复操作！");
										}
									});
								</script>
							</div>
							<div id="vote-${answer.A_ID}" class="zm-item-vote-info " data-votecount="9">
								<span></span>
								<#if answer.A_LIKE_VOTE != 0>
									赞同，来自&nbsp;
								</#if>
								<@zhidao_vote_list debugName="赞成人列表" answerId="${answer.A_ID}" best="best">
									<#list tag_list as item>
										<#if item_index == 0>
											${item.VOTE_USER__NAME}
										<#else>
											<#if item_index == 5>
												<a href="javascript:void(0);" class="more_a">&nbsp;更多</a>
												<span style="display:none;">
											</#if>
											、${item.VOTE_USER__NAME}
										</#if>
									</#list>
									<#if tag_list?size gt 6>
										</span>
									</#if>
								</@zhidao_vote_list>
							</div>
						</div>
						<div class="zm-item-rich-text" data-resourceid="${answer.A_ID}" data-action="/answer/content">
							<div class="zm-editable-content clearfix">
								<span id="ans_con_img" style="font-size:14px;">${answer.A_CONTENT}</span>
								 <a class="zu-edit-button" name="answerEdit">								 	
									<input type="hidden" name="editA_ID" value="${answer.A_ID}">
									<input type="hidden" name="editS_USER" value="${answer.S_USER}">
									<i class="zu-edit-button-icon"></i>修改
								</a>
								 <a class="zu-edit-button" name="answerDel">								 	
									<input type="hidden" name="delA_ID" value="${answer.A_ID}">
									<input type="hidden" name="delS_USER" value="${answer.S_USER}">
									<i></i>删除
								</a>
							</div>
						</div>
						<div class="zm-item-meta zm-item-comment-el answer-actions clearfix">
							<span class="answer-date-link-wrap meta-item"> 
								<#if answer.S_ATIME?length gt 10> 
									${answer.S_ATIME[0..10]}
								<#else> 
									${answer.S_ATIME} 
								</#if> 
							</span>
								 <#if answer.A_COMMENT_COUNTER gt 0 >
								  	<a id="answer-${answer.A_ID}"  name="addcomment" class=" meta-item zu-question-answer-meta-comment"> 
								  	<i class="z-icon-comment"></i>${answer.A_COMMENT_COUNTER} 条评论 </a>
								 <#else>
								    <a id="answer-${answer.A_ID}"  name="addcomment" class=" meta-item zu-question-answer-meta-comment">
								    <i class="z-icon-comment"></i>添加评论 </a> 
								 </#if>
								 <!--
								 <a href="#" dataId="${answer.A_ID}" servId="SY_COMM_ZHIDAO_ANSWER" class="meta-item js-zhidao-share" name="share">
									 <i class="z-icon-share"></i>分享
								 </a>
								 -->
								 <!--
								 <a href="#" dataId="${answer.A_ID}" servId="SY_COMM_ZHIDAO_ANSWER" class="meta-item js-zhidao-report" name="report">
									 <i class="z-icon-no-help"></i>举报
								 </a>
								 -->
								 <#if answer.Q_FEEDBACK>
								 	<div class="zd-q-feedback">
										<span class="zd-green">出题者评价</span>
										<span> ${answer.Q_FEEDBACK}</span>
									</div>
								 </#if>
								 
								 <!-----------------管理员选为满意答案-------开始---------------------->
								 <#if isAdminBest != 1>
								 	<#if isAdminRole == 'true'>
								 		<a href="" class="js-admin-best-answer zg-btn-best-white zd-btn-best" data-id='${answer.A_ID}'>管理员选为满意答案</a>
								 	</#if>
								 </#if>
								 <!-----------------管理员选为满意答案-------结束---------------------->
								 
								 
								 <a href="" class="js-best-answer zg-btn-best-white zd-btn-best js-zd-btn-best" data-id='${answer.A_ID}'>选为满意答案</a>								  
								<div class="panel-container">
									<div class="question-invite-panel" style="display: none;"></div>
									<div id="comment-${answer.A_ID}" class="zm-comment-box" data-count="2"></div>								
									<!--加载评论  -->
									<script type="text/javascript">
										bindAnswerComment('${answer.A_ID}','answer-${answer.A_ID}','comment-${answer.A_ID}');
									</script>
								</div>
								<span class="copyright zu-autohide"></span>
						</div>
					</div>
					
					
					</#list>
				</@zhidao_answer_list>

				</div>					
				<div id="zh-question-collapsed-wrap" class="zh-question-answer-wrapper" style="display: none"></div>
				<a name="draft"></a>
				<div id="zh-question-answer-form-wrap" class="zh-question-answer-form-wrap" data-isabout="0">
					<div style="display: none;" class="zm-editable-tip zu-answer-form-disabled-wrap"></div>
					<div style="display: none;" class="zm-editable-content">
						<p></p>
						<a href="javascript:;" class="zu-edit-button" name="edit">
							<i class="zu-edit-button-icon"></i>修改
						</a>
					</div>
					<div style="" class="zm-editable-editor-wrap">					
					
					
					   <div id="current_user" class="zh-answer-form clearfix">
							<a class="zm-item-link-avatar" style="width:35px;height:35px;" >
								<img src="" class="zm-list-avatar" style="width:auto;height:35px;border-radius:0;">
							</a>
							<div>
								<div class="zu-answer-form-title">
									<a href="#" title=""></a>
									<span></span>
								</div>
							</div>
					   </div>
				 	
					
						<!-- 编辑器 -->
						<div id="editor"  style="width:680px;height:170px;" ></div>					
						<div style="display: none;" class="zm-command">
							<a class="zg-r3px zg-btn-blue" href="javascript:;">我要回复</a>
						</div>
						<div style="display: block;" class="zm-command clearfix">
							<div class="draft-controls zg-left">
								<span style="display: none;" class="draft-saved-info">
									<a	class="draft-clear-button goog-inline-block" href="#" data-tip="s$b$删除草稿">
										<i class="zg-icon zg-icon-bin"></i>
										<span class="hide-text">清除草稿</span>
									</a>
									<span class="draft-saved-time"></span>
								</span>
							</div>
							<!--<label style="cursor: pointer; -moz-user-select: none;">
								<input style="-moz-user-select: none;" name="anno-checkbox" type="checkbox">匿名
							</label>--> 
							<a class="submit-button zg-btn-blue" name="save" style="margin-right:120px;" >发布回复</a>
						</div>
					</div>
				</div>
				<!-- end of content -->
			</div>
<!-----------------------------------------整个问题的content部分-------------结束-------------------------------------------------------->			
			
			
			
			
			
		</div>
		<div class="zu-main-sidebar">
			<div class="zm-side-section">
				<div class="zd-profile" id="real-name" style="display:none;">
					<h3>出题者</h3>
				<@user debugName="用户信息" userId="${data.S_USER}">
					<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${data.S_USER}')" target="_self">
						<#if data.S_USER__IMG??>
							<#list data.S_USER__IMG?split("?") as src>
								<#if src_index == 0>
									<img src="${src}?size=60x60" class="zm-user-img" style="width:auto;">
								</#if>
							</#list>
						<#else>
							<img src="/sy/theme/default/images/common/rh-lady-icon.png?size=60x60" class="zm-user-img" style="width:auto;">
						</#if>
					</a>
					<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${data.S_USER}')" target="_self">
						<span class="zd-user-name">${data.S_USER__NAME}</span>
					</a>
					<div class="zd-user-info">${user.DEPT_NAME}</div>
					<div class="zd-user-info">${user.USER_POST}</div>
				</@user>
				</div>
				<div class="zd-profile" id="no-real-name" style="display:none;">
					<h3>出题者</h3>
					<a href="javascript:void(0);" target="_self">
						<img src="/sy/theme/default/images/common/rh-lady-icon.png?size=60x60" class="zm-user-img" style="width:auto;">
					</a>
					<a href="javascript:void(0);" target="_self">
						<span class="zd-user-name">匿名</span>
					</a>
					<div class="zd-user-info">保密</div>
					<div class="zd-user-info">保密</div>
				</div>
			</div>
			<div class="zm-side-section">
				<div class="zm-side-section-inner">
					<div class="zd-follow-container">
						<a class="zg-follow zg-btn-green zg-mr10" id="questionFllow">关注</a>
						<input type="hidden" id='fllowID'>
					</div>
					<div class="zh-question-followers-sidebar">
						<h3>
							<a><strong id="fllowUsersSize"></strong></a>
							人关注该主题
						</h3>
						<!--用户头像 -->
						<div class="list zu-small-avatar-list zg-clear" id="fllowUsers">

						</div>

					</div>
					<div aria-haspopup="true" role="menu"
						style="-moz-user-select: none; visibility: visible; left: 92px; top: 34px; display: none;"
						class="goog-menu goog-menu-vertical">
						<div id=":c" style="-moz-user-select: none;" role="menuitem"
							aria-checked="false" class="goog-menuitem goog-option">
							<div class="goog-menuitem-content">
								<div class="goog-menuitem-checkbox"></div>
								使用匿名身份
							</div>
						</div>
						<div id=":d" style="-moz-user-select: none;" role="menuitem"
							class="goog-menuitem">
							<div class="goog-menuitem-content">主题重定向</div>
						</div>
					</div>
				</div>
			</div>
			<div class="zm-side-section zm-side-list-content zm-side-pinned-topics">
				<ul>
					<li style="width:130px; font-size:12px; text-align:center; color:#999999; float:left;">${data.Q_READ_PERSON_COUNT}人浏览</li>
					<li style="width:130px; font-size:12px; text-align:center; color:#999999;">${data.Q_READ_COUNTER}次浏览</li>
					<li style="width:130px; font-size:12px; text-align:center; color:#999999; float:left;">${data.Q_COMMENT_COUNTER}条评论</li>
					<@zhidao_ques_share_count debugName="主题总分享数" qId="${data.Q_ID}">
					<li style="width:130px; font-size:12px; text-align:center; color:#999999;">${shareCount}次分享</li>
					</@zhidao_ques_share_count>
				</ul>
			</div>
			<div class="zm-side-section">
				<div class="zm-side-section-inner">
						<h3>其它类似主题</h3>
						<ul id="js-related-questions" class="zh-question-related-questions">
							 
						</ul>
					</div>
				</div>
				<div class="zm-side-section">
				<div class="zm-side-section-inner">
						<h3>等待您来回复</h3>
						<ul id="js-wait-answer-questions" class="zh-question-related-questions">
							 
						</ul>
					</div>
				</div>
			</div>
		
		<div class="zg-clear"></div>
		<div style="display: none;" class="mention-popup">
			<div class="writing-bg">
				<input autocomplete="off" aria-haspopup="true">
			</div>
			<div class="tip">想用 @ 提到谁？</div>
		</div>
		<div style="display: none;" class="mention-popup">
			<div class="writing-bg">
				<input autocomplete="off" aria-haspopup="true">
			</div>
			<div class="tip">想用 @ 提到谁？</div>
		</div>
		<div style="display: none;" class="mention-popup">
			<div class="writing-bg">
				<input autocomplete="off" aria-haspopup="true">
			</div>
			<div class="tip">想用 @ 提到谁？</div>
		</div>
	</div>

	<input name="_xsrf" value="5ddd9840a16c43e08cb78e24ce79ee2c" type="hidden">
	<div class="zh-backtotop" style="opacity: 0; display: none;">
		<div class="arrow"></div>
		<div class="stick"></div>
	</div>
	<div style="visibility: hidden; left: 714px; top: 11268px; display: none;" class="tooltip right" id="zh-tooltip">
		<div class="tooltip-arrow"></div>
		<div class="tooltip-inner">1 人觉得这个很赞</div>
	</div>

	<div></div>
	<div style="display: none;">
		<div class="lang-select collapsed">
			<div style="-moz-user-select: none;" class="title-button">选择语言</div>
			<i class="zg-icon zg-icon-double-arrow"></i>
			<div class="input-wrapper">
				<input aria-haspopup="true" class="filter-input zg-form-text-input" placeholder="搜索语言">
			</div>
		</div>
	</div>
	<div style="display: none;">
		<div class="lang-select collapsed">
			<div style="-moz-user-select: none;" class="title-button">选择语言</div>
			<i class="zg-icon zg-icon-double-arrow"></i>
			<div class="input-wrapper">
				<input aria-haspopup="true" class="filter-input zg-form-text-input" placeholder="搜索语言">
			</div>
		</div>
	</div>
	<div id="zd-dialog" class="zd-feedback" title="标题">
		<div class="zd-dialog-title">
			<div class="zd-dialog-btn-close">
				<a class="js-dialog-close"></a>
			</div>
			<span></span>
		</div>
		<div class="zd-dialog-body">
			<div class="zd-dialog-content">
				<p class="title">采纳成功!</p>
				<div class="line"></div>
				<p class="tips">向帮助了您的知道网友说句感谢的话吧!</p>
				<div class="editor">
					<textarea cols="1" rows="1" placeholder="谢谢!"></textarea>
					<a class="zd-dialog-btn-ths">感谢TA</a>
				</div>
			</div>
		</div>
	</div>
</div>	
</div>
<div class="favorite-dialog"></div>
<!---------------返回顶部的div------开始--------------->
<div id="zd-goto-top"></div>
<!---------------返回顶部的div------结束--------------->
<#include "/SY_COMM_ZHIDAO/zhidao_footer.ftl">
<script type="text/javascript" src="/sy/comm/cms/js/iframeAutoHeight.js"></script>
<#-- 判断当前问题是否应该显示出题人信息 -->
<script>
	jQuery(document).ready(function() {
		if (userId == loginUserId || anony == '2') {
			jQuery("#real-name").show();
			jQuery("#no-real-name").hide();
		} else {
			jQuery("#real-name").hide();
			jQuery("#no-real-name").show();
		};
	});
	jQuery(window).load(function() {
		//var bodyHei = jQuery("body").height();
		Tab.setFrameHei(bodyHei);
	});
</script>
</body>
</html>
