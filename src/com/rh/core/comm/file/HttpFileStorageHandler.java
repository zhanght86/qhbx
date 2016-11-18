package com.rh.core.comm.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.Context.APP;
import com.rh.core.serv.OutBean;
import com.rh.core.util.httpclient.HttpClientUtils;
import com.rh.core.util.httpclient.HttpResponseUtils;

/**
 * 使用HTTP协议，将文件保存到其它WEB服务器的实现类。
 * @author yangjy
 * 
 */
public class HttpFileStorageHandler implements FileStorageHandler {
    private static final String AUTH_SEC = "HTTP_STORAGE_AUTH_SEC";
    /** 字符编码 */
    private static final String ENCODING = "UTF-8";
    /** log */
    private static Log log = LogFactory.getLog(HttpFileStorageHandler.class);

    /**
     * 
     * @param post HttpPost对象
     */
    public static void appendAuthInfo(HttpRequestBase post) {
        String authCode = Context.getSyConf(AUTH_SEC, "");
        post.addHeader("AUTH_SEC", authCode);
        if (authCode.isEmpty()) {
            log.error("访问节点：" + System.getProperty("servName") + "AUTH_SEC为空");
        }
    }

    @Override
    public long save(InputStream input, String path) throws IOException {
        final String action = "save";
        HttpPost post = HttpClientUtils.createHttpPost(path + "?act=" + action, null, ENCODING);
        appendAuthInfo(post);

        MultipartEntity entity = new MultipartEntity();
        entity.addPart("file", new InputStreamBody(input, "", path));
        post.setEntity(entity);

        OutBean outBean = HttpClientUtils.execute(post);

        return outBean.getLong("FILE_SIZE");
    }

    @Override
    public boolean createNewFile(String path) throws IOException {
        final String action = "createNewFile";
        HttpPost post = HttpClientUtils.createHttpPost(path + "?act=" + action, null, ENCODING);
        appendAuthInfo(post);

        OutBean outBean = HttpClientUtils.execute(post);
        return outBean.getBoolean("CREATED");
    }

    @Override
    public InputStream getInputStream(String absolutePath) throws IOException {
        final String action = "download";
        HttpClient httpclient = HttpClientUtils.createHttpClient();
        HttpGet get = HttpClientUtils.createHttpGet(absolutePath + "?act=" + action);
        
        appendAuthInfo(get);
        
        HttpResponse response = httpclient.execute(get);
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new IOException("Error:" + HttpResponseUtils.getResponseContent(response));
        }

        return response.getEntity().getContent();
    }
    
    @Override
    public OutputStream getOutputStream(String absolutePath) throws IOException {
        String tempFilePath = Context.appStr(APP.WEBINF_DOC + "temp/" + UUID.randomUUID());
        File file = new File(tempFilePath);  //临时文件
        if (file.getParentFile().exists()) {  //如果目录不存在则创建目录
            file.getParentFile().mkdirs();
        }
        
        HttpTempFileOutputStream output = new HttpTempFileOutputStream(file);
        output.setUrl(absolutePath);

        return output;
    }

    @Override
    public String[] list(String path) throws IOException {
        final String action = "list";
        HttpPost post = HttpClientUtils.createHttpPost(path + "?act=" + action, null, ENCODING);
        appendAuthInfo(post);

        Bean outBean = HttpClientUtils.execute(post);
        return outBean.getStr("FILE_NAMES").split(",");
    }

    @Override
    public boolean exists(String path) throws IOException {
        final String action = "exists";
        HttpPost post = HttpClientUtils.createHttpPost(path + "?act=" + action, null, ENCODING);
        appendAuthInfo(post);

        Bean outBean = HttpClientUtils.execute(post);
        return outBean.getBoolean("EXISTS");
    }

    @Override
    public boolean deleteFile(String path) throws IOException {
        final String action = "deleteFile";
        HttpPost post = HttpClientUtils.createHttpPost(path + "?act=" + action, null, ENCODING);
        appendAuthInfo(post);

        Bean outBean = HttpClientUtils.execute(post);
        return outBean.getBoolean("DELETED");
    }

    @Override
    public boolean deleteDirectory(String path) throws IOException {
        final String action = "deleteDirectory";
        HttpPost post = HttpClientUtils.createHttpPost(path + "?act=" + action, null, ENCODING);
        appendAuthInfo(post);

        Bean outBean = HttpClientUtils.execute(post);
        return outBean.getBoolean("DELETED");
    }

    @Override
    public long getSize(String path) throws IOException {
        return 0;
    }
}
