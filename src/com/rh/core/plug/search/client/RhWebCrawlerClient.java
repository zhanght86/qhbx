/*
 * Copyright (c) 2012 Ruaho All rights reserved.
 */
package com.rh.core.plug.search.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.rh.core.base.Context;
import com.rh.core.plug.search.webcrawler.nutch.WebCrawlerConfServ;
import com.rh.core.util.http.HttpUtils;

/**
 * ruaho web crawler service client
 * 
 * @author liwei
 * 
 */
public class RhWebCrawlerClient {

	private static RhWebCrawlerClient instance = new RhWebCrawlerClient();

	private static final String RH_WEBCRAWLER_SERVER = "SY_PLUG_SEARCH_WEBCRAWLER_SERVER";

	/**
	 * can not new instance
	 */
	private RhWebCrawlerClient() {

	}

	/**
	 * singleton
	 * @return instance
	 */
	public static RhWebCrawlerClient getInstance() {
		return instance;
	}

	/**
	 * upload webcrawler config
	 * @throws IOException IO例外
	 */
	public void uploadConfig() throws IOException {
		WebCrawlerConfServ confServ = new WebCrawlerConfServ();

		// upload index config
		String configStr = confServ.getIndexConf();
		String uri = getServerUri() + "config/rh_index";
		InputStream is = new ByteArrayInputStream(configStr.getBytes());
		HttpUtils.httpPost(uri, is);
		IOUtils.closeQuietly(is);

		// upload crawl url config
		configStr = confServ.getCrawlUrlConf();
		uri = getServerUri() + "config/crawl_url";
		is = new ByteArrayInputStream(configStr.getBytes());
		HttpUtils.httpPost(uri, is);
		IOUtils.closeQuietly(is);

		// upload url filter config
		configStr = confServ.getUrlFilterConf();
		uri = getServerUri() + "config/url_filter";
		is = new ByteArrayInputStream(configStr.getBytes());
		HttpUtils.httpPost(uri, is);
		IOUtils.closeQuietly(is);

	}

	/**
	 * crawl web site
	 * @param id - crawl id
	 * @param depth - crawl depth
	 * @param topN - crawl topN
	 * @param threads - crawl threads number
	 * @throws Exception throw exception , if requet target url
	 */
	public void crawl(String id, int depth, int topN, int threads) throws Exception {
		String uri = getServerUri() + "operation/crawl?id=" + id;
		uri += "&depth=" + depth;
		uri += "&topN=" + topN;
		uri += "&threads=" + threads;
		HttpUtils.httpGet(uri);
	}

	/**
	 * send index data to solr server
	 * @param id crawl id
	 * @throws Exception throw exception , if requet target url
	 */
	public void commit(String id) throws Exception {
	    RhSearchClient rsc = new RhSearchClient();
	    rsc.commit();
	}

	/**
	 * get webcrawler server uri
	 * 
	 * @return server uri
	 */
	public String getServerUri() {
//		if (true) {
//			return "http://172.16.0.110:1976/";
//		}
		String result = "";
		result = Context.getSyConf(RH_WEBCRAWLER_SERVER, "http://staff.zotn.com:1976");
		if (!result.endsWith("/")) {
			result += "/";
		}
		return result;
	}

}
