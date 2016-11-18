<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/sy/base/view/inHeader.jsp"%>
<%
	request.setCharacterEncoding("UTF-8");

	// 格式合同模板文件
	String file = request.getParameter("_FILE_");
	
	// 数据主键
	String id = request.getParameter("_ID_");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>格式合同编辑</title>
<style type="text/css">
html,body {
	margin: 0 auto;
	width: 100%;
	height: 100%;
}

#content {
	width: 100%;
	height: 100%;
}
</style>
<script type="text/javascript" src="<%=Context.getSyConf("SY_XDOC_URL", "")%>/fpd.js"></script>
</head>
<body>
	<div id="content"></div>
	<script type="text/javascript">
		var _fpdDataChange = false;
		function onFpdDataChange() {
			_fpdDataChange = true;
		}
	
		var ctId = "<%=id%>";
		if (ctId == "null") {
			if(confirm("您还没有保存合同，请回到上一个页面保存合同！\n即将关闭当前页面...")) {
				window.close();
			}
		}
		
		if (ctId != "null") {
			window.onbeforeunload = function(){
				if (_fpdDataChange) {
					if(confirm("确定要保存吗？")) {
						save();
					}
				}
			};
		}
	
		setXDocServer("<%=Context.getSyConf("SY_XDOC_URL", "")%>");
		createXFpd("<%=file%>", "content");
		
		// 该方法会被自动调用
		function onFpdOpen() {
			getFpd().setOpenEnable(false);
			getFpd().setPrintEnable(false);
			getFpd().setFullEnable(false);
			getFpd().setAction("js:save()");
			
			if (ctId && ctId.length > 0) {
				var contractData = FireFly.byId("LW_CONTRACT", ctId);
				// 合同数据初始化
				getFpd().setXmlData(contractData["CT_XML"]);
			}
		}

		// 保存格式合同内容
		function save() {
			if (ctId && ctId.length > 0) {
				var xmlData = encodeURIComponent(getFpd().getXmlData());
				
				var data = {"_PK_":ctId, "CT_XML":xmlData};
				var retData = FireFly.cardModify("LW_CONTRACT", data);
				tip = retData[UIConst.RTN_MSG];
		        if (tip.indexOf(UIConst.RTN_OK) == 0 || tip.indexOf("<%=Context.getSyMsg("SY_SAVE_NOCHANGE")%>") >= 0) {
			    	_fpdDataChange = false;
			    	alert("合同数据保存成功!");
		        } else if(tip.indexOf(UIConst.RTN_ERR) == 0) {
		        	alert("合同数据保存失败!");
		        }
			} else {
				alert("请先保存合同数据，然后再编辑合同");	
			}
		}
	</script>
</body>
</html>