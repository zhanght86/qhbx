package com.rh.core.comm.wenku;

import java.math.BigDecimal;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.integral.IntegralMgr;
import com.rh.core.comm.score.ScoreMgr;
import com.rh.core.org.UserBean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;

/**
 * @author liwei
 * 
 */
public class WenkuScoreServ extends CommonServ {

    private static final String DOCUMENT_SERV = ServMgr.SY_COMM_WENKU_DOCUMENT;

    /**
     * 获取投票评分信息
     * @param paramBean - param bean
     * @return out bean
     */
    public Bean show(Bean paramBean) {
        Bean document = ServDao.find(DOCUMENT_SERV, paramBean);
        UserBean user = Context.getUserBean();
        String voted = ScoreMgr.getInstance().isVoted(DOCUMENT_SERV, paramBean.getId(), user.getCode());
        // 是否已投票
        if (!("no_ip_voted".equals(voted)) && !("no_user_voted".equals(voted))) {
            document.set("VOTED", 1);
            document.set("VOTED_SCORE", voted);
        } else {
            document.set("VOTED", 2);
        }
        return document;
    }

    /**
     * 打分
     * @param paramBean - param bean
     * @return out bean
     */
    public Bean voteScore(Bean paramBean) {
        OutBean outBean = new OutBean();
        String documentId = paramBean.getStr("DATA_ID");
        Bean docQuery = new Bean().setId(documentId);
        int score = paramBean.get("VOTE_SCORE", 0);
        Bean scoreBean = ScoreMgr.getInstance().voteScore(DOCUMENT_SERV, documentId, score);
        int total = scoreBean.get("SCORE_TOTAL", 0);
        int counter = scoreBean.get("SCORE_SCORER_COUNTER", 1);

        docQuery.set("DOCUMENT_SCORER_COUNTER", counter);
        docQuery.set("DOCUMENT_AVG_SCORE", total / counter);
        BigDecimal b = new BigDecimal(total / counter);
        float avg = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();

        outBean.set("avg", avg + "");
        ServDao servBean = ServDao.update(DOCUMENT_SERV, docQuery);
        outBean.set("counter", servBean.getStr("DOCUMENT_SCORER_COUNTER"));

        // 增加积分
        UserBean currentUser = Context.getUserBean();
        OutBean docBean = new DocumentServ().byid(new ParamBean().setId(documentId));
        IntegralMgr.getInstance().handle(currentUser.getCode(), WenkuServ.SY_COMM_WENKU, ServMgr.SY_COMM_WENKU_DOCUMENT,
                docQuery.getId(), docBean.getStr("DOCUMENT_TITLE"), "SY_COMM_WENKU_DOCUMENT_SCORE");

        outBean.setOk("评分成功");
        return outBean;
    }

}
