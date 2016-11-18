package com.rh.core.plug.search.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.comm.FileMgr;
import com.rh.core.plug.search.client.RhFileClient;
import com.rh.core.util.Strings;
import com.rh.core.util.http.HttpGetResponse;
import com.rh.core.util.http.HttpUtils;

/**
 * 
 *
 */
public class SearchContext {
    /** log */
    private static Log log = LogFactory.getLog(SearchContext.class);
	/** 服务定义编码 */
	@SuppressWarnings("unused")
    private String serv = null;
	/** 数据信息 */
	private Bean data = null;

	/**
	 * 构建体方法
	 * @param servId 服务定义编码
	 * @param dataBean 数据信息
	 */
	public SearchContext(String servId, Bean dataBean) {
	    this.serv = servId;
		this.data = dataBean;
	}

	/**
	 * 得到数据某个字段中HTML代码的图片地址
	 * @param fieldName 字段名
	 * @param pos 第一个图片
	 * @param cacheFlag 是否缓存图片
	 * @return 图片URL地址
	 */
	public String getHtmlImgUrl(String fieldName, int pos, boolean cacheFlag) {
	    String result = "";
	    if (data.contains(fieldName)) {
	        String content = data.getStr(fieldName);
	        String pn = "<img.+src=[\"\']?([^\"\'> ]+)[\"\']?.*>";
	        Pattern pattern = Pattern.compile(pn, Pattern.CASE_INSENSITIVE);
	        Matcher mt = pattern.matcher(content);
	        for (int i = 1; mt.find(); i++) {
	            if (i == pos) {
	                result = mt.group(1);
	                if ((result.length() > 0) && cacheFlag) { //缓存图片
	                    try {
	                        if (!result.startsWith("http")) { //内部图片
	                            result = RhFileClient.getInstance().upload(
	                                    FileMgr.download(FileMgr.getFile(result)), "");
	                        } else { //公共图片路径
	                            HttpGetResponse resp = HttpUtils.httpGet(result);
	                            result = RhFileClient.getInstance().upload(resp.getInputStream(), "");
	                        }
                        } catch (Exception e) {
                            log.error(e.getMessage() + "  url:" + result, e);
                        }
	                }
	                break;
	            }
	        }
	    }
	    return result;
	}
	
	/**
     * 得到数据某个字段中的图片地址
     * @param fieldName 字段名
     * @param cacheFlag 是否缓存图片
     * @return 图片URL地址
     */
    public String getFileImgUrl(String fieldName, boolean cacheFlag) {
        String result = "";
        if (!data.isEmpty(fieldName)) {
            String img = Strings.getFirstBySep(data.getStr(fieldName));
            if (cacheFlag) { //缓存图片
                try {
                    result = RhFileClient.getInstance().upload(
                            FileMgr.download(FileMgr.getFile(img)), "");
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            } else { //不缓存图片
                if (img.length() > 0) {
                    result = "/file/" + img;
                }
            }
        }
        return result;
    }
}
