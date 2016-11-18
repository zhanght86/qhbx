package com.rh.core.comm.cms.directive;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.comm.cms.serv.ChannelServ;
import com.rh.core.serv.ParamBean;

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
public class ChannelHomeDirective implements TemplateDirectiveModel {
    /** log */
    private static Log log = LogFactory.getLog(ChannelHomeDirective.class);

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] model,
            TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String debugName = DirectiveUtils.getString("debugName", params);
        String channelId = DirectiveUtils.getString("channelId", params);
        String siteId = DirectiveUtils.getString("SITE_ID", params);
        int count = DirectiveUtils.getInt("count", params);
        log.debug(debugName + ":开始");
        List<Bean> channelList = null;
        ParamBean queryBean = new ParamBean();
        queryBean.set("channelId", channelId);
        queryBean.set("siteId", siteId);
        queryBean.set("count", count);
        channelList = new ChannelServ().getSubChannel(queryBean).getDataList();
        
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
