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
    String pageTitle = "归档文件目录打印";
    UserBean user = Context.getUserBean();
    // List<Bean> dataListII = DictMgr
    String strCmpy = user.getODeptName();
    String strCmpyCode = user.getCmpyCode();
%>

   
<%    
	
String column1="件号";
String AR_YEAR= request.getParameter("AR_YEAR");
String FILE_CLASSCOD= request.getParameter("FILE_CLASSCOD");
String FILE_TIMELIMIT= request.getParameter("FILE_TIMELIMIT");
String FILE_BOX_CODE= request.getParameter("FILE_BOX_CODE");
String GW_FILE_TYPE= null;
String FILE_FOLDER_CODE= request.getParameter("FILE_FOLDER_CODE");


StringBuilder sql = new StringBuilder("");
Bean param = new Bean();
List<Bean> dataList=null;
String strDept="";
if(FILE_CLASSCOD!=null)
{
	 sql.append("DEPT_CODE,ODEPT_CODE,DEPT_NAME");
     param.set(Constant.PARAM_SELECT, sql.toString());
     param.set(Constant.PARAM_WHERE, "and S_FLAG=1 and DEPT_CODE='"+FILE_CLASSCOD+"' and CMPY_CODE='"
               +strCmpyCode + "'");
     param = ServMgr.act("SY_ORG_DEPT", "finds", param);
    dataList = param.getList(Constant.RTN_DATA);
    for (Bean data : dataList) {
    	strDept=data.getStr("DEPT_NAME");       
    }
}


String where="and YEAR="  +AR_YEAR + "";
if(FILE_CLASSCOD!=null)
	where+=" and GD_DEPT="+"'"+FILE_CLASSCOD+"'";

String term="";
if(FILE_TIMELIMIT!=null)
{
	where+=" and TERM="+"'"+FILE_TIMELIMIT+"'";
	term=FILE_TIMELIMIT;
}

if(GW_FILE_TYPE!=null)
	where+=" and KIND_ID="+"'"+GW_FILE_TYPE+"'";

String BOX="";
if(FILE_BOX_CODE!=null)
{
	where+=" and BOX="+FILE_BOX_CODE;
	BOX="第"+FILE_BOX_CODE+"盒";
}

if(FILE_FOLDER_CODE!=null)
	where+=" and VOL_ID="+"'"+FILE_FOLDER_CODE+"'";

param = new Bean();
sql = new StringBuilder("");
sql.append("BOX,DA_NUM,DUTY,GW_YEAR_TEXT,TITLE,GW_DATE,COUNT,SECRET,REMARK");
param.set(Constant.PARAM_SELECT, sql.toString());
param.set(Constant.PARAM_WHERE, where);
param = ServMgr.act("DA_DANGAN", "finds", param);
dataList = param.getList(Constant.RTN_DATA);


int pagesize=10;    
int endpage=dataList.size()/pagesize + ( dataList.size() % pagesize > 0 ? 1 : 0 );

 %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>文书档案目录</title>
</head>
<style type="text/css">
<!--
.border7 {
    font-family:楷体_GB2312;
    font-size: 15px;
    border-top-width: 2px;
    border-right-width: 0px;
    border-bottom-width: 0px;
    border-left-width: 2px;
    border-top-style: solid;
    border-right-style: solid;
    border-bottom-style: solid;
    border-left-style: solid;
    border-top-color: #000000;
    border-right-color: #000000;
    border-bottom-color: #000000;
    border-left-color: #000000;
}
.border8 {
    font-family:楷体_GB2312;
    font-size: 15px;
    border-top-width: 2px;
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
.border9 {
    font-family:楷体_GB2312;
    font-size: 15px;
    border-top-width: 2px;
    border-right-width: 2px;
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
.border4 {
    font-size: 15px;
    border-top-color: #000000;
    border-top-width: 1px;
    border-right-width: 0px;
    border-bottom-width: 0px;
    border-left-width: 2px;
    border-top-style: solid;
    border-right-style: solid;
    border-bottom-style: solid;
    border-left-style: solid;
    border-right-color: #000000;
    border-bottom-color: #000000;
    border-left-color: #000000;
}
.border5 {
    font-size: 15px;
    border-top-width: 1px;
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
.border6 {
    font-size: 15px;
    border-top-width: 1px;
    border-right-width: 2px;
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
.border1 {
		font-size: 15px;
    border-top-width: 1px;
    border-right-width: 0px;
    border-bottom-width: 2px;
    border-left-width: 2px;
    border-top-style: solid;
    border-right-style: solid;
    border-bottom-style: solid;
    border-left-style: solid;
    border-top-color: #000000;
    border-right-color: #000000;
    border-bottom-color: #000000;
    border-left-color: #000000;
}

.border2 {
		font-size: 15px;
    border-top-width: 1px;
    border-right-width: 0px;
    border-bottom-width: 2px;
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


.border3 {
		font-size: 15px;
    border-top-width: 1px;
    border-right-width: 2px;
    border-bottom-width: 2px;
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

.td10{
    font-size: 32px;
    color: #000000;
    font-family:方正小标宋简体;
}
.td12{
    font-size: 16px;
    color: #000000;
}

td {
    font-size: 16px;
    color: #000000;
}


-->
</style>

<script >
	function PageSetup()
	{
		document.styleSheets[0].addRule( "Input.Btn" , "display:none" , 0 );
		WebBrowser1.ExecWB(8,1,0,2);
		document.styleSheets[0].removeRule(0);
	}


	function doPrint(){
		document.all.printLayer.style.display='none';
		//window.print();
		WebBrowser1.ExecWB(7,1);
		document.all.printLayer.style.display='';
	}
</script>
<object ID="WebBrowser1" WIDTH="0" HEIGHT="0" CLASSID="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" VIEWASTEXT>
</object>
<body>
<center>
<div id="printLayer" style="display:">
	<input class=Btn id=set type="button" value="页面设置" onclick="PageSetup();">
	<input class=Btn id=print type="button" value="打 印" onclick="javascript:doPrint()">
    <input class=Btn id=back type="button" value="关闭" onclick="window.close()">
</div>
<br>
<table width="700" border="0"  cellspacing="0" cellpadding="3" bgcolor=#FFFFFF>
        <tr>
            <td colspan=8>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td height="35" valign="bottom" align="center" class="td10" width=100% colspan=3>
                             归&nbsp;档&nbsp;文&nbsp;件&nbsp;目&nbsp;录
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td colspan=8>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td width="50%" height="15" width="518" id=dwmc class="td12"><%=strCmpy%>&nbsp;<%=AR_YEAR%>年度 &nbsp;<%=strDept%>&nbsp;<%=term%>&nbsp;<%=BOX%></td>
                        <td width="34%"  id=num class="td12" align="right">>第 1 页</td>
                        <td width="16%"  id=endpage class="td12" align="right">共 <%=endpage%> 页&nbsp;</td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr height="70">
            <td class="border7" width="7%" align="center"><%=column1%></td>
            <td class="border8" width="13%" align="center">责任者</td>
            <td class="border8" width="10%" align="center">文&nbsp;&nbsp;号</td>
            <td class="border8" width="42%" align="center">题&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名</td>
            <td class="border8" width="7%" align="center">日期</td>
            <td class="border8" width="7%" align="center">页数</td>
            <td class="border8" width="7%" align="center">密级</td>
            <td class="border9" width="7%" align="center">备注</td>
        </tr>



        <%
        int currentPage=1;
		int index=0;
        for(;index<dataList.size();index++)
        {  
        	Bean data=dataList.get(index);
            String box=data.getStr("BOX");
            String daNum=data.getStr("DA_NUM");
            if(daNum.trim().length()==0 )
            {
                daNum="&nbsp;";
            }
            //String daNum=Integer.toString(index+1);
            String duty=data.getStr("DUTY");
            if(duty.trim().length()==0)
            {
                duty="&nbsp;";
            }
            String gwNum=data.getStr("GW_YEAR_TEXT");
            
						
            if(gwNum.trim().length()==0)
            {
                gwNum="&nbsp;";
            }
            String title=data.getStr("TITLE");
            title=BrmComm.strReplace(title,"\"","&quot;");
            title.replaceAll("\"","&quot;");
            if(title.trim().length()==0)
            {
                title="&nbsp;";
            }
            String gwDate=data.getStr("GW_DATE");
            if(gwDate.trim().length()==0)
            {
                gwDate="&nbsp;";
            }
            else if(gwDate.length()>=10)
            {
                gwDate=gwDate.substring(0,4)+"<br>"+gwDate.substring(5,7)+gwDate.substring(8,10);
            }
            String  mediumNum=data.getStr("COUNT");
            String secret=data.getStr("SECRET");
            if(secret.trim().length()==0)
            {
                secret="&nbsp;";
            }
            
            String remark=data.getStr("REMARK");
            if(remark.trim().length()==0)
            {
                remark="&nbsp;";
            }
						


        if( (index+1)%pagesize>0 ){
        %>
        <tr height="75">
            <td  class="border4" align="center"><%=daNum %></td>
            <td  class="border5"><%=duty %></td>
            <td  class="border5"><%=gwNum %></td>
            <td  class="border5"><%=title %></td>
            <td  class="border5" align="center"><%=gwDate %></td>
            <td  class="border5" align="center"><%=mediumNum %></td>
            <td  class="border5" align="center"><%=secret %></td>
			<td  class="border6" ><%=remark %></td>
        </tr>
	
		<%  }else{	
			currentPage++; 
			%>
			        <tr height="75">
            <td  align="center" class="border1"><%=daNum %></td>
            <td  class="border2"><%=duty %></td>
            <td  class="border2"><%=gwNum %></td>
            <td  class="border2"><%=title %></td>
            <td  align="center" class="border2"><%=gwDate %></td>
            <td  align="center" class="border2"><%=mediumNum %></td>
            <td  align="center" class="border2"><%=secret %></td>
            <td  class="border3"><%=remark %></td>

        </tr>
        </table>
          <%if(index+1!=dataList.size())
           {%>
                <br clear=all style='page-break-before:always'>
		<br>
		<table width="700" border="0"  cellspacing="0" cellpadding="3" bgcolor=#FFFFFF>
        <tr>
            <td colspan=8>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td height="35" valign="bottom" align="center" class="td10" width=100% colspan=3>
                           归&nbsp;档&nbsp;文&nbsp;件&nbsp;目&nbsp;录
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td colspan=8>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                        <td width="50%" height="15" id=dwmc class="td12"><%=strCmpy%>&nbsp;<%=AR_YEAR%>年度 &nbsp;<%=strDept%>&nbsp;<%=term%>&nbsp;<%=BOX%></td>
                        <td width="34%"  id=num class="td12" align="right">µÚ <%=currentPage%> Ò³</td>
                        <td width="16%"  id=endpage2 class="td12" align="right">¹² <%=endpage%> Ò³&nbsp;</td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr height="70">
             <td class="border7" width="7%" align="center"><%=column1%></td>
            <td class="border8" width="13%" align="center">责任者</td>
            <td class="border8" width="10%" align="center">文&nbsp;&nbsp;号</td>
            <td class="border8" width="42%" align="center">题&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名</td>
            <td class="border8" width="7%" align="center">日期</td>
            <td class="border8" width="7%" align="center">页数</td>
            <td class="border8" width="7%" align="center">密级</td>
            <td class="border9" width="7%" align="center">备注</td>
        </tr>
        <%}%>
        <%}%>
        <%}%>

		<%
		//添加空白的行
		int surplus = pagesize - (index%pagesize);

		for(int i=0;i<surplus;i++){

			String strClass1 = "border4";
			String strClass2 = "border5";
			String strClass3 = "border6";

			if(i==surplus-1){
				strClass1 = "border1";
				strClass2 = "border2";
				strClass3 = "border3";
			}
        %>
        <tr height="75">
            <td  class="<%=strClass1%>" align="center">&nbsp;</td>
            <td  class="<%=strClass2%>"align="center">&nbsp;</td>
            <td  class="<%=strClass2%>"align="center">&nbsp;</td>
            <td  class="<%=strClass2%>">&nbsp;</td>
            <td  class="<%=strClass2%>" align="center">&nbsp;</td>
            <td  class="<%=strClass2%>">&nbsp;</td>
            <td  class="<%=strClass2%>">&nbsp;</td>
            <td  class="<%=strClass3%>" >&nbsp;</td>
        </tr>

		<%}%>
</table>

</center>

</body>
</html>