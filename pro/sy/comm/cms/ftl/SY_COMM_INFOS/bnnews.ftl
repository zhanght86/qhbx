<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${data.NEWS_SUBJECT!"没有标题"}</title>
<#include "/SY_COMM_INFOS/globalinfos.ftl">
<link rel="stylesheet" type="text/css" href="/sy/comm/news/css/news.css"/>

<style type="text/css">
#d-top {bottom: 10px;float: right;position: fixed;right: 20px;z-index: 10;}
.print {position: absolute; right: 10px;}

  
</style>
</head>
<body style="margin-top:0px;">
<#include "/SY_COMM_INFOS/config_constant.ftl">
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" newsid="${data.NEWS_ID}">
  <tr>
    <td valign="top" bgcolor="#FFFFFF">
    	<table width="1086px" border="0" align="center" cellpadding="0" cellspacing="0" class="mt5">
			<tr>
    			<td align="center">
    				<div width="1081px"><img style ="width:1088px;height:107px;" src="/sy/comm/cms/img/logo.jpg"/></div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="news_container">
						<div class="news_box" style="border-top:1px #999 solid;">
							<div class="news_content">${data.NEWS_BODY!""}</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</td>
</table>
<div id="d-top">
	<a title="回到顶部" onclick="document.body.scrollTop=0;document.documentElement.scrollTop=0;parent.document.body.scrollTop=0;parent.document.documentElement.scrollTop=0;this.blur();return false;" 
		href="javascritp:;">
		<img alt="TOP" src="/sy/comm/news/img/top.png" style="border:0px !important;">
	</a>
</div>
</body>
</html>
