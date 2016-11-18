package com.rh.bn.sync.job;

import java.net.InetAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.rh.bn.sync.impl.org.BnDeptSync;
import com.rh.bn.sync.impl.org.BnOrgSync;
import com.rh.bn.sync.impl.org.BnUserSync;
import com.rh.core.base.Bean;
import com.rh.core.comm.ConfMgr;
import com.rh.core.comm.schedule.job.InitializeContextJob;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.DateUtils;

/**
 * 百年人寿项目机构、部门、用户同步任务类
 * @author Tanyh 20151009
 *
 */
public class BnOrgAndUserSyncJob extends InitializeContextJob{

    private static Log log = LogFactory.getLog(BnOrgAndUserSyncJob.class);

    private static String hostname = getHostName();

    public void interrupt() {
        
    }

    @Override
    protected void executeJob(JobExecutionContext arg0) throws JobExecutionException {
        if (ConfMgr.getConf("BN_ORG_SYNC_HOST", "").length() == 0
                || hostname.equals(ConfMgr.getConf("BN_ORG_SYNC_HOST", ""))) {
            String beginTime = "";
            Bean syncLogBean = ServDao.find("BN_ORG_SYNC_LOG", (new ParamBean()).setOrder("SYNC_TIME desc"));
            if (syncLogBean != null) {
                beginTime = syncLogBean.getStr("SYNC_TIME");
            }

            
            // 根据数据的开始时间以时间正序的排序方式读取需要同步的组织机构数据
            (new BnOrgSync()).sync(beginTime);
            (new BnDeptSync()).sync(beginTime);
            //beginTime = "2015-04-19 09:00:00";
            (new BnUserSync()).sync(beginTime);
            // 记录Job执行时间
            logJobSyncTime();
        }
    }
    /**
     * 记录Job同步时间
     */
    private void logJobSyncTime() {
        ServDao.create("BN_ORG_SYNC_LOG",
                (new Bean()).set("SYNC_TIME", DateUtils.getDatetime()).set("SYNC_HOST", hostname));
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

}
