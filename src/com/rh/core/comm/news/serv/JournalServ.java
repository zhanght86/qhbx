package com.rh.core.comm.news.serv;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.rh.core.base.Bean;
import com.rh.core.comm.cms.mgr.TemplateMgr;
import com.rh.core.comm.news.mgr.JournalMgr;
import com.rh.core.comm.news.mgr.NewsMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.freemarker.FreeMarkerUtils;

/**
 * 期刊采集
 * @author chensheng
 * 
 */
public class JournalServ extends CommonServ {

    private static final int SHOW_NUM = 10;
    
    /**
     * 采集期刊
     * @param paramBean 参数Bean
     * @return 查询结果
     */
    public OutBean collect(ParamBean paramBean) {
        OutBean result = new OutBean();

        /*
         * 查找栏目模板
         */
        String chnlId = paramBean.getStr("CHNL_ID");
        if (StringUtils.isBlank(chnlId)) {
            result.setError("栏目不能为空！");
            return result;
        }
        ParamBean chnlParamBean = new ParamBean(JournalMgr.SY_COMM_INFOS_CHNL, ServMgr.ACT_BYID, chnlId);
        OutBean chnlBean = ServMgr.act(chnlParamBean);

        // 取得模板
        String headTmplId = chnlBean.getStr("CHNL_TITLE_TMPL");
        String tmplId = chnlBean.getStr("CHNL_CONTENT_TMPL");

        // 模板信息
        StringBuffer ftlText = new StringBuffer();

        // 头信息
        if (headTmplId.length() > 0) {
            String headTmplFullPath = TemplateMgr.getTmplFilePath(
                    ServDao.find(JournalMgr.SY_COMM_JOURNAL_TMPL, new ParamBean().setId(headTmplId)));
            String headerStr = FreeMarkerUtils.parseText(headTmplFullPath, null);
            ftlText.append(headerStr);
        }

        // 取得所有选中的信息内容
        String newsIdStr = paramBean.getStr(Constant.KEY_ID);
        if (StringUtils.isNotBlank(newsIdStr)) {
            newsIdStr = newsIdStr.replaceAll(",", "','");
            newsIdStr = "'" + newsIdStr + "'";
        }
        ParamBean infosParamBean = new ParamBean()
                .setServId(JournalMgr.SY_COMM_JOURNAL_NEWS)
                .setAct(ServMgr.ACT_QUERY)
                .setQuerySearchWhere(" and NEWS_ID in (" + newsIdStr + ")")
                .setQueryNoPageFlag(true)
                .setSelect("*");
        List<Bean> newsList = ServMgr.act(infosParamBean).getDataList();
        int len = newsList.size();

        if (tmplId.length() > 0) {
            String tmplFullPath = TemplateMgr.getTmplFilePath(
                    ServDao.find(JournalMgr.SY_COMM_JOURNAL_TMPL, new ParamBean().setId(tmplId)));
            for (int index = 0; index < len; index++) {
                Bean info = newsList.get(index);
                ftlText.append(FreeMarkerUtils.parseText(tmplFullPath, info));
            }
        } else { // 如果没有栏目模板则简单拼起来
            for (int index = 0; index < len; index++) {
                Bean info = newsList.get(index);
                ftlText.append(info.getStr("NEWS_BODY") + info.getStr("S_CMPY__NAME"));
            }
        }

        result.setData(ftlText.toString()).setOk("期刊采集成功！");
        return result;
    }

    /**
     * 保存期刊消息关联信息
     * @param paramBean 调用参数
     * @return
     */
    public void saveJournalNews(ParamBean paramBean) {
        String juId = paramBean.getStr(JournalMgr.COL_JU_ID);
        String newsIds = paramBean.getStr(JournalMgr.COL_NEWS_ID);
        if (StringUtils.isNotBlank(newsIds)) {
            List<Bean> journalNewsList = new ArrayList<Bean>();
            String[] newsIdArr = newsIds.split(",");
            int len = newsIdArr.length;
            for (int index = 0; index < len; index++) {
                Bean journalNewsBean = new Bean().set(JournalMgr.COL_JU_ID, juId)
                        .set(JournalMgr.COL_NEWS_ID, newsIdArr[index]);
                journalNewsList.add(journalNewsBean);
            }
            ServMgr.act(JournalMgr.SY_COMM_JOURNAL_NEWS_LINK, ServMgr.ACT_BATCHSAVE,
                    new ParamBean().setBatchSaveDatas(journalNewsList));
        }
    }

    /**
     * 保存之后检测期刊内容有没有变化如果有则生成新的期刊文件
     * @param paramBean 参数 
     * @param outBean 结果
     */
    @Override
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        super.afterSave(paramBean, outBean);
        if (paramBean.contains(JournalMgr.COL_JU_CONTENT)) {
            Bean fullBean = paramBean.getSaveFullData();
            String fileId = fullBean.getStr(JournalMgr.COL_FILE_ID);
            this.genJournalToDoc(paramBean.getStr(JournalMgr.COL_JU_CONTENT), fileId);
        }
    }

    /**
     * 把html生成word文档
     * @param html html字符串
     * @param fileId 文件ID
     */
    public void genJournalToDoc(String html, String fileId) {
        if (StringUtils.isNotBlank(fileId)) {
            // 覆盖文件
        } else {
            // 生成一个新的文件
        }
    }

    /**
     * 期刊批量审核 并记录相关状态
     * @param param 参数
     * @return 返回值
     */
    public OutBean batchCheck(ParamBean param) {
        OutBean out = new OutBean();
        Bean bean = new Bean();
        String str = param.getStr(Constant.KEY_ID);
        String[] strdata = str.split(",");
        List<String> data = new ArrayList<String>();
        Bean countBean = new Bean();
        for (int i = 0; i < strdata.length; i++) {
            String[] pkcontent = param.getStr(strdata[i]).split(",");
            if (!pkcontent[0].equals("2")) {
                bean.set(Constant.KEY_ID, strdata[i]);
                bean.set("JU_STATE", NewsMgr.CHECKING);
                Bean rtbean = ServDao.save(param.getStr("serv"), bean);
                countBean.set("SERV_ID", param.getStr("serv"));
                countBean.set("DATA_ID", strdata[i]);
                countBean.set("CHECKER_ID", pkcontent[3]);
                countBean.set("CK_TYPE", NewsMgr.CHECK_TYPE);
                countBean.set("S_ATIME", pkcontent[1]);
                countBean.set("CK_DATE", pkcontent[2]);
                countBean.set("CK_STATE", NewsMgr.CHECKING);
                countBean.set("NEWS_CHECK", NewsMgr.SIMPLE_CHECK);
                ServDao.save("SY_COMM_NEWS_COUNT", countBean);
                data.add(rtbean.getStr(Constant.KEY_ID));
            } else {
                bean.set(Constant.KEY_ID, strdata[i]);
                bean.set("JU_STATE", NewsMgr.POST);
                Bean rtbean = ServDao.save(param.getStr("serv"), bean);
                countBean.set("SERV_ID", param.getStr("serv"));
                countBean.set("DATA_ID", strdata[i]);
                countBean.set("CHECKER_ID", "");
                countBean.set("CK_TYPE", NewsMgr.CHECK_TYPE_NO);
                countBean.set("S_ATIME", pkcontent[1]);
                countBean.set("CK_DATE", pkcontent[2]);
                countBean.set("CK_STATE", NewsMgr.POST);
                countBean.set("NEWS_CHECK", NewsMgr.NO_CHECK);
                ServDao.save("SY_COMM_NEWS_COUNT", countBean);
                data.add(rtbean.getStr(Constant.KEY_ID));
            }

        }
        if (data.size() > 0 && data.size() == strdata.length) {

            out.set(Constant.RTN_MSG_OK, "提交审核成功");
        } else {
            out.set(Constant.RTN_MSG_ERROR, "提交审核失败");
        }

        return out;

    }

    /**
     * 期刊退回 并记录相关状态
     * @param param 参数
     * @return 返回值
     */
    public OutBean backModify(ParamBean param) {
        OutBean out = new OutBean();
        Bean bean = new Bean();
        String str = param.getStr(Constant.KEY_ID);
        String[] strdata = str.split(",");
        List<String> data = new ArrayList<String>();
        Bean countBean = new Bean();
        for (int i = 0; i < strdata.length; i++) {
            String[] pkcontent = param.getStr(strdata[i]).split(",");
            if (!pkcontent[0].equals("2")) {
                bean.set(Constant.KEY_ID, strdata[i]);
                bean.set("JU_STATE", NewsMgr.BACKMODITY);
                Bean rtbean = ServDao.save(param.getStr("serv"), bean);
                countBean.set("SERV_ID", param.getStr("serv"));
                countBean.set("DATA_ID", strdata[i]);
                countBean.set("CHECKER_ID", pkcontent[3]);
                countBean.set("CK_TYPE", NewsMgr.CHECK_TYPE);
                countBean.set("S_ATIME", pkcontent[1]);
                countBean.set("CK_DATE", pkcontent[2]);
                countBean.set("CK_STATE", NewsMgr.BACKMODITY);
                countBean.set("NEWS_CHECK", NewsMgr.SIMPLE_CHECK);
                ServDao.save("SY_COMM_NEWS_COUNT", countBean);
                data.add(rtbean.getStr(Constant.KEY_ID));
            } else {
                // 流程审核
            }

        }
        if (data.size() > 0 && data.size() == strdata.length) {

            out.set(Constant.RTN_MSG_OK, "退回成功");
        } else {
            out.set(Constant.RTN_MSG_ERROR, "退回失败");
        }

        return out;

    }

    /**
     * 期刊审核 通过 并记录相关状态
     * @param param 参数
     * @return 返回值
     */
    public OutBean checkPass(ParamBean param) {
        OutBean out = new OutBean();
        Bean bean = new Bean();
        String str = param.getStr(Constant.KEY_ID);
        String[] strdata = str.split(",");
        List<String> data = new ArrayList<String>();
        Bean countBean = new Bean();
        for (int i = 0; i < strdata.length; i++) {
            String[] pkcontent = param.getStr(strdata[i]).split(",");
            if (!pkcontent[0].equals("2")) {
                bean.set(Constant.KEY_ID, strdata[i]);
                bean.set("JU_STATE", NewsMgr.CHECKED);
                Bean rtbean = ServDao.save(param.getStr("serv"), bean);
                countBean.set("SERV_ID", param.getStr("serv"));
                countBean.set("DATA_ID", strdata[i]);
                countBean.set("CHECKER_ID", pkcontent[3]);
                countBean.set("CK_TYPE", NewsMgr.CHECK_TYPE);
                countBean.set("S_ATIME", pkcontent[1]);
                countBean.set("CK_DATE", pkcontent[2]);
                countBean.set("CK_STATE", NewsMgr.CHECKED);
                countBean.set("NEWS_CHECK", NewsMgr.SIMPLE_CHECK);
                ServDao.save("SY_COMM_NEWS_COUNT", countBean);
                data.add(rtbean.getStr(Constant.KEY_ID));
            } else {
                // 流程审核
            }

        }
        if (data.size() > 0 && data.size() == strdata.length) {

            out.set(Constant.RTN_MSG_OK, "审核成功");
        } else {
            out.set(Constant.RTN_MSG_ERROR, "审核失败");
        }

        return out;

    }

    /**
     * 期刊审核 不通过 并记录相关状态
     * @param param 参数
     * @return 返回值
     */
    public OutBean checkNoPass(ParamBean param) {
        OutBean out = new OutBean();
        Bean bean = new Bean();
        String str = param.getStr(Constant.KEY_ID);
        String[] strdata = str.split(",");
        List<String> data = new ArrayList<String>();
        Bean countBean = new Bean();
        for (int i = 0; i < strdata.length; i++) {
            String[] pkcontent = param.getStr(strdata[i]).split(",");
            if (!pkcontent[0].equals("2")) {
                bean.set(Constant.KEY_ID, strdata[i]);
                bean.set("JU_STATE", NewsMgr.NOPASS);
                Bean rtbean = ServDao.save(param.getStr("serv"), bean);
                countBean.set("SERV_ID", param.getStr("serv"));
                countBean.set("DATA_ID", strdata[i]);
                countBean.set("CHECKER_ID", pkcontent[3]);
                countBean.set("CK_TYPE", NewsMgr.CHECK_TYPE);
                countBean.set("S_ATIME", pkcontent[1]);
                countBean.set("CK_DATE", pkcontent[2]);
                countBean.set("CK_STATE", NewsMgr.NOPASS);
                countBean.set("NEWS_CHECK", NewsMgr.SIMPLE_CHECK);
                ServDao.save("SY_COMM_NEWS_COUNT", countBean);
                data.add(rtbean.getStr(Constant.KEY_ID));
            } else {
                // 流程审核
            }

        }
        if (data.size() > 0 && data.size() == strdata.length) {

            out.set(Constant.RTN_MSG_OK, "操作成功");
        } else {
            out.set(Constant.RTN_MSG_ERROR, "操作失败");
        }

        return out;

    }
    
    /**
     * 返回最新期刊
     * @param paramBean 参数
     * @return 返回结果
     */
    public OutBean getTopJournal(ParamBean paramBean) {
        // 考虑权限的问题，包括栏目和数据
        paramBean.setShowNum(SHOW_NUM);
        OutBean outBean = ServMgr.act(JournalMgr.SY_COMM_JOURNAL_BASE, "query", paramBean);
        return outBean;
    }
}
