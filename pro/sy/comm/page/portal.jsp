<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--page.jsp 平台page页面-->
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="com.rh.core.base.Context" %>
<%@ page import="com.rh.core.util.Lang" %>
<%@ page import="com.rh.core.comm.portal.PortalTemplServ" %>
<%@ page import="com.rh.core.org.mgr.OrgMgr"%>
<%@ page import="com.rh.core.org.mgr.UserMgr"%>
<%@ page import="com.rh.core.serv.OutBean" %>
<%@ page import="com.rh.core.util.Strings" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>个人门户</title>
    <%@ include file= "/sy/base/view/inHeader.jsp" %>
    <script type="text/javascript" src="<%=urlPath%>/bn/base/frame/engines/rhPageView.js"></script>
	<script type="text/javascript" src="<%=urlPath%>/bn/base/frame/engines/portal.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=urlPath %>/bn/style/css/common.css"/>
	<link rel="stylesheet" type="text/css" href="/sy/comm/page/css/css.css" />


</head>
<%  
  if (request.getQueryString() != null) {
	  request.getSession().setAttribute("GOTO_URL", request.getRequestURI().toString() + "?" + request.getQueryString());
  } else {
	  request.getSession().setAttribute("GOTO_URL", request.getRequestURI().toString());
  }	
  // 如果没有登录则导向首页去登录
  if(userBean == null) {
	  String loginUrl = Context.getSyConf("SY_LOGIN_URL","/");
	  response.sendRedirect(loginUrl);
	  return;
	  //RequestUtils.sendDisp(request, response, loginUrl);
  }
  String func = RequestUtils.getStr(request,"func");
  String openTab = RequestUtils.getStr(request,"openTab");//扩展打开的tab参数
  String rhClient = RequestUtils.getStr(request,"rhClient");//是否小桌面跳转
  String homeConfig = null;
  String bannerConfig = Context.getSyConf("SY_BANNER_CONFIG","");//banner的配置信息
  String bnPortalWidth = Context.getSyConf("SY_BNPORTAL_WIDTH","960");
  Bean homeTabColorBean = JsonUtils.toBean(Context.getSyConf("SY_TAB_COLOR","{'1':{'li':'lightBlueLi','a':'lightBlueA'}}")).getBean("1");//默认tab颜色配置
  Bean bannerBean = JsonUtils.toBean(bannerConfig);
  String banner = urlPath + bannerBean.get("banner","");
  String bannerBack = urlPath + bannerBean.get("bannerBack","");
  String bannerTabBack = urlPath + bannerBean.get("bannerTabBack","");
  String defaultTab =  Context.getSyConf("SY_DEFAULT_TAB","");
   String sysSub = request.getParameter("sysSub");
  String oDeptCode = request.getParameter("ODEPT_CODE");
  String deptCode = request.getParameter("DEPT_CODE");
  
  //判断是否为控股的和财险的
  int odeptlevel = Context.getUserBean().getODeptLevel();
  if(request.getParameter("ODEPT_LEVEL")==null){
  }else{
	  odeptlevel = Integer.parseInt(request.getParameter("ODEPT_LEVEL"));
  }
  String codepath = Context.getUserBean().getODeptCodePath();
  String roleCodes = Context.getUserBean().getRoleCodeStr();
  //总公司的用户可以看到总公司门户、个人门户。
  //其他用户以及总公司门户管理员可看到总公司门户、分公司、个人门户
  //&& roleCodes.indexOf("Z_MHGLY") == -1
  if(odeptlevel==2){
	  defaultTab = Context.getSyConf("SY_DEFAULT_TAB","");
  }else if(Context.getSyConf("BN_DEFAULT_CMPY_SUB","").equals(oDeptCode)){
	  defaultTab = Context.getSyConf("SY_DEFAULT_TAB","");
  }else{
	  defaultTab = Context.getSyConf("SY_DEFAULT_TAB","");
  }
  if(sysSub != null && sysSub.length()>0){
	  if("aa".equals(sysSub)){
	  defaultTab = Context.getSyConf("SY_DEFAULT_TAB_SUB","");
	  }else if("ab".equals(sysSub)){
		  defaultTab = Context.getSyConf("BN_DEFAULT_DEPT","");
	  }
	  List<Bean> tabBeanlist = JsonUtils.toBeanList(defaultTab);
	  String tabUrl = tabBeanlist.get(0).getStr("TAB_URL");
	  tabUrl = tabUrl + "&ODEPT_CODE="+oDeptCode+"&ODEPT_LEVEL="+odeptlevel+"&DEPT_CODE="+deptCode;
	  tabBeanlist.get(0).set("TAB_URL",tabUrl);
	  defaultTab = JsonUtils.toJson(tabBeanlist).replace("\\","").replace("\"","\'");
  }
  if (codepath.indexOf("3rin6giCR9vUIv6kHIO3ex^00000001") == -1){
	//defaultTab = Context.getSyConf("SY_DEFAULT_TAB_KG","");
  } else if (odeptlevel == 2){
   // 取得默认打开的Tab
    //defaultTab =  Context.getSyConf("SY_DEFAULT_TAB","");
  } else if(odeptlevel >= 3){
	//省分系统配置
	String tabComm = Context.getSyConf("SY_DEFAULT_TAB_COMMON", "");
	//用户的机构层级编码
	//String codepath = Context.getUserBean().getODeptCodePath();
	//当前用户的层级如果大于3只取第三级的ODEPT_CODE
	String odeptCode = OrgMgr.getOdeptlevelCode(codepath, 3);
	//模版类型 COMPANY_INFOS,根据模版类型和机构编码取对应的模版
	//OutBean outBean = PortalTemplServ.getOdeptTempl(odeptCode, "COMPANY_INFOS");
	//判断省分公司是否设置了自己的门户模版，没有默认使用总公司的配置模版
	//if (outBean.isNotEmpty("PT_TITLE")) { 	
	//  defaultTab = Context.getReplaceStr(tabComm, outBean.getStr("PT_TITLE"), outBean.getId());	
	//} else {
      //defaultTab =  Context.getSyConf("SY_DEFAULT_TAB","");
	  //}
	}
  List<Bean> tabList = JsonUtils.toBeanList(defaultTab);
  //获取委托的参数
  String TO_USER_CODE = RequestUtils.getStr(request,"TO_USER_CODE");
  String toUserName = "";
  if(TO_USER_CODE.length()>0){
      toUserName = UserMgr.getUser(TO_USER_CODE).getName();
  }
%>
<style type="text/css">
.footcss{text-align:center;color:#666;margin-left: -200px;height:20px;line-height:20px;width:1050px;font-family: Microsoft YaHei;}
#banner { width:100%; height:156px;}
.banner-logo {height:97px;}
.banner-color {background-color:#D50310;}
.user_info{padding-bottom:4px;}
.yellowLi replaceLi platformPage ui-state-default a span{color:#747270;}
.yellowLi replaceLi platformPage ui-state-default ui-tabs-selected ui-state-active{background-color:#999999;}
</style>
<body class="pageBody">
<div class="banner-bg"></div>
<div class="banner-bg-banner"></div>
<div class="banner-bg-text"><!-- 办 公 运 营 门 户 --></div>
<div>
<div id="banner" style="text-align:center;height:97px;">
 <div id="logo" class="banner-logo" style="width:100%;height:97px;float:left;">
 <div class="banner_info">
 <div class="user_info"><%=userBean.getTDeptName()%>&nbsp;<%=userBean.getName()%></div>
 <div class="date_info">
 <span class="date_span"></span>&nbsp;&nbsp;<span class="logout" id="loginOut">[退出]</span>
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
 </div>
 </div>
</div>
<div id="homeTabsULFill" class="homeTabsULFill"></div>
<div id="homeTabs"><!--Begin homeTabs -->
    <%
      if (homeConfig != null) {%>
      	<ul class="tabUL">
		</ul>
    <%
      } else {%>
	  <div class="tabDiv">
		<ul class="tabUL" style="width:<%=bnPortalWidth%>px;height:32px;left:-20px;">
		<%
			int size = tabList.size();
			if (size > 0) {
				for (int i = 0; i < size; i++) {
					Bean tab = tabList.get(i);
					String tabId = tab.getStr("TAB_ID");
					String tabName = tab.getStr("TAB_NAME");
					if(tabId.equals("system_sub")){
			%>
				<li class="<%=homeTabColorBean.getStr("li") %> replaceLi platformPage" pretabid="<%=tabId%>"><a class="<%=homeTabColorBean.getStr("a") %> rh-open-default" title="<%=tabName%>" href='#<%=tabId%>'><span><%=tabName%></span></a></li>
			<%
				}else{
			%>
				<li class="<%=homeTabColorBean.getStr("li") %> replaceLi platformPage" pretabid="<%=tabId%>"><a class="<%=homeTabColorBean.getStr("a") %> rh-open-default" title="<%=tabName%>" href='#<%=tabId%>'><span><%=tabName%></span></a></li>
			<%			
				}			
				}
			%>
			</ul> </div>
			<%
				for (int j = 0; j < size; j++) {
					Bean tab = tabList.get(j);
					String tabId = tab.getStr("TAB_ID");
			%>
			<div style="width:1040px;margin:0 auto;background-color:#fff;">
			<div id='<%=tabId%>' style="width:<%=bnPortalWidth%>px;margin:0 auto;padding-top:10px"></div></div>
			<%
				}
			} else {
			%>
			<li class="<%=homeTabColorBean.getStr("li") %> replaceLi platformPage" pretabid='platformPage'><a class="<%=homeTabColorBean.getStr("a") %> rh-open-default" title="信息平台" href='#platformPage'><span>信息平台</span></a></li>
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
<!-- foot开始 -->

<!-- div class="foot">
    	<dl class="clearfix">
   
			<div class="footcss">版权所有 2011 北京软虹科技有限公司</div>
        </dl>
    </div> -->
<!-- <table style="width:100%;">
	<tr align="center">
	<td>
		
	</td>
		<td style="text-align:center;border-top:1px solid #C0C0C0;padding-top:10px">
		Copyright@AEON LIFE INSURANCE COMPANY,Ltd All Rights Reserved
		</td>
	</tr>
	<tr align="center"><td></td><td style="text-align:center;padding-bottom:10px">版权所有 2015 北京软虹科技有限公司</td></tr>
</table> -->

<!-- foot结束 -->
</div>
<div id="agtdialog" class="dialog">
	<div></div>
</div>
</body>
<script type="text/javascript">
//全文检索
var bannerSearch = System.getVar("@C_SY_BANNER_SEARCH@") || "";
	if (typeof bannerSearch == "string" && (bannerSearch !== "false")) {
		var seaCon = jQuery("<div></div>").addClass("rh-head-sea");
		var temp = jQuery("<input type='text' style='border:2px #CE0300 solid;position: absolute; top: 75px; width: 174px;right: 247px; z-index: 11;font-style:italic;' class='rh-head-sea-input' value='请输入关键字'/>").appendTo(seaCon);
		temp.keypress(function(event) {
			if (event.keyCode == '13') {
				jQuery(".rh-head-sea-icon").click();
			}
		}).focus(function(event) {
			var value = jQuery(this).val();
			if (value == "请输入关键字") {
				jQuery(this).val('');
			}
		});
		var seaIcon = jQuery("<div style='background-color:#C5261D;position: absolute; line-height:29px;height: 29px; width: 76px;top: 75px; right: 172px; z-index: 11;color:white;' class='rh-head-sea-icon'>搜索</div>").appendTo(seaCon);
		seaIcon.bind("click",function(event) {
			var keywords = temp.val();
			if ((jQuery.trim(keywords).length > 0) && (keywords != "请输入关键字")) {
				var keyStr = encodeURIComponent(keywords);
				//var opts = {"sId":"SEARCH-RES","tTitle":"搜索","url":"/SY_PLUG_SEARCH.query.do?data={'KEYWORDS':'" + keyStr + "'}","menuFlag":3,'scrollFlag':true};
				//Tab.open(opts);
				window.open("/sy/comm/page/page.jsp?isSearch=true&keyWords="+keyStr,"_blank");
			} else {
			    alert("请输入关键字！");
			}
		});
		seaCon.appendTo(jQuery("#banner"));	
	}


//设置cookie
document.cookie="RhClientLogin=true";
//设置分公司
showSubCmpy();
//设置参数
var styleDef = "";//样式默认定义
var pageTitle = "门户";//页面的浏览器标题
var tabColor = "";//tab的颜色配置信息
//document.title = pageTitle;
var opts = {"id":"rhHome","styleDef":styleDef,"rhClient":"<%=rhClient%>","tabColor":tabColor};
opts["defaultTab"] = "<%=defaultTab%>";
var pageView = new rh.vi.pageView(opts);
pageView.show();
if('<%=TO_USER_CODE%>'.length>0){
	   var userid = "<%=userBean.getId()%>";
		showRHDialog("委托提醒", "您的工作已经委托给【<%=toUserName%>】，是否进入委托设置结束委托？", function(){
			window.open("/sy/comm/page/page.jsp?openTab=%7B%27tTitle%27%3A%27%E5%BF%AB%E6%8D%B7%E8%8F%9C%E5%8D%95%27%2C%27url%27%3A%27SY_ORG_USER_TYPE_AGENT.list.do%27%2C%27menuFlag%27%3A3%7D&userCode="
					+userid);
		},this);
}
</script>
</html>