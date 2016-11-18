<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--StdListView.jsp列表页面-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script language="JavaScript" src="/sy/util/office/zotnClientLib_NTKO.js"></script>
<script language="JavaScript" src="/sy/util/office/fileOperator.js"></script>

<%
	String urlPath = request.getContextPath();
    String fileIds =  (String) request.getParameter("FILE_ID");
%>

<%@ include file="/sy/util/stylus/ZotnClientLib.jsp" %>

<SCRIPT  LANGUAGE="JavaScript">

var fileIds = "<%=fileIds%>";

var wordApp;
var wordDoc;

var fileIdArray = fileIds.split(",");
for (var i=0;i<=fileIdArray.length-1;i++) {
    if (fileIdArray[i].length > 1) {
		printWordDoc(fileIdArray[i]);
	}
}

//closeApp();

function printWordDoc(fileId){
	try {
		var tempDownloadURL = getHostURL() + "/file/" + fileId;
		
		var tempFileName = ZotnClient.DownloadFile(tempDownloadURL, "demo_file.doc" ,false,false,true,false);
		
		printDoc(tempFileName);
	}catch(e){
		alert(e.message);
	}
}

//打印Word文件
function printDoc(fileName){

	fileName = "c:\\ZotnDoc\\" + fileName;
	try {
		createApp();
		wordDoc = wordApp.documents.open(fileName);
		wordDoc.ShowRevisions = false;
		wordDoc.PrintRevisions = false;
		wordDoc.TrackRevisions = false;
		wordDoc.AcceptAllRevisions();

		wordDoc.printout();
		//closeApp();		
		
		// -- setTimeout(function(){printTimeOut(wordDoc);}, 3000);
	}catch(e){
		alert(e.message);
		closeApp();
	}
	//window.close();
}

function printTimeOut(wordDoc) {
	wordDoc.printout();
	closeApp();
}

function createApp(){
	if(wordApp ==null || wordApp ==undefined) {
		try{
			wordApp = new ActiveXObject("Word.Application");
		}catch(e){
			throw e;
		}
	}
}

function closeApp(){
	if (wordApp ==null || wordApp==undefined) {
		
	} else {
		wordApp.documents.close(0);
		wordApp.quit(0);
		wordApp = null;
	}
}
</script>