

<style>
.portal-box-title-top{background-image: url("/bn/style/images/u160.jpg"); background-color: transparent; height: 45px; width: 45px; position: absolute; background-size: 100% 100%;}
.portal-box-title-bottom-line{left:0px;margin-left:-3px;border-bottom: 2px #d20000 solid;top: 0px;position:relative;}

</style>
<div id='EXTEND_LINK' class='portal-box ${boxTheme}'  style='width:258px; margin-left:0px; margin-right:0px; min-height:${minheight};'>
<div class='pt-gsyw-wrapper portal-box-title' style='position:relative;'>
	<span class='portal-box-title-icon icon_portal_links'></span>
	<span class='portal-box-title-top'></span>
	<span class="portal-box-title-label">${title}</span>
	
	<span class="portal-box-more-sc">&nbsp;<a href="#" onclick="openMoreListDownLoad()">更多</a></span>
	<!--
	<span class="portal-box-hideBtn conHeanderTitle-expand"></span>
	-->
	
</div>
<div class='portal-box-con'>
<table width="100%">
<#if (_DATA_?size == 0)>
<tr><td align="center">${message}</td></tr>
 </#if>
 <#list _DATA_ as links>
<tr>
    <td style="padding-left:17px;">&#8226;&nbsp;&nbsp;<a href="/file/${links.FILE_ID}" target="_blank">
	<!--
	${links.FILE_NAME}
	-->
	
	<#if (links.FILE_NAME?length > 13)>
		${links.FILE_NAME?substring(0,12)}...
	<#else>
		${links.FILE_NAME}
	</#if>

	</a></td>
</tr>
</#list>	
</table>
</div>
</div>
<script>
	function openMoreListDownLoad() {
		var params = {};
		params["LINK_TYPE"]="1";	
		var tabOpt = {"url":"BN_MH_DOWNLOAD.list.do","params":params,"menuFlag":3,"tTitle":"前海再保险",'nohex':'true'};
		var opts = {"tTitle":"常用下载","url":"BN_MH_DOWNLOAD.list.do","menuFlag":3};
		var tabP = jQuery.toJSON(opts);
		tabP = tabP.replace(/\"/g,"'");
		window.open("/sy/comm/page/page.jsp?openTab="+(encodeURIComponent(tabP)));
		
	}


</script>