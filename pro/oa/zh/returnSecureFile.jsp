<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.rh.core.util.RequestUtils" %>
<%@ page import="com.rh.core.serv.ParamBean" %>
<%@ page import="com.rh.oa.zh.seal.SealMgr" %>
<%@ page import="com.rh.core.base.Context" %>
<%
	request.setCharacterEncoding("UTF-8");
	String urlPath = request.getContextPath();
	Context.setRequest(request);
%>
<script type="text/javascript" src="<%=urlPath %>/sy/base/frame/jquery-1.8.2.min.js"></script> 
<%
	ParamBean paramBean = RequestUtils.transParam(request);
	
	String errorMsg = null;

//	try{
//		SealMgr.receiveSealFile(paramBean);
//	}catch(Exception e){
//	    errorMsg = e.getMessage();
//	}

//	if(errorMsg == null){   //操作正常
%>
<script>
	jQuery(document).ready(function(){
		try {
			if (typeof (window.parent.callbackAfterConvertDoc != "undefined")) {
				window.parent.callbackAfterConvertDoc('<%=paramBean.getStr("wyh")%>'
						,'<%=paramBean.getStr("retVal")%>', <%=paramBean.getBoolean("ifRefresh")%>);
			}
		} catch (e) {
			if (e.stack) {
				console.error(e.stack);
			}
		}

		//window.close();
	});
</script>
<%
//	}
%>