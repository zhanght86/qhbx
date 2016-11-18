<!-- 党群门户中两列版--第一条信息展示图片的模板   -->
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
			<#list _DATA_ as objone>
				<#if (objone_index == 0)>
					<script>
						(function ($) {
							var newsId = '${objone.NEWS_ID}';
							var newsBody = '${objone.NEWS_BODY}';
							var newsTitle = '${objone.NEWS_SUBJECT}';
							
							var newsWhere = {};
							newsWhere["_WHERE_"] = " and SERV_ID='SY_COMM_INFOS_BASE' and FILE_CAT='FENGMIANJI' and DATA_ID='"+newsId+"'";
						    newsWhere["_ORDER_"] = "FILE_SORT ASC,S_MTIME ASC";
						    var newsData = FireFly.doAct("SY_COMM_FILE","finds",newsWhere);
						    if (newsData._DATA_.length > 0) {
						        var fileId = newsData._DATA_[0].FILE_ID;
						        $("#" + newsId + "big-img-js").html('<img src="/file/' + fileId + '?size=158x92" onclick=newsView("' + newsId + '"); />');
						        if (newsTitle.length > 25) {
						        	newsTitle = newsTitle.substr(0,25) + "...";
						        } 
						        if (newsBody.length > 80) {
						        	newsBody = newsBody.substr(0,80) + "...";
						        }
						    } else {
						        $("#" + newsId + "big-img-js").parent().hide();
						        $("." + newsId + "big-first-right-js").css("width","98%");
						        if (newsTitle.length > 36) {
						            newsTitle = newsTitle.substr(0,36) + "...";
						        }
						        if (newsBody.length > 120) {
						            newsBody = newsBody.substr(0,120) + "...";
						        }
						    }
						    $("." + newsId + "big-first-right-title-js").html(newsTitle);
						    $("." + newsId + "news-body-js").html(newsBody);
						})(jQuery);
					</script>
					<div class="dq-big-first-news">
						<div class="big-first-left">
							<div id="${objone.NEWS_ID}big-img-js" class="big-first-left-img"></div>
						</div>
						<div class="${objone.NEWS_ID}big-first-right-js big-first-right">
							<span class="${objone.NEWS_ID}big-first-right-title-js big-first-right-title" onclick="newsView('${objone.NEWS_ID}');"></span>
							<span class="big-first-right-content"><span class="${objone.NEWS_ID}news-body-js news-body-js"></span>
								<span class="big-first-right-btn">
									[&nbsp;&nbsp;
										<a id='${CHNL_ID}${objone.NEWS_ID}' href='/cms/SY_COMM_INFOS/${objone.NEWS_ID}.html' data-id="${objone.NEWS_ID}" target="_blank">
										全文阅读
										</a>
									&nbsp;&nbsp;]
								</span>
							</span>
						</div>
					</div>
				</#if>
			</#list>
			<ul>
				<#list _DATA_ as obj>
				<#if (obj_index > 0) >
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
				</#if>
				</#list>
			</ul>
			<a href="javascript:void(0);" onclick="openDqListMoreByChnl('${_DATA_[0].CHNL_ID}','${title}')" class="dq-more-top dq-gray">更多</a>
		<#else>
			暂无内容!
		</#if>
	</div>
</div>