<%@ page import="com.rh.core.base.Context"%>
<%
String switchum = Context.getSyConf("OA_UM_SWITCH", "2");
System.out.println("switchum = "+switchum);
if(switchum.equals("1")){ %>
<script type="text/javascript">
window.location.href = "/login_um.jsp";
</script>
<%} else {%>
<script type="text/javascript">
window.location.href = "/login_sso.jsp";
</script>
<%}%>