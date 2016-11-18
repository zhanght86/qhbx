package com.rh.core.comm.zhidao.directive;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.comm.cms.directive.DirectiveUtils;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 获取最新知道提问
 * @author liwei
 * 
 */
public class ZhidaoHotListDirective implements TemplateDirectiveModel {
    /** log */
    private static Log log = LogFactory.getLog(ZhidaoHotListDirective.class);

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void execute(Environment env, Map params,
            TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String debugName = DirectiveUtils.getString("debugName", params);
        int count = DirectiveUtils.getInt("count", params);

        log.debug(debugName + ":开始");

        ParamBean param = new ParamBean(ServMgr.SY_COMM_ZHIDAO_QUESTION, ServMgr.ACT_FINDS)
                .setOrder("Q_ANSWER_COUNTER desc");
        if (count > 0) {
            param.setShowNum(count);
        } else {
            param.setShowNum(15);
        }
        List<Bean> askList = ServMgr.act(param).getDataList();
        
        
        //set answer, answer user
        for (Bean ask : askList) {
            ParamBean answQuery = new ParamBean(ServMgr.SY_COMM_ZHIDAO_ANSWER, ServMgr.ACT_FINDS);
            answQuery.set("Q_ID", ask.getId());
            answQuery.setOrder(" A_BEST DESC");
            answQuery.setShowNum(1);
            List<Bean> answList =  ServMgr.act(answQuery).getDataList();
            if (null != answList && 0 < answList.size()) {
                Bean bestAnsw = answList.get(0);
                ask.set("ANSWER", bestAnsw);
                Bean user = UserMgr.getUser(bestAnsw.getStr("S_USER"));
                ask.set("ANSWER_USER", user);
            } else {
                ask.set("ANSWER", new Bean()); 
                ask.set("ANSWER_USER", new Bean());
            }
        }
        

        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        paramWrap.put("tag_list", ObjectWrapper.DEFAULT_WRAPPER.wrap(askList));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        if (body != null) {
            body.render(env.getOut());
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
        log.debug(debugName + ":结束");

    }
}
