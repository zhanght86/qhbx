package com.rh.core.comm.bbs.directive;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.comm.cms.directive.DirectiveUtils;
import com.rh.core.serv.ServDao;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 宏定义:根据栏目ID取得子栏目信息 
 * @author chaizhiqiang
 */
public class ChannelLevelDirective implements TemplateDirectiveModel {
    /** log */
    private static Log log = LogFactory.getLog(ChannelLevelDirective.class);

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] model,
            TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String debugName = DirectiveUtils.getString("debugName", params);
        String channelId = DirectiveUtils.getString("channelId", params);
        log.debug(debugName + ":开始");
        List<Bean> channelList = null;
        if (null != channelId && channelId.length() > 0) {
            channelList = ServDao.finds("SY_COMM_BBS_CHNL", new Bean().set("CHNL_PID", channelId));
        }
        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        paramWrap.put("tag_list", ObjectWrapper.DEFAULT_WRAPPER.wrap(channelList));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        if (body != null) {
            body.render(env.getOut());
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
        log.debug(debugName + ":结束");
    }

}
