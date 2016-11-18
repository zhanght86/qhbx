/*
 * Copyright (c) 2012 Ruaho All rights reserved.
 */
package com.rh.core.plug.search.webcrawler.nutch;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.rh.core.base.Bean;
import com.rh.core.plug.search.SearchHelper;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;

/**
 * @author liwei
 * 
 */
public class WebCrawlerConfServ extends CommonServ {
	/** */
	public static final String CURRENT_SERV = ServMgr.SY_PLUG_SEARCH_WEBCRAW;

	/**
	 * @return index config str
	 * @throws UnsupportedEncodingException - encoding exception
	 */
	public String getIndexConf() throws UnsupportedEncodingException {
		OutBean sitesBean = finds(new ParamBean(CURRENT_SERV));
		List<Bean> allSites = sitesBean.getDataList();
		return buildIndexConf(allSites);
	}
	
	/**
	 * @return crawl url config str
	 * @throws UnsupportedEncodingException - encoding exception
	 */
	public String getCrawlUrlConf() throws UnsupportedEncodingException {
		OutBean sitesBean = finds(new ParamBean(CURRENT_SERV));
		List<Bean> allSites = sitesBean.getDataList();
		return buildCrawlUrl(allSites);
	}
	
	/**
	 * @return  url filter config str
	 * @throws UnsupportedEncodingException - encoding exception
	 */
	public String getUrlFilterConf() throws UnsupportedEncodingException {
		OutBean sitesBean = finds(new ParamBean(CURRENT_SERV));
		List<Bean> allSites = sitesBean.getDataList();
		return buildCrawlUrlFilter(allSites);
	}
	
	

	/**
	 * builder crawler index config
	 * @param allSites - allSite data
	 * @return conf text
	 * @throws UnsupportedEncodingException encoding exception
	 */
	private String buildIndexConf(List<Bean> allSites) throws UnsupportedEncodingException {
		StringBuilder builder = new StringBuilder();
		builder.append("<?xml version='1.0' encoding='GBK'?>");
		builder.append("<rh-index>");

		for (Bean site : allSites) {
			String name = site.getStr("NAME");
			String host = site.getStr("HOST");
			String categorys = site.getStr("CATEGORYS_CONF");
			builder.append("\n");
			builder.append("<site name='" + name + "' ");
			builder.append(" host='" + host + "'>");
			builder.append("\n");
			// set title conf
			builder.append("<title>");
			builder.append("\n");
			builder.append("<pre-tag><![CDATA[" + site.getStr("TITLE_PRE_TAG") + "]]></pre-tag>");
			builder.append("\n");
			builder.append("<post-tag><![CDATA[" + site.getStr("TITLE_POST_TAG") + "]]></post-tag>");
			builder.append("\n");
			builder.append("<reg-exp><![CDATA[" + site.getStr("TITLE_REGEXP") + "]]></reg-exp>");
			builder.append("\n");
			builder.append("</title>");
			builder.append("\n");
			// set content conf
			builder.append("<content>");
			builder.append("\n");
			builder.append("<pre-tag><![CDATA[" + site.getStr("CONTENT_PRE_TAG") + "]]></pre-tag>");
			builder.append("\n");
			builder.append("<post-tag><![CDATA[" + site.getStr("CONTENT_POST_TAG") + "]]></post-tag>");
			builder.append("\n");
			builder.append("<reg-exp><![CDATA[" + site.getStr("CONTENT_REGEXP") + "]]></reg-exp>");
			builder.append("\n");
			builder.append("</content>");
			builder.append("\n");

			builder.append(categorys);

			// grantee conf
			int granteeType = site.get("GRANTEE_TYPE", 1);
			String granteeValue = site.getStr("GRANTEE");
			if (granteeType == 2 && granteeValue.length() > 0) {
				StringBuilder granteeBuilder = new StringBuilder();
				String[] temp = granteeValue.split(",");
				for (String grantee : temp) {
					String expre = SearchHelper.getCmpyGranteeExpr(grantee);
					granteeBuilder.append(expre);
					granteeBuilder.append(",");
				}

				if (granteeBuilder.length() > 0) {
					builder.append("<grantee>" + granteeBuilder.toString() + "</grantee>");
					builder.append("\n");
				}

			}

			builder.append("</site>");
			builder.append("\n");
		}
		builder.append("</rh-index>");
		return builder.toString();
	}

	/**
	 * builder crawler url config
	 * @param allSites - allSite data
	 * @return conf text
	 */
	private String buildCrawlUrl(List<Bean> allSites) {
		StringBuilder builder = new StringBuilder();
		for (Bean site : allSites) {
			String crawlUrls = getStartCrawlUrl(site);
			builder.append(crawlUrls);
			builder.append("\n");
		}
		return builder.toString();
	}

	/**
	 * get start crawl urls
	 * @param site site bean
	 * @return urls
	 */
	private String getStartCrawlUrl(Bean site) {
		String crawlUrls = site.getStr("CRAWL_URL");
		// if not set crawlUrls ,we will use default host
		if (crawlUrls.trim().length() == 0) {
			String host = site.getStr("HOST");
			if (!host.startsWith("http://")) {
				host = "http://" + host;
			}
			crawlUrls = host;
		}
		return crawlUrls;
	}

	/**
	 * builder crawler url filter config
	 * @param allSites - allSite data
	 * @return conf text
	 */
	private String buildCrawlUrlFilter(List<Bean> allSites) {
		StringBuilder builder = new StringBuilder();
		String base = "-^(file|ftp|mailto):" + "\n"
				+ "-\\.(gif|GIF|jpg|JPG|png|PNG|ico|ICO|css|CSS|sit|SIT|eps|EPS|wmf"
				+ "|WMF|zip|ZIP|ppt|PPT|mpg|MPG|xls|XLS|gz|GZ|rpm|RPM|tgz|TGZ|mov|MOV"
				+ "|exe|EXE|jpeg|JPEG|bmp|BMP|js|JS)$" + "\n" + "-[?*!@=]" + "\n" + "-.*(/[^/]+)/[^/]+\\1/[^/]+\\1/"
				+ "\n";
		builder.append("\n");
		builder.append(base);
		for (Bean site : allSites) {
			String host = site.getStr("HOST");
			String filter = site.getStr("URL_FILTER");
			// append category
			String categorys = site.getStr("CATEGORYS_CONF");
			// find all category expresstion
			String pn = "reg-exp=\"(.+?)\"";
			Pattern pattern = Pattern.compile(pn); // 不区分大小写
			Matcher mt = pattern.matcher(categorys);
			while (mt.find()) {
				String exp = mt.group(1);
				builder.append("+^" + host + exp + "\n");
			}

			// start url
			String startUrls = getStartCrawlUrl(site);
			for (String url : startUrls.split("\n")) {
				builder.append("+^" + url + "$" + "\n");
			}
				builder.append(filter + "\n");
		}
		builder.append("-." + "\n");
		return builder.toString();
	}

}
