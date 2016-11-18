package com.rh.core.wfe.serv;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.base.BeanUtils;
import com.rh.core.base.Context;
import com.rh.core.base.Context.APP;
import com.rh.core.base.TipException;
import com.rh.core.comm.FileMgr;
import com.rh.core.comm.mind.MindUtils;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.ServUtils;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.serv.bean.WfOutBean;
import com.rh.core.serv.bean.WfParamBean;
import com.rh.core.serv.relate.RelatedServCreator;
import com.rh.core.serv.relate.RelatedServCreatorSetting;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.core.util.JsonUtils;
import com.rh.core.util.Lang;
import com.rh.core.util.RequestUtils;
import com.rh.core.util.msg.MsgCenter;
import com.rh.core.wfe.WfAct;
import com.rh.core.wfe.WfParam;
import com.rh.core.wfe.WfProcess;
import com.rh.core.wfe.WfProcessFactory;
import com.rh.core.wfe.attention.AttentionMsg;
import com.rh.core.wfe.condition.VarResource;
import com.rh.core.wfe.db.WfLineDao;
import com.rh.core.wfe.db.WfNodeDefDao;
import com.rh.core.wfe.db.WfNodeInstDao;
import com.rh.core.wfe.db.WfProcDefDao;
import com.rh.core.wfe.db.WfProcInstDao;
import com.rh.core.wfe.db.WfProcInstHisDao;
import com.rh.core.wfe.def.WFParser;
import com.rh.core.wfe.def.WfLineDef;
import com.rh.core.wfe.def.WfNodeDef;
import com.rh.core.wfe.def.WfProcDef;
import com.rh.core.wfe.def.WfProcDefManager;
import com.rh.core.wfe.def.WfServCorrespond;
import com.rh.core.wfe.resource.GroupBean;
import com.rh.core.wfe.resource.WfBinderManager;
import com.rh.core.wfe.resource.WfeBinder;
import com.rh.core.wfe.util.ComparatorBtnSort;
import com.rh.core.wfe.util.WfeConstant;

/**
 * 流程定义 服务类
 * 
 */
public class ProcDefServ extends CommonServ {
    private static Log log = LogFactory.getLog(ProcDefServ.class);

    /**
     * 将工作流的信息加到返回前台的Bean数据中
     * @param servId 服务ID
     * @param outBean 返回前台Bean , 一般是指具体的业务数据Bean
     * @param paramBean 参数Bean
     */
    public void addWfInfoToOut(String servId, Bean outBean, ParamBean paramBean) {
        //如果有忽略流程参数，则不加载流程信息。
        if (paramBean.isNotEmpty(Constant.IGNORE_WF_INFO)) {
            outBean.set(Constant.IGNORE_WF_INFO, "true");
            return;
        }
        
        // 先判断outBean是挂载流程信息了
        if (outBean.getStr("S_WF_INST") != null && outBean.getStr("S_WF_INST").length() > 0) {
            String pid = outBean.getStr("S_WF_INST");
            String procRunning = outBean.getStr("S_WF_STATE");

            boolean procInstIsRunning = true;
            if (outBean.getInt("S_FLAG") == Constant.NO_INT) { //如果数据被删除，则流程都被移動到歷史表。
                procInstIsRunning = false;
                procRunning = Constant.NO;
            } else {
                procInstIsRunning = this.procInstIsRunning(procRunning);
            }

            outBean.set("INST_IF_RUNNING", procRunning);
            outBean.set("PI_ID", pid);
            
            UserBean doUserBean = paramBean.getDoUserBean();
            if (!Context.isCurrentUser(doUserBean)) {
                outBean.set(Constant.AGENT_USER, doUserBean.getCode());
                outBean.set(Constant.AGENT_USER_BEAN, doUserBean);
            }

            WfAct wfAct = null;

            if (!paramBean.isEmpty("NI_ID")) { // 从参数中传过来了节点实例ID
                String nid = paramBean.getStr("NI_ID");
                wfAct = new WfAct(nid, procInstIsRunning);
                log.error("从参数中传过来了节点实例ID:"+nid);
            } else { // 查TO_USER_ID 得到最后的那个节点实例
            	log.error("查TO_USER_ID 得到最后的那个节点实例 ");
                wfAct = this.getUserLastToDoWfAct(doUserBean, pid, procInstIsRunning);
            }

            WfProcess process;
            
            if (wfAct != null) {
                process = wfAct.getProcess();
            } else {
                process = this.getWfProcess(pid, procInstIsRunning);
            }
            
            process.setServInstBean(outBean);
            if (wfAct != null) {
            	log.error("wfAct对象："+wfAct);
                outBean.set("nodeInstBean", wfAct.getNodeInstBean());
            }
            // 设置提醒的标题，在分发到时候，有用到
            String bindTitle = process.getProcInstTitle();
            outBean.set("bindTitle", bindTitle);

            // 根据办理人员的类型，组织相关数据
            WfOut wfOutBean = null;
            
            if (paramBean.isNotEmpty("SEND_ID")) { // 优先处理分发，因为分发过去的不需要有其他的按钮
                // 分发接收用户，或其他有权限浏览文件的用户
                outBean.set("SEND_ID", paramBean.getStr("SEND_ID"));
                wfOutBean = new SendOutBean(process, outBean, paramBean);
            } else {
                // 流程当前办理人
                if (null != wfAct && wfAct.isUserDoing(doUserBean.getCode())) {
                	log.error("流程当前办理人: "+doUserBean.getCode());
                    wfOutBean = new DoingOutBean(process, outBean, paramBean);
                } else if (wfAct != null) {
                    // 流经
                	log.error("流经: rh.core.wfe.serv.ProcDefServ 160");
                    wfOutBean = new FlowOutBean(process, outBean, paramBean);

                    if (process.isProcManage()) {
                        wfOutBean.fillOutBean(wfAct);
                        // 流程管理员
                        wfOutBean = new AdminOutBean(process, outBean, paramBean);
                    }
                } else if (process.isProcManage()) {
                    // 流程管理员
                    wfOutBean = new AdminOutBean(process, outBean, paramBean);
                } else {
                    // 分发接收用户，或其他有权限浏览文件的用户
                    wfOutBean = new BaseOutBean(process, outBean, paramBean);
                }
            }
            wfOutBean.setDoUser(doUserBean);
            wfOutBean.fillOutBean(wfAct);
            wfOutBean.filter(wfAct);

            // 对按钮进行排序
            @SuppressWarnings("unchecked")
            List<Bean> btnList = (List<Bean>) outBean.get("buttonBean");

            ComparatorBtnSort comparator = new ComparatorBtnSort();

            Collections.sort(btnList, comparator);
//            log.debug(JsonUtils.toJson(outBean));
        }
    }

    /**
     * @param paramBean 参数bean
     * @return 下一步的节点列表
     */
    public OutBean getNextSteps(ParamBean paramBean) {
        String procRunning = paramBean.getStr("S_WF_STATE");

        boolean procInstIsRunning = this.procInstIsRunning(procRunning);

        WfAct wfAct = null;
        if (!paramBean.isEmpty("NI_ID")) { // 从参数中传过来了节点实例ID
            String nid = paramBean.getStr("NI_ID");
            wfAct = new WfAct(nid, procInstIsRunning);
        } else {
            OutBean rtnBean = new OutBean();
            rtnBean.set("rtnStr", "不是当前节点,没有下一步的按钮");

            return rtnBean;
        }

        List<Bean> nextSteps = wfAct.getNextAvailableSteps(paramBean.getDoUserBean());

        OutBean rtnBean = new OutBean();
        rtnBean.set("rtnStr", "success");
        rtnBean.set("nextSteps", nextSteps);

        return rtnBean;

    }

    /**
     * @param paramBean 参数bean
     * @return 返回前台Bean
     */
    public OutBean cmLockFile(ParamBean paramBean) {
        UserBean userBean = paramBean.getDoUserBean();

        Bean procBean = new Bean();
        procBean.setId(paramBean.getStr("PI_ID"));
        procBean.set("INST_LOCK", WfProcess.PROC_INST_LOCK);
        procBean.set("INST_LOCK_USER", userBean.getCode());
        procBean.set("INST_LOCK_TIME", DateUtils.getDatetime());

        WfProcInstDao.updateWfProcInst(procBean);

        OutBean rtnBean = new OutBean();
        rtnBean.set("rtnstr", "success");

        return rtnBean;
    }

    /**
     * @param paramBean 参数bean
     * @return 返回前台Bean
     */
    public OutBean cmUnLockFile(ParamBean paramBean) {
        Bean procBean = new Bean();
        procBean.setId(paramBean.getStr("PI_ID"));
        procBean.set("INST_LOCK", WfProcess.PROC_INST_LOCK_NO); // 不锁定
        procBean.set("INST_LOCK_USER", "");
        procBean.set("INST_LOCK_TIME", "");

        WfProcInstDao.updateWfProcInst(procBean);

        OutBean rtnBean = new OutBean();
        rtnBean.set("rtnstr", "success");

        return rtnBean;
    }


    


    /**
     * 如果是发部门/角色， 点进去之后， 将TO_USER_ID 设置成这个人， 其他人进页面的时候，判断TO_USER_ID如果有了，则不出独占的按钮了
     * @param paramBean 参数Bean
     * @return 返回前台结果
     */
    public OutBean duZhan(ParamBean paramBean) {
        String pid = paramBean.getStr("PI_ID");

        WfAct wfAct = null;
        if (!paramBean.isEmpty("NI_ID")) {
            String nid = paramBean.getStr("NI_ID");
            wfAct = new WfAct(nid, true);
        } else {
            wfAct = this.getUserLastToDoWfAct(paramBean.getDoUserBean(), pid, true);
        }

        if (wfAct.canDuzhan()) { //点了独占之后，添加TO_USER_ID 和 TO_USER_NAME
            UserBean realUser = paramBean.getDoUserBean();
            wfAct.duzhan(realUser);
        }

        OutBean rtnBean = new OutBean();
        rtnBean.set("rtnstr", "success");

        return rtnBean;
    }

    /**
     * 结束并发
     * @param paramBean 参数
     * @return 合并后的节点实例
     */
    public OutBean converge(ParamBean paramBean) {
        String pid = paramBean.getStr("PI_ID");
        log.debug("合并工作流开始 piid = " + pid);

        WfAct wfAct = null;
        if (!paramBean.isEmpty("NI_ID")) {
            String nid = paramBean.getStr("NI_ID");
            wfAct = new WfAct(nid, true);
        } else {
            wfAct = this.getUserLastToDoWfAct(paramBean.getDoUserBean(), pid, true);
        }
        
        WfParam wfParam = new WfParam();
        wfParam.setDoneUser(paramBean.getDoUserBean());
        wfParam.setDoneType(WfeConstant.NODE_DONE_TYPE_STOP);
        wfParam.setDoneDesc(WfeConstant.NODE_DONE_TYPE_STOP_DESC);
        
        UserBean currentUser = Context.getUserBean();
        //当前用户和办理用户不一致，则设置成代理
        if (!currentUser.getCode().equals(wfParam.getDoneUser().getCode())) {
            wfParam.setIsAgent(true);
        }        
        
        WfAct newConverge = wfAct.convergeParallel(wfParam);

        return new OutBean(newConverge.getNodeInstBean());
    }

    /**
     * 删除指定流程的所有版本，先删除节点定义，连线定义
     * @param paramBean 参数
     * @return 删除状态
     */
    public OutBean deleteProcDef(ParamBean paramBean) {
        String procDefIds = paramBean.getStr("procIds");
        String[] procDefIdArray = procDefIds.split(",");
        // 删除该流程的所有版本
        for (int i = 0; i < procDefIdArray.length; i++) {
            String procCode = procDefIdArray[i];
            String procCodeWithoutVersion = 
                procCode.substring(0, procCode.lastIndexOf(WfeConstant.PROC_VERSION_PREFIX));
            int version = 
                Integer.parseInt(procCode.substring(procCode.lastIndexOf(WfeConstant.PROC_VERSION_PREFIX) + 1));
            for (int j = 1; j <= version; j++) {
                deleteProcDef(procCodeWithoutVersion + WfeConstant.PROC_VERSION_PREFIX + j);
            }
        }

        OutBean rtnBean = new OutBean();
        rtnBean.set("rtnstr", "success");

        return rtnBean;
    }

    /**
     * 删除指定版本的流程定义信息，先删除节点定义，连线定义 将下一版本的置为最新版本
     * @param paramBean 参数
     * @return 删除状态
     */
    public OutBean deleteProcDefOfSpecVersion(ParamBean paramBean) {
        String procDefIds = paramBean.getStr("procIds");
        String[] procDefIdArray = procDefIds.split(",");
        // 删除以procDefId为
        for (int i = 0; i < procDefIdArray.length; i++) {
            // 删除
            String procCode = procDefIdArray[i];
            String procCodeWithoutVersion = 
                procCode.substring(0, procCode.lastIndexOf(WfeConstant.PROC_VERSION_PREFIX));
            Bean oldProcBean = WfProcDefDao.getWfProcBeanByProcCode(procCode);
            // 删除流程
            deleteProcDef(procCode);
            // 重新设置流程的最新版本
            if (oldProcBean.getInt("PROC_IS_LATEST") == WfeConstant.PROC_IS_LATEST) {
                setProcDefLatest(procCodeWithoutVersion);
            }
        }

        OutBean rtnBean = new OutBean();
        rtnBean.set("rtnstr", "success");

        return rtnBean;
    }

    /**
     * 删除指定的procCode的流程定义，先删除节点定义，连线定义
     * @param procCode 流程定义主键
     */
    private void deleteProcDef(String procCode) {

        // 删除缓存中的定义
        Bean oldProcBean = WfProcDefDao.getWfProcBeanByProcCode(procCode);

        if (null != oldProcBean) {
            String oldServId = oldProcBean.getStr("SERV_ID");
            ParamBean param = new ParamBean(ServMgr.SY_SERV, "clearCache");
            param.setId(oldServId);
            ServMgr.act(param);
            
            //删除服务 流程对应的 缓存
            WfServCorrespond.removeFromCache(oldProcBean.getStr("SERV_ID"));

            // 删除节点
            WfNodeDefDao.deleteNodeDefByProcCode(procCode);

            // 删除连线
            WfLineDao.deleteLineDefByProcCode(procCode);

            // 删除流程
            WfProcDefDao.delWfProcDefBeanByProcCode(procCode);
        }
    }

    /**
     * 将流程定义导出<br>
     * 导出的格式为：zip包，里面为各个服务定义的json文件<br>
     * @param paramBean 参数，用procIds表示要导出的流程的proc_code
     * @return Bean
     */
    public OutBean export(ParamBean paramBean) {
        OutBean resultBean = new OutBean();

        String procDefIds = paramBean.getStr("procIds");
        if (procDefIds.indexOf(Constant.SEPARATOR) > -1) {
            procDefIds = procDefIds.replaceAll(Constant.SEPARATOR, "'" + Constant.SEPARATOR + "'");
        }
        Bean queryBean = new Bean();
        queryBean.set(Constant.PARAM_WHERE, "AND PROC_CODE IN ('" + procDefIds + "')");
        queryBean.set(Constant.PARAM_ORDER, "EN_NAME DESC, PROC_VERSION DESC");
        List<Bean> procBeanList = ServDao.finds(ServMgr.SY_WFE_PROC_DEF, queryBean);

        HttpServletRequest request = Context.getRequest();
        HttpServletResponse response = Context.getResponse();
        response.setContentType("application/x-download");
        RequestUtils.setDownFileName(request, response, ServMgr.SY_WFE_PROC_DEF + ".zip");
        ZipOutputStream zipOut = null;
        try {
            zipOut = new ZipOutputStream(response.getOutputStream());
            for (Bean procBean : procBeanList) {
            	//在流程定义上添加上公共按钮
            	SqlBean sql = new SqlBean();
            	sql.set("PROC_CODE", procBean.getId());
            	List<Bean> pActs = ServDao.finds(ServMgr.SY_WFE_NODE_PACTS, sql);
            	procBean.set("PUBLIC_ACTS", pActs);
            	
                zipOut.putNextEntry(new ZipEntry(procBean.getId() + ".json"));
                IOUtils.write(JsonUtils.toJson(procBean, true), zipOut, Constant.ENCODING);
                zipOut.closeEntry();
            }
        } catch (Exception e) {
            resultBean.setError();
            log.error("流程导出失败", e);
            e.printStackTrace();
        } finally {
            if (zipOut != null) {
                try {
                    zipOut.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                IOUtils.closeQuietly(zipOut);
                zipOut = null;
            }
        }

        return resultBean;
    }

    /**
     * 导入流程定义 如果系统中已经有此流程定义，则将导入的流程定义存为新版本
     * @param paramBean 要导入的文件的fileId
     * @return Bean
     */
    public OutBean importProcDef(ParamBean paramBean) {
        OutBean resultBean = new OutBean();
        int count = 0; // 导入的流程数量
        String fileId = paramBean.getStr("fileId");
        Bean fileBean = FileMgr.getFile(fileId);
        if (fileBean != null) {
            ZipInputStream zipIn = null;
            InputStream in = null;
            try {
                if (fileBean.getStr("FILE_MTYPE").equals("application/zip")) {
                    zipIn = new ZipInputStream(FileMgr.download(fileBean));
                    while (zipIn.getNextEntry() != null) {
                        in = new BufferedInputStream(zipIn);
                        impJsonBean(JsonUtils.toBean(IOUtils.toString(in, Constant.ENCODING)));
                        count++;
                        zipIn.closeEntry();
                    }
                } else {
                    in = FileMgr.download(fileBean);
                    impJsonBean(JsonUtils.toBean(IOUtils.toString(in, Constant.ENCODING)));
                    count++;
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            } finally {
                if (zipIn != null) {
                    IOUtils.closeQuietly(zipIn);
                }
                if (in != null) {
                    IOUtils.closeQuietly(in);
                }
            }
        }
        if (count > 0) {
            resultBean.setOk(count + "个流程导入成功！");
            FileMgr.deleteFile(fileBean);
        } else {
            resultBean.setError();
        } 
        
        return resultBean;
    }

    /**
     * 导入为流程定义
     * @param procDefBean 流程定义Bean
     */
    private void impJsonBean(Bean procDefBean) {
        ParamBean param = new ParamBean(procDefBean);
        param.setServId(ServMgr.SY_WFE_PROC_DEF).set("xmlStr", procDefBean.getStr("PROC_XML"));
        saveWfAsNewVersion(param);
    }

    /**
     * 办结
     * @param paramBean 参数信息
     * @return 返回前台参数
     */
    public OutBean finish(ParamBean paramBean) {
        String pid = paramBean.getStr("PI_ID");

        Bean aProcInstBean = WfProcInstDao.findProcInstById(pid);

        WfParam wfParam = new WfParam();

        UserBean doneUser = paramBean.getDoUserBean();

        wfParam.setDoneUser(doneUser);
        
        UserBean currentUser = Context.getUserBean();
        //当前用户和办理用户不一致，则设置成代理
        if (!currentUser.getCode().equals(wfParam.getDoneUser().getCode())) {
            wfParam.setIsAgent(true);
        }

        WfProcess wfProcess = new WfProcess(aProcInstBean);

        String servId = wfProcess.getServId();
        
        paramBean.set("wfProcess", wfProcess);
        ServMgr.act(servId, "beforeFinish", paramBean);
        
        wfProcess.finish(wfParam);

        // 启用部门内未启用的意见，常用于审批单不出部门就办结的情况。
        MindUtils.enableMindInDept(wfProcess.getDocId(), doneUser.getTDeptCode());

        ServMgr.act(servId, "afterFinish", paramBean);
        
        OutBean rtnBean = new OutBean();
        rtnBean.set("rtnstr", "success");

        return rtnBean;
    }

    /**
     * 根据serv_ID 获取服务类别
     * @param paramBean 参数Bean
     * @return 文件类型串
     */
    public OutBean getServFileType(ParamBean paramBean) {
        String servId = paramBean.getStr("SERV_ID");

        Bean servDefBean = ServUtils.getServDef(servId);

        OutBean rtnBean = new OutBean();

        rtnBean.set("rtnStr", servDefBean.getStr("SERV_FILE_CAT"));

        return rtnBean;
    }

    /**
     * 在未办结的时候，取User 最后办理的节点实例
     * @param doUser 办理用户
     * @param pid 节点实例ID
     * @param isRunningData 流程数据是否保存在运行表
     * @return User 最后办理的节点实例
     */
    private WfAct getUserLastDoneWfAct(UserBean doUser, String pid, boolean isRunningData) {

        return this.getWfProcess(pid, isRunningData).getUserLastDoneWfAct(doUser);
    }

    /**
     * 在 未办结的时候，取TO_USER_ID 的实例
     * @param doUser 办理用户
     * @param pid 节点实例ID
     * @param isRunningData 流程数据是否保存在运行表中
     * @return User 最后办理的节点实例。可能有以下几种情况：1，现在正在办理中的；2，我已经办理完成的、最后一次办理的实例（用于处理收回的情况）；
     */
    private WfAct getUserLastToDoWfAct(UserBean doUser, String pid, boolean isRunningData) {
        return this.getWfProcess(pid, isRunningData).getUserLastToDoWfAct(doUser);
    }

    /**
     * 
     * @param piId 流程实例ID
     * @param isRunningData 流程数据是否保存在运行表中
     * @return 流程实例
     */
    private WfProcess getWfProcess(String piId, boolean isRunningData) {
        WfProcess process = new WfProcess(piId, isRunningData);

        return process;
    }

    /**
     * 流程跟踪列表
     * @param paramBean 参数对象
     * @return 流程跟踪列表
     */
    public OutBean getWfTracking(ParamBean paramBean) {
        String pid = paramBean.getStr("PI_ID");
        String procRunning = paramBean.getStr("INST_IF_RUNNING");

        WfProcess wfProcess = new WfProcess(pid, procInstIsRunning(procRunning));

        List<Bean> trackingList = wfProcess.wfTracking();
        log.debug("流程跟踪 trackingList size " + trackingList.size());

        OutBean rtnBean = new OutBean();
        rtnBean.set("rtnstr", BeanUtils.toLinkedMap(trackingList, "NI_ID"));

        return rtnBean;
    }

    /**
     * @param procRunning 从页面传来的 WF_INST_ID 字符串类型的 流程是否运行
     * @return 流程是否运行
     */
    private boolean procInstIsRunning(String procRunning) {
        boolean procIsRunning = true;

        if (procRunning.equals(String.valueOf(WfeConstant.WFE_PROC_INST_NOT_RUNNING))) {
            procIsRunning = false;
        }

        return procIsRunning;
    }

    /**
     * 获取指定procCode的流程的最高版本Bean
     * @param procCodeWithoutVersion 流程code,不含版本信息
     * @return Bean 最高版本的流程Bean
     */
    public Bean getLatestProcDef(String procCodeWithoutVersion) {
        Bean queryBean = new Bean();
        queryBean.set(Constant.PARAM_WHERE,
                " AND PROC_CODE LIKE '" + procCodeWithoutVersion + WfeConstant.PROC_VERSION_PREFIX + "%'");
        queryBean.set(Constant.PARAM_ORDER, "PROC_VERSION DESC");
        List<Bean> procDefList = ServDao.finds(ServMgr.SY_WFE_PROC_DEF, queryBean);
        if (procDefList.size() > 0) {
            return procDefList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 将流程所有版本的PROC_IS_LATEST状态置为否 PROC_IS_NOT_LATEST
     * @param procCodeWithoutVersion 流程code,不含版本信息
     */
    private void updateProcDefToUnLatest(String procCodeWithoutVersion) {
        Bean queryBean = new Bean();
        queryBean.set(Constant.PARAM_WHERE,
                " AND PROC_CODE LIKE '" + procCodeWithoutVersion + WfeConstant.PROC_VERSION_PREFIX + "%'");
        ServDao.updates(ServMgr.SY_WFE_PROC_DEF, new Bean()
                        .set("PROC_IS_LATEST", WfeConstant.PROC_IS_NOT_LATEST), queryBean);
    }

    /**
     * 重新设置流程的最新版本
     * @param procCodeWithoutVersion 流程code,不含版本信息
     */
    private void setProcDefLatest(String procCodeWithoutVersion) {
        Bean queryBean = new Bean();
        queryBean.set(Constant.PARAM_WHERE,
                " AND PROC_CODE LIKE '" + procCodeWithoutVersion + WfeConstant.PROC_VERSION_PREFIX + "%'");
        queryBean.set(Constant.PARAM_ORDER, "PROC_VERSION DESC");
        List<Bean> procDefs = ServDao.finds(ServMgr.SY_WFE_PROC_DEF, queryBean);
        if (procDefs.size() > 0) {
            String procCodeTobeLatestVersion = procDefs.get(0).getStr("PROC_CODE");
            ServDao.update(ServMgr.SY_WFE_PROC_DEF,
                    new Bean().setId(procCodeTobeLatestVersion).set("PROC_IS_LATEST", WfeConstant.PROC_IS_LATEST));
        }
    }

    /**
     * 保存流程定义
     * @param paramBean 参数Bean
     * @return Bean
     */
    public OutBean saveWf(ParamBean paramBean) {
        // 将 paramBean 中的值 转成ProcDefBean
        String wfXmlStr = paramBean.getStr("xmlStr");
        // wfXmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + wfXmlStr;
        wfXmlStr = wfXmlStr.replaceAll("gb2312", "UTF-8");

        UserBean userBean = Context.getUserBean();
        String newServId = paramBean.getStr("SERV_ID");
        
        //procCode为流程定义主键
        String procCode = paramBean.getStr("PROC_CODE");
        Bean oldProcBean = WfProcDefDao.getWfProcBeanByProcCode(procCode);
        if (null != oldProcBean) {
            String oldServId = oldProcBean.getStr("SERV_ID");
            clearCache(oldServId);
        }
        clearCache(newServId);

        WFParser myParser = new WFParser(userBean.getCmpyCode(), paramBean);

        myParser.setOldProcCode(procCode);
        // 保存定义文件
        wfXmlStr = wfXmlStr.replaceAll("\r\n", "");
        myParser.setDefContent(wfXmlStr);

        // 判断是更新还是添加
        if (!paramBean.getAddFlag()) {
            // 重新赋值，因为修改时可能修改了EN_NAME字段
            procCode = paramBean.getStr("EN_NAME") + WfeConstant.PROC_CMPY_PREFIX + Context.getUserBean().getCmpyCode()
                    + WfeConstant.PROC_VERSION_PREFIX + paramBean.getStr("PROC_VERSION");
            myParser.setProcCode(procCode);
            myParser.modify();
        } else {
            paramBean.set("PROC_VERSION", 1);
            paramBean.set("PROC_IS_LATEST", WfeConstant.PROC_IS_LATEST);
            procCode = paramBean.getStr("EN_NAME") + WfeConstant.PROC_CMPY_PREFIX + Context.getUserBean().getCmpyCode()
                    + WfeConstant.PROC_VERSION_PREFIX + paramBean.getStr("PROC_VERSION");
            myParser.setProcCode(procCode);
            myParser.save();
        }
        
        //如果是新添加的，而且存在公共按钮的参数，则添加公共按钮
        if (paramBean.getAddFlag() && paramBean.isNotEmpty("PUBLIC_ACTS")) {
        	addPublicActs(paramBean, procCode);
        }

        //刷新流程 服务对应关系的缓存
        WfServCorrespond.removeFromCache(newServId);
        if (null != oldProcBean) {
            WfServCorrespond.removeFromCache(oldProcBean.getStr("SERV_ID"));
        }
        
        OutBean rtnBean = new OutBean();
        rtnBean.setOk(Context.getSyMsg("SY_SAVE_OK"));
        rtnBean.set(Constant.RTN_DATA, new Bean().set("PROC_CODE", procCode));
        return rtnBean;
    }

    /**
     * 
     * @param paramBean 参数Bean
     * @param procCode 流程编码
     */
	private void addPublicActs(ParamBean paramBean, String procCode) {
		List<Bean> pActs = paramBean.getList("PUBLIC_ACTS");
		
		for (Bean pAct: pActs) {
			String newPk = Lang.getUUID();
			pAct.setId(newPk);
			pAct.set("PACT_ID", newPk);
			pAct.set("PROC_CODE", procCode);
			
			ServDao.create(ServMgr.SY_WFE_NODE_PACTS, pAct);
		}
	}

    /**
     * 保存当前的流程定义为最新版本
     * @param paramBean 流程定义信息
     * @return Bean
     */
    public OutBean saveWfAsNewVersion(ParamBean paramBean) {
        if (paramBean.getAddFlag()) {
            return saveWf(paramBean); 
        }
        
        String procCode = paramBean.getStr("PROC_CODE");
        String procCodeWithoutVersion = procCode.substring(0, procCode.lastIndexOf(WfeConstant.PROC_VERSION_PREFIX));
        Bean latestProcDef = getLatestProcDef(procCodeWithoutVersion);
        if (latestProcDef == null) { //没有老的版本
            paramBean.setAddFlag(true);
            return saveWf(paramBean);
        }

        UserBean userBean = Context.getUserBean();
        // 置版本号
        int version = latestProcDef.getInt("PROC_VERSION") + 1;
        paramBean.set("PROC_VERSION", version);
        String newProcCode = procCodeWithoutVersion + WfeConstant.PROC_VERSION_PREFIX + version;
        paramBean.set("PROC_VERSION", version);
        paramBean.set("PROC_CODE", newProcCode);
        paramBean.set("PROC_IS_LATEST", WfeConstant.PROC_IS_LATEST);

        // 先将所有版本置为PROC_IS_LATEST=PROC_IS_NOT_LATEST
        updateProcDefToUnLatest(procCodeWithoutVersion);

        // 将 paramBean 中流程xml编码转换
        String wfXmlStr = paramBean.getStr("xmlStr");
        wfXmlStr = wfXmlStr.replaceAll("gb2312", "UTF-8");

        // 清除流程的业务服务的缓存
        String servId = paramBean.getStr("SERV_ID");
        ParamBean param = new ParamBean(ServMgr.SY_SERV, "clearCache");
        param.setId(servId);
        ServMgr.act(param);

        WFParser myParser = new WFParser(userBean.getCmpyCode(), paramBean);

        // 保存定义文件
        wfXmlStr = wfXmlStr.replaceAll("\r\n", "");
        myParser.setDefContent(wfXmlStr);
        myParser.setProcCode(newProcCode);
        myParser.save();

        OutBean rtnBean = new OutBean();
        rtnBean.setOk(Context.getSyMsg("SY_SAVE_OK"));
        rtnBean.setData(new Bean().set("PROC_CODE", newProcCode)
                .set("PROC_VERSION", version));

        return rtnBean;
    }

    /**
     * 启动流程服务
     * @param paramBean 参数信息，包含要启动的流程定义和数据信息 及起草节点处理人信息   TO_USERS(GroupBean类型),为空时，取当前用户
     * @return outBean 包含流程实例和节点实例
     */
    public OutBean start(ParamBean paramBean) {
        WfParamBean wfParam = (WfParamBean) paramBean;
        GroupBean users = null;
        if (paramBean.contains("TO_USERS")) {
            users = (GroupBean) paramBean.get("TO_USERS");
        }
        
        Bean dataBean = wfParam.getDataBean();
        beforeStart(paramBean, dataBean);
        
        WfAct wfAct = WfProcessFactory.startProcess(wfParam.getDataServId(), dataBean, users);
        WfOutBean outBean = new WfOutBean();
        if (null != wfAct) {
            outBean.setWfProcInst(wfAct.getProcess().getProcInstBean()).setWfActInst(wfAct.getNodeInstBean());
            outBean.setOk();
        }
        
        afterStart(paramBean, outBean);
       
        return outBean;
    }

    /**
     * 启动流程之后的拦截方法，由子类重载
     * @param paramBean 参数信息
     * @param outBean 输出信息
     */
    protected void afterStart(ParamBean paramBean, OutBean outBean) {
        WfParamBean wfParam = (WfParamBean) paramBean;
        
        
        ServDefBean servDef = ServUtils.getServDef(wfParam.getDataServId());
        String servSrcId = servDef.getSrcId();
        
        Bean whereBean = new Bean();
        whereBean.set("SERV_ID", servSrcId);
        whereBean.set("DATA_ID", wfParam.getDataBean().getId());
        
        Bean wfActBean = outBean.getBean("_WF_ACT_INST_");
        Bean setBean = new Bean().set("WF_NI_ID", wfActBean.getId());
        
        ServDao.updates(ServMgr.SY_COMM_FILE, setBean, whereBean);
    }
    
    /**
     * 
     * @param paramBean 前台参数Bean
     * @return 合并后的新的节点实例
     */
    public OutBean stopParallelWf(ParamBean paramBean) {
        String pid = paramBean.getStr("PI_ID");
        WfAct wfAct = null;

        this.checkDoUserIsRight(paramBean);
        
        if (!paramBean.isEmpty("NI_ID")) {
            String nid = paramBean.getStr("NI_ID");
            wfAct = new WfAct(nid, true);
        } else {
            wfAct = this.getUserLastToDoWfAct(paramBean.getDoUserBean(), pid, true);
        }
        
        WfParam wfParam = new WfParam();
        wfParam.setDoneUser(paramBean.getDoUserBean());
        wfParam.setDoneType(WfeConstant.NODE_DONE_TYPE_STOP);
        wfParam.setDoneDesc(WfeConstant.NODE_DONE_TYPE_STOP_DESC);
        
        UserBean currentUser = Context.getUserBean();
        //当前用户和办理用户不一致，则设置成代理
        if (!currentUser.getCode().equals(wfParam.getDoneUser().getCode())) {
            wfParam.setIsAgent(true);
        }
        
        WfAct newWfAct = wfAct.stopParallel(wfParam);

        OutBean rtnBean = new OutBean();
        rtnBean.set("rtnstr", "success" + newWfAct.getId());

        return rtnBean;
    }

    /**
     * 当活动节点大于1， 终止其中一个活动的节点 , 不创建新的节点实例
     * 
     * @param paramBean 从前台传来参数
     * @return 终止流程中节点
     */
    public OutBean stopWfNode(ParamBean paramBean) {
        String nid = paramBean.getStr("NI_ID");

        this.checkDoUserIsRight(paramBean);
        
        WfAct wfAct = new WfAct(nid, true);

        if (!wfAct.isRunning()) {
            throw new TipException("节点已经办结状态，不允许此操作");
        }
        
        WfParam wfParam = new WfParam();
        wfParam.setDoneUser(paramBean.getDoUserBean());
        wfParam.setDoneType(WfeConstant.NODE_DONE_TYPE_STOP);
        wfParam.setDoneDesc(WfeConstant.NODE_DONE_TYPE_STOP_DESC);
        
        UserBean currentUser = Context.getUserBean();
        //当前用户和办理用户不一致，则设置成代理
        if (!currentUser.getCode().equals(wfParam.getDoneUser().getCode())) {
            wfParam.setIsAgent(true);
        }
        
        if (wfAct.getNodeDef().getInt("NODE_IF_AUTOEND") == WfeConstant.NODE_AUTO_END_NO
                && wfAct.getNodeInstBean().getStr("TO_USER_ID")
                        .equals(paramBean.getDoUserBean().getCode())) {
            wfAct.stop(wfParam);
        } else if (wfAct.getProcess().isProcManage()) { // 流程管理员 TODO
            wfAct.stop(wfParam);
        } else {
            throw new TipException("没有权限终止此节点");
        }

        OutBean rtnBean = new OutBean();
        rtnBean.set("rtnstr", "success");

        return rtnBean;
    }
    
    
    /**
     * 送交下一节点 
     * @param paramBean 参数
     * @return 节点实例ID
     */
    public OutBean toNext(ParamBean paramBean) {
        WfProcess process = new WfProcess(paramBean.getStr("PI_ID"), paramBean.getBoolean("INST_IF_RUNNING"));
        WfParam param = new WfParam();
        param.copyFrom(paramBean);
        int typeTo = paramBean.getInt("TO_TYPE");
        param.setTypeTo(typeTo);
        param.setDoneUser(paramBean.getDoUserBean());
        param.setToUser(paramBean.getStr("TO_USERS"));
        param.setToDept(paramBean.getStr("TO_DEPT"));
        param.setToRole(paramBean.getStr("TO_ROLE"));
        if (typeTo != WfParam.TYPE_TO_USER && typeTo != WfParam.TYPE_TO_DEPT_ROLE) {
            throw new TipException("需要设置送交类型");
        } else if (typeTo == WfParam.TYPE_TO_USER) {
            if (paramBean.getStr("TO_USERS").isEmpty()) {
                throw new TipException("需要设置送交用户");
            }
        } else if (typeTo == WfParam.TYPE_TO_DEPT_ROLE) {
            if (paramBean.getStr("TO_DEPT").isEmpty() || paramBean.getStr("TO_ROLE").isEmpty()) {
                throw new TipException("需要设置送交部门、角色");
            }
        }
        param.set("TO_USERS", process.getNextActors(param));
        
        UserBean currentUser = Context.getUserBean();
        //当前用户和办理用户不一致，则设置成代理 , 
        if (!currentUser.getCode().equals(param.getDoneUser().getCode())) {
            param.setIsAgent(true);
        }
        
        //兼职的用户session串了，如果是session串了，则抛出错误，让用户退出所有，重新登录
		checkDoUserIsRight(paramBean);
        
        
        addMsg(paramBean.getStr("PI_ID"), process.getNextActors(param), 
                paramBean.getStr("NODE_CODE"), process.getProcInstTitle());
        
        process.toNext(param);
        
        OutBean out = new OutBean();
        out.setOk();
        if (param.isNotEmpty("_currentWfAct")) { //如果当前节点为不自动结束，则前台不关闭审批单页面。
            WfAct wfAct = (WfAct) param.get("_currentWfAct");
            if (wfAct.getNodeDef().getInt("NODE_IF_AUTOEND") == Constant.NO_INT) {
                out.set("_closeDlg", "false");
            }
        }
        
        return out; 
    }
    
    

    /**
     * @param paramBean 参数Bean
     * @return 送起草人的结果状态
     */
    public OutBean cmSendDrafter(ParamBean paramBean) {
        String pid = paramBean.getStr("PI_ID");
        String procRunning = paramBean.getStr("INST_IF_RUNNING");

        WfProcess wfProcess = new WfProcess(pid, procInstIsRunning(procRunning));

        Bean draftActBean = wfProcess.getFirstWfAct().getNodeInstBean();
        String draftUser = draftActBean.getStr("TO_USER_ID"); // 起草人
        UserBean draftUserBean = UserMgr.getUser(draftUser);

        ParamBean toNextBean = new ParamBean();
        toNextBean.set("PI_ID", pid);
        toNextBean.set("INST_IF_RUNNING", procRunning);
        toNextBean.set("NODE_CODE", draftActBean.getStr("NODE_CODE"));
        toNextBean.set("TO_USERS", draftUser);
        toNextBean.set("TO_TYPE", "3");

        this.toNext(toNextBean);

        OutBean rtnBean = new OutBean();
        rtnBean.set("rtnstr", draftUserBean.getName());

        return rtnBean;
    }


    /**
     * 取消办结
     * @param paramBean 参数信息
     * @return 返回前台参数
     */
    public OutBean undoFinish(ParamBean paramBean) {
        String pid = paramBean.getStr("PI_ID");

        // 取消办结，流程就是不在运行状态
        WfProcess wfProc = this.getWfProcess(pid, false);

        if (wfProc.canUndoFinish()) {
            wfProc.undoFinish();
        } else {
            throw new TipException("没有权限取消办结");
        }

        OutBean rtnBean = new OutBean();
        rtnBean.set("rtnstr", "success");

        return rtnBean;
    }

    /**
     * 
     * 
     * @param paramBean 前台传来
     * @return 返回前台信息
     */
    public OutBean withdraw(ParamBean paramBean) {
        String pid = paramBean.getStr("PI_ID");

        WfAct wfAct = null;

        if (!paramBean.isEmpty("NI_ID")) {
            String nid = paramBean.getStr("NI_ID");
            wfAct = new WfAct(nid, true);
        } else {
            wfAct = this.getUserLastDoneWfAct(paramBean.getDoUserBean(), pid, true);
        }
        paramBean.set("NI_ID", wfAct.getId());  //已经办理过的节点的ID
        this.checkDoUserIsRight(paramBean);
        
        WfParam wfParam = new WfParam();
        wfParam.setDoneType(WfeConstant.NODE_DONE_TYPE_WITHDRAW); // 设置办理类型是收回
        wfParam.setDoneDesc(WfeConstant.NODE_DONE_TYPE_WITHDRAW_DESC);
        wfParam.setTypeTo(WfeConstant.NODE_TO_USER);
        wfParam.setToUser(paramBean.getDoUserBean().getId());
        wfParam.setDoneUser(paramBean.getDoUserBean());
        
        UserBean currentUser = Context.getUserBean();
        //当前用户和办理用户不一致，则设置成代理
        if (!currentUser.getCode().equals(wfParam.getDoneUser().getCode())) {
            wfParam.setIsAgent(true);
        }
        
        log.debug("收回，当前ni_id = " + wfAct.getId());
        
        String strNextWfActIds = paramBean.getStr("nextNiIds");
        String[] wfActIds =  strNextWfActIds.split(",");
        
        WfAct newWfAct = wfAct.withdraw(wfParam, wfActIds);

        // 意见收回
        MindUtils.withDrawMind(wfAct.getId(), newWfAct.getId());

        OutBean rtnBean = new OutBean();
        rtnBean.set("rtnstr", newWfAct.getId());

        return rtnBean;
    }

    /**
     * 删除流程实例服务，需要提供流程实例ID和流程状态参数，不删除流程对应的数据信息
     * TODO 增加验证参数的机制
     * @param param 流程参数
     * @return 执行结果
     */
    public OutBean delete(ParamBean param) {
        WfParamBean wfParam = (WfParamBean) param;
        WfOutBean out = new WfOutBean();
        
        boolean isRunningData = wfParam.getProcSateRunning();
        //是否是回收站数据
        if (wfParam.getInt("S_FLAG") == Constant.NO_INT) {
            isRunningData = false; //运行数据为null
        }
        
        boolean falseDel = true;
        if (wfParam.isNotEmpty("falseDel")) {
            falseDel = wfParam.getBoolean("falseDel");
        }
        
        WfProcess wfProcess = new WfProcess(wfParam.getProcInstCode(), isRunningData);
        if (wfProcess != null) {
            if (falseDel) { //假删除
                wfProcess.delete(param.getDoUserBean());
            } else {
                wfProcess.destory();
            }
        } 
        return out;
    }

    /**
     * 删除流程，同时删除流程对应的数据信息
     * @param paramBean 参数Bean
     * @return 删除状态
     */
    public OutBean deleteDoc(ParamBean paramBean) {
        Bean proInstBean = new Bean();
        if (paramBean.getInt("INST_IF_RUNNING") == 1) { // 运行
            proInstBean = WfProcInstDao.findProcInstById(paramBean.getStr("PI_ID"));
        } else {
            proInstBean = WfProcInstHisDao.findProcInstById(paramBean.getStr("PI_ID"));
        }

        ParamBean servBean = new ParamBean(proInstBean.getStr("SERV_ID"), ServMgr.ACT_DELETE);
        servBean.set(Constant.PARAM_SERV_ID, proInstBean.getStr("SERV_ID"));
        servBean.setId(proInstBean.getStr("DOC_ID"));

        OutBean rtnBean = new OutBean();
        rtnBean.set("rtnstr", "success");

        OutBean actBack = ServMgr.act(servBean);
        if (!actBack.isOk()) {
            rtnBean.set("rtnstr", actBack.getMsg());
        }

        return rtnBean;
    }

    /**
     * @param paramBean 参数Bean
     * @return 返回页面的树结构 串
     */
    public OutBean getNextStepUsersForSelect(ParamBean paramBean) {
        String pid = paramBean.getStr("PI_ID");
        WfAct currWfAct = new WfAct(paramBean.getStr("NI_ID"), true);

        WfProcDef procDef = currWfAct.getProcess().getProcDef();
        String nextNodeCode = paramBean.getStr("NODE_CODE");
        WfNodeDef nextNodeDef = procDef.findNode(nextNodeCode);

        UserBean doUser = paramBean.getDoUserBean();
        WfBinderManager wfBinderManager = new WfBinderManager(nextNodeDef, currWfAct, doUser);
        
        WfLineDef lineBean = procDef.findLineDef(currWfAct.getCode(), nextNodeDef.getStr("NODE_CODE"));
        
        if (lineBean.isEnableOrgDef()) { //如果启动了线组织资源定义，则使用线组织资源定义
            wfBinderManager.initBinderResource(lineBean.getOrgDefBean());
        } else { //使用节点组织资源定义
            wfBinderManager.initBinderResource(nextNodeDef);
        }
        
        WfeBinder wfBinder = wfBinderManager.getWfeBinder();
        
        //优先取按组过滤任务处理人
        if (wfBinder.getGroupBeanList().size() > 0) {
            WfProcess process = new WfProcess(pid, paramBean.getBoolean("INST_IF_RUNNING"));
            WfParam param = new WfParam();
            param.set("NI_ID", paramBean.getStr("NI_ID"));
            param.set("NODE_CODE", paramBean.getStr("NODE_CODE"));
            param.set("TO_USERS", wfBinder.getGroupBeanList());
            param.setDoneUser(paramBean.getDoUserBean());
            process.toNext(param);
            //设置返回值中的送交人
            StringBuffer buffer = new StringBuffer();
            OutBean outBean = new OutBean();
            for (GroupBean groupBean : wfBinder.getGroupBeanList()) {
                buffer.append(groupBean.getUserNames()).append(",");
            }
            if (buffer.length() > 0) {
                buffer.setLength(buffer.length() - 1);
                outBean.set("TO_USERS", buffer);
            }
            
            return outBean.setOk();
            
        } else {
            String rtnTreeStr = wfBinder.getBinders();

            OutBean rtnBean = new OutBean();

            rtnBean.set("treeData", rtnTreeStr); // 树的数据
            rtnBean.set("multiSelect", wfBinder.isMutilSelect()); // 是否多选
            rtnBean.set("binderType", wfBinder.getBinderType()); // 角色还是用户
            rtnBean.set("roleCode", wfBinder.getRoleCode()); // 如果是角色，将角色code 带上
            // 是否需要自动选中用户
            rtnBean.set("autoSelect", wfBinder.isAutoSelect());

            return rtnBean;
        }
    }

    /**
     * 
     * @param paramBean 参数Bean
     * @return 返回前台
     */
    public OutBean getLineCondVars(ParamBean paramBean) {
        OutBean rtnBean = new OutBean();

        VarResource varRes = new VarResource();

        List<Bean> list = new ArrayList<Bean>();
        // 流程变量
        list.addAll(varRes.getParamList());
        // 服务变量
        list.add(varRes.getServParams(paramBean.getStr("SERV_ID")));

        // 添加树的标题
        Bean rootBean = new Bean();
        rootBean.set("ID", "lineConTitle");
        rootBean.set("NAME", "条件流变量");
        rootBean.set("NODETYPE", "DIR");
        rootBean.set("CHILD", list);

        String treeDataStr = JsonUtils.toJson(rootBean);

        String operatorList = varRes.getOperatorList();

        rtnBean.set("treeData", "[" + treeDataStr + "]"); // 树的数据
        rtnBean.set("operatorList", operatorList); // 操作集合

        return rtnBean;
    }


    @Override
    public OutBean byid(ParamBean paramBean) {
        OutBean outBean = super.byid(paramBean);
        
        if (outBean.isEmpty("PROC_XML")) {  //如果是新起的流程，那么默认装载一个流程模板。
            try {
                //默认流程模板的位置为："/sy/wfe/workflow_tmpl.xml"
                StringBuilder filePath = new StringBuilder();
                filePath.append(Context.appStr(APP.SYSPATH));
                filePath.append("sy").append(Constant.PATH_SEPARATOR);
                filePath.append("wfe").append(Constant.PATH_SEPARATOR);
                filePath.append("workflow_tmpl.xml");
                File tmplXml = new File(filePath.toString());
                String xml = FileUtils.readFileToString(tmplXml, "UTF-8");
                outBean.set("PROC_XML", xml);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } else {
            WfProcDef procDefBean = WfProcDefManager.getWorkflowDef(outBean.getStr("PROC_CODE"));
            outBean.set("BIND_TITLE", procDefBean.getProcTitle());
        }
        
        return outBean;
    }
    
    
    /**
     * 清除指定服务的流程定义缓存， 在流程定义有变动时调用
     * @param servId 服务Id
     */
    private void clearCache(String servId) {
        String procKey = "_WF_MAP";
        ServDefBean servDef = ServUtils.getServDef(servId);
        servDef.remove(procKey);
    }
    
    
    /**
     * 
     * @param paramBean 参数Bean
     * @return 流程定义的节点列表
     */
    public OutBean reteieveNodeDefList(ParamBean paramBean) {
        String piId = paramBean.getStr("S_WF_INST");
        
        Bean procInst = WfProcInstDao.findProcInstById(piId);
        
        WfProcDef procDef = WfProcDefManager.getWorkflowDef(procInst.getStr("PROC_CODE"));
        List<Bean> nodeDefList = procDef.getAllNodeDef();
        for (Bean node: nodeDefList) {
            node.set("ID", node.getStr("NODE_CODE"));
            node.set("NAME", node.getStr("NODE_NAME"));
        }
        
        BeanUtils.sort(nodeDefList, "NODE_NAME");
        
        OutBean out = new OutBean();
        out.put(Constant.RTN_DATA, nodeDefList);
        
        return out;
    }
    
    /**
     * 
     * @param pid 流程实例ID
     * @param list 具体送交人
     * @param nodeCode 下个节点ID
     * @param title 标题
     */
    private void addMsg(String pid, List<GroupBean> list, String nodeCode, String title) {
        StringBuilder userIds = new StringBuilder();
        
        for (GroupBean groupBean: list) {
            Set<String> userIdSet = groupBean.getUserIds();
            
            for (String userId : userIdSet) {
                userIds.append(userId).append(",");
            } 
        }
        if (userIds.length() > 0) {
            userIds.setLength(userIds.length() - 1);
        }
        
        Bean msgBean = new Bean();
        msgBean.set("PI_ID", pid);
        msgBean.set("TO_USERS", userIds.toString());
        msgBean.set("NEXT_NODE", nodeCode);
        msgBean.set("TITLE", title);
        
        AttentionMsg attentionMsg = new AttentionMsg(msgBean);
        MsgCenter.getInstance().addMsg(attentionMsg);
    }
    
    /**
     * 
     * @param paramBean 客户端传递的参数
     * @return 如果成功则返回新服务的实例数据，否则返回错误消息。
     */
    public OutBean createRelatedServ(ParamBean paramBean) {
        final String oldServId = paramBean.getStr("oldServId");
        final String oldDataId = paramBean.getStr("oldDataId");
        final String newServId = paramBean.getStr("newServId");
        
        OutBean outBean = null;
        RelatedServCreatorSetting setting = RelatedServCreatorSetting.getSetting(oldServId, newServId);
        if (setting == null) {
            outBean = new OutBean();
            outBean.setError("无效的服务设置");
            return outBean;
        }
        
        RelatedServCreator creator = null;
        if (StringUtils.isNotEmpty(setting.getExtendCls())) {
            // 如果存在扩展类，则使用扩展类处理创建相关文件功能。
            creator = Lang.createObject(RelatedServCreator.class, setting.getExtendCls());
        } else {
            creator = new RelatedServCreator();
        }
        creator.setSetting(setting);
        
        Bean oldBean = ServDao.find(oldServId, oldDataId);
        outBean = creator.create(paramBean, oldBean, newServId);
        
        return outBean;
    }
    
    /**
     * 
     * 当前人是否有权限办理, 没有则抛出异常. 
     * 在这几个地方有调用 ： 送交(toNext)/收回(withdraw)/结束当前工作(stopWfNode)/终止并发(stopParallelWf)
     * @param paramBean 参数Bean
     */
    private void checkDoUserIsRight(ParamBean paramBean) {
    	UserBean currUser = paramBean.getDoUserBean();
    	
    	if (paramBean.isEmpty("NI_ID")) {
    		return; //paramBean中没有NI_ID， 则不判断了 
    	}
    	
    	//通过NI_ID查询node_inst的 to_user_id
    	String niId = paramBean.getStr("NI_ID");
    	
    	Bean nodeInst = WfNodeInstDao.findNodeInstById(niId); //需要处理的都是 正在流经的， 办结的不需要再送交等操作了
    	
    	if (currUser.getCode().equals(nodeInst.getStr("TO_USER_ID"))) { //当前人，就是该节点的办理人
    		return;
    	}
    	
    	//当前人 ， 是该节点办理人的兼职， 则需要转换用户，TODO , 这个暂时提示他重新登录
    	
    	//当前办理人 和 TO_USER_ID不是一个， 则抛错
    	throw new TipException("请重新登录之后再次送交");
    }
}
