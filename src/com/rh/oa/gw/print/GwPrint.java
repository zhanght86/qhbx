package com.rh.oa.gw.print;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.serv.ServDao;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 公文通过freemarker 模板打印
 * 
 * @author zhaozhenxing
 *
 */
public class GwPrint {
    // 日志记录
    private static Log log = LogFactory.getLog(GwPrint.class);
    
    // 模板路径
    private static String ftlPath = Context.appStr(Context.APP.SYSPATH) + "gw/print/ftl";
    
    private String gwId = "";
    
    private Bean gwBean;
    
    private String servId;
    
    private static Configuration cfg = null;
    
    /**
     * @param aServId 服务ID
     * @param aGwId 公文ID
     */
    public GwPrint(String aServId, String aGwId) {
    	this.gwId = aGwId;
    	this.servId = aServId;
    	
    	this.gwBean = ServDao.find(servId, gwId);
    	
        cfg = new Configuration(); 
        try {
            cfg.setDirectoryForTemplateLoading(new File(ftlPath));
        } catch (IOException e) {
        	log.error("IOException " + e.getMessage(), e);
        } 
        cfg.setObjectWrapper(new DefaultObjectWrapper()); 
        cfg.setDefaultEncoding("UTF-8"); 
    }
    
    /**
     * @param printBody 打印的内容
     * @return 打印预览的串
     */
    public String createPreviewHtml(String printBody) {
        StringWriter sw = new StringWriter();
        try {
            Template temp = cfg.getTemplate(this.gwBean.getStr("TMPL_CODE") + "_preview.ftl");
            
            Map<String, Object> root = new HashMap<String, Object>();
            
            
            root.put("printBody", printBody);
            root.put("SERV_ID", this.gwBean.getStr("TMPL_CODE"));
            root.put("DATA_ID", this.gwBean.getId());
            
            
            /* 将模板和数据模型合并 */ 
            try {
                temp.process(root, sw);
            } catch (TemplateException e) {
                log.info("模板和数据模型合并失败：" + e);
            } 
            
           
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        
        return sw.toString();
    }
    
    
    /**
     * 根据模板名生成新闻静态html文件
     * @return 返回替换后的文本
     */
    public String createHtml() {
        StringWriter sw = new StringWriter();
        try {
        	//通过公文模板取到定义的打印模板的文件名称
        	Bean tmplBean = ServDao.find("OA_GW_TMPL", this.gwBean.getStr("TMPL_CODE"));

        	String printTmplFile = "default.ftl";
            
        	if (!StringUtils.isEmpty(tmplBean.getStr("TMPL_PRINT"))) {
        		printTmplFile = tmplBean.getStr("TMPL_PRINT");
        	}
        	Template temp = cfg.getTemplate(printTmplFile);
            
            Map<String, Object> root = new HashMap<String, Object>();
            
            //String secret = DictMgr.getFullName("GW_SECRET", gwBean.getStr("GW_SECRET"));
            //String emergency = DictMgr.getFullName("GW_EMERGENCY", gwBean.getStr("GW_EMERGENCY"));
            
            root.put("gwBean", this.gwBean);              
            
            /**
            root.put("GW_SECRET", secret);                // 密级
            root.put("GW_EMERGENCY", emergency);      // 缓急
            root.put("GW_TITLE", gwBean.getStr("GW_TITLE"));            // 标题
            root.put("GW_TOPIC", gwBean.getStr("GW_TOPIC"));                 
            root.put("GW_YEAR", gwBean.getStr("GW_YEAR"));             
            root.put("GW_YEAR_CODE", gwBean.getStr("GW_YEAR_CODE"));                
            root.put("GW_MAIN_TO", gwBean.getStr("GW_MAIN_TO"));                
            root.put("GW_COPY_TO", gwBean.getStr("GW_COPY_TO"));      
            root.put("GW_SEND_TO", gwBean.getStr("GW_SEND_TO"));             
            root.put("GW_BEGIN_TIME", gwBean.getStr("GW_BEGIN_TIME"));                  
            root.put("GW_CONTACT", gwBean.getStr("GW_CONTACT"));             
            root.put("GW_CONTACT_PHONE", gwBean.getStr("GW_CONTACT_PHONE"));                
            
            
            */
            // 查询意见  会签   TODO 加意见公用类，取到不同类型的意见
            Bean hqQueryBean = new Bean();
            hqQueryBean.set("SERV_ID", this.servId);
            hqQueryBean.set("DATA_ID", this.gwId);
            //mindQueryBean.set("MIND_CODE", "HQ");
            
            List<Bean> hqMindList = ServDao.finds("CM_MIND", hqQueryBean);
            root.put("hqMindList", hqMindList);
            
            // 查询意见  核批
            Bean hpQueryBean = new Bean();
            hpQueryBean.set("SERV_ID", this.servId);
            hpQueryBean.set("DATA_ID", this.gwId);
            //mindQueryBean.set("MIND_CODE", "HP");
            
            List<Bean> hpMindList = ServDao.finds("CM_MIND", hpQueryBean);
            root.put("hpMindList", hpMindList);
            
            
            // 查询意见  核稿
            Bean hgQueryBean = new Bean();
            hgQueryBean.set("SERV_ID", this.servId);
            hgQueryBean.set("DATA_ID", this.gwId);
            //mindQueryBean.set("MIND_CODE", "HP");
            
            List<Bean> hgMindList = ServDao.finds("CM_MIND", hgQueryBean);
            root.put("hgMindList", hgMindList);
            
            
            
            /* 将模板和数据模型合并 */ 
            try {
                temp.process(root, sw);
            } catch (TemplateException e) {
                log.info("模板和数据模型合并失败：" + e);
            } 
            
           
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        
        return sw.toString();
    }
}
