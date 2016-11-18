package com.rh.core.comm.wenku.directive;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.comm.cms.directive.DirectiveUtils;
import com.rh.core.comm.wenku.WenkuServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * @author zhangjinxi 获取跟当前文档相关的文辑列表
 */
public class DoclistListAboutDocDirective implements TemplateDirectiveModel {
    /** log */
    private static Log log = LogFactory.getLog(DoclistListAboutDocDirective.class);

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void execute(Environment env, Map params,
            TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String debugName = DirectiveUtils.getString("debugName", params);
        int count = DirectiveUtils.getInt("count", params);
        String docId = DirectiveUtils.getString("docId", params);

        log.debug(debugName + ":开始");

        ParamBean paramBean = new ParamBean();
        paramBean.set("docId", docId);
        paramBean.set("count", count);
        OutBean outBean = new WenkuServ().getReferenceWenji(paramBean);
        List<Bean> wenjiList = outBean.getDataList();
        Bean pageBean = outBean.getPage();
        
        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        paramWrap.put("tag_list", ObjectWrapper.DEFAULT_WRAPPER.wrap(wenjiList));
        paramWrap.put("_PAGE_", ObjectWrapper.DEFAULT_WRAPPER.wrap(pageBean));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        if (body != null) {
            body.render(env.getOut());
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
        log.debug(debugName + ":结束");

    }
}
