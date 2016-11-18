package com.rh.core.comm.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.comm.FileMgr;
import com.rh.core.comm.FileStorage;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.util.IpAddressMatcher;
import com.rh.core.util.JsonUtils;
import com.rh.core.util.RequestUtils;

/**
 * 
 * @author yangjy
 * 
 */
public class HttpFileStorageServlet extends HttpServlet {

    private static final long serialVersionUID = 366236807098963981L;
    /** log */
    private static Log log = LogFactory.getLog(HttpFileStorageServlet.class);

    private static final String IP_ADDRESS = "FILE_STORAGE_IP_ADDRESS";
    private static final String FILE_ITEM_LIST = "FILE_ITEM_LIST";
    private static final String FILE_PATH = "FILE_PATH";
    
    private static final String SERVLET_NAME = "/fileStorage"; 

    private static final String AUTH_SEC = "HTTP_STORAGE_AUTH_SEC";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doPost(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Context.cleanThreadData();
        OutBean out = null;
        String ips = Context.getSyConf(IP_ADDRESS, "");
        if (ips.length() > 0) { // 判断IP地址是否允许访问
            IpAddressMatcher iam = new IpAddressMatcher(ips);
            if (!iam.match(req.getRemoteAddr())) {
                out = new OutBean();
                out.setError("access deny");
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                res.getWriter().print(JsonUtils.toJson(out));
                res.flushBuffer();
                return;
            }
        }

        if (!auth(req)) { // 认证身份
            out = new OutBean();
            out.setError("User authentication failed");
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            res.getWriter().print(JsonUtils.toJson(out));
            res.flushBuffer();
            return;
        }

        ParamBean paramBean = handleFileUpload(req);

        String act = paramBean.getStr("act");

        try {
            if (act.equals("save")) {
                out = save(paramBean);
            } else if (act.equals("createNewFile")) {
                out = createNewFile(paramBean);
            } else if (act.equals("download")) {
                download(res, paramBean);
            } else if (act.equals("list")) {
                out = list(paramBean);
            } else if (act.equals("exists")) {
                out = exists(paramBean);
            } else if (act.equals("deleteFile")) {
                out = deleteFile(paramBean);
            } else if (act.equals("deleteDirectory")) {
                out = deleteDirectory(paramBean);
            } else {
                out = new OutBean();
                out.setError("act not exists. " + act);
            }
        } catch (Exception e) {
            out = new OutBean();
            out.setError(e.getMessage());
        }

        if (!res.isCommitted()) { // 如果response未关闭则返回OutBean
            if (out == null) {
                out = new OutBean();
                out.setError("outBean is null; act:" + act);
            }
            res.getWriter().print(JsonUtils.toJson(out));
            res.flushBuffer();
        }
    }

    /**
     * 保存文件
     * @param paramBean 参数
     * @return 操作结果，包含FILE_SIZE参数
     * @throws IOException IO 异常
     */
    private OutBean save(ParamBean paramBean) throws IOException {
        List<FileItem> fileItem = paramBean.getList(FILE_ITEM_LIST);
        OutBean outBean = new OutBean();
        if (fileItem.size() > 0) {
            String filePath = paramBean.getStr(FILE_PATH);
            long size = FileStorage.saveFile(fileItem.get(0).getInputStream(), filePath);
            outBean.setOk();
            outBean.set("FILE_SIZE", size);
        } else {
            outBean.setError("file is empty.");
        }

        return outBean;
    }

    /**
     * 创建新文件
     * @param paramBean 参数
     * @return 操作结果，创建成功则CREATED参数的值为true，否则为false。
     * @throws IOException IO 异常
     */
    private OutBean createNewFile(ParamBean paramBean) throws IOException {
        OutBean outBean = new OutBean();
        String filePath = paramBean.getStr(FILE_PATH);

        boolean success = FileStorage.createFile(filePath);
        outBean.setOk();
        outBean.set("CREATED", success);
        return outBean;
    }

    /**
     * 下载文件
     * @param res 响应
     * @param paramBean 参数
     * @throws IOException IO 异常
     */
    private void download(HttpServletResponse res, ParamBean paramBean) throws IOException {
        String filePath = paramBean.getStr(FILE_PATH);
        InputStream is = null;
        OutputStream out = null;
        try {
            is = FileStorage.getInputStream(filePath);
            out = res.getOutputStream();
            IOUtils.copyLarge(is, out);
            res.flushBuffer();
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 列出指定目录下的文件
     * @param paramBean 参数
     * @return 包含FILE_NAMES参数的outBean
     * @throws IOException IO 异常
     */
    private OutBean list(ParamBean paramBean) throws IOException {
        String filePath = paramBean.getStr(FILE_PATH);
        String[] result = FileStorage.list(filePath);

        OutBean outBean = new OutBean();
        outBean.setOk();
        outBean.set("FILE_NAMES", StringUtils.join(result, ","));

        return outBean;
    }

    /**
     * 判断指定目录文件是否存在
     * @param paramBean 参数
     * @return 如果文件存在，则EXISTS参数的值为true，否则为false。
     * @throws IOException IO异常
     */
    private OutBean exists(ParamBean paramBean) throws IOException {
        String filePath = paramBean.getStr(FILE_PATH);

        OutBean outBean = new OutBean();
        outBean.setOk();
        outBean.set("EXISTS", FileStorage.exists(filePath));

        return outBean;
    }

    /**
     * 删除文件
     * @param paramBean 参数Bean
     * @return 如果文件删除成功，则DELETED参数的值为true，否则为false
     * @throws IOException IO异常
     */
    private OutBean deleteFile(ParamBean paramBean) throws IOException {
        String filePath = paramBean.getStr(FILE_PATH);

        OutBean outBean = new OutBean();
        outBean.setOk();

        outBean.set("DELETED", FileStorage.deleteFile(filePath));

        return outBean;
    }

    /**
     * 删除目录。由于存在安全隐患，此功能未实现。
     * @param paramBean 参数
     * @return 如果目录删除成功，则DELETED参数的值为true，否则为false
     * @throws IOException IO 异常
     */
    private OutBean deleteDirectory(ParamBean paramBean) throws IOException {
        // String filePath = paramBean.getStr(FILE_PATH);
        OutBean outBean = new OutBean();
        outBean.setOk();
        // FileStorage.deleteDirectory(filePath)
        outBean.set("DELETED", true);

        return outBean;
    }

    /**
     * 处理文件上传表单
     * @param request request
     * @return paramBean
     */
    @SuppressWarnings("unchecked")
    private ParamBean handleFileUpload(HttpServletRequest request) {
        ParamBean paramBean = new ParamBean();

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            List<FileItem> items = new ArrayList<FileItem>();
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            try {
                List<FileItem> allItems = upload.parseRequest(request);
                for (FileItem item : allItems) {
                    if (item.isFormField()) {
                        paramBean.set(item.getFieldName(), item.getString());
                        continue;
                    }

                    items.add(item);
                }

                paramBean.set(FILE_ITEM_LIST, items);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new TipException("file upload param error.");
            }
        }

        paramBean.putAll(RequestUtils.transParam(request));
        paramBean.set(FILE_PATH, filePath(request));

        return paramBean;
    }

    /**
     * 解析文件路径
     * @param request 请求
     * @return 从request中取得文件存储路径
     */
    private String filePath(HttpServletRequest request) {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (path.startsWith(contextPath)) { // 去掉contextPath
            path = path.substring(contextPath.length());
        }

        // 处理成本地地址
        if (path.startsWith("/fileStorage")) {
            path = path.substring(SERVLET_NAME.length()); // 去掉ServletName
            path = FileMgr.SY_COMM_FILE_PATH_EXPR + path;
            path = FileMgr.getAbsolutePath(path);
        }

        return path;
    }

    /**
     * 
     * @param request 客户端请求对象
     * @return 验证用户名/密码是否正确
     */
    private boolean auth(HttpServletRequest request) {
        String tuser = request.getHeader("AUTH_SEC");
        String authSec = Context.getSyConf(AUTH_SEC, "");
        if (tuser != null && tuser.equals(authSec)) {
            return true;
        }
        log.error("访问节点：" + System.getProperty("servName") + "AUTH_SEC为" + tuser);
        return false;
    }

}
