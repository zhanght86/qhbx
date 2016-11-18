<#include "/SY_COMM_NEWS/config_constant.ftl">
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>栏目封面</title>
<script type="text/javascript" src="/sy/base/frame/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="/sy/comm/cms/js/page.js"></script>
<script type="text/javascript" src="/sy/base/frame/constant.js"></script>
<script type="text/javascript" src="/sy/base/frame/tools.js"></script>
<script type="text/javascript" src="/sy/base/frame/platform.js"></script>
<link href="/sy/comm/news/theme/css/layout.css" rel="stylesheet" type="text/css">
<link href="/sy/theme/default/common.css" rel="stylesheet" type="text/css">
<link href="/sy/theme/default/base.css" rel="stylesheet" type="text/css"/>
<style type="text/css">
body{margin:0;font:14px Verdana, Arial, Helvetica, sans-serif; color:#42515A; background:white; word-break:break-all;}
ul.list li.toptext a h3{text-align:center; font-weight:bold; font-size:16px; color:#f54100;}
.rgt{font-size:12px;}
dl.zt{width:260px; margin:5px; padding-bottom:10px;}
dl.zt dt,dl.zt dd.text{width:160px;}
ul.zxk{width:94%; margin:5px auto; padding:2px;}
ul.zxk li{width:100%; float:left; height:25px; line-height:25px;border-bottom:1px dashed #ccc; margin-left:10px;}
.news-channel .more{float:right;margin-right:10px;margin-top:5px;cursor:pointer;}
</style>
<script type="text/javascript">		
	function doView(id,name){
		var url = "/cms/SY_COMM_NEWS/" + id + ".html?group=${group}";			
		var opts={'scrollFlag':true , 'url':url,'tTitle':name,'menuFlag':1};
		top.Tab.open(opts);
	}
	function getMore(id,name){
		var url = "/cms/SY_COMM_CMS_CHNL/" + id + ".html";	
		if("${group}"=="true"){
			window.location = url + "?group=true";
			return false;
		}		
		var opts={'scrollFlag':true , 'url':url,'tTitle':name,'menuFlag':1};
		top.Tab.open(opts);
	}
</script>
</head>

<body class="portalBody">
<div class="portal-target wp100 floatLeft">
	<#if group="true">
		<@coms_html comsId="GROUP_NAV" siteId="${data.SITE_ID}" comparam="{'titleBar':'portal-title-none'}">
			${coms.AREA}
		</@coms_html>
		<@coms_html comsId="GROUP_NEWS_NAV" siteId="${data.SITE_ID}" comparam="{'titleBar':'portal-title-none'}">
			${coms.AREA}
		</@coms_html>
		<#else>
		<@coms_html comsId="NEWS_NAV" siteId="${data.SITE_ID}" comparam="{'titleBar':'portal-title-none'}">
			${coms.AREA}
		</@coms_html>
	</#if>
</div>
<div class="portal-target wp70 floatLeft">
	<@channel_list debugName="栏目下栏目" channelId="${data.CHNL_ID}" SITE_ID="${data.SITE_ID}">
	<#if tag_list?size gt 0>
	<#list tag_list as channel>
	<div class='portal-box news-channel' style='min-height:252px;'>
		<div class='portal-box-title'>
			<span class="portal-box-title-icon"></span>
			<span class="portal-box-title-label">${channel.CHNL_NAME}</span>
			<span class="more" onclick="javascript:getMore('${channel.CHNL_ID}','${channel.CHNL_NAME}')">查看更多&gt;&gt;</span>
		</div>
		<div class='portal-box-con'>
			<@news_list debugName="栏目下新闻列表" CHNL_ID="${channel.CHNL_ID}" count="10">
			<ul class="zxk box">
			<#if tag_list?size gt 0>
	        <#list tag_list as news>
				<li> · <a href="javascript:doView('${news.NEWS_ID}','${news.NEWS_SUBJECT}');">
						<@text_cut s="${news.NEWS_SUBJECT}" length="18">${text}</@text_cut>
					   </a>
					   <div align="right" style="display:inline;float:right;">${news.NEWS_TIME}</div>
				</li>
			</#list>
			<#else>&nbsp;&nbsp;&nbsp;&nbsp;该栏目暂无数据
			</#if>
			</ul>
	      </@news_list>
		</div>
	</div>
	</#list>
	</#if>
	</@channel_list>
</div>
<div class="portal-target wp30 floatLeft">
	<div class='portal-box'>
		<div class='portal-box-title'>
			<span class="portal-box-title-icon"></span>
			<span class="portal-box-title-label">新闻资讯排行</span>
		</div>
		<div class='portal-box-con'>
			<ul class="order">
			<@news_list debugName="新闻资讯排行" SITE_ID="${data.SITE_ID}" _ROWNUM_="9"> 
		  	<#list tag_list as news>
		  	<li><a href="javascript:doView('${news.NEWS_ID}','${news.NEWS_SUBJECT}');">
		  		<@text_cut s="${news.NEWS_SUBJECT}" length="18">${text}</@text_cut>
		  	</a></li>
		  	</#list>
		  	</@news_list>
			</ul>
		</div>
	</div>
	
	<div class='portal-box'>
		<div class='portal-box-title'>
			<span class="portal-box-title-icon"></span>
			<span class="portal-box-title-label">最新评论</span>
		</div>
		<div class='portal-box-con'>
			<@news_comment debugName="最新评论" count="4">
			<dl class="rmpl">
			<#list tag_list as comment>
				<dt>
					<span>
						<#if (comment.S_USER__NAME)??>
							${comment.S_USER__NAME}
							<#else>${comment.S_USER!"匿名用户"}
						</#if>
					</span> 
					对 <a href="javascript:doView('${comment.news.NEWS_ID}','${comment.news.NEWS_SUBJECT}');">${comment.news.NEWS_SUBJECT}</a> 评论道:</dt>
				<dd>${comment.C_CONTENT}</dd>
				<dd class="line"></dd>
			</#list>
			</dl>
			</@news_comment>
		</div>
	</div>
	
	<div class='portal-box' style="min-height:345px;">
		<div class='portal-box-title'>
			<span class="portal-box-title-icon"></span>
			<span class="portal-box-title-label">标题新闻</span>
		</div>
		<div class='portal-box-con'>
		   <@news_list debugName="标题新闻" SITE_ID="${data.SITE_ID}" _ROWNUM_="3" NEWS_TYPE="5">
		   <#list tag_list as news>	
			<dl class="list line zt box">
				<dt>
					<a href="javascript:doView('${news.NEWS_ID}','${news.NEWS_SUBJECT}');">
						<@text_cut s="${news.NEWS_SUBJECT}" length="11">${text}</@text_cut>
					</a>
				</dt>
				<dd class="pic">
					<a href="javascript:doView('${news.NEWS_ID}','${news.NEWS_SUBJECT}');">
						<img src="<@setPic news.NEWS_TITLE_IMAGE small/>" width="50" height="50">
					</a>
				</dd>
				<dd class="text">
					<@text_cut s="${news.NEWS_SUMMARY}" length="40">${text}</@text_cut>
				</dd>
			</dl>
		   </#list>
	       </@news_list>
		</div>
	</div>
</div>
</body>
</html> 
