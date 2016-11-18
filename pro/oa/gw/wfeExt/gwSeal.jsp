<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="com.rh.oa.gw.util.GwConstant" %>

<%@ include file="/sy/util/stylus/ZotnClientLib.jsp" %>
<%@ include file="/sy/base/view/inHeader.jsp"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script language="JavaScript" src="/sy/util/office/zotnClientLib_NTKO.js"></script>
<script language="JavaScript" src="/sy/util/office/fileOperator.js"></script>
<script language="JavaScript" src="/sy/util/office/functions.js"></script>
<script type="text/javascript" src="pageMask.js"></script>
</head>
<%
	// 加密正文扩展名
	String extFileName = ".gw";
	// 非加密正文扩展名
	String gdFileName = ".gd";
	
	String FileNameTif = ".tif";
	
	String gwId =  (String) request.getParameter("gwId");
	
	String servId =  (String) request.getParameter("servId");
	
	String dataId =  gwId;
	
	String source = (String) request.getParameter("source");
	
	//盖章类别
	String sealType = RequestUtils.getStr(request, "sealType");
	
	Bean serv = ServUtils.getServDef(servId);
    String fileServId = servId;
    if (!serv.isEmpty("SERV_PID")) {
    	fileServId = serv.getStr("SERV_PID");
    }
%>

<!--将Word文件转换成Sep文件的插件-->
<OBJECT ID="DocToSepCom" CLASSID="CLSID:2AAAC66F-B4C1-43F7-98FC-787FB882513F" height="0" width="0" ></OBJECT>
<!--书生提供的盖章的文件-->
<OBJECT ID="ShuShengSeal" CLASSID="CLSID:567FF69D-56E3-11D6-81D1-00E04CE60E84" height="0" width="0" ></OBJECT>
<OBJECT ID="ShuShengPakeCom" CLASSID="CLSID:729C32DB-55F0-4152-B7A7-348F2A4C94BC" height="0" width="0"></OBJECT>

<script language="javascript">

	// 临时文件的路径
	var TemporaryFilePath = "c:\\ZotnDoc\\";
	
	// 盖章类别
	var sealType = "<%=sealType%>";

	var SEAL_TYPE_CONSTANT = {
		SEP : "shusheng",
		JD : "jida"
	};

	function t() {
		//return window.returnValue = "";
	}
	
	/*
    	将Doc文件转换成Sep文件
	*/
    function convertDocToSepFile(fileName) {
        var newFileName = getFileNameWithoutExt(fileName) + ".sep";
        try {
            var rtnVal = DocToSepCom.PrintD(TemporaryFilePath + fileName ,TemporaryFilePath + newFileName);
        } catch(e) {
            throw e;
        }
        return newFileName;
    }
	
    /*
    	转换gw文件成gd文件，并返回转换后的文件名。
	*/
    function convertGwToGdFile(fileName) {
        window.status = "正在将文件脱密，请稍候……";
        var newFile = getFileNameWithoutExt(fileName) + ".gd";
        var a;
        a = ShuShengSeal.SedArchive(TemporaryFilePath+fileName,"111111",TemporaryFilePath+newFile,0,0,0,0);
        if(a!==0){
            ShuShengSeal.SedMsgBox();
            throw "转换文件失败！";
        }
        return newFile;
    }
	
    /*
   	 	取得不带扩展名的文件名
	*/
    function getFileNameWithoutExt(fileName) {
        var name = "";
        if (fileName.indexOf(".")>0) {
            name = fileName.substring(0,fileName.indexOf("."));
        } else {
            name = fileName;
        }
        return name;
    }
    
    /*
    	盖章加密(无界面有公钥)
	*/
    function sealWithShuSheng(sepFile,sPassword,nCopys,attFiles) {
      
      	var newFile = getFileNameWithoutExt(sepFile) + "<%=extFileName%>";
      	var rtn;

      	rtn = ShuShengSeal.SedEncryptEx(TemporaryFilePath + sepFile, attFiles,TemporaryFilePath + newFile , 0, sPassword, nCopys, 0, 2, TemporaryFilePath, "");

      	if (rtn != 0) {
          	ShuShengSeal.SedMsgBox();
          	throw "盖章失败！";
      	}
      	return newFile;
    }
    
    /*
    	上传文件
	*/
    function uploadFile(fileName,action){
        window.status = "正在上传文件，请稍候……";
        var UploadURL =  getHostURL() + "/file/?data={SERV_ID:<%=fileServId%>,DATA_ID:<%=dataId%>,SOURCE_ID:<%=source%>,FILE_CAT:ZHENGWEN,ITEM_CODE:<%=GwConstant.NO_ENC_ZHENGWEN%>,FILE_NAME:"+ fileName +",listener:GWFILEMANAGER}";
        ZotnClient.UploadFile(UploadURL, fileName ,false,false,false);
    }
    
    /*
    * 上传新文件
    */
    function uploadNewFile(fileName,fileType,disName){
    	window.status = "正在上传文件，请稍候……";
        UploadURL = getHostURL() + "/file/?data={SERV_ID:<%=fileServId%>,DATA_ID:<%=dataId%>,SOURCE_ID:<%=source%>,FILE_CAT:ZHENGWEN,ITEM_CODE:<%=GwConstant.ENC_ZHENGWEN%>,FILE_NAME:"+ fileName +",listener:GWFILEMANAGER}";
        ZotnClient.UploadFile(UploadURL, fileName ,false,false,false);
    }
    
    /*
    	下载正文文件
	*/
    function downloadFile(fileID,oldFileName){
        window.status = "正在下载文件，请稍候……";
        
        var DownloadURL = getHostURL() + "/file/<%=source%>"; 

        var rtnFileName = "";
        try{
            rtnFileName = ZotnClient.DownloadFile(DownloadURL, oldFileName ,false,false,true,false);
        }catch(e){
            throw e;
        }
        return rtnFileName;
    }
    
    function alertException(e) {
        if (e.message != undefined) {
            alert(e.message);
        } else {
            alert(e);
        }
        window.status = "操作失败！";
    }
    
    /*
    	处理书生整个盖章的过程
	*/
    function doSealWithShuSheng() {

        <%if(source == null || source.length() <= 0) {%>
            alert("正文不存在，无法盖章！");
            return;
        <%}%>

        try{
        	showMessage("2:开始下载正文……");
            var fileName = downloadFile('<%=source%>','test.doc');
            showMessage("3:转换DOC文件成SEP文件 ……");
            var sepFileName = convertDocToSepFile(fileName);
            showMessage("4:正在盖章 ……");
            var gwFileName = sealWithShuSheng(sepFileName,"111111",20,"");
            showMessage("5:脱密盖章的文件 ……");
            var gdFileName = convertGwToGdFile(gwFileName);
            showMessage("6:上传盖章文件 ……");
            uploadFile(gdFileName,"");
            //上传Gw文件，作为加密的正文
            uploadNewFile(gwFileName,'','');
            // 返回盖章成功消息给父窗口
            return window.returnValue = "success";
        }catch(e){
        	showMessage("盖章失败，请和管理员联系！");
            alertException(e);
         	// 返回盖章失败消息给父窗口
            return window.returnValue = "fail";
        }
    }
    
    /*
    * 打印书生文件
    */
    function doPrintShuSheng(gwId,gwFileName){
        try{
            var printType = 0;
            var rtnConfirm = vbConfirm("是否为激光打印？") ;
            
            if(rtnConfirm == '2'){
                window.status = "打印操作被取消！";
                return;
            }else if(rtnConfirm == '6'){
                printType = 1;
            }

            var fileName = downloadFile(gwId,gwFileName);
            fileName = TemporaryFilePath + fileName;

            var rtnValue = ShuShengSeal.SedPrintUI(fileName,printType,"111111");

            if(rtnValue != 0){
                throw "打印文件失败，请确认本机是否安装打印机，并且打印机工作正常。";
            }
            window.status = "打印完成";
        }catch(e){
            alertException(e);
            window.status = "打印操作失败";
        }
    }

	(function() {
		jQuery(document).ready(function() {
			showMask();
			showMessage("1:开始选择盖章类型……");

			if (sealType == SEAL_TYPE_CONSTANT.SEP) { //书生盖章
				doSealWithShuSheng();
			} else if (sealType == SEAL_TYPE_CONSTANT.JD) { //吉大盖章
				alert(sealType);
			} else {
				alert("您没有在工作流中配置对应的盖章类型！");
			}
			// 关闭提示信息框
			closeMask();
			// 关闭新窗口
			window.close();

		});
	})();
</script>
<Script language="vbscript">
    function vbConfirm(strInfo)
        '** 6 表示 yes ；7 表示 no ；2 表示 cancel
        vbConfirm = msgbox(strInfo,vbYesNoCancel + vbInformation,"提示")
    end function
</Script>