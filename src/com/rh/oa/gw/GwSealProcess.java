package com.rh.oa.gw;

import com.rh.core.base.Bean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;

/**
 * 公文盖章过程相关操作类。公文模块需要使用这些方法才能完成盖章、取消盖章、打印操作。
 * @author yangjy
 * 
 */
public interface GwSealProcess {

    /**
     * 取得需要盖章的文件信息
     * @param gwBean 审批单Bean
     * @param fileBean 被打印文件Bean
     * @param printNum 打印份数
     * @return 返回盖章文件信息
     */
    OutBean getSealFileInfo(Bean gwBean, Bean fileBean, int printNum);

    /**
     * 取得打印文件信息
     * @param gwBean 审批单Bean
     * @param fileBean 被打印文件Bean
     * @return 打印文件信息
     */
    OutBean getPrintFileInfo(Bean gwBean, Bean fileBean);

    /**
     * 设置文件默认打印份数
     * @param gwBean 公文Bean
     * @param zwFileBean 正文Bean
     */
    void setPrintNum(Bean gwBean, Bean zwFileBean);

    /**
     * 追加打印份数
     * @param gwBean 公文Bean
     * @param zwFileBean 正文Bean
     */
    void appendPrintNum(Bean gwBean, Bean zwFileBean);

    /**
     * 取消盖章
     * @param gwBean 审批单
     * @param fileBean 取消盖章文件
     * @return 返回取消盖章文件信息
     */
    OutBean undoSeal(Bean gwBean, Bean fileBean);

    /**
     * 是否已盖章
     * @param paramBean 参数Bean
     * @return 已盖章返回true
     */
    boolean hasSealed(ParamBean paramBean);

    /**
     * 改完章之后回写OA系统。例如：保存盖章文件，或者记录盖章文件与盖章前文件对应关系。
     * @param bean 参数
     */
    void afterSeal(ParamBean bean);

    /**
     * 
     * @param zwFileBean 正文Bean
     * @return 是否允许追加打印份数。
     */
    boolean canAppendPrintNum(Bean zwFileBean);
}
