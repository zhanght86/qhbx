package com.rh.core.comm.zhidao.directive;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.comm.cms.directive.DirectiveUtils;
import com.rh.core.comm.zhidao.ZhidaoServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 获取个人关注信息
 * @author zhangjinxi
 * 
 */
public class ZhidaoFollowDirective implements TemplateDirectiveModel {
    /** log */
    private static Log log = LogFactory.getLog(ZhidaoFollowDirective.class);

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void execute(Environment env, Map params,
            TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String debugName = DirectiveUtils.getString("debugName", params);
        String userCode = DirectiveUtils.getString("userCode", params);
        
        log.debug(debugName + ":开始");
        
        ParamBean userQuery = new ParamBean();
        userQuery.set("userId", userCode);
        
        OutBean outBean = new ZhidaoServ().getAllCount(userQuery);
        

        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        paramWrap.put("categoryList", ObjectWrapper.DEFAULT_WRAPPER.wrap(outBean.get("categoryList")));
        paramWrap.put("followCounter", ObjectWrapper.DEFAULT_WRAPPER.wrap(outBean.get("followCounter")));
        paramWrap.put("followedCounter", ObjectWrapper.DEFAULT_WRAPPER.wrap(outBean.get("followedCounter")));
        paramWrap.put("askCount", ObjectWrapper.DEFAULT_WRAPPER.wrap(outBean.get("askCount")));
        paramWrap.put("quesFollowCounter", ObjectWrapper.DEFAULT_WRAPPER.wrap(outBean.get("quesFollowCounter")));
        paramWrap.put("categoryCount", ObjectWrapper.DEFAULT_WRAPPER.wrap(outBean.get("categoryCount")));
        paramWrap.put("answerCount", ObjectWrapper.DEFAULT_WRAPPER.wrap(outBean.get("answerCount")));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        if (body != null) {
            body.render(env.getOut());
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
        log.debug(debugName + ":结束");

    }
}
