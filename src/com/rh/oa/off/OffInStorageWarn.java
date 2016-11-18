package com.rh.oa.off;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.serv.dict.DictMgr;

/**
 * 办公用品库存数量预警
 * @author hdy
 */
public class OffInStorageWarn extends CommonServ {
    
    private final String oaOffQuotation = "OA_OFF_QUOTATION"; //报价表服务ID
    
   /*创建[库存预警]视图
    * create or replace view instorage_warn as
    *    select t1.office_id office_id,t1.s_odept warn_odept,t2.s_odept off_odept,
    *    sum(t2.office_warn) office_warn, sum(t1.residue_number) office_stock
    *      from oa_off_instorage t1, oa_off_quotation t2
    *         where t1.office_id = t2.quotation_id and t1.s_flag = 1
    *             group by t1.office_id,t1.s_odept,t2.s_odept;
    */
    
    @Override
    protected void afterQuery(ParamBean paramBean, OutBean outBean) {
        List<Bean> outBeanList = getQueryNoPageList(paramBean.getServId());
        SqlBean query = new SqlBean();
        //重置之后的数据
        List<Bean> newBeanList = new ArrayList<Bean>();
        for (Bean b : outBeanList) {
            query.clear();
            query.and("QUOTATION_ID", b.getStr("OFFICE_ID")).and("S_ODEPT", Context.getUserBean().getODeptCode());
            //获取某个物品的基本信息
            Bean bean = ServDao.find(oaOffQuotation, query);
            if (null == bean || bean.isEmpty()) {
                continue;
            }
            //重置某个物品的基本信息
            b.set("OFFICE_NAME", bean.getStr("OFFICE_NAME")).set("OFFICE_UNITE", DictMgr.getFullName("OA_OFF_UNIT",
                    bean.getStr("OFFICE_UNITE"))).set("OFFICE_TYPE", DictMgr.getFullName("OA_OFF_TYPE",
                    bean.getStr("OFFICE_TYPE")));
            //获取库存数量
            int inStorageCount = b.getInt("OFFICE_STOCK");
            //获取报警值
            int warnCount = b.getInt("OFFICE_WARN");
            //比较上述两个值的大小
            if (inStorageCount < warnCount) {
                b.set("COLOR_FLAG", "RED"); //若库存小于报警值，则在页面显示红色
            } else {
                b.set("COLOR_FLAG", "BLUE");
            }
            newBeanList.add(b);
        }
        outBean.setData(newBeanList);
        outBean.setPage(newBeanList.size());
    }
    
    /**
     * 获取不分页数据
     * @param servId 服务id
     * @return 列表数据
     */
    private List<Bean> getQueryNoPageList(String servId) {
        StringBuffer sql = new StringBuffer(" ");
        String odeptCode = Context.getUserBean().getODeptCode();
        sql.append(" ").append("and WARN_ODEPT = '").append(odeptCode)
           .append("' and OFF_ODEPT = '").append(odeptCode).append("'");
        return ServDao.finds(servId, sql.toString());
    }
}
