package com.rh.core.comm.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;

import com.rh.core.util.httpclient.HttpClientUtils;

/**
 * 临时文件输出流，关闭输出流时，将文件上传到WEB服务器。
 * @author yangjy
 * 
 */
public class HttpTempFileOutputStream extends FileOutputStream {
    /** 字符编码 */
    private static final String ENCODING = "UTF-8";

    /**
     * 上传文件的URL
     */
    private String url = null;

    private File tempFile = null;

    /**
     * 
     * @param file 临时文件
     * @throws IOException IO错误 
     */
    public HttpTempFileOutputStream(File file) throws IOException {
        super(file);
        tempFile = file;
    }

    @Override
    public void close() throws IOException {
        super.close();
        upload();
    }

    /**
     * 上传临时文件到服务器，并删除临时文件
     * @throws IOException IO异常
     */
    private void upload() throws IOException {
        final String action = "save";
        HttpPost post = HttpClientUtils.createHttpPost(url + "?act=" + action, null, ENCODING);
        HttpFileStorageHandler.appendAuthInfo(post);
        FileInputStream is = null;
        try {
            is = new FileInputStream(tempFile);

            MultipartEntity entity = new MultipartEntity();
            entity.addPart("file", new InputStreamBody(is, "", url));
            post.setEntity(entity);

            HttpClientUtils.execute(post);
        } finally {
            IOUtils.closeQuietly(is);
            tempFile.delete();  //删除临时文件
        }
    }
    
    /**
     * 
     * @return URL
     */
    public String getUrl() {
        return url;
    }
    
    /**
     * 
     * @param url URL
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
