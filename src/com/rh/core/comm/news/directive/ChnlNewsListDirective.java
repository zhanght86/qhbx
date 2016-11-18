package com.rh.core.comm.news.directive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.comm.cms.directive.DirectiveUtils;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * @author liwei
 * 
 */
public class ChnlNewsListDirective implements TemplateDirectiveModel {

    /** log */
    private static Log log = LogFactory.getLog(ChnlNewsListDirective.class);
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void execute(Environment env, Map params,
            TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        
        String debugName = DirectiveUtils.getString("debugName", params);
        String chnlId = DirectiveUtils.getString("chnlId", params);
        log.debug(debugName + ":开始");
        
        ParamBean queryBean = new ParamBean(ServMgr.SY_COMM_NEWS, ServMgr.ACT_QUERY);
        List<Bean> treeWhere = new ArrayList<Bean>();
        treeWhere.add(new Bean().set("DICT_ITEM", "CHNL_ID").set("DICT_VALUE", chnlId));
        queryBean.set("_treeWhere", treeWhere);
        queryBean.setQueryPageNowPage(1).setQueryPageShowNum(10);
        
        OutBean outBean = ServMgr.act(queryBean);
        
        
        List<Bean> list = outBean.getDataList();
        Bean pageBean = outBean.getPage();

        
        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        paramWrap.put("tag_list", ObjectWrapper.DEFAULT_WRAPPER.wrap(list));
        paramWrap.put("_PAGE_", ObjectWrapper.DEFAULT_WRAPPER.wrap(pageBean));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        if (body != null) {
            body.render(env.getOut());
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);

        log.debug(debugName + ":结束");
    }
}
