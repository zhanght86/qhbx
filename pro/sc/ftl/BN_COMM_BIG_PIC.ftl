<style>
.title-dt{color:#999;}

</style>
<script type="text/javascript">
	function openView(element){
		var newsID = jQuery(element).attr("data-id");
		newsView(newsID);
	}

</script>
<#assign chnlName = CHNL_NAME!"">
<div class='portal-box-con' style='margin-top:7px;width:362px;height:${hei}px;max-height:${hei}px;border:0.5px #d1d6d2 solid;'>
<div class="entrance" style='margin-top:7px;'>
	<#list _DATA_ as content>
		<#if content_index == 0>	
				
		
        <a href="#" onclick="openDateListMoreByChnl('${CHNL_ID}','${CHNL_NAME}')" class="tolist">
								<strong class="s1" style="color:${firstColor};">${CHNL_NAME?substring(0,1)}</strong>
								<strong>${CHNL_NAME?substring(1)}</strong>
            <span>
				<img style="float:left;margin-top:8px;" src="/file/${content.picture.FILE_ID}">
			</span>
        </a>
			<dl>
			<#if (content.NEWS_SUBJECT?length < 17)>
				<dt data-id="${content.NEWS_ID}" style="font-family: Microsoft YaHei;color:#999;margin-top:8px;cursor:pointer;" onclick="openView(this);">${content.NEWS_SUBJECT}</dt>
			<#else>
				<dt data-id="${content.NEWS_ID}" style="font-family: Microsoft YaHei;color:#999;margin-top:8px;cursor:pointer;" onclick="openView(this);">${content.NEWS_SUBJECT?substring(0,17)}...</dt>
			</#if>	
			<#if (content.NEWS_SHORT_TITLE?length <=23)>
				<dd style="font-family: Microsoft YaHei;color:#999;">${content.NEWS_SHORT_TITLE?substring(0,content.NEWS_SHORT_TITLE?length)}</dd>
			<#elseif (content.NEWS_SHORT_TITLE?length >23 && content.NEWS_SHORT_TITLE?length <=40)>
				<dd style="font-family: Microsoft YaHei;color:#999;">${content.NEWS_SHORT_TITLE?substring(0,23)}</dd>
					<dd style="font-family: Microsoft YaHei;color:#999;position:relative;left:-2em;">${content.NEWS_SHORT_TITLE?substring(23,content.NEWS_SHORT_TITLE?length)}...</dd>
			<#else>
				<dd style="font-family: Microsoft YaHei;color:#999;">${content.NEWS_SHORT_TITLE?substring(0,23)}</dd>
					<dd style="font-family: Microsoft YaHei;color:#999;position:relative;left:-2em;">${content.NEWS_SHORT_TITLE?substring(23,40)}...</dd>
			
			</#if>	
				
			</dl>
        
		</#if>
	</#list>
    </div>
	</div>