package com.rh.oa.off;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.Constant;

/**
 * 办公用品出库表
 * @author hdy
 */
public class OffOutStorage extends CommonServ {
    
    private final String oaOffApply = "OA_OFF_APPLY"; //申请单
    private final String oaOffApplyTraits = "OA_OFF_APPLY_TRAITS"; //申请物品明细
    private final String oaOffInStorage = "OA_OFF_INSTORAGE"; //库存物品服务ID
    private final String outStorageStatus = "1"; //出库状态,1标示未出库
    private final String outStorageStatus2 = "2"; //出库状态,2标示已出库
    //private final String outStorageStatus3 = "3"; //出库状态,3标示未出库完全
    private final String applyStatus = "1"; //审批同意
    
   
   @Override
    protected void afterQuery(ParamBean paramBean, OutBean outBean) {
       
       String linkWhere = paramBean.getStr("_linkWhere");
       if (linkWhere.length() > 0) { //获取关联服务的父ID
           linkWhere = linkWhere.substring(linkWhere.indexOf("'") + 1, linkWhere.lastIndexOf("'"));
       }
       
       SqlBean queryApplyTraits = new SqlBean(); //查出此申请单中申请明细中没有发放的物品
       queryApplyTraits.selects("*").and("APPLY_ID", linkWhere).and("APPLY_STATUS", applyStatus)
               .and("OUTSTORAGE_STATUS", outStorageStatus);
       List<Bean> applyTraits = ServDao.finds(oaOffApplyTraits, queryApplyTraits); //获取此申请单下没有发放的物品
       
       List<Bean> outBeanList = outBean.getList(Constant.RTN_DATA);
       for (int i = 0; i < applyTraits.size(); i++) { //将申请单中没有发放的物品展示到列表
           Bean newBean = applyTraits.get(i); 
           newBean.set("OFFICE_STOCK", newBean.getStr("OFFICE_NUMBER"))
                           .set("OFFICE_UNITE__NAME", DictMgr.getName("OA_OFF_UNIT", newBean.getStr("OFFICE_UNITE")))
                           .set("OFFICE_TYPE__NAME", DictMgr.getName("OA_OFF_TYPE", newBean.getStr("OFFICE_TYPE")))
                           .set("APPLY_ID", linkWhere);
           outBeanList.add(i, newBean);
       }
    }
   
   @Override
    protected void beforeSave(ParamBean paramBean) { //若为出库列表中[出库]按钮传递保存操作
       //获取出库数量
       int count = Integer.parseInt(paramBean.getStr("OFFICE_STOCK"));
        SqlBean query = new SqlBean(); //入库表查询Bean
        query.and("OFFICE_ID", paramBean.getStr("OFFICE_ID")).and("RESIDUE_NUMBER", "<>", 0).asc("S_ATIME")
                  .selects("OFFICE_ID,RESIDUE_NUMBER");
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
   
  /**
   * 查看库存量是否满足出库量
   *  @param  paramBean officeId 某个物品的ID；count 某个物品的出库数量
   * @return 满足返回true，不满足返回false
   */
   public OutBean decInStorage(ParamBean paramBean) {
       SqlBean query = new SqlBean();
       query.selects("OFFICE_ID,sum(RESIDUE_NUMBER) RESIDUE_NUMBER").and("OFFICE_ID", paramBean.getStr("OFFICE_ID"))
           .groups("OFFICE_ID");
       List<Bean> countList = ServDao.finds(oaOffInStorage, query);
       if (countList.size() < 1) {
           return new OutBean().set("FLAG", false);
       }
       int count = 
       Integer.parseInt(countList.get(0).getStr("RESIDUE_NUMBER")) - Integer.parseInt(paramBean.getStr("OFFICE_STOCK"));
       OutBean outBean = new OutBean();
       if (countList.size() > 0 && count >= 0) {
           outBean.set("FLAG", true);
       } else {
           outBean.set("FLAG", false);
       }
       return outBean;
   }
   
   /**
    * 部门和用户字典表联动
    * @param paramBean 传入参数
    * @return 字典参数
    */
   public OutBean linkDic(ParamBean paramBean) {
       SqlBean query = new SqlBean();
       query.selects("DEPT_CODE,DEPT_NAME").and("USER_CODE", paramBean.getStr("USER_CODE"));
       return new OutBean(ServDao.find(paramBean.getStr("SERVID"), paramBean));
   }
   
   /**
    * 更新申请单的发放状态
    * @param paramBean 参数
    * @return 返回Bean
    */
   public OutBean saveApply(ParamBean paramBean) {
       SqlBean query = new SqlBean();
       query.and("APPLY_ID", paramBean.getStr("APPLY_ID")).selects("OUTSTORAGE_STATUS");
       Bean objApply = ServDao.find(oaOffApply, query);
       objApply.set("OUTSTORAGE_STATUS", outStorageStatus2);
       return new OutBean(ServDao.save(oaOffApply, objApply));
   }
   
   /**
    * 用于出库时，输入数量要与库存量比较，不能大于库存量
    * @param paramBean 参数
    * @return 输出Bean
    */
   public OutBean inStorageCount(ParamBean paramBean) {
       SqlBean query = new SqlBean(); //查询参数
       query.selects("OFFICE_ID,sum(RESIDUE_NUMBER) RESIDUE_NUMBER")
           .and("OFFICE_ID", paramBean.getStr("OFFICE_ID"))
           .groups("OFFICE_ID");
       List<Bean> queryList = ServDao.finds(oaOffInStorage, query);
       int count = 0;
       if (queryList.size() > 0) {
           count = queryList.get(0).getInt("RESIDUE_NUMBER");
       }
       return new OutBean().setCount(count);
   }
   
   /**
    * 修改申请明细列表出库状态 
    * @param paramBean 参数
    * @return 结果集
    */
   public OutBean updateOffTraits(ParamBean paramBean) {
       ParamBean offTraits = new ParamBean();
       offTraits.setAddFlag(false).setAct(ServMgr.ACT_SAVE);
       offTraits.setId(paramBean.getStr("PK_CODE"));
       offTraits.setServId(paramBean.getStr("SERV_ID"));
       offTraits.set("OUTSTORAGE_STATUS", outStorageStatus2);
       return ServMgr.act(offTraits);
   }
   
   
   /**
    * 修改审批单的发放状态
    * @param paramBean 参数集合
    * @return 结果集
    */
   /*public OutBean updateThisApplyOutStorageStart(ParamBean paramBean) {
       SqlBean allOffSql = new SqlBean(); //此审批单下所有审批同意明细查询语句
       allOffSql.and("APPLY_ID", paramBean.getStr("APPLY_ID")).and("APPLY_STATUS", applyStatus);
       int allOffCount = ServDao.count(oaOffApplyTraits, allOffSql);
       
       SqlBean notOutOffSql = new SqlBean(); //此审批单下没有出库切审批同意明细查询语句
       notOutOffSql.and("APPLY_ID", paramBean.getStr("APPLY_ID")).and("APPLY_STATUS", applyStatus);
       notOutOffSql.and("OUTSTORAGE_STATUS", 1);
       int  notOutOffCount = ServDao.count(oaOffApplyTraits, notOutOffSql);
       
       ParamBean updateApply = new ParamBean();
       updateApply.setId(paramBean.getStr("APPLY_ID")).setServId(oaOffApply);
       updateApply.setAddFlag(false).setAct(ServMgr.ACT_SAVE);
       
       if (allOffCount - notOutOffCount > 0) { //如果明细大于已出库的
           updateApply.set("OUTSTORAGE_STATUS", outStorageStatus3);
       } else if (allOffCount - notOutOffCount == 0) { //如果明细等于已出库的
           updateApply.set("OUTSTORAGE_STATUS", outStorageStatus2);
       }
       return ServMgr.act(paramBean);
   }*/
}
