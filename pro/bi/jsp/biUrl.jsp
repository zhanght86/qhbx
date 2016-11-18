<%@ include file="init.jsp" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
	//登陆超时
	if (userBean == null || Util.isNull(userBean.getId())) {
		response.sendRedirect("/bi/jsp/iframeError.jsp?errorKey=error_login_timeout");
	} else {
		String appId = request.getParameter("appId");
		//获取相应链接地址
		BiAppTreeView treeView = null;
		try {
			treeView = Bi.getTreeView(userBean.getLoginName(), appId);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("/bi/jsp/iframeError.jsp?errorKey=error_db_get_data_failed");
		}
		String actionUrl = treeView.getAppUrl();
		if (treeView.getType() == Const.TYPE_APP_SETTING) {
			String settingUrl = Context.getSyConf("bi_default_setting_url", "");
			if (Util.isNull(settingUrl)) {
				actionUrl = null;
			} else {
				actionUrl = settingUrl + actionUrl;
			}
		}
		if (Util.isNull(actionUrl)) {
			//response.sendRedirect("/bi-portlet/jsp/iframeError.jsp?errorKey=error_url_unreachable");
			response.sendRedirect("/bi/jsp/iframeError.jsp?errorKey=error_url_unreachable");
		}
		// oa审计
		String oaUserTracker = Context.getSyConf("bi_oa_user_tracker", Const.OA_USER_TRACKER);
		// bi审计
		String portalUserTracker = Context.getSyConf("bi_portal_user_tracker", Const.PORTAL_USER_TRACKER);
		if ("true".equals(oaUserTracker)) {
			//后续处理
			Bean OARecordBean = new Bean();
			//将Date类型转化为字符串类型
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");      
			String modifiedDate = format1.format(new Date());
			OARecordBean.set("APP_ID", appId);
			OARecordBean.set("MODIFIEDDATE",modifiedDate);
		    OARecordBean.set("UT_USERAGENT", treeView.getName());
	        OARecordBean.set("UT_REMOTEHOST", treeView.getCognosUser().getCUserId());
            OARecordBean.set("UT_REMOTEADDR", "BI_PORTLET");
            ServDao.create("BN_BI_USERTRACKER", OARecordBean);
 
		}
		if ("true".equals(portalUserTracker)) {
			try {
				Bi.savePortalUserTracker(treeView);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		response.sendRedirect(actionUrl);
	}
%>