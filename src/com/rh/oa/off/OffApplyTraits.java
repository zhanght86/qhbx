package com.rh.oa.off;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.Constant;

/**
 * 申请物品明细表，在保存之前进行校验，防止页面数据被篡改
 * @author hdy
 */
public class OffApplyTraits extends CommonServ {

    private final String oaOffApply = "OA_OFF_APPLY"; //申请单服务ID
    private final String oaOffQuotation = "OA_OFF_QUOTATION"; //用品报价ID
    private final String oaOffApplyStatus = "OA_OFF_APPLY_STATUS"; //申请状态字典服务ID
    private final String oaOffApplyTraits = "OA_OFF_APPLY_TRAITS"; //申请明细服务ID
    private final String oaOffInStorage = "OA_OFF_INSTORAGE"; //库存服务ID
    private final String applyStatus = "1"; //审批同意的物品状态
    private final String applyUnStatus = "2"; //审批不同意的物品状态
    
    @Override
    protected void beforeSave(ParamBean paramBean) {
        //获取数据库中真正的单价
       Bean  quotationObject = ServDao.find(oaOffQuotation, 
                 paramBean.getStr("OFFICE_ID").isEmpty() 
                 ?  paramBean.getBean("_OLDBEAN_").getStr("OFFICE_ID") : paramBean.getStr("OFFICE_ID"));
       //重置参数中单价和总金额
       paramBean.set("OFFICE_PRICE", quotationObject.getStr("OFFICE_PRICE")) .set("OFFICE_AMOUNT", 
               (Double.parseDouble(quotationObject.getStr("OFFICE_PRICE")) 
               * Integer.parseInt(paramBean.getStr("OFFICE_NUMBER").isEmpty() 
               ? paramBean.getBean("_OLDBEAN_").getStr("OFFICE_NUMBER") : paramBean.getStr("OFFICE_NUMBER"))) + "");
    }
    
    @Override
    protected void afterQuery(ParamBean paramBean, OutBean outBean) {
       //为每一条[申请明细]记录添加[申请状态]字段
        List<Bean> applyList = ServDao.finds(oaOffApply, new Bean()
            .set(Constant.PARAM_SELECT, "APPLY_ID, APPLY_STATUS"));
        List<Bean> traitsList = outBean.getList(Constant.RTN_DATA);
        List<Bean> dicList = DictMgr.getItemList(oaOffApplyStatus); //获取字典信息
        for (Bean b : applyList) {
            for (Bean c : traitsList) {
                if (b.getId().equals(c.getStr("APPLY_ID"))) {
                    c.set("COUNT", getOffCount(c.getStr("OFFICE_ID"))); //添加[库存数量]列
                    for (Bean d : dicList) {
                        if (d.getStr("ITEM_CODE").equals(b.getStr("APPLY_STATUS"))) {
                            c.set("APPLY_STATUS_V", d.getStr("ITEM_NAME"));
                        }
                    }
                }
            }
        }
    }
    
    
    /**
     * 修改某个审批单下的[同意审批]
     * @param paramBean 参数Bean，里面有ids为修改的记录id
     * @return Bean 返回更新条数
     */
    public OutBean updateApplyTraits(ParamBean paramBean) {
        Bean query = new Bean();
        int count = 0;
        query.set(Constant.PARAM_WHERE, " and TRAITS_ID in (" + paramBean.getStr("ids") + ")");
        List<Bean> applyTraitsList = ServDao.finds(paramBean.getServId(), query);
        if (paramBean.getStr("agreeFlag").equals("agree")) { //[同意]按钮操作
            for (Bean b : applyTraitsList) {
                if (!b.getStr("APPLY_STATUS").equals(applyStatus)) {
                    ParamBean param = new ParamBean(oaOffApplyTraits);
                    param.setId(b.getId()).set("APPLY_STATUS", applyStatus);
                    if (save(param).isOk()) {
                        count += 1;
                    }
                }
            }
        } else if (paramBean.getStr("agreeFlag").equals("unagree")) { //[不同意]按钮操作
            for (Bean b : applyTraitsList) {
                if (!b.getStr("APPLY_STATUS").equals(applyUnStatus)) {
                    ParamBean param = new ParamBean(oaOffApplyTraits);
                    param.setId(b.getId()).set("APPLY_STATUS", applyUnStatus);
                    if (save(param).isOk()) {
                        count += 1;
                    }
                }
            }
        }
        return new OutBean().set("count", count + "");
    }
    
    /**
     * 获取库存表中某个物品的剩余数量
     * @param offId 库存表中的物品ID
     * @return 返回库存表中某个物品的剩余数量
     */
    private int getOffCount(String offId) {
        SqlBean query = new SqlBean().selects("sum(RESIDUE_NUMBER) COUNT")
                .and("OFFICE_ID", offId);
        int count = 0;
        if (!ServDao.find(oaOffInStorage, query).getStr("COUNT").isEmpty()) {
            count = Integer.parseInt(ServDao.find(oaOffInStorage, query).getStr("COUNT"));
        }
        return count;
    }
    
    /**
     * 批量保存
     * @param paramBean 参数
     * @return 结果集
     */
    public OutBean batchSaveOff(ParamBean paramBean) {
        //办公用品名
        String[] officeName = paramBean.getStr("OFFICE_NAME").split(Constant.SEPARATOR);
        //单位
        String[] officeUnite = paramBean.getStr("OFFICE_UNITE").split(Constant.SEPARATOR);
        //单价
        String[] officePrice = paramBean.getStr("OFFICE_PRICE").split(Constant.SEPARATOR);
        //规格
        String[] officeBrand = paramBean.getStr("OFFICE_BRAND").split(Constant.SEPARATOR);
        //类别
        String[] officeType = paramBean.getStr("OFFICE_TYPE").split(Constant.SEPARATOR);
        //物品报价主键
        String[] quotationId = paramBean.getStr("QUOTATION_ID").split(Constant.SEPARATOR);
        //添加物品列表
        List<Bean> list = new ArrayList<Bean>();
        for (int i = 0; i < quotationId.length; i++) {
            Bean off = new Bean();
            off.set("OFFICE_ID", quotationId[i]);
            off.set("OFFICE_NAME", officeName[i]);
            off.set("OFFICE_UNITE", officeUnite[i]);
            off.set("OFFICE_PRICE", officePrice[i]);
            if (officeBrand.length == 0) {
                off.set("OFFICE_BRAND", "");
            } else {
                if (officeBrand.length <= i) {
                    off.set("OFFICE_BRAND", "");
                } else {
                    off.set("OFFICE_BRAND", officeBrand[i]);
                }
            }
            off.set("OFFICE_TYPE", officeType[i]);
            off.set("OFFICE_TYPE", officeType[i]);
            off.set("OFFICE_NUMBER", 1);
            off.set("APPLY_ID", paramBean.getStr("APPLY_ID"));
            list.add(off);
        }
        ParamBean batchParam = new ParamBean();
        batchParam.setServId("OA_OFF_APPLY_TRAITS");
        batchParam.setBatchSaveDatas(list);
        return batchSave(batchParam);
    }
}
