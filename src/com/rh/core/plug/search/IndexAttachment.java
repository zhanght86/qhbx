package com.rh.core.plug.search;

/**
 * index attachment 索引文件信息
 */
public class IndexAttachment {

    private String id;
    private String indexPath;
    private String title;
    private String accessPath;
    private String extension;

    /**
     * get attachment id
     * @return att id string
     */

    /**
     * get attachment id
     * @return att id string
     */
    public String getId() {
        return id;
    }

    /**
     * set attachment id
     * @param idStr id string
     */
    public void setId(String idStr) {
        id = idStr;
    }

    /**
     * @return 索引文件路径。创建索引时从此路径访问文件，并解析文件内容。
     */
    public String getIndexPath() {
        return indexPath;
    }

    /**
     * @param pathValue 索引文件路径，创建索引时从此路径访问文件，并解析文件内容。
     */
    public void setIndexPath(String pathValue) {
        indexPath = pathValue;
    }

    /**
     * get attachment title
     * @return title string
     */
    public String getTitle() {
        return title;
    }

    /**
     * set attachment title
     * @param titleVal title string
     */
    public void setTitle(String titleVal) {
        title = titleVal;
    }

    /**
     * @return 文件访问路径。文件查询结果页面，从此连接就可以打开文件。
     */
    public String getAccessPath() {
        return accessPath;
    }

    /**
     * 
     * @param accessPath 文件访问路径。文件查询结果页面，从此连接就可以打开文件。
     */
    public void setAccessPath(String accessPath) {
        this.accessPath = accessPath;
    }

    /**
     * 
     * @return 文件扩展名
     */
    public String getExtension() {
        return extension;
    }
    
    /**
     * 
     * @param extension 文件扩展名
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }
}
