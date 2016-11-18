package com.rh.core.comm.cms.directive;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.rh.core.base.Bean;
import com.rh.core.comm.portal.PortalTemplServ;
import com.rh.core.util.JsonUtils;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 宏定义：根据组件ID取得组件HTML
 * @author chaizhiqiang
 */
public class GetComsHtmlDirective implements TemplateDirectiveModel {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void execute(Environment env, Map params,
            TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String comsId = DirectiveUtils.getString("comsId", params);
        String siteId = DirectiveUtils.getString("siteId", params);
        String param = DirectiveUtils.getString("comparam", params);
        
        Bean paramBean = new Bean();
        Bean extBean = new Bean();
        if (comsId != null && comsId.length() > 0) {
            paramBean.set("PC_ID", comsId);
        }
        if (siteId != null && siteId.length() > 0) {
            paramBean.set("$SITE_ID$", siteId);
        }
        if (param != null && param.length() > 0) { //用于组件的参数覆盖
            extBean = JsonUtils.toBean(param);
            paramBean.putAll(extBean);
        }
        Bean outBean = new PortalTemplServ().getPortalArea(paramBean);
        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        paramWrap.put("coms", ObjectWrapper.DEFAULT_WRAPPER.wrap(outBean));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        if (body != null) {
            body.render(env.getOut());
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);

    }
}
