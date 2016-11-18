package com.rh.bn.serv;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Constant;

public class BannerPicServ extends CommonServ{
    public OutBean getPicInfos(ParamBean paramBean){
        ParamBean queryBean = new ParamBean();
        queryBean.setWhere(" and POSITION_PIC = " + paramBean.get("POSITION_PIC"));
        queryBean.setServId(paramBean.getServId());
        queryBean.setAct("query");
        OutBean picBeans = ServMgr.act(queryBean);
        /*paramBean.setAct("query");
        OutBean picBeans = ServMgr.act(paramBean);*/
        List<Object> newsList = picBeans.getList(Constant.RTN_DATA);
        // 2.遍历取得新闻图片
        for (Object o : newsList) {
            Bean b = (Bean) o;
            Bean bean = new Bean().set("DATA_ID", b.getId());
          
            bean.set("FILE_CAT", "TUPIANJI");
            bean.set(Constant.PARAM_SELECT, "FILE_ID");
            //添加获取新闻图片排序,默认升序，保证每次显示的是第一张上传的图片
            bean.set(Constant.PARAM_ORDER, "S_MTIME ASC");
            List<Bean> pictureList = ServDao.finds(ServMgr.SY_COMM_FILE, bean);
            if (!pictureList.isEmpty()) {
                if (!pictureList.get(0).isEmpty()) {
                b.set("picture", pictureList.get(0));
                }
            }
        }
        OutBean picBean = new OutBean();
        return picBean.setCount(picBeans.getCount()).set(Constant.RTN_DATA, newsList);
    }
    protected void afterByid(ParamBean paramBean, OutBean outBean) {
        outBean.set("AUDIT_ID", outBean.getId());
    }
    protected void afterQuery(ParamBean paramBean, OutBean outBean) {
    System.out.println(outBean.toString());
    }
}
