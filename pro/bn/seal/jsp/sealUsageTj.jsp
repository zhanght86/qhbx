<!-- $Header: /bn/seal/jsp/sealUsageTj.jsp       tanyh 2015-07-30
	 印章使用统计表
-->
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.rh.core.base.Bean"%>
<%@ page import="com.rh.core.serv.OutBean"%>
<%@ page import="com.rh.core.util.Constant"%>
<%@ page import="com.rh.core.util.JsonUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	OutBean outBean = (OutBean) request.getAttribute(Constant.RTN_DISP_DATA);
	//统计结果
	List<Bean> resultList = new ArrayList<Bean>();
	if(outBean.getStr("RESULT_LIST").length() > 0){
		resultList = JsonUtils.toBeanList(outBean.getStr("RESULT_LIST"));
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>印章使用统计</title>
<script type="text/javascript">
window.moveTo(0,0);
window.resizeTo(screen.availWidth,screen.availHeight);
</script>
<style>
.td_1{
	word-wrap:break-word;
	white-space:normal;
	border-bottom:1 solid #000;
	border-left:1 solid #000;
	border-top:none;
	border-right:1 solid #000;
	font-family:'宋体';
	text-align:center;
}
.td_2{
	word-wrap:break-word;
	white-space:normal;
	border-bottom:1 solid #000;
	border-left:none;
	border-top:none;
	border-right:1 solid #000;
	font-family:'宋体';
	text-align:center;
}
.td_3{
	word-wrap:break-word;
	white-space:normal;
	border-bottom:1 solid #000;
	border-left:none;
	border-top:none;
	border-right:none;
	font-family:'宋体';
}
</style>
</head>
<body>
<%
	StringBuffer tableStr = new StringBuffer("");//表格串
	tableStr.append("<table cellSpacing=0 cellPadding=0 border='0' align='center' width='700px' style='font-size:12px;overflow:auto;word-wrap:break-word;white-space:normal;'>");
		tableStr.append("<tr height='50'><td class='td_3' colspan='7' valign='bottom' align='center' style='font-weight:bold;font-size:16px;'>印章使用统计表<br>&nbsp;</td></tr>");
	StringBuffer catalogTr = new StringBuffer("<tr height='30'><td class='td_1' width='160px' rowspan='2'>公司/部门</td><td class='td_2' colspan='2'>公文用印</td><td class='td_2' colspan='2'>合同用印</td><td class='td_2' colspan='2'>其它用印</td></tr>");//用印类型行tr串
	StringBuffer typeTr = new StringBuffer("<tr height='30'><td width='90px' class='td_2'>电子</td><td width='90px' class='td_2'>实物</td><td class='td_2' width='90px'>电子</td><td class='td_2' width='90px'>实物</td><td class='td_2' width='90px'>电子</td><td class='td_2' width='90px'>实物</td></tr>");//用印方式行tr串
	StringBuffer bodyTr = new StringBuffer("");
	for(Bean dept : resultList){
		bodyTr.append("<tr height='30'>");
		//公司/部门名称
		bodyTr.append("<td class='td_1'>" + dept.getStr("DEPT_NAME") + "</td>");
		//公文电子用印
		bodyTr.append("<td class='td_2'>" + dept.getInt("SEAL_FOR_GW1") + "</td>");
		//公文实物用印
		bodyTr.append("<td class='td_2'>" + dept.getInt("SEAL_FOR_GW2") + "</td>");
		//合同电子用印
		bodyTr.append("<td class='td_2'>" + dept.getInt("SEAL_FOR_CT1") + "</td>");
		//合同实物用印
		bodyTr.append("<td class='td_2'>" + dept.getInt("SEAL_FOR_CT2") + "</td>");
		//其它电子用印
		bodyTr.append("<td class='td_2'>" + dept.getInt("SEAL_FOR_OTHER1") + "</td>");
		//其它实物用印
		bodyTr.append("<td class='td_2'>" + dept.getInt("SEAL_FOR_OTHER2") + "</td>");
		bodyTr.append("</tr>");
	}
	// 输出表头
	out.println(tableStr.toString());
	// 输出用印类型行
	out.println(catalogTr.toString());
	// 输出用印方式行
	out.println(typeTr.toString());
	// 输出统计结果行
	out.println(bodyTr.toString());
	out.println("</table>");
%>
</body>
</html>