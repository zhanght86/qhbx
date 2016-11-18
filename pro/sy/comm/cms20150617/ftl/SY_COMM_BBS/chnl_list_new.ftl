<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" dir="ltr">
<head >
<title>栏目列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<#include "global.ftl"/>
<#include "/SY_COMM_BBS/config_constant.ftl" />
<link href="/sy/comm/bbs/css/forum_new.css" rel="stylesheet" type="text/css"/>
<style type="text/css">
	.chnl_left_index{
		width:18%;height:600px;float:left;margin-left:10px;margin-top:20px;background-color:white;border:1px solid #7ea5cc;
	}
	.chnl_page{
		width:80%;float:right;margin-top:0px;
	}
</style>
<script type="text/javascript" src="/sy/comm/cms/js/page.js"></script>
<script type="text/javascript">
	
	function doPage(page){
		window.location = "/cms/SY_COMM_CMS_CHNL/${data.CHNL_ID}/index_"+page+".html";
	}
	function doGo(){
		var page = $(".page-input").val();
		window.location = "/cms/SY_COMM_CMS_CHNL/${data.CHNL_ID}/index_"+page+".html";
		
	}
	function publish(){
		var url = "/cms/SY_COMM_CMS_TMPL/${publish_tmpl_id}.html?CHNL_ID=${data.CHNL_ID}&SITE_ID=${data.SITE_ID}";			
		var opts={'scrollFlag':true , 'url':url,'tTitle':"发帖",'menuFlag':3};
		parent.Tab.open(opts);
	}
	$(document).ready(function(){
		var tt = '${type}';
		var chnl_name = '${chnl_name}';
		var divs = $(".con");
		var title = $(".title").text();
		divs.each(function(i,n){
			var as = $(n).find("a");
			as.each(function(i,m){
				var atext = $(m).text();
				if(atext.trim()==title.trim()){
					$(this).parents("div").show();
					$(this).css({color:"red"});
					$(this).parents("div").prev(".portal-box-title").find(".chnl_img").css({'background-position':'0px -67px'});
				}
			});
		});
		if(tt=='1'){
			$("#chnl_topic_hide").hide();
			$(".title").html(chnl_name);
			divs.each(function(i,n){
			var as = $(n).find("a");
			as.each(function(i,m){
				var atext = $(m).text();
				if(atext.trim()==title.trim()){
					$(this).css({color:"blue"});
				}
			});
			});
		}
		var chnls = $(".portal-box-title");
		chnls.click(function(){
			$(this).next("div").prevAll(".con").hide();
			$(this).next("div").nextAll(".con").hide();
			$(this).next("div").toggle();
			$(this).find(".chnl_img").css({'background-position':'0px -67px'});
			$(this).prevAll(".portal-box-title").find(".chnl_img").css({'background-position':'0px -17px'});
			$(this).nextAll(".portal-box-title").find(".chnl_img").css({'background-position':'0px -17px'});
		});
	});
</script>
</head>
<body>
<#if group="true">
	<@coms_html comsId="GROUP_NAV" siteId="${data.SITE_ID}" comparam="{'titleBar':'portal-title-none'}">
		${coms.AREA}
	</@coms_html>
</#if>
<div style='width:100%;'>
	<div class="chnl_left_index">
		<@coms_html comsId="SITE_BBS_CHANNEL_VIEW" siteId="${data.SITE_ID}" comparam="{'titleBar':'portal-title-none'}">
				${coms.AREA}
		</@coms_html>
	</div>
	<div class="chnl_page">
	    <div id="content" class="clearfix" style="margin-top:20px;">
	        <div id="main" style="background: white;">
	            <div id="forum_tab_show" class="clearfix"></div>
				<!-- 一级栏目栏目_begin -->
	            <div class="bbs_fath">
	                <h2>
	                	<span class="left">
	                		<img alt="${data.CHNL_NAME}" src="/sy/comm/bbs/img/bbs_ico_normal_new.png" />
							<span class="title">
								${data.CHNL_NAME}
							</span>
						</span>
						<#assign zhuti=0,huitie=0 />
						<span class="amount index${data_index}">${zhuti}主题，${huitie}回帖</span>
					</h2>
	                <div class="con">
	                	<@channel_list debugName="${data.CHNL_NAME}" channelId="${data.CHNL_PID}">
						<#list tag_list as channel2>
						<#if channel2.CHNL_ID=data.CHNL_ID>
						<dl class="bbs_son">
						<#else><dl class="bbs_son">
						</#if>
	                        <dt>
	                            <img alt="Android" src="/sy/comm/bbs/img/bbs_ico_android.png" />
	                        </dt>
							<dd>
								<strong>
									<a href="javascript:doChannel('${channel2.CHNL_ID}','${channel2.CHNL_NAME}');">${channel2.CHNL_NAME}</a>
								</strong>
								<@channel_msg debugName="--${channel2.CHNL_NAME}(主题|回帖)" channelId="${channel2.CHNL_ID}">
								<#assign zhuti=zhuti+tag.topicTotal,huitie=huitie+tag.commentTotal />
								<span class="amount">${tag.topicTotal!0}主题 / ${tag.commentTotal!0}回帖</span>
								</@channel_msg>
								<script type="text/javascript">
								  	//回写一级栏目主题数、评论数
									jQuery(".index${channel_index}").text("${zhuti}主题 ，${huitie}回帖");
							  	</script>
							</dd>
	                    </dl>
						</#list>
						</@channel_list>
	                 </div>
	              </div>
				  <!-- 一级栏目栏目_end -->
		    </div>
		    <div id="chnl_topic_hide">
		    <@topic_list debugName="获取栏目下讨论主题" chnlId="${data.CHNL_ID}" counter="5" page="${page}">
			<!-- 列表_begin -->
	        <div style='height:30px;margin-top: 18px;margin-bottom: 13px;'>
	        	<div style='float:left;width:60%'>
		        	<a class="rh-icon rhGrid-btnBar-a" href="javascript:publish();">
						<span class="rh-icon-inner"> 发帖&nbsp;&nbsp;</span>
						<span class="rh-icon-img btn-chat"></span>
					</a>
	        	</div>
				<div style='float:right'>
					<#list 1.._PAGE_.PAGES as x>
						<#if x== _PAGE_.NOWPAGE?number >
							<a class="current-page" href="javascript:;" onclick="doPage('${x}')" title="当前第${x}页">${x}</a>
						<#else>
							<a class="normal-page" href="#" onclick="doPage('${x}')" title="前往第${x}页">${x}</a>
						</#if>
						<#if x gt 10>
						<a class="normal-page" href="#" title="最后一页">_PAGE_.PAGES</a>
	                    <span class="page-span">
	                    	<input class='page-input' type="text" maxlength="10" id="query" size="2" >
							<a style='text-decoration: none;' href="javascript:;" onclick="javascript:doGo();">Go</a>
						</span>
						<#break>
						</#if>
					</#list>
				    <span class="page-span">
	                	<input class='page-input' type="text" maxlength="10" id="query" size="2" >
						<a style='text-decoration: none;' href="javascript:;" onclick="javascript:doGo();">GO</a>
					</span>
				</div>
	        </div>	
			<div style='height:25px;line-height:25px;background: white;'>
				<@channel_msg debugName="--${data.CHNL_NAME}(主题|回帖)" channelId="${data.CHNL_ID}">
				<span style='font-weight:bold;margin-left: 10px;'>${data.CHNL_NAME}</span><span>&nbsp;&nbsp;&nbsp;&nbsp;主题&nbsp;${tag.topicTotal!0},</span><span>&nbsp;回帖&nbsp;${tag.commentTotal!0}</span>
				</@channel_msg>
			</div>
			<div style='padding:5px;background: white;'>
		        <table width="100%" cellpadding="0" cellspacing="2" border="0">
		            <thead>
		                <tr style='border-top:1px #ccc dashed;'>
		                	<td class="t" width="30">&nbsp</td>
							<td class="t" width="100">作者</td>
		                    <td class="t" colspan="1"><font style="float:left;">主题</font> </td>
		                    <td class="t" width="50">回复 </td>
		                    <td class="t" width="50">阅读</td>
		                    <td class="t" width="150">最后更新</td>
		                </tr>
		            </thead>
		            <tbody>
		            	
		            <#list tag_list as topic>
		                <tr>
		                	<td width="30"><img src="/sy/comm/bbs/img/unread_topic.gif"/></td>
							<td>
		                        <#if topic.S_UNAME??>${topic.S_UNAME}<#else>${topic.S_USER}</#if>
		                    </td>
		                    <td align="left">
		                        <a href="javascript:doView('${topic.TOPIC_ID}','${topic.TOPIC_TITLE}');">${topic.TOPIC_TITLE}</a>
		                    </td>
		                    <td >
		                        ${topic.COMMENT_COUNTER!0}
		                    </td>
		                    <td>
		                        ${topic.TOPIC_READ_COUNTER!0}
		                    </td>
							<@topic_new_comment_list debugName="最后更新" counter="1" topicId="${topic.TOPIC_ID}">
		                    <td class="newInfo"> 
								<#list tag_list as comment>
								<div style="color: #6795C3;font-size: 15px;">
									${comment.S_UNAME!comment.S_USER}
								</div>
								<div><font>
								<script type="text/javascript">
									document.write(getTime('${comment.S_CTIME}'));
								</script></font>
								</div>
								</#list>
		                    </td>
							</@topic_new_comment_list>
		                </tr>
						</#list>
					</tbody>
				</table>			
			</div>
	        <div style='height:30px;margin-top: 15px;'>
	        	<div style='float:left;width:60%'>
		        	<a class="rh-icon rhGrid-btnBar-a" href="javascript:publish();">
						<span class="rh-icon-inner"> 发 帖&nbsp&nbsp</span>
						<span class="rh-icon-img btn-chat"></span>
					</a>
	        	</div>
				<div style='float:right'>
					<#list 1.._PAGE_.PAGES as x >
						<#if x== _PAGE_.NOWPAGE?number >
							<a class="current-page" href="javascript:;" onclick="doPage('${x}')" title="当前第${x}页">${x}</a>
						<#else>
							<a class="normal-page" href="#" title="前往第${x}页" onclick="doPage('${x}')">${x}</a>
						</#if>
						<#if x gt 10>
						<a class="normal-page" href="#" title="最后一页">_PAGE_.PAGES</a>
	                    <span class="page-span">
	                    	<input class='page-input' type="text" maxlength="10" id="query" size="2" >
							<a style='text-decoration: none;' href="javascript:;" onclick="javascript:doGo();">Go</a>
						</span>
						<#break>
						</#if>
					</#list>
				    <span class="page-span">
	                	<input class='page-input' type="text" maxlength="10" id="query" size="2" >
						<a style='text-decoration: none;' href="javascript:;" onclick="javascript:doGo();">GO</a>
					</span>
				</div>
	        </div>
	        </@topic_list>
	        </div>
			<!-- 列表_end -->
		</div>
	</div>
</div>
</body>
</html>
