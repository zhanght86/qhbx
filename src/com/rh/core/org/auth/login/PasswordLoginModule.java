package com.rh.core.org.auth.login;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.EncryptUtils;
import com.rh.core.util.encoder.Base64;

/**
 * 基于用户名密码的认证模块
 * 
 * @author cuihf
 *
 */
public class PasswordLoginModule extends AbstractLoginModule {

    private static final String PARAM_LOGINNAME = "loginName";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_CMPYCODE = "cmpyCode";

    @Override
    public UserBean authenticate(Bean paramBean) {
        // 接受的参数：loginName,password,cmpyCode
        String loginName = paramBean.getStr(PARAM_LOGINNAME);
        String password = paramBean.getStr(PARAM_PASSWORD);
        String cmpyCode = paramBean.getStr(PARAM_CMPYCODE);

        if (loginName.length() <= 0 || password.length() <= 0 || cmpyCode.length() <= 0) {
            throw new RuntimeException("认证参数错误，用户名：" + loginName + ";公司编码：" + cmpyCode 
                    + ";密码长度：" + password.length());
        }
         password = new String(Base64.decode(password.getBytes()));
        //验证公司信息
        Bean cmpy = DictMgr.getItem("SY_ORG_CMPY", cmpyCode);
        if (cmpy == null) {
            throw new TipException(Context.getSyMsg("SY_CMPY_NOT_FOUND", cmpyCode));
        } else if (cmpy.getInt("FLAG") != Constant.YES_INT) { //公司被禁用
            throw new TipException(Context.getSyMsg("SY_CMPY_FORBIDDEN", cmpyCode));
        }
        if (Context.getSyConf("SY_ORG_LOGIN_NAME_LOWERCASE", true)) {
            loginName = loginName.toLowerCase();
        }
        UserBean userBean = UserMgr.getUser(loginName, cmpyCode);
        //加密后进行判断
        password = EncryptUtils.encrypt(password, 
                Context.getSyConf("SY_USER_PASSWORD_ENCRYPT", EncryptUtils.DES));
        if (!password.equals(userBean.getPassword())) {
            throw new RuntimeException("输入的密码错误。");
        }
        
        return userBean;
    }

}
