<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Travnsitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Untitled Document</title>
<#include "global.ftl"/>
<#include "/SY_COMM_ZHIDAO/config_constant.ftl">
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/userzhidao_center.css"/>
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/baidu_style_files/module_fe8dd525.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/baidu_style_files/module_c595d1cf.css">
<link rel="stylesheet"	href="/sy/comm/zhidao/zhihu_style_files/z1303291140368123.css"	type="text/css" media="screen,print">
<script type="text/javascript" src="/sy/base/frame/jquery.tmpl.js"></script>
<script type="text/javascript" src="/sy/comm/zhidao/zhihu_style_files/zhidao_comment.js"></script>
<script type="text/javascript" src="/sy/comm/cms/js/zhidao.js"></script>
<!---------------返回顶部的Js/CSS------开始--------------->
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/zd-goto-top.css">
<link rel="stylesheet" type="text/css" href="/sy/theme/default/common.css">
<script type="text/javascript" src="/sy/comm/zhidao/js/zd-goto-top.js"></script>

<!---------------返回顶部的Js/CSS------结束--------------->
<script type="text/javascript">
	var otherId='${who}',
		tmplId='${userZhidao_center_tmpl_id}',
		myCode=parent.System.getVar("@USER_CODE@"),
		active='${active}';
</script>
<script type="text/javascript" src="/sy/comm/zhidao/js/userzhidao_center.js"></script>

  
<#noparse>
<!-------------------回复模版-------开始------------------------->
<script id="js-myAnswer-Template" type="text/x-jquery-tmpl">
	 <div class="zd-profile-section-item clearfix"> 
	 	  <a class="zd-profile-vote-count" href="javascript:void(0);">
          		<div class="zd-profile-vote-num">${A_LIKE_VOTE}</div>
          		<div class="zd-profile-vote-type">赞同</div>
          </a>
          <div class="zd-profile-section-main">
            <h2 class="zd-profile-question"> 
	             <a class="question_link" target="_self" href="javascript:view('${Q_ID}','${Q_TITLE}');">
						${Q_TITLE}
	             </a> 
            </h2>
            <div> {{html A_CONTENT}}</div>
          </div>
        </div>
</script>
<!-------------------回复模版-------结束------------------------->

<!----------------------出题模版-------开始-------------------->
<script id="js-myAsk-Template" type="text/x-jquery-tmpl">
	<div class="zd-profile-section-item zd-clear">
            <span class="zd-profile-vote-count">
                <div class="zd-profile-vote-num">${Q_READ_COUNTER}</div>
                <div class="zd-profile-vote-type">浏览</div>
            </span>
            <div class="zd-profile-section-main">
                <h2 class="zd-profile-question">
                    <a class="question_link" target="_self" href="javascript:view('${Q_ID}','${Q_TITLE}');">
                    	${Q_TITLE}
                    </a>
                </h2>
                <div class="zd-gray">
                    ${Q_ANSWER_COUNTER} 个回复
                    <span class="zd-bull">•</span>
                    <span class="quesFollowCounter">
                        ${quesfollowCounter} 
                    </span> 人关注
                </div>
            </div>
        </div>
</script>
<!----------------------出题模版-------结束-------------------->
<!--------------------最新动态模版---------开始------------------------>
<script id="js-myActivity-Template" type="text/x-jquery-tmpl">
{{if ACT_CODE=="ZHIDAO_CREATE_ANSWER" || ACT_CODE=="ZHIDAO_LIKE_ANSWER"}}
<div class="zd-profile-section-item zd-item clearfix" act="${ACT_CODE}">
    <span class="zd-profile-setion-time zd-gray zd-right">
      	 ${parent.$.timeago(parent.Format.substr(0,19,S_ATIME))}
    </span>
    <div class="zd-profile-section-main zd-profile-section-activity-main zd-profile-activity-page-item-main">
        <div class="clearfix">
            <a href="#" class="zd-link" title=" ${USER_CODE__NAME}">
                ${USER_CODE__NAME}
            </a>
            	${ACT_CODE__NAME}
        </div>
        <a class="question_link" href="javascript:view('${answer.Q_ID}','${answer.Q_TITLE}');">
          	  ${parent.Format.limit(40,DATA_DIS_NAME)}
        </a>
    </div>
    <div class="zd-item-answer">
        <a class="zd-anchor-hidden"> </a>
        <div class="zd-item-vote">
            <a name="expand" class="zd-item-vote-count" href="javascript:;">
                ${answer.A_LIKE_VOTE}
            </a>
        </div>
        <div class="answer-head">
            <div class="zd-item-answer-author-info">
                <h3 class="zd-item-answer-author-wrap">
                    <a class="zd-item-link-avatar zd-user js-zd-view"  data-id="${answer.S_USER}">
                    	<img src="${answer.S_USER__IMG}t&size=30x30" class="zd-list-avatar" style="width:auto;height:30px;border-radius:0;" />
                    </a>
                    <a class="zd-user js-zd-view"  data-id="${answer.S_USER}">
                        	${answer.S_USER__NAME}
                    </a>
                    <strong title="" class="zu-question-my-bio"></strong>
                </h3>
            </div>
             
        </div>
        <div class="zd-item-rich-text">
            <div class="zd-summary summary clearfix">
              	  {{html answer.A_CONTENT}}
              	  {{if readMore=="true"}}
                  	<a href="javascript:void(0);" data-resourceid="${answer.A_ID}" class="toggle-expand">显示全部</a>
                  {{/if}}
            </div>
        </div>
        <div class="zd-item-meta zd-item-comment-el answer-actions clearfix">
            <a class="zd-question-follow meta-item zd-user-activiy" data-id="${answer.Q_ID}" data-dis-name="${answer.Q_TITLE}">
                <i class="z-icon-follow"> </i>
              		<span>关注主题</span>
            </a>
            <a href="javascript:;" name="addcomment" class="meta-item toggle-comment" style="display:none;">
                <i class="z-icon-comment"> </i>
                ${answer.A_COMMENT_COUNTER} 条评论
            </a>
            <a href="#" name="collapse" class="collapse meta-item zd-right"><i class="z-icon-fold"></i>收起</a>
        </div>
    </div>
</div>
{{else}}
<div class="zd-profile-section-item zd-item clearfix" act="${ACT_CODE}">
    <span class="zd-profile-setion-time zd-gray zd-right">
       ${parent.$.timeago(parent.Format.substr(0,19,S_ATIME))}
    </span>
    <div class="zd-profile-section-main zd-profile-section-activity-main zd-profile-activity-page-item-main">
        <a href="" class="zd-link" title="${USER_CODE__NAME}">
          ${USER_CODE__NAME}
        </a>
       	 ${ACT_CODE__NAME}
       	 {{if ACT_CODE=="ZHIDAO_FOLLOW_CATEGORY"}}
	        <a class="category_link"  href="javascript:viewCategory('${DATA_ID}');">
	            	${DATA_DIS_NAME}
	        </a>
	     {{else  ACT_CODE=="ZHIDAO_FOLLOW_USER"}}
	     	<a class="category_link"  href="javascript:othersZhidao('${tmplId}','${DATA_ID}');">
	     		{{if DATA_ID_IMG == ""}}
		     		<img src="/sy/theme/default/images/common/rh-male-icon.png?size=30x30" class="pic" style="width:auto;height:30px;border-radius:0;" />
	     		{{else}}
	     			<img src="${DATA_ID_IMG}&size=30x30" class="pic" style="width:auto;height:30px;border-radius:0;" />
	     		{{/if}}
	            	${DATA_DIS_NAME}
	        </a>
	     {{else}}
	     	<a class="question_link"  href="javascript:view('${DATA_ID}','${DATA_DIS_NAME}');">
	            	${parent.Format.limit(40,DATA_DIS_NAME)}
	        </a>
         {{/if}}
    </div>
</div>
{{/if}}
</script>
<!--------------------最新动态模版---------结束------------------------>


</#noparse>
</head>

<body class="layout-center">
	<div>
		<div class="line">
		</div>

		<div id="body" class="container" style="width:100%;margin:0 20px;background-color:#FFF!important;">
			<div id="sub-menu">
				<ul>
				</ul>
			</div>
			<!-----------头部-------------------->
			
			
			<div class="zd-wrapper clearfix">
			<@zhidao_user debugName="用户信息" userId='${who}'>
			  <div class="zd-content">
			  <div class="zd-main-content-inner" id="zd-pm-page-wrap">
			    <div class="zd-profile-header">
			      <div class="zd-profile-header-main">
			        <div class="zd-profile-header-name">
			        	<span id="js-username-span" class="zm-profile-header-username zg-left">${user.USER_NAME}</span>
						<!--<span id="js-spec-outside" class="zm-profile-header-bio" style="font-weight:100" title="领域专家"><span id="spec-span"></span>领域专家</span>-->
			        	<span class="zm-profile-header-bio">，${user.USER_POST}</span>
			        	<@zhidao_only_specialist debugName="单个专家的专家信息" userId="${user.USER_CODE}">
				        	<#if specialist.SPEC_TYPE == 1>
				        		<span class="zm-profile-header-bio">，知道平台领导，${user.CMPY_NAME}，${user.DEPT_NAME}</span>
				        	<#elseif specialist.SPEC_TYPE == 2>
				        		<span class="zm-profile-header-bio">，知道平台职能部门专家，${user.CMPY_NAME}，${user.DEPT_NAME}</span>
				        	<#elseif specialist.SPEC_TYPE == 3>
				        		<span class="zm-profile-header-bio">，知道平台业务专家，${user.CMPY_NAME}，${user.DEPT_NAME}</span>
				        	<#else>
				        		<span class="zm-profile-header-bio">，${user.CMPY_NAME}，${user.DEPT_NAME}</span>
				        	</#if>
			        	</@zhidao_only_specialist>
			        </div>
			        <div class="zd-profile-header-img-wrapper clearfix"> 
			        	<a class=""> 
			        		<img alt="${user.USER_NAME}" src="<@setUserImg user />?size=100x100" class="zd-profile-header-img zd-avatar-big zd-avatar-editor-preview" /> 
			        	</a> 
			        </div>
			        <div class="zd-profile-header-info">
			          <div class="zd-profile-header-user-describe">
			            <!--<div class="items">
			              <div class="item editable-group"> 
			              	<i class="zd-icon location"></i> 
			                <span class="info-wrap"> 
			                    <span class="location item" title="公司">
			                          	 {user.CMPY_NAME}
			                    </span> 
			                </span> 
			              </div>
			              <div class="item editable-group " data-name="education"> 
			              	<i class="zd-icon education"></i> 
			                <span class="info-wrap"> 
			                	<span class="education item" title="{user.DEPT_CODE}">{user.DEPT_NAME}</span> 
			                </span> 
			              </div>
			              <a class="zd-profile-header-user-detail zd-link-litblue-normal js-goto-detail" href="javascript:void(0);"> 
			              	<i class="zd-profile-header-icon"></i> <span>查看详细资料</span> 
			              </a>
			            </div>-->
			            
			            <!--------------------------个人简介显示/编辑框-----开始------------------------->
			            <@zhidao_user_desc debugName="知道用户个人简介" userId="${who}">
			            <!-----if----如果有个人简介才显示这个div---------------->
			            <div data-name="description" class="zm-profile-header-description editable-group  editing" style="font-size:13px;min-height:50px;"> 
							<span class="info-wrap fold-wrap fold disable-fold"> 
								<!-----------------完整显示个人简介----------------------->
								<span class="description unfold-item" style="display:none;"> 
									<span class="content">${USER_DESC}</span> 
									<a class="fold collapse meta-item" name="fold" href="javascript:;">
									<i class="z-icon-fold"></i>收起</a> 
								</span>
								
								<!-----------------缩略显示个人简介----------------------->
								<#if (USER_DESC?length gt 100)> 
								<span class="fold-item"> 
									<span class="content">${USER_DESC?substring(0,100)}...</span> 
									<a name="unfold" class="unfold" href="javascript:;">显示全部»</a> 
								</span>
								<#else>
								<span class="fold-item"> 
									<span class="content">${USER_DESC}</span> 
								</span>
								</#if> 
								
								<!-----------------显示修改按钮----------------------->
								<#if USER_DESC?length != 0 >
								<a href="javascript:;" name="edit" class="zu-edit-button edit myself">
									<i class="zu-edit-button-icon"></i>
									<span class="edit-msg">修改</span>
								</a> 
								<#else>
								<a href="javascript:;" name="edit" style="display:none;" class="zu-edit-button edit myself">
									<i class="zu-edit-button-icon"></i>
									<span class="edit-msg">修改</span>
								</a>
								</#if>
							</span> 
							<!-----------------没有简介时显示填写按钮----------------------->
							<#if USER_DESC?length != 0 >
							<span class="info-empty-wrap myself" style="display:none;">
								<a class="zg-link-litblue-normal" name="edit" href="javascript:;">
									<i class="zg-icon zg-icon-edit-button-blue"></i>
									填写个人简介
								</a>
							</span> 
							<#else>
							<span class="info-empty-wrap myself">
								<a class="zg-link-litblue-normal" name="edit" href="javascript:;">
									<i class="zg-icon zg-icon-edit-button-blue"></i>
									填写个人简介
								</a>
							</span>
							</#if>
							
							<!-----------------文本编辑框----------------------->
							<span style="display:none;" class="edit-wrap">
					  			<div class="zm-editable-editor-outer">
					    			<div class="zm-editable-editor-inner zg-form-text-input">
					      				<textarea id="profile-header-description-input" class="zm-editable-editor-input description zu-seamless-input-origin-element" value="" name="description" style="white-space: pre;">${USER_DESC}</textarea>
					    			</div>
					  			</div>
					  			<a href="javascript:;" name="save" class="zg-btn-blue">确定</a> 
							</span>
						</div>
						</@zhidao_user_desc>
						<!--------------------------个人简介显示/编辑框-----结束------------------------->
			            
			            
			            <div class="zd-profile-header-description editable-group " data-name="description">
			                    <i class="zd-icon business" style="background-position:-208px -147px;"></i> 
				                <span class="info-wrap">
				                	<!------------------统计用户总积分-------开始-------------------> 
				                	<@zhidao_user_allIntegral debugName="用户总积分" userId="${who}">
				                	<span class="business item" title="积分">
				                    	积分${allIntegral}
				                    </span> 
				                    </@zhidao_user_allIntegral>
				                    <!------------------统计用户总积分-------结束------------------->
				                </span> 
				                <i class="zd-icon business" style="background-position:-190px -147px;"></i>
				                <@zhidao_user_adoptionRate debugName="用户采纳率" userId="${who}">
				                <span class="info-wrap">
				                	<span class="item">采纳率${adoptionRate?string.percent}</span>
				                </span>
				                </@zhidao_user_adoptionRate>
			            </div>
			          </div>
			        </div>
			      </div>
			      <!---------end of header main--------------->
			      <div class="zd-profile-header-operation clearfix">
			        <div class="zd-profile-header-info-list">
			           <span class="zd-profile-header-info-title">获得</span>
			            <span class="zd-profile-header-user-agree">
			            	<span class="zd-profile-header-icon"></span>
			                <strong id="aLikeVoteCounter-strong"></strong>赞同
			            </span>
			            <span class="zd-profile-header-user-thanks">
			            	<span class="zd-profile-header-icon"></span>
			                <strong id="aBestCounter-strong"></strong>回复被出题者采纳
			            </span>
			            <span class="zd-profile-header-user-thanks">
			            	<span class="zd-profile-header-icon"></span>
			                <strong id="aAdminBestCounter"></strong>回复被管理员采纳
			            </span>
			            <span class="zd-profile-header-user-share">
			            	<span class="zd-profile-header-icon"></span>
			                <strong id="shareCounter"></strong>被分享
			            </span> 
			        </div>
			        <div class="zd-profile-header-op-btns">
			         <a href="#" name="focus" id="zd-person-follow" class="zd-right zd-btn zd-btn-follow zd-rich-follow-btn">关注</a>
			        </div>
			      </div>
			      <!----------------end of header-operation----------------------->
			      <div class="navbar">
			      <@zhidao_question_ask_follow_counter debugName="统计出题数,回复数,收藏数,最新动态数" userId='${who}'>
			        <div class="navbar-inner clearfix">
				        <a class="item active js-mark-default" href="#" style="border-left:none;">  
				        	主页 
				        </a>
				        <a class="item js-mark-answers" href="">
				        	回复
				        	<span class="zd-gray-normal num">${answerCounter}</span>
				        </a>
				        <a class="item js-mark-questions" href="">
				        	出题
				       	 <span class="zd-gray-normal num">${askCounter}</span>
				        </a>
				        <a class="item js-mark-favorites" href="">
				        	收藏
				       	 <span class="zd-gray-normal num">${favoriteCounter}</span>
				        </a>
				        <a class="item js-mark-activity" href="">
				        	最新动态
				       	 <span id="activity-span" class="zd-gray-normal num"></span>
				        </a>
				        <!--
				        <a class="item js-mark-feedback myself" href="">
				        	意见
				        </a>
				        <a class="item js-mark-report myself" href="">
				        	举报
				        </a>
				        -->
			        </div>
			      </@zhidao_question_ask_follow_counter>
			      </div>
			    </div>
			    <!-----------------end of header------------------------->
			    
		<#if mark==''>	    
		<!--------------------------------首页应该显示的区域块儿---------------开始-------------------------------------------->
				
				
				<!------------------回复列表-------开始--------------------->
				<@zhidao_answer_list debugName="回复列表" userId='${who}' summaryLength="50" count="10" >
			    <#if tag_list?size!=0 >
			    <div class="zd-profile-section-wrap zd-profile-answer-wrap">
				      <div class="zd-profile-section-head"> 
				      	<a class="zd-profile-section-title js-mark-answers" target="_self" name="${user.USER_CODE}-answer" href=''>
					        <h2 class="zd-profile-section-name">${user.USER_NAME}的回复</h2>
					        <i class="zd-right zd-icon zd-profile-section-more-btn js-mark-answers-i"></i>
					        <!--原来的翻页效果
					        <i class="zd-right zd-next" title="下一页">></i>
					        <i class="zd-right zd-prev" title="上一页"><</i>
					       	-->
				        </a> 
				      </div>
			      <div class="zd-profile-section-list" id="zd-answers-list">
			        <#list tag_list as answer>
			        	<div class="zd-profile-section-item clearfix"> 
					        	<a class="zd-profile-vote-count" href="javascript:void(0);">
						          <div class="zd-profile-vote-num">${answer.A_LIKE_VOTE}</div>
						          <div class="zd-profile-vote-type">赞同</div>
					          	</a>
					            <div class="zd-profile-section-main">
						            <h2 class="zd-profile-question"> 
							             <a class="question_link" target="_self" href="javascript:view('${answer.Q_ID}','${answer.Q_TITLE}');">
							             	<#if answer.Q_TITLE?length gt 30>
												${answer.Q_TITLE[0..30]}...
											<#else>
												${answer.Q_TITLE}	
											</#if>
							             </a> 
						            </h2>
						            <div>
											${answer.A_CONTENT}	
						            </div>
								</div>
			           	 </div>
			        </#list>
			      </div>
			    </div>
			    </#if>
			    </@zhidao_answer_list>
			    <!------------------end of answer----------------------------------------------->
			    
			   	<!--------------------出题列表--------开始------------------------>
			    <@zhidao_ask_list debugName="出题列表" userId='${who}' count=10 isFollowCount="isFollowCount">
			    <#if tag_list?size!=0 >
			    <div class="zd-profile-section-wrap zd-profile-ask-wrap" id="zd-profile-ask-wrap">
			        <div class="zd-profile-section-head">
			        	<a class="zd-profile-section-title js-mark-questions" name="${user.USER_CODE}-ask" href="">
			                <h2 class="zd-profile-section-name">${user.USER_NAME}的出题</h2>
			                <i class="zd-right zd-icon zd-profile-section-more-btn js-mark-questions-i"></i>
			                <!--
			                <i class="zd-right zd-next" title="下一页">></i>
			                <i class="zd-right zd-prev" title="上一页"><</i>
			                -->
			        	</a>
			        </div>
			        <div class="zd-profile-section-list" id="zd-ask-list"> 
			        <#list tag_list as ask>
			            <div class="zd-profile-section-item zd-clear">
			                <span class="zd-profile-vote-count">
			                    <div class="zd-profile-vote-num">${ask.Q_READ_COUNTER!0}</div>
			                    <div class="zd-profile-vote-type">浏览</div>
			                </span>
			                <div class="zd-profile-section-main">
			                    <h2 class="zd-profile-question">
			                        <a class="question_link" target="_self" href="javascript:view('${ask.Q_ID}','${ask.Q_TITLE}');">
			                        	${ask.Q_TITLE}
			                        </a>
			                    </h2>
			                    <div class="zd-gray">
			                        ${ask.Q_ANSWER_COUNTER!0} 个回复
			                        <span class="zd-bull">•</span>
			                        <span class="quesFollowCounter">
				                        <@zhidao_question_follow_counter debugName="出题关注" questionId='${ask.Q_ID}'>
				                        		${quesfollowCounter} 
				                        </@zhidao_question_follow_counter>
			                        </span> 人关注
			                    </div>
			                </div>
			            </div>
			          </#list>
					</div>
			    </div>
			    </#if>
			    </@zhidao_ask_list>
			    <!--------------------end of ask---------------------------------------------------------->
			    
			    <!-------------------最新动态---------开始-------------------------->
			    <div class="zd-profile-section-wrap" id="zh-profile-activity-wrap">
			        <div class="zd-profile-section-head">
			        	<a class="zd-profile-section-title" href="javascript:void(0);" name="${user.USER_CODE}-activity">
			            <h2 class="zd-profile-section-name">
			                	最新动态
			            </h2>
			            </a>
			        </div>
			        <div class="zd-profile-section-list profile-feed-wrap">
			            <div id="zd-activity-list">
			            
			            </div>
			            <a id="zd-load-more" class="zd-btn-white zd-btn-more">
			            	<!--<i class="spinner-gray"></i>正在加载-->
			            	加载更多
			            </a>
			        </div>
			    </div>
			    <!-------------------------------end of 最新动态--------------------------------------------------------->
	   <!--------------------------------首页应该显示的区域块儿--------------结束-------------------------------------------->
	   </#if>
		
	   <#if mark=='answers'>		
	   <!--------------------------------点击		回复		应该显示的区域块儿---------------开始-------------------------------------------->
	   			<@zhidao_answer_list debugName="回复列表" userId='${who}' summaryLength="50" count="10" >
			    <#if tag_list?size!=0 >
	   			<div class="zm-profile-section-wrap zm-profile-answer-page">
				  <div class="zm-profile-section-head"> 
				  	<span class="zm-profile-section-name">
				  		<a href="#">${user.USER_NAME}</a> 的回复
				  	</span>
				  	<a href="#" class="zg-right zg-link-litblue-normal zm-profile-answer-page-return js-mark-default">返回个人主页</a>
				  	<!--
				    <div class="zm-profile-section-sort-wrap">
				      <label>排序方式：</label>
				      <a class="zg-link-blue-normal" href="#">赞同数</a> 
				      <span class="zg-bull">|</span> 
				      <a class="zg-link-gray" href="#">回复时间</a> 
				    </div>
				    -->
				  </div>
				  <div class="zm-profile-section-list profile-answer-wrap">
			        <div>
			          <div id="js-mark-answer-list" page="1" class="zh-general-list clearfix navigable">
			            <#list tag_list as answer>
				        	<div class="zd-profile-section-item clearfix"> 
						        	<a class="zd-profile-vote-count" href="javascript:void(0);">
							          <div class="zd-profile-vote-num">${answer.A_LIKE_VOTE}</div>
							          <div class="zd-profile-vote-type">赞同</div>
						          	</a>
						            <div class="zd-profile-section-main">
							            <h2 class="zd-profile-question"> 
								             <a class="question_link" target="_self" href="javascript:view('${answer.Q_ID}','${answer.Q_TITLE}');">
								             	<#if answer.Q_TITLE?length gt 30>
													${answer.Q_TITLE[0..30]}...
												<#else>
													${answer.Q_TITLE}	
												</#if>
								             </a> 
							            </h2>
							            <div>
												${answer.A_CONTENT}	
							            </div>
									</div>
				           	 </div>
				        </#list>
			          </div>
			          <a id="js-mark-answer-more" aria-role="button" class="zg-btn-white zu-button-more">
			          	<!--<i class="spinner-gray"></i>正在加载-->
			          	加载更多
			          </a> 
			        </div>
				  </div>
				</div>
				</#if>
			    </@zhidao_answer_list>
	   <!--------------------------------点击		回复		应该显示的区域块儿---------------结束-------------------------------------------->	
	   </#if>
	   <#if mark='questions'>
	   <!--------------------------------点击		出题		应该显示的区域块儿---------------开始-------------------------------------------->
	   			<@zhidao_ask_list debugName="出题列表" userId='${who}' count=10 isFollowCount="isFollowCount">
			    <#if tag_list?size!=0 >
	   			<div class="zm-profile-section-wrap zm-profile-ask-wrap">
				  <div class="zm-profile-section-head"> 
				  	<span class="zm-profile-section-name">
				  		<a href="#">${user.USER_NAME}</a> 的出题
				  	</span> 
				  	<a href="#" class="zg-right zg-link-litblue-normal zm-profile-answer-page-return js-mark-default">返回个人主页</a> 
				  </div>
				  <div class="zm-profile-section-list">
				    <div>
				      <div id="js-mark-ask-list" page="1">
				        <#list tag_list as ask>
			            <div class="zd-profile-section-item zd-clear">
			                <span class="zd-profile-vote-count">
			                    <div class="zd-profile-vote-num">${ask.Q_READ_COUNTER!0}</div>
			                    <div class="zd-profile-vote-type">浏览</div>
			                </span>
			                <div class="zd-profile-section-main">
			                    <h2 class="zd-profile-question">
			                        <a class="question_link" target="_self" href="javascript:view('${ask.Q_ID}','${ask.Q_TITLE}');">
			                        	${ask.Q_TITLE}
			                        </a>
			                    </h2>
			                    <div class="zd-gray">
			                        ${ask.Q_ANSWER_COUNTER!0} 个回复
			                        <span class="zd-bull">•</span>
			                        <span class="quesFollowCounter">
				                        <@zhidao_question_follow_counter debugName="出题关注" questionId='${ask.Q_ID}'>
				                        		${quesfollowCounter} 
				                        </@zhidao_question_follow_counter>
			                        </span> 人关注
			                    </div>
			                </div>
			             </div>
			          	 </#list>
				      </div>
				      <a id="js-mark-ask-more" aria-role="button" class="zg-btn-white zu-button-more">
			          	<!--<i class="spinner-gray"></i>正在加载-->
			          	加载更多
			          </a>
				    </div>
				  </div>
				</div>
				</#if>
			    </@zhidao_ask_list>
	   <!--------------------------------点击		出题		应该显示的区域块儿---------------结束-------------------------------------------->
	   </#if>
	   
	   <#if mark='activity'>
	   		<!-------------------最新动态---------开始-------------------------->
		    <div class="zd-profile-section-wrap" id="zh-profile-activity-wrap">
		        <div class="zm-profile-section-head">
		        	<span class="zm-profile-section-name">
				  		<a href="#">${user.USER_NAME}</a> 的最新动态
				  	</span> 
				  	<a href="#" class="zg-right zg-link-litblue-normal zm-profile-answer-page-return js-mark-default">返回个人主页</a> 
		        </div>
		        <div class="zd-profile-section-list profile-feed-wrap">
		            <div id="zd-activity-list">
		            
		            </div>
		            <a id="zd-load-more" class="zd-btn-white zd-btn-more">
		            	<!--<i class="spinner-gray"></i>正在加载-->
		            	加载更多
		            </a>
		        </div>
		    </div>
		    <!-------------------------------end of 最新动态--------------------------------------------------------->
	   </#if>
	   
	   <#if mark='favorites'>
	   <!--------------------------------点击		收藏		应该显示的区域块儿---------------开始-------------------------------------------->
	   			<@zhidao_favorites_list debugName="收藏列表" userId='${who}' count=10>
			    <#if tag_list?size!=0 >
	   			<div class="zm-profile-section-wrap zm-profile-ask-wrap">
				  <div class="zm-profile-section-head"> 
				  	<span class="zm-profile-section-name">
				  		<a href="#">${user.USER_NAME}</a> 的收藏
				  	</span> 
				  	<a href="#" class="zg-right zg-link-litblue-normal zm-profile-answer-page-return js-mark-default">返回个人主页</a> 
				  </div>
				  <div class="zm-profile-section-list">
				    <div>
				      <div id="js-mark-favorite-list" page="1">
				        <#list tag_list as ask>
			            <div class="zd-profile-section-item zd-clear">
			                <span class="zd-profile-vote-count">
			                    <div class="zd-profile-vote-num">${ask.question.Q_READ_COUNTER!0}</div>
			                    <div class="zd-profile-vote-type">浏览</div>
			                </span>
			                <div class="zd-profile-section-main">
			                    <h2 class="zd-profile-question">
			                        <a class="question_link" target="_self" href="javascript:view('${ask.question.Q_ID}','${ask.question.Q_TITLE}');">
			                        	${ask.question.Q_TITLE}
			                        </a>
			                    </h2>
			                    <div class="zd-gray">
			                        ${ask.question.Q_ANSWER_COUNTER!0} 个回复
			                        <span class="zd-bull">•</span>
			                        <span class="quesFollowCounter">
				                        <@zhidao_question_follow_counter debugName="出题关注" questionId='${ask.question.Q_ID}'>
				                        		${quesfollowCounter} 
				                        </@zhidao_question_follow_counter>
			                        </span> 人关注
			                    </div>
			                </div>
			             </div>
			          	 </#list>
				      </div>
				      <a id="js-mark-favorites-more" aria-role="button" class="zg-btn-white zu-button-more">
			          	<!--<i class="spinner-gray"></i>正在加载-->
			          	加载更多
			          </a>
				    </div>
				  </div>
				</div>
				</#if>
			    </@zhidao_favorites_list>
	   <!--------------------------------点击		收藏		应该显示的区域块儿---------------结束-------------------------------------------->
	   </#if>
	   
	   <#if mark='follows'>
	   <!--------------------------------点击		关注了		应该显示的区域块儿---------------开始-------------------------------------------->
			    <@zhidao_follow_list debugName="关注人列表" userId='${who}' count=10 isFollow="isFollow">
			    <#if tag_list?size!=0 >
			    <div class="zm-profile-section-wrap zm-profile-followee-page">
				  <div class="zm-profile-section-head"> 
				  	<span class="zm-profile-section-name">
				  		<a href="/people/li-ting-ting-83">${user.USER_NAME}</a> 关注了${page.ALLNUM} 人
				  	</span> 
			  		<a href="/people/li-ting-ting-83" class="zg-right zg-link-litblue-normal zm-profile-answer-page-return js-mark-default">返回个人主页</a> 
			  	  </div>
			  	  
				  <div class="zm-profile-section-list">
				    <div>
				      <div id="js-mark-follow-list" page="1" class="zh-general-list clearfix">
				    	<#list tag_list as person>
				        <div class="zm-profile-card zm-profile-section-item zg-clear">
				          <div class="zg-right"> 
				          	<!--<a class="zg-btn zg-btn-follow zm-rich-follow-btn small nth-0" id="tf-043ff01e5d03c529c268d50f388012c2" href="javascript:;" data-follow="m:button">关注</a>--> 
				          </div>
				          <a href="javascript:void(0);" class="zm-item-link-avatar" userName=${person.user.USER_CODE} data-tip="#" data-original_title=""> 
				          	<#if person.user.USER_IMG??>
								<#list person.user.USER_IMG?split("?") as src>
									<#if src_index == 0>
										<img class="zm-item-img-avatar" src="${src}?size=50x50">
									</#if>
								</#list>
							<#else>
								<img class="zm-item-img-avatar" src="/sy/theme/default/images/common/rh-lady-icon.png?size=40x40">
							</#if>
				          </a>
				          <div class="zm-list-content-medium">
				            <h2 class="zm-list-content-title">
				            	<a class="zg-link" href="javascript:void(0);" userName=${person.user.USER_CODE} data-tip="#" data-original_title="">${person.user.USER_NAME}</a>
				            </h2>
				            <div class="zg-big-gray">${person.user.USER_POST}</div>
				            <div class="details zg-gray"> 
				            	<a class="zg-link-gray-normal" href="#" target="_blank">${person.followCount} 关注者</a> / 
				            	<a class="zg-link-gray-normal" href="#" target="_blank">${person.askCount} 出题</a> / 
				            	<a class="zg-link-gray-normal" href="#" target="_blank">${person.answerCount}回复</a> / 
				            	<a class="zg-link-gray-normal" href="#" target="_blank">${person.likeVoteCount}赞同</a> 
				            </div>
				          </div>
				        </div>
				        </#list>
				       </div>
				       <a id="js-mark-follow-more" aria-role="button" class="zg-btn-white zu-button-more">
			          	 <!--<i class="spinner-gray"></i>正在加载-->
			          	    加载更多
			           </a> 
				    </div>
				  </div>
				</div>
				</#if>
				</@zhidao_follow_list>
	   <!--------------------------------点击		关注了		应该显示的区域块儿---------------结束-------------------------------------------->
	   </#if>
	   
	   <!------------------点击		意见反馈		应该显示的区域块儿----------------开始------------------------->
	   <#if mark='feedback'>
	   		<!-------------------最新动态---------开始-------------------------->
		    <div class="zd-profile-section-wrap" id="zh-profile-activity-wrap">
		        <div class="zm-profile-section-head">
		        	<span class="zm-profile-section-name">
				  		关于<a href="#">${user.USER_NAME}</a> 的意见反馈
				  	</span> 
				  	<a href="#" class="zg-right zg-link-litblue-normal zm-profile-answer-page-return js-mark-default">返回个人主页</a> 
		        </div>
		        <div class="zd-profile-section-list profile-feed-wrap">
		            <div>
		            	<@zhidao_feedback_list debugName="获取意见建议" userId="${who}">
						<#if feedBackList?size == 0>
							<h3 style="text-align:center;height:60px;line-height:60px;">没有意见建议！</h3>
						<#else>
						<#list feedBackList as feedBack>
						<!-------------------------一个单元----------开始--------------------------->
						<div id="zh-message-container" class="zg-info-message" style="display: none;"></div>
						<div class="zm-editable-status-normal" id="zh-question-title" style="margin-top:5px;">
							<h2 class="zm-item-title zm-editable-content">
								<span class="zm-title-img bg-img"></span>
								<span id="js-qTitle-span">${feedBack.F_TITLE}</span>
							</h2>
						</div>
						<div id="zh-question-detail" class="zm-item-rich-text zm-editable-status-normal" data-resourceid="474145" data-action="/question/detail">
							<div class="zm-editable-content">
								${feedBack.F_CONTENT}
							</div>
						</div>
		
						<div class="zm-item-meta zm-item-comment-el" id="zh-question-meta-wrap">
							<span class="answer-date-link-wrap meta-item"> 
								<#if feedBack.S_ATIME?length gt 10> 
									${feedBack.S_ATIME[0..10]}
								<#else> 
									${feedBack.S_ATIME} 
								</#if> 
							</span>
							
							 <#if feedBack.F_COMMENT_COUNTER gt 0 >&nbsp;
								 <a id="feedback-${feedBack.F_ID}" href="javascript:;" name="addcomment" class="comment meta-item">
									<i class="z-icon-comment"></i> ${feedBack.F_COMMENT_COUNTER}条评论
								 </a>
							 <#else>
								  <a id="feedback-${feedBack.F_ID}" href="javascript:;" name="addcomment" class="comment meta-item"> 
									<i class="z-icon-comment"></i> 添加评论
								  </a> 
							</#if>
							<div class="panel-container">
								<div class="question-invite-panel" style="display: none;"></div>
								<div id="comment-${feedBack.F_ID}" class="zm-comment-box" data-count="2"></div>
								
									<!--加载评论  -->
							   <script type="text/javascript">
							   		bindFeedBackComment('${feedBack.F_ID}','feedback-${feedBack.F_ID}','comment-${feedBack.F_ID}');
							   </script>
		
							</div>
						</div>
						<div id="zh-question-collapsed-wrap" class="zh-question-answer-wrapper" style="display: block;"></div>
						<!--------------------------一个单元----------结束------------------->
						</#list>
						</#if>
						</@zhidao_feedback_list>
		            </div>
		        </div>
		    </div>
		    <!-------------------------------end of 最新动态--------------------------------------------------------->
	   </#if>
	   <!------------------点击		意见反馈		应该显示的区域块儿----------------结束------------------------->
	   
	   <!------------------点击		举报		应该显示的区域块儿----------------开始------------------------->
	   <#if mark='report'>
	   		<!-------------------举报信息---------开始-------------------------->
		    <div class="zd-profile-section-wrap">
		        <div class="zm-profile-section-head">
		        	<span class="zm-profile-section-name">
				  		关于<a href="#">${user.USER_NAME}</a> 的举报信息
				  	</span> 
				  	<a href="#" class="zg-right zg-link-litblue-normal zm-profile-answer-page-return js-mark-default">返回个人主页</a> 
		        </div>
		        <div class="zd-profile-section-list profile-feed-wrap">
		            <div>
		            	<!------------------------------我举报的--------------------------------->
		            	<@zhidao_report_list debugName="获取举报信息" userId="${who}">
						<#if myReportList?size == 0>
							<h3 style="text-align:center;height:60px;line-height:60px;">没有我举报的信息！</h3>
						<#else>
						<#list myReportList as myReport>
						<!-------------------------一个单元----------开始--------------------------->
						<div id="zh-message-container" class="zg-info-message" style="display: none;"></div>
						
						<div class="zm-editable-status-normal" id="zh-question-title" style="margin-top:5px;">
							<span style="color:green;font-weight:700;font-size:14px;">举报 ： </span>
							<#if myReport.SERV_ID == 'SY_COMM_ZHIDAO_QUESTION'>
								<span style="font-size:12px;color:#999999;">[${myReport.dataDetail.Q_TITLE}]</span>
								<a href="javascript:view('${myReport.dataDetail.Q_ID}','${myReport.dataDetail.Q_TITLE}');" style="font-size:12px;">查看详情</a>
							<#else>
								<span style="font-size:12px;color:#999999;">[
									<#if (myReport.dataDetail.Q_TITLE?length > 5)>
										${myReport.dataDetail.Q_TITLE?substring(0,5)}...
									<#else>
										${myReport.dataDetail.Q_TITLE}
									</#if>
									]主题的[
									<#if (myReport.dataDetail.A_CONTENT?length > 10)>
										${myReport.dataDetail.A_CONTENT?substring(0,10)}...
									<#else>
										${myReport.dataDetail.A_CONTENT}
									</#if>
								]</span>
								<a href="javascript:viewAnswer('${myReport.dataDetail.Q_ID}','${myReport.dataDetail.Q_TITLE}','${myReport.dataDetail.A_ID}');" style="font-size:12px;">查看详情</a>
							</#if>
						</div>
						<div id="zh-question-detail" class="zm-item-rich-text zm-editable-status-normal" data-resourceid="474145" data-action="/question/detail">
							<div class="zm-editable-content">
								<span style="color:blue;font-weight:700;font-size:14px;">原因 ： </span>
								<#if myReport.DATA_TYPE == 0>
									<span style="font-size:12px;color:blue;">${myReport.DATA_EXPLAIN}</span>
								<#else>
									<span style="font-size:12px;color:blue;">${myReport.DATA_TYPE__NAME}</span>
								</#if>
							</div>
						</div>
						<div id="zh-question-detail" class="zm-item-rich-text zm-editable-status-normal" data-resourceid="474145" data-action="/question/detail">
							<div class="zm-editable-content">
								<#if myReport.FEEDBACK == ''>
									<span style="font-size:12px;">暂无处理意见！</span>
								<#else>
									<span style="color:red;font-weight:700;font-size:14px;">处理意见 ： </span>
									<span style="font-size:12px;color:red;">${myReport.FEEDBACK}</span>
								</#if>
							</div>
						</div>
						<div class="zm-item-meta zm-item-comment-el" id="zh-question-meta-wrap">
							<span class="answer-date-link-wrap meta-item"> 
								<#if (myReport.S_ATIME?length > 10)>
									${myReport.S_ATIME?substring(0,10)} 
								<#else>
									${myReport.S_ATIME}
								</#if>
							</span>
						</div>
						
						<div id="zh-question-collapsed-wrap" class="zh-question-answer-wrapper" style="display: block;"></div>
						<!--------------------------一个单元----------结束------------------->
						</#list>
						</#if>
						
						<!-------------------------举报我的--------------------------------------->
						<#if reportMeList?size == 0>
							<h3 style="text-align:center;height:60px;line-height:60px;">没有举报我的信息！</h3>
						<#else>
						<#list reportMeList as reportMe>
						<!-------------------------一个单元----------开始--------------------------->
						<div id="zh-message-container" class="zg-info-message" style="display: none;"></div>
						
						<div class="zm-editable-status-normal" id="zh-question-title" style="margin-top:5px;">
							<span style="color:green;font-weight:700;font-size:14px;">被举报 ： </span>
							<#if reportMe.SERV_ID == 'SY_COMM_ZHIDAO_QUESTION'>
								<span style="font-size:12px;color:#999999;">[${reportMe.dataDetail.Q_TITLE}]</span>
								<a href="javascript:view('${reportMe.dataDetail.Q_ID}','${reportMe.dataDetail.Q_TITLE}');" style="font-size:12px;">查看详情</a>
							<#else>
								<span style="font-size:12px;color:#999999;">[
									<#if (reportMe.dataDetail.Q_TITLE?length > 5)>
										${reportMe.dataDetail.Q_TITLE?substring(0,5)}...
									<#else>
										${reportMe.dataDetail.Q_TITLE}
									</#if>
									]主题的[
									<#if (reportMe.dataDetail.A_CONTENT?length > 10)>
										${reportMe.dataDetail.A_CONTENT?substring(0,10)}...
									<#else>
										${reportMe.dataDetail.A_CONTENT}
									</#if>
								]</span>
								<a href="javascript:viewAnswer('${reportMe.dataDetail.Q_ID}','${reportMe.dataDetail.Q_TITLE}','${reportMe.dataDetail.A_ID}');" style="font-size:12px;">查看详情</a>
							</#if>
						</div>
						<div id="zh-question-detail" class="zm-item-rich-text zm-editable-status-normal" data-resourceid="474145" data-action="/question/detail">
							<div class="zm-editable-content">
								<span style="color:blue;font-weight:700;font-size:14px;">原因 ： </span>
								<#if reportMe.DATA_TYPE == 0>
									<span style="font-size:12px;color:blue;">${reportMe.DATA_EXPLAIN}</span>
								<#else>
									<span style="font-size:12px;color:blue;">${reportMe.DATA_TYPE__NAME}</span>
								</#if>
							</div>
						</div>
						<div id="zh-question-detail" class="zm-item-rich-text zm-editable-status-normal" data-resourceid="474145" data-action="/question/detail">
							<div class="zm-editable-content">
								<#if reportMe.FEEDBACK == ''>
									<span style="font-size:12px;">暂无处理意见！</span>
								<#else>
									<span style="color:red;font-weight:700;font-size:14px;">处理意见 ： </span>
									<span style="font-size:12px;color:red;">${reportMe.FEEDBACK}</span>
								</#if>
							</div>
						</div>
						<div class="zm-item-meta zm-item-comment-el" id="zh-question-meta-wrap">
							<span class="answer-date-link-wrap meta-item"> 
								<#if (reportMe.S_ATIME?length > 10)>
									${reportMe.S_ATIME?substring(0,10)} 
								<#else>
									${reportMe.S_ATIME}
								</#if>
							</span>
						</div>
						
						<div id="zh-question-collapsed-wrap" class="zh-question-answer-wrapper" style="display: block;"></div>
						<!--------------------------一个单元----------结束------------------->
						</#list>
						</#if>
						</@zhidao_report_list>
		            </div>
		        </div>
		    </div>
		    <!-------------------------------举报信息------------结束--------------------------------------------->
	   </#if>
	   <!------------------点击		举报		应该显示的区域块儿----------------结束------------------------->
	   
	   <#if mark='followed'>
	   <!--------------------------------点击		关注者		应该显示的区域块儿---------------结束-------------------------------------------->
			    <@zhidao_follow_list debugName="关注人列表" userId='${who}' count=10 isFollow="Followed">
			    <#if tag_list?size!=0 >
			    <div class="zm-profile-section-wrap">
				  <div class="zm-profile-section-head"> 
				  	<span class="zm-profile-section-name">
				  		<a href="#">${user.USER_NAME}</a>被${page.ALLNUM}人关注
				  	</span> 
				  	<a href="#" class="zg-right zg-link-litblue-normal zm-profile-answer-page-return js-mark-default">返回个人主页</a> 
				  </div>
				  <div class="zm-profile-section-list">
				    <div>
				      <div id="js-mark-followed-list" page="1" class="zh-general-list clearfix">
				        <#list tag_list as person>
				        <div class="zm-profile-card zm-profile-section-item zg-clear">
				          <div class="zg-right"> 
				          	<!--<a class="zg-btn zg-btn-follow zm-rich-follow-btn small nth-0" id="tf-e592f20aad872ef9aa0b72db5fc65ab8" href="javascript:;" data-follow="m:button">关注</a>--> 
				          </div>
				          <a href="#" userName=${person.user.USER_CODE} class="zm-item-link-avatar" data-tip="p$t$hugo-wong-9" title="Hugo Wong"> 
				          	<#if person.user.USER_IMG??>
								<#list person.user.USER_IMG?split("?") as src>
									<#if src_index == 0>
										<img class="zm-item-img-avatar" src="${src}?size=50x50">
									</#if>
								</#list>
							<#else>
								<img class="zm-item-img-avatar" src="/sy/theme/default/images/common/rh-lady-icon.png?size=40x40">
							</#if>
				          </a>
				          <div class="zm-list-content-medium">
				            <h2 class="zm-list-content-title">
				            	<a title="" class="zg-link" href="#" userName=${person.user.USER_CODE} data-tip="p$t$hugo-wong-9">
				            		${person.user.USER_NAME}
				            	</a>
				            </h2>
				            <div class="zg-big-gray">${person.user.USER_POST}</div>
				            <div class="details zg-gray"> 
				            	<a class="zg-link-gray-normal" href="#" target="_blank">${person.followCount} 关注者</a> / 
				            	<a class="zg-link-gray-normal" href="#" target="_blank">${person.askCount} 出题</a> / 
				            	<a class="zg-link-gray-normal" href="#" target="_blank">${person.answerCount}回复</a> / 
				            	<a class="zg-link-gray-normal" href="#" target="_blank">${person.likeVoteCount}赞同</a> 
				            </div>
				          </div>
				        </div>
				        </#list>
				      </div>
				      <a id="js-mark-followed-more" aria-role="button" class="zg-btn-white zu-button-more">
				      	<!--<i class="spinner-gray"></i>正在加载-->
				      	加载更多
				      </a> 
				     </div>
				  </div>
				</div>
				</#if>
				</@zhidao_follow_list>
	   <!--------------------------------点击		关注者		应该显示的区域块儿---------------结束-------------------------------------------->	   
	   </#if>		   
			   </div>
			  </div>
			  
			  
		  	<div class="zd-main-sidebar">
		  		
		  		<!------------------查询知道用户的擅长领域--------开始------------------>
		  		<@zhidao_user_subject debugName="知道用户个人擅长领域" userId="${who}">
		  		<div class="zd-profile-side-following">
		  			<h3 id="spec-edit" style="display:inline;">
		  				擅长
		  			</h3>
		  			<ul id="js-subject-ul">
		  				<#if subjectList?size == 0>
		  					此人还没有擅长领域！
		  				<#else>
			  				<#list subjectList as subject>
			  				<li class="li-css">${subject.CHNL_NAME}</li>
			  				<input type="hidden" name="categoryID" value="${subject.CHNL_ID}" />
			  				<input type="hidden" name="categoryName" value="${subject.CHNL_NAME}" />
			  				</#list>
		  				</#if>
		  			</ul>
		  		</div>
		  		</@zhidao_user_subject>
		  		<!------------------查询知道用户的擅长领域--------结束------------------>
		  		
		  		
		  		<!------------------关注了---------开始----------------->
		  		<@zhidao_follow_person debugName="关注了" count="10" isFollow="isFollow" userId='${who}'>
		  		<div class="zd-profile-side-following">
					<h3>
						关注了
						<a class="js-mark-follows" href=""><strong id="fllowUsersSize">${followBean._OKCOUNT_}</strong></a>
						人
					</h3>
					<!--用户头像 -->
					<div class="list zu-small-avatar-list zg-clear" style="margin-bottom:5px;">
						<#list followBean._DATA_ as person>
						<a usercode="admin" href="#" class="css-follow-user-img myUseImg" title="${person.DATA_DIS_NAME}">
							<#if person.DATA_ID__IMG??>
								<#list person.DATA_ID__IMG?split("?") as src>
									<#if src_index == 0>
										<img class="css-follow-user" src="${src}?size=40x40">
									</#if>
								</#list>
							<#else>
								<img class="css-follow-user" src="/sy/theme/default/images/common/rh-lady-icon.png?size=40x40">
							</#if>
						</a>
						</#list>
					</div>
				</div>
				</@zhidao_follow_person>
				<!------------------关注了---------结束----------------->
				<!------------------关注者---------开始----------------->
				<@zhidao_follow_person debugName="关注者" count="10" isFollow="" userId='${who}'>
				<div class="zd-profile-side-following">
					<h3>
						被
						<a class="js-mark-followed" href=""><strong id="fllowUsersSize">${followBean._OKCOUNT_}</strong></a>
						人关注
					</h3>
					<!--用户头像 -->
					<div class="list zu-small-avatar-list zg-clear" style="margin-bottom:5px;">
						<#list followBean._DATA_ as person>
						<a usercode="admin" href="#" class="css-follow-user-img myUseImg" title="${person.USER_CODE__NAME}">
							<#if person.USER_CODE__IMG??>
								<#list person.USER_CODE__IMG?split("?") as src>
									<#if src_index == 0>
										<img class="css-follow-user" src="${src}?size=40x40">
									</#if>
								</#list>
							<#else>
								<img class="css-follow-user" src="/sy/theme/default/images/common/rh-lady-icon.png?size=40x40">
							</#if>
						</a>
						</#list>
					</div>
				</div>
				</@zhidao_follow_person>
				<!------------------关注者---------结束----------------->
			  <@zhidao_follow debugName="关注信息" userCode='${who}'>
			    <!--<div class="zd-profile-side-following clearfix">
			        <a class="item js-mark-follows" href="">
			            <span class="zd-gray-normal">关注了</span>
			            <br>
			            <strong> followCounter </strong>
			            <label> 人 </label>
			        </a>
			        <a class="item js-mark-followed" href="">
			            <span class="zd-gray-normal">关注者</span>
			            <br>
			            <strong id="followedConter">followedCounter</strong>
			            <label>人</label>
			        </a>
			    </div>-->
			    
			    
			    <div class="zd-profile-side-section">
					<div class="zd-side-section-inner">
						<h3>
			                	关注的主题
			                <a href="#">
			                    <strong>
			                        ${quesFollowCounter}
			                    </strong>
			                </a>
			                	个
			                <!--<span style="float:right;">更多</span>-->
			            </h3>
			            <@zhidao_my_follow_question debugName="关注的主题列表" userId="${who}" count="10" page="1">
			            <#list questionBean._DATA_ as question>
			            <dl class="my-follow-dl-css">
			            	<dd class="my-follow-dd-css">
			            		<a target="_self" title="${question.DATA_DIS_NAME}" href="javascript:view('${question.DATA_ID}','${question.DATA_DIS_NAME}');" class="right_font">
			            			<#if question.DATA_DIS_NAME?length gt 15>
			            				${question.DATA_DIS_NAME?substring(0,15)}...
			            			<#else>
			            				${question.DATA_DIS_NAME}
			            			</#if>
			            		</a>
			            	</dd>
			            </dl>
			            </#list>
			            </@zhidao_my_follow_question>
					</div>
				</div>
			    
			    
			    <div class="zd-profile-side-section">
			        <div class="zd-side-section-inner clearfix">
			            <h3>
			                	关注了
			                <a href="#">
			                    <strong>
			                        ${categoryList?size}
			                    </strong>
			                </a>
			                	 个分类
			            </h3>
			            <div class="zd-profile-side-topics" id="zh-profile-following-topic">
			             <#list categoryList as category>
			                <a class="zd-list-avatar-link" href="javascript:viewCategory('${category.DATA_ID}');">
			                	${category.DATA_DIS_NAME}
			                    <!-- <img class="zd-list-avatar-medium zd-profile-side-topic-avatar" src="http://p2.zhimg.com/be/a2/bea2e1c35_m.jpg"
			                    title="${category.DATA_DIS_NAME}"> -->
			                </a>
			             </#list>
			            </div>
			        </div>
			    </div>
			    
			    
			    <div class="zd-profile-side-section">
			        <div class="zd-side-section-inner">
			           <span class="zd-gray-normal">
			                	个人知道主页
			                <strong id="js-countUser-strong">XXX</strong>人/<strong id="js-countReq-strong">XXX</strong>次浏览
			            </span>
			        </div>
			        <!--<div class="zd-side-section-inner">
			           <span class="zd-gray-normal">
			                	被浏览
			                <strong id="js-countReq-strong">
			                    XXX
			                </strong>
			               	 次
			            </span>
			        </div>-->
			        <!--<div class="zd-side-section-inner">
			           <span class="zd-gray-normal">
			                	最近浏览时间
			                <strong id="js-time-strong">
			                    XXXX-XX-XX XX:XX
			                </strong>
			            </span>
			        </div>-->
			    </div>
			    </@zhidao_follow>
			 </div>
			 
			  </@zhidao_user>
			    <!--------------------------end of aside--------------------------------------->
			</div>
		</div>
<#include "/SY_COMM_ZHIDAO/zhidao_footer.ftl">
	</div>
	<!---------------返回顶部的div------开始--------------->
	<div id="zd-goto-top" style="position:fixed;bottom:30px; right:20px; margin-top:5px;"></div>
	<!---------------返回顶部的div------结束--------------->
	<script type="text/javascript" src="/sy/comm/cms/js/iframeAutoHeight.js"></script>
</body>
</html>
