package com.rh.oa.sso;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.TaskLock;

/**
 * 立即执行同步操作
 * @author chensheng
 */
public class ImmediateSyncServ extends CommonServ {
    
    /**
     * 立即同步
     * @param paramBean 参数
     * @return 返回
     */
    public OutBean sync(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        Bean jobBean = ServDao.find("SY_COMM_SCHED", new Bean().set("JOB_CODE", "ORG_SYNC"));
        if (jobBean != null) {
            Map<String, String> jobDataMap = new HashMap<String, String>();
            String jobData = jobBean.getStr("JOB_DATA");
            String[] data = jobData.split(",");
            int len = data.length;
            for (int i = 0; i < len; i++) {
                String s = data[i];
                jobDataMap.put(s.split("=")[0], s.split("=")[1]);
            }
            AccountJob job = new AccountJob();
            String portalURL = Context.getSyConf("SY_PORTAL_IURL", null);
            if (portalURL == null) { // 如果接口地址没有配置则取正常的门户地址
                log.error("门户接口地址系统变量SY_PORTAL_IURL未配置，尝试取门户地址SY_PORTAL_URL！");
                portalURL = Context.getSyConf("SY_PORTAL_URL", null);
                if (portalURL == null) {
                    log.error("门户接口地址系统变量SY_PORTAL_IURL以及门户地址系统变量SY_PORTAL_URL均未配置！");
                    throw new RuntimeException("门户接口地址系统变量SY_PORTAL_IURL以及门户地址系统变量SY_PORTAL_URL均未配置！");
                }
            }
            job.setPortalURL(portalURL);
            job.setSysId(jobDataMap.get("SYS_ID"));
            job.setCmpyCode(jobDataMap.get("CMPY"));
            job.setUserLoginName(jobDataMap.get("USER"));
            job.setPassword(jobDataMap.get("PWD"));
            
            // 开始执行任务
            boolean locked = false;
            TaskLock lock = null;
            lock = new TaskLock("JOB", FilenameUtils.getName("AccountJob"));
            locked = lock.lock();
            try {
                if (locked) {
                    job.syncData();
                } else {
                    return outBean.setError("存在一个线程正在执行同步操作！");
                }
            } catch (Exception e) {
                // 如果不是最后一条数据抛出的异常则记录日志
                if (!(e instanceof TipException)) {
                    log.error(e.getMessage(), e);
                }
            } finally {
                lock.release();
                try {
                    if (job.getClient() != null) {
                        job.getClient().logout();
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return outBean.setOk("同步数据成功！");
    }
    
}
