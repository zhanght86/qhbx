package com.rh.core.comm.zhidao;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.comm.cms.CacheableServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Constant;

/**
 * 知道用户领域服务类
 * 
 */
public class ZhidaoUserSubjectServ extends CacheableServ {

    /**
     * 批量增加用户领域数据
     * @param param - 参数
     * @return - 返回值
     */
    public OutBean addBatchSubject(ParamBean param) {
        String userId = param.getStr("USER_ID");
        String chnlIds = param.getStr("chnlIds");
        
        //查找当前有无这个userId的知道用户
        Bean query = new Bean();
        query.set("USER_ID", userId);
        Bean exits = ServDao.find(ServMgr.SY_COMM_ZHIDAO_USER, query);
        String zhidaoUserId = "";
        if (exits == null) {
            ParamBean addUserParam = new ParamBean();
            addUserParam.set("USER_ID", userId);
            addUserParam.set("USER_DESC", "");
            addUserParam.setServId(ServMgr.SY_COMM_ZHIDAO_USER);
            addUserParam.setAct(ServMgr.ACT_SAVE);
            zhidaoUserId = ServMgr.act(addUserParam).getId();
        } else {
            zhidaoUserId = exits.getId();
        }
        ParamBean  executeBean = new ParamBean();
        executeBean.set("zhidaoUserId", zhidaoUserId);
        executeBean.set("chnlIds", chnlIds);
        deleteOldSubject(executeBean);
        saveNewSubject(executeBean);
        return new OutBean();
    }
    
    /**
     * 批量删除掉原来旧的用户领域
     * @param param - 参数zhidaoUserId
     */
    protected void deleteOldSubject(ParamBean param) {
        String zhidaoUserId = param.getStr("zhidaoUserId");
        Bean deleteBean = new Bean();
        deleteBean.set(Constant.PARAM_WHERE, " and ZHIDAO_USER_ID = '" + zhidaoUserId + "'");
        ServDao.deletes(ServMgr.SY_COMM_ZHIDAO_USER_SUBJECT, deleteBean);
    }
    
    /**
     * 批量添加新的用户领域
     * @param param - 参数
     */
    protected void saveNewSubject(ParamBean param) {
        String[] chnlIdArray = param.getStr("chnlIds").split(",");
        String zhidaoUserId = param.getStr("zhidaoUserId");
        List<Bean> subjectList = new ArrayList<Bean>();
        for (int i = 0; i < chnlIdArray.length; i++) {
            Bean subjectBean = new Bean();
            subjectBean.set("ZHIDAO_USER_ID", zhidaoUserId);
            subjectBean.set("CHNL_ID", chnlIdArray[i]);
            subjectList.add(subjectBean);
        }
        ServDao.creates(ServMgr.SY_COMM_ZHIDAO_USER_SUBJECT, subjectList);
    }


    @Override
    protected String getCacheServId() {
        return ServMgr.SY_COMM_ZHIDAO_USER_SUBJECT;
    }
}
