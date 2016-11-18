package com.rh.bn.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import com.rh.core.base.Context;
import com.rh.core.base.Context.THREAD;
import com.rh.core.comm.FileMgr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * 根据doc模板生成doc文档类
 * @author Tanyh
 *
 */
public class DocUtil {
	
		private static Log log = LogFactory.getLog(DocUtil.class);
    
    private static Configuration config = null;
    
    /**
     * 初始化模板配置类
     * @return Configuration
     * @throws IOException
     */
    public static Configuration getInstance() throws IOException {
        if (config != null) {
            return config;
        }
        config = new Configuration();
        config.setDefaultEncoding("utf-8");
        config.setTemplateLoader(new StringTemplateLoader());
        config.setObjectWrapper(new DefaultObjectWrapper());
        config.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        
        System.out.println(FileMgr.getRootPath() + Context.getThread(THREAD.CMPYCODE) + "/BN_QB_DATA_QUERY/template");
        
        config.setDirectoryForTemplateLoading(new File(FileMgr.getRootPath() + Context.getThread(THREAD.CMPYCODE) + "/BN_QB_DATA_QUERY/template"));
        log.info("模版路径：" + FileMgr.getRootPath() + Context.getThread(THREAD.CMPYCODE) + "/BN_QB_DATA_QUERY/template");
        return config;
    }
    
    /**
     * 生成doc文档
     * @param map 数据
     * @param tempName 模板文件
     * @param outFile doc文档路径
     * @throws TemplateException
     * @throws IOException
     */
    public static void process (Map<String, Object> map, String tempName, File outFile) throws TemplateException, IOException {
        Template template = getInstance().getTemplate(tempName);
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"utf-8"));
        template.process(map, writer);
    }
}
