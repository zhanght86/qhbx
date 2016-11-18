package com.rh.oa.mt;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.schedule.job.RhJob;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;

/**
 * 定时发送会议通知
 * @author ruaho_hdy
 * 
 */
public class SendNoticeJob implements RhJob {

    /** 日志对象 */
    private static Log log = LogFactory.getLog(SendNoticeJob.class);
    /** 办公用品申请单服务id */
    private static final String OA_OFF_APPLY = "OA_OFF_APPLY";
    /** 未发送领用通知 */
    private static final int NOT_STATUS = 1;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobData = context.getJobDetail().getJobDataMap();
        List<Bean> list = getApplyList();
        for (Bean b : list) {
            ParamBean paramBean = new ParamBean(); // 参数
            stuffParamBean(paramBean, b, jobData); // 填充对象
            try {
                ServMgr.act(OA_OFF_APPLY, "sendNoticeMsg", paramBean);
            } catch (Exception e) {
                log.error("办公用品领用通知发送异常，主键为：" + b.getId() + e);
            }
        }
    }

    /**
     * 填充参数对象
     * @param paramBean 参数
     * @param b 当前对象
     * @param jobData 当前任务对象
     */
    private void stuffParamBean(ParamBean paramBean, Bean b, JobDataMap jobData) {
        paramBean.setServId(OA_OFF_APPLY);
        paramBean.set("APPLY_ID", b.getId());
        paramBean.set("ids", b.getStr("S_USER"));
        String sendUser = jobData.getString("USER");
        if (StringUtils.isBlank(sendUser)) {
            log.error("定时任务中未配置办公用品领用通知发送用户信息！主键为：" + b.getId());
            throw new RuntimeException("定时任务中未配置办公用品领用通知发送用户信息！主键为：" + b.getId());
        }
        paramBean.set("SEND_USER", sendUser);
        paramBean.set("TODO_TITLE", "[办公用品领用通知]" + b.getStr("APPLY_TITLE"));
        String dateYM = Context.getSyConf("OA_OFF_NOTICE_TODO_CONTENT", "");
        if (!StringUtils.isBlank(dateYM)) {
            dateYM = dateYM.replaceAll("#DATE_YM#", b.getStr("SS_TIME").substring(0, 7));
        }
        paramBean.set("TODO_CONTENT", dateYM);
        paramBean.set("servId", OA_OFF_APPLY);
        paramBean.set("pk", b.getId());
    }

    /**
     * 获取审批单列表
     * @return 审批单列表
     */
    private List<Bean> getApplyList() {
        SqlBean sql = new SqlBean();
        sql.and("S_FLAG", Constant.YES).and("NOTICE_STATUS", NOT_STATUS).andNotNull("S_USER").andNotNull("SS_TIME");
        List<Bean> list = ServDao.finds(OA_OFF_APPLY, sql);
        return list;
    }

    public void interrupt() {
        // TODO Auto-generated method stub
    }
}
