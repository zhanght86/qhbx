<#include "/cms_constant.ftl">

<!-- 站点id 和 根栏目id -->
<#assign site_id="ZHIDAO_DEFAULT"/>
<!--首页模版ID -->
<#assign index_tmpl_id="28bfWlGS13cpnlf1oEwixT"/>

<!-- 专家模版ID -->
<#assign specialist_tmpl_id="2YPjeGzX514XJuZKFgrPOz"/>

<!-- 站点id 和 根栏目id -->
<#assign root_channel_id="1CviNNo_l3YVYpOSonA5p0"/>

<!-- 宏定义：输入用户头像_begin -->
<#macro setUserImg user>
		<#if user.USER_IMG?length gt 0>
			<#--${user.USER_IMG}-->
			<#list user.USER_IMG?split("?") as src>
				<#if src_index == 0>
					${src}
				</#if>
			</#list>
		<#elseif 1 == user.USER_SEX>
			/sy/theme/default/images/common/rh-lady-icon.png
		<#else>
		  /sy/theme/default/images/common/rh-male-icon.png
		</#if>
</#macro>
<!-- 宏定义：输入用户头像_end -->

<!-- 宏定义：显示用户头像和在线状态_begin -->
<#macro formatUserImg userCode user id clazz width height>
	<img src="<@setUserImg user/>?size=${width?replace("px","")}x${height?replace("px","")}"
	onmouseover="zhidaoUserInfo(event,'${userCode}');"
	<#if id != "">
		id="${id}"
	</#if>
	<#if clazz != "">
		class="${clazz}"
	</#if>
	<#--
	<#if width != "">
		width="${width}"
	</#if>
	<#if height != "">
		height="${height}"
	</#if>
	-->
	<#if height != "">
		style = 'height:${height};width:auto;'
	</#if>
	/>
	
</#macro>
<!-- 宏定义：显示用户头像和在线状态_end -->


<!-- 公告模版-->
<#assign notice_tmpl_id="0erjzskmZ2npg7l2M17DeEy"/>

<!-- 查看其它人的知道 -->
<#assign userZhidao_center_tmpl_id="2AUqlhuapdyJpWeU4rwWyJKf"/>

<!-- 导航--我的关注-->
<#assign myfollow_tmpl_id="0tGPqXDLdcaUXqhy4fFH0N"/>
<!-- 导航--精彩推荐-->
<#assign zhidao_recommend_tmpl_id="2r3es2AYxeQrI8rXBpPZSy"/>