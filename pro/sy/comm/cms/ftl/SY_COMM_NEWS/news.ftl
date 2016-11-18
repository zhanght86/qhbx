<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新闻内容</title>
<#include "global.ftl"/>
<link rel="stylesheet" type="text/css" href="/sy/comm/news/css/news.css"/>
<link rel="stylesheet" type="text/css" href="/sy/comm/poll/poll.css" />
<script type="text/javascript" src="/sy/comm/poll/poll.js"></script>
<link href="/sy/comm/news/theme/css/layout.css" rel="stylesheet" type="text/css" >
<style type="text/css">
#d-top {bottom: 10px;float: right;position: fixed;right: 20px;z-index: 2000;}<#-- 编辑器覆盖了返回顶部按钮，重置z-index-->
</style>
</head>

<body>
<#include "/SY_COMM_NEWS/config_constant.ftl">
<@channel debugName="主题所属栏目" channelId="${data.CHNL_ID}">
<@news_attach debugName="新闻页面" newsId="${data.NEWS_ID}" picCount="-1" attachCount="-1" pollCount="1">
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top" bgcolor="#FFFFFF" align="center">
    	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="mt5">
			<tr>
    			<td><#--
    				<#if group="true">
						<@coms_html comsId="GROUP_NAV" siteId="${channel.SITE_ID}" comparam="{'titleBar':'portal-title-none'}">
							${coms.AREA}
						</@coms_html>
						<@coms_html comsId="GROUP_NEWS_NAV" siteId="${channel.SITE_ID}" comparam="{'titleBar':'portal-title-none'}">
							${coms.AREA}
						</@coms_html>
						<#else>
						<@coms_html comsId="NEWS_NAV" siteId="${channel.SITE_ID}" comparam="{'titleBar':'portal-title-none'}">
							${coms.AREA}
						</@coms_html>
					</#if>
					-->
				</td>
			</tr>
			<!--
			<tr>
				<td style="margin:10px 0;padding:0 1%;align:left;" align="left">当前位置： 
					<a href="/cms/SY_COMM_CMS_CHNL/${channel.CHNL_ID}/index_1.html?group=${group}">
						${channel.CHNL_NAME!""}
					</a> &gt; 
					${data.NEWS_SUBJECT!""}
				</td>
			</tr>
			-->
			<tr>
				<td style="padding-bottom:10px;padding:0 1%;">
					<div class="news_container">
						<div class="news_box">
							<div class="news_title">${data.NEWS_SUBJECT!"没有标题"}</div>
							<div class="news_info">
								<#if (news.NEWS_TIME)??>
									发布日期：${data.NEWS_TIME}&nbsp;&nbsp;
								</#if>
								作者：
								<#if (news.NEWS_USER)??>
									${data.NEWS_USER__NAME}
									<#else> ${data.NEWS_USER__NAME}
								</#if>			
								<#if (news.NEWS_SOURCE)??>
									来源：${data.NEWS_SOURCE}
								</#if>
							</div>
							<div class="news_content">${data.NEWS_BODY!""}</div>
							
							<!-- 图片集 begin -->
							<#if tag.imgList?size gt 0>
							<div class="news_imgs">
								<#list tag.imgList as img>
								<div class="img_target" id="img_${img_index+1}">
									<div class="title" id="img_title">图片集(${img_index+1}/${tag.imgList?size})</div>
									<div align="center">
										<img border="0" src="/file/${img.FILE_ID}?size=${biger!''}" width="600" height="360">
									</div>
								</div>
								</#list>
								<div align="center" style="margin-top:10px;">
									<input type="button" id="pre" value="上一张"/>
									<input type="button" id="next" value="下一张"/>
								</div>
							</div>
							</#if>
							<!-- 图片集 end -->
							
							<!-- 附件 begin  -->
							<#if tag.attachList?size gt 0>
							<div class="news_attach">
								<table width="100%" cellpadding="6" cellspacing="0">
									<tr>
										<td colspan="4" style="font-weight:bold;">附件</td>
									</tr>
								      <#list tag.attachList as attach>
										<tbody>
									        <tr>
									            <td>
									              	  附件${attach_index+1}:
									            </td>
									            <td>
									                <a href="/file/${attach.FILE_ID}">${attach.FILE_NAME}</a>
									            </td>
												<td>${attach.S_UNAME!attach.S_USER!""}</td>
												<td>${attach.S_MTIME!""}</td>
									        </tr>
											<tr><td height="1" colspan="4" style="border-bottom:1px #cccccc dashed;"></td></tr>
										<tbody>
								      </#list>
								</table>
							</div>
							</#if>
							<!-- 投票 begin-->
							<div class="news_vote" id="vote_item"></div>
							<!-- 投票 end-->
						</div>
						<!-- 评论 begin-->
						<#if (data.NEWS_COMMENT_STATUS!0)?number != 0>
						<div class="news_comment" id="comment_show">
							
						</div>
						</#if>
						<!-- 评论 begin-->
					</div>
				</td>
			</tr>
		</table>
	</td>
</table>

<script type="text/javascript">	
jQuery(document).ready(function(){
	/* 投票_begin */
	(function vote(){
		if(!"${tag.pollId!''}"){
			return ;
		}
	    var opts = {
	        "sId": "SY_PLUG_SEARCH",
	        "pollId": "${tag.pollId!''}",
	        "pCon": jQuery("#vote_item")
	    };
	    listView = new rh.vi.poll(opts);
	    listView.show();
	})();
	/* 投票_end */

	/* 图片轮转_begin */
	var imgIndex = 1;
	jQuery(".img_target").hide();
	jQuery(".img_target").eq(imgIndex-1).show();
	jQuery("#pre").click(function(){
		jQuery(".img_target").hide();
		if(imgIndex-1>0){
			imgIndex--;
		}
		jQuery(".img_target").eq(imgIndex-1).show();
	});
	jQuery("#next").click(function(){
		jQuery(".img_target").hide();
		if(imgIndex<"${tag.imgList?size}"){
			imgIndex++;
		}
		jQuery(".img_target").eq(imgIndex-1).show();
	});
	/* 图片轮转_end */
	
	/* 评论_begin */
	(function news(){
		var opts = {"DATA_ID": "${data.NEWS_ID}", "SERV_ID":"SY_COMM_NEWS", "SHOWNUM":10,"NOWPAGE":1,"pCon":jQuery("#comment_show")};
	    var listView = new rh.vi.comment(opts);
	    listView.show();
	})();
	/* 评论_end */
});
</script>
<div id="d-top">
	<a title="回到顶部" onclick="document.body.scrollTop=0;document.documentElement.scrollTop=0;parent.document.body.scrollTop=0;parent.document.documentElement.scrollTop=0;this.blur();return false;" 
		href="javascritp:;">
		<img alt="TOP" src="/sy/comm/news/img/top.png">
	</a>
</div>
</@news_attach>
</@channel>
</body>
</html>
