/*
 * Copyright (c) 2011 Ruaho All rights reserved.
 */
package com.rh.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hg.util.SysMgr;
import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.Context.APP;
import com.rh.core.base.TipException;
import com.rh.core.comm.ConfMgr;
import com.rh.core.comm.FileMgr;
import com.rh.core.comm.file.FileUploadListener;
import com.rh.core.comm.file.upload.DefaultUploadHandler;
import com.rh.core.comm.file.upload.FileUploadHandler;
import com.rh.core.comm.file.upload.RedheadUploadHandler;
import com.rh.core.comm.file.upload.UEditorUploadHandler;
import com.rh.core.org.DeptBean;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.OrgMgr;
import com.rh.core.plug.search.client.RhFileClient;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.BarcodeUtils;
import com.rh.core.util.Constant;
import com.rh.core.util.Lang;
import com.rh.core.util.RequestUtils;
import com.rh.core.util.Strings;
import com.rh.core.util.file.RuahoFileItem;
import com.rh.core.util.http.HttpGetResponse;
import com.rh.core.util.http.HttpUtils;

/**
 * 文件响应Servlet
 * @author liwei
 */
public class FileServlet extends HttpServlet {

    /** log */
    private static Log log = LogFactory.getLog(FileServlet.class);
    /** UID */
    private static final long serialVersionUID = 1L;
    /** default io buffer size */
    /** default http response client error code */
    private static final int RESPONSE_CLIENT_ERROR_CODE = 400;
    /** default http response server error code */
    private static final int RESPONSE_SERVER_ERROR_CODE = 500;
    /** default file download uri pattern example-http://localhost:8080/file/download/sjdh33242.doc */
    private static final String FILE_DOWNLOAD_URI_PATTERN = "download";
    /** default file preview uri pattern example-http://localhost:8080/file/preview/sjdh33242.doc */
    private static final String FILE_PREVIEW_URI_PATTERN = "preview";

    private static final String FILE_OPEN_URI_PATTERN = "open";

    /**
     * 上传文件
     * @param request 请求头
     * @param response 响应头
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        request.setCharacterEncoding(Constant.ENCODING);
        Context.cleanThreadData();
        Context.setRequest(request);
        UserBean user = Context.getUserBean(request);
        FileUploadHandler handler = null;
        String handlerStr = request.getParameter("handler");
        if (handlerStr == null) {
            handler = new DefaultUploadHandler();
        } else if (handlerStr.equals("ueditor")) {
            handler = new UEditorUploadHandler();
        } else if (handlerStr.equals("redhead")) {
            handler = new RedheadUploadHandler();
        } else {
            handler = new DefaultUploadHandler();
        }

        ParamBean param = new ParamBean(request);
        handler.handleRequest(request, param); // 处理参数信息
        Bean resultBean = null;
        if (null == user) { //session过期
        	log.error("session is timeout");
        	resultBean = new Bean();
            resultBean.set("STATUS", "ERROR");
        } else {
	        // upload...
	        String fileId = getFileId(request);
	        String model = RequestUtils.getStr(request, "model");
	        try {
	            if (0 < fileId.length()) {
	                if (exists(fileId)) {
	                    if (model.equals("saveHist")) {
	                        resultBean = update(request, param, false);
	                    } else {
	                        resultBean = update(request, param, true);
	                    }
	                } else {
	                    resultBean = create(request, param);
	                }
	            } else {
	                resultBean = create(request, param);
	            }
	        } catch (TipException e) { // 不记录Log，直接传递错误信息给界面
	            if (log.isDebugEnabled()) { // 调试模式下也输出exception到log中
	                log.debug(e.getMessage(), e);
	            }
	            resultBean = new Bean();
	            resultBean.set(Constant.RTN_MSG, Constant.RTN_MSG_ERROR + e.getMessage());
	        } catch (Exception e) {
	            log.error(e.getMessage(), e);
	            resultBean = new Bean();
	            resultBean.set(Constant.RTN_MSG, Constant.RTN_MSG_ERROR + e.getMessage());
	        }
        }
        // after upload handle
        handler.handleResponse(request, response, resultBean);
    }

    /**
     * 检查特殊文件 ， 如果存在于黑名单中，则抛出提示错误信息
     * @param item 文件条目
     * 
     */
	private void checkSpecialFile(FileItem item) {
		String fileName = item.getName();
		if (0 < fileName.lastIndexOf(".")) {
		    String suffix = "," + FilenameUtils.getExtension(fileName) + ",";
		    
		    //不能上传的文件后缀
			String blackFile = "," + Context.getSyConf("SY_FILE_NOT_UPLOAD_SUFFIX", "") + ","; 
			
			if (blackFile.toLowerCase().indexOf(suffix.toLowerCase()) >= 0) { //存在于黑名单中
				throw new TipException("不能上传后缀是 " + suffix + " 的文件");
			}
		}
	}

    /**
     * 下载/预览 文件请求处理
     * @param request 请求头
     * @param response 响应头
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // use restful uri
        request.setCharacterEncoding("UTF-8");
        // TODO upload qrcode use post method
        String act = RequestUtils.getStr(request, "act");
        if (act.equals("qrCode")) {
            String value = RequestUtils.getStr(request, "value")+"/bnoa.apk";
            OutputStream out = response.getOutputStream();
            try {
                int size = RequestUtils.get(request, "size", 200);
                BarcodeUtils.genQRCode(out, value, size, size);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            out.flush();
            response.flushBuffer();
            return;
        }

        if (act.equals(FILE_PREVIEW_URI_PATTERN)) {
            // file preview
            preview(request, response);
        } else {
            // set client cache
            response.setHeader("Server", "rhfile-server");
//            response.setHeader("Cache-Control", "max-age=" + (3600 * 2 * 12));
            // file download
            download(request, response);
        }

    }

    /**
     * 文件预览
     * @param request 请求头
     * @param response 响应头
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    private void preview(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String currentHost = getHost(request.getRequestURL().toString());

        Context.getUserBean(request);
        String fileId = getFileId(request);
        fileId = URLDecoder.decode(fileId, "UTF-8");
        String type = RequestUtils.getStr(request, "type");
        boolean isMobile = false;
        if (type.equals("mobile")) {
            isMobile = true;
        }
        
        // http file
        if (fileId.startsWith("http://")) {
            // get file from http
            HttpGetResponse getResp = HttpUtils.httpGet(fileId);
            // String fileName = getResp.getDownloadName();
            InputStream is = getResp.getInputStream();
            previewWithInputStream(currentHost, is, "", response, isMobile);
        } else {
            // get file from internal fs
            Bean fileBean = FileMgr.getFile(fileId);
            if (null == fileBean) {
                String errorMsg = Context.getSyMsg("SY_DOWNLOAD_FILE_NOT_FOUND");
                if (null == errorMsg || 0 == errorMsg.length()) {
                    errorMsg = "the file not found";
                }
                throw new TipException(errorMsg);
            }

//            // get file checksum
//            String checksum = fileBean.getStr("FILE_CHECKSUM");
//            // preview with file checksum first
//            int status = 0;
//            if (null == checksum || 0 == checksum.length()) {
//                log.warn("the file's checksum is null, file id:" + fileId);
//            } else {
//                status = RhFileClient.getInstance().getPreviewStatusCode(checksum, isMobile);
//            }
//            
//            if (200 == status) {
//                String url = RhFileClient.getInstance().getServerUri();
//                if (!url.endsWith("/")) {
//                    url += "/";
//                }
//                url += "preview/" + checksum + fileId.substring(fileId.lastIndexOf(".")) + ".html";
//                if (isMobile) {
//                    url += "?previewType=mobile";
//                }
//                response.sendRedirect(url);
//            } else {
                // String fileName = resultBean.getStr("FILE_NAME");
                InputStream is = FileMgr.download(fileBean);
                previewWithInputStream(currentHost, is, "", response, isMobile);
//            }
        }

    }

    /**
     * 基于inputstream文件预览
     * @param currentHost - 当前访问服务器host
     * @param is - inputstream
     * @param fileName - file name
     * @param response 响应头
     * @param isMobile - 是否为手机版
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    private void previewWithInputStream(String currentHost, InputStream is, String fileName,
            HttpServletResponse response, boolean isMobile)
            throws ServletException, IOException {
        String url = "";
        try {
            url = RhFileClient.getInstance().preview(is, fileName, isMobile);
        } catch (IOException e) {
            if (e.getMessage().contains("Do not support suffix")) {
                log.warn("该文件不支持预览!", e);
                response.getOutputStream().write("该文件不支持预览!".getBytes("UTF-8"));
                response.getOutputStream().flush();
                return;
            } else {
                throw e;
            }

        }
        url = RhFileClient.getInstance().getRedirectServerUri() + url;
        url = url.replace("${host}", currentHost);
        log.debug("File Service redirect to " + url);

        IOUtils.closeQuietly(is);
        response.sendRedirect(url);
    }

    /**
     * 下载/使用浏览器打开文件
     * @param request 请求头
     * @param response 响应头
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    private void download(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        StopWatch sw = new StopWatch();
        sw.start();
        String act = RequestUtils.getStr(request, "act");
        InputStream is = null;
        String fileId = null;
        Bean resultBean = null;
        try {
            Context.getUserBean(request);
            fileId = getFileId(request);
            // 获取图片文件
            if (isImage(fileId)) {
                String size = RequestUtils.getStr(request, "size");
                resultBean = FileMgr.getImgFile(fileId, size);
            } else {
                // 获取普通文件
                resultBean = FileMgr.getFile(fileId);
            }
            if (null == resultBean) {
                String errorMsg = Context.getSyMsg("SY_COMM_FILE_NOT_EXIST", fileId);
                if (null == errorMsg || 0 == errorMsg.length()) {
                    errorMsg = "the file not found";
                }
                throw new TipException(errorMsg);
            }
            String relativePath = resultBean.getStr("FILE_PATH");
            if (0 == relativePath.length()) {
                // set waiting for storage default image
                String waiting4Storage = Context.app(APP.SYSPATH) + File.separator
                        + "sy/comm/file/images/waiting_for_storage.png";
                is = new FileInputStream(waiting4Storage);
            } else {
                is = FileMgr.download(resultBean);
            }

            // response for download request
            // set response file name
            if (resultBean.contains("FILE_NAME") && !FILE_OPEN_URI_PATTERN.equals(act)) {
                String downloadFileName = FileMgr.getFileDisName(resultBean);
                RequestUtils.setDownFileName(request, response, downloadFileName);
                // RequestUtils.setDownFileName(request, response, resultBean.getStr("FILE_NAME"));
            }
            // set response content type
            String downFileType = "attachment;";
            if (0 < resultBean.getStr("FILE_MTYPE").length()) {
                downFileType = resultBean.getStr("FILE_MTYPE");
            }
            if (act.equals(FILE_DOWNLOAD_URI_PATTERN)) {
                downFileType = "attachment;";
            }
            if (!FILE_OPEN_URI_PATTERN.equals(act)) {
                // set response file content
                response.setContentType(downFileType);
            }
            OutputStream out = response.getOutputStream();
            IOUtils.copyLarge(is, out);
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(out);
            out.flush();
        } catch (TipException e) { // 不记录Log，直接传递错误信息给界面
            if (log.isDebugEnabled()) { // 调试模式下也输出exception到log中
                log.debug(e.getMessage(), e);
            }
            response.setStatus(RESPONSE_CLIENT_ERROR_CODE);
            response.setContentType("text/html;charset=UTF-8");
            OutputStream out = response.getOutputStream();            
            out.write(Strings.escapeHtml(e.getMessage()).getBytes("UTF-8"));
            out.flush();
            
        } catch (FileNotFoundException fe) {
            log.error(fe.getMessage());
            response.setStatus(RESPONSE_SERVER_ERROR_CODE);
            response.setContentType("text/html;charset=UTF-8");
            OutputStream out = response.getOutputStream();
            out.write("file not found in the server.".getBytes("UTF-8"));
            out.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response.setStatus(RESPONSE_SERVER_ERROR_CODE);
            response.setContentType("text/html;charset=UTF-8");
            OutputStream out = response.getOutputStream();
            out.write(Strings.escapeHtml(e.getMessage()).getBytes("UTF-8"));
            out.flush();
        } finally {
            IOUtils.closeQuietly(is);
            response.flushBuffer();
        }
        
        sw.stop();
        final long count = sw.getTime();
        //记录异常日志
        if (count > (10 * 1000)) {
            StringBuilder sb = new StringBuilder();
            sb.append("#DOWNLOAD_FILE_SPEND_TIME:").append(count);
            sb.append(",");
            sb.append(fileId);
            if (resultBean != null && resultBean.getInt("FILE_SIZE") > 0) {
                sb.append(",");
                sb.append(resultBean.getInt("FILE_SIZE"));
            }

            log.error(sb.toString());
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("#DOWNLOAD_FILE_SPEND_TIME:").append(count);
            sb.append(",");
            sb.append(fileId);
            log.debug(sb.toString());
        }
        fileDow(resultBean);//文件下载次数
    }

    /**
     * create file
     * @param request httpServletRequest
     * @param paramBean param Bean
     * @return out bean
     * @throws IOException IOException
     */
    private Bean create(HttpServletRequest request, Bean paramBean) throws IOException {
        String fileId = getFileId(request);
        paramBean.set("FILE_ID", fileId);

        // handle file uploads param
        List<FileItem> items = handleFileUpload(request, paramBean);

        FileUploadListener listener = this.createListener(paramBean);

        if (listener != null) {
            listener.befortAdd(paramBean);
        }

        String fileName = paramBean.getStr("FILE_NAME");
        List<Bean> dataList = new ArrayList<Bean>();
        // upload file from file form
        Iterator<FileItem> iter = items.iterator();
        while (iter.hasNext()) {
            FileItem item = iter.next();
            try {
                checkSpecialFile(item);
            	
                if (null == fileName || 0 == fileName.length()) {
                    fileName = item.getName();
                    paramBean.set("FILE_NAME", fileName);
                }

                String mType = getMimeType(item);
                paramBean.set("MTYPE", mType);

                Bean data = FileMgr.upload(paramBean, item.getInputStream());

                dataList.add(data);
            } catch (IOException ioe) {
                log.error(" file upload error.", ioe);
                throw ioe;
            } finally {
                item.delete();
            }

        }
        Bean resultBean = new Bean();
        resultBean.set(Constant.RTN_DATA, dataList);

        if (listener != null) {
            listener.afterAdd(paramBean, dataList);
        }

        return resultBean;
    }

    /**
     * update file
     * @param request httpServletRequest
     * @param paramBean paramBean
     * @param overWrite is owver write
     * @return out bean
     * @throws IOException IOException
     */
    private Bean update(HttpServletRequest request, Bean paramBean, boolean overWrite) throws IOException {
        String fileId = getFileId(request);

        paramBean.setId(fileId);
        // handle file uploads param
        List<FileItem> items = handleFileUpload(request, paramBean);
        List<Bean> dataList = new ArrayList<Bean>();
        // upload file from file form
        Iterator<FileItem> iter = items.iterator();

        FileUploadListener listener = this.createListener(paramBean);

        if (listener != null) {
            listener.beforeUpdate(paramBean);
        }

        while (iter.hasNext()) {
            FileItem item = iter.next();

            try {
            	checkSpecialFile(item);
            	
                InputStream is = item.getInputStream();

                String mType = "";
                if (paramBean.getStr("FILE_MTYPE") != null && paramBean.getStr("FILE_MTYPE").length() > 0) {
                    mType = paramBean.getStr("FILE_MTYPE");
                } else {
                    mType = getMimeType(item);
                }

                // Process a file upload
                Bean data = null;
                if (overWrite) {
                    boolean keepMeta = RequestUtils.get(request, "keepMetaData", false);
                    data = FileMgr.overWrite(fileId, is, item.getName(), paramBean.getStr("DIS_NAME")
                            , keepMeta, paramBean);
                } else {
                    data = FileMgr.update(fileId, is, mType);
                }
                IOUtils.closeQuietly(is);
                dataList.add(data);
            } catch (IOException ioe) {
                log.error(" file upload error.", ioe);
                throw ioe;
            } finally {
                item.delete();
            }
        }
        Bean resultBean = new Bean();
        resultBean.set(Constant.RTN_DATA, dataList);
        if (listener != null) {
            listener.afterUpdate(paramBean, dataList);
        }
        return resultBean;
    }

    /**
     * get file id from restful url
     * @param request HttpServletRequest
     * @return file Id
     */
    private String getFileId(HttpServletRequest request) {
        String uri = request.getRequestURI();

        String servlet = request.getServletPath();
        int index = uri.indexOf(servlet);
        String fileId = uri.substring(index + servlet.length());

        if (fileId.startsWith("/")) {
            fileId = fileId.substring(1);
            try {
                fileId = URLDecoder.decode(fileId, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.warn(e);
            }
        } else {
            fileId = "";
        }
        return fileId;
    }

    /**
     * 是否是图片文件?
     * @param fileId - file id
     * @return 该文件是否是图片类型
     */
    private boolean isImage(String fileId) {
        String surfix = "";
        if (0 < fileId.lastIndexOf(".")) {
            surfix = fileId.substring(fileId.lastIndexOf(".") + 1);
        }
        surfix = surfix.toLowerCase();
        boolean result = false;
        if (surfix.equals("jpg")) {
            result = true;
        } else if (surfix.equals("jpeg")) {
            result = true;
        } else if (surfix.equals("jpeg")) {
            result = true;
        } else if (surfix.equals("bmp")) {
            result = true;
        } else if (surfix.equals("png")) {
            result = true;
        } else if (surfix.equals("gif")) {
            result = true;
        }
        return result;
    }

    /**
     * 处理文件上传表单
     * @param request request
     * @param paramBean 参数Bean
     * @return 有效的上传文件的FileItem
     */
    @SuppressWarnings("unchecked")
    private List<FileItem> handleFileUpload(HttpServletRequest request, Bean paramBean) {
        // Check that we have a file upload request
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        List<FileItem> items = new ArrayList<FileItem>();
        if (isMultipart) {
            // Create a factory for disk-based file items
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);
            // Parse the request
            try {
                List<FileItem> allItems = upload.parseRequest(request);
                for (FileItem item : allItems) {
                    if (item.isFormField()) {
                        paramBean.set(item.getFieldName(), item.getString());
                        continue;
                    }

                    items.add(item);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new TipException("file upload param error.");
            }
        } else {
            try {
                items.add(new RuahoFileItem(request.getInputStream()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return items;
    }

    /**
     * get file mime Type
     * @param item <CODE>FileItem</CODE>
     * @return file mtype
     */
    private String getMimeType(FileItem item) {
        // parse file mime type
        String contentType = item.getContentType();
        if (null == contentType || 0 == contentType.length() || contentType.equals("application/octet-stream")) {
            contentType = FileMgr.getMTypeByName(item.getName());
        }
        return contentType;
    }

    /**
     * is file exits?
     * @param fileId file id
     * @return exits boolean value
     */
    private boolean exists(String fileId) {
        Bean fileParam = FileMgr.getFile(fileId);
        return (fileParam != null);
    }

    /**
     * 
     * @param paramBean 参数名称
     * @return 创建监听对象
     */
    private FileUploadListener createListener(Bean paramBean) {
        String listener = paramBean.getStr("listener");
        if (paramBean.isEmpty("listener")) {
            return null;
        }
        String clsName = ConfMgr.getConf(FileUploadListener.CONF_PREFIX + listener, "");
        if (StringUtils.isEmpty(clsName)) {
            return null;
        }

        return (FileUploadListener) Lang.createObject(FileUploadListener.class, clsName);
    }

    /**
     * 获取用户访问的目标地址
     * @param url - url
     * @return host
     */
    private String getHost(String url) {
        url = url.replace("http://", "");
        url = url.replace("https://", "");
        int portFlag = url.indexOf(":");
        if (-1 < portFlag) {
            url = url.substring(0, portFlag);
        }
        int uriFlag = url.indexOf("/");
        if (-1 < uriFlag) {
            url = url.substring(0, uriFlag);
        }
        return url;
    }
    
    /**
     * 文件下载次数记录
     * @param bean
     */
	public void fileDow(Bean bean) {
		String servId = bean.getStr("SERV_ID");
		if("BN_FILE".equals(servId)){
			String name = "";
			UserBean user = Context.getUserBean();
			String codePath = user.getStr("CODE_PATH");
			String[] codes = codePath.split("\\^");
			for (int i = 0; i < codes.length; i++) {
				String code = codes[i];
				DeptBean deptBean = OrgMgr.getDept(code);
				String deptName = deptBean.getName();
				if (i==0) {
					name = name+deptName;
				}else {
					name = name+"/"+deptName;
				}
			}
			ParamBean paramBean = new ParamBean();
			paramBean.set("FILE_ID", bean.get("FILE_ID"));
			paramBean.set("FILE_NAME",bean.get("FILE_NAME"));
			paramBean.set("DIS_NAME",bean.get("DIS_NAME"));
			paramBean.set("S_UNAME",user.getName());
			paramBean.set("CODE_PATH",codePath);
			paramBean.set("DEPT_NAME",name);
			paramBean.set("DOW_NUM","1");
			ServDao.create("BN_FILE_DOWNLOAD", paramBean);
		}
		
	}
   
}
