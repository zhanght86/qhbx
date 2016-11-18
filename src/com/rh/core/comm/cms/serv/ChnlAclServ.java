package com.rh.core.comm.cms.serv;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;

/**
 * 
 * @author yangjy
 * 
 */
public class ChnlAclServ extends CommonServ {

    private static final String SERV_ID = "SY_COMM_INFOS_ACL_CHNL";

    /**
     * 
     * @param paramBean 参数Bean
     * @return 执行结果
     */
    public OutBean addAcl(ParamBean paramBean) {
        OutBean out = new OutBean();

        List<Bean> list = createAclBeanList(paramBean);
        paramBean.setBatchSaveDatas(list);

        // 批量保存数据
        batchSave(paramBean);

        return out;
    }

    /**
     * 
     * @param paramBean 参数Bean
     * @return 创建AclBean列表
     */
    private List<Bean> createAclBeanList(ParamBean paramBean) {
        String idArray = paramBean.getStr("idArray");
        String[] ids = idArray.split(",");
        List<Bean> list = new ArrayList<Bean>();
        for (String id : ids) {
            Bean aclBean = new Bean();
            aclBean.set("OWNER_CODE", id);
            aclBean.set("ACL_TYPE", paramBean.getStr("ACL_TYPE"));
            aclBean.set("DATA_ID", paramBean.getStr("DATA_ID"));
            aclBean.set("SERV_ID", paramBean.getStr("SERV_ID"));
            aclBean.set("OWNER_TYPE", paramBean.getStr("OWNER_TYPE"));
            aclBean.set("OWNER_SCOPE", "ALL");
            // SERV_ID
            if (!existItem(aclBean)) {
                list.add(aclBean);
            }
        }

        return list;
    }

    @Override
    protected void beforeSave(ParamBean paramBean) {
        super.beforeSave(paramBean);
    }

    /**
     * 
     * @param aclBean aclBean
     * @return 指定记录是否存在
     */
    private boolean existItem(Bean aclBean) {
        SqlBean sql = new SqlBean();
        sql.and("OWNER_CODE", aclBean.getStr("OWNER_CODE"));
        sql.and("DATA_ID", aclBean.getStr("DATA_ID"));
        sql.and("ACL_TYPE", aclBean.getStr("ACL_TYPE"));

        int count = ServDao.count(SERV_ID, sql);

        if (count == 0) {
            return false;
        }

        return true;
    }
}
