package com.rh.pt;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.EncryptUtils;

/**
 * @author liuxinhe
 * @version v1.0 创建时间：2013-6-19 上午10:39:25 类说明 AD域账号管理扩展类
 */
public class PtADUserServ extends CommonServ {

    /** 服务主键：用户开通系统服务 */
    public static final String PT_AD_USER = "PT_AD_USER";

    /**
     * 获得AD的用户对应平台用户的信息
     * 
     * @param paramBean
     *            参数
     * @return OutBean
     */
    public OutBean getADUser(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        List<Bean> datas = ServDao.finds(PT_AD_USER, paramBean);
        if (datas != null && datas.size() > 0) {
            Bean bean = datas.get(0);
            bean.set("USER_LOGIN_NAME", bean.getStr("USER_LOGIN_NAME"));
            bean.set("CMPY_CODE", bean.getStr("CMPY_CODE"));
            bean.set("USER_PASSWORD", EncryptUtils.decrypt(bean.getStr("USER_PASSWORD"), "DES"));
            outBean.setData(bean);
        }
        return outBean;
    }

    /**
     * AD单点登录
     * @param paramBean userName
     * @return outBean
     */
    public OutBean toADLogin(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        String homeUrl = "";
        String userCode = "";
            ParamBean paramADBean = new ParamBean();
            paramADBean.set("USER_LOGIN_NAME", paramBean.getStr("userName"));
            List<Bean> datas = ServDao.finds("SY_ORG_USER", paramADBean);
            if (datas != null && datas.size() > 0) {
                Bean bean = datas.get(0);
                userCode = bean.getStr("USER_CODE");
                if (userCode != null && userCode.length() > 0) {
                    //登录成功
                    //获取用户编码                
                    UserBean userBean = UserMgr.getUser(userCode);
                    Context.setOnlineUser(Context.getRequest(), userBean); //登录成功
                    homeUrl = "/sy/comm/page/portal.jsp";
                }
            }
        return outBean.set("homeUrl", homeUrl);
    }
}
