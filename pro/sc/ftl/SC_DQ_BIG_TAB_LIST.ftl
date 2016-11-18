<!-- 党群门户中两列版--有Tab切换的模板   -->
<div class='portal-box portal-box-dq dq-header-tzz' style='height:377px;width:568px;margin-top:20px;'>
	<div class="portal-box-title" style="overflow: visible; filter: Alpha(Opacity=0);opacity: 0;position: absolute;top:0"></div>
	<span class="dq-header-top" style="background-repeat: repeat-x;"></span>
	<div class="dq-big-title">
		<span class="selected dq-btn-item-js ${iconType!''}">
			<span class="dq-big-title-left"></span>
			<span class="dq-big-title-middle dq-tab-btn-js" divId="tf-js-${CHNL_ID2}">${title1}</span>
			<span class="dq-big-title-right"></span>
		</span>
		<span class="default dq-btn-item-js ${iconType!''}">
			<span class="dq-big-title-left" style="margin-left:-2px;"></span>
			<span class="dq-big-title-middle dq-tab-btn-js" divId="tg-js-${CHNL_ID1}">${title2}</span>
			<span class="dq-big-title-right"></span>
		</span>
		<span class="dq-big-title-shadow"></span>
	</div>    
	
	<div id="tf-js-${CHNL_ID2}" class='portal-box-con-dq dq-content dq-big-tzz-h clearfix dq-div-item-js'>
		<#if _DATA_0._DATA_?size !=0 >
			<#list _DATA_0._DATA_ as objone0>
				<#if (objone0_index == 0)>
					<script>
						(function ($) {
							var newsId = '${objone0.NEWS_ID}';
							var newsBody = '${objone0.NEWS_BODY}';
							var newsTitle = '${objone0.NEWS_SUBJECT}';
							
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
							<div id="${objone0.NEWS_ID}big-img-js" class="big-first-left-img"></div>
						</div>
						<div class="${objone0.NEWS_ID}big-first-right-js big-first-right">
							<span class="${objone0.NEWS_ID}big-first-right-title-js big-first-right-title" onclick="newsView('${objone0.NEWS_ID}');"></span>
							<span class="big-first-right-content"><span class="${objone0.NEWS_ID}news-body-js news-body-js"></span>
								<span class="big-first-right-btn">
									[&nbsp;&nbsp;
										<a id='${CHNL_ID2}${objone0.NEWS_ID}' href='/cms/SY_COMM_INFOS/${objone0.NEWS_ID}.html' data-id="${objone0.NEWS_ID}" target="_blank">
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
				<#list _DATA_0._DATA_ as obj0>
				<#if (obj0_index > 0) >
				<li>
					<div class="news-title" style="width:445px;">
	                	<span class="icon"></span>
						<a id = '${CHNL_ID2}${obj0.NEWS_ID}' href='/cms/SY_COMM_INFOS/${obj0.NEWS_ID}.html' data-id="${obj0.NEWS_ID}" target="_blank">
						<span class="elipd" style="max-width:97%;">${obj0.NEWS_SUBJECT}</span>
						<script>
							var newQx = "${NEW_QX!'0'}";
							if (rhDate.doDateDiff('D',"${obj0.NEWS_TIME?substring(0,10)}",rhDate.getCurentTime().substring(0, 10))<=newQx) {
								$("<i class='new'></i>").appendTo($("#${CHNL_ID2}${obj0.NEWS_ID}"));
							}
						</script>
						</a>
	                </div>
	                <div class="news-date">${obj0.S_ATIME?substring(0,10)}</div>
				</li>
				</#if>
				</#list>
			</ul>
			<a href="javascript:void(0);" onclick="openDqListMoreByChnl('${_DATA_0._DATA_[0].CHNL_ID}','${title1}')" class="dq-more-top dq-gray">更多</a>
		<#else>
			暂无内容!
		</#if>
	</div>
	
	<div id="tg-js-${CHNL_ID1}" style="display:none;" class='portal-box-con-dq dq-content dq-big-tzz-h clearfix dq-div-item-js'>
		<#if _DATA_1._DATA_?size !=0 >
			<#list _DATA_1._DATA_ as objone1>
				<#if (objone1_index == 0)>
					<script>
						(function ($) {
							var newsId = '${objone1.NEWS_ID}';
							var newsBody = '${objone1.NEWS_BODY}';
							var newsTitle = '${objone1.NEWS_SUBJECT}';
							
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
							<div id="${objone1.NEWS_ID}big-img-js" class="big-first-left-img"></div>
						</div>
						<div class="${objone1.NEWS_ID}big-first-right-js big-first-right">
							<span class="${objone1.NEWS_ID}big-first-right-title-js big-first-right-title" onclick="newsView('${objone1.NEWS_ID}');"></span>
							<span class="big-first-right-content"><span class="${objone1.NEWS_ID}news-body-js news-body-js"></span>
								<span class="big-first-right-btn">
									[&nbsp;&nbsp;
										<a id='${CHNL_ID1}${objone1.NEWS_ID}' href='/cms/SY_COMM_INFOS/${objone1.NEWS_ID}.html' data-id="${objone1.NEWS_ID}" target="_blank">
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
				<#list _DATA_1._DATA_ as obj1>
				<#if (obj1_index > 0) >
				<li>
					<div class="news-title" style="width:445px;">
	                	<span class="icon"></span>
						<a id = '${CHNL_ID1}${obj1.NEWS_ID}' href='/cms/SY_COMM_INFOS/${obj1.NEWS_ID}.html' data-id="${obj1.NEWS_ID}" target="_blank">
						<span class="elipd" style="max-width:98%;">${obj1.NEWS_SUBJECT}</span>
						<script>
							var newQx = "${NEW_QX!'0'}";
							if (rhDate.doDateDiff('D',"${obj1.NEWS_TIME?substring(0,10)}",rhDate.getCurentTime().substring(0, 10))<=newQx) {
								$("<i class='new'></i>").appendTo($("#${CHNL_ID1}${obj1.NEWS_ID}"));
							}
						</script>
						</a>
	                </div>
	                <div class="news-date">${obj1.S_ATIME?substring(0,10)}</div>
				</li>
				</#if>
				</#list>
			</ul>
			<a href="javascript:void(0);" onclick="openDqListMoreByChnl('${_DATA_1._DATA_[0].CHNL_ID}','${title2}')" class="dq-more-top dq-gray">更多</a>
		<#else>
			暂无内容!
		</#if>
	</div>
</div>
<script>
	$(document).ready(function () {
		$(".dq-tab-btn-js").off("click").on("click",function () {
			var $self = $(this);
			if ($self.parent().hasClass("selected")) {
				return;
			}
			$self.parent().parent().find(".dq-btn-item-js").removeClass("selected").addClass("default");
			$self.parent().removeClass("default").addClass("selected");
			$("#"+$self.attr("divId")).show().siblings(".dq-div-item-js").hide();
		});
	});
</script>