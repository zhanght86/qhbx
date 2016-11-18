<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.rh.core.base.Bean"%>
<%@ page import="com.rh.core.util.Constant"%>
<%@ include file="../../core/view/inHeader.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="java.lang.*"%>
<%@ page import="com.rh.core.base.Context" %>
<%@ page import="com.rh.core.base.Bean" %>
<%@ page import="com.rh.core.util.Constant" %> 

<%@ page import="com.rh.core.serv.ServMgr " %> 
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.rh.core.org.UserBean"%>
<%@ page import="com.rh.da.BrmComm " %>

<%     UserBean user = Context.getUserBean();
       String strODeptCode = user.getODeptCode();
       String strCmpyCode = user.getCmpyCode();
%>

<title>库房管理</title>

<body>
<table border="0" cellspacing="1" cellpadding="0" width="100%" height="100%">

 <tr>
		<td valign="top"  height=4>
		    
		</td>
    </tr>
    <tr>
        <td width="100%" height=100% align="center">
			<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
					id="workFlow"   width="98%" height="100%"
					codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
					<param name="movie" value="daManager.swf" />
					<param name="quality" value="high" />
					<param name="bgcolor" value="#869ca7" />
					<param name="allowScriptAccess" value="sameDomain" />
					<param name="allowFullScreen" value="true" />
					<param name="FlashVars" value="cmpcode=<%=strCmpyCode %>&oDeptCode=<%=strODeptCode %>" />
					<embed id="workFlow"  src="daManager.swf" quality="high" bgcolor="#869ca7"
						width="96%" height="100%" name="workFlow" align="middle"
						play="true"
						loop="false"
						quality="high"
						allowScriptAccess="sameDomain"
						type="application/x-shockwave-flash"
						FlashVars="cmpcode=<%=strCmpyCode %>&oDeptCode=<%=strODeptCode %>"
						pluginspage="http://www.adobe.com/go/getflashplayer">
					</embed>			
			</object>
        </td>
    </tr>
 
     
    </tr>
</table>

<br>


</body>
	