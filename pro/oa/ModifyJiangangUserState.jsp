<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--OaTransmitGongwen.jsp 平台迁移文件页面-->
<%@ page import="com.rh.core.base.Context" %>
<%@ page import="com.rh.core.serv.ServDao" %>
<%@ page import="com.rh.core.serv.ServMgr" %>
<%@ page import="com.rh.core.serv.bean.SqlBean" %>
<%@ page import="com.rh.core.serv.flow.FlowMgr" %>
<%@ page import="com.rh.core.org.mgr.UserMgr" %>
<%@ page import="com.rh.core.comm.entity.EntityMgr" %>
<%@ page import="com.rh.core.serv.ParamBean" %>
<%@ page import="com.rh.core.util.Strings" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	    <title>智能平台系统</title>
	    <%@ include file= "/sy/base/view/inHeader.jsp" %>
	</head>
	
<%
	//
 	if (request.getQueryString() != null) {
 		request.getSession().setAttribute("GOTO_URL", request.getRequestURI().toString() + "?" + request.getQueryString());
	} else {
	  	request.getSession().setAttribute("GOTO_URL", request.getRequestURI().toString());
	}
	// 如果没有登录则导向首页去登录
	if(userBean == null) {
		 String loginUrl = Context.getSyConf("SY_LOGIN_URL","/");
		 RequestUtils.sendDisp(request, response, loginUrl);
	}
	//主程序
	String action = RequestUtils.getStr(request, "action");
	String res = "无可用操作";
	if (action.equals("1")) {
	    try {
	      //清空用户状态表兼岗状态
		    SqlBean where = new SqlBean();
		    where.andNotNull("JIAN_CODES");
		    Bean setBean = new Bean();
		    setBean.set("JIAN_CODES", "");
		    ServDao.updates("SY_ORG_USER_STATE", setBean, where);
		    //查兼岗关系表，校对用户状态表
		    ParamBean find = new ParamBean();
		    find.set("S_FLAG", Constant.YES_INT);
		    find.set("RELATION_TYPE", Constant.YES_INT);
		    List<Bean> dataList = ServDao.finds("SY_ORG_USER_JIANGANG", find);
		    Bean mainUsers = new Bean();
		    for (Bean data: dataList) {
		        String mUserCode = data.getStr("ORIGIN_USER_CODE"); //主用户编码
		        String jUserCode = data.getStr("USER_CODE"); //兼用户编码
		        String jGroup;
		        if (mainUsers.contains(mUserCode)) {
		            if (UserMgr.getUserStateOrCreate(jUserCode) == null) { //用户不存在
		                ServDao.save("SY_ORG_USER_JIANGANG", data.set("S_FLAG", Constant.NO_INT));
		                continue;
		            }
		            jGroup = mainUsers.getStr(mUserCode);
		            jGroup = Strings.addValue(jGroup, jUserCode);
		        } else {            	            
		            if (UserMgr.getUserStateOrCreate(mUserCode) == null || UserMgr.getUserStateOrCreate(jUserCode) == null) { //用户不存在
		                ServDao.save("SY_ORG_USER_JIANGANG", data.set("S_FLAG", Constant.NO_INT));
		                continue;
		            }
		            jGroup = mUserCode + Constant.SEPARATOR + jUserCode;
		        }
		        mainUsers.set(mUserCode, jGroup);
	        }
		    //重置兼岗状态 
		    for (Object key: mainUsers.keySet()) {
		        String jGroup = mainUsers.getStr(key.toString());
				String[] jGroupInSql = jGroup.split(Constant.SEPARATOR);
				SqlBean sql = new SqlBean();
		        sql.andIn("USER_CODE", jGroupInSql);
		        Bean set = new Bean().set("JIAN_CODES", jGroup);
		        ServDao.updates(ServMgr.SY_ORG_USER_STATE, set, sql);
		    }
		    res = "已完成";
	    } catch (Exception e) {
	        res = "错误:" + e.getMessage();
	    }
	}
%>
	<style>
	</style>
	<body>
	</body>
	<script type="text/javascript">
		alert("<%=res%>");
		window.close();
	</script>
</html>