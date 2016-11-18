package com.rh.oa.zh.seal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.httpclient.HttpClientUtils;
import com.rh.core.util.lang.Assert;
import com.rh.core.util.threadpool.RhThreadPool;

/**
 * 
 * @author yangjy
 * 
 */
public class SecureFile {
    /**
     * 记录日志信息的函数
     */
    protected static Log log = LogFactory.getLog(SecureFile.class);

    private static final String SERV_LIB_FILE = "OA_LIB_FILE";
    
    private static final String SUPPORT_EXT_NAME =  ",doc,docx,excel,pdf,wps,xls,xlsx,jpg,gif,tiff,png,txt,bmp,jpeg,";

    /** 文档服务器地址 ， 例如：Context.getSyConf("SEAL_SYS_HOST", "") **/
    private String sealSysHost = "";

    /** OA系统地址, 例如：RequestUtils.getSysHost() */
    private String oaSysHost = "";

    /**
     * 
     * @return 文档转换服务器地址
     */
    private String getConverterUrl() {
        String url = this.getSealSysHost();

        url += "/general/file/saveFile.jsp";
        return url;
    }
    
    /**
     * 
     * @param extName 文件扩展名
     * @return 是否是支持的文件
     */
    private boolean isSupportFile(String extName) {

        int opt = SUPPORT_EXT_NAME.indexOf("," + extName.toLowerCase() + ",");
        if (opt >= 0) {
            return true;
        }

        return false;
    }

    /**
     * 
     * @param fileBean 文件Bean
     */
    public void createFile(Bean fileBean) {
        final String fileId = fileBean.getId();
        String extName = FilenameUtils.getExtension(fileBean.getStr("FILE_NAME"));
        //判读文件类型是否符合条件
        if (!isSupportFile(extName)) {
            ParamBean dataBean = new ParamBean();
            dataBean.setId(fileId);
            // 失败
            dataBean.set("CONVERT_OK", 1);
            dataBean.set("ERROR_MSG", "not supported file type.");
            ServMgr.act(SERV_LIB_FILE, ServMgr.ACT_SAVE, dataBean);
            return;
        }
        
        final String oaHost = this.getOaSysHost();

        Map<String, String> params = new HashMap<String, String>();
        params.put("wyh", fileId);
        params.put("file_path", oaHost + "/file/" + fileId);
        
        //使用新线程执行文档转换任务
        SecureFileConvertTask task = new SecureFileConvertTask(fileId, this.getConverterUrl(), params);
        RhThreadPool.getDefaultPool().execute(task);
    }

    /**
     * 
     * @param fileId 文件ID
     */
    public void deleteFile(String fileId) {
        // http://IP:PORT/general/modules/wendang/two/waterMark.jsp?gh="+员工工号+"&filePath="+文件路径;
        // http://IP:Port/general/file/deleteFile.jsp?wyh=wyh
        StringBuffer url = new StringBuffer();
        url.append(getSealSysHost());
        url.append("/general/file/deleteFile.jsp?wyh=");
        url.append(fileId);

        try {
            String result = HttpClientUtils.getWebContent(url.toString());
            result = result.trim();
            if (result.startsWith("ok")) {
                // 成功
            } else if (result.startsWith("error")) {
                // 失败
                log.error("delete file error, result:" + result);
            } else {
                // 无返回，等同于失败
                log.error("delete file error: result is empty.");
            }
        } catch (Exception e) {
            log.error("delete file error. ", e);
        }
    }

    /**
     * 
     * @return 安全文档服务器地址
     */
    public String getSealSysHost() {
        Assert.hasLength(sealSysHost, "sealSysHost属性值（文档转换服务器地址）不能为NULL");
        return sealSysHost;
    }

    /**
     * 
     * @param sealSysHost 安全文档服务器地址
     */
    public void setSealSysHost(String sealSysHost) {
        this.sealSysHost = sealSysHost;
    }

    /**
     * 
     * @return OA系统地址
     */
    public String getOaSysHost() {
        Assert.hasLength(oaSysHost, "oaSysHost属性值（OA服务器地址）不能为NULL");
        return oaSysHost;
    }

    /**
     * 
     * @param oaSysHost OA系统地址
     */
    public void setOaSysHost(String oaSysHost) {
        this.oaSysHost = oaSysHost;
    }
}
