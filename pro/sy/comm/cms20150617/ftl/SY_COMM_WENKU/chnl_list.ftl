<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>栏目列表</title>
	<#include "/SY_COMM_WENKU/config_constant.ftl">
	<link href="/sy/theme/default/common.css" rel="stylesheet" type="text/css"/>
	<link href="/sy/comm/wenku/css/style_daoh.css" rel="stylesheet" type="text/css"/>
	<link href="/sy/comm/wenku/css/style.css" rel="stylesheet" type="text/css"/>
	<script src="/sy/comm/news/theme/js/jquery.js" type="text/javascript"></script>
	<script type="text/javascript" src="/sy/base/frame/constant.js"></script>
	<script type="text/javascript" src="/sy/base/frame/tools.js"></script>
	<script type="text/javascript" src="/sy/base/frame/platform.js"></script>
	<script type="text/javascript" src="/sy/comm/cms/js/page.js"></script>
	<style type="text/css">
		html,body{background:white;margin:0;font-size:14px;color:#999999;}
		.top{color:black;padding:0 5%;}
		a img{border:1px #DCDCDC solid;color:#2D64B3;}
		a:hover img{border:1px #E8811C solid;outline:2px #E8811C solid;}
		.unknown{position:absolute;right:8px;bottom:10px; height: 18px; width: 24px; background: url("/sy/comm/wenku/img/unkonwn.png") no-repeat scroll 0 0 transparent;}
		.mp3{position:absolute;bottom:10px;right:8px;height:18px; width:24px;background: url("/sy/comm/wenku/img/mp3.png") no-repeat;}
		.flv{position:absolute;bottom:8px;right:10px;height:18px; width:24px;background: url("/sy/comm/wenku/img/film.png") no-repeat;}
		.txt{position:absolute;right:6px;bottom:8px; height: 18px; width: 24px; background: url("/sy/comm/wenku/img/b.png") no-repeat scroll 0 -879px transparent;}
		.doc{position:absolute;right:6px;bottom:8px; height: 18px; width: 24px; background:url("/sy/comm/wenku/img/b.png") no-repeat scroll 0 1px transparent;}
		.docx{position:absolute;right:6px;bottom:8px; height: 18px; width: 24px; background: url("/sy/comm/wenku/img/b.png") no-repeat scroll 0 1px transparent;}
		.ppt{position:absolute;right:6px;bottom:8px; height: 18px; width: 24px; background: url("/sy/comm/wenku/img/b.png") no-repeat scroll 0 -79px transparent;}
		.pptx{position:absolute;right:6px;bottom:8px; height: 18px; width: 24px; background: url("/sy/comm/wenku/img/b.png") no-repeat scroll 0 -79px transparent;}
		.xls{position:absolute;right:6px;bottom:8px; height: 18px; width: 24px; background: url("/sy/comm/wenku/img/b.png") no-repeat scroll 0 -159px transparent;} 
		.xlsx{position:absolute;right:6px;bottom:8px; height: 18px; width: 24px; background: url("/sy/comm/wenku/img/b.png") no-repeat scroll 0 -159px transparent;} 
		.vsd{position:absolute;right:6px;bottom:8px; height: 18px; width: 24px; background: url("/sy/comm/wenku/img/b.png") no-repeat scroll 0 -239px transparent;} 
		.pot{position:absolute;right:6px;bottom:8px; height: 18px; width: 24px; background: url("/sy/comm/wenku/img/b.png") no-repeat scroll 0 -319px transparent;}
		.pps{position:absolute;right:6px;bottom:8px; height: 18px; width: 24px; background: url("/sy/comm/wenku/img/b.png") no-repeat scroll 0 -399px transparent;}
		.rtf{position:absolute;right:6px;bottom:8px; height: 18px; width: 24px; background: url("/sy/comm/wenku/img/b.png") no-repeat scroll 0 -479px transparent;}
		.wps{position:absolute;right:6px;bottom:8px; height: 18px; width: 24px; background: url("/sy/comm/wenku/img/b.png") no-repeat scroll 0 -559px transparent;}
		.et{position:absolute;right:6px;bottom:8px; height: 18px; width: 24px; background: url("/sy/comm/wenku/img/b.png") no-repeat scroll 0 -639px transparent;} 
		.dps{position:absolute;right:6px;bottom:8px; height: 18px; width: 24px; background: url("/sy/comm/wenku/img/b.png") no-repeat scroll 0 -719px transparent;} 
		.pdf{position:absolute;right:6px;bottom:8px; height: 18px; width: 24px; background: url("/sy/comm/wenku/img/b.png") no-repeat scroll 0 -799px transparent;}
		.epub{position:absolute;right:6px;bottom:8px; height: 18px; width: 24px; background: url("/sy/comm/wenku/img/b.png") no-repeat scroll 0 -959px transparent;}
	</style>
	<script type="text/javascript">	
		var currentPage = "${_PAGE_.NOWPAGE }";
		var pageNum = "${_PAGE_.PAGES }";
		function doPage(page){
			window.location = "/wenku/channel/" + "${chanel.CHNL_ID}" + "/index_"+page+".html";
			window.location += "?group=true";
		}
		function doView(id,name){
			var url = "/wenku/content/" + id + ".html";	
			if("${group}"=="true"){
				window.location = url + "?group=true";
				return false;
			}
			var opts={'scrollFlag':true , 'url':url,'tTitle':name,'menuFlag':4};
			top.Tab.open(opts);
		}
	</script>
</head>
<body>
	<#if group="true">
		<@coms_html comsId="GROUP_NAV" siteId="${chanel.SITE_ID}" comparam="{'titleBar':'portal-title-none'}">
			${coms.AREA}
		</@coms_html>
		<@coms_html comsId="GROUP_WENKU_NAV" siteId="${chanel.SITE_ID}" comparam="{'titleBar':'portal-title-none'}">
			${coms.AREA}
		</@coms_html>
		<#else>
		<@coms_html comsId="WENKU_NAV" siteId="${chanel.SITE_ID}" comparam="{'titleBar':'portal-title-none'}">
			${coms.AREA}
		</@coms_html>
	</#if>
	<div class="w101"></div>
	<table border="0" cellpadding="4" cellspacing="0" width="100%" class="top">
		<tr>
			<td colspan="2"><h2>${chanel.CHNL_NAME!""}</h2></td>
		</tr>
		<tr>
			<td colspan="2"><div style="height:1px;background:#aaa;"></div></td>
		</tr>
		<#list _DATA_ as doc>
		<#if doc_index%2=0>
		<tr>
		</#if>
			<td>
				<table border="0" cellpadding="4" cellspacing="0" width="100%">
					<tr>
						<td rowspan="3" width="100" height="170" align="center">
							<a href="javascript:doView('${doc.DOCUMENT_ID}', '${doc.DOCUMENT_TITLE}');">
								<img width="80" height="110" alt="${doc.DOCUMENT_TITLE!''}" src="<@setPic doc middle/>">
								<b class="unknown ${doc.DOCUMENT_FILE_SUFFIX!'txt'}"></b>
							</a>
						</td>
						<td>
							<a href="javascript:doView('${doc.DOCUMENT_ID}', '${doc.DOCUMENT_TITLE}');">${doc.DOCUMENT_TITLE!''}</a>
						</td>
					</tr>
					<tr>
						<td>
							下载量：${doc.DOCUMENT_DOWNLOAD_COUNTER!0}
							浏览量：${doc.DOCUMENT_READ_COUNTER!0}
						</td>
					</tr>
					<tr>
						<td><a href="javascript:doView('${doc.DOCUMENT_ID}', '${doc.DOCUMENT_TITLE}');">阅读全文&raquo;</a></td>
					</tr>
				</table>
			</td>
		 <#if _DATA_?size%2=1 && !doc_has_next>
		 <td>&nbsp;</td></tr>
		 </#if>			
		<#if doc_index%2=1>
		</tr>
		<tr><td colspan="2"><div style="height:1px;background:#D4D4D4"></div></td></tr>
		</#if>
		</#list>
		<tr>
			<td colspan="2">
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
			</td>
		</tr>
	</table>
</body>
</html>
