/*
 * Copyright (c) 2012 Ruaho All rights reserved.
 */
package com.rh.core.comm.cms;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;

/**
 * Cms响应Servlet
 * @author liwei
 */
public class CmsServlet extends HttpServlet {
    
    /** cms page request servers */
  //  public static final String CMS_PAGE_REFERENCE_SERVS = "CMS_PAGE_REFERENCE_SERVS";

    /** log */
    private static Log log = LogFactory.getLog(CmsServlet.class);
    /** UID */
    private static final long serialVersionUID = 1L;

    /** 缓存文件存储路径 */
    private static final String CACHE_FILE_ROOT_PATH = System.getProperty("java.io.tmpdir");
    
    private static CmsServletHandler  defaultCmsHandler = new DefaultCmsServletHandler();
    
    private static Map<String, CmsServletHandler> cmsHandlerMap = new HashMap<String, CmsServletHandler>();
    
    //指定服务的handler
    static {
        cmsHandlerMap.put("SY_COMM_INFOS", new NewsCmsServletHandler());
    }

    /**
     * cms url format1: http://${host}/cms/${servId}/${dataId}/index_${page}.html <br>
     * format2: http://${host}/cms/${servId}/${dataId}.html (default page=1) <br>
     * 
     * cms资源访问
     * @param request 请求头
     * @param response 响应头
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        Context.setRequest(request);
        
        String uri = request.getRequestURI();
        String relativePath = CmsHelper.getResource(request);
//        String staticFile = getCacheFilePath(request);
//        
//        if (cacheEnable()  && cached(request)) {
//            InputStream src = new FileInputStream(staticFile);
//            IOUtils.copy(src, response.getOutputStream());
//            IOUtils.closeQuietly(src);
//            IOUtils.closeQuietly(response.getOutputStream());
//        } else {
            
            long startTime = System.currentTimeMillis();
            // parse relativePath
            // url format /cms/${servId}/${dataId}/index_${page}.html
            Bean param = CmsHelper.parseUri(relativePath);
            if (null == param || param.isEmpty()) {
                CmsHelper.buildErrorPage(request, response, "uri parse error:" + relativePath);
            }

            String servId = CmsHelper.getServId(param);
            String dataId = CmsHelper.getDataId(param);
            int page = CmsHelper.getPage(param);

            log.debug(" cms view, servId:" + servId + "\t dataId:" + dataId + "\t" + "page:" + page);

            // get data
            ParamBean queryBean = new ParamBean(servId, ServMgr.ACT_BYID);
            queryBean.setId(dataId);
            Bean data = ServMgr.act(queryBean);
            data.set("servId", servId);
          
            //使用指定cmshandler
            CmsServletHandler targetHandler = cmsHandlerMap.get(servId);
            if (null == targetHandler) {
                targetHandler = defaultCmsHandler;
            }
            
            String html = targetHandler.doGet(request, data, response);
            
            //创建缓存文件 用于临时cache
//            createCacheFile(staticFile, html);
            log.debug(" response " + uri + " html qtime:" + (System.currentTimeMillis() - startTime));
            
            response.getOutputStream().write(html.getBytes("UTF-8"));
            IOUtils.closeQuietly(response.getOutputStream());
            
            
//        }
    
    }
    
    /**
     * 是否已缓存化(缓存)
     * @param request - httprequest
     * @return 是否已缓存化
     */
    protected boolean cached(HttpServletRequest request) {
        String staticFile = getCacheFilePath(request);
        return new File(staticFile).exists();
    }

    /**
     * 根据当前请求URL获取缓存文件保存路径
     * @param request - http request
     * @return 文件路径
     */
    protected String getCacheFilePath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String staticFile = "";
        if (CACHE_FILE_ROOT_PATH.endsWith(File.separator)) {
            staticFile = CACHE_FILE_ROOT_PATH.substring(0, CACHE_FILE_ROOT_PATH.length() - 1) + uri;
        } else {
            staticFile = CACHE_FILE_ROOT_PATH + uri;
        }
        return staticFile;
    }

 
    /**
     * 生成缓存文件
     * @param path - 文件保存路径
     * @param content - 内容
     * @throws IOException - io exception
     */
    protected void createCacheFile(String path, String content) throws IOException {
        File file = new File(path);

        if (file.getParentFile().isFile()) {
            file.getParentFile().delete();
        }

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            file.createNewFile();
        }
        OutputStream out = new FileOutputStream(file);
        out.write(content.getBytes("UTF-8"));
        IOUtils.closeQuietly(out);
        log.debug("成功创建缓存文件:" + path);
      
     // 获取该页面数据所涉及的服务   
     //   List<String> referenceServ = (List<String>) Context.getThread(CMS_PAGE_REFERENCE_SERVS);
    }

    /**
     * @param suffix :要判断的字符串
     * @return support :返回格式是否支持
     */
//    private boolean isSupport(String suffix) {
//        boolean support = false;
//        String allSuffix = "mp3.flv.doc.docx.ppt.pptx.xls.xlsx.vsd.pot.pps.rtf.wps.et.dps.pdf.txt.epub";
//        if (suffix == null || suffix.length() == 0) {
//            return support;
//        }
//        if (allSuffix.contains(suffix)) {
//            support = true;
//        }
//        return support;
//    }
}
