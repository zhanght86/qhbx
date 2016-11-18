<!--#include file="include/conn.asp"-->
<!--#include file="include/config.asp"-->

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="include/css.css" type="text/css">
<title><%=oyaya_title%>-<%=W_Powered%></title>
<meta name="keywords" content="<%=oyaya_keywords%>">
<meta name="description" content="<%=oyaya_description%>">
<meta content="<%=author%>" name="author">
<link rel="Bookmark" href="favicon.ico" >
<link rel="Shortcut Icon" href="favicon.ico">
<meta content=all name="robots">
<meta content=all name="googleot">
<script language="JavaScript">
<!--
function win_gonggao(theurl)
{
  window.open(theurl,'','toolbar=no,  status=no,menubar=no, scrollbars=yes,resizable=no,width=520,height=400,left=200,top=100,scrollbars=yes');
 //OW("E","650","580","","","no","no","no","no","no",theurl,"modal");
}
// -->//-->
</script>
</head>
<body topmargin="0">
<!--#include file="include/top.asp"-->
<table width="900" height="29" border="0" align="center" cellpadding="0" cellspacing="0" background="images/gaobei_top2.gif">
     <tbody>
     <tr>
      <td width="93"></td>
       <td valign="center" width="210"><div style="padding-bottom:6px; color:#ffffff">
<script src="include/date.js" type=text/javascript></script> <span id="time"></span>	<script>setInterval("time.innerHTML=new Date().getHours()+':'+new Date().getMinutes()+':'+new Date().getSeconds()",1000);</script>
       </div></td>
     <td width="20"></td>
 <td width="577"><div style="padding-bottom:3px;"><font color="#008af7">您现在所在的位置：</font><a href="default.asp">首页</a></div></td>
       </tr>
  </tbody>
</table>
<%if gonggao=true then%>
<table width="900" border="0" align="center" cellpadding="3" class="turl">
  <tr>
    <td width="66">站内公告：</td>
    <td width="816"><MARQUEE scrollAmount=2 width=800 height=16>
<%
set rsg = server.CreateObject("adodb.recordset")
rsg.open "[gonggao] where d_data>#"&date&"# order by d_id desc",conn,1,1
if rsg.bof and rsg.eof then 
response.Write"暂无公告"&date&""
rsg.close
else
for i=1 to 5
if rsg.EOF or rsg.BOF then exit for%>
<a href="javascript:win_gonggao('gonggao.asp?id=<%=rsg("d_id")%>')"><%=rsg("title")%></a> <%=rsg("d_date")%> &nbsp;&nbsp;
<%rsg.movenext
next
rsg.close
end if
%></MARQUEE>
	</td>
  </tr>
</table>
<%end if%>
<table width="900" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr> 
    <td width="680" align="center" valign="top">
<div style="width:680px;">
  <div style="width:100%"><img src="images/spacer.gif" width="1" height="5"></div>
  <table width="680" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="280" align="center" bgcolor="#ffffff"><%call imgnews()%></td>
    <td width="390" align="center" valign="top" bgcolor="#ffffff"><%call tuijiannews()%></td>
  </tr>
</table>
<div style="width:100%"><img src="images/spacer.gif" width="1" height="8"></div>
  <table width="680" border="0" cellspacing="0" cellpadding="0">
<% sqlt="select * from bigclass where hide=true order by BigClassID"
set rst=server.createobject("ADODB.Recordset")
rst.open sqlt,conn,1,1
do while not rst.eof
%>
  <tr>
  <% for n=1 to 2 %>
    <td width="330" align="center" valign="top">
<table width=298 border=0 cellpadding=0 cellspacing=0 class="hemo_new">
      <tbody>
        <tr>
          <td width=49 height="33" align="center"><img src="images/books.ico" width="16" height="16"></td>
          <td width=210>&nbsp;&nbsp;&nbsp;<a href="otype.asp?classid=<%=rst("BigClassid")%>"><strong><%=rst("BigClassName")%></strong></a></td>
          <td width=30>　</td>
        </tr>
        <tr>
          <td height="23" colspan="3" align="center">
		  <div style="width:100%"><img src="images/spacer.gif" width="1" height="3"></div>
<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
<%
sql="select top 8 * from NEWS where BigClassName='"& rst("BigClassname") &"' and ifshow=1 order by id desc"
set rs=conn.execute(sql) 
do while not rs.eof 
%>
  <tr>
    <td height="24" align="left" class="i_news"> <span>[<%=rs("intime")%>]</span><img src="images/news_ico.gif" width="15" height="11">
      <% if rs("imagenum")<>"0" then response.write "<img src='images/news.gif' border=0 alt='图片新闻'>" end if %><a href="onews.asp?id=<%=rs("id")%>" title="<%=rs("title")%>" target="_blank" class="<%=rs("oColor")%>"><font class="<%=rs("oStyle")%>"><%=left(rs("title"),16)%></font><% if len(rs("title"))>13 then %>…<% end if %></a></td>
  </tr>
<%
rs.movenext 
loop
%>
</table>
<%
rs.close   
set rs=nothing   
%>
		  <div style="width:100%"><img src="images/spacer.gif" width="1" height="5"></div></td>
          </tr>
      </tbody>
    </table>
	<div style="width:100%"><img src="images/spacer.gif" width="1" height="5"></div>
	</td>
	<%
		rst.movenext
		if rst.eof then exit for
		if rst.eof then exit do
		Next
		%>
    </tr>
	<%
loop
rst.close
set rst=nothing%>
</table>
  
  <div style="width:100%"><img src="images/spacer.gif" width="1" height="5"></div>
  </div>
  </td>
  
<td width="220" height="100%" align="center" valign="top">
 
 
<table width="220" border="0" cellpadding="0" cellspacing="5">
    <tr>
      <td height="30" align="right" background="images/tit_bg.gif"><strong>公 司 公 告</strong>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <a href="gonggaolist.asp">more...</a>
      </td>
    </tr>
    <tr>
      <td colspan="2" align="center"><table width="100%" border="0" cellpadding="0" cellspacing="0" class="tab_k">
          <%
t=0
set rs=server.createobject("adodb.recordset") 
sql="select d_id,title from gonggao  order by d_id desc" 
rs.open sql,conn,1,1
if not rs.eof then
do while not rs.eof
t=t+1
%>
          <tr onMouseOver="this.bgcolor='#ffffff';" onMouseOut="this.bgcolor='#F4F4F4';" bgcolor=#F4F4F4>
            <td width="10%" height="23" align="center" style="background:url(images/no_list_bg_1.gif) 1px 28px repeat-y;"><%= t %></td>
            <td width="90%" align="left">&nbsp;<a href="javascript:win_gonggao('gonggao.asp?id=<%=rs("d_id")%>')"><%=left(rs("title"),13)%>
                    <% if Len(rs("title"))>12 then %>
              …
              <% end if %>
            </a></td>
          </tr>
          <% 
if t>=9 then exit do 
rs.movenext 
loop 
else 
response.write "<tr><td align=center colspan=2 bgcolor=#e8e8f4>尚无公告</td></tr>" 
end if 
rs.close 
%>
      </table></td>
    </tr>
  </table>

 

<table width="220" border="0" cellpadding="0" cellspacing="5">
    <tr>
      <td height="30" align="center" background="images/tit_bg.gif" >
        <a href="other/bnymx/star.html" target=_blank><image src="/images/star.gif"  width=208 > </a>
	  </td>
    </tr>
</table>



<table width="220" border="0" cellpadding="0" cellspacing="5">
    <tr>
      <td height="30" align="center" background="images/tit_bg.gif"><strong>推 荐 文 件</strong></td>
    </tr>
    <tr>
      <td colspan="2" align="center"><table width="100%" border="0" cellpadding="0" cellspacing="0" class="tab_k">
          <%
t=0
set rs=server.createobject("adodb.recordset") 
sql="select id,title,oColor,oStyle,top from news where tuijian=1 and ifshow=1 order by id desc" 
rs.open sql,conn,1,1
if not rs.eof then
do while not rs.eof
t=t+1
%>
          <tr onMouseOver="this.bgcolor='#ffffff';" onMouseOut="this.bgcolor='#F4F4F4';" bgcolor=#F4F4F4>
            <td width="10%" height="23" align="center" style="background:url(images/no_list_bg_1.gif) 1px 28px repeat-y;"><%= t %></td>
            <td width="90%" align="left">&nbsp;<a class="<%=rs("oColor")%>" href="onews.asp?id=<%=rs("id")%>" title="<%=rs("title")%>" target="_blank"><span class="<%=rs("oStyle")%>"><%=left(rs("title"),13)%>
                    <% if Len(rs("title"))>12 then %>
              …
              <% end if %>
            </span></a></td>
          </tr>
          <% 
if t>=9 then exit do 
rs.movenext 
loop 
else 
response.write "<tr><td align=center colspan=2 bgcolor=#e8e8f4>尚无文件</td></tr>" 
end if 
rs.close 
%>
      </table></td>
    </tr>
  </table>





  <table width="220" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td height="30" align="center" background="images/tit_bg.gif"><strong>热 门 文 件</strong></td>
    </tr>
    <tr>
      <td height="22" align="center">
      <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tab_k">
          <%
t=0
set rs=server.createobject("adodb.recordset") 
sql="select id,title,oColor,oStyle,hits from news where ifshow=1 order by hits desc" 
rs.open sql,conn,1,1
if not rs.eof then
do while not rs.eof
t=t+1
%>
          <tr onMouseOver="this.bgcolor='#ffffff';" onMouseOut="this.bgcolor='#F4F4F4';" bgcolor=#F4F4F4>
            <td width="85%" align="left">&nbsp; <a class="<%=rs("oColor")%>" href="onews.asp?id=<%=rs("id")%>" title="<%=rs("title")%>" target="_blank"><span class="<%=rs("oStyle")%>"><%=left(rs("title"),12)%>
                    <% if Len(rs("title"))>15 then %>
              …
              <% end if %>
            </span></a></td>
            <td width="15%" height="23"><font color="#ff0000"><%=rs("hits")%></font></td>
          </tr>
          <% 
if t>=10 then exit do 
rs.movenext 
loop 
else 
response.write "<tr><td align=center colspan=2>尚无文件</td></tr>" 
end if 
rs.close 
%>
      </table>
      </td>
    </tr>
</table>



    <table width="220" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="82%" height="30" align="center" background="images/tit_bg.gif"><strong>最 新 下 载</strong></td>
      </tr>
      <tr>
        <td height="100%" align="center">
          <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F4" class="tab_k">
            <tr onMouseOver="this.bgcolor='#ffffff';"onmouseout="this.bgcolor='#efefef';">
              <td width="82%" height="213" align="center" valign="top"><%call Download()%></td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
    



 <table width="220" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td height="30" align="center" background="images/tit_bg.gif"><strong>文 件 搜 索</strong></td>
  </tr>
  <tr>
    <td align="center">
     <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tab_k">
      <form action="search.asp" method="get" name="form1" id="form1">
        <tr>
          <td height="35" align="center" bgcolor="#F4F4F4"><input name="key" type="text" class="input" id="key" size="19" /></td>
        </tr>
        <tr>
          <td height="35" align="center" bgcolor="#F4F4F4">
          <select name="otype" class="input" id="otype" style="line-height:30px;">
            <option value="title" selected="selected" class="input">文件标题</option>
            <option value="msg" class="input">文件内容</option>
          </select>
           <input type="submit" name="submit2" value="搜索" class="input" /></td>
        </tr>
      </form>
     </table>
    </td>
  </tr>
</table>

    
    
    
   </td>
  </tr>
</table>





<%if oyaya_link=true then%>
 <table width="900" border="0" align="center" cellpadding="0" cellspacing="0">
   <tr>
     <td width="82%" height="33" background="images/tit_bg.gif"><span class="links_">&nbsp;<strong>友情链接&nbsp;&amp; 合作伙伴</strong></span></td>
   </tr>
   <tr>
     <td height="22" align="center"><table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#F4F4F4" class="tab_k">
         <%strsql = "select * from sogo_link where sogo_link_yes_no=1 order by link_paixu asc"
set rs = server.createobject("adodb.recordset")
rs.open strsql,conn,1,2
if rs.eof then
else
%>
         <tr onMouseOver="this.bgcolor='#ffffff';"onmouseout="this.bgcolor='#efefef';">
           <td width="82%" height="27" align="left">
<%
for link=0 to rs.recordcount
if rs.eof or link=rs.recordcount then exit for
%>		   
		   <%if rs("sogo_link_logo_or_txt")=1 then%>
             [<a href="<%=rs("sogo_link_url")%>" target="_blank"><%=rs("sogo_link_font")%></a>]
             <%else%>
             <a href="<%=rs("sogo_link_url")%>" target="_blank"><img src="<%=rs("sogo_link_login")%>" alt="<%=rs("sogo_link_alt")%>" width="88" height="31" hspace="2" vspace="2" border="0" /></a>
             <%end if%>
<% 
rs.movenext
next
end if
rs.close
%>           </td>
         </tr>
     </table></td>
   </tr>
 </table><%end if%>
 </center>
 <div style="width:100%"><img src="images/spacer.gif" width="1" height="5"></div>
<!--#include file="include/bottom.asp"--></body>
</html>