package com.rh.core.comm.wenku;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.comm.cms.mgr.ReqhisMgr;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;

/**
 * 
 * @author zhangjinxi
 *
 */
public class NoticeServ extends CommonServ {

    
    /**
     * 获取公告
     * @param paramBean - 参数bean
     * @return - outbean
     */
    public OutBean getNotice(ParamBean paramBean) {
        return byid(paramBean);
    }
    
    /**
     * 阅读量 +1
     * @param param - 参数bean
     */
    public void increaseReadCounter(ParamBean param) {
        String key = "NOTICE_READ_COUNTER";
        Bean notice = ServDao.find(ServMgr.SY_COMM_NOTICE, param);

        // 更新阅读历史
        String act = "read";
        ReqhisMgr.save(ServMgr.SY_COMM_NOTICE, notice.getId(), act, notice.getStr("NOTICE_TITLE"),
                notice.getStr("S_USER"));
        
        // 获取当前公告的阅读人数统计
        int count = ReqhisMgr.countUser(ServMgr.SY_COMM_NOTICE, notice.getId(), act);
        // 更新阅读人数
        notice.set("NOTICE_READ_PERSON_COUNTER", count);
        //更新阅读次数
        notice.set(key, notice.get(key, 0) + 1);
        ServDao.update(ServMgr.SY_COMM_NOTICE, notice);
    }

    /**
     * 分页获取notice
     * @param param 参数
     * @return notic列表
     */
    public OutBean getNoticeList(ParamBean param) {
        ParamBean queryBean = new ParamBean(ServMgr.SY_COMM_WENKU_NOTICE, ServMgr.ACT_QUERY);
        queryBean.setOrder("S_MTIME DESC");
        queryBean.setQueryPageShowNum(param.getInt("showNum"));
        queryBean.setQueryPageNowPage(param.getInt("currPage"));
        OutBean outBean = ServMgr.act(queryBean);
        List<Bean> list = outBean.getDataList();
        for (Bean b : list) {
            UserBean user = UserMgr.getUser(b.getStr("S_USER"));
            b.set("DEPT_NAME", user.get("DEPT_NAME"));
        }
        return outBean;
    }
}
