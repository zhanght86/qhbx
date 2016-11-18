package com.rh.oa.gs.serv;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServUtils;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.oa.cn.mgr.ConcertMgr;
import com.rh.oa.gs.mgr.QsMgr;
import com.rh.oa.gw.GwServ;
import com.rh.oa.gw.util.AuditUtils;

/**
 * 机关部门工作请示报告，新旧用一个机关代字
 * @author ruaho_hdy
 *
 */
public class JgServ extends GwServ {
    
    private static Log log = LogFactory.getLog(QsServ.class);

    @Override
    protected void afterByid(ParamBean paramBean, OutBean outBean) {
        super.afterByid(paramBean, outBean);
        // 如果是添加模式生成编号
        if (outBean.getByidAddFlag()) {
            // 取得请示类型
            String servId = outBean.getStr("TMPL_TYPE_CODE");
            // 取得机关代字
            String word = AuditUtils.getOrgWord(servId);
            if (StringUtils.isNotBlank(word)) {
                outBean.set(QsMgr.COL_GW_YEAR_CODE, word);
            } else {
                log.debug("当前部门没有配置请示报告(" + servId + ")的机关代字！");
                outBean.setError("当前部门没有配置请示报告的机关代字！");
            }
            // 年份
            if (StringUtils.isBlank(outBean.getStr(QsMgr.COL_GW_YEAR))) {
                outBean.set(QsMgr.COL_GW_YEAR, DateUtils.getYear());
            }
            // 取得流水号
            if (word != null) {
                int year = outBean.getInt(QsMgr.COL_GW_YEAR);
                int maxNum = getMaxNum(servId, word, year);
                outBean.set(QsMgr.COL_GW_YEAR_NUMBER, maxNum);
            }
        }
    }

    /**
     * 
     * @param servId 服务
     * @param word 代字号码
     * @param year 年度
     * @return 最大编号
     */
    private static int getMaxNum(String servId, String word, int year) {
        ServDefBean sdf = ServUtils.getServDef(servId);
        ParamBean queryBean = new ParamBean()
                .set(QsMgr.COL_GW_YEAR_CODE, word)
                .set(QsMgr.COL_GW_YEAR, year)
                .set(QsMgr.COL_TMPL_TYPE_CODE, sdf.getSrcId());
        return AuditUtils.getSerial(queryBean, servId, QsMgr.COL_GW_YEAR_NUMBER);
    }

    @Override
    protected void beforeQuery(ParamBean paramBean) {
        super.beforeQuery(paramBean);

        // 如果是查询的话
        boolean searchFlag = paramBean.getBoolean(ConcertMgr.COL_SEARCH_FLAG);
        if (searchFlag) {
            paramBean.setQueryExtWhere(QsMgr.getQuerySql(paramBean));
        }
    }

    /**
     * 校验文件编号是否重复
     * @param dataBean 包含编号数据的Bean
     * @param paramBean 参数Bean
     */
    protected void checkRepeatedGwCode(Bean dataBean, ParamBean paramBean) {
        checkRepeatedCode(dataBean, paramBean);
    }
    
    /**
     * 校验文件编号是否重复，如果参数重复，则取最大编号
     * @param dataBean 包含编号数据的Bean
     * @param paramBean 参数Bean
     */
    public static void checkRepeatedCode(Bean dataBean, ParamBean paramBean) {
        if (dataBean.getInt(GW_YEAR_NUMBER) == 0) {
            return;
        } else if (dataBean.isEmpty(GW_YEAR_CODE)) {
            return;
        } else if (dataBean.isEmpty(GW_YEAR)) {
            return;
        }

        String code = dataBean.getStr(GW_YEAR_CODE);
        String servId = dataBean.getStr("TMPL_CODE");
        ServDefBean sdf = ServUtils.getServDef(servId);
        

        SqlBean queryBean = new SqlBean();
        queryBean.and("TMPL_CODE", servId);
        queryBean.and("GW_YEAR_CODE", code);
        queryBean.and("GW_YEAR", dataBean.getInt(GW_YEAR));
        queryBean.and("GW_YEAR_NUMBER", dataBean.getInt(GW_YEAR_NUMBER));
        queryBean.and("S_FLAG", Constant.YES_INT);
        queryBean.selects(sdf.getPKey());

        List<Bean> gwList = ServDao.finds(servId, queryBean);

        boolean isRepeated = false;
        for (Bean bean : gwList) {
            if (!dataBean.getId().equals(bean.getId())) {
                isRepeated = true;
                break;
            }
        }

        if (isRepeated) { // 如果重复，则重取参数
            int maxNum = getMaxNum(servId, code, dataBean.getInt(GW_YEAR));
            paramBean.set(QsMgr.COL_GW_YEAR_NUMBER, maxNum);
        }
    }

}
