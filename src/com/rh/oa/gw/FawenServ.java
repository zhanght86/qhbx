package com.rh.oa.gw;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.base.TipException;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.ServUtils;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;
import com.rh.core.util.Lang;
import com.rh.oa.gw.util.GwConstant;
import com.rh.oa.gw.util.GwFileHelper;
import com.rh.oa.gw.util.GwUtils;
import com.rh.oa.zh.seal.SealMgr;

/**
 * 
 * @author yangjy
 * 
 */
public class FawenServ extends GwServ {

    @Override
    protected void beforeByid(ParamBean paramBean) {
        super.beforeByid(paramBean);
        //copyZhengwen(paramBean);
        // 不是新增，而且ID不为null
        if (!paramBean.getAddFlag() && paramBean.getId().length() > 0) {
            // 增加是否存在PDF盖章文件参数
            GwSealProcess gwSeal = GwUtils.createGwSeal();

            if (gwSeal.hasSealed(paramBean)) {
                Bean gwParamBean = getGwParamBean(paramBean);
                gwParamBean.set(GwConstant.EXIST_SEAL_PDF_FILE, "true");
            }

            GwFileHelper gwFile = super.getFileHelper(paramBean);
            Bean gwParamBean = super.getGwParamBean(paramBean);

            // 是否可以盖章
            gwParamBean.set("CAN_SEAL", gwFile.canSeal());
            // 是否能套红头
            gwParamBean.set("CAN_REDHEAD", gwFile.canRedhead());
            // 如果有盖章文件，表示文件已盖章
            gwParamBean.set("CAN_UNDOSEAL", gwFile.hasSealFile());
            // 有正文文件，则不能编辑文稿，正文，
            gwParamBean.set("EXIST_ZHENGWEN", gwFile.hasZhengwen());
            // 文件帮助类
            gwParamBean.set("gwFileHelper", gwFile);

            appendRedheadFileInfo(gwParamBean, gwFile);

            paramBean.set(GwConstant.GW_PARAM, gwParamBean);
        }
    }

    @Override
    protected void afterByid(ParamBean paramBean, OutBean outBean) {
        super.afterByid(paramBean, outBean);
        if (outBean.getBoolean("_ADD_")) { // 新建公文时从公文模版复制正文
            outBean.setId(outBean.getStr("GW_ID"));
            paramBean.setId(outBean.getStr("GW_ID"));
            copyZhengwen(paramBean, outBean);
        }
        if (paramBean.containsKey(GwConstant.GW_PARAM)) {
            Bean gwParamBean = paramBean.getBean(GwConstant.GW_PARAM);
            outBean.copyFrom(gwParamBean);
        }
        getClassFiles(paramBean, outBean);
    }

    /**
     * 将文件分类
     * @param paramBean 参数对象
     * @param outBean 结果集
     */
    private void getClassFiles(ParamBean paramBean, OutBean outBean) {
        //获取文件
        GwFileHelper gwFile = super.getFileHelper(paramBean);
        //获取文件集合
        List<Bean> fileList = gwFile.getFileList();
        //获取发文服务定义
        ServDefBean servObj = ServUtils.getServDef(paramBean.getServId());
        //获取服务定义文件字段列表
        List<Bean> fileSelfItems = servObj.getFileSelfItems();
        //声明字段列表对象
        Bean fileItems = new Bean();
        //分类
        for (Bean fileItem : fileSelfItems) { //循环附件字段
            List<Bean> newFileList = new ArrayList<Bean>();
            for (Bean file : fileList) { //遍历文件列表，取得指定类型的文件列表
                if (fileItem.getStr("ITEM_CODE").equals(file.getStr("FILE_CAT"))) {
                    newFileList.add(file);
                }
            }
            
            List<Bean> finalFileList = null;
            if (fileItem.getStr("ITEM_CODE").equals("ZHENGWEN")) {
                finalFileList = FawenMgr.fileViewRightFilter(outBean.getStr("WF_DISPLAY_MODE"), newFileList);
            } else {
                finalFileList = newFileList;
            }
            
            //找出对应文件列表
            fileItems.set(fileItem.getStr("ITEM_CODE"), finalFileList);
        }
        //添加到输出对象
        outBean.set("_FILE_ITEM_LIST_", fileItems);
    }
       
    /** 
     * 取消印章
     * @param paramBean bean
     * @return bean
     */
    public OutBean undoSeal(ParamBean paramBean) {
        String gwId = paramBean.getStr("PK_CODE");
        Bean gwBean = FawenMgr.getFawenBean(gwId);
        GwFileHelper helper = new GwFileHelper(gwId, "");
        Bean fileBean = helper.getZhengwen();
        GwSealProcess gwSeal = GwUtils.createGwSeal();
        return gwSeal.undoSeal(gwBean, fileBean);
    }

    /**
     * 
     * @param outBean 数据输出Bean
     * @param gwFile 公文文件
     */
    private void appendRedheadFileInfo(Bean outBean, GwFileHelper gwFile) {
        Bean redheadFileInfo = new Bean();

        if (gwFile.canRedhead()) { // 是否能套红头
            if (gwFile.hasWenGao()) { // 有文稿？
                Bean wengao = gwFile.getWengao();
                redheadFileInfo.set("wengaoId", wengao.getId());
                Bean zhengwen = gwFile.getZhengwen();
                // 有红头则覆盖原有的红头文件
                redheadFileInfo.set("zhengwenId", zhengwen.getId());
                // 设置Flag，预览红头只看正文即可
                redheadFileInfo.set("previewRedHead", "zhengwen");
            } else if (gwFile.hasZhengwen()) { // 有正文？
                Bean zhengwen = gwFile.getZhengwen();
                redheadFileInfo.set("wengaoId", zhengwen.getId());
                // 没有红头文件，则创建新文件
                redheadFileInfo.set("zhengwenId", Lang.getUUID() + "."
                        + FilenameUtils.getExtension(zhengwen.getStr(GwConstant.FILE_NAME)));
                // 设置Flag，预览红头只要重套红头
                redheadFileInfo.set("previewRedHead", "doRedHead");
            }
        }
        outBean.set("redheadFileInfo", redheadFileInfo);
    }

    /**
     * 
     * @param paramBean 参数Bean
     * @return 盖章文件及其印章信息
     */
    public OutBean getSealFileInfo(ParamBean paramBean) {
        final String gwId = paramBean.getStr("PK_CODE");
        GwFileHelper fileHelper = new GwFileHelper(gwId, GwConstant.OA_GW_TYPE_FW);
        if (fileHelper.getZhengwen() == null) {
            throw new TipException("正文不存在。");
        }
        Bean gwBean = ServDao.find(GwConstant.OA_GW_TYPE_FW , gwId);
        
        GwSealProcess gwSeal = GwUtils.createGwSeal();
        
        final String gwCode = gwBean.getStr("GW_YEAR_CODE");
        final String oDpet = gwBean.getStr("S_ODEPT");
        
        int printNum = SealMgr.getDefaultPrintNum();
        
        OutBean out = gwSeal.getSealFileInfo(gwBean, fileHelper.getZhengwen(), printNum);
        
        Bean codeBean = GwCodeMgr.findCodeBean(gwCode, oDpet);
        //能找到相关代字，则取得印章编码和印章名称
        if (codeBean != null) {
            out.put("SEAL_CODE", codeBean.getStr("SEAL_CODE"));
            out.put("SEAL_NAME", codeBean.getStr("SEAL_NAME"));
            //如果是联合发文，则把联合发文部门对应的印章放到SEAL_CODE、SEAL_NAME参数中。多个印章之间使用英文的逗号分隔。
            getSealDepts(gwBean, out);
        }
        return out;
    }

    /**
     * 获取联合发文部门印章
     * @param gwBean 当前文
     * @param out 数据
     */
    private void getSealDepts(Bean gwBean, OutBean out) {
        String lhDepts = gwBean.getStr("SEAL_DEPTS"); //获取联合发文部门字段
        if (StringUtils.isNotBlank(lhDepts)) { //如果存在联合发文部门
            StringBuffer sealCodes = new StringBuffer();
            StringBuffer sealNames = new StringBuffer();
            for (String dept : lhDepts.split(Constant.SEPARATOR)) {
                if (StringUtils.isNotBlank(dept)) { //去除空串
                    Bean deptSealBean = ServDao.find(GwConstant.OA_SEAL_DEPTS , new SqlBean().and("DEPT_CODE", dept));
                    if (null != deptSealBean) {
                        String sealCode = deptSealBean.getStr("SEAL_CODE");
                        if (StringUtils.isNotBlank(sealCode)) {
                            sealCodes.append(",").append(sealCode);
                            sealNames.append(",").append(deptSealBean.getStr("SEAL_NAME"));
                        }
                    }
                }
            }
            out.put("SEAL_CODE", out.getStr("SEAL_CODE") + sealCodes.toString());
            out.put("SEAL_NAME", out.getStr("SEAL_NAME") + sealNames.toString()); 
        }
    }
    
    /**
     * 批量打印正文附件
     * @param paramBean 参数
     * @return 附件列表
     */
    public OutBean getAttachInfo(ParamBean paramBean) {
        String dataId = paramBean.getStr("DATA_ID");
        SqlBean sql = new SqlBean();
        sql.and("DATA_ID", dataId);
        sql.orders("FILE_SORT");
        List<Bean> outList = ServDao.finds(ServMgr.SY_COMM_FILE, sql);
        List<Bean> returnList = new ArrayList<Bean>();
        for (Bean b :outList) {
            String itemCode = b.getStr("ITEM_CODE");
            if (StringUtils.isBlank(itemCode) || itemCode.equals("ZHENGWEN")) {
                returnList.add(b);
            }
        }
        return new OutBean().setData(returnList);
    }
    public OutBean getNodeCode(ParamBean paramBean){
        String nodeName = paramBean.getStr("S_WF_NODE");
        OutBean outBean = new OutBean();
        if(nodeName.length()!=0&&nodeName.split(",").length==1){
            Bean instBean = ServDao.find("SY_WFE_PROC_INST", paramBean.getStr("S_WF_INST"));
            List<Bean> nodeBeans = ServDao.finds("SY_WFE_NODE_DEF", new SqlBean().set("PROC_CODE", instBean.getStr("PROC_CODE")).set("NODE_NAME", nodeName));
            for (Bean nodeBean :nodeBeans) {
                outBean.set("NODE_CODE", nodeBean.getStr("NODE_CODE"));
            }
        }
        if(nodeName.length()==0){
            outBean.set("NODE_CODE", 2);
        }
        return outBean;
    }
}
