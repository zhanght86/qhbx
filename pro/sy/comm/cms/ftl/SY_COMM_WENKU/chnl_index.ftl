<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>首页</title>
	<#include "/SY_COMM_WENKU/config_constant.ftl">
	<link href="/sy/theme/default/common.css" rel="stylesheet" type="text/css"/>
	<link href="/sy/comm/wenku/css/style_daoh.css" rel="stylesheet" type="text/css"/>
	<link href="/sy/comm/wenku/css/style.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript">var FireFlyContextPath ='';</script>
	<script type="text/javascript" src="/sy/base/frame/jquery-1.8.2.min.js"></script>
	<script type="text/javascript" src="/sy/base/frame/constant.js"></script>
	<script type="text/javascript" src="/sy/base/frame/tools.js"></script>
	<script type="text/javascript" src="/sy/base/frame/platform.js"></script>
	<style type="text/css">
		.tit1 a,.tit1 a:visited{
			color:#614927;
		}
	</style>
	<script type="text/javascript">
		jQuery(document).ready(function(){
			jQuery(".r #tab01 li span").mouseover(function(){
				jQuery(".r #tab01 li span").attr("class","");
				jQuery(this).attr("class","here");
				jQuery("#tab01_Content0,#tab01_Content1,#tab01_Content2").css("display","none");
				jQuery("#"+this.title).css("display","block");
			});
			jQuery("#tab02 a").mouseover(function(){
				jQuery("#tab02 a").attr("class","");
				jQuery(this).attr("class","here");
				jQuery("#tab02_Content0,#tab02_Content1").css("display","none");
				jQuery("#"+this.title).css("display","block");
			});
			jQuery("#tab03 a").mouseover(function(){
				jQuery("#tab03 a").attr("class","");
				jQuery(this).attr("class","here");
				jQuery("#tab03_Content0,#tab03_Content1").css("display","none");
				jQuery("#"+this.title).css("display","block");
			});
			jQuery(".bor1 .cont3 .c #ul1 .h113 .b1 a").mouseover(function(){
				jQuery(this).parent("div").parent("div").addClass("h114");
				jQuery(this).parent(".b1").prev("div").css("display","block");
				var _this = jQuery(this);
				_this.mouseout(function(){
					_this.parent(".b1").prev("div").css("display","none");
					_this.parent("div").parent("div").removeClass().addClass("h113");
				});
				_this.click(function(){
					_this.parent(".b1").prev("div").css("display","none");
					_this.parent("div").parent("div").removeClass().addClass("h113");
				});
			});
			
			/* 文档总数 */
			var result = parent.FireFly.doAct("SY_COMM_WENKU_DOCUMENT","count",{"DOCUMENT_STATUS":2},false,false);
			var number = result["_DATA_"];
			jQuery("#number").text(number);
			/* 文档总数 */
		});
		
		function doView(id,name){
			var url = "/wenku/content/" + id + ".html";	
			if("${group}"=="true"){
				window.location = url + "?group=true";
				return false;
			}
			var opts={'scrollFlag':true , 'url':url,'tTitle':name,'menuFlag':4};
			top.Tab.open(opts);
		}
		function getMore(id,name){
			var url = "/wenku/channel/" + id + "/index.html";	
			if("${group}"=="true"){
				window.location = url + "?group=true";
				return false;
			}			
			var opts={'scrollFlag':true , 'url':url,'tTitle':name,'menuFlag':4};
			top.Tab.open(opts);
		}
		function upload(){
			var url = "/wenku/tmpl/${upload_tmpl_id}.html";		
			var opts={'scrollFlag':true , 'url':url,'tTitle':"上传文档",'menuFlag':4};
			parent.Tab.open(opts);
		}
	</script>
</head>
<body>
	<#if group="true">
		<@coms_html comsId="GROUP_NAV" siteId="${data.SITE_ID}" comparam="{'titleBar':'portal-title-none'}">
			${coms.AREA}
		</@coms_html>
		<@coms_html comsId="GROUP_WENKU_NAV" siteId="${data.SITE_ID}" comparam="{'titleBar':'portal-title-none'}">
			${coms.AREA}
		</@coms_html>
		<#else>
		<@coms_html comsId="WENKU_NAV" siteId="${data.SITE_ID}" comparam="{'titleBar':'portal-title-none'}">
			${coms.AREA}
		</@coms_html>
	</#if>
	<div class="w101"></div>
	<div class="w980">
		<div class="l1">
			<!-- 栏目 -->
			<@channel_list debugName="父栏目" channelId="${data.CHNL_ID}">
			<#if tag_list?size gt 0>
			<#list tag_list as channel>
			<#if channel_index=0>
			<div class="bor1" style="margin-top:0;">
			<#else><div class="bor1">
			</#if>
			    <div class="tit1">
			    	${channel.CHNL_NAME}
					<div style="float:right; margin-right:20px;">
						<a href="javascript:getMore('${channel.CHNL_ID}','${channel.CHNL_NAME}');">查看更多&raquo;</a>
					</div>
				</div>
			    <div class="cont3">
			        <div class="di"></div>
			        <div class="c">
			            <ul id="ul1">
			                <@wenku_list debugName="栏目${channel_index}" channelId="${channel.CHNL_ID}" count="5">
							<#list tag_list as doc>
							<#if doc_index=4>
			                <li class="li0">
			                <#else>
			                <li>
			                    </#if>
			                    <div class="h113">
			                        <div style="display:none" class="title">
			                            <a href="javascript:void(0)">
											<@text_cut s="${doc.DOCUMENT_DESCRIPTION!''}" length="40">${text}</@text_cut>
										</a>
			                        </div>
			                        <div class="b1">
			                            <a href="javascript:doView('${doc.DOCUMENT_ID}', '${doc.DOCUMENT_TITLE}');"><img width="81" height="111" name="a" alt="${doc.DOCUMENT_TITLE!''}" 
										src="<@setPic doc middle/>">
										<b class="${doc.DOCUMENT_FILE_SUFFIX!'txt'}"></b></a>
			                        </div>
			                    </div>
			                    <p align="center">
			                        <a href="javascript:doView('${doc.DOCUMENT_ID}', '${doc.DOCUMENT_TITLE}');">${doc.DOCUMENT_TITLE!''}</a>
			                    </p>
			                </li>
			                </#list></@wenku_list> 
			            </ul>
			        </div>
			    </div>
			</div>
			</#list>
			</#if>
			</@channel_list>
        </div>
		<div class="r1">	
	        <!-- 热门文档排行榜 -->
			<div class="bor2">
				<div class="di1"></div>
				<div class="tit4">热门文档排行榜</div>
				<div id="tab02" class="an1"><a title="tab02_Content0" class="here">阅读榜</a><a title="tab02_Content1">下载榜</a></div>
				<@wenku_list debugName="阅读排行" siteId="${SITE_ID}" count="10" order="DOCUMENT_READ_COUNTER desc">
				<ul id="tab02_Content0" class="list3" style="display: block;">
				<#list tag_list as doc>
	            	<li class="${doc.DOCUMENT_FILE_SUFFIX!'txt'}">
	            		<a title="${doc.DOCUMENT_TITLE}"  href="javascript:doView('${doc.DOCUMENT_ID}', '${doc.DOCUMENT_TITLE}');">${doc.DOCUMENT_TITLE}</a><br>
						阅读：${doc.DOCUMENT_READ_COUNTER!0}次</li>
				</#list>
				</ul>
				</@wenku_list> 
				<@wenku_list debugName="下载排行" siteId="${SITE_ID}" count="10" order="DOCUMENT_DOWNLOAD_COUNTER desc">
				<ul style="display:none" id="tab02_Content1" class="list3">
				<#list tag_list as doc>
					<li class="${doc.DOCUMENT_FILE_SUFFIX!'txt'}">
						<a title="${doc.DOCUMENT_TITLE}"  href="javascript:doView('${doc.DOCUMENT_ID}', '${doc.DOCUMENT_TITLE}');">${doc.DOCUMENT_TITLE} </a><br>
						下载：${doc.DOCUMENT_DOWNLOAD_COUNTER!0}次</li>
				</#list>
				</ul> 
				</@wenku_list>           
			</div>
			<!-- 热门文档排行榜 -->
			<!-- 最新上传 -->
			<div class="bor2">
				<div class="di1"></div>
				<div class="tit4">最新上传</div>
				<@wenku_list debugName="最新上传" siteId="${SITE_ID}" count="10" order="S_CTIME desc">
				<ul id="tab_Content" class="list3" style="display: block;">
				<#list tag_list as doc>
	            	<li class="unknown ${doc.DOCUMENT_FILE_SUFFIX!'txt'}">
	            		<a title="${doc.DOCUMENT_TITLE}"  href="javascript:doView('${doc.DOCUMENT_ID}', '${doc.DOCUMENT_TITLE}');">${doc.DOCUMENT_TITLE}</a>
						<br/>
						<#if (doc.S_UNAME)??>
							${doc.S_UNAME!''}
							<#else>${doc.S_USER!''}
						</#if>
						&nbsp;&nbsp;上传于：<@text_cut s="${doc.S_CTIME!''}" length="11">${text}</@text_cut>
					</li>
				</#list>
				</ul>
				</@wenku_list>           
			</div>
			<!-- 最新上传 -->	
			<div class="clear"></div>
	</div>
</body>
</html>
