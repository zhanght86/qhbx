package com.rh.core.comm.news.mgr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author chensheng
 * 
 */
public class HtmlParser {
    
    /**
     * 文件存储目录
     */
    private static final String FILE_DIR = "/file/";

    /**
     * 解析出html代码中的所有远程图片链接地址
     * @param html 前端传过来的页面html代码
     * @return 返回替换成本地图片的html代码
     */
    public String parse(String html) {
        String sAllowExt = this.getAllowExt("remote");

        // 取得网页上URL的正则表达式
        Pattern pRemoteHttpURL = Pattern.compile("((http|https|ftp|rtsp|mms):(//|\\\\){1}(([A-Za-z0-9_-])+[.]){1,}"
                + "(net|com|cn|org|cc|tv|[0-9]{1,3})(\\S*/)((\\S)+[.]{1}(" + sAllowExt + ")))");

        // 对传入的字符串进行匹配
        Matcher mRemoteHttpURL = pRemoteHttpURL.matcher(html);

        String localFileName = "";
        
        StringBuffer sb = new StringBuffer();
        while (mRemoteHttpURL.find()) {
            localFileName = RemoteFile.download(mRemoteHttpURL.group(0));
 
            if (localFileName != null) {
                mRemoteHttpURL.appendReplacement(sb , FILE_DIR + localFileName);
            }
        }
        
        // 替换路径
        mRemoteHttpURL.appendTail(sb);
        
        return sb.toString();
    }

    /**
     * 获取可以上传的文件后缀
     * @param type 文件类型
     * @return 返回可以上传的后缀
     */
    public String getAllowExt(String type) {
        String sAllowExt;
        if (type.equalsIgnoreCase("remote")) {
            sAllowExt = "gif|jpg|jpeg|bmp";
            sAllowExt = sAllowExt + "|" + sAllowExt.toUpperCase();
        } else if (type.equalsIgnoreCase("file")) {
            sAllowExt = "rar|zip|exe|doc|xls|chm|hlp|pdf";
            sAllowExt = sAllowExt + "|" + sAllowExt.toUpperCase();
        } else if (type.equalsIgnoreCase("media")) {
            sAllowExt = "rm|mp3|wav|mid|midi|ra|avi|mpg|mpeg|asf|asx|wma|mov";
            sAllowExt = sAllowExt + "|" + sAllowExt.toUpperCase();
        } else if (type.equalsIgnoreCase("flash")) {
            sAllowExt = "swf";
            sAllowExt = sAllowExt + "|" + sAllowExt.toUpperCase();
        } else {
            sAllowExt = "gif|jpg|jpeg|bmp";
            sAllowExt = sAllowExt + "|" + sAllowExt.toUpperCase();
        }
        return sAllowExt;
    }
    
}
