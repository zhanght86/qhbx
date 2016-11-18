package com.rh.oa.cd;


/**
 * 自动催督办的Bean对象
 * 
 * @author cuihf
 *
 */
public class AutoBean {
    
    private String servId;
    
    private String dataId;
    
    private String acptUser;
    
    private String autoDate;
    
    private int autoInterval;
    
    private String title;
    
    private String draftUser;

    /**
     * 获得服务ID     
     * @return 服务ID
     */
    public String getServId() {
        return servId;
    }

    /**
     * 设置服务ID
     * @param servId 服务ID
     */
    public void setServId(String servId) {
        this.servId = servId;
    }

    /**
     * 获得数据ID
     * @return 数据ID
     */
    public String getDataId() {
        return dataId;
    }

    /**
     * 设置数据ID
     * @param dataId 数据ID
     */
    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    /**
     * 获取接收用户编码
     * @return 接收用户编码
     */
    public String getAcptUser() {
        return acptUser;
    }

    /**
     * 设置接收用户编码
     * @param acptUser 接收用户编码
     */
    public void setAcptUser(String acptUser) {
        this.acptUser = acptUser;
    }

    /**
     * 获取自动催督办日期
     * @return 自动催督办日期
     */
    public String getAutoDate() {
        return autoDate;
    }

    /**
     * 设置自动催督办日期
     * @param autoDate 自动催督办日期
     */
    public void setAutoDate(String autoDate) {
        this.autoDate = autoDate;
    }

    /**
     * 获取自动催督办日期间隔
     * @return 自动催督办日期间隔
     */
    public int getAutoInterval() {
        return autoInterval;
    }

    /**
     * 设置自动催督办日期间隔
     * @param autoInterval 自动催督办日期间隔
     */
    public void setAutoInterval(int autoInterval) {
        this.autoInterval = autoInterval;
    }
    
    /**
     * 获取催督办标题
     * @return 催督办标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置催督办标题
     * @param title 催督办标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取起草人编码
     * @return 起草人编码
     */
    public String getDraftUser() {
        return draftUser;
    }

    /**
     * 设置起草人编码
     * @param draftUser 起草人编码
     */
    public void setDraftUser(String draftUser) {
        this.draftUser = draftUser;
    }
}
