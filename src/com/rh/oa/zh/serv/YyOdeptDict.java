package com.rh.oa.zh.serv;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.dict.DictItems;
import com.rh.core.serv.dict.DictMgr;

/**
 * 用印审批可选择的印章所属机构树的自定义字典实现类
 * @author wangchen
 *
 */
public class YyOdeptDict implements DictItems {

    /**
     * 默认本级部门
     */
    private String dictId = "SY_ORG_ODEPT_SUB";
    private String dictAllId = "SY_ORG_ODEPT_ALL";
    
    public String getDictId(ParamBean paramBean) {
        return dictId;
    }

    public Bean getItems(ParamBean paramBean) {       
        Bean root = new Bean();
        List<Bean> ancientList = null;
        if (paramBean.isEmpty("PID")) {           
            //
            Bean dict = DictMgr.getDict(dictAllId);
            final LinkedHashMap<String, Bean> odeptMap = DictMgr.getItemMap(dict);
            //
            String currOdeptCode = Context.getUserBean().getODeptCode();
            Bean odept = odeptMap.get(currOdeptCode);
            //
            List<Bean> oList = new ArrayList<Bean>();
            Bean oBean = new Bean();
            oBean.set("NAME", odept.getStr("NAME"));
            oBean.set("DEPT_TYPE", 2);
            oBean.set("ID", odept.getStr("ID"));
            oBean.set("LEAF", 2);
            oBean.set("FLAG", 1);
            oBean.set("PID", odept.getStr("PID"));
            oBean.set(DictMgr.CHILD_NODE, DictMgr.getTreeList(dictId, currOdeptCode, 2, false));
            oList.add(oBean);
            ancientList = getAncient(oList, odeptMap);
        } else {
            ancientList = DictMgr.getTreeList(dictId, paramBean.getStr("PID"));
        }
        //
        root.set("DICT_ID", "oaYyOdeptDict");
        root.set("DICT_DIS_LAYER", "0");
        root.set("DICT_DIS_ID", "oaAuditDict");
        root.set("DICT_NAME", "可选印章所属机构");
        root.set(DictMgr.CHILD_NODE, ancientList);
        return root;
    }
    
    /**
     * 递归生成祖先树
     * @param oList 当前子树
     * @param odeptMap 机构缓存Map
     * @return 祖先树
     */
    private List<Bean> getAncient(List<Bean> oList, LinkedHashMap<String, Bean> odeptMap) {
        //
        String currOdeptCode = oList.get(0).getStr("PID");
        if (currOdeptCode.isEmpty()) {
            return oList;
        }
        Bean odept = odeptMap.get(currOdeptCode);       
        //
        List<Bean> pList = new ArrayList<Bean>();
        Bean pBean = new Bean();
        pBean.set("NAME", odept.getStr("NAME"));
        pBean.set("DEPT_TYPE", 2);
        pBean.set("ID", odept.getStr("ID"));
        pBean.set("LEAF", 2);
        pBean.set("FLAG", 1);
        pBean.set("PID", odept.getStr("PID"));
        pBean.set(DictMgr.CHILD_NODE, oList);
        pList.add(pBean);
        List<Bean> ancientList = getAncient(pList, odeptMap);

        return ancientList;
    }
}