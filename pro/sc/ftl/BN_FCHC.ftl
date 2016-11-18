<link rel="stylesheet" type="text/css" href="/sc/css/system_reset.css"/>
<div  class='portal-box pt-gsyw-wrapper' id='BN_FCHC__box'>

<#assign hasDate = hasDate!"2">
<#assign conhei = hei!"auto">

<div class='portal-box-title ${titleBar}' style='position:relative;'>
	<span class='portal-box-title-top'></span>
	<span class="portal-box-title-label">${title}</span>
	<div class="portal-box-title-bottom-line"></div>
	<#if hasDate=='2'>
	<span class="portal-box-more-sc">&nbsp<a href="#" onclick="openListMoreByChnl('${CHNL_ID}','${title}')">更多</a></span>
	<#elseif hasDate=='3'>
	<span class="portal-box-more-sc">&nbsp<a href="#" onclick="openListMoreByChnlNomal('${CHNL_ID}','${title}')">更多</a></span>
	<#else>
	<span class="portal-box-more-sc">&nbsp<a href="#" onclick="openDateListMoreByChnl('${CHNL_ID}','${title}')">更多</a></span>
	</#if>
</div>
<#if conhei=='auto'>
<div class='portal-box-con info-column' style='height:auto;'>
<#else>
<div class='portal-box-con info-column' style='height:${hei}px;max-height:${hei}px;'>
</#if>
<table width="100%" style="table-layout:fixed;margin-left:8px;">
<#if (_DATA_?size == 0)>
<tr><td align="center">该栏目下没有信息！</td></tr>
</#if>

<#list _DATA_ as content>
<tr>
	<td class="icon"></td>
	<td style="width:76%;position: relative;">
		<a id = '${CHNL_ID}${content.NEWS_ID}' title='${content.NEWS_SUBJECT}' style="width:100%;margin-left:3px;display: block;height:28px;" href="javascript:void(0);" onclick="newsView('${content.NEWS_ID}')">
			<span class="elipd">${content.NEWS_SUBJECT}</span>
			<script>
			var newQx = "${NEW_QX!'0'}";
			if (rhDate.doDateDiff('D',"${content.NEWS_TIME?substring(0,10)}",rhDate.getCurentTime().substring(0, 10))<=newQx) {
				$("<i class='new'></i>").appendTo($("#${CHNL_ID}${content.NEWS_ID}"));
			}
			</script>
		</a>
	</td>
 	<td style="width:20%;">
		<span style="float:right;margin-right:6px;color:#999999;">
				${content.NEWS_TIME?substring(5,10)}
		</span>
	</td>
 
</tr>
</#list>
</table>
</div>
</div>
<script type ="text/javascript" >
	jQuery(document).ready(function(jQuery) {
		var boxHtml = jQuery("#BN_FCHC__temp #BN_FCHC__box").html();
		jQuery("#BN_FCHC__temp").html("<div class='portal-box pt-gsyw-wrapper' id='BN_FCHC__box'>" + boxHtml + "</div>");
	});
</script>