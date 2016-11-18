package com.rh.core.util.var;

import java.util.HashMap;
import java.util.Map;

import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;


/**
 * 组织用户变量类，变量格式为"@系统配置键值@"，内部无前缀。
 * 
 * @author Jerry Li
 * @version $Id$
 */
public class OrgVar implements Var {
    /** 单例 */
    private static OrgVar inst = null;
    /**
     * 私有构建体，单例模式
     */
    private OrgVar() {
    }
    
    /**
     * 单例方法
     * @return 获取系统配置变量类
     */
    public static OrgVar getInst() {
        if (inst == null) {
            inst = new OrgVar();
        }
        return inst;
    }
    
    /**
     * 获取变量值
     * @param key 键值
     * @return 变量值
     */
    public String get(String key) {
        String value = getMap().get(key);
        if (value == null) {
            value = key;
        }
        return value;
    }
    
    
    /**
     * 获取系统变量信息
     * @return 系统变量信息
     */
    public Map<String, String> getMap() {
        Map<String, String> vars;
        UserBean userBean = Context.getUserBean();
        if (userBean != null) {
            String userCode = userBean.getCode();
            vars = UserMgr.getCacheVarMap(userCode);
            if (vars != null) {
                return vars;
            } else {
                vars = new HashMap<String, String>();
                vars.put("@USER_CODE@", userBean.getCode()); //用户编码
                vars.put("@USER_NAME@", userBean.getName()); //用户名称
                vars.put("@LOGIN_NAME@", userBean.getLoginName()); //用户登录名
                vars.put("@USER_POST@", userBean.getPost()); //用户岗位
                vars.put("@USER_SEX@", String.valueOf(userBean.getSex())); //用户性别
                vars.put("@USER_IMG@", userBean.getImg());  //截取后的头像文件
                vars.put("@USER_IMG_SRC@", userBean.getImgSrc()); //原始头像文件
                vars.put("@CMPY_CODE@", userBean.getCmpyCode()); //公司编码
                vars.put("@CMPY_NAME@", userBean.getCmpyName()); //公司名称（简称）
                vars.put("@CMPY_FULLNAME@", userBean.getCmpyFullName()); //公司全名
                vars.put("@DEPT_CODE@", userBean.getDeptCode()); //处室编码
                vars.put("@DEPT_NAME@", userBean.getDeptName()); //处室名称
                vars.put("@DEPT_SRC_TYPE1@", userBean.getDeptSrcType1());   //处室扩展类型1
                vars.put("@DEPT_SRC_TYPE2@", userBean.getDeptSrcType2());   //处室扩展类型2
                vars.put("@DEPT_LINE@", userBean.getDeptLine());   //部门业务条线
                vars.put("@TDEPT_CODE@", userBean.getTDeptCode()); //部门编码（有效部门）
                vars.put("@TDEPT_NAME@", userBean.getTDeptName()); //部门名称
                vars.put("@TDEPT_SRC_TYPE1@", userBean.getTDeptSrcType1()); //部门扩展类型1
                vars.put("@TDEPT_SRC_TYPE2@", userBean.getTDeptSrcType2()); //部门扩展类型2
                vars.put("@OFFICE_PHONE@", userBean.getOfficePhone()); //办公电话
                vars.put("@USER_MOBILE@", userBean.getMobile()); //手机
                vars.put("@USER_EMAIL@", userBean.getEmail()); //邮箱
                vars.put("@ROLE_CODES@", userBean.getRoleCodeQuotaStr());   //用户所有角色，单引号包含，逗号分隔
                vars.put("@GROUP_CODES@", userBean.getGroupCodeQuotaStr());   //用户所有群组，单引号包含，逗号分隔
                vars.put("@ADMIN_GROUP_CODES@", userBean.getAdminGroupCodeQuotaStr());   //用户所有管理群组
                vars.put("@DEPT_CODES@", userBean.getDeptCodeQuotaStr());   //用户所有部门，（当前、父、父的父...）
                vars.put("@ODEPT_CODE@", userBean.getODeptCode()); // 机构代码(有效部门的父)
                vars.put("@ODEPT_NAME@", userBean.getODeptName()); //机构名称
                vars.put("@ODEPT_FULL_NAME@", userBean.getODeptFullName()); //机构全称
                vars.put("@ODEPT_SRC_TYPE1@", userBean.getODeptSrcType1()); //机构部门扩展类型1
                vars.put("@ODEPT_SRC_TYPE2@", userBean.getODeptSrcType2()); //机构部门扩展类型1
                vars.put("@ODEPT_CODE_PATH@", userBean.getODeptCodePath()); //机构代码路径 
                vars.put("@ODEPT_PCODE@", userBean.getParentODeptCode()); //父机构代码
                vars.put("@ODEPT_LEVEL@", String.valueOf(userBean.getODeptLevel())); //机构所在层级
                vars.put("@SUB_CODES@", userBean.getCurrentSubCodesQuotaStr()); //所有工作委托给当期人的用户列表
                vars.put("@LEAD_CODES@", userBean.getCurrentLeadCodesQuotaStr()); //秘书对应领导编码列表
                vars.put("@JIAN_CODES@", userBean.getCurrentJianCodes()); //所有设定跟当前人工作兼岗的用户列表
                vars.put("@AGT_FLAG@", String.valueOf(userBean.getCurrentAgtFlag())); //当前用户是否出于委托别人办理状态
                vars.put("@USER_CMLE_DEG@", String.valueOf(userBean.getCurrentCmleDeg())); //当前用户的资料完整度
                vars.put("@urlPath@", Context.appStr(Context.APP.CONTEXTPATH)); //虚路径变量
                vars.put("@USER_PT@", userBean.getUserPt()); //用户模版
                vars.put("@DEPT_PT@", userBean.getDeptPt()); //处室模版
                vars.put("@TDEPT_PT@", userBean.getTDeptPt()); //部门模版
                vars.put("@ODEPT_PT@", userBean.getODeptPt()); //机构模版
                vars.put("@CMPY_PT@", userBean.getDeptPt()); //公司模版
                vars.put("@USER_WORK_NUM@", userBean.getStr("USER_WORK_NUM")); //用户工号
                UserMgr.setCacheVarMap(userCode, vars);
            }
        } else {
            vars = new HashMap<String, String>();
        }
        return vars;
    }
}
