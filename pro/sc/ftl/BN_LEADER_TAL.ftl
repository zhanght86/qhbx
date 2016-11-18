<style>

</style>
<div class='portal-box pt-gsyw-wrapper' style="border:0px #000 solid;width:92%;height:260px;" id="${id}__box">
<#assign hasDate = hasDate!"2">
<#assign conhei = hei!"auto">
<div style='position:relative;height:35px;'>

	<div style="width:auto;float:left;line-height:35px;height:35px;border-bottom-width:2px;border-bottom-style:solid;border-bottom-color:#f2f2f2;position:absolute;width:100%;">
	<span style="margin-left:10px;padding-right:10px;font-size:16px;font-weight:bold;">${CHNL_NAME}</span>
	</div>
	<#if hasDate=='2'>
	<span class="portal-box-more-sc">&nbsp<a href="#" onclick="openListMoreByChnl('${CHNL_ID}','${CHNL_NAME}')">更多</a></span>
	<#elseif hasDate=='3'>
	<span class="portal-box-more-sc">&nbsp<a href="#" onclick="openListMoreByChnlNomal('${CHNL_ID}','${CHNL_NAME}')">更多</a></span>
	<#else>
	<span class="portal-box-more-sc">&nbsp<a href="#" onclick="openDateListMoreByChnl('${CHNL_ID}','${CHNL_NAME}')">更多</a></span>
	</#if>
</div>
<#if conhei=='auto'>
<div class='portal-box-con' style='height:auto;'>
<#else>
<div class='portal-box-con' style='height:${hei}px;max-height:${hei}px;'>
</#if>

<table width="100%" style="table-layout:fixed;margin-left:8px;">
<#if (_DATA_?size == 0)>
<tr><td align="center">该栏目下没有信息！</td></tr>
</#if>
<ul class="ult6" style="list-style-type:disc;">
<#list _DATA_ as content>
	<#if content_index == 0>
	
	<div class="w330 lh20" style="overflow: hidden;padding-top: 10px;">
                            <img style="float:left;margin-right:5px;"src="/file/${content.picture.FILE_ID}?size=54x57">
                            <p onclick="newsView('${content.NEWS_ID}')" style="cursor:pointer;font-size:13px;color:#999;line-height:22px;">
								
								<strong>
								<#if (content.NEWS_SUBJECT?length > 14)>
                                    <a style="margin-left:25px;font-size:14px;font-weight:bold;text-align:center;">${content.NEWS_SUBJECT?substring(0,14)}...</a>
								<#else>
									<a style="margin-left:25px;font-size:14px;font-weight:bold;text-align:center;">${content.NEWS_SUBJECT}</a>
								</#if>
								</strong>
								</b>
								</br>	
                               <#if (content.NEWS_SHORT_TITLE?length > 20)>
                                ${content.NEWS_SHORT_TITLE?substring(0,20)}...
								<#else>
									 ${content.NEWS_SHORT_TITLE}
								</#if>
							</p>
                        </div>
	
	<#else>

	
		<li style='list-style-type:none;'><a id = '${CHNL_ID}${content.NEWS_ID}' title='${content.NEWS_SUBJECT}' style="width:100%;margin-left:3px;display: block;margin-top:2px;margin-bottom:1px;height:28px;line-height:25px;color:#888;font-size:12px;" href="javascript:void(0);" 
		onclick="newsView('${content.NEWS_ID}')">
		<span class="elipd" style="float:left;">&#8226;&nbsp;&nbsp;</span>
		<span class="elipd" style="max-width:64%;float:left;">
		
		${content.NEWS_SUBJECT}
		</span>
		<#--
			<script>
			var newQx = "${NEW_QX!'0'}";
			
			if (rhDate.doDateDiff('D',"${content.NEWS_TIME?substring(0,10)}",rhDate.getCurentTime().substring(0, 10))<=newQx) {
				$("<i class='new'></i>").appendTo($("#${CHNL_ID}${content.NEWS_ID}"));
			}
			</script>
		-->	
			<span style="float:right;margin-right:6px;color:#999999;">
				[${content.NEWS_TIME?substring(0,10)}]
		</span>
		</a></li>

	
 </#if>

</#list>
	</ul>
</table>
</div>
</div>
<script type ="text/javascript" >
	/*jQuery(document).ready(function(jQuery) {
		var boxHtml = jQuery("#${id}__temp #${id}__box").html();
		jQuery("#${id}__temp").html("<div class='portal-box pt-gsyw-wrapper' id='${id}__box'>" + boxHtml + "</div>");
	});*/
</script>