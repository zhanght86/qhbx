package com.rh.core.comm.bbs.directive;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.rh.core.base.Bean;
import com.rh.core.comm.bbs.BBSMgr;
import com.rh.core.comm.cms.directive.DirectiveUtils;
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
public class ChannelMessageDirective implements TemplateDirectiveModel {
    /** log */
    private static Log log = LogFactory.getLog(ChannelMessageDirective.class);
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void execute(Environment env, Map params,
            TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String debugName = DirectiveUtils.getString("debugName", params);
        String channelId = DirectiveUtils.getString("channelId", params);
        
        log.debug(debugName + ":开始");
        Bean tag = new Bean();
        if (channelId != null && channelId.length() > 0) {
            int commentTotal = BBSMgr.getInstance().getCategoryCommentCouner(channelId);
            int newTopicCount = BBSMgr.getInstance().getCategoryNewTopicCouner(channelId);
            int topicTotal = BBSMgr.getInstance().getCategoryTopicCouner(channelId);
            tag.set("commentTotal", commentTotal);
            tag.set("newTopicCount", newTopicCount);
            tag.set("topicTotal", topicTotal);
        }      

        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        paramWrap.put("tag", ObjectWrapper.DEFAULT_WRAPPER.wrap(tag));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        if (body != null) {
            body.render(env.getOut());
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
        log.debug(debugName + ":结束");

    }
}
