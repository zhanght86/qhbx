<#assign channel_serv="SY_COMM_CMS_CHNL"/>

<#macro zhidaoUrl askId>
	/cms/SY_COMM_ZHIDAO_QUESTION/${askId}.html
</#macro>

<#macro chnlUrl chnl page>
	/cms/${channel_serv}/${chnl}/index_${page}.html
</#macro>

<#macro tmplUrl tmpl>
	/cms/SY_COMM_CMS_TMPL/${tmpl}/index_${page}.html
</#macro>
