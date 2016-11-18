<link rel="stylesheet" type="text/css" href="/sc/css/system_reset.css"/>
<div  class='portal-box pt-gsyw-wrapper' id="${id}__box">

<#assign hasDate = hasDate!"2">
<#assign conhei = hei!"auto">

<div class='portal-box-title ${titleBar}' style='position:relative;'>
	<span class='portal-box-title-top'></span>
	<span class="portal-box-title-label" style="color:#fff;">${title}</span>
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
<div class='portal-box-con info-column' style='height:auto;'>
<#else>
<div class='portal-box-con info-column' style='height:${hei}px;max-height:${hei}px;'>
</#if>
<table id="${CHNL_ID}" width="100%" style="table-layout:fixed;margin-left:8px;">

</table>
</div>
</div>
<script>
	jQuery(document).ready(function(jQuery) {
		var boxHtml = jQuery("#${id}__temp #${id}__box").html();
		jQuery("#${id}__temp").html("<div  class='portal-box pt-gsyw-wrapper' id='${id}__box'>" + boxHtml + "</div>");
	});
	jQuery(document).ready(function(jQuery) {
	var hrefStr = location.search;
	var odeptCode = System.getUser("ODEPT_CODE"); 
	if(hrefStr.indexOf("ODEPT_CODE")>=0){
	odeptCode = GetRequest("ODEPT_CODE");
	}else{
		if(odeptCode=="0001B210000000000BU3"){
		odeptCode = "0001B210000000000ET6";
		}
	}
		FireFly.doAct("SY_COMM_INFOS","getTopInfos",{"COUNT":"6","SITE_ID":"SY_COMM_SITE_INFOS","OPEN_SCOPE":"1","_WHERE_":"","CHNL_ID":"${CHNL_ID}"},null,false,function(returnData){
			 createNews3(returnData);
			
		});
	});
	function createNews3(newsLists){
		for(var i=0;i<newsLists._DATA_.length;i++){
			var listBean = newsLists._DATA_[i];
			var newsList = "<tr><td class='icon'></td><td style='width:69%;padding-left:3px;position: relative;'><a id = '${CHNL_ID}"+listBean.NEWS_ID+"' title='"+listBean.NEWS_SUBJECT+"' style='width:100%;margin-left:3px;display: block;height:28px;' news-id='"+listBean.NEWS_ID+"' href='javascript:void(0);' onclick='newsView(this)'><span class='elipd' >"+listBean.NEWS_SUBJECT+"</span>	</a></td><td style='width:26%;'><span style='float:right;margin-right:6px;color:#999999;'>["+listBean.NEWS_TIME.substring(0,10)+"]</span></td></tr>";
			jQuery("#${CHNL_ID}").append(newsList);
			var newQx = "${NEW_QX!'0'}";
			<#--
			if (rhDate.doDateDiff('D',listBean.NEWS_TIME.substring(0,10),rhDate.getCurentTime().substring(0, 10))<=newQx) {
				$("<i class='new'></i>").appendTo($("#${CHNL_ID}"+listBean.NEWS_ID));
			}
			-->
		}
	}
	/*打开新闻*/
	function newsView(element){
		var url = "/cms/SY_COMM_INFOS/" + $(element).attr("news-id") + ".html";			
		window.open(url);
	}
	/*获取url中的参数*/
	function GetRequest(param) {
	  var url = location.search; 
	  
	   if (url.indexOf("?") != -1) {
		  var str = url.substr(1);
		  
		  strs = str.split("&");
		  
		  for(var i = 0; i < strs.length; i ++) {
			  if(param==strs[i].split("=")[0]){
				return strs[i].split("=")[1];
			  }
				 
		  }
	   }
	   
	}
</script>