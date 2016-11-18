/**
 * 打开吉大正源印章系统的盖章文件
 * @param fileId 文件ID
 */
function openJdSealFile(fileId) {
	//因为吉大正元的盖章文件只有OA有，因此取OA系统地址
	var oaHost = System.getVar("@C_OA_HOST_ADDR@");
	if(System.getVar("@C_DEVICE_TYPE@") == "DESKTOP"){
		window.open(oaHost + "/oa/zh/showJdSealFile.jsp?fileId=" + fileId);
	} else { // 移动设备
		//装载打开文件的js
		Load.scriptJS("/sy/util/office/zotnClientLib_NTKO.js");
		// 进入OA系统
		var url = oaHost + "/file/" + fileId;
		RHFile.readFile(url, "read.pdf");				
	}
}