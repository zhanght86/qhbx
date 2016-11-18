package com.rh.core.comm.bbs.directive;

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
 * @author chaizhiqiang
 * 
 */
public class TopicDirective implements TemplateDirectiveModel {
    /** log */
    private static Log log = LogFactory.getLog(TopicDirective.class);
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void execute(Environment env, Map params,
            TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String debugName = DirectiveUtils.getString("debugName", params);
        int counter = DirectiveUtils.getInt("counter", params);
        int page = DirectiveUtils.getInt("page", params);
        String chnlId = DirectiveUtils.getString("chnlId", params);
        
        log.debug(debugName + ":开始");
        ParamBean queryBean = new ParamBean(ServMgr.SY_COMM_BBS_TOPIC, ServMgr.ACT_QUERY);
        if (counter > 0) {
            queryBean.setQueryPageShowNum(counter);
        }
        if (page > 1) {
            queryBean.setQueryPageNowPage(page);
        }
        if (chnlId != null && chnlId.length() > 0) {
            // param.set("DOCUMENT_CHNL", channelId);
            List<Bean> treeWhere = new ArrayList<Bean>();
            treeWhere.add(new Bean().set("DICT_ITEM", "CHNL_ID").set("DICT_VALUE", chnlId));
            queryBean.set("_treeWhere", treeWhere);
        }
        
        queryBean.set("TOPIC_SHOW_TYPE", 1);
        queryBean.set("TOPIC_CHECKED", 1);
        queryBean.setQueryPageOrder("S_CTIME desc,S_MTIME desc");
        OutBean outBean = ServMgr.act(queryBean);
        List<Bean> topicList = outBean.getDataList();
        Bean pageBean = outBean.getPage();
        
        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        paramWrap.put("tag_list", ObjectWrapper.DEFAULT_WRAPPER.wrap(topicList));
        paramWrap.put("_PAGE_", ObjectWrapper.DEFAULT_WRAPPER.wrap(pageBean));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        if (body != null) {
            body.render(env.getOut());
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
        log.debug(debugName + ":结束");

    }
}
