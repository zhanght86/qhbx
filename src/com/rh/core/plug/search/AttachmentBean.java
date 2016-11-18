package com.rh.core.plug.search;

import com.rh.core.base.Bean;
import com.rh.core.plug.search.client.RhSearchClient;

/**
 * 搜索附件bean
 * @author zhl
 *
 */
public class AttachmentBean extends Bean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 私有化构造方法
     */
    public AttachmentBean() {

	}
    /**
     * 构造方法
     * @param attBean 数据对象
     */
	public AttachmentBean(Bean attBean) {
		super(attBean);
	}
	
	/**
	 * 设置附件索引
	 * @param index 附件索引
	 */
	public void setIndex(int index) {
		this.set(RhSearchClient.ATTACHMENT_INDEX, index);
	}
	/**
	 * 设置附件内容
	 * @param content 附件内容
	 */
	public void setContent(String content) {
		this.set(RhSearchClient.ATTACHMENT_CONTENT, content);
	}
	/**
	 * 设置附件路径
	 * @param path 附件路径
	 */
	public void setPath(String path) {
		this.set(RhSearchClient.ATTACHMENT_PATH, path);
	}
	/**
	 * 设置附件类型
	 * @param mtype 附件类型
	 */
	public void setMtype(String mtype) {
		this.set(RhSearchClient.ATTACHMENT_MTYPE, mtype);
	}
	/**
	 * 获取附件标题
	 * @param title 附件标题
	 */
	public void setTitle(String title) {
		this.set(RhSearchClient.ATTACHMENT_TITLE, title);
	}
	/**
	 * 设置附件id
	 * @param attid 附件id
	 */
	public void setAttId(String attid) {
		this.set(RhSearchClient.ATTACHMENT_ID, attid);
	}
	/**
	 * 获取附件id
	 * @return 附件id
	 */
	public String getAttId() {
		return this.getStr(RhSearchClient.ATTACHMENT_ID);
	}
    /**
     * 获取附件标题
     * @return 返回附件标题
     */
	public String getTitle() {
		return this.getStr(RhSearchClient.ATTACHMENT_TITLE);
	}
    /**
     * 获取附件路径
     * @return 附件路径
     */
	public String getPath() {
		return this.getStr(RhSearchClient.ATTACHMENT_PATH);
	}
    /**
     * 获取附件类型
     * @return 返回附件类型
     */
	public String getMtype() {
		return this.getStr(RhSearchClient.ATTACHMENT_MTYPE);
	}
    /**
     * 获取附件索引
     * @return 返回附件索引
     */
	public int getIndex() {
		return this.getInt(RhSearchClient.ATTACHMENT_INDEX);
	}
    /**
     * 获取附件内容
     * @return 返回附件内容
     */
	public String getContent() {
		return this.getStr(RhSearchClient.ATTACHMENT_CONTENT);
	}
    /**
     * 根据附件类型 获取对应的图标
     * @return 返回附件图标
     */
	public String getIcon() {
	    String attMtype = this.getMtype();
		String icon = "";
		if (attMtype.equals("xls") || attMtype.equals("xlsx")) {
			icon = "icon-excel";
		} else if (attMtype.endsWith("swf") || attMtype.endsWith("flv")) {
			icon = "icon-flash";
		} else if (attMtype.endsWith("pdf")) {
			icon = "icon-pdf";
		} else if (attMtype.endsWith("ppt")) {
			icon = "icon-ppt";
		} else if (attMtype.endsWith("doc") || attMtype.endsWith("docx") || attMtype.endsWith("xdoc")) {
			icon = "icon-word";
		} else if (attMtype.endsWith("txt") || attMtype.endsWith("asc")) {
			icon = "icon-txt";
		} else if (attMtype.endsWith("gif") || attMtype.endsWith("png") || attMtype.endsWith("jpg")
				|| attMtype.endsWith("jpeg")) {
			icon = "icon-image";
		} else if (attMtype.endsWith("zip") || attMtype.endsWith("rar") || attMtype.endsWith("7z")
				|| attMtype.endsWith("gzip") || attMtype.endsWith("tar") || attMtype.endsWith("gz")) {
			icon = "icon-zip";
		} else {
			icon = "icon-unknown";
		}
	   return icon;
	}	  
}
