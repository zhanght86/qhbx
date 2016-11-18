package com.rh.oa.off;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;

/**
 * 办公用品出库表没有申请单流程，直接出库
 * @author hdy
 */
public class OffOutStorageNoFlow extends CommonServ {
    
    private final String oaOffInStorage = "OA_OFF_INSTORAGE"; //库存物品服务ID
   
   @Override
    protected void beforeSave(ParamBean paramBean) {
       //获取出库数量
       int count = Integer.parseInt(paramBean.getStr("OFFICE_STOCK"));
        SqlBean query = new SqlBean(); //入库表查询Bean
        query.selects("OFFICE_ID,RESIDUE_NUMBER")
            .and("OFFICE_ID", paramBean.getStr("OFFICE_ID"))
            .and("RESIDUE_NUMBER", "<>", 0)
            .asc("S_ATIME");
        //获取相应物品库存数量集合
        List<Bean> queryBean = ServDao.finds(oaOffInStorage, query);
        for (Bean b :queryBean) {
            int number = Integer.parseInt(b.getStr("RESIDUE_NUMBER"));
            count = count - number;
            if (count == 0) {
                b.set("RESIDUE_NUMBER", "0");
                ServDao.save(oaOffInStorage, b); //重置入库表中的剩余数量
                break;
            } else if (count > 0) {
                b.set("RESIDUE_NUMBER", "0");
                ServDao.save(oaOffInStorage, b); //重置入库表中的剩余数量
            } else if (count < 0) {
                b.set("RESIDUE_NUMBER", (-count) + "");
                ServDao.save(oaOffInStorage, b); //重置入库表中的剩余数量
                break;
            }
        }
    }
}
