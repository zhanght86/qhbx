package com.rh.oa.off;

import com.rh.core.serv.CommonServ;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;

/**
 * 办公用品入库表，在保存之前进行校验，防止页面数据被篡改
 * @author hdy
 */
public class OffInStorage extends CommonServ {
    /**
     * 物品报价报服务ID
     */
    private final String oaOffQuotation = "OA_OFF_QUOTATION";
    
   @Override
    protected void beforeSave(ParamBean paramBean) {
       //重置参数中单价和总金额及剩余数量
       paramBean.set("OFFICE_AMOUNT", 
               (Double.parseDouble(paramBean.getStr("OFFICE_PRICE").isEmpty() 
               ? paramBean.getBean("_OLDBEAN_").getStr("OFFICE_PRICE") : paramBean.getStr("OFFICE_PRICE"))) 
               * (Integer.parseInt(paramBean.getStr("OFFICE_STOCK").isEmpty() 
               ? paramBean.getBean("_OLDBEAN_").getStr("OFFICE_STOCK") : paramBean.getStr("OFFICE_STOCK"))) + "")
       .set("RESIDUE_NUMBER", paramBean.getStr("OFFICE_STOCK").isEmpty()
               ? paramBean.getBean("_OLDBEAN_").getStr("OFFICE_STOCK") : paramBean.getStr("OFFICE_STOCK"))
       .set("OFFICE_PRICE", paramBean.getStr("OFFICE_PRICE"));
       updataOffObj(paramBean.getStr("OFFICE_ID"), paramBean.getStr("OFFICE_PRICE")); //修改报价表中的物品单价
    }
   
   /**
    * 修改报价表中的物品单价
    * @param id 物品主键
    * @param price 单价
    */
   private void updataOffObj(String id, String price) {
       ParamBean paramBean = new ParamBean();
       paramBean.setId(id);
       paramBean.setServId(oaOffQuotation);
       paramBean.setAddFlag(false);
       paramBean.setAct(ServMgr.ACT_SAVE);
       paramBean.set("OFFICE_PRICE", price);
       ServMgr.act(paramBean);
   }
}
