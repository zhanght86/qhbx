package com.rh.bn.linktransfer.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.rh.bn.linktransfer.LinkTransfer;
import com.rh.bn.xml.XML2NewsReader;
import com.rh.core.base.Bean;
import com.rh.core.base.TipException;
import com.rh.core.base.db.Transaction;
import com.rh.core.comm.cms.mgr.TemplateMgr;
import com.rh.core.util.RequestUtils;

/**
 * 百年人寿项目旧OA新闻处理类
 * @author tanyh 20151214
 *
 */
public class NewsLinkTransfer implements LinkTransfer{

    /**
     * 根据请求头获取新闻信息
     * @param request 请求头
     * @param response 响应头
     */

    public void transferByRequest(HttpServletRequest request, HttpServletResponse response) {
        //获取新闻ID
        String articleId = RequestUtils.getStr(request, "articleId");
        //根据新闻ID获取新闻内容、标题、时间、拟稿人、来源
        Bean articleBean = Transaction.getExecutor().queryOne(
                "select TITLE as NEWS_SUBJECT, GROUPID, to_char(DISPLAYDATE,'yyyy-MM-dd hh24:mi:ss') as NEWS_TIME, USERNAME as NEWS_USER__NAME, CONTENT as NEWS_BODY from journalarticle where articleid='"
                        + articleId + "'");
        if (articleBean != null && !articleBean.isEmpty()) {
            try {
                request.setCharacterEncoding("UTF-8");
                response.setContentType("text/html; charset=UTF-8");
                //新闻内容提取
                Clob content = (Clob) articleBean.get("NEWS_BODY");
                if (content != null) {
                    //转换为字节流
                    Reader reader = content.getCharacterStream();
                    char[] b = new char[1024];
                    int word = 0;
                    StringBuffer sb = new StringBuffer("");
                    while ( (word = reader.read(b)) != -1) {
                        //写入字符串对象中
                        if (word < 1024) {
                            sb.append((new String(b)).substring(0, word));
                        } else {
                            sb.append(new String(b));
                        }
                    }
                    reader.close();
                    //重新设置新闻内容
                    articleBean.set("NEWS_BODY", getNewsContentByXml(sb.toString()));
                } else {
                    //重新设置新闻内容
                    articleBean.set("NEWS_BODY", "");
                }
                Map<String, Object> result = new HashMap<String, Object>();
                result.put("data", articleBean);
                StringWriter writer = new StringWriter();
                //根据模板id生成html
                TemplateMgr.getInstance().buildHtml("37WsrpFDSN9i8Uit8pDoPMZ", result, writer);
                String html = writer.toString();
                IOUtils.closeQuietly(writer);
                //传回前端展示
                response.getOutputStream().write(html.getBytes("UTF-8"));
                IOUtils.closeQuietly(response.getOutputStream());
            } catch (UnsupportedEncodingException e) {
                throw new TipException(e.getMessage());
            } catch (SQLException e) {
                throw new TipException(e.getMessage());
            } catch (IOException e) {
                throw new TipException(e.getMessage());
            } catch (RuntimeException e) {
                throw new TipException(e.getMessage());
            }
        }
        
    }
    
    /**
     * 解析xml格式数据，提取出新闻内容html
     * @param xmlStr xml格式数据
     * @return String 返回新闻内容
     * @throws UnsupportedEncodingException 
     */
    private String getNewsContentByXml(String xmlStr) throws UnsupportedEncodingException {
        XML2NewsReader newsReader = new XML2NewsReader();
        newsReader.parseStream(new ByteArrayInputStream(xmlStr.getBytes("UTF-8")));
        //newsReader.parseString(xmlStr);
        return newsReader.getNewsContent();
    }

}
