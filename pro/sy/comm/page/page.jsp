<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--page.jsp 平台page页面-->
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.rh.bn.serv.BnUtils"%>
<%@ page import="com.rh.core.base.Context" %>
<%@ page import="com.rh.core.util.Lang" %>
<%@ page import="com.rh.core.comm.portal.PortalTemplServ" %>
<%@ page import="com.rh.core.org.mgr.OrgMgr"%>
<%@ page import="com.rh.core.serv.OutBean" %>
<%@ page import="com.rh.core.util.Strings" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>OA办公系统</title> 
   <script type="text/javascript">
		window.moveTo(0,0);
		window.resizeTo(screen.availWidth,screen.availHeight);  
	</script>
    <%@ include file= "/sy/base/view/inHeader.jsp" %>
    
    <script type="text/javascript" src="<%=urlPath%>/sy/base/frame/engines/rhPageView.js"></script>
</head>
<%
  if (request.getQueryString() != null) {
	  System.out.println(request.getQueryString());
	  System.out.println(request.getRequestURI().toString() + "?" + request.getQueryString());
	  request.getSession().setAttribute("GOTO_URL", request.getRequestURI().toString() + "?" + request.getQueryString());
  } else {
	  request.getSession().setAttribute("GOTO_URL", request.getRequestURI().toString());
  }	
  // 如果没有登录则导向首页去登录
  if(userBean == null) {
	 String switchum = Context.getSyConf("OA_UM_SWITCH", "2");
	 System.out.println("UM控制开关switchum = " + switchum);
	 if(switchum.equals("1")){
		  String loginUrl = Context.getSyConf("SY_LOGIN_UM_URL","/");
			 //RequestUtils..sendDisp(request, response, loginUrl);
		 response.sendRedirect(loginUrl);
		 return;
	 }else { 
	 String loginUrl = Context.getSyConf("SY_LOGIN_URL","/");
	 response.sendRedirect(loginUrl);
	 return;
	 //RequestUtils.sendDisp(request, response, loginUrl);
	}
  }
  String func = RequestUtils.getStr(request,"func");
  String todoServId = RequestUtils.getStr(request,"todoServId");
  String todoUrl = RequestUtils.getStr(request,"todoUrl");
  String todoTitle = RequestUtils.getStr(request,"todoTitle");
  String servPk = RequestUtils.getStr(request,"servPk");
  String openTab = RequestUtils.getStr(request,"openTab");//扩展打开的tab参数
  String hexToStr = RequestUtils.getStr(request,"hexToStr");
  if("true".equals(hexToStr)){ //需要对URL进行转换
	openTab = new String(Strings.escapeAngle(Lang.hexToStr(openTab)));
  }
  //String openTab = "";
  
  String rhClient = RequestUtils.getStr(request,"rhClient");//是否小桌面跳转
  String rhDevFlag = request.getParameter("rhDevFlag");
  String homepage = request.getParameter("home");
  String homeConfig = null;
  //从连接中的openTab中拿出url。还原系统参数的设置
  System.out.println(openTab);
  Bean openTabBean = JsonUtils.toBean(openTab);
  if(openTabBean.isNotEmpty("url")){
	  String url = openTabBean.getStr("url");
	  rhDevFlag = BnUtils.GetRequest(url,"rhDevFlag");
	  homepage = BnUtils.GetRequest(url,"home");
  }
  if (rhDevFlag != null && rhDevFlag.equalsIgnoreCase("true")) { 
  } else if(homepage !=null && homepage.equalsIgnoreCase("true")){
  }else{
	  homeConfig = Context.getSyConf("SY_HOME_CONFIG",null);//首页的参数定义信息
  }
  String bannerConfig = Context.getSyConf("SY_BANNER_CONFIG","");//banner的配置信息
  String wbimFlag = Context.getSyConf("SY_WBIM_FLAG","");//即时通讯是否启用
  Bean homeTabColorBean = JsonUtils.toBean(Context.getSyConf("SY_TAB_COLOR","{'1':{'li':'lightBlueLi','a':'lightBlueA'}}")).getBean("1");//默认tab颜色配置
  Bean bannerBean = JsonUtils.toBean(bannerConfig);
  String banner = urlPath + bannerBean.get("banner","");
  String bannerBack = urlPath + bannerBean.get("bannerBack","");
  String bannerTabBack = urlPath + bannerBean.get("bannerTabBack","");
  String defaultTab = ""; 
  //判断是否为控股的和财险的
  int odeptlevel = Context.getUserBean().getODeptLevel();
  String codepath = Context.getUserBean().getODeptCodePath();
//  if (odeptlevel == 1){
//	defaultTab = Context.getSyConf("SY_DEFAULT_TAB_KG","");
//  } else if (odeptlevel == 2){
   // 取得默认打开的Tab
//    defaultTab =  Context.getSyConf("SY_DEFAULT_TAB","");
//  } else if(odeptlevel >= 3){
	//省分系统配置
//	String tabComm = Context.getSyConf("SY_DEFAULT_TAB_COMMON", "");
	//用户的机构层级编码
	//String codepath = Context.getUserBean().getODeptCodePath();
	//当前用户的层级如果大于3只取第三级的ODEPT_CODE
//	String odeptCode = OrgMgr.getOdeptlevelCode(codepath, 3);
	//模版类型 COMPANY_INFOS,根据模版类型和机构编码取对应的模版
//	OutBean outBean = PortalTemplServ.getOdeptTempl(odeptCode, "COMPANY_INFOS");
	//判断省分公司是否设置了自己的门户模版，没有默认使用总公司的配置模版
//	if (outBean.isNotEmpty("PT_TITLE")) { 	
//	  defaultTab = Context.getReplaceStr(tabComm, outBean.getStr("PT_TITLE"), outBean.getId());	
//	} else {
//      defaultTab =  Context.getSyConf("SY_DEFAULT_TAB","");
//	  }
//	}
  
  if (codepath.indexOf("3rin6giCR9vUIv6kHIO3ex^00000001") == -1){
	defaultTab = Context.getSyConf("SY_DEFAULT_TAB_KG","");
  } else if (odeptlevel == 2){
   // 取得默认打开的Tab
    defaultTab =  Context.getSyConf("SY_DEFAULT_TAB3","");
  } else if(odeptlevel >= 3){
	//省分系统配置
	String tabComm = Context.getSyConf("SY_DEFAULT_TAB_COMMON", "");
	//用户的机构层级编码
	//String codepath = Context.getUserBean().getODeptCodePath();
	//当前用户的层级如果大于3只取第三级的ODEPT_CODE
	String odeptCode = OrgMgr.getOdeptlevelCode(codepath, 3);
	//模版类型 COMPANY_INFOS,根据模版类型和机构编码取对应的模版
	OutBean outBean = PortalTemplServ.getOdeptTempl(odeptCode, "COMPANY_INFOS");
	//判断省分公司是否设置了自己的门户模版，没有默认使用总公司的配置模版
	if (outBean.isNotEmpty("PT_TITLE")) { 	
	  defaultTab = Context.getReplaceStr(tabComm, outBean.getStr("PT_TITLE"), outBean.getId());	
	} else {
      defaultTab =  Context.getSyConf("SY_DEFAULT_TAB3","");
	  }
	}
	System.out.println(homeConfig);
  List<Bean> tabList = JsonUtils.toBeanList(defaultTab);
%>
<%if(null != openTab && !"".equals(openTab)){%>
	
<%}%>
<%
if (wbimFlag.length() > 0 && wbimFlag.equals("true")) {//即时通讯模块所需文件引用%>
    <script src='<%=urlPath%>/sy/plug/webim/scripts/strophe.js'></script>
    <script src='<%=urlPath%>/sy/plug/webim/scripts/flXHR.js'></script>
    <script src='<%=urlPath%>/sy/plug/webim/scripts/strophe.flxhr.js'></script>
    
    <script src='<%=urlPath%>/sy/plug/webim/scripts/iso8601_support.js'></script>
    <script src='<%=urlPath%>/sy/plug/webim/scripts/strophe.rsm.js'></script>
    <script src='<%=urlPath%>/sy/plug/webim/scripts/strophe.archive.js'></script>
    <script src='<%=urlPath%>/sy/plug/webim/scripts/strophe.hismsg.js'></script>
    <script src='<%=urlPath%>/sy/plug/webim/scripts/strophe.recentcontact.js'></script>
  
  <!-- suport file upload -->  
  <script type="text/javascript" src="<%=urlPath%>/sy/base/frame/coms/file/swfupload.js"></script>
  <script type="text/javascript" src="<%=urlPath%>/sy/base/frame/coms/file/js/swfupload.queue.js"></script>
  <script type="text/javascript" src="<%=urlPath%>/sy/base/frame/coms/file/js/fileprogress.js"></script>
  <script type="text/javascript" src="<%=urlPath%>/sy/base/frame/coms/file/js/handlers.js"></script>
  <link rel="stylesheet" type="text/css" href="<%=urlPath%>/sy/plug/webim/chat/webim.css"/>
  <script type="text/javascript" src="<%=urlPath%>/sy/plug/webim/chat/rhWbimView.js"></script>
<%}%>
<%
if (bannerTabBack.length() > 0) {%>
<style type="text/css">
#homeTabs .ui-widget-header {background:white url(<%=bannerTabBack %>) left top repeat-x;}
</style>
<%}%>
<style type="text/css">
#banner {background:url(<%=bannerBack %>) left top;}
.banner-logo {background:url(<%=banner %>) left top no-repeat;}
</style>
<body class="pageBody">
<div id="banner" style="text-align:center;height:73px;">
 <div id="logo" class="banner-logo" style="width:100%;height:73px;float:left;">
 <div class="banner_info" style="
    width: 220px;
    float: right;
    position: relative;
    top: 15px;
    right: 180px;
">
 <div class="user_info" style="margin-bottom:8px;"><%=userBean.getTDeptName()%>&nbsp;<%=userBean.getName()%>&nbsp;</div>
 <div class="date_info">
 <span class="date_span"></span>&nbsp;&nbsp;
 <script type="text/javascript">
 var mydate = rhDate.pattern("yyyy-MM-dd");
 var week = {
 "0" : "星期日",
 "1" : "星期一",
 "2" : "星期二",
 "3" : "星期三",
 "4" : "星期四",
 "5" : "星期五",
 "6" : "星期六"
 };
 var nowDate = new Date();
 jQuery(".date_span").text(mydate+' '+week[nowDate.getDay()]);
 </script>
 </div>
 </div></div>
 <div id="rh-slideMsg" class="rh-slideMsg"></div><!-- 消息下拉面板 -->
</div>
<div id="homeTabsULFill" class="homeTabsULFill"></div>
<div id="homeTabs"><!--Begin homeTabs -->
    <%
      if (homeConfig != null) {%>
      	<ul class="tabUL">
		</ul>
    <%
      } else {%>
		<ul class="tabUL">
		<%
			int size = tabList.size();
			if (size > 0) {
				for (int i = 0; i < size; i++) {
					Bean tab = tabList.get(i);
					String tabId = tab.getStr("TAB_ID");
					String tabName = tab.getStr("TAB_NAME");
			%>
				<li class="<%=homeTabColorBean.getStr("li") %> replaceLi platformPage" pretabid="<%=tabId%>">
					<a class="<%=homeTabColorBean.getStr("a") %> rh-open-default" title="<%=tabName%>" href='#<%=tabId%>'>
						<span><%=tabName%></span></a></li>
			<%					
				}
			%>
			</ul> 
			<%
				for (int j = 0; j < size; j++) {
					Bean tab = tabList.get(j);
					String tabId = tab.getStr("TAB_ID");
			%>
			<div id='<%=tabId%>'></div>
			<%
				}
			} else {
			%>
			<li class="<%=homeTabColorBean.getStr("li") %> replaceLi platformPage" pretabid='platformPage'>
				<a class="<%=homeTabColorBean.getStr("a") %> rh-open-default" title="信息平台" href='#platformPage'>
					<span>信息平台</span></a></li>
			</ul>
			<div id='platformPage'></div>	
			<%
			}
			%>
    <%      
      }
    %>
</div><!-- End homeTabs -->  
<div id="homeMenu" style="display:none;"></div>
<div id="rh-slideStyle" class="rh-slideStyle"><!--Begin 风格面板 -->
	  <div class="rh-slideStyle-content" style="">
	     <div id="rh-slideStyle-box" class="rh-slideStyle-box"></div>
	  </div>
</div><!-- End 风格面板 -->
</body>
<script type="text/javascript">
//设置cookie
document.cookie="RhClientLogin=true";
//设置参数
var topAlert = System.getVar("@C_SY_TOP_ALERT@") || "false";//顶部消息面板是否启用
var styleDef = System.getVar("@C_SY_STYLE_DEF@") || "";//样式默认定义
var pageTitle = System.getVar("@C_SY_PAGE_TITLE@") || System.getVar("@CMPY_NAME@") + "集成平台";//页面的浏览器标题
var mbLink = System.getVar("@C_SY_MB_LINK@") || "false";//banner的配置信息
var pwdFlag = System.getVar("@C_SY_PWD_SHOW@") || "true";//密码修改是否显示
var preDeptUser = System.getVar("@C_SY_PRE_DEPT_USER@") || "";//用户名前缀显示的部门
var tabColor = System.getVar("@C_SY_TAB_COLOR@") || "";//tab的颜色配置信息
var wbimFlag = System.getVar("@C_SY_WBIM_FLAG@") || "false";//即时通讯是否启用
document.title = pageTitle;
var opts = {"id":"rhHome","styleDef":styleDef,"topPannel":topAlert,"wbimFlag":wbimFlag,"rhClient":"<%=rhClient%>",
		"mbLink": mbLink,"tabColor":tabColor,"pwdFlag":pwdFlag,"preDeptUser":preDeptUser};
if (<%=homeConfig%> != null) {//首页的配置
	opts["home"] = <%=homeConfig%>;
}
if ("<%=func%>" === "openTodo") {//自动进入待办的配置
    opts["openTodo"] = {"todoServId":"<%=todoServId%>","todoUrl":"<%=todoUrl%>","todoTitle":"<%=todoTitle%>","servPk":"<%=servPk%>"};
}
//opts["openTab"] = "<new String(Strings.escapeAngle(Lang.hexToStr(openTab)))%>";
opts["openTab"] = "<%=openTab%>";
opts["defaultTab"] = "<%=defaultTab%>";
var pageView = new rh.vi.pageView(opts);
//where条件参数
pageView.addOpenTabParams({"_WHERE_":"<%=StringUtils.isBlank(request.getParameter("where")) ? null : request.getParameter("where")%>"});
pageView.show();
<%
//门户中的智能搜索定制
String isSearch = RequestUtils.getStr(request, "isSearch");
if(isSearch != null && "true".equals(isSearch)){
	//搜索关键词
	String keyWords = RequestUtils.getStr(request, "keyWords");
	%>
	var opts = {"sId":"SEARCH-RES","tTitle":"搜索","url":"/SY_PLUG_SEARCH.query.do?data={'KEYWORDS':'<%=keyWords%>'}","menuFlag":3,'scrollFlag':true};
	Tab.open(opts);
	<%
}
%>
//外部rh桌面调用
function client_pageFunc () {
	jQuery("#rh-slideMsgBtn").click();
};
//外部rh桌面调用取代办数量
function client_TodoCount () {
	var res = Todo.getCount();
	if (res) {
	    return res;
	} else {
		return "";
	}
};
</script>
</html>