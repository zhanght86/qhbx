package com.rh.core.comm.xdoc;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.hg.xdoc.XDoc;
import com.hg.xdoc.XDocClient;
import com.hg.xdoc.XDocIO;
import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.Context.APP;
import com.rh.core.base.TipException;
import com.rh.core.serv.CommonServ;

/**
 * XDOC格式化文件流输出
 * 
 * @author hdy
 * 
 */
public class OutputXdoc extends CommonServ {

	/**
	 * XDOC文件格式化输出流
	 * 
	 * @param paramBean
	 *            XDOC模板参数
	 */
	public void outputXdoc(Bean paramBean) {

		// 文件类型
		HashMap<String, String> fileType = new HashMap<String, String>();
		fileType.put("xls", "application/msexcel");
		fileType.put("xlsx", "application/msexcel");
		fileType.put("doc", "application/msword");
		fileType.put("docx", "application/msword");
		fileType.put("html", "text/html");
		fileType.put("pdf", "application/pdf");
		fileType.put("wps", "application/vnd.ms-works");
		fileType.put("txt", "text/plain");
		// 设置文件mime类型
		String format = paramBean.getStr("format").isEmpty() ? "pdf"
				: paramBean.getStr("format");
		if (format.equals("doc")) { // 统一word文档格式
			format = "docx";
		}

		// 获取文件名
		String fileName = paramBean.getStr("fileName").isEmpty() ? "expFile"
				: paramBean.getStr("fileName");
		String fileFullName = fileName + "." + format;
		try {
			XDoc xdoc = null;
			/*
			 * //如果是通过xdoc service访问的，则通过xdoc client获取xdoc文件流 if
			 * (paramBean.isNotEmpty("USE_XDOC_SERVICE")) { XDocClient client =
			 * new XDocClient(paramBean.getStr("URL")); xdoc =
			 * XDocIO.read(client.run(paramBean.getStr("filePath"), paramBean));
			 * } else { //初始化XDOC文件 xdoc =
			 * XDocIO.read(Context.appStr(APP.SYSPATH) +
			 * paramBean.getStr("filePath")).run(paramBean); } //输出XDOC格式文件流
			 * XDocIO.write(xdoc, response.getOutputStream(), format);
			 */
			if (paramBean.isNotEmpty("USE_XDOC_SERVICE")) {
				XDocClient client = new XDocClient(paramBean.getStr("URL"));
				paramBean.set("_format", format);
				InputStream inp = client.run(paramBean.getStr("filePath"), paramBean);
				if (null == inp) {
					throw new TipException("Xdoc Service 没有返回数据");
				}
				OutputStream os = null;
				// 获取servlet响应
				HttpServletResponse response = Context.getResponse();
				try {
					response.setContentType(fileType.get(format) + ";charset=utf-8");
					// 设置文件头
					response.setHeader("Content-Disposition", "attachment; filename="
								+ URLEncoder.encode(fileFullName, "UTF-8"));
					os = response.getOutputStream();
					IOUtils.copyLarge(inp, os);
				} finally {
					IOUtils.closeQuietly(os);
					IOUtils.closeQuietly(inp);
					response.flushBuffer();
				}
			} else {
				// 获取servlet响应
				HttpServletResponse response = Context.getResponse();
				response.resetBuffer();
				response.setContentType(fileType.get(format) + ";charset=utf-8");
				// 设置文件头
				response.setHeader("Content-Disposition", "attachment; filename="
						+ URLEncoder.encode(fileFullName, "UTF-8"));
				// 初始化XDOC文件
				xdoc = XDocIO.read(Context.appStr(APP.SYSPATH) + paramBean.getStr("filePath")).run(paramBean);
				// 输出XDOC格式文件流
				XDocIO.write(xdoc, response.getOutputStream(), format);
			}
		} catch (Exception e) {
			log.error("XDOC文件输出错误：" + e.getMessage(), e);
			throw new TipException("XDOC文件输出错误：" + e.getMessage());
		}
	}
}
