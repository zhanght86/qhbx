package com.rh.oa.aud;

import java.util.ArrayList;
import java.util.List;
import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.DeptBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.dict.DictItems;
import com.rh.core.serv.dict.DictMgr;

/**
 * 稽核审计可稽核机构树的自定义字典实现类
 * @author cwwc
 *
 */
public class AuditOdeptDict implements DictItems {

    /**
     * 默认本级部门
     */
    private String dictId = "SY_ORG_ODEPT_ALL";
    
  
    public String getDictId(ParamBean paramBean) {
        return dictId;
    }

  
    public Bean getItems(ParamBean paramBean) {
        Bean root = new Bean();
        List<Bean> allData = new ArrayList<Bean>();
        if (paramBean.isEmpty("PID")) {
            String currUserCode = Context.getUserBean().getCode();
            List<DeptBean> deptList = AuditUtil.getAuditableOdeptList(currUserCode);
            //构造列表字典
            for (DeptBean dept : deptList) {
                allData.addAll(DictMgr.getTreeList(dictId, dept.getId(), 2, true));
            }
        } else {
            allData = DictMgr.getTreeList(dictId, paramBean.getStr("PID"));
        }
       
        root.set("DICT_ID", "oaAuditDict");
        root.set("DICT_DIS_LAYER", "0");
        root.set("DICT_DIS_ID", "oaAuditDict");
        root.set("DICT_NAME", "已授权机构");
        root.set(DictMgr.CHILD_NODE, allData);
        return root;
    }
    
}