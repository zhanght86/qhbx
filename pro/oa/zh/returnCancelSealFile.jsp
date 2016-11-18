<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.rh.core.util.RequestUtils"%>
<%@ page import="com.rh.core.serv.ParamBean"%>
<%@ page import="com.rh.oa.zh.seal.SealMgr"%>
<%@ page import="com.rh.core.base.Context"%>
<%@ page import="com.rh.core.org.UserBean"%>

<%
	request.setCharacterEncoding("UTF-8");
	String urlPath = request.getContextPath();
	Context.cleanThreadData();
	//在线程上下文中加入UserBean
	UserBean userBean = Context.getUserBean(request);
	Context.setRequest(request);
%>
<script type="text/javascript"
	src="<%=urlPath %>/sy/base/frame/jquery-1.8.2.min.js"></script>
<%
	ParamBean paramBean = RequestUtils.transParam(request);
	paramBean.set("cancelSeal", 2);
	String errorMsg = null;

	try{
		SealMgr.receiveSealFile(paramBean);
	}catch(Exception e){
	    errorMsg = e.getMessage();
	}

	if(errorMsg == null){   //操作正常
%>
<center>
	<P>取消盖章完成！</P>
	<input type="button" value="返回" onclick="javascript:refreshMindTab()"></input>
	<script>
		var _isClosing = false;
		function refreshMindTab() {
			try {
				if (typeof (window.opener.callbackAfterSeal) != "undefined") {
					window.opener.callbackAfterSeal();
				}
			} catch (e) {
				if (e.stack) {
					console.error(e.stack);
				}
			}
			if (!_isClosing) {
				_isClosing = true;
				window.close();
			}
		}

		jQuery(window).unload(function() {
			if (!_isClosing) {
				_isClosing = true;
				refreshMindTab();
			}
		});
	</script>
</center>
<%
	}else{   //操作出现异常
%>
<center>
	<P>
		取消盖章错误：<span><%=errorMsg%></span>
	</P>
</center>
<%
	}
%>