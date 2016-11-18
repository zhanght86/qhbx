<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.rh.core.plug.search.client.*"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>文库页面</title>
	<%@ include file="/sy/base/view/inHeader-mb.jsp"%>
    <script src="mbDocView.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="doc.css"/>
    <script src="<%=urlPath %>/sy/plug/search/suggest.js" type=text/javascript></script>
<style type="text/css">
#suggest {
	position: absolute;
	background-color: #FFFFFF;
	border: 1px solid #CCCCFF;
	width: 404;
}

#suggest div {
	padding: 5px;
	display: block;
	width: 404;
	FONT: 15px arial;
	overflow: hidden;
	white-space: nowrap;
}

#suggest div.select {
	color: #FFFFFF;
	background-color: #3366FF;
}

#suggest div.over {
	background-color: #99CCFF;
}
</style>
    
</head>
<body class="mbList-body">
</body>
<script type="text/javascript">
//suggestion
function startSuggest(){
}

(function() {
    jQuery(document).ready(function(){
	    var temp = {"sId":"SY_PLUG_DOC","pCon":jQuery("body")};
	    var docView = new mb.vi.doc(temp);
	    docView.show();
	    if (Browser.versions().mobile) {
		    window.addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false);
		    function hideURLbar(){
		      window.scrollTo(0,1);
		    }
	    }
    });
})();
</script>
</html>