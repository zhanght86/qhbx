<style>
.portal-box-title-top{background-image: url("/bn/style/images/u160.jpg"); background-color: transparent; height: 45px; width: 45px; position: absolute; background-size: 100% 100%;}
.portal-box-title-bottom-line{left:0px;margin-left:-3px;border-bottom: 2px #d20000 solid;top: 0px;position:relative;}
</style>
<div id='EXTEND_LINK' class='portal-box ${boxTheme}'  style='width:${width}px; margin-right:0px; min-height:${height};'>
<div class='pt-gsyw-wrapper portal-box-title' style='position:relative;'>
	<span class='portal-box-title-icon icon_portal_links'></span>
	<span class='portal-box-title-top'></span>
	<span class="portal-box-title-label">${title}</span>
	<!--
	<span class="portal-box-more-sc">&nbsp;<a href="#" onclick="openMoreListLink()">更多</a></span>
	
	
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
    <td style="padding-left:17px;">&#8226;&nbsp;&nbsp;<a href="${links.LINK_ADDRESS}" target="_blank">${links.LINK_NAME}</a></td>
</tr>
</#list>	
</table>
</div>
</div>

<script>
	function openMoreListLink() {
		var params = {};
		params["LINK_TYPE"]="2";	
		var tabOpt = {"url":"SY_COMM_LINK.list.do","params":params,"menuFlag":3,"tTitle":"百年人寿",'nohex':'true'};
		var opts = {"tTitle":"常用链接","url":"SY_COMM_LINK.list.do","menuFlag":3};
		var tabP = jQuery.toJSON(opts);
		tabP = tabP.replace(/\"/g,"'");
		window.open("/sy/comm/page/page.jsp?openTab="+(encodeURIComponent(tabP)));
		
	}


</script>