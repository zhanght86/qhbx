package com.rh.oa.gw.util;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.DeptBean;
import com.rh.core.org.mgr.OrgMgr;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.Constant;
import com.rh.oa.gw.GwTmplMgr;

/**
 * 用于审批单上的公共方法
 * @author chensheng
 *
 */
public class AuditUtils {
    /**
     * 取得指定模板的机关代字
     * @param tmplId 模板ID
     * @return 返回机关代字
     */
    public static String getOrgWord(final String tmplId) {
        DeptBean odeptBean = Context.getUserBean().getODeptBean();
        String deptId = Context.getUserBean().getTDeptCode();
        final int baseLevel = 4;
        final int level = odeptBean.getLevel();
        // 现支公司及以下取市公司的机关代子
        if (level > baseLevel) { 
            String codePath = odeptBean.getCodePath();
            if (codePath != null) {
                String[] codePathArr = codePath.split("\\" + Constant.CODE_PATH_SEPERATOR);
                // 如果这时CODE_PATH小于四级表示CODE_PATH有问题
                if (codePathArr.length < baseLevel - 1) {
                    throw new RuntimeException("部门" + odeptBean.getName() + "的CODE_PATH有错误！");
                }
                deptId = OrgMgr.getDept(codePathArr[3]).getCode();
            }
        }
        List<Bean> codeBeans = GwTmplMgr.getYearCodeList(deptId, tmplId);
        if (codeBeans.size() > 0) {
            return codeBeans.get(0).getStr("NAME");
        }
        return null;
    }
    
    /**
     * 按照年份和机构查出最大的流水号
     * @param paramBean 查询参数
     * @param servId 查询对应的服务ID
     * @param maxItem 查询最大流水号对应的字段
     * @return 返回最大的流水号
     */
    public static int getSerial(ParamBean paramBean, String servId, String maxItem) {
        int maxValue = 1;
        String max = "MAX(" + maxItem + ")";
        paramBean.set(Constant.PARAM_SELECT, " " + max);
        Bean result = ServDao.find(servId, paramBean);
        maxValue = result.getInt(max);
        if (maxValue <= 0) {
            maxValue = 1;
        } else {
            ++maxValue;
        }
        return maxValue;
    }
    
}
