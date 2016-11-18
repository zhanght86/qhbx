package com.rh.bn.tj.serv;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.core.util.JsonUtils;

/**
 * 印章统计服务类
 * @author tanyh 20150729
 *
 */
public class SealTjServ extends CommonServ{
    /** 印章使用统计服务 **/
    private final static String SEAL_USAGE_TJ_SERV = "BN_SEAL_USAGE_TJ_V";
    /**
     * 印章使用统计
     * @param paramBean
     * @return OutBean
     */
    public OutBean sealUsageTj(ParamBean paramBean){
        OutBean outBean = new OutBean();
        UserBean userBean = Context.getUserBean();
        // 获取查询条件
        StringBuffer strWhere = new StringBuffer("");
        // 默认本年度1月1号
        String startTime = String.valueOf(DateUtils.getYear()) + "-01-01";
        // 默认当前日期 
        String endTime = DateUtils.getDate();
        String cmpyLevel = "0";
        String zODeptCode = Context.getSyConf("BN_ZGS_DEPT_CODE", "00000002");
        // 判断默认当前用户所在机构是否为总公司
        if (userBean.getODeptCode().equals(zODeptCode)) {
            // 为总公司，则默认查询总公司各部门的用印数据
            cmpyLevel = "1";
        } else {
            // 为分公司，则默认查询省分公司及下属公司的用印数据
            cmpyLevel = "2";
        }
        // 获取查询条件
        if (paramBean.getStr("WHERE_JSON").length() > 0) {
            Bean strBean = JsonUtils.toBean(paramBean.getStr("WHERE_JSON"));
            if (strBean.getStr("CMPY_LEVEL").length() > 0) {
                cmpyLevel = strBean.getStr("CMPY_LEVEL");
            }
            // 选择了开始时间
            if (strBean.getStr("START_SEAL_TIME").length() > 0) {
                startTime = strBean.getStr("START_SEAL_TIME");
            }
            // 选择了结束时间
            if (strBean.getStr("END_SEAL_TIME").length() > 0) {
                endTime = strBean.getStr("END_SEAL_TIME");
            }
        }
        //拼写查询条件
        strWhere.append(" and substr(seal_time,0,10) >='" + startTime + "'");
        strWhere.append(" and substr(seal_time,0,10) <= '" + endTime + "'");
        // sql对象 
        SqlBean sqlBean = new SqlBean();
        sqlBean.selects("SEAL_CATALOG,SEAL_TYPE,COUNT(*) AS COUNT");
        // 分组处理
        sqlBean.groups("SEAL_CATALOG,SEAL_TYPE");
        // 存放结果对象
        List<Bean> resultList = new ArrayList<Bean>();
        // 查询总公司
        if ("1".equals(cmpyLevel)) {
            // 获取总公司下所有部门
            List<Bean> deptList = ServDao.finds(ServMgr.SY_ORG_DEPT, "  and DEPT_TYPE=" + Constant.DEPT_TYPE_DEPT
                    + " and ODEPT_CODE='" + zODeptCode + "' and DEPT_PCODE='"
                    + zODeptCode + "' and S_FLAG=1 order by DEPT_SORT");
            
            for (Bean dept : deptList) {
                // 根据部门依次统计
                sqlBean.set(Constant.PARAM_WHERE,
                        strWhere + " and ODEPT_CODE='" + zODeptCode
                                + "' and TDEPT_CODE='" + dept.getStr("DEPT_CODE") + "'");
                resultList.add(convertData(dept,sqlBean));
            }
        } else if ("2".equals(cmpyLevel)) { // 查询省分公司
            // 获取所有省分公司
            List<Bean> deptList = ServDao.finds(ServMgr.SY_ORG_DEPT, "  and DEPT_TYPE=" + Constant.DEPT_TYPE_ORG
                    + " and DEPT_PCODE='"
                    + zODeptCode + "' and S_FLAG=1 order by DEPT_SORT");
            for (Bean dept : deptList) {
                // 根据省分公司依次统计
                sqlBean.set(
                        Constant.PARAM_WHERE,
                        strWhere
                                + " and ODEPT_CODE in (select DEPT_CODE from SY_ORG_DEPT WHERE S_FLAG=1 and DEPT_TYPE=2 start with DEPT_CODE='"
                                + dept.getStr("DEPT_CODE") + "' connect by prior DEPT_CODE=DEPT_PCODE)");
                resultList.add(convertData(dept,sqlBean));
            }
        }
        //结果转为json格式传回前端
        outBean.set("RESULT_LIST", JsonUtils.toJson(resultList));
        outBean.setToDispatcher("/bn/seal/jsp/sealUsageTj.jsp");
        return outBean;
    }
    
    private Bean convertData(Bean dept, SqlBean sqlBean) {
        List<Bean> dataList = ServDao.finds(SEAL_USAGE_TJ_SERV, sqlBean);
        for (Bean data : dataList) {
            if ("SEAL_FOR_GW".equals(data.getStr("SEAL_CATALOG")) && "1".equals(data.getStr("SEAL_TYPE"))) {
                // 公文电子用印
                dept.set("SEAL_FOR_GW1", data.getInt("COUNT"));
            } else if ("SEAL_FOR_GW".equals(data.getStr("SEAL_CATALOG")) && "2".equals(data.getStr("SEAL_TYPE"))) {
                // 公文实物用印
                dept.set("SEAL_FOR_GW2", data.getInt("COUNT"));
            } else if ("SEAL_FOR_CT".equals(data.getStr("SEAL_CATALOG")) && "1".equals(data.getStr("SEAL_TYPE"))) {
                // 合同电子用印
                dept.set("SEAL_FOR_CT1", data.getInt("COUNT"));
            } else if ("SEAL_FOR_CT".equals(data.getStr("SEAL_CATALOG")) && "2".equals(data.getStr("SEAL_TYPE"))) {
                // 合同实物用印
                dept.set("SEAL_FOR_CT2", data.getInt("COUNT"));
            } else if ("SEAL_FOR_OTHER".equals(data.getStr("SEAL_CATALOG")) && "1".equals(data.getStr("SEAL_TYPE"))) {
                // 其它电子用印
                dept.set("SEAL_FOR_OTHER1", data.getInt("COUNT"));
            } else if ("SEAL_FOR_OTHER".equals(data.getStr("SEAL_CATALOG")) && "2".equals(data.getStr("SEAL_TYPE"))) {
                // 其它实物用印
                dept.set("SEAL_FOR_OTHER2", data.getInt("COUNT"));
            }
        }
        return dept;
    }
}
