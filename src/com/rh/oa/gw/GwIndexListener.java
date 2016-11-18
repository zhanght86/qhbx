package com.rh.oa.gw;

import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.rh.core.base.Bean;
import com.rh.core.plug.search.IndexListener;
import com.rh.core.plug.search.RhIndex;
import com.rh.core.plug.search.client.RhFileClient;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServUtils;
import com.rh.oa.comm.AbstractIndexListener;
import com.rh.oa.gw.util.GwConstant;
import com.rh.oa.gw.util.GwFileHelper;
import com.rh.oa.gw.util.GwUtils;
import com.rh.oa.gw.util.ZW_TYPE;

/**
 * 为公文创建索引的监听类
 * @author yangjy
 * 
 */
public class GwIndexListener extends AbstractIndexListener implements IndexListener {

    
    public void index(RhIndex iaMsg, Bean searchDef, Bean data) {
        // 访问URL
        iaMsg.setUrl(createAccessUrl(data.getStr("GW_TITLE"), getServId(data), data.getStr("GW_ID")));

        // 增加授权
        grant(iaMsg, data);

        // 增加文件列表
        addFileList(iaMsg, data.getId(), getServId(data));
    }

    @Override
    protected void addFileList(RhIndex iaMsg, String dataId, String servId) {
        // data.getId()
        GwFileHelper fileHelper = new GwFileHelper(dataId, "");
        
        if (fileHelper.getFileList().size() == 0) {
            return;
        }
        
        addZhengwen(iaMsg, dataId, servId, fileHelper);

        // 为正文之外的文件建索引
        ServDefBean servDef = ServUtils.getServDef(servId);
        List<Bean> fileSelfItems = servDef.getFileSelfItems();
        for (Bean item : fileSelfItems) {
            // 忽略正文
            if (item.getStr("ITEM_CODE").equals(GwConstant.ZHENGWEN)) {
                continue;
            }

            addFileList(iaMsg, fileHelper.getFileList(item.getStr("ITEM_CODE")));
        }
    }
    
    /**
     * 增加正文
     * @param iaMsg 索引信息
     * @param dataId 公文ID
     * @param servId 服务ID
     * @param fileHelper 文件帮助类
     */
    private void addZhengwen(RhIndex iaMsg, String dataId, String servId, GwFileHelper fileHelper) {
        // 为正文建索引，正文文件可能有多个，找一个合适的类型创建索引即可。
        Bean zwFileBean = fileHelper.getZhengwen();
        String extName = "";
        if (zwFileBean != null) {
            final String zwFileName = zwFileBean.getStr("FILE_NAME");
            extName = FilenameUtils.getExtension(zwFileName);
        }

        if (zwFileBean != null && RhFileClient.aviableFileType(extName)) {
            // 正文是符合要求的文件格式
            ParamBean param = new ParamBean();
            param.setId(dataId);
            param.setServId(servId);
            param.set("_GW_FILE", fileHelper);

            GwSealProcess gwSeal = GwUtils.createGwSeal();
            if (gwSeal.hasSealed(param)) { // 这是一个盖章文件
                zwFileBean.set("_ACCESS_URL", "javascript:openJdSealFile('" + zwFileBean.getId() + "');");
            }

            super.addFile(iaMsg, zwFileBean);
        } else if (fileHelper.getFileList(GwConstant.ZHENGWEN, ZW_TYPE.RED_HEAD.getCode()).size() > 0) {
            // 是否有红头文件
            addFileList(iaMsg, fileHelper.getFileList(GwConstant.ZHENGWEN, ZW_TYPE.RED_HEAD.getCode()));
        } else if (fileHelper.getFileList(GwConstant.ZHENGWEN, ZW_TYPE.WEN_GAO.getCode()).size() > 0) {
            // 是否有文稿文件
            addFileList(iaMsg, fileHelper.getFileList(GwConstant.ZHENGWEN, ZW_TYPE.WEN_GAO.getCode()));
        }
    }

    /**
     * 
     * @param bean data Bean
     * @return 公文模板
     */
    private String getServId(Bean bean) {
        if (bean.isNotEmpty("TMPL_CODE__CODE")) {
            return (bean.getStr("TMPL_CODE__CODE"));
        }

        return bean.getStr("TMPL_CODE");
    }

}
