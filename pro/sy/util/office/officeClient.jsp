<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.lang.RuntimeException" %>
<%@ page import="com.rh.core.base.*" %>
<%@ page import="com.rh.core.base.Context" %>
<%@ page import="com.rh.core.org.*" %>
<%@ page import="com.rh.core.util.RequestUtils" %>
<%@ page import="com.rh.core.util.Constant" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>文件</title>

<!-- office client -->
<script language="javascript" src="/sy/util/office/officeBase.js"></script>
<script language="javascript" src="/sy/util/office/officeOpen.js"></script>
<script type="text/javascript" src="/sy/util/office/ntkoocx.js"></script>
<script type="text/javascript" src="/sy/util/office/fileOperator.js"></script>
<%
	request.setCharacterEncoding("UTF-8");
	String fileAction = RequestUtils.get(request,"fileAction","");
	String fileName = RequestUtils.get(request,"fileName","");
	fileName = fileName.toLowerCase();

	//是否出现显示痕迹/保留痕迹按钮
	boolean showMark = true;
	if((fileName.endsWith(".xlsx")) ||(fileName.endsWith(".xls")) || (fileName.endsWith(".et"))){
		showMark = false;
	}

	//是否显示保存按钮
	boolean ifShowSaveBtn = false;
	if(fileAction.indexOf(";edit;") >= 0){
		String showSaveBtn = (String) request.getParameter("showSaveBtn");
		if(showSaveBtn == null || showSaveBtn.equals("true")){
			ifShowSaveBtn = true;
		}
	}
	
	//保留文件的修改痕迹并允许用户查看，显示“显示痕迹”和“隐藏痕迹”按钮
	boolean showRevision = RequestUtils.get(request, "showRevision", false);
	String downLoadUrl = RequestUtils.get(request, "downLoadUrl", "");
	boolean refreshParent = RequestUtils.get(request, "callback", false);
	boolean hiddenButton = RequestUtils.get(request, "hiddenButton", false);
	String upLoadUrl = RequestUtils.get(request, "upLoadUrl", "");
	
	// 二维码
	String qrCode = RequestUtils.get(request, "qrCode", null);
	
	// 插入二维码
	boolean isAddQrCode = false; 
	if (qrCode != null && qrCode.length() >0) {
		isAddQrCode = true;
	}
	// 印章ID
	String sealID = RequestUtils.get(request, "sealID", "");
	// 印章在EKey中的索引号
	int sealIndex = Integer.parseInt(RequestUtils.get(request, "sealIndex", "-1"));
	// eKey序列号
	String eKeySN = RequestUtils.get(request, "eKeySN", "");
	// 打印份数
	int printNum = Integer.parseInt(RequestUtils.get(request, "printNum", "0"));
	// 文件ID
	String fileId = RequestUtils.get(request, "fileId", "");
	// 数据ID
	String dataId = RequestUtils.get(request, "dataId", "");
	// 服务ID
	String servId = RequestUtils.get(request, "servId", "");
	// 是否加盖骑缝章
	boolean isQfz = RequestUtils.get(request, "isQfz", false);
	// 正文转pdf
	boolean isTransPdf = RequestUtils.get(request, "isTransPdf", false);
	UserBean userBean = Context.getUserBean(request);
	String userName = "unknow";
	String userLoginName = "unknow";
	if (null != userBean) {
		userName = userBean.getName();
		userLoginName = userBean.getLoginName();
	} else {
%>
	<script type="text/javascript">
		alert("没有获取到当前登录用户信息，请重新登录！");
		window.close();
	</script>
<% 		
	}
%>
</head>
<body style="margin:0px 0px 0px 0px;overflow-y:hidden;">
<script>
	var isAddQrCode = "<%=isAddQrCode%>" == "true"; 

	var _menuItems = new Array();
	var _menuIndex = 0;
<%
	if(showMark && fileAction.indexOf(";showRevision;") >= 0){
%>	
	_menuItems[_menuIndex++] = {"name":" 隐藏痕迹 ","id":"1"};
	_menuItems[_menuIndex++] = {"name":" 显示痕迹 ","id":"2"};
<%
	}
	
	if(ifShowSaveBtn){
%>	
	_menuItems[_menuIndex++] = {"name":"　保　存　","id":"3"};
	_menuItems[_menuIndex++] = {"name":"　保存并关闭　","id":"4"};
<%	
	}
%>
//	_menuItems[_menuIndex++] = {"name":"　调试信息　","id":"5"};
<%
	if(fileAction.indexOf(";seal;") >= 0){
%>	
	_menuItems[_menuIndex++] = {"name":"盖　章","id":"6"};
<%
	}
%>
<%
	if(fileAction.indexOf(";bnPrint;") >= 0){
%>	
	_menuItems[_menuIndex++] = {"name":"打　印","id":"7"};
<%
	}
%>
</script>
<%@ include file="include_NTKO.jsp"%>
	<input type="hidden" id='userName' value="<%=userName%>" />
	<input type="hidden" id='userLoginName' value="<%=userLoginName%>" />
	<input type="hidden" id='sealIndex' value="<%=sealIndex%>" />
	<input type="hidden" id='sealID' value="<%=sealID%>" />
	<input type="hidden" id='fileId' value="<%=fileId%>" />
	<input type="hidden" id='dataId' value="<%=dataId%>" />
	<input type="hidden" id='servId' value="<%=servId%>" />
	<input type="hidden" id='eKeySN' value="<%=eKeySN%>" />
	<input type="hidden" id='isQfz' value="<%=isQfz%>" />
	<input type="hidden" id='fileAction' value="<%=fileAction%>" />
	<input type="hidden" id='downLoadUrl' value="<%=downLoadUrl%>" />
	<input type="hidden" id="upLoadUrl" value="<%=upLoadUrl%>" />
	<input type="hidden" id='fileName' value="<%=fileName%>" />
	<input type="hidden" id="bookmark" value="<%=RequestUtils.get(request,"bookmark","")%>">
	<input type="hidden" id="displayRev" value="<%=Context.getSyConf(Constant.CONF_OFFICE_DISPLAY_REVISION,"true")%>">
</body>
<script language="javascript">
var closeFlag = false; 
var _open_file_ok = false; //是否正常打开文件
var stopwatch = null; //计时器

/**
*	点击关闭按钮
*/
function closeWindow(){
	beforeOfficeFile();
	window.close();
}

/**
* 关闭窗口之前先保存文件
**/
function beforeOfficeFile(){
	if(!_open_file_ok){
		return;
	}

	if(_officeParam.edit && !isAddQrCode){
		var d = new Date();
		//当前时间与最后一次保存的时间间隔相差超过5秒且有文件打开,则保存文件。
		if((d.getTime() - _saveTime) > 5000 && TANGER_OCX_bDocOpen){
			if(confirm("确定要保存文件吗?")){
				saveOfficeFile();
			}
		}
	}
	
	//修正Wps的bug
	if(_officeParam.currentApp == "wps" 
			&& ( _officeParam.extName == ".docx"
				|| _officeParam.extName == ".doc"
				|| _officeParam.extName == ".wps"
				)
	){
		try{
			TANGER_OCX.ActiveDocument.close();
		}catch(e){
			
		}
	}
	
	TANGER_OCX_OBJ.close();
	closeFlag = true;
	if ( opener ) {
		var objType = typeof (opener.officeClientCloseCallBack) ;
		if(objType == "function" || objType == "object"){
			opener.officeClientCloseCallBack();//关闭窗口回调刷新父页面函数
		}		
	}
}

<%
if( fileAction.indexOf(";edit;") >= 0 && ifShowSaveBtn && !isAddQrCode){
%>
	setTimeout(saveOfficeFile,10 * 60 * 1000);
<%
}
%>

/**
* 点击保存按钮
*/
function clickSaveBtn(){
	if(!TANGER_OCX_bDocOpen){
		alert("没有打开文件");
		return;
	}
	saveOfficeFile();
}

function initPageParam(){
	if(document.readyState != "complete"){
		return;
	}
	stopwatch = new Stopwatch(); //计时开始
	stopwatch.start();
	openFile(); //打开文件
	initOverEvent();
}

// 页面初始化完成事件
function initOverEvent() {
	if (isAddQrCode) {
		try {
			addTwoDimCodeToHeader(parent._left_, parent._top_, parent._relative_, parent._scale_);	
		} catch(e) {
			alert("插入二维码错误," + e);
			return;
		}
		parent.setAddedQrCode();
	}
}

/**字符串对象增加方法：是否以指定字符串结束**/
String.prototype.endWith=function(str){ 
	if(str==null||str==""||this.length==0||str.length>this.length) 
	  return false; 
	if(this.substring(this.length-str.length)==str) 
	  return true; 
	else 
	  return false; 
	return true; 
} 

/**字符串对象增加方法：是否以指定字符串开始**/
String.prototype.startWith=function(str){ 
	if(str==null||str==""||this.length==0||str.length>this.length) 
	  return false; 
	if(this.substr(0,str.length)==str) 
	  return true; 
	else 
	  return false; 
	return true; 
}

/**
* 为指定DOM对象绑定事件及回调方法 
* domObj：DOM对象
*  event: 事件名称
*  callback: 回调事件
**/
function addDomEvent(domObj,event,callback){
	if(!domObj){
		return;
	}

	if (domObj.attachEvent) {
		if(event.startWith("on")){
			domObj.attachEvent(event, callback); 
		}else{
			domObj.attachEvent("on" + event,callback);
		}
	} else if (domObj.addEventListener){
		if(event.startWith("on") && event.length > 2){
			domObj.addEventListener(event.substr(2), callback, false);
		}else{
			domObj.addEventListener(event, callback, false);
		}
	}
}

//调用初始化方法
addDomEvent(document,'onreadystatechange', initPageParam);

//增加窗口关闭事件
addDomEvent(window,"onbeforeunload",function() {
	if(!closeFlag){
　　	closeWindow();
	}
});

// 在页眉插入二维码
function addTwoDimCodeToHeader(left, top, relative, scale) {
	
	var url = "/file?act=qrCode&value=" + encodeURIComponent("<%=qrCode%>") + "&size=150";
	
	if (TANGER_OCX_bDocOpen) {
		try {
			var ActiveDocument = TANGER_OCX_OBJ.ActiveDocument;
			var App = ActiveDocument.Application; 
	
			// 9：页眉；10：页脚
			ActiveDocument.ActiveWindow.ActivePane.View.SeekView = 9; 
			var left = parseInt(left);
			var top = parseInt(top);
			var relative = parseInt(relative);
			var scale = parseInt(scale);
			TANGER_OCX_OBJ.AddPicFromUrl(url, true, left, top, relative, scale); 
			App.Selection.ParagraphFormat.Alignment = 3;
			ActiveDocument.ActiveWindow.ActivePane.View.SeekView = 0;//退出页脚视图 
			
			saveOfficeFile();
		} catch(e) {
			throw Error(e);
		}
	}
}

function saveToPdf(){
	if(TANGER_OCX_bDocOpen){ //文件是否已打开
		TANGER_OCX_OBJ.PublishAsPDFToURL("<%=upLoadUrl%>", "EDITFILE", "", "<%=fileName%>", "actForm", "", true, true, true, "", false, false);
	}
}

</script>
<script language="JScript" for="TANGER_OCX" event="OnCustomButtonOnMenuCmd(btnPos,btnCaption,btnCmdid)">
	//alert("btnPos=" + btnPos + ",btnCaption=" + btnCaption+ ",btnCmdid=" + btnCmdid);
	if(1 == btnCmdid){　//隐藏痕迹
		showOrHiddenMark(false);
	}else if(2 == btnCmdid){
		showOrHiddenMark(true);
	}else if(3 == btnCmdid){
		clickSaveBtn();
	}else if(4 == btnCmdid){ //保存并关闭
		clickSaveBtn();
		window.close();
	}else if(5 == btnCmdid){
		var _msg_out = document.getElementById("_msg_out");
		alert(_msg_out.innerHTML);
	}else if(6 == btnCmdid){//盖章按钮
		openAndSealOfficeFile();
		//try{
			//var selLeft=TANGER_OCX_OBJ.ActiveDocument.Application.Selection.Information(5);
			//var selTop=TANGER_OCX_OBJ.ActiveDocument.Application.Selection.Information(6);
		//	TANGER_OCX_OBJ.AddSecSignFromEKey("<%=userLoginName%>",0,0,1,2,false,true,false,false,null,<%=sealIndex%10%>,false,false);
		//}catch(e){
		//	alert(e.message);
		//}
	}else if(7 == btnCmdid){//打印按钮
		openAndPrintFile(<%=printNum%>,"<%=fileId%>","<%=dataId%>","<%=servId%>");
	}else if(9 == btnCmdid){
		closeWindow();
	}
</script>
<script language="JScript" for="TANGER_OCX" event="OnDocumentOpened(file,doc)">
	//如果文件正常打开则设置标志值为1
	_open_file_ok = true;
	try{
		logOptTime("文件装载完成");
		<%if(isTransPdf){%>
		//如果是转pdf操作，则在文件打开1秒后，开始转pdf
		setTimeout(saveToPdf, 1000);
		<%}%>
	}catch(e){
		//alert(e.message);
	}
</script>
<script language="javascript">
<%
	if(fileAction.indexOf(";seal;") >= 0){
%>	
	//加载印章管理控件，读取ekey信息
	document.write('<object id="ntkosignctl" classid="clsid:97D0031E-4C58-4bc7-A9BA-872D5D572896"    ');
	document.write('codebase="/sy/util/office/ntkosigntoolv3.cab#version=4,0,0,0" width=450 height=20>   ');
	//document.write('<param name="BackColor" value="14402205">   ');
	//document.write('<param name="ForeColor" value="14402205">   ');
	document.write('<param name="IsShowStatus" value="-1">   ');
	document.write('<param name="EkeyType" value="1">   ');
	document.write('<SPAN STYLE="color:red">不能装载印章管理控件。请在检查浏览器的选项中检查浏览器的安全设置。</SPAN>   ');
	document.write('</object>   ');
<%
	}
%>
</script>
<div id="_msg_out"></div>
</html>