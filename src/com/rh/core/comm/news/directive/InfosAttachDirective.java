package com.rh.core.comm.news.directive;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.rh.core.base.Bean;
import com.rh.core.comm.cms.directive.DirectiveUtils;
import com.rh.core.comm.news.mgr.NewsMgr;
import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 宏定义： 1.根据新闻id取得新闻信息、附件信息、图片集
 * 模板：<@news newsId=""  picCount="" commentCount="" attachCount="" pollCount="">
 * 不需要的属性可以不写
 * @author zhanghailong 
 */
public class InfosAttachDirective implements TemplateDirectiveModel {

    /** log */
    private static Log log = LogFactory.getLog(InfosAttachDirective.class);
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void execute(Environment env, Map params,
            TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String debugName = DirectiveUtils.getString("debugName", params);
        String newsId = DirectiveUtils.getString("newsId", params);
        String picCount = DirectiveUtils.getString("picCount", params);
        String attachCount = DirectiveUtils.getString("attachCount", params);
        String pollCount = DirectiveUtils.getString("pollCount", params);
        String servid = DirectiveUtils.getString("servid", params);
        String auditid = DirectiveUtils.getString("auditid", params);
        log.debug(debugName + ":开始");
        
        Bean paramBean = new Bean()
            .set("newsId", newsId).set("picCount", picCount)
            .set("attachCount", attachCount).set("pollCount", pollCount).set("servid", servid)
            .set("auditid", auditid);
        
        Bean news = NewsMgr.getInstance().getInfosAttach(paramBean);
            

        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        paramWrap.put("tag", ObjectWrapper.DEFAULT_WRAPPER.wrap(news));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        if (body != null) {
            body.render(env.getOut());
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
        log.debug(debugName + ":结束");

    }
}
