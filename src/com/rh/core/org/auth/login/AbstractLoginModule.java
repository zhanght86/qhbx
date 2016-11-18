package com.rh.core.org.auth.login;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;

/**
 * 抽象的认证模块累
 * 
 * @author cuihf
 * 
 */
public abstract class AbstractLoginModule implements LoginModule {

    /**
     * 验证用户身份
     * 
     * @param paramBean 传入的参数对象
     * @return 验证后的用户Bean
     */
    public abstract UserBean authenticate(Bean paramBean);

    /**
     * 用户身份认证接口
     * 
     * @param paramBean 传入的参数
     * @return 认证结果，SessionID，以及USER_CODE
     */
    public OutBean login(ParamBean paramBean) {
        UserBean userBean = authenticate(paramBean);
        //设用在线用户信息
        Context.setOnlineUser(userBean);
        Bean user = Context.getOnlineUserState();
        OutBean outBean = new OutBean();
        outBean.setId(user.getId()).set("USER_CODE", user.getStr("USER_CODE"));
        return outBean;
    }
}
