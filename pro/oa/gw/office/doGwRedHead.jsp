<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--StdListView.jsp列表页面-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.rh.core.base.*" %>
<%@ page import="com.rh.core.serv.*" %>
<%@ page import="com.rh.core.util.*" %>
<%@ page import="com.rh.core.comm.*" %>
<%@ page import="com.rh.oa.gw.util.GwConstant" %>
<%@ page  import="com.rh.oa.gw.GwRedHeadHelper" %>
<%@ page import="com.rh.oa.gw.util.ZW_TYPE"%>
<%@ page import="java.util.List" %>
<script language="JavaScript" src="/sy/util/office/redHead.js"></script>
<script language="JavaScript" src="/sy/util/office/wpsRedHead.js"></script>
<script language="JavaScript" src="/sy/util/office/zotnClientLib_NTKO.js"></script>
<script language="JavaScript" src="/sy/util/office/fileOperator.js"></script>

<%
	String urlPath = request.getContextPath();
	
%>
<script type="text/javascript">
	var FireFlyContextPath = "<%=urlPath%>";//虚拟路径
</script>
<%@ include file="/sy/util/stylus/ZotnClientLib.jsp" %>
<%
	String servId =  (String) request.getParameter("servId");
	String dataId =  (String) request.getParameter("gwId");
	String source = (String) request.getParameter("source");
	String target = (String) request.getParameter("target");
	String tmplId = (String) request.getParameter("cwTmplFileId");
	String wfNiId = (String) request.getParameter("wfNiId");
	//是否只用于预览，并不保存
	String readonly = (String) request.getParameter("readonly");
	
    String date = DateUtils.getDate();  
    String year = date.substring(0,4);
    String month = date.substring(5,7);
    if (month.startsWith("0"))
        month=month.substring(1,2);
    String day = date.substring(8,10);
    if (day.startsWith("0")){
        day=day.substring(1,2);
    }
    
    Bean serv = ServUtils.getServDef(servId);
    String fileServId = servId;
    if (!serv.isEmpty("SERV_PID")) {
    	fileServId = serv.getStr("SERV_PID");
    }
    
    //当前日期
    String chineseday= year + "年" + month + "月" + day + "日";
    
    GwRedHeadHelper gwRedHead = new GwRedHeadHelper(servId,dataId);
%>

<script language="JavaScript">
	//是否只读
	var readonly =  <%=readonly!=null?"true":"false"%>

	function showStatus(msg){
	    window.status = msg;
	}
	
	function showEndMsg(){
	    dangdang.innerHTML = "<center><font  style=\"font-size:18px; font-weight: bold; line-height:24px\" >处理文件结束,请手工调整!</font></center>";
	}
	
	function existWord(){
		
		var wordVer = ZotnClient.WordVersion;
		if(wordVer && wordVer.length > 0){
			ZotnClient.EditorWordTypes = "doc,wps";
			ZotnClient.EditorExcelTypes= "xls,et";			
			return true;
		}
		return false;
	}
	
	//是否存在Wps
	function existWps(){
	
		var wpsVersion = ZotnClient.wpsVersion;
		if(wpsVersion && wpsVersion.length > 0){
			ZotnClient.EditorWpsTypes = "doc,wps";
			return true;
		}
		return false;
	}
	
	var redHead;
	
	function insertdata(){
		var fileListObj = window.dialogArguments;
		if(!fileListObj){
			fileListObj = window.parent._redHatArgs;
		}
		
		try{
		
			if(existWord()){
				redHead = new RedHead();
			}else if(existWps()){
				redHead = new WpsRedHead();
			}else{
				alert("请安装word或wps.");
			}	
			
			showStatus("正在生成文件正文：0%");
			 //附件列表
		    var annex = "<%=gwRedHead.getFujian()%>";
		    var psMindList = "";
		    var hgMindList = "";
		    var hqMindList = "";
		    var qfMindList = "";
		
		    var fileID = "fileId";
		
		    var DownloadURL = getHostURL() + "/file/<%=source%>"; 
		    
		    var uploadURLData = "{SERV_ID:<%=fileServId%>,DATA_ID:<%=dataId%>,WF_NI_ID:<%=wfNiId%>,updateWfNiId:1,"
		    		+ "listener:GW,doAct:redhead,FILE_SORT:10,SOURCE_ID:<%=source%>,FILE_CAT:ZHENGWEN,"
		    		+ "handler:redhead,ITEM_CODE:<%=ZW_TYPE.ZHENG_WEN.getCode()%>,DIS_NAME:正文}";
		    var UploadURL =  "/file/<%=target%>?data=" + encodeURIComponent(uploadURLData);
	
		    
		    var TempDownloadURL = getHostURL() + "/file/<%=tmplId%>";
		
			var needEncrypt = false;
			//从服务器上下载到本地
			try{
			var fileName = ZotnClient.DownloadFile(DownloadURL, "demo_file.doc" ,false,false,true,false);
			}catch(e){
				alert(e);
			}
			
			var tempFileName = ZotnClient.DownloadFile(TempDownloadURL, "demo_file.doc" ,false,false,true,false);
			var downLoadFileName = "";
			redHead.createWord("c:\\ZotnDoc\\"+fileName,"c:\\ZotnDoc\\"+tempFileName,tempFileName);
			
			redHead.repleaceMarker("#fileEmergy#", fileListObj.S_EMERGENCY__NAME);
		    showStatus("正在生成文件正文："+(Math.random() * 10 + 5).toFixed(1)+"%");
			//redHead.repleaceMarker("#note#", fileListObj.S_EMERGENCY__NAME);
			redHead.repleaceMarker("#fileSecret#", /* fileListObj.GW_SECRET__NAME == "普通" ? "" :  */fileListObj.GW_SECRET__NAME);
			redHead.repleaceMarker("#companyname#", "<%=gwRedHead.getOdeptName()%>");
			//redHead.repleaceMarker("#fileDept#", fileListObj.GW_YEAR_CODE);
			redHead.repleaceMarker("#fileYear#", '<%=gwRedHead.getValue("GW_YEAR")%>');
			showStatus("正在生成文件正文："+(Math.random() * 10 + 10).toFixed(1) +"%");
			//redHead.repleaceMarker("#fileNum#", fileListObj.GW_YEAR_NUMBER);
			redHead.repleaceMarker("#fnshrName#", '<%=gwRedHead.getValue("GW_END_USER")%>');
			redHead.repleaceMarker("#mainTo#", "<%=gwRedHead.getMainTo()%>");
			redHead.repleaceBigValMarker("#annex#",annex);
			showStatus("正在生成文件正文："+(Math.random() * 10 + 20).toFixed(1)+"%");
			redHead.repleaceMarker("#dfterDept#", '<%=gwRedHead.getValue("GW_ZB_TNAME")%>');
			//redHead.repleaceMarker("#dfterDept#", '<%=gwRedHead.getTDept()%>');
			redHead.repleaceMarker("#num#", '<%=gwRedHead.getValue("GW_COPIES")%>');
			redHead.repleaceMarker("#day#", "<%=day%>");
			redHead.repleaceMarker("#month#", "<%=month%>");
			redHead.repleaceMarker("#year#", "<%=year%>");
			//redHead.repleaceMarker("#topicWord#", fileListObj.GW_TOPIC);
			showStatus("正在生成文件正文："+(Math.random() * 10 + 30).toFixed(1)+"%");
			//redHead.repleaceMarker("#printTo#", fileListObj.GW_PRINT);
			redHead.repleaceMarker("#signName#", '<%=gwRedHead.getValue("GW_SIGN_UNAME")%>');
			redHead.repleaceMarker("#fileType#", "");
			redHead.repleaceMarker("#yearCode#", '<%=gwRedHead.getValue("GW_YEAR_CODE")%>');
			redHead.repleaceMarker("#yearNumber#", '<%=gwRedHead.getValue("GW_YEAR_NUMBER")%>');
			redHead.repleaceMarker("#gwCode#", '<%=gwRedHead.getValue("GW_CODE")%>');
			showStatus("正在生成文件正文："+(Math.random() * 10 + 40).toFixed(1)+"%");
			redHead.repleaceMarker("#memo#", '<%=gwRedHead.getValue("GW_MEMO")%>');
			redHead.repleaceMarker("#page#", '<%=gwRedHead.getValue("GW_PAGE")%>');
			redHead.repleaceMarker("#sendto#", '<%=gwRedHead.getValue("GW_SEND_TO")%>');
			redHead.repleaceMarker("#title#", '<%=gwRedHead.getValue("GW_TITLE")%>');
			showStatus("正在生成文件正文："+(Math.random() * 10 + 50).toFixed(1)+"%");
			redHead.repleaceMarker("#cwDeptName#", '<%=gwRedHead.getValue("GW_CW_TNAME")%>');
			//redHead.repleaceMarker("#deptName#",  '<%=gwRedHead.getValue("S_TNAME")%>');
			redHead.repleaceMarker("#deptName#",  '<%=gwRedHead.getTDept()%>');
			redHead.repleaceMarker("#username#", '<%=gwRedHead.getValue("S_UNAME")%>');
			redHead.repleaceMarker("#topic#", '<%=gwRedHead.getValue("GW_TOPIC")%>');
			redHead.repleaceMarker("#copyTo#", "<%=gwRedHead.getCopyTo()%>");
			showStatus("正在生成文件正文："+(Math.random() * 10 + 60).toFixed(1)+"%");
			redHead.repleaceMarker("#codeTime#", '<%=gwRedHead.getValue("GW_CODE_TIME")%>');
			redHead.repleaceMarker("#dfterName#", '<%=gwRedHead.getValue("GW_CONTACT")%>');
			redHead.repleaceMarker("#huiyitime#", '<%=gwRedHead.getValue("GW_SEND_TO")%>');
			redHead.repleaceMarker("#cosignTo#", '<%=gwRedHead.getValue("GW_COSIGN_TO")%>');
			redHead.repleaceMarker("#copyupTo#", '<%=gwRedHead.getValue("GW_COPYUP_TO")%>');
			redHead.repleaceMarker("#signInfo#", "signInfo");
			showStatus("正在生成文件正文："+(Math.random() * 10 + 70).toFixed(1)+"%");
			redHead.repleaceMarker("#beginTime#", '<%=gwRedHead.getValue("GW_BEGIN_TIME")%>');
			redHead.repleaceMarker("#beginTimeChinese#", '<%=gwRedHead.getValue("GW_BEGIN_TIME")%>');
			redHead.repleaceMarker("#currentDateChinese#", '<%=gwRedHead.getValue("GW_BEGIN_TIME")%>');
			redHead.repleaceMarker("#fromCode#", "fromCode");
			redHead.repleaceMarker("#userPhone#", '<%=gwRedHead.getValue("GW_CONTACT_PHONE")%>');
			
			//签发时间
			redHead.repleaceMarker("#signTime#", '<%=gwRedHead.getSignTime()%>');
			
			showStatus("正在生成文件正文："+(Math.random() * 10 + 80).toFixed(1)+"%");
			redHead.repleaceMarker("#psMindList#",psMindList);
			redHead.repleaceMarker("#hgMindList#",hgMindList);
			redHead.repleaceMarker("#hqMindList#",hqMindList);
			redHead.repleaceMarker("#qfMindList#",qfMindList);
			showStatus("正在生成文件正文："+(Math.random() * 10 + 90).toFixed(1)+"%");
			redHead.replaceText();
			if (redHead.tempDocObj) {
				<%--判断是否为部门发文，因为目前只有部门发文有联合发文--%>
				//if (<%=gwRedHead.getValue("TMPL_CODE").equals("OA_GW_TMPL_FW_BM")%>) {
				var headDeptName = "（<%=gwRedHead.getTDept()%>）";
				var bookmarks = redHead.tempDocObj.Bookmarks;
				if (bookmarks.Exists("mulDeptName")) {
					var mulDeptName = redHead.tempDocObj.Bookmarks("mulDeptName").Range;
					if (mulDeptName != null) {
						mulDeptName.InsertBefore(drowHeadRangeTable(redHead.tempDocObj, mulDeptName, headDeptName));
					}
				}
				//}
				var isLhFw = "<%=gwRedHead.getDeptNames()%>";
				if (isLhFw.length > 0 && isLhFw.split(",").length > 1) { //联合发文
					if(bookmarks.Exists("mulDeptSeal")) {
						var mulDeptSeal = redHead.tempDocObj.Bookmarks("mulDeptSeal").Range;
						if (mulDeptSeal != null) {
							//mulDeptSeal.Paste(drowRangeTable(redHead.tempDocObj, mulDeptSeal, isLhFw, "<%=chineseday %>"));
							mulDeptSeal.InsertBefore(drowRangeTable(redHead.tempDocObj, mulDeptSeal, isLhFw, "<%=chineseday %>"));
						}
					}
					redHead.repleaceMarker("#chineseday#", "");
					redHead.repleaceMarker("#deptName2#",  "");
				} else {
					redHead.repleaceMarker("#deptName2#",  '<%=gwRedHead.getTDept()%>');
					redHead.repleaceMarker("#chineseday#", "<%=chineseday%>");
				}
			}
			downLoadFileName = redHead.downLoadFileName;
		}finally{
			redHead.clearResource();
			redHead = null;
		}
	 	
	 	showStatus("完成");
	 	if(readonly){
	 		var flag = readOfficeFile(downLoadFileName,"file:\\c:/ZotnDoc/"+downLoadFileName,false);
			if(!(flag == 'undefined')) {
				//window.parent.closeGwRedHead({readonly:true});
			}
	 	}else{
			var flag = zotnClientNTKO.openRedHeadFile("file:\\c:/ZotnDoc/"+downLoadFileName,UploadURL,downLoadFileName);

			if(!(flag == 'undefined')) {
				//window.parent.closeGwRedHead();
			}
	 	}
	 	

	}
	setTimeout("insertdata()", 50);
	
	//关闭文件操作
	function officeClientCloseCallBack(){
		window.parent.closeGwRedHead();
	}
	
	//画出书签表格
	function drowRangeTable(obj, mulDeptSeal, dataString, date){
		var table = new Array();
		var dataArray = dataString.split(",");
		var trCount = Math.ceil(dataArray.length / 3) * 4 - 2; //获取表格行数，*4是为了添加对应时间，-2是去除最后来两行
		var tableform=obj.Tables.Add(mulDeptSeal, trCount, 3) //trCount行3列的表格
		
		for (var i = 1; i <= dataArray.length; i++) {
			var row = Math.ceil(i / 3); //行
			var deptCell = 4 * row - 3; //显示部门单元格
			var dateCell = 4 * row - 2; //显示日期单元格
			if (i % 3 == 1) { //第一列，则添加到word表格最后一列（因为是右对齐）
				tableform.Cell(deptCell, 3).range.Text = dataArray[i-1];
				tableform.Cell(dateCell, 3).range.Text = date;
			} else if (i % 3 == 2) { //第二列
				tableform.Cell(deptCell, 2).range.Text = dataArray[i-1];
				tableform.Cell(dateCell, 2).range.Text = date;
			} else if (i % 3 == 0){ //第三列
				tableform.Cell(deptCell, 1).range.Text = dataArray[i-1];
				tableform.Cell(dateCell, 1).range.Text = date;
			}
		}
		tableform.range.ParagraphFormat.Alignment = 1; //居中对齐
	}
	
	//画出头部书签表格
	function drowHeadRangeTable(obj, mulDeptName, dataString){
		var table = new Array();
		var dataArray = dataString.split("\\r\\n");
		var trCount = dataArray.length; //获取表格行数，*2是为了添加对应时间
		var tableform=obj.Tables.Add(mulDeptName, trCount, 1) //trCount行3列的表格
		
		for (var i = 1; i <= dataArray.length; i++) {
			tableform.Cell(i, 1).range.Text = dataArray[i-1];
		}
		tableform.range.ParagraphFormat.Alignment = 1; //居中对齐
	}
</script>
<center>
	<div id=dangdang style='width:200px;height:60px;z-index: 1'>正在处理文件，请稍候……</div>
</center>


