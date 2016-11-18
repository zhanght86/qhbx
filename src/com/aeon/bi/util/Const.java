package com.aeon.bi.util;

public class Const {
	
	public final static String DB_CONNECTION_URL = "java:comp/env/jdbc/biDBConnection";
	
	public final static long DEFAULT_APP_TREE_VIEW_ROOT_ID = 2;
	
	public final static int TYPE_APP_SETTING = 0;
	
	public final static int TYPE_APP_NORMAL = 1;
	
	public final static int TYPE_APP_COGNOS = 2;
	
	public final static String FORMAT_SETTING_EXT_URL = "&un=%s&pw=%s&uri=%s&isMD5=true";
	
	public final static String FORMAT_COGNOS_EXT_URL = 
		"&CAMUsername=%s&CAMPassword=%s&p_Date=%s&p_SDate=%s&p_EDate=%s&p_Month=%s&p_SMonth=%s&p_EMonth=%s&run.prompt=false";
	
	public final static String DEFAULT_PORTLET_HEIGHT = "520px";
	
	public final static String DEFAULT_TREEVIEW_WIDTH = "20%";
	
	public final static String OA_USER_TRACKER = "false";
	
	public final static String PORTAL_USER_TRACKER = "false";
	
	public final static String DEFAULT_IS_DEBUG = "false";
}