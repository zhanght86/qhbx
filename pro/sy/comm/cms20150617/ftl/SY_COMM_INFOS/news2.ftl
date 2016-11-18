<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${data.NEWS_SUBJECT!"没有标题"}</title>
<#include "/SY_COMM_INFOS/globalinfos.ftl">
<link rel="stylesheet" type="text/css" href="/sy/comm/news/css/news.css"/>
<link rel="stylesheet" type="text/css" href="/sy/comm/poll/poll.css" />
<script type="text/javascript" src="/sy/comm/poll/poll.js"></script>

<style type="text/css">
#d-top {bottom: 10px;float: right;position: fixed;right: 20px;z-index: 10;}
.print {position: absolute; right: 10px;}

  
</style>
<script type ="text/javascript" >
function doPrint()
{
	if(window.print)
		window.print();
}
</script>
</head>
<body style="margin-top:0px;">
<#include "/SY_COMM_INFOS/config_constant.ftl">
<@infos_attach debugName="新闻页面" newsId="${data.NEWS_ID}" servid ="${data.servId}" auditid ="${data.AUDIT_ID}" picCount="-1" attachCount="-1" pollCount="1">
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" newsid="${data.NEWS_ID}">
  <tr>
    <td valign="top" bgcolor="#FFFFFF">
    	<table width="1086px" border="0" align="center" cellpadding="0" cellspacing="0" class="mt5">
			<tr>
    			<td align="center">
    				<div width="1081px"><img style ="width:1088px;height:80px;" src="/sy/comm/cms/img/head_logo_zhbx.gif"/></div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="news_container">
						<div class="news_box">
							<div class="news_title"><p>${data.NEWS_SUBJECT!"没有标题"}</p></div>
							<div class="news_info">
								<#if (data.NEWS_TIME)??>
									发布日期：${data.NEWS_TIME}&nbsp;&nbsp;
								</#if>		
								<#if (data.NEWS_SOURCE)??>
									来源：${data.NEWS_SOURCE}
								</#if>
							
									点击量 ： <span id="count"></span>		
								
						<a  class ="print" href="javascript:doPrint()"><img src="/sy/comm/cms/img/print.gif"/></a>
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
												
												<td><#if (attach.S_MTIME?length >10)>${attach.S_MTIME?substring(0,10)}<#else>${attach.S_MTIME}</#if></td>
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
	

 /* 点击量 */
 var newid = jQuery("table").attr("newsid");
 function count(newid){
		var data = {};
		data["_PK_"] = newid;
		var out = FireFly.doAct("SY_COMM_INFOS","increaseReadCounter",data);
		jQuery("#count").text(out["COUNTER"]);	
	}
  count(newid);
	/* 点击量 */
});
</script>
<div id="d-top">
	<a title="回到顶部" onclick="document.body.scrollTop=0;document.documentElement.scrollTop=0;parent.document.body.scrollTop=0;parent.document.documentElement.scrollTop=0;this.blur();return false;" 
		href="javascritp:;">
		<img alt="TOP" src="/sy/comm/news/img/top.png" style="border:0px !important;">
	</a>
</div>
</@infos_attach>
</body>
</html>
