package com.rh.bn.invest;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.ServDao;

/**
 * 问卷调查模块帮助类
 * @author Jiling
 *
 */
public class QuestionUtil {
    
    private static final String BN_QS_OPTIONS_SERV = "BN_QS_OPTIONS";
    /**
     * 记录问题选项被选次数
     * @param answerStr 用户所选的问题选项串
     */
    public static void countQuestionOption(String answerStr) {
        synchronized (QuestionUtil.class) {
            // 根据选项串，获取选项信息
            List<Bean> dataList = ServDao.finds(BN_QS_OPTIONS_SERV,
                    " and option_id in ('" + answerStr.replaceAll(";", "','") + "')");
            if (dataList != null && dataList.size() > 0) {
                for (Bean data : dataList) {
                    data.set("CHOOSED_COUNT", data.getInt("CHOOSED_COUNT") + 1);
                    // 更新选项被选次数
                    ServDao.update(BN_QS_OPTIONS_SERV, data);
                }
            }
        }
    }
}
