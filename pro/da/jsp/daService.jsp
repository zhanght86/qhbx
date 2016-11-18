<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.lang.*"%>
<%@ page import="com.rh.core.base.Context" %>
<%@ page import="com.rh.core.base.Bean" %>

<%@ page import="com.rh.ar.ArItemFile " %>
<%@ page import="com.rh.core.util.Constant" %> 
<%@ page import="com.rh.core.util.Constant" %> 
<%@ page import="com.rh.core.serv.ServMgr " %> 

<% 

String AR_YEAR = request.getParameter("AR_YEAR"); 
   Bean param = new Bean();
   StringBuilder sql = new StringBuilder("");
   sql.append("YEAR,AR_CLASS_MODE,AR_SERVICE_MODE");
   param.set(Constant.PARAM_SELECT, sql.toString());
   if(AR_YEAR!=null)
   param.set(Constant.PARAM_WHERE, "and year="  +AR_YEAR + "");
   param = ServMgr.act("DA_YEAR_DEF", "finds", param);
   List<Bean> dataList = param.getList(Constant.RTN_DATA);
   if(dataList.size()>0)
   {  
         int i=dataList.size()-1;
		 String classMode = dataList.get(i).getStr("AR_CLASS_MODE");
		 String serviceMode = dataList.get(i).getStr("AR_SERVICE_MODE");
	     String s_service_id="DA_DANGAN_I";
	     String s_year=dataList.get(i).getStr("YEAR");
	    
	
	    if(serviceMode.equals("00002"))
 		  s_service_id="DA_DANGAN_FOLDER";	 

	  
	  String path="QX;JG";	  
	  if(classMode.equals("00002"))
	  path="JG:QX";	  
	  
	  if(serviceMode.equals("00001"))
	  path+=";BOX"; 
	  
   
     response.sendRedirect("/core/view/stdListView.jsp?sId="+s_service_id+"&frameId=-ar-printjsp-arService-tabsIframe&parWhere= and YEAR%3D'@AR_YEAR@'&parVar={'@AR_YEAR@':'"+s_year+"','@PATH@':'"+path+"'}");

   }


%>