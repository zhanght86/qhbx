package com.rh.bn.filter.impl;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jcifs.smb.NtlmPasswordAuthentication;

import com.rh.bn.filter.AbstractSsoFilter;

/**
 * 域认证过滤类，用户认证成功后，进行OA登录
 * @author Tanyh
 *
 */
public class BnNtlmFilter extends AbstractSsoFilter{
    
    protected String getAccountInfo(ServletRequest req) {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpSession session = request.getSession();
        NtlmPasswordAuthentication ntlm = null;
        if ((session == null) || ((ntlm = (NtlmPasswordAuthentication) session.getAttribute("NtlmHttpAuth")) == null)) {
            return null;
        }
        return ntlm.getUsername();
    }
    
    @Override
    protected String getAccountKey() {
        return KEY_LOGIN_NAME;
    }

}
