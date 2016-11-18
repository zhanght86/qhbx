package com.rh.oa.cd.util;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.ServDao;
import com.rh.core.util.Constant;
import com.rh.oa.cd.AutoBean;

/**
 * 催督办帮助类
 * 
 * @author cuihf
 * 
 */
public class CdUtils {

    /**
     * 自动催督办的状态：未完成
     */
    public static final int STATUS_AUTO_UNFINISH = 0;

    /**
     * 自动催督办的状态：已完成
     */
    public static final int STATUS_AUTO_FINISHED = 1;

    /**
     * 自动催督办的类型：催办
     */
    public static final int TYPE_AUTO_REMD = 0;

    /**
     * 自动催督办的类型：督办
     */
    public static final int TYPE_AUTO_SUPE = 1;

    /**
     * 获取代字流水号
     * @param servId 服务ID
     * @param cdBean 催办或督办Bean
     * @param codeNum 代字流水号
     * @return 代字流水号
     */
    public static Bean getMaxCode(String servId, Bean cdBean, String codeNum) {
        cdBean.set(Constant.PARAM_SELECT, "max(" + codeNum + ") " + codeNum);
        Bean codeBean = ServDao.find(servId, cdBean);
        if (!codeBean.isEmpty(codeNum)) {
            codeBean.set(codeNum, codeBean.getInt(codeNum) + 1);
        } else {
            codeBean.set(codeNum, 1);
        }
        return codeBean;
    }

    /**
     * 增加自动催办信息
     * @param autoBean 自动催办信息
     * @param autoType 催督办类型
     * @return 自动催办信息
     */
    protected static final Bean add(AutoBean autoBean, int autoType) {
        Bean bean = new Bean();
        bean.set("SERV_ID", autoBean.getServId());
        bean.set("DATA_ID", autoBean.getDataId());
        bean.set("ACPT_USER", autoBean.getAcptUser());
        bean.set("AUTO_DATE", autoBean.getAutoDate());
        bean.set("AUTO_INTERVAL", autoBean.getAutoInterval());
        bean.set("AUTO_TITLE", autoBean.getTitle());
        bean.set("DRAFT_USER", autoBean.getDraftUser());
        /**
         * 根据ACPT_USER获取部门信息，写入Bean
         */
        UserBean userBean = UserMgr.getUser(autoBean.getAcptUser());
        bean.set("ACPT_DEPT", userBean.getDeptCode());
        bean.set("ACPT_TDEPT", userBean.getTDeptCode());
        bean.set("ACPT_ODEPT", userBean.getODeptCode());
        bean.set("ACPT_PHONE", userBean.getOfficePhone());

        bean.set("AUTO_STATUS", new Integer(CdUtils.STATUS_AUTO_UNFINISH));
        bean.set("AUTO_TYPE", new Integer(autoType));
        bean.set("AUTO_TIMES", new Integer(0));
        bean.set("AUTO_FAILS", new Integer(0));
        ServDao.save("OA_CD_AUTO", bean);

        return bean;
    }

    /**
     * 结束自动催办
     * @param autoBean 自动催办信息
     * @param autoType 催督办类型
     * @return 自动催办信息
     */
    protected static final Bean finish(AutoBean autoBean, int autoType) {
        Bean bean = new Bean();
        bean.set("SERV_ID", autoBean.getServId());
        bean.set("DATA_ID", autoBean.getDataId());
        bean.set("ACPT_USER", autoBean.getAcptUser());
        bean.set("AUTO_TYPE", new Integer(autoType));
        List<Bean> beanList = ServDao.finds("OA_CD_AUTO", bean);
        for (Bean tempBean : beanList) {
            tempBean.set("AUTO_STATUS", new Integer(CdUtils.STATUS_AUTO_FINISHED));
            ServDao.save("OA_CD_AUTO", tempBean);
        }

        return bean;
    }
}
