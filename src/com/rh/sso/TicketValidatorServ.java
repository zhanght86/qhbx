package com.rh.sso;

import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.comm.CacheMgr;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.base.BaseServ;

/**
 * 验证ticket是否有效，仅允许指定服务器访问此服务。
 * 
 * @author yangjy
 */
public class TicketValidatorServ extends BaseServ {
    
    private static String[] items = { "USER_CODE", "USER_NAME", "USER_EMAIL",
            "USER_MOBILE", "USER_LOGIN_NAME", "USER_LOGIN_TYPE",
            "USER_OFFICE_PHONE", " CMPY_CODE", " DEPT_CODE" };
    
    /**
     * 
     * @param param
     * @return
     */
    public OutBean check(ParamBean param){
        
        OutBean outBean = new OutBean();
        
        if (!isValidHost()) {
            // 无效的服务器
            outBean.setError("Unauthorized server.");
            return outBean;
        }
        
        String userId = findUserByTicket(param);
        if (userId == null) {
            // 无效的ticket，验证不通过
            outBean.setError("Invalid  ticket.");
            return outBean;
        }
        
        UserBean userBean = UserMgr.getUser(userId);
        
        if (userBean == null) {
            // 无效的ticket，验证不通过
            outBean.setError("Invalid  user.");
            return outBean;
        }
        
        // 验证通过，返回用户信息
        outBean.putAll(toUserInfo(userBean));
        outBean.setOk();
        
        return outBean;
    }
    
    /**
     * 转换成其它应用需要的服务。
     * 
     * @param userBean 指定用户
     * @return 转换成其它应用需要的服务。
     */
    private Bean toUserInfo(UserBean userBean) {
        return userBean.copyOf(items);
    }
    
    /**
     * 只允许指定服务器可以访问此接口
     * 
     * @param req 请求
     * @return 是否有效的服务器
     */
    private boolean isValidHost() {
//        HttpServletRequest req
        return true;
    }
    
    /**
     * 检查Ticket是否有效
     * 
     * @param req 请求
     * @return ticket是否有效，无效返回false，否则返回true。
     */
    private String findUserByTicket(ParamBean param) {
        if (param.isEmpty("ticket")) {
            return null;
        }
        String ticket = param.getStr("ticket");
        
        String userCode = this.getCache(ticket);
        if (StringUtils.isEmpty(userCode)) {
            return null;
        }
        return userCode;
    }
    
    /**
     * SSO ticket
     */
    protected static final String CACHE_TYPE = "SSO_TICKET";
    
    protected String getCache(String key) {
        return (String) CacheMgr.getInstance().get(key, CACHE_TYPE);
    }
}
