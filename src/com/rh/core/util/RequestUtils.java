/*
 * Copyright (c) 2011 Ruaho All rights reserved.
 */
package com.rh.core.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.rh.core.base.Context;
import com.rh.core.base.Context.APP;
import com.rh.core.org.UserStateBean;
import com.rh.core.serv.ParamBean;

/**
 * <p>
 * 处理对于Request的一些得到客户提交参数和设置数据的方法
 * </p>
 * 
 * @author Jerry Li
 * @version $Id$
 */
public class RequestUtils {
	/** the logger */
	private static Logger logger = Logger.getLogger(RequestUtils.class);

	/**
	 * 对应字符串的简短显示格式，方便页面显示
	 * @param strview 要显示的字符串
	 * @param len 短显示格式字符串的长度
	 * 
	 * @return 指定字符串的短显示格式
	 */
	public static String shortView(String strview, int len) {
		int strlen = strview.length();
		String strshortview = "";

		if (strlen > len) {
			strshortview = strview.substring(0, len) + "...";
		} else {
			strshortview = strview;
		}

		return strshortview;
	}

	/**
	 * 得到Session中的指定对象。
	 * 
	 * @param req reqeust对象
	 * @param id Session中对象名称
	 * 
	 * @return Session中存放的对象
	 */
	public static Object getSession(HttpServletRequest req, String id) {
		HttpSession session = req.getSession(true);
        if (session == null) {
            return null;
        }
        return session.getAttribute(id);
	}

	/**
	 * 得到Session中的指定对象,如果为null，则返回缺省对象。
	 * 
	 * @param req reqeust对象
	 * @param id Session中对象名称
	 * @param defaultObj 缺省对象
	 * 
	 * @return Session中存放的对象
	 */
	public static Object getSession(HttpServletRequest req, String id, Object defaultObj) {
		Object obj = getSession(req, id);
		if (obj == null) {
			obj = defaultObj;
		}
		return obj;
	}

	/**
	 * 向session中存放对象。
	 * 
	 * @param req request对象
	 * @param id 对象对应键值
	 * @param value 要放到Session中的对象
	 */
	public static void setSession(HttpServletRequest req, String id, Object value) {
		HttpSession session = req.getSession(true);
		if (session.getAttribute(id) != null) { //兼容tomcat等设置session时自动触发已经存在的该session对象失效的情况。
		    session.removeAttribute(id);
		}
		session.setAttribute(id, value);
	}

	/**
	 * 移除Session中指定的对象。
	 * 
	 * @param req request对象
	 * @param id 对象键值
	 */
	public static void removeSession(HttpServletRequest req, String id) {
		HttpSession session = req.getSession(true);
		Object value = session.getAttribute(id);
		if (value != null) {
			session.removeAttribute(id);
		}
	}

	/**
     * 移除Session中所有的对象。
     * 
     * @param req request对象
     */
    public static void removeSession(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
    
	/**
	 * 对应解开客户端进行简单加密的字符串，进一步提高系统的安全性 原理：对应客户端加密的字符串进行拆解，转为Unicode对应的数字，对每一个数字进行恢复的反向调整。
	 * @param src 原加密字符串
	 * @return String 解密后的字符串
	 */
	public static String unEscapeStr(String src) {
		String ret = "";

		if (src == null) {
			return ret;
		}

		for (int i = src.length() - 1; i >= 0; i--) {
			int iCh = src.substring(i, i + 1).hashCode();

			if (iCh == 15) {
				iCh = 10;
			} else if (iCh == 16) {
				iCh = 13;
			} else if (iCh == 17) {
				iCh = 32;
			} else if (iCh == 18) {
				iCh = 9;
			} else {
				iCh = iCh - 5;
			}

			ret += (char) iCh;
		}

		// logger.debug("unEscape: input=" + src + "   output=" + ret);
		return ret;
	}

	/**
	 * 加密字符串，进一步提高系统的安全性
	 * @param src 未加密字符串
	 * @return String 加密后的字符串
	 */
	public static String escapeStr(String src) {
		String ret = "";

		if (src == null) {
			return ret;
		}

		for (int i = src.length() - 1; i >= 0; i--) {
			int iCh = src.substring(i, i + 1).hashCode();

			if (iCh == 10) {
				iCh = 15;
			} else if (iCh == 13) {
				iCh = 16;
			} else if (iCh == 32) {
				iCh = 17;
			} else if (iCh == 9) {
				iCh = 18;
			} else {
				iCh = iCh + 5;
			}

			ret += (char) iCh;
		}

		// logger.debug("unEscape: input=" + src + "   output=" + ret);
		return ret;
	}

	/**
	 * 将html标记转化为规定标示符
	 * 
	 * @param s 要转换的HTML
	 * @return HTML对应的文本
	 */
	public static String escapeHTML(String s) {
		if ((s == null) || (s.length() == 0)) {
			return s;
		}
		int len = s.length();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);     
            switch (c) {
            case 60: // '<'
                sb.append("&lt;");
                break;
            case 62: // '>'
                sb.append("&gt;");
                break;
            case 10: // '\n'
                sb.append("<br>");
                break;
            case 13: // '\r'
                sb.append("<br>");
                i++;
                break;
            case 32: // ' '
                sb.append("&nbsp;");
                break;
            case 39: // '\''
                sb.append("&acute;");
                break;
            case 34: // '"'
                sb.append("&quot;");
                break;
            default:     
                sb.append(c);    
            }
        }
        return sb.toString(); 
	}

	/**
	 * 加密方法，
	 * 
	 * @param str 加密对象
	 * @return 加密后的对象
	 */
	public static String encodeStr(String str) {
		return encodeStr(str, "UTF-8");
	}

	/**
	 * 加密方法，
	 * 
	 * @param str 加密对象
	 * @param encoding 字符集
	 * @return 加密后的对象
	 */
	public static String encodeStr(String str, String encoding) {
		try {
			return URLEncoder.encode(str, encoding);
		} catch (Exception e) {
			logger.debug("编码错误：" + str, e);
			return str;
		}
	}

	/**
	 * 解码方法，采用缺省字符集
	 * @param str 解码对象
	 * @return 解码后的对象
	 */
	public static String decodeStr(String str) {
		return decodeStr(str, "UTF-8");
	}

	/**
	 * 解码方法
	 * @param str 解码对象
	 * @param encoding 解码采用字符集
	 * @return 解密后的对象
	 */
	public static String decodeStr(String str, String encoding) {
		try {
			return URLDecoder.decode(str, encoding);
		} catch (Exception e) {
			logger.debug("解码错误：" + str, e);
			return str;
		}
	}

	/**
	 * 通过Direct方式向JSP或Servlet跳转.
	 * 
	 * @param res response对象
	 * @param url 目标的URL相对路径
	 */
	public static void sendDir(HttpServletResponse res, String url) {
		try {
			res.sendRedirect(url);
		} catch (java.io.IOException e) {
			logger.error("Unable to redirect to /" + url, e);
		}
	}

	/**
	 * 通过Dispatcher方式向JSP或Servlet跳转.
	 * 
	 * @param req reqeust对象
	 * @param res response对象
	 * @param url 目标的URL相对路径
	 */
	public static void sendDisp(HttpServletRequest req, HttpServletResponse res, String url) {
		RequestDispatcher rd = null;
		if (!url.substring(0, 1).equals("/")) {
			url = "/" + url;
		}
		rd = req.getRequestDispatcher(url);
		try {
			rd.forward(req, res);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
     * 得到对应的URL的全名称信息及虚路径，比如：http://www.sina.com.cn:81/ruaho
     * 先从request中获取，如果取不到则从系统配置中获取URL，系统配置键值为:SY_HTTP_URL
     * @return URL全名称字符串
     */
    public static String getHttpURL() {
        String url = null;
        UserStateBean userState = Context.getOnlineUserState();
        if (userState != null) {
            url = userState.getHttpUrl();
            if (url.length() == 0) {
                url = getHttpURLByRequest(Context.getRequest());
                if (url != null) {
                    userState.setHttpUrl(url);
                }
            }
        }
        if (url == null) { //如果从用户上获取不到，则从系统配置中获取
            url = Context.getSyConf("SY_HTTP_URL", "");
            url = url + Context.appStr(APP.CONTEXTPATH);
        }
        return url;
    }
    
	/**
	 * 根据reuqest对象得到对应的URL的全名称信息及虚路径，比如：http://www.sina.com.cn:81/ruaho
	 * @param request request对象
	 * @return URL全名称字符串
	 */
	public static String getHttpURL(HttpServletRequest request) {
	    Context.setRequest(request);
	    return getHttpURL();
	}

	/**
     * 根据reuqest对象得到对应的URL的全名称信息及虚路径，比如：http://www.sina.com.cn:81/ruaho
     * @param request request对象
     * @return URL全名称字符串
     */
    public static String getHttpURLByRequest(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        StringBuilder sbURL = new StringBuilder();
        String[] protocol = request.getProtocol().split("/"); // HTTP/1.1,"HTTP","1.1"
        sbURL.append(protocol[0]).append("://").append(request.getServerName());
        int port = request.getServerPort(); // 
        if (port != 80) { // 80
            sbURL.append(":").append(port);
        }
        sbURL.append(Context.appStr(APP.CONTEXTPATH));
        return sbURL.toString();
    }
    
	/**
	 * 获取IP地址
	 * @param request request对象
	 * @return IP地址
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 设置下载文件显示名称
	 * @param request 请求头
	 * @param response 响应头
	 * @param fileName 文件名称
	 */
	public static void setDownFileName(HttpServletRequest request, HttpServletResponse response, String fileName) {
		String userbrowser = "unknow";
		try {
		    userbrowser = request.getHeader("User-Agent");
			if (-1 < userbrowser.indexOf("MSIE 6.0") || -1 < userbrowser.indexOf("MSIE 7.0")) {
				//IE6、7
				response.addHeader("content-disposition", "attachment;filename="
						// + new String(fileName.getBytes(), "ISO8859-1"));
						+ URLEncoder.encode(fileName, "UTF-8"));
			} else if (-1 < userbrowser.indexOf("MSIE 9.0") || -1 < userbrowser.indexOf("MSIE 8.0")) {
				//IE8、9
				response.addHeader("content-disposition", "attachment;filename=" 
			+ URLEncoder.encode(fileName, "UTF-8"));
			} else if (-1 < userbrowser.indexOf("Chrome")) {
				//chrome
				response.addHeader("content-disposition",
						"attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
			} else if (-1 < userbrowser.indexOf("Safari")) {
				//safari
				response.addHeader("content-disposition", "attachment;filename="
						+ new String(fileName.getBytes(), "ISO8859-1"));
			} else {
				//other brower
				response.addHeader("content-disposition",
						"attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
			}
		} catch (Exception e) {
			logger.warn("get user browser error. agent info:" + userbrowser, e);
		}
	}

	/**
	 * 
	 * @param request 客户端请求
	 * @return  参数Bean
	 */
    @SuppressWarnings("unchecked")
    public static ParamBean transParam(HttpServletRequest request) {
        ParamBean param;
        String data = request.getParameter("data");
        if (data != null) {
            param = new ParamBean(JsonUtils.toBean(data));
        } else {
            param = new ParamBean();
        }
        //获取其他参数信息
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String pName = paramNames.nextElement();
            if (!pName.equals("data")) { // 不再处理data中的数据
                String value = RequestUtils.getStr(request, pName);
                if (pName.equals(Constant.KEY_ID)) { // 预处理直接传主键的机制
                    param.setId(value);
                } else {
                    param.set(pName, value);
                }
            }
        }
        param.remove(Constant.PARAM_JSON_RANDOM); //去掉客户端的 随机参数	    
	    return param;
	}

	/**
	 * 获取request参数
	 * @param request http request
	 * @param param 参数
	 * @return 参数值
	 */
	public static String getStr(HttpServletRequest request, String param) {
		String value = request.getParameter(param);
		if (value == null) {
			value = "";
		}
		return value;
	}

	/**
	 * 获取request参数
	 * @param request http request
	 * @param param 参数
	 * @param def 缺省值
	 * @return 参数值
	 */
	public static String get(HttpServletRequest request, String param, String def) {
		String value = request.getParameter(param);
		if (value == null) {
			value = def;
		}
		return value;
	}

	/**
	 * 获取request参数
	 * @param request http request
	 * @param param 参数
	 * @param def 缺省值
	 * @return 参数值
	 */
	public static int get(HttpServletRequest request, String param, int def) {
		String value = request.getParameter(param);
		if (value != null) {
			return Integer.parseInt(value);
		} else {
			return def;
		}
	}

	/**
	 * 获取request参数
	 * @param request http request
	 * @param param 参数
	 * @param def 缺省值
	 * @return 参数值
	 */
	public static boolean get(HttpServletRequest request, String param, boolean def) {
		String value = request.getParameter(param);
		if (value != null) {
			return Boolean.parseBoolean(value);
		} else {
			return def;
		}
	}
	
	/**
	 * 客户端是否使用msie浏览器
	 * @param request 请求
	 * @return 客户端是否使用msie浏览器，是返回true，否返回false
	 */
    public static boolean isMsie(HttpServletRequest request) {
        String userbrowser = request.getHeader("User-Agent");
        if (userbrowser != null && userbrowser.indexOf("MSIE") >= 0) {
            return true;
        }

        return false;
    }
    
    /**
     * 获取本机地址，如果系统集群则返回系统配置中的配置地址
     * @return 本机地址
     */
    public static String getSysHost() {
        // 添加一个外部待办URL，用于外系统访问，比如UC
        HttpServletRequest request = Context.getRequest();
        String serverAddre = "";
        if (request != null) {
            // 服务器地址
            serverAddre = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        }

        return Context.getSyConf(Constant.CONF_SYS_HOST_ADDR, serverAddre);
    }
}