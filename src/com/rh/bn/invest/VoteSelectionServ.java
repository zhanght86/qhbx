package com.rh.bn.invest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.org.UserBean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.JsonUtils;
import com.rh.core.util.RequestUtils;
import com.rh.lw.ct.mgr.ContractMgr;

/**
 * 投票评选服务
 * @author jiling
 * 
 */
public class VoteSelectionServ extends CommonServ {
    public static final String BN_VOTE_SELECTION_SERV = "BN_VOTE_SELECTION";
    public static final String BN_VOTE_THEME_SERV = "BN_VOTE_THEME";
    public static final String BN_VOTE_OPTION_SERV = "BN_VOTE_OPTION";
    public static final String BN_VOTE_RESULT_SERV = "BN_VOTE_RESULT";

    public Bean startVote(ParamBean paramBean) {
        // 投票评选
        OutBean outBean = new OutBean();
        SqlBean sqlBean = new SqlBean();
        sqlBean.setId(paramBean.getStr("SELECTION_ID"));
        Bean selectionBean = ServDao.find(BN_VOTE_SELECTION_SERV, sqlBean);
        outBean.set("SELECTION_INFO", selectionBean);
        // 投票主题
        SqlBean themeBean = new SqlBean();
        themeBean.and("SELECTION_ID", paramBean.getStr("SELECTION_ID")).asc("VOTE_NUMBER");
        List<Bean> themeListBean = new ArrayList<Bean>();
        themeListBean = ServDao.finds(BN_VOTE_THEME_SERV, themeBean);
        outBean.set("THEME_LIST", themeListBean);
        // 投票选项
        SqlBean optionBean = new SqlBean();

        StringBuilder themeSB = new StringBuilder();
        for (int i = 0; i < themeListBean.size(); i++) {
            themeSB.append(themeListBean.get(i).getStr("VOTE_ID")).append(",");
        }
        String themeStr = themeSB.toString();
        if (!"".equals(themeStr)) {
            themeStr = themeStr.substring(0, themeStr.length() - 1);
            optionBean.andIn("VOTE_ID", themeStr.split(","));
            List<Bean> optionListBean = new ArrayList<Bean>();
            optionListBean = ServDao.finds(BN_VOTE_OPTION_SERV, optionBean);
            outBean.set("OPTION_LIST", optionListBean);
            outBean.set("THEME_IDS", themeStr);
        }
        outBean.setToDispatcher("/bn/jsp/startVote.jsp");
        outBean.setOk();
        return outBean;
    }
    
    /**
     * 查询投票结果是否为空且投票人是否已存在
     * @param paramBean
     * @return
     */
    public OutBean votedCheck(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        // 评选ID
        String selectionID = paramBean.getStr("SELECTION_ID");
      //获取当前登录用户信息
      UserBean userBean = Context.getUserBean();
      if (userBean != null) {
          SqlBean sqlBean=new SqlBean();
          sqlBean.and("SELECTION_ID", selectionID);
          sqlBean.and("S_USER", userBean.getCode());
          sqlBean.andNotNull("ANS_RESULT");
          List<Bean> ansListBean =  ServDao.finds(BN_VOTE_RESULT_SERV, sqlBean);
              if(ansListBean.size()>0){
                  outBean.set("HAS_VATE", "yes");
              } else {
                  outBean.set("HAS_VATE", "no");
              }
      }else {
          throw new TipException("用户未登录，请重新登录!");
      }
        return outBean;
    }
    
    
    /**
     * 点击提交插入投票数据至投票結果表中，并记录候選人选项被选次数
     * @param paramBean
     * @return OutBean
     */
    public OutBean getSubmitBtnInsertIntoAnswer(ParamBean paramBean) {
        // 评选ID
        String selectionID = paramBean.getStr("SELECTION_ID");
        // 提交的选择题答案串，json格式为：{'主题ID':'答案串'.....}
        String answerStr = paramBean.getStr("ANS_RESULT");
        // 没有做任何投票，则抛错
        if ((answerStr == null) || (answerStr.length() <= 0)) {
            return new OutBean().setError("您还没有投票！");
        }
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
                String themeID = "";
                while (it.hasNext()) {
                    dataBean = new Bean();
                    // 问卷ID
                    dataBean.set("SELECTION_ID", selectionID);
                    // 问题ID
                    themeID = it.next().toString();
                    dataBean.set("VOTE_ID", themeID);
                    // 答案串
                    dataBean.set("ANS_RESULT", answerBean.getStr(themeID));
                    dataList.add(dataBean);
                    answerSb.append(";" + answerBean.getStr(themeID));
                }
                // 用户答案批量入库
                ServDao.creates(BN_VOTE_RESULT_SERV, dataList);

                // 记录问题选项被选次数
                SelectionVoteUtil.countSelectionVote(answerSb.substring(1));
            }
        } else {
            return new OutBean().setError("您还没有投票！");
        }
        return new OutBean().setOk();
    }

    /**
     * 点击候选人名字跳转到候选人信息界面展示选中的候选人信息
     * @param paramBean
     * @return OutBean
     */
    public Bean showCandidateInfo(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        SqlBean sqlBean = new SqlBean();
        sqlBean.setId(paramBean.getStr("SELECTION_ID"));
        Bean selectionBean = ServDao.find(BN_VOTE_SELECTION_SERV, sqlBean);
        outBean.set("SELECTION_INFO", selectionBean);
        // 投票主题
        SqlBean themeBean = new SqlBean();
        themeBean.and("SELECTION_ID", paramBean.getStr("SELECTION_ID")).asc("VOTE_NUMBER");
        List<Bean> themeListBean = new ArrayList<Bean>();
        themeListBean = ServDao.finds(BN_VOTE_THEME_SERV, themeBean);
        outBean.set("THEME_LIST", themeListBean);
        // 投票选项
        SqlBean optionBean = new SqlBean();

        StringBuilder themeSB = new StringBuilder();
        for (int i = 0; i < themeListBean.size(); i++) {
            themeSB.append(themeListBean.get(i).getStr("VOTE_ID")).append(",");
        }
        String themeStr = themeSB.toString();
        if (!"".equals(themeStr)) {
            themeStr = themeStr.substring(0, themeStr.length() - 1);
            optionBean.andIn("VOTE_ID", themeStr.split(","));
            optionBean.and("OPTION_ID", paramBean.getStr("OPTION_ID"));
            List<Bean> optionListBean = new ArrayList<Bean>();
            optionListBean = ServDao.finds(BN_VOTE_OPTION_SERV, optionBean);
            outBean.set("OPTION_LIST", optionListBean);
            
            Bean fileBean = null;
            fileBean = ServDao.find(ServMgr.SY_COMM_FILE, new Bean()
                    .set("SERV_ID", "BN_VOTE_OPTION").set("DATA_ID", optionListBean.get(0).getId()));

            String photoId = fileBean.getStr("FILE_ID");
            outBean.set("PHOTO_ID", photoId);
        }
        outBean.setToDispatcher("/bn/jsp/showCandidateInfo.jsp");
        outBean.setOk();
        return outBean;
    }

    /**
     * 点击预览投票结果展示当前的个候选人被投票信息
     * @param paramBean
     * @return OutBean
     */
    public Bean showVoteResult(ParamBean paramBean) {
        // 评选信息
        OutBean outBean = new OutBean();
        SqlBean sqlBean = new SqlBean();
        sqlBean.setId(paramBean.getStr("SELECTION_ID"));
        Bean selectionBean = ServDao.find(BN_VOTE_SELECTION_SERV, sqlBean);
        outBean.set("SELECTION_INFO", selectionBean);
        // 主题列表
        SqlBean themeBean = new SqlBean();
        themeBean.and("SELECTION_ID", paramBean.getStr("SELECTION_ID")).asc("VOTE_NUMBER");
        List<Bean> themeListBean = ServDao.finds(BN_VOTE_THEME_SERV, themeBean);
        // 返回至页面的主题列表对象
        List<Bean> themesList = new ArrayList<Bean>();
        // 根据问题获取问题选项
        if (themeListBean != null && themeListBean.size() > 0) {
            for (Bean theme : themeListBean) {
                // 只统计选择题
                if (theme.getInt("THEME_TYPE") == 1 || theme.getInt("THEME_TYPE") == 2) {
                    List<Bean> optionsList = ServDao.finds(BN_VOTE_OPTION_SERV, " and vote_id='" + theme.getId()
                            + "' and selection_id='" + selectionBean.getId() + "' order by CHOOSED_COUNT desc");
                    theme.set("OPTION_LIST", optionsList);
                }
                themesList.add(theme);
            }
        }
        outBean.set("THEME_LIST", themesList);
        outBean.setToDispatcher("/bn/jsp/showVoteResult.jsp");
        outBean.setOk();
        return outBean;

    }
}
