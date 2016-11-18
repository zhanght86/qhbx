package com.rh.oa.zh.seal;

import java.util.Map;

import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.exception.ExceptionUtil;
import com.rh.core.util.httpclient.HttpClientUtils;
import com.rh.core.util.threadpool.ThreadTask;

/**
 * 
 * @author yangjy
 *
 */
public class SecureFileConvertTask extends ThreadTask {
    private static final String SERV_LIB_FILE = "OA_LIB_FILE";

    private String converterUrl = null;
    private Map<String, String> params = null;
    private String fileId = null;
    
    /**
     * 
     * @param fileId 文件ID
     * @param converterUrl 转换文档的URL
     * @param params 转换文档请求参数
     */
    public SecureFileConvertTask(String fileId, String converterUrl, Map<String, String> params) {
        this.fileId = fileId;
        this.converterUrl = converterUrl;
        this.params = params;
    }

    @Override
    public void execute() {
        ParamBean dataBean = new ParamBean();
        dataBean.setId(fileId);
        String result = "";
        try {
            result = HttpClientUtils.getWebContent(converterUrl, params, "UTF-8");
            
            log.error("**********:" + result);
            
            result = result.trim();
            if (result.startsWith("ok:")) {
                //成功
                dataBean.set("CONVERT_OK", 1);
                dataBean.set("ERROR_MSG", result);
            } else if (result.startsWith("error:")) {
                //失败
                dataBean.set("CONVERT_OK", 2);
                dataBean.set("ERROR_MSG", result);
            } else {
              //失败
                dataBean.set("CONVERT_OK", 2);
                dataBean.set("ERROR_MSG", result);                
            }
        } catch (Exception e) {
            dataBean.set("CONVERT_OK", 2);
            dataBean.set("ERROR_MSG", result + ";" + ExceptionUtil.toMsgString(e));
        } finally {
            ServMgr.act(SERV_LIB_FILE, ServMgr.ACT_SAVE, dataBean);
        }        
    }

}
