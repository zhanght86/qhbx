package com.rh.core.comm.zhidao.directive;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
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
 * 
 * @author jason
 *
 */
public class ZhidaoRecommendListDirective implements TemplateDirectiveModel {
    /** log */
    private static Log log = LogFactory.getLog(ZhidaoAskListDirective.class);
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void execute(Environment env, Map params, TemplateModel[] arg2, TemplateDirectiveBody body)
            throws TemplateException, IOException {
       
        String debugName = DirectiveUtils.getString("debugName", params);
        int count = DirectiveUtils.getInt("count", params); 
       
        log.debug(debugName + ":开始");
        
        ParamBean param = new ParamBean();
        param.set("count", count);
        
        OutBean outBean = new ZhidaoServ().getRecommendList(param);
        // 1. 我提出的问题 
        List<Bean> myAskList = (List<Bean>) outBean.get("myAskList");
        // 2. 我关注的问题 
        List<Bean> myQusetFollowList = (List<Bean>) outBean.get("myQusetFollowList");
        // 3. 我关注的人     
        List<Bean> myFollowPersonList = (List<Bean>) outBean.get("myFollowPersonList");
        // 4. 我关注的分类 
        List<Bean> myCategoryList = (List<Bean>) outBean.get("myCategoryList");
        
        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        
        paramWrap.put("myAskList", ObjectWrapper.DEFAULT_WRAPPER.wrap(myAskList));
        paramWrap.put("myQusetFollowList", ObjectWrapper.DEFAULT_WRAPPER.wrap(myQusetFollowList));
        paramWrap.put("myFollowPersonList", ObjectWrapper.DEFAULT_WRAPPER.wrap(myFollowPersonList));
        paramWrap.put("myCategoryList", ObjectWrapper.DEFAULT_WRAPPER.wrap(myCategoryList));
        
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        if (body != null) {
            body.render(env.getOut());
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
        log.debug(debugName + ":结束");
        
    }

}
