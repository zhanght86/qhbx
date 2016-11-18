package com.rh.oa.cd;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.comm.ConfMgr;
import com.rh.core.comm.mind.MindUtils;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.wfe.util.WfeConstant;
import com.rh.oa.cd.util.CdUtils;

/**
 * 督办单扩展类
 * 
 * @author cuihf
 * 
 */
public class SupeServ extends CommonServ {

    /**
     * 配置项：默认存为办理结果的意见编码
     */
    private static final String CONF_OA_CD_SUPE_DOMINDCODE = "OA_CD_SUPE_DOMINDCODE";

    /**
     * 字段：意见内容
     */
    private static final String COLUMN_MIND_CONTENT = "MIND_CONTENT";

    /**
     * 字段：办理结果
     */
    private static final String COLUMN_DO_MIND = "DO_MIND";

    /**
     * 字段：督办状态
     */
    private static final String COLUMN_SUPE_STATUS = "SUPE_STATUS";

    /**
     * 字段：流程实例状态
     */
    private static final String COLUMN_S_WF_STATE = "S_WF_STATE";

    /**
     * 督办状态：未完成
     */
    private static final int SUPE_STATUS_UNFINISH = 0;

    /**
     * 督办状态：已完成
     */
    private static final int SUPE_STATUS_FINISHED = 1;

    /**
     * 字段：流水号
     */
    private static final String SUPE_NUM = "SUPE_NUM";

    /**
     * 字段：年度
     */
    private static final String SUPE_YEAR = "SUPE_YEAR";

    /**
     * 字段：催办代字
     */
    private static final String SUPE_CODE = "SUPE_CODE";

    /**
     * 处理流程办结或取消办结的程序逻辑
     * @param paramBean 参数信息
     */
    public void beforeSave(ParamBean paramBean) {
        Bean oldBean = paramBean.getSaveOldData();
        if (oldBean.getInt(COLUMN_S_WF_STATE) == WfeConstant.WFE_PROC_INST_NOT_RUNNING
                || paramBean.getInt(COLUMN_S_WF_STATE) == WfeConstant.WFE_PROC_INST_NOT_RUNNING) {
            if (oldBean.getInt(COLUMN_S_WF_STATE) == WfeConstant.WFE_PROC_INST_IS_RUNNING) {
                // 以下为“办结”的逻辑：修改督办状态为“已完成”；如果办理结果未空，则取办理意见放入
                paramBean.set(COLUMN_SUPE_STATUS, new Integer(SUPE_STATUS_FINISHED));
                if (paramBean.getStr(COLUMN_DO_MIND).length() == 0) {
                    // 根据系统配置获取默认读取的意见类型，并获取相关的意见内容，放入办理结果中
                    String mindCode = ConfMgr.getConf(CONF_OA_CD_SUPE_DOMINDCODE, "");
                    if (mindCode.length() > 0) {
                        StringBuilder doMind = new StringBuilder("");
                        List<Bean> mindList = MindUtils.getMindListByCode(mindCode,
                                paramBean.getServId(), paramBean.getId());
                        for (Bean mindBean : mindList) {
                            doMind.append(mindBean.getStr(COLUMN_MIND_CONTENT) + "\n");
                        }
                        if (doMind.length() > 0) {
                            doMind.delete(doMind.length() - 1, doMind.length());
                        }
                        paramBean.set(COLUMN_DO_MIND, doMind.toString());
                    }
                }
            } else if (paramBean.getInt(COLUMN_S_WF_STATE) == WfeConstant.WFE_PROC_INST_IS_RUNNING) {
                // 以下为“取消办结”的逻辑：修改督办状态为“未完成”
                paramBean.set(COLUMN_SUPE_STATUS, new Integer(SUPE_STATUS_UNFINISH));
            }
        }
    }

    /**
     * 根据代字、年度取得最大流水号
     * @param paramBean 参数信息
     * @return 流水号
     */
    public OutBean getMaxCode(ParamBean paramBean) {
        String servId = paramBean.getServId();
        Bean cdBean = new Bean();
        cdBean.set(SUPE_CODE, paramBean.getStr(SUPE_CODE));
        cdBean.set(SUPE_YEAR, paramBean.getStr(SUPE_YEAR));
        OutBean codeBean = new OutBean(CdUtils.getMaxCode(servId, cdBean, SUPE_NUM));
        codeBean.setOk();
        return codeBean;
    }
}
