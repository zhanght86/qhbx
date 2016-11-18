package com.rh.bn.invest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;
import com.rh.core.util.JsonUtils;

/**
 * 问卷调查扩展服务类
 * @author Jiling
 *
 */
public class QsNaireInvestServ extends CommonServ {
    public static final String BN_QS_INVEST_SERV = "BN_QS_INVEST";
    public static final String BN_QS_QUESTION_SERV = "BN_QS_QUESTION";
    public static final String BN_QS_OPTIONS_SERV = "BN_QS_OPTIONS";
    public static final String BN_QS_ANSWER_SERV = "BN_QS_ANSWER";

    public Bean showQuestionNaire(ParamBean paramBean) {
        // 问卷
        OutBean outBean = new OutBean();
        SqlBean sqlBean = new SqlBean();
        sqlBean.setId(paramBean.getStr("INVEST_ID"));
        Bean investBean = ServDao.find(BN_QS_INVEST_SERV, sqlBean);
        outBean.set("SHIJUAN_INFO", investBean);
        // 问题列表
        SqlBean questionBean = new SqlBean();
        questionBean.and("INVEST_ID", paramBean.getStr("INVEST_ID")).asc("QUESTION_NUMBER");
        List<Bean> questionListBean = new ArrayList<Bean>();
        questionListBean = ServDao.finds(BN_QS_QUESTION_SERV, questionBean);
        outBean.set("QUES_LIST", questionListBean);
        // outBean.setData(questionListBean);
        // 选项
        SqlBean optionBean = new SqlBean();

        StringBuilder quesSB = new StringBuilder();
        for (int i = 0; i < questionListBean.size(); i++) {
            quesSB.append(questionListBean.get(i).getStr("QUESTION_ID")).append(",");
        }
        String quesStr = quesSB.toString();
        quesStr = quesStr.substring(0, quesStr.length() - 1);
        optionBean.andIn("QUESTION_ID", quesStr.split(","));
        List<Bean> optionListBean = new ArrayList<Bean>();
        optionListBean = ServDao.finds(BN_QS_OPTIONS_SERV, optionBean);
        outBean.set("OPTION_LIST", optionListBean);
        outBean.set("QUESTION_IDS", quesStr);
        outBean.setToDispatcher("/bn/jsp/showQuestionNaire.jsp");
        outBean.setOk();
        return outBean;
    }
    /**
     * 问卷结果统计
     * @param paramBean
     * @return OutBean
     */
    public OutBean surveyStatisShow(ParamBean paramBean) {
        // 问卷信息
        OutBean outBean = new OutBean();
        SqlBean sqlBean = new SqlBean();
        sqlBean.setId(paramBean.getStr("INVEST_ID"));
        Bean investBean = ServDao.find(BN_QS_INVEST_SERV, sqlBean);
        outBean.set("SHIJUAN_INFO", investBean);
        // 问题列表
        SqlBean questionBean = new SqlBean();
        questionBean.and("INVEST_ID", paramBean.getStr("INVEST_ID")).asc("QUESTION_NUMBER");
        List<Bean> questionListBean = ServDao.finds(BN_QS_QUESTION_SERV, questionBean);
        // 返回至页面的问题列表对象
        List<Bean> questionsList = new ArrayList<Bean>();
        // 根据问题获取问题选项
        if (questionListBean != null && questionListBean.size() > 0) {
            for (Bean question : questionListBean) {
                // 只统计选择题
                if (question.getInt("QUESTION_TYPE") == 1 || question.getInt("QUESTION_TYPE") == 2) {
                    List<Bean> optionsList = ServDao.finds(BN_QS_OPTIONS_SERV, " and question_id='" + question.getId()
                            + "' and invest_id='" + investBean.getId() + "'");
                    question.set("OPTIONS_LIST", optionsList);
                    // 获取问题选项被选总数
                    Bean totalBean = ServDao.find(
                            BN_QS_OPTIONS_SERV,
                            (new ParamBean()).setSelect(" sum(choosed_count) as TOTAL_CHOOSED ").setWhere(
                                    " and question_id='" + question.getId()
                                            + "' and invest_id='" + investBean.getId() + "'"));
                    if (totalBean != null && totalBean.contains("TOTAL_CHOOSED")) {
                        question.set("TOTAL_CHOOSED", totalBean.getInt("TOTAL_CHOOSED"));
                    }
                }
                questionsList.add(question);
            }
        }
        outBean.set("QUES_LIST", questionsList);
        outBean.setToDispatcher("/bn/jsp/surveyStatisShow.jsp");
        outBean.setOk();
        return outBean;
    }

    /**
     * 点击提交问卷插入数据至答案表中，并记录问题选项被选次数
     * @param paramBean
     * @return OutBean
     */
    public OutBean getSubmitBtnInsertIntoAnswer(ParamBean paramBean) {
        // 问卷ID
        String investID = paramBean.getStr("INVEST_ID");
        // 提交的选择题答案串，json格式为：{'问题ID':'答案串'.....}
        String answerStr = paramBean.getStr("ANS_RESULT");
        // 提交的简答题答案串，json格式为：{'问题ID':'答案'....}
        String answerContent = paramBean.getStr("ANS_CONTENT");
     
        // 解析选择题答案，并保存入答案库，记录选项被选择次数
            Bean answerBean = JsonUtils.toBean(answerStr);
            if (!answerBean.isEmpty()) {
                // 所有答案串，后边需依据它记录选项被选次数
                StringBuffer answerSb = new StringBuffer("");
                // 答案list，做批量入库操作
                List<Bean> dataList = new ArrayList<Bean>();
                // 依次获取问题ID，再依据问题ID获取答案串
                Iterator it = answerBean.keySet().iterator();
                if (it != null) {
                    Bean dataBean = null;
                    String questionID = "";
                    while (it.hasNext()) {
                        dataBean = new Bean();
                        // 问卷ID
                        dataBean.set("INVEST_ID", investID);
                        // 问题ID
                        questionID = it.next().toString();
                        dataBean.set("QUESTION_ID", questionID);
                        // 答案串
                        dataBean.set("ANS_RESULT", answerBean.getStr(questionID));
                        dataList.add(dataBean);
                        answerSb.append(";" + answerBean.getStr(questionID));
                    }
                    // 用户答案批量入库
                    ServDao.creates(BN_QS_ANSWER_SERV, dataList);
                    
                    // 记录问题选项被选次数
                    QuestionUtil.countQuestionOption(answerSb.substring(1));
                }
            }
        // 保存简答题
            Bean contentBean = JsonUtils.toBean(answerContent);
            if (!contentBean.isEmpty()) {
                // 答案list，做批量入库操作
                List<Bean> dataList = new ArrayList<Bean>();
                // 依次获取问题ID，再依据问题ID获取答案串
                Iterator it = contentBean.keySet().iterator();
                if (it != null) {
                    Bean dataBean = null;
                    String questionID = "";
                    while (it.hasNext()) {
                        dataBean = new Bean();
                        // 问卷ID
                        dataBean.set("INVEST_ID", investID);
                        // 问题ID
                        questionID = it.next().toString();
                        dataBean.set("QUESTION_ID", questionID);
                        // 答案串
                        dataBean.set("ANS_CONTENT", contentBean.getStr(questionID));
                        dataList.add(dataBean);
                    }
                    // 用户答案批量入库
                    ServDao.creates(BN_QS_ANSWER_SERV, dataList);
                }
            }
         // 没有做任何投票，则抛错
            if (answerBean.isEmpty() && contentBean.isEmpty()) {
                return new OutBean().setError("您还没有作答！");
            }else{
                return new OutBean().setOk();
            }
        }
   
}
