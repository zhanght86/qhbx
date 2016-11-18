package com.rh.bn.invest;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;

public class VoteSelectionOptionServ extends CommonServ{
    public static final String BN_VOTE_SELECTION_SERV = "BN_VOTE_SELECTION";
    public static final String BN_VOTE_THEME_SERV = "BN_VOTE_THEME";
    public static final String BN_VOTE_OPTION_SERV = "BN_VOTE_OPTION";
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
        if(!"".equals(themeStr)){
            themeStr = themeStr.substring(0, themeStr.length() - 1);
            optionBean.and("OPTION_ID", paramBean.getStr("OPTION_ID"));
            List<Bean> optionListBean = new ArrayList<Bean>();
            optionListBean = ServDao.finds(BN_VOTE_OPTION_SERV, optionBean);
            outBean.set("OPTION_LIST", optionListBean);
            outBean.set("THEME_IDS", themeStr);
        }
        outBean.setToDispatcher("/bn/jsp/showCandidateInfo.jsp");
        outBean.setOk();
        return outBean; 
        }
}
