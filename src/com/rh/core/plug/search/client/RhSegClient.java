/*
 * Copyright (c) 2012 Ruaho All rights reserved.
 */
package com.rh.core.plug.search.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Context;
import com.rh.core.util.http.HttpUtils;

/**
 * ruaho seg service client
 * 
 * @author liwei
 * 
 */
public class RhSegClient {

	private static RhSegClient instance = new RhSegClient();
	
	private static final String  RHSEG_SERVER = "SY_PLUG_SEARCH_SEG_SERVER";

	/**
	 * can not new instance
	 */
	private RhSegClient() {

	}

	/**
	 * singleton
	 * @return instance
	 */
	public static RhSegClient getInstance() {
		return instance;
	}

	/** log */
//	private static Log log = LogFactory.getLog(RhSegClient.class);

	/**
	 * add new word into seg server. <br>
	 * we do not add this target word, if it extis<br>
	 * We need to call <CODE>commit</CODE>, to take new word effect .
	 * @param word new word
	 */
	public void addWord(String word) {
		List<String> words = new ArrayList<String>();
		words.add(word);
		addWords(words);
	}

	/**
	 * add new words into seg server. <br>
	 * we will not add, If the word repeated <br>
	 * We need to call <CODE>commit</CODE>, to take new word effect .
	 * @param words new word list
	 */
	public void addWords(List<String> words) {
		if (null == words || 0 == words.size()) {
			return;
		}
		String xml = " <?xml version=\"1.0\" encoding=\"UTF-8\"?><add>";
		for (String word : words) {
			xml += "<word>";
			xml += word;
			xml += "</word>";
		}
		xml += "</add>";
		//encode xml string
		try {
			xml = URLEncoder.encode(xml, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		InputStream is = new ByteArrayInputStream(xml.getBytes());

		try {
			HttpUtils.httpPost(getServerUri(), is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * delete targets word from seg server. <br>
	 * We need to call <CODE>commit</CODE>, to take new word effect .
	 * @param word the word will be deleted
	 */
	public void deleteWord(String word) {
		List<String> words = new ArrayList<String>();
		words.add(word);
		deleteWords(words);
	}

	/**
	 * delete targets word from seg server. <br>
	 * We need to call <CODE>commit</CODE>, to take new word effect .
	 * @param words the words list will be deleted
	 */
	public void deleteWords(List<String> words) {
		// FIX ME
	}

	/**
	 * commit
	 * @param words
	 */
	public void commit() {
		// FIX ME
	}

	/**
	 * get seg server uri
	 * 
	 * @return server uri
	 */
	private String getServerUri() {
	//	if (true) return "http://172.16.0.110:8888/searchserver/seg";
		String result = "";
		result = Context.getSyConf(RHSEG_SERVER, "http://staff.zotn.com:8888/searchserver/seg");
		if (!result.endsWith("/")) {
			result += "/";
		}
		return result;
	}

	/**
     * 测试方法 
     * @param args 入参
	 * @throws UnsupportedEncodingException 
     */
	public static void main(String [] args) throws UnsupportedEncodingException {
		String src = "王将军出发了";
		String xml = URLEncoder.encode(src, "UTF-8");
		System.out.println(xml);
	}
}
