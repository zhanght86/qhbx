/*
 * Copyright (c) 2012 Ruaho All rights reserved.
 */
package com.rh.core.plug.search.client;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Context;

/**
 * ruaho suggestion service client
 * 
 * @author liwei
 * 
 */
public class RhSuggestionClient {

	private static RhSuggestionClient instance = new RhSuggestionClient();
	
	private static final String RHSUGGEST_SERVER = "SY_PLUG_SEARCH_SUGGEST_SERVER";

	/**
	 * can not new instance
	 */
	private RhSuggestionClient() {

	}
	
	/**
	 * singleton
	 * @return instance
	 */
	public static RhSuggestionClient getInstance() {
		return instance;
	}

	/** log */
//	private static Log log = LogFactory.getLog(RhSuggestionClient.class);

	/**
	 * add new item into suggestion server. <br>
	 * we do not add this target item, if it extis<br>
	 * We need to call <CODE>commit</CODE>, to take new item effect .
	 * @param item new item
	 */
	public void addItem(String item) {
		List<String> items = new ArrayList<String>();
		items.add(item);
		addItems(items);
	}

	/**
	 * add new items into suggestion server. <br>
	 * we will not add, If the item repeated <br>
	 * We need to call <CODE>commit</CODE>, to take new item effect .
	 * @param items new item list
	 */
	public void addItems(List<String> items) {
		// FIX ME
	}

	/**
	 * delete targets item from suggestion server. <br>
	 * We need to call <CODE>commit</CODE>, to take new item effect .
	 * @param item the item  will be deleted
	 */
	public void deleteItem(String item) {
		List<String> items = new ArrayList<String>();
		items.add(item);
		deleteItems(items);
	}

	/**
	 * delete targets item from suggestion server. <br>
	 * We need to call <CODE>commit</CODE>, to take new item effect .
	 * @param items the items list will be deleted
	 */
	public void deleteItems(List<String> items) {
		// FIX ME
	}

	/**
	 * commit 
	 * @param items
	 */
	public  void commit() {
		// FIX ME
	}

	/**
	 * get suggestion server uri
	 * 
	 * @return server uri
	 */
	public String getServerUri() {
	//	if(true) return "http://172.16.0.110:8888/searchserver/suggestion";
		String result = "";
		result = Context.getSyConf(RHSUGGEST_SERVER, "http://staff.zotn.com:8888/searchserver/suggestion");
		if (!result.endsWith("/")) {
			result += "/";
		}
		return result;
	}

}
