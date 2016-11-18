package com.rh.oa.zh.cw.service;

/**
 * 财务写入流经表数据接口
 * @author ruaho_hdy
 *
 */
public interface CwFlowServ {

    /**
     * 返回报文格式
     * <?xml version="1.0" encoding="UTF-8"?>
     *  <result>
     *      <status>Y</status>
            <message>successful</message>
            <description>描述</description>
     *  </result>
     * 
     * 添加流经数据
     * @param workNum 用户工号
     * @param dataId oa主单数据主键
     * @return 成功报文
     */
    public String addFlow(String workNum, String dataId);
}
