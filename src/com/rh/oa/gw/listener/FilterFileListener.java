package com.rh.oa.gw.listener;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.oa.gw.FawenMgr;

/**
 * 过滤文件
 * @author ruaho_hdy
 * 
 */
public class FilterFileListener {

    /**
     * 过滤正文中非流经本人文稿不可显示;隐藏红头红章文件
     * @param paramBean 参数
     * @param outBean 结果集
     */
    public void afterFinds(ParamBean paramBean, OutBean outBean) {
        if (!paramBean.getStr("SERV_ID").equals("OA_GW_TYPE_FW")) { // 不是发文
            return;
        }

        if (!paramBean.getStr("FILE_CAT").equals("ZHENGWEN")) { // 不是正文
            return;
        }
        final String dispMode = paramBean.getStr("WF_DISPLAY_MODE");
        // 获取正文列表数据
        List<Bean> list = outBean.getDataList();

        List<Bean> newList = FawenMgr.fileViewRightFilter(dispMode, list);

        outBean.setData(newList);
    }

}
