<#include "/cms_constant.ftl">

<!-- 页面图片三种尺寸 -->
<#assign big="200x250"/>
<#assign small="42x57"/>
<#assign middle="150x200"/>
<!-- 页面图片三种尺寸 -->

<!-- 宏定义：设置默认图片_begin -->
<#macro setPic doc size>
	<#if doc.DOCUMENT_FILE_SUFFIX="mp3">
		/sy/comm/wenku/format/mp3.png
		<#elseif doc.DOCUMENT_FILE_SUFFIX="flv">
			/sy/comm/wenku/format/video2.png
		<#elseif doc.DOCUMENT_FILE_SNAPSHOT?? && doc.DOCUMENT_FILE_SNAPSHOT?length gt 0>
			/file/${doc.DOCUMENT_FILE_SNAPSHOT}?size=${size!''}
		<#else>
			/sy/comm/wenku/format/unknown2.png
	</#if>
</#macro>
<!-- 宏定义：设置默认图片_end -->

<!----------------宏定义-设置缩略图显示效果--开始------------------------->
<#macro setDocPic doc sizes>
	<#if "ppt"==doc.DOCUMENT_FILE_SUFFIX>
		<dl class="isPPT${sizes!''}">
	<#elseif "pptx"==doc.DOCUMENT_FILE_SUFFIX>	
		<dl class="isPPT${sizes!''}">
	<#else>
		<dl class="notPPT${sizes!''}">
	</#if>
		<dt class="classItemImg${sizes!''}">
			<a href="#" onclick="javascript:view('${doc.DOCUMENT_ID}','${doc.DOCUMENT_TITLE}')";
			title="${doc.DOCUMENT_TITLE}" target="_self">
				<#if "1"==doc.IMPORTANT_DOCUMENT>
				<b class="im"></b>
				</#if>
					<img class="lazy" src="/sy/comm/wenku/baidu_style_files/gray_img.gif"
						alt="${doc.DOCUMENT_TITLE}"
						data-original="<@setPic doc middle />"
						class="logSend lazyLoadImage" />
				<b class="ic-${doc.DOCUMENT_FILE_SUFFIX!'ic-unknow'}"></b>
			</a>
		</dt>
		<#if "ppt"==doc.DOCUMENT_FILE_SUFFIX>
			<dd class="classItemTitle${sizes!''}" style="height:37px;">
		<#elseif "pptx"==doc.DOCUMENT_FILE_SUFFIX>	
			<dd class="classItemTitle${sizes!''}" style="height:37px;">
		<#else>
			<dd class="classItemTitle${sizes!''}" style="height:40px;">
		</#if>
			<a href="#"	onclick="javascript:view('${doc.DOCUMENT_ID}','${doc.DOCUMENT_TITLE}')";
			class="logSend" target="_self"
			title="${doc.DOCUMENT_TITLE}">
				<#if doc.DOCUMENT_TITLE?length gt 10>
					${doc.DOCUMENT_TITLE[0..10]}...
				<#else>
					${doc.DOCUMENT_TITLE}	
				</#if>
			</a>
		</dd>
		<dd class="classItemInfo${sizes!''}">
			<#if doc.DOCUMENT_FILE_PAGES gt 0>
				<span class="classItemPageCount${sizes!''}">${doc.DOCUMENT_FILE_PAGES}页</span>
			</#if>
			<span class="classItemPrice${sizes!''}">${doc.DOCUMENT_READ_COUNTER}次</span>
		</dd>
	</dl>
</#macro>

<!----------------宏定义-设置缩略图显示效果--结束------------------------->
<!-- 宏定义：输入用户头像_begin -->
<#macro setUserImg user>
		<#if user.USER_IMG?length gt 0>
			${user.USER_IMG}
		<#elseif 1 == user.USER_SEX>
			/sy/theme/default/images/common/rh-lady-icon.png?t=t
		<#else>
		  /sy/theme/default/images/common/rh-male-icon.png?t=t
		</#if>
</#macro>
<!-- 宏定义：输入用户头像_end -->
<!-- 站点id 和 根栏目id -->
<#assign site_id="SY_COMM_CMS"/>
<#assign root_channel_id="WENKU_3hWpJcmkZcNHarHMgIvMm2"/>
<!-- 站点id 和 根栏目id -->

<!-- 文档上传模板ID_begin -->
<#assign upload_tmpl_id="1W2A6nSlp0noco4zLfYUuw"/>
<!-- 文档上传模板ID_end -->
<!-- 文库公告模版-->
<#assign notice_tmpl_id="1exEQ0VaJ1HpX54vAl1UGqE"/>
<!--创建文辑模版 -->
<#assign create_doclist_tmpl_id="0WXXRGwDx6MVKrJzwPklfyz"/>

<!--热门文档模版id -->
<#assign hot_document_tmpl_id="0bQEBoSrl8e6oUmkwZxdZg3"/>

<!--热门文辑模版id -->
<#assign hot_doclist_tmpl_id="3jaH5ppTBfXoGEOxDIRGEqOo"/>

<!-- 最近文档模版id -->
<#assign new_document_tmpl_id="3BOumEvt58CEwcZXUyZcEkj"/>

<!-- 最近文辑模版id -->
<#assign new_doclist_tmpl_id="3kxEKqVpdeWFxfSxgUTOpJ"/>

<!-- 查看其它人的文档文辑 -->
<#assign user_center_tmpl_id="38NrX0dU9dLFVCHSDw0pjYK"/>

<!-- 导航ID -->
<#assign coms_nav_id="WENKU_NAV"/>
<!-- 导航ID -->
