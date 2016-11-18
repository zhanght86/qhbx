package com.rh.oa.gw.util;

/**
 * 正文文件类型
 * @author yangjy
 * 
 */
public interface ZwType {
    /**
     * 
     * @return 文件类型编号
     */
    String getCode();

    /**
     * 
     * @return 正文名称
     */
    String getName();

    /**
     * 
     * @return 正文排序号
     */
    int getSort();
}
