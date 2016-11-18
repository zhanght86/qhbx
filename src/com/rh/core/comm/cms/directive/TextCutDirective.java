package com.rh.core.comm.cms.directive;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 宏定义：对给定字符串进行截串处理
 * @author chaizhiqiang
 */
public class TextCutDirective implements TemplateDirectiveModel {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void execute(Environment env, Map params,
            TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String s = DirectiveUtils.getString("s", params);
        String length = DirectiveUtils.getString("length", params);
       
        if (s.length() > Integer.parseInt(length)) {
            s = s.substring(0, Integer.parseInt(length));
        }
        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        paramWrap.put("text", ObjectWrapper.DEFAULT_WRAPPER.wrap(s));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        if (body != null) {
            body.render(env.getOut());
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);

    }
}
