package com.rh.bn.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * sax解析xml数据父类
 * @author tanyh 20151214
 * 
 */
public class XMLSAXReader extends DefaultHandler {

    /**
     * 构造方法
     */
    public XMLSAXReader() {
        super();
    }

    /**
     * 解析xml文件
     * @param file xml文件对象
     */
    public void parseFile(File file) {
        try {
            InputStream stream = new FileInputStream(file);
            SAXParserFactory spf = SAXParserFactory.newInstance();

            try {
                SAXParser parser = spf.newSAXParser();
                parser.parse(stream, this);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 解析xml文件
     * @param file xml文件对象
     */
    public void parseStream(InputStream is) {
        SAXParserFactory spf = SAXParserFactory.newInstance();

        try {
            SAXParser parser = spf.newSAXParser();
            parser.parse(is, this);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析xml格式的字符串
     * @param str xml格式的数据
     */
    public void parseString(String str) {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        InputStream stream = new ByteArrayInputStream(str.getBytes());
        try {
            SAXParser parser = spf.newSAXParser();
            parser.parse(stream, this);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 在文档开始的时候调用此方法
     * @throws SAXException 解析异常
     */
    public void startDocument() throws SAXException {
        afterStartDocument();
    }

    /**
     * 文档开始后的扩展方法，子类继承后，覆盖即可
     */
    protected void afterStartDocument() {

    }

    /**
     * 在文档结束的时候调用此方法
     * @throws SAXException 解析异常
     */
    public void endDocument() throws SAXException {
        afterEndDocument();
    }

    /**
     * 文档结束后的扩展方法，子类继承后，覆盖即可
     */
    protected void afterEndDocument() {

    }

    /**
     * 开始读取节点的方法 其中参数中的namespaceURI就是名域，localName是标签名，qName是标签的修饰前缀，
     * 当没有使用名域的时候，这两个参数都未null。而atts是这个标签所包含的属性列表。通过atts，可以得到所有的属性名和相应的值
     * @param uri 名域
     * @param localName 标签名
     * @param qName 标签的修饰前缀
     * @param attributes 标签所包含的属性列表
     * @throws SAXException 解析异常
     */
    public void startElement(String uri,
            String localName,
            String qName,
            Attributes attributes) throws SAXException {

        afterStartElement(uri, localName, qName, attributes);
    }

    /**
     * 开始读取节点后的扩展方法
     * @param uri 名域
     * @param localName 标签名
     * @param qName 标签的修饰前缀
     * @param attributes 标签所包含的属性列表
     */
    protected void afterStartElement(String uri,
            String localName,
            String qName,
            Attributes attributes) {

    }

    /**
     * 遇到结束标签时调用此方法
     * @param uri 名域
     * @param localName 标签名
     * @param qName 标签的修饰前缀
     * @throws SAXException 解析异常
     */
    public void endElement(String uri, String localName, String qName) throws SAXException {
        afterEndElement(uri, localName, qName);
    }

    /**
     * 读取节点结束后的扩展方法
     * @param uri 名域
     * @param localName 标签名
     * @param qName 标签的修饰前缀
     */
    protected void afterEndElement(String uri, String localName, String qName) {

    }

    /**
     * 当遇到标签中的字符串时，调用这个方法，它的参数是一个字符数组，以及读到的这个字符串在这个数组中的起始位置和长度
     * @param ch 字符数组
     * @param start 起始位置
     * @param length 长度
     * @throws SAXException 解析异常
     */
    public void characters(char[] ch, int start, int length) throws SAXException {
        afterCharacters(ch, start, length);
    }

    /**
     * 解析标签中的字符串后的扩展方法
     * @param ch 字符数组
     * @param start 起始位置
     * @param length 长度
     */
    protected void afterCharacters(char[] ch, int start, int length) {

    }
}
