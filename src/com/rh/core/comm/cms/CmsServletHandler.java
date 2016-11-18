package com.rh.core.comm.cms;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rh.core.base.Bean;

/**
 * 
 * cms servlet 处理接口
 * 
 * @author liwei
 *
 */
public interface CmsServletHandler {
    
    /**
     * do get 处理
     * @param request - http request
     * @param data - data bean
     * @param response - http response
     * @throws ServletException - servlet exception
     * @throws IOException - io exception
     * @return html string
     */
     String doGet(HttpServletRequest request, Bean data, HttpServletResponse response)
            throws ServletException, IOException;

}
