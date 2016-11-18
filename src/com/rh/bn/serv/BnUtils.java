package com.rh.bn.serv;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.DateUtils;
import com.rh.oa.gw.GwServ;
/**
 * 公共服务类
 * @author lidongdong
 *
 */
public class BnUtils {
    private static Log log = LogFactory.getLog(BnUtils.class);
    /**
     * 改变印章状态，0.未启用;1.启用;2.停用;3.报废;4.销毁
     * @param sealId 印章ID
     * @param status 更改状态
     * @return
     */
    public static Bean changeSealStatus(String sealId,String status) {
        final Bean sealInfo = new Bean();
        sealInfo.setId(sealId);
        sealInfo.set("SEAL_STATE", status);
        sealInfo.set("SEAL_DESTORY_TIME", DateUtils.getDatetime());
        Bean sealOutBean = ServDao.save("BN_SEAL_BASIC_INFO", sealInfo);
        return sealOutBean;
    }
    /**
     * 印章周期
     * paramBean中需设置参数
     * SEAL_ID 印章ID
     * SERV_ID 服务ID
     * DATA_ID 数据ID
     * @param paramBean
     */
    public static void inSealCircle(ParamBean paramBean) {
        ParamBean circleBean = new ParamBean();
        try {
            //保存的印章主键，关联查询时候作为关联条件
            circleBean.set("SEAL_ID", paramBean.getStr("SEAL_ID"));
            //服务主键，打开对应的服务
            circleBean.set("SERV_ID", paramBean.getServId());
            //数据主键，打开对应数据
            circleBean.set("DATA_ID", paramBean.getId());
            circleBean.set("TITLE", paramBean.getStr("TITLE"));
            circleBean.set("CIRCLE_STATUS", paramBean.get("TYPE"));
            circleBean.setAddFlag(true);
            ServDao.save("BN_SEAL_CIRCLE", circleBean);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    /**
     * 取得链接中的参数
     * url 链接
     * param 参数的编码
     * return 参数值
     */
    public static String GetRequest(String url,String param) {
        if (url.indexOf("?") != -1) {
            String str = url.substring(url.indexOf("?")+1);
            
            String[] strs = str.split("&");
            
            for(int i = 0; i < strs.length; i ++) {
                if(param.equals(strs[i].split("=")[0])){
                  return strs[i].split("=")[1];
                }
                   
            }
         }
        return null;
    }
}
