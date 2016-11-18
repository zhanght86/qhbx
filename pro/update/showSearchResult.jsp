<!-- $Header: /update/showSearchResult.jsp       tanyh 2015-11-05
	 程序搜索结果展示
-->
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.rh.core.serv.OutBean"%>
<%@ page import="com.rh.core.util.Constant"%>
<%@ page import="java.io.File"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	OutBean outBean = (OutBean) request.getAttribute(Constant.RTN_DISP_DATA);
	//搜索结果
	ArrayList fileList = new ArrayList();
	if(outBean.get("FILE_LIST") != null){
		fileList = (ArrayList)outBean.get("FILE_LIST");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>程序搜索结果</title>
<script type="text/javascript">
function doPackage(){
	var fileList = document.getElementsByName("fileList");
	var fileSelected = false;
	if (fileList) {
		for(var i=0; i<fileList.length; i++){
			if(fileList[i].checked){
				fileSelected=true;
				break;
			}
		}
	}
	if(fileSelected){
		document.packageForm.submit();
	}else{
		alert("请选择需要打包的程序文件");
	}
}
</script>
</head>
<body style="background-color:#ededed">
<form name="packageForm" action="/SY_COMM_UPDATE.doPackage.do" method="post">
<table width="90%" align="center">
	<%if(fileList != null && fileList.size() > 0){%>
	<%for(int i=0; i<fileList.size(); i++){
			File fileItem = (File)fileList.get(i);
	%>
	<tr>
	<td width="5%" align="right"><input type="checkBox" name="fileList" checked value="<%=fileItem.getPath()%>"></td>
	<td width="95%">[<%=(new SimpleDateFormat("yyyy.MM.dd")).format(new Date(fileItem.lastModified()))%>]&nbsp;<%=fileItem.getPath()%></td>
	</tr>
	<%}%>
	<tr><td colspan="2" align="left">共找出<%=fileList.size()%>个文件</td></tr>
	<tr><td colspan="2" align="center"><input type="button" onClick="javascript:doPackage();" name="package" value="打包"></td></tr>
	<%}else{%>
	<tr>
	<td>对不起，没有搜索到更新文件</td>
	</tr>
	<%}%>
</table>
</form>
</body>
</html>