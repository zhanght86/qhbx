package com.rh.core.comm.news.directive;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.rh.core.base.Bean;
import com.rh.core.comm.cms.directive.DirectiveUtils;
import com.rh.core.comm.news.mgr.NewsMgr;
import com.rh.core.util.Constant;
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
public class NewsListDirective implements TemplateDirectiveModel {

    /** log */
    private static Log log = LogFactory.getLog(NewsListDirective.class);
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void execute(Environment env, Map params,
            TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        
        Bean paramBean = DirectiveUtils.getBean(params);
        log.debug(paramBean.getStr("debugName") + ":开始");
        List<Bean> list = null;
        String newsType = paramBean.getStr("NEWS_TYPE");
        
        //标题图文
        if ("5".equals(newsType)) {
            list = NewsMgr.getInstance().getTitleNews(paramBean).getList(Constant.RTN_DATA);
        } else if ("3".equals(newsType)) {
            list = NewsMgr.getInstance().getTuWenNews(paramBean).getList(Constant.RTN_DATA);
        } else if ("2".equals(newsType)) {
            list = NewsMgr.getInstance().getJiaoDianNews(paramBean).getList(Constant.RTN_DATA);
        } else {
            list = NewsMgr.getInstance().getGeneralNews(paramBean).getList(Constant.RTN_DATA);
        }
        
        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        paramWrap.put("tag_list", ObjectWrapper.DEFAULT_WRAPPER.wrap(list));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        if (body != null) {
            body.render(env.getOut());
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);

        log.debug(paramBean.getStr("debugName") + ":结束");
    }
}
