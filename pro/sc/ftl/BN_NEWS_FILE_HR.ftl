<link rel="stylesheet" type="text/css" href="/sc/css/system_reset.css"/>
<style>

</style>
<div  class='portal-box pt-gsyw-wrapper hidebor' id='${id}__box'>

<#assign hasDate = hasDate!"2">
<#assign conhei = hei!"auto">

<div class='portal-box-title doubanner' style='position:relative;'>
	
	<ul>
		<li class="${C} on" chnlId="${_DATA_0.CHNL_ID}" ${C} style="float:left;margin:0px 10px;" onmouseover="changeBox(this)">
		<span>${_DATA_0.CHNL_NAME}</span>
		</li>
		<li class="${C}" style="float:left;margin:0px 10px;" chnlId="${_DATA_1.CHNL_ID}" onmouseover="changeBox(this)">
		<span>${_DATA_1.CHNL_NAME}</span>
		</li>
	</ul>
	
	<#if hasDate=='2'>
	<span class="portal-box-more-sc">&nbsp<a href="#" onclick="openListMoreByChnl('${CHNL_ID}','${title}')">更多</a></span>
	<#elseif hasDate=='3'>
	<span class="portal-box-more-sc">&nbsp<a href="#" onclick="openListMoreByChnlNomal('${CHNL_ID}','${title}')">更多</a></span>
	<#else>
	<span class="portal-box-more-sc">&nbsp<a href="#" onclick="openMoreByChnl('${C}')">更多</a></span>
	</#if>
</div>
<#if conhei=='auto'>
<div class='portal-box-con' style='height:auto;'>
<#else>
<div class='portal-box-con' style='height:${hei}px;max-height:${hei}px;'>
</#if>
<div id="1">
<#if (_DATA_0._DATA_?size == 0)>
<tr><td align="center">该栏目下没有信息！</td></tr>
</#if>
<ul id="${CHNL_ID}" class="uly">
<#list _DATA_0._DATA_ as content>
<script>
</script>
	<li style="width:100%;position: relative;">
		<a id = '${CHNL_ID}${content.NEWS_ID}' title='${content.NEWS_SUBJECT}' style="width:96.5%;margin-left:8px;display: block;height:25px;margin-top:3px;" href="javascript:void(0);" onclick="newsView('${content.NEWS_ID}')">
			<span class="icon" style="float:left;"></span>

			<span class="elipd" style="width:64%;float:left;height:25px;line-height:25px;">${content.NEWS_SUBJECT}</span>
			<#--
			<script>
			var newQx = "${NEW_QX!'0'}";
			if (rhDate.doDateDiff('D',"${content.NEWS_TIME?substring(0,10)}",rhDate.getCurentTime().substring(0, 10))<=newQx) {
				$("<i class='new'></i>").appendTo($("#${CHNL_ID}${content.NEWS_ID}"));
			}
			</script>
			-->
			<span style="float:right;margin-right:6px;line-height:25px;color:#999999;">
				[${content.NEWS_TIME?substring(0,10)}]
		</span>
		</a>
	</li>
</#list>
</ul>
</div>
<div id="2" style="display:none;">
<#if (_DATA_1._DATA_?size == 0)>
<tr><td align="center">该栏目下没有信息！</td></tr>
<#else>
<ul id="${CHNL_ID}" class="uly">
<#list _DATA_1._DATA_ as content>
	<li style="width:100%;position: relative;">
		<a id = '${CHNL_ID}${content.NEWS_ID}' title='${content.NEWS_SUBJECT}' style="width:96.5%;padding-left:8px;display: block;height:25px;margin-top:3px;" href="javascript:void(0);" onclick="newsView('${content.NEWS_ID}')">
			<span class="icon" style="float:left;"></span>

			<span class="elipd" style="width:64%;float:left;height:25px;line-height:25px;">${content.NEWS_SUBJECT}</span>
			<#--
			<script>
			var newQx = "${NEW_QX!'0'}";
			if (rhDate.doDateDiff('D',"${content.NEWS_TIME?substring(0,10)}",rhDate.getCurentTime().substring(0, 10))<=newQx) {
				$("<i class='new'></i>").appendTo($("#${CHNL_ID}${content.NEWS_ID}"));
			}
			</script>
			-->
			<span style="float:right;margin-right:6px;line-height:25px;color:#999999;">
				[${content.NEWS_TIME?substring(0,10)}]
		</span>
		</a>
	</li>

</#list>
	</ul>
</#if>
</div>
</div>
</div>
<script type ="text/javascript" >
	/*jQuery(document).ready(function(jQuery) {
		var boxHtml = jQuery("#${id}__temp #${id}__box").html();
		jQuery("#${id}__temp").html("<div class='portal-box pt-gsyw-wrapper' style='border:0px #000 solid;width:92%;' id='BN_NEWS_FILE_HR__box'>" + boxHtml + "</div>");
	});*/
	function changeBox(obj){
	var MsgInx = $(obj).index();
	$(obj).addClass("on");
	$(obj).siblings().removeClass("on");
	$(obj).parent().parent().siblings(".portal-box-con").children().eq(MsgInx).show();
	$(obj).parent().parent().siblings(".portal-box-con").children().eq(MsgInx).siblings().hide();
}
function openMoreByChnl(dd){
	var chnlId = $("."+dd+".on").attr("chnlId");
	var chnlName = $("."+dd+".on>span").text();
	openDateListMoreByChnl(chnlId,chnlName);
}
</script>
