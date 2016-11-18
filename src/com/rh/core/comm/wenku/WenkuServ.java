package com.rh.core.comm.wenku;

import java.util.ArrayList;
import java.util.List;

import com.rh.core.base.Bean;
import com.rh.core.base.Context;
import com.rh.core.comm.integral.IntegralMgr;
import com.rh.core.org.UserBean;
import com.rh.core.org.mgr.UserMgr;
import com.rh.core.serv.CommonServ;
import com.rh.core.serv.OutBean;
import com.rh.core.serv.ParamBean;
import com.rh.core.serv.ServDao;
import com.rh.core.serv.ServMgr;
import com.rh.core.util.Constant;
import com.rh.core.util.DateUtils;

/**
 * 
 * 文库虚拟服务 用于提供文库数据
 * 
 * @author liwei
 * 
 */
public class WenkuServ extends CommonServ {
    /** 文库服务名称 */
    public static final String SY_COMM_WENKU = "SY_COMM_WENKU";

    /**
     * 获取用户文库信息
     * @param paramBean - 参数bean
     * @return outBean - 输出bean
     */
    public OutBean getMyInfo(ParamBean paramBean) {
        log.debug("获取用户文库信息---- start");
        UserBean currentUser = Context.getUserBean();
        ParamBean userQuery = new ParamBean().set("userId", currentUser.getCode());

        // get integral
        int integral = IntegralMgr.getInstance().getUserIntegral(SY_COMM_WENKU, currentUser.getCode());

        // get public doc count
        int publicDoc = getUsersPubDocumentCounter(userQuery).get("PUB_DOC_COUNT", 0);

        // get doclist count
        int doclistCount = getUsersPubDoclistCounter(userQuery).get("DOCLIST_COUNT", 0);

        // get view history
        ParamBean hisQuery = new ParamBean();
        hisQuery.setAct(ServMgr.ACT_QUERY);
        hisQuery.setServId(ServMgr.SY_COMM_WENKU_MYREAD);
        hisQuery.set("S_USER", currentUser.getCode());
        hisQuery.set("SERV_ID", ServMgr.SY_COMM_WENKU_DOCUMENT);
        hisQuery.set("ACT_CODE", "read");
        hisQuery.setShowNum(5);
        hisQuery.setOrder("S_MTIME DESC");
        List<Bean> hisList = ServMgr.act(hisQuery).getDataList();

        // get my upload doc
        ParamBean myDocQuery = new ParamBean(ServMgr.SY_COMM_WENKU_MYDOCUMENT, ServMgr.ACT_QUERY);
        myDocQuery.setQueryPageShowNum(5);
        myDocQuery.setQueryPageOrder("S_MTIME DESC");
        List<Bean> myDocList = ServMgr.act(myDocQuery).getDataList();

        OutBean outBean = new OutBean();
        outBean.set("USER_INTEGRAL", integral);
        outBean.set("PUB_DOC_COUNT", publicDoc);
        outBean.set("DOCLIST_COUNT", doclistCount);
        outBean.set("READ_HIS", hisList);
        outBean.set("MY_DOC_LIST", myDocList);
        
        //增加积分
        String key = "SY_COMM_WENKU_VISIT_" + DateUtils.getDate();
        IntegralMgr.getInstance().handle(currentUser.getCode(), WenkuServ.SY_COMM_WENKU, WenkuServ.SY_COMM_WENKU,
                key, "每日登录", "SY_COMM_WENKU_VISIT");
        
        log.debug("获取用户文库信息---- end");
        return outBean;
    }

    /**
     * 获取指定用户所有文档被下载的次数
     * @param paramBean - 参数bean
     * @return - outbean
     */
    public OutBean getUserDocsSumDownload(ParamBean paramBean) {
        String userId = paramBean.getStr("userId");
        ParamBean queryBean = new ParamBean(ServMgr.SY_COMM_WENKU_DOCUMENT, ServMgr.ACT_FINDS);
        queryBean.setSelect("SUM(DOCUMENT_DOWNLOAD_COUNTER) as DOCUMENT_DOWNLOAD_COUNTER");
        String where = " AND S_USER='" + userId + "' ";
        queryBean.set("S_USER", userId);
        queryBean.setWhere(where);
        return ServMgr.act(queryBean);
    }

    /**
     * 获取用户公共文档数量
     * @param paramBean - 参数bean
     * @return - outbean
     */
    public OutBean getUsersPubDocumentCounter(ParamBean paramBean) {
        String userId = paramBean.getStr("userId");

        // get public doc count
        ParamBean docQuery = new ParamBean();
        docQuery.setServId(ServMgr.SY_COMM_WENKU_DOCUMENT);
        docQuery.setAct(ServMgr.ACT_COUNT);
        docQuery.set("S_USER", userId);
        docQuery.set("DOCUMENT_STATUS", 2);
        int publicDoc = ServMgr.act(docQuery).getCount();
        return new OutBean().set("PUB_DOC_COUNT", publicDoc);
    }

    /**
     * 获取用户公共文辑数量
     * @param paramBean - 参数bean
     * @return - outbean
     */
    public OutBean getUsersPubDoclistCounter(ParamBean paramBean) {
        String userId = paramBean.getStr("userId");

        // get public doc count
        ParamBean doclistQuery = new ParamBean();
        doclistQuery.setServId(ServMgr.SY_COMM_WENKU_DOCLIST);
        doclistQuery.setAct(ServMgr.ACT_COUNT);
        doclistQuery.set("S_USER", userId);
        int doclistCount = ServMgr.act(doclistQuery).getCount();
        return new OutBean().set("DOCLIST_COUNT", doclistCount);
    }

    /**
     * 获取本人可见的栏目树
     * @param param - 参数bean
     * @return 栏目树
     */
    public OutBean getCategoryTree(ParamBean param) {
        UserBean user = Context.getUserBean();
        List<String> groups = user.getGroupCodeList();
        List<String> depts = user.getDeptCodeList();

        StringBuilder sqlBuilder = new StringBuilder();
        // dept site
        sqlBuilder.append(" ( AND SERV_ID='SY_ORG_DEPT AND DATA_ID IN ('");
        for (String dept : depts) {
            sqlBuilder.append("'");
            sqlBuilder.append(dept);
            sqlBuilder.append("',");
        }
        sqlBuilder.append("'-1') ) OR ");

        // group site
        sqlBuilder.append(" ( AND SERV_ID='SY_ORG_GROUP AND DATA_ID IN ('");
        for (String group : groups) {
            sqlBuilder.append("'");
            sqlBuilder.append(group);
            sqlBuilder.append("',");
        }
        sqlBuilder.append("'-1') ) ");
        Bean queryBean = new Bean();
        queryBean.set("_extWhere", sqlBuilder.toString());
        List<Bean> sitesList = ServDao.finds(ServMgr.SY_COMM_CMS_SITE, queryBean);

        StringBuilder chnlBuilder = new StringBuilder();
        chnlBuilder.append(" AND SITE_ID IN (");
        for (Bean bean : sitesList) {
            chnlBuilder.append("'");
            chnlBuilder.append(bean.getId());
            chnlBuilder.append("',");
        }
        chnlBuilder.append("-1 ) ");
        return new OutBean(param);
    }

    /**
     * 获取文档列表
     * @param param 参数Bean
     * @return 文档列表
     */
    public OutBean getDocumentList(ParamBean param) {
        // return new OutBean().setData(list);
        String siteId = param.getStr("siteId");
        String channelId = param.getStr("channelId");
        String userId = param.getStr("userId");
        int page = param.getInt("page");
        int count = param.getInt("count");
        String order = param.getStr("order");

        if (0 == page) {
            page = 1;
        }
        if (null == order || 0 == order.length()) {
            order = "S_CTIME desc";
        }
        String whereBuilder = " AND DOCUMENT_STATUS = '2' ";
        ParamBean query = new ParamBean();

        if (siteId != null && siteId.length() > 0) {
            whereBuilder += " AND SITE_ID='" + siteId + "'";
        } else {
            whereBuilder += " AND SITE_ID='SY_COMM_CMS'";
        }
        if (userId != null && userId.length() > 0) {
            whereBuilder += " and S_USER='" + userId + "'";
        }
        query.setQueryExtWhere(whereBuilder);
        if (channelId != null && channelId.length() > 0) {
            List<Bean> treeWhere = new ArrayList<Bean>();
            treeWhere.add(new Bean().set("DICT_ITEM", "DOCUMENT_CHNL").set("DICT_VALUE", channelId));
            query.set("_treeWhere", treeWhere);
        }
        if (count > 0) {
            query.setQueryPageNowPage(page).setQueryPageShowNum(count);
        }
        if (order != null && order.length() > 0) {
            query.setQueryPageOrder(order);
        }

        query.setServId(ServMgr.SY_COMM_WENKU_DOCUMENT);
        query.setAct(ServMgr.ACT_QUERY);
        OutBean outBean = ServMgr.act(query);
        return outBean;
    }

    /**
     * 根据站点ID和栏目个数取得指定数目的栏目+文档信息
     * @param paramBean 参数Bean
     * @return 返回站点下栏目列表和与之相关的文档信息
     * @deprecated
     */
    public OutBean getChannelDoc(Bean paramBean) {
        paramBean.set(Constant.PARAM_SELECT, "CHNL_NAME,SITE_ID");
        paramBean.set("SERV_ID", "SY_COMM_WENKU");
        paramBean.set("CHNL_LEVEL", 2);
        if (paramBean.isEmpty("_ROWNUM_")) {
            paramBean.set("_ROWNUM_", 3);
        }
        if (paramBean.isEmpty("SITE_ID")) {
            paramBean.set("SITE_ID", "SY_COMM_CMS");
        }

        // 查询指定数目的栏目信息
        List<Bean> channelList = ServDao.finds(ServMgr.SY_COMM_WENKU_CHNL, paramBean);

        // 遍历栏目id，查询栏目下文档信息
        for (Bean b : channelList) {
            Bean docBean = new Bean();
            docBean.set(Constant.PARAM_SELECT, "DOCUMENT_TITLE,DOCUMENT_FILE_SUFFIX,DOCUMENT_FILE_SNAPSHOT");
            docBean.set(Constant.PARAM_ROWNUM, 10);

            /*
             * docBean.set("SITE_ID", paramBean.getStr("SITE_ID")); docBean.set("DOCUMENT_CHNL", b.getStr("CHNL_ID"));
             * docBean.set("DOCUMENT_STATUS", 2);
             */

            docBean.set(Constant.PARAM_WHERE,
                    " and DOCUMENT_STATUS=2 and SITE_ID='" + paramBean.getStr("SITE_ID")
                            + "' and DOCUMENT_CHNL in (select CHNL_ID from SY_COMM_CMS_CHNL where CHNL_PATH like '%"
                            + b.getStr("CHNL_ID") + "%')");

            // if (channelId != null && channelId.length() > 0) {
            // List<Bean> treeWhere = new ArrayList<Bean>();
            // treeWhere.add(new Bean().set("DICT_ITEM", "DOCUMENT_CHNL").set("DICT_VALUE", channelId));
            // param.set("_treeWhere", treeWhere);
            // }

            List<Bean> docList = ServDao.finds(ServMgr.SY_COMM_WENKU_DOCUMENT, docBean);
            b.set("docList", docList);
        }
        return new OutBean().setData(channelList);
    }

    /**
     * 在上传文件之前，判断文件是否重复了，并给出提示
     * @param paramBean 参数Bean
     * @return 是否有重复
     */
    public OutBean doubleUpload(Bean paramBean) {
        OutBean outBean = new OutBean();
        String tempName = paramBean.getStr("fileName");
        String fileName = tempName.substring(0, tempName.lastIndexOf('.'));
        String servId = paramBean.getStr("serv");
        Bean bean = new ParamBean();
        bean.set("DOCUMENT_TITLE", fileName);
        UserBean currentUser = Context.getUserBean();
        bean.set("S_USER", currentUser.getCode());
        List<Bean> listBean = ServDao.finds(servId, bean);
        if (listBean != null && listBean.size() > 0) {
            outBean.set("repeat", true);
        } else {
            outBean.set("repeat", false);
        }
        return outBean;
    }

    /**
     * 获取文辑列表
     * @param paramBean - 参数bean
     * @return - outbean
     */
    public OutBean getWenjiList(ParamBean paramBean) {

        int count = paramBean.getInt("count");
        int page = paramBean.getInt("page");
        String order = paramBean.getStr("order");
        // String siteId = paramBean.getStr("siteId");
        String userId = paramBean.getStr("userId");
        String channelId = paramBean.getStr("channelId");

        ParamBean param = new ParamBean(ServMgr.SY_COMM_WENKU_DOCLIST, ServMgr.ACT_QUERY);
        // set("LIST_STATUS", 2).setOrder("LIST_READ_COUNTER desc");
        param.setQueryPageNowPage(page);
        if (count > 0) {
            param.setQueryPageShowNum(count);
        }

        if (null != order && order.length() > 0) {
            param.setQueryPageOrder(order);
        } else {
            param.setQueryPageOrder("S_MTIME desc");
        }

        StringBuilder where = new StringBuilder();
        // if (siteId != null && siteId.length() > 0) {
        // where.append(" and SITE_ID='" + siteId + "'");
        // }
        if (userId != null && userId.length() > 0) {
            where.append(" and S_USER='" + userId + "'");
        }
        if (where.toString().length() > 0) {
            param.setQueryExtWhere(where.toString());
        }

        if (channelId != null && channelId.length() > 0) {
            List<Bean> treeWhere = new ArrayList<Bean>();
            treeWhere.add(new Bean().set("DICT_ITEM", "LIST_CHNL").set("DICT_VALUE", channelId));
            param.set("_treeWhere", treeWhere);
        }

        OutBean outBean = ServMgr.act(param);
        List<Bean> list = outBean.getDataList();
        for (Bean b : list) {
            if (b.get("LIST_READ_COUNTER", 0) == 0) {
                b.set("LIST_READ_COUNTER", 0);
            }
        }
        return outBean;
    }

    /**
     * 根据文档ID 获取其相关的文辑列表
     * @param paramBean - 参数bean
     * @return - outbean
     */
    public OutBean getReferenceWenji(ParamBean paramBean) {
        String docId = paramBean.getStr("docId");
        int count = paramBean.getInt("count");

        // 从文辑项中查询出10条文档ID等于当前文档的文辑ID
        ParamBean wenjiBean = new ParamBean(ServMgr.SY_COMM_WENKU_DOCLIST_ITEM, ServMgr.ACT_QUERY);
        // wenjiBean.set("DOCUMENT_ID", docId);
        wenjiBean.setQueryExtWhere("and DOCUMENT_ID = '" + docId + "'");

        if (0 < count) {
            wenjiBean.setQueryPageShowNum(count);
        } else {
            wenjiBean.setQueryPageShowNum(5);
        }
        wenjiBean.setQueryPageOrder("LIST_READ_COUNTER desc");
        OutBean outBean = ServMgr.act(wenjiBean);
        return outBean;
    }

    /**
     * 获取用户上传文档
     * @param paramBean - 参数bean
     * @return - outbean
     */
    public OutBean getUserDocument(ParamBean paramBean) {
        String userId = paramBean.getStr("userId");
        int count = paramBean.getInt("count");
        String docId = paramBean.getStr("docId");

        Bean param = new Bean().set("DOCUMENT_STATUS", 2)
                .set(Constant.PARAM_ORDER, "DOCUMENT_READ_COUNTER desc,S_CTIME desc");
        if (userId != null && userId.length() > 0) {
            param.set(Constant.PARAM_WHERE, " and S_USER='" + userId + "'");
        }
        if (count > 0) {
            param.set(Constant.PARAM_ROWNUM, count);
        }
        if (docId != null && docId.length() > 0) {
            param.set(Constant.PARAM_WHERE, param.getStr(Constant.PARAM_WHERE) + " and DOCUMENT_ID!='" + docId + "'");
        }
        List<Bean> list = ServDao.finds(ServMgr.SY_COMM_WENKU_DOCUMENT, param);
        return new OutBean().setData(list);
    }

    /**
     * 根据文辑ID获取文辑项
     * @param paramBean - 参数bean
     * @return - outbean
     */
    public OutBean getDoclistItem(ParamBean paramBean) {
        String listId = paramBean.getStr("listId");

        ParamBean param = new ParamBean(ServMgr.SY_COMM_WENKU_DOCLIST_ITEM, ServMgr.ACT_QUERY);
        // set("LIST_STATUS", 2).setOrder("LIST_READ_COUNTER desc");
        param.setQueryLinkWhere("and LIST_ID = '" + listId + "'");
        // param.set("LIST_ID", chnlId);
        OutBean outBean = ServMgr.act(param);
        return outBean;
    }

    /**
     * 获取文档下载排行
     * @param paramBean - 参数bean
     * @return - out bean
     */
    public OutBean getDownloadTopDocument(ParamBean paramBean) {
        String order = "DOCUMENT_DOWNLOAD_COUNTER desc";
        paramBean.set("order", order);
        return getDocumentList(paramBean);
    }

    /**
     * 获取文档阅读排行
     * @param paramBean - 参数bean
     * @return - out bean
     */
    public OutBean getReadTopDocument(ParamBean paramBean) {
        // String siteId = paramBean.getStr("siteId");
        // int count = paramBean.getInt("count");
        String order = "DOCUMENT_READ_COUNTER desc";
        paramBean.set("order", order);
        return getDocumentList(paramBean);
    }

    /**
     * 获取文档打分排行
     * @param paramBean - 参数bean
     * @return - out bean
     */
    public OutBean getScoreTopDocument(ParamBean paramBean) {
        // String siteId = paramBean.getStr("siteId");
        // int count = paramBean.getInt("count");

        String order = "DOCUMENT_SCORE_COUNTER desc";
        paramBean.set("order", order);
        return getDocumentList(paramBean);
    }

    /**
     * 获取文库公告
     * @param paramBean - 参数bean
     * @return - out bean
     */
    public OutBean getWenkuNotice(ParamBean paramBean) {
        String id = paramBean.getId();

        ParamBean param = new ParamBean(ServMgr.SY_COMM_WENKU_NOTICE, ServMgr.ACT_BYID);
        param.set("SERV_ID", WenkuServ.SY_COMM_WENKU);
        param.setOrder("S_MTIME DESC");
        param.setId(id);
        OutBean outBean = ServMgr.act(param);
        return outBean;
    }

    /**
     * 获取文库公告列表
     * @param paramBean - 参数bean
     * @return - 结果bean
     */
    public OutBean getWenkuNoticeList(ParamBean paramBean) {
        boolean needMore = paramBean.getBoolean("needMore");
        int count = paramBean.getInt("count");

        // TODO query
        ParamBean param = new ParamBean();
        param.set("SERV_ID", WenkuServ.SY_COMM_WENKU);
        param.setShowNum(count);
        param.setOrder("S_MTIME DESC");
        List<Bean> list = ServDao.finds(ServMgr.SY_COMM_WENKU_NOTICE, param);
        // OutBean outbean=ServMgr.act(ServMgr.SY_COMM_WENKU_NOTICE, ServMgr.ACT_QUERY, param);
        // List<Bean> list=outbean.getDataList();
        if (needMore) { // 用于公告阅读 取出用户头像 用户所属部门
            // 获取用户信息
            for (Bean b : list) {
                UserBean user = UserMgr.getUser(b.getStr("S_USER"));
                b.set("DEPT_NAME", user.get("DEPT_NAME"));
                b.set("USER_IMG", user.get("USER_IMG"));
            }
        }
        return new OutBean().setData(list);
    }

    /**
     * 获取文档总量
     * @param paramBean - 参数bean
     * @return - outbean
     */
    public OutBean getTotal(ParamBean paramBean) {
        String siteId = paramBean.getStr("siteId");

        Bean queryBean = new Bean();
        if (siteId != null && siteId.length() > 0) {
            queryBean.set("SITE_ID", siteId);
        }
        int counter = ServDao.count(ServMgr.SY_COMM_WENKU_DOCUMENT, queryBean);
        return new OutBean().setCount(counter);
    }

}
