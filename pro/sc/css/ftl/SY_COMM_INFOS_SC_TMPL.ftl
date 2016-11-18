<link rel="stylesheet" type="text/css" href="/sc/css/system_reset.css"/>
<style type="text/css">
	.portal-box-title-bottom-line{width:78px;border-bottom: 2px #d20000 solid;top: 32px;position:absolute;}
	.pictureScroll-${$CHNL_ID$} {width:98%;margin:auto;margin-left:8px;}
	#banner-${$CHNL_ID$} {position:relative; width:200px;height:130px !important;border:1px;overflow:hidden; font-size:12px} 
	#banner_list-${$CHNL_ID$} img {border:0px;} 
	#banner_bg-${$CHNL_ID$} {position:absolute; bottom:0;height:30px;filter: Alpha(Opacity=30);opacity:0.3;z-index:1000;cursor:pointer; width:478px; } 
	#banner_info-${$CHNL_ID$}{position:absolute; bottom:4px; left:5px;height:22px;color:#fff;z-index:1001;cursor:pointer} 
	#banner_text-${$CHNL_ID$} {position:absolute;width:120px;z-index:1002; right:3px; bottom:3px;} 
	#banner-${$CHNL_ID$} ul {position:absolute;list-style-type:none;filter: Alpha(Opacity=80);opacity:0.8; z-index:9; 
	margin:0; padding:0; bottom:10px; right:5px; height:20px} 
	#banner-${$CHNL_ID$} ul li { padding:0 8px; line-height:22px;float:left;display:block;color:#FFF;margin-left:4px;background-color:#4e4c4c;cursor:pointer; font-size:16px;} 
	#banner_list-${$CHNL_ID$} a{position:absolute;}
	#first-new-${$CHNL_ID$} {width: 100%;margin-bottom:10px;}
	#first-new-title-${$CHNL_ID$} {float: left;height: 100%;width: 60%;display: inline-block;position:relative;}
	a.newTitle{display: block;color: #cb0428;font-size:14px;font-weight:bold;line-height:24px;text-align: center;text-decoration: none;text-overflow:ellipsis;white-space:nowrap;overflow:hidden;}
	a.readMore {float:right;color:#000;line-height:25px;text-align: right;text-decoration: none;}
	a.readMore:hover{color: #0196E3;}
</style>
<div id='SC_COMM_CMS_TMPL-${$CHNL_ID$}' class='portal-box pt-gsyw-wrapper'>
<#assign hasDate = hasDate!"2">
<#assign conhei = hei!"auto">
<div class='portal-box-title ${titleBar}' style='position:relative;'>
	<span class='portal-box-title-top'></span>
	<span class="portal-box-title-label">${title}</span>
	<div class="portal-box-title-bottom-line"></div>
	<#if hasDate=='2'>
	<span class="portal-box-more-sc">&nbsp<a href="#" onclick="openListMoreByChnl('${$CHNL_ID$}','${title}')">更多</a></span>
	<#elseif hasDate=='3'>
	<span class="portal-box-more-sc">&nbsp<a href="#" onclick="openListMoreByChnlNomal('${$CHNL_ID$}','${title}')">更多</a></span>
	<#else>
	<span class="portal-box-more-sc">&nbsp<a href="#" onclick="openDateListMoreByChnl('${$CHNL_ID$}','${title}')">更多</a></span>
	</#if>
</div>
<#if conhei=='auto'>
<div class='portal-box-con info-column' style="padding-top:10px;height:auto;">
<#else>
<div class='portal-box-con info-column' style="padding-top:10px;height:${hei}px;max-height:${hei}px;">
</#if>
<table width="100%" style="table-layout:fixed;width:100%">
	<tbody>
		<#if (_DATA_?size == 0)>
		<tr><td align=center>该栏目下没有信息！</td></tr>
		<#else>
		<#list _DATA_ as content>
			<#if content_index == 0>
				<div id='first-new-${$CHNL_ID$}' class='first-new-${$CHNL_ID$}' isPaused="${content.IS_PAUSED}">
					<div id='PICTURE_SCROLL-${$CHNL_ID$}' style='float: left;height: 100%;width: 40%;'>
						<div id="banner-${$CHNL_ID$}">
							<div id="banner_bg-${$CHNL_ID$}" ></div> 
							<div id="banner_info-${$CHNL_ID$}"></div>
							<div id="banner_list-${$CHNL_ID$}"></div>  
						</div>	
					</div>
					<div id='first-new-title-${$CHNL_ID$}' class='first-new-title-${$CHNL_ID$}'>
						<a href="#" onclick="newsView('${content.NEWS_ID}')" title='${content.NEWS_SUBJECT}' class='newTitle'>
								${content.NEWS_SUBJECT}
						</a>
						<p id='news-body-${$CHNL_ID$}'style='word-wrap: break-word;text-indent: 2em;line-height:22px;color:#9da0a4;'>
						<#if (content.NEWS_SHORT_TITLE?length >45)>
								${content.NEWS_SHORT_TITLE?substring(0,45)}...
							<#else>
								${content.NEWS_SHORT_TITLE}
							</#if>
								
							<a href='#' class='readMore' onclick="newsView('${content.NEWS_ID}')">[全文阅读]</a>
						</p>
					</div>
					<input type='hidden' value="${content.NEWS_ID}" class="news-title">
				</div>
				<div style='border-bottom:1px #cccccc dashed;'></div>
			<#else>
				<tr>
					<td class="icon"></td>
					<td  style="width:73%;position:relative">
						<a id='${$CHNL_ID$}${content.NEWS_ID}' title='${content.NEWS_SUBJECT}' style="width:100%;margin-left:3px;display: block;height:28px;" href="javascript:void(0);" onclick="newsView('${content.NEWS_ID}')">
							<span class="elipd" >
							<#if (content.NEWS_SUBJECT?length >20)>
								${content.NEWS_SUBJECT?substring(0,20)}...
							<#else>
								${content.NEWS_SUBJECT}
							</#if>
							</span>
							<script>
							var newQx = "${NEW_QX!'0'}";
							if (rhDate.doDateDiff('D',"${content.NEWS_TIME?substring(0,10)}",rhDate.getCurentTime().substring(0, 10))<=newQx) {
								$("<i class='new'></i>").appendTo($("#${$CHNL_ID$}${content.NEWS_ID}"));
							}
							</script>
						</a>
						<input type='hidden' value="${content.NEWS_ID}" class="news-title-${$CHNL_ID$}">
					</td>
				 	<td style="width:5%;" class="js-dept" data-index="${content_index}" data-tdept="${content.S_TDEPT}" data-odept="${content.S_ODEPT}">
					 </td>
					<td style="width:20%;">
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
		</#if>
	</tbody>
</table>
</div>
</div>
<script type ="text/javascript" >
	jQuery(document).ready(function(jQuery) {
 		var newsid = jQuery("#SC_COMM_CMS_TMPL-${$CHNL_ID$}").find("input[type='hidden']").first().val();
 		var newsWhere = {};
		newsWhere["_WHERE_"] = " and SERV_ID='SY_COMM_INFOS_BASE' and FILE_CAT='FENGMIANJI' and DATA_ID='"+newsid+"'";
		newsWhere["_ORDER_"] = "FILE_SORT ASC,S_MTIME ASC";
		var newsData = FireFly.doAct("SY_COMM_FILE","finds",newsWhere);
		var index = 0;
 
		if(newsData._DATA_.length > 0) {
			var bannerList = $("#banner_list-${$CHNL_ID$}");
			var dataAdr = newsData._DATA_[0].FILE_PATH;
			var newImg = "/file/"+dataAdr.substr(dataAdr.lastIndexOf("/")+1)+"?size=200x130";
			$("<a href='#' onclick=newsView('"+newsid+"')><img width='200px' height='130px' src='"+newImg+"' /></a>").appendTo(bannerList);
			$("#first-new-${$CHNL_ID$}").attr("style","height:130px")
		} else {
			$("#PICTURE_SCROLL-${$CHNL_ID$}").remove();
			var cont = $("#first-new-${$CHNL_ID$}").text();
			if(cont.length>136){
				$("#first-new-${$CHNL_ID$}").attr("style","min-height:70px;");
			}else{
				$("#first-new-${$CHNL_ID$}").attr("style","height:70px;");
			}		
			$("#first-new-title-${$CHNL_ID$}").attr("style","width:98%");
		}
		
		
		var deptCodeArr = [];
		$(".js-dept").each(function(i){
		 	var tdept = $(this).attr("data-tdept"),
		 		odept = $(this).attr("data-odept");
		 	deptCodeArr.push(tdept);
		 	if(odept){
		 		deptCodeArr.push(odept);
		 	}
		});
		deptCodeArr = $.unique(deptCodeArr);
		var ids = "'" + deptCodeArr.join("','") + "'";
		var deptOption ={
			"_WHERE_" : " AND DEPT_CODE in(" + ids +")",
			"_SELECT_":	"DEPT_CODE,DEPT_SHORT_NAME,DEPT_PCODE,DEPT_TYPE"
		};
		FireFly.doAct("SY_ORG_DEPT","finds",deptOption,false,false,function(data){
			var result = data._DATA_,
				len	   = result.length,
				temp  = {};
			if(len){
			 	$.each(result,function(i,obj){
			 		var deptShortName;
			 		if(obj["DEPT_TYPE"]==2) {
			 			deptShortName = obj["DEPT_SHORT_NAME"];
			 		} else {
			 			for(var i = 0 ; i<len ;i++){
			 				if( (obj["DEPT_PCODE"] == result[i]["DEPT_CODE"]) && (result[i]["DEPT_TYPE"]==2)){
			 					deptShortName = result[i]["DEPT_SHORT_NAME"];
			 					break;
			 				}
			 			}
			 		}
					$(".js-dept[data-tdept='" + obj["DEPT_CODE"] + "']").html(deptShortName);
			 	});
			 	
			}
		});
		
		
	
		
	}); 

</script>