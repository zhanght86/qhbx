<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="com.rh.core.util.var.VarMgr" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.rh.core.org.mgr.UserMgr" %>
<%@ page import="com.rh.core.org.UserBean" %>
<%@ page import="com.rh.core.base.Context" %>
<%@ page import="com.rh.core.util.JsonUtils" %>
<%@ page import="com.rh.core.util.Lang" %>
<%@ page import="com.rh.core.util.Strings" %>
<%
   UserBean userBean = Context.getUserBean(request);
	String orgMapJson = "";
	if (userBean ==null ) { // 如果没取到用户编码
		// TODO 强制退出
		response.sendRedirect("/oa/mobile/jsp/index.jsp");
	} else {
	    UserMgr.clearSelfUserCache(userBean);
	    Context.setOnlineUser(request, userBean);
		Map orgMap = VarMgr.getOrgMap();
		orgMapJson = JsonUtils.mapsToJson(orgMap);
	}
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<title>首页</title>
	<script>
		var 
			FireFlyContextPath, // ajax请求路径
			ZhbxImgPath;			// 图片加载路径
			
			// 移动办公系统地址
			FireFlyContextPath = "";//"<%=Context.getSyConf("SYS_HOST_ADDR_MOBILE", "http://localhost:8009") %>";
			//OA系统地址
			OAHost = "<%=Context.getSyConf("SYS_HOST_ADDR", "http://localhost:8009") %>"
			// 门户系统地址
			ZhbxImgPath = "<%=Context.getSyConf("WX_IMG_PATH", "http://localhost:8009") %>";
			
		var 
			userCode 	= "<%=userBean.getId()%>",
			orgMapJson 	= <%=orgMapJson%>, // 是一个json，不能加双引号
			ScImgPath = FireFlyContextPath;
			
			homeUrl 	= window.location.href;
			
			var defaultSmallProfile = "/oa/mobile/css/customImages/profile.png";
			var defaultBigProfile = "/oa/mobile/css/customImages/sc-default-big-profile.jpg";
	</script>
	<!-- *****************开发模式--start**************** -->
	
	<!-- jqueryMobile样式 -->
	<link rel="stylesheet" type="text/css" href="/oa/mobile/plugins/themes/Bootstrap.css" />
	<link rel="stylesheet" type="text/css" href="/oa/mobile/plugins/jquery.mobile-1.4.0/jquery.mobile.structure-1.4.0.css">
	<link rel="stylesheet" type="text/css" 	href="/oa/mobile/plugins/themes/jquery.mobile.icons.min.css" />
	
	<!-- custom样式 -->
	<link rel="stylesheet" type="text/css"
		href="/oa/mobile/js/tree/style.css" />
	<link rel="stylesheet" type="text/css"
		href="/oa/mobile/css/mbCustom.css" />
	
	<!-- jquery相关脚本 -->
	<script type="text/javascript"
		src="/oa/mobile/plugins/jquery/jquery-1.8.2.min.js"></script>
	<script type="text/javascript" src="/oa/mobile/js/q.js"></script>
	<script type="text/javascript" src="/oa/mobile/js/tree/jquery.tree.js"></script>
	<script type="text/javascript" src="/sy/base/frame/coms/DatePicker/WdatePicker.js"></script>
	<!-- 微信JS -->
	<script type="text/javascript" src="/oa/mobile/plugins/wx/jweixin-1.0.0.js"></script>
	
	<!-- 平台相关脚本 -->
	<script type="text/javascript" src="/oa/mobile/js/platform.js"></script>
	<script type="text/javascript" src="/oa/mobile/js/tools.js"></script>
	<script type="text/javascript" src="/oa/mobile/js/constant.js"></script>
	
	<!-- costom脚本 -->
	<script type="text/javascript" src="/oa/mobile/js/mbCustom.js"></script>
	<!-- 因为需要在jqm初始化之前执行，所以引用的顺序要往前放 -->
	<script type="text/javascript"
		src="/oa/mobile/plugins/jquery.mobile-1.4.0/jquery.mobile-1.4.0.js"></script>
	
	<!-- PhotoSwipe -->
	<link rel="stylesheet" type="text/css"
		href="/oa/mobile/plugins/PhotoSwipe-3.0.5/styles.css">
	<link rel="stylesheet" type="text/css"
		href="/oa/mobile/plugins/PhotoSwipe-3.0.5/photoswipe.css">
	<script type="text/javascript"
		src="/oa/mobile/plugins/PhotoSwipe-3.0.5/klass.min.js"></script>
	<script type="text/javascript"
		src="/oa/mobile/plugins/PhotoSwipe-3.0.5/code.photoswipe-3.0.5.js"></script>

	<!-- 各组件相关脚本 -->
	<script type="text/javascript" src="/oa/mobile/js/mbDeskView.js"></script>
	<script type="text/javascript" src="/oa/mobile/js/mbListView.js"></script>
	<script type="text/javascript" src="/oa/mobile/js/mb.ui.grid.js"></script>
	<script type="text/javascript" src="/oa/mobile/js/mbNewsView.js"></script>
	<script type="text/javascript" src="/oa/mobile/js/mbCardView.js"></script>
	<script type="text/javascript" src="/oa/mobile/js/mb.ui.form.js"></script>
	<script type="text/javascript" src="/oa/mobile/js/mb.ui.write.form.js"></script>
	<script type="text/javascript" src="/oa/mobile/js/mbSelectListView.js"></script>
	<script type="text/javascript" src="/oa/mobile/js/mbWfCardView.js"></script>
	<script type="text/javascript" src="/oa/mobile/js/mbMind.js"></script>
	<script type="text/javascript" src="/oa/mobile/js/mbCardToCardView.js"></script>
	<script type="text/javascript" src="/oa/mobile/js/mbContacts.js"></script>
	<script type="text/javascript" src="/oa/mobile/js/mbContactDetail.js"></script>
	<script type="text/javascript" src="/oa/mobile/js/mbLeaderAction.js"></script>
	<script type="text/javascript" src="/oa/mobile/js/mbLeaderActionDetail.js"></script>
	
	
	
<!-- *****************开发模式-- end **************** -->
</head>
<body>
	<!-- desktop首页 -->
	<div id="mbDesk" data-role="page" data-dom-cache="true" data-theme="a">
		<!-- <div id="mbDesk_header" data-role="header" data-position="fixed">
			<img class="zhbx-logo" src="/oa/mobile/images/zhbx-logo.png" />
			<h2 class="zhbx-username"><span></span></h2>
		</div--> 
		<div id="mbDesk_content" role="main" class="ui-content">
			<div id="mbDesk_grid" class="ui-grid-b">
			
			</div>
		</div>
	</div>
	
	<!-- search搜索页 -->
	<div id="search" data-role="page" data-dom-cache="true">
		<div data-role="header" data-position="fixed" data-theme="b">
			<a href="" data-rel="back" data-icon="back" data-iconpos="notext">返回</a>
			<h2>查&nbsp;&nbsp;&nbsp;&nbsp;询</h2>
		</div>
		<div role="main" class="ui-content">
			<form class="clearfix">
				<input type="text" id="searchtext" placeholder="搜索" data-type="search" />
				<a href="#" id="searchbtn" data-role="button" class="ui-btn-inline">搜索</a>
			</form>
			<ul id="autocomplete" data-role="listview" data-inset="true" data-filter="true" data-input="#searchtext" >
			</ul>
		</div>
	</div>
	
	<!-- listview列表页-待办、已办、待阅事项、委托事务 -->
	<div id="listview" data-role="page">
		<div id="listview_header" data-role="header" data-position="fixed" data-theme="b">
			<a href="#" data-rel="back" data-icon="back" data-iconpos="notext">返回</a>
			<h2></h2>
			<!-- <a href="#" data-icon="refresh" data-iconpos="notext">刷新</a> -->
		</div>
		<div id="listview_content" role="main" class="ui-content">
		</div>
	</div>
	
		<!-- listview 委托事务设置 -->
	<div id="agtSet" data-role="page">
		<a href="#" id="createAgt" data-role="button" class="ui-btn-inline">新建委托</a>
		<div id="agtSet_header" data-role="header" data-position="fixed" data-theme="b">
			<a href="#" data-rel="back" data-icon="back" data-iconpos="notext">返回</a>
			<h2></h2>
			<!-- <a href="#" data-icon="refresh" data-iconpos="notext">刷新</a> -->
		</div>
		<div id="agtSet_content" role="main" class="ui-content">
		</div>
	</div>
	<!-- cardview卡片页 -表单名-->
	<div id="cardview" data-role="page"  data-theme="b">
		<div id="cardview_header" data-role="header" data-position="fixed" data-tap-toggle="false" data-theme="b">
			<a href="#" data-rel="back" data-icon="back" data-iconpos="notext">返回</a>
			<h2></h2>
			<!-- <a href="#" data-icon="refresh" data-iconpos="notext">刷新</a> -->
		</div>
		<div id="cardview_content" role="main" class="ui-content">
		</div>
		<div id="cardview_footer" data-role="footer" data-position="fixed" data-tap-toggle="false" data-theme="b">
		</div>
	</div>
	<!-- cardview卡片页 -相关文件-->
	<div id="relateCard" data-role="page"  data-theme="b">
		<div id="relateCard_header" data-role="header" data-position="fixed" data-tap-toggle="false" data-theme="b">
			<a href="#" data-rel="back" data-icon="back" data-iconpos="notext">返回</a>
			<h2>相关文件</h2>
			<!-- <a href="#" data-icon="refresh" data-iconpos="notext">刷新</a> -->
		</div>
		<div id="relateCard_content" role="main" class="ui-content">
		</div>
		<div id="relateCard_footer" data-role="footer" data-position="fixed" data-tap-toggle="false" data-theme="b">
		</div>
	</div>
	<!-- newsview新闻页 -->
	<div id="newsview" data-role="page">
		<div id="newsview_header" data-role="header" data-position="fixed" data-theme="b">
			<a href="#" data-rel="back" data-icon="back" data-iconpos="notext">返回</a>
			<h2>通知公告</h2>
		</div>
		<div id="newsview_content" role="main" class="ui-content">
		</div>
	</div>

	<!-- cardToCardView卡片页 -->
	<div id="cardToCardView" data-role="page">
		<div id="cardToCardView_header" data-role="header" data-position="fixed" data-tap-toggle="false" data-theme="b">
			<a href="#" data-rel="back" data-icon="back" data-iconpos="notext">返回</a>
			<h2>文件</h2>
			<!-- <a href="#" data-icon="refresh" data-iconpos="notext">刷新</a> -->
		</div>
		<div id="cardToCardView_content" role="main" class="ui-content">
		</div>
		<div id="cardToCardView_footer" data-role="footer" data-position="fixed" data-tap-toggle="false" data-theme="b">
		</div>
	</div>
	
	<!--通讯录-->
	<div data-role="page" id="contacts" >
		<div data-role="header" data-theme="b" id="contacts_header" data-position="fixed" data-tap-toggle="false">
			<a href="#mbDesk" class="ui-btn ui-btn-icon-left ui-btn-icon-notext ui-icon-back">返回</a>
			<h2>通讯录</h2>
		</div>
		<div role="main" class="ui-content" id="contacts_content">
            <div class="contacts-content js-contacts-group contacts-content-active">
            	<div data-role="collapsible-set" id="contactsGroup">
            	</div>
            </div><!--contacts-group-->
            <!-- div class="contacts-content js-contacts-all">
            	<ul data-role="listview"  id="contactsList" data-icon="false" data-autodividers="true" data-swipable="true" style="position:relative;">
            	</ul>
            </div--><!--contacts-all-->
		</div><!-- /content -->
	</div><!-- /contacts -->
	<!-- 通讯录人员选择 -->
	<div data-role="page" id="contactsChoose">
		<div data-role="header" data-position="fixed" data-tap-toggle="false">
			<a href="#" class="ui-btn ui-btn-icon-left ui-btn-icon-notext ui-icon-back">返回</a>
			<h1>联系人</h1>
		</div>
		<div role="main" class="ui-content" id="contactsChoose_content" ></div>
    	<div data-role="footer" data-theme='b' id="contactsChoose_footer" data-position="fixed" data-tap-toggle="false">
    		<div data-role="navbar">
    			<ul><li>
    					<a href="#" data-rel="back" class="js-sc-cancel ui-link ui-btn ui-icon-cancel ui-btn-icon-top">取消</a>
    				</li><li>
    					<a href="#" class="js-sc-save ui-link ui-btn ui-icon-confirm ui-btn-icon-top">确认</a>
    				</li>
    			</ul>
    		</div>
    	</div>
	</div><!-- /contactsChoose -->
	<!-- 个人详情 -->
	<div data-role="page" id="contactDetail">
		<div data-role="header" data-theme="b" id="contactDetail_header" data-position="fixed" data-tap-toggle="false">
	    	<a href="#" data-rel="back" class="ui-btn ui-btn-icon-left ui-btn-icon-notext ui-icon-back">返回</a>
	        <h1></h1>
	    </div>
	    <div role="main" class="ui-content">
	            <ul data-role="listview" id="contactDetailList" data-icon="false">
	            </ul> 
	    </div><!-- /content -->
	</div><!-- /contactsDetail -->
	<!-- 领导日程 -->
	<div data-role="page" id="ldrAction">
		<div data-role="header" data-theme="b" id="ldrAction_header" data-position="fixed" data-tap-toggle="false">
			<a href="#mbDesk" class="ui-btn ui-btn-icon-left ui-btn-icon-notext ui-icon-back">返回</a>
			<h1></h1>
		</div>
		<div role="main" class="ui-content" id="ldrAction_content">
            <div class="ldrAction-content  ldrAction-content-active">
            	<div data-role="collapsible-set" id="ldrActionGroup">
            	</div>
            </div><!--ldrAction-group-->
		</div><!-- /ldrActioncontent -->
	</div>
	<!-- 领导日程详情 -->
	<div data-role="page" id="ldrActionDetail">
		<div  data-role="header" data-theme="b" id="ldrActionDetail_header" data-position="fixed" data-tap-toggle="false">
	    	<a href="#" data-rel="back" class="ui-btn ui-btn-icon-left ui-btn-icon-notext ui-icon-back">返回</a>
	        <h1></h1>
	    </div>
	    <div role="main" class="ui-content">
	    	<p align="center" style="font-weight:bold"><span id="weekStartDay"></span> —— <span id="weekEndDay"></span></p>
    		<div  style="text-align: center;">
	    		<a href="#" id="lastWeek" data-role="button" data-corners="true" data-mini="true" class="button" data-inline="true">上周</a>
	    		<a href="#" id="thisWeek" data-role="button" data-corners="true" data-mini="true" class="button" data-inline="true">本周</a>
	    		<a href="#" id="nextWeek" data-role="button" data-corners="true" data-mini="true" class="button"  data-inline="true">下周</a>
    		 </div>
    		<div data-role="content" >
    			<table id="actionTable" border="1" style="width: 100%; border-collapse: collapse; height=:100%">
    			</table>
    			<table width="100%"  border="0" id="note" >
						  <tr>
						    <td align="center"><font  style='font-weight:bold'>图&nbsp;例</font></td>
						    <td width="215px"> </td>
						  </tr>
						  <tr>
						    <td bgcolor='#0000fe'></td>
						    <td> 蓝色：表示为"会议"类型</td>
						  </tr>
						  <tr>
						    <td bgcolor='#ff9900'>&nbsp;</td>
						    <td> 橙色：表示为"活动"类型</td>
						  </tr>
						  <tr>
						    <td bgcolor='#fcff00'>&nbsp;</td>
						    <td> 黄色：表示为"出差"类型</td>
						  </tr>
						  <tr>
						    <td bgcolor='#fe0000'>&nbsp;</td>
						    <td> 红色：表示为"休假"类型</td>
						  </tr>
						</table>
    		</div>
	    </div><!-- /content -->
	</div><!-- /ldrActionDetail -->
	<!-- 单个领导一天日程 -->
	<div data-role="page" id="oneLdrAction">
		<div data-role="header" id="oneLdrAction_header" data-position="fixed" data-tap-toggle="false">
			<a href="#mbDesk" data-rel="back" class="ui-btn ui-btn-icon-left ui-btn-icon-notext ui-icon-back">返回</a>
			<h1></h1>
		</div>
		<div role="main" class="ui-content" id="oneLdrAction_content">
            <div data-role="content" >
    			<table id="oneDayActionTable" border="1" style="width: 100%; border-collapse: collapse; height=:100%">
    			</table>
    		</div>
		</div>
	</div>
</body>
</html>