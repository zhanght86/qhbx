package com.rh.core.comm.cms;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.FileStorage;

/**
 * 默认cms处理
 * 
 * @author liwei
 * 
 */
public class NewsCmsServletHandler extends DefaultCmsServletHandler {

    /** log */
  //  private static Log log = LogFactory.getLog(NewsCmsServletHandler.class);

    /** 新闻静态文件根目录 */
    private static final String SY_COMM_NEWS_STATIC_FILE_ROOT = "SY_COMM_NEWS_STATIC_FILE_ROOT";

    @Override
    public String doGet(HttpServletRequest request, Bean data, HttpServletResponse response) throws ServletException,
            IOException {
        String relativePath = data.getStr("STATIC_FILE");
        //如果没有静态文件数据，进行默认处理
        if (0 == relativePath.length()) {
            return super.doGet(request, data, response);
        }
        
        //提取静态文件相对路径
        String staticFileRoot = Context.getSyConf(SY_COMM_NEWS_STATIC_FILE_ROOT, "");
        if (staticFileRoot.length() == 0) {
            throw new ServletException(String.format("sytem config :%s not found!", SY_COMM_NEWS_STATIC_FILE_ROOT));
        }
      
        //构造出目标静态文件绝对路径
        if (!staticFileRoot.endsWith("/")) {
            staticFileRoot += "/"; 
        }
        if (relativePath.startsWith("/")) {
            relativePath = relativePath.substring(1);
        }
        
        String absolutePath = staticFileRoot + relativePath;
        
        //TODO: 代码优化
        InputStream intput = FileStorage.getInputStream(absolutePath);
        
        IOUtils.copy(intput, response.getOutputStream());
        IOUtils.closeQuietly(intput);
        IOUtils.closeQuietly(response.getOutputStream());
        
       return "";
    }

  

}
