/*
 * Copyright (c) 2012 Ruaho All rights reserved.
 */
package com.rh.core.plug.search;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;

import com.rh.core.base.Bean;
import com.rh.core.comm.FileMgr;
import com.rh.core.plug.search.client.RhFileClient;
import com.rh.core.plug.search.client.RhSearchClient;
import com.rh.core.plug.search.client.TikaHelper;
import com.rh.core.util.Constant;
import com.rh.core.util.Lang;
import com.rh.core.util.http.HttpGetResponse;
import com.rh.core.util.http.HttpUtils;

/**
 * 
 * search helper
 * 
 * @author liwei
 * 
 */
public class SearchHelper {
    private static Log log = LogFactory.getLog(RhSearchClient.class);

    /** all users grantee */
    public static final String ALL_USERS_GRANTEE = "allusers";
    
    /** 最大允许索引的文件  **/
    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024;

    /**
     * can not new instance
     */
    private SearchHelper() {

    }

    /**
     * get user grantee expresstion
     * @param user user id
     * @return grantee string
     */
    public static String getUserGranteeExpr(String user) {
        return "u_" + user;
    }

    /**
     * get deparment grantee expresstion
     * @param deparment deparment id
     * @return grantee string
     */
    public static String getDeptGranteeExpr(String deparment) {
        return "d_" + deparment;
    }

    /**
     * get group grantee expresstion
     * @param group - group id
     * @return grantee - string
     */
    public static String getGroupGranteeExpr(String group) {
        return "g_" + group;
    }

    /**
     * get role grantee expresstion
     * @param role role id
     * @return grantee string
     */
    public static String getRoleGranteeExpr(String role) {
        return "r_" + role;
    }

    /**
     * get company grantee expresstion
     * @param cmpy company id
     * @return grantee string
     */
    public static String getCmpyGranteeExpr(String cmpy) {
        return "c_" + cmpy;
    }

    /**
     * get multiple-valued grantee expresstion
     * @param cmpy - company id
     * @param dept - deparment id
     * @param role - role id
     * @return grantee - string
     */
    public static String getMulitGranteeExpr(String cmpy, String dept, String role) {
        String tag = "&";
        String expresstion = "";
        if (null != cmpy && cmpy.length() > 0) {
            expresstion += getCmpyGranteeExpr(cmpy) + tag;
        }
        if (null != dept && dept.length() > 0) {
            expresstion += getDeptGranteeExpr(dept) + tag;
        }
        if (null != role && role.length() > 0) {
            expresstion += getRoleGranteeExpr(role) + tag;
        }
        if (expresstion.endsWith(tag)) {
            expresstion.substring(0, expresstion.length() - 1);
        }
        return expresstion;
    }

    /**
     * get file content
     * @param path - file path expresstion <br>
     *            example: internal://xxxxx , http://sina.com/file/a.doc
     * @return file - content string
     */
    public static ExtractResult getFileContent(String path) {
        String internal = Constant.FILE_INNER_URL_PREFIX;
        String http = "http://";
        ExtractResult result = new ExtractResult();
        
        // inernal file
        if (path.startsWith(internal)) {
            String id = path.substring(internal.length());
            try {
                Bean bean = FileMgr.getFile(id);
                long filesize = FileMgr.getFileSize(bean);
                
                if (filesize <= MAX_FILE_SIZE) {
                    InputStream is = FileMgr.download(bean);
                    result = TikaHelper.extractText(is);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

        } else if (path.startsWith(http)) { // http file
            try {
                HttpGetResponse httpFileResp = HttpUtils.httpGet(path);
                InputStream is = httpFileResp.getInputStream();
                result = TikaHelper.extractText(is);
                httpFileResp.closeClient();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } else if (path.startsWith(RhFileClient.FILE_SERVER_EXPR)) {
            String fileServer = RhFileClient.getInstance().getServerUri();
            path = path.replace(RhFileClient.FILE_SERVER_EXPR, fileServer);
            return getFileContent(path);
        } else {
            log.warn("handler not yet implemented:" + path);
        }

        String suffix = "";
        if (-1 < path.lastIndexOf(".")) {
            suffix = path.substring(path.lastIndexOf(".") + 1);
        }
        result.setMType(suffix);
        return result;
    }

    /**
     * get file content
     * @param path - file path expresstion <br>
     *            example: internal://xxxxx , http://sina.com/file/a.doc
     * @return file - content string
     * @throws IOException - io exception
     */
    public static String saveFileSnapshot(String path) throws IOException {
        if (0 == path.length()) {
            return "";
        }
        String internal = Constant.FILE_INNER_URL_PREFIX;
        String http = "http://";
        String resultUrl = "";
        InputStream is = null;
        String name = "";
        // inernal file
        if (path.startsWith(internal)) {
            String fileName = path.substring(internal.length());
            try {
                Bean bean = FileMgr.getFile(fileName);
                if (bean.getStr("FILE_NAME").length() > 0) {
                    fileName = bean.getStr("FILE_NAME");
                }
                name = fileName;

                is = FileMgr.download(bean);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

        } else if (path.startsWith(http)) { // http file
            try {
                HttpGetResponse getResponse = HttpUtils.httpGet(path);
                is = getResponse.getInputStream();
                String fileName = getResponse.getDownloadName();
                name = fileName;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

        } else if (path.startsWith(RhFileClient.FILE_SERVER_EXPR)) {
            String fileServer = RhFileClient.getInstance().getServerUri();
            path = path.replace(RhFileClient.FILE_SERVER_EXPR, fileServer);
            return saveFileSnapshot(path);
        } else {
            System.out.println("handler not yet implemented:" + path);
            return resultUrl;
        }

        // to snapshot
        try {
            String xml = "";
            xml = RhFileClient.getInstance().snapshot(is, name);
            resultUrl = Lang.subString(xml, "<url>", "</url>");
            // String url = RhFileClient.getInstance().getServerUri() + file;

            IOUtils.closeQuietly(is);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return RhFileClient.FILE_SERVER_EXPR + "/" + resultUrl;
    }

    /**
     * extraction rich text
     * @param inputStream - inputstream
     * @return content - string
     * 
     */
    // public static ExtractResult extractText(InputStream inputStream) {
    //
    // // TODO 超大文件测试，buff size 设置
    // // text extracting
    // Parser parser = new AutoDetectParser();
    // ParseContext context = new ParseContext();
    // context.set(Parser.class, parser);
    //
    // // ContentHandler content = new BodyContentHandler();
    // StringWriter textBuffer = new StringWriter();
    // ContentHandler content = new BodyContentHandler(textBuffer);
    // Metadata metadata = new Metadata();
    // try {
    // parser.parse(inputStream, content, metadata, context);
    // } catch (IOException e) {
    // e.printStackTrace();
    // throw new RuntimeException("text extracting error, " + e.getMessage());
    // } catch (SAXException e) {
    // e.printStackTrace();
    // throw new RuntimeException("text extracting error, " + e.getMessage());
    // } catch (TikaException e) {
    // e.printStackTrace();
    // throw new RuntimeException("text extracting error, " + e.getMessage());
    // } finally {
    // try {
    // inputStream.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // // ignore, don't throw
    // }
    // }
    // ExtractResult result = new ExtractResult();
    // result.setContent(content.toString().replace("�", ""));
    // String mType = metadata.get("Content-Type");
    // mType = parseMimeType(mType);
    // result.setMType(mType);
    // return result;
    // }

    /**
     * parse mimtype to type <br>
     * application/msword ==> doc
     * @param mType - mime type string
     * @return type string
     */
    public static String parseMimeType(String mType) {
        String docType = "";
        if (mType.contains("word")) {
            docType = "doc";
        } else if (mType.contains("excel") || mType.contains("openxmlformats-officedocument.spreadsheetml")) {
            docType = "xls";
        } else if (mType.contains("powerpoint")) {
            docType = "ppt";
        } else if (mType.contains("text/plain")) {
            docType = "txt";
        } else if (mType.contains("application/zip")) {
            docType = "zip";
        } else {
            docType = mType;
        }
        return docType;
    }

    /**
     * keywords filter
     * @param keywords - text
     * @return Validated keywords string
     */
    public static String keywordsFilter(String keywords) {
        keywords = removeSpecChar(keywords);
        try {
            keywords = URLDecoder.decode(keywords, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        return keywords.trim();
    }
    
    /**
     * 去掉字符串中的特殊字符
     * @param keywords 搜索关键字
     * @return 去掉特殊字符后的字符串
     */
    private static String removeSpecChar(String keywords) {
        final char[] sprcChars = { '[', ']', '\'', '"', '<', '>', '/', '(', ')' };
        if (keywords == null) {
            return "";
        }
        StringBuilder str = new StringBuilder();
        char[] chars = keywords.toCharArray();
        for (char word : chars) {
            boolean exist = false;
            for (char specChar : sprcChars) {
                if (specChar == word) {
                    exist = true;
                    break;
                }
            }

            if (!exist) {
                str.append(word);
            }
        }
        return str.toString();
    }

    /**
     * html source encode
     * @param source source text
     * @return encode string
     */
    public static String queryEncode(String source) {
        source = source.replace("'", "__@1_");
        return source;
    }

    /**
     * get abstract content
     * @param content - content string
     * @param maxLeng - max abstract text leng
     * @param index - pre tag
     * @param hlWords - post tag
     * @return abstract content
     */
    public static String getAbstract(String content, int maxLeng, int index, List<String> hlWords) {
        if (content == null) {
            return "";
        }
        int halfMaxLeng = maxLeng / 2;
        int start = index - halfMaxLeng;
        int end = index + halfMaxLeng;

        // foramt
        if (start < 0) {
            start = 0;
        }
        if (end > content.length()) {
            end = content.length();
        }

        // keep abstract text leng about 100
        if (end - start < maxLeng) {
            int dvalue = maxLeng - (end - start);
            if (start == 0) {
                end += dvalue;
            }
            if (end == content.length()) {
                start -= dvalue;
            }
        }
        // foramt
        if (start < 0) {
            start = 0;
        }

        // if the text not be end of content, we append "..."
        String suffix = "...";
        if (end > content.length()) {
            end = content.length();
        }

        if (end == content.length()) {
            suffix = "";
        }
        String text = content.substring(start, end) + suffix;
        // hl
        for (String word : hlWords) {
            text = text.replace(word, SearchServ.HL_PRE_TAG + word + SearchServ.HL_POST_TAG);
        }
        return text;
    }
    
    /**
     * get abstract content
     * @param content - content string
     * @param maxLeng - max abstract text leng
     * @param preTag - pre tag
     * @param postTag - post tag
     * @return abstract content
     */
    public static String getAbstract(String content, int maxLeng, String preTag, String postTag) {
        List<String> hlWords = getHlWords(content, preTag, postTag);
        String keyword = "";
        if (0 < hlWords.size()) {
            keyword = hlWords.get(0);
        }
        content = stripHTML(content);
        int halfMaxLeng = maxLeng / 2;
        int index = content.indexOf(keyword);
        int start = index - halfMaxLeng;
        int end = index + halfMaxLeng;

        // foramt
        if (start < 0) {
            start = 0;
        }
        if (end > content.length()) {
            end = content.length();
        }

        // keep abstract text leng about 100
        if (end - start < maxLeng) {
            int dvalue = maxLeng - (end - start);
            if (start == 0) {
                end += dvalue;
            }
            if (end == content.length()) {
                start -= dvalue;
            }
        }
        // foramt
        if (start < 0) {
            start = 0;
        }

        // if the text not be end of content, we append "..."
        String suffix = "...";
        if (end > content.length()) {
            end = content.length();
        }

        if (end == content.length()) {
            suffix = "";
        }

        String text = content.substring(start, end) + suffix;
        // hl
        for (String word : hlWords) {
            text = text.replace(word, preTag + word + postTag);
        }
        return text;
    }

    /** default mime types */
    private static final String[] DEFAULT_MIME_TYPES = { "doc", "xls", "xlsx", "swf", "flv", "pdf", "ppt", "doc",
            "docx", "xdoc", "asc", "txt", "jpeg", "jpg", "png", "gif", "zip", "rar", "7z", "gzip", "tar", "gz" };

    /**
     * is the value valid mime type
     * @param text - value string
     * @return is a mime type
     */
    public static boolean isMimeType(String text) {
        for (String mtype : DEFAULT_MIME_TYPES) {
            if (text.equals(mtype)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * get highlight words
     * @param content content text
     * @param preTag hl pre tag
     * @param postTag hl post tag
     * @return hl words list
     */
    private static List<String> getHlWords(String content, String preTag, String postTag) {
        List<String> words = new ArrayList<String>();
        int start = content.indexOf(preTag);
        int end = content.indexOf(postTag);
        while (start > -1 && end > 1) {
            int offset = start + preTag.length();
            String hlWord = content.substring(offset, end);
            if (!words.contains(hlWord)) {
                words.add(hlWord);
            }
            content = content.substring(end + postTag.length());
            start = content.indexOf(preTag);
            end = content.indexOf(postTag);
        }
        return words;
    }

    /**
     * stript html code
     * @param html text
     * @return no html text
     */
    private static String stripHTML(String html) {
        return Jsoup.parse(html).text();
    }    
}
