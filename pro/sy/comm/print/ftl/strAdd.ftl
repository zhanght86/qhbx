<#macro stradd string1 string2>

<#if string1?trim != "" && string2?trim != ""> 

${string1},${string2} 

<#else> 

${string1}${string2} 

</#if>

</#macro>