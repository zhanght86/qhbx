<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${data.NEWS_SUBJECT!"没有标题"}</title>
<#include "/SY_COMM_INFOS/globalinfos.ftl">
<link rel="stylesheet" type="text/css" href="/sy/comm/news/css/news.css"/>
<link rel="stylesheet" type="text/css" href="/sy/comm/poll/poll.css" />
<link rel="stylesheet" type="text/css" href="/sc/css/infoMore_reset.css"/>
<script type="text/javascript" src="/sy/base/frame/jquery-1.8.2.min.js"></script> 
<script type="text/javascript" src="/sy/comm/poll/poll.js"></script>

<style type="text/css">
#d-top {bottom: 10px;float: right;position: fixed;right: 20px;z-index: 10;}
.print {position: absolute; right: 10px;}
.info-wrapper-news{width:100%;margin:auto;color:#585858;}
.info-wrapper-news a {text-decoration: none;color:black;}
.info-wrapper-news ul, li {list-style: none;padding: 0;margin: 0;}
</style>
<script type ="text/javascript" >
function doPrint()
{
	if(window.print)
		window.print();
}
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
<body style="margin-top:0px;">
<#include "/SY_COMM_INFOS/config_constant.ftl">
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" newsid="${data.NEWS_ID}">
  <tr>
    <td valign="top" bgcolor="#FFFFFF">
    	<table width="1086px" border="0" align="center" cellpadding="0" cellspacing="0" class="mt5">
			<tr>
    			<td align="center">
    				<div width="1081px"><img style ="width:1088px;height:107px;" src="/sy/comm/cms/img/logo.jpg"/></div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="info-wrapper-news">
						<div class="info-header">
							<span>关于百年</span>
							<div class="info-header-icon"></div>
						</div>
						<div class="info-title">
							<div style="float:left;width:15%;">&nbsp;</div>
							<div style="float:left;width:5px;">&nbsp;</div>
							<div style="float:left;width:80%;">内容</div>
							
						</div>
						 <div class = "news-content">  
							 <div class ="news-list-left" style="float:left;width:20%;" >
								<ul class="date-list"></ul>
							 </div>  
							 <div class = "news-list-right" style="float:left;width:80%;" >
								 <ul>
								<#if data?size ==0>
								<li style="font-family:宋体;color:#3e3e3e;">该栏目下没有消息！！！</li>
									
								
								<#else>
									<#list data as content>
									<li class="info-item">
									<span class="info-icon"></span>
									<span id = "newsBody" style="width:98%;" >
										<#if (content.NEWS_BODY?length >1050)>
											${content.NEWS_BODY?substring(0,1050)}...
											<a href='#' data_id='${content.NEWS_ID}' class='readMore' onclick="newsView(this)">[全文阅读...]</a>
										<#else>
											${content.NEWS_BODY}
										</#if>
									
									</span>
									</li>
									</#list>
								</#if>
								</ul>
								<script language="javascript">
								var params = {"CHNL_ID":"${CHNL_ID}","CHNL_PID":"${CHNL_PID}"};
								var newsData = FireFly.doAct("SY_COMM_INFOS_VIEW","getBNChnlAll",params);
								
								var listBeans = newsData.listBeans;
								var childBeans = [];
								for(var i=0;i<listBeans.length;i++){
								
								var listBean = listBeans[i];
									if(listBean.CHNL_LEVEL==3){
									
										childBeans.push(listBean);
									}
									if(listBean.CHNL_ID != 'undefined'&&listBean.CHNL_LEVEL==2&&listBean.CHNL_ID == '${CHNL_ID}'){
										var dLi = jQuery("<li id='"+listBean.CHNL_ID+"' class='date-list-li-selected'></li>").appendTo(jQuery(".date-list"));
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
									}else if(listBean.CHNL_LEVEL==2){
									
										var dLi = jQuery("<li id='"+listBean.CHNL_ID+"'></li>").appendTo(jQuery(".date-list"));
										jQuery("<a data-id='"+listBean.CHNL_ID+"' href='javascript:void(0);' title="+listBean.CHNL_NAME+">"+listBean.CHNL_NAME+"</a><ul style='display:none;visibility:visible;position: static;z-index:0;' id='ul-"+listBean.CHNL_ID+"' class='date-list-li'></ul>").appendTo(dLi).unbind("click").bind("click",function(){
											var parentObj = jQuery(".date-list li");
											for(var n=0;n<parentObj.length;n++){
												if($(parentObj[n]).hasClass("date-list-li-selected")){
													$(parentObj[n]).removeClass("date-list-li-selected");
												}
											}
											$(this).parent().addClass("date-list-li-selected");
											jQuery("ul[id^='ul-']").css('display','none');
											jQuery("#ul-"+$(this).attr("data-id")).css('display','block');
											showNews($(this).attr("data-id"));
										});
										
									}
								}
								if(childBeans.length>0){
								
									for(var i=0;i<childBeans.length;i++){
										var listBean = childBeans[i];
										
										if(listBean.CHNL_LEVEL==3){
											var dLi = jQuery("<li id='"+listBean.CHNL_ID+"'></li>").appendTo(jQuery("#ul-"+listBean.CHNL_PID));
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
								}	
								
								</script>
								
							</div>      
						</div>
					</div>
					
				</td>
			</tr>
		</table>
	</td>
</table>
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
