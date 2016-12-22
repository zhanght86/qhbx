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
<%@ page import="com.rh.core.comm.print.PrintDataHelper" %>
<%@ page import="org.apache.commons.io.FileUtils"%>

<%
	request.setCharacterEncoding("UTF-8");

	// 手动获取一下这样在线程中就有了UserBean
	Context.getUserBean(request);
	Context.setRequest(request);
	ParamBean paramBean = RequestUtils.transParam(request);
 
	PrintDataHelper pdh = new PrintDataHelper();
    String ftlcontent =paramBean.getStr("Content");
    
    String ftlPath = Context.appStr(Context.APP.SYSPATH) + "/sy/comm/print/ftl/strAdd.ftl";
    File ftlStrAdd = new File(ftlPath);
    String strAdd = FileUtils.readFileToString(ftlStrAdd);    
    
    ftlcontent = strAdd + ftlcontent;
    
    //替换因uEditor 编辑器引起的"<> 被替换问题
    String regContent = pdh.replaceRegex(ftlcontent);
    
    //String regContent = ftlcontent.replaceAll("&quot;", "\"");
    HashMap map = PrintDataHelper.getCommData(paramBean);
	String htmlStr = FreeMarkerUtils.parseString(regContent, map);
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>通用打印</title>

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
		var print = document.getElementById("_print_");
		print.style.display = "none";
		WebBrowser.ExecWB(7, 1);
		//print.show();
		print.style.display = "";
	}
	
	// 打印
	function doPrint() {
		var print = document.getElementById("_print_");
		print.style.display = "none";
		WebBrowser.ExecWB(6, 6);
		print.style.display = "";
	}
	
	//以post的方式给后台传值
	function openPostWindow(PT_CONTENT) {
		var tempForm = document.createElement("form");
		tempForm.id = "tempForm1";
		tempForm.method = "post";
		tempForm.target = "_blank";
		tempForm.action = "SY_COMM_PRINT_TMPL.writeWordFile.do"; // 在此处设置你要跳转的url
		var hiddInput_content = document.createElement("input");
		hiddInput_content.type = "hidden";
		hiddInput_content.name = "PT_CONTENT";
		hiddInput_content.value = PT_CONTENT;
		tempForm.appendChild(hiddInput_content);
		tempForm.attachEvent("onsubmit", function() {
				});
		document.body.appendChild(tempForm);
		tempForm.fireEvent("onsubmit");
		// 将form的target设置成和windows.open()的name参数一样的值，通过浏览器自动识别实现了将内容post到新窗口中
		tempForm.submit();
		document.body.removeChild(tempForm);
	};
	// 另存
	function saveAs() {

	    //var saveHtml = document.getElementById("convert_word_table");
	    //if (null == saveHtml) {
	    //	saveHtml = document.getElementById("_content_");
	    //}
	    //openPostWindow(saveHtml.innerHTML);
		doPrint();
	}
	
	function closeWin(){
		this.window.opener = null;  
		window.close();
	}
	
	window.onload = function() {
		//添加打印模版个性化处理，防止内部表格撑破问题
		/*jQuery("table[name='print_inner_table']").each(function(i){
			var _self = this;
			jQuery(_self).find("tr:last").css({"height":jQuery(_self).parent().height() - jQuery(_self).find("tr:first").height() + 3});
		});*/
		
		var innerTabObjs = jQuery("table[name='print_inner_table']");
		innerTabObjs.each(function(n){
			var _self = this; //当前对象
			var parentHei = jQuery(_self).parent().height(); //当前对象父元素高度
			var trHeiArray = []; //当前对象内部tr高度数组
			var thisTrObjs = jQuery(_self).find("tr"); //当前对象内部tr的jQuery对象
			var countHei = 0; //当前对象内部tr的j总高度，用于计算每个tr的百分比，便于等比例拉伸
			thisTrObjs.each(function(i){ //遍历tr对象数组，获取tr总高度
				var thisCount = jQuery(this).height();
				trHeiArray[i] = thisCount; //获取tr高度
				if (thisTrObjs.length > 1 && i == 0 && n == 0) { //第一个固定行号，不需要拉伸
					parentHei - thisCount;
				} else {
					countHei += thisCount;
				}
			});
			var thisLastCount = 0; //通过百分比计算出最后tr总高度
			var trHeiArraySize = trHeiArray.length; //tr个数
			if (trHeiArraySize > 1) { //大于1个tr才有必要去做计算
				var lastTrHei = 0; //最后一个tr高度
				for (var i = 0; i < trHeiArraySize; i++) { //按百分比给每个tr重新计算高度
					if (!(i == 0 && n == 0)) { //不考虑第一个固定高度
						var percen = (trHeiArray[i]/countHei).toFixed(2); //计算百分比
						var everyHei = percen * parentHei; //通过父元素计算出要拉高的高度
						thisLastCount += everyHei; //计算通过百分比的值，便于后面计算误差
						thisTrObjs.eq(i).css({"height":everyHei}); //赋值给每个tr
						if (i == trHeiArraySize - 1) { //最后一个tr
							lastTrHei = everyHei; //记住最后一个tr高度，把误差值都加在最后一个tr上
						}
					}
				}
				var escCount = parentHei - thisLastCount + 3;
				thisTrObjs.eq(trHeiArray.length - 1).css({"height":lastTrHei + escCount});
			}
		});
		
		//去除意见中的br标签
		jQuery("div[name='remove-br-div']").find("div").each(function(){
			jQuery(this).find("br").remove();
		});
	};
</script>
</head>
<body>
	<object id="WebBrowser" width="0" height="0" classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2"></object>
	<div id="_container_" class="_center_">	
		<div id="_print_" >
			<input class="_btn_" id="_setupBtn_" type="button" value="页面设置" onclick="javascript:PageSetup();" />
			<input class="_btn_" id="_viewBtn_" type="button" value="打印预览" onclick="javascript:printView();" />
			<input class="_btn_" id="_printBtn_" type="button" value="打  印" onclick="javascript:doPrint();" />
			<!-- input class="_btn_" id="_printBtn_" type="button" value="另 存 " onclick="javascript:saveAs();" /> -->
		    <input class="_btn_" id="_closeBtn_" type="button" value="关  闭" onclick="javascript:closeWin();" />
		</div>
		<div style="margin-top:5px;margin-bottom:5px;">&nbsp;</div>
		<div id="_content_" class="_center_">
			<%
				out.println(htmlStr);
			%>
			
		</div>
	</div>
</body>
</html>