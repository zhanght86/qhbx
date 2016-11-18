<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--StdListView.jsp列表页面-->
<%@page import="com.rh.core.base.Context"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script language="JavaScript" src="/sy/util/office/zotnClientLib_NTKO.js"></script>
<script language="JavaScript" src="/sy/util/office/fileOperator.js"></script>

<%
	String urlPath = request.getContextPath();
    String fileId =  (String) request.getParameter("FILE_ID");
	String picType = (String) request.getParameter("PICTYPE");
	String basePathHeaderPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
	basePathHeaderPath = Context.getSyConf("SYS_HOST_ADDR", basePathHeaderPath);
%>

<%@ include file="/sy/util/stylus/ZotnClientLib.jsp" %>

<OBJECT   classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2   height=21   id=WebBrowser   width=87></OBJECT>

<div id = "picId" align="center"><img id="printImgId" src="<%=basePathHeaderPath%>/file/<%=fileId%>"></div>

<SCRIPT  LANGUAGE="JavaScript">
function printWordDoc(){
	try {
		printPic();
	}catch(e){
		alert(e.message);
	}
}

//打印Word文件
function printPic(){
	//fileName = "c:\\ZotnDoc\\" + fileName;
	try {
		var iMaxWidth = 620;
		var iMaxHeight = 800;
		var imgObj = document.getElementById("printImgId");
		var widthInt = imgObj.width;
		var heightInt = imgObj.height;
		//如果是竖图，则不做反转处理
		if (widthInt <= heightInt ) {
			if (widthInt > iMaxWidth) {
				imgObj.width = iMaxWidth;
			}
			if (heightInt > iMaxHeight) {
				imgObj.height = iMaxHeight;
			}
		//如果是横图则做反转处理
		} else {
			if (widthInt > iMaxWidth) {
				imgObj.width = iMaxHeight;
			}
			if (heightInt > iMaxHeight) {
				imgObj.height = iMaxWidth;
			}
		}
		document.all.WebBrowser.ExecWB(6,1);
	}catch(e){
		alert(e.message);
	}
	window.close();
}

window.onload = function() {
	printWordDoc();
}
</script>