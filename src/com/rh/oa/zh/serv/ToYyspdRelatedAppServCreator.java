package com.rh.oa.zh.serv;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.comm.FileMgr;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.serv.relate.RelatedServCreator;
import com.rh.core.util.Constant;
import com.rh.core.util.Strings;

/**
 * 根据某审批单起草用印审批单。
 * @author wangchen
 * 
 */
public class ToYyspdRelatedAppServCreator extends RelatedServCreator {

    /**电子用印转换服务id*/
    private static final String OA_YY_YYSPD_RELATE = "OA_YY_YYSPD_RELATE";
    /**实物用印转换服务id*/
    private static final String OA_YY_YYSPD_SW_RELATE = "OA_YY_YYSPD_SW_RELATE";
    /**被现实多次转换用印数据服务id*/
    private static final String OA_CREATOR_SEAL_CONTENT = "OA_CREATOR_SEAL_CONTENT";
    /**是否启用限制送交用印次数配置*/
    private static final String OA_CREATOR_SEAL_CONFIG = "OA_CREATOR_SEAL_CONFIG";
    /**转换用印serv_id，默认实物用印*/
    private static String thisNewServId = OA_YY_YYSPD_SW_RELATE;
    /**正文转换附件标识*/
    private static boolean resetZhengwen2Fujian = false;
    
    
    @Override
    protected void beforeCreate(Bean oldBean, ParamBean newServBean) {
        //电子用印
        if (newServBean.getInt("YY_TYPE") == Constant.YES_INT) {
            thisNewServId = OA_YY_YYSPD_RELATE;
        }
        resetZhengwen2Fujian = newServBean.getBoolean("RESET_ZHENGWEN2FUJIAN"); //正文转换附件标识
    }
    
    /**
     * @param oldBean 老审批单
     * @param outBean 新审批单保存之后，输出的数据
     */
    protected void afterCreate(Bean oldBean, OutBean outBean) {
        //不是实物用印
        if (thisNewServId.equals(OA_YY_YYSPD_RELATE)) {
            String mainSpdId = outBean.getId();
            List<Bean> fileList = FileMgr.getFileListBean("", mainSpdId);
            if (fileList == null) {
                return;
            }

            String addFileIds = "";
            String addFileCodes = "";
            for (Bean fileBean : fileList) {
                String addFileId = fileBean.getId();
                String addFileCode = fileBean.getStr("DIS_NAME");
                addFileIds = Strings.addValue(addFileIds, addFileId);
                addFileCodes = Strings.addValue(addFileCodes, addFileCode);
            }

            if (!addFileIds.isEmpty()) {
                ParamBean addBean = new ParamBean();
                addBean.set("mainSpdId", mainSpdId);
                addBean.set("addFileIds", addFileIds);
                addBean.set("addFileCodes", addFileCodes);
                addBean.setServId("OA_YY_YYSPD_FILE_PRI");
                addBean.setAct("addFileToPriList");
                OutBean addRes = ServMgr.act(addBean);
                if (!addRes.isOk()) {
                    throw new TipException("用印审批单添加用印材料失败！");
                }
            }
        }
        //正文转换附件标识,并且是实物用印转换
        if (resetZhengwen2Fujian) {
            resetFiles(outBean.getId());
        }
        saveLimitServData(new Bean().set("SERV_ID", thisNewServId).set("DATA_ID", oldBean.getId())
                                    .set("RELATE_ID", outBean.getId()));
    }
    
    /**
     * 保存送交用印之后限制数据
     * @param data 参数
     */
    private void saveLimitServData(Bean data) {
        if (Context.getSyConf(OA_CREATOR_SEAL_CONFIG, false)) {
            ServMgr.act(OA_CREATOR_SEAL_CONTENT, "save", data);
        }
    }
    
    /**
     * 重置附件cat类型
     * @param dataId 数据主键
     */
    private void resetFiles(String dataId) {
        SqlBean sql = new SqlBean();
        sql.set("DATA_ID", dataId);
        sql.orders("FILE_SORT");
        List<Bean> fileList = ServDao.finds(ServMgr.SY_COMM_FILE, sql);
        for (Bean b : fileList) {
            if (b.getStr("FILE_CAT").equals("ZHENGWEN")) {
                b.set("FILE_CAT", "FUJIAN").set("ITEM_CODE", "ZHENGWEN");
                ServDao.save(ServMgr.SY_COMM_FILE, b);
            }
        }
    }
}
