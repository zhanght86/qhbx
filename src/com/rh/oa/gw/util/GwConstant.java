package com.rh.oa.gw.util;

/**
 * 公文常量类
 * 
 * @author wangl
 * 
 */
public class GwConstant {
    /** 盖章非加密正文编码 */
    public static final String NO_ENC_ZHENGWEN = "NOENCZHENGWEN";
    /**附件**/
    public static final String FUJIAN = "FUJIAN";
    /**正文**/
    public static final String ZHENGWEN = "ZHENGWEN";
    
    /** 字段名：文件名 **/
    public static final String FILE_NAME = "FILE_NAME";
    /** 字段名：正文小类型 **/
    public static final String ITEM_CODE = "ITEM_CODE";
    /** 字段名：文件大类型，如正文、附件、转发原文等 **/
    public static final String FILE_CAT = "FILE_CAT";
    /** 字段名： 显示名称 **/
    public static final String DIS_NAME = "DIS_NAME";
    /** 字段名：文件排序号 **/
    public static final String FILE_SORT = "FILE_SORT";

    /** 公文模板服务 **/
    public static final String OA_GW_TMPL = "OA_GW_TMPL";

    /** 机关代字服务 **/
    public static final String OA_GW_CODE = "OA_GW_CODE";

    /** 公文服务 **/
    public static final String OA_GW_GONGWEN = "OA_GW_GONGWEN";
    
    /** 发文服务  */
    public static final String OA_GW_TYPE_FW = "OA_GW_TYPE_FW";
    
    /** 公文控制参数 **/
    public static final String GW_PARAM = "_GW_CTL_PARAM_"; 
    
    /** 是否存在盖章文件**/
    public static final String EXIST_SEAL_PDF_FILE = "EXIST_SEAL_PDF_FILE"; 
    
    /** 系统配置：公文如果没有最终编号，也保存代字+年度+0号到数据库中 ；默认值为true**/
    public static final String CONF_SAVE_CODE_WHEN_NO_NUM = "GW_SAVE_CODE_WHEN_NO_NUM";
    
    /**部门印章管理服务*/
    public static final String OA_SEAL_DEPTS = "OA_SEAL_DEPTS";
}
