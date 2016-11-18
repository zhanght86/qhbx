package com.rh.core.comm.mark;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;

/**
 * 收藏夹的标签
 * @author jason
 *
 */
public class MarkServ extends CommonServ {

    
    
    @Override
    protected void afterDelete(ParamBean paramBean, OutBean outBean) {
        // TODO Auto-generated method stub
        super.afterDelete(paramBean, outBean);
      //获取标签ID
        String markId = paramBean.getId();
        
        ParamBean query = new ParamBean(ServMgr.SY_COMM_FAVORITES_MARK , ServMgr.ACT_FINDS);
                  query.set("MARK_ID", markId);
                  
        OutBean out = ServMgr.act(query);
        
        List<Bean> list = out.getDataList();
        
        if (list.size() > 0) {
            Bean tempBean = new Bean();
            tempBean.set("MARK_ID", markId);
            //删除  收藏-标签
            ServDao.deletes(ServMgr.SY_COMM_FAVORITES_MARK, tempBean);
            
            query.remove("MARK_ID");
            
            for (Bean bean : list) {
                //获取收藏ID  
                String favoriteId = bean.getStr("FAVORITE_ID");
                
                query.set("FAVORITE_ID", favoriteId);
                out = ServMgr.act(query);
                //如果在关系表中不存在favoriteId，则删除
                if (out.getDataList().size() == 0) {
                    Bean favBean = new Bean();
                    favBean.set("FAVORITE_ID", favoriteId);
                    ServDao.deletes(ServMgr.SY_COMM_FAVORITES, favBean);
                }
            }
            
        }
    }
 
}
