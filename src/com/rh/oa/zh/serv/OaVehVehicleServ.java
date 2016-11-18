package com.rh.oa.zh.serv;

import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;

/**
 * 车辆信息serv
 * @author ruaho_hdy
 *
 */
public class OaVehVehicleServ extends CommonServ {

    /**
     * 数据是否已存在
     * @param paramBean 参数
     * @return 数据信息
     */
    public OutBean isExist(ParamBean paramBean) {
        SqlBean sql = new SqlBean();
        sql.and(paramBean.getStr("QUERY_ITEM"), paramBean.getStr("QUERY_VALUE"));
        return new OutBean().set("COUNT", ServDao.count(paramBean.getServId(), sql));
    }
    
    /**
     * 获取车牌号
     * @param paramBean 参数
     * @return 车牌号
     */
    public OutBean getVehPlateNumber(ParamBean paramBean) {
        SqlBean sql = new SqlBean();
        sql.selects("VEH_NAME").and("VEH_PLATE_NUMBER", paramBean.getStr("VEH_PLATE_NUMBER"));
        return new OutBean(ServDao.find(paramBean.getServId(), sql));
    }
}
