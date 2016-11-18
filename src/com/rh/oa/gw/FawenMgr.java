package com.rh.oa.gw;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.serv.ServDao;
import com.rh.oa.gw.util.GwConstant;

/**
 * 发文常用方法
 * @author yangjy
 * 
 */
public class FawenMgr {
    /**
     * 
     * @param gwId 发文ID
     * @return 发文记录数据
     */
    public static Bean getFawenBean(String gwId) {
        Bean gwBean = ServDao.find(GwConstant.OA_GW_TYPE_FW, gwId);

        return gwBean;
    }
    
    
    /**
     * 
     * @param dispMode 审批单查看模式
     * @param list 正文文件列表
     * @return 有查看权限的正文文件列表
     */
    public static List<Bean> fileViewRightFilter(final String dispMode, List<Bean> list) {
        
        // 获取符合条件的数据
        List<Bean> newList = new ArrayList<Bean>();
        for (Bean b : list) {
            // 如果是红头文件，则不把红头文件返回给前台
            if (b.getStr("ITEM_CODE").equals("REDHEAD")) {
                continue;
            }
            
            // 如果是文稿，且显示模式为分发或最低权限，则不把文稿列表返回前台
            if (b.getStr("ITEM_CODE").equals("WENGAO") && (StringUtils.isEmpty(dispMode)
                    || dispMode.equals("MODE_SEND")
                    || dispMode.equals("MODE_BASE"))) {
                continue;
            }

            newList.add(b);
        }
        return newList;
    }    
}
