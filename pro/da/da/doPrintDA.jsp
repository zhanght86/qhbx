<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.lang.*"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.FileWriter"%>
<%@ page import="java.io.IOException"%>
<%@ page import="javax.swing.JFileChooser"%>
<%@ page import="javax.swing.filechooser.FileNameExtensionFilter"%>
<%@ page import="com.rh.core.serv.*" %>
<%@ page import="com.rh.core.base.*" %>
<%@ page import="com.rh.core.util.freemarker.*" %>
<%@ page import="com.rh.core.comm.mind.*" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.rh.core.util.Constant" %>
<%@ page import="com.rh.core.util.RequestUtils" %>
<%@ page import="com.rh.da.print.DaPrintDataHelper" %>
<%@ include file="/sy/base/view/inHeader.jsp"%>
<%
	request.setCharacterEncoding("UTF-8");

	// 手动获取一下这样在线程中就有了UserBean
	Context.getUserBean(request);
	Context.setRequest(request);
	ParamBean paramBean = RequestUtils.transParam(request);
 
	DaPrintDataHelper pdh = new DaPrintDataHelper();
	
	OutBean outBean = pdh.getPrintData(paramBean);
	
	String htmlStr = outBean.getStr("htmlStr");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>打印</title>
<link rel="stylesheet" type="text/css" href="/sy/print/style.css" />
<style type="text/css">
	html, body {
		margin:0 0;
		padding:0 0 20px 0; 
	}
	
	._center_ {
		margin:0 auto;
	}

	#_container_ {		
		width:100%;
		height:100%;
	}
	
	#_print_ {
		margin-top:5px;
		margin-bottom:5px;
	}
	
	._btn_ {
		width:80px;
		height:25px;
		margin-left:5px;
	}
	
	#_content_ p {
	margin-top: 5px;
	}
</style>
<script type="text/javascript" src="/sy/base/frame/jquery-1.8.2.min.js"></script> 
<script type="text/javascript">
	/**
	 * WebBrowser.ExecWB(1, 1)  打开 
	 * WebBrowser.ExecWB(2, 1)  关闭现在所有的IE窗口，并打开一个新窗口 
	 * WebBrowser.ExecWB(4, 1)  保存网页 
	 * WebBrowser.ExecWB(6, 1)  打印 
	 * WebBrowser.ExecWB(7, 1)  打印预览 
	 * WebBrowser.ExecWB(8, 1)  打印页面设置 
	 * WebBrowser.ExecWB(10, 1) 查看页面属性 
	 * WebBrowser.ExecWB(15, 1) 好像是撤销，有待确认 
	 * WebBrowser.ExecWB(17, 1) 全选 
	 * WebBrowser.ExecWB(22, 1) 刷新 
	 * WebBrowser.ExecWB(45, 1) 关闭窗体无提示  
	*/

	// 打印设置
	function PageSetup() {
		WebBrowser.ExecWB(8, 1, 0, 2);
	}

	// 打印预览
	function printView() {
			var print = $("#_print_");
			print.hide();
			WebBrowser.ExecWB(7, 1);
			print.show();
	}
	
	// 打印
	function doPrint() {
		var print = $("#_print_");
		print.hide();
		WebBrowser.ExecWB(6, 6);
		print.show();
	}
	//以post的方式给后台传值
	function openPostWindow(PT_CONTENT) {
		var tempForm = document.createElement("form");
		tempForm.id = "tempForm1";
		tempForm.method = "post";
		tempForm.target = "_blank";
		tempForm.action = "DA_PRINT_SAVEAS.writeWordFile.do"; // 在此处设置你要跳转的url
		var hiddInput_content = document.createElement("input");
		hiddInput_content.type = "hidden";
		hiddInput_content.name = "printContent";
		hiddInput_content.value = PT_CONTENT;
		tempForm.appendChild(hiddInput_content);
		tempForm.attachEvent("onsubmit", function() {
				});
		document.body.appendChild(tempForm);
		tempForm.fireEvent("onsubmit");
		// 将form的target设置成和windows.open()的name参数一样的值，通过浏览器自动识别实现了将内容post到新窗口中
		tempForm.submit();
		document.body.removeChild(tempForm);
		//location.reload();
	};
	// 另存
	function saveAs() {
	    var saveHtml = jQuery("#_content_").html();
	    if(saveHtml.indexOf("<textarea") > 0){
	    	saveHtml = saveHtml.replace(jQuery("#juanshuoming")[0]["outerHTML"] , jQuery("#juanshuoming").html());
	    	saveHtml = saveHtml.replace(jQuery("#jiancharen")[0]["outerHTML"] , jQuery("#jiancharen").attr("value"));
	    	saveHtml = saveHtml.replace(jQuery("#zhengliren")[0]["outerHTML"] , jQuery("#zhengliren").attr("value"));
	    	saveHtml = saveHtml.replace(jQuery("#shijian")[0]["outerHTML"] , jQuery("#shijian").attr("value"));
	    }
	    openPostWindow(saveHtml);
	}
</script>
</head>
<body>
	<object id="WebBrowser" width="0" height="0" classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2"></object>
	<div id="_container_" class="_center_">	
		<div id="_print_">
			<input class="_btn_" id="_setupBtn_" type="button" value="页面设置" onclick="javascript:PageSetup();" />
			<input class="_btn_" id="_viewBtn_" type="button" value="打印预览" onclick="javascript:printView();" />
			<input class="_btn_" id="_printBtn_" type="button" value="打  印" onclick="javascript:doPrint();" />
			<input class="_btn_" id="_printBtn_" type="button" value="另 存 " onclick="javascript:saveAs();" />
		    <input class="_btn_" id="_closeBtn_" type="button" value="关  闭" onclick="window.close();" />
		</div>

		<div id="_content_" class="_center_">
			<%
				out.println(htmlStr);
			%>
			
		</div>
	</div>
</body>
</html>

