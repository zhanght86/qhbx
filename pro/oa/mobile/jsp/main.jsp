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
	//	response.sendRedirect(Context.getSyConf("WX_FIREFLY_PATH", "http://localhost:8009")+"/oa/mobile/jsp/closeWindow.jsp");
	} else {
		Map orgMap = VarMgr.getOrgMap();
		orgMapJson = JsonUtils.mapsToJson(orgMap);
	}
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<title>图标化首页</title>
	<script>
		var 
			FireFlyContextPath, // ajax请求路径
			ZhbxImgPath;			// 图片加载路径
			
			// OA系统地址
			FireFlyContextPath = "<%=Context.getSyConf("WX_FIREFLY_PATH", "http://localhost:8009") %>";
			// 门户系统地址
			ZhbxImgPath = "<%=Context.getSyConf("WX_IMG_PATH", "http://localhost:8009") %>";
			
		var 
			userCode 	= "<%=userBean.getId()%>",
			orgMapJson 	= <%=orgMapJson%>, // 是一个json，不能加双引号
			homeUrl 	= window.location.href;
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
	<script type="text/javascript" src="/oa/mobile/js/mbWfCardView.js"></script>
	<script type="text/javascript" src="/oa/mobile/js/mbMind.js"></script>
	<script type="text/javascript" src="/oa/mobile/js/mbCardToCardView.js"></script>
	
	
<!-- *****************开发模式-- end **************** -->
</head>
<body>
	<!-- desktop首页 -->
	<div id="mbDesk" data-role="page" data-dom-cache="true" data-theme="b">
		<div id="mbDesk_header" data-role="header" data-position="fixed">
			<img class="zhbx-logo" src="/oa/mobile/images/zhbx-logo.png" />
			<h2 class="zhbx-username"><span></span></h2>
		</div>
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
	
	<!-- listview列表页 -->
	<div id="listview" data-role="page">
		<div id="listview_header" data-role="header" data-position="fixed" data-theme="b">
			<a href="#" data-rel="back" data-icon="back" data-iconpos="notext">返回</a>
			<h2>待办、已办、待阅事项、委托事务</h2>
			<!-- <a href="#" data-icon="refresh" data-iconpos="notext">刷新</a> -->
		</div>
		<div id="listview_content" role="main" class="ui-content">
		</div>
	</div>
	
	<!-- cardview卡片页 -->
	<div id="cardview" data-role="page">
		<div id="cardview_header" data-role="header" data-position="fixed" data-tap-toggle="false" data-theme="b">
			<a href="#" data-rel="back" data-icon="back" data-iconpos="notext">返回</a>
			<h2>表单名</h2>
			<!-- <a href="#" data-icon="refresh" data-iconpos="notext">刷新</a> -->
		</div>
		<div id="cardview_content" role="main" class="ui-content">
		</div>
		<div id="cardview_footer" data-role="footer" data-position="fixed" data-tap-toggle="false" data-theme="b">
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
			<h2>表单名</h2>
			<!-- <a href="#" data-icon="refresh" data-iconpos="notext">刷新</a> -->
		</div>
		<div id="cardToCardView_content" role="main" class="ui-content">
		</div>
		<div id="cardToCardView_footer" data-role="footer" data-position="fixed" data-tap-toggle="false" data-theme="b">
		</div>
	</div>
	
</body>
</html>