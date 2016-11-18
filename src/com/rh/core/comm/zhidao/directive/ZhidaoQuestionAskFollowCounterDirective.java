package com.rh.core.comm.zhidao.directive;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.comm.cms.directive.DirectiveUtils;
import com.rh.core.comm.zhidao.ZhidaoServ;
import com.rh.core.serv.ParamBean;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 获取一个人的提问数和回答数和收藏数
 * @author zhangjinxi
 * 
 */
public class ZhidaoQuestionAskFollowCounterDirective implements TemplateDirectiveModel {
    /** log */
    private static Log log = LogFactory.getLog(ZhidaoQuestionAskFollowCounterDirective.class);

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void execute(Environment env, Map params,
            TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String debugName = DirectiveUtils.getString("debugName", params);
        String userId = DirectiveUtils.getString("userId", params);
        
        log.debug(debugName + ":开始");
        
        ParamBean userQuery = new ParamBean();
        userQuery.set("userId", userId);
        //统计提问数量
        int askCounter = new ZhidaoServ().getAskCount(userQuery).getCount();
        
        //统计回答数量
        int answerCounter = new ZhidaoServ().getAnswerCount(userQuery).getCount();
        
       //我的收藏的数量
        int favoriteCounter = new ZhidaoServ().getFavoriteCount(userQuery).getCount();
        
        
        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        paramWrap.put("askCounter", ObjectWrapper.DEFAULT_WRAPPER.wrap(askCounter));
        paramWrap.put("answerCounter", ObjectWrapper.DEFAULT_WRAPPER.wrap(answerCounter));
        paramWrap.put("favoriteCounter", ObjectWrapper.DEFAULT_WRAPPER.wrap(favoriteCounter));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        if (body != null) {
            body.render(env.getOut());
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
        log.debug(debugName + ":结束");

    }
}
