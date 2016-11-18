<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.*"%>
<%@ page import="java.lang.*"%>
<%@ page import="com.rh.core.base.Context" %>
<%@ page import="com.rh.core.base.Bean" %>
<%@ page import="com.rh.core.util.Constant" %> 

<%@ page import="com.rh.core.serv.ServMgr " %> 
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.rh.core.org.UserBean"%>
<%@ page import="com.rh.da.BrmComm " %>
<%
    String pageTitle = "档案打印";
    UserBean user = Context.getUserBean();
    String strCmpy = user.getODeptName();
    String strCmpyCode = user.getCmpyCode();
%>

<%
    
	String boxScript="";
	if(request.getAttribute("boxScript")!=null){
		boxScript=(String)request.getAttribute("boxScript");
	}
	
	String name1="";
	if(request.getAttribute("name1")!=null){
		name1=(String)request.getAttribute("name1");
	}
	
	String name2="";
	if(request.getAttribute("name2")!=null){
		name2=(String)request.getAttribute("name2");
	}
	
	String printYear="";
	if(request.getAttribute("printYear")!=null){
		printYear=(String)request.getAttribute("printYear");
	}
	
	String printMonth="";
	if(request.getAttribute("printMonth")!=null){
		printMonth=(String)request.getAttribute("printMonth");
	}
	String printDay="";
	if(request.getAttribute("printDay")!=null){
		printDay=(String)request.getAttribute("printDay");
	}
	

   
%>

<object ID="WebBrowser1" WIDTH="0" HEIGHT="0" CLASSID="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" VIEWASTEXT>
</object>
<%
String currentDate=BrmComm.getCurrentDateByString().substring(0, 10);
if(printYear.length()==0){
	printYear=currentDate.substring(0,4);
}
if(printMonth.length()==0){
	printMonth=currentDate.substring(5,7);
}
if(printDay.length()==0){
	printDay=currentDate.substring(8,10);
}
if(name1.length()==0){
	name1=user.getName();
}
if(name2.length()==0){
	name2=user.getName();
}

String AR_YEAR= request.getParameter("AR_YEAR");
String FILE_BOX_CODE= request.getParameter("FILE_BOX_CODE");
String FILE_CLASSCOD= request.getParameter("FILE_CLASSCOD");
String FILE_TIMELIMIT= request.getParameter("FILE_TIMELIMIT");



String term="";
if(FILE_TIMELIMIT!=null)
{	
	term=FILE_TIMELIMIT;
}

String where ="and YEAR=" +AR_YEAR+"";

String BOX="";
if(FILE_BOX_CODE!=null)
{
	where+=" and BOX="+"'"+FILE_BOX_CODE+"'";
	BOX="第"+FILE_BOX_CODE+"盒";
}

Bean param = new Bean();
StringBuilder sql = new StringBuilder("");
String gdDeptName="";    
List<Bean> dataList=null;
if(FILE_CLASSCOD!=null)
{
	 sql.append("DEPT_CODE,ODEPT_CODE,DEPT_NAME");
     param.set(Constant.PARAM_SELECT, sql.toString());
     param.set(Constant.PARAM_WHERE, "and S_FLAG=1 and DEPT_CODE='"+FILE_CLASSCOD+"' and CMPY_CODE='"
               +strCmpyCode + "'");
     param = ServMgr.act("SY_ORG_DEPT", "finds", param);
    dataList = param.getList(Constant.RTN_DATA);
    for (Bean data : dataList) {
    	gdDeptName=data.getStr("DEPT_NAME");       
    }
}

int num=0;
int MediumNum=0;

param = new Bean();
sql = new StringBuilder("");
sql.append("count(*) CNT,sum(COUNT) GW_PAGE");
param.set(Constant.PARAM_SELECT, sql.toString());
param.set(Constant.PARAM_WHERE,where);
param = ServMgr.act("DA_DANGAN", "finds", param);
dataList = param.getList(Constant.RTN_DATA);

if(dataList.size() > 0)
{
	num =  dataList.get(0).getInt("CNT");
	MediumNum  =  dataList.get(0).getInt("GW_PAGE");
}





%>

<script language=javaScript>
      //打印页面设置
    function PageSetup()
    {
        document.styleSheets[0].addRule( "Input.Btn" , "display:none" , 0 );
        WebBrowser1.ExecWB(8,1,0,2);
        document.styleSheets[0].removeRule(0);
    }
      //打印文件
    function printPage(StartPage,EndPage)
    {
        document.all("idControls").style.display="none";
        WebBrowser1.ExecWB(7,1);
        document.all("idControls").style.display="";
    }
</script>

<style type="text/css">
<!--
td
{
    font-size: 16px;
}
.border0 {
	border-top-width:  1px;
	border-right-width:  0px;
	border-bottom-width: 0px;
	border-left-width:  1px;
	border-top-style: solid;
	border-right-style: solid;
	border-bottom-style: solid;
	border-left-style: solid;
	border-top-color: #000000;
	border-right-color: #000000;
	border-bottom-color: #000000;
	border-left-color: #000000;
}
.border1 {
	border-top-width:  1px;
	border-right-width:  1px;
	border-bottom-width: 0px;
	border-left-width:  0px;
	border-top-style: solid;
	border-right-style: solid;
	border-bottom-style: solid;
	border-left-style: solid;
	border-top-color: #000000;
	border-right-color: #000000;
	border-bottom-color: #000000;
	border-left-color: #000000;
}



.border5 {
    border-top-width: 0px;
    border-right-width: 1px;
    border-bottom-width: 0px;
    border-left-width: 0px;
    border-top-style: solid;
    border-right-style: solid;
    border-bottom-style: solid;
    border-left-style: solid;
    border-top-color: #000000;
    border-right-color: #000000;
    border-bottom-color: #000000;
    border-left-color: #000000;
}

.border6 {
    border-top-width: 0px;
    border-right-width: 0px;
    border-bottom-width: 0px;
    border-left-width: 1px;
    border-top-style: solid;
    border-right-style: solid;
    border-bottom-style: solid;
    border-left-style: solid;
    border-top-color: #000000;
    border-right-color: #000000;
    border-bottom-color: #000000;
    border-left-color: #000000;
}

.border7 {
    border-top-width: 0px;
    border-right-width: 1px;
    border-bottom-width: 1px;
    border-left-width: 1px;
    border-top-style: solid;
    border-right-style: solid;
    border-bottom-style: solid;
    border-left-style: solid;
    border-top-color: #000000;
    border-right-color: #000000;
    border-bottom-color: #000000;
    border-left-color: #000000;
}

.doc_text1 {
	BORDER-RIGHT: rgb(255,255,255) 0px solid; BORDER-TOP: rgb(255,255,255) 0px solid; FONT-WEIGHT: normal; FONT-SIZE: 22px; BACKGROUND: white; BORDER-LEFT: rgb(255,255,255) 0px solid; COLOR: #000000; BORDER-BOTTOM: rgb(255,255,255) 0px solid; FONT-FAMILY: "Verdana", "Arial", "Helvetica", "sans-serif"
}
-->
</style>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" bgcolor="#FFFFFF" >
<center>
<table width="700" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
    <tr>
        <td align="left" valign="top">
            <table width="100%" border="0" cellpadding="0" cellspacing="0" >
                <tr>
                    <td height="70" align="center"><br>
                    <font size="6">
                    	
                    	<strong>备&nbsp;&nbsp;考&nbsp;&nbsp;表</strong></font>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table width="100%"  border="0" cellpadding="0" cellspacing="0">
                            <tr>
                            	 <td   class="border0" valign=top width=10%>&nbsp;</td>
                                <td height="100" colspan="2" align=left  valign=center class="border1">
	                                <br>
	                                <font size="5"><strong>盒内文件情况说明</strong></font>
                                </td>
                            </tr>
                            <tr>
                            	  <td   class="border6" valign=top width=10%>&nbsp;</td>
                                <td  colspan="2" class="border5" valign=top>
  	                              <font size="5"><%=AR_YEAR%>年度<%=gdDeptName%>，<%=term%>，<%=BOX%>，盒内文件共 <%=num%>件，共 <%=MediumNum%>页.</font>
  	                            </td>
                            </tr>
                            
                           <tr onclick="javascript:showScriptDiv(<%=FILE_BOX_CODE %>)">
                            	  <td height=700   class="border6" valign=top width=10%>&nbsp;</td>
                                <td   valign=top id=boxScriptTD >
                                	
  	                             <P style="LINE-HEIGHT:26pt;font-size:18pt"><%=BrmComm.addBr(BrmComm.toHTML(boxScript),"<br>") %></p>
  	                            </td>
  	                            <td  class="border5" valign=top width=10%>&nbsp;</td>
                            </tr>
                            
                           
                            <td class="border7" colspan="3" valign=top width=100%>
                            		<table width=100%>
                            			<tr>
                            				<td width=60%></td>
                            				<td width=40% height="40">
                            					<font size="5">整理人：&nbsp;<%=name1%></font>
                            				</td>
                            			</tr>		
                            			<tr>
                            				<td width=60%></td>
                            				<td width=40% height="40">
                            					<font size="5">检查人：&nbsp;<%=name2%></font>
                            				</td>
                            			</tr>		
                            			<tr>
                            				<td width=60%></td>
                            				<td width=40% height="60">
                                      <font size="5"><%=printYear%>年&nbsp;</font>
                                      <font size="5"><%=printMonth%>月&nbsp;</font>
                                      <font size="5"><%=printDay%>日</font>
                            				</td>
                            			</tr>	
                            		</table>
                            </td>
                            
                           
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</center>



<div id=idControls class="noprint" align="center">
<input class=Btn id=set type=button value="页面设置" onclick="PageSetup();">
<input class=Btn id=print type=button value="打印" onclick="printPage();">
<input class=Btn id=back type="button" value="关闭" ONCLICK="window.close()">
</div>


<div id="scriptDiv" style="position:absolute; width:0px; height:0px; z-index:1;visibility:hidden;">
<table width='330' border='0' cellspacing='10' bgcolor='#ddffFF' style='{  border-color: #3388cc; border-style: solid; border-width: 1px;}'>
	 <form name=boxScriptForm method="post" action="com.zotn.screens.danganFlex.DanganPrintServlet">
	 <input type=hidden name=func value="doPrintBK">
	 <input type=hidden name=year value="<%=printYear%>">
	 <input type=hidden name=box value="<%=printMonth%>">
	 <input type=hidden name=gdDeptID value="<%=printDay %>">
	 <input type=hidden name=term value="<%=term %>">
	 <tr><td align=center>盒内文件情况说明：
	 </td></tr>
	 <tr><td>
	 <TEXTAREA name=boxScript  ROWS="12" style="width:540px;" ><%=boxScript%></TEXTAREA>
	 </td></tr>
	 
	 <tr><td>
	 	整理人 ：<input  name=name1    value='<%=name1%>' size=10>
	 	检查人 ：<input  name=name2  value='<%=name2%>' size=10>
	 </td></tr>
	 
	 <tr><td>
	 	年 ：<input  name=printYear   value='<%=printYear%>' size=4>
	 	月 ：<input  name=printMonth   value='<%=printMonth%>' size=2>
	 	日 ：<input  name=printDay   value='<%=printDay%>' size=2>
	 </td></tr>
	 
	 <tr><td align=center>
	 <input type=submit name=bt value='保存' calss=button >
	 <input type=button name=bt value='关闭' calss=button onclick="javascript:colseDiv();">
	 </td></tr>
	 </form>
</table>
</div>


<script type="text/javascript">

function showScriptDiv(box)
{
 	var divObj=document.getElementById("scriptDiv");
    divObj.style.visibility = 'visible';
    divObj.style.left =  document.body.scrollLeft+210;
    divObj.style.top = document.body.scrollTop+190;
   
    
}
function colseDiv()
{
    var divObj=document.getElementById("scriptDiv");
    divObj.style.visibility = 'hidden';
}
showScriptDiv(<%=FILE_BOX_CODE%>);
colseDiv();
</script>
</body>
</html>