<!-- $Header: /update/showUpdateFileList.jsp       tanyh 2015-11-05
	 待更新程序文件列表
-->
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.rh.core.serv.OutBean"%>
<%@ page import="com.rh.core.util.Constant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	OutBean outBean = (OutBean) request.getAttribute(Constant.RTN_DISP_DATA);
	//文件列表
	ArrayList fileList = new ArrayList();
	if(outBean.get("FILE_LIST") != null){
		fileList = (ArrayList)outBean.get("FILE_LIST");
	}
	//zip包文件ID
	String fileId = outBean.getStr("FILE_ID");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>待更新程序文件列表</title>
<script type="text/javascript">
function doUpdate(){
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
		document.updateForm.submit();
	}else{
		alert("请选择需要打包的程序文件");
	}
}
</script>
</head>
<body style="background-color:#ededed">
<form name="updateForm" action="/SY_COMM_UPDATE.doUpdate.do" method="post">
<input type="hidden" name="fileId" value="<%=fileId%>" />
<table width="90%" align="center">
	<%if(fileList != null && fileList.size() > 0){%>
	<%for(int i=0; i<fileList.size(); i++){
			String fileName = (String)fileList.get(i);
	%>
	<tr>
	<td width="5%" align="right"><input type="checkBox" name="fileList" checked value="<%=fileName%>"></td>
	<td width="95%"><%=fileName%></td>
	</tr>
	<%}%>
	<tr><td colspan="2" align="left">共找出<%=fileList.size()%>个文件</td></tr>
	<tr><td colspan="2" align="center"><input type="button" onClick="javascript:doUpdate();" name="package" value="更新"></td></tr>
	<%}else{%>
	<tr>
	<td>对不起，没有要更新的文件</td>
	</tr>
	<%}%>
</table>
</form>
</body>
</html>