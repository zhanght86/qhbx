<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--StdListView.jsp列表页面-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<OBJECT ID="DealWordDoc" CLASSID="CLSID:296C8EF9-AAFB-4A5C-98F6-22DE064E9176" CODEBASE="/da/jsp/cab/pDealWord.CAB#version=3,0,0" style="visibility: hidden;" width="0" height="0"></OBJECT>

<script language="JavaScript" src="/sy/util/office/zotnClientLib_NTKO.js"></script>
<script language="JavaScript" src="/sy/util/office/fileOperator.js"></script>

<%
	String urlPath = request.getContextPath();
    String fileId =  (String) request.getParameter("FILE_ID");
%>



<script language="JavaScript">
    doPrintContent();
	function doPrintContent(){
		var srcFileUrl = "http://" + window.location.host;
		
		if (window.location.port.length > 0 && window.location.host.indexOf(":") < 0) {
			srcFileUrl += ":" + window.location.port;
		}
		srcFileUrl += "/file/<%=fileId%>";
	
		var SedControl = new ActiveXObject("SEDOI.SedOICtrl.1");
  		DealWordDoc.canPrintNumber = 1;
   		DealWordDoc.printGWReceiveFile(srcFileUrl)
	}
</script>
