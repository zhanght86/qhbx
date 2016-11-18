<#assign conhei = hei!"auto">
<div class='portal-box ${boxTheme}'>
	<#assign hasDate = hasDate!"2">
	<div class='portal-box-title'>
		<span class="portal-box-title-pre"></span>
		<span class="portal-box-title-label">${title}</span>
		<span class='portal-box-title-fix'></span>
		<#if hasDate=='2'>
		<span class="portal-box-more-sc">&nbsp<a href="#" onclick="openListMoreByChnl('${CHNL_ID}','${title}')">更多</a></span>
		<#elseif hasDate=='3'>
		<span class="portal-box-more-sc">&nbsp<a href="#" onclick="openListMoreByChnlNomal('${CHNL_ID}','${title}')">更多</a></span>
		<#else>
		<span class="portal-box-more-sc">&nbsp<a href="#" onclick="openDateListMoreByChnl('${CHNL_ID}','${title}')">更多</a></span>
		</#if>
	</div>
	<#if conhei=='auto'>
	<div class='portal-box-con'  style='height:auto;'>
	<#else>
	<div class='portal-box-con'  style='height:${hei}px;max-height:${hei}px;'>
	</#if>
		<div id='chnlImg-${CHNL_ID}' style='border: 1px solid #eee;padding: 3px;margin:6px;'>
			<img width=100% height=75px ></img>
		</div>
		<script>
		 	var newsWhere = {};
			newsWhere["_WHERE_"] = " and SERV_ID='SY_COMM_CMS_CHNL' and FILE_CAT='LANMUTUPIAN' and DATA_ID='${CHNL_ID}'";
			newsWhere["_ORDER_"] = "S_MTIME ASC";
			var newsData = FireFly.doAct("SY_COMM_FILE","finds",newsWhere);
			if(newsData._DATA_==0){
				jQuery("#chnlImg-${CHNL_ID}").remove();
			}else{
				var imgAdr = newsData._DATA_[0].FILE_PATH;
				var imgPath = "/file/"+imgAdr.substr(imgAdr.lastIndexOf("/")+1);
				jQuery("#chnlImg-${CHNL_ID} img").attr("src",imgPath);
			}
		</script>
		<table style="table-layout:fixed;"><tbody>
		<#if (_DATA_?size == 0)>
		<tr>
			<td colspan="2" align=center>该栏目下没有信息！</td>
		</tr>
		</#if>
		<#list _DATA_ as content>
		<tr>
			<td  style="width:70%;position: relative;">
				<a id = '${CHNL_ID}${content.NEWS_ID}' title='${content.NEWS_SUBJECT}' style="width:100%;margin-left:3px;display: block;height:28px;" href="javascript:void(0);" onclick="newsView('${content.NEWS_ID}')">
					<span class="elipd" >${content.NEWS_SUBJECT}</span>
					<script>
					if (rhDate.getCurentTime().substring(0, 10) == "${content.NEWS_TIME?substring(0,10)}") {
						$("<i class='new'></i>").appendTo($("#${CHNL_ID}${content.NEWS_ID}"));
					}
					</script>
				</a>
			</td>
			<td style="width:30%">
				<span style="float:right;margin-right:6px;color:#999999;">
					<#if (content.NEWS_TIME?length >10)>
						${content.NEWS_TIME?substring(0,10)}
					<#else>
						${content.NEWS_TIME}
					</#if>
				</span>
			</td>
		</tr>
		</#list>
		</tbody></table>
	</div>
</div>