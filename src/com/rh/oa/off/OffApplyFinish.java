package com.rh.oa.off;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.rh.core.serv.CommonServ;
import com.rh.core.serv.ParamBean;

/**
 * 申请办结通知单
 * @author hdy
 *
 */
public class OffApplyFinish extends CommonServ {

    @Override
    protected void beforeQuery(ParamBean paramBean) {
       if (paramBean.getStr("_extWhere").isEmpty()) {
           String newDate = new SimpleDateFormat("yyyy-MM").format(new Date());
           paramBean.set("_extWhere",
                   " and  substr(S_MTIME,1,7) = '" + newDate + "'");
       }
    }
}
