<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" dir="ltr">
<head>
<title>栏目列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<#include "global.ftl"/>
<#include "/SY_COMM_BBS/config_constant.ftl" />
<link href="/sy/comm/bbs/css/forum.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="/sy/comm/cms/js/page.js"></script>
</head>
<body>
<@topic_list debugName="获取栏目下讨论主题" chnlId="${data.CHNL_ID}" counter="10">

<script type="text/javascript">
	var currentPage = "${_PAGE_.NOWPAGE }";
	var pageNum = "${_PAGE_.PAGES }";
	function doPage(page){
		window.location = "<@chnlUrl data.CHNL_ID 1 />?group=${group}";
	}
	function publish(){
		var url = "/bbs/tmpl/${publish_tmpl_id}.html?CHNL_ID=${CHNL_ID}&SITE_ID=${data.SITE_ID}";			
		var opts={'scrollFlag':true , 'url':url,'tTitle':"发帖",'menuFlag':1};
		parent.Tab.open(opts);
	}
</script>

<#if group="true">
	<@coms_html comsId="GROUP_NAV" siteId="${data.SITE_ID}" comparam="{'titleBar':'portal-title-none'}">
		${coms.AREA}
	</@coms_html>
</#if>
<div id="page">
    <div id="content" class="clearfix">
        <div id="main">
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
                	<@channel_list debugName="${data.CHNL_NAME}" channelId="${data.CHNL_ID}">
					<#list tag_list as channel2>
					
					<#if channel2.CHNL_ID=data.CHNL_ID>
					<dl class="bbs_son active">
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
		<!-- 列表_begin -->
        <div class="actions">
        	<div class="buttons">
                <a title="发表新帖" class="new_topic" href="javascript:publish();"></a>
            </div>
            <div class="wrapper">
                <div class="pagination" align="center">
				 	<span><input type="button" value="首页" onclick="first();"></span>
					<span><input type="button" value="上一页" onclick="pre();"></span>
				  	<span><input type="button" value="下一页" onclick="next();"></span>
				  	<span><input type="button" value="末页" onclick="last();"></span>
				  	转到第<input type="text" id="query" size="2">页
				  	<input type="button" value="GO" onclick="doG0();">&nbsp;
				  	当前第${_PAGE_.NOWPAGE }页，总共${_PAGE_.PAGES }页，${_PAGE_.ALLNUM }条记录
				</div>
            </div>
        </div>
		<style type="text/css">
			table{
				background:#FAFAFA;
				text-align:center;
			}
			table .t{
				background: url("/sy/comm/bbs/img/bbs_titlebar.gif") repeat-x scroll center top transparent;
			    color: #666666;
			    font-size: 14px;
				font-weight:bold;
			    height: 34px;
			    line-height: 34px;
				text-align:center;
			}
			table tbody tr td{
				border-bottom:1px #ccc dashed;
				height:40px;
				vertical-align:middle;
			}
		</style>
        <table width="100%" cellpadding="0" cellspacing="2" border="0">
            <thead>
                <tr>
                    <td class="t" colspan="2">
                        	主题
                    </td>
                    <td class="t" width="50">
                        	回复
                    </td>
                    <td class="t" width="100">
                       	 作者
                    </td>
                    <td class="t" width="50">
                        	阅读
                    </td>
                    <td class="t" width="150">
                       	 最新回复
                    </td>
                </tr>
            </thead>
            <tbody>
            	
            <#list tag_list as topic>
                <tr>
                    <td width="30"><img src="/sy/comm/bbs/img/unread_topic.gif"/></td>
                    <td align="left">
                        <a href="javascript:doView('${topic.TOPIC_ID}','${topic.TOPIC_TITLE}');">${topic.TOPIC_TITLE}</a>
                    </td>
                    <td >
                        ${topic.COMMENT_COUNTER!0}
                    </td>
                    <td>
                        <#if topic.S_UNAME??>${topic.S_UNAME}<#else>${topic.S_USER}</#if>
                    </td>
                    <td>
                        ${topic.TOPIC_READ_COUNTER!0}
                    </td>
					<@topic_new_comment_list debugName="最新回复" counter="1" topicId="${topic.TOPIC_ID}">
                    <td> 
						<#list tag_list as comment>  	
						${comment.S_UNAME!comment.S_USER}，
						<script type="text/javascript">
							document.write(getTime('${comment.S_CTIME}'));
						</script>	
						</#list>
                    </td>
					</@topic_new_comment_list>
                </tr>
				</#list>
			</tbody>
		</table>
		<div class="actions">
            <div class="buttons">
                <a title="发表新帖" class="new_topic" href="javascript:publish();"></a>
            </div>
            <div class="wrapper">
                <div class="pagination" align="center">
				 	<span><input type="button" value="首页" onclick="first();"></span>
					<span><input type="button" value="上一页" onclick="pre();"></span>
				  	<span><input type="button" value="下一页" onclick="next();"></span>
				  	<span><input type="button" value="末页" onclick="last();"></span>
				  	转到第<input type="text" id="query" size="2">页
				  	<input type="button" value="GO" onclick="doG0();">&nbsp;
				  	当前第${_PAGE_.NOWPAGE }页，总共${_PAGE_.PAGES }页，${_PAGE_.ALLNUM }条记录
				</div>
            </div>
        </div>
		<!-- 列表_end -->
	</div>
</div>
</@topic_list> 
</body>
</html>
