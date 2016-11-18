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
 * @author AoDanni
 * 我的收藏列表
 */
public class ZhidaoFavoritesListDirective implements TemplateDirectiveModel{
	/** log */
    private static Log log = LogFactory.getLog(ZhidaoFavoritesListDirective.class);
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void execute(Environment env, Map params,
            TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String debugName = DirectiveUtils.getString("debugName", params);
        int page = DirectiveUtils.getInt("page", params);
        String userId = DirectiveUtils.getString("userId", params);
        int count = DirectiveUtils.getInt("count", params); 
        String order = DirectiveUtils.getString("order", params);
        log.debug(debugName + ":开始");
        ParamBean query = new ParamBean();
        query.set("page", page);
        query.set("userId", userId);
        query.set("count", count);
        query.set("order", order);
        OutBean outBean = new ZhidaoServ().getFavoritesList(query);
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
