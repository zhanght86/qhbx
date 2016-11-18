/*
 * Copyright (c) 2012 Ruaho All rights reserved.
 */
package com.rh.core.comm.cms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.comm.cms.mgr.ChannelMgr;
import com.rh.core.comm.cms.mgr.TemplateMgr;
import com.rh.core.comm.news.mgr.NewsMgr;
import com.rh.core.comm.news.serv.NewsServ;
import com.rh.core.comm.wenku.WenkuServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.DateUtils;

/**
 * Cms响应Servlet
 * @deprecated
 */
public class CmsInfosServlet extends HttpServlet {

    /** log */
    private static Log log = LogFactory.getLog(CmsInfosServlet.class);
    /** UID */
    private static final long serialVersionUID = 1L;
    /** default channel uri pattern example: http://localhost:8080/cms/content/sjdh33242.html */
    private static final String CHANNEL_URI_PATTERN = "/channel";
    private static final String CONTENT_URI_PATTERN = "/content";
    private static final String SITE_URI_PATTERN = "/site";

    /** wenku doclist uri pattern example: http://localhost:8080/wenku/doclist/sjdh33242.html */
    private static final String DOCLIST_URI_PATTERN = "/doclist";

    /** cms template uri pattern example: http://localhost:8080/cms/template/xxxss.html */
    private static final String TMPL_URI_PATTERN = "/tmpl";

    /** channel service id */
    private static final String SITE_SERV = ServMgr.SY_COMM_CMS_SITE;
    /** channel service id */
    private static final String CHANNEL_SERV = ServMgr.SY_COMM_INFOS_CHNL;
    /** news service id */
    private static final String NEWS_SERV = ServMgr.SY_COMM_INFOS;
    /** bbs service id */
    private static final String BBS_SERV = "SY_COMM_BBS";
    /** zhidao service id */
    private static final String ZHIDAO_SERV = "SY_COMM_ZHIDAO";
    /** wenku service id */
    private static final String WENKU_SERV = "SY_COMM_WENKU"; // ServMgr.SY_COMM_CMS_WENKU;

    /** 静态文件存储路径 */
    private static final String STATIC_FILE_ROOT_PATH = System.getProperty("java.io.tmpdir");
    /** 缓存文件机制是否开启 */
    private static boolean cacheEnable = false;

    /**
     * cms资源访问
     * @param request 请求头
     * @param response 响应头
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        String uri = request.getRequestURI();
        String relativePath = getResource(request);
        String staticFile = getStaticFilePath(request);
        if (!cacheEnable || !hasStatic(request)) {
            long startTime = System.currentTimeMillis();
            // route base uri
            String html = "";
            if (relativePath.startsWith(CHANNEL_URI_PATTERN)) {
                html = buildChannelPage(request, response);
            } else if (relativePath.startsWith(CONTENT_URI_PATTERN)) {
                html = buildContentPage(request, response);
            } else if (relativePath.startsWith(SITE_URI_PATTERN)) {
                html = buildSitePage(request, response);
            } else if (relativePath.startsWith(DOCLIST_URI_PATTERN)) {
            } else if (relativePath.startsWith(TMPL_URI_PATTERN)) {
                html = buildTemplatePage(request, response);
            } else {
                html = buildDoclistPage(request, response);
                System.out.println("not implemnted yet!");
            }
            log.debug(" response " + uri + " html qtime:" + (System.currentTimeMillis() - startTime));
            // 创建静态文件
            createStaticFile(staticFile, html);
        }
        InputStream src = new FileInputStream(staticFile);
        IOUtils.copy(src, response.getOutputStream());
        IOUtils.closeQuietly(response.getOutputStream());
        IOUtils.closeQuietly(response.getOutputStream());
    }

    /**
     * 是否已静态化
     * @param request - httprequest
     * @return 是否已静态化
     */
    private boolean hasStatic(HttpServletRequest request) {
        String staticFile = getStaticFilePath(request);
        return new File(staticFile).exists();
    }

    /**
     * 根据当前请求URL获取静态文件保存路径
     * @param request - http request
     * @return 文件路径
     */
    private String getStaticFilePath(HttpServletRequest request) {
        String uri = request.getRequestURI().substring(1);
        String staticFile = "";
        if (STATIC_FILE_ROOT_PATH.endsWith(File.separator)) {
            staticFile = STATIC_FILE_ROOT_PATH.substring(0, STATIC_FILE_ROOT_PATH.length() - 1) + uri;
        } else {
            staticFile = STATIC_FILE_ROOT_PATH + uri;
        }
//        staticFile = FileMgr.buildPathExpr(NEWS_SERV, uri); 
        return staticFile;
    }

    /**
     * 站点访问
     * @param request 请求头
     * @param response 响应头
     * @throws ServletException ServletException
     * @throws IOException IOException
     * @return html string
     */
    private String buildSitePage(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String resource = getResource(request);
        String siteId = resource.substring(SITE_URI_PATTERN.length());
        // example:http://localhost:8080/cms/site/xxxxxxxx.html
        // get page info
        String preTag = "index_";
        String postTag = ".html";
        int psi = siteId.indexOf(preTag);
        int pei = siteId.indexOf(postTag);
        int page = 1;
        int start = psi + preTag.length();
        if (-1 < psi && -1 < pei && start != pei) {
            String pageStr = siteId.substring(start, pei);
            page = Integer.valueOf(pageStr);
        }
        siteId = siteId.replace(preTag + page + postTag, "");
        siteId = siteId.replace("index.html", "");
        if (siteId.startsWith("/")) {
            siteId = siteId.substring(1);
        }
        if (siteId.endsWith("/")) {
            siteId = siteId.substring(0, siteId.length() - 1);
        }
        Bean siteBean = ServDao.find(SITE_SERV, new Bean().setId(siteId));
        // response 404
        if (null == siteBean || siteBean.isEmpty()) {
            log.warn("Could not get the site:" + siteId);
            response.setStatus(404);
            response.sendError(404);
            String errorHtml = "<html>" + "<br><br><br>Could not get the site:" + siteId + "</html>";
            return errorHtml;
        }
        String tmplId = "";
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("site", siteBean);
        tmplId = siteBean.getStr("TMPL_ID");
        StringWriter writer = new StringWriter();
        TemplateMgr.getInstance().buildHtml(tmplId, root, writer);
        String html = writer.toString();
        IOUtils.closeQuietly(writer);
        return html;
    }

    /**
     * 模版访问
     * @param request 请求头
     * @param response 响应头
     * @throws ServletException ServletException
     * @throws IOException IOException
     * @return html string
     */
    @SuppressWarnings("unchecked")
    private String buildTemplatePage(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String resource = getResource(request);
        String tmplId = resource.substring(TMPL_URI_PATTERN.length());
        if (tmplId.startsWith("/")) {
            tmplId = tmplId.substring(1);
        }
        if (tmplId.endsWith("/")) {
            tmplId = tmplId.substring(0, tmplId.length() - 1);
        }
        tmplId = tmplId.replace(".html", "");
        Map<String, Object> root = new HashMap<String, Object>();

        // 以形如CHNL_ID=123&SITE_ID=234,参数回传页面
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            String value = request.getParameter(name);
            if (!value.isEmpty()) {
                root.put(name, value);
            }
        }
        StringWriter writer = new StringWriter();
        TemplateMgr.getInstance().buildHtml(tmplId, root, writer);
        String html = writer.toString();
        IOUtils.closeQuietly(writer);
        return html;
    }

    /**
     * 栏目访问
     * @param request 请求头
     * @param response 响应头
     * @throws ServletException ServletException
     * @throws IOException IOException
     * @return html string
     *TODO:用宏获取数据,servlet 只负责数据与模版的对应
     */
    @SuppressWarnings("unchecked")
    private String buildChannelPage(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String resource = getResource(request);
        String channelId = resource.substring(CHANNEL_URI_PATTERN.length());
        int count = 10;
        String countStr = request.getParameter("count");
        if (null != countStr && countStr.length() > 0) {
            count = Integer.valueOf(countStr);
        }
        String order = request.getParameter("order");
        // example:http://localhost:8080/news/channel/TEST_3/index.html
        // get page info
        String preTag = "index_";
        String postTag = ".html";
        int psi = channelId.indexOf(preTag);
        int pei = channelId.indexOf(postTag);
        int page = 1;
        int start = psi + preTag.length();
        if (-1 < psi && -1 < pei && start != pei) {
            String pageStr = channelId.substring(start, pei);
            page = Integer.valueOf(pageStr);
        }
        channelId = channelId.replace(preTag + page + postTag, "");
        channelId = channelId.replace("index.html", "");
        if (channelId.startsWith("/")) {
            channelId = channelId.substring(1);
        }
        if (channelId.endsWith("/")) {
            channelId = channelId.substring(0, channelId.length() - 1);
        }
        // Bean chnlBean = ServDao.find(CHANNEL_SERV, new Bean().setId(channelId));
        Bean chnlBean = ChannelMgr.getInstance().getChannel(channelId);
        // response 404
        if (null == chnlBean || chnlBean.isEmpty()) {
            log.warn("Could not get the channel:" + channelId);

            String errorHtml = "<html>" + "<br><br><br>Could not get the channel:" + channelId + "</html>";
            return errorHtml;
        }

        String tmplId = "";
        Map<String, Object> root = new HashMap<String, Object>();
        // 以形如CHNL_ID=123&SITE_ID=234,参数回传页面
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            String value = request.getParameter(name);
            if (!value.isEmpty()) {
                root.put(name, value);
            }
        }

        root.put("chanel", chnlBean);
        String chnlId = chnlBean.getId();
        // show the news data 1:列表式，2:封面式
        if (1 == chnlBean.get("CHNL_TMPL_DISMODEL", 1)) {
            tmplId = chnlBean.getStr("CHNL_LIST_TMPL");
            ParamBean queryBean = new ParamBean().setQueryPageNowPage(page).setQueryPageShowNum(count);
            if (null != order && order.length() > 0) {
                queryBean.setOrder(order);
            }
            String service = "";
            // 判断是新闻还是文档
            if (WENKU_SERV.equals(chnlBean.getStr("SERV_ID"))) {
                // queryBean.set("DOCUMENT_CHNL", chnlId);
                service = ServMgr.SY_COMM_WENKU_DOCUMENT;
                queryBean.setQueryLinkWhere(" AND DOCUMENT_CHNL='" + chnlId + "' ");
                queryBean.setOrder("S_CTIME DESC");
            } else if (NEWS_SERV.equals(chnlBean.getStr("SERV_ID"))) {
                // queryBean.set("CHNL_ID", chnlId);
                service = ServMgr.SY_COMM_NEWS;
                queryBean.setQueryLinkWhere(" AND CHNL_ID='" + chnlId + "' ");
            } else if (BBS_SERV.equals(chnlBean.getStr("SERV_ID"))) {
                service = ServMgr.SY_COMM_BBS_TOPIC;
                queryBean.setQueryLinkWhere(" AND CHNL_ID='" + chnlId + "' ");
                root.put("CHNL_ID", chnlId);
                chnlBean = ServDao.find(CHANNEL_SERV, new Bean().setId(chnlBean.getStr("CHNL_PID")));
                root.put("channel", chnlBean);
            } else if (ZHIDAO_SERV.equals(chnlBean.getStr("SERV_ID"))) {
                service = ServMgr.SY_COMM_ZHIDAO_QUESTION;
                queryBean.setOrder(" S_MTIME DESC ");
                List<Bean> treeWhere = new ArrayList<Bean>();
                treeWhere.add(new Bean().set("DICT_ITEM", "CHNL_ID").set("DICT_VALUE", chnlId));
                queryBean.set("_treeWhere", treeWhere);
            } else {
                service = ServMgr.SY_COMM_CMS_CHNL;
            }

            OutBean outBean = ServMgr.act(service, ServMgr.ACT_QUERY, queryBean);

            // 格式化 时间
            List<Bean> list = outBean.getDataList();
            for (Bean b : list) {
                String timeStr = b.getStr("S_CTIME");
                if (0 == timeStr.length()) {
                    continue;
                }
                Date d = DateUtils.getDateFromString(timeStr);
                String timeAgo = DateUtils.timeAgo(d);
                b.set("S_CTIME_TIMEAGO", timeAgo);
            }

            root.put("_DATA_", list);
            root.put("_PAGE_", outBean.getPage());
            root.put("page", page);

        } else {
            tmplId = chnlBean.getStr("CHNL_TMPL");
        }
        StringWriter writer = new StringWriter();
        TemplateMgr.getInstance().buildHtml(tmplId, root, writer);
        String html = writer.toString();
        IOUtils.closeQuietly(writer);
        return html;
    }

    /**
     * 内容访问
     * @param request 请求头
     * @param response 响应头
     * @throws ServletException ServletException
     * @throws IOException IOException
     * @return html string
     */
    @SuppressWarnings("unchecked")
    private String buildContentPage(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String resource = getResource(request);
        String id = resource.substring(CONTENT_URI_PATTERN.length());
        if (id.startsWith("/")) {
            id = id.substring(1);
        }
        if (id.endsWith("/")) {
            id = id.substring(0, id.length() - 1);
        }
        id = id.replace(".html", "");

        String service = request.getServletPath();
        Map<String, Object> root = new HashMap<String, Object>();

        // 以形如CHNL_ID=123&SITE_ID=234,参数回传页面
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            String value = request.getParameter(name);
            if (!value.isEmpty()) {
                root.put(name, value);
            }
        }

        String tmplId = "";
        if (-1 < service.lastIndexOf("infos")) {
            Bean news = new NewsServ().byid(new ParamBean(ServMgr.SY_COMM_INFOS).setId(id));
            Bean channel = ServDao.find(CHANNEL_SERV, new Bean().set("CHNL_ID", news.getStr("CHNL_ID")));
            tmplId = news.getStr("TMPL_ID");
            Bean counter = NewsMgr.getInstance().increaseReadCounter(new Bean(id));
            root.put("news", news);
            root.put("channel", channel);
            root.put("counter", counter);
           
        } else if (-1 < service.lastIndexOf("checks")) {
            
            Bean news = new NewsServ().byid(new ParamBean(ServMgr.SY_COMM_INFOS_AUDIT).setId(id));
            Bean channel = ServDao.find(CHANNEL_SERV, new Bean().set("CHNL_ID", news.getStr("CHNL_ID")));
            tmplId = news.getStr("TMPL_ID");
           // Bean counter = NewsMgr.getInstance().increaseReadCounter(new Bean(id));
            root.put("news", news);
            root.put("channel", channel);
            //root.put("counter", counter);
            
        } else if (-1 < service.lastIndexOf("wenku")) {
            // Bean document = WenkuMgr.getInstance().getDocument(id);
            Bean document = new WenkuServ().byid(new ParamBean(ServMgr.SY_COMM_WENKU_DOCUMENT).setId(id));
            String file = document.getStr("DOCUMENT_FILE");
            if (file.contains(",")) {
                file = file.substring(0, file.indexOf(","));
            }
            document.set("DOCUMENT_FILE", file);
            tmplId = document.getStr("TMPL_ID");
            String chnId = document.getStr("DOCUMENT_CHNL");
            Bean channel = ServDao.find(CHANNEL_SERV, new Bean().set("CHNL_ID", chnId));

            // 加上格式是否支持标记
            root.put("isSupport", isSupport(document.getStr("DOCUMENT_FILE_SUFFIX")));
            root.put("document", document);
            root.put("channel", channel);
        } else if (-1 < service.lastIndexOf("bbs")) {
            Bean topic = new NewsServ().byid(new ParamBean(ServMgr.SY_COMM_BBS_TOPIC).setId(id));
            Bean channel = ServDao.find(CHANNEL_SERV, new Bean().set("CHNL_ID", topic.getStr("CHNL_ID")));
            tmplId = topic.getStr("TMPL_ID");
            root.put("topic", topic);
            root.put("channel", channel);
        } else {
            log.error(service + "not implemented!");
            return "";
        }

        StringWriter writer = new StringWriter();
        TemplateMgr.getInstance().buildHtml(tmplId, root, writer);
        String html = writer.toString();
        IOUtils.closeQuietly(writer);
        return html;
    }

    /**
     * 文辑访问
     * @param request 请求头
     * @param response 响应头
     * @throws ServletException ServletException
     * @throws IOException IOException
     * @return html string
     */
    private String buildDoclistPage(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String resource = getResource(request);
        String id = resource.substring(DOCLIST_URI_PATTERN.length());
        String preTag = "index_";
        String postTag = ".html";
        int psi = id.indexOf(preTag);
        int pei = id.indexOf(postTag);
        int page = 1;
        int start = psi + preTag.length();
        if (-1 < psi && -1 < pei && start != pei) {
            String pageStr = id.substring(start, pei);
            page = Integer.valueOf(pageStr);
        }
        id = id.replace(preTag + page + postTag, "");
        id = id.replace("index.html", "");
        if (id.startsWith("/")) {
            id = id.substring(1);
        }
        if (id.endsWith("/")) {
            id = id.substring(0, id.length() - 1);
        }
        // 0.根据id取得文辑信息
        Bean channel = ServDao.find("SY_COMM_WENKU_DOCLIST", id);
        // 1.对指定文辑下文档分页取得页信息和文档的id； 2.根据id序列取得文档数据，加上页信息后返回
        ParamBean queryBean = new ParamBean("SY_COMM_WENKU_DOCLIST_ITEM", ServMgr.ACT_QUERY)
                .setQueryPageNowPage(page).setQueryPageShowNum(10)
                .setSelect("DOCUMENT_ID")
                .setQueryLinkWhere(" and LIST_ID='" + id + "'");
        OutBean rtnBean = ServMgr.act(queryBean);
        StringBuilder ids = new StringBuilder("(");
        List<Bean> dataList = rtnBean.getDataList();
        for (Object o : dataList) {
            Bean b = (Bean) o;
            ids.append("'" + b.getId() + "',");
        }
        ids.deleteCharAt(ids.length() - 1);
        ids.append(")");

        List<Bean> docList = ServDao.finds(ServMgr.SY_COMM_WENKU_DOCUMENT,
                " and DOCUMENT_STATUS=2 and DOCUMENT_ID in " + ids);
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("channel", channel);
        root.put("_PAGE_", rtnBean.getBean("_PAGE_"));
        root.put("_DATA_", docList);
        String tmplId = "";
        Bean tmplBean = ServDao.find(ServMgr.SY_COMM_WENKU_TMPL,
                new Bean().set("TMPL_TYPE", "doclist").set("SERV_ID", WENKU_SERV));
        tmplId = tmplBean.getId();

        System.out.println("----debug------doclist id:" + id);
        System.out.println("----debug------tmpl id:" + tmplId);

        StringWriter writer = new StringWriter();
        TemplateMgr.getInstance().buildHtml(tmplId, root, writer);
        String html = writer.toString();
        IOUtils.closeQuietly(writer);
        return html;
    }

    /**
     * get resource from request uri
     * @param request - HttpRequest
     * @return resource string
     * @throws UnsupportedEncodingException - url decode exception
     */
    private String getResource(HttpServletRequest request) throws UnsupportedEncodingException {
        String uri = request.getRequestURI();
        uri = URLDecoder.decode(uri, "UTF-8");
        String servlet = request.getServletPath();
        int index = uri.indexOf(servlet);
        String relativePath = uri.substring(index + servlet.length());
        if (!relativePath.startsWith("/")) {
            relativePath = "";
        }
        return relativePath;
    }

    /**
     * 生成静态文件
     * @param path - 文件保存路径
     * @param content - 静态内容
     * @throws IOException - io exception
     */
    private void createStaticFile(String path, String content) throws IOException {
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
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(
//                content.getBytes("UTF-8"));
//        String servId = NEWS_SERV;
//        String mtype = "text/html";
//        String name = path.substring(path.lastIndexOf("/"));
//        String fileId = name.substring(1, name.lastIndexOf("."));
//        String category = NEWS_SERV;
//        TempFile tmp = null;
//        try {
//            long start = System.currentTimeMillis();
//            // save inputstream data to Temporary storage
//            tmp = new TempFile(Storage.SMART, inputStream);
//            tmp.read();
//            IOUtils.closeQuietly(inputStream);
//            log.debug(" read inputstream to temp storage qtime:" + (System.currentTimeMillis() - start));
//            start = System.currentTimeMillis();
//
//            InputStream is1 = tmp.openNewInputStream();
//            // extract file md5 checksum
////            checksum = Lang.getMd5checksum(is1);
//            IOUtils.closeQuietly(is1);
//            
//            start = System.currentTimeMillis();
//            InputStream is2 = tmp.openNewInputStream();
//            long size = FileStorage.saveFile(is2, path);
//            IOUtils.closeQuietly(is2);
//            log.debug(" save file to storage qtime:" + (System.currentTimeMillis() - start));
////            // get the real size
////            fileParam.set("FILE_SIZE", size);
//            
//        } catch (IOException ioe) {
//            log.error(" file upload error.", ioe);
//            throw new RuntimeException("file upload error.", ioe);
//        } finally {
//            tmp.destroy();
//        }

        log.debug("成功创建静态文件:" + path);
    }

    /**
     * @param suffix :要判断的字符串
     * @return support :返回格式是否支持
     */
    private boolean isSupport(String suffix) {
        boolean support = false;
        String allSuffix = "mp3.flv.doc.docx.ppt.pptx.xls.xlsx.vsd.pot.pps.rtf.wps.et.dps.pdf.txt.epub";
        if (suffix == null || suffix.length() == 0) {
            return support;
        }
        if (allSuffix.contains(suffix)) {
            support = true;
        }
        return support;
    }
}
