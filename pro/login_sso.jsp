<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page import="com.rh.core.base.TipException"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.rh.core.org.mgr.UserMgr"%>
<%@ page import="com.rh.core.util.RequestUtils"%>
<%@ page import="com.rh.core.base.Context"%>
<%@ page import="com.rh.core.org.UserBean"%>
<%@ page import="com.rh.client.RHSSO"%>
<%@ page import="com.rh.client.SSOUser"%>
<%@ page import="com.rh.core.util.Constant"%>
<%@ page import="com.rh.core.serv.bean.SqlBean"%>
<%@ page import="com.rh.core.serv.ServDao"%>
<%@ page import="com.rh.core.base.Bean"%>

<%
	final String CONTEXT_PATH = request.getContextPath();
	UserBean userBean = Context.getUserBean(request);
	
    String homeUrl = "";
	if (userBean == null) {
		String portalURL = Context.getSyConf("SY_PORTAL_URL", null);// 门户地址
		String clientURL = getSysHostUrl(request);// 客户端地址
		// 配置了门户地址则默认启用了单点登陆
		if (portalURL != null) {
			SSOUser user = new RHSSO(portalURL).getUser(request,
					response, clientURL.toLowerCase());
			if (user != null) {//登录成功
				//获取用户编码
				String userId = user.getID();
				//获取用户名称
				String userName = user.getName();
				try {
					UserMgr.clearSelfUserCache(UserMgr.getUser(userId));
					userBean = UserMgr.getUser(userId);
					Context.setOnlineUser(request, userBean); //登录成功
					String gotoUrl = (String) request.getSession()
							.getAttribute("GOTO_URL");
					request.getSession().removeAttribute("GOTO_URL");

					if (gotoUrl != null) {
						homeUrl = gotoUrl;
					} else {
						homeUrl = "sy/comm/page/page.jsp";
					}
				} catch (TipException e) {
					RequestUtils.removeSession(request);
					out.println("用户" + userName + "还没有同步到OA系统！");
					homeUrl = "/userNotExist.jsp";
				}
			} else {
				return;
			}
		}
	} else {
		homeUrl = "sy/comm/page/page.jsp";
	}
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="servName" content="<%=System.getProperty("servName")%>" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>正在进行登陆跳转...</title>
<link rel="shortcut icon" href="favicon.ico" />
<script type="text/javascript">
/**
 * rh封装的判断浏览器类别
 * 如：Browser.versions.iPad
 */
var Browser = {
	versions:function() {
		var u = navigator.userAgent, app = navigator.appVersion;
		return {
			trident:u.indexOf('Trident') > -1, // IE内核
			presto:u.indexOf('Presto') > -1, // opera内核
			webKit:u.indexOf('AppleWebKit') > -1, // 苹果、谷歌内核
			gecko:u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, // 火狐内核
			mobile:!!u.match(/AppleWebKit.*Mobile.*/)
					|| !!u.match(/AppleWebKit/), // 是否为移动终端
			ios:!!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), // ios终端
			android:u.indexOf('Android') > -1,// || u.indexOf('Linux') > -1, // android终端或者uc浏览器
			iPhone:u.indexOf('iPhone') > -1, // 是否为iPhone或者QQHD浏览器 || u.indexOf('Mac') > -1
			iPad:u.indexOf('iPad') > -1, // 是否iPad
			webApp:u.indexOf('Safari') == -1
		};
	}
};

var homeUrl = "<%=homeUrl%>";
if (homeUrl.length == 0) {
	// 移动版页面
	/* if (Browser.versions().android || Browser.versions().iPhone) {
		homeUrl = "sy/comm/desk-mb/desk-mb.jsp";
	} else { */
		homeUrl = "<%=CONTEXT_PATH%>/index.jsp";
	/*}*/
} /* else if (homeUrl == "sy/comm/page/page.jsp") { // 避免PC版干扰移动版
	if (Browser.versions().android || Browser.versions().iPhone) {
		homeUrl = "sy/comm/desk-mb/desk-mb.jsp";
	}
} */
// 设置cookie
document.cookie = "RhClientLogin=true";
window.location.href = homeUrl;	
</script>
</head>
<body>
</body>
</html>
<%!private String getSysHostUrl(HttpServletRequest request) {
		String url = Context.getSyConf(Constant.CONF_SYS_HOST_ADDR, "");
		if (url.length() == 0) {
			url = RequestUtils.getHttpURLByRequest(request);
		}
		return url;
	}%>
