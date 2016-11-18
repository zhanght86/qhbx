package com.rh.oa.gw.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.comm.FileMgr;

/**
 * 获取公文的所有附件，及其相关类型公文文件类型
 * @author yangjy
 * 
 */
public class GwFileHelper {

    
    private List<Bean> fileList = null;

    /**
     * 
     * @param gwId 公文ID
     * @param servId 服务ID
     */
    public GwFileHelper(String gwId, String servId) {
        if (StringUtils.isEmpty(gwId)) {
            fileList = new ArrayList<Bean>();
        } else {
            fileList = FileMgr.getFileListBean(servId, gwId);
        }
    }
    
    /**
     * 
     * @return 文稿文件数据Bean
     */
    public Bean getWengao() {
        return getZhengwenFileBean(ZW_TYPE.WEN_GAO.getCode());
    }
    
    /**
     * 
     * @return 正文文件数据Bean
     */
    public Bean getZhengwen() {
        return getZhengwenFileBean(ZW_TYPE.ZHENG_WEN.getCode());
    }
    
    /**
     * 
     * @return 当前公文是否能套红头
     */
    public boolean canRedhead() {
        if (hasSealFile()) {
            return false;
        }
        
        //如果正文文件为doc或docx文件，则可以套红头
        Bean fileBean = getZhengwenFileBean(ZW_TYPE.ZHENG_WEN.getCode());
        String extName = (fileBean == null) ? "" : FilenameUtils.getExtension(fileBean.getStr(GwConstant.FILE_NAME));
        if (fileBean != null && (extName.equals("doc") || extName.equals("docx"))) {
            return true;
        }
        
        return false;
    }
    
    /**
     * 
     * @return 是否能盖章
     */
    public boolean canSeal() {
        // 如果文件已盖章，则不能再盖章，要先取消盖章
        if (hasSealFile()) {
            return false;
        }
        // 如果文件套红头了，则可以盖章
        if (this.hasWenGao()) {
            return true;
        }

        return false;
    }

    /**
     * 
     * @return boolean 判断公文是否盖章
     */
    public boolean hasSealFile() {
        Bean fileBean = getZhengwenFileBean(ZW_TYPE.ENC_ZHENGWEN.getCode());
        if (fileBean != null) { //有盖章文件，则表示已盖章
            return true;
        }
        
        if (this.hasRedheadFile()) { //有红头文件，则表示已盖章
            return true;
        }
        
        return false;
    }

    /**
     * 
     * @return 判断公文是否套红头
     */
    public boolean hasRedheadFile() {
        Bean fileBean = getZhengwenFileBean(ZW_TYPE.RED_HEAD.getCode());
        if (fileBean != null) {
            return true;
        }
        return false;
    }

    /**
     * 
     * @return 判断公文是否有正文
     */
    public boolean hasZhengwen() {
        Bean fileBean = getZhengwenFileBean(ZW_TYPE.ZHENG_WEN.getCode());
        if (fileBean != null) {
            return true;
        }

        return false;
    }

    /**
     * @return 判断公文是否有文稿
     */
    public boolean hasWenGao() {
        Bean fileBean = getZhengwenFileBean(ZW_TYPE.WEN_GAO.getCode());
        if (fileBean != null) {
            return true;
        }

        return false;
    }
    
    /**
     * 
     * @param itemCode  正文小类型
     * @return 根据正文的小类型，取得指定类型的文件Bean
     */
    private Bean getZhengwenFileBean(String itemCode) {
        for (Bean bean : fileList) {
            if (bean.getStr(GwConstant.FILE_CAT).equals(ZW_TYPE.ZHENG_WEN.getCode()) 
                    && bean.getStr(GwConstant.ITEM_CODE).equals(itemCode)) {
                return bean;
            }
        }

        return null;
    }
    
    /**
     * 
     * @return 取得文件列表
     */
    public List<Bean> getFileList() {
        return fileList;
    }
    
    /**
     * 
     * @return 附件列表
     */
    public List<Bean> getFujianList() {
        List<Bean> list = new ArrayList<Bean>();
        for (Bean bean : fileList) {
            if (bean.getStr(GwConstant.FILE_CAT).equals(GwConstant.FUJIAN)) {
                list.add(bean);
            }
        }
        
        return list;
    }
    
    /**
     * 
     * @return 取得红头文件Bean
     */
    public Bean getRedHeadFileBean() {
        return this.getZhengwenFileBean(ZW_TYPE.RED_HEAD.getCode());
    }
    
    /**
     * 正文套红头之后，上传红头文件时，把红头文件设置成正文，原有的正文设置成文稿
     * @param oldBean 老的文件Bean（可能是文稿，也可能是正文Bean）
     */
    public static void change2Wengao(Bean oldBean) {
        //如果指定文件已经是文稿了，则不做任何操作。
        if (oldBean.getStr("ITEM_CODE").equals(ZW_TYPE.WEN_GAO.getCode())) {
            return;
        }
        Bean updateBean = new Bean(oldBean.getId());
        
        changeZwType(ZW_TYPE.WEN_GAO, updateBean);
        FileMgr.updateFile(updateBean);
    }
    
    /**
     * 红头文件盖章之后，上传盖章文件时，把原有的正文设置成红头文件，盖章文件设置成正文
     * @param oldBean 正文Bean
     * @return 是否成功？
     */
    public static boolean change2Redhead(Bean oldBean) {
        //如果原有的文件已经是红头文件了，则不用修改
        if (oldBean.getStr("ITEM_CODE").equals(ZW_TYPE.RED_HEAD.getCode())) {
            return false;
        }
        
        GwFileHelper helper = new GwFileHelper(oldBean.getStr("DATA_ID"), oldBean.getStr("SERV_ID"));
        // 如果已经存在红头文件，则不再保存成红头
        if (helper.hasRedheadFile()) {
            return false;
        }
        
        Bean updateBean = new Bean(oldBean.getId());
        changeZwType(ZW_TYPE.RED_HEAD, updateBean);
        FileMgr.updateFile(updateBean);
        
        return true;
    }
    
    /**
     * 修改正文类型参数值
     * @param zwType 正文类型对象
     * @param zwBean 需要被修改的Bean
     */
    public static void changeZwType(ZwType zwType, Bean zwBean) {
        zwBean.set("DIS_NAME", zwType.getName());
        zwBean.set("ITEM_CODE", zwType.getCode());
        zwBean.set("FILE_SORT", zwType.getSort());
    }
    
    /**
     * 
     * @param fileCat 文件类型
     * @return 指定类型文件列表
     */
    public List<Bean> getFileList(String fileCat) {
        List<Bean> list = new ArrayList<Bean>();
        for (Bean bean : fileList) {
            if (bean.getStr(GwConstant.FILE_CAT).equals(fileCat)) {
                list.add(bean);
            }
        }

        return list;
    }
    
    /**
     * 
     * @param fileCat 文件类型 (大类)
     * @param itemCode 文件类型(小类)
     * @return 指定类型文件列表
     */
    public List<Bean> getFileList(String fileCat, String itemCode) {
        List<Bean> list = new ArrayList<Bean>();
        for (Bean bean : fileList) {
            if (bean.getStr(GwConstant.FILE_CAT).equals(fileCat)) {
                list.add(bean);
            }
        }

        return list;
    }    
}
