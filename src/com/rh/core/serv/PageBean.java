package com.rh.core.serv;

import com.rh.core.base.Bean;
import com.rh.core.util.Constant;

/**
 * 
 * @author yangjy
 * 
 */
public class PageBean extends Bean {
    /**
     * 
     */
    private static final long serialVersionUID = 100928673463997710L;

    /**
     * 构造函数
     */
    public PageBean() {
        // 设置默认值
        this.set(Constant.SHOWNUM, 50);
        this.set(Constant.NOWPAGE, 1);
        this.set(Constant.ALLNUM, 0);
    }
    
    /**
     * 
     * @param bean 数据Bean
     */
    public PageBean(Bean bean) {
        super(bean);
        if (this.isEmpty(Constant.SHOWNUM)) {
            this.set(Constant.SHOWNUM, 50);
        }

        if (this.isEmpty(Constant.NOWPAGE)) {
            this.set(Constant.NOWPAGE, 1);
        }
    }
    
    /**
     * 
     * @return 当前页
     */
    public int getNowPage() {
        return this.get(Constant.NOWPAGE, 1);
    }

    /**
     * 
     * @param page 当前页码，从1开始
     * @return 分页对象
     */
    public PageBean setNowPage(int page) {
        this.set(Constant.NOWPAGE, page);
        return this;
    }
    
    /**
     * 
     * @return 取得总页数
     */
    public int getPages() {
        return this.get(Constant.PAGES, 1);
    }

    /**
     * 
     * @param pages 设置共有多少页
     * @return 分页对象
     */
    public PageBean setPages(int pages) {
        this.set(Constant.PAGES, pages);
        return this;
    }
    
    /**
     * 
     * @return 每页显示记录数
     */
    public int getShowNum() {
        return this.get(Constant.SHOWNUM, 50);
    }

    /**
     * 
     * @param showNum 每页显示多少条
     * @return 分页对象
     */
    public PageBean setShowNum(int showNum) {
        this.set(Constant.SHOWNUM, showNum);
        return this;
    }
    
    /**
     * 
     * @return 返回总记录数
     */
    public int getAllNum() {
        return this.get(Constant.ALLNUM, 0);
    }

    /**
     * 
     * @param allNum 总记录数
     * @return 分页对象
     */
    public PageBean setAllNum(int allNum) {
        this.set(Constant.ALLNUM, allNum);
        int pages = 0;
        if (allNum > 0) {
            int showNum = this.getShowNum();
            if (allNum % showNum == 0) {
                pages = allNum / showNum;
            } else {
                pages = allNum / showNum + 1;
            }
        }

        this.setPages(pages);
        return this;
    }
    
    /**
     * 
     * @return 记录偏移量
     */
    public int getOffset() {
        int page = this.getNowPage();
        int showNum = this.getShowNum();
        int offset = (page - 1) * showNum;

        return offset;
    }
}
