package com.rh.bn.fileentry.serv;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.Constant;

/**
 * 百年旧内网文档库服务类
 * @author tanyh 20160114
 *
 */
public class BnFileEntryServ extends CommonServ{

    /**文档库服务**/
    private static final String BN_DT_FILEENTRY_SERV = "BN_DT_DLFILEENTRY";
    /**文档授权服务**/
    private static final String BN_DT_FILEENTRY_ROLE_SERV = "BN_DT_FILEENTRY_ROLE";
    
    @Override
    protected void beforeQuery(ParamBean paramBean) {
        //如果没选择栏目，则默认不显示数据
        if (paramBean.get("_treeWhere") == null) {
            paramBean.set("_searchWhere", " and 1>2");
        }
    }
    
    /**
     * 处理文档授权
     * @param paramBean 参数对象
     * @return outBean 处理结果
     */
    public OutBean doAcl(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        //获取选择的文档列表
        String docIds = paramBean.getStr("docIds");
        String[] docArray = docIds.split(",");
        if (docIds.length() <= 0 || docArray.length <= 0) {
            outBean.setError("未选择授权文档");
            return outBean;
        }
        //获取选择的授权角色列表
        String roleIds = paramBean.getStr("roleIds");
        String[] roleArray = roleIds.split(",");
        if (roleIds.length() <= 0 || roleArray.length <= 0) {
            outBean.setError("未选择授权角色");
            return outBean;
        }
        List<Bean> aclList = new ArrayList<Bean>();
        for (String docId : docArray) {
            //每个文档、每个角色创建一条权限记录
            for (String roleId : roleArray) {
                Bean aclBean = new Bean();
                aclBean.set("FILEENTRY_ID", docId);
                aclBean.set("ROLE_CODE", roleId);
                aclList.add(aclBean);
            }
        }
        if (aclList.size() > 0) {
            //批量保存
            ServDao.creates(BN_DT_FILEENTRY_ROLE_SERV, aclList);
        }
        // 批量更新
        ServDao.updates(BN_DT_FILEENTRY_SERV, (new Bean()).set("VERSION", 100),
                (new Bean()).set(Constant.PARAM_WHERE, " and UUID_  in ('" + docIds.replaceAll(",", "','") + "')"));
        outBean.setOk("授权成功");
        return outBean;
    }
    /**
     * 取消文档授权
     * @param paramBean 参数对象
     * @return outBean 处理结果
     */
    public OutBean unDoAcl(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        //获取选择的文档列表
        String docIds = paramBean.getStr("docIds");
        if (docIds.length() <= 0) {
            outBean.setError("未选择授权文档");
            return outBean;
        }
        // 批量删除
        Bean deleteBean = new Bean();
        deleteBean.set(Constant.PARAM_WHERE, " and FILEENTRY_ID in('" + docIds.replaceAll(",", "','") + "')");
        ServDao.deletes(BN_DT_FILEENTRY_ROLE_SERV, deleteBean);
        // 批量重置为未授权
        ServDao.updates(BN_DT_FILEENTRY_SERV, (new Bean()).set("VERSION", 1),
                (new Bean()).set(Constant.PARAM_WHERE, " and UUID_  in ('" + docIds.replaceAll(",", "','") + "')"));
        outBean.setOk("成功取消授权");
        return outBean;
    }
}
