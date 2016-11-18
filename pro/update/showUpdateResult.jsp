<!-- $Header: /update/showUpdateResult.jsp       tanyh 2015-11-05
	 文件更新结果
-->
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.rh.core.serv.OutBean"%>
<%@ page import="com.rh.core.util.Constant"%>
<%@ page import="java.io.File"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.rh.update.utils.UpdateResultList"%>
<%@ page import="com.rh.update.utils.UpdateResultElement"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	OutBean outBean = (OutBean) request.getAttribute(Constant.RTN_DISP_DATA);
	//文件列表
	UpdateResultList fileList = new UpdateResultList();
	if(outBean.get("RESULT_LIST") != null){
		fileList = (UpdateResultList)outBean.get("RESULT_LIST");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>文件更新结果</title>
<script type="text/javascript">
function doClose(){
	window.close();
}
</script>
</head>
<body style="background-color:#ededed">
<table width="90%" align="center">
	<%if(fileList != null && fileList.size() > 0){%>
	<%for(int i=0; i<fileList.size(); i++){
			UpdateResultElement element = fileList.getElement(i);
	%>
	<tr>
	<td width="95%"><%=element%></td>
	</tr>
	<%}%>
	<tr><td align="left">共更新<%=fileList.size()%>个文件</td></tr>
	<tr><td align="center"><input type="button" onClick="javascript:doClose();" name="package" value="关闭"></td></tr>
	<%}else{%>
	<tr>
	<td>对不起，没有更新文件</td>
	</tr>
	<%}%>
</table>
</body>
</html>