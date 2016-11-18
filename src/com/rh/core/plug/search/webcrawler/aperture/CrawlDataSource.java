package com.rh.core.plug.search.webcrawler.aperture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liwei
 * 
 */
public class CrawlDataSource {
    private String id = "";
    private String url = "";
    private String name = "";
    private String titlePreTag = "";
    private String titlePostTag = "";
    private String titleRegexp = "";
    private String contentPreTag = "";
    private String contentPostTag = "";
    private String contentRegexp = "";
    private List<String> granteeUsers = new ArrayList<String>();
    private List<String> includeList = new ArrayList<String>();
    private List<String> excludeList = new ArrayList<String>();
    // categorys mapping set up
    private Map<String, String> categoryMapping = new HashMap<String, String>();
    
    /**
     * 是否需要对文本进行截取
     * @return result
     */
    public boolean subText() {
        if (subTitleText()) {
            return true;
        } 
        if (subContentText()) {
            return true;
        } 
        return false;
    }
    
    /**
     * 是否需要对标题进行截取
     * @return result
     */
    public boolean subTitleText() {
        if (0 < titlePreTag.length() && 0 < titlePostTag.length()) {
            return true; 
        }
        if (0 < titleRegexp.length()) {
            return true;
        }
        return false;
    }
    
    /**
     * 是否需要对内容进行截取
     * @return result
     */
    public boolean subContentText() {
        if (0 < contentPreTag.length() && 0 < contentPostTag.length()) {
            return true; 
        }
        if (0 < contentRegexp.length()) {
            return true;
        }
        return false;
    }
    
    /**
     *  get site id
     * @return id 
     */
    public String getId() {
        return id;
    }

    /**
     * set site id
     * @param id - id 
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * get site url
     * @return url
     */
    public String getUrl() {
        return url;
    }
    
    /**
     * set url
     * @param url - url
     */
    public void setUrl(String url) {
        this.url = url;
    }
    
    /**
     * get name
     * @return - name string
     */
    public String getName() {
        return name;
    }
    
    /**
     * set name 
     * @param name - name string
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * get tile prefix tag
     * @return - tag string
     */
    public String getTitlePreTag() {
        return titlePreTag;
    }
    /**
     * set title prefix tag
     * @param titlePreTag - tag string
     */
    public void setTitlePreTag(String titlePreTag) {
        this.titlePreTag = titlePreTag;
    }
    /**
     * get title postfix tag
     * @return - tag str
     */
    public String getTitlePostTag() {
        return titlePostTag;
    }
    /**
     * set title postfix tag
     * @param titlePostTag - tag str
     */
    public void setTitlePostTag(String titlePostTag) {
        this.titlePostTag = titlePostTag;
    }
    
    /**
     * get title regexp
     * @return regexp pattern
     */
    public String getTitleRegexp() {
        return titleRegexp;
    }
    /**
     * set title Pattern
     * @param titleRegexp - pattern
     */
    public void setTitleRegexp(String titleRegexp) {
        this.titleRegexp = titleRegexp;
    }
    
    /**
     * get content prefix tag
     * @return - tag str
     */
    public String getContentPreTag() {
        return contentPreTag;
    }
    
    /**
     * set content prefix tag in html page
     * @param contentPreTag - tag str
     */
    public void setContentPreTag(String contentPreTag) {
        this.contentPreTag = contentPreTag;
    }
    /**
     * get content postfix tag
     * @return - tag str
     */
    public String getContentPostTag() {
        return contentPostTag;
    }
    /**
     * set content postfix tag
     * @param contentPostTag - tag str
     */
    public void setContentPostTag(String contentPostTag) {
        this.contentPostTag = contentPostTag;
    }
    /**
     * get content regexp
     * @return - content pattern 
     */
    public String getContentRegexp() {
        return contentRegexp;
    }
    /**
     * set content expresstion pattern
     * @param contentRegexp - pattern str
     */
    public void setContentRegexp(String contentRegexp) {
        this.contentRegexp = contentRegexp;
    }
    /**
     * grantee users
     * @return - users list
     */
    public List<String> getGranteeUsers() {
        return granteeUsers;
    }
    /**
     * set grantee users
     * @param granteeUsers - users list
     */
    public void setGranteeUsers(List<String> granteeUsers) {
        this.granteeUsers = granteeUsers;
    }
    /**
     * get include path (expresstion pattern) list
     * @return - path list
     */
    public List<String> getIncludeList() {
        return includeList;
    }
    /**
     * set include path (expresstion pattern) list
     * @param includeList - path list
     */
    public void setIncludeList(List<String> includeList) {
        this.includeList = includeList;
    }
    /**
     * get exclude url pattern list
     * @return - pattern list
     */
    public List<String> getExcludeList() {
        return excludeList;
    }
    /**
     * set exclude url pattern list
     * @param excludeList - pattern list
     */
    public void setExcludeList(List<String> excludeList) {
        this.excludeList = excludeList;
    }
    /**
     * get category mapping
     * key:name, value:pattern
     * @return - category mapping.
     */
    public Map<String, String> getCategoryMapping() {
        return categoryMapping;
    }
    /**
     * set category mapping
     * key:name, value:pattern
     * @param categoryMapping - category mapping
     */
    public void setCategoryMapping(Map<String, String> categoryMapping) {
        this.categoryMapping = categoryMapping;
    }
   

}
