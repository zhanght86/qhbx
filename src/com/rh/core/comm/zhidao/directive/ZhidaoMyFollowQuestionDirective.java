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
 * 获取一个用户关注的问题列表
 * 前台调用的方法<@zhidao_my_follow_question debugName="关注的问题列表" userId="" count="5" page="1">
 * @author zhangjinxi
 * 
 */
public class ZhidaoMyFollowQuestionDirective implements TemplateDirectiveModel {
    /** log */
    private static Log log = LogFactory.getLog(ZhidaoMyFollowQuestionDirective.class);

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void execute(Environment env, Map params,
            TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String debugName = DirectiveUtils.getString("debugName", params);
        String userId = DirectiveUtils.getString("userId", params);
        int count = DirectiveUtils.getInt("count", params);
        int page = DirectiveUtils.getInt("page", params);
        
        

        log.debug(debugName + ":开始");

        // 获取当前用户关注的人或者是关注者的信息
        ParamBean query = new ParamBean();
        query.set("debugName", debugName);
        query.set("userId", userId);
        query.set("count", count);
        query.set("page", page);
        OutBean questionBean = new ZhidaoServ().getMyFollowQuestion(query);
        

        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        
        paramWrap.put("questionBean", ObjectWrapper.DEFAULT_WRAPPER.wrap(questionBean));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        if (body != null) {
            body.render(env.getOut());
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
        log.debug(debugName + ":结束");
    }
}
