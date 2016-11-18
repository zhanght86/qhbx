// Source File Name:   RemotePic.java

package com.rh.core.comm.news.mgr;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.comm.FileMgr;

/**
 * 远程文件处理类，用户下载远程的图片文件等到本地服务器
 * 
 * @author chensheng
 */
public class RemoteFile {
    /**
     * 日志记录
     */
    private static Log logger = LogFactory.getLog(RemoteFile.class);

    /**
     * 信息发布服务ID
     */
    private static String servId = "SY_COMM_NEWS";

    /**
     * 指定路径下载远程图片文件
     * @param fileUrl 远程图片文件的地址
     * @return 返回上传完保存在本地的文件名字
     */
    public static String download(String fileUrl) {

        // 设置代理
        //setProxy();

        HttpURLConnection httpUrl = null;
        URL url = null;

        Bean fileBean = null;
        
        try {
            url = new URL(fileUrl);

            httpUrl = (HttpURLConnection) url.openConnection();

            // 模拟浏览器
            httpUrl.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            // 连接指定的资源
            httpUrl.connect();

            String name = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            
//            String fileType = name.substring(name.indexOf(".") + 1);
            
            //String mtype = getMtype(fileType);
            
            //FileMgr.asyncUpload(servId, httpUrl.getInputStream(), name, mtype);
            fileBean = FileMgr.upload(servId, "", "", httpUrl.getInputStream(), name);
        } catch (MalformedURLException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
        if (fileBean != null) {
            fileBean.getId();
        }
        return null;
    }

    /**
     * 通过文件类型返回文件mtype
     * @param fileType 文件后缀
     * @return 返回文件的mime-type
     */
    public static String getMtype(String fileType) {
        if ("jpg".equals(fileType.toLowerCase()) || "jpeg".equals(fileType.toLowerCase())) {
            return "image/jpeg";
        } else if ("gif".equals(fileType.toLowerCase())) {
            return "image/gif";
        } else if ("png".equals(fileType.toLowerCase())) {
            return "image/png";
        }
        return null;
    }

    /**
     * 代理网络设置
//     */
//    private static void setProxy() {
//        Properties prop = System.getProperties();
//
//        // 设置代理服务器地址
//        prop.setProperty("http.proxyHost", "172.16.0.94");
//
//        // 设置代理服务器端口
//        prop.setProperty("http.proxyPort", "808");
//
//        // 设置代理服务器用户名
//        // prop.setProperty("http.proxyUser", "");
//
//        // 设置代理服务器密码
//        // prop.setProperty("http.proxyPassword", "");
//    }
}
