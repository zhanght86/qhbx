<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.lang.*"%>
<%@ page import="com.rh.core.base.Context" %>
<%@ page import="com.rh.core.base.Bean" %>

<%@ page import="com.rh.ar.ArItemFile " %>
<%@ page import="com.rh.core.util.Constant" %> 
<%@ page import="com.rh.core.util.Constant" %> 
<%@ page import="com.rh.core.serv.ServMgr " %> 
<%
String AR_YEAR_ID = request.getParameter("AR_YEAR_ID");
String FILE_BOX_CODE= request.getParameter("FILE_BOX_CODE");
String FILE_FOLDER_CODE= request.getParameter("FILE_FOLDER_CODE");

String where ="and AR_YEAR_ID='" +AR_YEAR_ID+"'";

if(FILE_BOX_CODE==null||FILE_BOX_CODE.length()<1)
	where+=" and FILE_FOLDER_CODE="+"'"+FILE_FOLDER_CODE+"'";
else
	where+=" and FILE_BOX_CODE="+FILE_BOX_CODE;


String user_add= request.getParameter("user_add");
if(user_add==null)
	user_add="";

String user_check= request.getParameter("user_check");
if(user_check==null)
	user_check="";

String print_date= request.getParameter("print_date");

String toPrintDate="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日";
if(print_date!=null&&print_date.length()>=10)
toPrintDate=print_date.substring(0,4)+"年"+print_date.substring(5,7)+"月"+print_date.substring(8,10);


int cnt=0;
int pageCnt=0;


Bean param = new Bean();
StringBuilder sql = new StringBuilder("");
sql.append("count(*) CNT,sum(GW_PAGE) GW_PAGE");
param.set(Constant.PARAM_SELECT, sql.toString());
param.set(Constant.PARAM_WHERE,where);
param = ServMgr.act("AR_ITEM_FILE", "finds", param);
List<Bean> dataList = param.getList(Constant.RTN_DATA);

if(dataList.size() > 0)
{
	cnt =  dataList.get(0).getInt("CNT");
	pageCnt  =  dataList.get(0).getInt("GW_PAGE");
}

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
 


<html> 

  <div class='split'  style="height:20px"></div>
  
  <TABLE width="100%"  cellspacing="0" cellpadding="1" >
  <TR><TD align="left"><div style="border-bottom:1px solid black;padding=5px;width=200px">盒内文件情况说明</div></TD></TR>
  <TR><TD height=600 valign="top">
  <div style="padding=8px">共有<%=cnt %>件<%=pageCnt %>页</div>
  </TD></TR>
  <TR><TD align=right><div style="border-bottom:1px solid black;padding=5px;width=200px;text-align=left">整理人:<%= user_add %></div></TD></TR>
  <TR><TD align=right><div style="border-bottom:1px solid black;padding=5px;width=200px;text-align=left">检查人:<%= user_check %></div></TD></TR>
  <TR><TD align=right><div style="border-bottom:1px solid black;padding=5px;width=200px;text-align=center"><%=toPrintDate %></div></TD></TR>
  </TABLE> 
 
 
 
</BODY>   
</HTML>   
