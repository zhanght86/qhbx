package com.rh.bn.serv;

import java.util.List;

import com.rh.bn.util.SealUtil;
import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.DateUtils;

public class ChangeApplyServ extends CommonServ{
    /*印章申请单-变更详单 服务ID*/
    public static final String BN_CHANGE_TYPE = "BN_CHANGE_TYPE";
    /*印章基本信息库 服务ID*/
    public static final String BN_SEAL_BASIC_INFO = "BN_SEAL_BASIC_INFO";
    public OutBean saveInLibrary(ParamBean paramBean){
        OutBean outBean = new OutBean();
        //获取主单ID
        String id = paramBean.getId();
        String whereSql = " and APPLY_ID='"+id+"'";
        //根据主单ID查询出此单下的所有子单数据
        List<Bean> beans = ServDao.finds(BN_CHANGE_TYPE, whereSql ); 
        //遍历子单中每一条数据,根据变更类型分别更新印章库中对应数据
        for(Bean bean:beans){
            String sealCode = bean.getStr("SEAL_CODE");
            String changeType = bean.getStr("CHANGE_TYPE");
            ParamBean param = new ParamBean();
            param.setId(sealCode);
            if(changeType.equals("1")){
                param.set("SEAL_STATE", "1");
                param.set("SEAL_START_TIME", bean.getStr("S_ATIME"));
            }else if(changeType.equals("2")){
                param.set("SEAL_STATE", "2");
                param.set("SEAL_STOP_TIME", bean.getStr("S_ATIME"));
            }else if(changeType.equals("3")){
                //人员变更类型
                if(bean.getStr("CHANGE_PER_TYPE").equals("3")){
                    param.set("XZ_MANAGER", bean.getStr("CHANGE_PERSON"));
                    param.set("XZ_MANAGER_PHONE", bean.getStr("TEL_PHONE"));
                }else if(bean.getStr("CHANGE_PER_TYPE").equals("4")){
                    param.set("SEAL_RESPONS_USER", bean.getStr("CHANGE_PERSON"));
                    param.set("CHARGE_PHONE", bean.getStr("TEL_PHONE"));
                }else if(bean.getStr("CHANGE_PER_TYPE").equals("5")){
                    Bean keeperBean = ServDao.find("SY_ORG_USER", bean.getStr("CHANGE_PERSON"));
                    Bean keeperDeptBean = ServDao.find("SY_ORG_DEPT",keeperBean.getStr("DEPT_CODE"));
                    param.set("KEEP_ODEPT_CODE", keeperDeptBean.getStr("ODEPT_CODE"));
                    param.set("SEAL_USER_PHONE", bean.getStr("TEL_PHONE"));
                    param.set("SEAL_OWNER_USER", bean.getStr("CHANGE_PERSON"));
                }
            }else if(changeType.equals("4")){
                if(bean.getStr("CHANGE_SC_TYPE").equals("1")){
                    param.set("SEAL_STATE", "3");
                }else if(bean.getStr("CHANGE_SC_TYPE").equals("2")){
                    param.set("SEAL_STATE", "3");
                }else if(bean.getStr("CHANGE_SC_TYPE").equals("3")){
                    param.set("SEAL_STATE", "4");
                    param.set("SEAL_DESTORY_TIME", bean.getStr("S_ATIME"));
                }
                
            }
            
            Bean servBean = ServDao.update("BN_SEAL_BASIC_INFO", param);
            ServDao.update(paramBean.getServId(), paramBean.set("APPLY_STATUS", 2));
            //自动办结
        }
        SealUtil.finish(paramBean);
        return outBean;
    }

}
