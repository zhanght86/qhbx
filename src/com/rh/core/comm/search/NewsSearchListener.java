package com.rh.core.comm.search;

import java.util.List;


import com.rh.core.base.Bean;
import com.rh.core.plug.search.IndexListener;
import com.rh.core.plug.search.RhIndex;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
/**
 * 信息搜索授权监听类
 *    
 * @author zhl
 * @version $Id$ 
 */
public class NewsSearchListener implements IndexListener {


	
    @Override
    public void index(RhIndex iaMsg, Bean searchDef, Bean data) {
        //获取信息Id
        String newsId = data.getStr("NEWS_ID");
        
        /**给信息作者授检索权限**/
        Bean documentBean = ServDao.find("SY_COMM_INFOS_BASE", newsId);
        String documentUser = documentBean.getStr("NEWS_USER");
        iaMsg.grantUser(documentUser);
        
         
        //查询信息的权限
        ParamBean aclQuery = new ParamBean("SY_COMM_NEWS_ACL", ServMgr.ACT_QUERY);
        aclQuery.setQueryExtWhere(" and DATA_ID = '" + newsId + "'");
        aclQuery.setQueryNoPageFlag(true);
        List<Bean> aclList = ServMgr.act(aclQuery).getDataList();
        for (int i = 0; i < aclList.size(); i++) {
            String newsOwner = aclList.get(i).getStr("OWNER_CODE");
            /**拿到一个权限代码，逐一判断是什么权限，然后给放索引里**/
            
            // 判断，是不是用户
            Bean userBean = ServDao.find("SY_ORG_USER", newsOwner);
            if (userBean != null) { // 是用户
                iaMsg.grantUser(newsOwner);
            } else {
                // 判断，是不是公司
                Bean cmpyBean = ServDao.find("SY_ORG_CMPY", newsOwner);
                if (cmpyBean != null) { // 是公司
                    iaMsg.grantCmpy(newsOwner);
                } else {
                    // 判断，是不是部门
                    Bean deptBean = ServDao.find("SY_ORG_DEPT", newsOwner);
                    if (deptBean != null) { // 是部门
                        iaMsg.grantDept(newsOwner);
                    } else {
                        // 判断，是不是角色
                        Bean roleBean = ServDao.find("SY_ORG_ROLE", newsOwner);
                        if (roleBean != null) { // 是角色
                            iaMsg.grantRole(newsOwner);
                        } else {
                            //判断是不是业务条线！！！（暂时先不判断）
                        }
                    }
                }
            }
        }
    }
}
