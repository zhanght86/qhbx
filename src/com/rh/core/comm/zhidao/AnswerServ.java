package com.rh.core.comm.zhidao;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.comm.cms.CacheableServ;
import com.rh.core.comm.event.mgr.EventMgr;
import com.rh.core.comm.integral.IntegralMgr;
import com.rh.core.comm.vote.VoteMgr;
import com.rh.core.org.UserBean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.comment.CommentServ;
import com.rh.core.util.Constant;

/**
 * 
 * @author anan
 * 
 */
public class AnswerServ extends CacheableServ {
    /**
     * 回答服务
     */
    public static final String SY_COMM_ZHIDAO_ANSWER = ServMgr.SY_COMM_ZHIDAO_ANSWER;

    /**
     * 查询前添加查询条件
     * 
     * @param paramBean - 参数bean
     */
    public void beforeQuery(ParamBean paramBean) {
        
    }

    @Override
    protected void afterSave(ParamBean paramBean, OutBean outBean) {

        if (paramBean.getAddFlag()) {
            // 回答问题，增加积分
            String askId = outBean.getStr("Q_ID");
            String answerUser = outBean.getStr("S_USER");
            ParamBean askQuery = new ParamBean(ServMgr.SY_COMM_ZHIDAO_QUESTION, ServMgr.ACT_BYID);
            askQuery.setId(askId);
            Bean askBean = ServMgr.act(askQuery);

            String askTitle = askBean.getStr("Q_TITLE");
            Bean currentUser = Context.getUserBean();
            IntegralMgr.getInstance().handle(currentUser.getId(), ZhidaoServ.SY_COMM_ZHIDAO,
                    ServMgr.SY_COMM_ZHIDAO_ANSWER, outBean.getId(), askTitle, "SY_COMM_ZHIDAO_ANSWER_ADD");

            // 回答问题，保存事件
            String act = "ZHIDAO_CREATE_ANSWER";
            EventMgr.save(ZhidaoServ.SY_COMM_ZHIDAO, SY_COMM_ZHIDAO_ANSWER, outBean.getId(), act, askTitle, answerUser);
            outBean.setOk();
        }

        // 回答问题， 更新问题回答总数
        ParamBean countQuery = new ParamBean(ServMgr.SY_COMM_ZHIDAO_ANSWER, ServMgr.ACT_COUNT);
        countQuery.set("Q_ID", outBean.getStr("Q_ID"));
        int totalAnswer = ServMgr.act(countQuery).getCount();

        ParamBean updateQuery = new ParamBean(ServMgr.SY_COMM_ZHIDAO_QUESTION, ServMgr.ACT_SAVE);
        updateQuery.setId(outBean.getStr("Q_ID"));
        updateQuery.set("Q_ANSWER_COUNTER", totalAnswer);
        ServDao.save(ServMgr.SY_COMM_ZHIDAO_QUESTION, updateQuery);
    }

    /**
     * 保存答案的时候，判断有没有待办请自己回答，如果有，清除该待办
     * @param paramBean 参数Bean
     */
    public void beforeSave(ParamBean paramBean) {
        // UserBean userBean = Context.getUserBean();
        //
        // Bean delBean = new Bean();
        // delBean.set("OWNER_CODE", userBean.getCode());
        // delBean.set("TODO_OBJECT_ID1", paramBean.getStr("Q_ID"));
        // delBean.set("TODO_CODE", QuestionServ.ASK_SERVER);
        //
        // TodoUtils.destroys(delBean);
    }

    /**
     * 
     * @param paramBean 参数Bean
     * @return 是否需要显示 设置最佳的按钮
     */
    public OutBean getBestState(ParamBean paramBean) {
        String qid = paramBean.getStr("Q_ID");
        UserBean userBean = Context.getUserBean();

        String rtnStr = "notShow";
        // 是提问题的人 && 还没设置最佳答案
        Bean qBean = ServDao.find(QuestionServ.ASK_SERVER, qid);
        if (qBean.getStr("S_USER").equals(userBean.getCode()) && qBean.isEmpty("ANS_ID")) {
            rtnStr = "show";
        }

        OutBean rtnBean = new OutBean();
        rtnBean.set("rtnStr", rtnStr);

        return rtnBean;
    }

    /**
     * 
     * @param paramBean 参数Bean
     * @return 设置最佳答案
     */
    public OutBean setBestAnswer(ParamBean paramBean) {
        String qid = paramBean.getStr("Q_ID");
        String aid = paramBean.getStr("A_ID");

        // 更新问题
        Bean questionBean = new Bean(qid);
        questionBean.set("ANS_ID", aid);
        questionBean.set("Q_CLOSE", Constant.YES_INT);

        ServDao.update(QuestionServ.ASK_SERVER, questionBean);

        // 更新答案为最佳， 给分 TODO
        Bean answerBean = new Bean(aid);
        answerBean.set("A_BEST", Constant.YES_INT);

        ServDao.update(SY_COMM_ZHIDAO_ANSWER, answerBean);

        OutBean rtnBean = new OutBean();
        rtnBean.set("rtnStr", "success");

        return rtnBean;
    }

    /**
     * 
     * @param paramBean 参数Bean
     * @return 回答的评论数
     */
    public OutBean getAnsCommCount(ParamBean paramBean) {
        String ansIds = paramBean.getStr("ansIds");

        List<Bean> ansList = new ArrayList<Bean>();
        for (String ansId : ansIds.split(",")) {
            Bean ansBean = new Bean();
            int count = CommentServ.getCommentsCount(ansId, "OA_ZD_ANSWER");
            ansBean.setId(ansId);
            ansBean.set("count", count);

            ansList.add(ansBean);
        }

        OutBean rtnBean = new OutBean();
        rtnBean.set("rtnBean", ansList);

        return rtnBean;
    }

    /**
     * 更新评论次数
     * @param param - 参数bean
     * @return - out bean
     */
    public OutBean updateCommentCount(ParamBean param) {
        // select comment count
        ParamBean queryCount = new ParamBean(ServMgr.SY_SERV_COMMENT, ServMgr.ACT_COUNT);
        queryCount.set("SERV_ID", ServMgr.SY_COMM_ZHIDAO_ANSWER);
        queryCount.set("DATA_ID", param.getId());
        int count = ServMgr.act(queryCount).getCount();

        // update
        ParamBean upBean = new ParamBean(SY_COMM_ZHIDAO_ANSWER, ServMgr.ACT_SAVE);
        upBean.setId(param.getId());
        upBean.set("A_COMMENT_COUNTER", count);
        ServMgr.act(upBean);

        // response
        OutBean outBean = new OutBean();
        outBean.set("A_COMMENT_COUNTER", count);
        outBean.setOk();
        return outBean;
    }

    /**
     * 支持数量 +1
     * @param param - 参数bean
     * @return out bean
     */
    public OutBean increaseLikevote(ParamBean param) {
        OutBean outBean = new OutBean();
        String answerId = param.getId();
        UserBean currentUser = Context.getUserBean();
        // 检查该用户是否已投票
        if (VoteMgr.getInstance().isVoted(SY_COMM_ZHIDAO_ANSWER, answerId, currentUser.getCode())) {
            outBean.setError("您已参与投票！");
            return outBean;
        }
        // 更新支持票数
        String key = "A_LIKE_VOTE";
        Bean answer = ServDao.find(SY_COMM_ZHIDAO_ANSWER, param);
        answer.set(key, answer.get(key, 0) + 1);
        ServDao.update(SY_COMM_ZHIDAO_ANSWER, answer);

        // 更新投票记录
        VoteMgr.getInstance().vote(SY_COMM_ZHIDAO_ANSWER, answerId, 1);

        // 记录投票事件
        String act = "ZHIDAO_LIKE_ANSWER";
        ParamBean askQuery = new ParamBean(ServMgr.SY_COMM_ZHIDAO_QUESTION, ServMgr.ACT_BYID);
        askQuery.setId(answer.getStr("Q_ID"));
        OutBean ask = ServMgr.act(askQuery);
        String askTitle = ask.getStr("Q_TITLE");
        EventMgr.save(ZhidaoServ.SY_COMM_ZHIDAO, ServMgr.SY_COMM_ZHIDAO_ANSWER, answerId, act, askTitle,
                answer.getStr("S_USER"));
        // 增加积分
        IntegralMgr.getInstance().handle(answer.getStr("S_USER"), ZhidaoServ.SY_COMM_ZHIDAO,
                ServMgr.SY_COMM_ZHIDAO_ANSWER_VOTE, answerId, askTitle, "SY_COMM_ZHIDAO_ANSWER_VOTE_ADD");

        return outBean.setOk();
    }

    /**
     * 反对数量 +1
     * @param param - 参数bean
     * @return out bean
     */
    public OutBean increaseUnlikevote(ParamBean param) {
        OutBean outBean = new OutBean();
        String answerId = param.getId();
        UserBean currentUser = Context.getUserBean();
        // 检查该用户是否已投票
        if (VoteMgr.getInstance().isVoted(SY_COMM_ZHIDAO_ANSWER, answerId, currentUser.getCode())) {
            outBean.setError("您已参与投票！");
            return outBean;
        }

        // 更新反对票数
        String key = "A_DISLIKE_VOTE";
        Bean topic = ServDao.find(SY_COMM_ZHIDAO_ANSWER, param);
        topic.set(key, topic.get(key, 0) + 1);
        ServDao.update(SY_COMM_ZHIDAO_ANSWER, topic);

        // 更新投票记录
        VoteMgr.getInstance().vote(SY_COMM_ZHIDAO_ANSWER, answerId, 2);

        return outBean.setOk();
    }

    /**
     * 更新最佳答案
     * @param param - 参数bean
     * @return - out bean
     */
    public OutBean updateBestAnswer(ParamBean param) {
        // update
        ParamBean upBean = new ParamBean(AnswerServ.SY_COMM_ZHIDAO_ANSWER, ServMgr.ACT_SAVE);
        upBean.setId(param.getId());
        upBean.set("Q_FEEDBACK", param.getStr("Q_FEEDBACK"));
        upBean.set("A_BEST", 1);
        OutBean outBean = ServMgr.act(upBean);
        
        //增加积分
        IntegralMgr.getInstance().handle(outBean.getStr("S_USER"), 
                ZhidaoServ.SY_COMM_ZHIDAO, ServMgr.SY_COMM_ZHIDAO_ANSWER,
                outBean.getId(), outBean.getStr("A_TITLE"), "SY_COMM_ZHIDAO_ANSWER_SET_BEST");
        
        // response
        outBean.setOk();
        return outBean;
    }
    
    /**
     * 更新管理员最佳答案
     * @param param - 参数Bean
     * @return - 返回值
     */
    public OutBean updateAdminBestAnswer(ParamBean param) {
        ParamBean upBean = new ParamBean(AnswerServ.SY_COMM_ZHIDAO_ANSWER, ServMgr.ACT_SAVE);
        upBean.setId(param.getId());
        upBean.set("A_ADMIN_BEST", 1);
        OutBean outBean = ServMgr.act(upBean);
        return outBean;
    }

    @Override
    protected String getCacheServId() {
        return ServMgr.SY_COMM_ZHIDAO_ANSWER;
    }
    
    public OutBean getAdoptionRate(ParamBean paramParamBean)
    {
      String str1 = paramParamBean.getStr("userId");
      if ((null == str1) || (0 == str1.length())) {
        throw new TipException("userId can not be null!");
      }
      ParamBean localParamBean1 = new ParamBean("SY_COMM_ZHIDAO_ANSWER", "count");
      localParamBean1.set("S_USER", str1);
      int i = ServMgr.act(localParamBean1).getCount();

      ParamBean localParamBean2 = new ParamBean("SY_COMM_ZHIDAO_ANSWER", "count");
      String str2 = " AND (A_BEST=1 OR A_ADMIN_BEST=1 ) AND S_USER='" + str1 + "' ";
      localParamBean2.setWhere(str2);
      int j = ServMgr.act(localParamBean2).getCount();

      double d = 0;
      if (i > 0) {
    	  d = j / i;
      }
      return new OutBean().set("adp", Double.valueOf(d));
    }
}
