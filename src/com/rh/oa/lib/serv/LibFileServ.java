package com.rh.oa.lib.serv;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FilenameUtils;

import com.rh.client.DES;
import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.RequestUtils;

/**
 * 
 * @author yangjy
 * 
 */
public class LibFileServ extends CommonServ {
    private static final String SERV_LIB_FILE = "OA_LIB_FILE";
    private static final String FILE_TYPE = "doc;docx;excel;pdf;wps;xls;xlsx;jpg;gif;tiff;png;txt;bmp;jpeg;";
    private static Map<String, String> availableFileTypes = new ConcurrentHashMap<String, String>();
    static {
        String[] fileTypes = FILE_TYPE.split("\\;");
        for (String type : fileTypes) {
            if (type.length() > 0) {
                availableFileTypes.put(type, "");
            }
        }
    }
    
    /**
     * 
     * @param fileName 文件名
     * @return 是否可用的文件类型
     */
    private boolean isAvailableFileType(String fileName) {
        String extName = FilenameUtils.getExtension(fileName);
        return availableFileTypes.containsKey(extName.toLowerCase());
    }

    @Override
    protected void afterQuery(ParamBean paramBean, OutBean outBean) {
        super.afterQuery(paramBean, outBean);

        List<Bean> list = outBean.getDataList();

        UserBean user = Context.getUserBean(); 
        // 设置访问地址，前台不做任何处理
        for (Bean bean : list) {
            if (this.isAvailableFileType(bean.getStr("FILE_ID"))) {
                StringBuilder str = new StringBuilder();
                // 安全服务器浏览文件地址
                str.append(Context.getSyConf("SEAL_SYS_HOST", ""));
                str.append("/general/modules/wendang/two/waterMark.jsp?disno=");

                StringBuilder opts = new StringBuilder();
                opts.append(user.getStr("USER_WORK_NUM"));
                opts.append(";");
                opts.append(bean.getStr("FILE_ID"));
                opts.append(";");
                opts.append(RequestUtils.getSysHost());
                opts.append("/file/").append(bean.getStr("FILE_ID"));

                try {
                    str.append(DES.desEncrypt(opts.toString()));
                } catch (Exception e) {
                    log.info(e.getMessage(), e);
                }
                bean.set("ACCESS_URL", str.toString());
            } else {
                bean.set("ACCESS_URL", "/file/" + bean.getStr("FILE_ID"));
            }
        }
    }

    /**
     * 设置文件转换成功标记
     * @param paramBean 参数Bean
     * @return 操作结果
     */
    public OutBean convertOk(ParamBean paramBean) {
        String fileId = paramBean.getStr("fileId");
        String result = paramBean.getStr("result");
        ParamBean dataBean = new ParamBean();
        dataBean.setId(fileId);
        if (result.equals("ok")) {
            // 成功
            dataBean.set("CONVERT_OK", 1);
            dataBean.set("ERROR_MSG", result);
        } else if (result.equals("error")) {
            // 失败
            dataBean.set("CONVERT_OK", 2);
            dataBean.set("ERROR_MSG", result);
        }
        OutBean out = ServMgr.act(SERV_LIB_FILE, ServMgr.ACT_SAVE, dataBean);
        return out;
    }

}
