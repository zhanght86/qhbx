package com.rh.oa.cd;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.Context.THREAD;
import com.rh.core.comm.ConfMgr;
import com.rh.core.comm.schedule.job.InitializeContextJob;
import com.rh.core.serv.ServDao;
import com.rh.core.util.DateUtils;
import com.rh.oa.cd.util.CdUtils;

/**
 * 催督办的后台线程，处理自动起草催办单或督办单
 * 
 * @author cuihf
 * 
 */
public abstract class AbstractCdJob extends InitializeContextJob {
    private static Log log = LogFactory.getLog(AbstractCdJob.class);

    @Override
    protected void executeJob(JobExecutionContext context) throws JobExecutionException {
        String nowDate = DateUtils.getDate();
        /**
         * 读取自动催督办表中，需要催督办的数据
         */
        Bean autoBean = new Bean();
        autoBean.set("AUTO_TYPE", new Integer(getAutoType()));
        autoBean.set("AUTO_STATUS", new Integer(CdUtils.STATUS_AUTO_UNFINISH));
        StringBuilder sqlWhere = new StringBuilder("");
        sqlWhere.append(" and AUTO_TYPE=" + getAutoType());
        sqlWhere.append(" and AUTO_STATUS=" + CdUtils.STATUS_AUTO_UNFINISH);
        sqlWhere.append(" and AUTO_DATE<='" + nowDate + "'");
        sqlWhere.append(" and S_FLAG='1'");
        if (ConfMgr.getConf("OA_CD_MAX_TIMES", 0) > 0) {
            sqlWhere.append(" and AUTO_TIMES<" + ConfMgr.getConf("OA_CD_MAX_TIMES", 0));
        }
        if (ConfMgr.getConf("OA_CD_MAX_FAILS", 0) > 0) {
            sqlWhere.append(" and AUTO_FAILS<" + ConfMgr.getConf("OA_CD_MAX_FAILS", 0));
        }
        sqlWhere.append(" and S_CMPY='" + Context.getThreadStr(THREAD.CMPYCODE) + "'");

        List<Bean> beanList = ServDao.finds("OA_CD_AUTO", sqlWhere.toString());

        /**
         * 判断是否需要起草催督办单
         */
        for (Bean bean : beanList) {
            if (bean.getStr("AUTO_DATE").length() > 0) {
                if (bean.getStr("AUTO_DATE").equals(nowDate)) {
                    /**
                     * 如果预期催办日期为当前日期时，自动创建催督办
                     */
                    retrieveDataAndCreate(bean);
                } else if (bean.getInt("AUTO_INTERVAL") > 0) {
                    int passDays = DateUtils.selectDateDiff(nowDate, bean.getStr("AUTO_DATE"));
                    if (passDays > 0 && (passDays % (bean.getInt("AUTO_INTERVAL"))) == 0) {
                        retrieveDataAndCreate(bean);
                    }
                }
            }
        }
    }

    /**
     * 获取被催办或督办的数据，然后起草催办或督办单
     * @param bean 被催办或督办的数据信息
     */
    private void retrieveDataAndCreate(Bean bean) {
        try {
            if (bean.getStr("AUTO_TITLE").length() > 0 && bean.getStr("DRAFE_USER").length() > 0) {
                Bean dataBean = new Bean();
                dataBean.set("AUTO_TITLE", ConfMgr.getConf("OA_CD_TITLE_REMD", "") + bean.getStr("AUTO_TITLE"))
                        .set("S_USER", bean.getStr("DRAFE_USER"));
                create(bean, dataBean);
            } else {
                create(bean, ServDao.find(bean.getStr("SERV_ID"), bean.getStr("DATA_ID")));
            }
            bean.set("AUTO_TIMES", new Integer(bean.getInt("AUTO_TIMES") + 1));
            ServDao.update("OA_CD_AUTO", bean);
        } catch (Exception e) {
            log.error("起草催督办出错:" + bean.getStr("SERV_ID") + "@" + bean.getStr("DATA_ID"), e);
            bean.set("AUTO_FAILS", new Integer(bean.getInt("AUTO_FAILS") + 1));
            ServDao.update("OA_CD_AUTO", bean);
        }
    }

    /**
     * 获取催督办类型
     * @return 催督办类型
     */
    protected abstract int getAutoType();

    /**
     * 创建催督办单
     * @param autoBean 自动催办或督办的配置信息
     * @param dataBean 相关数据
     */
    protected abstract void create(Bean autoBean, Bean dataBean);

}
