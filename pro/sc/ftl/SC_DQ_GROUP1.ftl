<div class='portal-box portal-box-dq' style='height:369px'>
	<div class="portal-box-title" style="overflow: visible; filter: Alpha(Opacity=0);opacity: 0;position: absolute;top:0"></div>
	
	<div class='dq-tab-wrapper'>
		<ul class='tabControl' id='tabControl'>
            <li class='dq-tab-item current'>
                <a href='#js-dflzjy' title='党风廉政教育'> 党风廉政教育 </a>
            </li>
            <li class='dq-tab-item'>
                <a href='#js-xchl' title='宣传栏'> 宣传栏 </a>
            </li>
        </ul>
	</div>	    
	<div id="js-dflzjy" class='portal-box-con-dq dq-content dq-tab-h clearfix'>
		<#if _DATA_0._DATA_?size !=0 >
			<ul>
			<#list _DATA_0._DATA_ as obj>
					<li>
						<div class="news-title">
	                    	<span class="icon"></span>
							<a id = '${CHNL_ID1}${obj.NEWS_ID}' href='/cms/SY_COMM_INFOS/${obj.NEWS_ID}.html' data-id="${obj.NEWS_ID}" target="_blank">
							<span class="elipd" >${obj.NEWS_SUBJECT}</span>
							<script>
								var newQx = "${NEW_QX!'0'}";
								if (rhDate.doDateDiff('D',"${obj.NEWS_TIME?substring(0,10)}",rhDate.getCurentTime().substring(0, 10))<=newQx) {
									$("<i class='new'></i>").appendTo($("#${CHNL_ID1}${obj.NEWS_ID}"));
								}
							</script>
							</a>
	                    </div>
	                    <div class="news-date">${obj.S_ATIME?substring(0,10)}</div>
					</li>
			</#list>
			</ul>
			<a href="javascript:void(0);" onclick="openListMoreByChnl('${_DATA_0._DATA_[0].CHNL_ID}','学习教育')" class="dq-more-bottom dq-red">更多&gt;&gt;</a> 
		<#else>
			 暂无内容! 
		</#if>	
	</div>
	<div id="js-xchl" class='portal-box-con-dq dq-content dq-tab-h clearfix' style="display:none;">
		<#if _DATA_1._DATA_?size !=0 >
			<ul>
			<#list _DATA_1._DATA_ as obj>
					<li>
						<div class="news-title">
	                    	<span class="icon"></span>
							<a id = '${CHNL_ID2}${obj.NEWS_ID}' href='/cms/SY_COMM_INFOS/${obj.NEWS_ID}.html' data-id="${obj.NEWS_ID}" target="_blank">
							<span class="elipd" >${obj.NEWS_SUBJECT}</span>
							<script>
								var newQx = "${NEW_QX!'0'}";
								if (rhDate.doDateDiff('D',"${obj.NEWS_TIME?substring(0,10)}",rhDate.getCurentTime().substring(0, 10))<=newQx) {
									$("<i class='new'></i>").appendTo($("#${CHNL_ID2}${obj.NEWS_ID}"));
								}
							</script>
							</a>
	                    </div>
	                    <div class="news-date">${obj.S_ATIME?substring(0,10)}</div>
					</li>
			</#list>
			</ul>
			<a href="javascript:void(0);" onclick="openDqListMoreByChnl('${_DATA_1._DATA_[0].CHNL_ID}','我身边的先锋')" class="dq-more-bottom dq-red">更多&gt;&gt;</a> 
		<#else>
			 暂无内容! 
		</#if>
	</div>
</div>
<script>
$(document).ready(function(){
	$('.dq-tab-item').on('click',function(event){
			event.preventDefault();
			if($(this).hasClass('current')){
				return false;
			}
			var id=$(this).find('a').attr('href'),
			    hid=$(this).siblings('.current').find('a').attr('href');
			$(this).siblings('.current').removeClass('current');
			$(this).addClass('current');
			$(hid).hide();
			$(id).show();
	});
});
</script>