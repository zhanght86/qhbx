package com.rh.core.comm.cms;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.rh.core.base.Bean;
import com.rh.core.comm.cms.mgr.TemplateMgr;
import com.rh.core.serv.ServDao;

/**
 * 默认cms处理
 * 
 * @author liwei
 * 
 */
public class DefaultCmsServletHandler implements CmsServletHandler {

   // private static Log log = LogFactory.getLog(DefaultCmsServletHandler.class);

    @Override
    public String doGet(HttpServletRequest request, Bean data, HttpServletResponse response) throws ServletException,
            IOException {
        // get param
        String relativePath = CmsHelper.getResource(request);
        Bean param = CmsHelper.parseUri(relativePath);
        int page = CmsHelper.getPage(param);
        String servId = CmsHelper.getServId(param);
        String dataId = CmsHelper.getDataId(param);

        // get template id
        String tmpl = data.getStr("TMPL_ID");
        if (null == tmpl || 0 == tmpl.length()) {
            CmsHelper.buildErrorPage(request, response, "template not found, from data:" + data.getId());
        }

        // response result
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("servId", servId);
        result.put("dataId", dataId); 
        result.put("data", data);
        result.put("page", page);
        setParameterNames(request, result);
     
        String html = buildTemplatePage(tmpl, result);

        return html;
    }

    /**
     * put http request's parameters in the root map
     * @param request - request
     * @param root - map
     */
    @SuppressWarnings("unchecked")
    protected void setParameterNames(HttpServletRequest request, Map<String, Object> root) {
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            String value = request.getParameter(name);
            if (!value.isEmpty()) {
                root.put(name, value);
            }
        }
    }

    /**
     * 模版访问
     * @param tmplId - template id
     * @param params - map params
     * @return html string
     */
    protected String buildTemplatePage(String tmplId, Map<String, Object> params) {
        Map<String, Object> root = new HashMap<String, Object>();

        if (null != params) {
            root.putAll(params);
        }
        //信息手机模版
        if (params.containsKey("ftl_name")) { 
        	String tmplpath  = params.get("ftl_name").toString();
        	String where = " AND TMPL_PATH='"+tmplpath+"'";
        	List<Bean> tmplList= ServDao.finds("SY_COMM_CMS_TMPL", where);
        	if(tmplList.size()==1){
        		for(Bean bean : tmplList){
        			tmplId = bean.getId();
        		}
        	} else {
        		throw new RuntimeException("信息手机模版配置有错误！");
        	} 	
        }
        StringWriter writer = new StringWriter();
        TemplateMgr.getInstance().buildHtml(tmplId, root, writer);
        String html = writer.toString();
        IOUtils.closeQuietly(writer);
        return html;
    }

}
