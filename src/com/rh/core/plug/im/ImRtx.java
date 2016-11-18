/*
 * Copyright (c) 2011 Ruaho All rights reserved.
 */
package com.rh.core.plug.im;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rtx.RTXSvrApi;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.util.Constant;



/**
 * 处理rtx相关的操作。
 * 
 * @author Jerry Li
 * @version $Id$
 */
public class ImRtx implements ImListener {
    /** log */
    private static Log log = LogFactory.getLog(ImRtx.class);
    
    /** rtx api 对象 */
    private static RTXSvrApi rtxApi = null;

    /**
     * 初始化IM对象
     * @return 是否初始化成功
     */
    public boolean init() {
        boolean result = false;
        if (rtxApi == null) {
            rtxApi = new RTXSvrApi();
            String rtxServIp = Context.getSyConf("SY_HUB_IM_SERVIP", "127.0.0.1");
            String rtxServPort = Context.getSyConf("SY_HUB_IM_PORT", "6000");
            if (rtxApi.Init()) {
                rtxApi.setServerIP(rtxServIp);
                rtxApi.setServerPort(Integer.parseInt(rtxServPort));
                result = true;
            } else {
                log.error("rtx start failed!");
            }
        }
        return result;
    }
    
    /**
     * 关闭IM对象
     */
    public void close() {
        if (rtxApi != null) {
            rtxApi.UnInit();
        }
    }
    
    /**
     * 保存部门信息，不存添加，存在修改
     * @param deptBean 部门信息
     * @return 操作是否成功
     */
    public boolean saveDept(Bean deptBean) {
        int result = 1;
        if (rtxApi != null) {
            String deptCode = toDeptId(deptBean.getStr("DEPT_CODE"));
            String pdeptCode = toDeptId(deptBean.getStr("DEPT_PCODE"));
            log.debug(deptCode + "(" + deptBean.getStr("DEPT_CODE") + ")  " + deptBean.getStr("DEPT_NAME"));
            if (rtxApi.deptIsExist(deptCode) != 0) { //不存在时再添加 
                result = rtxApi.addDept(deptCode, deptBean.getStr("DEPT_MEMO"), deptBean.getStr("DEPT_NAME"), 
                        pdeptCode, deptBean.getStr("DEPT_SORT"));
            } else {
                result = rtxApi.setDept(deptCode, deptBean.getStr("DEPT_MEMO"), deptBean.getStr("DEPT_NAME"), 
                        pdeptCode, deptBean.getStr("DEPT_SORT"));
            }
        }
        return (result == 0);
    }
        
    /**
     * 删除部门
     * @param deptCodes 部门编码列表，多个逗号分隔
     * @return 实际删除数量
     */
    public int deleteDept(String deptCodes) {
        int result = 0;
        if (rtxApi != null) {
            String[] codes = deptCodes.split(Constant.SEPARATOR);
            for (String code : codes) {
                if (rtxApi.deleteDept(String.valueOf(code.hashCode()), "0") == 0) { //只删除当前组织
                    result++;
                }
            }
        }
        return result;
    }
    
    /**
     * 保存用户，不存在添加，存在修改
     * @param userBean 用户信息
     * @return 是否保存成功
     */
    public boolean saveUser(Bean userBean) {
        int result = 1;
        if (rtxApi != null) {
            String userCode = userBean.getStr("USER_CODE");
            String deptCode = toDeptId(userBean.getStr("DEPT_CODE"));
            log.debug(userCode + "  " + deptCode + "  " + userBean.getStr("USER_NAME"));
            if (rtxApi.userIsExist(userCode) != 0) { //不存在时再添加 
                result = rtxApi.addUser(userCode, deptCode, 
                    userBean.getStr("USER_NAME"), userBean.getStr("USER_PASSWORD_REAL"), 
                    userBean.getStr("USER_SORT"));
                if (result == 0) { //添加成功则处理用户扩展信息
                    result = rtxApi.SetUserSimpleInfo(userCode, userBean.getStr("USER_NAME"), 
                            userBean.getStr("USER_EMAIL"), userBean.getStr("USER_SEX"), 
                            userBean.getStr("USER_MOBILE"), userBean.getStr("USER_OFFICE_PHONE"), 
                            null);
                }
            } else {
                result = rtxApi.SetUserSimpleInfoEx(userCode, 
                        deptCode, 
                        userBean.getStr("USER_NAME"), 
                        userBean.getStr("USER_EMAIL"), userBean.getStr("USER_SEX"), 
                        userBean.getStr("USER_MOBILE"), userBean.getStr("USER_OFFICE_PHONE"), 
                        userBean.getStr("USER_SORT"), userBean.getStr("USER_PASSWORD_REAL"));
            }
        }
        return (result == 0);
    }
    
    /**
     * 删除用户
     * @param userCodes 用户编码列表，多个逗号分隔
     * @return 实际删除的数量
     */
    public int deleteUser(String userCodes) {
        int result = 0;
        if (rtxApi != null) {
            String[] codes = userCodes.split(Constant.SEPARATOR);
            for (String code : codes) {
                if (rtxApi.deleteUser(code) == 0) {
                    result++;
                }
            }
        }
        return result;
    }

    /**
     * 发送普通消息提醒
     * 
     * @param receivers 接收人(多个接收人以逗号分隔)
     * @param title 消息标题
     * @param msg 消息内容，如果带连接，采用特殊格式如：[腾讯|http://www.qq.com]
     * @return true:发送成功 false:操作不成功
     */
    public boolean sendNotify(String receivers, String title, String msg) {
        return sendNotify(receivers, title, msg, false);
    }
    
    /**
     * 发送消息提醒
     * 
     * @param receivers 接收人(多个接收人以逗号分隔)
     * @param title 消息标题
     * @param msg 消息内容
     * @param isEmergency 是否紧急消息
     * @return true:发送成功 false:操作不成功
     */
    public boolean sendNotify(String receivers, String title, String msg, boolean isEmergency) {
        boolean result = false;
        if (rtxApi != null) {
            String type = isEmergency ? "1" : "0";
            result = (rtxApi.sendNotify(receivers, title, msg, type, "0") == 0);
        }
        return result;
    }
    
    /**
     * 发送短信
     * @param sender 发送人
     * @param receivers 接收人(RTX用户或手机号码均可,最多128个)
     * @param smsInfo 短信内容
     * @return 成功返回true，失败返回false
     */
    public boolean sendSms(String sender, String receivers, String smsInfo) {
        boolean result = false;
        if (rtxApi != null) {
            result = (rtxApi.sendSms(sender, receivers, smsInfo, 1, 0) == 0);
        }
        return result;
    }
    
    /**
     * 根据部门编码生成尽量短的不重复的数字型的部门ID
     * @param deptCode 部门编码
     * @return 生成后的部门ID
     */
    private String toDeptId(String deptCode) {
        int num = deptCode.hashCode();
        int a  = num / 10000;
        int b = num % 10000;
        return String.valueOf(Math.abs(a + b));
    }
}