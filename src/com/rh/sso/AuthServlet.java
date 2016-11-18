package com.rh.sso;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.utils.StringUtils;
import org.apache.commons.lang.RandomStringUtils;

import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.util.Constant;
import com.rh.core.util.RequestUtils;

/**
 * @author yangjy
 */
public class AuthServlet extends BaseServlet {
    
    /**
	 * 
	 */
    private static final long serialVersionUID = -5500413246354999284L;
    
    
    @Override
    protected void doPost(HttpServletRequest req , HttpServletResponse res) throws ServletException ,
        IOException {
        req.setCharacterEncoding(Constant.ENCODING);
        res.setContentType("text/html; charset=utf-8");
        UserBean userBean = Context.getUserBean(req);
        if (userBean == null) {
            // 无Session
            redirToLogin(req, res);
        } else {
            // 有Session
            redirToApp(req, res, userBean);
        }
    }
    
    /**
     * @param req 请求
     * @param res
     */
    private void redirToLogin(HttpServletRequest req , HttpServletResponse res) {
        String service = req.getParameter(REDIR_TO);
        String loginUrl = "/login2/login.jsp";
        loginUrl = appendContextPath(loginUrl);
        if (!StringUtils.isEmpty(service)) {
            loginUrl = addParameter(loginUrl, REDIR_TO, service);
        }
        
        RequestUtils.sendDir(res, loginUrl);
    }
    
    /**
     * 有Session，则跳转到应用页面
     * 
     * @param req 请求对象
     * @param res 响应对象
     */
    private void redirToApp(HttpServletRequest req ,
        HttpServletResponse res ,
        UserBean userBean) {
        String service = req.getParameter(REDIR_TO);
        String dirUrl = null;
        String ticket = createToken(userBean);
        if (StringUtils.isEmpty(service)) { // 没有service
            dirUrl = appendContextPath("/?ticket=" + ticket);
        } else { // 有service
            dirUrl = addParameter(service, "ticket", ticket);
        }
        
        if(isIE(req)) {
        	res.setContentType("text/html; charset=utf-8");
	        req.setAttribute("redirTo", dirUrl);
	        RequestUtils.sendDisp(req, res, "/ssoRedir.jsp");
        } else {
        	RequestUtils.sendDir(res, dirUrl);
        }
    }
    
    /**
     * @return ticket
     */
    private String createToken(UserBean userBean) {
        StringBuilder ticket = new StringBuilder();
        ticket.append("RH-");
        ticket.append(RandomStringUtils.randomAlphanumeric(10));
        
        addCache(ticket.toString(), userBean.getId());
        
        return ticket.toString();
    }
    
    
    private boolean isIE(HttpServletRequest req) {
    	//User-Agent: Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36
        String userAgent = req.getHeader("User-Agent");
        int msie = userAgent.indexOf("MSIE ");
        if (msie > 0) {
             return true;
        }
        
        int trident = userAgent.indexOf("Trident/");
        if (trident > 0) {
        	return true;
        }

        return false;
    }
    
}
