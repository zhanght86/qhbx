<div class='portal-box portal-box-dq padding:0'>
	<div class="portal-box-title-dq"></div>	    
        <span class="portal-box-title-dq-label">${title}</span>
        <span class="portal-box-more-sc-dq">>&nbsp<a href="#" onclick="openDqListMoreByChnl('${CHNL_ID}','${title}')">更多</a></span>
	<div class="portal-box-con-dq">
		<#if (_DATA_?size == 0)>
		<div>该栏目下没有信息！</div>
		</#if>
		<#list  _DATA_ as content>
		<#if content_index == 0>
		<div><img width=255 height=170px id='dqChnlImg-${CHNL_ID}'></img></div>
		<script>
		 	var newsWhere = {};
			newsWhere["_WHERE_"] = " and SERV_ID='SY_COMM_CMS_CHNL' and FILE_CAT='TUPIANJI' and DATA_ID='${content.NEWS_ID}'";
			newsWhere["_ORDER_"] = "S_MTIME ASC";
			var newsData = FireFly.doAct("SY_COMM_FILE","finds",newsWhere);
			if(newsData._DATA_==0){
				jQuery("#chnlImg-${content.NEWS_ID}").remove();
			}else{
				var imgAdr = newsData._DATA_[0].FILE_PATH;
				var imgPath = "/file/"+imgAdr.substr(imgAdr.lastIndexOf("/")+1);
				jQuery("#dqChnlImg-${CHNL_ID}").attr("src",imgPath);
			}
		</script>
		</#if>
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