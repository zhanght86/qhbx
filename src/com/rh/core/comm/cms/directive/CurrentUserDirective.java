package com.rh.core.comm.cms.directive;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Context;
import com.rh.core.org.UserBean;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 宏定义:当前用户
 * @author liwei
 */
public class CurrentUserDirective implements TemplateDirectiveModel {
    /** log */
    private static Log log = LogFactory.getLog(CurrentUserDirective.class);

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] model,
            TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String debugName = DirectiveUtils.getString("debugName", params);
        //根据用户id获取用户，用于【他的文档】模块
        log.debug(debugName + ":开始");
        //获取用户信息
        UserBean user = Context.getUserBean();
        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        paramWrap.put("user", ObjectWrapper.DEFAULT_WRAPPER.wrap(user));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        if (body != null) {
            body.render(env.getOut());
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
        log.debug(debugName + ":结束");
    }

}
