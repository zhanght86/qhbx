
<div id='SY_COMM_TASK' class='portal-box ${boxTheme}'>
<div class='portal-box-title ${titleBar}'><span class='portal-box-title-icon ${icon}'>
</span><span class="portal-box-title-label">${title}
</span><span class="portal-box-hideBtn  conHeanderTitle-expand"></span>
<span class="portal-box-more"><a href="javascript:void(0);" onclick="openMore()"></a></span></div>
<div class='portal-box-con'>
<table width="100%">
<#if (_DATA_?size == 0)>
<tr><td align=center id='haha'>没有任务需要处理！</td></tr>
</#if>
<#list _DATA_ as content>

<tr>
<td style="width:2%;">&nbsp;&nbsp;●</td>
<td>
<a href="javascript:void(0);" onclick="openTitle('${content.TASK_ID}');">
<#if (content.TASK_NAME?length > 25)>${content.TASK_NAME?substring(0,25)}...<#else>${content.TASK_NAME}</#if></a></br>
 ${content.S_ATIME}  --${content.TASK_END_DATE}
</td>
</tr>
</#list>
</table>
</div>
</div>
<script type="text/javascript" >
var servId = "${servId}";
var tTitle =  "${title}";
function openMore() {
	var options = {"url": servId + ".list.do", "tTitle":tTitle, "menuFlag":"4"};
	var tabP = jQuery.toJSON(options);
	tabP = tabP.replace(/\"/g,"'");
	window.open("/sy/comm/page/page.jsp?openTab="+tabP);
}

function openTitle(TASK_ID){
 var options = {"url":servId + ".card.do?pkCode="+TASK_ID,"tTitle":tTitle,"menuFlag":"4"};
 var tabP = jQuery.toJSON(options);
 tabP = tabP.replace(/\"/g,"'");
 window.open("/sy/comm/page/page.jsp?openTab="+tabP);
}
</script>
