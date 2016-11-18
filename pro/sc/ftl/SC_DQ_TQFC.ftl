<div class='portal-box portal-box-dq dq-header-tzz' style='height:497px'>
	<div class="portal-box-title" style="overflow: visible; filter: Alpha(Opacity=0);opacity: 0;position: absolute;top:0"></div>
	
	<span class="dq-header-top"></span>
    <div class="dq-title"> 
     	<span class="dq-white">${title}</span>
	</div>    
	<div class='portal-box-con-dq dq-content dq-tzz-h clearfix'>
		<#if _DATA_?size !=0 >
			<div>
	            <div class="dq-summary ${_DATA_[0].CHNL_ID}">
	            	<a href='/cms/SY_COMM_INFOS/${_DATA_[0].NEWS_ID}.html' data-id="${_DATA_[0].NEWS_ID}" class="dq-summary-title" target="_blank">
	            		${_DATA_[0].NEWS_SUBJECT}
	            	</a>
	                <p>
	                	${_DATA_[0].NEWS_BODY}
	                	<a href="/cms/SY_COMM_INFOS/${_DATA_[0].NEWS_ID}.html" class="dq-summary-read-more" target="_blank">[全文阅读]</a>
	                	
	                </p>
	            </div>
	        </div>
 			<script type ="text/javascript" >
				var imgParam = {};
					imgParam['_WHERE_'] = " and SERV_ID='SY_COMM_INFOS_BASE' and FILE_CAT='FENGMIANJI' and DATA_ID='${_DATA_[0].NEWS_ID}'";
					imgParam['_ORDER_'] = 'FILE_SORT ASC,S_MTIME ASC';
				var imgResult = FireFly.doAct("SY_COMM_FILE","finds",imgParam);
				if(imgResult._DATA_.length > 0){
					var path = imgResult._DATA_[0]['FILE_PATH'];
					var src  = "/file/"+path.substr(path.lastIndexOf("/")+1);
					jQuery("<img width='255' height='168'/>").attr("src",src).insertBefore(".dq-summary.${_DATA_[0].CHNL_ID}");
				} 
			</script>
			<ul>
			<#list _DATA_ as obj>
				<#if obj_index==0>
				<#else>
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
	                    <div class="news-date">${obj.S_ATIME?substring(0,10)}</div>
					</li>
				</#if>
			</#list>
			</ul>
			<a href="javascript:void(0);" onclick="openDqListMoreByChnl('${CHNL_ID}','${title}')" class="dq-more-top dq-gray">&gt;更多</a> 
		<#else>
			暂无内容!
		</#if>	
	</div>
</div>
<script>
 
</script>