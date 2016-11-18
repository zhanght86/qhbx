<#include "/cms_constant.ftl">

<!-- 四种图片大小 -->
<#assign big="308x208"/>
<#assign small="50x50"/>
<#assign middle="120x120"/>
<#assign biger="600x360"/>
<!-- 四种图片大小 -->

<!-- 宏定义：设置默认图片_begin -->
<#macro setPic pic size>
	<#if pic??>
		/file/${pic}?size=${size}
	</#if>
</#macro>
<!-- 宏定义：设置默认图片_end -->

<!-- 站点id 和 根栏目id -->
<#assign site_id="SY_COMM_CMS"/>
<#assign root_channel_id="NEWS_3hWpJcmkZcNHarHMgIvMm2"/>
<!-- 站点id 和 根栏目id -->

<!-- 导航ID -->
<#assign coms_nav_id="NEWS_NAV"/>
<!-- 导航ID -->
