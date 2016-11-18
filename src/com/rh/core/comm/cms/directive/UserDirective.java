package com.rh.core.comm.cms.directive;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 获取指定用户信息
 * @author jason
 * 
 */
public class UserDirective implements TemplateDirectiveModel {
    /** log */
    private static Log log = LogFactory.getLog(CurrentUserDirective.class);

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] model,
            TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String debugName = DirectiveUtils.getString("debugName", params);
        // 根据用户id获取用户，用于【他的文档】模块
        String userId = DirectiveUtils.getString("userId", params);
        log.debug(debugName + ":开始");
        // 获取用户信息
        UserBean user = UserMgr.getUser(userId);

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
