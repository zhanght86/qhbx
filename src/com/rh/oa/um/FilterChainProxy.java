package com.rh.oa.um;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.util.AbstractConfigurationFilter;
import org.jasig.cas.client.util.AssertionHolder;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;

//import sso.entity.SwitchBean;
//import sso.singleton.SwitchManager;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.base.Bean;
import com.rh.core.base.Context;

public class FilterChainProxy extends AbstractConfigurationFilter {

    Logger log = Logger.getLogger(getClass());

    public FilterConfig config;
    
    private static final String COL_TDEPT_CODE = "TDEPT_CODE";
    
    private static final String COL_DEPT_CODE = "DEPT_CODE";
    
    private static final String COL_S_FLAG = "S_FLAG";
    
    private static final String COL_CMPY_CODE = "CMPY_CODE";
    
    private static final String COL_USER_LOGIN_NAME = "USER_LOGIN_NAME";
    
    private static final String COL_USER_WORK_NUM = "USER_WORK_NUM";

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        UserBean userBean = Context.getUserBean((HttpServletRequest) request);
        if (userBean == null) {
            HttpSession session = ((HttpServletRequest) request).getSession();
            Assertion assertion = AssertionHolder.getAssertion();
            String userName = assertion.getPrincipal().getName();
           
            if (!userName.equals("")) {
                if (userName.equals("admin001")) {
                    userName = "admin";
                }
                if (userName.equals("shanwenwen001")) {
                    userName = "shanwenwen";
                }
                if (userName.equals("houshudong001")) {
                    userName = "houshudong";
                }
                if (userName.equals("duanlixia001")) {
                    userName = "duanlixia";
                }
                try {
                    // 根据单点登录的账户的用户名，从数据库用户表查找用户信息response.sendRedirect(request.getContextPath()+"/page/login.jsp");
                    //request.getSession().setAttribute("GOTO_URL", request.getRequestURI().toString() + "?" + request.getQueryString());
                    //userBean = UserMgr.getUser(userName, "zhbx");
                    SqlBean sql = new SqlBean();
                    sql.and(COL_USER_LOGIN_NAME, userName).and(COL_CMPY_CODE, "zhbx").and(COL_S_FLAG, 1);
                    Bean bean = ServDao.find(ServMgr.SY_ORG_USER, sql);
                    
                    if(bean != null){
                        userBean = new UserBean(bean);
                        Context.setOnlineUser((HttpServletRequest) request, userBean); // 登录成功
                    }else{
                        ((HttpServletRequest) request).getSession().setAttribute("GOTO_URL", "/userNotExist.jsp");
                    }
                 } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            chain.doFilter(request, response);
            return;
        }
        // String URL = ((HttpServletRequest) request).getRequestURL().toString(); // 获取访问的url地址(不包括参数信息)

        
    }

    public void destroy() {
        System.out.println("oa取消");
    }

}
