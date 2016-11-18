<#include "/cms_constant.ftl">

<!-- 站点id 和 根栏目id -->
<#assign site_id="ZHIDAO_DEFAULT"/>
<!--首页模版ID -->
<#assign index_tmpl_id="1oRlDxWJN35Ug4r6wwDglu"/>

<!-- 专家模版ID -->
<#assign specialist_tmpl_id="1T4m9Ehul1QrkF85KV19wl"/>

<!-- 站点id 和 根栏目id -->
<#assign root_channel_id="1CviNNo_l3YVYpOSonA5p0"/>

<!-- 宏定义：输入用户头像_begin -->
<#macro setUserImg user>
		<#if user.USER_IMG?length gt 0>
			${user.USER_IMG}
		<#elseif 1 == user.USER_SEX>
			/sy/theme/default/images/common/rh-lady-icon.png
		<#else>
		  /sy/theme/default/images/common/rh-male-icon.png
		</#if>
</#macro>
<!-- 宏定义：输入用户头像_end -->

<!-- 宏定义：显示用户头像和在线状态_begin -->
<#macro formatUserImg user id clazz width height onlineTop onlineLeft>
	<img src="<@setUserImg user/>"
	<#if id != "">
		id="${id}"
	</#if>
	<#if clazz != "">
		class="${clazz}"
	</#if>
	<#if width != "">
		width="${width}"
	</#if>
	<#if height != "">
		height="${height}"
	</#if>
	/>
	
	<#if user.S_USER__STATUS == 1>
		<div style="top:${onlineTop}; left:${onlineLeft};" class='rh-user-info-list-online rh-user-info-on-line' title='在线'>&nbsp;</div>
	<#else>
		<div style="top:${onlineTop}; left:${onlineLeft};" class='rh-user-info-list-offline rh-user-info-on-line' title='离线'>&nbsp;</div>
	</#if>
</#macro>
<!-- 宏定义：显示用户头像和在线状态_end -->


<!-- 公告模版-->
<#assign notice_tmpl_id="0erjzskmZ2npg7l2M17DeEy"/>

<!-- 查看其它人的知道 -->
<#assign userZhidao_center_tmpl_id="0i0y5gDfN3yH3IMt0hpErY"/>

<!-- 导航--我的关注-->
<#assign myfollow_tmpl_id="0tGPqXDLdcaUXqhy4fFH0N"/>
<!-- 导航--精彩推荐-->
<#assign zhidao_recommend_tmpl_id="2r3es2AYxeQrI8rXBpPZSy"/>