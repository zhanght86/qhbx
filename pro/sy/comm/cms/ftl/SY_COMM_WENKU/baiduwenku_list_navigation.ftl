<#macro list_navigation data>
<style>
.list_a_div {
	height: 28px;
	width: 740px;
}

.list_a_span {
	display: inline-block;
	height: 28px;
	line-height: 28px;
}
</style>

<div class="list_a_div">
	<span class="list_a_span"> 
		<@channel_navigation debugName="二级导航" channelId="${data.CHNL_ID}"> 
			<#list tag_list as navi> 
				<#if navi.CHNL_ID == root_channel_id> 
					<a href="<@chnlUrl root_channel_id 1 />?selectMenu=first">文库首页</a>
		〉		<#else> 
					<a href="<@chnlUrl navi.CHNL_ID 1 />">${navi.CHNL_NAME}</a>
		〉 		</#if> 
			</#list> 
			<span>${data.CHNL_NAME}</span>
		</@channel_navigation>
	</span>
</div>




<@channel_list debugName="栏目三级导航"
channelId="${data.CHNL_ID}"> <#if (tag_list?size gt 0)>
<div id="threeChannel" class="mod catalog">
	<b class="top"> <b class="tl"></b> <b class="tr"></b>
	</b>
	<div class="inner">
		<div class="hd">
			<h2>${data.CHNL_NAME}</h2>
		</div>
		<div class="bd">
			<table id="classList" cellpadding="0" cellspacing="0" width="100%">
				<tr>
				<tbody>
					<#list tag_list as obj> <#if obj.CHNL_ID == data.CHNL_ID>
					<td><strong>${obj.CHNL_NAME}</strong></td> <#else>
					<td><a href="<@chnlUrl obj.CHNL_ID 1 />">${obj.CHNL_NAME}</a>&nbsp;</td>
					</#if> <#if (obj_index+1) gt 0 && (obj_index+1) % 4 == 0>
					</tr>
					<tr>
					</tr>

					</#if> </#list>
				</tbody>
			</table>
		</div>
	</div>
	<b class="bottom" style="z-index:1;"><b class="bl"></b><b class="br"></b></b>
</div>
<#else>
<!-- 如果没有子栏目，列出同级栏目-->

</#if> </@channel_list> </#macro>
