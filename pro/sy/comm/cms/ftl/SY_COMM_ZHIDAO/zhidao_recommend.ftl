<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>精彩推荐</title>
<#include "/SY_COMM_ZHIDAO/config_constant.ftl">
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/baidu_style_files/module_fe8dd525.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/baidu_style_files/module_c595d1cf.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/zhidao_recommend.css"/>
<script type="text/javascript" src="/sy/base/frame/jquery-1.8.2.min.js"></script>
<script>
	var FireFlyContextPath ="";
	var tmplId='${userZhidao_center_tmpl_id}';
</script>
<script type="text/javascript" src="/sy/base/frame/constant.js"></script>
<script type="text/javascript" src="/sy/base/frame/tools.js"></script>
<script type="text/javascript" src="/sy/base/frame/platform.js"></script>
<script type="text/javascript" src="/sy/comm/wenku/js/lazy-load-img.js"></script>
<script type="text/javascript" src="/sy/base/frame/jquery.tmpl.js"></script>
<script type="text/javascript" src="/sy/comm/cms/js/zhidao.js"></script>
<script type="text/javascript" src="/sy/comm/zhidao/js/zhidao_recomment.js"></script>
<!---------------返回顶部的Js/CSS------开始--------------->
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/zd-goto-top.css">
<script type="text/javascript" src="/sy/comm/zhidao/js/zd-goto-top.js"></script>
<!---------------返回顶部的Js/CSS------结束--------------->
<#noparse>
<script id="js-qa-Template" type="text/x-jquery-tmpl">
<div class="zd-grid">
   <div class="zd-grid-header clearfix">
   		<div class="zd-pic">
   			{{if S_USER__IMG}}
   				<a href="javascript:othersZhidao('${tmplId}','${S_USER}');">
        			<img src="${S_USER__IMG}" alt=""/>
        		</a>
        	{{else}}
        		<img src="/sy/comm/zhidao/img/default.png" alt="" />
        	{{/if}}
        </div>
   		<div class="zd-question">
            <h2 class="zd-title">
                <span>
                	{{if S_USER__NAME}}
                		<a href="javascript:othersZhidao('${tmplId}','${S_USER}');">${S_USER__NAME}</a>
                	{{else}}
                		<a href="#">游客</a>
                	{{/if}}
                </span>：<a href="javascript:view('${Q_ID}','${Q_TITLE}');">${Q_TITLE}</a>
            </h2>
            <div class="zd-bus">
                <span>
                		${parent.Format.substr(0,10,ask.S_ATIME)}
                </span>
                <span><!--<a href="">-->XXX<!--</a>-->人/${ask.Q_READ_COUNTER}次浏览</span>
               <!--  <span><a href="">20</a>人关注</span> -->
                <span><!--<a href="">-->${Q_COMMENT_COUNTER}<!--</a>-->人评论</span>
                <span><!--<a href="">-->${ask.Q_ANSWER_COUNTER}<!--</a>-->人回答</span>
            </div>
        </div>
   </div>
    <!-------------end of zd-grid-header---------->
    <div class="zd-grid-content" data="${Q_ID}">
    	<div class="zd-question-detail"> 
   	  		{{html ask.Q_CONTENT}} 
   	  	</div>
   	  	  <!---------------answer----------------->
	   	  {{if answer}}
	      <div class="zd-grid-answer clearfix">
	      		<div class="zd-grid-answer-arrow">
	            	<em></em><i></i>
	            </div>
	            <a  href="javascript:othersZhidao('${tmplId}','${answer.S_USER}');">
	            	<img class="zd-big-pic" src="${answer.S_USER__IMG}" alt=""/>
	            </a>
	        <div class="zd-answer-detail">
	        	<p>
	        		<h2>
	        			<span class="zd-answer-name">
	        				{{if answer.S_USER__NAME}}
	            				<a  href="javascript:othersZhidao('${tmplId}','${answer.S_USER}');">${answer.S_USER__NAME}</a>
	                    	{{else}}
	                    		<a href="#">游客</a>
	                    	{{/if}}
	        				:
	        			</span>
	        		</h2>
	        	</p>
	        	<p>${answer.A_CONTENT}</p>
	            <p>
	            	<span class="zd-gray">
	                		${parent.Format.substr(0,19,answer.S_ATIME)}
	            	</span>
	            	{{if answer.readMore == "true"}}
	            		<span class="zd-answer-read-more">
	            			<a href="javascript:viewAnswer('${Q_ID}','${Q_TITLE}');">
	            				阅读全部 >>
	            			</a>
	            		</span>
	            	{{/if}}
	            </p>
	     </div>
	    </div>
	    {{/if}}
    </div>
    <!-------------end of zd-grid-content---------->
</div>
</script>
<script id="js-noqa-Template" type="text/x-jquery-tmpl">
<div class="zd-grid">
   <div class="zd-grid-header clearfix">
   		<div class="zd-pic">
        </div>
   		<div class="zd-question">
            <h2 class="zd-title">
               
            </h2>
            <div class="zd-bus">
            </div>
        </div>
   </div>
    <div class="zd-grid-content tc">
    	 	${msg}
    </div>
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
<div class="zd-wrapper clearfix">
  <div id="zd-container-js" class="zd-container">
  <!------------------begin of zd-content-header----------------------------->
      <div class="zd-header">
        <div class="zd-tab"> 
         
          <ul class="tabControl" id="tabControl">
                <li class="tab-item current">
                    <a href="#js-latest-qa" title="最新问答"> 最新问答</a>
                </li>
                <li class="line">
                    |
                </li>
                <li class="tab-item">
                    <a href="#js-invited-specialist-qa" title="领导问答"> 领导问答 </a>
                </li>
                 <li class="line">
                    |
                </li>
                <li class="tab-item">
                    <a href="#js-dept-qa" title="职能部门问答"> 职能部门问答</a>
                </li>
                 <li class="line">
                    |
                </li>
                <li class="tab-item">
                    <a href="#js-bzn-specialist-qa" title="业务专家问答"> 业务专家问答 </a>
                </li>
                <li class="line">
                    |
                </li>
                <li class="tab-item">
                    <a href="#js-bzn-workmate-share" title="同事分享"> 同事分享 </a>
                </li>
                
                <li class="slide" style="left:0;">
                    <div class="icon">
                    </div>
                </li>
                <li class="hover" id="hover" style="left: 0px;">
                </li>
            </ul>
        </div>
      </div>
      <!------------------end of zd-content-header----------------------------->
       <div id="js-latest-qa">
	        <div class="ui-pager pager-center">
	            <a class="btn-white btn-loading btn-load-more" href="">更多</a>
	        </div>
      </div>     
      <!---------------end of js-latest-qa----------------------------> 
      <div id="js-invited-specialist-qa" style="display:none">
        	<div class="ui-pager pager-center">
            	<a class="btn-white btn-loading btn-load-more" href="">更多</a>
        	</div>
 	  </div>
        
      <!---------------end of js-leader-qa----------------------------> 
       <div id="js-dept-qa" style="display:none"> 
       		<div class="ui-pager pager-center">
            	<a class="btn-white btn-loading btn-load-more" href="">更多</a>
        	</div>
       </div>
      <!---------------end of js-dept-qa----------------------------> 
       <div id="js-bzn-specialist-qa" style="display:none">
       		<div class="ui-pager pager-center">
            	<a class="btn-white btn-loading btn-load-more" href="">更多</a>
        	</div>
       </div>
      <!---------------end of js-specialist-qa----------------------------> 
    </div>
  <!---------------end of content---------------------------->
  <div class="zd-right-side">
  <@zhidao_recommend_list debugName="推荐列表"  count="3">
      <div class="zd-rs-content">
     	<div class="zd-rs-grid">
           <h2 class="zd-rs-title">猜你喜欢</h2>
        <!-- </div> -->
        <!------------end of myAskList------------------->
      	<!-- <div class="zd-rs-grid"> -->
           <!-- <h2 class="zd-rs-title">最近提问</h2> -->
           <ul>
           		<#list myAskList as obj>
	           		<li>
	                	<div class="zd-rs-item">
	                		 <a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${obj.S_USER}');">
	                		 	<img src="${obj.S_USER__IMG}" alt=""/>
	                		 </a>
	           		 		 <div class="zd-rs-name">
	           		 		 	<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${obj.S_USER}');">
	           		 		 		${obj.S_USER__NAME}：
	           		 		 	</a>
	           		 		 </div>
	                        <div class="zd-rs-question">
	                        	<a href="javascript:view('${obj.Q_ID}','${obj.Q_TITLE}');" title="${obj.Q_TITLE}">${obj.Q_TITLE}</a>
	                        </div>
	                    </div>
	                </li>
                </#list>
          <!--  </ul>
                   </div>
                   ----------end of myAskList-----------------
                   <div class="zd-rs-grid">
           <h2 class="zd-rs-title">关注的问题</h2>
           <ul> -->
           		<#list myQusetFollowList as obj>
	           		<li>
	                	<div class="zd-rs-item">
	                		<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${obj.DATA_OWNER}');">
	                			<img src="${obj.DATA_OWNER__IMG}" alt=""/>
	                		</a>
	           		 		 <div class="zd-rs-name">
	           		 		 	<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${obj.DATA_OWNER}');">
	           		 		 		${obj.DATA_OWNER__NAME}：
	           		 		 	</a>
	           		 		 </div>
	                        <div class="zd-rs-question">
	                        	<a href="javascript:view('${obj.DATA_ID}','${obj.DATA_DIS_NAME}');" title="${obj.DATA_DIS_NAME}">${obj.DATA_DIS_NAME}</a>
	                        </div>
	                    </div>
	                </li>
                </#list>
         <!--   </ul>
                   </div>
                   ----------end of myAskList-----------------
                   <div class="zd-rs-grid">
           <h2 class="zd-rs-title">关注的人</h2>
           <ul> -->
           		<#list myFollowPersonList as obj>
	           		<li>
	                	<div class="zd-rs-item">
	                		<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${obj.DATA_ID}');">
	                			<img src="${obj.DATA_ID__IMG}" alt=""/>
	                		</a>
	           		 		<div class="zd-rs-name">
	           		 			<a  href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${obj.DATA_ID}');">
	           		 				${obj.DATA_ID__NAME}：
	           		 			</a>
	           		 				${obj.ACT_CODE__NAME!"没有最新动态"}
	           		 		</div>
	                        <div class="zd-rs-question">
	                        <#if obj.ACT_CODE=='ZHIDAO_FOLLOW_CATEGORY'>
	                        	<a href="javascript:viewCategory('${obj.ACTIVITY_DATA_ID}');" title="${obj.ACTIVITY_DATA_DIS_NAME}">
	                        		${obj.ACTIVITY_DATA_DIS_NAME}
	                        	</a>
	                        <#elseif obj.ACT_CODE=="ZHIDAO_FOLLOW_USER">
	                        	<a href="javascript:othersZhidao('${userZhidao_center_tmpl_id}','${obj.ACTIVITY_DATA_ID}');" title="${obj.ACTIVITY_DATA_DIS_NAME}">
	                        		${obj.ACTIVITY_DATA_DIS_NAME}
	                        	</a>
	                        <#else>
	                        	<a href="javascript:view('${obj.ACTIVITY_DATA_ID}','${obj.ACTIVITY_DATA_DIS_NAME}');" title="${obj.ACTIVITY_DATA_DIS_NAME}">
	                        		${obj.ACTIVITY_DATA_DIS_NAME}
	                        	</a>
	                        </#if>
	                        </div>
	                    </div>
	                </li>
                </#list>
           </ul>
      <!--   </div>
        ----------end of myAskList-----------------
        <div class="zd-rs-grid">
           <h2 class="zd-rs-title">关注的分类</h2>
           <ul> -->
           		<#list myCategoryList as obj>
	           		<li>
	                	<div class="zd-rs-item">
	                		<img src="" alt=""/>
	           		 		<div class="zd-rs-name">
	           		 			<a href="javascript:viewCategory('${obj.DATA_ID}','${obj.DATA_DIS_NAME}');" title="${obj.DATA_DIS_NAME}">
	           		 				${obj.DATA_DIS_NAME}
	           		 			</a>
	           		 		</div>
	                    </div>
	                </li>
                </#list>
           </ul>
        </div>
        <!------------end of myAskList------------------->
      </div>
      </@zhidao_recommend_list>
   </div>
   <!-----------------end of right side-------------------------->
   	<!---------------返回顶部的div------开始--------------->
	<div id="zd-goto-top"></div>
	<!---------------返回顶部的div------结束--------------->
</div>
</body>
</html>
