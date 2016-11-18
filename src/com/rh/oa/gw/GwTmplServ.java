package com.rh.oa.gw;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.UserBean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServDefBean;
import com.rh.core.serv.ServMgr;
import com.rh.core.serv.ServUtils;
import com.rh.core.serv.bean.SqlBean;
import com.rh.core.util.Constant;

/**
 * 处理公文模板相关请求
 * @author yangjy
 * 
 */
public class GwTmplServ extends CommonServ {
    
    @Override
    protected void beforeSave(ParamBean paramBean) {
        //获取文件大类名称
        String tmplTypeName = paramBean.getStr("TMPL_TYPE_NAME");
        //获取服务定义对象
        ServDefBean servDef = ServUtils.getServDef(getTmplCode(paramBean));
        String servPid = servDef.getSrcId();
        //如果change数据中没有此参数
        if (StringUtils.isEmpty(tmplTypeName)) {
            //查看老数据中是否存在
            tmplTypeName = paramBean.getSaveOldData().getStr("TMPL_TYPE_NAME");
            //如过不存在大类名称，则修改
            if (StringUtils.isEmpty(tmplTypeName)) {
              ServDefBean servPDef = ServUtils.getServDef(servPid);
              paramBean.set("TMPL_TYPE_NAME", servPDef.getName());
            }
        }
        //保存大类code
        paramBean.set("TMPL_TYPE_CODE", servPid);
    }
    
    /**
     * 根据服务主键获取对应的模版定义信息
     * @param paramBean 参数信息
     * @return 模版的相关定义信息
     */
    public OutBean gwYearCodes(ParamBean paramBean) {
        String tmplId = paramBean.getStr("tmplId");

        String deptId = paramBean.getStr("ZB_TDEPT");
        if (deptId.length() == 0) {
            UserBean userBean = Context.getUserBean();
            deptId = userBean.getTDeptCode();
        }
        
        OutBean tmplBean = new OutBean();
        tmplBean.set("GW_YEAR_CODES", GwTmplMgr.getYearCodeList(deptId, tmplId));
        return tmplBean.setOk();
    }
    
    /**
     * 取得指定模板的第一个机关代字
     * @param tmplId 模板ID
     * @return 返回机关代字
     */
    public String gwYearCode(String tmplId) {
        ParamBean paramBean = new ParamBean().setServId("OA_GW_CODE_V")
                .setAct("find").set("TMPL_CODE", tmplId).set("DEPT_ID", Context.getUserBean().getTDeptCode());
        Bean result = ServMgr.act(paramBean);
        if (result != null) {
            return result.getStr("");
        }
        return null;
    }

    /**
     * 重置文件模板大类
     * @param paramBean 参数
     * @return 输出结果集
     */
    public OutBean resetTmplType(ParamBean paramBean) {
        //获取当前用户对象
        UserBean user = Context.getUserBean();
        //sql查询对象
        SqlBean sql = new SqlBean();
        //填充查询条件
        sql.and("S_CMPY", user.getCmpyCode()).and("S_FLAG", Constant.YES);
        //获取列表数据
        String servId = paramBean.getServId();
        List<Bean> quertList = ServDao.finds(servId, sql);
        //遍历重置文件大类
        for (Bean b : quertList) {
            //获取模板服务id
            String tmplCode = b.getStr("TMPL_CODE");
            //获取服务定义对象
            ServDefBean servDef = ServUtils.getServDef(tmplCode);
            String servPid = servDef.getSrcId();
            b.set("TMPL_TYPE_CODE", servPid);
            isExistTypeName(b, servPid);
            ServMgr.act(servId, ServMgr.ACT_SAVE, new ParamBean(b));
        }
        return new OutBean().setOk();
    }
    
    /**
     * 是否存在文件大类名称，存在则不修改，不存在则修文件大类名称
     * @param bean 当前模板对象
     * @param servPid 最终大类服务id
     */
    private void isExistTypeName(Bean bean, String servPid) {
        if (bean.isEmpty("TMPL_TYPE_NAME")) {
            ServDefBean servDef = ServUtils.getServDef(servPid);
            bean.set("TMPL_TYPE_NAME", servDef.getName());
        }
    }
    
    /**
     * 获取模板code
     * @param paramBean 参数
     * @return 模版参数
     */
    private String getTmplCode(ParamBean paramBean) {
        String tmplCode = paramBean.getStr("TMPL_CODE");
        if (StringUtils.isEmpty(tmplCode)) {
            return paramBean.getSaveOldData().getStr("TMPL_CODE");
        }
        return tmplCode;
    }
}
