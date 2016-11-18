package com.rh.oa.off;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.Constant;
import com.rh.core.util.NumberUtils;

/**
 * 办公用品入库表，统计
 * @author hdy
 */
public class OffInStorageCount extends CommonServ {
    
    private final String sFlag = "1"; //假删除标示位
    private final String oaOffQuotation = "OA_OFF_QUOTATION"; //报价表服务ID
    private final String oaOffInStorage = "OA_OFF_INSTORAGE"; //入库用品服务ID
   
   @Override
    protected void afterQuery(ParamBean paramBean, OutBean outBean) {
       List<Bean> outBeanList = getQueryNoPageList(paramBean.getServId());
       List<Bean> queryList = getGroupByList(paramBean);
       List<Bean> newList = new ArrayList<Bean>();
       //重置数据
       for (int i = 0; i < queryList.size(); i++) {
           Bean objBean = ServDao.find(oaOffQuotation, queryList.get(i).getStr("OFFICE_ID"));
           Bean thisBean = outBeanList.get(i);
           thisBean.set("OFFICE_NAME", objBean.getStr("OFFICE_NAME"))
                   .set("OFFICE_MODEL", objBean.getStr("OFFICE_MODEL"))
                   .set("OFFICE_PRICE", NumberUtils.formatDouble(objBean.getDouble("OFFICE_PRICE"), 2))
                   .set("OFFICE_BRAND", objBean.getStr("OFFICE_BRAND"))
                   .set("OFFICE_STOCK", queryList.get(i).getStr("OFFICE_STOCK"))
                   .set("OFFICE_AMOUNT", NumberUtils.formatDouble(queryList.get(i).getDouble("OFFICE_AMOUNT"), 2));
           newList.add(thisBean);
       }
       outBean.setData(newList);
       outBean.setPage(newList.size());
       outBean.set("LEFT_DATE", paramBean.getStr("LEFT_DATE"))
                     .set("RIGHT_DATE", paramBean.getStr("RIGHT_DATE"));
    }
   
   /**
    * 获取按用品ID分组之后的数据
    * @param paramBean 参数集合
    * @return 符合条件的记录
    */
   private List<Bean> getGroupByList(Bean paramBean) {
       Bean query = new Bean(); //查询参数
       query.set(Constant.PARAM_SELECT, "OFFICE_ID,sum(OFFICE_STOCK) OFFICE_STOCK,sum(OFFICE_AMOUNT) OFFICE_AMOUNT");
       if (paramBean.getStr("LEFT_DATE").isEmpty() && paramBean.getStr("RIGHT_DATE").isEmpty()
               && paramBean.getStr("_searchWhere").isEmpty()) {
           String newDate = new SimpleDateFormat("yyyy-MM").format(new Date());
           query.set(Constant.PARAM_WHERE,  " and substr(S_ATIME,1,7) between '" + newDate + "' and '" 
                   + newDate + "' and S_FLAG = '" + sFlag + "' and S_ODEPT = '" 
                   + Context.getUserBean().getODeptCode() + "' group by OFFICE_ID");
       } else {
           query.set(Constant.PARAM_WHERE,  paramBean.getStr("_searchWhere") + " group by OFFICE_ID");
       }
       return ServDao.finds(oaOffInStorage, query);
   }
   
   /**
    * 获取不分页列表数据
    * @param servId 服务id
    * @return 结果列表
    */
   private List<Bean> getQueryNoPageList(String servId) {
       StringBuffer sql = new StringBuffer();
       sql.append(" ").append("and S_ODEPT = '").append(Context.getUserBean().getODeptCode())
          .append("' and S_FLAG='").append("1'");
       return ServDao.finds(servId, sql.toString());
   }
}
