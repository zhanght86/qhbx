<div class='portal-box portal-box-dq padding:0'>
	<div class="portal-box-title-dq"></div>	    
        <span class="portal-box-title-dq-label">${title}</span>
        <span class="portal-box-more-sc-dq">>&nbsp<a href="#" onclick="openDqListMoreByChnl('${CHNL_ID}','${title}')">更多</a></span>
	<div class="portal-box-con-dq">
		<#if (_DATA_?size == 0)>
		<div>该栏目下没有信息！</div>
		</#if>
		<#list  _DATA_ as content>
		<ul>
			<li></li>
			<li><a href="#" onclick="newsView('${content.NEWS_ID}')" title="${content.NEWS_SUBJECT}">${content.NEWS_SUBJECT}</a></li>
			<li>
				<#if (content.NEWS_TIME?length >10)>
					${content.NEWS_TIME?substring(0,10)}
				<#else>
					${content.NEWS_TIME}
				</#if>
			</li>
		</ul>
		</#list>
	</div>
</div>