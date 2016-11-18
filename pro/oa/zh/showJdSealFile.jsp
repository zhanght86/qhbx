<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.rh.core.util.RequestUtils" %>
<%@ page import="com.rh.core.serv.ParamBean" %>
<%@ page import="com.rh.oa.zh.seal.SealMgr" %>
<%@ page import="com.rh.core.base.Context" %>
<%@ page import="com.rh.core.serv.OutBean" %>
<%@ page import="com.rh.core.serv.ServMgr" %>
<%@ page import="com.rh.core.org.UserBean" %>
<%
request.setCharacterEncoding("UTF-8");
Context.cleanThreadData();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>打开印章系统文件</title>
    <%@ include file= "/sy/base/view/inHeader.jsp" %>
</head>
<body  class="bodyBack bodyBackPad stdCardView">
<%
	if (request.getQueryString() != null) {
		  request.getSession().setAttribute("GOTO_URL", request.getRequestURI().toString() + "?" + request.getQueryString());
	} else {
		  request.getSession().setAttribute("GOTO_URL", request.getRequestURI().toString());
	}
	// 如果没有登录则导向首页去登录
	if(userBean == null) {
		 String loginUrl = Context.getSyConf("SY_LOGIN_URL","/");
		 RequestUtils.sendDisp(request, response, loginUrl);
		 return;
	}

    String fileId = request.getParameter("fileId");
    if(fileId== null){
        out.println("Error:fileId 不能为null。");
    }
    
    ParamBean param = new ParamBean();
    param.set("fileId", fileId);
    
    OutBean outBean = ServMgr.act("OA_GW_SEAL_FILE","getViewSealFileInfo", param);
    
    if (outBean.isOk() || outBean.isNotEmpty("URL")) {
        RequestUtils.sendDir(response, outBean.getStr("URL"));
    } else {
        out.println("Error:");
        out.println(outBean.getMsg());
    }
%>
</body>
</html>