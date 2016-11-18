package com.rh.bn.xml;

import org.xml.sax.Attributes;

/**
 * xml格式数据中提取新闻内容
 * @author tanyh 20151214
 * 
 */
public class XML2NewsReader extends XMLSAXReader {
    /** 新闻内容 **/
    private StringBuffer  newsContent = new StringBuffer("");

    /** 记录上一节点的名称 **/
    private String preTagName = null;

    /**
     * 构造方法
     */
    public XML2NewsReader() {
        super();
    }

    @Override
    protected void afterStartDocument() {

    }

    @Override
    protected void afterEndDocument() {

    }

    @Override
    protected void afterStartElement(String uri,
            String localName,
            String qName,
            Attributes attributes) {
        if ("static-content".equals(qName)) {
            preTagName = qName;
        }
    }

    @Override
    protected void afterEndElement(String uri, String localName, String qName) {
        if ("static-content".equals(qName)) {
            preTagName = null;
        }
    }

    @Override
    protected void afterCharacters(char[] ch, int start, int length) {
        if (preTagName != null && !"".equals(preTagName)) {
            newsContent.append(new String(ch, start, length));
        }
    }

    /**
     * 获取新闻内容
     * @return String 
     */
    public String getNewsContent() {
        return newsContent.toString();
    }
}
