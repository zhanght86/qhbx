package com.rh.core.comm.news;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.dict.DictItems;
import com.rh.core.serv.dict.DictMgr;

/**
 * 
 * @author yangjy
 *
 */
public class InfosChnlDict implements DictItems {
    /**
     * 数据字典 SY_COMM_INFOS_CHNL_MANAGE
     */
    private String dictId = "SY_COMM_INFOS_CHNL_MANAGE";
    
    /**
     * 
     * @return 取得信息发布管理员角色
     */
    private String getInfosAdminRole() {
        return Context.getSyConf("CMS_INFOS_ADMIN_ROLE", "CJADMIN");
    }    
    
    @Override
    public Bean getItems(ParamBean paramBean) {
        if (paramBean.isNotEmpty("REP_DICT_ID")) {
            dictId = paramBean.getStr("REP_DICT_ID");
        }
        
        Bean root = new Bean();

        List<Bean> child = new ArrayList<Bean>();
        root.set("CHILD", child);
        
        //设置信息管理员用户权限
        UserBean userBean = Context.getUserBean();
        if (userBean != null && userBean.existInRole(getInfosAdminRole())) {
            Context.getThread("_IS_SERV_DACL_ADMIN", true);
        } else {
            Context.setThread("_IS_SERV_DACL_ADMIN", false);
        }
        
        String ODeptCode = userBean.getODeptCode();
        if("0001B210000000000BU3".equals(ODeptCode)){
        	if (paramBean.isEmpty("PID")) {
        		appendDictInfo(root);
        		List<Bean> list = DictMgr.getTreeList(dictId);
        		child.addAll(list);
        	} else {
        		List<Bean> list = DictMgr.getTreeList(dictId, paramBean.getStr("PID"));
        		child.addAll(list);
        	}
        }else{
        	List<Bean> list = DictMgr.getTreeList(dictId, "2cR1zYTN3J0oVwwczQeapij", 0,true);
    		child.addAll(list);
        }
        
        return root;
    }
    
    /**
     * 增加字典基本信息
     * @param root 数据Bean
     */
    private void appendDictInfo(Bean root) {
        root.set("DICT_ID", dictId);
        root.set("DICT_DIS_LAYER", "0");
//        root.set("DICT_DIS_ID", "ZhuanfaDict");
        root.set("DICT_NAME", "组织机构");
        root.set("DICT_TYPE", "2");
    }    

    @Override
    public String getDictId(ParamBean paramBean) {
        return dictId;
    }

}
