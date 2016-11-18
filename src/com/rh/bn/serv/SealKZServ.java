package com.rh.bn.serv;

import java.util.List;

import com.rh.bn.util.SealUtil;
import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.DateUtils;
import com.rh.core.wfe.serv.ProcDefServ;

public class SealKZServ extends CommonServ{
    public OutBean saveSeal(ParamBean paramBean){
        String strWhere = " and APPLY_ID = '" + paramBean.getId() + "'";
        paramBean.setId(paramBean.getId());
        paramBean.set("SEAL_FLAG1","2");
        ServDao.update(paramBean.getServId(),paramBean);
        List<Bean> KZInfoBeans = ServDao.finds("BN_SEAL_KZ_INFO", strWhere);
        UserBean userBean = Context.getUserBean();
        for (int i = 0; i < KZInfoBeans.size(); i++) {
            /*
             * 遍历得到每一个印章信息，根据印章信息得到印章基本信息，保存入库
             */
            Bean KZInfoBean = KZInfoBeans.get(i);
            ParamBean sealBean = saveNewSeal(userBean, KZInfoBean);
            
            //印章入库后带入当前印章大类的印章规格到BN_SEAL_KZ_INFO(刻章信息)表 Aodanni
            //根据所选择的印章大类获取规格
            List<Bean> styleBeans = ServDao.finds("BN_SEAL_STYLE",
            		new SqlBean().set("SEAL_TYPE1", KZInfoBean.getStr("SEAL_TYPE1")));
            //得到唯一大类规格 存入印章信息
            if(styleBeans.size()==1){
                Bean styleBean = styleBeans.get(0);
                sealBean.set("SEAL_FONT", styleBean.get("SEAL_FONT"));//字体排列
                sealBean.set("SEAL_QUALITY", styleBean.get("SEAL_QUALITY"));//印章材质
                sealBean.set("SEAL_WIDTH", styleBean.get("SEAL_WIDTH"));//印章宽度(mm)
                sealBean.set("SEAL_HEIGHT", styleBean.get("SEAL_HEIGHT"));//印章高度(mm)
                sealBean.set("SEAL_COLOR", styleBean.get("SEAL_COLOR"));//字体颜色
                sealBean.set("SEAL_FORM", styleBean.get("SEAL_FORM"));//印章外形
            }
            
            
            //生成最大索引 ——因为索引不需要直接生成 所以注释掉
//            sealBean.set("EKEY_ADDRESS",getMaxCode()+1);
            sealBean.setServId("BN_SEAL_BASIC_INFO").setAct("save");
            
            OutBean sealOutBean = ServMgr.act(sealBean);
            
            /*
             * 印章入库后更改BN_SEAL_KZ_INFO(刻章信息)表的责任人为当前人
             */
            KZInfoBean.set("AUDIT_USER", userBean.getUser());
            KZInfoBean.set("SEAL_STATE1", "2");//入库状态
            ServDao.update("BN_SEAL_KZ_INFO", KZInfoBean);
           
            paramBean.set("SEAL_ID",sealOutBean.getId());
        }
        paramBean.set("TYPE","1");
        //记录刻章信息到周期表中
        BnUtils.inSealCircle(paramBean);
        //入库后自动办结审批单
        SealUtil.finish(paramBean);
        return new OutBean().setOk("已入库");
    }
    
//    public Integer getMaxCode() {
//        ParamBean paramBean = new ParamBean();
//        paramBean.setSelect("max(EKEY_ADDRESS) EKEY_ADDRESS");
//        paramBean.setServId("BN_SEAL_BASIC_INFO");
//        paramBean.setAct("query");
//        OutBean outBean = ServMgr.act(paramBean);
//   /*     ServDao.find("BN_SEAL_BASIC_INFO",paramBean);*/
//        return outBean.getDataList().get(0).getInt("EKEY_ADDRESS");
//    }

    

    /**
      * 遍历得到每一个印章信息，根据印章信息得到印章基本信息，保存入库
      */
    private ParamBean saveNewSeal(UserBean userBean, Bean KZInfoBean) {
        ParamBean sealBean = new ParamBean();
        sealBean.set("SEAL_NAME", KZInfoBean.get("SEAL_NAME"));//印章名称
        sealBean.set("SEAL_REASON",KZInfoBean.get("SEAL_REASON"));//刻章原因
        sealBean.set("SEAL_OWNER_USER",KZInfoBean.get("SEAL_OWNER_USER"));//保管人
        sealBean.set("SEAL_USER_PHONE",KZInfoBean.get("SEAL_USER_PHONE"));//保管人电话
        sealBean.set("KEEP_TDEPT_CODE",KZInfoBean.get("KEEP_TDEPT_CODE"));//保管部门
        sealBean.set("KEEP_ODEPT_CODE",KZInfoBean.get("KEEP_ODEPT_CODE"));//保管机构
        sealBean.set("SEAL_DZ", KZInfoBean.get("SEAL_DZ"));//有无电子印章
        sealBean.set("SEAL_CODE", "");//印章编号
        sealBean.set("SEAL_TYPE1", KZInfoBean.get("SEAL_TYPE1"));//印章类别1
        sealBean.set("SEAL_TYPE2", KZInfoBean.get("SEAL_TYPE2"));//印章类别2
        sealBean.set("SEAL_RESPONS_USER", userBean.getUser());//责任人
        sealBean.set("SEAL_OWNER_USER", KZInfoBean.get("S_USER"));
        sealBean.set("SEAL_SPEC", KZInfoBean.get("SEAL_SPEC"));
        sealBean.set("SEAL_STATE", "0");//印章状态
        sealBean.set("SEAL_START_TIME", DateUtils.getDatetime());//启用时间
        return sealBean;
    }
    
}
