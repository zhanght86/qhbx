/*
 * Copyright (c) 2011 Ruaho All rights reserved.
 */
package com.rh.oa.gw;

import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.base.Context.APP;
import com.rh.core.comm.FileMgr;
import com.rh.core.comm.FileServ;
import com.rh.core.comm.entity.EntityMgr;
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
import com.rh.core.serv.bean.WfParamBean;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.core.util.Lang;
import com.rh.core.util.RequestUtils;
import com.rh.core.util.Strings;
import com.rh.core.util.file.FileHelper;
import com.rh.core.wfe.WfProcess;
import com.rh.core.wfe.db.WfNodeInstDao;
import com.rh.oa.gw.print.GwPrint;
import com.rh.oa.gw.util.GwConstant;
import com.rh.oa.gw.util.GwFileHelper;
import com.rh.oa.gw.util.GwUtils;
import com.rh.oa.gw.util.ZW_TYPE;

/**
 * 所有公文服务的父类。主要用于处理公共功能，例如：1，起草文件时，复制正文模板；2，检查公文编号是否重复。3，byId过程中加载公共数据。
 * @author yangjinyun
 * 
 */
public class GwServ extends CommonServ {

    /** log */
    private static Log log = LogFactory.getLog(GwServ.class);
    
    /** 字段名：公文机关代字字段  **/
    public static final String GW_YEAR_CODE = "GW_YEAR_CODE";
    /** 字段名：公文年度  **/
    public static final String GW_YEAR = "GW_YEAR";
    /** 字段名：公文编号 **/
    public static final String GW_YEAR_NUMBER = "GW_YEAR_NUMBER";
    
    @Override
    protected void afterByid(ParamBean paramBean, OutBean outBean) {
        super.afterByid(paramBean, outBean);
        String tmplId = "";
        if (outBean.getByidAddFlag()) {
            tmplId = paramBean.getServId();
            outBean.set("TMPL_CODE", tmplId);
        } else {
            tmplId = outBean.getStr("TMPL_CODE");
        }

        // 获取公文模板Bean，并放到outBean中供前端调用
        Bean tmplBean = GwTmplMgr.getTmpl(tmplId, outBean.getStr("S_ODEPT"));
        if (tmplBean != null) {
            outBean.set("tmpl", tmplBean);
        }
        
        // 设置编号字段，避免显示0号文件。
        if (outBean.containsKey(GW_YEAR_NUMBER) && outBean.getInt(GW_YEAR_NUMBER) == 0) {
            outBean.set(GW_YEAR_NUMBER, "");
        }
    }
    /**
     * @author lidongdong
     * 获取机关代字
     */
    protected void afterByidCardTmpl(ParamBean paramBean, Bean out) {
    	out.set("SERV_CARD_TMPL_CONTENT_MB", getCardTmplContentMBQB(paramBean,out));
    }
    public String getCardTmplContentMBQB(ParamBean paramBean, Bean out) {
    	try {
	        if (!Context.isDebugMode() && out.contains("SERV_CARD_TMPL_CONTENT_MB")) {
	            return out.getStr("SERV_CARD_TMPL_CONTENT_MB");
	        }
	        int pos = paramBean.getServId().indexOf("_");
	        String path = Context.appStr(APP.SYSPATH) + paramBean.getServId().substring(0, pos).toLowerCase() + "/tmpl/"
	                + paramBean.getServId()+"_MB" + ".html";
	        return FileHelper.readFile(path);
    	} catch (Exception e) {
    		log.info(e.getMessage());
    		return "";
    	}
    }
	/**
     * @author lidongdong
     * 获取机关代字
     */
    public OutBean getTmpl(ParamBean paramBean){
        OutBean tmplBeans = new OutBean();
        if(paramBean.get("GW_YEAR_CODE")!=""){
           String deptId = Context.getUserBean().getTDeptCode();
           Bean tmplBean = GwTmplMgr.getTmpl(paramBean.getServId(), deptId);
           tmplBeans.set("GW_YEAR_CODES", GwTmplMgr.getYearCodeList(deptId, paramBean.getServId()));
           tmplBeans.set("TMPL_TITLE",tmplBean.getStr("TMPL_TITLE"));
           tmplBeans.set("TMPL_TYPE_NAME",tmplBean.getStr("TMPL_TYPE_NAME"));
        }
        return tmplBeans;
    }
    
    /**
     * 保存之前的业务处理
     * @param paramBean 入参
     */
    protected void beforeSave(ParamBean paramBean) {
        if (paramBean.getAddFlag()) { // 新建公文时从公文模版复制查询策略
            String servId = paramBean.getServId();
//            Bean tmplBean = GwTmplMgr.getTmplByid(servId);
            // 新建公文的时候，通过公文模板，填上查询策略
//            paramBean.set("GW_QUERY_FLAG", tmplBean.getStr("QUERY_FLAG"));
            paramBean.set("TMPL_CODE", servId);
            ServDefBean servDef = ServUtils.getServDef(servId);
            paramBean.set("TMPL_TYPE_CODE", servDef.getSrcId());
        }
        
        checkGwCode(paramBean);
        
        // 得到公文编号，如果公文编号大于0，则获取公文编号信息，将信息组合成格式为“人保财办发（2012）12号”的编号
        saveGwCode(paramBean);
    }


    /**
     * 保存公文编号的值到 GW_CODE 字段，用于显示
     * @param paramBean 参数
     */
    protected void saveGwCode(ParamBean paramBean) {
        boolean saveGwCode = Context.getSyConf(GwConstant.CONF_SAVE_CODE_WHEN_NO_NUM, true);
        // 得到最新的数据，将该数据存数到paramBean中
        Bean fullBean = paramBean.getSaveFullData();
        if (paramBean.getStr("GW_YEAR_CODE").length() > 0) {
            // 将获取的值拼成格式为：机关代字+（年度）+文号+“号”，存储到GW_CODE字段中。
            paramBean.set("GW_CODE", fullBean.getStr("GW_YEAR_CODE") + "〔" + fullBean.getStr("GW_YEAR") + "〕"
                    + fullBean.getStr("GW_YEAR_NUMBER") + "号");
        } else if (!saveGwCode && (paramBean.isNotEmpty("GW_YEAR_CODE") || paramBean.isNotEmpty("GW_YEAR"))) {
            // 发文如果没有最终编号，也保存代字+年度+0号到数据库中 ；默认值为true
            paramBean.set("GW_CODE", fullBean.getStr("GW_YEAR_CODE") + "〔" + fullBean.getStr("GW_YEAR") + "〕0号");
        }
    }
    
    /**
     * 检查公文编码是否重复
     * @param paramBean 参数Bean
     */
    private void checkGwCode(ParamBean paramBean) {
        if (paramBean.getAddFlag()) { // 新增数据
            //如果包含代字的完整数据，则校验
            if (paramBean.isNotEmpty(GW_YEAR_CODE)
                    && paramBean.isNotEmpty(GW_YEAR)
                    && paramBean.isNotEmpty(GW_YEAR_NUMBER) 
                    && paramBean.getInt(GW_YEAR_NUMBER) > 0) {
                checkRepeatedGwCode(paramBean, paramBean);
            }
            return;
        }
        
        //修改数据时，任意数据不为null，则可以启动校验
        if (paramBean.isNotEmpty(GW_YEAR_CODE)
                || paramBean.isNotEmpty(GW_YEAR)
                || (paramBean.isNotEmpty(GW_YEAR_NUMBER)
                && paramBean.getInt(GW_YEAR_NUMBER) > 0)) {

            Bean oldBean = paramBean.getSaveFullData();
            if (oldBean.isNotEmpty(GW_YEAR_CODE)
                    && oldBean.isNotEmpty(GW_YEAR)
                    && oldBean.isNotEmpty(GW_YEAR_NUMBER)) {
                checkRepeatedGwCode(oldBean, paramBean);
            }

        }
    }
    
    /**
     * 
     * @param dataBean 校验文件编号是否重复
     * @param paramBean 参数Bean
     */
    protected void checkRepeatedGwCode(Bean dataBean, ParamBean paramBean) {
        if (dataBean.getInt(GW_YEAR_NUMBER) == 0) {
            return;
        } else if (dataBean.isEmpty(GW_YEAR_CODE)) {
            return;
        } else if (dataBean.isEmpty(GW_YEAR)) {
            return;
        }
        
        String codes = GwCodeMgr.getCodeNamesInGroup(dataBean.getStr(GW_YEAR_CODE)
                , dataBean.getStr("S_ODEPT"));
        
        String servId = dataBean.getStr("TMPL_CODE");
        SqlBean sql = new SqlBean();
        sql.and("TMPL_TYPE_CODE", dataBean.getStr("TMPL_TYPE_CODE"));
        sql.andIn("GW_YEAR_CODE", codes.split(","));
        sql.and("GW_YEAR", dataBean.getInt(GW_YEAR));
        sql.and("GW_YEAR_NUMBER", dataBean.getInt(GW_YEAR_NUMBER));
        sql.and("S_ODEPT", Context.getUserBean().getODeptCode());
        sql.and("S_FLAG", Constant.YES_INT);

        sql.selects(" GW_ID, GW_TITLE ");
        
        List<Bean> gwList = ServDao.finds(servId, sql);
        
        for (Bean bean : gwList) {
            if (!dataBean.getId().equals(bean.getId())) {
                throw new TipException("机关代字重复！重复的数据标题为：" + bean.getStr("GW_TITLE"));
            }
        }
    }

    /**
     * 保存之后的业务处理
     * @param paramBean 入参
     * @param outBean 保存后的数据信息
     */
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        /*if (paramBean.getAddFlag()) { // 新建公文时从公文模版复制正文
            copyZhengwen(paramBean, outBean);
        }*/
        // 公文，起草节点修改某种数据类型后，删除原有流程，启动新流程
        // 比如：发文，修改发文类型后需重启流程；签报，修改签报类型后需重启流程
        if (!paramBean.getAddFlag()) {
            Bean data = paramBean.getSaveFullData();
            Bean oldData = paramBean.getSaveOldData();
            if (data != null
                    && !data.isEmpty()
                    && ("," + Context.getSyConf("BN_WF_RESET_TMPL_CODES", "OA_GW_TMPL_QB_GS,OA_GW_TMPL_FW_GS") + ",")
                            .indexOf("," + data.getStr("TMPL_CODE") + ",") >= 0) {
                // 获取数据项字段
                String columnCode = Context.getSyConf(data.getStr("TMPL_CODE") + "_COLUMN", "");
                // 判断类型是否已修改，如已修改且为起草节点，则删除原有流程，启动新流程
                if (columnCode.length() > 0 && !oldData.getStr(columnCode).equals(data.getStr(columnCode))) {
                    // 获取流程实例ID
                    String wfActId = data.getStr("S_WF_INST");
                    if (wfActId.length() > 0) {
                        //获取节点信息
                        List<Bean> nodeInstBeanList = WfNodeInstDao.findNodeInstListByWhere(" and PI_ID='" + wfActId + "'");
                        if (nodeInstBeanList != null && nodeInstBeanList.size() == 1) {
                            Bean nodeInstBean = nodeInstBeanList.get(0);
                            //起草节点且为流程的第一个节点实例，则删除原有流程，启动新流程
                            if (nodeInstBean.getStr("PRE_NI_ID") == null || "".equals(nodeInstBean.getStr("PRE_NI_ID"))) {
                                //删除原有流程信息
                                WfProcess wfProcess = new WfProcess(nodeInstBean.getStr("PI_ID"), true);
                                if (wfProcess != null) {
                                    try {
                                        wfProcess.destory();
                                        //启动新流程
                                        WfParamBean param = new WfParamBean(WfParamBean.ACT_START);
                                        param.setDataServId(paramBean.getServId()).setDataBean(data);
                                        if (ServMgr.act(param).isOk()){
                                            
                                        } else {
                                            throw new TipException("启动新流程失败");
                                        }
                                    } catch (Exception e) {
                                        throw new TipException(e.getMessage());
                                    }
                                }
                            } else {
                                throw new TipException("非起草节点，不允许重新启动流程") ;
                            }
                        } else {
                            throw new TipException("非起草节点，不允许重新启动流程");
                        }
                    }
                }
            }
        }
    }


    /**
     * 保存正文
     * @param paramBean 参数Bean
     * @param outBean 数据输出Bean
     */
    public void copyZhengwen(ParamBean paramBean, OutBean outBean) {
        String servId = paramBean.getServId();
        Bean tmplBean = GwTmplMgr.getTmplByid(servId);
        if (tmplBean.isNotEmpty("TMPL_ZHENGWEN")) { // 存在正文则复制正文文件
            String fileId = Strings.getFirstBySep(tmplBean.getStr("TMPL_ZHENGWEN"), ",");
            
            Bean fileBean = FileMgr.getFile(fileId);
            if (fileBean == null) {
                throw new RuntimeException("模板文件记录不存在！");
            }

            Bean serv = ServUtils.getServDef(servId);
            UserBean user = null;
            String userCode = outBean.getStr("S_USER");
            if (!StringUtils.isBlank(userCode)) {
                //防止有人在创建服务定义时，将S_USER字段默认值为@USER_NAME@
                //^[\u4e00-\u9fa5]+$ 判断中文, [\u4e00-\u9fa5]包含中文
                Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
                Matcher m = p.matcher(userCode);
                if (!m.find()) {
                    user = UserMgr.getUser(userCode);
                } 
            }
            Bean bean = new Bean();
            bean.set("SERV_ID", serv.getStr("SERV_SRC_ID"))
                .set("FILE_CAT", "ZHENGWEN").set("DATA_ID", outBean.getId())
                .set("DIS_NAME", ZW_TYPE.ZHENG_WEN.getName()).set("ITEM_CODE", ZW_TYPE.ZHENG_WEN.getCode());
            if (null != user) {
                bean.set("S_USER", user.getCode()).set("S_UNAME", user.getName()).set("S_DEPT", user.getDeptCode())
                    .set("S_DNAME", user.getDeptName());
            }
            FileMgr.copyFile(fileBean, bean);

        }
    }
    

    /**
     * 
     * @param paramBean 参数Bean
     * @return 打印替换后的html页面
     */
    public OutBean cmPrint(ParamBean paramBean) {
        String servId = paramBean.getStr("TMP_CODE");
        String gwId = paramBean.getStr("PK_CODE");
        GwPrint gwPrint = new GwPrint(servId, gwId);

        String printBody = gwPrint.createHtml();

        String htmlStr = gwPrint.createPreviewHtml(printBody);

        OutBean rtnBean = new OutBean();
        // rtnBean.set("htmlStr", htmlStr);

        rtnBean.setToHtml(htmlStr);

        return rtnBean;
    }
    
    /**
     * 根据机关代字、年度取得最大公文编号
     * @param paramBean 参数信息
     * @return 公文编号值
     */
    public OutBean getMaxCode(ParamBean paramBean) {
        if (paramBean.isEmpty("GW_YEAR_CODE")) {
            OutBean out = new OutBean();
            out.set("GW_YEAR_NUMBER", "");
            /*out.setError("代字为空，不能编号!");*/
            return out;
        }
        
        String servId = paramBean.getServId();
        
        UserBean userBean = Context.getUserBean();
        //取得同组的所有代字
        String codes = GwCodeMgr.getCodeNamesInGroup(paramBean.getStr("GW_YEAR_CODE"), userBean.getODeptCode());
        if (codes.length() == 0) {
            codes = paramBean.getStr("GW_YEAR_CODE");
        }
        
        SqlBean sql = new SqlBean();
        sql.selects("max(GW_YEAR_NUMBER) GW_YEAR_NUMBER");
        
        sql.andIn("GW_YEAR_CODE", codes.split(","));
        sql.and("GW_YEAR", paramBean.getStr("GW_YEAR"));
        sql.and("TMPL_TYPE_CODE", paramBean.getStr("TMPL_TYPE_CODE"));
        sql.and("GW_YEAR", paramBean.getStr("GW_YEAR"));
        sql.andNot("GW_ID", paramBean.getId());
        sql.and("S_FLAG", Constant.YES_INT);
        
        OutBean codeBean = new OutBean(ServDao.find(servId, sql));
        if (!codeBean.isEmpty("GW_YEAR_NUMBER")) {
            codeBean.set("GW_YEAR_NUMBER", codeBean.getInt("GW_YEAR_NUMBER") + 1);
        } else {
            codeBean.set("GW_YEAR_NUMBER", 1);
        }
        // 生成最大公文编号的同时保存代字、年度和编号
        //如果是收文的添加模式，则不保存
        if(paramBean.getStr("").equals("")&&paramBean.getStr("").endsWith("")){
            
        }else{
            ParamBean param = new ParamBean(servId, ServMgr.ACT_SAVE);
            param.setId(paramBean.getId()).set("GW_YEAR_CODE", paramBean.getStr("GW_YEAR_CODE"))
                .set("GW_YEAR", paramBean.getStr("GW_YEAR")).set("GW_YEAR_NUMBER", codeBean.getStr("GW_YEAR_NUMBER"));
            ServMgr.act(param);
        }

        codeBean.setOk();
        return codeBean;
    }

    /**
     * 获取公文对应的正文的文件ID
     * @param paramBean 参数Bean
     * @return 获取公文对应的正文的文件ID
     */
    public OutBean getGwZhengWenId(ParamBean paramBean) {
        String gwId = paramBean.getStr("PK_CODE");

        Bean queryBean = new Bean();
        queryBean.set("DATA_ID", gwId);

        queryBean.set("FILE_CAT", ZW_TYPE.ZHENG_WEN.getCode()); // DIS_NAME
        queryBean.set("ITEM_CODE", ZW_TYPE.ZHENG_WEN.getCode());

        Bean fileBean = ServDao.find(ServMgr.SY_COMM_FILE, queryBean);

        OutBean rtnBean = new OutBean();
        if (null == fileBean) {
            rtnBean.set("zhengWenId", Lang.getUUID());
            return rtnBean;
        }

        rtnBean.set("zhengWenId", fileBean.getId());

        return rtnBean;
    }

    /**
     * 获取公文对应的正文 文稿 的文件ID
     * @param paramBean 参数Bean
     * @return 获取公文对应的正文 文稿 的文件ID
     */
    public OutBean getGwWenGaoId(ParamBean paramBean) {
        String gwId = paramBean.getStr("PK_CODE");

        Bean queryBean = new Bean();
        queryBean.set("DATA_ID", gwId);
        queryBean.set("FILE_CAT", ZW_TYPE.ZHENG_WEN.getCode());
        queryBean.set("ITEM_CODE", ZW_TYPE.WEN_GAO.getCode());

        Bean fileBean = ServDao.find(ServMgr.SY_COMM_FILE, queryBean);

        if (null == fileBean) {
            throw new TipException("文稿不存在");
        }

        OutBean rtnBean = new OutBean();
        rtnBean.set("wenGaoId", fileBean.getId());

        return rtnBean;
    }
    
    /**
     * 将审批单中的文件同步到文库某栏目中 1、复制主文件 2、新添主文档 3、复制其余辅文件并关联主文档主键
     * @param paramBean 参数bean
     * @return outBean 结果bean
     */
    public OutBean fileToWenku(ParamBean paramBean) {
        if (paramBean.isEmpty("_DOCUMENT_MAIN_ID")) {
            throw new TipException("未找到正文");
        }

        // 查询公文对应的所有文件
        FileServ fs = new FileServ();
        ParamBean param = new ParamBean();
        Bean gwServDef = ServUtils.getServDef(paramBean.getServId());
        param.set("SERV_ID", gwServDef.getStr("SERV_SRC_ID"));
        param.set("DATA_ID", paramBean.getId());
        List<Bean> fileList = fs.finds(param).getList(Constant.RTN_DATA);
        String gwFiles = "";
        for (Bean file : fileList) {
            if (gwFiles.equals("")) {
                gwFiles = file.getStr("FILE_ID");
            } else {
                gwFiles = gwFiles + Constant.SEPARATOR + file.getStr("FILE_ID");
            }
        }

        // 检查前台提交的主文件是否在后台存在
        if (gwFiles.equals("") || gwFiles.indexOf(paramBean.getStr("_DOCUMENT_MAIN_ID")) < 0) {
            throw new TipException("未找到文件");
        }

        // 复制主文件
        ParamBean fileBean = new ParamBean();
        Bean fileCopyBean = new Bean();
        fileBean.set(Constant.KEY_ID, paramBean.getStr("_DOCUMENT_MAIN_ID"));
        OutBean fileOutBean = fs.byid(fileBean);
        fileCopyBean.set("SERV_ID", "SY_COMM_WENKU_DOCUMENT");
        fileCopyBean.set("DATA_ID", "");
        fileCopyBean = FileMgr.copyFile(fileOutBean, fileCopyBean);

        // 更新公文记录的栏目字段DOCUMENT_CHNL
        param = new ParamBean(paramBean.getServId());
        param.setId(paramBean.getId());
        param.set("DOCUMENT_CHNL", paramBean.getStr("DOCUMENT_CHNL"));
        this.save(param);

        // 新添主文档
        param = new ParamBean(ServMgr.SY_COMM_WENKU_DOCUMENT, ServMgr.ACT_SAVE);
        param.setId("");
        param.set("DOCUMENT_FILE", fileCopyBean.getId());
        param.set("DOCUMENT_CHNL", paramBean.getStr("DOCUMENT_CHNL"));
        OutBean resultBean = ServMgr.act(param);

        String[] auxFiles = paramBean.getStr("_DOCUMENT_AUX_ID").split(Constant.SEPARATOR);
        // 复制其余辅文件并关联主文档主键
        for (int i = 0; i < auxFiles.length; i++) {
            // 检查前台提交的辅文件是否在后台存在
            if (auxFiles[i].equals("") || gwFiles.indexOf(auxFiles[i]) < 0) {
                continue;
            }
            fileCopyBean = new Bean();
            fileBean.set(Constant.KEY_ID, auxFiles[i]);
            fileBean.set(Constant.PARAM_SERV_ID, "SY_COMM_FILE");
            fileOutBean = fs.byid(fileBean);
            fileCopyBean.set("SERV_ID", "SY_COMM_WENKU_DOCUMENT");
            fileCopyBean.set("DATA_ID", resultBean.getId());
            fileCopyBean = FileMgr.copyFile(fileOutBean, fileCopyBean);
        }
        OutBean outBean = new OutBean();
        outBean.setOk(Context.getSyMsg("成功将文件同步到文库相应栏目"));
        return outBean;
    }

    /**
     * 相关文件关联附件和转发原文
     * @param paramBean 参数
     * @return outBean
     */
    public OutBean getRelate(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        // 获取当前审批单的服务ID和DATA_ID和关联类型
        String servId = paramBean.getServId();
        ServDefBean fwServ = ServUtils.getServDef(servId);

        // 获取用户要关联的数据个数
        int datasLen = paramBean.getInt("datasLen");
        for (int i = 0; i < datasLen; i++) {
            // 获取关联的服务ID
            String sId = paramBean.getStr(i + "[sId]");
            ServDefBean servDef = ServUtils.getServDef(sId);
            String dId = paramBean.getStr(i + "[dataId]");
            // 查找当前的相关文件
            List<Bean> filesBean = FileMgr.getFileListBean(servDef.getStr("SERV_SRC_ID"), dId);
            copyFileList(fwServ, paramBean, filesBean, dId);
            outBean.setOk();
        }
        return outBean;
    }
    
    /**
     * 
     * @param targetServ 被关联数据对应服务定义
     * @param paramBean 参数bean
     * @param filesBean 关联数据的文件列表。
     * @param dId 关联数据ID
     */
    private void copyFileList(ServDefBean targetServ, ParamBean paramBean, List<Bean> filesBean, String dId) {
        // 获取当前相关文件的个数
        int filesSize = filesBean.size();
        // 如果相关文件个数不为空则开始复制附件或者转发原文
        for (int k = 0; k < filesSize; k++) {
            Bean oldBean = filesBean.get(k);
            if (oldBean.getStr("FILE_CAT").equals(ZW_TYPE.ZHENG_WEN.getCode())) {
                //只有标明正文文件（发文）和未指明正文文件（收文）才能复制
                if (oldBean.getStr("ITEM_CODE").equals(ZW_TYPE.ZHENG_WEN.getCode())
                        || oldBean.isEmpty("ITEM_CODE")) {
                    Bean entityBean = EntityMgr.getEntity(dId);
                    if (entityBean != null && entityBean.isNotEmpty("TITLE")) {
                        oldBean.set("DIS_NAME", entityBean.getStr("TITLE"));
                    }
                    copyFile(targetServ, paramBean, oldBean);
                }
            } else {
                copyFile(targetServ, paramBean, oldBean);
            }
        }
    }

    /**
     * 
     * @param fwServ 被关联数据对应服务定义
     * @param paramBean 参数bean
     * @param oldBean 关联数据的文件对象。
     */
    private void copyFile(ServDefBean fwServ, ParamBean paramBean, Bean oldBean) {
        String dataId = paramBean.getStr("DATA_ID");
        String relateType = paramBean.getStr("relateType");   
        try {
            // 复制文件并返回文件ID
            FileMgr.copyFile(oldBean,
                    new Bean().set("SERV_ID", fwServ.getStr("SERV_SRC_ID"))
                            .set("FILE_CAT", relateType).set("DATA_ID", dataId));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    
    /**
     * 
     * @param paramBean 参数Bean
     * @return 转换类型是否成功
     */
    public OutBean conversion(ParamBean paramBean) {
        String oldServId = paramBean.getStr("SERV_ID");
        String dataId = paramBean.getStr("DATA_ID");
        String newServId = paramBean.getStr("TO_SERV_ID");
        
        
        GwConversion gwConvert = new GwConversion(oldServId, dataId, newServId);
        String rtnMsg = gwConvert.doConvert(paramBean.getDoUserBean());
        

        OutBean outBean = new OutBean();
        outBean.set(Constant.RTN_MSG, rtnMsg);
        return outBean;
    }
    
    /**
     * 
     * @param paramBean 参数Bean
     * @return 打印红头红章正文的信息
     */
    public OutBean getPrintFileInfo(ParamBean paramBean) {
        final String gwId = paramBean.getStr("PK_CODE");
        String servId = paramBean.getStr("TMP_CODE");
        OutBean out = new OutBean();

        GwFileHelper fileHelper = new GwFileHelper(gwId, servId);
        Bean zwBean = fileHelper.getZhengwen();
        if (zwBean == null) {
            out.setError("找不到正文。");
            return out;
        }

        GwSealProcess gwSeal = GwUtils.createGwSeal();
        // 取得文档所属机构编号
        Bean gwBean = ServDao.find(GwConstant.OA_GW_GONGWEN, gwId);
        out = gwSeal.getPrintFileInfo(gwBean, zwBean);
        out.setOk();

        return out;
    }
    
    /**
     * 用于在beforeByid方法中取得GwFileHelper
     * @param paramBean 参数Bean
     * @return 公文文件对象
     */
    public static GwFileHelper getFileHelper(ParamBean paramBean) {
        if (paramBean.isNotEmpty("_GW_FILE")) {
            return (GwFileHelper) paramBean.get("_GW_FILE");
        }
        String servId = paramBean.getServId();
        String gwId = paramBean.getId();

        GwFileHelper gwFile = new GwFileHelper(gwId, servId);
        paramBean.set("_GW_FILE", gwFile);
        return gwFile;
    }
    
    /**
     * 用于在beforeByid方法中取得GwParamBean
     * @param paramBean 参数Bean
     * @return 公文控制参数Bean
     */
    protected static Bean getGwParamBean(ParamBean paramBean) {
        if (paramBean.isNotEmpty(GwConstant.GW_PARAM)) {
            return paramBean.getBean(GwConstant.GW_PARAM);
        }

        Bean gwParamBean = new Bean();
        paramBean.set(GwConstant.GW_PARAM, gwParamBean);

        return gwParamBean;
    }
    /**
     * 根据选择的新闻栏目，生成一条新闻，将公文红头文件发布至这条新闻中，作为附件
     * @param paramBean 前端传递的栏目、公文ID等信息
     * @return OutBean 返回结果bean
     */
    public OutBean publishToPortal(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        //服务ID
        String servId = paramBean.getStr("SERV_ID");
        //公文ID
        String gwId = paramBean.getStr("DATA_ID");
        if (gwId.length() <= 0) {
            throw new TipException("未传递公文ID");
        }
        //栏目ID
        String chnlId = paramBean.getStr("CHNL_ID");
        if (chnlId.length() <= 0) {
            throw new TipException("请选择栏目");
        }
        //获取红头文件
        List<Bean> fileList = ServDao.finds("SY_COMM_FILE", " and DATA_ID='" + gwId + "' and FILE_CAT='ZHENGWEN' and ITEM_CODE='ZWPDF'");
        if (fileList == null || fileList.size() <= 0) {
            throw new TipException("未找到正文");
        }
        if (fileList.size() >= 2) {
            throw new TipException("多个正文，请检查公文相关信息是否准确");
        }
        //当前用户信息
        UserBean userBean = Context.getUserBean();
        //获取公文信息
        Bean gwBean = ServDao.find(servId, gwId);
        if (gwBean == null || gwBean.isEmpty()) {
            throw new TipException("未找到公文信息");
        }
        //新闻主键ID
        String newId = Lang.getUUID();
        //重置附件相关数据
        Bean newFile = fileList.get(0);
        //新闻附件主键
        newFile.set("FILE_ID", Lang.getUUID());
        //根据原文件名获取文件类型，包括.号
        String fileType = "";
        if (newFile.getStr("FILE_NAME").lastIndexOf(".") > 0) {
            fileType = newFile.getStr("FILE_NAME").substring(newFile.getStr("FILE_NAME").lastIndexOf("."));
        }
        //文件名
        newFile.set("FILE_NAME", gwBean.getStr("GW_TITLE") + fileType);
        //文件显示名
        newFile.set("DIS_NAME", gwBean.getStr("GW_TITLE"));
        //附件所属服务ID
        newFile.set("SERV_ID", "SY_COMM_INFOS_BASE");
        //附件所属数据ID
        newFile.set("DATA_ID", newId);
        //附件类别
        newFile.set("FILE_CAT", "FUJIAN");
        //清空ITEM_CODE
        newFile.set("ITEM_CODE", "");
        //清空流转信息ID
        newFile.set("WF_NI_ID", "");
        //上传人
        newFile.set("S_USER", userBean.getCode());
        //上传人姓名
        newFile.set("S_UNAME", userBean.getName());
        //上传人所属处室
        newFile.set("S_DEPT", userBean.getDeptCode());
        newFile.set("S_DNAME", userBean.getDeptName());
        //上传时间
        newFile.set("S_MTIME", DateUtils.getDatetime());
        //保存附件信息
        ServDao.create(ServMgr.SY_COMM_FILE, newFile);
        
        
        //发布新闻
        ParamBean newBean = new ParamBean("SY_COMM_INFOS_PUSLISH", ServMgr.ACT_SAVE);
        newBean.set("NEWS_ID", newId);
        //新闻标题即为公文标题
        newBean.set("NEWS_SUBJECT", gwBean.getStr("GW_TITLE"));
        //新闻简介即为公文标题
        newBean.set("NEWS_SHORT_TITLE", gwBean.getStr("GW_TITLE"));
        //新闻发布时间
        newBean.set("NEWS_TIME", DateUtils.getDatetime());
        //新闻内容类型，普通新闻
        newBean.set("NEWS_TYPE", 1);
        //所属新闻栏目
        newBean.set("CHNL_ID", chnlId);
        //公共标识，非公共
        newBean.set("S_PUBLIC", 2);
        //启用标识
        newBean.set("S_FLAG", 1);
        //发布人
        newBean.set("S_USER", userBean.getCode());
        //发布人所属部门
        newBean.set("S_TDEPT", userBean.getTDeptCode());
        //数据更新时间
        newBean.set("S_MTIME", DateUtils.getDatetime());
        //新闻审核状态，已发布
        newBean.set("NEWS_CHECKED", 6);
        //站点
        newBean.set("SITE_ID", "SY_COMM_SITE_INFOS");
        //是否有图片，无图片
        newBean.set("HAS_IMAGE", 2);
        //是否有附件，有附件
        newBean.set("HAS_ATTACH", 1);
        //数据添加时间
        newBean.set("S_ATIME", DateUtils.getDatetime());
        //发布人
        newBean.set("NEWS_USER", userBean.getCode());
        //所属机构
        newBean.set("S_ODEPT", userBean.getODeptCode());
        //公开范围
        newBean.set("NEWS_SCOPE", 2);
        //审核级别，2不审核
        newBean.set("NEWS_CHECK", 2);
        //保存新闻
        //ServDao.create("SY_COMM_INFOS_PUSLISH", newBean);
        newBean.setAddFlag(true);
        ServMgr.act(newBean);
        outBean.setOk("公文发布成功");
        return outBean;
    }
    /**
     * 批量下载公文中指定某种类型的文件，如：附件、正文等
     * @param paramBean
     * @return OutBean
     */
    public OutBean batchDownloadFile(ParamBean paramBean) {
        //服务ID
        String servId = paramBean.getStr("SERV_ID");
        if (servId.length() <= 0) {
            throw new TipException("缺少服务ID参数");
        }
        //数据ID
        String dataId = paramBean.getStr("DATA_ID");
        if (dataId.length() <= 0) {
            throw new TipException("缺少数据ID参数");
        }
        //文件字段编码串
        String fileCodes = paramBean.getStr("FILE_CODES");
        if (fileCodes.length() <= 0) {
            throw new TipException("缺少文件编码参数");
        }
        //获取文件列表
        fileCodes = "'" + fileCodes.replaceAll(",", "','") + "'";
        List<Bean> fileList = ServDao.finds(ServMgr.SY_COMM_FILE, " and DATA_ID='" + dataId + "'" + 
                " and FILE_CAT in(" + fileCodes + ")");
        if (fileList != null && fileList.size() > 0) {
            HttpServletRequest request = Context.getRequest();
            HttpServletResponse response = Context.getResponse();
            response.setContentType("application/x-download; charset=UTF-8");
            RequestUtils.setDownFileName(request, response, servId + ".zip");  //指定导出格式及名字
            
            ZipArchiveOutputStream zipOut = null; //输出流
            try {
                zipOut = new ZipArchiveOutputStream(response.getOutputStream());
                for (Bean file : fileList) {
                    InputStream is = FileMgr.openInputStream(file);//输入流
                    zipOut.putArchiveEntry(new ZipArchiveEntry(file.getStr("FILE_NAME")));  //指定输出文件名称
                    IOUtils.copyLarge(is, zipOut);
                    zipOut.closeArchiveEntry(); //关闭输出流
                    if (is != null) {
                        IOUtils.closeQuietly(is);  //关闭输入流
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            } finally {
                if (zipOut != null) {
                    IOUtils.closeQuietly(zipOut);  //关闭输入流
                }
                try {
                    response.flushBuffer();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        } else {
            throw new TipException("未找到文件");
        }
        return new OutBean();
    }
    /**
     * 判断当前公文正文是否已转过pdf
     * @param paramBean 参数对象
     * @return OutBean 返回结果
     */
    public OutBean hasPDF(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        //数据ID
        String dataId = paramBean.getStr("DATA_ID");
        if (dataId.length() <= 0) {
            throw new TipException("缺少文件所属数据ID");
        }
        //文件编码
        String fileCat = paramBean.getStr("FILE_CAT");
        if (fileCat.length() <= 0) {
            throw new TipException("缺少文件字段编码");
        }
        //获取正文转的pdf文件
        List<Bean> pdfFileList = ServDao.finds("SY_COMM_FILE",  "and ITEM_CODE='ZWPDF' and FILE_CAT='" + fileCat + "' and DATA_ID='" + dataId
                        + "'");
        if(pdfFileList!=null){
            outBean.set("HAS_PDF",true);
        }else{
            outBean.set("HAS_PDF",false);
        }
        return outBean;
    }
    /**
     * 判断当前公文正文是否已转过pdf，若无，则将正文信息回传前端
     * @param paramBean 参数对象
     * @return OutBean 返回结果
     */
    public OutBean getGwZwFileInfo(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        //数据ID
        String dataId = paramBean.getStr("DATA_ID");
        if (dataId.length() <= 0) {
            throw new TipException("缺少文件所属数据ID");
        }
        //文件编码
        String fileCat = paramBean.getStr("FILE_CAT");
        if (fileCat.length() <= 0) {
            throw new TipException("缺少文件字段编码");
        }
        //获取正文转的pdf文件
        List<Bean> pdfFileList = ServDao.finds("SY_COMM_FILE",  "and ITEM_CODE='ZWPDF' and FILE_CAT='" + fileCat + "' and DATA_ID='" + dataId
                        + "'");
        //判断是否已转过pdf，已转，则先删除原有pdf文件
        if (pdfFileList != null) {
            for (Bean pdf : pdfFileList) {
                FileMgr.deleteFile(pdf);
            }
        }
        // 获取正文红头、文稿信息
        List<Bean> fileList = ServDao.finds("SY_COMM_FILE", " and FILE_CAT='" + fileCat
                + "' and DATA_ID='" + dataId + "' and ITEM_CODE='ZHENGWEN' order by FILE_SORT asc,S_MTIME asc ");
        //取第一个正文文件
        if (fileList != null && fileList.size() > 0) {
            outBean.set("HAS_ZW", "true");
            Bean fileBean = fileList.get(0);
            outBean.set("ZW_FILE_ID", fileBean.getStr("FILE_ID"));
            outBean.set("ZW_FILE_NAME", fileBean.getStr("FILE_NAME"));
            outBean.set("ZW_FILE_DIS_NAME", fileBean.getStr("DIS_NAME"));
            outBean.set("ZW_SERV_ID", fileBean.getStr("SERV_ID"));
        } else {
            outBean.set("HAS_ZW", "false");
        }
        return outBean;
    }
}
