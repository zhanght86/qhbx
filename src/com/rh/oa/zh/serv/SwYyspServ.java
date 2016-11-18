package com.rh.oa.zh.serv;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;


/**
 * 实物用印审批单服务
 * @author hedongyang
 *
 */
public class SwYyspServ extends YyspServ {
    
    /**公共文档子服务*/
    private static final String YYSPD_FILE_PUB_SERV = "OA_YY_YYSPD_FILE_PUB";
    
    /**
     * 获取附件列表
     * @param paramBean 参数
     * @return 文件列表
     */
    public OutBean getAttachInfo(ParamBean paramBean) {
        String dataId = paramBean.getStr("DATA_ID");
        List<Bean> lastList = new ArrayList<Bean>();
        SqlBean sql = new SqlBean();
        sql.set("DATA_ID", dataId);
        sql.orders("FILE_SORT");
        lastList.addAll(ServDao.finds(ServMgr.SY_COMM_FILE, sql));
        getPubServFiles(lastList, dataId);
        return new OutBean().setData(lastList);
    }
    
    /**
     * 获取公共文档
     * @param lastList 最终数据对象
     * @param dataId 主单ID
     */
    private void getPubServFiles(List<Bean> lastList, String dataId) {
        //检查是否已存在
        SqlBean sql = new SqlBean();
        sql.selects("YY_FILE_ID").and("YYID", dataId);
        List<Bean> relPubFiles = ServDao.finds(YYSPD_FILE_PUB_SERV, sql);
        StringBuffer values = new StringBuffer("");
        for (Bean b : relPubFiles) {
            boolean isExitFile = false; //是否存在此文件
            String fileId = b.getStr("YY_FILE_ID");
            for (Bean c : lastList) {
                if (fileId.equals(c.getStr("FILE_ID"))) {
                    isExitFile = true;
                    break;
                }
            }
            if (!isExitFile) {
                values.append(Constant.SEPARATOR).append(fileId);
            }
        }
        String valuesStr = values.toString();
        if (valuesStr.length() > 1 && valuesStr.startsWith(Constant.SEPARATOR)) {
            SqlBean query = new SqlBean();
            query.andIn("FILE_ID", valuesStr.substring(1).split(Constant.SEPARATOR));
            lastList.addAll(ServDao.finds(ServMgr.SY_COMM_FILE, query));
        }
    }
}
