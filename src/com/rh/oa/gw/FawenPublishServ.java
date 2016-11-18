package com.rh.oa.gw;

import java.util.ArrayList;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.serv.comment.CommentServ;
import com.rh.core.util.Constant;
import com.rh.core.util.Strings;

/**
 * 发文发布服务
 * @author hdy
 *
 */
public class FawenPublishServ extends CommentServ {
    
    /**公文发布服务ID*/
    private static final String OA_GW_FW_PUBLISH = "OA_GW_FW_PUBLISH";
    /**总公司或者省公司用户配置*/
    private static final String CONF_ORG_ZGS_LEVEL = "ORG_ZGS_LEVEL";
    /**总省公司角色*/
    private static final String ROLE_ZS = "ZS";
    /**是否公开权限值标示位，1标示是公开*/
    private static final String IS_OPEN = "1";
    
    @Override
    protected void beforeQuery(ParamBean paramBean) {
        String odeptCode = Context.getUserBean().getODeptCode();
        
        ArrayList<String> roles = new ArrayList<String>();
        roles.add(Constant.PUBLIC_ROLE_CODE);
        roles.add(odeptCode);
        if (isZSUser()) {
            roles.add(ROLE_ZS);
        }
        
        paramBean.setQueryExtWhere(" and GW_ID in(select DATA_ID from OA_GW_FW_PUBLISH where RIGHT in('"
                + Strings.toString(roles, "','") + "'))");
    }
    
    /**
     * 
     * @return 判断用户是否是总公司或省公司用户
     */
    private boolean isZSUser() {
        int currentLevel = Context.getUserBean().getODeptLevel();
        
        int zgsLevel = Context.getSyConf(CONF_ORG_ZGS_LEVEL, -1);
        if (zgsLevel >= 0) {
            if (currentLevel == zgsLevel
                    || currentLevel == (zgsLevel + 1)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 操作公文发布数据
     * @param hdy
     * @param paramBean 公文参数
     * @return add/update/delete的数据
     */
    public OutBean operatePublishData(ParamBean paramBean) {
        if (!paramBean.getStr("ISOPEN").equals(IS_OPEN)) {
            return new OutBean().set("MSG", "ERROR");
        }
        SqlBean sql = new SqlBean().and("DATA_ID", paramBean.getId());
        Bean gwFaBu = ServDao.find(OA_GW_FW_PUBLISH, sql);
        if (null == gwFaBu) {
            gwFaBu = new Bean();
        }
        gwFaBu.set("DATA_ID", paramBean.getId()).set("RIGHT", paramBean.getStr("OPEN_TYPE"));
        //当公文允许公开时，添加公文发布数据
        if (paramBean.getStr("ISOPEN").equals(Constant.YES)) {
            //向公文发布服务add/update数据
            ServDao.save(OA_GW_FW_PUBLISH, gwFaBu);
        } 
        return new OutBean().set("MSG", "OK");
    }
}
