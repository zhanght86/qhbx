<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Untitled Document</title>
<#include "/SY_COMM_ZHIDAO/config_constant.ftl">
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/userzhidao_center.css"/>
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/baidu_style_files/module_fe8dd525.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/baidu_style_files/module_c595d1cf.css">
<script type="text/javascript" src="/sy/base/frame/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="/sy/base/frame/jquery.tmpl.js"></script>
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
<!-------------------回答模版-------开始------------------------->
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
<!-------------------回答模版-------结束------------------------->

<!----------------------提问模版-------开始-------------------->
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
                    ${Q_ANSWER_COUNTER} 个回答
                    <span class="zd-bull">•</span>
                    <span class="quesFollowCounter">
                        ${quesfollowCounter} 
                    </span> 人关注
                    <span class="zd-bull">•</span>
                    <a class="zd-question-follow meta-item" data-id="${Q_ID}" data-dis-name="${Q_TITLE}">
                        <i class="z-icon-follow"></i>
                        	<span>关注问题</span>
                    </a>
                </div>
            </div>
        </div>
</script>
<!----------------------提问模版-------结束-------------------->
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
                        <img src="${answer.S_USER__IMG}" class="zd-list-avatar" />
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
              		<span>关注问题</span>
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
		     		<img src="/sy/theme/default/images/common/rh-male-icon.png" class="pic" />
	     		{{else}}
	     			<img src="${DATA_ID_IMG}" class="pic" />
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
	<div class='layout-center-div' style="background-color: #FAFAFA;background: url(/sy/comm/zhidao/baidu_style_files/body_back_3107edb8_2.png) repeat-x 0 75px white;">
		<div class="line">
			<!-- userbar -->
		</div>

		<#include "/SY_COMM_ZHIDAO/zhidao_search.ftl">
		<div id="body" class="container">
			<#include "/SY_COMM_ZHIDAO/zhidao_navigation.ftl">
			<div id="sub-menu">
				<ul>
			
				</ul>
			</div>
			<!-----------头部-------------------->
			
			
			<div class="zd-wrapper clearfix">
			<@user debugName="用户信息" userId='${who}'>
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
				        		<span class="zm-profile-header-bio">，知道平台领导</span>
				        	<#elseif specialist.SPEC_TYPE == 2>
				        		<span class="zm-profile-header-bio">，知道平台职能部门专家</span>
				        	<#elseif specialist.SPEC_TYPE == 3>
				        		<span class="zm-profile-header-bio">，知道平台业务专家</span>
				        	<#else>
				        	</#if>
			        	</@zhidao_only_specialist>
			        </div>
			        <div class="zd-profile-header-img-wrapper clearfix"> 
			        	<a class=""> 
			        		<img alt="${user.USER_NAME}" src="<@setUserImg user />" class="zd-profile-header-img zd-avatar-big zd-avatar-editor-preview" /> 
			        	</a> 
			        </div>
			        <div class="zd-profile-header-info">
			          <div class="zd-profile-header-user-describe">
			            <div class="items">
			              <div class="item editable-group"> 
			              	<i class="zd-icon location"></i> 
			                <span class="info-wrap"> 
			                    <span class="location item" title="公司">
			                          	 XX公司
			                    </span> 
			                </span> 
			              </div>
			              <div class="item editable-group " data-name="education"> 
			              	<i class="zd-icon education"></i> 
			                <span class="info-wrap"> 
			                	<span class="education item" title="${user.DEPT_CODE}">${user.DEPT_NAME}</span> 
			                </span> 
			              </div>
			              <a class="zd-profile-header-user-detail zd-link-litblue-normal js-goto-detail" href="javascript:void(0);"> 
			              	<i class="zd-profile-header-icon"></i> <span>查看详细资料</span> 
			              </a> 
			            </div>
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
			                <strong id="aLikeVoteCounter-strong">28840</strong>赞同
			            </span>
			            <span class="zd-profile-header-user-thanks">
			            	<span class="zd-profile-header-icon"></span>
			                <strong id="aBestCounter-strong">7148</strong>回答被提问者采纳
			            </span>
			            <span class="zd-profile-header-user-thanks">
			            	<span class="zd-profile-header-icon"></span>
			                <strong id="aAdminBestCounter">7148</strong>回答被管理员采纳
			            </span>
			            <span class="zd-profile-header-user-share">
			            	<span class="zd-profile-header-icon"></span>
			                <strong>XXX</strong>被分享
			            </span> 
			        </div>
			        <div class="zd-profile-header-op-btns">
			         <a href="#" name="focus" id="zd-person-follow" class="zd-right zd-btn zd-btn-follow zd-rich-follow-btn">关注</a>
			        </div>
			      </div>
			      <!----------------end of header-operation----------------------->
			      <div class="navbar">
			      <@zhidao_question_ask_follow_counter debugName="提问数和回答数" userId='${who}'>
			        <div class="navbar-inner clearfix">
				        <a class="item active js-mark-default" href="#" style="border-left:none;">  
				        	主页 
				        </a>
				        <a class="item js-mark-answers" href="">
				        	回答
				        	<span class="zd-gray-normal num">${answerCounter}</span>
				        </a>
				        <a class="item js-mark-questions" href="">
				        	提问
				       	 <span class="zd-gray-normal num">${askCounter}</span>
				        </a>
				        <a class="item " href="#${user.USER_CODE}-activity">
				        	最新动态
				       	 <span id="activity-span" class="zd-gray-normal num"></span>
				        </a>
			        </div>
			      </@zhidao_question_ask_follow_counter>
			      </div>
			    </div>
			    <!-----------------end of header------------------------->
			    
		<#if mark==''>	    
		<!--------------------------------首页应该显示的区域块儿---------------开始-------------------------------------------->
				
				
				<!------------------回答列表-------开始--------------------->
				<@zhidao_answer_list debugName="回答列表" userId='${who}' summaryLength="50" count="5" >
			    <#if tag_list?size!=0 >
			    <div class="zd-profile-section-wrap zd-profile-answer-wrap">
				      <div class="zd-profile-section-head"> 
				      	<a class="zd-profile-section-title js-mark-answers" target="_self" name="${user.USER_CODE}-answer" href=''>
					        <h2 class="zd-profile-section-name">${user.USER_NAME}的回答</h2>
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
							             	<#if answer.Q_TITLE?length gt 20>
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
			    
			   	<!--------------------提问列表--------开始------------------------>
			    <@zhidao_ask_list debugName="提问列表" userId='${who}' count=5 isFollowCount="isFollowCount">
			    <#if tag_list?size!=0 >
			    <div class="zd-profile-section-wrap zd-profile-ask-wrap" id="zd-profile-ask-wrap">
			        <div class="zd-profile-section-head">
			        	<a class="zd-profile-section-title js-mark-questions" name="${user.USER_CODE}-ask" href="">
			                <h2 class="zd-profile-section-name">${user.USER_NAME}的提问</h2>
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
			                        ${ask.Q_ANSWER_COUNTER!0} 个回答
			                        <span class="zd-bull">•</span>
			                        <span class="quesFollowCounter">
				                        <@zhidao_question_follow_counter debugName="提问关注" questionId='${ask.Q_ID}'>
				                        		${quesfollowCounter} 
				                        </@zhidao_question_follow_counter>
			                        </span> 人关注
			                        <span class="zd-bull">•</span>
			                        <a class="zd-question-follow meta-item" data-id="${ask.Q_ID}" data-dis-name="${ask.Q_TITLE}">
			                            <i class="z-icon-follow"></i>
			                            	<span>关注问题</span>
			                        </a>
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
			            <a id="zd-load-more" class="zd-btn-white zd-btn-more" style="opacity:0.3;">
			                	更多
			            </a>
			        </div>
			    </div>
			    <!-------------------------------end of 最新动态--------------------------------------------------------->
	   <!--------------------------------首页应该显示的区域块儿--------------结束-------------------------------------------->
	   </#if>
		
	   <#if mark=='answers'>		
	   <!--------------------------------点击		回答		应该显示的区域块儿---------------开始-------------------------------------------->
	   			<@zhidao_answer_list debugName="回答列表" userId='${who}' summaryLength="50" count="5" >
			    <#if tag_list?size!=0 >
	   			<div class="zm-profile-section-wrap zm-profile-answer-page">
				  <div class="zm-profile-section-head"> 
				  	<span class="zm-profile-section-name">
				  		<a href="/people/kaifulee">${user.USER_NAME}</a> 的回答
				  	</span>
				  	<a href="/people/kaifulee" class="zg-right zg-link-litblue-normal zm-profile-answer-page-return js-mark-default">返回个人主页</a>
				  	<!--
				    <div class="zm-profile-section-sort-wrap">
				      <label>排序方式：</label>
				      <a class="zg-link-blue-normal" href="/people/kaifulee/answers?order_by=vote_num">赞同数</a> 
				      <span class="zg-bull">|</span> 
				      <a class="zg-link-gray" href="/people/kaifulee/answers?order_by=created">回答时间</a> 
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
								             	<#if answer.Q_TITLE?length gt 20>
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
			          	更多
			          </a> 
			        </div>
				  </div>
				</div>
				</#if>
			    </@zhidao_answer_list>
	   <!--------------------------------点击		回答		应该显示的区域块儿---------------结束-------------------------------------------->	
	   </#if>
	   <#if mark='questions'>
	   <!--------------------------------点击		提问		应该显示的区域块儿---------------开始-------------------------------------------->
	   			<@zhidao_ask_list debugName="提问列表" userId='${who}' count=5 isFollowCount="isFollowCount">
			    <#if tag_list?size!=0 >
	   			<div class="zm-profile-section-wrap zm-profile-ask-wrap">
				  <div class="zm-profile-section-head"> 
				  	<span class="zm-profile-section-name">
				  		<a href="/people/kaifulee">${user.USER_NAME}</a> 的提问
				  	</span> 
				  	<a href="/people/kaifulee" class="zg-right zg-link-litblue-normal zm-profile-answer-page-return js-mark-default">返回个人主页</a> 
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
			                        ${ask.Q_ANSWER_COUNTER!0} 个回答
			                        <span class="zd-bull">•</span>
			                        <span class="quesFollowCounter">
				                        <@zhidao_question_follow_counter debugName="提问关注" questionId='${ask.Q_ID}'>
				                        		${quesfollowCounter} 
				                        </@zhidao_question_follow_counter>
			                        </span> 人关注
			                        <span class="zd-bull">•</span>
			                        <a class="zd-question-follow meta-item" data-id="${ask.Q_ID}" data-dis-name="${ask.Q_TITLE}">
			                            <i class="z-icon-follow"></i>
			                            	<span>关注问题</span>
			                        </a>
			                    </div>
			                </div>
			             </div>
			          	 </#list>
				      </div>
				      <a id="js-mark-ask-more" aria-role="button" class="zg-btn-white zu-button-more">
			          	更多
			          </a>
				    </div>
				  </div>
				</div>
				</#if>
			    </@zhidao_ask_list>
	   <!--------------------------------点击		提问		应该显示的区域块儿---------------结束-------------------------------------------->
	   </#if>
	   <#if mark='follows'>
	   <!--------------------------------点击		关注了		应该显示的区域块儿---------------开始-------------------------------------------->
			    <@zhidao_follow_list debugName="关注人列表" userId='${who}' count=5 isFollow="isFollow">
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
				          <a href="javascript:void(0);" class="zm-item-link-avatar" userName=${person.user.USER_CODE} data-tip="p$t$kaifulee" data-original_title=""> 
				          	<img class="zm-item-img-avatar" src="${person.user.USER_IMG}"> 
				          </a>
				          <div class="zm-list-content-medium">
				            <h2 class="zm-list-content-title">
				            	<a class="zg-link" href="javascript:void(0);" userName=${person.user.USER_CODE} data-tip="p$t$kaifulee" data-original_title="">${person.user.USER_NAME}</a>
				            </h2>
				            <div class="zg-big-gray">${person.user.USER_POST}</div>
				            <div class="details zg-gray"> 
				            	<a class="zg-link-gray-normal" href="#" target="_blank">${person.followCount} 关注者</a> / 
				            	<a class="zg-link-gray-normal" href="#" target="_blank">${person.askCount} 提问</a> / 
				            	<a class="zg-link-gray-normal" href="#" target="_blank">${person.answerCount}回答</a> / 
				            	<a class="zg-link-gray-normal" href="#" target="_blank">${person.likeVoteCount}赞同</a> 
				            </div>
				          </div>
				        </div>
				        </#list>
				       </div>
				       <a id="js-mark-follow-more" aria-role="button" class="zg-btn-white zu-button-more">
			          	 更多
			           </a> 
				    </div>
				  </div>
				</div>
				</#if>
				</@zhidao_follow_list>
	   <!--------------------------------点击		关注了		应该显示的区域块儿---------------结束-------------------------------------------->
	   </#if>
	   <#if mark='followed'>
	   <!--------------------------------点击		关注者		应该显示的区域块儿---------------结束-------------------------------------------->
			    <@zhidao_follow_list debugName="关注人列表" userId='${who}' count=5 isFollow="Followed">
			    <#if tag_list?size!=0 >
			    <div class="zm-profile-section-wrap">
				  <div class="zm-profile-section-head"> 
				  	<span class="zm-profile-section-name">
				  		<a href="#">${user.USER_NAME}</a>被${page.ALLNUM}人关注
				  	</span> 
				  	<a href="/people/kaifulee" class="zg-right zg-link-litblue-normal zm-profile-answer-page-return js-mark-default">返回个人主页</a> 
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
				          	<img class="zm-item-img-avatar" src="${person.user.USER_IMG}"> 
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
				            	<a class="zg-link-gray-normal" href="#" target="_blank">${person.askCount} 提问</a> / 
				            	<a class="zg-link-gray-normal" href="#" target="_blank">${person.answerCount}回答</a> / 
				            	<a class="zg-link-gray-normal" href="#" target="_blank">${person.likeVoteCount}赞同</a> 
				            </div>
				          </div>
				        </div>
				        </#list>
				      </div>
				      <a id="js-mark-followed-more" aria-role="button" class="zg-btn-white zu-button-more">
				      	更多
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
		  		<div class="zd-profile-side-following">
		  			<div class="zg-gray-normal">
	                	简介
	                	<span id="desc-edit" class="myself" style="float:right;">编辑</span>
		            </div>
		            <!------------------查询知道用户的个人简介--------开始------------------>
		            <@zhidao_user_desc debugName="知道用户个人简介" userId="${who}">
                    <p id="look-p" style="font-size:12px;">
                    	<#if (USER_DESC?length gt 110)>
                    	${USER_DESC?substring(0,110)}...
                    	<#else>
                    	${USER_DESC}
                    	</#if>
                    </p>
                    <textarea id="edit-textarea" style="font-size:12px; display:none;" cols="47" rows="6">${USER_DESC}</textarea>
		  			</@zhidao_user_desc>
		  			<!------------------查询知道用户的个人简介--------结束------------------>
		  		</div>
		  		
		  		<!-------------------专家领域-----开始-------------------->
		  		<!--<zhidao_only_specialist_subject debugName="专家领域" userId='who'>-->
		  		
		  		<!------------------查询知道用户的擅长领域--------开始------------------>
		  		<@zhidao_user_subject debugName="知道用户个人擅长领域" userId="${who}">
		  		<div class="zd-profile-side-following">
		  			<div class="zg-gray-normal">
		  				擅长
			  			<span id="spec-edit" class="myself" style="float:right;">编辑</span>
		  			</div>
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
		  		
		  		<!--</zhidao_only_specialist_subject>-->
		  		<!-------------------专家领域-----开始-------------------->
		  		<!------------------关注了---------开始----------------->
		  		<@zhidao_follow_person debugName="关注了" count="10" isFollow="isFollow" userId='${who}'>
		  		<div class="zd-profile-side-following">
					<div class="zg-gray-normal">
						关注了
						<a><strong id="fllowUsersSize">${followBean._OKCOUNT_}</strong></a>
						人
						<a class="js-mark-follows" href="">
							<span style="float:right;">更多</span>
						</a>
					</div>
					<!--用户头像 -->
					<div class="list zu-small-avatar-list zg-clear" style="margin-bottom:5px;">
						<#list followBean._DATA_ as person>
						<a usercode="admin" href="#" class="css-follow-user-img myUseImg" title="${person.DATA_DIS_NAME}">
							<img class="css-follow-user" src="${person. DATA_ID__IMG}">
						</a>
						</#list>
					</div>
				</div>
				</@zhidao_follow_person>
				<!------------------关注了---------结束----------------->
				<!------------------关注者---------开始----------------->
				<@zhidao_follow_person debugName="关注者" count="10" isFollow="" userId='${who}'>
				<div class="zd-profile-side-following">
					<div class="zg-gray-normal">
						被
						<a><strong id="fllowUsersSize">${followBean._OKCOUNT_}</strong></a>
						人关注
						<a class="js-mark-followed" href="">
							<span style="float:right;">更多</span>
						</a>
					</div>
					<!--用户头像 -->
					<div class="list zu-small-avatar-list zg-clear" style="margin-bottom:5px;">
						<#list followBean._DATA_ as person>
						<a usercode="admin" href="#" class="css-follow-user-img myUseImg" title="${person.USER_CODE__NAME}">
							<img class="css-follow-user" src="${person.USER_CODE__IMG}">
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
			        <div class="zd-side-section-inner clearfix">
			            <div class="zg-gray-normal">
			                	关注了
			                <a href="#">
			                    <strong>
			                        ${categoryList?size} 个分类
			                    </strong>
			                </a>
			            </div>
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
						<div class="zg-gray-normal">
			                	关注的问题
			                <a href="#">
			                    <strong>
			                        ${quesFollowCounter} 个
			                    </strong>
			                </a>
			                <!--<span style="float:right;">更多</span>-->
			            </div>
			            <@zhidao_my_follow_question debugName="关注的问题列表" userId="${who}" count="5" page="1">
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
			 
			  </@user>
			    <!--------------------------end of aside--------------------------------------->
			</div>
		</div>
	</div>
	<!---------------返回顶部的div------开始--------------->
	<div id="zd-goto-top"></div>
	<!---------------返回顶部的div------结束--------------->
</body>
</html>
