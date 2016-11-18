/*
 * Copyright (c) 2013 Ruaho All rights reserved.
 */
package com.rh.core.comm.cms.serv;

import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.base.TipException;
import com.rh.core.comm.cms.mgr.ChannelMgr;
import com.rh.core.org.UserBean;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Lang;

/**
 * cms site service
 * @author liwei
 * 
 */
public class SiteServ extends CommonServ {

    /** chnl serv id */
    private static final String CHANNEL_SERVICE = ServMgr.SY_COMM_CMS_CHNL;

    private static final String SITE_SERVICE = ServMgr.SY_COMM_CMS_SITE;
    
    /** 文库 */
    private static final String SY_COMM_CMS_BBS = "SY_COMM_CMS_BBS";
    /** BBS */
    private static final String SY_COMM_CMS_WENKU = "SY_COMM_CMS_WENKU";
    /**
     * 注册(开通)站点 注册某群组、部门站点
     * 
     * @param paramBean - 参数bean
     * @return - out bean
     */
    public OutBean register(ParamBean paramBean) {
        OutBean outBean = new OutBean();
        int siteType = paramBean.getInt("SITE_TYPE");
        String servId = paramBean.getStr("SERV_ID");
        String dataId = paramBean.getStr("DATA_ID");
        if (paramBean.isEmpty("SERV_ID")) {
            throw new TipException("SERV_ID can not be null");
        }
        if (paramBean.isEmpty("DATA_ID")) {
            throw new TipException("DATA_ID can not be null");
        }
        if (2 != siteType) {
            throw new TipException("SITE_TYPE fail.");
        }
        // check reduplicate
        // 如果站点已经注册过，我们将返回警告
        int exites = ServDao.count(SITE_SERVICE, paramBean);
        if (exites > 0) {
            outBean.setError("站点已存在!");
            return outBean;
        }

        // get default site
        Bean queryBean = new Bean();
        queryBean.set("SITE_TYPE", 1);
        queryBean.set("SERV_ID", servId);
        Bean dftSite = ServDao.find(SITE_SERVICE, queryBean);
        if (null == dftSite || dftSite.isEmpty()) {
            outBean.setError("无法找到对应默认站点!");
            return outBean;
        }

        // copy of default site
        ParamBean copyOf = new ParamBean(dftSite.copyOf());
        copyOf.setId("");
        copyOf.remove("SITE_ID");
        copyOf.set("SITE_TYPE", 2);
        copyOf.set("SERV_ID", servId);
        copyOf.set("DATA_ID", dataId);

        // save new register site
        copyOf.setServId(SITE_SERVICE);
        return save(copyOf);
    }

    /**
     * 访问权限验证
     * @param param - 参数bean
     * @return - out bean
     */
    public OutBean authorization(ParamBean param) {
        OutBean siteBean = new OutBean(ServDao.find(SITE_SERVICE, param));
        String servId = siteBean.getStr("SERV_ID");
        String dataId = siteBean.getStr("DATA_ID");
        UserBean currentUser = Context.getUserBean();
        if ("SY_ORG_GROUP".equals(servId)) {
            // 群组站点
            List<String> groups = currentUser.getGroupCodeList();
            if (!groups.contains(dataId)) {
                throw new TipException("您没有此群组站点访问权限!");
            }
            
//            Bean groupUserQuery = new Bean();
//            groupUserQuery.set("USER_CODE", currentUser.getCode());
//            List<Bean> groups = ServDao.finds("SY_ORG_GROUP_USER", groupUserQuery);
//            boolean contains = false;
//            for (Bean gu : groups) {
//                if (gu.getStr("GROUP_CODE").equals(dataId)) {
//                    contains = true;
//                }
//            }
//            if (!contains) {
//                throw new TipException("您没有此群组站点访问权限!");
//            }
            

        } else if ("SY_ORG_DEPT".equals(servId)) {
            // 机构站点
            List<String> deptList = currentUser.getDeptCodeList();
            if (!deptList.contains(dataId)) {
                throw new TipException("您没有此机构站点访问权限!");
            }
        } else {

        }
        return siteBean;
    }

    @Override
    protected void beforeSave(ParamBean paramBean) {
        // 忽略修改
        if (!paramBean.getAddFlag()) {
            return;
        }
        if (paramBean.isEmpty("SITE_ID")) {
            String pk = paramBean.getId();
            if (null == pk || 0 == pk.length()) {
                pk = Lang.getUUID();
                paramBean.setId(pk);
            }
            paramBean.set("SITE_ID", pk);
        }
    }

    @Override
    protected void afterSave(ParamBean paramBean, OutBean outBean) {
        // 忽略修改
        if (!paramBean.getAddFlag()) {
            return;
        }

        String siteId = outBean.getStr("SITE_ID");
        String servId = outBean.getStr("SERV_ID");
        int siteType = outBean.getInt("SITE_TYPE");
        if ("SY_ORG_GROUP".equals(servId) && 2 == siteType) {
            // copy默认群组栏目
            Bean queryBean = new Bean();
            queryBean.set("SERV_ID", servId);
            queryBean.set("SITE_TYPE", 1);
            Bean defaultSite = ServDao.find(SITE_SERVICE, queryBean);
            copyChnls(outBean, defaultSite.getId());
        } else if ("SY_ORG_DEPT".equals(servId) && 2 == siteType) {
            // copy默认机构栏目
            Bean queryBean = new Bean();
            queryBean.set("SERV_ID", servId);
            queryBean.set("SITE_TYPE", 1);
            Bean defaultSite = ServDao.find(SITE_SERVICE, queryBean);
            copyChnls(outBean, defaultSite.getId());
        } else if (outBean.getId().equals(siteId)) {
            updateRootChnl(outBean);
        }

    }

    @Override
    protected void afterDelete(ParamBean paramBean, OutBean outBean) {
        String pk = paramBean.getId();
        ChannelMgr.getInstance().delete(new Bean().set("SITE_ID", pk));
        ChannelMgr.getInstance().delete(new Bean().setId(getWenkuChnlPk(pk)));
        ChannelMgr.getInstance().delete(new Bean().setId(getNewsChnlPk(pk)));
        ChannelMgr.getInstance().delete(new Bean().setId(getBBSChnlPk(pk)));
    }

    /**
     * 根据站点复制栏目
     * @param siteBean - site bean
     * @param defaultChnl - 默认站点栏目
     */
    private void copyChnls(Bean siteBean, String defaultChnl) {
        Bean queryBean = new Bean().set("SITE_ID", defaultChnl);
        queryBean.set("CHNL_PID", null);
        List<Bean> list = ServDao.finds(CHANNEL_SERVICE, queryBean);
        if (list.size() > 3) {
            log.warn("1 level channel list size > 3 , interrupt copy.");
            return;
        }
        // TODO 查询出全部，遍历复制
        for (Bean bean : list) {
            Bean copyBean = saveCopyBean(bean, siteBean, "");

            copyChnlsInDepth(bean, copyBean, siteBean);
        }
    }

    /**
     * 递归复制栏目
     * @param srcParent - 源父栏目
     * @param tarParent - 目标父栏目
     * @param siteBean - 站点
     */
    private void copyChnlsInDepth(Bean srcParent, Bean tarParent, Bean siteBean) {
        Bean queryBean = new Bean();
        queryBean.set("CHNL_PID", srcParent.getId());
        List<Bean> items = ServDao.finds(CHANNEL_SERVICE, queryBean);
        for (Bean bean : items) {
            Bean copyBean = saveCopyBean(bean, siteBean, tarParent.getId());

            copyChnlsInDepth(bean, copyBean, siteBean);
        }
    }

    /**
     * 复制栏目
     * @param src - 源栏目
     * @param siteBean - site bean
     * @param pid - 目标pid
     * @return - 复制后的新栏目
     */
    private Bean saveCopyBean(Bean src, Bean siteBean, String pid) {
        ParamBean copyBean = new ParamBean(src.copyOf());
        copyBean.setId("");
        copyBean.set("CHNL_PID", pid);
        copyBean.remove("CHNL_PATH");
        copyBean.remove("CHNL_ID");
        copyBean.set("CHNL_IMAGE", "del");
        copyBean.set("SITE_ID", siteBean.getId());
        copyBean.setServId(CHANNEL_SERVICE);
        OutBean outBean = new ChannelServ().save(copyBean);
        return outBean;
    }

    /**
     * 更新站点的1级根栏目
     * @param outBean - channel bean
     */
    private void updateRootChnl(Bean outBean) {
        Bean channel = new Bean();
        channel.setId(outBean.getId());
        channel.set("CHNL_ID", outBean.getId());
        channel.set("CHNL_NAME", outBean.getStr("SITE_NAME") + "(站点)");
        channel.set("CHNL_LEVEL", 0);
        channel.set("CHNL_SORT", 1);
        channel.set("SERV_ID", outBean.getStr("SERV_ID"));

        // 更新文库站点根栏目
        String wenkuPk = getWenkuChnlPk(outBean.getId());
        int exitis = ServDao.count(CHANNEL_SERVICE, new Bean().setId(wenkuPk));
        if (0 < exitis) {
            channel.set("SERV_ID", SY_COMM_CMS_WENKU);
            channel.setId(wenkuPk);
            channel.set("CHNL_ID", wenkuPk);
            channel.set("CHNL_CODE", wenkuPk);
            channel.set("SITE_ID", outBean.getId());
            channel.set("CHNL_NAME", outBean.getStr("SITE_NAME") + "(文库站点)");
            ChannelMgr.getInstance().update(channel);
        } else {
            channel.set("SERV_ID", SY_COMM_CMS_WENKU);
            channel.setId(wenkuPk);
            channel.set("CHNL_ID", wenkuPk);
            channel.set("CHNL_CODE", wenkuPk);
            channel.set("SITE_ID", outBean.getId());
            channel.set("CHNL_NAME", outBean.getStr("SITE_NAME") + "(文库站点)");
            ChannelMgr.getInstance().create(channel);
        }

        // 更新新闻站点根栏目
        String newsPk = getNewsChnlPk(outBean.getId());
        exitis = ServDao.count(CHANNEL_SERVICE, new Bean().setId(newsPk));
        if (0 < exitis) {
            channel.set("SERV_ID", ServMgr.SY_COMM_NEWS);
            channel.setId(newsPk);
            channel.set("CHNL_ID", newsPk);
            channel.set("CHNL_CODE", newsPk);
            channel.set("SITE_ID", outBean.getId());
            channel.set("CHNL_NAME", outBean.getStr("SITE_NAME") + "(新闻站点)");
            ChannelMgr.getInstance().update(channel);
        } else {
            channel.set("SERV_ID", ServMgr.SY_COMM_NEWS);
            channel.setId(newsPk);
            channel.set("CHNL_ID", newsPk);
            channel.set("CHNL_CODE", newsPk);
            channel.set("SITE_ID", outBean.getId());
            channel.set("CHNL_NAME", outBean.getStr("SITE_NAME") + "(新闻站点)");
            ChannelMgr.getInstance().create(channel);
        }

        // 更新bbs站点根栏目
        String bbsPk = getBBSChnlPk(outBean.getId());
        exitis = ServDao.count(CHANNEL_SERVICE, new Bean().setId(bbsPk));
        if (0 < exitis) {
            channel.set("SERV_ID", SY_COMM_CMS_BBS);
            channel.setId(bbsPk);
            channel.set("CHNL_ID", bbsPk);
            channel.set("CHNL_CODE", bbsPk);
            channel.set("SITE_ID", outBean.getId());
            channel.set("CHNL_NAME", outBean.getStr("SITE_NAME") + "(论坛站点)");
            ChannelMgr.getInstance().update(channel);
        } else {
            channel.set("SERV_ID", SY_COMM_CMS_BBS);
            channel.setId(bbsPk);
            channel.set("CHNL_ID", bbsPk);
            channel.set("CHNL_CODE", bbsPk);
            channel.set("SITE_ID", outBean.getId());
            channel.set("CHNL_NAME", outBean.getStr("SITE_NAME") + "(论坛站点)");
            ChannelMgr.getInstance().create(channel);
        }

    }

    /**
     * 获取文库栏目ID
     * @param siteId - 站点ID
     * @return 文库栏目ID
     */
    private String getWenkuChnlPk(String siteId) {
        return "WENKU_" + siteId;
    }

    /**
     * 获取新闻栏目ID
     * @param siteId - 站点ID
     * @return 新闻栏目ID
     */
    private String getNewsChnlPk(String siteId) {
        return "NEWS_" + siteId;
    }

    /**
     * 获取bbs栏目ID
     * @param siteId - 站点ID
     * @return bbs栏目ID
     */
    private String getBBSChnlPk(String siteId) {
        return "BBS_" + siteId;
    }
}
