package com.rh.oa.zh.seal;

import com.rh.core.base.Bean;
import com.rh.core.base.TipException;
import com.rh.core.comm.FileMgr;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.util.lang.Assert;
import com.rh.oa.gw.GwSealProcess;
import com.rh.oa.gw.util.GwConstant;
import com.rh.oa.gw.util.GwFileHelper;
import com.rh.oa.gw.util.ZW_TYPE;
import com.rh.oa.gw.util.ZwType;

/**
 * 
 * 中华保险项目：吉大正源电子印章与公文模块的接口实现类
 * @author yangjy
 * 
 */
public class GwZhJdSealProcess extends AbstractZhJdSealProcess implements GwSealProcess {

    public OutBean undoSeal(Bean gwBean, Bean fileBean) {
        OutBean rtnBean = new OutBean();

        GwFileHelper helper = new GwFileHelper(gwBean.getId(), gwBean.getStr("TMPL_TYPE_CODE"));
        Bean zwBean = helper.getZhengwen();

        deleteSealFile(helper);

        if (zwBean != null) {
            SealMgr.removeSealFileData(zwBean);
        }

        rtnBean.set("rtnMsg", "succ");
        return rtnBean;
    }

    /**
     * 
     * @param fileHelper 删除盖章文件
     */
    private void deleteSealFile(GwFileHelper fileHelper) {
        Bean redHeadBean = fileHelper.getRedHeadFileBean();
        // 找不到盖章前的文件，则认为文件没有盖章。
        if (redHeadBean == null) {
            return;
        }

        // 删除盖章后的正文文件（脱密文件）
        Bean zw = fileHelper.getZhengwen();
        if (zw != null) {
            FileMgr.deleteFile(zw);
        }

        // 把盖章前的红头文件改成正文
        Bean update = new Bean(redHeadBean.getId());
        changeZwType(ZW_TYPE.ZHENG_WEN, update);
        FileMgr.updateFile(update);
    }

    /**
     * 修改正文类型参数值
     * @param zwType 正文类型对象
     * @param zwBean 需要被修改的Bean
     */
    private static void changeZwType(ZwType zwType, Bean zwBean) {
        zwBean.set("DIS_NAME", zwType.getName());
        zwBean.set("ITEM_CODE", zwType.getCode());
        zwBean.set("FILE_SORT", zwType.getSort());
    }

    public void afterSeal(ParamBean paramBean) {
        // OA 中原始文件ID，盖章前的Word文件
        String wyh = paramBean.getStr("wyh");
        Assert.hasText(wyh, "wyh 参数不能为空。");
        
        // 印章平台的文件ID
        String fileNo = paramBean.getStr("fileNo");
        Assert.hasText(fileNo, "fileNo 参数不能为空。");
        
        String[] fileIds = wyh.split(";");
        if (fileIds.length != 3) {
            throw new RuntimeException("参数格式不正确。");
        }
        
        //盖章前Word文件ID
        String fileId = fileIds[0];
        //盖章文件所属审批单ID
        String dataId = fileIds[1];

        // 原文件数据Bean
        Bean docFileBean = FileMgr.getFile(fileId);

        // 判断文件是否重复盖章，避免程序错误。
        GwFileHelper fileHelper = new GwFileHelper(dataId, GwConstant.OA_GW_TYPE_FW);
        if (fileHelper.getRedHeadFileBean() != null && fileHelper.getZhengwen() != null) {
            throw new TipException("文件已盖章，重新盖章之前请先取消盖章。");
        }
        
        //保存黑头黑长文件
        Bean newZwBean = saveSealedFile(fileNo, docFileBean);
        if (newZwBean != null) { //保存文件成功，则
            // 保存红章文件、灰章文件与原始文件的对应关系到数据库
            saveSealFileData(newZwBean, docFileBean.getId(), fileNo);
        }
    }
    
    /**
     * 保存黑头黑长文件
     * @param fileNo 印章系统红头文件ID
     * @param docFileBean 盖章前红头Word文件
     * @return 黑头黑章文件对象
     */
    private Bean saveSealedFile(String fileNo, Bean docFileBean) {
        Bean newZwBean = new ParamBean();
        newZwBean.set("FILE_NAME", "正文.pdf");
        newZwBean.set("SERV_ID", docFileBean.getStr("SERV_ID"));
        newZwBean.set("DATA_ID", docFileBean.getStr("DATA_ID"));
        newZwBean.set("FILE_CAT", docFileBean.getStr("FILE_CAT"));

        boolean success = GwFileHelper.change2Redhead(docFileBean); // 将盖章前的Word文件改成红头文件
        if (!success) {
            return null;
        }

        GwFileHelper.changeZwType(ZW_TYPE.ZHENG_WEN, newZwBean); // 设置新正文Bean为正文

        // 下载黑头黑章文件到OA系统。
        newZwBean = downloadSealFile(newZwBean, fileNo);
        return newZwBean;
    }

    @Override
    public String getModelName() {
        return "gw";
    }

    @Override
    public void checkSealFileType(Bean fileBean) {
        String fileName = fileBean.getStr("FILE_NAME").toLowerCase();
        if (!fileName.endsWith(".doc")) {
            // 如果文件不是doc文件，则不允许盖章，避免出现对已盖章pdf文件再盖章。
            throw new TipException("该类型文件不允许盖章，或者文件已盖章。原始文件为：" + fileName);
        }
    }

    @Override
    public String getODeptNum(Bean gwBean) {
        return SealMgr.getODeptNum(gwBean);
    }
}
