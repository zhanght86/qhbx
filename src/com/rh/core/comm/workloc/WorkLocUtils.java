package com.rh.core.comm.workloc;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.rh.core.base.Context;

/**
 * 工位工具类
 * @author wanghg
 */
public class WorkLocUtils {
    /**
     * 输出xml
     * @param doc doc
     */
    public static void write(Document doc) {
        try {
            HttpServletResponse response = Context.getResponse();
            response.setContentType("text/xml;charset=UTF-8");
            ServletOutputStream out = response.getOutputStream();
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(out, format);
            writer.write(doc);
            writer.flush();
            out.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("写入xml数据错误", e);
        }
    }

}
