package com.rh.bn.invest;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.ServDao;

/**
 * 投票评选模块帮助类
 * @author Jiling
 *
 */
public class SelectionVoteUtil {
    private static final String BN_VOTE_OPTION_SERV = "BN_VOTE_OPTION";
    /**
     * 记录投票选项被选次数
     * @param answerStr 用户所选的问题选项串
     */
    public static void countSelectionVote(String answerStr) {
        synchronized (SelectionVoteUtil.class) {
            // 根据选项串，获取选项信息
            List<Bean> dataList = ServDao.finds(BN_VOTE_OPTION_SERV,
                    " and option_id in ('" + answerStr.replaceAll(";", "','") + "')");
            if (dataList != null && dataList.size() > 0) {
                for (Bean data : dataList) {
                    data.set("CHOOSED_COUNT", data.getInt("CHOOSED_COUNT") + 1);
                    // 更新选项被选次数
                    ServDao.update(BN_VOTE_OPTION_SERV, data);
                }
            }
        }
    }
}
