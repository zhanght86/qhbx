package com.rh.oa.zh.seal;

import java.util.ArrayList;
import java.util.List;

import com.rh.client.DES;
import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.FileMgr;
import com.rh.core.org.UserBean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;

/**
 * 盖章文件服务
 * @author yangjy
 *
 */
public class SealFileServ extends CommonServ {
    /**
     * 
     * @param param 参数
     * @return 查询结果
     */
    public OutBean findPrintODept(ParamBean param) {
        List<Object> values = new ArrayList<Object>();
        
        values.add(param.getStr("ORIG_FILE_ID"));
        values.add(param.getStr("GW_ID"));
        
        StringBuilder sql = new StringBuilder();
        sql.append("select DEPT_CODE ,DEPT_NAME from SY_ORG_DEPT a where DEPT_CODE in (");
        sql.append("select S_ODEPT from OA_GW_GONGWEN where GW_ID in ( ");
        sql.append("select data_id from SY_COMM_FILE where ORIG_FILE_ID = ?)");
        sql.append(" union ");
        sql.append("select S_ODEPT from OA_GW_GONGWEN where GW_ID = ?");
        sql.append(") order by a.DEPT_LEVEL, a.DEPT_SORT");
        
        List<Bean> list = Context.getExecutor().query(sql.toString(), values);

        for (Bean bean : list) {
            final String deptCode = bean.getStr("DEPT_CODE");
            String deptNum = SealMgr.getODeptNum(deptCode);
            bean.set("ODEPT_NUM", deptNum);
        }
        
        OutBean out = new OutBean();
        out.setData(list);
        out.setOk();
        out.setCount(list.size());

        return out;
    }
    
    /**
     * 取得查看有水印的黑头黑长文件的在印章系统的URL
     * @param bean 参数
     * @return 返回URL
     */
    public OutBean getViewSealFileInfo(ParamBean bean) {
        OutBean out = new OutBean();
        UserBean userBean = Context.getUserBean();
        String workNum = userBean.getStr("USER_WORK_NUM");
        if (workNum.length() == 0) {
            out.setError("员工编号不能为空。");
            return out;
        }
        
        out.put("URL", "");
        
        final String fileId = bean.getStr("fileId");
        Bean fileBean = FileMgr.getFile(fileId);
        Bean sealFile = SealMgr.getSealFileBean(fileBean);
        if (sealFile != null && sealFile.isNotEmpty("SEAL_FILE_ID")) {
            StringBuilder url = new StringBuilder();
            url.append(Context.getSyConf(SealMgr.CONF_SEAL_SYS_HOST, ""));
            url.append("/general/modules/wendang/two/sealedFileShow.jsp");
            url.append("?disno=");
            
            StringBuilder opts = new StringBuilder();
            opts.append(sealFile.getStr("SEAL_FILE_ID"));
            opts.append(";");
            opts.append(userBean.getStr("USER_WORK_NUM"));
            
            try {
                url.append(DES.desEncrypt(opts.toString()));
            } catch (Exception e) {
                log.info(e.getMessage(), e);
            }
                    
            out.put("URL", url.toString());
            out.setOk();
        }

        return out;
    }
    
}
