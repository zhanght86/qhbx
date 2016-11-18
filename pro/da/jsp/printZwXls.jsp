<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--StdListView.jsp列表页面-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script language="JavaScript" src="/sy/util/office/zotnClientLib_NTKO.js"></script>
<script language="JavaScript" src="/sy/util/office/fileOperator.js"></script>

<%
	String urlPath = request.getContextPath();
    String fileId =  (String) request.getParameter("FILE_ID");
%>

<%@ include file="/sy/util/stylus/ZotnClientLib.jsp" %>


<SCRIPT  LANGUAGE="JavaScript">

var wordApp;

printWordDoc();

function printWordDoc(){
	try {
		var tempDownloadURL = getHostURL() + "/file/<%=fileId%>";
		
		var tempFileName = ZotnClient.DownloadFile(tempDownloadURL, "demo_file.xls" ,false,false,true,false);
		
		printExcel(tempFileName);
	}catch(e){
		alert(e.message);
	}
}

//打印Word文件
function printExcel(fileName){
	var wordDoc;
	fileName = "c:\\ZotnDoc\\" + fileName;
    var xlsApp = null;      
	
    try {          
        xlsApp = new ActiveXObject('Excel.Application');    
	} catch(e) {   
        alert(e+', 原因分析: 浏览器安全级别较高导致不能创建Excel对象或者客户端没有安装Excel软件');   
        return;   
    }      
    //var xlBook = xlsApp.Workbooks.Open('http://'+window.location.host+obj.value);  
    var xlBook = xlsApp.Workbooks.Open(fileName);  
    var xlsheet = xlBook.Worksheets(1);  
    xlsApp.Application.Visible = false;   
    xlsApp.visible = false;   
    xlsheet.printout();   
    xlsApp.Quit();   
    xlsApp=null;   
	
	window.close();
}

</script>