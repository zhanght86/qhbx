<#if fileList?size != 0>
	<#list fileList as file>
	${file.DIS_NAME}<#if file_has_next>，&nbsp;&nbsp;</#if>
	</#list>
</#if>