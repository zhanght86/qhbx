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
 * @author liwei
 * 提问列表
 */
public class ZhidaoAskListDirective implements TemplateDirectiveModel {
    /** log */
    private static Log log = LogFactory.getLog(ZhidaoAskListDirective.class);
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void execute(Environment env, Map params,
            TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String debugName = DirectiveUtils.getString("debugName", params);
        String chnlId = DirectiveUtils.getString("chnlId", params);
        int page = DirectiveUtils.getInt("page", params);
        String userId = DirectiveUtils.getString("userId", params);
        int count = DirectiveUtils.getInt("count", params); 
        
        //查询的是否是零回答的数据
        String noAnswer = DirectiveUtils.getString("noAnswer", params);
        
        if (0 == page) {
            page = 1;
        }
        
        log.debug(debugName + ":开始");
        ParamBean param = new ParamBean();
        param.set("chnlId", chnlId);
        param.set("page", page);
        param.set("count", count);
        param.set("userId", userId);
        param.set("noAnswer", noAnswer);
        OutBean outBean = new ZhidaoServ().getAskList(param);
        
        List<Bean> dataList = outBean.getDataList();
        Bean pageBean = outBean.getPage();
        
        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        paramWrap.put("tag_list", ObjectWrapper.DEFAULT_WRAPPER.wrap(dataList));
        paramWrap.put("_PAGE_", ObjectWrapper.DEFAULT_WRAPPER.wrap(pageBean));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        if (body != null) {
            body.render(env.getOut());
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
        log.debug(debugName + ":结束");

    }
}
