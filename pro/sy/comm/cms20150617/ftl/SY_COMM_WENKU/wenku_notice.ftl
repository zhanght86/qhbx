<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>公告内容</title> <#include "global.ftl"/>
<link rel="stylesheet" type="text/css" href="/sy/comm/news/css/news.css" />
<link rel="stylesheet" type="text/css" href="/sy/comm/poll/poll.css" />
<script type="text/javascript" src="/sy/comm/poll/poll.js"></script>
<link href="/sy/comm/news/theme/css/layout.css" rel="stylesheet"
	type="text/css">
<style type="text/css">
#d-top {
	bottom: 10px;
	float: right;
	position: fixed;
	right: 20px;
	z-index: 2000;<#-- 编辑器覆盖了返回顶部按钮，重置z-index-->
}
</style>
</head>

<body>
	<#include "/SY_COMM_WENKU/config_constant.ftl"> <@wenku_notice
	debugName="公告页面" id="${NOTICE_ID}" picCount="-1" attachCount="-1"
	pollCount="1">
	<table width="100%" border="0" align="center" cellpadding="0"
		cellspacing="0">
		<tr>
			<td valign="top" bgcolor="#FFFFFF" align="center">
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="mt5">
					<tr>
						<td></td>
					</tr>
					<tr>
						<td style="padding-bottom: 10px; padding: 0 1%;">
							<div class="news_container">
								<div class="news_box">
									<div class="news_title">${notice.NOTICE_TITLE!"没有标题"}</div>
									<div class="news_info"><#if (notice.S_MTIME)??>
										发布日期：${notice.S_MTIME}&nbsp;&nbsp; </#if> 作者： <#if
										(notice.S_USER__NAME)??> ${notice.S_USER__NAME} <#else>
										${notice.S_USER} </#if></div>
									<div class="news_content">${notice.NOTICE_CONTENT!""}</div>

									<!-- 投票 begin-->
									<div class="news_vote" id="vote_item"></div>
									<!-- 投票 end-->
								</div>
								<!-- 评论 begin-->
								<div class="news_comment" id="comment_show"></div>
								<!-- 评论 begin-->
							</div>
						</td>
					</tr>
				</table>
			</td>
			</tr>
	</table>

	<script type="text/javascript">
		jQuery(document).ready(function() {

		});
	</script>
	<div id="d-top">
		<a title="回到顶部"
			onclick="document.body.scrollTop=0;document.documentElement.scrollTop=0;parent.document.body.scrollTop=0;parent.document.documentElement.scrollTop=0;this.blur();return false;"
			href="javascritp:;"> <img alt="TOP"
			src="/sy/comm/news/img/top.png">
		</a>
	</div>
	</@wenku_notice>
</body>
</html>
