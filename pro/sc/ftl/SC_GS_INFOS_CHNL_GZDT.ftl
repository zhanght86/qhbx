<style type="text/css">
	.portal-box-title-bottom-line{width:78px;border-bottom: 1px #08C solid;top: 32px;position:absolute;}
	.pictureScroll-${CHNL_ID} {width:98%;margin:auto;margin-left:8px;}
	#banner-${CHNL_ID} {position:relative; width:200px;height:130px !important;border:1px;overflow:hidden; font-size:12px} 
	#banner_list-${CHNL_ID} img {border:0px;} 
	#banner_bg-${CHNL_ID} {position:absolute; bottom:0;height:30px;filter: Alpha(Opacity=30);opacity:0.3;z-index:1000;cursor:pointer; width:478px; } 
	#banner_info-${CHNL_ID}{position:absolute; bottom:4px; left:5px;height:22px;color:#fff;z-index:1001;cursor:pointer} 
	#banner_text-${CHNL_ID} {position:absolute;width:120px;z-index:1002; right:3px; bottom:3px;} 
	#banner-${CHNL_ID} ul {position:absolute;list-style-type:none;filter: Alpha(Opacity=80);opacity:0.8; z-index:9; 
	margin:0; padding:0; bottom:10px; right:5px; height:20px} 
	#banner-${CHNL_ID} ul li { padding:0 8px; line-height:22px;float:left;display:block;color:#FFF;margin-left:4px;background-color:#4e4c4c;cursor:pointer; font-size:16px;} 
	#banner_list-${CHNL_ID} a{position:absolute;}
	#first-new-${CHNL_ID} {width: 100%;margin-bottom:15px;}
	#first-new-title-${CHNL_ID} {float: left;height: 100%;width: 60%;display: inline-block;position:relative;}
	#all-read-${CHNL_ID} a {float:right;color:#000;position: absolute;left:1.5%;right:1.5%;line-height:25px;text-align: right;text-decoration: none;}
	#all-read-${CHNL_ID} a:hover{color: #0196E3;}
</style>
<#assign conhei = hei!"auto">
<div class='portal-box pt-gzdt-wrapper'>
	<#assign hasDate = hasDate!"2">
	<div class='portal-box-title ${titleBar}' style='position:relative;'>
		<span class='portal-box-title-top'></span>
		<span class="portal-box-title-label">${title}</span>
		<div class="portal-box-title-bottom-line"></div>
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
		<div id='chnlImg-${CHNL_ID}' style='border: 1px solid #eee;'>
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
		<table style="table-layout:fixed;width:99%"><tbody>
		<#if (_DATA_?size == 0)>
		<tr>
			<td colspan="2" align=center>该栏目下没有信息！</td>
		</tr>
		</#if>
		<#list _DATA_ as content>
		<tr>
			<td class="icon"></td>
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
			<td style="width:28%">
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
