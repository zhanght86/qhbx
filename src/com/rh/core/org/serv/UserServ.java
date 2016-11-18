package com.rh.core.org.serv;

import java.net.InetAddress;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.bn.sync.impl.org.BnUserSync;
import com.rh.bn.sync.job.BnOrgAndUserSyncJob;
import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.Context.APP;
import com.rh.core.base.Context.THREAD;
import com.rh.core.base.TipException;
import com.rh.core.comm.CacheMgr;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.plug.im.ImMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.core.util.EncryptUtils;

/**
 * 用户服务类
 * 
 * @author liyanwei
 * 
 */
public class UserServ extends CommonServ {
	
	private static Log log = LogFactory.getLog(BnOrgAndUserSyncJob.class);
    
    /** 服务名称：工作兼岗 */
    public static final String JIANGANG_SERV = "SY_ORG_USER_JIANGANG";  
    /** 关联类型：工作兼岗 */
    public static final int RELATE_TYPE_JIANGANG = 1;
    /** 用户缓存类型 */
    public static final String CACHE_TYPE_USER = "SY_ORG_USER";
    
    @Override
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        if (outBean.isOk()) {
            if (!paramBean.getAddFlag()) { //修改模式
            	UserBean currUser = Context.getUserBean();
                if (currUser != null && paramBean.getId().equals(currUser.getCode())) { //如果操作者为当前人，则清除当前用户缓存
                    UserMgr.clearSelfUserCache();
                } else { //不是当前用户，清除该用户缓存
                    UserBean userBean = UserMgr.getCacheUser(paramBean.getId());
                    if(userBean != null) {
                        UserMgr.clearSelfUserCache(userBean);
                    }

                }
                    
                if (paramBean.contains("DEPT_CODE")) { //如果修改了部门，则清除用户的菜单缓存
                    UserMgr.clearMenuByUsers(paramBean.getId());
                }
            }
            if (Context.appBoolean(APP.IM)) { //启动了IM进行同步
                if (paramBean.contains("USER_PASSWORD_REAL")) {
                    outBean.set("USER_PASSWORD_REAL", paramBean.getStr("USER_PASSWORD_REAL")); //真实密码给接口用
                }
                ImMgr.getIm().saveUser(outBean);
            }
        }
        
        /** 处理兼岗用户 */
        if (!paramBean.containsKey("_JIANGANG_FLAG_")) {
            treatJiangangUser(paramBean, outBean);
        }
    }
    
    /**
     * 保存之前的操作
     * @param paramBean 传入的参数Bean
     */
    @Override
    protected void beforeSave(ParamBean paramBean) {
        
        // 去除姓名左右空格
        if (paramBean.isNotEmpty("USER_NAME")) {
            paramBean.set("USER_NAME", paramBean.getStr("USER_NAME").trim());
        }
        
        // 去除登录名左右空格
        if (paramBean.isNotEmpty("USER_LOGIN_NAME")) {
            System.out.println(paramBean.getStr("USER_LOGIN_NAME").trim());
            paramBean.set("USER_LOGIN_NAME", paramBean.getStr("USER_LOGIN_NAME").trim());
        }
        
        // 去除工号左右空格
        if (paramBean.isNotEmpty("USER_WORK_NUM")) {
            paramBean.set("USER_WORK_NUM", paramBean.getStr("USER_WORK_NUM").trim());
        }
        
        //if (!paramBean.containsKey("_JIANGANG_FLAG_")) {
            if (paramBean.getAddFlag() && (paramBean.isEmpty("USER_PASSWORD"))) { //添加模式没有设密码则给缺省密码
                paramBean.set("USER_PASSWORD", Context.getSyConf("SY_USER_PASSWORD_INIT", "123456"));
            }
            if (paramBean.isNotEmpty("USER_PASSWORD")) {
                paramBean.set("USER_PASSWORD_REAL", paramBean.getStr("USER_PASSWORD")); //记录真实密码给接口用
                paramBean.set("USER_PASSWORD", 
                        EncryptUtils.encrypt(paramBean.getStr("USER_PASSWORD"), 
                                Context.getSyConf("SY_USER_PASSWORD_ENCRYPT", EncryptUtils.DES)));
            } else if (paramBean.contains("USER_PASSWORD")) {
                paramBean.remove("USER_PASSWORD");
            }
        //}
    }
    
    /**
     * 删除之前处理
     * @param paramBean 参数信息
     */
    protected void beforeDelete(ParamBean paramBean) {
        /** 处理兼岗用户 */
        if (!paramBean.containsKey("_JIANGANG_FLAG_")) {
            if (!UserMgr.isMainUser(paramBean.getId())) {
                throw new TipException("不能删除兼岗用户信息");
            }
        }
    }
    
    /**
     * 删除之后处理
     * @param paramBean 参数信息
     * @param outBean 删除结果信息
     */
    protected void afterDelete(ParamBean paramBean, OutBean outBean) {
        if (outBean.isOk()) {
            String[] ids = outBean.getDeleteIds().split(Constant.SEPARATOR);
            for (String id : ids) {
                CacheMgr.getInstance().remove(id, UserMgr.CACHE_TYPE_USER); // 清除缓存
            }
            if (Context.appBoolean(APP.IM)) { // 启动了IM进行同步
                ImMgr.getIm().deleteUser(paramBean.getId());
            }
            
            ParamBean modifyBean = new ParamBean();
            modifyBean.setServId(ServMgr.SY_ORG_USER_ALL);
            for (String id : ids) {
    		Bean userBean = ServDao.find(ServMgr.SY_ORG_USER, new Bean(id));
    		modifyBean.setAct(ServMgr.ACT_SAVE);
    		modifyBean.setId(id);
    		// 非兼岗用户被删除的时候在登录名后面添加随机后缀
    		if (userBean.getInt("JIANGANG_FLAG") == Constant.NO_INT) {
    		    String newLoginName = userBean.getStr("USER_LOGIN_NAME");
    		    newLoginName += "@" + id;
                    int size = 40;
                    if (newLoginName.length() > size) {
                    	newLoginName = newLoginName.substring(0, size);
                    }
                    modifyBean.set("USER_LOGIN_NAME", newLoginName);
                    ServMgr.act(modifyBean);
    		}
            }
        }
    }
    
    /**
     * 兼岗：处理兼岗用户
     * @param paramBean 请求bean
     * @param outBean 结果bean
     */
    private void treatJiangangUser(ParamBean paramBean, OutBean outBean) {
        String mainUserCode = paramBean.getId();
        if (!UserMgr.isMainUser(mainUserCode)) {
            throw new TipException("不能修改兼岗用户信息");
        }

        ParamBean auxBean = copyUserInfo(paramBean);
        auxBean.set("_JIANGANG_FLAG_", true);
        String mainDeptCode = "";
        String mainDeptName = "";
        if (!paramBean.getAddFlag() && paramBean.containsKey("DEPT_CODE")) {
            mainDeptCode = paramBean.getStr("DEPT_CODE");
            mainDeptName = DictMgr.getFullNames("SY_ORG_DEPT_ALL", mainDeptCode);
        }
        List<Bean> auxUserList = UserMgr.getAuxiliaryUserBeansByMainUser(mainUserCode);
        for (Bean user : auxUserList) {
            String auxDeptCode = user.getStr("DEPT_CODE");
            if (mainDeptCode.equals(auxDeptCode)) {
                throw new TipException("当前用户所在部门和其兼岗部门相同，请先删除" + mainDeptName + "部门下的兼岗设定");
            }
            auxBean.setId(user.getId());
            auxBean.set("USER_CODE", user.getId());
            auxBean.set("JIANGANG_FLAG", Constant.YES_INT);
            //设置了唯一分组变更时需要单独处理的字段
            if (paramBean.contains("USER_WORK_NUM")) {
                Bean updateWnBean = new Bean();
                updateWnBean.setId(user.getId());
                updateWnBean.set("USER_WORK_NUM", paramBean
                        .getStr("USER_WORK_NUM"));
                ServDao.update(paramBean.getServId(), updateWnBean);
                auxBean.remove("USER_WORK_NUM");
            }
            if (paramBean.contains("USER_LOGIN_NAME")) {
                String auxUserLoginName = paramBean.getStr("USER_LOGIN_NAME");
                if (auxUserLoginName.isEmpty()) {
                    auxUserLoginName = outBean.getStr("USER_CODE");
                }
                // 如果本身超过33位则截取33位
                int size = 33;
                if (auxUserLoginName.length() > size) {
                    auxUserLoginName = auxUserLoginName.substring(0, size);
                } 
                auxUserLoginName += "@" + RandomStringUtils.randomAlphanumeric(6);
//                int size = 40;
//                if (auxUserLoginName.length() > size) {
//                    auxUserLoginName = auxUserLoginName.substring(0, size);
//                }
                Bean updateUlnBean = new Bean();
                updateUlnBean.setId(user.getId());
                updateUlnBean.set("USER_LOGIN_NAME", auxUserLoginName);
                ServDao.update(paramBean.getServId(), updateUlnBean);
                auxBean.remove("USER_LOGIN_NAME");
            }
            ServMgr.act(auxBean);
        }
    }

    /**
     * 复制用户信息
     * @param srcBean 原始bean
     * @return ParamBean 结果
     */
    public ParamBean copyUserInfo(Bean srcBean) {
        ParamBean toBean = new ParamBean();
        toBean.copyFrom(srcBean);
        removeUnuseKeys(toBean);
        return toBean;
    }

    /**
     * 去除bean中的无用key
     * @param b 待处理bean
     */
    public void removeUnuseKeys(Bean b) {
        b.remove(Constant.KEY_ID);
        b.remove("USER_CODE");
        b.remove("S_MTIME");
        b.remove("PT_DEPT_CODE");
        b.remove("DEPT_SORT");
        b.remove("DEPT_FLAG");
        b.remove("DEPT_NAME");
        b.remove("_MSG_");
        b.remove("_SERV_ID");
        if (b.getInt("S_FLAG") != Constant.NO_INT) {
            b.remove("S_FLAG");
        }
        b.remove("CODE_PATH");
        b.remove("DEPT_CODE");
        b.remove("DEPT_PT_ID");
        b.remove("DEPT_LEVEL");
        b.remove("TDEPT_CODE");
        b.remove("ODEPT_CODE");
        b.remove("_OLDBEAN_");
    }
    /**
     * 按指定时间批量更新HR用户
     * @author ldd
     */
    public OutBean upUserForTime(ParamBean paramBean){
    	(new BnUserSync()).sync(paramBean.getStr("BEGIN_TIME"));
        // 记录Job执行时间
    	ServDao.create("BN_ORG_SYNC_LOG",
                (new Bean()).set("SYNC_TIME", DateUtils.getDatetime()).set("SYNC_HOST", getHostName()));
    	return new OutBean();
    }
    /**
     * 获取运行环境的主机名
     * @return 主机名
     */
    private static String getHostName() {
        String hostName = "";
        try {
            hostName = InetAddress.getLocalHost().getCanonicalHostName();
        } catch (Exception e) {
            log.error("无法获得主机地址:" + e.getMessage());
        }
        return hostName;
    }
    /**
     * 批量重置密码
     * @author changyc
     * @param paramBean
     */
    public OutBean upUserPwd(ParamBean paramBean) {
    	OutBean outBean = new OutBean();
    	String userIds = paramBean.getStr("userIds");
    	String servId = paramBean.getStr("serv");
    	String[] uIds = userIds.split(",");
        String password = EncryptUtils.encrypt(paramBean.getStr("pwd"), 
        		Context.getSyConf("SY_USER_PASSWORD_ENCRYPT", EncryptUtils.DES));
    	for (int i = 0; i < uIds.length; i++) {
    		ParamBean pBean = new ParamBean();
    		pBean.set("_PK_", uIds[i]);
    		pBean.set("USER_PASSWORD",password);
    		ServDao.update(servId, pBean);
    		/*
    		 * 清除修改密码的人的缓存。否则用户第一次修改密码的时候会从缓存中去密码,清除类型SY_ORG_USER
    		 */
    		CacheMgr.getInstance().remove(uIds[i], CACHE_TYPE_USER);
    		//Context.setThread(THREAD.USERBEAN, ((UserBean) Context.getThread(THREAD.USERBEAN)).set("USER_PASSWORD",password));
		}
    	outBean.setOk("重置成功！");
    	return outBean;
	}
    
}
