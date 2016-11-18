package com.rh.oa.invest;

import java.util.ArrayList;
import java.util.List;


import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;

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
    public Bean surveyStatisShow(ParamBean paramBean) {
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
        outBean.setToDispatcher("/bn/jsp/surveyStatisShow.jsp");
        outBean.setOk();
        return outBean;
    }
    // 点击提交问卷插入数据至答案表中
    public Bean getSubmitBtnInsertIntoAnswer(ParamBean paramBean) {
        Bean dataBean = new Bean();
        String inputType = paramBean.getStr("INPUT_TYPE");
        dataBean.set("INVEST_ID", paramBean.getStr("INVEST_ID"));
        dataBean.set("QUESTION_ID", paramBean.getStr("QUESTION_ID"));
        if (inputType.equals("text")) {
            dataBean.set("ANS_CONTENT", paramBean.getStr("ANS_CONTENT"));
        }else{
            dataBean.set("ANS_RESULT", paramBean.getStr("ANS_RESULT"));
        }
        ServDao.create(BN_QS_ANSWER_SERV, dataBean);
        return new Bean().set(Constant.RTN_MSG, Constant.RTN_MSG_OK);
    }
}
