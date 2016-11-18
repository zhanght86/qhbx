<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/SY_COMM_ZHIDAO/config_constant.ftl">
<#include "global.ftl"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>文库-公告-阅读</title>

<link rel="stylesheet" type="text/css" href="/sy/comm/wenku/css/wk-gg-read.css"/>
<script type="text/javascript" src="/sy/base/frame/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="/sy/comm/cms/js/zhidao.js"></script>
<script type="text/javascript">
	var currId='${NOTICE_ID}';
</script>
<script type="text/javascript" src="/sy/comm/wenku/js/wk-gg-read.js"></script>
</head>
<body>
<div id="wk-gg-wrap">
<div class="wk-gg-right">
  <div id="wk-gg-content">
    <div class="header"> 
    	<span class="gray"> 
	    	<span class="middot"></span>
	    	&nbsp;被浏览 
	    	<span class="js-read-count"></span>
	    	&nbsp;次 
	    	&nbsp;/&nbsp;
	    	浏览人数&nbsp;
	    	<span class="js-read-person-count"></span>
	    	&nbsp;人
      </span>
    </div>
    <div id="wk-gg-main">
      <h1></h1>
      <div id="wk-gg-artical">
        <div class="artical-info clearfix"> 
        	<a href=""> 
        		<img class="author-img"/>
        	</a> 
        	<a href="#"><span class="author-name"></span></a>
        </div>
        <div class="artical-body">
          <div class="artical-body-content"></div>
          <div class="artical-body-foot"></div>
        </div>
      </div>
    </div>
  </div>
</div>
  <!--------------------end of content----------------------------->
  <div id="wk-gg-aside">
    <div class="header">
    	<a href="javascript:void(0);" class="logo-zd"></a>
    </div>
    <div class="title"><span class="gray">全部</span></div>
    <div id="wk-gg-aside-nav" class="scroll-pane">
    	<ul>
	<@zhidao_notice_list debugName="知道公告" count="10" needMore="true"> 
		<#list tag_list as notice>
    		<li>
            	<a href="#" id="${notice.NOTICE_ID}" data="${notice.NOTICE_READ_COUNTER}">
                	<h2>${notice.NOTICE_TITLE}</h2>
                	<p>
                		<img src="${notice.USER_IMG}" alt="${notice.S_UNAME}" />
                		<strong>${notice.S_UNAME}</strong><strong>(${notice.DEPT_NAME})</strong>
                	</p>
                    <span class="view-count" title="${notice.NOTICE_READ_COUNTER}">${notice.NOTICE_READ_COUNTER}</span>
                </a>
            </li>
		</#list>
	 </@zhidao_notice_list>
        </ul>
    </div>
  </div>
</div>
</body>
</html>
