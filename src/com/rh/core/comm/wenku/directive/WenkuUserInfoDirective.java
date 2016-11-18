package com.rh.core.comm.wenku.directive;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.comm.cms.directive.DirectiveUtils;
import com.rh.core.comm.wenku.WenkuServ;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.ParamBean;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 
 * @author Administrator
 *
 */
public class WenkuUserInfoDirective implements TemplateDirectiveModel {

    /** log */
    private static Log log = LogFactory.getLog(WenkuUserInfoDirective.class);

    /**
     * 
     * @param env 环境变量
     * @param params 参数
     * @param loopVars 模版参数
     * @param body 信息体
     * @throws TemplateException 模版例外
     * @throws IOException IO例外
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String debugName = DirectiveUtils.getString("debugName", params);
        String userId = DirectiveUtils.getString("userId", params);
        // 获取用户信息
        UserBean user = UserMgr.getUser(userId);

        WenkuServ wenkuServ = new WenkuServ();
        
        ParamBean userQuery = new ParamBean().set("userId", user.getCode());
        // 获取该用户的文档数量
        int publicDocCount = wenkuServ.getUsersPubDocumentCounter(userQuery).get("PUB_DOC_COUNT", 0);
        
        // 获取文档下载量
        int downloadCount = wenkuServ.getUserDocsSumDownload(userQuery).get("DOCUMENT_DOWNLOAD_COUNTER", 0);

        // 获取该用户的文辑数量
        int doclistCount = wenkuServ.getUsersPubDoclistCounter(userQuery).get("DOCLIST_COUNT", 0);

        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        paramWrap.put("publicDocCount", ObjectWrapper.DEFAULT_WRAPPER.wrap(publicDocCount));
        paramWrap.put("doclistCount", ObjectWrapper.DEFAULT_WRAPPER.wrap(doclistCount));
        paramWrap.put("downloadCount", ObjectWrapper.DEFAULT_WRAPPER.wrap(downloadCount));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        if (body != null) {
            body.render(env.getOut());
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
        log.debug(debugName + ":结束");
    }

}
