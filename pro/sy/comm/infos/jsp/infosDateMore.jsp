<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.File"%>
<%@ page import="com.rh.core.serv.*" %>
<%@ page import="com.rh.core.base.*" %>
<%@ page import="com.rh.core.util.freemarker.*" %>
<%@ page import="com.rh.core.comm.mind.*" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.rh.core.util.Constant" %>
<%@ page import="com.rh.core.util.DateUtils" %>
<%
	request.setCharacterEncoding("UTF-8");
    final String CONTEXT_PATH = request.getContextPath();
    OutBean outData = (OutBean)request.getAttribute(Constant.RTN_DISP_DATA);
    //String seachYear = request.getParameter("CUR_DATE");
    String seachYear = outData.getStr("CUR_DATE");	
    if(seachYear==null){
    	seachYear = Integer.valueOf(DateUtils.getYear()).toString();
    }
	// 手动获取一下这样在线程中就有了UserBean
	Context.getUserBean(request);
	List<Bean> data = outData.getDataList();
	String chnlid = outData.getStr("CHNL_ID").trim();
	String chnl_name = outData.getStr("CHNL_NAME").trim();
	int total = outData.getInt("_OKCOUNT_");//总记录数
	int nowpage = outData.getPage().getInt("NOWPAGE");	
	int countPage =outData.getPage().getInt("PAGES"); 
	//存放时间
	Set setDate = (Set)outData.get("SET_DATE");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" id = "<%=chnlid%>-datemore">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>信息更多</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/sc/css/infoMore_reset.css"/>
<style type="text/css">

 
</style>

<script type="text/javascript" src="/sy/base/frame/jquery-1.8.2.min.js"></script> 
<script type="text/javascript">
	function newsView(id){
		var url = "/cms/SY_COMM_INFOS/" + id + ".html";			
		window.open(url);
	}
</script>	
	
</head>
<body style="background-color:#fff !important;">
<div class="info-wrapper">
  	<div class="info-header">
  		<span><%=chnl_name%></span>
  		<div class="info-header-icon"></div>
  	</div>
	<div class="info-title">
		<div style="width:15%;">&nbsp;</div>
		<div style="width:10px;">&nbsp;</div>
		<div style="width:61%;text-align:center;">标题</div>
		<div style="width:15%;">发布人</div>
		<div>发布时间</div>
	</div>
	 <div class = "news-content">  
		 <div class ="news-list-left" style="width:15%;" >
		 	<ul class="date-list"></ul>
		 </div>  
		 <div class = "news-list-right" style="width:85%;" >
			 <ul>
			<%
			 if (data.size()==0){
			%>
			<li style="font-family:微软雅黑;color:#3e3e3e;"> 该栏目暂无信息！</li>
			<%}%>
			
			<% for(Bean bean : data) 
			   {	
			%>
			<li class="info-item">
				<span class="info-icon"></span>
				<span class="info-subject info-elipd" style="width:71%;" title="<%=bean.getStr("NEWS_SUBJECT") %>" onclick="newsView('<%=bean.getStr("NEWS_ID") %>')"><%=bean.getStr("NEWS_SUBJECT") %></span>
				<span class="info-user" style="width:18%;overflow:hidden;"><%=bean.getStr("NEWS_USER__NAME")%></span>
				<span class="info-time"><%=bean.getStr("NEWS_TIME").substring(0, 10) %></span>
			</li>
			<%}%>
			</ul>
			<script language="javascript">
			var dataSize = <%= data.size()%>;
			var setDate = <%=setDate%>;
			var setSize = <%= setDate.size()%>;
			var chnlId = '<%=chnlid%>';
			if (dataSize == 0){
				jQuery(".date-list").css({"display":"none"});
			}
			var year = new Date().getFullYear();
			if(setSize != 0){
				for(var i = setDate.length-1;i>=0; i--){
					if(setDate[i]==<%=seachYear%>){
						 jQuery(".date-list")
						 .append("<li class='date-list-li-selected'><a href='SY_COMM_INFOS.getDateMoreInfos.do?CHNL_ID=<%=chnlid%>&CHNL_NAME=<%=chnl_name%>&CUR_DATE="+setDate[i]+"'>"+setDate[i]+"</a></li>");
					}else{
						 jQuery(".date-list")
						 .append("<li><a href='SY_COMM_INFOS.getDateMoreInfos.do?CHNL_ID=<%=chnlid%>&CHNL_NAME=<%=chnl_name%>&CUR_DATE="+setDate[i]+"'>"+setDate[i]+"</a></li>");
					}
					if(chnlId=="3UUkAaxv55IrilI7T5AO5t"){
						<!--  公司新闻 -->
						jQuery(".date-list")
						 .append("<li><a type_='company-news' href='javascript:void(0);' onclick='openBNNews(this);'>2015</a></li>");
					}else if(chnlId=="2jvJBpe6F8w8T4hO8R1E4u"){
						<!--  何董讲话 -->
						jQuery(".date-list")
						 .append("<li><a type_='hedongjianghua' href='javascript:void(0);' onclick='openBNNews(this);'>2015</a></li>");
					}else if(chnlId=="0MOH0eHv9dub5IAnUlXYtk"){
						<!--  人事文件 -->
						jQuery(".date-list")
						 .append("<li><a type_='company-system' groupid='13851' href='javascript:void(0);' onclick='openBNNews(this);'>2015</a></li>");
					}else if(chnlId=="0lOEaY6LVd19bzs8svqySu"){
						<!--  机构拓展 -->
						jQuery(".date-list")
						 .append("<li><a type_='company-system' groupid='13839' href='javascript:void(0);' onclick='openBNNews(this);'>2015</a></li>");
					}else if(chnlId=="3kWkWhLqgkWl7S8UaqE6uWa1I"){
						<!--  产品园地 -->
						jQuery(".date-list")
						 .append("<li><a type_='company-system' groupid='13809' href='javascript:void(0);' onclick='openBNNews(this);'>2015</a></li>");
					}else if(chnlId=="0xLPxAkIChdwaNHd0XHPf6j"){
						<!--  党建园地 -->
						jQuery(".date-list")
						 .append("<li><a type_='zonghe' href='javascript:void(0);' onclick='openBNNews(this);'>2015</a></li>");
					}
						
					
					 
				}
			}
			function openBNNews(element){
			var type = $(element).attr("type_");
			var chnlName = encodeURIComponent($(element).text());
				if(type=="company-system"){
					var groupid = $(element).attr("groupid");
					window.open("/sy/base/view/stdListView.jsp?sId=BN_DT_JOURNALARTICLE&TYPE_="+type+"&groupid="+groupid+"&title="+chnlName,"_blank");
				}else{
					window.open("/sy/base/view/stdListView.jsp?sId=BN_DT_JOURNALARTICLE&TYPE_="+type+"&title="+chnlName,"_blank");
				}
				
			}
			</script>
			<div class="news_list_page">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			    <tr> 
			    	<td align="center" class='page-list-content'>           
					<script language="JavaScript">
					var currentPage = <%=nowpage%>;//所在页从0开始
					var prevPage = currentPage-1;//上一页
					var nextPage = currentPage+1;//下一页
					var countPage = <%=countPage%>;//共多少页
					//设置上一页代码
					if(countPage>1&&currentPage!=1)
						document.write("<a href='SY_COMM_INFOS.getDateMoreInfos.do?CHNL_ID=<%=chnlid%>&CHNL_NAME=<%=chnl_name%>&CUR_DATE=<%=seachYear%>&curtentpage="+prevPage+"' class='page-updown'>"
								+"<span>上一页</span></a>");
					else
						document.write("<div class='page-updown disabled'><span>上一页</span></div>");

					//循环
					var num = 5;
					for(var i=0+(currentPage-(currentPage-1)%num) ; i<=(num+(currentPage-(currentPage-1)%num))&&(i<=countPage) ; i++){
						if(currentPage==i)
							document.write("<div class='list-page-bg selected'><span>"+i+"</span></div>");
						else
							document.write("<a href='SY_COMM_INFOS.getDateMoreInfos.do?CHNL_ID=<%=chnlid%>&CHNL_NAME=<%=chnl_name%>&CUR_DATE=<%=seachYear%>&curtentpage=" +i+ "' class='list-page-bg'>"
									+"<span>"+i+"</span></a>");
					}
					//设置下一页代码 
					if(countPage>1&&currentPage!=(countPage))
						document.write("<a href='SY_COMM_INFOS.getDateMoreInfos.do?CHNL_ID=<%=chnlid%>&CHNL_NAME=<%=chnl_name%>&CUR_DATE=<%=seachYear%>&curtentpage="+nextPage+"' class='page-updown'><span>下一页</span></a>");
					else{
						document.write("<div class='page-updown disabled'><span>下一页</span></div>");
					}
					</script> 
					</td>
			     </tr>
			</table>
			</div>
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