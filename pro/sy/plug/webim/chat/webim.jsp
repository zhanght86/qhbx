<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
  <title>webim</title>
  <%@ include file= "/sy/base/view/inHeader.jsp" %>
    <script src='/cm/webim/scripts/strophe.js'></script>
    <script src='/cm/webim/scripts/flXHR.js'></script>
    <script src='/cm/webim/scripts/strophe.flxhr.js'></script>
    
    <script src='/cm/webim/scripts/iso8601_support.js'></script>
    <script src='/cm/webim/scripts/strophe.rsm.js'></script>
    <script src='/cm/webim/scripts/strophe.archive.js'></script>
    <script src='/cm/webim/scripts/strophe.hismsg.js'></script>
    <script src='/cm/webim/scripts/strophe.recentcontact.js'></script>
  
  <!-- 
    suport file upload
   -->  
  <script type="text/javascript" src="/sy/base/frame/coms/file/swfupload.js"></script>
  <script type="text/javascript" src="/sy/base/frame/coms/file/js/swfupload.queue.js"></script>
  <script type="text/javascript" src="/sy/base/frame/coms/file/js/fileprogress.js"></script>
  <script type="text/javascript" src="/sy/base/frame/coms/file/js/handlers.js"></script>
  <link rel="stylesheet" type="text/css" href="/cm/webim/chat/webim.css"/>
  <script type="text/javascript" src="/cm/webim/chat/rhWbimView.js"></script>
</head>
<body>

</body>
<script type="text/javascript">
(function() {
    jQuery(document).ready(function(){
    	var userCode = System.getVar("@USER_CODE@");
    	if (!userCode) {
        	alert("please re-login...");
        	}
    	var imDomain = "@rhim.server";
    	var jid = userCode + imDomain;  
	    var temp = {"id":"wbim", "jid":jid, "token":"","domain":imDomain};
	    var imObj = new rh.vi.wbimView(temp);
	    imObj.show();
	    jQuery(document).data("imObj",imObj);
    });
})();
</script>
</html>
