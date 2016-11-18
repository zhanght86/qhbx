package com.rh.bn.linktransfer.factory;

import com.rh.bn.linktransfer.LinkTransfer;
import com.rh.core.base.TipException;

/**
 * 相关链接处理实现类实例化工厂
 * @author tanyh 20151208
 *
 */
public class LinkTransferFactory {

    /** 图片链接处理类 **/
    private static final String imageClass = "com.rh.bn.linktransfer.impl.ImageLinkTransfer";
    /** 文档链接处理类 **/
    private static final String documentClass = "com.rh.bn.linktransfer.impl.DocumentLinkTransfer";
    /** 新闻内容处理类 **/
    private static final String newsClass = "com.rh.bn.linktransfer.impl.NewsLinkTransfer";
    /** 签报相关链接处理类 **/
    private static final String qbClass = "com.rh.bn.linktransfer.impl.QbLinkTransfer";
    
    public static LinkTransfer getLinkTransfer(String urlStr) {
        try {
            if ("image_gallery".equals(urlStr)) {
                return (LinkTransfer) Class.forName(imageClass).newInstance();
            } else if ("get_file".equals(urlStr)) {
                return (LinkTransfer) Class.forName(documentClass).newInstance();
            } else if ("get_news".equals(urlStr)) {
                return (LinkTransfer) Class.forName(newsClass).newInstance();
            } else if ("home".equals(urlStr)) {
                return (LinkTransfer) Class.forName(qbClass).newInstance();
            } else {
                return null;
            }
        } catch (InstantiationException e) {
            throw new TipException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new TipException(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new TipException(e.getMessage());
        }
    }
}
