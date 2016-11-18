package com.rh.oa.zh.serv;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rh.core.base.Bean;
import com.rh.core.comm.FileMgr;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;
import com.rh.oa.gs.mgr.QsMgr;
import com.rh.oa.gs.serv.QsServ;
import com.rh.oa.gw.GwServ;
import com.rh.oa.gw.util.AuditUtils;

/**
 * 信访转办函
 * @author hai
 * 
 */
public class LetterServ extends GwServ {
    private static Log log = LogFactory.getLog(LetterServ.class);

    @Override
    protected void beforeQuery(ParamBean paramBean) {
        super.beforeQuery(paramBean);
    }

    @Override
    protected void afterByid(ParamBean paramBean, OutBean outBean) {
        super.afterByid(paramBean, outBean);
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
     * 复制报表
     * @param paramBean 【servid+pk】
     * @return 返回
     */
    public OutBean copyReport(ParamBean paramBean) {
        String servId = paramBean.getServId();
        String fileid = paramBean.getStr("FILE_ID");
        Bean fileBean = FileMgr.getFile(fileid);
        if (fileBean == null) {
            throw new RuntimeException("此报表不存在！");
        }
        Bean copyfile = FileMgr.copyFile(fileBean, new Bean().set("SERV_ID", servId)
                .set("FILE_CAT", paramBean.getStr("FILE_CAT")).set("DATA_ID", paramBean.getStr("DATA_ID"))
                .set("DIS_NAME", fileBean.get("DIS_NAME")));
        List<Bean> filelist = new ArrayList<Bean>();
        filelist.add(copyfile);
        return new OutBean().setData(filelist).setCount(filelist.size());
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
