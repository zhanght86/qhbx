package com.rh.bn.serv;

import java.io.File;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.org.mgr.OrgMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.DateUtils;
import com.rh.core.util.freemarker.FreeMarkerUtils;

public class CountUseServ extends CommonServ{
    /**
     * 取得帮助信息的HTML字符串
     * @param paramBean 参数Bean
     * @return 帮助信息的HTML字符串
     */
    public OutBean doHelp(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        
        String fileStr = Context.appStr(Context.APP.SYSPATH) + File.separator + "bn"
                + File.separator + "seal" + File.separator + "ftl" + File.separator + "SEAL_COUNT_USE_help.html";
        
        String html = FreeMarkerUtils.parseText(fileStr, null);
        outBean.set("html", html);
        
        return outBean;
    }
    
    /**
     * 取得印章使用统计的HTML字符串
     * @param paramBean 参数Bean
     * @return 印章使用统计的HTML字符串
     */
    public OutBean doCount(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        
        String sealSql = "";
        String timeSql = "";
        String odept = paramBean.getStr("COUNT_BLG_DEPT");
        ParamBean bean = null;
        //{"0":"只统计本级","2":"向下级联到省分","3":"向下级联到地市","4":"向下级联到县支"}
        int connect = paramBean.getInt("COUNT_CONNECT");
        if (odept.isEmpty()) {
            odept = Context.getUserBean().getODeptCode();
        }
       if (connect != 0 && OrgMgr.getDept(odept).getLevel() >= connect) {
            connect = 0;
        }
        if (connect > 0) {
            /*sealSql += " and DEPT_LEVEL<=" + connect + " and CODE_PATH like '%" + odept + "^%'";*/
        } else {
            sealSql += " and KEEP_ODEPT_CODE='" + odept + "'";
        }
        if (paramBean.isNotEmpty("COUNT_BEGIN_TIME")) {
            timeSql += " and S_ATIME>='" + paramBean.getStr("COUNT_BEGIN_TIME") + "'";
        }
        if (paramBean.isNotEmpty("COUNT_END_TIME")) {
            timeSql += " and S_ATIME<'" + DateUtils.getDateAdded(1, paramBean.getStr("COUNT_END_TIME")) + "'";
        }
        
        //1.取得印章分类字典
        bean = new ParamBean()
            .setSelect("SEAL_ID ID,SEAL_NAME NAME,DEPT_SORT");
        List<Bean> typeList = ServDao.finds("BN_SEAL_INFO_LEAF_V", bean);
        outBean.set("typeList", typeList);
        
        //2.遍历取得分类下印章列表
        for (Bean type : typeList) {
            bean = new ParamBean().setSelect("ID SEAL_ID,SEAL_NAME")
                .setWhere(sealSql + " and SEAL_TYPE1='" + type.getStr("ID")+"'");
            List<Bean> sealList = ServDao.finds("BN_SEAL_BASIC_INFO", bean);
            type.set("sealList", sealList);
            
            /*
             * 3.对每一个印章，以用印类型，方式进行分组：
             * APPLY_TYPE{"SEAL_USE_APPLY_GR":"个人使用","SEAL_USE_APPLY_HT":"合同","SEAL_USE_APPLY_ZH":"综合","FW":"发文"}
             * USE_WAY{"实物","电子"}
             */
            for (Bean seal : sealList) {
                bean = new ParamBean()
                    .setSelect("APPLY_TYPE || '-' || USE_WAY ID,APPLY_TYPE,USE_WAY,count(*) COUNT")
                    .setWhere(timeSql + " and SEAL_ID='" + seal.getStr("SEAL_ID") + "' group by APPLY_TYPE,USE_WAY");
                for (Bean way : ServDao.finds(paramBean.getServId(), bean)) { //把结果列表按ID挂在印章信息下，以在前端直接使用
                    seal.set(way.getStr("ID"), way);
                }
                
                //4.小计：实物，电子
                bean = new ParamBean()
                    .setSelect("USE_WAY,count(*) COUNT")
                    .setWhere(timeSql + " and SEAL_ID='" + seal.getStr("SEAL_ID") + "' group by USE_WAY");
                for (Bean way : ServDao.finds(paramBean.getServId(), bean)) { //把结果列表按ID挂在印章信息下，以在前端直接使用
                    seal.set(way.getStr("USE_WAY"), way);
                }
                
            }
            //5.合计：实物，电子
            bean = new ParamBean()
                .setSelect("USE_WAY,count(*) COUNT")
                .setWhere(sealSql + timeSql + " and SEAL_TYPE1='" + type.getStr("ID") + "' group by USE_WAY");
            for (Bean way : ServDao.finds(paramBean.getServId(), bean)) {
                type.set(way.getStr("USE_WAY"), way);
            }
        }
        
        //6.总计
        Bean total = new Bean();
        outBean.set("total", total);
        
        //6.1 合计
        bean = new ParamBean()
            .setSelect("USE_WAY,count(*) COUNT")
            .setWhere(sealSql + timeSql + " group by USE_WAY");
        for (Bean way : ServDao.finds(paramBean.getServId(), bean)) {
            total.set(way.getStr("USE_WAY"), way);
        }
        
        //6.2 其他
        bean = new ParamBean()
            .setSelect("APPLY_TYPE || '-' || USE_WAY ID,APPLY_TYPE,USE_WAY,count(*) COUNT")
            .setWhere(sealSql + timeSql + " group by APPLY_TYPE,USE_WAY");
        for (Bean way : ServDao.finds(paramBean.getServId(), bean)) {
            total.set(way.getStr("ID"), way);
        }
        String fileStr = Context.appStr(Context.APP.SYSPATH) + File.separator + "bn" + File.separator + "seal"
                + File.separator + "ftl" + File.separator + "SEAL_COUNT_USE.html";
        String html = FreeMarkerUtils.parseText(fileStr, outBean);
        outBean.set("html", html);
        
        
        return outBean;
    }
}
