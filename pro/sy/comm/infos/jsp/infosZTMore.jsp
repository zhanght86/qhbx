<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.File"%>
<%@ page import="com.rh.core.serv.*" %>
<%@ page import="com.rh.core.base.*" %>
<%@ page import="com.rh.core.util.freemarker.*" %>
<%@ page import="com.rh.core.comm.mind.*" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.rh.core.util.Constant" %>
<%
	request.setCharacterEncoding("UTF-8");
    final String CONTEXT_PATH = request.getContextPath();
    OutBean outData = (OutBean)request.getAttribute(Constant.RTN_DISP_DATA);
	// 手动获取一下这样在线程中就有了UserBean
	Context.getUserBean(request);
	OutBean outBean = new OutBean();
	if(outData.contains("_INFOSZT_PARAM_")){
	 outBean= (OutBean)outData.getBean("_INFOSZT_PARAM_");
	}
	List<Bean> data = outBean.getDataList();
	String chnlid = outBean.getStr("CHNL_PID").trim();//栏目id
	String chnl_name = outBean.getStr("CHNL_NAME").trim();//栏目名称
	int count = outBean.getPage().getInt("SHOWNUM");//每页显示数
	int total = outBean.getInt("TOTAL");//总记录数
	int nowpage = outBean.getPage().getInt("NOWPAGE");	
	int countPage =0;//总页数
	if((total%count)==0){
		countPage =total/count;
	} else {
		countPage =(total/count)+1;
	} 
%>
<style type="text/css">
.pageBody__default .portal-box-title {
color: #000000;
}
.uul{width:700px;list-style:none; border:red 1px solid; overflow:hidden;} 
.lli{width:340px;float:left; margin-right:10px;line-height:20px; display:inline;} 
.tdd{float:left;width:700px; text-align:left;} 
.elipd{
 overflow:hidden;
white-space:nowrap;
text-overflow:ellipsis;
-o-text-overflow:ellipsis;
-moz-text-overflow:ellipsis;
-webkit-text-overflow:ellipsis;
-icab-text-overflow: ellipsis;
-khtml-text-overflow: ellipsis;
color:#FF8008;
}

.list_content a:hover {
color: #FF8008;
font-weight: bold;
text-decoration: none;
}
a {
text-decoration: none;
}
</style>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>信息更多</title>
<script type="text/javascript" src="/sy/base/frame/jquery-1.8.2.min.js"></script> 

	
</head>
<body style="background-color:#ffffff;" >
<div style="color:#585858; width:80%;left:10%;right:10%; margin:auto;">
<table width="100%" border="0"  cellpadding="2" cellspacing="0">
 <tr>
    <td height="20" align="left" class="page_top_menu_grey"><a href="SY_COMM_TEMPL.show.do?pkCode=3ziU0Bi5la0G_Jcvrr73ac"><img src="/sy/comm/infos/img/btn_index.jpg" border="0" align="absmiddle"></a> 
      &gt;&gt;<span style="font-size:12px;"> <%=chnl_name%></span></td>
  </tr>
  </table>
  <table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#EBEBEB">
      <tbody><tr>
 <td width="115">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tbody><tr>
 <td height="2" bgcolor="#FB2608"></td>
  </tr>
</tbody></table>
</td>
  <td bgcolor="#EBEBEB"></td>
      </tr>
    </tbody></table>
  
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tbody><tr>
        <td height="5"></td>
      </tr>
    </tbody></table>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#F7F0E6">
      <tr>
        <td width="7" height="28" valign="top"><img src="/sy/comm/infos/img/content_list_top_left.jpg" width="7" height="28"></td>
		<td width="54" valign="top"><img src="/sy/comm/infos/img/content_list_top_img_01.jpg" width="54" height="28"></td>
		<td valign="top">&nbsp;</td>
		<td width="75" valign="top"><img src="/sy/comm/infos/img/content_list_top_img_02.jpg" width="26" height="28"></td>
		<td width="7" height="28" valign="top"><img src="/sy/comm/infos/img/content_list_top_right.jpg" width="7" height="28"></td>
      </tr>
    </table> 
<table width="100%" style="table-layout:fixed;border-color:gray">
<%
 if (data.size()==0){
%>
<tr><td align=center id='haha'>该栏目暂无信信息！</td></tr>
<%}%>

<% for(Bean bean : data) 
   {
%>
<tr style="width:100%;line-height:20px;" class="list_content">
<td class="elipd" style="width:75%;"><img src="/sy/comm/infos/img/d.png"/><a href="SY_COMM_INFOS.getMoreInfos.do?CHNL_ID=<%=bean.getStr("CHNL_ID") %>"  style="margin-left:3px;">
<span style="font-size:12px;"><%=bean.getStr("CHNL_NAME")%></span>
</a></td>
<td class="elipd" style="width:25%;"><span style="float:right;margin-right:30px;color:#999999;font-size:12px;"><%=bean.getStr("CHNL_TIME") %></span></td>
</tr>
<%}%>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td align="center" class='list_content'>           
 <script language="JavaScript">
var currentPage = <%=nowpage%>;//所在页从0开始
var prevPage = currentPage-1;//上一页
var nextPage = currentPage+1;//下一页
var countPage = <%=countPage%>;//共多少页
var total2 = <%=total%>;
var count2 = <%=count%>;
//设置上一页代码
if(countPage>1&&currentPage!=1)
	document.write("<a href='SY_COMM_INFOS.getMoreZT.do?CHNL_PID=<%=chnlid%>&curtentpage="+prevPage+"' onMouseOver=\"pageup.src='/sy/comm/infos/img/page_up_currected.jpg'\"  onMouseOut=\"pageup.src='/sy/comm/infos/img/page_up.jpg'\"><img src=/sy/comm/infos/img/page_up.jpg border=0 name=pageup>&nbsp;<span class=font_c_black>上一页</span></a>&nbsp;");
else
	document.write("<img src=/sy/comm/infos/img/page_up.jpg border=0>&nbsp;上一页&nbsp;");
//循环
var num = 5;
for(var i=0+(currentPage-(currentPage-1)%num) ; i<=(num+(currentPage-(currentPage-1)%num))&&(i<=countPage) ; i++){
	if(currentPage==i)
		document.write((i)+"&nbsp;|");
	else
		document.write("&nbsp;<a href='SY_COMM_INFOS.getMoreZT.do?CHNL_PID=<%=chnlid%>&curtentpage=" +i+ "'>" + (i) + "</a>&nbsp;|");
}
//设置下一页代码 
if(countPage>1&&currentPage!=(countPage))
	document.write("&nbsp;<a href='SY_COMM_INFOS.getMoreZT.do?CHNL_PID=<%=chnlid%>&curtentpage="+nextPage+"' onMouseOver=\"pagedown.src='/sy/comm/infos/img/page_down_currected.jpg'\"  onMouseOut=\"pagedown.src='/sy/comm/infos/img/page_down.jpg'\">&nbsp;<span class=font_c_black>下一页</span>&nbsp;<img src=/sy/comm/infos/img/page_down.jpg width=12 height=12 border=0 name=pagedown></a>&nbsp;");
else
	document.write("&nbsp;下一页&nbsp;<img src=/sy/comm/infos/img/page_down.jpg width=12 height=12 border=0>");
</script> 
</td>
      </tr>
            </table>
</div>
</body>
</html>