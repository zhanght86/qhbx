package com.rh.oa.gw.listener;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.comm.FileMgr;
import com.rh.core.comm.file.FileUploadListener;
import com.rh.core.serv.ServDao;
import com.rh.oa.gw.util.GwConstant;
import com.rh.oa.gw.util.GwFileHelper;
import com.rh.oa.gw.util.ZW_TYPE;
import com.rh.oa.gw.util.ZwType;

/**
 * 公文套红头正文处理的监听类
 * 
 * @author wangl
 * 
 */
public class GwFileListener implements FileUploadListener {

    /**
     * 更新文件之前调用
     * @param paramBean request中的获取的参数数据
     */
    public void beforeUpdate(Bean paramBean) {
        //System.out.println("--------------" + GwFileListener.class + "---------beforeUpdate");
    }

    /**
     * 上传红头文件之前调用此方法
     * @param paramBean request中的获取的参数数据
     */
    public void befortAdd(Bean paramBean) {
        if (paramBean.isNotEmpty("SOURCE_ID")) { //
            String doAct = paramBean.getStr("doAct"); // 操作类型
            if (doAct.equals("redhead")) { // 操作类型为:套红头
                GwFileHelper fileHelper = new GwFileHelper(paramBean.getStr("DATA_ID")
                        , paramBean.getStr("SERV_ID"));
                // 如果指定文件已经是文稿了，则不做任何操作。
                if (fileHelper.hasWenGao()) {
                    final String zwId = fileHelper.getZhengwen().getId();
                    if (!zwId.equals(paramBean.getStr("FILE_ID"))) { // 覆盖红头文件
                        paramBean.set("FILE_ID", zwId);
                    }
                }
            }
        }
    }

    /**
     * 更新服务器文件之后调用
     * @param paramBean request中的获取的参数数据
     * @param dataList 修改后的数据列表对象
     */
    public void afterUpdate(Bean paramBean, List<Bean> dataList) {
        //System.out.println("--------------" + GwFileListener.class + "---------afterUpdate");
    }

    /**
     * 上传文件之后调用
     * @param paramBean request中的获取的参数数据
     * @param dataList 修改后的数据列表对象
     */
    public void afterAdd(Bean paramBean, List<Bean> dataList) {
        if (paramBean.isNotEmpty("SOURCE_ID")) {
            Bean zhengwen = ServDao.find(FileMgr.CURRENT_SERVICE, paramBean.getStr("SOURCE_ID"));
            
            String doAct = paramBean.getStr("doAct");   //操作类型 
            
            if (doAct.equals("redhead")) { //操作类型为套红头
            	GwFileHelper fileHelper = new GwFileHelper(paramBean.getStr("DATA_ID")
                        , paramBean.getStr("SERV_ID"));
            	// 判断是否套红头后，成功生成正文
            	if (fileHelper.getFileList(ZW_TYPE.ZHENG_WEN.getCode(), ZW_TYPE.ZHENG_WEN.getCode()).size() == 2) {
            		// 正确生成套完红头后的正文，则将原来的正文变成文稿
            		GwFileHelper.change2Wengao(zhengwen);
            	}
                //把原来的文稿变成正文
                //GwFileHelper.change2Wengao(zhengwen);
                
            } else if (paramBean.getStr("ITEM_CODE").equals(GwConstant.NO_ENC_ZHENGWEN)) {  //书生盖章
                // 如果当前正文不为空，则将当前正文修改为红头
                if (zhengwen != null) {
                    GwFileHelper.change2Redhead(zhengwen);
                }
                GwFileHelper.changeZwType(ZW_TYPE.ZHENG_WEN, paramBean);
            } else if (paramBean.getStr("ITEM_CODE").equals(ZW_TYPE.ENC_ZHENGWEN.getCode())) {
                ZwType zwType = ZW_TYPE.ENC_ZHENGWEN;
                GwFileHelper.changeZwType(zwType, paramBean);
            }
        }
    }

}
