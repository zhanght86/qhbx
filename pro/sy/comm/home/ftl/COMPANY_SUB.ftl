<div id='SY_COMM_INFOS_VIEW' class='portal-box ${boxTheme}'  style="margin: 0px auto; width: 99.5%;">
<script type="text/javascript">
	function newsMHDView(title,id){
		var opts = {"tTitle":title,"url":"SY_COMM_TEMPL.show.do?pkCode="+id,"menuFlag":3};
		Tab.open(opts);
	}
    function openMHListMore(pttype,title) {
		var opts = {"tTitle":title,"url":"SY_COMM_INFOS.getMoreMhTmpl.do?PT_TYPE="+pttype,"menuFlag":3};
		Tab.open(opts);
    }
</script>
<style type="text/css">
.portal-box-con tr td a {
color: #000000;
}
.pageBody__default .portal-box-title {
color: #000000;
}
.elipd{
overflow:hidden;
white-space:nowrap;
text-overflow:ellipsis;
-o-text-overflow:ellipsis;
-moz-text-overflow:ellipsis;
-webkit-text-overflow:ellipsis;
-icab-text-overflow: ellipsis;
-khtml-text-overflow: ellipsis;
}
</style>
<div class='portal-box-title ${titleBar}'><span class='portal-box-title-icon ${icon}'></span><span class="portal-box-title-label">${title}</span><span class="portal-box-hideBtn  conHeanderTitle-expand"></span><span class="portal-box-more"><a href="#" onclick="openMHListMore('COMPANY_INFOS','分公司新闻中心')"></a></span></div>
<div class='portal-box-con'>
<table width="100%" style="table-layout:fixed;">
<#assign dataListCount = _DATA_?size>
<#assign cellCount = 9>
<#assign rowCount = (dataListCount / cellCount)?int + 1>
	<#list 1..rowCount as t>
		<tr style="width:100%">
			<#assign limit = (t-1) * cellCount>
			<#list _DATA_ as content>
		     <#if (content_index gte limit && content_index lt (limit+cellCount))>
		<#--  -->     
		     <#if '${content.PT_TITLE}' == '财险公司新闻中心' || '${content.PT_TITLE}' == '寿险公司新闻中心(筹)'>
           <td class="elipd"><img src="/sy/comm/home/img/d.png"/><a href="javascript:void(0);" onclick="newsMHDView('${content.PT_TITLE}','${content.PT_ID}')" title="${content.PT_TITLE}" style="margin-left:3px;">
		   			${content.PT_TITLE}</a></td>
            <#else>
           <td class="elipd">	${content.PT_TITLE}</a></td>
           </#if>
		   	 </#if>
		   	</#list>
		</tr>
</#list>
</table>
</div>
</div>