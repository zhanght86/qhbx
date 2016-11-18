<#include "/cms_constant.ftl">

<script type="text/javascript">
	function getTime(time){
		var timeago = "";
		if (time) {
			timeago = time;
			timeago = timeago.substring(0, 19);
			timeago = jQuery.timeago(timeago); 
		}
		return timeago;
	}	
	function doChannel(id,name){
		window.location = "/cms/SY_COMM_CMS_CHNL/" + id + "/index_1.html?group=${group}";	
		//var opts={'scrollFlag':true , 'url':url,'tTitle':name,'menuFlag':3};
		//top.Tab.open(opts);
	}
	function doView(id,name){
		var url = "/cms/SY_COMM_BBS_TOPIC/" + id + ".html?group=${group}";	
		var opts={'scrollFlag':true , 'url':url,'tTitle':name,'menuFlag':1};
		top.Tab.open(opts);
	}
</script>

<!-- 宏定义：设置栏目默认图片_begin -->
<#macro setPic channel default>
	<#if (channel.CHNL_IMAGE)??  && channel.CHNL_IMAGE?length gt 0>
		<#if channel.CHNL_IMAGE?index_of(",") != -1>
			/file/${channel.CHNL_IMAGE?substring(0,channel.CHNL_IMAGE?index_of(","))}
			<#else>/file/${channel.CHNL_IMAGE}
		</#if>
		<#else>${default}
	</#if>
</#macro>
<!-- 宏定义：设置栏目默认图片_end -->

<!-- 站点id 和 根栏目id 和 发帖模板id -->
<#assign site_id="SY_COMM_SITE_INFOS"/>
<#assign root_channel_id="BBS_3hWpJcmkZcNHarHMgIvMm2"/>
<#assign publish_tmpl_id="0O1WepMWN2DbljgA5sXg7T"/>
<!-- 站点id 和 根栏目id -->