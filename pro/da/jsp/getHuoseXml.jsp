<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.*"%>
<%@ page import="java.lang.*"%>
<%@ page import="com.rh.core.base.Context" %>
<%@ page import="com.rh.core.base.Bean" %>
<%@ page import="com.rh.core.util.Constant" %> 

<%@ page import="com.rh.core.serv.ServMgr " %> 
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.rh.core.org.UserBean"%>
<%@ page import="com.rh.da.BrmComm " %>
<?xml version="1.0" encoding="UTF-8"?>
<floors>
<%
    String strCmpyCode = (String)request.getParameter("cmpCode");
    String stroDeptCode = (String)request.getParameter("oDeptCode");

    Bean param = new Bean();
    StringBuilder sql = new StringBuilder("");
    
    List<Bean> dataList=null;

	 sql.append("HOUSE_ID,HOUSE_NAME,PIC_NAME,MEMO,SELF_COUNT, SELF_LIMIT, TEMP_ALERT,HUM_ALERT,ALERT_URL");
     param.set(Constant.PARAM_SELECT, sql.toString());
     param.set(Constant.PARAM_WHERE, " and S_CMPY='"+strCmpyCode+"' and S_ODEPT='"+stroDeptCode+"'");
     param = ServMgr.act("DA_HOUSE", "finds", param);
     dataList = param.getList(Constant.RTN_DATA);
    for (Bean data : dataList) {    	
    	%>
    	
    	 <floor id="<%=data.getStr("HOUSE_ID") %>" name="<%=data.getStr("HOUSE_NAME") %>" memo="null">
         
     
         <%  
        param = new Bean();
        sql = new StringBuilder("");
        
        sql.append("SHELF_ID,SHELF_ORDER,IS_FULL,MEMO,SELF_COUNT,SELF_LIMIT");
        param.set(Constant.PARAM_SELECT, sql.toString());
        param.set(Constant.PARAM_WHERE, "and HOUSE_ID='"+data.getStr("HOUSE_ID")+"'");
        param = ServMgr.act("DA_SHELF", "finds", param);
        List<Bean> dataListII = param.getList(Constant.RTN_DATA);    
        
        for (Bean dataII : dataListII) {  
         %>
         <loc id="<%=dataII.getStr("SHELF_ORDER") %>" full="<%=dataII.getStr("IS_FULL") %>" memo="null" />
         
         <% } %>
         </floor>
    	
    <%  } %>

</floors>

