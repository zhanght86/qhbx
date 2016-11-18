package com.rh.bn.job;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.db.Transaction;
import com.rh.core.comm.schedule.job.RhJob;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;

public class OrgAndUserSyncJob implements RhJob{
    private static final Log log = LogFactory.getLog(OrgAndUserSyncJob.class);
    /**公司临时服务**/
    private static final String CMPY_TEMP_SERV = "BN_ORG_COMPANY_TEMP";
    /**部门临时服务**/
    private static final String DEPT_TEMP_SERV = "BN_ORG_DEPT_TEMP";
    /**用户临时服务**/
    private static final String USER_TEMP_SERV = "BN_ORG_USER_TEMP";
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        //HR系统中百年人寿股份有限公司编码
        String cmpyCode = Context.getSyConf("BN_CMPY_CODE_HR", "0001B4100000000074UD");
        //获取公司临时表中数据，只获取百年人寿股份有限公司的数据
        List<Bean> cmpyBeanList = ServDao.finds(CMPY_TEMP_SERV,
                " connect by prior cmpy_code=cmpy_pcode start with cmpy_code='" + 
                        cmpyCode +"'");
        for (Bean cmpy : cmpyBeanList) {
            try {
                Transaction.begin();
                if (cmpy.getStr("CMPY_CODE").equals(cmpyCode)) { //根数据不处理
                    continue;
                }
                //3rin6giCR9vUIv6kHIO3ex
                //机构转为部门
                Bean cmpyBean = new Bean();
                cmpyBean.set("DEPT_CODE", cmpy.getStr("CMPY_CODE"));
                cmpyBean.set("DEPT_NAME", cmpy.getStr("CMPY_NAME"));
                cmpyBean.set("DEPT_FULL_NAME", cmpy.getStr("CMPY_NAME"));
                cmpyBean.set("DEPT_TYPE", "2");
                if (cmpy.getStr("CMPY_PCODE").equals(cmpyCode)) {
                    //上级机构为根数据
                    if ("0001B210000000000BU3".equals(cmpy.getStr("CMPY_CODE"))) {//寿险总公司
                        cmpyBean.set("DEPT_PCODE", "3rin6giCR9vUIv6kHIO3ex");
                        cmpyBean.set("DEPT_LEVEL", "2");
                        cmpyBean.set("CODE_PATH", "3rin6giCR9vUIv6kHIO3ex^" + cmpy.getStr("CMPY_CODE") + "^");
                    } else {//分公司，则归属至寿险总公司下
                        cmpyBean.set("DEPT_PCODE", "0001B210000000000BU3");
                        cmpyBean.set("DEPT_LEVEL", "3");
                        cmpyBean.set("CODE_PATH", "3rin6giCR9vUIv6kHIO3ex^0001B210000000000BU3^" + cmpy.getStr("CMPY_CODE") + "^");
                    }
                } else {
                    //获取上级机构信息
                    Bean pCmpyBean = ServDao.find("SY_ORG_DEPT", cmpy.getStr("CMPY_PCODE"));
                    if (pCmpyBean == null || pCmpyBean.isEmpty()) {
                        log.error("机构：" + cmpy.getStr("CMPY_NAME") + "，未找到上级机构：" + cmpy.getStr("CMPY_PCODE"));
                        continue;
                    }
                    cmpyBean.set("DEPT_PCODE", cmpy.getStr("CMPY_PCODE"));
                    cmpyBean.set("DEPT_LEVEL", pCmpyBean.getInt("DEPT_LEVEL") + 1);
                    cmpyBean.set("CODE_PATH", pCmpyBean.getStr("CODE_PATH") + cmpy.getStr("CMPY_CODE") + "^");
                }
                cmpyBean.set("ODEPT_CODE", cmpy.getStr("CMPY_CODE"));
                cmpyBean.set("TDEPT_CODE", cmpy.getStr("CMPY_CODE"));
                cmpyBean.set("CMPY_CODE", "zhbx");
                cmpyBean.set("S_USER", "1Xvd5e5X50O8J0Ir-0zlwd");
                ServDao.create("SY_ORG_DEPT", cmpyBean);
                Transaction.commit();
                //获取该机构下的一级部门
                List<Bean> topDeptBeanList = ServDao.finds(DEPT_TEMP_SERV, " and cmpy_code='" + cmpy.getStr("CMPY_CODE") + "' and  (dept_pcode='' or dept_pcode is null) ");
                for (Bean topDept : topDeptBeanList) {
                    Bean topDeptBean = null;
                    try{
                        topDeptBean = createNewDeptBean(topDept, cmpyBean);
                        ServDao.create("SY_ORG_DEPT", topDeptBean);
                        //处理该部门下的用户
                        createUserBean(topDeptBean.getStr("DEPT_CODE"));
                        Transaction.commit();
                    } catch (Exception e) {
                        log.error("部门：" + topDeptBean.getStr("DEPT_NAME") + "，部门code：" + topDeptBean.getStr("DEPT_CODE") + "数据转换失败！" + e.getMessage());
                        continue;
                    }
                    // 获取下属部门
                    List<Bean> lDeptBeanList = ServDao.finds(
                            DEPT_TEMP_SERV,
                            " and cmpy_code='" + cmpy.getStr("CMPY_CODE")
                                    + "' connect by prior dept_code=dept_pcode start with dept_code='"
                                    + topDeptBean.getStr("DEPT_CODE") + "'");
                    for (Bean lDept : lDeptBeanList) {
                        if (lDept.getStr("DEPT_CODE").equals(topDeptBean.getStr("DEPT_CODE"))) {
                            continue;
                        }
                        //获取上级部门
                        Bean upDeptBean = ServDao.find("SY_ORG_DEPT", lDept.getStr("DEPT_PCODE"));
                        if (upDeptBean == null || upDeptBean.isEmpty()) {
                            log.error("部门：" + lDept.getStr("DEPT_NAME") + "，未找到上级部门：" + lDept.getStr("DEPT_PCODE"));
                            continue;
                        }
                        Bean lDeptBean = createNewDeptBean(lDept, topDeptBean);
                        try {
                            ServDao.create("SY_ORG_DEPT", lDeptBean);
                            // 处理该部门下的用户
                            createUserBean(lDeptBean.getStr("DEPT_CODE"));
                            Transaction.commit();
                        } catch (Exception e) {
                            log.error("部门：" + lDeptBean.getStr("DEPT_NAME") + "，部门code："
                                    + lDeptBean.getStr("DEPT_CODE") + "数据转换失败！" + e.getMessage());
                            continue;
                        }
                    }
                }
            } catch (Exception e) {
                log.error("数据转换失败！" + e.getMessage());
            } finally {
                Transaction.end();
            }
            
        }
    }
    private void createUserBean(String deptCode) {
      //获取用户
        List<Bean> userTempList = ServDao.finds(USER_TEMP_SERV, " and dept_code='" + deptCode + "'");
        for (Bean user : userTempList) {
            try {
                Bean userBean = new Bean();
                userBean.set("USER_CODE", user.getStr("USER_CODE"));
                userBean.set("USER_NAME", user.getStr("USER_NAME"));
                // 设置用户邮箱名为系统登录名，如果用户没有填写邮箱，则取用户工号
                if (user.getStr("USER_EMAIL").length() > 0) {
                    userBean.set("USER_LOGIN_NAME", user.getStr("USER_EMAIL").split("@")[0]);
                } else {
                    userBean.set("USER_LOGIN_NAME", user.getStr("USER_WORK_NUM"));
                }
                userBean.set("USER_SPELLING", userBean.getStr("USER_LOGIN_NAME"));
                userBean.set("DEPT_CODE", user.getStr("DEPT_CODE"));
                userBean.set("USER_PASSWORD", "e10adc3949ba59abbe56e057f20f883e");// 用户初始密码为123456
                userBean.set("USER_EMAIL", user.getStr("USER_EMAIL"));
                userBean.set("USER_WORK_NUM", user.getStr("USER_WORK_NUM"));
                userBean.set("USER_MOBILE", user.getStr("USER_MOBILE"));
                userBean.set("USER_OFFICE_PHONE", user.getStr("USER_OFFICE_PHONE"));
                // 设置用户在职状态
                if ("N".equals(user.getStr("USER_STATE"))) {// 在职
                    userBean.set("USER_STATE", 1);
                    userBean.set("S_FLAG", 1);
                } else {
                    if ("07".equals(user.getStr("USER_STATE_TYPE"))) {// 离职
                        userBean.set("USER_STATE", 2);
                        userBean.set("S_FLAG", 2);
                    } else if ("08".equals(user.getStr("USER_STATE_TYPE"))) {
                        userBean.set("USER_STATE", 3);
                        userBean.set("S_FLAG", 2);
                    } else {// 非正常用户
                        userBean.set("USER_STATE", 4);
                        userBean.set("S_FLAG", 2);
                    }
                }
                userBean.set("CMPY_CODE", "zhbx");
                userBean.set("S_USER", "1Xvd5e5X50O8J0Ir-0zlwd");
                //判断用户登录名是否已存在
                int count = ServDao.count("SY_ORG_USER", (new ParamBean()).setWhere(" and USER_LOGIN_NAME='" + userBean.getStr("USER_LOGIN_NAME") + "' "));
                if (count > 0) {//用户登录名已存在，则以工号作为登录名
                    userBean.set("USER_LOGIN_NAME", userBean.getStr("USER_WORK_NUM"));
                    userBean.set("USER_STATE", 5);
                }
                ServDao.create("SY_ORG_USER", userBean);
            } catch (Exception e) {
                log.error("用户：" + user.getStr("USER_NAME") + "，部门code：" + user.getStr("DEPT_CODE") + "； 数据转换失败！"
                        + e.getMessage());
            }
        }
    }
    private Bean createNewDeptBean(Bean oldDept, Bean pDept) {
        Bean newDept = new Bean();
        newDept.set("DEPT_CODE", oldDept.getStr("DEPT_CODE"));
        newDept.set("DEPT_NAME", oldDept.getStr("DEPT_NAME"));
        newDept.set("DEPT_FULL_NAME", oldDept.getStr("DEPT_NAME"));
        newDept.set("DEPT_TYPE", "1");
        newDept.set("DEPT_LEVEL", pDept.getInt("DEPT_LEVEL") +1);
        newDept.set("CODE_PATH", pDept.getStr("CODE_PATH") + oldDept.getStr("DEPT_CODE") + "^");
        newDept.set("DEPT_PCODE", pDept.getStr("DEPT_CODE"));
        newDept.set("ODEPT_CODE", pDept.getStr("ODEPT_CODE"));
        if (pDept.getInt("DEPT_TYPE") == 2) { //上级部门为机构
            newDept.set("TDEPT_CODE", oldDept.getStr("DEPT_CODE"));
        } else {
            newDept.set("TDEPT_CODE", pDept.getStr("DEPT_CODE"));
        }
        newDept.set("CMPY_CODE", "zhbx");
        newDept.set("S_USER", "1Xvd5e5X50O8J0Ir-0zlwd");
        return newDept;
    }
    public void execute1(JobExecutionContext arg0) throws JobExecutionException {
        
        //获取临时部门表中数据
        List<Bean> deptBeanList = ServDao.finds("TBL_ZOTN_DEPARTMENT", " and 1=1");
        if (deptBeanList != null && deptBeanList.size() > 0) {
            List<Bean> saveBeanList = new ArrayList<Bean>();
            for (Bean deptBean : deptBeanList) {
                //部门code
                deptBean.set("DEPT_CODE", deptBean.getStr("DEPART_ID"));
                //部门名称
                deptBean.set("DEPT_NAME", deptBean.getStr("DEPART_NAME"));
                //公司code
                deptBean.set("CMPY_CODE", "zhbx");
                //父部门code
                deptBean.set("DEPT_PCODE", "00000002");
                //部门路径
                deptBean.set("CODE_PATH", "3rin6giCR9vUIv6kHIO3ex^00000002^" + deptBean.getStr("DEPART_ID") + "^");
                //部门级别
                deptBean.set("DEPT_LEVEL", "3");
                //部门属性
                deptBean.set("DEPT_TYPE", "1");
                //所属机构
                deptBean.set("ODEPT_CODE", "00000002");
                //所属部门
                deptBean.set("TDEPT_CODE", deptBean.getStr("DEPART_ID"));
                deptBean.set("S_FLAG", "1");
                deptBean.set("DEPT_SORT", "1");
                
                saveBeanList.add(deptBean);
            }
            // 批量新增
            ServDao.deletes("SY_ORG_DEPT", (new ParamBean()).setWhere(" and DEPT_PCODE='00000002'"));
            ServDao.creates("SY_ORG_DEPT", saveBeanList);
            
            saveBeanList.clear();
            // 同步用户
            List<Bean> userBeanList = ServDao.finds("TBL_ZOTN_USER", " and 1=1 ");
            if (userBeanList != null && userBeanList.size() > 0) {
                for (Bean userBean : userBeanList) {
                    // 用户code
                    userBean.set("USER_CODE", userBean.getStr("USER_ID"));
                    // 姓名
                    userBean.set("USER_NAME", userBean.getStr("USER_LAST_NAME") 
                            + userBean.getStr("USER_FIRST_NAME"));
                    userBean.set("USER_PASSWORD", "e10adc3949ba59abbe56e057f20f883e");
                    userBean.set("S_FLAG", "1");
                    userBean.set("CMPY_CODE", "zhbx");
                    userBean.set("USER_SORT", "1");
                    //用户、部门关联表中获取用户所属部门code
                    List<Bean> duBeanList = ServDao.finds("TBL_ZOTN_DEPARTMENT_USER", " and USER_ID='" 
                            + userBean.getStr("USER_ID") + "'");
                    if (duBeanList.size() == 1) {
                        String deptCode = (duBeanList.get(0)).getStr("DEPART_ID");
                        // 判断部门是否存在
                        Bean dept = ServDao.find("SY_ORG_DEPT", deptCode);
                        if (dept != null && dept.getId().length() > 0) {
                            //所属部门code
                            userBean.set("DEPT_CODE", dept.getId());
                            saveBeanList.add(userBean);
                        }
                    }
                }
                // 批量保存用户
                ServDao.creates("SY_ORG_USER", saveBeanList);
            }
        }
    }

    public void interrupt() {
        // TODO Auto-generated method stub
        
    }

}
