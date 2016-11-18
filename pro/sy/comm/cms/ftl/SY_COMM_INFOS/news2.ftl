<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${data.NEWS_SUBJECT!"没有标题"}</title>
<#include "/SY_COMM_INFOS/globalinfos.ftl">
<link rel="stylesheet" type="text/css" href="/sy/comm/news/css/news.css"/>
<link rel="stylesheet" type="text/css" href="/sy/comm/poll/poll.css" />
<script type="text/javascript" src="/sy/comm/poll/poll.js"></script>
<style type="text/css">
#d-top {bottom: 10px;float: right;position: fixed;right: 20px;z-index: 10;}
.print {
    float: right;
    position: relative;
    top: -13px;
    right: 20px;
}
.footcss{text-align:center;color:#666;margin-left: -200px;height:20px;line-height:20px;width:1050px;font-family: Microsoft YaHei;}
.news_title p {font-family: Microsoft YaHei; font-size:18px; font-weight:bold;}
.foot{ width:100%; background:#f7f7f7; padding:25px 0px; margin-top:20px;}
.foot dl{ width:1190px; margin:0px auto;}
.foot dl dt{ float:left; width:350px; height:42px;}
.foot dl dt img{ width:350px; height:42px; overflow:hidden;}
.foot dl dd{ float:right; width:550px;}
.foot dl dd ul{ width:550px; height:42px;}
.foot dl dd ul li{ float:right; width:110px; height:42px; text-align:right; line-height:42px; color:#666; font-size:14px; font-family:微软雅黑;}
.foot dl dd ul li a{ display:block; width:100px; height:42px; text-align:center; line-height:42px; color:#666; font-size:14px; font-family:微软雅黑;}
.foot dl dd ul li a:hover{ color:#d50310; text-decoration:underline;}
.user_news_info{
    position: absolute;
    float: right;
    right: 220px;
    top: 30px;
    font-size: 12px;
}
.user_news_info .user_user_info{
    margin-bottom: 8px;
    padding-bottom: 4px;
	text-align: left;
}
.user_news_info .user_date_info{margin-bottom: 8px; text-align:left;}
.user_news_info .user_date_info .logout{
	cursor: pointer;
	color: #e7340c;
}
.news_container .news_box .news_info a{font-size:12px; font-family:Microsoft Yahei; font-weight:normal;}
</style>
<script type ="text/javascript" >
Load.scriptJS("/sy/util/office/zotnClientLib_NTKO.js");
function doPrint()
{
	if(window.print)
		window.print();
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
</head>
<body style="margin-top:0px;">
<#include "/SY_COMM_INFOS/config_constant.ftl">
<@infos_attach debugName="新闻页面" newsId="${data.NEWS_ID}" servid ="${data.servId}" auditid ="${data.AUDIT_ID}" picCount="-1" attachCount="-1" pollCount="1">
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" newsid="${data.NEWS_ID}">
  <tr>
    <td valign="top" bgcolor="#FFFFFF">
    	<table width="1086px" border="0" align="center" cellpadding="0" cellspacing="0" class="mt5">
			<tr>
    			<td align="center">
    				<div width="1081px"><img style ="width:1088px;height:107px;" src="/sy/comm/cms/img/logo.jpg"/></div>
					<div class="user_news_info">
					 <div class="user_user_info" id="news_user_info">寿险总公司&nbsp;系统管理员</div>
					 <div class="user_date_info">
					 <span class="user_date_span"></span>&nbsp;&nbsp;<span class="logout" id="loginOut">[退出]</span>
					 <script type="text/javascript">
					 var mydate = rhDate.pattern("yyyy-MM-dd");
					 var week = {
					 "0" : "星期日",
					 "1" : "星期一",
					 "2" : "星期二",
					 "3" : "星期三",
					 "4" : "星期四",
					 "5" : "星期五",
					 "6" : "星期六"
					 };
					 var nowDate = new Date();
					 jQuery(".user_date_span").text(mydate+' '+week[nowDate.getDay()]);
					 </script>
					 </div>
					 </div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="news_container">
						<div class="news_box" style="border:1px #ddd solid;">
							<div class="news_title"><p>${data.NEWS_SUBJECT!"没有标题"}</p></div>
							<div class="news_info">
								<#if (data.NEWS_TIME)??>
									发布日期：${data.NEWS_TIME}&nbsp;&nbsp;
								</#if>		
								<#if (data.NEWS_SOURCE)??>
									来源：${data.NEWS_SOURCE}
								</#if>
							
									点击量 ： <span id="count"></span>		
								<a style="position: absolute; right: 10px;" href="#"><span>首页 > </span><span id="link_span">百年讲坛</span></a>
							</div>
						
							
							
							
							<!-- 图片集 begin -->
							<#if tag.imgList?size gt 0>
							<div class="news_imgs">
								<#list tag.imgList as img>
								<div class="img_target" id="img_${img_index+1}">
									<div class="title" id="img_title">图片集(${img_index+1}/${tag.imgList?size})</div>
									<div align="center">
										<img border="0" src="/file/${img.FILE_ID}?size=${biger!''}" >
									</div>
								</div>
								</#list>
								<div align="center" style="margin-top:10px;">
									<input type="button" id="pre" value="上一张"/>
									<input type="button" id="next" value="下一张"/>
								</div>
							</div>
							</#if>
							<!-- 图片集 end -->
							<div class="news_content" style="font-family:仿宋;font-size:18px;line-height:150%;">${data.NEWS_BODY!""}</div>
							<!-- 附件 begin  -->
							<#if tag.attachList?size gt 0>
							<div class="news_attach">
								<table width="100%" cellpadding="6" cellspacing="0">
									<tr>
										<td colspan="4" style="font-weight:bold;"><!--附件--></td>
									</tr>
								      <#list tag.attachList as attach>
										<tbody>
									        <tr>
									            <td>
									              	  <!--附件${attach_index+1}:-->
									            </td>
									            <td>
									                <a href="javascript:RHFile.read('${attach.FILE_ID}','${attach.FILE_NAME}')">${attach.FILE_NAME}</a>
									            </td>
												<td>${attach.S_UNAME!attach.S_USER!""}</td>
												
												<td><#if (attach.S_MTIME?length >10)>${attach.S_MTIME?substring(0,10)}<#else>${attach.S_MTIME}</#if></td>
									        </tr>
											<tr><td height="1" colspan="4" style="border-bottom:1px #cccccc dashed;"></td></tr>
										<tbody>
								      </#list>
								</table>
							</div>
							</#if>
							<!-- 投票 begin-->
							<div class="news_vote" id="vote_item">
							<a  class ="print" href="javascript:doPrint()"><img style="border:0px;" src="/sy/comm/cms/img/print.gif"/></a>
							</div>
							<!-- 投票 end-->
						</div>
						<!-- 评论 begin-->
					    
				
						
						<!-- 评论 begin-->
					</div>
				</td>
			</tr>
			
		</table>
	</td>
    	
</tr>

</table>


<script type="text/javascript">	

jQuery(document).ready(function(){
	var result = FireFly.doAct("SY_COMM_INFOS","getUserBean");
	var tdept_code = result.TDEPT_CODE;
	var tdept = FireFly.doAct("SY_ORG_DEPT","byid",{"_PK_":tdept_code});
	
	var chnlBean = FireFly.doAct("SY_COMM_INFOS","getChnlBean",{"CHNL_ID":"${data.CHNL_ID}"});
	jQuery("#news_user_info").html(tdept.DEPT_NAME+ "&nbsp;"+result.USER_NAME);
	jQuery("#link_span").text(chnlBean.CHNL_NAME);
	jQuery("#loginOut").on("click", function(event) {//退出
	       var resultData = FireFly.logout();
	       if (resultData[UIConst.RTN_MSG].indexOf(UIConst.RTN_OK) == 0) {
	    	  
	    		   Tools.redirect(FireFly.getContextPath() + "/logout.jsp");  
	    	  
	       }
	       event.stopPropagation();
	       return false;
	});
	/* 投票_begin */
	(function vote(){
		if(!"${tag.pollId!''}"){
			return ;
		}
	    var opts = {
	        "sId": "SY_PLUG_SEARCH",
	        "pollId": "${tag.pollId!''}",
	        "pCon": jQuery("#vote_item")
	    };
	    listView = new rh.vi.poll(opts);
	    listView.show();
	})();
	/* 投票_end */

	/* 图片轮转_begin */
	var imgIndex = 1;
	jQuery(".img_target").hide();
	jQuery(".img_target").eq(imgIndex-1).show();
	jQuery("#pre").click(function(){
		jQuery(".img_target").hide();
		if(imgIndex-1>0){
			imgIndex--;
		}
		jQuery(".img_target").eq(imgIndex-1).show();
	});
	jQuery("#next").click(function(){
		jQuery(".img_target").hide();
		if(imgIndex<"${tag.imgList?size}"){
			imgIndex++;
		}
		jQuery(".img_target").eq(imgIndex-1).show();
	});
	/* 图片轮转_end */
	

 /* 点击量 */
 var newid = jQuery("table").attr("newsid");
 function count(newid){
		var data = {};
		data["_PK_"] = newid;
		var out = FireFly.doAct("SY_COMM_INFOS","increaseReadCounter",data);
		jQuery("#count").text(out["COUNTER"]);	
	}
  count(newid);
	/* 点击量 */
});
</script>
<script type="text/javascript">	
	window.onload=function(){
		var newsContent = jQuery(".news_content");
		var img = jQuery(newsContent).find("img");
		jQuery(img).css("max-width","980px");
	}

</script>

<!-- 百年foot开始 -->
<div class="foot">
    	<dl class="clearfix">
        	<dt><img alt="" src="/sy/comm/page/images/foot.jpg" /></dt>
			<div class="footcss">Copyright Aeon Life Insurance Company,Ltd.All Rights Reserved.</div>
			<div class="footcss">版权所有 2015 百年人寿保险股份有限公司</div>
			   
        </dl>
    </div>
	<!-- 百年foot结束 -->
<div id="d-top">

	<a title="回到顶部" onclick="document.body.scrollTop=0;document.documentElement.scrollTop=0;parent.document.body.scrollTop=0;parent.document.documentElement.scrollTop=0;this.blur();return false;" 
		href="javascritp:;">
		<img alt="TOP" src="/sy/comm/news/img/top.png" style="border:0px !important;">
	</a>
	


</div>
</@infos_attach>
</body>
</html>
