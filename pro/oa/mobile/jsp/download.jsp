<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<%@ page import="com.rh.core.comm.FileStorage" %>
<%@ page import="com.rh.core.util.RequestUtils" %>
<%@ page import="com.rh.core.base.Context"%>
<%@ page import="java.io.InputStream" %>
<%@ page import="org.apache.commons.io.IOUtils" %>
<%@ page import="java.io.OutputStream" %>

<%
	request.setCharacterEncoding("UTF-8");
	response.setHeader("Server", "rhfile-server");
    final String CONTEXT_PATH = request.getContextPath();
	String fileId = RequestUtils.getStr(request, "fileId");
	String pageName = RequestUtils.getStr(request, "pageName");
	String fileRoot = Context.getSyConf("WX_FILE_DIR", "/oawxshare/file-temp-dir/");
	
	String absolutePath = fileRoot + "" + fileId + "/" + pageName;
	System.out.println("download.jsp is absolutePath : " + absolutePath);
    InputStream wxis = null;
    OutputStream wxout = null;
	
	try {
		
		wxis = FileStorage.getInputStream(absolutePath);
	} catch(Exception e) {
		System.out.println("11"+e);
	} 
	try {
		
		wxout = response.getOutputStream();
	} catch(Exception e) {
		System.out.println("22"+e);
	} 
	try {
	    IOUtils.copyLarge(wxis, wxout);
	} catch(Exception e) {
		System.out.println("33"+e);
	} 
	try{
	    IOUtils.closeQuietly(wxis);
	    IOUtils.closeQuietly(wxout);
	    wxout.flush();
	} catch(Exception e) {
		System.out.println("44"+e);
	} finally {
		IOUtils.closeQuietly(wxis);
        response.flushBuffer();
	}
     out.clear(); 
     out=pageContext.pushBody();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>download</title>
</head>
<body>
</body>
</html>