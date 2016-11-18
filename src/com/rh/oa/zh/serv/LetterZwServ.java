package com.rh.oa.zh.serv;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.FileMgr;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServUtils;
import com.rh.core.serv.dict.DictMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.core.util.Strings;
import com.rh.core.util.freemarker.FreeMarkerUtils;
import com.rh.oa.gs.mgr.QsMgr;
import com.rh.oa.gs.serv.QsServ;
import com.rh.oa.gw.GwServ;
import com.rh.oa.gw.GwTmplMgr;
import com.rh.oa.gw.util.AuditUtils;
import com.rh.oa.gw.util.ZW_TYPE;

/**
 * 信访转办函
 * @author hai
 * 
 */
public class LetterZwServ extends GwServ {

    private static Log log = LogFactory.getLog(LetterZwServ.class);

    @Override
    protected void beforeByid(ParamBean paramBean) {
        super.beforeByid(paramBean);
    }

    /**
     * 解析【OA_ZH_NYCBYW_CB】模版
     * @param paramBean 数据
     */
    private void parseFtl(OutBean paramBean) {
        Bean ftlBean = new Bean();
        // 模版存放路径
        String fileStr = Context.appStr(Context.APP.SYSPATH) + File.separator
                + "oa" + File.separator + "tmpl" + File.separator
                + File.separator;
        fileStr += "OA_ZH_NYCBYW_CB.ftl";
        // 业务类型[CB_YW_TYPE] 和表现形式[CB_BXXS]
        String cmpycode = Context.getCmpy();
        String ywlxDict = "CB_YW_TYPE";
        String bxxsDict = "CB_BXXS";

        Bean ywlxBean = DictMgr.getDict(ywlxDict);
        // 业务类型字典项信息
        List<Bean> ywlxList = DictMgr.getItemList(cmpycode, ywlxBean);

        Bean bxxsBean = DictMgr.getDict(bxxsDict);
        // 表现形式字典项信息
        List<Bean> bxxsList = DictMgr.getItemList(cmpycode, bxxsBean);
        // 服务编码
        String ywlxServ = "OA_ZH_NYCBYW" + "-" + ywlxDict;
        String bxxsServ = "OA_ZH_NYCBYW" + "-" + bxxsDict;
        ftlBean.set("_YWLX_", ywlxList);
        ftlBean.set("_BXXS_", bxxsList);
        ftlBean.set("_ywlxServ_", ywlxServ);
        ftlBean.set("_bxxsServ_", bxxsServ);
        ftlBean.set("CB_YW_TYPE", paramBean.get("CB_YW_TYPE"));
        ftlBean.set("CB_BXXS", paramBean.get("CB_BXXS"));
        // 从字典表中获取表现类型
        String str = FreeMarkerUtils.parseText(fileStr, ftlBean);
        paramBean.set("Ftl_Content", str);
    }

    /**
     * 生成正文
     * @param paramBean 【servid+pk】
     * @return 返回
     */
    public OutBean copyZhengwen(ParamBean paramBean) {
        String servId = paramBean.getServId();
        Bean tmplBean = GwTmplMgr.getTmplByid(servId);
        Bean copyfile = null;
        if (tmplBean.isNotEmpty("TMPL_ZHENGWEN")) { // 存在正文则复制正文文件
            String fileId = Strings.getFirstBySep(tmplBean.getStr("TMPL_ZHENGWEN"), ",");

            Bean fileBean = FileMgr.getFile(fileId);
            if (fileBean == null) {
                throw new RuntimeException("模板文件记录不存在！");
            }
            Bean serv = ServUtils.getServDef(servId);
            copyfile = FileMgr.copyFile(fileBean, new Bean().set("SERV_ID", serv.getStr("SERV_SRC_ID"))
                    .set("FILE_CAT", "ZHENGWEN").set("DATA_ID", paramBean.getId())
                    .set("DIS_NAME", ZW_TYPE.ZHENG_WEN.getName()).set("ITEM_CODE", ZW_TYPE.ZHENG_WEN.getCode()));
        }
        return new OutBean().set("fileBean", copyfile);
    }

    @Override
    protected void afterByid(ParamBean paramBean, OutBean outBean) {
        super.afterByid(paramBean, outBean);
        // byid加模版数据
        parseFtl(outBean);

        // 如果是添加模式生成编号
        if (outBean.getByidAddFlag()) {
            // 取得请示类型
            String servId = outBean.getStr("TMPL_TYPE_CODE");
            // 取得机关代字
            String word = AuditUtils.getOrgWord(servId);
            if (StringUtils.isNotBlank(word)) {
                outBean.set(QsMgr.COL_GW_YEAR_CODE, word);
            } else {
                log.debug("当前部门没有配置请示报告(" + servId + ")的机关代字！");
                outBean.setError("当前部门没有配置请示报告的机关代字！");
            }
            // 年份
            if (StringUtils.isBlank(outBean.getStr(QsMgr.COL_GW_YEAR))) {
                outBean.set(QsMgr.COL_GW_YEAR, DateUtils.getYear());
            }
            // 取得流水号
            if (word != null) {
                ParamBean queryBean = new ParamBean()
                        .set(QsMgr.COL_GW_YEAR_CODE, word)
                        .set(QsMgr.COL_GW_YEAR, outBean.getStr(QsMgr.COL_GW_YEAR))
                        .set(QsMgr.COL_TMPL_CODE, servId);

                outBean.set(QsMgr.COL_GW_YEAR_NUMBER,
                        AuditUtils.getSerial(queryBean, servId, QsMgr.COL_GW_YEAR_NUMBER));
            }
        }
    }

    /**
     * 部门和用户字典表联动
     * @param paramBean 传入参数
     * @return 字典参数
     */
    public OutBean linkDicBean(ParamBean paramBean) {
        // 查询Bean
        Bean query = new Bean();
        String sql = " and DEPT_CODE in (select ODEPT_CODE from SY_ORG_USER_V where USER_CODE in ("
                + paramBean.getStr("USER_CODE") + "))";
        // 注入查询条件
        query.set(Constant.PARAM_SELECT, "DEPT_CODE,DEPT_NAME")
                .set(Constant.PARAM_WHERE, sql);
        // 获取查询结果集
        List<Bean> outBeanList = new ArrayList<Bean>();
        outBeanList = ServDao.finds(paramBean.getStr("DICT_SERV_ID"), query);
        // 去除不必要的值，用于list去重
        for (Bean b : outBeanList) {
            b.set("USER_CODE", "").set("_PK_", "").set("_ROWNUM_", "");
        }
        // 去除list中重复值
        removeDuplicateElemInList(outBeanList);
        // 返回结果集
        return new OutBean().setData(outBeanList);
    }
    /**
     * 去除list中重复的对象
     * @param srcList 数据list
     * @param <T> 类型
     */
    private <T> void removeDuplicateElemInList(List<T> srcList) {
        Set<T> tempSet = new LinkedHashSet<T>(srcList);
        srcList.clear();
        srcList.addAll(tempSet);
    }
    
    /**
     * 校验文件编号是否重复
     * @param dataBean 包含编号数据的Bean
     * @param paramBean 参数Bean
     */
    protected void checkRepeatedGwCode(Bean dataBean, ParamBean paramBean) {
        QsServ.checkRepeatedCode(dataBean, paramBean);
    }
}
