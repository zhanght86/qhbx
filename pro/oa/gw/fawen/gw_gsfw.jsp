<!--平台级的jsp文件-->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--StdListView.jsp列表页面-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.rh.core.base.*" %>
<%@ page import="com.rh.core.util.*" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>功能列表页面</title> 
   <%@ include file= "/core/view/inHeader.jsp" %>
    <script type="text/javascript" src="/core/frame/coms/all/ui.all.js"></script>
    <!-- mind-->
    <script type="text/javascript" src="/cm/mind/mind.js"></script>
	
     <script type="text/javascript" src="/gw/file/gwfile.js"></script>
     <!-- office client -->
     <script type="text/javascript" src="/cm/office/zotnClientLib_NTKO.js"></script>
     <script type="text/javascript" src="/cm/office/ntkoocx.js"></script>
     <script type="text/javascript" src="/cm/office/office.js"></script>
     <script type="text/javascript" src="/cm/office/redHead.js"></script>
     <script type="text/javascript" src="/cm/office/fileOperator.js"></script>
     <script type="text/javascript" src="/cm/office/functions.js"></script>
     
	<link rel="stylesheet" type="text/css" href="/core/frame/coms/all/style.css" />
</head>
<%
String sId = "GW_FW_GSFW";
Bean data = (Bean) request.getAttribute(Constant.RTN_DISP_DATA);
String pkCode = data.getId();
String frameId = request.getParameter("frameId");
frameId = "GW_FW_GSFW-" + pkCode;
%>
<body>
<script type="text/javascript">
GLOBAL.setFrameId('<%=frameId%>');
(function() {
    jQuery(document).ready(function(){
    	//ACT_LIST_MODIFY   
	    var temp = {"act":UIConst.ACT_CARD_MODIFY,"sId":"<%=sId%>","parHandler":null,"parTabClose":"true"};
    	
	    temp[UIConst.PK_KEY] = "<%=pkCode%>";
	    var cardView = new rh.vi.cardView(temp);
	    cardView.show();  
    });
})();
</script>

</body>
</html>