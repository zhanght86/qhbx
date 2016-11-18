<div id='SC_COMM_CMS_TMPL' class='portal-box ${boxTheme}'>
<div class='portal-box-title ${titleBar}'>
	<span class="portal-box-title-pre"></span>
	<span class="portal-box-title-label">${title}</span>
	<span class='portal-box-title-fix'></span>
	<span class="portal-box-more-sc">>&nbsp<a href="#" onclick="openListMoreByChnl('${CHNL_ID}','${title}')">更多</a></span>
</div>
<div class='portal-box-con info-column' style="margin-top:10px;">
<table width="100%" style="table-layout:fixed;">
	<tbody>
		<#if (_DATA_?size == 0)>
		<tr><td align=center id='haha'>该栏目下没有信息！</td></tr>
		</#if>
		<#list _DATA_ as content>
			<#if content_index == 0>
				<script>
					$(".portal-box-more-sc a").attr("onclick","openListMoreByChnl('${content.CHNL_ID}')");
				</script>
				<div id='first-new' style='height: 130px;width: 100%;'>
					<div id='first-new-img' style='float: left;height: 100%;width: 40%;'>
						<img src='' style='float:left;width:200px;height:120px;margin-left: 10px;'/>
					</div>
					<div style="float: left;height: 100%;width: 58%;display: inline-block;">
						<a style='color: red;text-align: center;display: block;'>${content.NEWS_SUBJECT}</a>
						<p id='news-body'style='word-wrap: break-word;text-indent: 2em;'></p>
						<script type ="text/javascript" >
							paramBean = {};
							paramBean["newsBody"] = "${content.NEWS_BODY}";
							var data = FireFly.doAct("SC_COREMAIL_SERV","replaceHtmlTag",paramBean);
							jQuery("#news-body").text(data.newsBody);
						</script>
					</div>
					<input type='hidden' value="${content.NEWS_ID}" class="news-title">
				</div>
			<#else>
				<tr style="width:100% border-bottom:">
					<td style='width:5%; background:url("/sc/blue/img/bg.gif") no-repeat 0 -683px;'></td>
					<td class="elipd" style="width:70%;">
						<a title='${content.NEWS_SUBJECT}'style="margin-left:3px;" href="javascript:void(0);" onclick="newsView('${content.NEWS_ID}')">
							<#if (content.NEWS_SUBJECT?length >25)>
								${content.NEWS_SUBJECT?substring(0,25)}...
							<#else>
								${content.NEWS_SUBJECT}
							</#if>
						</a>
						<input type='hidden' value="${content.NEWS_ID}" class="news-title">
					</td>
					<td class="elipd" style="width:25%;">
						<span style="float:right;margin-right:6px;color:#999999;">
							<#if (content.NEWS_TIME?length >10)>
								${content.NEWS_TIME?substring(0,10)}
							<#else>
								${content.NEWS_TIME}
							</#if>
						</span>
					</td>
				</tr>
			</#if>
		</#list>
	</tbody>
</table>
</div>
</div>
<script type ="text/javascript" >
	jQuery(function(jQuery) {
 		var newsid = jQuery("#SC_COMM_CMS_TMPL").find("input[type='hidden']").first().val();
 		var newsWhere = {};
		newsWhere["_WHERE_"] = " and SERV_ID='SY_COMM_INFOS_BASE' and FILE_CAT='TUPIANJI' and DATA_ID='"+newsid+"'";
		var newsData = FireFly.doAct("SY_COMM_FILE","finds",newsWhere)._DATA_[0];
		var dataAdr = newsData.FILE_PATH;
		var newImg = dataAdr.substr(dataAdr.lastIndexOf("/")+1);
		jQuery("#first-new-img").find("img").attr("src","/file/"+newImg);
	});
</script>