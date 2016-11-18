<!-- 党群门户中两列版--没图只有信息列表的模板   -->
<div class='portal-box portal-box-dq dq-header-tzz' style='height:377px;width:568px;margin-top:20px;'>
	<div class="portal-box-title" style="overflow: visible; filter: Alpha(Opacity=0);opacity: 0;position: absolute;top:0"></div>
	<span class="dq-header-top" style="background-repeat: repeat-x;"></span>
	<div class="dq-big-title ${iconType!''}">
		<span class="dq-big-title-left"></span>
		<span class="dq-big-title-middle">${title}</span>
		<span class="dq-big-title-right"></span>
		<span class="dq-big-title-shadow"></span>
	</div>
	<div class='portal-box-con-dq dq-content dq-big-tzz-h clearfix'>
		<#if _DATA_?size !=0 >
		<ul>
			<#list _DATA_ as obj>
			<li>
				<div class="news-title" style="width:445px;">
                	<span class="icon"></span>
					<a id = '${CHNL_ID}${obj.NEWS_ID}' href='/cms/SY_COMM_INFOS/${obj.NEWS_ID}.html' data-id="${obj.NEWS_ID}" target="_blank">
					<span class="elipd" style="max-width:97%;">${obj.NEWS_SUBJECT}</span>
					<script>
						var newQx = "${NEW_QX!'0'}";
						if (rhDate.doDateDiff('D',"${obj.NEWS_TIME?substring(0,10)}",rhDate.getCurentTime().substring(0, 10))<=newQx) {
							$("<i class='new'></i>").appendTo($("#${CHNL_ID}${obj.NEWS_ID}"));
						}
					</script>
					</a>
                </div>
                <div class="news-date">${obj.S_ATIME?substring(0,10)}</div>
			</li>
		</#list>
		</ul>
		<a href="javascript:void(0);" onclick="openDqListMoreByChnl('${_DATA_[0].CHNL_ID}','${title}')" class="dq-more-top dq-gray">更多</a>
		<#else>
			暂无内容!
		</#if>
	</div>
</div>