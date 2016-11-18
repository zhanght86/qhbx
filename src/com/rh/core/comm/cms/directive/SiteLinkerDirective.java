package com.rh.core.comm.cms.directive;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.Constant;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 宏定义： 1.根据站点id取得站点下链接信息
 * @author chaizhiqiang
 */
public class SiteLinkerDirective implements TemplateDirectiveModel {

    /** log */
    private static Log log = LogFactory.getLog(SiteLinkerDirective.class);

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void execute(Environment env, Map params,
            TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        String debugName = DirectiveUtils.getString("debugName", params);
        String siteId = DirectiveUtils.getString("siteId", params);
        String count = DirectiveUtils.getString("count", params);
        String pic = DirectiveUtils.getString("pic", params);

        log.debug(debugName + ":开始");
        Bean bean = new Bean().set("SITE_ID", siteId);

        if (count != null && count.length() > 0) {
            bean.set(Constant.PARAM_ROWNUM, count);
        }

        if (pic.equals("1")) {
            bean.set(Constant.PARAM_WHERE, " and LINKER_IMAGE is not null");
        }
        List<Bean> linkerList = ServDao.finds("SY_COMM_CMS_SITE_LINKER", bean);
        // trim link image url
        for (Bean link : linkerList) {
            String image = link.getStr("LINKER_IMAGE");
            int i = image.lastIndexOf(",");
            if (i > -1) {
                image = image.substring(0, i);
                link.set("LINKER_IMAGE", image);
            }
        }
        Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
        paramWrap.put("tag_list", ObjectWrapper.DEFAULT_WRAPPER.wrap(linkerList));
        Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
        if (body != null) {
            body.render(env.getOut());
        }
        DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);

        log.debug(debugName + ":结束");
    }
}
