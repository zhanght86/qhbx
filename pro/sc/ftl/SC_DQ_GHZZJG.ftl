<div class='portal-box portal-box-dq dq-header-gh' style='height:369px'>
	<div class="portal-box-title" style="overflow: visible; filter: Alpha(Opacity=0);opacity: 0;position: absolute;top:0"></div>
	
	<span class="dq-header-top"></span>
    <div class="dq-title"> 
     	<span>${title}</span>
	</div>
	<div class='portal-box-con-dq dq-content dq-gh-h clearfix'>
		<#if _DATA_?size !=0 >
			<ul>
			<#list _DATA_ as obj>
					<li>
						<div class="news-title">
	                    	<span class="icon"></span>
							<a id = '${CHNL_ID}${obj.NEWS_ID}' href='/cms/SY_COMM_INFOS/${obj.NEWS_ID}.html' data-id="${obj.NEWS_ID}" target="_blank">
							<span class="elipd" >${obj.NEWS_SUBJECT}</span>
							<script>
								var newQx = "${NEW_QX!'0'}";
								if (rhDate.doDateDiff('D',"${obj.NEWS_TIME?substring(0,10)}",rhDate.getCurentTime().substring(0, 10))<=newQx) {
									$("<i class='new'></i>").appendTo($("#${CHNL_ID}${obj.NEWS_ID}"));
								}
							</script>
							</a>
	                    </div>
	                    <div class="news-date">
	                    	<#if (obj.NEWS_TIME?length >10)>
								${obj.NEWS_TIME?substring(0,10)}
							<#else>
								${obj.NEWS_TIME}
							</#if>
	                    </div>
					</li>
			</#list>
			</ul>
			<a href="javascript:void(0);" onclick="openDqListMoreByChnl('${CHNL_ID}','${title}')" class="dq-more-top dq-gray">&gt;&nbsp更多</a> 
		<#else>
			暂无内容!
		</#if>	
	</div>
</div>