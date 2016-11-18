package com.rh.oa.lib.listener;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.file.FileUploadListener;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.RequestUtils;
import com.rh.oa.lib.LibConstants;
import com.rh.oa.lib.serv.ItemMgr;
import com.rh.oa.zh.seal.SecureFile;

/**
 * 文库监听类
 * @author yangjy
 * 
 */
public class LibFileListener implements FileUploadListener {
    private static final String LIB_FILE = "OA_LIB_FILE";

    
    public void beforeUpdate(Bean paramBean) {

    }

    
    public void befortAdd(Bean paramBean) {

    }

    
    public void afterUpdate(Bean paramBean, List<Bean> dataList) {
        // TODO 删除印章系统原有数据

        // TODO 重新创建加密文档
    }

    
    public void afterAdd(Bean paramBean, List<Bean> fileList) {
        int readType = 0;
        if (paramBean.isNotEmpty("READ_TYPE")) {
            readType = paramBean.getInt("READ_TYPE");
        } else if (fileList.size() > 0) {
            Bean libItem = null;
            String itemId = fileList.get(0).getStr("DATA_ID");
            libItem = ItemMgr.find(itemId);
            if (libItem != null) {
                readType = libItem.getInt("READ_TYPE");
            }
        }

        for (Bean fileBean : fileList) {
            ParamBean libFileBean = new ParamBean();
            libFileBean.setId(fileBean.getId());
            libFileBean.set("FILE_ID", fileBean.getId());
            libFileBean.set("ITEM_ID", fileBean.get("DATA_ID"));
            libFileBean.setAddFlag(true);
            ServMgr.act(LIB_FILE, ServMgr.ACT_SAVE, libFileBean);
            if (readType == LibConstants.READ_TYPE_READONLY) {
                // 如果是只读文件，则转换成安全文档
//                SecureFile sfile = new SecureFile();
//                sfile.setSealSysHost(Context.getSyConf("SEAL_SYS_HOST", ""));
//                sfile.setOaSysHost(RequestUtils.getSysHost());
//                sfile.createFile(fileBean);
            }
        }
    }

    /**
     * 
     * @param paramBean 参数Bean
     * @param outBean 处理结果Bean
     */
    public void afterDelete(ParamBean paramBean, OutBean outBean) {
        List<Bean> deletedDatas = outBean.getDataList();
        for (Bean file : deletedDatas) {
            deleteItemFile(file.getId());
        }
    }
    
    /**
     * 删除LibFile记录，以及删除安全文档
     * @param fileId 文件ID
     */
    public static void deleteItemFile(String fileId) {
        ParamBean fileBean = new ParamBean();
        fileBean.setId(fileId);
        OutBean outBean = ServMgr.act(LIB_FILE, ServMgr.ACT_DELETE, fileBean);
        
        List<Bean> libFiles = outBean.getDataList();
        
        Bean libItem = null;
        if (libFiles.size() > 1) {
            String itemId = libFiles.get(0).getStr("DATA_ID");
            libItem = ItemMgr.find(itemId);
        }
        
        if (libItem != null && libItem.getInt("READ_TYPE") == LibConstants.READ_TYPE_READONLY) {
            // 如果是只读文件，则删除安全文档
            SecureFile sfile = new SecureFile();
            sfile.setSealSysHost(Context.getSyConf("SEAL_SYS_HOST", ""));
            sfile.setOaSysHost(RequestUtils.getSysHost());
            sfile.deleteFile(fileId);
        }
    }
}
