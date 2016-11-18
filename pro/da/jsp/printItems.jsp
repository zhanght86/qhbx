<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.lang.*"%>
<%@ page import="com.rh.core.base.Context" %>
<%@ page import="com.rh.core.base.Bean" %>

<%@ page import="com.rh.ar.ArItemFile " %>
<%@ page import="com.rh.core.util.Constant" %> 
<%@ page import="com.rh.core.util.Constant" %> 
<%@ page import="com.rh.core.serv.ServMgr " %> 

<%@ page import="com.rh.da.BrmComm " %>

<%
String AR_YEAR= request.getParameter("AR_YEAR");
String FILE_CLASSCOD= request.getParameter("FILE_CLASSCOD");
String FILE_TIMELIMIT= request.getParameter("FILE_TIMELIMIT");
String FILE_BOX_CODE= request.getParameter("FILE_BOX_CODE");
String GW_FILE_TYPE= request.getParameter("GW_FILE_TYPE");
String FILE_FOLDER_CODE= request.getParameter("FILE_FOLDER_CODE");

Bean param = new Bean();
StringBuilder sql = new StringBuilder("");
sql.append("DA_NUM,DUTY,GW_YEAR_CODE,GW_YEAR,GW_YEAR_NUMBER,GW_YEAR_TEXT,TITLE,GW_DATE,COUNT,REMARK");

String where="and YEAR="  +AR_YEAR + "";
if(FILE_CLASSCOD!=null)
	where+=" and GD_DEPT="+"'"+FILE_CLASSCOD+"'";

if(FILE_TIMELIMIT!=null)
	where+=" and TERM="+"'"+FILE_TIMELIMIT+"'";

if(GW_FILE_TYPE!=null)
	where+=" and KIND_ID="+"'"+GW_FILE_TYPE+"'";

if(FILE_BOX_CODE!=null)
	where+=" and BOX="+FILE_BOX_CODE;

if(FILE_FOLDER_CODE!=null)
	where+=" and VOL_ID="+"'"+FILE_FOLDER_CODE+"'";

param.set(Constant.PARAM_SELECT, sql.toString());
param.set(Constant.PARAM_WHERE, where);
param = ServMgr.act("DA_DANGAN", "finds", param);
List<Bean> dataList = param.getList(Constant.RTN_DATA);
%>

<HTML>   
 <HEAD>   
 
<script  language= "javascript"> 
HKEY_Root= "HKEY_CURRENT_USER"; 
HKEY_Path= "\\Software\\Microsoft\\Internet Explorer\\PageSetup\\"; 
//设置网页打印的页眉页脚为空 
function   PageSetup_Null() 
{ 
  try 
  { 
    var   Wsh=new   ActiveXObject( "WScript.Shell"); 
    HKEY_Key= "header"; 
    Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key, ""); 
    HKEY_Key= "footer"; 
    Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key, ""); 
  } 
  catch(e){} 
} 
//设置网页打印的页眉页脚为默认值 
function     PageSetup_Default() 
{     
  try 
  { 
    var   Wsh=new   ActiveXObject( "WScript.Shell"); 
    HKEY_Key= "header"; 
    Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key, "&w&b页码,&p/&P"); 
    HKEY_Key= "footer"; 
    Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key, "&u&b&d"); 
  } 
  catch(e){} 
} 
</script> 
 
 
 
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
 
  <TITLE>打印文件目录</TITLE>   
  <style>   
  *{margin:0px; padding:0px;}  
  @media print{    
  .toolbar{display:none;} 
   .split{display:none;}     
  }    
  .toolbar{border:1px solid #6A9BFA;background:#E8F7E8;padding:5px 5px;}    
  .paging{page-break-after :always}    
  td{font-size:12px;color:#000000;}      
   
  </style>  
  
  <style>
  .cell1
  {padding:3px 3px;border-top: rgb(0,0,0) 1px groove; border-bottom: rgb(0,0,0) 1px groove; border-left: rgb(0,0,0) 1px groove; border-right: rgb(0,0,0) 1px groove}
  .cell2
  {padding:3px 3px;border-top: rgb(0,0,0) 1px groove; border-bottom: rgb(0,0,0) 1px groove; border-right: rgb(0,0,0) 1px groove}
  .cell3
  {padding:3px 3px;border-bottom: rgb(0,0,0) 1px groove; border-left: rgb(0,0,0) 1px groove; border-right: rgb(0,0,0) 1px groove}
  .cell4
  {padding:3px 3px;border-bottom: rgb(0,0,0) 1px groove; border-right: rgb(0,0,0) 1px groove}


  
   </style> 
 </HEAD>   
   
 <BODY >  

  
 <div class='toolbar'>   
 <OBJECT id=WebBrowser classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 height=0 width=0>   
 </OBJECT>   
 <input type=button value=打印 onclick=document.all.WebBrowser.ExecWB(6,1)>   
 <input type=button value=直接打印 onclick=document.all.WebBrowser.ExecWB(6,6)>   
 <input type=button value=页面设置 onclick=document.all.WebBrowser.ExecWB(8,1)>   
 <input type=button value=打印预览 onclick=document.all.WebBrowser.ExecWB(7,1)>
 &nbsp;&nbsp;&nbsp;&nbsp;    
 <input type= "button"   value= "清空页码 "   onclick=PageSetup_Null()> 
 <input type= "button"   value= "恢复页码 "   onclick=PageSetup_Default()> 
   &nbsp;&nbsp;&nbsp;&nbsp;    
 <input type= "button"   value= "关闭窗口 "   onclick="window.close()"> 
 </div> 
 
 <%! 

String checkStr( String s ){ 

if(s!=null&&s!="")
{
return s; 
}
else
{
	return "&nbsp;";
}
} 

%> 



<html> 

 
 
 <%
 
 
 int i=0;
 for (Bean data : dataList) { %>
 <% if(i%10==0){%>
 <div class='split'  style="height:20px"></div>
 
 <TABLE width="100%"  cellspacing="0" cellpadding="1" >
 <THEAD style="display:table-header-group;font-weight:bold;text-align:center">     
  <TR >   
      <TD class="cell1" width=40>件号</TD>   
      <TD class="cell2" width=120>责任者</TD>   
      <TD class="cell2" width=100>文号</TD>   
      <TD class="cell2">题名</TD>   
      <TD class="cell2" width=60>日期</TD>   
      <TD class="cell2" width=30>页数</TD>
      <TD class="cell2" width=120>备注</TD>
    </TR>   
</THEAD>  
<% }; %> 
   <TR>   
    <TD class="cell3"><%=checkStr(data.getStr("FILE_ITEM_CODE")) %></TD>   
    <TD class="cell4"><%=checkStr(data.getStr("FILE_DUTY")) %> </TD>   
    <TD class="cell4"><%=checkStr(data.getStr("GW_YEAR_TEXT")) %></TD>   
    <TD class="cell4"><%=checkStr(data.getStr("GW_TITLE")) %></TD>   
    <TD class="cell4"><%=checkStr(data.getStr("GW_CW_TIME")) %></TD>   
    <TD class="cell4"><%=checkStr(data.getStr("GW_PAGE")) %></TD>
    <TD class="cell4"><%=checkStr(data.getStr("FILE_MEMO")) %></TD>
   </TR>   
  
  
  <% 
  i++;
  if(i%10==0){%>
 </TABLE>    
 <div class="paging"></div>   
  <% };
     
    %>
  
  <% }; %>
  
  <% if(i>0){%>
 </TABLE> 
   <% }; %>
 
</BODY>   
</HTML>   
