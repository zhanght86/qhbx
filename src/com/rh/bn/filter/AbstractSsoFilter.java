package com.rh.bn.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.org.UserBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;

/**
 * 处理单点登录的过滤器
 * 
 * @author cuihf
 * 
 */
public abstract class AbstractSsoFilter implements Filter {
    
    private static Logger log = Logger.getLogger(AbstractSsoFilter.class);

    /**
     * 编码
     */
    protected String encoding = null;

    /**
     * 过滤器配置
     */
    protected FilterConfig filterConfig = null;

    /**
     * 登录名
     */
    protected static final String KEY_LOGIN_NAME = "USER_LOGIN_NAME";

    /**
     * 工号
     */
    protected static final String KEY_WORK_NUM = "USER_WORK_NUM";

    public void destroy() {
        this.encoding = null;
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException,
            IOException {
        if (null == request.getCharacterEncoding()) {
            if (this.encoding != null) {
                request.setCharacterEncoding(this.encoding);
            }
        }

        // 判断Request对象中是否包含验证串，如果没有验证串，则不进行身份认证
        String accountInfo = getAccountInfo(request);
        if (accountInfo == null) {
            chain.doFilter(request, response);
            return;
        } else {
            // 取当前用户信息，如果当前用户的验证串与Request中的相同，则不进行身份认证
            try {
                if (Context.getUserBean() != null) {
                    if (Context.getUserBean().getStr(getAccountKey()).equals(accountInfo)) {
                        Context.setOnlineUser((HttpServletRequest) request, Context.getUserBean());
                        chain.doFilter(request, response);
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }

        // 根据accountInfo获取UserBean
        UserBean userBean = getUserBean(accountInfo);
        // 登录
        if (userBean != null) {
            Context.setOnlineUser((HttpServletRequest) request, userBean);
        }

        chain.doFilter(request, response);
    }

    public void init(FilterConfig chain) throws ServletException {
        this.filterConfig = chain;
        this.encoding = chain.getInitParameter("encoding");
    }

    /**
     * 子类中应调用相应的接口获取验证信息
     * @param request request
     * @return 验证串信息
     */
    protected abstract String getAccountInfo(ServletRequest request);

    /**
     * 取验证key。一般为登录名或工号
     * @return 验证key
     */
    protected String getAccountKey() {
        return KEY_WORK_NUM;
    }

    /**
     * 默认通过工号获取UserBean
     * @param accountInfo 帐号信息
     * @return 用户对象UserBean
     */
    protected UserBean getUserBean(String accountInfo) {
        Bean paramBean = new Bean();
        paramBean.set(getAccountKey(), accountInfo).set("S_FLAG", 1);
        Bean bean = ServDao.find(ServMgr.SY_ORG_USER, paramBean);
        if (bean == null) {
            throw new TipException(Context.getSyMsg("SY_USER_NOT_FOUND", accountInfo));
        }
        return new UserBean(bean);
    }

}
