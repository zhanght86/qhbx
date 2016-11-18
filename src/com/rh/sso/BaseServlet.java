package com.rh.sso;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.utils.StringUtils;
import org.apache.log4j.Logger;

import com.rh.core.base.Context;
import com.rh.core.base.Context.APP;
import com.rh.core.comm.CacheMgr;

/**
 * @author yangjy
 */
public class BaseServlet extends HttpServlet {
    
    /**
     * 
     */
    private static final long serialVersionUID = 3276577037439605905L;

    /**
     * 跳转URL
     */
    protected static final String REDIR_TO = "redirectUrl";
    
    /**
     * SSO ticket
     */
    protected static final String CACHE_TYPE = "SSO_TICKET";
    
    
    protected Logger log = Logger.getLogger(this.getClass());
    
    @Override
    protected void doGet(HttpServletRequest req , HttpServletResponse res) throws ServletException ,
        IOException {
        this.doPost(req, res);
    }
    
    protected void addCache(String key , String value) {
        CacheMgr.getInstance().set(key, value, CACHE_TYPE);
    }
    
    protected void removeCache(String key) {
        CacheMgr.getInstance().remove(key, CACHE_TYPE);
    }
    
    protected String getCache(String key) {
        return (String) CacheMgr.getInstance().get(key, CACHE_TYPE);
    }
    
    /**
     * 在URL上增加参数
     * @param url url参数
     * @param key 参数名
     * @param value 参数值
     */
    protected String addParameter(String url , String key , String value) {
        try {
            value = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        if (url.indexOf("?") > 0) {
            url += "&" + key + "=" + value;
        } else {
            url += "?" + key + "=" + value;
        }
        
        return url;
    }
    
    /**
     * 
     * @param url url
     * @return 在URL前面增加contextPath
     */
    protected String appendContextPath(String url) {
        String contextPath = Context.appStr(APP.CONTEXTPATH);
        if (!url.startsWith("/")) {
            return url;
        }
        if (!StringUtils.isEmpty(contextPath)) {
            return contextPath + url;
        }
        
        return url;
    }
}
