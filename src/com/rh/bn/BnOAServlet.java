package com.rh.bn;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.bn.linktransfer.LinkTransfer;
import com.rh.bn.linktransfer.factory.LinkTransferFactory;
import com.rh.core.base.TipException;

/**
 * 百年人寿项目处理旧oa系统图片、文档等相关链接响应servlet
 * @author tanyh 20151208
 *
 */
public class BnOAServlet extends HttpServlet{

    /** UID **/
    private static final long serialVersionUID = 3177850544975158170L;
    /** log **/
    private static Log log = LogFactory.getLog(BnOAServlet.class);
    
    /**
     * 请求路径：http://..../c/document_library/get_file、http://..../image/image_gallery等
     * 不同路径调用不同处理类
     * @param request 请求头
     * @param response 响应头
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
        IOException {
        request.setCharacterEncoding("UTF-8");
        // =================处理界面传入参数===================
        String uri = request.getRequestURI();
        String urlStr = uri.substring(uri.lastIndexOf("/") + 1);
        LinkTransfer linkTransfer = LinkTransferFactory.getLinkTransfer(urlStr);
        //链接格式符合要求，初始化相应的处理类
        if (linkTransfer != null) {
            try {
                linkTransfer.transferByRequest(request, response);
            } catch (TipException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                // 返回信息
                response.setContentType("text/html; charset=utf-8");
                PrintWriter out = response.getWriter();
                out.write(e.getMessage());
                out.flush();
                out.close();
            }
        }
        // =================处理返回信息===================
        if (!response.isCommitted()) {
            // 返回信息
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write("");
            out.flush();
            out.close();
        }
    }
}
