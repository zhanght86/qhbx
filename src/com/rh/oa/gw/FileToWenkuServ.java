package com.rh.oa.gw;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.base.db.Transaction;
import com.rh.core.comm.FileMgr;
import com.rh.core.comm.FileServ;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.ServUtils;
import com.rh.core.util.Constant;
import com.rh.core.util.Lang;

/**
 * 文件同步文库类
 * @author wangchen
 * 
 */
public class FileToWenkuServ extends CommonServ {
    
    /**
     * 将审批单中的文件同步到文库某栏目中 1、复制主文件 2、新添主文档 3、复制其余辅文件并关联主文档主键
     * @param paramBean 参数bean
     * @return outBean 结果bean
     */
    public OutBean fileToWenku(ParamBean paramBean) {
        if (paramBean.isEmpty("DOCUMENT_MAIN_ID")) {
            throw new TipException("未找到正文");
        }
        ServDefBean mainServ = ServUtils.getServDef(paramBean.getStr("MAIN_SERV_ID"));
        ServDefBean srcServ = ServUtils.getServDef(mainServ.getSrcId());
        
        // 查询公文对应的所有文件
        FileServ fs = new FileServ();
        ParamBean param = new ParamBean();
        param.set("SERV_ID", mainServ.getSrcId());
        param.set("DATA_ID", paramBean.getStr("MAIN_DATA_ID"));
        List<Bean> fileList = fs.finds(param).getList(Constant.RTN_DATA);
        String fileStr = "";
        for (Bean file: fileList) {
            if (fileStr.equals("")) {
                fileStr = file.getStr("FILE_ID");
            } else {
                fileStr = fileStr + Constant.SEPARATOR + file.getStr("FILE_ID");
            }
        }

        // 检查前台提交的主文件是否在后台存在
        if (fileStr.equals("") || fileStr.indexOf(paramBean.getStr("DOCUMENT_MAIN_ID")) < 0) {
            throw new TipException("未找到文件");
        }
        
        String mainFileId = Lang.getUUID();
        String mainDocId = Lang.getUUID();
        
        //Context.getDSBean().getStr("NAME");
        Transaction.begin("");
        try {
         // 复制主文件
            ParamBean fileBean = new ParamBean();
            Bean fileCopyBean = new Bean();
            fileBean.set("serv", FileMgr.CURRENT_SERVICE);
            fileBean.set(Constant.KEY_ID, paramBean.getStr("DOCUMENT_MAIN_ID"));
            OutBean fileOutBean = fs.byid(fileBean);
            fileCopyBean.set("FILE_ID", mainFileId);
            fileCopyBean.set("SERV_ID", "SY_COMM_WENKU_DOCUMENT");
            fileCopyBean.set("DATA_ID", mainDocId);
            fileCopyBean = FileMgr.copyFile(fileOutBean, fileCopyBean);

            // 更新公文记录的栏目字段DOCUMENT_CHNL
            if (srcServ.getItem("DOCUMENT_CHNL") != null) {
                param = new ParamBean();
                param.setServId(mainServ.getSrcId());
                param.setAct(ServMgr.ACT_SAVE);
                param.setId(paramBean.getStr("MAIN_DATA_ID"));
                param.set("DOCUMENT_CHNL", paramBean.getStr("DOCUMENT_CHNL"));
                ServMgr.act(param);
            }
            
            // 复制其余辅文件并关联主文档主键        
            if (paramBean.getStr("DOCUMENT_AUX_ID").length() != 0) {
                String[] auxFiles = paramBean.getStr("DOCUMENT_AUX_ID").split(Constant.SEPARATOR);
                for (String auxFile : auxFiles) {
                    // 检查前台提交的辅文件是否在后台存在
                    if (fileStr.indexOf(auxFile) < 0) {
                        OutBean outBean = new OutBean().setError("子文件包含非主单文件");
                        return outBean;
                    }
                    fileCopyBean = new Bean();
                    fileBean.set(Constant.KEY_ID, auxFile);
                    fileBean.set(Constant.PARAM_SERV_ID, "SY_COMM_FILE");
                    fileOutBean = fs.byid(fileBean);
                    fileCopyBean.set("SERV_ID", "SY_COMM_WENKU_DOCUMENT");
                    fileCopyBean.set("DATA_ID", mainDocId);
                    fileCopyBean = FileMgr.copyFile(fileOutBean, fileCopyBean);
                }
            }
            Transaction.commit();
        } catch (Exception e) {
            if (e.getCause() instanceof TipException) {
                throw (TipException) (e.getCause());
            }
        } finally {
            Transaction.end();
        }
        
        // 新添主文档
        param = new ParamBean();
        param.setServId(ServMgr.SY_COMM_WENKU_DOCUMENT);
        param.setAct(ServMgr.ACT_SAVE);
        param.setAddFlag(true);
        param.setId(mainDocId);
        param.set("DOCUMENT_ID", mainDocId);
        param.set("DOCUMENT_FILE", mainFileId);
        param.set("DOCUMENT_CHNL", paramBean.getStr("DOCUMENT_CHNL"));
        param.set("DOCUMENT_TITLE", paramBean.getStr("DOCUMENT_TITLE"));
        ServMgr.act(param);
        
        OutBean outBean = new OutBean();
        outBean.setOk(Context.getSyMsg("成功将文件同步到文库相应栏目"));
        return outBean;
    }
    
}
