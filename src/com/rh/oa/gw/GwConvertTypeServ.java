package com.rh.oa.gw;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.org.mgr.OrgMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.util.Strings;

/**
 * 公文类型转换，获取能被转换的公文模板
 * @author yangjy
 * 
 */
public class GwConvertTypeServ extends CommonServ {

    @Override
    protected void beforeQuery(ParamBean paramBean) {
        super.beforeQuery(paramBean);
        String servId = paramBean.getStr("TMPL_CODE");
        
        Bean tmplBean = GwTmplMgr.getTmplByid(servId);
        if (tmplBean != null && tmplBean.isNotEmpty("TMPL_TRANS_CODES")) {
            paramBean.setQueryExtWhere(" and TMPL_CODE in ('" + Strings.replace(tmplBean.getStr("TMPL_TRANS_CODES")
                    , ",", "','") + "')");
        } else {
            paramBean.setQueryExtWhere(" and 1=2 ");
        }
    }

    @Override
    protected void afterQuery(ParamBean paramBean, OutBean outBean) {
        super.afterQuery(paramBean, outBean);
        List<Bean> dataList = outBean.getDataList();
        if (dataList.size() == 0) {
            outBean.setOk("请检查公文模板配置是否正确。");
            return;
        }
        
        List<Bean> result = new ArrayList<Bean>();
        for (Bean bean:dataList) {
            String tmplCode = bean.getStr("TMPL_CODE");
            if (OrgMgr.checkServMenuAuth(tmplCode)) {
                result.add(bean);
            }
        }
        
        outBean.setData(result);
        outBean.setPage(result.size());
    }

}
