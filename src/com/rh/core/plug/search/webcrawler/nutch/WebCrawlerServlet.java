/*
 * Copyright (c) 2011 Ruaho All rights reserved.
 */
package com.rh.core.plug.search.webcrawler.nutch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.TipException;

/**
 * web crawler Servlet
 * @author liwei
 */
public class WebCrawlerServlet extends HttpServlet {

	/** log */
	private static Log log = LogFactory.getLog(WebCrawlerServlet.class);
	/** UID */
	private static final long serialVersionUID = 1L;
	/** default http response client error code */
	private static final int RESPONSE_CLIENT_ERROR_CODE = 400;
	/** default http response server error code */
	private static final int RESPONSE_SERVER_ERROR_CODE = 500;

	/**
	 * @param request 请求头
	 * @param response 响应头
	 * @throws ServletException ServletException
	 * @throws IOException IOException
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		request.setCharacterEncoding("UTF-8");
	}

	/**
	 * download crawler config file
	 * @param request 请求头
	 * @param response 响应头
	 * @throws ServletException ServletException
	 * @throws IOException IOException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String conf = request.getParameter("conf");
		if (null == conf || 0 == conf.length()) {
			conf = "url_filter";
		}
		// use restful uri
		request.setCharacterEncoding("UTF-8");
		String configText = "";
		try {
			// response.setContentType(downFileType);

			if (conf.equals("crawl_url")) {
				response.setContentType("text");
				configText = new WebCrawlerConfServ().getCrawlUrlConf();
			} else if (conf.equals("url_filter")) {
				response.setContentType("text");
				configText = new WebCrawlerConfServ().getUrlFilterConf();
			} else if (conf.equals("rh_index")) {
				response.setContentType("text/xml");
				configText = new WebCrawlerConfServ().getIndexConf();
			}

			// set response file content
			byte[] rtnBytes = configText.getBytes();
			response.reset();
			response.setContentLength(rtnBytes.length);
			response.getOutputStream().write(rtnBytes);
			response.flushBuffer();
		} catch (TipException e) { // 不记录Log，直接传递错误信息给界面
			if (log.isDebugEnabled()) { // 调试模式下也输出exception到log中
				log.debug(e.getMessage(), e);
			}
			response.setStatus(RESPONSE_CLIENT_ERROR_CODE);
			response.setContentType("text/html");
			OutputStream out = response.getOutputStream();
			out.write(e.getMessage().getBytes());
			out.flush();
		} catch (FileNotFoundException fe) {
			log.error(fe.getMessage(), fe);
			response.setStatus(RESPONSE_SERVER_ERROR_CODE);
			response.setContentType("text/html");
			OutputStream out = response.getOutputStream();
			out.write("file not found in the server.".getBytes());
			out.flush();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setStatus(RESPONSE_SERVER_ERROR_CODE);
			response.setContentType("text/html");
			OutputStream out = response.getOutputStream();
			out.write(e.getMessage().getBytes());
			out.flush();
		}

	}

}
