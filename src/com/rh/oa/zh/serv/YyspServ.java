package com.rh.oa.zh.serv;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.org.DeptBean;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.OrgMgr;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.ServUtils;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.oa.gw.util.AuditUtils;
import com.rh.oa.zh.seal.SealMgr;

/**
 * 用印审批单服务
 * @author wangchen
 * 
 */
public class YyspServ extends CommonServ {

    private static Log log = LogFactory.getLog(YyspServ.class);
    private static final int SHENGLEVEL = 3;
    private static final int ZHONGZHILEVEL = 4;
    /** 电子用印转换服务 */
    private static final String OA_YY_YYSPD_RELATE = "OA_YY_YYSPD_RELATE";
    /** 实物用印转换服务 */
    private static final String OA_YY_YYSPD_SW_RELATE = "OA_YY_YYSPD_SW_RELATE";
    /** 实物用印服务 */
    private static final String OA_YY_YYSPD_SW = "OA_YY_YYSPD_SW";
    /** 被现实多次转换用印数据服务id */
    private static final String OA_CREATOR_SEAL_CONTENT = "OA_CREATOR_SEAL_CONTENT";

    @Override
    protected void beforeSave(ParamBean paramBean) {
        Bean fullData = paramBean.getSaveFullData();
        super.beforeSave(paramBean);
        // 如果是添加模式生成编号
        if (paramBean.getAddFlag() || fullData.isEmpty("YY_CODE")) {
            ServDefBean servDef = ServUtils.getServDef(paramBean.getServId());
            String srcServId = servDef.getSrcId();
            setPservId(paramBean, srcServId); // 添加父服务id
            // 取得机关代字
            String word = AuditUtils.getOrgWord(srcServId);
            if (word != null && !word.equals("")) {
                paramBean.set("YY_YEAR_CODE", word);
            } else {
                log.debug("当前部门没有配置用印审批单(" + srcServId + ")的机关代字！");
                throw new TipException("当前部门没有用印审批单的机关代字！");
            }
            // 年份
            String year = Integer.toString(DateUtils.getYear());
            paramBean.set("YY_YEAR", year);
            // 取得流水号
            if (word != null) {
                ParamBean queryBean = new ParamBean()
                        .set("YY_YEAR", paramBean.getStr("YY_YEAR"))
                        .set("YY_YEAR_CODE", word);
                int serialInt = AuditUtils.getSerial(queryBean, srcServId, "YY_YEAR_NUMBER");
                String serial = Integer.toString(serialInt);
                paramBean.set("YY_YEAR_NUMBER", serial);

                // 设置合成编号
                paramBean.set("YY_CODE", word + "〔" + year + "〕" + serial + "号");
            }
        }
        // 计算打印人所在机构级别(添加/修改了打印人所在机构或者级别为空)
        String printOdeptCode = "";
        if (fullData.contains("YY_PRINTUSER_ODEPT") || fullData.isEmpty("YY_PRINTODEPT_LEVEL")) {
            printOdeptCode = fullData.getStr("YY_PRINTUSER_ODEPT");
        }
        if (!printOdeptCode.isEmpty()) {
            int printOdeptLevel = OrgMgr.getDept(printOdeptCode).getLevel();
            paramBean.set("YY_PRINTODEPT_LEVEL", printOdeptLevel);
        }
    }

    @Override
    protected void afterByid(ParamBean paramBean, OutBean outBean) {
        super.afterByid(paramBean, outBean);
        // 输出给前台自定义权限
        createCustomAuth(paramBean, outBean);
    }

    /**
     * 获取印章机构编码
     * @param paramBean 参数
     * @return 印章机构编码对象
     */
    public OutBean getSealOdept(ParamBean paramBean) {
        String addUserCode = paramBean.getStr("addUser");
        UserBean thisUser = null;
        if (StringUtils.isBlank(addUserCode)) {
            thisUser = Context.getUserBean();
        } else {
            thisUser = UserMgr.getUser(addUserCode);
        }
        return new OutBean().set("ODEPT_CODE", SealMgr.getODeptNum(thisUser.getODeptCode()));
    }

    /**
     * 获取可以追加打印分数的机构
     * @param paramBean 参数
     * @return 结果集
     */
    public OutBean getAddSealPrintOdept(ParamBean paramBean) {
        Bean bean = ServDao.find(paramBean.getStr("servC"), paramBean.getId());
        String returnOdeptCode = ""; // 返回机构编码
        if (null == bean || bean.isEmpty()) { // 不存在此数据
            return new OutBean().setError("数据不存在！");
        }
        String sealOdeptCodes = bean.getStr("SEAL_ODEPTS"); // 获取盖章的机构编码
        if (StringUtils.isBlank(sealOdeptCodes)) { // 如果不存在值
            returnOdeptCode = Context.getUserBean().getODeptCode();
        } else {
            String[] odeptCodes = sealOdeptCodes.split(Constant.SEPARATOR);
            List<Integer> levelList = new ArrayList<Integer>(); // 存放层级，用于索引查找
            for (String s : odeptCodes) {
                String[] ss = s.split(":");// 去组织机构
                DeptBean dept = OrgMgr.getDept(ss[0]);
                if(ss[1].equals("2")){
                levelList.add(dept.getLevel());
                }
            }
            // 查找最高层级值
            int theHighestLevel = 5; // 最低层级为5
            int theHighestLevelIndex = levelList.size(); // 最高层级索引值
            for (int i = 0; i < levelList.size(); i++) {
                int thisLevel = levelList.get(i);
                if (thisLevel < theHighestLevel) {
                    theHighestLevel = thisLevel;
                    theHighestLevelIndex = i;
                }
            }
            if(theHighestLevelIndex > 0){
            returnOdeptCode = odeptCodes[theHighestLevelIndex].split(":")[0]; // 根据最大层级索引值获取机构编码
            }
        }
        return new OutBean().set("SEAL_ODEPTL", returnOdeptCode).setOk("OK");
    }

    /**
     * 添加父服务
     * @param paramBean 参数
     * @param srcServId 父服务id
     */
    private void setPservId(ParamBean paramBean, String srcServId) {
        String servId = paramBean.getServId();
        // 如果是转换用印
        paramBean.set("TMPL_CODE", servId).set("TMPL_TYPE_CODE", srcServId);
    }

    /**
     * 生成自定义权限，输出到前台
     * @param paramBean 参数bean
     * @param outBean 待输出bean
     */
    private void createCustomAuth(ParamBean paramBean, OutBean outBean) {
        UserBean currUser = Context.getUserBean();
        String odeptCode = currUser.getODeptCode();
        String yyOdeptCode = outBean.getStr("YY_ODEPT");

        Bean customAuthBean = new Bean();

        // 处理打印及追加打印权限
        String printRole = Context.getSyConf("OA_GW_PRINT_ROLE", "");
        boolean existInRoles = false;

        for (String role : printRole.split(",")) {
            if (currUser.existInRole(role)) {
                existInRoles = true;
                break;
            }
        }

        if (existInRoles) {
            // 取消以单位为判断条件的判断改为谁盖章，谁追加20150212,
            String usercode = currUser.getCode();
            String yyPrintUsercode = outBean.getStr("YY_SIGN_USER");
            if (usercode.equals(yyPrintUsercode)) {
                customAuthBean.set("YYAPPENDPRINT", true);
            }

            String yyPrintUserOdept = outBean.getStr("YY_PRINTUSER_ODEPT");
            if (odeptCode.equals(yyPrintUserOdept)) {
                customAuthBean.set("YYPRINT", true);
            }

        }

        // 处理盖章权限
        if (!yyOdeptCode.isEmpty()) {
            DeptBean yyOdept = OrgMgr.getOdept(yyOdeptCode);
            int yyLevel = yyOdept.getLevel(); // 要盖的章所属层级
            int curLevel = currUser.getODeptLevel(); // 盖章人所属层级
            boolean sealAuth = false;
            /** 判断逻辑可重写-开始 **/
            // 上级含本级可以盖,但设定的特定的省的中支机构才可以盖章
            if (curLevel <= yyLevel) {
                if (curLevel == ZHONGZHILEVEL) {
                    String provinceCodes = Context.getSyConf("OA_SEAL_PROVINCE_CODES", "");
                    if (!provinceCodes.endsWith(",")) {
                        provinceCodes = provinceCodes + ",";
                    }
                    String currProvinceCode = currUser.getParentODeptCode();
                    if (provinceCodes.indexOf(currProvinceCode + ",") >= 0) {
                        sealAuth = true;
                    }
                } else {
                    if (yyLevel >= ZHONGZHILEVEL) {
                        String provinceCodes = Context.getSyConf("OA_SEAL_PROVINCE_CODES", "");
                        if (!provinceCodes.endsWith(",")) {
                            provinceCodes = provinceCodes + ",";
                        }
                        String yyProvinceCode = getProvinceOdeptCode(yyOdept);
                        if (provinceCodes.indexOf(yyProvinceCode + ",") < 0) {
                            sealAuth = true;
                        }
                    } else {
                        sealAuth = true;
                    }
                }
            }
            /*
             * if (curLevel == ZHONGZHILEVEL && curLevel <= yyLevel) { String provinceCodes =
             * Context.getSyConf("OA_SEAL_PROVINCE_CODES", ""); if (!provinceCodes.endsWith(",")) { provinceCodes =
             * provinceCodes + ","; } String currProvinceCode = currUser.getParentODeptCode(); if
             * (provinceCodes.indexOf(currProvinceCode + ",") >= 0) { sealAuth = true; } } else { if (curLevel <=
             * yyLevel) { if (yyLevel >= ZHONGZHILEVEL) { String provinceCodes =
             * Context.getSyConf("OA_SEAL_PROVINCE_CODES", ""); if (!provinceCodes.endsWith(",")) { provinceCodes =
             * provinceCodes + ","; } String yyProvinceCode = getProvinceOdeptCode(yyOdept); if
             * (provinceCodes.indexOf(yyProvinceCode + ",") < 0) { sealAuth = true; } } else { sealAuth = true; } } }
             */
            /** 判断逻辑可重写-结束 **/
            customAuthBean.set("YYSEALUNSEAL", sealAuth);
        }

        outBean.set("customAuth", customAuthBean);
    }

    /**
     * 递归查找所属省公司编码
     * @param odept 当前机构
     * @return 所属省公司编码
     */
    private String getProvinceOdeptCode(DeptBean odept) {
        int level = odept.getLevel();
        if (level < SHENGLEVEL) {
            return "";
        } else if (level == SHENGLEVEL) {
            return odept.getCode();
        } else {
            String pId = odept.getPcode();
            DeptBean pOdept = OrgMgr.getOdept(pId);
            return getProvinceOdeptCode(pOdept);
        }
    }

    @Override
    protected void beforePrint(ParamBean paramBean, OutBean outBean) {
        super.beforePrint(paramBean, outBean);
        SqlBean query = new SqlBean();
        query.and("YYID", paramBean.getId());
        query.orders("S_ATIME");
        List<Bean> files = ServDao.finds("OA_YY_YYSPD_FILE_PRI", query);
        List<Bean> priFile = new ArrayList<Bean>();
        List<Bean> pubFile = new ArrayList<Bean>();
        // 实物用印
        boolean isSwSealFlag = false;
        if (paramBean.getServId().contains(OA_YY_YYSPD_SW)) {
            SqlBean sql = new SqlBean();
            sql.set("DATA_ID", paramBean.getId());
            sql.orders("FILE_SORT");
            priFile.addAll(ServDao.finds(ServMgr.SY_COMM_FILE, sql));
            isSwSealFlag = true;
            outBean.set("IS_SW", "true"); // 实物用印
        }
        for (Bean file : files) {
            int type = file.getInt("YY_FILE_TYPE");
            if (type == 2 && !isSwSealFlag) {
                priFile.add(file);
            } else if (type == 1) {
                pubFile.add(file);
            }
        }
        outBean.set("_YYPRIFILE_", priFile);
        outBean.set("_YYPUBFILE_", pubFile);
        /*
         * // 添加所有意见人 SqlBean mindFind = new SqlBean(); mindFind.and("DATA_ID", outBean.getId());
         * //mindFind.and("S_FLAG", Constant.YES_INT); mindFind.asc("S_MTIME"); List<Bean> mindList =
         * ServDao.finds("SY_COMM_MIND", mindFind); if (mindList != null && mindList.size() > 0) { String mindUserCodes
         * = ""; String mindUserNames = ""; for (Bean mind : mindList) { if
         * (mindUserCodes.indexOf(mind.getStr("S_USER")) < 0) { mindUserCodes = Strings.addValue(mindUserCodes,
         * mind.getStr("S_USER"), Constant.SEPARATOR); mindUserNames = Strings.addValue(mindUserNames,
         * mind.getStr("S_UNAME"), "、"); } } outBean.set("MIND_USERS", mindUserNames); }
         */
    }

    @Override
    protected void afterDelete(ParamBean paramBean, OutBean outBean) {
        super.afterDelete(paramBean, outBean);
        // 制定了强制删除
        if (paramBean.getDeleteDropFlag()) {
            // 删除当前用印审批单对应限制用印次数配置
            ServMgr.act(OA_CREATOR_SEAL_CONTENT, "delete", new Bean().set("RELATE_ID", paramBean.getId()));
        }
    }

    /**
     * 判断当前起草人是否是控股和财险的
     * @param paramBean 参数
     * @return 是否为控股起草人
     */
    public OutBean isKgAddUser(ParamBean paramBean) {
        String addUser = paramBean.getStr("ADD_USER");
        UserBean user = null;
        int deptLevel = 100;
        boolean isKgAddUser = false;
        if (StringUtils.isBlank(addUser)) {
            user = Context.getUserBean();
        } else {
            user = UserMgr.getUser(addUser);
        }
        deptLevel = user.getODeptLevel();
        if (deptLevel <= 2) {
            isKgAddUser = true;
        }
        return new OutBean().set("IS_KG", isKgAddUser);
    }

    /**
     * 查看是否已转换用印
     * @param paramBean 参数
     * @return 是否转换状态
     */
    public OutBean isExitCreatorSeal(ParamBean paramBean) {
        SqlBean sql = new SqlBean();
        sql.and("DATA_ID", paramBean.getStr("DATA_ID")).andIn("SERV_ID", OA_YY_YYSPD_RELATE, OA_YY_YYSPD_SW_RELATE);
        List<Bean> list = ServDao.finds(OA_CREATOR_SEAL_CONTENT, sql);
        return new OutBean().set("IS_EXIT", list.size() > 0 ? true : false);
    }
}
