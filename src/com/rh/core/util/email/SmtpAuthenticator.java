package com.rh.core.util.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * SMTP协议密码验证类
 * @author yangjinyun
 */
public class SmtpAuthenticator extends Authenticator {
    private String username = null;
    private String password = null;

    /**
     * 
     * @param username 用户名
     * @param password 密码
     */
    public SmtpAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * @return 返回密码验证对象
     */
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }

}
