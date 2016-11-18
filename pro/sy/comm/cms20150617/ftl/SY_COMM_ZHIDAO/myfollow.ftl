<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我的关注</title>
<#include "/SY_COMM_ZHIDAO/config_constant.ftl">
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/baidu_style_files/module_fe8dd525.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/baidu_style_files/module_c595d1cf.css">
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/myfollow.css"/>
<script type="text/javascript" src="/sy/base/frame/jquery-1.8.2.min.js"></script>
<script>
	var FireFlyContextPath ="";
	var tab_num = '${tab}',
	    tmplId='${userZhidao_center_tmpl_id}';
</script>
<script type="text/javascript" src="/sy/base/frame/constant.js"></script>
<script type="text/javascript" src="/sy/base/frame/tools.js"></script>
<script type="text/javascript" src="/sy/base/frame/platform.js"></script>
<script type="text/javascript" src="/sy/base/frame/jquery.tmpl.js"></script>
<script type="text/javascript" src="/sy/comm/cms/js/zhidao.js"></script>
<script type="text/javascript" src="/sy/comm/zhidao/js/myfollow.js"></script>
<!---------------返回顶部的Js/CSS------开始--------------->
<link rel="stylesheet" type="text/css" href="/sy/comm/zhidao/css/zd-goto-top.css">
<script type="text/javascript" src="/sy/comm/zhidao/js/zd-goto-top.js"></script>
<!---------------返回顶部的Js/CSS------结束--------------->
<#noparse>
<script id="js-follow-people-Template" type="text/x-jquery-tmpl">
	 <div class="zd-grid-list">
			<div class="nomal w100">
				<img class="zd-pic" src="${user.USER_IMG}" alt=""/>
				<a class="zd-user-name" href="javascript:othersZhidao('${tmplId}','${DATA_ID}');">
					${DATA_DIS_NAME}
				</a>
			</div>
			<div class="nomal w100">${user.DEPT_NAME}</div>    
			<div class="nomal w100">${user.USER_POST}</div>
			<div class="nomal w80  ">
				{{each 	specialist}}
					{{if ($index) == 0}}
						<a href="">${$value.CHNL_NAME}&nbsp;</a>
					{{/if}}
					{{if ($index+1) %2==0 }}
						<!--<br/>-->
					{{/if}}
				{{/each}}
			</div>
			<div class="nomal w140 tr">
				{{if latestTime }}
					${parent.$.timeago(parent.Format.substr(0,10,latestTime))}
				{{/if}}
			</div>
			<div class="nomal w60 tr">
				<a href="#" name="focus" class="js-person-follow fr zd-btn zd-btn-follow zd-rich-follow-btn zd-btn-unfollow" data-id="${DATA_ID}">取消关注</a>
			</div>
	   </div> 
</script>
<script id="js-follow-question-Template" type="text/x-jquery-tmpl">
	 <div class="zd-grid-list">
		<div class="nomal w140">
			<a href="javascript:view('${DATA_ID}','${DATA_DIS_NAME}');">
				${parent.Format.limit(10,DATA_DIS_NAME)}
			</a>
		</div>
		<div class="nomal w80 tc">${Q_ANSWER_COUNTER}</div>   
		<div class="nomal w80 tc">${Q_COMMENT_COUNTER}</div>
		<div class="nomal w80 tc">${Q_READ_COUNTER}</div>
		<div class="nomal w140 tr">
			${parent.$.timeago(parent.Format.substr(0,10,S_MTIME))}
		</div>
		<div class="nomal w60 tr">
			<a href="#" name="focus" class="js-question-follow fr zd-btn zd-btn-follow zd-rich-follow-btn zd-btn-unfollow" data-id="${DATA_ID}">取消关注</a>
		</div>
   </div>
</script>
<script id="js-my-question-Template" type="text/x-jquery-tmpl">
	<div class="zd-grid-list">
		<div class="nomal w140">
			<a href="javascript:view('${Q_ID}','${Q_TITLE}');">
				${parent.Format.limit(10,Q_TITLE)}
			</a>
		</div>
		<div class="nomal w100 tc">${Q_ANSWER_COUNTER}</div>    
		<div class="nomal w100 tc">${Q_COMMENT_COUNTER}</div>
		<div class="nomal w100 tc">${Q_READ_COUNTER}</div>
		<div class="nomal w100 tr">${parent.$.timeago(parent.Format.substr(0,10,S_MTIME))}</div>
   </div>
</script>
<script id="js-follow-category-Template" type="text/x-jquery-tmpl">
	 <div class="zd-grid-list">
		<div class="nomal w250">
			<a href="javascript:viewCategory('${DATA_ID}');">
				${parent.Format.limit(15,DATA_DIS_NAME)}
			</a>
		</div>
		<div class="nomal w180">
			{{if Q_TITLE}}
				<a href="javascript:view('${Q_ID}','${Q_TITLE}');">
					${parent.Format.limit(9,Q_TITLE)}
				</a>
			{{/if}}
			</div>    
        <div class="nomal w140 tr">
        	{{if QA_MTIME}}
        		${parent.$.timeago(parent.Format.substr(0,10,QA_MTIME))}
        	{{/if}}	
        </div>
        <div class="nomal w60 tr">
				<a href="#" name="focus" class="js-category-follow fr zd-btn zd-btn-follow zd-rich-follow-btn zd-btn-unfollow" data-id="${DATA_ID}">取消关注</a>
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
<div class="zd-wrapper">
  <div class="zd-container">
    <div class="zd-main">
    <@current_user debugName="用户信息" >
      <div class="zd-m-content">
        <div class="zd-user-info"> 
          <span class="act"> <a href="#" title="查看更多" target="_blank">  <span class="more"> </span> </a> </span>
          <h2 class="block-title">我的关注</h2>
          <div class="basic-info box">
            <div class="media">
              <div class="title"> 关注信息： </div>
            </div>
            <div class="zd-count">
              <ul>
              <@zhidao_follow debugName="关注信息" userCode='${user.USER_CODE}'>
                <li class="first"> 关注的人<b>${followCounter}</b> </li>
                <li> 关注的问题<b>${quesFollowCounter}</b></li>
                <li> 提出的问题 <b> ${askCount} </b> </li>
                <li> 关注的分类 <b> ${categoryList?size} </b> </li>
               </@zhidao_follow>
              </ul>
           </div>
          </div>
        </div>
      </div>
       <div class="zd-grid">
        	<ul class="tabControl" id="tabControl">
                <li class="tab-item current">
                    <a href="#js-follow-people" title="关注的人"> 关注的人</a>
                </li>
                <li class="line">
                    |
                </li>
                <li class="tab-item">
                    <a href="#js-follow-question" title="关注的问题"> 关注的问题 </a>
                </li>
                 <li class="line">
                    |
                </li>
                <li class="tab-item">
                    <a href="#js-my-question" title="提出的问题"> 提出的问题 </a>
                </li>
                 <li class="line">
                    |
                </li>
                <li class="tab-item">
                    <a href="#js-follow-category" title="关注的分类"> 关注的分类 </a>
                </li>
                <li class="slide" style="left:0;">
                    <div class="icon">
                    </div>
                </li>
                <li class="hover" id="hover" style="left: 0px;">
                </li>
            </ul>
            <div id="js-follow-people">
            	<div class="zd-grid-header">
                    <div class="nomal w100">姓名</div>
                    <div class="nomal w100">单位/部门</div>    
                    <div class="nomal w100">职务</div>
                    <div class="nomal w80">专家领域</div>
                    <div class="nomal w140 tr">最近问答时间</div>
                    <div class="nomal w60 tr"></div>
                </div>
            	<ul class="tabContent">
                    <li>
                       <div class="ui-pager pager-center">
                            <a href="javascript:;"class="btn-white btn-load-more">
               							 更多
            				</a>
                        </div>
                    </li>
                </ul>
            </div>
            <!-------------------end of #js-follow-people------------------------>
             <div id="js-follow-question" style="display:none">
            	<div class="zd-grid-header">
                    <div class="nomal w140">问题</div>
                    <div class="nomal w80 tc">回答数量</div>    
                    <div class="nomal w80 tc">评论次数</div>
                    <div class="nomal w80 tc">浏览次数</div>
                    <div class="nomal w140 tr">最近活动时间</div>
                    <div class="nomal w60 tr"></div>
                </div>
            	<ul class="tabContent">
                    <li>
                       <div class="ui-pager pager-center">
                       		 <a href="javascript:;"class="btn-white btn-load-more">
               							 更多
            				</a>
                        </div>
                    </li>
                </ul>
            </div>
            <!-------------------end of #js-follow-question------------------------>
            <div id="js-my-question" style="display:none">
            	<div class="zd-grid-header">
                    <div class="nomal w140">问题</div>
                    <div class="nomal w100 tc">回答数量</div>    
                    <div class="nomal w100 tc">评论次数</div>
                    <div class="nomal w100 tc">浏览次数</div>
                    <div class="nomal w100 tr">最新活动时间</div>
                </div>
            	<ul class="tabContent">
                    <li>
                        
                       <div class="ui-pager pager-center">
                       		 <a href="javascript:;"class="btn-white btn-load-more">
               							 更多
            				</a>
                        </div>
                    </li>
                </ul>
            </div>
            <!-------------------end of #js-my-question------------------------>
            <div id="js-follow-category" style="display:none">
            	<div class="zd-grid-header">
                    <div class="nomal w250">分类名称</div>
                    <div class="nomal w180">最近问答</div>    
                    <div class="nomal w140 tr">问答时间</div>
                    <div class="nomal w60 tr"></div>
                </div>
            	<ul class="tabContent">
                    <li>
                       <div class="ui-pager pager-center">
                       		 <a href="javascript:;"class="btn-white btn-load-more">
               							 更多
            				</a>
                        </div>
                    </li>
                </ul>
            </div>
            <!-------------------end of #js-follow-category------------------------>
         </div>
    </div>
    <div class="zd-right-side">
      <div class="zd-rs-content">
        <h2 class="zd-rs-title"> </h2>
        <div class="zd-profile">
          <div class="zd-profile-name">${user.USER_NAME}</div>
          <div class="zd-profile-photo"><img src="${user.USER_IMG}"></div>
        </div>
      
      </div>
    </div>
      </@current_user>
  </div>
</div>
<!---------------返回顶部的div------开始--------------->
<div id="zd-goto-top"></div>
<!---------------返回顶部的div------结束--------------->
</body>
</html>
