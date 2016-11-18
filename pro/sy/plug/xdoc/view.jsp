<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/sy/base/view/inHeader.jsp"%>
<%
	request.setCharacterEncoding("UTF-8");

	//格式合同模板文件
	String file = request.getParameter("_FILE_");
	
	// 数据主键
	String id = request.getParameter("_ID_");
	
	// 二维码信息
	String cid = request.getParameter("cid");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>格式合同查看</title>
<script type="text/javascript" src="<%=Context.getSyConf("SY_XDOC_URL", "")%>/fpd.js"></script>
</head>
<body>
<script type="text/javascript">

	var ctId = "<%=id%>";
	var xdata = "";
	if (ctId && ctId.length > 0) {
		var contractData = FireFly.byId("LW_CONTRACT", "<%=id%>");
		xdata = contractData["CT_XML"];
	} 
	
	setXDocServer("<%=Context.getSyConf("SY_XDOC_URL", "")%>");
	var cid = "<%=(cid!=null)?cid:""%>";
	if (cid && cid.length > 0) {
		cid = encodeURIComponent(cid);
		runXData("<%=file%>", xdata, "&_runinput=true&cid=" + cid);
	} else {
		runXData("<%=file%>", xdata, "&_runinput=true");
	}
	
</script>
</body>
</html>