<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>关于前海再保险</title>
<link rel="stylesheet" type="text/css" href="/sc/css/infoMore_reset.css"/>
<style type="text/css">

 
</style>

<script type="text/javascript" src="/sy/base/frame/jquery-1.8.2.min.js"></script> 
<script type="text/javascript">
	function newsView(element){
		var url = "/cms/SY_COMM_INFOS/" + $(element).attr("data_id") + ".html";			
		window.open(url);
	}
	function showNews(chnlId){
		var news = FireFly.doAct("SY_COMM_INFOS_VIEW","getNews",{"CHNL_ID":chnlId});
		if(news.NEWS_BODY.length>1050){
		jQuery("#newsBody").html(news.NEWS_BODY.substring(0,1050)+"<a href='#' class='readMore' onclick='newsView(this)' data_id='"+news.NEWS_ID+"'>[全文阅读]</a>");
		}else{
		jQuery("#newsBody").html(news.NEWS_BODY);
		}
	}
</script>	
	
</head>
<body style="background-color:#fff !important;">
<div class="info-wrapper">
  	<div class="info-header">
  		<span>关于前海再保险</span>
  		<div class="info-header-icon"></div>
  	</div>
	<div class="info-title">
		<div style="float:left;width:15%;">&nbsp;</div>
		<div style="float:left;width:5px;">&nbsp;</div>
		<div style="float:left;width:80%;">内容</div>
		
	</div>
	 <div class = "news-content">  
		 <div class ="news-list-left" style="float:left;width:15%;" >
		 	<ul class="date-list"></ul>
		 </div>  
		 <div class = "news-list-right" style="float:left;width:85%;" >
			 <ul>
			<#if _DATA_?size ==0>
			<li style="font-family:宋体;color:#3e3e3e;">该栏目下没有消息！！！</li>
				
			</li>
			<#else>
				<#list _DATA_ as content>
				<li class="info-item">
				<span class="info-icon"></span>
				<span id = "newsBody" style="width:98%;" >
					<#if (content.NEWS_BODY?length >1050)>
						${content.NEWS_BODY?substring(0,1050)}...
						<a href='#' data_id='${content.NEWS_ID}' class='readMore' onclick="newsView(this)">[关于前海再保险]</a>
					<#else>
						${content.NEWS_BODY}
					</#if>
				
				</span>
				
				</#list>
			</#if>
			</ul>
			<script language="javascript">
			var params = {"CHNL_ID":"${CHNL_ID}","CHNL_PID":"${CHNL_PID}"};
			var newsData = FireFly.doAct("SY_COMM_INFOS_VIEW","getBNChnlAll",params);
			
			
			var listBeans = newsData.listBeans;
			for(var i=0;i<listBeans.length;i++){
			
			var listBean = listBeans[i];

				if(listBean.CHNL_ID != 'undefined'&&listBean.CHNL_ID == '${CHNL_ID}'){
					var dLi = jQuery("<li class='date-list-li-selected'></li>").appendTo(jQuery(".date-list"));
					jQuery("<a data-id='"+listBean.CHNL_ID+"' href='javascript:void(0);'  title="+listBean.CHNL_NAME+" >"+listBean.CHNL_NAME+"</a>").appendTo(dLi).unbind("click").bind("click",function(){
						var parentObj = jQuery(".date-list li");
						for(var m=0;m<parentObj.length;m++){
							if($(parentObj[m]).hasClass("date-list-li-selected")){
								$(parentObj[m]).removeClass("date-list-li-selected");
							}
						}
						$(this).parent().addClass("date-list-li-selected");
						showNews($(this).attr("data-id"));
					});
				}else{
					var dLi = jQuery("<li></li>").appendTo(jQuery(".date-list"));
					jQuery("<a data-id='"+listBean.CHNL_ID+"' href='javascript:void(0);' title="+listBean.CHNL_NAME+">"+listBean.CHNL_NAME+"</a>").appendTo(dLi).unbind("click").bind("click",function(){
						var parentObj = jQuery(".date-list li");
						for(var n=0;n<parentObj.length;n++){
							if($(parentObj[n]).hasClass("date-list-li-selected")){
								$(parentObj[n]).removeClass("date-list-li-selected");
							}
						}
						$(this).parent().addClass("date-list-li-selected");
						
						showNews($(this).attr("data-id"));
					});
				}
			}
				
			
			</script>
			
		</div>      
	</div>
</div>
<script>
$(document).ready(function(){
	var docHei = parseInt($(document).find(".news-content").height())+190;
	var defaultHei = parent.GLOBAL.defaultFrameHei;
   	if(docHei<=defaultHei){
   		$(parent.document).find("#"+window.name).attr("style","position: relative;max-height:"+defaultHei+"px;height:"+defaultHei+"px");
   	}else{
   		$(parent.document).find("#"+window.name).attr("style","position: relative;max-height:"+docHei+"px;height:"+docHei+"px");
   	}
});
</script>
</body>
</html>