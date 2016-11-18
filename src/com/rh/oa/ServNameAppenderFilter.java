package com.rh.oa;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * 在Response Header中增加服务器名称
 * @author yangjy
 *
 */
public class ServNameAppenderFilter implements Filter {


    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws ServletException,
            IOException {
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        if (System.getProperty("servName") != null) { //增加应用服务名称
            response.addHeader("appServName", System.getProperty("servName"));
        }

        chain.doFilter(servletRequest, servletResponse);
    }


    public void destroy() {

    }


    public void init(FilterConfig arg0) throws ServletException {

    }

}
