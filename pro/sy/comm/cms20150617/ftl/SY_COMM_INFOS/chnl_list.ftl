<#include "/SY_COMM_NEWS/config_constant.ftl">
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>栏目列表(蓝色)</title>
<script type="text/javascript" src="/sy/base/frame/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="/sy/comm/cms/js/page.js"></script>
<script type="text/javascript" src="/sy/base/frame/constant.js"></script>
<script type="text/javascript" src="/sy/base/frame/tools.js"></script>
<script type="text/javascript" src="/sy/base/frame/platform.js"></script>
<link href="/sy/comm/news/theme/css/layout.css" rel="stylesheet" type="text/css"/>
<link href="/sy/theme/default/common.css" rel="stylesheet" type="text/css"/>
<link href="/sy/theme/default/base.css" rel="stylesheet" type="text/css"/>
<style type="text/css">
body{margin:0;font:14px Verdana, Arial, Helvetica, sans-serif; color:#42515A; background:white; word-break:break-all;}
ul.list li.toptext a h3{text-align:center; font-weight:bold; font-size:16px; color:#f54100;}
.rgt{font-size:12px;}
dl.zt{width:260px; margin:5px; padding-bottom:10px;}
dl.zt dt,dl.zt dd.text{width:160px;}
ul.zxk{width:94%; margin:5px auto; padding:2px;}
ul.zxk li{width:100%; float:left; height:25px; line-height:25px;border-bottom:1px dashed #ccc; margin-left:10px;}
</style>
<script type="text/javascript">	
	var currentPage;
	var pageNum;
	jQuery(function(){
		currentPage = "${_PAGE_.NOWPAGE }";
		pageNum = "${_PAGE_.PAGES }";
	});
	function doView(id,name){
		var url = "/cms/SY_COMM_NEWS/" + id + ".html";	
		if("${group}"=="true"){
			window.location = url + "?group=true";
			return false;
		}		
		var opts={'scrollFlag':true , 'url':url,'tTitle':name,'menuFlag':3};
		top.Tab.open(opts);
	}
	function doPage(page){
		window.location = "/cms/SY_COMM_CMS_CHNL/" + "${data.CHNL_ID}" + "/index_"+page+".html";
		if("${group}"=="true"){
			window.location += "?group=true";
		}
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
	<div class='portal-box' style='min-height:252px;'>
		<div class='portal-box-title'>
			<span class="portal-box-title-icon"></span>
			<span class="portal-box-title-label">${data.CHNL_NAME!""}</span>
		</div>
		<div class='portal-box-con'>
			<ul class="zxk box">
	        <#list _DATA_ as news>
				<li> · <a href="javascript:doView('${news.NEWS_ID}','${news.NEWS_SUBJECT}');">
					   		${news.NEWS_SUBJECT}
					   </a>
					   <div align="right" style="display:inline;float:right;">${news.NEWS_TIME}</div>
				</li>
			</#list>
			</ul>
			
			<#if _DATA_?size gt 0>
			<div align="center" style="margin-top: 10px;">
			 	<span><input type="button" value="首页" onclick="first();"></span>
				<span><input type="button" value="上一页" onclick="pre();"></span>
			  	<span><input type="button" value="下一页" onclick="next();"></span>
			  	<span><input type="button" value="末页" onclick="last();"></span>
			  	转到第<input type="text" id="query" size="2">页
			  	<input type="button" value="GO" onclick="doG0();">&nbsp;
			  	当前第${_PAGE_.NOWPAGE }页，总共${_PAGE_.PAGES }页，${_PAGE_.ALLNUM }条记录
			</div>
			<#else>&nbsp;&nbsp;&nbsp;&nbsp;该栏目暂无数据
			</#if>
		</div>
	</div>
</div>
<div class="portal-target wp29 floatLeft">
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
