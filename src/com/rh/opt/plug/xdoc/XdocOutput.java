package com.rh.opt.plug.xdoc;

import java.net.URLEncoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import com.hg.xdoc.XDoc;
import com.hg.xdoc.XDocIO;
import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.Context.APP;
import com.rh.core.base.TipException;
import com.rh.core.plug.IXdoc;

/**
 * XDOC格式化文件流输出
 * 需要用到Jar包：xdoc.jar、poi.jar、fgio.jar、
 * @author hdy
 *
 */
public class XdocOutput implements IXdoc {
    
    /**
     * XDOC文件格式化输出流
     * @param paramBean XDOC模板参数
     */
    @SuppressWarnings("null")
    public void outputXdoc(Bean paramBean) {
        String sb = XdocUtils.toXml("xdoc", new Bean().set("data", paramBean.get("data")));
        paramBean.set("_xdata", sb);
        //获取servlet响应
        HttpServletResponse response = Context.getResponse();
        //文件类型
        HashMap<String, String>fileType = new HashMap<String, String>();
        fileType.put("xls", "application/msexcel");
        fileType.put("xlsx", "application/msexcel");
        fileType.put("doc", "application/msword");
        fileType.put("docx", "application/msword");
        fileType.put("html", "text/html");
        fileType.put("pdf", "application/pdf");
        fileType.put("wps", "application/vnd.ms-works");
        fileType.put("txt", "text/plain");
        //设置文件mime类型
        String format = paramBean.getStr("format").isEmpty() ? "pdf" :  paramBean.getStr("format");
        if (format.equals("doc")) { //统一word文档格式
            format = "docx"; 
        }
        response.setContentType(fileType.get(format));
        //获取文件名
        String fileName = paramBean.getStr("fileName").isEmpty() ? "expFile" : paramBean.getStr("fileName");
        String fileFullName = fileName + "." + format;
        try {
          //设置文件头
            response.setHeader("Content-Disposition", "attachment; filename="
                        + URLEncoder.encode(fileFullName, "UTF-8"));
            //初始化XDOC文件
            XDoc xdoc = XDocIO.read(Context.appStr(APP.SYSPATH) + paramBean.getStr("filePath")).run(paramBean);
            //输出XDOC格式文件流
            XDocIO.write(xdoc, response.getOutputStream(), format);
        } catch (Exception e) {
            throw new TipException("XDOC文件输出错误：" + e.getMessage());
        }
    }
}
