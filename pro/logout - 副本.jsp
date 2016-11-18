<%@ page import="com.rh.core.base.Context"%>
<%@ page import="com.rh.client.RHSSO"%>
<%
	final String CONTEXT_PATH = request.getContextPath();
%>

<script type="text/javascript">
	function closeWindow() {
		var browserName = navigator.appName; 
		if (browserName == "Netscape") { 
	    	window.open("","_self").close();
		}
		if (browserName == "Microsoft Internet Explorer") { 
	    	window.close(); 
	    }
	} 
<%
	String portalURL = Context.getSyConf("SY_PORTAL_URL", null);// 门户地址
	// 配置了门户地址则默认启用了单点登陆
	if (portalURL != null) {
%>	closeWindow();
<%} else {
		if("true".equals(Context.getSyConf("SY_LOGOUT_CLOSE","true"))){%>
		window.opener = null;
		window.open("","_self").close();
	<%}else{%>
		window.location.href = "<%=CONTEXT_PATH%>/index.jsp";
	<%}%>
<%}%>
</script>

