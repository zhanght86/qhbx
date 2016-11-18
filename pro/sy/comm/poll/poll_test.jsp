<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- custom search view  -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.rh.core.plug.search.client.*"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<%@ include file="/sy/base/view/inHeader.jsp"%>
	
    <script src="/sy/comm/poll/poll.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="/sy/comm/poll/poll.css" />
    
</head>
<body class="mbList-body">
</body>

<script type="text/javascript">


(function() {
    jQuery(document).ready(function(){
	    var opts = {"sId":"SY_PLUG_SEARCH","pollId":"3zI-I222t0oFtn6L34ToKa","pCon":jQuery("body")};
	    var listView = new rh.vi.poll(opts);
	    listView.show();
    });
})();
</script>
</html>
	
	
	