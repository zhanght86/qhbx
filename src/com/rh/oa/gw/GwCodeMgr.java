package com.rh.oa.gw;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.Constant;
import com.rh.oa.gw.util.GwConstant;

/**
 * 机关代字相关方法
 * @author yangjy
 * 
 */
public class GwCodeMgr {
    /**
     * 
     * @param codeName 代字名称
     * @param odeptCode 机构名称
     * @return 取得指定机构的代字
     */
    public static Bean findCodeBean(String codeName, String odeptCode) {
        StringBuilder sb = new StringBuilder();
        sb.append(" and S_ODEPT ='").append(odeptCode).append("'");
        sb.append(" and CODE_NAME ='").append(codeName).append("'");

        Bean paramBean = new Bean();
        paramBean.set(Constant.PARAM_WHERE, sb.toString());

        Bean bean = ServDao.find(GwConstant.OA_GW_CODE, paramBean);

        return bean;
    }
    
    /**
     * 
     * @param codeName  代字名称
     * @param odeptCode 机构代码
     * @return 与指定机关代字属于同一个组的所有机关代字名称，多个代字之间使用逗号分隔。
     */
    public static String getCodeNamesInGroup(String codeName, String odeptCode) {
        List<Bean> list = findCodeGroup(codeName, odeptCode);
        
        if (list.size() == 0) {
            return codeName;
        }
        
        StringBuilder sb = new StringBuilder();
        for (Bean bean : list) {
            sb.append(bean.getStr("CODE_NAME"));
            sb.append(",");
        }

        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }
    
    /**
     * 
     * @param codeName 代字名称
     * @param odeptCode 机构代码
     * @return 与指定机关代字属于同一个组的所有机关代字List（包括自己）。如果没有组则返回自己。如果没有找打指定代字，则返回没有数据的List；
     */
    public static List<Bean> findCodeGroup(String codeName, String odeptCode) {
        Bean bean = findCodeBean(codeName, odeptCode);
        
        if (bean == null) {
            List<Bean> beanList = new ArrayList<Bean>();
            return beanList;
        } else if (bean.isEmpty("GROUP_CODE")) {
            List<Bean> beanList = new ArrayList<Bean>();
            beanList.add(bean);
            return beanList;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(" and S_ODEPT ='").append(odeptCode).append("'");
            sb.append(" and GROUP_CODE ='").append(bean.getStr("GROUP_CODE")).append("'");
            Bean paramBean = new Bean();
            paramBean.set(Constant.PARAM_WHERE, sb.toString());

            return ServDao.finds(GwConstant.OA_GW_CODE, paramBean);
        }
    }
}
