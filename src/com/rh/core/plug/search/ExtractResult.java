package com.rh.core.plug.search;

/**
 * extract result
 * @author liwei
 * 
 */
public class ExtractResult {

	/**
	 * 
	 */
	public ExtractResult() {

	}

	/**
	 * get content text
	 * @return content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * set content text
	 * @param text - content text
	 */
	public void setContent(String text) {
		this.content = text;
	}

	/**
	 * get mime type
	 * @return mime type
	 */
	public String getMType() {
		return mType;
	}

	/**
	 * set content mime type
	 * @param type - mime type
	 */
	public void setMType(String type) {
		this.mType = type;
	}

	private String content = "";
	private String mType = "";

}