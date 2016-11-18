package com.rh.oa.gw.listener;

import com.rh.core.base.Bean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.DateUtils;

/**
 * 公文签发监听
 * @author hdy
 *
 */
public class GwQfMindListener {

    /**查看此服务为公文发文的前缀字符串*/
    private static final String OA_GW_TYPE_FW = "OA_GW_TYPE_FW";
    /**意见类型为 签发 的类型*/
    private static final String MIND_CODE_QF = "QF";
    /**意见类型为 批示 的类型*/
    private static final String MIND_CODE_PS = "PS";
    
    /**操作方法*/
    private static final String NONE_WRITE_ACTIONS = "save,batchSave";
    
    /**
     * 执行后
     * @param act 当前监听服务方法
     * @param paramBean 当前操作传入参数
     * @param result 当前操作传出参数
     */
    public void after(String act, ParamBean paramBean, OutBean result) {
        //如果是不是保存方操作
        if (!NONE_WRITE_ACTIONS.contains(act)) {
            return;
        }
        //以公司发文服务开头的并且意见类型为签发
        Bean fullBean = paramBean.getSaveFullData();
        String servID = fullBean.getStr("SERV_ID");
        if (!servID.startsWith(OA_GW_TYPE_FW)) {
            return;
        }
        
        String mindCode = fullBean.getStr("MIND_CODE");
        if (mindCode.startsWith(MIND_CODE_QF)
                || mindCode.startsWith(MIND_CODE_PS)) {
            Bean fawenBean = new Bean();
            fawenBean.set("GW_SIGN_TIME", DateUtils.getDatetime())
                    .set("GW_SIGN_UNAME", fullBean.getStr("S_UNAME"))
                    .setId(fullBean.getStr("DATA_ID"));
            ServDao.save(fullBean.getStr("SERV_ID"), fawenBean);
        }       

    }
}
