<!DOCTYPE html>
<html dropeffect="none" class=" no-touch no-mobile" lang="zh-CN">
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<meta charset="utf-8">
	
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<#include "global.ftl"/>
	<#include "/SY_COMM_ZHIDAO/config_constant.ftl" />
	<title>软虹 - 知道</title>
	<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/baidu_style_files/module_fe8dd525.css">
	<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/baidu_style_files/module_c595d1cf.css">
	<link rel="stylesheet"	href="/sy/comm/zhidao/zhihu_style_files/z1303291140368123.css"	type="text/css" media="screen,print">
	
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
	<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/favorites.css">
	<script type="text/javascript" src="/sy/comm/zhidao/js/favorites.js"></script>
	<script>
		var Q_ID = "${data.Q_ID}" ,
		  userId="${data.S_USER}",
		  tempId = "${userZhidao_center_tmpl_id}";
	</script>
<#noparse>
<script id="js-related-questions-Template" type="text/x-jquery-tmpl">
	<li>
		<a class="question_link" href="javascript:view('${Q_ID}','${Q_TITLE}');">
			${parent.Format.limit(10,Q_TITLE)}
		</a>
		<span class="num">${Q_ANSWER_COUNTER} 个回答</span>
		<span class="time">${parent.$.timeago(parent.Format.substr(0,10,S_MTIME))}</span>
	</li>
</script>
<script id="js-wait-answer-questions-Template" type="text/x-jquery-tmpl">
	<li>
		<a class="question_link" href="#" href="javascript:view('${Q_ID}','${Q_TITLE}');">
			${parent.Format.limit(10,Q_TITLE)}
		</a>
		<span class="num">${Q_ANSWER_COUNTER} 个回答</span>
	</li>
</script>
<script id="favorites-dialog-Template" type="text/x-jquery-tmpl">
    <div class="dialog-header">
		<span class="dialog-title">收藏</span>
	    <a href="#" class="js-dialog-close dialog-close"></a>
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
		      <a href="" class="">去我的收藏夹</a>
		</div>
	</div>
	<div class="dialog-footer">
		<a href="#" class="js-dialog-save btn savebtn">保存</a>
		<a href="#" class="js-dialog-close btn closebtn">关闭</a>
	</div>
</script>
</#noparse>	
</head>

<body>
	<div class="layout-center">
		<div class="layout-center-div">
			<div class="line"> </div>
			<#include "/SY_COMM_ZHIDAO/zhidao_search.ftl">
			<div id="body" class="container">
				<#include "/SY_COMM_ZHIDAO/zhidao_navigation.ftl">
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
	<div role="main" class="zg-wrap zu-main question-page"
		id="zh-single-question-page">
		<div class="zu-main-content">
			<div class="zu-main-content-inner" id="zh-single-question">
				<div id="zh-message-container" class="zg-info-message" style="display: none;"></div>
				<!-- ask meta data -->
				
				<!----------------------我觉得没用--先给注释掉--开始-------------------------------->
				<!--   
				<@zhidao_ask debugName="提问" id="${id}">
				-->
				<!----------------------我觉得没用--先给注释掉--结束-------------------------------->
				<div class="zm-editable-status-normal" id="zh-question-title">
						<!-- <strong style='color: #259;font-weight: bold;' title="">
							<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${data.S_USER}')"
								target="_self">
								<img src="${data.S_USER__IMG}" class="zm-list-avatar">
							</a>
							&nbsp;
							<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${data.S_USER}')"
								target="_self">
								${data.S_USER__NAME}
							</a>
						</strong> -->
						
						<!------应该是部门信息-先注释掉---开始------->
						<!--
						<strong class="zu-question-my-bio">${data.S_DEPT}</strong>
						-->
						<!------应该是部门信息-先注释掉---结束------->
					<h2 class="zm-item-title zm-editable-content">
						<span class="zm-title-img bg-img"></span>
						<span id="js-qTitle-span">${data.Q_TITLE}</span>
						<#if data.S_USER!="1">
						<a class="zu-edit-button" name="questionEdit">
							<input type="hidden" name="editQ_ID" value="${data.Q_ID}">
							<input type="hidden" name="editQ_USER" value="${data.S_USER}">
							<i class="zu-edit-button-icon"></i>修改
						</a>
						</#if>
						<script type='text/javascript'>
								
						</script>
					</h2>
				</div>
				<div id="zh-question-detail" class="zm-item-rich-text zm-editable-status-normal" data-resourceid="474145" data-action="/question/detail">
					<div class="zm-editable-content">
						${data.Q_CONTENT}
					</div>
				</div>

				<div class="zm-item-meta zm-item-comment-el" id="zh-question-meta-wrap">
<!-- 
					<a href="javascript:;" name="report" class="report zg-right meta-item">
						 <i class="z-icon-no-help"></i>举报
					</a>
 -->					
					
					<span class="answer-date-link-wrap"> 
								<a class="answer-date-link meta-item" target="_self" >
									<#if data.S_ATIME?length gt 10> 
										${data.S_ATIME[0..10]}
									<#else> 
										${data.S_ATIME} 
									</#if> 
								</a>
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
					<span class="zg-bull">•</span> 
					<a href="javascript:void(0);" name="invite" class="meta-item">邀请回答</a>
					<span id="show_msg" style="font-size:20px; color:red;"></span>
					<a href="javascript:void(0);" dataId="${data.Q_ID}" servId="SY_COMM_ZHIDAO_QUESTION" class="meta-item js-zhidao-share" name="share">
						<i class="z-icon-share"></i>分享
					</a>
					<span class="zg-bull">•</span> 
					<a href="javascript:void(0);" dataId="${data.Q_ID}" disname="${data.Q_TITLE}" servId="SY_COMM_ZHIDAO_QUESTION" owner="${data.S_USER}" class="meta-item js-favorite">收藏</a>
					<div class="favorite-dialog"></div>
					<div class="panel-container">
						<div class="question-invite-panel" style="display: none;"></div>
						<div id="comment-${data.Q_ID}" class="zm-comment-box" data-count="2"></div>
						
							<!--加载评论  -->
					   <script type="text/javascript">
					   bindAskComment('${data.Q_ID}','ask-${data.Q_ID}','comment-${data.Q_ID}');
						</script>

					</div>
				</div>
				
				<!----------------------我觉得没用--先给注释掉--开始-------------------------------->
				<!--
				</@zhidao_ask>
				-->
				<!----------------------我觉得没用--先给注释掉--结束-------------------------------->
				
				<@zhidao_answer_list debugName="回答列表" id="${data.Q_ID}" sort="${sort}">	
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
							<h3 id="zh-question-answer-num">其它${tag_list?size-answer_index} 个回答</h3>
							
							
							
						</div>
					</#if>
					
					<div class="zm-item-answer zd-nomal-answer" index="${answer_index}" isbest="${answer.A_BEST}">
						<#if (answer_index==0 || answer_index==1) && (answer.A_BEST=="1") && (answer.A_ADMIN_BEST=="1")>
							<span class="best-answer answer-best-recommend bg-img" who="askerAndAdmin" title="提问者、管理员采纳"></span>
							<span class="zd-best-answer-tips" title="提问者、管理员采纳">提问者、管理员采纳</span>
							<div style="width:100%; height:50px;"></div>
						<#else>
							<#if (answer_index==0 || answer_index==1) && (answer.A_BEST=="1")>
								<span class="best-answer zd-best-answer bg-img" who="asker" title="提问者采纳"></span>
								<span class="zd-best-answer-tips" title="提问者采纳" style="left:15%;">提问者采纳</span>
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
										<a class="zm-item-link-avatar" href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${answer.S_USER}')" target="_self">
											<img src="${answer.S_USER__IMG}" class="zm-list-avatar">
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
									<button id="like-${answer.A_ID}" data-tip="s$r$赞同" aria-pressed="false" class="up" style="float:left;">
										<span class="img"></span>赞同
									</button>
									<span style="float:left;background:none repeat scroll 0 0 #E7F3F9; height:22px; line-height:22px;border-radius:0px 3px 3px 0px;color:rgb(153,153,153); width:32px; text-align:center;">${answer.A_LIKE_VOTE}</span>
									<button id="unlike-${answer.A_ID}" data-tip="s$r$反对，不会显示你的姓名"	aria-pressed="false" class="down" style="margin:0 0 0 5px; float:left;">
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
											Tip.show("操作成功!");
											
											jQuery("#vote-${answer.A_ID}").find("span").first().text(afterVote + " 票");
										} else {
											Tip.showError(resultData["_MSG_"], true);
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
											Tip.show("操作成功!");
										} else {
											Tip.showError(resultData["_MSG_"], true);
										}
									});
								</script>
							</div>
							<div id="vote-${answer.A_ID}" class="zm-item-vote-info " data-votecount="9">
								<span>${answer.A_LIKE_VOTE} 票</span>
								<#if answer.A_LIKE_VOTE != 0>
									，来自&nbsp;
								</#if>
								<@zhidao_vote_list debugName="赞成人列表" answerId="${answer.A_ID}" best="best">
									<#list tag_list as item>
										<#if item_index == 0>
											${item.VOTE_USER__NAME}
										<#else>
											<#if item_index == 2>
												<a href="javascript:void(0);" class="more_a">&nbsp;更多</a>
												<span style="display:none;">
											</#if>
											、${item.VOTE_USER__NAME}
										</#if>
									</#list>
									<#if tag_list?size gt 3>
										</span>
									</#if>
								</@zhidao_vote_list>
							</div>
						</div>
						<div class="zm-item-rich-text" data-resourceid="${answer.A_ID}" data-action="/answer/content">
							<div class="zm-editable-content clearfix">
								<span style="font-size:14px;">${answer.A_CONTENT}</span>
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
							<span class="answer-date-link-wrap"> 
								<a class="answer-date-link meta-item" target="_self" >
									<#if answer.S_ATIME?length gt 10> 
										${answer.S_ATIME[0..10]}
									<#else> 
										answer.S_ATIME 
									</#if> 
								</a>
							</span>
								 <#if answer.A_COMMENT_COUNTER gt 0 >
								  	<a id="answer-${answer.A_ID}"  name="addcomment" class=" meta-item zu-question-answer-meta-comment"> 
								  	<i class="z-icon-comment"></i>${answer.A_COMMENT_COUNTER} 条评论 </a>
								 <#else>
								    <a id="answer-${answer.A_ID}"  name="addcomment" class=" meta-item zu-question-answer-meta-comment">
								    <i class="z-icon-comment"></i>添加评论 </a> 
								 </#if>
								 <a href="#" dataId="${answer.A_ID}" servId="SY_COMM_ZHIDAO_ANSWER" class="meta-item zu-autohide js-zhidao-share" name="share">
									 <i class="z-icon-share"></i>分享
								 </a>
								 <#if answer.Q_FEEDBACK>
								 	<div class="zd-q-feedback">
										<span class="zd-green">提问者评价</span>
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
						
						<!-- 
								<a href="javascript:;" class="meta-item zu-autohide" name="thanks">
									<i class="z-icon-thank"></i>感谢
								</a>
						-->
						<!--
								<a href="javascript:;" class="meta-item zu-autohide fav" name="favo">
									<i class="z-icon-collect"></i>收藏
								</a> 
								<a href="javascript:;" class="meta-item zu-autohide" name="nohelp"> 
									<i class="z-icon-no-help"></i>没有帮助
								</a> 
						 -->		
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
							<a class="zm-item-link-avatar" >
								<img src="" class="zm-list-avatar">
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
							<a class="zg-r3px zg-btn-blue" href="javascript:;">我要回答</a>
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
							<label style="cursor: pointer; -moz-user-select: none;">
								<input style="-moz-user-select: none;" name="anno-checkbox" type="checkbox">匿名
							</label> 
							<a class="submit-button zg-btn-blue" name="save" >发布回答</a>
						</div>
					</div>
				</div>
				<!-- end of content -->
			</div>
		</div>
		<div class="zu-main-sidebar">
			<div class="zm-side-section">
				<div class="zd-profile">
				<h3>提问者</h3>
				<@user debugName="用户信息" userId="${data.S_USER}">
					<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${data.S_USER}')" target="_self">
						<img src="${data.S_USER__IMG}" class="zm-user-img">
					</a>
					<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${data.S_USER}')" target="_self">
						<span class="zd-user-name">${data.S_USER__NAME}</span>
					</a>
					<div class="zd-user-info">${user.DEPT_NAME}</div>
					<div class="zd-user-info">${user.USER_POST}</div>
				</@user>
				</div>
			</div>
			<div class="zm-side-section">
				<div class="zm-side-section-inner">
					<div class="zd-follow-container">
						<a class="zg-follow zg-btn-green zg-mr10" id="questionFllow">关注</a>
						<input type="hidden" id='fllowID'>
					</div>
					<div class="zh-question-followers-sidebar">
						<div class="zg-gray-normal">
							<a><strong id="fllowUsersSize"></strong></a>
							人关注该问题
						</div>
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
							<div class="goog-menuitem-content">问题重定向</div>
						</div>
					</div>
				</div>
			</div>
			<div class="zm-side-section zm-side-list-content zm-side-pinned-topics">
				<ul>
					<li style="width:130px; text-align:center; color:#999999; float:left;">XXX人浏览</li>
					<li style="width:130px; text-align:center; color:#999999;">XXX次浏览</li>
					<li style="width:130px; text-align:center; color:#999999; float:left;">XXX条评论</li>
					<li style="width:130px; text-align:center; color:#999999;">XXX次分享</li>
				</ul>
			</div>
			<div class="zm-side-section">
				<div class="zm-side-section-inner">
						<h3>其它类似问题</h3>
						<ul id="js-related-questions" class="zh-question-related-questions">
							 
						</ul>
					</div>
				</div>
				<div class="zm-side-section">
				<div class="zm-side-section-inner">
						<h3>等待您来回答</h3>
						<ul id="js-wait-answer-questions" class="zh-question-related-questions">
							 
						</ul>
					</div>
				</div>
			</div>
			<!--
			<div class="zm-side-section">
				<div class="zm-side-section-inner">
					<h3>分享问题</h3>
					<div id="zh-question-webshare-container" data-type="q"
						data-id="474145" data-url="/question/20734157"
						class="zh-question-webshare-links clearfix">
						 <a
							href="javascript:;" name="mail"><i
							class="zg-icon zg-icon-webshare-mail"></i>邮件</a> <a
							href="javascript:;" name="letter"><i
							class="zg-icon zg-icon-webshare-letter"></i>站内私信</a>
					</div>
					<div style="display: none">
						<div id="zh-webshare-dialog" class="zh-webshare-dialog">
							<ul class="zm-common-nav-bar zg-clear">
								<li class="zm-common-nav-bar-item">
									<a href="javascript:;" class="zm-common-nav-bar-link zm-common-nav-bar-current-light" id="tb-webshare-sina">微博分享</a>
								</li>
								<li class="zm-common-nav-bar-item">
									<a href="javascript:;" class="zm-common-nav-bar-link" id="tb-webshare-mail">邮件分享</a>
								</li>
								<li class="zm-common-nav-bar-item">
									<a href="javascript:;" class="zm-common-nav-bar-link" id="tb-webshare-letter">站内信分享</a>
								</li>
							</ul>
							<div class="zm-tab-content weibo-panel"
								id="tb-webshare-sina-content">
								<div class="zg-section">
									<label> 
										<input data-bindurl="/oauth/auth/sina?next=/oauth/callback" class="sina-checkbox" type="checkbox"> 
										<span class="zg-ico-sina"></span> 新浪微博
									</label>
									 <label>
									 	 <input data-bindurl="/oauth/auth/qq?next=/oauth/callback" class="qq-checkbox" type="checkbox"> 
										 <span class="zg-ico-qq"></span> 腾讯微博
									</label>
								</div>
								<div class="zg-section">
									<div class="zg-form-text-input">
										<textarea style="width: 100%" id="zh-webshare-sina-pm"></textarea>
									</div>
								</div>
								<div class="zm-command">
									<div class="zm-item-meta zg-left">
										<span id="zg-webshare-sina-counter">还可以输入 140字</span>
									</div>
									<a class="zm-command-cancel" name="cancel" href="javascript:;">取消</a>
									<a class="zg-btn-blue weibo-share-button" name="send_sina" href="javascript:;">发布</a>
								</div>
							</div>
							<div class="zm-tab-content" id="tb-webshare-mail-content" style="display: none;">
								<dl class="zm-form-table zm-form-table-medium">
									<dt class="zm-form-table-head zm-form-table-head-align-middle">
										<label for="user-name" class="zg-medium-gray">收件人：</label>
									</dt>
									<dd class="zm-form-table-field">
										<input class="zg-form-text-input" id="zh-webshare-mail-reciiver" type="email">
									</dd>
									<dt class="zm-form-table-head zm-form-table-head-align-middle">
										<label for="zh-webshare-mail-content" class="zg-medium-gray">内容：</label>
									</dt>
									<dd class="zm-form-table-field">
										<div class="zg-form-text-input">
											<textarea style="width: 100%" id="zh-webshare-mail-content"></textarea>
										</div>
									</dd>
								</dl>
								<div class="zm-command">
									<a class="zm-command-cancel" name="cancel" href="javascript:;">取消</a>
									<a class="zg-r3px zg-btn-blue" name="send_mail"	href="javascript:;">发送</a>
								</div>
							</div>
							<div id="tb-webshare-letter-content" class="zm-tab-content"	style="display: none;"></div>
						</div>
					</div>
				</div>
			</div>
			 
			 
			<div class="zm-side-section">
				<div class="zm-side-section-inner">
					<h3>问题状态</h3>
					<div class="zg-gray-normal">
						最近活动于 <span class="time">15:04</span> <span class="zg-bull">•</span>
						<a href="#">查看问题日志</a>
					</div>
					<div class="zg-gray-normal">
						被浏览 <strong>6855</strong> 次，相关话题关注者 <strong>211970</strong> 人
					</div>
				</div>
			</div>
			 
		-->	
		<!-------------------这个先注释掉--去掉两条线中的一条----开始------------------------->	
		<!--
		</div>
		-->
		<!-------------------这个先注释掉--去掉两条线中的一条----结束------------------------->
		
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


	<div id="zh-footer" class="zu-footer">
		<div class="zg-wrap zu-footer-inner">
		<!-- 
			<div class="zg-left">
				<a href="http://www.zhihu.com/read" class="zg-link-gray-14">知道阅读</a>
			</div>
		 -->	
			<a href="#" target="_blank"
				class="zg-link-gray-14">知道指南</a> <span class="zg-bull">•</span> <a
				href="javascript:;" class="zg-link-gray-14 zg-report" id="zh-report">建议反馈</a>
			<div style="display: none;" id="zh-report-content">
				<select class="zg-editor-type zg-right">
					<option selected="selected" value="help">使用帮助</option>
					<option value="bug">问题反馈</option>
					<option value="suggest">功能建议</option>
				</select>
				<div class="zg-report-title">
					<span class="zg-gray-darker">请填写你的反馈内容</span> 
					<span class="zg-report-msg" style="display: none;">请选择反馈种类：</span>
				</div>
				<div class="zg-editor-simple-wrap zg-form-text-input zg-report-content">
					<textarea class="zg-editor-input" name="content"></textarea>
				</div>
			</div>
			<span class="zg-bull">•</span> 
			<a target="_blank" class="zg-link-gray-14">知道协议</a>
			<span class="zg-bull">•</span> <span>© 2013 知道</span>
		</div>
	</div>
	<!---------------------干扰提问功能--先注释掉--------开始--------------------------->
	<!--
	<div style="display: none">
		<div id="zh-add-question-form" class="zh-add-question-form">
			<div
				class="zg-section-big zm-modal-dialog-guide-title zg-r5px add-question-guide"
				style="display: none">
				<p>
					知道禁止求职招聘、寻人寻物等类型的内容，详情可参看 
					<a target="_blank">知道提问规范有哪些？</a>
				</p>
			</div>
			<div class="zg-section-big">
				<div id="zm-modal-dialog-info-wrapper"></div>
				<div style="display: none; position: relative;"
					id="zm-modal-dialog-warnmsg-wrapper">
					<div class="zm-modal-dialog-warnmsg zm-modal-dialog-guide-warn-message zg-r5px"></div>
					<a name="close" title="关闭" href="javascript:;" class="zu-global-notify-close" style="display: none">x</a>
					 <span class="zm-modal-dialog-guide-title-msg"></span>
				</div>
				<div class="zg-form-text-input" style="position: relative;">
					<textarea class="zg-editor-input add-question-title-form" title="在这里输入问题" id="zh-question-suggest-title-content"></textarea>
				</div>
				<div id="zh-question-suggest-ac-wrap" style="position: absolute;"></div>
			</div>
			<div class="zg-section-big">
				<div style="display: none; position: relative;" class="zm-modal-dialog-warnmsg-wrapper">
					<div class="zm-modal-dialog-warnmsg zm-modal-dialog-guide-warn-message zg-r5px"></div>
					<a name="close" title="关闭" href="javascript:;" class="zu-global-notify-close" style="display: none">x</a> 
					<span class="zm-modal-dialog-guide-title-msg"></span>
				</div>
				<div>
					<a href="javascript:;" id="zh-question-suggest-detail-trigger">
						<span class="zm-add-question-detail-icon"></span></a>
						<span id="zh-question-form-detail-err"></span>
				</div>
				<div id="zh-question-suggest-detail-container">
					<div class="zm-editable-content" data-disabled="1"></div>
				</div>
			</div>
			<div class="zm-add-question-form-topic-wrap">
				<div style="" id="zh-question-suggest-topic-container" class="zm-tag-editor zg-section">
					<div class="zm-tag-editor-labels"></div>
					<div id="zh-question-suggest-autocomplete-container"></div>
				</div>
			</div>
			<div class="zm-command">
				<label class="zm-question-form-hide-in-about-question-el zg-left">
					<input value="1" class="zg-addq-isanon" type="checkbox">匿名
					<span class="zm-question-form-split">&nbsp;</span>
				</label>
				<div class="zg-left zm-add-question-form-sns-wrap">
					<span>分享到</span>
				</div>
				<a href="javascript:;" name="cancel" class="zm-command-cancel">取消</a>
				<a href="javascript:;" name="addq" class="zg-r5px zu-question-form-add zg-btn-blue">确认</a>
				<a href="" name="jumpq" class="zg-r5px zg-btn-blue zu-question-form-jump" style="display: none;">查看问题</a>
			</div>
		</div>
	</div>
	-->
	<!---------------------干扰提问功能--先注释掉--------结束--------------------------->
 
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
<!---------------返回顶部的div------开始--------------->
<div id="zd-goto-top"></div>
<!---------------返回顶部的div------结束--------------->
</body>
</html>
