package com.rh.oa.zh.txl;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.txl.AbstractTxlSecurer;
import com.rh.core.org.UserBean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.Constant;
import com.rh.core.util.JsonUtils;

/**
 * 通讯录保密级别接口默认实现类
 * @author ZJW
 * 
 */
public class ZHTxlSecurer extends AbstractTxlSecurer {

    /**
     * 是否默认控制，被查看用户是否为本级部门及下级用户
     * @param userBean 查看用户数据载体
     * @return boolean值
     */
    public boolean isDefSecured(Bean userBean) {
        //获取当前用户
        UserBean usBean = Context.getUserBean();
        //如果是当前用户和被看用户是上下级关系，则可看
        if (usBean.getODeptLevel() < Integer.parseInt(userBean.getStr("DEPT_LEVEL"))) {
            return true;
        //如果当前用户和被看用户是同级，并且当前用户是领导角色，则可看
        } else if (usBean.getODeptLevel() == Integer.parseInt(userBean.getStr("DEPT_LEVEL"))) {
            String[] roles = usBean.getRoleCodes();
            for (int i = 0; i < roles.length; i++) {
                if ("RGSLD".equals(roles[i])) {
                    return true;
                }
            }
        }
       return false;
    }

    /**
     * 根据配置信息检查当前用户是否为被开放用户
     * @return 是返回true,否则返回false
     */
    public boolean isOpenObj() {
        String objStr = Context.getSyConf(SY_COMM_ADDRESS_OPEN_OBJ, "");
        // 当前用户
        UserBean usBean = Context.getUserBean();
        if ("".equals(objStr)) {
            return false;
        } else {
            Bean objStrBean = JsonUtils.toBean(objStr);
            // 判断当前用户所在部门是否是被开放部门
            String depts = objStrBean.getStr("DEPTS");
            if (!"".equals(depts)) {
                for (int i = 0; i < depts.split(Constant.SEPARATOR).length; i++) {
                    if (usBean.getDeptCode().equals(depts.split(Constant.SEPARATOR)[i])) {
                        return true;
                    }
                }
            }
            // 判断当前用户所在部门是否是被开放角色
            String rols = objStrBean.getStr("ROLES");
            if (!"".equals(rols)) {
                List<String> usRolsList = usBean.getRoleCodeList();
                for (int i = 0; i < rols.split(Constant.SEPARATOR).length; i++) {
                    if (usRolsList.contains(depts.split(Constant.SEPARATOR)[i])) {
                        return true;
                    }
                }
            }
            // 判断当前用户所在部门是否是被开放用户
            String users = objStrBean.getStr("USERS");
            if (!"".equals(users)) {
                for (int i = 0; i < users.split(Constant.SEPARATOR).length; i++) {
                    if (usBean.getCode().equals(users.split(Constant.SEPARATOR)[i])) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    /**
     * 根据通讯录保密控制类判断当前用户是否对查看用户进行过权限申请并返回申请的字段内容信息列表
     * @param userCode 判断userCode
     * @return 返回已授予查看权限内容的通讯录信息列表
     */
    protected ArrayList<String> getShareContent(String userCode) {
        ArrayList<String> resulConten = new ArrayList<String>();
        // 当前用户
        UserBean usBean = Context.getUserBean();
        Bean shareBean = new Bean();
        shareBean.set("USER_CODE", userCode);
        shareBean.set("SHARE_TYPE", SHARE_TYPE_USER);
        shareBean.set("SHARE_TO", usBean.getStr("USER_CODE"));
        shareBean = ServDao.find(SY_COMM_ADDRESS_ASSIST, shareBean);
        if (shareBean != null) {
            String content = shareBean.getStr("SHARE_CONTENT");
            if (!"".equals(content)) {
                for (String item : content.split(Constant.SEPARATOR)) {
                    resulConten.add(item);
                }
            }
        }
        return resulConten;
    }

    /**
     * 获得通讯录信息重组后的User信息
     * @param userBean 查看用户数据载体
     * @return OutBean 重组后的UserInfo
     */
    @Override
    public Bean getSecuredUserInfo(Bean userBean) {

        if (isOpenObj()) {
            userBean.set("USER_RESUME", "1");
            userBean.set("USER_PHONE_OK", "OK");
            return userBean;
        } else {
            ArrayList<String> resulContent = new ArrayList<String>();
            //
            String securdInfo = Context.getSyConf(SY_COMM_ADDRESS_CTR_INFO, "");
            // 如果设置为0或未设置控制项目，则表示不进行控制
            if ("0".equals(securdInfo) || "".equals(securdInfo)) {
                userBean.set("USER_PHONE_OK", "OK");
                userBean.set("USER_RESUME", "1");
                return userBean;
            } else {
                if (isDefSecured(userBean)) {
                    userBean.set("USER_PHONE_OK", "OK");
                    userBean.set("USER_RESUME", "1");
                    return userBean;
                }
                // 如果进行控制了，则取通讯录共享表中被查看用户的授权信息内容，
                // 查看当前用户是否有对控制信息的查看权限
                resulContent = getShareContent(userBean.getStr("USER_CODE"));
                String[] infos = securdInfo.split(Constant.SEPARATOR);
                // 如果得到结果非空，则对未开放权限进行系统配置中的设置
                if (resulContent.size() > 0) {
                    for (String info : infos) {
                        String[] ctrInfo = info.split(":");
                        String key = ctrInfo[0];
                        String value = ctrInfo[1];
                        if (resulContent.contains(key)) {
                            if ("USER_MOBILE".equals(key)) {
                                userBean.set("USER_PHONE_OK", "OK");
                            }
                            continue;
                        }
                        userBean.set(key, value);
                    }
                    if (resulContent.contains("USER_RESUME")) {
                        userBean.set("USER_RESUME", "1");
                    }
                    // 如果查询结果为空，则使用系统配置中的设置对控制字段进行设置
                } else {
                    for (String info : infos) {
                        String[] ctrInfo = info.split(":");
                        String key = ctrInfo[0];
                        String value = ctrInfo[1];
                        userBean.set(key, value);
                    }
                }
            }
        }
        return userBean;
    }

}
