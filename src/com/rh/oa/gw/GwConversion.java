package com.rh.oa.gw;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.org.UserBean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.ServUtils;
import com.rh.core.util.Constant;
import com.rh.core.wfe.WfAct;
import com.rh.core.wfe.WfProcess;
import com.rh.core.wfe.def.WfProcDef;
import com.rh.core.wfe.def.WfServCorrespond;
import com.rh.core.wfe.util.WfeConstant;

/**
 * 公文类型转换
 * @author anan
 *
 */
public class GwConversion {
    
    private String servId = "";
    
    private String dataId = "";
    
    private String newServId = "";

    /**
     * 
     * @param servId 服务 ID
     * @param dataId 数据ID 
     * @param newServId 转换之后的servId
     */
    public GwConversion(String servId, String dataId, String newServId) {
        this.servId = servId;
        this.dataId = dataId;
        this.newServId = newServId;
    }
    
    
    /**
     * 转换
     * @param userBean 办理用户
     * @return 转换结果
     */
    public String doConvert(UserBean userBean) {
        String rtnMsg = "";
        
        ServDefBean toServDef = ServUtils.getServDef(newServId);
        
        
        ParamBean queryBean = new ParamBean();
        queryBean.set(Constant.PARAM_SERV_ID, servId);
        queryBean.setId(dataId);
        
        OutBean gwBean = ServMgr.act(servId, "byid", queryBean);
        
        WfProcDef wfDef = WfServCorrespond.getProcDef(toServDef.getId(), gwBean);
        
        if (null != wfDef) { //定义有流程
            String procCode = wfDef.getStr("PROC_CODE");
            String piId = gwBean.getStr("S_WF_INST");
            
            //将活动的节点干掉，结束类型是， 类型转换
            finishNodeInst(userBean, piId);
            
            //修改 OA_GW_GONGWEN 表中  TMPL_CODE 和 TMPL_TYPE_CODE 的值
            updateGw(toServDef, gwBean);
            
            //流程实例表 将 PROC_CODE 和 SERV_ID 换成新的
            updateProcInst(newServId, procCode, piId);
            
            //添加一条新的节点实例，对应找到的流程的起草节点
            createNewInst(piId);
            
            //修改之前的附件，实体表  
            updateFiles(servId, dataId, toServDef.getSrcId());
            
            updateEntity(toServDef.getSrcId());
            
            //查意见的时候，没过滤serv_id, 只是根据data_id ,故不改？
        } else {
            rtnMsg = "没有定义流程";
        }
        rtnMsg = Constant.RTN_MSG_OK;
        
        return rtnMsg;
    }

    /**
     * 修改实体表 ServId
     * @param servSrcId 引用servId
     */
    private void updateEntity(String servSrcId) {
        ServDefBean toServDef = ServUtils.getServDef(newServId);
        Bean entity = new Bean();
        entity.set("SERV_ID", newServId);
        entity.set("SERV_NAME", toServDef.getName());
        entity.set("SERV_SRC_ID", servSrcId);
        
        Bean whereBean = new Bean();
        StringBuilder strWhere = new StringBuilder();
        strWhere.append(" and DATA_ID = '").append(dataId).append("'");
        
        whereBean.set(Constant.PARAM_WHERE, strWhere);
        
        ServDao.updates(ServMgr.SY_COMM_ENTITY, entity, whereBean);
    }


    /**
     * 
     * @param oldServId 旧服务ID
     * @param dataId 数据ID
     * @param srcId src_serv_id
     */
    private void updateFiles(String oldServId, String dataId, String srcId) {
        Bean uFile = new Bean();
        uFile.set("SERV_ID", srcId);
        
        Bean whereBean = new Bean();
        StringBuilder strWhere = new StringBuilder();
        strWhere.append(" and DATA_ID = '").append(dataId).append("'");
        
        whereBean.set(Constant.PARAM_WHERE, strWhere);
        
        ServDao.updates(ServMgr.SY_COMM_FILE, uFile, whereBean);
    }

    /**
     * 修改流程实例
     * @param convertToServ 转换成的服务
     * @param procCode 新流程编码
     * @param piId 流程实例
     */
    public void updateProcInst(String convertToServ, String procCode, String piId) {
        Bean uProcInst = new Bean(piId);
        uProcInst.set("PROC_CODE", procCode);
        uProcInst.set("SERV_ID", convertToServ);
        ServDao.update(ServMgr.SY_WFE_PROC_INST, uProcInst);
    }

    /**
     * 
     * @param toServDef 转换成的服务
     * @param gwBean 公文数据
     */
    private void updateGw(ServDefBean toServDef, OutBean gwBean) {
//        ParamBean paramBean = new ParamBean("OA_GW_TMPL_FW_GS", "modify", gwBean.getId());
//        paramBean.set("TMPL_CODE", toServDef.getId());
//        paramBean.set("TMPL_TYPE_CODE", toServDef.getSrcId());
//        
//        ServMgr.act(paramBean);
        
        Bean paramBean = new Bean(gwBean.getId());
        paramBean.set("TMPL_CODE", toServDef.getId());
        paramBean.set("TMPL_TYPE_CODE", toServDef.getSrcId());
        
        ServDao.update(newServId, paramBean);
    }

    /**
     * 结束正在运行的节点实例， 找到所有正在运行的节点
     * @param doUser 办理用户
     * @param piId 流程实例
     */
    private void finishNodeInst(UserBean doUser, String piId) {
        WfProcess wfProcess = new WfProcess(piId, true);
        
        List<WfAct> wfActs = wfProcess.getRunningWfAct();
        
        for (WfAct wfAct: wfActs) {
            wfAct.stop(doUser,  WfeConstant.NODE_DONE_TYPE_CONVERTION, WfeConstant.NODE_DONE_TYPE_CONVERTION_DESC);
        }
    }

    /**
     * 创建新的流程的起草点的节点实例
     * @param piId 流程实例ID
     */
    private void createNewInst(String piId) {
        WfProcess wfProcess = new WfProcess(piId, true);
        wfProcess.createStartWfNodeInst(null);
    }
}
