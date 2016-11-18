<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--StdListView.jsp列表页面-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.rh.core.base.*" %>
<%@ page import="com.rh.core.serv.*" %>
<%@ page import="com.rh.core.util.*" %>
<%@ page import="com.rh.core.comm.*" %>
<%@ page import="com.rh.core.org.mgr.OrgMgr"%>
<%@ page import="com.rh.oa.gw.util.GwConstant" %>
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
    Bean gwRedHead = ServDao.find(servId, dataId);
    
   String dept_name = OrgMgr.getDept(gwRedHead.get("LT_DEPT").toString()).getName();

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
			return true;
		}
		return false;	
	}
	
	//是否存在Wps
	function existWps(){
		var wpsVersion = ZotnClient.wpsVersion;
		if(wpsVersion && wpsVersion.length > 0){
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
		
		if(existWord()){
			redHead = new RedHead();
		}else if(existWps()){
			redHead = new WpsRedHead();
		}else{
			alert("请安装word或wps.");
		}	
		
		showStatus("正在生成文件正文：0%");
		
	
	    var fileID = "fileId";
	
	    var DownloadURL = getHostURL() + "/file/<%=source%>"; 
	    
	    var uploadURLData = "{SERV_ID:<%=fileServId%>,DATA_ID:<%=dataId%>,WF_NI_ID:<%=wfNiId%>,updateWfNiId:1,"
	    		+ "listener:GW,doAct:redhead,FILE_SORT:10,SOURCE_ID:<%=source%>,FILE_CAT:ZHENGWEN,"
	    		+ "handler:redhead,ITEM_CODE:<%=ZW_TYPE.ZHENG_WEN.getCode()%>,DIS_NAME:正文}";
	    var UploadURL =  "/file/<%=target%>?data=" + encodeURIComponent(uploadURLData);

	    
	    var TempDownloadURL = getHostURL() + "/file/<%=tmplId%>";
	
		var needEncrypt = false;
		//从服务器上下载到本地
		var fileName = ZotnClient.DownloadFile(DownloadURL, "demo_file.doc" ,false,false,true,false);
		var tempFileName = ZotnClient.DownloadFile(TempDownloadURL, "demo_file.doc" ,false,false,true,false);
		var downLoadFileName = "";
		try{
			redHead.createWord("c:\\ZotnDoc\\"+fileName,"c:\\ZotnDoc\\"+tempFileName,tempFileName);
			
			redHead.repleaceMarker("#fileEmergy#", fileListObj.GW_EMERGENCY__NAME);
		    showStatus("正在生成文件正文："+(Math.random() * 10 + 5).toFixed(1)+"%");
			//redHead.repleaceMarker("#fileDept#", fileListObj.GW_YEAR_CODE);
			redHead.repleaceMarker("#fileYear#", '<%=gwRedHead.get("GW_YEAR")%>');
			showStatus("正在生成文件正文："+(Math.random() * 10 + 10).toFixed(1) +"%");
			//redHead.repleaceMarker("#fileNum#", fileListObj.GW_YEAR_NUMBER);
			
			redHead.repleaceMarker("#chineseday#", "<%=chineseday%>");
			redHead.repleaceMarker("#mainTo#", "<%=dept_name%>");
			showStatus("正在生成文件正文："+(Math.random() * 10 + 20).toFixed(1)+"%");
			redHead.repleaceMarker("#day#", "<%=day%>");
			redHead.repleaceMarker("#month#", "<%=month%>");
			redHead.repleaceMarker("#year#", "<%=year%>");
			//redHead.repleaceMarker("#topicWord#", fileListObj.GW_TOPIC);
			showStatus("正在生成文件正文："+(Math.random() * 10 + 30).toFixed(1)+"%");
			redHead.repleaceMarker("#yearCode#", '<%=gwRedHead.get("GW_YEAR_CODE")%>');
			redHead.repleaceMarker("#yearNumber#", '<%=gwRedHead.get("GW_YEAR_NUMBER")%>');
			redHead.repleaceMarker("#gwCode#", '<%=gwRedHead.get("GW_CODE")%>');
			showStatus("正在生成文件正文："+(Math.random() * 10 + 40).toFixed(1)+"%");
			redHead.repleaceMarker("#title#", '<%=gwRedHead.get("LT_CONTENT")%>');
			showStatus("正在生成文件正文："+(Math.random() * 10 + 70).toFixed(1)+"%");		
			//签发时间
			redHead.repleaceMarker("#signTime#", '<%=gwRedHead.get("LT_TIME")%>');
			redHead.repleaceMarker("#postalcode#", '<%=gwRedHead.get("LT_CODE")%>');
			redHead.repleaceMarker("#adress#", '<%=gwRedHead.get("LT_ADRESS")%>');
			showStatus("正在生成文件正文："+(Math.random() * 10 + 90).toFixed(1)+"%");
			redHead.replaceText();
			downLoadFileName = redHead.downLoadFileName;
		}finally{
			redHead.clearResource();
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
</script>
<center>
	<div id=dangdang style='width:200px;height:60px;z-index: 1'>正在处理文件，请稍候……</div>
</center>


