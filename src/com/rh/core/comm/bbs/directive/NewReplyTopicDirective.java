package com.rh.core.comm.bbs.directive;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.rh.core.base.Bean;
import com.rh.core.comm.bbs.BBSMgr;
import com.rh.core.comm.cms.directive.DirectiveUtils;
import com.rh.core.util.Constant;
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
public class NewReplyTopicDirective implements TemplateDirectiveModel {
    /** log */
    private static Log log = LogFactory.getLog(NewReplyTopicDirective.class);
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void execute(Environment env, Map params,
            TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String debugName = DirectiveUtils.getString("debugName", params);
        String counter = DirectiveUtils.getString("counter", params);       
        
        log.debug(debugName + ":开始");
        Bean bean = new Bean().set(Constant.PARAM_ROWNUM, 5);
        if (counter != null && counter.length() > 0) {
            bean.set(Constant.PARAM_ROWNUM, counter);
        }
        
        List<Bean> commentList = BBSMgr.getInstance().getNewReplyTopics(bean);
        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        paramWrap.put("tag_list", ObjectWrapper.DEFAULT_WRAPPER.wrap(commentList));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        if (body != null) {
            body.render(env.getOut());
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
        log.debug(debugName + ":结束");

    }
}
