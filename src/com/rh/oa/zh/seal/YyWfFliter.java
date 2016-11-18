package com.rh.oa.zh.seal;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.wfe.WfAct;
import com.rh.core.wfe.WfFilter;
import com.rh.core.wfe.serv.WfOut;

/**
 * 用印从流程定义的节点上取出的 表单按钮 ， 根据实际情况进行过滤
 * @author wangchen
 * 
 */
public class YyWfFliter implements WfFilter {

	/** 文件办结 */
	public static final String BUTTON_FINISH = "finish";

    /**
     * @param aWfAct 节点实例
     * @param aWfOutBean 工作流输出到前台的数据对象
     */
    public void doButtonFilter(WfAct aWfAct, WfOut aWfOutBean) {
        // 过滤办结按钮
        filterFinishBtn(aWfOutBean);
    }

    /**
     * 过滤办结按钮(当前人所在机构与表单上打印人所在机构不一致则不显示办结按钮)
     * @param aWfOutBean 工作流输出到前台的数据对象
     */
    private void filterFinishBtn(WfOut aWfOutBean) {
        if (aWfOutBean.existBtnBean(BUTTON_FINISH)) {
            Bean outBean = aWfOutBean.getOutBean();
            String currOdeptCode = Context.getUserBean().getODeptCode();
            String yyPrintUserOdept = outBean.getStr("YY_PRINTUSER_ODEPT");
            if (yyPrintUserOdept.isEmpty() || !yyPrintUserOdept.equals(currOdeptCode)) {
                // 移除按钮
                aWfOutBean.removeBtnBean(BUTTON_FINISH);
            }
        }
    }

}
