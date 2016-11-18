package com.rh.oa.mt;

import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;

/**
 * 添加常用组信息
 * @author ruaho_hdy 
 *
 */
public class MtFreQuentUserGroup extends CommonServ {

    @Override
    public OutBean save(ParamBean paramBean) {
        Bean isExitBean = isExitDept(paramBean);
        if (isExitBean.getBoolean("IS_EXIT")) {
            return new OutBean().setError(isExitBean.getStr("ERROR"));
        }
        return super.save(paramBean);
    }
    
    /**
     * 判断是否存在部门编码
     * @param paramBean 参数
     * @return 存在状态
     */
    private Bean isExitDept(ParamBean paramBean) {
        String deptCodes = paramBean.getStr("USER_CODES");
        String[] deptCodesArray = deptCodes.split(Constant.SEPARATOR);
        for (String dept : deptCodesArray) {
            SqlBean sql = new SqlBean();
            sql.selects("DEPT_CODE, DEPT_NAME").and("DEPT_CODE", dept);
            Bean deptBean = ServDao.find("SY_ORG_DEPT", sql);
            if (null != deptBean && StringUtils.isNotBlank(deptBean.getStr("DEPT_CODE"))) {
                return new Bean().set("ERROR" , "选择用户名为[" + deptBean.getStr("DEPT_NAME") + "]的不是用户，请从新选择")
                        .set("IS_EXIT", true);
            }
        }
        return new Bean().set("IS_EXIT", false);
    }
}
