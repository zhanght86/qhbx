package com.rh.oa.mt;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.comment.CommentServ;

/**
 * 会议通知签收查看
 * @author hdy
 *
 */
public class ConfereeNoticeServ extends CommentServ {
    
    /** 获取用户字典表服务ID*/
    private final String syOrgUser = "SY_ORG_USER";
    
    /** 会议回执单服务ID*/
    private final String oaMtReturnNotice = "OA_MT_RETURN_NOTICE";
    
    @Override
    protected void afterQuery(ParamBean paramBean, OutBean outBean) {
        disposalDataList(outBean.getDataList());
    }
    
    /**
     * 按一定规则处理数据
     * @param list 处置之前数据串
     */
    private void disposalDataList(List<Bean> list) {
        Bean thisObj = null;
        for (Bean b : list) {
            thisObj = linkDic(b.getStr("USER_CODE"));
            b.set("DEPT_CODE", thisObj.getStr("DEPT_CODE"));
            b.set("DEPT_CODE__NAME", thisObj.getStr("DEPT_NAME"));
            thisObj.clear();
            thisObj = getReturnNotice(b);
            if (null == thisObj || thisObj.isEmpty()) {
                b.set("MEMO", "");
            } else {
                b.set("MEMO", thisObj.getStr("MEMO"));
            }
        }
    }
    
    /**
     * 获取相应回执单中的[备注]
     * @param bean 某一条通知记录
     * @return 返回查询到的回执单
     */
    private Bean getReturnNotice(Bean bean) {
        ParamBean query = new ParamBean();
        query.setWhere(" and MEETING_ID = '" + bean.getStr("MEETING_ID") + "' and S_USER = '"
                + bean.getStr("USER_CODE") + "'");
        return ServDao.find(oaMtReturnNotice, query);
    }
    
    /**
     * 部门和用户字典表联动
     * @param userCode 传入参数
     * @return 字典参数
     */
    private Bean linkDic(String userCode) {
        ParamBean query = new ParamBean();
        query.setSelect("DEPT_CODE,DEPT_NAME").setWhere(" AND USER_CODE = '" + userCode + "'");
        return ServDao.find(syOrgUser, query);
    }
}
